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
package it.govpay.web.console.pagamenti.datamodel;

import it.govpay.web.console.pagamenti.bean.EventoBean;
import it.govpay.web.console.pagamenti.iservice.IEventiService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.ajax4jsf.model.DataVisitor;
import org.ajax4jsf.model.Range;
import org.ajax4jsf.model.SequenceRange;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.web.datamodel.BaseDataModel;


@Named("eventiDM") @RequestScoped
public class EventiDM extends BaseDataModel<Long, EventoBean, IEventiService>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3498902673441284275L;

	@Inject  
    private transient Logger log;
	
	@Override @Inject @Named("eventiService")
	public void setDataProvider(IEventiService dataProvider) {
			super.setDataProvider(dataProvider);
			this.log.debug("SetProvider");
	}
	
	@Override
	public void walk(FacesContext context, DataVisitor visitor, Range range,
			Object argument) throws IOException {
		try{
			if(detached || this.wrappedKeys != null){
				for (Long  key : this.wrappedKeys) {
					setRowKey(key);
					visitor.process(context, key, argument);
				}
			}else{
				int start = 0; int limit = 0;
				// ripristino la ricerca.
				if(this.getDataProvider().getForm().isRestoreSearch()){
					start = this.getDataProvider().getForm().getStart();
					limit = this.getDataProvider().getForm().getLimit();
					this.getDataProvider().getForm().setRestoreSearch(false);
					
					int pageIndex = (start / limit) + 1;
					this.getDataProvider().getForm().setPageIndex(pageIndex);
					this.getDataProvider().getForm().setCurrentPage(pageIndex);
					// Aggiorno valori paginazione
					range = new SequenceRange(start,limit);
				}
				else{
					  start = ((SequenceRange)range).getFirstRow();
					  limit = ((SequenceRange)range).getRows();
				}
				
				log.debug("Richiesti Record S["+start+"] L["+limit+"], FiltroPagina ["+this.getDataProvider().getForm().getPageIndex()+"]"); 

				this.getDataProvider().getForm().setStart(start);
				this.getDataProvider().getForm().setLimit(limit); 
				
				this.wrappedKeys = new ArrayList<Long>();
				List<EventoBean> list = this.getDataProvider().findAll(start, limit);
				for (EventoBean evento : list) {
					Long id = evento.getDTO().getId();

					this.wrappedData.put(id, evento);
					this.wrappedKeys.add(id);
					visitor.process(context, id, argument);
				}
			}
		}catch (Exception e) {
			log.error(e,e);
		}
	}

	@Override
	public int getRowCount() {
		if(this.rowCount==null){
			if(this.getDataProvider().getForm().isNewSearch()){
				try {
					this.rowCount = this.getDataProvider().totalCount();
					this.getDataProvider().getForm().setTotalCount(this.rowCount);
				} catch (ServiceException e) {
					this.getDataProvider().getForm().setTotalCount(0);
					return 0;
				}
				this.getDataProvider().getForm().setNewSearch(false); 
			}
			else {
				this.rowCount = this.getDataProvider().getForm().getTotalCount();
			}
		}
		return this.rowCount;
	}

	@Override
	public Object getRowData() {
		if(this.currentPk==null)
			return null;
		else{
			EventoBean t = this.wrappedData.get(this.currentPk);
			if(t==null){
				try {
					t=this.getDataProvider().findById(this.currentPk);
					this.wrappedData.put(this.currentPk, t);
				} catch (ServiceException e) {}
			}
			return t;
		}
	}
}
