package thesignal.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

import javax.swing.JTextField;

import thesignal.bus.Bus;
import thesignal.bus.Event;
import thesignal.bus.EventListener;
import thesignal.bus.RegisterException;
import thesignal.bus.commands.SendMessage;
import thesignal.bus.events.Connected;
import thesignal.bus.events.MessageReceived;
import thesignal.entity.TSGroup;
import thesignal.entity.TSMessage;
import thesignal.entity.TSPeer;

public class TSTextInputUI implements EventListener<Event> {

	private JTextField mMessageInput;
	private Bus bus;

	private class MessageSendListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String text = mMessageInput.getText().trim();
			if (!text.isEmpty()) {
				Date date = new Date();
				TSPeer sender = new TSPeer("Todo");
				TSGroup receiver = new TSGroup("Todo", Arrays.asList(sender));
				int secondDiff = new Random(date.getTime()).nextInt(121) - 60;
				TSMessage message = new TSMessage(date.toString()
						+ (secondDiff < 0 ? " - " : " + ")
						+ Math.abs(secondDiff) + ": " + text, sender, receiver,
						new Date(date.getTime() + secondDiff * 1000));

				SendMessage command =  new SendMessage(message);
				
				bus.handle(command);

//				messagesListModel.handleEvent(dummyEvent);

//				messagesList.ensureIndexIsVisible(messagesList
//					.getModel()
//					.getSize() - 1);
			}

			mMessageInput.setText(null);
			mMessageInput.requestFocusInWindow();
		}
	}

	public TSTextInputUI(Bus bus_) {
		bus = bus_;
		
		mMessageInput = new JTextField();
		mMessageInput.addActionListener(new MessageSendListener());
		
		try {
			bus.register(this, Connected.class.getName());
		} catch (RegisterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public JTextField getTextInputField()
	{
		return mMessageInput;
	}
	
	@Override
	public void handle(Event event, Bus bus) {
		// TODO Auto-generated method stub
		
	}

}
