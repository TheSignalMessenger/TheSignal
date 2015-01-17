package thesignal.bus;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

public class Bus {
	private HashMap<String, CommandHandler> commandHandlers = new HashMap<String, CommandHandler>();
	private ArrayList<EventListener> eventListener = new ArrayList<EventListener>();
	private Logger logger = Logger.getLogger(this.getClass().getName());
	
	public void handle(Command command) {
		logger.info("Handling command \"" + command.getClass().getName() + "\" ...");
	
		CommandHandler commandHandler = commandHandlers.get(command.getClass().getName());
		/*
		try {
			Class <?> c = Class.forName(commandHandler.getClass().getName());	

			for(Method m: c.getDeclaredMethods())  {
				
			}
		}
		catch(ClassNotFoundException e) {
			
		}
		*/
		
		logger.info("Command \"" + command.getClass().getName() + "\" handled.");
		
	}

	public void raise(Event event) {
		// @todo
	}
	
	/**
	 * @throws TooManyCommandHandlersExceptions
	 * @param commandHandler
	 */
	public void register(CommandHandler commandHandler, String commandName) 
			throws TooManyCommandHandlersExceptions {
		if (commandHandlers.containsKey(commandName)) {
			throw new TooManyCommandHandlersExceptions();
		}
		commandHandlers.put(commandName, commandHandler);
	}
	
	public void register(EventListener eventListener) {
		this.eventListener.add(eventListener);
	}

	private Set<Method> getMethodsByCommand(CommandHandler commandHandler,
			Class<? extends Command> command) {
		return ReflectionUtils.getAllMethods(
				commandHandler.getClass(), 
				ReflectionUtils.withModifier(Modifier.PUBLIC),
				ReflectionUtils.withParametersCount(1),
				ReflectionUtils.withParametersAssignableTo(command)
				);
	}
}
