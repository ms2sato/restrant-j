package net.infopeers.util;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * 変換器
 * 
 * @author ms2
 *
 */
public class Convertor {
	
	/**
	 * valueをtypeへパースして変換する
	 * @param value 文字列
	 * @param type 変換先型
	 * @return オブジェクト
	 */
	public static Object convert(String value, Class<?> type) {
		
		if(type == Boolean.class || type == Boolean.TYPE){
			return Boolean.valueOf(value);
		}

		if(type == Integer.class || type == Integer.TYPE){
			return Integer.valueOf(value);
		}
		
		if(type == Long.class || type == Long.TYPE){
			return Long.valueOf(value);
		}
		
		if(type == Double.class || type == Double.TYPE){
			return Double.valueOf(value);
		}
		
		if(type == String.class){
			return value;
		}
		
		if(type == BigInteger.class){
			return new BigInteger(value);
		}
		
		if(type == BigDecimal.class){
			return new BigDecimal(value);
		}
		
		throw new RuntimeException("対応していない型です");
	}	

}
