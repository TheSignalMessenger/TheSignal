package thesignal.utils;

import java.awt.Color;
import java.io.IOException;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import thesignal.entity.DHTMessage;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;

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

	/**
	 * Returns a pseudo-random number between 0 and Number160.MAX:VALUE,
	 * inclusive.
	 *
	 * @return random Number160.
	 * @see java.util.Random#nextBytes()
	 */
	public static Number160 randNumber160() {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random(System.currentTimeMillis());

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		byte[] randBytes = new byte[20];
		rand.nextBytes(randBytes);

		return new Number160(randBytes);
	}

	/**
	 * Returns a pseudo-random number between min and max, inclusive. The
	 * difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min
	 *            Minimum value
	 * @param max
	 *            Maximum value. Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public static int randInt(int min, int max) {

		// NOTE: Usually this should be a field rather than a method
		// variable so that it is not re-seeded every call.
		Random rand = new Random(System.currentTimeMillis());

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = rand.nextInt((max - min) + 1) + min;

		return randomNum;
	}

	public static boolean less(Number160 left, Number160 right) {
		String leftString = left.toString();
		String rightString = right.toString();
		if(leftString.length() < rightString.length())
		{
			return true;
		}
		else if(leftString.length() > rightString.length())
		{
			return false;
		}
		else
		{
			return leftString.compareTo(rightString) < 0;
		}
	}

	public static boolean greater(Number160 left, Number160 right) {
		return less(right, left);
	}
	
	public static String getMessageFromData(Data data)
	{
		String message = "";
		Object dataObject = null;
		try {
			DHTMessage msg;
			dataObject = data.getObject();
			msg = (DHTMessage) dataObject;
			message = msg.payload;
		} catch (ClassNotFoundException e1) {
			// e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ClassCastException e) {
			message = dataObject.toString();
		}
		return message;
	}
	
	public static Color mix(Color left, Color right)
	{
		Color result = new Color((left.getRed() + right.getRed()) / 2, (left.getGreen() + right.getGreen()) / 2, (left.getBlue() + right.getBlue()) / 2, (left.getAlpha() + right.getAlpha()) / 2);
		return result;
	}
}
