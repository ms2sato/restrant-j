package net.infopeers.restrant;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 基本的な機能を実装したController
 * 
 * @author ms2
 * 
 * @see net.infopeers.restrant.Controller
 */
public class BasicController implements Controller {

	private HttpServletRequest request;
	private HttpServletResponse response;
	private Params params;
	private ControllerServlet servlet;

	protected ControllerServlet getServlet(){
		return servlet;
	}
	
	protected HttpServletRequest getRequest() {
		return request;
	}

	protected HttpServletResponse getResponse() {
		return response;
	}

	protected Params getParams() {
		return params;
	}
	
	/**
	 * フォワードする
	 * 
	 * @param name
	 *            フォワード先servlet-name
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void forward(String name) throws ServletException, IOException{
		RequestDispatcher rd = getServlet().getServletContext().getNamedDispatcher(name);
		rd.forward(getRequest(), getResponse());
	}

	/**
	 * フォワードする
	 * 
	 * @param to
	 *            フォワード先
	 * @throws ServletException
	 * @throws IOException
	 */
	protected void forwardTo(String to) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher(to);
		dispatcher.forward(request, response);
	}

	/**
	 * リダイレクトする
	 * 
	 * @param to
	 *            リダイレクト先
	 * @throws IOException
	 */
	protected void redirectTo(String to) throws IOException {
		response.sendRedirect(to);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenet.infopeers.restrant.Controller#setRequest(javax.servlet.http.
	 * HttpServletRequest)
	 */
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seenet.infopeers.restrant.Controller#setResponse(javax.servlet.http.
	 * HttpServletResponse)
	 */
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * net.infopeers.restrant.Controller#setParams(net.infopeers.restrant.Params
	 * )
	 */
	public void setParams(Params params) {
		this.params = params;
	}
	
	

	@Override
	public void afterInvoke(String method) throws Exception {
	}

	@Override
	public boolean beforeInvoke(String method) throws Exception {
		return true;
	}

	@Override
	public void setServlet(ControllerServlet servlet) {
		this.servlet = servlet;
	}
}
