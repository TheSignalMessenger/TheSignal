package thesignal.bus;

import static org.reflections.ReflectionUtils.*;

public class Bus {
	public Bus() {
		
	}

	public void handle(Command command) {
		// @todo
	}

	public void raise(Event event) {
		// @todo
	}
	
	/**
	 * @todo reflection magic
	 * @todo Exception werfen, wenn zwei CommandHandler auf das gleiche Command registriert werden
	 * @param commandHandler
	 */
	public void register(CommandHandler commandHandler) {
		/*
	 	commands = ListAllClassesThatImplementTheCommandInterface
		for(Command command:commands) {
			commandHandler.getMethodsThatHaveExactlyOneArgumentOfType(command)
			commandHandler(command, this)
		}
		*/
	}
	
	public void register(EventListener eventListener) {
		
	}
}
