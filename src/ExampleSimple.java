import java.awt.font.NumericShaper;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import net.tomp2p.futures.FutureDHT;
import net.tomp2p.futures.FutureBootstrap; 
import net.tomp2p.p2p.Peer;
import net.tomp2p.p2p.PeerMaker;
import net.tomp2p.peers.Number160;
import net.tomp2p.storage.Data;
public class ExampleSimple {

    final private Peer peer;
    private Number160 getContentKey = new Number160(0);
    private Number160 putContentKey = new Number160(0);
    
    public static Number160 inc(Number160 other)
    {
    	String otherString = other.toString().substring(2);
    	otherString = otherString.equals("")?"0":otherString;
    	String resultString = new BigInteger(otherString, 16).add(new BigInteger("1")).toString(16);
    	return new Number160("0x" + resultString); 
    }
    
   	/**
	 * Returns a pseudo-random number between 0 and Number160.MAX:VALUE, inclusive.
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
	 * Returns a pseudo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min Minimum value
	 * @param max Maximum value.  Must be greater than min.
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
	
    public ExampleSimple(int peerId) throws Exception {
        peer = new PeerMaker(new Number160(peerId)).setPorts(4000).makeAndListen();
        FutureBootstrap fb = peer.bootstrap().setBroadcast().setPorts(4000).start();
        fb.awaitUninterruptibly();
        if (fb.getBootstrapTo() != null) {
            peer.discover().setPeerAddress(fb.getBootstrapTo().iterator().next()).start().awaitUninterruptibly();
        }
    }

    public static void main(String[] args) throws NumberFormatException, Exception {
//    	int peerId = randInt(0, 10000);
//      ExampleSimple dns = new ExampleSimple(peerId);
        final ExampleSimple dns = new ExampleSimple(Integer.parseInt(args[0]));
        
//        Number160 getContentKey = new Number160(Number160.MAX_VALUE / )
//        
        final Number160 ownLocation = new Number160(Integer.valueOf(args[0]));
        final Number160 targetLocation = new Number160(Integer.valueOf(args[1]));
        
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
			
			@Override
			public void run() {
				String value = null;
				do {
					
					try {
						value = dns.get(ownLocation, targetLocation, dns.getContentKey);
					} catch (ClassNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(value != null)
					{
						System.out.println(value);
						dns.getContentKey = inc(dns.getContentKey);
					}
				} while (value != null);
			}
		}, 0, 1000);
        
//        if (args.length == 3) {
//            dns.store(targetLocation, ownLocation, new Number160(0), args[2]);
//        }
//        if (args.length == 2) {
//        	dns.get(ownLocation, targetLocation, new Number160(0));
//        }

        for(;;)
        {
        	try{
	    	    BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
	    	    String s = bufferRead.readLine();
	     
	    	    if(dns.store(targetLocation, ownLocation, dns.putContentKey, s))
	    	    {
	    	    	dns.putContentKey = inc(dns.putContentKey);
	    	    }
	    	    else
	    	    {
					System.out.println("Value could not be put");
					return;
	    	    }
	    	}
	    	catch(IOException e)
	    	{
	    		e.printStackTrace();
	    	}
        }
        
//        
//    	System.out.println("Enter something here : ");
    	 
    }

    private String get(Number160 location, Number160 domain, Number160 contentKey) throws ClassNotFoundException, IOException {
        FutureDHT futureDHT = peer.get(location).setDomainKey(domain).setContentKey(contentKey).start();
//        FutureDHT futureDHT = peer.get(location).setDomainKey(domain).setContentKey(contentKey).start();
        futureDHT.awaitUninterruptibly();
        if (futureDHT.isSuccess()) {
        	return futureDHT.getData().getObject().toString();
//        	for(Number160 key: futureDHT.getDataMap().keySet())
//        	{
//        		System.out.println("key: " + key.toString() + " - value: " + futureDHT.getDataMap().get(key).getObject().toString());
//        	}
//            return futureDHT.getData().getObject().toString();
        }
        return null;
    }

    private boolean store(Number160 location, Number160 domain, Number160 contentKey, String value) throws IOException {
    	FutureDHT fut = peer.put(location).setData(contentKey, new Data(value)).setDomainKey(domain).start().awaitUninterruptibly();
    	return fut.isSuccess();
    }
}

