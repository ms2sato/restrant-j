package net.infopeers.restrant.util;

import net.infopeers.restrant.Method;

public class AnnotationUtils {

	/**
	 * get annotation args by 
	 * @param ma
	 * @return
	 */
	public static String[] getArgs(Method ma) {

		if(ma == null){
			throw new IllegalArgumentException("ma == null");
		}
		
		String[] valueParams = ma.value();
		String[] argsParams = ma.args();

		// names not found. no params;
		if (valueParams.length == 0 && argsParams.length == 0)
			return new String[] {};

		// Method(name = "methodName", args = {...})
		if(argsParams.length > 0){
			return argsParams;
		}
		
		// Method({...})
		return valueParams;
	}
	
	
	public static int checkParameterLengthToAnnotationArgLabelLength(
			java.lang.reflect.Method method, String[] argLabels) {
		int parameterLen = method.getParameterTypes().length; 
		if (parameterLen != argLabels.length) {
			//TODO: 専用の例外か？
			throw new RuntimeException(
					"Method parameter difinitions not matched to Annotation args. " + method.toString() 
			);
		}
		return parameterLen;
	}
}
