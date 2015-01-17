package thesignal;

import thesignal.bus.Bus;
import thesignal.bus.Command;
import thesignal.bus.CommandHandler;

public class TestHandler implements CommandHandler<TestCommand> {
	@Override
	public void handle(TestCommand command, Bus bus) {
		System.err.println("HANDLER CALLED!");
	}
}
