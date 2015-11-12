package thesignal.ui;

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

import thesignal.entity.TSMessage;
import thesignal.utils.Util;

public class TSMessageCellRenderer extends DefaultListCellRenderer {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1963324608886252023L;

	private static Color ColorSending = Color.ORANGE;
	private static Color ColorSendOK = Color.CYAN;
	private static Color ColorSendAck = Color.GREEN;
	private static Color ColorSendFailed = Color.RED;
	
	@Override
	public Component getListCellRendererComponent(JList list, Object value,
			int index, boolean isSelected, boolean cellHasFocus) {
		TSMessage message = (TSMessage) value;
		JLabel component = new JLabel(message.getTimestamp().toString() + ": " + message.getPayload());
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
		if(message.state.equals(TSMessage.State.Sending))
		{
			component.setForeground(Util.mix(component.getForeground(), ColorSending));
		}
		else if(message.state.equals(TSMessage.State.SendFailed))
		{
			component.setForeground(Util.mix(component.getForeground(), ColorSendFailed));
		}
		else if(message.state.equals(TSMessage.State.SendOK))
		{
			component.setForeground(Util.mix(component.getForeground(), ColorSendOK));
		}
		else if(message.state.equals(TSMessage.State.SendAcknowledged))
		{
			component.setForeground(Util.mix(component.getForeground(), ColorSendAck));
		}

		return component;
	}
}
