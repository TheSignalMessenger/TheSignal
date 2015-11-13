package thesignal.ui;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.google.inject.Inject;

import thesignal.bus.Bus;
import thesignal.bus.Event;
import thesignal.bus.EventListener;
import thesignal.repository.GroupRepository;

public class TSGroupUI implements EventListener<Event> {
	private TSGroupsListModel groupsListModel;
	private JList groupsList;
	private JScrollPane groupsScrollPane;
	private GroupRepository groupRepository;

	@Inject
	public TSGroupUI(GroupRepository groupRepository_, TSGroupsListModel groupsListModel_) {
		groupRepository = groupRepository_;
		
		groupsListModel = groupsListModel_;
		groupsList = new TSBaseList(groupsListModel);
		groupsScrollPane = new JScrollPane(groupsList);
		groupsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		groupsList.setVisibleRowCount(5);

		groupsList.setCellRenderer(new TSGroupCellRenderer());
		
		groupsList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				groupRepository.selectGroup(groupsList.getSelectedIndex());
			}
		});
		
	}

	public JScrollPane getGroupsScrollPane()
	{
		return groupsScrollPane;
	}

	@Override
	public void handle(Event event, Bus bus) {
		// TODO Auto-generated method stub
		
	}
}
