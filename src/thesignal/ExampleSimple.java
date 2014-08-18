package thesignal;

import java.awt.FlowLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
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
import org.eclipse.swt.widgets.Text;

import thesignal.utils.Util;
import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureBootstrap;
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

public class ExampleSimple {

	final private Peer peer;
	private Number160 getContentKey = new Number160(0);
	private Number160 putContentKey = new Number160(0);

	public static Number160 inc(Number160 other) {
		String otherString = other.toString().substring(2);
		otherString = otherString.equals("") ? "0" : otherString;
		String resultString = new BigInteger(otherString, 16).add(
				new BigInteger("1")).toString(16);
		return new Number160("0x" + resultString);
	}

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

	public ExampleSimple(int peerId) throws Exception {
		peer = new PeerMaker(new Number160(peerId)).setPorts(4000)
				.makeAndListen();
		FutureBootstrap fb = peer.bootstrap().setBroadcast().setPorts(4000)
				.start();
		fb.awaitUninterruptibly();
		if (fb.getBootstrapTo() != null) {
			peer.discover()
					.setPeerAddress(fb.getBootstrapTo().iterator().next())
					.start().awaitUninterruptibly();
		}
	}

	public static void main(String[] args) throws NumberFormatException,
			Exception {
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

		RowLayout scrollLayout = new RowLayout(SWT.VERTICAL);
//		scrollLayout.justify = true;
		
		scrollContainer.setLayout(scrollLayout);
		scrollContainer.setLayoutData(new RowData(SWT.MAX, SWT.FILL));
		
		scroll.setContent(scrollContainer);
		FormData scrollLayoutData = new FormData();
		scrollLayoutData.top = new FormAttachment(0,0);
		scrollLayoutData.left = new FormAttachment(0,0);
		scrollLayoutData.right = new FormAttachment(100, 0);
		scrollLayoutData.bottom = new FormAttachment(inputContainer, 0);
		scroll.setLayoutData(scrollLayoutData);
		
		scrollContainer.setBackground(display.getSystemColor(SWT.COLOR_BLACK));

		scrollContainer.setSize(scrollContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		scroll.setExpandHorizontal(true);
		scroll.setExpandVertical(true);
		scroll.setMinSize(scrollContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		inputContainer.setLayout(new FormLayout());

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
					
					Label label = new Label(scrollContainer, SWT.NONE);
					label.setBackground(display.getSystemColor(SWT.TRANSPARENT));
					label.setForeground(display.getSystemColor(SWT.COLOR_DARK_GREEN));

					String input = inputTextField.getText();
					// normalize line endings to \n, because \r is not recognized as "end of line"
					input = input.replace ("/\\s*\\R/g", "\n");
					// remove leading and trailing whitespace
					input = input.replace ("/^\\s*|[\\t ]+$/gm", "");
					label.setText(inputTextField.getText().trim());
					
					inputTextField.setText("");

					label.pack();

					scrollContainer.setSize(scrollContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
					scroll.setMinSize(scrollContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
					
					scroll.setOrigin(0, scrollContainer.getSize().y);
				}
			}
		});
		
//		text.addSelectionListener(new SelectionListener() {
//			
//			@Override
//			public void widgetSelected(SelectionEvent e) {
//				System.out.println("Enter catched");
//				
//				Label label = new Label(scrollContainer, SWT.NONE);
//				label.setBackground(display.getSystemColor(SWT.TRANSPARENT));
//				label.setForeground(display.getSystemColor(SWT.COLOR_DARK_GREEN));;
//				label.setText(text.getText());
//				text.selectAll();
//				text.clearSelection();
//
//				label.pack();
//				
//				scrollContainer.setSize(scrollContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//				scroll.setMinSize(scrollContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//				
//				scroll.setOrigin(0, scrollContainer.getSize().y);
//			}
//			
//			@Override
//			public void widgetDefaultSelected(SelectionEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		
//		text.addKeyListener(new KeyListener() {
//			
//			@Override
//			public void keyReleased(KeyEvent e) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void keyPressed(KeyEvent e) {
//				if(e.keyCode == SWT.CR || e.keyCode == SWT.KEYPAD_CR)
//				{
//					System.out.println("Enter catched");
//					
//					Label label = new Label(scrollContainer, SWT.NONE);
//					label.setBackground(display.getSystemColor(SWT.TRANSPARENT));
//					label.setForeground(display.getSystemColor(SWT.COLOR_DARK_GREEN));;
//					label.setText(text.getText());
//					text.selectAll();
//					text.clearSelection();
//
//					label.pack();
//					
//					scrollContainer.setSize(scrollContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//					scroll.setMinSize(scrollContainer.computeSize(SWT.DEFAULT, SWT.DEFAULT));
//					
//					scroll.setOrigin(0, scrollContainer.getSize().y);
//				}
//			}
//		});
		
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
		// int peerId = randInt(0, 10000);
		// ExampleSimple dns = new ExampleSimple(peerId);
		Util.add(new Number160(4), new Number160(5437856));
		Util.inc(new Number160(78));

		final ExampleSimple dns = new ExampleSimple(Integer.parseInt(args[0]));

		// Number160 getContentKey = new Number160(Number160.MAX_VALUE / )
		//
		final Number160 ownLocation = new Number160(Integer.valueOf(args[0]));
		final Number160 targetLocation = new Number160(Integer.valueOf(args[1]));

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				String value = null;
				do {

					try {
						value = dns.get(ownLocation, targetLocation,
								dns.getContentKey);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if (value != null) {
						System.out.println(value);
						dns.getContentKey = inc(dns.getContentKey);
					}
				} while (value != null);
			}
		}, 0, 1000);

		// if (args.length == 3) {
		// dns.store(targetLocation, ownLocation, new Number160(0), args[2]);
		// }
		// if (args.length == 2) {
		// dns.get(ownLocation, targetLocation, new Number160(0));
		// }

		for (;;) {
			try {
				BufferedReader bufferRead = new BufferedReader(
						new InputStreamReader(System.in));
				String s = bufferRead.readLine();

				if (dns.store(targetLocation, ownLocation, dns.putContentKey, s)) {
					dns.putContentKey = inc(dns.putContentKey);
				} else {
					System.out.println("Value could not be put");
					return;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		//
		// System.out.println("Enter something here : ");

	}

	private String get(Number160 location, Number160 domain,
			Number160 contentKey) throws ClassNotFoundException, IOException {
		FutureDHT futureDHT = peer.get(location).setDomainKey(domain)
				.setContentKey(contentKey).start();
		// FutureDHT futureDHT =
		// peer.get(location).setDomainKey(domain).setContentKey(contentKey).start();
		futureDHT.awaitUninterruptibly();
		if (futureDHT.isSuccess()) {
			return futureDHT.getData().getObject().toString();
			// for(Number160 key: futureDHT.getDataMap().keySet())
			// {
			// System.out.println("key: " + key.toString() + " - value: " +
			// futureDHT.getDataMap().get(key).getObject().toString());
			// }
			// return futureDHT.getData().getObject().toString();
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