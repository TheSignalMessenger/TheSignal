package thesignal;

import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
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
import com.google.common.collect.Ordering;
import com.google.common.collect.TreeMultimap;

import thesignal.entity.DHTMessage;
import thesignal.entity.TSPeer;
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
	final private String ownName;
	final private Number160 ownLocation;
	private String currentPeer;

	private Display display = new Display();
	private Shell shell = new Shell(display);

	private ScrolledComposite scroll = new ScrolledComposite(shell,
			SWT.V_SCROLL | SWT.VERTICAL);
	private Composite scrollContainer = new Composite(scroll, SWT.NONE);
	private Composite inputContainer = new Composite(shell, SWT.NONE);
	private Table groupList = new Table(shell, SWT.SINGLE | SWT.BORDER
			| SWT.FULL_SELECTION);
	private Text inputTextField = new Text(inputContainer, SWT.MULTI | SWT.WRAP
			| SWT.BORDER);

	private String prefKeyKnownPeers = "known_peers";
	private Preferences prefKnownPeers = prefs.node(prefKeyKnownPeers);

	private String prefKeyNextPutContentKeys = "next_put_content_keys";
	private Preferences prefNextPutContentKeys = prefs
			.node(prefKeyNextPutContentKeys);

	private HashMap<String, TSPeer> knownPeers;

	private TreeMultimap<Long, Label> dateTimeLabels = TreeMultimap.create(
			Ordering.natural(), Ordering.arbitrary());

	public ExampleSimple(String name, Number160 peerHash) throws Exception {
		ownName = name;
		ownLocation = peerHash;
		// TODO: This was just a quick hack to be able to start the
		// ExampleSimple multiple times on the same machine (apparently that
		// can't be done with the same port)
		tomP2PPeer = new PeerMaker(peerHash).setPorts(
				4000 + Math.round(new Random(System.currentTimeMillis())
						.nextFloat() * 200.f)).makeAndListen();
		boolean success = false;
		do {
//			FutureBootstrap fb = tomP2PPeer
//					.bootstrap()
//					.setInetAddress(
//							InetAddress.getByName("user.nullteilerfrei.de"))
//					.setPorts(4242).start();
			FutureBootstrap fb = tomP2PPeer
			.bootstrap()
			.setInetAddress(InetAddress.getByName("tsp.no-ip.org"))
			.setPorts(4242).start();
			fb.awaitUninterruptibly();
			success = fb.isSuccess();
			if (fb.getBootstrapTo() != null) {
				FutureDiscover fc = tomP2PPeer.discover()
						.setPeerAddress(fb.getBootstrapTo().iterator().next())
						.start();
				fc.awaitUninterruptibly();
			}
		} while (!success);
	}

	private void pushTextToStream(TSPeer peer, Data data, int typeCode) {
		if (peer.name.equals(currentPeer)) {
			final String labelText;
			if (typeCode == TSPeer.getCode) {
				labelText = generateReceivedMessageString(peer, data);
			} else {
				labelText = generatePutMessageString(ownName, data);
			}
			Date messageDate = Util.getDate(data);

			List<Control> scrollChildren = Arrays.asList(scrollContainer
					.getChildren());
			Label label = new Label(scrollContainer, SWT.NONE);
			label.setBackground(display.getSystemColor(SWT.TRANSPARENT));
			label.setForeground(display.getSystemColor(SWT.COLOR_DARK_GREEN));
			Long sortAfterDate = dateTimeLabels.keySet().floor(
					messageDate.getTime());
			if (sortAfterDate != null) {
				NavigableSet<Label> labels = dateTimeLabels.get(sortAfterDate);
				int lastLabelIndex = -1;
				Label lastLabel = null;
				for (Label l : labels) {
					int labelIndex = scrollChildren.indexOf(l);
					if (labelIndex > lastLabelIndex) {
						lastLabelIndex = labelIndex;
						lastLabel = l;
					}
				}
				label.moveBelow(lastLabel);
			} else if (scrollChildren.size() > 0) {
				label.moveAbove(scrollChildren.get(0));
			}
			dateTimeLabels.put(messageDate.getTime(), label);

			label.setText(labelText);

			label.pack();

			scrollContainer.setSize(scrollContainer.computeSize(SWT.DEFAULT,
					SWT.DEFAULT));
			scroll.setMinSize(scrollContainer.computeSize(SWT.DEFAULT,
					SWT.DEFAULT));

			scroll.setOrigin(0, scrollContainer.getSize().y);
		}
	}

	private void pushInput() {
		System.out.println("Enter catched");

		String input = this.inputTextField.getText();
		// normalize line endings to \n, because \r is not recognized as
		// "end of line"
		input = input.replace("/\\s*\\R/g", "\n");
		// remove leading and trailing whitespace
		input = input.replace("/^\\s*|[\\t ]+$/gm", "");

		input = input.trim();

		if (!input.isEmpty()) {
			String recipient = "no recipient";

			Label label = new Label(scrollContainer, SWT.NONE);
			label.setBackground(display.getSystemColor(SWT.TRANSPARENT));
			label.setForeground(display.getSystemColor(SWT.COLOR_DARK_GREEN));

			FutureDHT putDHT = null;
			if (groupList.getSelection().length == 1) {
				recipient = groupList.getSelection()[0].getText();
				TSPeer peer = knownPeers.get(recipient);
				try {
					putDHT = store(peer.peerHash, ownLocation,
							peer.nextPutContentKey, input);
					if (putDHT.isSuccess()) {
						System.out.println("Put " + input + " at content key "
								+ peer.nextPutContentKey.toString()
								+ " for peer " + recipient);
						HashMap<Number160, Data> map = new HashMap<Number160, Data>();
						DHTMessage msg = new DHTMessage();
						msg.createdDateTime = new Date().getTime();
						msg.payload = input;
						map.put(peer.nextPutContentKey, new Data(msg));
						peer.addNewPutData(map);
						peer.nextPutContentKey = Util
								.inc(peer.nextPutContentKey);
						prefNextPutContentKeys.put(recipient,
								peer.nextPutContentKey.toString());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			if (putDHT == null) {
				label.setText("Put of \"" + input + "\" to " + recipient
						+ " was not successful");
			} else {
				label.setText(SimpleDateFormat.getDateTimeInstance().format(
						new Date())
						+ " - " + ownName + ": " + input);
			}

			label.pack();
		}

		inputTextField.setText("");

		scrollContainer.setSize(scrollContainer.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));
		scroll.setMinSize(scrollContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		scroll.setOrigin(0, scrollContainer.getSize().y);
	}

	public static void main(final String[] args) throws NumberFormatException,
			Exception {
		// // Test code for Util.less...
		// Random rand = new Random(System.currentTimeMillis());
		// for(int i = 0; i < 50; ++i)
		// {
		// int left = rand.nextInt(Integer.MAX_VALUE);
		// int right = rand.nextInt(Integer.MAX_VALUE);
		// boolean test = Util.less(new Number160(left), new Number160(right));
		//
		// System.out.println(left + (test?"<":">"));
		// System.out.println(right);
		// }

		final ExampleSimple es = new ExampleSimple(args[0],
				Number160.createHash(args[0]));

		es.prefKnownPeers.clear();
		
		for (int i = 1; i < args.length; ++i) {
			if (es.prefKnownPeers.get(args[i], "").isEmpty()) {
				es.prefKnownPeers.put(args[i], Number160.createHash(args[i]).toString());
			}
		}
		es.prefKnownPeers.flush();

		es.knownPeers = new HashMap<String, TSPeer>(
				es.prefKnownPeers.keys().length);
		for (String peerName : es.prefKnownPeers.keys()) {
			TSPeer peer = new TSPeer(peerName);

			peer.nextPutContentKey = es.getNextContentKey(peer.peerHash,
					es.ownLocation);

			peer.nextPutContentKey = new Number160(
					es.prefNextPutContentKeys.get(peerName,
							peer.nextPutContentKey.toString()));

			System.out.println("Next put contentKey for peer " + peer + " is "
					+ peer.nextPutContentKey.toString());

			Map<Number160, Data> putData = es.getPutData(peer);
			if (putData != null) {
				for (Map.Entry<Number160, Data> entry : putData.entrySet()) {
					es.pushTextToStream(peer, entry.getValue(), TSPeer.putCode);
					peer.addNewPutData(putData);
				}
			}

			es.knownPeers.put(peerName, peer);
		}

		// the layout manager handle the layout
		// of the widgets in the container
		FormLayout shellLayout = new FormLayout();
		es.shell.setLayout(shellLayout);

		es.currentPeer = args[1];

		RowLayout scrollLayout = new RowLayout(SWT.VERTICAL);
		// scrollLayout.justify = true;

		es.scrollContainer.setLayout(scrollLayout);
		es.scrollContainer.setLayoutData(new RowData(SWT.MAX, SWT.FILL));

		es.scroll.setContent(es.scrollContainer);
		FormData scrollLayoutData = new FormData();
		scrollLayoutData.top = new FormAttachment(0, 0);
		scrollLayoutData.left = new FormAttachment(0, 0);
		scrollLayoutData.right = new FormAttachment(100, -180);
		scrollLayoutData.bottom = new FormAttachment(es.inputContainer, 0);
		es.scroll.setLayoutData(scrollLayoutData);

		es.scrollContainer.setBackground(es.display
				.getSystemColor(SWT.COLOR_BLACK));

		es.scrollContainer.setSize(es.scrollContainer.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));

		es.scroll.setExpandHorizontal(true);
		es.scroll.setExpandVertical(true);
		es.scroll.setMinSize(es.scrollContainer.computeSize(SWT.DEFAULT,
				SWT.DEFAULT));

		es.inputContainer.setLayout(new FormLayout());

		FormData groupListLayoutData = new FormData();
		groupListLayoutData.top = new FormAttachment(0, 0);
		groupListLayoutData.left = new FormAttachment(es.scroll, 0);
		groupListLayoutData.right = new FormAttachment(100, 0);
		groupListLayoutData.bottom = new FormAttachment(es.inputContainer, 0);
		es.groupList.setLayoutData(groupListLayoutData);
		es.groupList.setBackground(es.display.getSystemColor(SWT.TRANSPARENT));
		es.groupList.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				for (int i = 0; i < es.groupList.getItemCount(); ++i) {
					TableItem item = es.groupList.getItem(i);
					if (item.equals(e.item)) {
						es.currentPeer = item.getText();
						// item.setForeground(display.getSystemColor(SWT.COLOR_RED));
						item.setBackground(es.display
								.getSystemColor(SWT.COLOR_GRAY));
					} else {
						item.setForeground(es.display
								.getSystemColor(SWT.COLOR_DARK_GREEN));
						item.setBackground(es.display
								.getSystemColor(SWT.TRANSPARENT));
					}
				}

				for (Control control : es.scrollContainer.getChildren()) {
					control.dispose();
				}
				es.dateTimeLabels.clear();

				ImmutableMultimap<Long, Pair<Integer, Number160>> dataDates = es.knownPeers
						.get(es.currentPeer).getDataDates();
				Map<Number160, Data> receivedData = es.knownPeers.get(
						es.currentPeer).getReceivedData();
				Map<Number160, Data> putData = es.knownPeers
						.get(es.currentPeer).getPutData();
				for (Map.Entry<Long, Pair<Integer, Number160>> entry : dataDates
						.entries()) {
					int dataCode = entry.getValue().first;
					Number160 contentKey = entry.getValue().second;
					Label label = new Label(es.scrollContainer, SWT.NONE);
					label.setBackground(es.display
							.getSystemColor(SWT.TRANSPARENT));
					label.setForeground(es.display
							.getSystemColor(SWT.COLOR_DARK_GREEN));
					Data data = null;
					String labelText = "";
					if (dataCode == TSPeer.getCode) {
						data = receivedData.get(contentKey);
						labelText = es.generateReceivedMessageString(
								es.knownPeers.get(es.currentPeer), data);
					} else if (dataCode == TSPeer.putCode) {
						data = putData.get(contentKey);
						labelText = es.generatePutMessageString(args[0], data);
					}
					label.setText(labelText);
					label.pack();
				}

				es.scrollContainer.setSize(es.scrollContainer.computeSize(
						SWT.DEFAULT, SWT.DEFAULT));
				es.scroll.setMinSize(es.scrollContainer.computeSize(
						SWT.DEFAULT, SWT.DEFAULT));

				es.scroll.setOrigin(0, es.scrollContainer.getSize().y);

				es.shell.layout();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				TableItem[] selItems = es.groupList.getSelection();

				System.out.println(selItems.toString() + " selected");
			}
		});

		FormData inputLayoutData = new FormData();
		inputLayoutData.left = new FormAttachment(0, 0);
		inputLayoutData.right = new FormAttachment(100, 0);
		inputLayoutData.bottom = new FormAttachment(100, 0);
		es.inputContainer.setLayoutData(inputLayoutData);

		es.inputTextField.setBackground(es.display
				.getSystemColor(SWT.TRANSPARENT));
		es.inputTextField.setForeground(es.display
				.getSystemColor(SWT.COLOR_DARK_GREEN));

		es.inputTextField.addListener(SWT.KeyDown, new Listener() {

			@Override
			public void handleEvent(Event event) {
				if (event.keyCode == SWT.CR || event.keyCode == SWT.KEYPAD_CR) {
					es.pushInput();
				}
			}
		});

		int peerIndex = 0;
		int startPeerIndex = 0;
		for (String peer : es.knownPeers.keySet()) {
			if (!peer.equals(args[0])) {
				TableItem tableItem = new TableItem(es.groupList, SWT.NONE);
				tableItem.setText(peer);
				tableItem.setForeground(es.display
						.getSystemColor(SWT.COLOR_DARK_GREEN));
				if (args.length > 1 && peer.equals(args[1])) {
					startPeerIndex = peerIndex;
				}
				peerIndex++;
			}
		}

		es.groupList.select(startPeerIndex);

		Button button = new Button(es.inputContainer, SWT.PUSH);
		button.setText("send");

		FormData inputTextFieldLayoutData = new FormData();
		inputTextFieldLayoutData.left = new FormAttachment(0, 0);
		inputTextFieldLayoutData.right = new FormAttachment(button, 0);
		inputTextFieldLayoutData.top = new FormAttachment(0, 0);
		inputTextFieldLayoutData.bottom = new FormAttachment(100, 0);
		es.inputTextField.setLayoutData(inputTextFieldLayoutData);

		FormData buttonLayoutData = new FormData();
		buttonLayoutData.right = new FormAttachment(100, 0);
		buttonLayoutData.top = new FormAttachment(0, 0);
		buttonLayoutData.bottom = new FormAttachment(100, 0);
		button.setLayoutData(buttonLayoutData);

		// register listener for the selection event
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				es.pushInput();
			}
		});

		es.shell.open();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				for (final Map.Entry<String, TSPeer> knownPeer : es.knownPeers
						.entrySet()) {
					Map<Number160, Data> newData = null;
					final TSPeer peer = knownPeer.getValue();
					if (peer.peerHash.equals(es.ownLocation)) {
						continue;
					}
					System.out.println("Polling for peer " + knownPeer.getKey());
					newData = es.getNewData(peer);
					if (newData != null && newData.size() > 0) {
						System.out.println(newData.size()
								+ " new received entries found...");
						if (peer.name.equals(es.currentPeer)) {
							for (final Map.Entry<Number160, Data> entry : newData
									.entrySet()) {
								es.display.syncExec(new Runnable() {
									@Override
									public void run() {
										es.pushTextToStream(peer,
												entry.getValue(),
												TSPeer.getCode);
									}
								});
							}
						}
						peer.addNewReceivedData(newData);
					}
					Map<Number160, Data> newPutData = es.getPutData(peer);
					if (newPutData != null && newPutData.size() > 0) {
						System.out.println(newPutData.size()
								+ " new put entries found...");
						if (peer.name.equals(es.currentPeer)) {
							for (final Map.Entry<Number160, Data> entry : newPutData
									.entrySet()) {
								es.display.syncExec(new Runnable() {

									@Override
									public void run() {
										es.pushTextToStream(peer,
												entry.getValue(),
												TSPeer.putCode);
									}
								});
							}
						}
						peer.addNewPutData(newPutData);
					}
				}
			}
		}, 0, 1000);

		// run the event loop as long as the window is open
		while (!es.shell.isDisposed()) {
			// read the next OS event queue and transfer it to a SWT event
			if (!es.display.readAndDispatch()) {
				// if there are currently no other OS event to process
				// sleep until the next OS event is available
				es.display.sleep();
			}
		}

		timer.cancel();
		// disposes all associated windows and their components
		es.display.dispose();

		es.prefNextPutContentKeys.flush();

		System.out.println("Finished everything and flushed the preferences!");

		System.exit(0);
	}

	private FutureDHT store(Number160 location, Number160 domain,
			Number160 contentKey, String value) throws IOException {
		DHTMessage msg = new DHTMessage();
		msg.createdDateTime = new Date().getTime();
		msg.payload = value;
		FutureDHT fut = tomP2PPeer.put(location)
				.setData(contentKey, new Data(msg)).setDomainKey(domain)
				.start().awaitUninterruptibly();
		return fut;
	}

	private Map<Number160, Data> getNewData(TSPeer contact) {
		FutureDHT futureDHT = tomP2PPeer.get(ownLocation)
				.setDomainKey(contact.peerHash).setAll(true).start();
		futureDHT.awaitUninterruptibly();
		Map<Number160, Data> newData = null;
		if (futureDHT.isSuccess()) {
			newData = futureDHT.getDataMap();
			for (Number160 contentKey : contact.receivedData.keySet()) {
				newData.remove(contentKey);
			}
		}
		return newData;
	}

	private Map<Number160, Data> getPutData(TSPeer peer) {
		FutureDHT futureDHT = tomP2PPeer.get(peer.peerHash)
				.setDomainKey(ownLocation).setAll(true).start();
		futureDHT.awaitUninterruptibly();
		Map<Number160, Data> newData = null;
		if (futureDHT.isSuccess()) {
			newData = futureDHT.getDataMap();
			for (Number160 contentKey : peer.putData.keySet()) {
				newData.remove(contentKey);
			}
		}
		return newData;
	}

	private Number160 getNextContentKey(Number160 location, Number160 domain) {
		System.out.println("Looking for the highest contentKey in location "
				+ location.toString() + " and domain " + domain.toString());
		FutureDHT futureDHT = tomP2PPeer.get(location).setDomainKey(domain)
				.setAll(true).start();
		futureDHT.awaitUninterruptibly();
		Number160 retVal = new Number160(0);
		if (futureDHT.isSuccess()) {
			Set<Number160> contentKeys = futureDHT.getDataMap().keySet();
			for (Number160 contentKey : contentKeys) {
				System.out.println("Found contentKey " + contentKey.toString());
				if (!Util.less(contentKey, retVal)) {
					retVal = Util.inc(contentKey);
					System.out.println("Setting the new next contentKey to "
							+ retVal.toString());
				}
			}
		}
		return retVal;
	}

	public String generateReceivedMessageString(TSPeer peer, Data data) {
		return Util.getLocaleFormattedCreationDateTimeString(data) + " - "
				+ peer.name + ": " + Util.getMessageFromData(data);
	}

	public String generatePutMessageString(String ownName, Data data) {
		return Util.getLocaleFormattedCreationDateTimeString(data) + " - "
				+ ownName + ": " + Util.getMessageFromData(data);
	}
}