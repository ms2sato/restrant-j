package net.infopeers.web.taglib;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;

import net.infopeers.util.web.WebUtils;

public class UrlForTag implements Tag {
	private PageContext pageContext;
	private Tag parentTag;
	private String path;

	@Override
	public int doEndTag() throws JspException {
		return 0;
	}

	@Override
	public int doStartTag() throws JspException {
		try {
			JspWriter out = pageContext.getOut();
			HttpServletRequest req = (HttpServletRequest) pageContext
					.getRequest();
			out.print(WebUtils.urlFor(req, path));
			return SKIP_BODY;
		} catch (IOException e) {
			throw new JspException(e);
		}
	}

	@Override
	public Tag getParent() {
		return parentTag;
	}

	@Override
	public void release() {
	}

	@Override
	public void setPageContext(PageContext ctx) {
		this.pageContext = ctx;
	}

	@Override
	public void setParent(Tag parent) {
		this.parentTag = parent;
	}

	public void setPath(String path) {
		this.path = path;
	}
}
