package thesignal.utils;

import java.math.BigInteger;

import net.tomp2p.peers.Number160;

public final class Util {
    public static Number160 add(final Number160 left, final Number160 right)
    {
    	byte[] leftBytes = left.toByteArray();
    	byte[] rightBytes = right.toByteArray();
    	return new Number160(new BigInteger(leftBytes).add(new BigInteger(rightBytes)).toByteArray());
    }

    public static Number160 inc(final Number160 val)
    {
    	return add(val, new Number160(1));
    }
}
