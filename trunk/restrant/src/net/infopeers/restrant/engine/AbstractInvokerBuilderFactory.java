package net.infopeers.restrant.engine;

public abstract class AbstractInvokerBuilderFactory implements
		InvokerBuilderFactory {

	private InvokerBuilder invokerBuilder;
	private boolean isEvery = false;

	public AbstractInvokerBuilderFactory() {
		super();
	}

	@Override
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

	protected abstract InvokerBuilder createInvokerBuilder();

}