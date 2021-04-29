package com.bandido.app.zuul.filters;

import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;


@Component
public class PreValidatedHeadersFilter extends ZuulFilter {
	
	private static Logger log = LoggerFactory.getLogger(PreValidatedHeadersFilter.class);

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();
		String uriPath = "/app/documents/listarPageable";
		String pass = "Basic dXNlcjo2MjZjOTRhMC1mN2I3LTQ5YTQtOTljNi1mYmUyNjAwN2FmYzE=";
		
		StringBuilder strLog = new StringBuilder();
		strLog.append("\n------ FILTRANDO ACCESO A listarPageable - PRE FILTER ------\n");

			String usuario = ctx.getRequest().getHeader("authorization") == null ? "" : ctx.getRequest().getHeader("authorization");
			boolean isUriPath = ctx.getRequest().getRequestURI().equals(uriPath);

			if (!usuario.equals("")) {
				if (!usuario.equals(pass) && isUriPath) {
					String msgError = "Password de Usuario invalida";
					String msgErrorNext = String.format("Para URI-PATH %s", uriPath);
					strLog.append("\n" + msgError + "\n");
					strLog.append(msgErrorNext + "\n");
					ctx.setResponseBody(strLog.toString());
					ctx.setResponseStatusCode(HttpStatus.SC_FORBIDDEN);
					ctx.setSendZuulResponse(false);
					log.info(strLog.toString());
					return null;
				}
			}

		log.info(strLog.toString());
		return null;
	}

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return FilterConstants.DEBUG_FILTER_ORDER+1;
	}

}
