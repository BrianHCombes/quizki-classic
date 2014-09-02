package com.haxwell.apps.questions.servlets.filters;

/**
 * Copyright 2013,2014 Johnathan E. James - haxwell.org - jj-ccs.com - quizki.com
 *
 * This file is part of Quizki.
 *
 * Quizki is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Quizki is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Quizki. If not, see http://www.gnu.org/licenses.
 */

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Sets attributes on the HTTP header saying "Do not cache anything!!"
 * 
 * This is so that back button presses will come back to the server, allowing it to update Session settings.
 * 
 */
@WebFilter("/SetDoNotCacheAttributesFilter")
public class SetDoNotCacheAttributesFilter extends AbstractFilter {

    public SetDoNotCacheAttributesFilter() { /* do nothing */ }

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		if (request instanceof HttpServletRequest) {
			HttpServletResponse resp = ((HttpServletResponse)response);
			
			resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
			resp.setHeader("Pragma", "no-cache"); // HTTP 1.0
			resp.setDateHeader("Expires", 0); // Proxies.
		}
		
		// pass the request along the filter chain
		chain.doFilter(request, response);
	}
}
