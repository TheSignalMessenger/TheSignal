package thesignal.ui;

import java.awt.Color;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.ListModel;

public class TSBaseList extends JList {
	public TSBaseList() {
		setColors();
	}

	public TSBaseList(Object[] listData) {
		super(listData);
		setColors();
	}

	public TSBaseList(Vector<?> listData) {
		super(listData);
		setColors();
	}

	public TSBaseList(ListModel dataModel) {
		super(dataModel);
		setColors();
	}

	private void setColors() {
		setBackground(Color.BLACK);
		setForeground(Color.GREEN);
		setSelectionBackground(Color.CYAN);
		setSelectionForeground(Color.BLUE);
	}

}
