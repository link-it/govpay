package it.govpay.web.servlet;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;

public class LogoutServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Logger log = LogManager.getLogger();

	protected void initLogger(String cmd,HttpServletResponse response) {
		String codOperazione = UUID.randomUUID().toString();
		ThreadContext.put("cmd", cmd);
		ThreadContext.put("op",  codOperazione);
		response.setHeader("X-GP-CMDID", cmd);
	}


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		try{
			HttpSession session = req.getSession(false);
//			boolean doRedirect = false;
//			String logout = req.getParameter("logout");

			// chiamata di logout
//			if(logout != null){
				initLogger("logout", resp);
				log.debug("Logout in corso...");
				session.invalidate();
//				doRedirect = true;
//			} 
			
//			else {
//				initLogger("checkSession", resp);
//				log.debug("Controllo della sessione in corso...");
//				// controllo se la sessione e' valida.
//				if (req.getRequestedSessionId() != null
//						&& !req.isRequestedSessionIdValid()) {
//					// Session is expired
//					doRedirect = true;
//					log.debug("Controllo sessione completato, sessione non valida.");
//				} else {
//					log.debug("Controllo sessione completato, la sessione e' valida.");
//				}
//			}

//			if(doRedirect){
				String location = req.getContextPath() ;//+ "/public/login.html";
//				log.debug("Effetto redirect alla location: " + location);
				resp.sendRedirect(location);
//			}else 
//			{
//				resp.setStatus(200);
//				resp.getOutputStream().write("".getBytes()); 
//			}
		}catch(Exception e){

		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
	}

}
