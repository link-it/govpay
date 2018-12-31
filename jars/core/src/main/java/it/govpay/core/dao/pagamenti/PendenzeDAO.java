/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2018 Link.it srl (http://www.link.it).
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
package it.govpay.core.dao.pagamenti;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;
import org.springframework.security.core.Authentication;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.model.eventi.EventoNota;
import it.govpay.bd.model.eventi.EventoNota.TipoNota;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.VersamentoFilter;
import it.govpay.bd.viste.VersamentiIncassiBD;
import it.govpay.bd.viste.filters.VersamentoIncassoFilter;
import it.govpay.bd.viste.model.VersamentoIncasso;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.business.GiornaleEventi;
import it.govpay.core.business.model.Iuv;
import it.govpay.core.business.model.PrintAvvisoDTO;
import it.govpay.core.business.model.PrintAvvisoDTOResponse;
import it.govpay.core.dao.anagrafica.utils.UtenzaPatchUtils;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PatchPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.PatchPendenzaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PutPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.PutPendenzaDTOResponse;
import it.govpay.core.dao.pagamenti.exception.PendenzaNonTrovataException;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.AvvisaturaUtils;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.IuvUtils;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.PatchOp;
import it.govpay.model.PatchOp.OpEnum;
import it.govpay.model.StatoPendenza;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.model.Versamento.AvvisaturaOperazione;
import it.govpay.model.Versamento.ModoAvvisatura;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.model.avvisi.AvvisoPagamento;
import it.govpay.stampe.model.AvvisoPagamentoInput;

public class PendenzeDAO extends BaseDAO{

	private static final String NON_E_CONSENTITO_AGGIORNARE_LO_STATO_DI_UNA_PENDENZA_AD_0 = "Non e'' consentito aggiornare lo stato di una pendenza ad {0}";
	private static final String PATH_DESCRIZIONE_STATO = "/descrizioneStato";
	private static final String PATH_STATO = "/stato";
	private static final String PATH_ACK = "/ack";
	public static final String PATH_NOTA = "/nota";

	public PendenzeDAO() {
	}

	public ListaPendenzeDTOResponse listaPendenze(ListaPendenzeDTO listaPendenzaDTO) throws ServiceException,PendenzaNonTrovataException, NotAuthorizedException, NotAuthenticatedException{
		BasicBD bd = null;
		
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			return listaPendenzaDTO.isInfoIncasso() ? this.listaPendenzeConInformazioniIncasso(listaPendenzaDTO, bd) : this.listaPendenze(listaPendenzaDTO, bd);
		}finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public ListaPendenzeDTOResponse listaPendenze(ListaPendenzeDTO listaPendenzaDTO, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		this.autorizzaRichiesta(listaPendenzaDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, bd);
		// Autorizzazione sui domini
		List<Long> idDomini = AuthorizationManager.getIdDominiAutorizzati(listaPendenzaDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA);
		if(idDomini == null) {
			throw new NotAuthorizedException("L'utenza autenticata ["+listaPendenzaDTO.getUser().getPrincipal()+"] non e' autorizzata ai servizi " + Servizio.PAGAMENTI_E_PENDENZE + " per alcun dominio");
		}
		
		GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(listaPendenzaDTO.getUser());
		
		VersamentiBD versamentiBD = new VersamentiBD(bd);
		VersamentoFilter filter = versamentiBD.newFilter();
		
		if(idDomini != null && idDomini.size() > 0)
			filter.setIdDomini(idDomini);

		filter.setOffset(listaPendenzaDTO.getOffset());
		filter.setLimit(listaPendenzaDTO.getLimit());
		filter.setDataInizio(listaPendenzaDTO.getDataDa());
		filter.setDataFine(listaPendenzaDTO.getDataA());
		filter.setCodVersamento(listaPendenzaDTO.getIdPendenza());
		if(listaPendenzaDTO.getStato()!=null) {
			try {
				StatoVersamento statoVersamento = null;
				StatoPendenza statoPendenza = StatoPendenza.valueOf(listaPendenzaDTO.getStato());
				
				//TODO mapping...piu' stati?
				switch(statoPendenza) {
				case ANNULLATA: statoVersamento = StatoVersamento.ANNULLATO;
					break;
				case ESEGUITA: statoVersamento = StatoVersamento.ESEGUITO;
					break;
				case ESEGUITA_PARZIALE: statoVersamento = StatoVersamento.PARZIALMENTE_ESEGUITO;
					break;
				case NON_ESEGUITA: statoVersamento = StatoVersamento.NON_ESEGUITO;
					break;
				case SCADUTA: statoVersamento = StatoVersamento.NON_ESEGUITO; //TODO aggiungere data scadenza < ora
					break;
				default:
					break;
				
				}
				filter.setStatoVersamento(statoVersamento);
			} catch(Exception e) {
				return new ListaPendenzeDTOResponse(0, new ArrayList<LeggiPendenzaDTOResponse>());
			}
		}
		filter.setCodDominio(listaPendenzaDTO.getIdDominio() );
		filter.setCodPagamentoPortale(listaPendenzaDTO.getIdPagamento());
		filter.setCodUnivocoDebitore(listaPendenzaDTO.getIdDebitore());
		filter.setCodApplicazione(listaPendenzaDTO.getIdA2A());
		
		filter.setFilterSortList(listaPendenzaDTO.getFieldSortList());
		if(!listaPendenzaDTO.isOrderEnabled()) {
			filter.addFilterSort(filter.getDefaultFilterSortWrapperDesc());
		}
		
		if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
			filter.setCfCittadino(userDetails.getIdentificativo()); 
		}

		long count = versamentiBD.count(filter);

		List<LeggiPendenzaDTOResponse> resList = new ArrayList<>();
		if(count > 0) {
			List<Versamento> findAll = versamentiBD.findAll(filter);

			for (Versamento versamento : findAll) {
				LeggiPendenzaDTOResponse elem = new LeggiPendenzaDTOResponse();
				elem.setVersamento(versamento);
				elem.setApplicazione(versamento.getApplicazione(versamentiBD));
				elem.setDominio(versamento.getDominio(versamentiBD));
				elem.setUnitaOperativa(versamento.getUo(versamentiBD));
//				List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(versamentiBD);
//				for (SingoloVersamento singoloVersamento : singoliVersamenti) {
//					singoloVersamento.getCodContabilita(bd);
//					singoloVersamento.getIbanAccredito(bd);
//					singoloVersamento.getTipoContabilita(bd);
//					singoloVersamento.getTributo(bd);
//					
//				}
//				elem.setLstSingoliVersamenti(singoliVersamenti);

				resList.add(elem);
			}
		} 

		return new ListaPendenzeDTOResponse(count, resList);
	}
	
	public ListaPendenzeDTOResponse listaPendenzeConInformazioniIncasso(ListaPendenzeDTO listaPendenzaDTO, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		this.autorizzaRichiesta(listaPendenzaDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, bd);
		// Autorizzazione sui domini
		List<Long> idDomini = AuthorizationManager.getIdDominiAutorizzati(listaPendenzaDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA);
		if(idDomini == null) {
			throw new NotAuthorizedException("L'utenza autenticata ["+listaPendenzaDTO.getUser().getPrincipal()+"] non e' autorizzata ai servizi " + Servizio.PAGAMENTI_E_PENDENZE + " per alcun dominio");
		}
		GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(listaPendenzaDTO.getUser());
		
		VersamentiIncassiBD versamentiBD = new VersamentiIncassiBD(bd);
		VersamentoIncassoFilter filter = versamentiBD.newFilter();
		
		if(idDomini != null && idDomini.size() > 0)
			filter.setIdDomini(idDomini);

		filter.setOffset(listaPendenzaDTO.getOffset());
		filter.setLimit(listaPendenzaDTO.getLimit());
		filter.setDataInizio(listaPendenzaDTO.getDataDa());
		filter.setDataFine(listaPendenzaDTO.getDataA());
		if(listaPendenzaDTO.getStato()!=null) {
			try {
				it.govpay.bd.viste.model.VersamentoIncasso.StatoVersamento statoVersamento = null;
				StatoPendenza statoPendenza = StatoPendenza.valueOf(listaPendenzaDTO.getStato());
				
				//TODO mapping...piu' stati?
				switch(statoPendenza) {
				case ANNULLATA: statoVersamento = it.govpay.bd.viste.model.VersamentoIncasso.StatoVersamento.ANNULLATO;
					break;
				case ESEGUITA: statoVersamento = it.govpay.bd.viste.model.VersamentoIncasso.StatoVersamento.ESEGUITO;
					break;
				case ESEGUITA_PARZIALE: statoVersamento = it.govpay.bd.viste.model.VersamentoIncasso.StatoVersamento.PARZIALMENTE_ESEGUITO;
					break;
				case NON_ESEGUITA: statoVersamento = it.govpay.bd.viste.model.VersamentoIncasso.StatoVersamento.NON_ESEGUITO;
					break;
				case SCADUTA: statoVersamento = it.govpay.bd.viste.model.VersamentoIncasso.StatoVersamento.NON_ESEGUITO; //TODO aggiungere data scadenza < ora
					break;
				case INCASSATA: statoVersamento = it.govpay.bd.viste.model.VersamentoIncasso.StatoVersamento.INCASSATO;
				default:
					break;
				
				}
				filter.setStatoVersamento(statoVersamento);
			} catch(Exception e) {
				return new ListaPendenzeDTOResponse(0, new ArrayList<LeggiPendenzaDTOResponse>());
			}
		}
		filter.setCodDominio(listaPendenzaDTO.getIdDominio() );
		filter.setCodPagamentoPortale(listaPendenzaDTO.getIdPagamento());
		filter.setCodUnivocoDebitore(listaPendenzaDTO.getIdDebitore());
		filter.setCodApplicazione(listaPendenzaDTO.getIdA2A());
		filter.setCodVersamento(listaPendenzaDTO.getIdPendenza());
		
		filter.setFilterSortList(listaPendenzaDTO.getFieldSortList());
		if(!listaPendenzaDTO.isOrderEnabled()) {
			filter.addFilterSort(filter.getDefaultFilterSortWrapperDesc());
		}
		if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
			filter.setCfCittadino(userDetails.getIdentificativo()); 
		}

		long count = versamentiBD.count(filter);

		List<LeggiPendenzaDTOResponse> resList = new ArrayList<>();
		if(count > 0) {
			List<VersamentoIncasso> findAll = versamentiBD.findAll(filter);

			for (VersamentoIncasso versamentoIncasso : findAll) {
				LeggiPendenzaDTOResponse elem = new LeggiPendenzaDTOResponse();
				elem.setVersamentoIncasso(versamentoIncasso);
				elem.setApplicazione(versamentoIncasso.getApplicazione(versamentiBD));
				elem.setDominio(versamentoIncasso.getDominio(versamentiBD));
				elem.setUnitaOperativa(versamentoIncasso.getUo(versamentiBD));
//				List<SingoloVersamento> singoliVersamenti = versamentoIncasso.getSingoliVersamenti(versamentiBD);
//				for (SingoloVersamento singoloVersamento : singoliVersamenti) {
//					singoloVersamento.getCodContabilita(bd);
//					singoloVersamento.getIbanAccredito(bd);
//					singoloVersamento.getTipoContabilita(bd);
//					singoloVersamento.getTributo(bd);
//					
//				}
//				elem.setLstSingoliVersamenti(singoliVersamenti);

				resList.add(elem);
			}
		} 

		return new ListaPendenzeDTOResponse(count, resList);
	}

	public LeggiPendenzaDTOResponse leggiPendenza(LeggiPendenzaDTO leggiPendenzaDTO) throws ServiceException,PendenzaNonTrovataException, NotAuthorizedException, NotAuthenticatedException, GovPayException{
		LeggiPendenzaDTOResponse response = new LeggiPendenzaDTOResponse();
		if(leggiPendenzaDTO.isInfoIncasso())
			_leggiPendenzaConInfromazionIncasso(leggiPendenzaDTO, response);
		else 
			_leggiPendenza(leggiPendenzaDTO, response);
		return response;
	}

	private void _leggiPendenza(LeggiPendenzaDTO leggiPendenzaDTO, LeggiPendenzaDTOResponse response)
			throws ServiceException, NotAuthenticatedException, NotAuthorizedException, PendenzaNonTrovataException, GovPayException {
		Versamento versamento = null;
		BasicBD bd = null;
		
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(leggiPendenzaDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, bd);

			
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			if(leggiPendenzaDTO.getCodA2A() != null)
				versamento = versamentiBD.getVersamento(AnagraficaManager.getApplicazione(versamentiBD, leggiPendenzaDTO.getCodA2A()).getId(), leggiPendenzaDTO.getCodPendenza());
			else if(leggiPendenzaDTO.getNumeroAvviso() != null)
				versamento = versamentiBD.getVersamento(leggiPendenzaDTO.getIdDominio(), IuvUtils.toIuv(leggiPendenzaDTO.getNumeroAvviso()));
			
			
			Dominio dominio = versamento.getDominio(versamentiBD);
			// controllo che il dominio sia autorizzato
			this.autorizzaRichiesta(leggiPendenzaDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, dominio.getCodDominio(), null, bd);
			
			response.setVersamento(versamento);
			response.setApplicazione(versamento.getApplicazione(versamentiBD));
		
			response.setDominio(dominio);
			response.setUnitaOperativa(versamento.getUo(versamentiBD));
			List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(versamentiBD);
			response.setLstSingoliVersamenti(singoliVersamenti);
			for (SingoloVersamento singoloVersamento : singoliVersamenti) {
				singoloVersamento.getCodContabilita(bd);
				singoloVersamento.getIbanAccredito(bd);
				singoloVersamento.getTipoContabilita(bd);
				singoloVersamento.getTributo(bd);
				
			}

		} catch (NotFoundException e) {
			throw new PendenzaNonTrovataException(e.getMessage(), e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}
	
	private void _leggiPendenzaConInfromazionIncasso(LeggiPendenzaDTO leggiPendenzaDTO, LeggiPendenzaDTOResponse response)
			throws ServiceException, NotAuthenticatedException, NotAuthorizedException, PendenzaNonTrovataException {
		VersamentoIncasso versamentoIncasso;
		BasicBD bd = null;
		
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(leggiPendenzaDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, bd);

			VersamentiIncassiBD versamentiBD = new VersamentiIncassiBD(bd);
			versamentoIncasso = versamentiBD.getVersamento(AnagraficaManager.getApplicazione(versamentiBD, leggiPendenzaDTO.getCodA2A()).getId(), leggiPendenzaDTO.getCodPendenza());
			
			Dominio dominio = versamentoIncasso.getDominio(versamentiBD);
			// controllo che il dominio sia autorizzato
			this.autorizzaRichiesta(leggiPendenzaDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, dominio.getCodDominio(), null, bd);
			
			response.setVersamentoIncasso(versamentoIncasso);
			response.setApplicazione(versamentoIncasso.getApplicazione(versamentiBD));
		
			response.setDominio(dominio);
			response.setUnitaOperativa(versamentoIncasso.getUo(versamentiBD));
			List<SingoloVersamento> singoliVersamenti = versamentoIncasso.getSingoliVersamenti(versamentiBD);
			response.setLstSingoliVersamenti(singoliVersamenti);
			for (SingoloVersamento singoloVersamento : singoliVersamenti) {
				singoloVersamento.getCodContabilita(bd);
				singoloVersamento.getIbanAccredito(bd);
				singoloVersamento.getTipoContabilita(bd);
				singoloVersamento.getTributo(bd);
				List<Pagamento> pagamenti = singoloVersamento.getPagamenti(bd);
				for (Pagamento pagamento: pagamenti) {
					this.populatePagamento(pagamento, bd);
				}
				
				List<Rendicontazione> rendicontazioni = singoloVersamento.getRendicontazioni(bd); 
				if(rendicontazioni != null) {
					for(Rendicontazione rend: rendicontazioni) {
						Pagamento pagamento = rend.getPagamento(bd);
						if(pagamento != null) {
							pagamento.getSingoloVersamento(bd).getVersamento(bd).getApplicazione(bd);
							pagamento.getDominio(bd);
							pagamento.getRpt(bd);
							pagamento.getIncasso(bd);
						}
					}
				}
			}

		} catch (NotFoundException e) {
			throw new PendenzaNonTrovataException(e.getMessage(), e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}
	
	private void populatePagamento(Pagamento pagamento, BasicBD bd) throws ServiceException, NotFoundException {
		pagamento.getSingoloVersamento(bd).getVersamento(bd).getApplicazione(bd);
		pagamento.getSingoloVersamento(bd).getVersamento(bd).getUo(bd);
		pagamento.getRpt(bd);
		pagamento.getDominio(bd);
		pagamento.getRendicontazioni(bd);
		pagamento.getIncasso(bd);
	}
	
	
	public PatchPendenzaDTOResponse patch(PatchPendenzaDTO patchPendenzaDTO) throws PendenzaNonTrovataException, GovPayException, NotAuthorizedException, NotAuthenticatedException, ValidationException{
		
		PatchPendenzaDTOResponse response = new PatchPendenzaDTOResponse();
		BasicBD bd = null;
		
		try {
			GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(patchPendenzaDTO.getUser());
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(patchPendenzaDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.SCRITTURA, bd);

			VersamentiBD versamentiBD = new VersamentiBD(bd);
			GiornaleEventi giornaleEventi = new GiornaleEventi(bd);
			EventoNota eventoNota = null;
			it.govpay.bd.model.Versamento versamentoLetto = versamentiBD.getVersamento(AnagraficaManager.getApplicazione(bd, patchPendenzaDTO.getIdA2a()).getId(), patchPendenzaDTO.getIdPendenza());
			
			// controllo che il dominio sia autorizzato
			this.autorizzaRichiesta(patchPendenzaDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.SCRITTURA, versamentoLetto.getDominio(bd).getCodDominio(), null, bd);
			
			for(PatchOp op: patchPendenzaDTO.getOp()) {
				
				if(PATH_STATO.equals(op.getPath())) {
					String motivazione = null;
					//cerco il patch di descrizione stato
					for(PatchOp op2: patchPendenzaDTO.getOp()) {
						if(PATH_DESCRIZIONE_STATO.equals(op2.getPath())) {
							try { motivazione = (String) op2.getValue(); } catch (Exception e) {}
						}
					}
					eventoNota = this.patchStato(patchPendenzaDTO.getUser(), versamentoLetto, op, motivazione, bd);
					eventoNota.setCodDominio(versamentoLetto.getUo(bd).getDominio(bd).getCodDominio());
					eventoNota.setIdVersamento(versamentoLetto.getId());
					eventoNota.setIuv(versamentoLetto.getIuvVersamento());
					
					eventoNota.setTipoEvento(it.govpay.bd.model.eventi.EventoNota.TipoNota.SistemaInfo);
					giornaleEventi.registraEventoNota(eventoNota);
				}
				
				if(PATH_DESCRIZIONE_STATO.equals(op.getPath())) {
					this.patchDescrizioneStato(versamentoLetto, op);
				}
				
				if(PATH_ACK.equals(op.getPath())) {
					this.patchAck(versamentoLetto, op);
				}
				
				if(PATH_NOTA.equals(op.getPath())) {
					eventoNota = this.patchNota(patchPendenzaDTO.getUser(), versamentoLetto, op, bd);
					eventoNota.setCodDominio(versamentoLetto.getUo(bd).getDominio(bd).getCodDominio());
					eventoNota.setIdVersamento(versamentoLetto.getId());
					eventoNota.setIuv(versamentoLetto.getIuvVersamento());
					giornaleEventi.registraEventoNota(eventoNota);
				}
				
				// Casi di operazioni patch che implicano una nota:
				// ANNULLAMENTO
				if(PATH_DESCRIZIONE_STATO.equals(op.getPath()) && PATH_STATO.equals(op.getPath()) && this.getNuovoStatoVersamento(op).equals(StatoVersamento.ANNULLATO)) {
			 		eventoNota = new EventoNota();
					eventoNota.setAutore(userDetails.getUtenza().getIdentificativo());
					eventoNota.setOggetto("Pendenza annullata");
					eventoNota.setTesto(versamentoLetto.getDescrizioneStato());
					eventoNota.setPrincipal(userDetails.getUtenza().getPrincipal());
					eventoNota.setData(new Date());
					eventoNota.setTipoEvento(TipoNota.SistemaInfo);
					eventoNota.setCodDominio(versamentoLetto.getUo(bd).getDominio(bd).getCodDominio());
					eventoNota.setIdVersamento(versamentoLetto.getId());
					eventoNota.setIuv(versamentoLetto.getIuvVersamento());
					giornaleEventi.registraEventoNota(eventoNota);
				}
				
				// RIPRISTINO
				if(PATH_DESCRIZIONE_STATO.equals(op.getPath()) && PATH_STATO.equals(op.getPath()) && this.getNuovoStatoVersamento(op).equals(StatoVersamento.NON_ESEGUITO)) {
			 		eventoNota = new EventoNota();
					eventoNota.setAutore(userDetails.getUtenza().getIdentificativo());
					eventoNota.setOggetto("Pendenza ripristinata");
					eventoNota.setTesto(versamentoLetto.getDescrizioneStato());
					eventoNota.setPrincipal(userDetails.getUtenza().getPrincipal());
					eventoNota.setData(new Date());
					eventoNota.setTipoEvento(TipoNota.SistemaInfo);
					eventoNota.setCodDominio(versamentoLetto.getUo(bd).getDominio(bd).getCodDominio());
					eventoNota.setIdVersamento(versamentoLetto.getId());
					eventoNota.setIuv(versamentoLetto.getIuvVersamento());
					giornaleEventi.registraEventoNota(eventoNota);
				}
				
			}
			
			versamentoLetto.setDataUltimoAggiornamento(new Date());
			versamentiBD.updateVersamento(versamentoLetto);
			return response;
			
		} catch (ServiceException e) {
			throw new GovPayException(e);
		} catch (NotFoundException e) {
			throw new PendenzaNonTrovataException(e.getMessage(), e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}

	}

	private void patchDescrizioneStato(it.govpay.bd.model.Versamento versamentoLetto, PatchOp op) throws ValidationException {
		if(!op.getOp().equals(OpEnum.REPLACE)) {
			throw new ValidationException(MessageFormat.format(UtenzaPatchUtils.OP_XX_NON_VALIDO_PER_IL_PATH_YY, op.getOp(), op.getPath()));
		}
		
		String descrizioneStato = (String) op.getValue();
		versamentoLetto.setDescrizioneStato(descrizioneStato);
	}

	private EventoNota patchStato(Authentication authentication, it.govpay.bd.model.Versamento versamentoLetto, PatchOp op, String motivazione, BasicBD bd) throws ValidationException {
		if(!op.getOp().equals(OpEnum.REPLACE)) {
			throw new ValidationException(MessageFormat.format(UtenzaPatchUtils.OP_XX_NON_VALIDO_PER_IL_PATH_YY, op.getOp(), op.getPath()));
		}
		
		StatoVersamento nuovoStato = this.getNuovoStatoVersamento(op);
		
		GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(authentication);
		EventoNota eventoNota = new EventoNota();
		eventoNota.setPrincipal(userDetails.getUtenza().getPrincipal());
		eventoNota.setAutore(userDetails.getIdentificativo());
		eventoNota.setData(new Date());
		eventoNota.setTesto(motivazione);
		eventoNota.setTipoEvento(TipoNota.SistemaInfo);

		switch (nuovoStato) {
		case ANNULLATO:
			if(versamentoLetto.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)) {
				versamentoLetto.setStatoVersamento(StatoVersamento.ANNULLATO);
				versamentoLetto.setAvvisaturaOperazione(AvvisaturaOperazione.DELETE.getValue());
				versamentoLetto.setAvvisaturaDaInviare(true);
				String avvisaturaDigitaleModalitaAnnullamentoAvviso = GovpayConfig.getInstance().getAvvisaturaDigitaleModalitaAnnullamentoAvviso();
				if(!avvisaturaDigitaleModalitaAnnullamentoAvviso.equals(AvvisaturaUtils.AVVISATURA_DIGITALE_MODALITA_USER_DEFINED)) {
					versamentoLetto.setAvvisaturaModalita(avvisaturaDigitaleModalitaAnnullamentoAvviso.equals("asincrona") ? ModoAvvisatura.ASICNRONA.getValue() : ModoAvvisatura.SINCRONA.getValue());
				}
				
				eventoNota.setOggetto("Pendenza annullata");
			} else {
				throw new ValidationException("Non e' consentito aggiornare lo stato di una pendenza ad ANNULLATO da uno stato diverso da NON_ESEGUITO");
			}
			break;
		case NON_ESEGUITO:
			if(versamentoLetto.getStatoVersamento().equals(StatoVersamento.ANNULLATO)) {
				versamentoLetto.setStatoVersamento(StatoVersamento.NON_ESEGUITO);
				versamentoLetto.setAvvisaturaOperazione(AvvisaturaOperazione.CREATE.getValue());
				versamentoLetto.setAvvisaturaDaInviare(true);
				String avvisaturaDigitaleModalitaAnnullamentoAvviso = GovpayConfig.getInstance().getAvvisaturaDigitaleModalitaAnnullamentoAvviso();
				if(!avvisaturaDigitaleModalitaAnnullamentoAvviso.equals(AvvisaturaUtils.AVVISATURA_DIGITALE_MODALITA_USER_DEFINED)) {
					versamentoLetto.setAvvisaturaModalita(avvisaturaDigitaleModalitaAnnullamentoAvviso.equals("asincrona") ? ModoAvvisatura.ASICNRONA.getValue() : ModoAvvisatura.SINCRONA.getValue());
				}
				
				eventoNota.setOggetto("Pendenza ripristinata");
			} else {
				throw new ValidationException("Non e' consentito aggiornare lo stato di una pendenza ad NON_ESEGUITO da uno stato diverso da ANNULLATO");
			}
			break;
		default:
			throw new ValidationException(MessageFormat.format(NON_E_CONSENTITO_AGGIORNARE_LO_STATO_DI_UNA_PENDENZA_AD_0, nuovoStato.name()));
		}
		
		
		
		return eventoNota;
	}
	
	private void patchAck(it.govpay.bd.model.Versamento versamentoLetto, PatchOp op) throws ValidationException {
		if(!op.getOp().equals(OpEnum.REPLACE)) {
			throw new ValidationException(MessageFormat.format(UtenzaPatchUtils.OP_XX_NON_VALIDO_PER_IL_PATH_YY, op.getOp(), op.getPath()));
		}

		Boolean ackVersamento = (Boolean) op.getValue();
		versamentoLetto.setAck(ackVersamento != null ? ackVersamento.booleanValue() : false);
	}
	
	private EventoNota patchNota(Authentication authentication, it.govpay.bd.model.Versamento versamentoLetto, PatchOp op, BasicBD bd) throws ValidationException, ServiceException, NotFoundException { 
		if(!op.getOp().equals(OpEnum.ADD)) {
			throw new ValidationException(MessageFormat.format(UtenzaPatchUtils.OP_XX_NON_VALIDO_PER_IL_PATH_YY, op.getOp(), op.getPath()));
		}
		
	 	return UtenzaPatchUtils.getNotaFromPatch(authentication, op, bd); 
	}

	private StatoVersamento getNuovoStatoVersamento(PatchOp op) throws ValidationException {
		StatoPendenza nuovoStatoPendenza = StatoPendenza.valueOf((String) op.getValue());
		
		StatoVersamento nuovoStato = null;
		switch (nuovoStatoPendenza) {
		case ANNULLATA:
			nuovoStato = StatoVersamento.ANNULLATO;
			break;
		case NON_ESEGUITA:
			nuovoStato = StatoVersamento.NON_ESEGUITO;
			break;
		default:
			throw new ValidationException(MessageFormat.format(NON_E_CONSENTITO_AGGIORNARE_LO_STATO_DI_UNA_PENDENZA_AD_0, nuovoStatoPendenza.name()));
		}
		return nuovoStato;
	}
	
	public PutPendenzaDTOResponse createOrUpdate(PutPendenzaDTO putVersamentoDTO) throws GovPayException, NotAuthorizedException, NotAuthenticatedException{ 
		PutPendenzaDTOResponse createOrUpdatePendenzaResponse = new PutPendenzaDTOResponse();
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(putVersamentoDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.SCRITTURA, bd);
			
			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento(bd);
			Versamento chiediVersamento = versamentoBusiness.chiediVersamento(putVersamentoDTO.getVersamento());
			
			// controllo che il dominio sia autorizzato
			this.autorizzaRichiesta(putVersamentoDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.SCRITTURA, chiediVersamento.getDominio(bd).getCodDominio(), null, bd);
			
			
			createOrUpdatePendenzaResponse.setCreated(false);
			VersamentiBD versamentiBD = new VersamentiBD(bd);

			try {
				versamentiBD.getVersamento(AnagraficaManager.getApplicazione(versamentiBD, putVersamentoDTO.getVersamento().getCodApplicazione()).getId(), putVersamentoDTO.getVersamento().getCodVersamentoEnte());
			}catch(NotFoundException e) {
				createOrUpdatePendenzaResponse.setCreated(true);
			}
			
			// TODO verificare e poi togliere
//			if(putVersamentoDTO.isAvvisaturaDigitale()) {
//				// controllare se e' abilitata a livello di sistema
//				chiediVersamento.setAvvisaturaAbilitata(true); 
//			}
//			
//			if(putVersamentoDTO.getAvvisaturaModalita() != null) {
//				chiediVersamento.setAvvisaturaModalita(putVersamentoDTO.getAvvisaturaModalita().getValue()); 
//			}
			
			versamentoBusiness.caricaVersamento(chiediVersamento, chiediVersamento.getNumeroAvviso() == null, true);
			
			// restituisco il versamento creato
			createOrUpdatePendenzaResponse.setVersamento(chiediVersamento);
			createOrUpdatePendenzaResponse.setDominio(chiediVersamento.getDominio(bd));
			
			Iuv iuv = IuvUtils.toIuv(chiediVersamento, chiediVersamento.getApplicazione(bd), chiediVersamento.getDominio(bd));
			
			createOrUpdatePendenzaResponse.setBarCode(iuv.getBarCode() != null ? new String(iuv.getBarCode()) : null);
			createOrUpdatePendenzaResponse.setQrCode(iuv.getQrCode() != null ? new String(iuv.getQrCode()) : null);
			
			if(putVersamentoDTO.isStampaAvviso()) {
				it.govpay.core.business.AvvisoPagamento avvisoBD = new it.govpay.core.business.AvvisoPagamento(bd);
				AvvisoPagamento avvisoPagamento = new AvvisoPagamento();
				avvisoPagamento.setCodDominio(chiediVersamento.getDominio(bd).getCodDominio());
				avvisoPagamento.setIuv(iuv.getIuv());
				PrintAvvisoDTO printAvvisoDTO = new PrintAvvisoDTO();
				printAvvisoDTO.setAvviso(avvisoPagamento);
				AvvisoPagamentoInput input = avvisoBD.fromVersamento(avvisoPagamento, chiediVersamento);
				printAvvisoDTO.setInput(input); 
				PrintAvvisoDTOResponse printAvvisoDTOResponse = avvisoBD.printAvviso(printAvvisoDTO);
				createOrUpdatePendenzaResponse.setPdf(Base64.getEncoder().encodeToString(printAvvisoDTOResponse.getAvviso().getPdf()));
			}

		} catch (ServiceException e) {
			throw new GovPayException(e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
		return createOrUpdatePendenzaResponse;
	}

	public LeggiPendenzaDTOResponse leggiAvvisoPagamento(LeggiPendenzaDTO leggiPendenzaDTO) throws ServiceException,PendenzaNonTrovataException, NotAuthorizedException, NotAuthenticatedException{
		LeggiPendenzaDTOResponse response = new LeggiPendenzaDTOResponse();
		Versamento versamento;
		BasicBD bd = null;
		
		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			this.autorizzaRichiesta(leggiPendenzaDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, bd);

			VersamentiBD versamentiBD = new VersamentiBD(bd);
			versamento = versamentiBD.getVersamentoFromDominioNumeroAvviso(leggiPendenzaDTO.getIdDominio(), leggiPendenzaDTO.getNumeroAvviso()); 
			
			Dominio dominio = versamento.getDominio(versamentiBD);
			// controllo che il dominio sia autorizzato
			this.autorizzaRichiesta(leggiPendenzaDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, dominio.getCodDominio(), null, bd);
			
			it.govpay.core.business.AvvisoPagamento avvisoBD = new it.govpay.core.business.AvvisoPagamento(bd);
			AvvisoPagamento avvisoPagamento = new AvvisoPagamento();
			avvisoPagamento.setCodDominio(versamento.getDominio(bd).getCodDominio());
			avvisoPagamento.setIuv(versamento.getIuvVersamento());
			PrintAvvisoDTO printAvvisoDTO = new PrintAvvisoDTO();
			printAvvisoDTO.setAvviso(avvisoPagamento);
			AvvisoPagamentoInput input = avvisoBD.fromVersamento(avvisoPagamento, versamento);
			printAvvisoDTO.setInput(input); 
			PrintAvvisoDTOResponse printAvvisoDTOResponse = avvisoBD.printAvviso(printAvvisoDTO);
			response.setAvvisoPdf(printAvvisoDTOResponse.getAvviso().getPdf());

		} catch (NotFoundException e) {
			throw new PendenzaNonTrovataException(e.getMessage(), e);
		}  finally {
			if(bd != null)
				bd.closeConnection();
		}
		return response;
	}
}
