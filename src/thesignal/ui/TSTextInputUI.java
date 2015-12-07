package thesignal.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Random;

import javax.swing.JTextField;

import thesignal.TSBus;
import thesignal.bus.Bus;
import thesignal.bus.Event;
import thesignal.bus.EventListener;
import thesignal.bus.commands.SendMessage;
import thesignal.entity.Group;
import thesignal.entity.Message;
import thesignal.entity.User;
import thesignal.manager.MeManager;
import thesignal.repository.GroupRepository;

import com.google.inject.Inject;

public class TSTextInputUI implements EventListener<Event> {

	private JTextField mMessageInput;
	private Bus bus;
	private MeManager meManager;
	private GroupRepository groupRepository;

	@Inject
	public TSTextInputUI(TSBus bus_, MeManager meManager,
			GroupRepository groupRepository) {
		bus = bus_;
		this.meManager = meManager;
		this.groupRepository = groupRepository;

		mMessageInput = new JTextField();
		mMessageInput.addActionListener(new MessageSendListener());
	}

	private class MessageSendListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String text = mMessageInput.getText().trim();
			if (!text.isEmpty()) {
				Date date = new Date();
				User sender = meManager.user;
				Group receiver = groupRepository.getSelectedGroup();
				int secondDiff = new Random(date.getTime()).nextInt(121) - 60;
				Message message = new Message(date.toString()
						+ (secondDiff < 0 ? " - " : " + ")
						+ Math.abs(secondDiff) + ": " + text, sender, receiver,
						new Date(date.getTime() + secondDiff * 1000));
				message.state = Message.State.Sending;

				SendMessage command = new SendMessage(message);

				bus.handle(command);

				// messagesListModel.handleEvent(dummyEvent);

				// messagesList.ensureIndexIsVisible(messagesList
				// .getModel()
				// .getSize() - 1);
			}

			mMessageInput.setText(null);
			mMessageInput.requestFocusInWindow();
		}
	}

	public JTextField getTextInputField() {
		return mMessageInput;
	}

	@Override
	public void handle(Event event, Bus bus) {
		// TODO Auto-generated method stub

	}

}
