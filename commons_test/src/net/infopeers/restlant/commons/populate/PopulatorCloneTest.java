package net.infopeers.restlant.commons.populate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

import junit.framework.TestCase;
import net.infopeers.restrant.commons.populate.DefaultPopulatorBuilder;
import net.infopeers.restrant.commons.populate.Populator;

public class PopulatorCloneTest extends TestCase {

	public void testOne() throws Exception {
		TestClass1 test1 = new TestClass1();
		test1.setIntValue(111);

		Populator populator = createPopulator();

		TestClass2 test2 = (TestClass2) populator
				.clone(TestClass2.class, test1);

		assertEquals(111, test2.getIntValue());
	}

	public void testBeanHaveBeanArray() throws Exception {
		TestClass1 test1 = new TestClass1();
		TestClass1[] test1Array = new TestClass1[3];
		for (int i = 0; i < 3; ++i) {
			test1Array[i] = Utils.createTestClass1();
		}
		test1.setTestObjectArray2linkedList(test1Array);

		Populator populator = createPopulator();

		TestClass2 test2 = (TestClass2) populator
				.clone(TestClass2.class, test1);

		LinkedList list = test2.getTestObjectArray2linkedList();
		assertEquals(3, list.size());
		Utils.validate(this, (TestClass2) list.get(0));
		Utils.validate(this, (TestClass2) list.get(1));
		Utils.validate(this, (TestClass2) list.get(2));
	}

	public void testLight() throws Exception {
		TestClass1 test1 = Utils.createTestClass1();

		Populator populator = createPopulator();

		TestClass2 test2 = (TestClass2) populator
				.clone(TestClass2.class, test1);

		Utils.validate(this, test2);
	}

	public void testDeep() throws Exception {
		Calendar cal = Utils.createCalendar();

		TestClass1 test1 = Utils.createTestClass1();
		test1.setTestObject(Utils.createTestClass1());

		Populator populator = createPopulator();
		TestClass2 test2 = (TestClass2) populator
				.clone(TestClass2.class, test1);

		Utils.validate(this, test2);
		Utils.validate(this, test2.getTestObject());
	}

	public void testCollection2Collection() throws Exception {
		Calendar cal = Utils.createCalendar();

		ArrayList from = new ArrayList();
		for (int i = 0; i < 5; ++i) {
			TestClass1 test1 = Utils.createTestClass1();
			test1.setTestObject(Utils.createTestClass1());
			from.add(test1);
		}

		Populator populator = createPopulator();

		LinkedList to = (LinkedList) populator.clone(LinkedList.class, from);

		for (int i = 0; i < 5; ++i) {
			TestClass2 test2 = (TestClass2) to.get(i);
			Utils.validate(this, test2);
			Utils.validate(this, test2.getTestObject());
		}
	}

	public void testCollection2Array() throws Exception {
		Calendar cal = Utils.createCalendar();

		ArrayList from = new ArrayList();
		for (int i = 0; i < 5; ++i) {
			TestClass1 test1 = Utils.createTestClass1();
			test1.setTestObject(Utils.createTestClass1());
			from.add(test1);
		}

		Populator populator = createPopulator();

		TestClass2[] to = (TestClass2[]) populator.clone(TestClass2[].class,
				from);

		for (int i = 0; i < 5; ++i) {
			TestClass2 test2 = (TestClass2) to[i];
			Utils.validate(this, test2);
			Utils.validate(this, test2.getTestObject());
		}
	}

	public void testArray2Collection() throws Exception {
		Calendar cal = Utils.createCalendar();

		TestClass1[] from = new TestClass1[5];
		for (int i = 0; i < 5; ++i) {
			TestClass1 test1 = Utils.createTestClass1();
			test1.setTestObject(Utils.createTestClass1());
			from[i] = test1;
		}

		Populator populator = createPopulator();

		LinkedList to = (LinkedList) populator.clone(LinkedList.class, from);

		for (int i = 0; i < 5; ++i) {
			TestClass2 test2 = (TestClass2) to.get(i);
			Utils.validate(this, test2);
			Utils.validate(this, test2.getTestObject());
		}
	}

	public void testArray2Array() throws Exception {
		Calendar cal = Utils.createCalendar();

		TestClass1[] from = new TestClass1[5];
		for (int i = 0; i < 5; ++i) {
			TestClass1 test1 = Utils.createTestClass1();
			test1.setTestObject(Utils.createTestClass1());
			from[i] = test1;
		}

		Populator populator = createPopulator();

		TestClass2[] to = (TestClass2[]) populator.clone(TestClass2[].class,
				from);

		for (int i = 0; i < 5; ++i) {
			TestClass2 test2 = (TestClass2) to[i];
			Utils.validate(this, test2);
			Utils.validate(this, test2.getTestObject());
		}
	}

	
	
	
	public void testPopulateOne() throws Exception {
		TestClass1 test1 = new TestClass1();
		test1.setIntValue(111);

		TestClass2 test2 = new TestClass2();

		Populator populator = createPopulator();

		populator.populate(test1, test2);

		assertEquals(111, test2.getIntValue());
	}

	public void testPopulateBeanHaveBeanArray() throws Exception {
		
		TestClass1 test1 = new TestClass1();
		TestClass1[] test1Array = new TestClass1[3];
		for (int i = 0; i < 3; ++i) {
			test1Array[i] = Utils.createTestClass1();
		}
		test1.setTestObjectArray2linkedList(test1Array);
		
		TestClass2 test2 = new TestClass2();
		

		Populator populator = createPopulator();
		populator.populate(test1, test2);

		LinkedList list = test2.getTestObjectArray2linkedList();
		assertEquals(3, list.size());
		Utils.validate(this, (TestClass2) list.get(0));
		Utils.validate(this, (TestClass2) list.get(1));
		Utils.validate(this, (TestClass2) list.get(2));
	}

	
	public void testPopulateBeanHaveBeanArrayAndTarget() throws Exception {
		
		TestClass1 test1 = new TestClass1();
		TestClass1[] test1Array = new TestClass1[3];
		for (int i = 0; i < 3; ++i) {
			test1Array[i] = Utils.createTestClass1();
		}
		test1.setTestObjectArray2linkedList(test1Array);
		
		TestClass2 test2 = new TestClass2();
		TestClass2 innner = new TestClass2();
		{
			//test2にも内包するオブジェクトが既に存在する
			LinkedList list = new LinkedList();
			list.add(innner);
			test2.setTestObjectArray2linkedList(list);
		}
		

		Populator populator = createPopulator();
		populator.populate(test1, test2);

		LinkedList list = test2.getTestObjectArray2linkedList();
		assertEquals(3, list.size());
		assertEquals(innner, list.get(0)); //オブジェクトは置き換えられていない。
		Utils.validate(this, (TestClass2) list.get(0));
		Utils.validate(this, (TestClass2) list.get(1));
		Utils.validate(this, (TestClass2) list.get(2));
	}
	
	
	private Populator createPopulator() {
		DefaultPopulatorBuilder builder = new DefaultPopulatorBuilder();
		builder.addBean2Bean(TestClass1.class, TestClass2.class);
		Populator populator = builder.create();
		return populator;
	}

}
