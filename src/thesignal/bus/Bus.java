package thesignal.bus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class Bus {
	private HashMap<String, CommandHandler<?>> commandHandlers = new HashMap<String, CommandHandler<?>>();
	private ArrayList<EventListener> eventListeners = new ArrayList<EventListener>();
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public void handle(Command command) {
		logger.info("Handling command \"" + command.getClass().getName() + "\" ...");
	
		@SuppressWarnings("unchecked")
		CommandHandler<Command> commandHandler = (CommandHandler<Command>) commandHandlers.get(command.getClass().getName());
		if (commandHandler == null) {
			logger.info("No handler for \"" + command.getClass().getName() + "\" registered.");
			return;
		}
		commandHandler.handle(command, this);
		logger.info("Command \"" + command.getClass().getName() + "\" handled.");
	}

	public void raise(Event event) {
		for(EventListener eventListener : eventListeners) {
			System.err.println("ABCABC");
		}
	}
	
	/**
	 * @throws TooManyCommandHandlersExceptions
	 * @param commandHandler
	 */
	public void register(CommandHandler<?> commandHandler, String commandName) 
			throws TooManyCommandHandlersExceptions {
		if (commandHandlers.containsKey(commandName)) {
			throw new TooManyCommandHandlersExceptions();
		}
		commandHandlers.put(commandName, commandHandler);
	}
	
	/**
	 * @param commandHandler
	 * @param commandName
	 * @throws UnregisterException
	 */
	public void unregister(CommandHandler<?> commandHandler, String commandName)
			throws UnregisterException {
		if (!commandHandlers.containsKey(commandName)) {
			throw new UnregisterException();
		}
		if (commandHandlers.get(commandName).equals(commandHandler)) {
			throw new UnregisterException();
		}
		commandHandlers.remove(commandName);
	}
	
	/**
	 * @param eventListener
	 */
	public void register(EventListener eventListener) {
		this.eventListeners.add(eventListener);
	}
}
