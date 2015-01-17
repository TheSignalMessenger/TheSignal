package thesignal.ui;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import thesignal.entity.TSMessage;

public class TSMessageCellRenderer extends DefaultListCellRenderer {
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		TSMessage message = (TSMessage) value;
		JLabel component = new JLabel(message.getPayload());
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
