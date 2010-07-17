package net.infopeers.restrant.engine;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.infopeers.restrant.Controller;
import net.infopeers.restrant.ControllerServlet;
import net.infopeers.restrant.Method;
import net.infopeers.restrant.Params;
import net.infopeers.restrant.Request;
import net.infopeers.restrant.ResourceNotFoundException;
import net.infopeers.util.Convertor;

/**
 * リフレクションを利用したInvoker
 * 
 * @author ms2
 *
 */
public class ReflectInvoker implements Invoker {

	private static final Logger logger = Logger.getLogger(ReflectInvoker.class
			.getName());

	private Controller controller;

	private java.lang.reflect.Method method;

	private Params params;

	private String[] argLabels; // 引数となるプレースホルダ

	private ControllerServlet servlet; //サーブレット

	/**
	 * コンストラクタ
	 * @param servlet ControllerServlet
	 * @param phFormatter PlaceholderFormatter
	 * @param controller Controller
	 * @param methodName コールするメソッド名
	 * @param params パラメータ
	 * @throws ResourceNotFoundException
	 */
	public ReflectInvoker(ControllerServlet servlet, PlaceholderFormatter phFormatter, Controller controller,
			String methodName, Params params){
		this.servlet = servlet;
		this.controller = controller;
		this.params = params;

		this.method = getMethod(controller, methodName);
	}

	private java.lang.reflect.Method getMethod(Controller controller,
			String methodName) {
		Class<?> cls = controller.getClass();

		Set<String> exNames = params.getExtensionNames();
		removeCorePlaceholders(exNames);

		// 名前が一致し、引数なしのメソッドをデフォルトとして扱う。
		// 引数まで完全に一致しない場合にはこれが呼ばれる。
		java.lang.reflect.Method defaultMethod = null;
		
		java.lang.reflect.Method methods[] = cls.getMethods();
		for (int i = 0; i < methods.length; ++i) {

			java.lang.reflect.Method method = methods[i];
			Method ma = method.getAnnotation(Method.class);
			
			if (ma == null) {
				String name = method.getName();
				if(!name.equals(methodName)){
					continue;
				}
				
				argLabels = new String[] {};
				
			} else {
				String name = (ma.name().equals(""))? method.getName() : ma.name(); 
				if(!name.equals(methodName)){
					continue;
				}
				
				argLabels = ma.value();// 直接配列が指定されているケース
				
				//指定がデフォルトの可能性があるなら
				if (argLabels.length == 0) {
					// ラベル指定
					argLabels = ma.args();
				}
			}

			//実際のパラメータの数と、アノテーションのラベルの数が一致しなければならない
			int parameterLen = method.getParameterTypes().length; 
			if (parameterLen != argLabels.length) {
				//TODO: 専用の例外か？
				throw new RuntimeException("パラメータの数と、アノテーションのラベルの数が一致しない");
			}
			
			if(parameterLen != exNames.size()){
				if(parameterLen == 0){
					defaultMethod = method;
				}
				continue;
			}

			for (int j = 0; j < argLabels.length; ++j) {
				if (!exNames.contains(argLabels[j])) {
					continue;
				}
			}
			
			
			return method;
		}
		
		if(defaultMethod == null){
			throw new IllegalStateException("Method not found");
		}
		
		return defaultMethod;
	}

	static void removeCorePlaceholders(Set<String> exNames) {
		exNames.remove(InvokerBuilder.CONTROLLER_PLACEHOLDER_LABEL);
		exNames.remove(InvokerBuilder.ACTION_PLACEHOLDER_LABEL);
	}

	/* (non-Javadoc)
	 * @see net.infopeers.restrant.engine.Invoker#invoke(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void invoke(HttpServletRequest req, HttpServletResponse resp)
			throws Exception {

		controller.setServlet(servlet);
		controller.setRequest(req);
		controller.setResponse(resp);
		controller.setParams(params);

		setMembers(controller, req);

		Class<?>[] types = method.getParameterTypes();
		if (types.length == 0) {
			invoke(new Object[] {});
			return;
		}

		Object[] args = new Object[types.length];
		for (int i = 0; i < argLabels.length; ++i) {
			args[i] = params.asObject(types[i], argLabels[i]);
		}

		invoke(args);
	}

	private void invoke(Object[] args) throws Exception {
		String m = method.toString();
		logger.info("method: " + m);
		if(controller.beforeInvoke(m)){
			method.invoke(controller, args);
			controller.afterInvoke(m);
		}
	}

	private void setMembers(Object action, HttpServletRequest req) {

		for (Field field : action.getClass().getFields()) {
			Request annotation = field.getAnnotation(Request.class);
			if (annotation == null)
				continue;

			setValue(action, annotation, field);
		}
	}

	private void setValue(Object action, Request annotation, Field field) {

		String name = annotation.name();
		if (field.getType().isArray()) {
			String[] values = params.getParameters(name);
			if (values == null)
				return;

			try {
				field.set(action, values);
			} catch (Exception e) {
				handleParameterException(e, field, values);
			}
			return;
		}

		// 設定されていなければフィールドの名前を利用する
		if (name == null || name.length() == 0) {
			name = field.getName();
		}

		String value = params.get(name);

		if (value != null) {
			try {
				field.set(action, Convertor.convert(value, field.getType()));
			} catch (Exception e) {
				handleParameterException(e, field, value);
			}
		}
	}

	private void handleParameterException(Exception e, Field field, Object value) {
		logger.log(Level.INFO, "リクエストパラメータが設定出来ません：" + e.toString()
				+ "\n[field]" + field.getName() + "\n[value]" + value, e);
	}

}
