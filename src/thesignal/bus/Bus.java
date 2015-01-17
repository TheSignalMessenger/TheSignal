package thesignal.bus;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.ReflectionUtils;
import org.reflections.Reflections;

public class Bus {
	private ArrayList<CommandHandler> commandHandlers;
	
	public Bus() {
		this.commandHandlers = new ArrayList<CommandHandler>();
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
	 * @throws TooManyCommandHandlersExceptions
	 * @param commandHandler
	 */
	public void register(CommandHandler commandHandler) throws TooManyCommandHandlersExceptions {
		ArrayList <Method> handlerMethods = new ArrayList<Method>();
		Reflections reflections = new Reflections("thesignal");
		Class<? extends Command> commandToListenTo;
		for(Class<? extends Command> command: reflections.getSubTypesOf(Command.class)) {
			Set<Method> methods = getMethodsByCommand(commandHandler, command);
			if (methods.size() == 1) {
				commandToListenTo = command;
			} else if (methods.size() > 1) {
				throw new TooManyCommandHandlersExceptions();
			}
		}
		
		for(CommandHandler otherCommandHandler: this.commandHandlers) {
			
		}
		
		commandHandlers.add(commandHandler);
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
	
	public void register(EventListener eventListener) {
		// @todo
	}
}
