package io.mycat.mcache;

import java.io.IOException;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
	
	@Test
	public void testConfigLoader(){
		try {
			ConfigLoader.loadProperties(null);
			System.out.println(ConfigLoader.getProperty("DB_SERVER"));
			ConfigLoader.forEach();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
