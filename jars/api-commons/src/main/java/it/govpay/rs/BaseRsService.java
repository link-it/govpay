/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.rs;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Path;
//import javax.ws.rs.OPTIONS;
//import javax.ws.rs.Path;
//import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.openspcoop2.utils.service.context.IContext;


public abstract class BaseRsService {
	
	public static final String ERRORE_INTERNO = "Errore Interno";

	@Context protected HttpServletRequest request;
	@Context protected HttpServletResponse response;
	@Context protected UriInfo uriInfo;

	protected String nomeServizio;
	protected Logger log;

	protected String codOperazione;

	public BaseRsService() throws ServiceException{
		this.log = LoggerWrapperFactory.getLogger(BaseRsService.class);
	}

	public BaseRsService(String nomeServizio) throws ServiceException{
		this();
		this.nomeServizio = nomeServizio;

	}

	public void setHttpServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	protected Authentication getUser() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	protected Response getUnauthorizedResponse(){
		Response res =	Response.status(Response.Status.UNAUTHORIZED)
				.header("Access-Control-Allow-Origin", "*")
				.build();

		return res;
	}

	protected Response getForbiddenResponse(){
		Response res =	Response.status(Response.Status.FORBIDDEN)
				.header("Access-Control-Allow-Origin", "*")
				.build();

		return res;
	}

	public void invalidateSession(Logger log){
		if(log!= null)
			log.info("Invalidate Session in corso...");

		HttpSession session = this.request.getSession(false);
		if(session != null){
			session.invalidate();
		}

		if(log!= null)
			log.info("Invalidate Session completata.");
	}

	public static boolean isEmpty(List<?> lista){
		if(lista == null)
			return true;

		return lista.isEmpty();
	}

	public abstract int getVersione();
	
	protected synchronized IContext getContext() {
		IContext context = ContextThreadLocal.get();
		if(context instanceof org.openspcoop2.utils.service.context.Context) {
			((org.openspcoop2.utils.service.context.Context)context).update(this.request, this.response, this.uriInfo, 2, this.log);
			((org.openspcoop2.utils.service.context.Context)context).setRestPath(this.getPathFromRestMethod(context.getMethodName()));
		}
		return context;
	}
	
	private String getPathFromRestMethod(String methodName) {

        try {
        	Class<?> c = this.getClass();
        	Class<?> [] interfaces = c.getInterfaces();
        	if(interfaces==null || interfaces.length<=0) {
        		return null;
        	}
        	Class<?> cInterface = null;
        	for (int i = 0; i < interfaces.length; i++) {
        		if (interfaces[i] != null && interfaces[i].isAnnotationPresent(Path.class)) {
        			cInterface = interfaces[i];
        			break;
        		}
			}
        	if(cInterface==null) {
        		return null;
        	}
        	Method [] methods = cInterface.getMethods();
        	if(methods==null || methods.length<=0) {
        		return null;
        	}
        	Method method = null;
        	for (int i = 0; i < methods.length; i++) {
        		if (methods[i] != null && methods[i].getName().equals(methodName) && methods[i].isAnnotationPresent(Path.class)) {
        			method = methods[i];
        			break;
        		}
			}
        	if(method==null) {
        		return null;
        	}
        	Path path = method.getAnnotation(Path.class);
        	if(path==null) {
        		return null;
        	}
        	return path.value();
        } catch (Exception e) {
            this.log.error(e.getMessage(),e);
        }

        return null;
    }
}

