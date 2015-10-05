package thesignal.ui;

import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

public class TSGroupUI {
	private TSGroupsListModel groupsListModel;
	private JList groupsList;
	JScrollPane groupsScrollPane;

	public TSGroupUI() {
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
}
