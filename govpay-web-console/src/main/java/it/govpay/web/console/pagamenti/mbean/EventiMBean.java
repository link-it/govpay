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
package it.govpay.web.console.pagamenti.mbean;

import it.govpay.ejb.ndp.model.EventiInterfacciaModel.Categoria;
import it.govpay.web.console.GovPayWebConsoleConversationManager;
import it.govpay.web.console.pagamenti.bean.EventoBean;
import it.govpay.web.console.pagamenti.form.EventiSearchForm;
import it.govpay.web.console.pagamenti.iservice.IEventiService;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.web.form.CostantiForm;
import org.openspcoop2.generic_project.web.impl.jsf1.input.impl.SelectListImpl;
import org.openspcoop2.generic_project.web.impl.jsf1.mbean.BaseMBean;
import org.openspcoop2.generic_project.web.iservice.IBaseService;


@Named("eventiMBean") @ConversationScoped
public class EventiMBean extends BaseMBean<EventoBean,Long, EventiSearchForm> implements Serializable{
	
	@Inject @Named("govpayConversationManager")
	GovPayWebConsoleConversationManager conversationManager;
	
	@Inject
	Conversation conversation;
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L ;

	private List<SelectItem> elencoCategorieEvento = null;
	
	private List<SelectItem> elencoTipiEvento = null;
	
	private List<SelectItem> elencoSottotipiEvento = null;
	
	private String tipoExport = null;
	
	private Long selectedId = null;
	
	public EventiMBean (){
		super(org.apache.log4j.Logger.getLogger(EventiMBean.class)); 
	}
	
	@PostConstruct
	private void _EventiMBean(){
		log.debug("Init EventiMBean completato."); 
	}
	
	@Override @Inject @Named("eventiService")
	public void setService(
			IBaseService<EventoBean, Long, EventiSearchForm> service) { 
		super.setService(service);
	}
		
	@Override @Inject @Named("eventiSearchForm")
	public void setSearch(EventiSearchForm search) {
		this.search = search;
		this.selectedElement = null;
		this.selectedId = null;
	}
	
	public String filtra(){
		return "listaEventi?faces-redirect=true";
	}
	
	public String menuAction(){
		this.conversationManager.startConversation(GovPayWebConsoleConversationManager.GDE_CID, this.conversation);
		
		((SelectListImpl) this.search.getTipo()).setElencoSelectItems(this.getElencoTipiEvento());
		((SelectListImpl) this.search.getCategoria()).setElencoSelectItems(this.getElencoCategorieEvento());
		((SelectListImpl) this.search.getSottoTipo()).setElencoSelectItems(this.getElencoSottotipiEvento()); 
		
		this.search.reset();
		((EventiSearchForm)this.search).setFiltroSet("true");
		
		return "listaEventi";
	}
	
	public String restoreSearch(){
		this.search.setRestoreSearch(true); 
		((EventiSearchForm)this.search).setFiltroSet("true");
		
		return "listaEventi";
	}
	
	
	public String resetFiltro(){
		this.search.reset();
		((EventiSearchForm)this.search).setFiltroSet("true");
		
		return "listaEventi?faces-redirect=true";
	}
	
	
	public String exportSelected() {
		try {

			// recupero lista diagnostici
			List<Long> idEventi = new ArrayList<Long>();

			// se nn sono in select all allore prendo solo quelle selezionate
			if (!this.isSelectedAll()) {
				Iterator<EventoBean> it = this.selectedIds.keySet().iterator();
				while (it.hasNext()) {
					EventoBean evtBean = it.next();
					if (this.selectedIds.get(evtBean).booleanValue()) {
						idEventi.add(evtBean.getDTO().getId());
						it.remove();
					}
				}
			}

			// We must get first our context
			FacesContext context = FacesContext.getCurrentInstance();

			// Then we have to get the Response where to write our file
			HttpServletResponse response = (HttpServletResponse) context
					.getExternalContext().getResponse();

			response.sendRedirect(context.getExternalContext()
					.getRequestContextPath()
					+ "/eventiexporter?isAll="
					+ this.isSelectedAll()
					+ "&ids="
					+ StringUtils.join(idEventi, ",")
					+ "&formato="+ this.tipoExport);

			context.responseComplete();

			// End of the method
		} catch (Exception e) {
			FacesContext.getCurrentInstance().responseComplete();
			log.error(e, e);
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Si e' verificato un errore durante l'esportazione degli eventi selezionati.",null));
		}

		return null;
	}
	
	
	
