package net.infopeers.restrant.engine;

import java.util.HashSet;

import junit.framework.TestCase;
import net.infopeers.restrant.TestParams;

public class ReflectInvokerTest extends TestCase {
	
	private PlaceholderFormatter phFormatter = new DefaultPlaceholderFormatter();
	
	
	public void testSenario() throws Exception{
		
		TestController controller = new TestController();
		TestParams params = new TestParams();
		
		//"test"はメソッドとして存在する
		ReflectInvoker invoker = new ReflectInvoker(null, controller, "test", params);
		invoker.invoke(null, null);
		
		assertTrue(controller.isTestCalled);
	}

	public void testAnnotatedArg() throws Exception{
		
		TestController controller = new TestController();
		TestParams params = new TestParams();
		params.addExtension("id", "12345");
		
		// idは@Method({"id"})で指定されている
		ReflectInvoker invoker = new ReflectInvoker(null, controller, "testAnnotated", params);
		invoker.invoke(null, null);
		
		assertEquals(12345, controller.testAnnotatedId);
	}

	public void testAnnotatedArgs() throws Exception{
		
		TestController controller = new TestController();
		TestParams params = new TestParams();
		params.addExtension("id", "12345");
		params.addExtension("label", "LABEL");
		
		// idは@Method({"id", "label"})で指定されている
		ReflectInvoker invoker = new ReflectInvoker(null, controller, "testAnnotated", params);
		invoker.invoke(null, null);
		
		assertEquals(12345, controller.testAnnotatedId);
		assertEquals("LABEL", controller.testAnnotatedLabel);
	}
	
	public void testNamed() throws Exception{
		
		TestController controller = new TestController();
		TestParams params = new TestParams();
		params.addExtension("id", "12345");
		
		// "namedMethod"は@Method(name="namedMethod")で指定されている
		ReflectInvoker invoker = new ReflectInvoker(null, controller, "namedMethod", params);
		invoker.invoke(null, null);
		
		assertEquals(12345, controller.namedMethodId);
	}
	
	public void testArray() throws Exception{
		
		TestController controller = new TestController();
		TestParams params = new TestParams();
		params.addExtension("values", "12345");
		params.addExtension("values", "12346");
		
		// "namedMethod"は@Method(name="namedMethod")で指定されている
		ReflectInvoker invoker = new ReflectInvoker(null, controller, "testArray", params);
		invoker.invoke(null, null);
		
		assertEquals(2, controller.arrayValues.length);
		assertTrue(12345 == controller.arrayValues[0]);
		assertTrue(12346 == controller.arrayValues[1]);
	}

	public void testArrayKeyNotFound() throws Exception{
		
		TestController controller = new TestController();
		TestParams params = new TestParams();
		//実際のキーに対応した値が送信されなかった場合
		params.addExtension("nokey", "12345");
		params.addExtension("nokey", "12346");
		
		try{
			ReflectInvoker invoker = new ReflectInvoker(null, controller, "testArray", params);
			invoker.invoke(null, null);
			fail("値が合致しない場合には例外");
		}catch(IllegalStateException e){
			//ここに来ればいい
		}
	}
	
	
	public void testComplexed() throws Exception{
		
		TestController controller = new TestController();
		TestParams params = new TestParams();
		params.addExtension("id", "12345");
		params.addExtension("label", "ThisIsIt");

		// "namedMethod"及び、複数の引数で、かつメソッド名一致条件。
		// @Method(name="namedMethod", args={"id", "label"})で指定されている
		ReflectInvoker invoker = new ReflectInvoker(null, controller, "namedMethod", params);
		invoker.invoke(null, null);
		
		assertEquals(12345, controller.namedMethodId);
		assertEquals("ThisIsIt", controller.namedMethodLabel);
	}
	
	
	public void testNotFound() throws Exception{
		
		TestController controller = new TestController();
		TestParams params = new TestParams();
		
		try{
			new ReflectInvoker(null, controller, "NotFound", params);
			fail("メソッドが見つからなければ例外");
		}catch(IllegalStateException e){
			//ここにくれば良い。
		}
	}

	public void testNotFoundByNamed() throws Exception{
		
		TestController controller = new TestController();
		TestParams params = new TestParams();
		
		try{
			 //testNamedは見つからない名前。メソッドとしては存在するが、nameで隠されている
			new ReflectInvoker(null, controller, "testNamed", params);
			fail("メソッドが見つからなければ例外");
		}catch(IllegalStateException e){
			//ここにくれば良い。
		}
	}
	
	public void testRemoveCorePlaceholders() throws Exception{
	
		HashSet<String> exNames = new HashSet<String>();
		exNames.add("controller"); //消える予定
		exNames.add("action");  //消える予定
		exNames.add("test"); //残る予定
		
		ReflectInvoker.removeCorePlaceholders(exNames);
		
		assertEquals(1, exNames.size());
		assertTrue(exNames.contains("test"));
			
		
	}
	
}
