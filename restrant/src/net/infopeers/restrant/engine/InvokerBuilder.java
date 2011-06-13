package net.infopeers.restrant.engine;

import javax.servlet.http.HttpServletRequest;

import net.infopeers.restrant.ControllerServlet;
import net.infopeers.restrant.ResourceNotFoundException;

public interface InvokerBuilder {

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
	Invoker build(ControllerServlet servlet, HttpServletRequest req)
			throws ResourceNotFoundException;

}