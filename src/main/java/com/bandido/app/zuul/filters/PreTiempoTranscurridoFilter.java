package com.bandido.app.zuul.filters;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;

@Component
public class PreTiempoTranscurridoFilter extends ZuulFilter{

	private static Logger log = LoggerFactory.getLogger(PreTiempoTranscurridoFilter.class);
	
	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() throws ZuulException {
		RequestContext ctx = RequestContext.getCurrentContext();  
		HttpServletRequest request = ctx.getRequest();
		Long tiempoInicio = System.currentTimeMillis();
		request.setAttribute("tiempoInicio", tiempoInicio);
		
        StringBuilder strLog = new StringBuilder();
        strLog.append("\n------ NUEVA PETICION ------\n");                     
        strLog.append(String.format("Server: %s Metodo: %s Path: %s \n",ctx.getRequest().getServerName()                
                   ,ctx.getRequest().getMethod(),
                   ctx.getRequest().getRequestURI()));
        Enumeration<String> enume= ctx.getRequest().getHeaderNames();
        String header;
        while (enume.hasMoreElements())
        {
            header=enume.nextElement();
            strLog.append(String.format("Headers: %s = %s \n",header,ctx.getRequest().getHeader(header)));                 
        };         
        log.info(strLog.toString());
        
		return null;
	}

	@Override
	public String filterType() {
		return FilterConstants.PRE_TYPE;
	}

	@Override
	public int filterOrder() {
		return FilterConstants.DEBUG_FILTER_ORDER;
	}

}
