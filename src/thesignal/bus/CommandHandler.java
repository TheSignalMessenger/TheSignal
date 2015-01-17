package thesignal.bus;

public interface CommandHandler {
	abstract void handle(Command command, Bus bus);
}
