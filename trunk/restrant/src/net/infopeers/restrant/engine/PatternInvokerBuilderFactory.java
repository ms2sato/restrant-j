package net.infopeers.restrant.engine;

import java.util.logging.Level;
import java.util.logging.Logger;

import net.infopeers.restrant.engine.params.ExtensionMultimapFactory;
import net.infopeers.restrant.engine.parser.UrlParserArranger;

/**
 * 要求の度に再生成するか初期化時一度だけ生成するかしていできるInvokerBuilderFactory
 * 
 * @author ms2
 */
public class PatternInvokerBuilderFactory extends AbstractInvokerBuilderFactory {

	private static final Logger logger = Logger
			.getLogger(PatternInvokerBuilderFactory.class.getName());

	private ExtensionMultimapFactory exPolicy;
	private UrlParserArranger parserArranger;
	private String rootPackage;

	public PatternInvokerBuilderFactory(String rootPackage,
			UrlParserArranger parserArranger, ExtensionMultimapFactory exPolicy) {

		this.exPolicy = exPolicy;
		this.parserArranger = parserArranger;
		this.rootPackage = rootPackage;
	}

	protected InvokerBuilder createInvokerBuilder() {
		logger.log(Level.FINE, "create");

		PatternInvokerBuilder invokerBuilder = new PatternInvokerBuilder(exPolicy);
		invokerBuilder.setRootPackage(rootPackage);
		parserArranger.arrange(invokerBuilder);
		return invokerBuilder;
	}

}
