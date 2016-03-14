package thesignal;

import thesignal.ui.singlegroup.TSMessagesUI;
import thesignal.ui.singlegroup.TSTextInputUI;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.name.Names;

public class SingleDisplayModule extends AbstractModule {
	@Override
	protected void configure() {
		bind(TSMessagesUI.class).to(
				Key.get(TSMessagesUI.class, Names.named("singleGroup")));
		bind(TSTextInputUI.class).to(
				Key.get(TSTextInputUI.class, Names.named("singleGroup")));
	}
}