	public List<SelectItem> getElencoCategorieEvento() {
		if(this.elencoCategorieEvento == null){
		
			this.elencoCategorieEvento = new ArrayList<SelectItem>();
			
			this.elencoCategorieEvento.add(new SelectItem(
					 new  org.openspcoop2.generic_project.web.impl.jsf1.input.SelectItem(
							 CostantiForm.ALL,  ("commons.label.qualsiasi"))));

			this.elencoCategorieEvento.add(new SelectItem(
					new  org.openspcoop2.generic_project.web.impl.jsf1.input.SelectItem(
							Categoria.INTERFACCIA.toString(), "eventi.search.categoria.interfaccia")));
			
		}
		return elencoCategorieEvento;
	}
	
	public void setElencoCategorieEvento(List<SelectItem> elencoCategorieEvento) {
		this.elencoCategorieEvento = elencoCategorieEvento;
	}
	
	public List<SelectItem> getElencoTipiEvento() {
		if(this.elencoTipiEvento == null){
			this.elencoTipiEvento = new ArrayList<SelectItem>();
			
			this.elencoTipiEvento.add(new SelectItem(
					 new  org.openspcoop2.generic_project.web.impl.jsf1.input.SelectItem(
							 CostantiForm.ALL,  ("commons.label.qualsiasi"))));
			
			
			List<String> valFromDb = ((IEventiService)this.service).getElencoTipiEvento();
			
			for (String tipo : valFromDb) {
				this.elencoTipiEvento.add(new SelectItem(tipo,tipo));
			}
		}
		return elencoTipiEvento;
	}
	
	public void setElencoTipiEvento(List<SelectItem> elencoTipiEvento) {
		this.elencoTipiEvento = elencoTipiEvento;
	}
	
	public List<SelectItem> getElencoSottotipiEvento() {
		if(this.elencoSottotipiEvento == null){
			this.elencoSottotipiEvento = new ArrayList<SelectItem>();
			
			this.elencoSottotipiEvento.add(new SelectItem(
					 new  org.openspcoop2.generic_project.web.impl.jsf1.input.SelectItem(
							 CostantiForm.ALL,  ("commons.label.qualsiasi"))));
			
			//[TODO] aggiungere i valori leggendoli dal db
			List<String> valFromDb = ((IEventiService)this.service).getElencoSottoTipiEvento();
			
			for (String sotTipo : valFromDb) {
				this.elencoSottotipiEvento.add(new SelectItem(sotTipo,sotTipo));
				
			}
		}
		
		return elencoSottotipiEvento;
	}
	public void setElencoSottotipiEvento(List<SelectItem> elencoSottotipiEvento) {
		this.elencoSottotipiEvento = elencoSottotipiEvento;
	}
	
	@Override
	public void setSelectedElement(EventoBean selectedElement) {
		this.selectedElement = selectedElement;
		
		log.debug("Selezionato Evento ["+ this.selectedElement +"]"); 
		
		if(this.selectedElement != null && this.selectedElement.getDTO() != null
				&& this.selectedElement.getDTO().getCategoria() != null ){
			this.selectedElement.setInfospcoop(((IEventiService)this.service).getInfospcoopByIdEgov(this.selectedElement.getDTO().getIdEgov()));
		}
	}
	
	public String getTipoExport() {
		return tipoExport;
	}

	public void setTipoExport(String tipoExport) {
		this.tipoExport = tipoExport;
	}

	public Long getSelectedId() {
		return selectedId;
	}

	public void setSelectedId(Long selectedId) {
		this.log.debug("Selected Id ["+selectedId+"]");
				
		this.selectedId = selectedId;
		
		if(selectedId!= null){
			try {
				EventoBean findById = this.service.findById(selectedId);
				
				this.setSelectedElement(findById); 
			} catch (ServiceException e) {
				log.error("Errore durante la lettura dell'evento con Id ["+this.selectedId+"]: "+ e.getMessage(), e); 
			}
		}
	}
	
}