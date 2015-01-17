package thesignal.bus;

public interface CommandHandler<T extends Command> {
	public void handle(T command, Bus bus);
}
