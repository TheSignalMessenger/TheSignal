package thesignal.utils;

public class Pair<F extends Comparable<F> , S extends Comparable<S>> implements Comparable<Pair<F, S>>{
    public F first;
    public S second;
    
    public Pair(F first, S second) {
    	this.first = first;
    	this.second = second;
	}

	@Override
	public int compareTo(Pair<F, S> o) {
		int compRes = first.compareTo(o.first);
		if(compRes == 0)
		{
			return second.compareTo(o.second);
		}
		return compRes;
	}
}