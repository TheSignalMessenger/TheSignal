package thesignal.bus;

import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Logger;

public class Bus {
	private HashMap<String, CommandHandler<?>> commandHandlers = new HashMap<String, CommandHandler<?>>();
	private HashMap<String, HashSet<EventListener<?>>> eventListeners = new HashMap<String, HashSet<EventListener<?>>>();
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

	@SuppressWarnings("unchecked")
	public void raise(Event event) {
		logger.info("Raising event \"" + event.getClass().getName() + "\" ...");
		HashSet<EventListener<?>> listeners = eventListeners.get(event.getClass().getName());
		if(listeners == null)
		{
			logger.info("No listeners for \"" + event.getClass().getName() + "\" registered.");
			return;
		}
		for(EventListener<?> eventListener : listeners) {
			((EventListener<Event>)eventListener).handle(event, this);
			logger.info("Event \"" + event.getClass().getName() + "\" handled by listener " + eventListener.getClass().getName() + ".");
		}
	}
	
	/**
	 * @throws TooManyCommandHandlersExceptions
	 * @param commandHandler
	 * @throws RegisterException 
	 */
	public void register(CommandHandler<?> commandHandler, String commandName) throws RegisterException 
	{
		if(commandHandler == null)
		{
			throw new RegisterException("Trying to register a null commandHandler!");
		}
		CommandHandler<?> oldHandler =  commandHandlers.get(commandName);
		if(commandHandler.equals(oldHandler))
		{
			throw new RegisterException("Reregistering same commandhandler!");
		}
		else if(oldHandler != null) {
			throw new RegisterException("Another handler was already registered for the command " + commandName + "!");
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
		if(commandHandler == null)
		{
			throw new UnregisterException("Trying to unregister a null commandHandler!");
		}
		if (!commandHandlers.containsKey(commandName)) {
			throw new UnregisterException("Command " + commandName + " not registered!");
		}
		if (commandHandlers.get(commandName).equals(commandHandler)) {
			throw new UnregisterException("Another commandhandler was registred!");
		}
		commandHandlers.remove(commandName);
	}
	
	/**
	 * @param eventListener
	 * @throws RegisterException 
	 */
	public void register(EventListener<?> eventListener, String eventName) throws RegisterException {
		if(eventListener == null)
		{
			throw new RegisterException("Trying to register a null eventListener!");
		}
		HashSet<EventListener<?>> listeners = eventListeners.get(eventName);
		if(listeners == null)
		{
			listeners = new HashSet<EventListener<?>>();
			eventListeners.put(eventName, listeners);
		}
		if(listeners.contains(eventListener))
		{
			throw new RegisterException("Reregistering same eventListener!");
		}
		listeners.add(eventListener);
	}

	public void unregister(EventListener<?> eventListener, String eventName)
			throws UnregisterException {
		if(eventListener == null)
		{
			throw new UnregisterException("Trying to unregister a null eventListener!");
		}
		HashSet<EventListener<?>> listeners = eventListeners.get(eventName);
		if (listeners == null) {
			throw new UnregisterException("Event " + eventName + " not registered!");
		}
		if(!listeners.remove(eventListener))
		{
			throw new UnregisterException(eventListener.getClass().getName() + " not registered for event " + eventName + "!");
		}
	}
}
