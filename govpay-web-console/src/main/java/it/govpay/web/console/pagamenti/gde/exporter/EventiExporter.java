/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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
package it.govpay.web.console.pagamenti.gde.exporter;

import it.govpay.web.console.pagamenti.iservice.IEventiService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;

/****
 * 
 * export, aggiunti header negli xml esportati transazioni.export.enableHeaderInfo=true
 * Se il mapping per un determinato mime type non viene trovato
 * viene lanciata una eccezione se questa proprieta e' a true
 * altrimenti viene restituito 'bin' durante la fase di export/download 
 * transazioni.download.mime.throwExceptionIfMappingNotFound=false
 * 
 * @author pintori
 *
 */
public class EventiExporter extends HttpServlet{


	private static Boolean enableHeaderInfo = false;
	private static Boolean mimeThrowExceptionIfNotFound = false;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1314905657194691373L;
	@Inject
	private Logger log;

	@Inject @Named("eventiService")
	private IEventiService service; 
	
	@Override
	public void init() throws ServletException {
		super.init();
		
		log.debug("Eventi Exporter Start...");
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		this.processRequest(req,resp);		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		this.processRequest(req,resp);		
	}


	private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException{
		try{

//			ApplicationContext context = WebApplicationContextUtils.getWebApplicationContext(getServletContext());

//			  service = (IEventiService)context.getBean("eventiService");

//			EventiSearchForm sfInSession = 	(EventiSearchForm)context.getBean("searchFormEventi");
//			EventiSearchForm searchForm = (EventiSearchForm)sfInSession.clone();
//			service.setForm(searchForm);

			// Then we have to get the Response where to write our file
			HttpServletResponse response = resp;

			String isAllString = req.getParameter("isAll");
			Boolean isAll = Boolean.parseBoolean(isAllString);
			String idtransazioni=req.getParameter("ids");
			String[] ids = StringUtils.split(idtransazioni, ",");
			String formato=req.getParameter("formato");

			String fileName = "Eventi.zip";

			response.setContentType("x-download");					
			response.setHeader("Content-Disposition", "attachment; filename="+fileName);
			response.addHeader("Cache-Control", "no-cache");
			response.setStatus(200);
			response.setBufferSize(1024);
			// committing status and headers
			response.flushBuffer();

			ExporterProperties prop = new ExporterProperties();
			prop.setEnableHeaderInfo(EventiExporter.enableHeaderInfo);
			prop.setFormatoExport(formato);
			prop.setMimeThrowExceptionIfNotFound(EventiExporter.mimeThrowExceptionIfNotFound);

			SingleFileExporter sfe = new SingleFileExporter(response.getOutputStream(), prop, service);

			if(isAll){
				//transazioni = service.findAll(start, limit);
				sfe.export();
			}else{
				List<String> idEvento = new ArrayList<String>();
				for (int j = 0; j < ids.length; j++) {
					idEvento.add(ids[j]);					
				}
				sfe.export(idEvento);
			}		



		}catch(IOException se){
			this.log.error(se,se);
			throw se;
		} catch(Exception e){
			this.log.error(e,e);
			throw new ServletException(e);
		}
	}
}
