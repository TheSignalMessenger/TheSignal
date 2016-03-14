package thesignal.ui.singlegroup;

import com.google.inject.Inject;

import thesignal.entity.Group;
import thesignal.ui.GroupDisplayInterface;

public class UiSingleGroupDisplay implements GroupDisplayInterface, SelectActiveGroupInterface {
	private TSMessagesUI messagesUi;
	private TSTextInputUI textInputUi;
	private TSGroupUI groupUi;

	private Group activeGroup;
	
	@Inject
	public UiSingleGroupDisplay(TSMessagesUI _messagesUi,
			TSTextInputUI _textInputUi, TSGroupUI _groupUi) {
		messagesUi = _messagesUi;
		textInputUi = _textInputUi;
		groupUi = _groupUi;
	}

	@Override
	public void refresh(Group group) {
		if (group != activeGroup) {
			return;
		}
		
		messagesUi.refresh(group);
	}

	@Override
	public void selectGroup(Group group) {
		textInputUi.activeGroup = group;
		activeGroup = group;
	}
}
