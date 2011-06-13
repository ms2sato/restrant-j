package net.infopeers.restrant.engine;

/**
 * InvokerBuilderの作成器。
 * @author ms2
 */
public interface InvokerBuilderFactory {

	InvokerBuilder getInvokerBuilder();

	public abstract void setEvery(boolean value);
}
