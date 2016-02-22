package thesignal.ui.singlegroup;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import thesignal.entity.Group;

public class TSGroupCellRenderer extends DefaultListCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1963324608886252023L;

	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		Group group = (Group) value;
		JLabel component = new JLabel(group.name());
		component.setOpaque(true);
		if(isSelected)
		{
			component.setBackground(list.getSelectionBackground());
			component.setForeground(list.getSelectionForeground());
		}
		else
		{
			component.setBackground(list.getBackground());
			component.setForeground(list.getForeground());
		}

		return component;
	}
}
