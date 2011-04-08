package net.infopeers.restrant.engine;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.infopeers.restrant.engine.params.ExtensionParamPolicy;
import net.infopeers.restrant.engine.parser.UrlParserArranger;

/**
 * 要求の度に再生成するか初期化時一度だけ生成するかしていできるInvokerBuilderFactory
 * 
 * @author ms2
 */
public class DefaultInvokerBuilderFactory implements InvokerBuilderFactory {

	private static final Logger logger = Logger
			.getLogger(DefaultInvokerBuilderFactory.class.getName());

	private InvokerBuilder invokerBuilder;

	private ExtensionParamPolicy exPolicy;
	private UrlParserArranger parserArranger;
	private String rootPackage;
	private boolean isEvery = false;

	public DefaultInvokerBuilderFactory(String rootPackage,
			UrlParserArranger parserArranger, ExtensionParamPolicy exPolicy) {

		this.exPolicy = exPolicy;
		this.parserArranger = parserArranger;
		this.rootPackage = rootPackage;
	}

	public void setEvery(boolean value) {
		this.isEvery = value;
	}

	@Override
	public InvokerBuilder getInvokerBuilder() {

		if (isEvery || invokerBuilder == null) {
			invokerBuilder = createInvokerBuilder();
		}
		return invokerBuilder;
	}

	private InvokerBuilder createInvokerBuilder() {
		logger.log(Level.FINE, "create");

		InvokerBuilder invokerBuilder = new InvokerBuilder(exPolicy);
		invokerBuilder.setRootPackage(rootPackage);
		parserArranger.arrange(invokerBuilder);
		return invokerBuilder;
	}

}
