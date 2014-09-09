package thesignal;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.prefs.Preferences;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.TreeMultimap;

import thesignal.utils.Pair;
import thesignal.utils.Util;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.futures.FutureDiscover;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

public class ExampleSimple {
	final static String nodeName = "/the/signal";
	
	public final static Preferences prefs = Preferences.userRoot().node(nodeName);
	
	final private Peer tomP2PPeer;
	final private Number160 ownLocation;
	private String currentPeer;

	/**
	 * Returns a pseudo-random number between 0 and Number160.MAX:VALUE,
	 * inclusive.
	 * 
	 * @return random Number160.
	 * @see java.util.Random#nextBytes()
	 */
	public static Number160 randNumber160() {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random(System.currentTimeMillis());

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		byte[] randBytes = new byte[20];
		rand.nextBytes(randBytes);

		return new Number160(randBytes);
	}

	/**
	 * Returns a pseudo-random number between min and max, inclusive. The
	 * difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 * 
	 * @param min
	 *            Minimum value
	 * @param max
	 *            Maximum value. Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public static int randInt(int min, int max) {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random(System.currentTimeMillis());

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	public ExampleSimple(Number160 peerHash) throws Exception {
		ownLocation = peerHash;
		// TODO: This was just a quick hack to be able to start the ExampleSimple multiple times on the same machine (apparently that can't be done with the same port)
		tomP2PPeer = new PeerMaker(peerHash).setPorts(4000 + Math.round(new Random(System.currentTimeMillis()).nextFloat() * 200.f))
				.makeAndListen();
		boolean success = false;
		do {
			FutureBootstrap fb = tomP2PPeer
					.bootstrap()
					.setInetAddress(InetAddress.getByName("user.nullteilerfrei.de"))
					.setPorts(4001).start();
//			FutureBootstrap fb = tomP2P
//			.bootstrap()
//			.setInetAddress(InetAddress.getByName("tsp.no-ip.org"))
//			.setPorts(4242).start();
			fb.awaitUninterruptibly();
			success = fb.isSuccess();
			if(fb.getBootstrapTo() != null)
			{
				FutureDiscover fc = tomP2PPeer.discover()
				.setPeerAddress(fb.getBootstrapTo().iterator().next())
				.start();
				fc.awaitUninterruptibly();
			}
		} while (!success);
	}
	
	public static class TSMessage implements Serializable
	{
		private static final long serialVersionUID = -5115111712520066065L;
		public long createdDateTime; // epoch milliseconds
		public String message;
	}
	
	private static class TSPeer
	{
		static final int putCode = 0;
		static final int getCode = 1;
		
		final String name;
		final Number160 peerHash;
		final private TreeMultimap<Long, Pair<Integer, Number160>> dataDates = TreeMultimap.create();
		final private HashMap<Number160, Data> receivedData = new HashMap<Number160, Data>();
		final private HashMap<Number160, Data> putData = new HashMap<Number160, Data>();
		Number160 nextPutContentKey = new Number160(0);
		
		TSPeer(String name)
		{
			this.name = name;
			peerHash = Number160.createHash(name);
		}
		
		void addNewReceivedData(Map<Number160, Data> newData)
		{
			if(newData != null)
			{
				for(Map.Entry<Number160, Data> entry : newData.entrySet())
				{
					dataDates.put(entry.getValue().getCreated(), new Pair<Integer, Number160>(getCode, entry.getKey()));
				}
				receivedData.putAll(newData);
			}
		}
		
		void addNewPutData(Map<Number160, Data> newData)
		{
			if(newData != null)
			{
				for(Map.Entry<Number160, Data> entry : newData.entrySet())
				{
					dataDates.put(entry.getValue().getCreated(), new Pair<Integer, Number160>(putCode, entry.getKey()));
				}
				putData.putAll(newData);
			}
		}
		
		Map<Number160, Data> getReceivedData()
		{
			return Collections.unmodifiableMap(receivedData); 
		}
		
		Map<Number160, Data> getPutData()
		{
			return Collections.unmodifiableMap(putData); 
		}
		
		ImmutableMultimap<Long, Pair<Integer, Number160>> getDataDates()
		{
			return ImmutableMultimap.copyOf(dataDates);
		}
	}
	
	public static void main(final String[] args) throws NumberFormatException,
			Exception {
//		// Test code for Util.less...
//		Random rand = new Random(System.currentTimeMillis());
//		for(int i = 0; i < 50; ++i)
//		{
//			int left = rand.nextInt(Integer.MAX_VALUE);
//			int right = rand.nextInt(Integer.MAX_VALUE);
//			boolean test = Util.less(new Number160(left), new Number160(right));
//			
//			System.out.println(left + (test?"<":">"));
//			System.out.println(right);
//		}

		final Number160 ownLocation = Number160.createHash(args[0]);
		final ExampleSimple dns = new ExampleSimple(ownLocation);

		final String prefKeyKnownPeers = "known_peers";
		Preferences prefKnownPeers = prefs.node(prefKeyKnownPeers);
		
		final String prefKeyNextPutContentKeys = "next_put_content_keys";
		final Preferences prefNextPutContentKeys = prefs.node(prefKeyNextPutContentKeys);

		for(int i = 1; i < args.length; ++i)
		{
			if(prefKnownPeers.get(args[i], "").isEmpty())
			{
				prefKnownPeers.put(args[i], Number160.createHash(args[i]).toString());
			}
		}
		prefKnownPeers.flush();
		
		final HashMap<String, TSPeer> knownPeers = new HashMap<String, TSPeer>(prefKnownPeers.keys().length);
		for(String peerName : prefKnownPeers.keys())
		{
			TSPeer peer = new TSPeer(peerName);
			
			peer.nextPutContentKey = dns.getNextContentKey(peer.peerHash, ownLocation);
			
			peer.nextPutContentKey = new Number160(prefNextPutContentKeys.get(peerName, peer.nextPutContentKey.toString()));
			
			System.out.println("Next put contentKey for peer " + peer + " is " + peer.nextPutContentKey.toString());
			
			peer.addNewPutData(dns.getPutData(peer));
			
			knownPeers.put(peerName, peer);
		}
		
		final Display display = new Display();
		final Shell shell = new Shell(display);

		// the layout manager handle the layout
		// of the widgets in the container
		FormLayout shellLayout = new FormLayout();
		shell.setLayout(shellLayout);

		final ScrolledComposite scroll = new ScrolledComposite(shell, SWT.V_SCROLL
				| SWT.VERTICAL);
		final Composite scrollContainer = new Composite(scroll, SWT.NONE);
		final Composite inputContainer = new Composite(shell, SWT.NONE);
		final Table groupList = new Table(shell, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);

		dns.currentPeer = args[1];

		RowLayout scrollLayout = new RowLayout(SWT.VERTICAL);
//		scrollLayout.justify = true;
		
		scrollContainer.setLayout(scrollLayout);
		scrollContainer.setLayoutData(new RowData(SWT.MAX, SWT.FILL));
		
		scroll.setContent(scrollContainer);
		FormData scrollLayoutData = new FormData();
		scrollLayoutData.top = new FormAttachment(0,0);
		scrollLayoutData.left = new FormAttachment(0,0);
		scrollLayoutData.right = new FormAttachment(100, -180);
		scrollLayoutData.bottom = new FormAttachment(inputContainer, 0);
		scroll.setLayoutData(scrollLayoutData);
		
		scrollContainer.setBackground(display.getSystemColor(SWT.COLOR_BLACK));

		scrollContainer.setSize(scrollContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		scroll.setExpandHorizontal(true);
		scroll.setExpandVertical(true);
		scroll.setMinSize(scrollContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		inputContainer.setLayout(new FormLayout());
		
		FormData groupListLayoutData = new FormData();
		groupListLayoutData.top = new FormAttachment(0,0);
		groupListLayoutData.left = new FormAttachment(scroll,0);
		groupListLayoutData.right = new FormAttachment(100, 0);
		groupListLayoutData.bottom = new FormAttachment(inputContainer, 0);
		groupList.setLayoutData(groupListLayoutData);
		groupList.setBackground(display.getSystemColor(SWT.TRANSPARENT));
		groupList.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				for(int i = 0 ; i < groupList.getItemCount(); ++i)
				{
					TableItem item = groupList.getItem(i);
					if(item.equals(e.item))
					{
						dns.currentPeer = item.getText();
//		                item.setForeground(display.getSystemColor(SWT.COLOR_RED));
		                item.setBackground(display.getSystemColor(SWT.COLOR_GRAY));
					}
					else
					{
						item.setForeground(display.getSystemColor(SWT.COLOR_DARK_GREEN));
						item.setBackground(display.getSystemColor(SWT.TRANSPARENT));
					}
				}

				for(Control control : scrollContainer.getChildren())
				{
					control.dispose();
				}
				
				ImmutableMultimap<Long, Pair<Integer, Number160>> dataDates = knownPeers.get(dns.currentPeer).getDataDates();
				Map<Number160, Data> receivedData = knownPeers.get(dns.currentPeer).getReceivedData();
				Map<Number160, Data> putData = knownPeers.get(dns.currentPeer).getPutData();
				for(Map.Entry<Long, Pair<Integer, Number160>> entry : dataDates.entries())
				{
					int dataCode = entry.getValue().first;
					Number160 contentKey = entry.getValue().second;
					Label label = new Label(scrollContainer, SWT.NONE);
					label.setBackground(display.getSystemColor(SWT.TRANSPARENT));
					label.setForeground(display.getSystemColor(SWT.COLOR_DARK_GREEN));
					Data data = null;
					String labelText = "";
					if(dataCode == TSPeer.getCode) {
						data = receivedData.get(contentKey);
						labelText = dns.generateReceivedMessageString(knownPeers.get(dns.currentPeer), data);
					} else if(dataCode == TSPeer.putCode) {
						data = putData.get(contentKey);
						labelText = dns.generatePutMessageString(args[0], data);
					}
					label.setText(labelText);
					label.pack();
				}
				
				scrollContainer.setSize(scrollContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				scroll.setMinSize(scrollContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));

				scroll.setOrigin(0, scrollContainer.getSize().y);
				
				shell.layout();
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
	            TableItem[] selItems = groupList.getSelection();
	            
				System.out.println(selItems.toString() + " selected");
			}
		});

		FormData inputLayoutData = new FormData();
		inputLayoutData.left = new FormAttachment(0,0);
		inputLayoutData.right = new FormAttachment(100, 0);
		inputLayoutData.bottom = new FormAttachment(100, 0);
		inputContainer.setLayoutData(inputLayoutData);

		final Text inputTextField = new Text(inputContainer, SWT.MULTI | SWT.WRAP | SWT.BORDER);
		inputTextField.setBackground(display.getSystemColor(SWT.TRANSPARENT));
		inputTextField.setForeground(display.getSystemColor(SWT.COLOR_DARK_GREEN));
		
		inputTextField.addListener(SWT.KeyDown, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				if(event.keyCode == SWT.CR || event.keyCode == SWT.KEYPAD_CR)
				{
					System.out.println("Enter catched");
					
					String input = inputTextField.getText();
					// normalize line endings to \n, because \r is not recognized as "end of line"
					input = input.replace ("/\\s*\\R/g", "\n");
					// remove leading and trailing whitespace
					input = input.replace ("/^\\s*|[\\t ]+$/gm", "");
					
					input = input.trim();
					
					if(!input.isEmpty())
					{
						String recipient = "no recipient";
						
						Label label = new Label(scrollContainer, SWT.NONE);
						label.setBackground(display.getSystemColor(SWT.TRANSPARENT));
						label.setForeground(display.getSystemColor(SWT.COLOR_DARK_GREEN));

						FutureDHT putDHT = null;
						if(groupList.getSelection().length == 1)
						{
							recipient = groupList.getSelection()[0].getText();
							TSPeer peer = knownPeers.get(recipient);
							try {
								putDHT = dns.store(peer.peerHash, ownLocation, peer.nextPutContentKey, input);
								if(putDHT.isSuccess()) {
									System.out.println("Put " + input + " at content key " + peer.nextPutContentKey.toString() + " for peer " + recipient);
									HashMap<Number160, Data> map = new HashMap<Number160, Data>();
									map.put(peer.nextPutContentKey, new Data(input));
									peer.addNewPutData(map);
									peer.nextPutContentKey = Util.inc(peer.nextPutContentKey);
									prefNextPutContentKeys.put(recipient, peer.nextPutContentKey.toString());
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						
						if(putDHT == null) {
							label.setText("Put of \"" + input + "\" to " + recipient + " was not successful");
						} else {
							label.setText(SimpleDateFormat.getDateTimeInstance().format(new Date()) + " - " + args[0] + ": " + input);
						}

						label.pack();
					}

					inputTextField.setText("");

					scrollContainer.setSize(scrollContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
					scroll.setMinSize(scrollContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
					
					scroll.setOrigin(0, scrollContainer.getSize().y);
				}
			}
		});

		int peerIndex = 0;
		int startPeerIndex = 0;
		for(String peer : knownPeers.keySet())
		{
			if(!peer.equals(args[0]))
			{
				TableItem tableItem = new TableItem(groupList, SWT.NONE);
				tableItem.setText(peer);
				tableItem.setForeground(display.getSystemColor(SWT.COLOR_DARK_GREEN));
				if(args.length > 1 && peer.equals(args[1]))
				{
					startPeerIndex = peerIndex;
				}
				peerIndex++;
			}
		}
		
		groupList.select(startPeerIndex);
		
		Button button = new Button(inputContainer, SWT.PUSH);
		button.setText("send");

		FormData inputTextFieldLayoutData = new FormData();
		inputTextFieldLayoutData.left = new FormAttachment(0,0);
		inputTextFieldLayoutData.right = new FormAttachment(button, 0);
		inputTextFieldLayoutData.top = new FormAttachment(0, 0);
		inputTextFieldLayoutData.bottom = new FormAttachment(100, 0);
		inputTextField.setLayoutData(inputTextFieldLayoutData);

		FormData buttonLayoutData = new FormData();
		buttonLayoutData.right = new FormAttachment(100, 0);
		buttonLayoutData.top = new FormAttachment(0, 0);
		buttonLayoutData.bottom = new FormAttachment(100, 0);
		button.setLayoutData(buttonLayoutData);

		// register listener for the selection event
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Called!");
				
				Label label = new Label(scrollContainer, SWT.NONE);
				label.setBackground(display.getSystemColor(SWT.TRANSPARENT));
				label.setForeground(display.getSystemColor(SWT.COLOR_DARK_GREEN));;
				label.setText("Called");

				label.pack();
				
				scrollContainer.setSize(scrollContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				scroll.setMinSize(scrollContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
				
				scroll.setOrigin(0, scrollContainer.getSize().y);
				
				shell.layout();
			}
		});

		shell.open();
//
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				for (final Map.Entry<String, TSPeer> knownPeer : knownPeers.entrySet()) {
					Map<Number160, Data> newData = null;
					TSPeer peer = knownPeer.getValue();
					if(peer.peerHash.equals(ownLocation))
					{
						continue;
					}
					System.out.println("Polling for peer " + knownPeer.getKey());
					newData = dns.getNewData(peer);
					if (newData != null) {
						System.out.println(newData.size() + " new entries found...");
						if(peer.name.equals(dns.currentPeer))
						{
							for(Map.Entry<Number160, Data> entry : newData.entrySet())
							{
								final String newLabelText = dns.generateReceivedMessageString(peer, entry.getValue());
										
								display.syncExec(new Runnable() {
									
									@Override
									public void run() {
										Label label = new Label(scrollContainer, SWT.NONE);
										label.setBackground(display.getSystemColor(SWT.TRANSPARENT));
										label.setForeground(display.getSystemColor(SWT.COLOR_DARK_GREEN));
		
										label.setText(newLabelText);
										
										label.pack();
		
										scrollContainer.setSize(scrollContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
										scroll.setMinSize(scrollContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
										
										scroll.setOrigin(0, scrollContainer.getSize().y);
									}
								});
							}
						}
						peer.addNewReceivedData(newData);
					}
				}
			}
		}, 0, 1000);

		// run the event loop as long as the window is open
		while (!shell.isDisposed()) {
			// read the next OS event queue and transfer it to a SWT event
			if (!display.readAndDispatch()) {
				// if there are currently no other OS event to process
				// sleep until the next OS event is available
				display.sleep();
			}
		}

		timer.cancel();
		// disposes all associated windows and their components
		display.dispose();
		
		prefNextPutContentKeys.flush();
		
		System.out.println("Finished everything and flushed the preferences!");
		
		System.exit(0);
	}

	private Data get(Number160 location, Number160 domain,
			Number160 contentKey) throws ClassNotFoundException, IOException {
		FutureDHT futureDHT = tomP2PPeer.get(location).setDomainKey(domain)
				.setContentKey(contentKey).start();
		futureDHT.awaitUninterruptibly();
		if (futureDHT.isSuccess()) {
			return futureDHT.getData();
		}
		return null;
	}

	private FutureDHT store(Number160 location, Number160 domain,
			Number160 contentKey, String value) throws IOException {
		FutureDHT fut = tomP2PPeer.put(location).setData(contentKey, new Data(value))
				.setDomainKey(domain).start().awaitUninterruptibly();
		return fut;
	}

	private Map<Number160, Data> getNewData(TSPeer peer)
	{
		FutureDHT futureDHT = tomP2PPeer.get(ownLocation).setDomainKey(peer.peerHash).setAll(true).start();
		futureDHT.awaitUninterruptibly();
		Map<Number160, Data> newData = null;
		if (futureDHT.isSuccess()) {
			newData = futureDHT.getDataMap();
			for(Number160 contentKey : peer.receivedData.keySet())
			{
				newData.remove(contentKey);
			}
		}
		return newData;
	}
	
	private Map<Number160, Data> getPutData(TSPeer peer)
	{
		FutureDHT futureDHT = tomP2PPeer.get(peer.peerHash).setDomainKey(ownLocation).setAll(true).start();
		futureDHT.awaitUninterruptibly();
		Map<Number160, Data> newData = null;
		if (futureDHT.isSuccess()) {
			newData = futureDHT.getDataMap();
			for(Number160 contentKey : peer.putData.keySet())
			{
				newData.remove(contentKey);
			}
		}
		return newData;
	}
	
	private Number160 getNextContentKey(Number160 location, Number160 domain) {
		System.out.println("Looking for the highest contentKey in location " + location.toString() + " and domain " + domain.toString());
		FutureDHT futureDHT = tomP2PPeer.get(location).setDomainKey(domain).setAll(true).start();
		futureDHT.awaitUninterruptibly();
		Number160 retVal = new Number160(0);
		if (futureDHT.isSuccess()) {
			Set<Number160> contentKeys = futureDHT.getDataMap().keySet();
			for(Number160 contentKey : contentKeys)
			{
				System.out.println("Found contentKey " + contentKey.toString());
				if(!Util.less(contentKey, retVal))
				{
					retVal = Util.inc(contentKey);
					System.out.println("Setting the new next contentKey to " + retVal.toString());
				}
			}
		}
		return retVal;
	}
	
	public String generateReceivedMessageString(TSPeer peer, Data data)
	{
		try {
			return Util.getLocaleFormattedCreationDateTimeString(data) + " - " + peer.name + ": " + data.getObject().toString();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public String generatePutMessageString(String ownName, Data data)
	{
		try {
			return Util.getLocaleFormattedCreationDateTimeString(data) + " - " + ownName + ": " + data.getObject().toString();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
}