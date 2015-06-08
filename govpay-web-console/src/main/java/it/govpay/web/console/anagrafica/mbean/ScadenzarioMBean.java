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
package it.govpay.web.console.anagrafica.mbean;

import it.govpay.ejb.core.model.ConnettoreModel.EnumAuthType;
import it.govpay.ejb.core.model.ConnettoreModel.EnumSslType;
import it.govpay.ejb.core.model.ScadenzarioModelId;
import it.govpay.ejb.ndp.model.DominioEnteModel;
import it.govpay.web.console.anagrafica.bean.EnteBean;
import it.govpay.web.console.anagrafica.bean.IntermediarioNdpBean;
import it.govpay.web.console.anagrafica.bean.ScadenzarioBean;
import it.govpay.web.console.anagrafica.bean.StazioneBean;
import it.govpay.web.console.anagrafica.form.ScadenzarioForm;
import it.govpay.web.console.anagrafica.form.ScadenzarioSearchForm;
import it.govpay.web.console.anagrafica.iservice.IEnteService;
import it.govpay.web.console.anagrafica.iservice.IIntermediarioNdpService;
import it.govpay.web.console.anagrafica.iservice.IScadenzarioService;
import it.govpay.web.console.utils.Utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;

import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.web.core.MessageUtils;
import org.openspcoop2.generic_project.web.form.CostantiForm;
import org.openspcoop2.generic_project.web.mbean.BaseMBean;

