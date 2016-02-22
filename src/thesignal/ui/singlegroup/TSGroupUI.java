package thesignal.ui.singlegroup;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import thesignal.bus.Bus;
import thesignal.bus.Event;
import thesignal.bus.EventListener;
import thesignal.repository.GroupRepository;

import com.google.inject.Inject;

public class TSGroupUI {
	private TSGroupsListModel groupsListModel;
	private JList groupsList;
	private JScrollPane groupsScrollPane;
	private GroupRepository groupRepository;
	private SelectActiveGroupInterface selectActiveGroupInterface;

	@Inject
	public TSGroupUI(GroupRepository groupRepository_,
			TSGroupsListModel groupsListModel_, SelectActiveGroupInterface selectActiveGroupInterface_) {
		groupRepository = groupRepository_;
		groupsListModel = groupsListModel_;
		selectActiveGroupInterface = selectActiveGroupInterface_;
		groupsList = new TSBaseList(groupsListModel);
		groupsScrollPane = new JScrollPane(groupsList);
		groupsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		groupsList.setVisibleRowCount(5);

		groupsList.setCellRenderer(new TSGroupCellRenderer());

		groupsList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				// goon: call selectGroup with id, which has to be added to groupsList
				// selectActiveGroupInterface.selectGroup());
			}
		});

	}

	public JScrollPane getGroupsScrollPane() {
		return groupsScrollPane;
	}
}
