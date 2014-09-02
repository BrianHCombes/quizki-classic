package com.haxwell.apps.questions.events;

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

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.haxwell.apps.questions.events.handlers.IAttributeEventHandler;
import com.haxwell.apps.questions.events.handlers.IDynamicAttributeEventHandler;
import com.haxwell.apps.questions.events.handlers.IObjectEventHandler;
import com.haxwell.apps.questions.events.utils.AttributeEventHandlerList;
import com.haxwell.apps.questions.events.utils.DynamicAttributeEventHandlerBean;
import com.haxwell.apps.questions.events.utils.DynamicAttributeEventHandlerList;
import com.haxwell.apps.questions.events.utils.ObjectEventHandlerList;

/**
 * HOW EVENTS WORK IN QUIZKI:
 * 	So, something needs to be done when something else happens. We define a handler to do that thing.
 * 	The handler is a type of IEventHandler. Define the handler as a bean in Spring. That handler is associated with an
 * 	actual event in another bean. This bean, is an AttributeEventHandlerBean, and it is also defined in Spring. The 
 * 	something that needs to be done, at press time, is resetting an attribute in the session to null, once an event 
 * 	happens. So, its been designed for that case. It should likely be expanded for other cases. But anyway, here we go.
 * 
 *  On AttributeEventHandlerBean, the means of associating an event with its handler,
 * 
 * 	We set
 * 		- an Attribute (attr), the key, which says execute this handler, when this event happens, for this attribute.
 * 		- an Event, a key defined in EventConstants, to identify a unique event
 * 		- a Handler, an IEventHandler object which is called when the associated event is fired.
 * 
 *   This AttributeEventHandlerBean is set on the AttributeEventHandlerList.
 *   
 *   The AEHL sets the AEHBean in a map with the key as the attribute, and the bean itself as the value. The beans are
 *   only collected here, but if the event were to happen, no handlers would be called. To make the handlers active,
 *   an HttpSessionAttributeListener is used. This listener catches each setting of an attribute on the session. It
 *   notices the Attribute being set, and calls the AEHL to say, activate the events associated with this attribute.
 *   At that point, the AEHL gets the list of handlers associated with attribute, and adds each of those handlers to
 *   a list of handlers associated with the event name.
 *   
 *   When the event happens, the EventDispatcher is called on to tell all the interested parties, "Hey, this event just
 *   happened". It does this via the fireEvent() method. FireEvent() calls the AEHL, and gets the list of handlers
 *   associated with the event. It then executes each handler in the list. At this point, AEHL no longer has handlers 
 *   associated with that event. That attribute has to be set in the session again in order to have the handlers made 
 *   active for that event again.
 * 
 * @author johnathanj
 *
 */
public class EventDispatcher {

	private static Logger log = LogManager.getLogger();
	
	protected static EventDispatcher instance;
	
	protected EventDispatcher() { }
	
	public static EventDispatcher getInstance() {
		if (instance == null)
			instance = new EventDispatcher();
		
		return instance;
	}

	public void fireEvent(HttpServletRequest req, String eventName) {
		handleAttributeEventHandlerList(req, eventName);
		handleDynamicEventHandlerList(req, eventName);
	}
	
	private void handleAttributeEventHandlerList(HttpServletRequest req, String eventName) {
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(req.getSession().getServletContext());
		AttributeEventHandlerList aehl = (AttributeEventHandlerList)ctx.getBean("attributeEventHandlerList");
		
		List<IAttributeEventHandler> list = aehl.getEventHandlerList(eventName);
		
		log.trace("EventDisptacher: called to fire the event (" + eventName + ")");
		
		if (list != null) {
			for (IAttributeEventHandler handler : list) {
				
				log.trace("EventDispatcher: calling the handler (" + handler.toString() + ")");
				
				handler.execute(req);
			}
		}
		else
			log.trace("No active event handlers found associated with '" + eventName + "'");
	}
	
	private void handleDynamicEventHandlerList(HttpServletRequest req, String eventName) {
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(req.getSession().getServletContext());
		DynamicAttributeEventHandlerList daehl = (DynamicAttributeEventHandlerList)ctx.getBean("dynamicAttributeEventHandlerList");
		
		List<DynamicAttributeEventHandlerBean> list = daehl.getBeansByEventName(eventName);
		
		if (list != null) {
			for (DynamicAttributeEventHandlerBean bean : list) {
				
				IDynamicAttributeEventHandler handler = bean.getEventHandler();
				handler.execute(bean.getAttribute_endsWith(), req.getSession());
			}
		}
		else
			log.trace("(dynamic) No active event handlers found associated with '" + eventName + "'");
	}
	
	public void fireEvent(HttpServletRequest req, String eventName, Object o) {
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(req.getSession().getServletContext());
		ObjectEventHandlerList oehl = (ObjectEventHandlerList)ctx.getBean("objectEventHandlerList");
		
		List<IObjectEventHandler> list = oehl.getEventHandlerList(eventName);
		
		log.trace("EventDisptacher: called to fire the event (" + eventName + ")");
		
		if (list != null) {
			for (IObjectEventHandler handler : list) {
				
				log.trace("EventDispatcher: calling the handler (" + handler.toString() + ")");
				
				handler.execute(req, o);
			}
		}
		else
			log.trace("No event handlers found associated with '" + eventName + "'");
	}
	
}
