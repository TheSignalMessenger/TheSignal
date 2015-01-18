package thesignal.bus;

public interface EventListener<T extends Event> {
	public void handle(T event, Bus bus);
}
