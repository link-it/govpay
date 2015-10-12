package it.govpay.web.console.listener;

import java.io.Serializable;

import javax.enterprise.context.spi.Context;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;
import org.jboss.weld.Container;
import org.jboss.weld.context.ManagedConversation;
import org.jboss.weld.context.http.HttpConversationContext;

public class WeldListener implements  ServletRequestListener, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 

	public static final String NO_CID = "nocid";

	@Inject  
	Logger log;

	@Override
	public void requestDestroyed(ServletRequestEvent sre) {
		// TODO Auto-generated method stub

	}

	@Override
	public void requestInitialized(ServletRequestEvent event) {
		if(hasConversationContext(event)){
			String cid = getConversationId(event);

			HttpConversationContext conversationContext = instance().select(HttpConversationContext.class).get();

			ManagedConversation conversation = conversationContext.getConversation(cid);


			if(conversation == null){
				try{
					conversation = conversationContext.getCurrentConversation();
				}
				catch(IllegalStateException ie){
					conversation = null;
				}
			}

			if(conversation != null){

				if(!conversationContext.isActive()) {
					conversationContext.activate(cid);
				}

				log.debug("Conversation Status: Expired ["+isExpired(conversation)+"]");

				conversation.touch();

				//				conversationContext.invalidate();
				if(conversationContext.isActive()) {
					// Only deactivate the context if one is already active, otherwise we get Exceptions
					conversationContext.deactivate();
				}
			}

		}

	}

	private String getConversationId(ServletRequestEvent event) {
		return event.getServletRequest().getParameter("cid");
	}

	private boolean hasConversationContext(ServletRequestEvent event) {
		return StringUtils.isNotEmpty(getConversationId(event));
	}

	private static Instance<Context> instance() {
		return Container.instance().deploymentManager().instance().select(Context.class);
	}

	private static boolean isExpired(ManagedConversation conversation) {
		return System.currentTimeMillis() > (conversation.getLastUsed() + conversation.getTimeout());
	}
}
