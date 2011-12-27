package net.infopeers.restlant.commons.populate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import junit.framework.TestCase;

public class Utils {

	public static Calendar createCalendar() {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(11111111);
		return cal;
	}

	public static TestClass1 createTestClass1() {
		Calendar cal = createCalendar();
		
		TestClass1 test1 = new TestClass1();
		test1.setDateValue(cal.getTime());
		test1.setIntValue(123);
		test1.setLongValue(new Long(345));
		test1.setName("てすと");
		test1.setCanToStr(new Long(556));
		test1.setPrimitiveToStr(567L);
	
		ArrayList list = new ArrayList();
		list.add(new Long(123));
		list.add(new Long(456));
		list.add(new Long(789));
		test1.setLongCollectionToNumberArray(list);
	
		Long[] longs = new Long[] { new Long(135), new Long(246) };
		test1.setLongArrayToNumberCollection(longs);
	
		LinkedList linkedList = new LinkedList();
		linkedList.add(new Double(1.2));
		linkedList.add(new Double(2.3));
		linkedList.add(new Double(3.4));
		test1.setLinkedListToArrayList(linkedList);
	
		return test1;
	}

	public static void validate(TestCase body, TestClass2 test2) {
		Calendar cal = createCalendar();
		
		// プロパティ名が一致する項目についてそれぞれ値が移植されたことを確認
		body.assertEquals(123, test2.getIntValue());
		body.assertEquals("てすと", test2.getName());
		body.assertEquals(cal.getTime().getTime(), test2.getDateValue().getTime());
	
		// プロパティが合致しなければ移動されない
		body.assertEquals(0, test2.getNotLong());
	
		body.assertEquals("556", test2.getCanToStr()); // 文字列化変換可能なクラスは変換される
		body.assertEquals("567", test2.getPrimitiveToStr()); // プリミティブは変換される
	
		Number[] numbers = test2.getLongCollectionToNumberArray();
		body.assertEquals(3, numbers.length);
		body.assertEquals(new Long(123), numbers[0]);
		body.assertEquals(new Long(456), numbers[1]);
		body.assertEquals(new Long(789), numbers[2]);
	
		Collection numCol = test2.getLongArrayToNumberCollection();
		body.assertEquals(2, numCol.size());
		Iterator itr = numCol.iterator();
		body.assertEquals(new Long(135), itr.next());
		body.assertEquals(new Long(246), itr.next());
	
		ArrayList arrayList = test2.getLinkedListToArrayList();
		body.assertEquals(3, arrayList.size());
		body.assertEquals(new Double(1.2), arrayList.get(0));
		body.assertEquals(new Double(2.3), arrayList.get(1));
		body.assertEquals(new Double(3.4), arrayList.get(2));
	}

}
