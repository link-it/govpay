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
package it.govpay.web.console.anagrafica.service;

import it.govpay.ejb.controller.AnagraficaEJB;
import it.govpay.ejb.model.EnteCreditoreModel;
import it.govpay.ejb.model.GatewayPagamentoModel;
import it.govpay.ejb.model.ScadenzarioModel;
import it.govpay.ejb.model.GatewayPagamentoModel.EnumFornitoreGateway;
import it.govpay.ejb.model.GatewayPagamentoModel.EnumStato;
import it.govpay.ndp.controller.PspController;
import it.govpay.ndp.ejb.AnagraficaDominioEJB;
import it.govpay.ndp.model.DominioEnteModel;
import it.govpay.web.console.anagrafica.bean.PspBean;
import it.govpay.web.console.anagrafica.form.PspSearchForm;
import it.govpay.web.console.anagrafica.iservice.IPspService;
import it.govpay.web.console.anagrafica.model.PspModel;
import it.govpay.web.console.anagrafica.model.PspModel.CanaleModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.web.service.BaseService;

@Singleton @Named("pspService")
public class PspService extends BaseService<PspSearchForm> implements IPspService,Serializable {

	private static final long serialVersionUID = 1L;

	@Inject  
	private transient Logger log;

	@Inject
	AnagraficaEJB anagraficaEjb;
	
	@Inject
	AnagraficaDominioEJB anagraficaDominioEjb;

	@Inject
	PspController pspCtrl;

	@Override
	public List<PspBean> findAll(int arg0, int arg1) throws ServiceException {
		return null;
	}

	@Override
	public int totalCount() throws ServiceException {
		return 0;
	}

	@Override
	public void delete(PspBean arg0) throws ServiceException {
	}

	@Override
	public void deleteById(Long arg0) throws ServiceException {
	}

	@Override
	public List<PspBean> findAll() throws ServiceException {
		log.debug("FindAll Psp"); 
		List<PspBean> lst= new ArrayList<PspBean>();

		try{
			ThreadContext.put("proc", "FindAll Psp");
			ThreadContext.put("dom", null);
			ThreadContext.put("iuv", null);
			ThreadContext.put("ccp", null);

			List<GatewayPagamentoModel> listaPsp = anagraficaEjb.findAllGatewayPagamento();
			
			List<PspModel> pspModels = new ArrayList<PspModel>();
			
			if(listaPsp != null && listaPsp.size() > 0){
				for (GatewayPagamentoModel psp : listaPsp) {
					
					PspModel newPspModel = new PspModel();
					
					CanaleModel canale = newPspModel.new CanaleModel();
					canale.setDescrizione(psp.getDescrizione());
					canale.setDisponibilita(psp.getDisponibilitaServizio());
					canale.setIdPsp(psp.getIdGateway());
					canale.setModelloVersamento(psp.getModelloVersamento());
					canale.setStornoGestito(psp.isStornoGestito());
					canale.setTipoVersamento(psp.getModalitaPagamento());
					canale.setAbilitato(psp.getStato().equals(EnumStato.ATTIVO));
					canale.setCommissioni(psp.getImportoCommissioneMassima());
					canale.setInformazioni(psp.getUrlInformazioniCanale());
					
					// Marker per vedere se ho aggiunto il canale ad un psp gia' presente
					boolean added = false;
					
					for(PspModel pspModel : pspModels) {
						if(pspModel.getRagioneSociale().equals(psp.getDescrizioneGateway())) {
							pspModel.getTipiVersamentoGestiti().add(psp.getModalitaPagamento());
							pspModel.getCanali().add(canale);
							added = true;
						}
					}
					
					if(!added) {
						// Il Psp non era in lista, lo aggiungo nuovo
						newPspModel.setRagioneSociale(psp.getDescrizioneGateway());
						newPspModel.getTipiVersamentoGestiti().add(psp.getModalitaPagamento());
						newPspModel.getCanali().add(canale);
						newPspModel.setInizioValidita(psp.getDataInizioValidita());
						newPspModel.setFineValidita(psp.getDataFineValidita());
						newPspModel.setInformazioni(psp.getUrlInformazioniPsp());
						pspModels.add(newPspModel);
					}
					
				}
				
				for (PspModel pspModel : pspModels) {
					PspBean bean = new PspBean();
					bean.setDTO(pspModel);

					lst.add(bean);	
				}
			}
			return lst;
		}catch(Exception e){
			log.error("Si e' verificato un errore durante la lettura della lista PSP: "+ e.getMessage(), e);
			throw new ServiceException(e);
		}
	}

	@Override
	public PspBean findById(Long arg0) throws ServiceException {
		return null;
	}

	@Override
	public void store(PspBean arg0) throws ServiceException {
	}

	@Override
	public void aggiornaListaPsp() throws ServiceException {
		log.debug("Aggioramento della lista Psp in corso."); 
		try{
			ThreadContext.put("dom", null);
			ThreadContext.put("iuv", null);
			ThreadContext.put("ccp", null);

			List<EnteCreditoreModel> enti = anagraficaEjb.getEntiCreditori();
			List<GatewayPagamentoModel> listaPsp = new ArrayList<GatewayPagamentoModel>();
			for(EnteCreditoreModel ente : enti) {
				ThreadContext.put("dom", ente.getIdFiscale());
				List<ScadenzarioModel> scadenzari = anagraficaEjb.getScadenzari(ente.getIdEnteCreditore());
				for(ScadenzarioModel scadenzario : scadenzari) {
					DominioEnteModel dominioEnte = anagraficaDominioEjb.getDominioEnte(ente.getIdEnteCreditore(), scadenzario.getIdStazione());
				
					List<GatewayPagamentoModel> listaPspScadenzario = pspCtrl.chiediListaPsp(dominioEnte);
					for(GatewayPagamentoModel psp : listaPspScadenzario) {
						if(!listaPsp.contains(psp)) listaPsp.add(psp);
					}
				}
			}
			anagraficaEjb.aggiornaGatewayPagamento(listaPsp,EnumFornitoreGateway.NODO_PAGAMENTI_SPC);
		} catch(Exception e) {
			log.error("Si e' verificato un errore durante l'aggiornamento della lista PSP: "+ e.getMessage(), e);
			throw new ServiceException(e);
		}
	}
}
