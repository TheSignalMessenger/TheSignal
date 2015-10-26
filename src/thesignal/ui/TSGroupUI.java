package thesignal.ui;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import thesignal.bus.Bus;
import thesignal.bus.Event;
import thesignal.bus.EventListener;
import thesignal.bus.RegisterException;
import thesignal.bus.events.GotMessages;
import thesignal.bus.events.GroupAdded;
import thesignal.bus.events.GroupOrderChanged;

public class TSGroupUI implements EventListener<Event> {
	private TSGroupsListModel groupsListModel;
	private JList groupsList;
	private JScrollPane groupsScrollPane;

	public TSGroupUI(Bus bus) {
		groupsListModel = new TSGroupsListModel();
		groupsList = new TSBaseList(groupsListModel);
		groupsScrollPane = new JScrollPane(groupsList);
		groupsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		groupsList.setVisibleRowCount(5);

		groupsList.setCellRenderer(new TSMessageCellRenderer());
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
