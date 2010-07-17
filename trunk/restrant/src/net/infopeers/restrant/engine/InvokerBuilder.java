package net.infopeers.restrant.engine;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.infopeers.restrant.Controller;
import net.infopeers.restrant.ControllerServlet;
import net.infopeers.restrant.Params;
import net.infopeers.restrant.ResourceNotFoundException;

/**
 * Invokerの生成器
 * 
 * @author ms2
 * 
 */
public class InvokerBuilder {

	private static final String CONTROLLER_SUFFIX = "Controller";

	/**
	 * アクションを示すプレースホルダのラベル
	 */
	public static final String ACTION_PLACEHOLDER_LABEL = "action";

	/**
	 * コントローラを示すプレースホルダのラベル
	 */
	public static final String CONTROLLER_PLACEHOLDER_LABEL = "controller";

	private String rootPackage;

	private List<Parser> parsers = new ArrayList<Parser>();

	private PlaceholderFormatter phFormatter;

	/**
	 * コンストラクタ
	 * 
	 * @param phFormatter
	 *            　PlaceholderFormatter
	 */
	public InvokerBuilder(PlaceholderFormatter phFormatter) {
		this.phFormatter = phFormatter;
	}

	/**
	 * ルートパッケージを指定する
	 * 
	 * @param value
	 *            ルートパッケージ
	 */
	public void setRootPackage(String value) {
		rootPackage = value;
	}

	/**
	 * パーサを追加する
	 * 
	 * @param parser
	 *            Parser
	 */
	public void addParser(Parser parser) {
		parsers.add(parser);
	}

	/**
	 * Invokerを作成する
	 * 
	 * @param servlet
	 *            サーブレット
	 * @param req
	 *            HttpServletRequest
	 * @return Invoker
	 * @throws ResourceNotFoundException
	 */
	public Invoker build(ControllerServlet servlet, HttpServletRequest req)
			throws ResourceNotFoundException {

		String path = req.getRequestURI().substring(
				req.getContextPath().length());
		
		ParserManager pm = new ParserManager();
		pm.setParsers(parsers);
		pm.setRequest(req);
		pm.execute(path);
		Params params = pm.getSelectedParam();
		Parser selectedParser = pm.getSelectedParser();

		if (params == null) {
			throw new ResourceNotFoundException(path + "に対応するフォーマットがありません");
		}

		String controllerName = params.get(CONTROLLER_PLACEHOLDER_LABEL);
		if (controllerName == null) {
			throw new RuntimeException(selectedParser + "でコントローラが取得できません");
		}

		String actionName = params.get(ACTION_PLACEHOLDER_LABEL);
		if (actionName == null) {
			throw new RuntimeException(selectedParser + "でアクションが取得できません");
		}

		String className = controllerName.substring(0, 1).toUpperCase()
				+ controllerName.substring(1) + CONTROLLER_SUFFIX;
		String fullClassName = rootPackage + "." + className;

		try {
			Class<?> clazz = Class.forName(fullClassName);
			Controller controller = (Controller) clazz.newInstance();
			return new ReflectInvoker(servlet, phFormatter, controller, actionName,
					params);

		} catch (ClassNotFoundException e) {
			throw new RuntimeException(path + "に対応する" + fullClassName
					+ "クラスが見つかりません", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InstantiationException e) {
			throw new RuntimeException(e);
		}
	}

}
