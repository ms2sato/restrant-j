package net.infopeers.restrant.engine.jsservice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.infopeers.restrant.ControllerServlet;
import net.infopeers.restrant.ResourceNotFoundException;
import net.infopeers.restrant.engine.Invoker;
import net.infopeers.restrant.engine.InvokerBuilder;

class InvokerBuilderImpl implements InvokerBuilder {

	private String response;

	InvokerBuilderImpl(String response) {
		this.response = response;
	}

	@Override
	public Invoker build(ControllerServlet servlet, HttpServletRequest req)
			throws ResourceNotFoundException {

		return new Invoker() {

			@Override
			public void invoke(HttpServletRequest req,
					HttpServletResponse resp) throws Exception {
				resp.getWriter().append(response);
			}

		};
	}

}