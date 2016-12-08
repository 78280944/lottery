package com.lottery.api.filter;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.lottery.orm.util.MessageTool;

public class RestAuthFilter implements Filter {

   /* private ApiUserService apiUserService;

    public ApiUserService getApiUserService() {
	return apiUserService;
    }

    public void setApiUserService(ApiUserService apiUserService) {
	this.apiUserService = apiUserService;
    }*/

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
	ServletContext context = filterConfig.getServletContext();
	ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);
	//apiUserService = (ApiUserService) ctx.getBean("apiUserService");
    }

    /**
     * (non-Javadoc)
     * 
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     *      javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
	    throws IOException, ServletException {
	HttpServletRequest httpRequest = (HttpServletRequest) request;
	RestHttpServletRequest restRequest = new RestHttpServletRequest(httpRequest);
	boolean result = false;
	int errorCode = MessageTool.FailCode;

	String requestURI = httpRequest.getRequestURI();
	if (requestURI.contains("/error/")) {
	    chain.doFilter(restRequest, response);
	    return;
	}
	try {

	    InputStream is = null;
	    String contentStr = "";
	    is = restRequest.getInputStream();
	    contentStr = IOUtils.toString(is, "utf-8");
	    System.out.println(contentStr);
	    result = true;
	    /*JSONObject jObj = new JSONObject(contentStr);
	    String remoteIP = jObj.getString("remoteIP");
	    String apiToken = jObj.getString("apiToken");
	    is.close();
	    if (apiToken != null && !"".equals(apiToken)) {
		Des3Util des3Util = new Des3Util();
		if (des3Util.decode(apiToken).equals(remoteIP)) {
		    result = true;
		} else {
		    errorCode = 1001;
		}
	    } else {
		errorCode = 1002;
	    }*/

	} catch (Exception e) {
	    e.printStackTrace();
	}

	if (result) {
	    chain.doFilter(restRequest, response);
	    return;
	} else {
	    restRequest.getRequestDispatcher("/lottery/error/" + errorCode).forward(restRequest, response);
	}
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void destroy() {

    }

}
