package net.infopeers.restrant.kitchen.util;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class EntityUtilsTest {

	@Test
	public void senario(){
	
		TestEntity t = new TestEntity();
		
		{
			EntityUtils.setAutoId(t);
			// generate id
			assertNotNull(t.getId());
			assertNull(t.getDummy());  // if has not @Id, ignored
		}

		{
			t.setId("TEST");
			EntityUtils.setAutoId(t);
			// already has value, not generate id
			assertEquals("TEST", t.getId());
			assertNull(t.getDummy());  // if has not @Id, ignored
		}
		
		
	}
}
