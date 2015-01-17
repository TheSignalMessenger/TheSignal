package thesignal;

import thesignal.bus.Bus;
import thesignal.bus.CommandHandler;

public class TestHandler implements CommandHandler<TestCommand> {
	@Override
	public void handle(TestCommand command, Bus bus) {
		bus.raise(new TestEvent());
		System.err.println("HANDLER CALLED!");
	}
}
