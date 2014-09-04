package thesignal;

import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import thesignal.utils.Pair;
import thesignal.utils.Util;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

public class ExampleSimple {
	final static String nodeName = "/the/signal";
	
	public final static Preferences prefs = Preferences.userRoot().node(nodeName);
	
	final private Peer peer;
	private Number160 getContentKey = new Number160(0);
	private Number160 putContentKey = new Number160(0);

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
		peer = new PeerMaker(peerHash).setPorts(4000 + Math.round(new Random(System.currentTimeMillis()).nextFloat() * 200.f))
				.makeAndListen();
		FutureBootstrap fb = peer
				.bootstrap()
				.setInetAddress(InetAddress.getByName("user.nullteilerfrei.de"))
				.setPorts(4001).start();
		fb.awaitUninterruptibly();
		if (fb.getBootstrapTo() != null) {
			peer.discover()
					.setPeerAddress(fb.getBootstrapTo().iterator().next())
					.start().awaitUninterruptibly();
		}
	}
	
	public static void main(String[] args) throws NumberFormatException,
			Exception {
		final Number160 ownLocation = Number160.createHash(args[0]);
		final ExampleSimple dns = new ExampleSimple(ownLocation);

		final String prefKeyKnownPeers = "known_peers";
		Preferences prefKnownPeers = prefs.node(prefKeyKnownPeers);
		
		for(int i = 1; i < args.length; ++i)
		{
			if(prefKnownPeers.get(args[i], "").isEmpty())
			{
				prefKnownPeers.put(args[i], Number160.createHash(args[i]).toString());
			}
		}
		prefKnownPeers.flush();
		
		final HashMap<String, Pair<Number160, Pair<Number160, Number160>>> knownPeers = new HashMap<String, Pair<Number160, Pair<Number160, Number160>>>(prefKnownPeers.keys().length);
		for(String peer : prefKnownPeers.keys())
		{
			knownPeers.put(peer, new Pair<Number160, Pair<Number160, Number160>>(new Number160(prefKnownPeers.get(peer, "")), new Pair(new Number160(0), new Number160(0))));
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
//		                item.setForeground(display.getSystemColor(SWT.COLOR_RED));
		                item.setBackground(display.getSystemColor(SWT.COLOR_GRAY));
					}
					else
					{
						item.setForeground(display.getSystemColor(SWT.COLOR_DARK_GREEN));
						item.setBackground(display.getSystemColor(SWT.TRANSPARENT));
					}
				}
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

						if(groupList.getSelection().length == 1)
						{
							recipient = groupList.getSelection()[0].getText();
							Pair<Number160, Pair<Number160, Number160>> pair = knownPeers.get(recipient);
							try {
								if (dns.store(pair.first, ownLocation, pair.second.second, input)) {
									pair.second.second = Util.inc(pair.second.second);
								}
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						
						label.setText("To " + recipient + ": " + input);

						label.pack();
					}

					inputTextField.setText("");

					scrollContainer.setSize(scrollContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
					scroll.setMinSize(scrollContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
					
					scroll.setOrigin(0, scrollContainer.getSize().y);
				}
			}
		});
		
		for(String peer : knownPeers.keySet())
		{
			if(!peer.equals(args[0]))
			{
				TableItem tableItem = new TableItem(groupList, SWT.NONE);
				tableItem.setText(peer);
				tableItem.setForeground(display.getSystemColor(SWT.COLOR_DARK_GREEN));
			}
		}
		
		groupList.select(0);
		
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
				for (final Map.Entry<String, Pair<Number160, Pair<Number160, Number160>>> knownPeer : knownPeers
						.entrySet()) {
					String value = null;
					Pair<Number160, Pair<Number160, Number160>> pair = knownPeer.getValue();
					do {
						
						try {
							value = dns.get(ownLocation, pair.first,
									pair.second.first);
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (value != null) {
							System.out.println(value);
							final String newLabelText = knownPeer.getKey() + ": " + value;
							
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

							pair.second.first = Util.inc(pair.second.first);
						}
					} while (value != null);
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

		// disposes all associated windows and their components
		display.dispose();
	}

	private String get(Number160 location, Number160 domain,
			Number160 contentKey) throws ClassNotFoundException, IOException {
		FutureDHT futureDHT = peer.get(location).setDomainKey(domain)
				.setContentKey(contentKey).start();
		futureDHT.awaitUninterruptibly();
		if (futureDHT.isSuccess()) {
			return futureDHT.getData().getObject().toString();
		}
		return null;
	}

	private boolean store(Number160 location, Number160 domain,
			Number160 contentKey, String value) throws IOException {
		FutureDHT fut = peer.put(location).setData(contentKey, new Data(value))
				.setDomainKey(domain).start().awaitUninterruptibly();
		return fut.isSuccess();
	}
}