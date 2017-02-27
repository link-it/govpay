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
			this.initLogger("logout", resp);
			this.log.debug("Logout in corso...");
			
			String msg = req.getParameter("msg");
			
			HttpSession session = req.getSession(false);
			if(session != null)
				session.invalidate();
			String location = req.getContextPath() + (msg != null ? ("?msg="+msg):"")	;//+ "/public/login.html";
			resp.sendRedirect(location);
		}catch(Exception e){
			this.log.error("Si e' verificato un errore durante il logout: "+e.getMessage(),e);
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		super.doGet(req, resp);
	}

}
