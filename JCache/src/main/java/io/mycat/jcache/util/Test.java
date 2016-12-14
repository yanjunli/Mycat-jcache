package io.mycat.jcache.util;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import sun.nio.ch.DirectBuffer;

/**
 * 
 * @author liyanjun
 *
 */
public class Test {
	
	public static void main(String[] args) {
		ByteBuffer bb = ByteBuffer.allocateDirect(100);
		long addr = ((DirectBuffer) bb).address();
		Random r = new Random();
		
    	List<Thread> threads = IntStream.range(0, 2)
    			.mapToObj(i->new Thread(()->{
    				 int dorm = r.nextInt(10);
    				for(;;){
				    	 ItemUtil.setPrev(addr,(byte)dorm);
				    	 try {
							Thread.sleep(r.nextInt(100));
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
				    	 System.out.println("current thread "+i+"= set === "+ dorm +"  get=="+ItemUtil.getPrev(addr));
    				}}))
    			.filter(t->{t.start();return true;}).collect(Collectors.toList());
    	
    	threads.forEach(t->{
	  		try {
	  			t.join();
	  		} catch (InterruptedException e) {
	  		 
	  		}
	  	});
	}

}