//@Named("scadenzarioMBean") @ConversationScoped 
public class ScadenzarioMBean 
extends BaseMBean<ScadenzarioBean, Long, ScadenzarioSearchForm> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Logger log;

	private String azione = null;

	//	@Inject @Named("scadenzarioForm")
	private ScadenzarioForm form = null;

	//  @Inject @Named("intNdpService")
	private IIntermediarioNdpService ndpService ;

	//	@Inject @Named("enteService")
	private IEnteService enteService ;

	private boolean showForm = false;

	private String selectedIdEnte = null;
	private String selectedIdSystem = null;
	private ScadenzarioModelId selectedId= null;

	private EnteBean selectedEnte = null;
	private EnteMBean enteMbean = null;

	private List<ScadenzarioBean> listaScadenzari;

	private TributoMBean tributoMBean = null;

	public ScadenzarioMBean(){

		this.search = new ScadenzarioSearchForm();
		this.search.reset();
		this.form = new ScadenzarioForm();
		this.form.setMbean(this); 
		this.showForm = false;
		this.form.setRendered(this.showForm);
		this.azione = null;
		this.selectedIdEnte= null;
		this.selectedIdSystem= null; 
		this.selectedId = null;
		this.form.setRendered(this.showForm);
		this.form.getConnettoreNotifica().getAutenticazione().setElencoSelectItems(this.getListaTipiAutenticazione());
		this.form.getConnettoreNotifica().getTipoSsl().setElencoSelectItems(this.getListaTipiSSL());
		this.form.getConnettoreVerifica().getAutenticazione().setElencoSelectItems(this.getListaTipiAutenticazione());
		this.form.getConnettoreVerifica().getTipoSsl().setElencoSelectItems(this.getListaTipiSSL());
		this.form.reset();


		this.setTributoMBean(new TributoMBean());
		this.getTributoMBean().setScadenzarioService((IScadenzarioService) this.service); 
	}

	@Override
	public void setSelectedElement(ScadenzarioBean selectedElement) {
		super.setSelectedElement(selectedElement);
		this.showForm = false;
		this.form.setRendered(this.showForm);
		this.azione = null;

		this.getTributoMBean().setSelectedEnte(this.selectedEnte);
		this.getTributoMBean().setListaTributi(null); 
		this.getTributoMBean().setSelectedElement(null);
		this.getTributoMBean().setSelectedId(null);
		this.getTributoMBean().setAzione(null);
		this.getTributoMBean().setShowForm(false); 

	}

	public String menuAction(){
		this.search.reset();

		this.selectedIdEnte= null;
		this.selectedIdSystem= null;  
		this.selectedId = null;
		this.showForm = false;
		this.form.setRendered(this.showForm);
		this.azione = null;
		this.form.getConnettoreNotifica().getAutenticazione().setElencoSelectItems(this.getListaTipiAutenticazione());
		this.form.getConnettoreNotifica().getTipoSsl().setElencoSelectItems(this.getListaTipiSSL());
		this.form.getConnettoreVerifica().getAutenticazione().setElencoSelectItems(this.getListaTipiAutenticazione());
		this.form.getConnettoreVerifica().getTipoSsl().setElencoSelectItems(this.getListaTipiSSL());
		this.form.reset();
		return "listaEnti";
	}

	public String invia(){

		String msg = this.form.valida();

		if(msg!= null){
			MessageUtils.addErrorMsg(Utils.getMessageFromResourceBundle("scadenzario.form.erroreValidazione")+": " + msg);
			return null;
		}

		try{
			String oldIdEnte = null;
			String oldIdSystem = null;
			ScadenzarioModelId oldId = null;

			// Add
			if(!azione.equals("update")){
				ScadenzarioModelId key = new ScadenzarioModelId();
				String _idEnteValue = this.form.getIdEnte().getValue();
				key.setIdEnte(_idEnteValue);
				key.setIdSystem(this.form.getNome().getValue());
				ScadenzarioBean oldInt = ((IScadenzarioService) this.service).findById(key);

				if(oldInt!= null){
					MessageUtils.addErrorMsg(Utils.getMessageFromResourceBundle("scadenzario.form.erroreValidazione") +
							": " +Utils.getMessageWithParamsFromResourceBundle("scadenzario.form.intermediarioEsistente",this.form.getNome().getValue()));
					return null;
				}
			} else {
				oldIdEnte = this.selectedElement.getDTO().getIdEnte();
				oldIdSystem = this.selectedElement.getDTO().getIdSystem();
				oldId = new ScadenzarioModelId(oldIdEnte,oldIdSystem);
			}

			it.govpay.web.console.anagrafica.model.ScadenzarioModel newScad = this.form.getScadenzario();
			//			StazioneModel stazione = this.form.getStazione();


			ScadenzarioBean bean = new ScadenzarioBean();
			bean.setDTO(newScad);
			//			bean.setStazione(stazione); 


			((IScadenzarioService)this.service).store(oldId,bean);
			MessageUtils.addInfoMsg(Utils.getMessageFromResourceBundle("scadenzario.form.salvataggioOk"));
			this.setSelectedIdSystem(bean.getNome().getValue());
			this.setSelectedIdEnte(this.form.getIdEnte().getValue());
			this.setSelectedEnte(this.getSelectedEnte());


			this.dettaglio();

			return null;//"invia";
		}catch(Exception e){
			log.error("Si e' verificato un errore durante il salvataggio dello scadenzario: " + e.getMessage(), e);
			MessageUtils.addErrorMsg(Utils.getMessageFromResourceBundle("scadenzario.form.erroreGenerico"));
			return null;
		}
		//return "listaEnti?faces-redirect=true";
	}

	public String dettaglio(){
		this.selectedId = new ScadenzarioModelId();
		this.selectedId.setIdEnte(selectedIdEnte);
		this.selectedId.setIdSystem(selectedIdSystem);

		if(this.selectedId != null){
			try {
				ScadenzarioBean findById = ((IScadenzarioService)this.service).findById(selectedId);
				this.setSelectedElement(findById);
			} catch (ServiceException e) {
				this.log.error("Si e' verificato un errore durante la lettura dell'IntermediarioNDP: " + e.getMessage(), e); 
			}
		}

		this.tributoMBean.setSelectedScadenzario(this.selectedElement); 
		this.tributoMBean.setSelectedElement(null);
		this.tributoMBean.setSelectedId(null);
		this.tributoMBean.setAzione(null);
		this.tributoMBean.setShowForm(false); 

		return "listaEnti?faces-redirect=true";
	}

	public String showSearch(){
		return "listaEnti?faces-redirect=true";
	}

	// Action della pagina di ricerca
	public String filtra(){
		return "listaEnti?faces-redirect=true";
	}


	public String resetFiltro(){
		this.search.reset();
		return "listaEnti?faces-redirect=true";
	}

	public String annulla(){
		if(this.azione.equals("update")){
			this.showForm = false;
			this.azione = null;
		} 
		this.form.setRendered(this.showForm); 
		this.form.reset();

		return "listaEnti?faces-redirect=true";
	}

	/**
	 * Listener eseguito prima di aggiungere un nuovo ricerca, setta a null il selectedElement
	 * in modo da "scordarsi" i valori gia' impostati.
	 * @param ae
	 */
	public void addNewListener(ActionEvent ae){
		super.addNewListener(ae);
		this.selectedElement = null;
		this.selectedIdEnte= null;
		this.selectedIdSystem= null; 
		this.selectedId = null;
		this.azione = "add";
		this.showForm = true;
		this.form.setRendered(this.showForm);
		this.form.setValues(this.selectedElement);

		this.form.getIdEnte().setDefaultValue(this.selectedEnte.getIdEnteCreditore().getValue());
		this.form.getIdIntermediarioPA().setElencoSelectItems(this.getListaIntermediari());
		this.form.getStazione().setElencoSelectItems(this.getListaStazioni(null));
		//		this.form.getIdIntermediarioPA().setDefaultValue(new org.openspcoop2.generic_project.web.form.field.SelectItem(CostantiForm.NON_SELEZIONATO, CostantiForm.NON_SELEZIONATO)); 


		this.form.reset();

		this.tributoMBean.setListaTributi(null);
		this.tributoMBean.setSelectedEnte(this.selectedEnte);
		this.tributoMBean.setSelectedScadenzario(null);
		this.tributoMBean.setSelectedElement(null);
		this.tributoMBean.setSelectedId(null);
		this.tributoMBean.setAzione(null);
		this.tributoMBean.setShowForm(false); 

	}

	public String getAzione() {
		return azione;
	}

	public void setAzione(String azione) {
		this.azione = azione;
	}

	public ScadenzarioForm getForm() {
		return form;
	}

	public void setForm(ScadenzarioForm form) {
		this.form = form;
	}

	public boolean isShowForm() {
		return showForm;
	}

	public void setShowForm(boolean showForm) {
		this.showForm = showForm;
	}

	public String getSelectedIdEnte() {
		return selectedIdEnte;
	}

	public void setSelectedIdEnte(String selectedIdEnte) {
		this.selectedIdEnte = selectedIdEnte;
	}

	public String getSelectedIdSystem() {
		return selectedIdSystem;
	}

	public void setSelectedIdSystem(String selectedIdSystem) {
		this.selectedIdSystem = selectedIdSystem;
	}

	public ScadenzarioModelId getSelectedId() {
		return selectedId;
	}

	public void setSelectedId(ScadenzarioModelId selectedId) {
		this.selectedId = selectedId;
	}

	public String modifica(){
		this.showForm = true;
		this.azione = "update";
		this.form.setRendered(this.showForm);
		this.form.setValues(this.selectedElement);

		this.form.getIdEnte().setDefaultValue(this.selectedEnte.getIdEnteCreditore().getValue());


		this.form.reset();

		this.form.getIdIntermediarioPA().setElencoSelectItems(this.getListaIntermediari()); 
		this.form.getStazione().setElencoSelectItems(this.getListaStazioni(this.form.getIdIntermediarioPA().getValue().getValue()));  

		this.getTributoMBean().setSelectedEnte(this.selectedEnte);
		this.getTributoMBean().setSelectedElement(null);
		this.getTributoMBean().setSelectedId(null);
		this.getTributoMBean().setAzione(null);
		this.getTributoMBean().setShowForm(false); 


		return "listaEnti?faces-redirect=true";
	}

	public String elimina(){

		return "listaEnti?faces-redirect=true";
	}


	public List<SelectItem> getListaTipiAutenticazione(){
		List<SelectItem> lista = new ArrayList<SelectItem>();

		lista.add(new SelectItem(
				new org.openspcoop2.generic_project.web.form.field.SelectItem(
						EnumAuthType.NONE.toString(), ("connettore.autenticazione.nessuna"))));
		lista.add(new SelectItem(
				new org.openspcoop2.generic_project.web.form.field.SelectItem(
						EnumAuthType.HTTPBasic.toString(),   ("connettore.autenticazione.http"))));
		lista.add(new SelectItem(
				new org.openspcoop2.generic_project.web.form.field.SelectItem(
						EnumAuthType.SSL.toString(),   ("connettore.autenticazione.ssl"))));

		return lista;
	}

	public List<SelectItem> getListaTipiSSL(){
		List<SelectItem> lista = new ArrayList<SelectItem>();

		lista.add(new SelectItem(
				new org.openspcoop2.generic_project.web.form.field.SelectItem(
						CostantiForm.NON_SELEZIONATO, CostantiForm.NON_SELEZIONATO)));

		lista.add(new SelectItem(
				new org.openspcoop2.generic_project.web.form.field.SelectItem(
						EnumSslType.CLIENT.toString(), ("connettore.autenticazione.ssl.tipoSsl.client"))));
		lista.add(new SelectItem(
				new org.openspcoop2.generic_project.web.form.field.SelectItem(
						EnumSslType.SERVER.toString(),   ("connettore.autenticazione.ssl.tipoSsl.server"))));

		return lista;
	}

	public String getIdIntermediarioPAFromEnte(String idEnteCreditore){
		try{
			DominioEnteModel dominioEnte = this.enteService.getDominioEnte(idEnteCreditore);

			if(dominioEnte != null && dominioEnte.getIntermediario() != null)
				return dominioEnte.getIntermediario().getIdIntermediarioPA();
		}catch(Exception e){
			log.debug("Si e' verificato un errore durante la letture dell'intermendiario:" + e.getMessage());
		}

		return null;
	}

	public Logger getLog() {
		return log;
	}

	public void setLog(Logger log) {
		this.log = log;
		this.getTributoMBean().setLogger(log);

	}

	public IEnteService getEnteService() {
		return enteService;
	}

	public void setEnteService(IEnteService enteService) {
		this.enteService = enteService;
		this.getTributoMBean().setEnteService(enteService); 
	}

	public IIntermediarioNdpService getNdpService() {
		return ndpService;
	}

	public void setNdpService(IIntermediarioNdpService ndpService) {
		this.ndpService = ndpService;
	}

	public EnteBean getSelectedEnte() {
		return selectedEnte;
	}

	public void setSelectedEnte(EnteBean selectedEnte) {
		this.selectedEnte = selectedEnte;
		this.getTributoMBean().setSelectedEnte(selectedEnte); 

		this.selectedEnte = selectedEnte;

		if(selectedEnte !=  null){
			try {
				ScadenzarioSearchForm ricerca= new ScadenzarioSearchForm() ;
				ricerca.getIdEnte().setValue(this.selectedEnte.getIdEnteCreditore().getValue());

				this.listaScadenzari = ((IScadenzarioService) this.service).findAll(ricerca );
			} catch (ServiceException e) {
				log.error("Errore durante il caricamento della lista sscadenzari");
				this.listaScadenzari = new ArrayList<ScadenzarioBean>();
			}
		} else {
			this.listaScadenzari = new ArrayList<ScadenzarioBean>();
		}
	}

	public EnteMBean getEnteMbean() {
		return enteMbean;
	}

	public void setEnteMbean(EnteMBean enteMbean) {
		this.enteMbean = enteMbean;
		this.getTributoMBean().setEnteMbean(enteMbean); 
	}

	public TributoMBean getTributoMBean() {
		return tributoMBean;
	}

	public void setTributoMBean(TributoMBean tributoMBean) {
		this.tributoMBean = tributoMBean;
	}

	public List<ScadenzarioBean> getListaScadenzari() {
		return listaScadenzari;
	}

	public void setListaScadenzari(List<ScadenzarioBean> listaEnti) {
		this.listaScadenzari = listaEnti;
	}

	public List<SelectItem> getListaIntermediari(){
		List<SelectItem> lista = new ArrayList<SelectItem>();
		lista.add(new SelectItem(
				new org.openspcoop2.generic_project.web.form.field.SelectItem(
						CostantiForm.NON_SELEZIONATO, CostantiForm.NON_SELEZIONATO)));

		try {
			List<IntermediarioNdpBean> findAll = this.ndpService.findAll();

			if(findAll  != null && findAll.size() > 0){
				for (IntermediarioNdpBean intermediarioNdpBean : findAll) {
					String id = intermediarioNdpBean.getIdIntermediarioPA().getValue();
					String label = intermediarioNdpBean.getNomeSoggettoSPC().getValue();
					lista.add(new SelectItem(
							new org.openspcoop2.generic_project.web.form.field.SelectItem(id,label + " (" + id + ")")));
				}
			}
		} catch (ServiceException e) {
			log.error("Errore durante la findAll intermediari:" + e.getMessage(),e);
		}

		return lista;
	}

	public List<SelectItem> getListaStazioni(String idIntermediarioPA){
		List<SelectItem> lista = new ArrayList<SelectItem>();
		lista.add(new SelectItem(
				new org.openspcoop2.generic_project.web.form.field.SelectItem(
						CostantiForm.NON_SELEZIONATO, CostantiForm.NON_SELEZIONATO)));

		try {
			if(idIntermediarioPA != null){
				List<StazioneBean> findAll = this.ndpService.findAllStazioni(idIntermediarioPA); 

				if(findAll  != null && findAll.size() > 0){
					for (StazioneBean stazioneBean : findAll) {
						boolean found = false;
						String id = stazioneBean.getIdStazioneIntermediarioPA().getValue();

						if(this.listaScadenzari != null && this.listaScadenzari.size() > 0)
							for (ScadenzarioBean scadenzario : this.listaScadenzari) {
								String idStazioneUsata = scadenzario.getIdStazioneIntermediarioPA().getValue();

								if(idStazioneUsata.equals(stazioneBean.getIdStazioneIntermediarioPA().getValue())){
									found = true;
									break;
								}
							} 

						if(!found)
							lista.add(new SelectItem(new org.openspcoop2.generic_project.web.form.field.SelectItem(id,id)));
					}
				}
			}
		} catch (ServiceException e) {
			log.error("Errore durante la findAll stazioni:" + e.getMessage(),e);
		}

		return lista;
	}


}
