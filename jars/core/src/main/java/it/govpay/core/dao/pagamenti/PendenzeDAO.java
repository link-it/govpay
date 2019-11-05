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
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.serialization.IOException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.springframework.security.core.Authentication;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.IdUnitaOperativa;
//import it.govpay.bd.model.Evento;
import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.TipoVersamentoDominio;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.PagamentiPortaleBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.PagamentoPortaleFilter;
import it.govpay.bd.pagamento.filters.RptFilter;
import it.govpay.bd.pagamento.filters.VersamentoFilter;
import it.govpay.bd.viste.VersamentiIncassiBD;
import it.govpay.bd.viste.filters.VersamentoIncassoFilter;
import it.govpay.bd.viste.model.VersamentoIncasso;
import it.govpay.bd.viste.model.converter.VersamentoIncassoConverter;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.beans.tracciati.PendenzaPost;
import it.govpay.core.business.Applicazione;
import it.govpay.core.business.model.Iuv;
import it.govpay.core.business.model.PrintAvvisoDTO;
import it.govpay.core.business.model.PrintAvvisoDTOResponse;
import it.govpay.core.dao.anagrafica.exception.DominioNonTrovatoException;
import it.govpay.core.dao.anagrafica.exception.TipoVersamentoNonTrovatoException;
import it.govpay.core.dao.anagrafica.exception.UnitaOperativaNonTrovataException;
import it.govpay.core.dao.anagrafica.utils.UtenzaPatchUtils;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeConInformazioniIncassoDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTOResponse;
import it.govpay.core.dao.pagamenti.dto.PatchPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.PutPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.PutPendenzaDTOResponse;
import it.govpay.core.dao.pagamenti.exception.PendenzaNonTrovataException;
import it.govpay.core.exceptions.EcException;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.AvvisaturaUtils;
import it.govpay.core.utils.GovpayConfig;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.IuvUtils;
import it.govpay.core.utils.TracciatiConverter;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.core.utils.validator.PendenzaPostValidator;
import it.govpay.model.PatchOp;
import it.govpay.model.PatchOp.OpEnum;
import it.govpay.model.StatoPendenza;
import it.govpay.model.TipoVersamento;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.model.Versamento.AvvisaturaOperazione;
import it.govpay.model.Versamento.ModoAvvisatura;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.orm.PagamentoPortaleVersamento;

public class PendenzeDAO extends BaseDAO{

	private static final String NON_E_CONSENTITO_AGGIORNARE_LO_STATO_DI_UNA_PENDENZA_AD_0 = "Non e'' consentito aggiornare lo stato di una pendenza ad {0}";
	private static final String NUOVO_STATO_PENDENZA_NON_VALIDO = "Il campo value indicato per il path ''{0}'' non e'' valido.";
	public static final String PATH_DESCRIZIONE_STATO = "/descrizioneStato";
	public static final String PATH_STATO = "/stato";
	public static final String PATH_ACK = "/ack";
	public static final String PATH_NOTA = "/nota";
	public static final String[] OPERAZIONI_CONSENTITE_PENDENZE = { PATH_DESCRIZIONE_STATO, PATH_STATO, PATH_ACK, PATH_NOTA }; 

	public PendenzeDAO() {
	}
	
	public ListaPendenzeDTOResponse countPendenze(ListaPendenzeConInformazioniIncassoDTO listaPendenzaDTO) throws ServiceException,PendenzaNonTrovataException, NotAuthorizedException, NotAuthenticatedException{
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());
			GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(listaPendenzaDTO.getUser());

			VersamentiIncassiBD versamentiBD = new VersamentiIncassiBD(bd);
			VersamentoIncassoFilter filter = versamentiBD.newFilter();

			filter.setIdDomini(listaPendenzaDTO.getIdDomini());
			filter.setIdTipiVersamento(listaPendenzaDTO.getIdTipiVersamento());
			
			if(listaPendenzaDTO.getUnitaOperative() != null) {
				List<Long> idDomini = new ArrayList<>();
				List<Long> idUO = new ArrayList<>();
				for (IdUnitaOperativa uo : listaPendenzaDTO.getUnitaOperative()) {
					if(uo.getIdDominio() != null && !idDomini.contains(uo.getIdDominio())) {
						idDomini.add(uo.getIdDominio());
					}
					
					if(uo.getIdUnita() != null) {
						idUO.add(uo.getIdUnita());
					}
				}
				filter.setIdDomini(idDomini);
				filter.setIdUo(idUO);
			}

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
			filter.setAbilitaFiltroCittadino(listaPendenzaDTO.isAbilitaFiltroCittadino());
			filter.setFilterSortList(listaPendenzaDTO.getFieldSortList());
			if(!listaPendenzaDTO.isOrderEnabled()) {
				filter.addFilterSort(filter.getDefaultFilterSortWrapperDesc());
			}
			if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
				filter.setCfCittadino(userDetails.getIdentificativo()); 
				filter.setAbilitaFiltroCittadino(true);
			}
			filter.setCodTipoVersamento(listaPendenzaDTO.getIdTipoVersamento());
			filter.setDivisione(listaPendenzaDTO.getDivisione());
			filter.setDirezione(listaPendenzaDTO.getDirezione()); 
			filter.setIuv(listaPendenzaDTO.getIuv()); 
			filter.setIuvOnumAvviso(listaPendenzaDTO.getIuvONumAvviso()); 
			
			long count = versamentiBD.count(filter);
			
			return new ListaPendenzeDTOResponse(count, new ArrayList<>());
		}finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public ListaPendenzeDTOResponse listaPendenzeConInformazioniIncasso(ListaPendenzeConInformazioniIncassoDTO listaPendenzaDTO) throws ServiceException,PendenzaNonTrovataException, NotAuthorizedException, NotAuthenticatedException{
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());
			return this.listaPendenzeConInformazioniIncasso(listaPendenzaDTO, bd);
		}finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public ListaPendenzeDTOResponse listaPendenzeConInformazioniIncasso(ListaPendenzeConInformazioniIncassoDTO listaPendenzaDTO, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		VersamentiIncassiBD versamentiBD = new VersamentiIncassiBD(bd);
		VersamentoIncassoFilter filter = versamentiBD.newFilter();

		filter.setIdDomini(listaPendenzaDTO.getIdDomini());
		filter.setIdTipiVersamento(listaPendenzaDTO.getIdTipiVersamento());
		
		if(listaPendenzaDTO.getUnitaOperative() != null) {
			List<Long> idDomini = new ArrayList<>();
			List<Long> idUO = new ArrayList<>();
			for (IdUnitaOperativa uo : listaPendenzaDTO.getUnitaOperative()) {
				if(uo.getIdDominio() != null && !idDomini.contains(uo.getIdDominio())) {
					idDomini.add(uo.getIdDominio());
				}
				
				if(uo.getIdUnita() != null) {
					idUO.add(uo.getIdUnita());
				}
			}
			filter.setIdDomini(idDomini);
			filter.setIdUo(idUO);
		}

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
		filter.setAbilitaFiltroCittadino(listaPendenzaDTO.isAbilitaFiltroCittadino());
		filter.setFilterSortList(listaPendenzaDTO.getFieldSortList());
		if(!listaPendenzaDTO.isOrderEnabled()) {
			filter.addFilterSort(filter.getDefaultFilterSortWrapperDesc());
		}
//		if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
			if(listaPendenzaDTO.getCfCittadino() != null) {
			filter.setCfCittadino(listaPendenzaDTO.getCfCittadino()); 
			filter.setAbilitaFiltroCittadino(true);
		}
		filter.setCodTipoVersamento(listaPendenzaDTO.getIdTipoVersamento());
		filter.setDivisione(listaPendenzaDTO.getDivisione());
		filter.setDirezione(listaPendenzaDTO.getDirezione());
		filter.setIuv(listaPendenzaDTO.getIuv());
		filter.setIuvOnumAvviso(listaPendenzaDTO.getIuvONumAvviso()); 

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
				versamentoIncasso.getTipoVersamentoDominio(versamentiBD);
				versamentoIncasso.getTipoVersamento(versamentiBD);
				List<SingoloVersamento> singoliVersamenti = versamentoIncasso.getSingoliVersamenti(versamentiBD);
				for (SingoloVersamento singoloVersamento : singoliVersamenti) {
					singoloVersamento.getCodContabilita(bd);
					singoloVersamento.getIbanAccredito(bd);
					singoloVersamento.getTipoContabilita(bd);
					singoloVersamento.getTributo(bd);

				}
				elem.setLstSingoliVersamenti(singoliVersamenti);

				resList.add(elem);
			}
		} 

		return new ListaPendenzeDTOResponse(count, resList);
	}

	public ListaPendenzeDTOResponse listaPendenze(ListaPendenzeDTO listaPendenzaDTO) throws ServiceException,PendenzaNonTrovataException, NotAuthorizedException, NotAuthenticatedException{
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());
			//			return listaPendenzaDTO.isInfoIncasso() ? this.listaPendenzeConInformazioniIncasso(listaPendenzaDTO, bd) : this.listaPendenze(listaPendenzaDTO, bd);
			return this.listaPendenze(listaPendenzaDTO, bd);
		}finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public ListaPendenzeDTOResponse listaPendenze(ListaPendenzeDTO listaPendenzaDTO, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {
		GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(listaPendenzaDTO.getUser());

		VersamentiBD versamentiBD = new VersamentiBD(bd);
		VersamentoFilter filter = versamentiBD.newFilter();

		filter.setIdDomini(listaPendenzaDTO.getIdDomini());
		filter.setIdTipiVersamento(listaPendenzaDTO.getIdTipiVersamento());

		filter.setOffset(listaPendenzaDTO.getOffset());
		filter.setLimit(listaPendenzaDTO.getLimit());
		filter.setDataInizio(listaPendenzaDTO.getDataDa());
		filter.setDataFine(listaPendenzaDTO.getDataA());
		if(listaPendenzaDTO.getStato()!=null) {
			try {
				it.govpay.bd.model.Versamento.StatoVersamento statoVersamento = null;
				StatoPendenza statoPendenza = StatoPendenza.valueOf(listaPendenzaDTO.getStato());

				//TODO mapping...piu' stati?
				switch(statoPendenza) {
				case ANNULLATA: statoVersamento = it.govpay.bd.model.Versamento.StatoVersamento.ANNULLATO;
				break;
				case ESEGUITA: statoVersamento = it.govpay.bd.model.Versamento.StatoVersamento.ESEGUITO;
				break;
				case ESEGUITA_PARZIALE: statoVersamento = it.govpay.bd.model.Versamento.StatoVersamento.PARZIALMENTE_ESEGUITO;
				break;
				case NON_ESEGUITA: statoVersamento = it.govpay.bd.model.Versamento.StatoVersamento.NON_ESEGUITO;
				break;
				case SCADUTA: statoVersamento = it.govpay.bd.model.Versamento.StatoVersamento.NON_ESEGUITO; //TODO aggiungere data scadenza < ora
				break;
				//				case INCASSATA: statoVersamento = it.govpay.bd.viste.model.VersamentoIncasso.StatoVersamento.INCASSATO;
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
		filter.setCodTipoVersamento(listaPendenzaDTO.getIdTipoVersamento());
		filter.setDivisione(listaPendenzaDTO.getDivisione());
		filter.setDirezione(listaPendenzaDTO.getDirezione()); 
		filter.setIuv(listaPendenzaDTO.getIuv()); 
		filter.setIuvOnumAvviso(listaPendenzaDTO.getIuvONumAvviso()); 

		long count = versamentiBD.count(filter);

		List<LeggiPendenzaDTOResponse> resList = new ArrayList<>();
		if(count > 0) {
			List<Versamento> findAll = versamentiBD.findAll(filter);

			for (Versamento versamento : findAll) {
				LeggiPendenzaDTOResponse elem = new LeggiPendenzaDTOResponse();

				VersamentoIncasso versamentoIncasso = VersamentoIncassoConverter.fromVersamento(versamento);

				elem.setVersamentoIncasso(versamentoIncasso);
				elem.setApplicazione(versamentoIncasso.getApplicazione(versamentiBD));
				elem.setDominio(versamentoIncasso.getDominio(versamentiBD));
				elem.setUnitaOperativa(versamentoIncasso.getUo(versamentiBD));
				versamentoIncasso.getTipoVersamentoDominio(versamentiBD);
				versamentoIncasso.getTipoVersamento(versamentiBD);
				List<SingoloVersamento> singoliVersamenti = versamentoIncasso.getSingoliVersamenti(versamentiBD);
				for (SingoloVersamento singoloVersamento : singoliVersamenti) {
					singoloVersamento.getCodContabilita(bd);
					singoloVersamento.getIbanAccredito(bd);
					singoloVersamento.getTipoContabilita(bd);
					singoloVersamento.getTributo(bd);

				}
				elem.setLstSingoliVersamenti(singoliVersamenti);

				resList.add(elem);
			}
		} 

		return new ListaPendenzeDTOResponse(count, resList);
	}

	public LeggiPendenzaDTOResponse leggiPendenzaConInformazioniIncasso(LeggiPendenzaDTO leggiPendenzaDTO) throws ServiceException,PendenzaNonTrovataException, NotAuthorizedException, NotAuthenticatedException, GovPayException{
		LeggiPendenzaDTOResponse response = new LeggiPendenzaDTOResponse();

		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());
			String idA2A = leggiPendenzaDTO.getCodA2A();
			String idPendenza = leggiPendenzaDTO.getCodPendenza();
			response = _leggiPendenzaConInfoIncasso(idA2A,idPendenza, response, bd);
		} catch (NotFoundException e) {
			throw new PendenzaNonTrovataException(e.getMessage(), e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}

		return response;
	}

	private LeggiPendenzaDTOResponse _leggiPendenzaConInfoIncasso(String idA2A, String idPendenza, LeggiPendenzaDTOResponse response, BasicBD bd)
			throws NotFoundException, ServiceException {

		VersamentiIncassiBD versamentiBD = new VersamentiIncassiBD(bd);
		VersamentoIncasso versamentoIncasso = versamentiBD.getVersamento(AnagraficaManager.getApplicazione(versamentiBD, idA2A).getId(), idPendenza);


		Dominio dominio = versamentoIncasso.getDominio(versamentiBD);
		TipoVersamento tipoVersamento = versamentoIncasso.getTipoVersamento(versamentiBD);
		versamentoIncasso.getTipoVersamentoDominio(versamentiBD);

		response.setVersamentoIncasso(versamentoIncasso);
		response.setApplicazione(versamentoIncasso.getApplicazione(versamentiBD));

		response.setDominio(dominio);
		response.setTipoVersamento(tipoVersamento);
		response.setUnitaOperativa(versamentoIncasso.getUo(versamentiBD));
		List<SingoloVersamento> singoliVersamenti = versamentoIncasso.getSingoliVersamenti(versamentiBD);
		response.setLstSingoliVersamenti(singoliVersamenti);
		for (SingoloVersamento singoloVersamento : singoliVersamenti) {
			populateSingoloVersamento(bd, singoloVersamento);
		}

		PagamentiPortaleBD pagamentiPortaleBD = new PagamentiPortaleBD(bd);
		PagamentoPortaleFilter newFilter = pagamentiPortaleBD.newFilter();
		List<PagamentoPortaleVersamento> allPagPortVers = pagamentiPortaleBD.getAllPagPortVers(versamentoIncasso.getId());
		List<Long> idPagamentiPortale = new ArrayList<>();

		if(allPagPortVers != null && !allPagPortVers.isEmpty()) {
			for (PagamentoPortaleVersamento pagamentoPortaleVersamento : allPagPortVers) {
				idPagamentiPortale.add(pagamentoPortaleVersamento.getIdPagamentoPortale().getId());
			}

			newFilter.setIdPagamentiPortale(idPagamentiPortale);
			List<PagamentoPortale> findAll = pagamentiPortaleBD.findAll(newFilter);
			response.setPagamenti(findAll);
		}

		RptBD rptBD = new RptBD(bd);
		RptFilter newFilter2 = rptBD.newFilter();
		newFilter2.setIdPendenza(versamentoIncasso.getCodVersamentoEnte());
		newFilter2.setCodApplicazione(versamentoIncasso.getApplicazione(bd).getCodApplicazione());
		long count = rptBD.count(newFilter2);

		if(count > 0) {
			List<Rpt> findAll = rptBD.findAll(newFilter2);

			for (Rpt rpt : findAll) {
				rpt.getVersamentoIncasso(bd);
				rpt.getVersamentoIncasso(bd).getDominio(bd);
				rpt.getVersamentoIncasso(bd).getUo(bd);
				rpt.getVersamentoIncasso(bd).getApplicazione(bd);
				rpt.getVersamentoIncasso(bd).getTipoVersamento(versamentiBD);
				rpt.getVersamentoIncasso(bd).getTipoVersamentoDominio(versamentiBD);
			}

			response.setRpts(findAll);
		}

		return response;
	}

	public LeggiPendenzaDTOResponse leggiPendenza(LeggiPendenzaDTO leggiPendenzaDTO) throws ServiceException,PendenzaNonTrovataException, NotAuthorizedException, NotAuthenticatedException, GovPayException{
		LeggiPendenzaDTOResponse response = new LeggiPendenzaDTOResponse();

		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());
			String idA2A = leggiPendenzaDTO.getCodA2A();
			String idPendenza = leggiPendenzaDTO.getCodPendenza();
			response = _leggiPendenza(idA2A,idPendenza, response, bd);
		} catch (NotFoundException e) {
			throw new PendenzaNonTrovataException(e.getMessage(), e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}

		return response;
	}

	private LeggiPendenzaDTOResponse _leggiPendenza(String idA2A, String idPendenza, LeggiPendenzaDTOResponse response, BasicBD bd) throws NotFoundException, ServiceException {
		VersamentiBD versamentiBD = new VersamentiBD(bd);
		Versamento versamento = versamentiBD.getVersamento(AnagraficaManager.getApplicazione(versamentiBD, idA2A).getId(), idPendenza);
		VersamentoIncasso versamentoIncasso = VersamentoIncassoConverter.fromVersamento(versamento); 

		Dominio dominio = versamentoIncasso.getDominio(versamentiBD);
		TipoVersamento tipoVersamento = versamentoIncasso.getTipoVersamento(versamentiBD);
		versamentoIncasso.getTipoVersamentoDominio(versamentiBD);

		response.setVersamentoIncasso(versamentoIncasso);
		response.setApplicazione(versamentoIncasso.getApplicazione(versamentiBD));
		response.setTipoVersamento(tipoVersamento);
		response.setDominio(dominio);
		response.setUnitaOperativa(versamentoIncasso.getUo(versamentiBD));
		List<SingoloVersamento> singoliVersamenti = versamentoIncasso.getSingoliVersamenti(versamentiBD);
		response.setLstSingoliVersamenti(singoliVersamenti);
		for (SingoloVersamento singoloVersamento : singoliVersamenti) {
			populateSingoloVersamento(bd, singoloVersamento);
		}

		PagamentiPortaleBD pagamentiPortaleBD = new PagamentiPortaleBD(bd);
		PagamentoPortaleFilter newFilter = pagamentiPortaleBD.newFilter();
		List<PagamentoPortaleVersamento> allPagPortVers = pagamentiPortaleBD.getAllPagPortVers(versamentoIncasso.getId());
		List<Long> idPagamentiPortale = new ArrayList<>();

		if(allPagPortVers != null && !allPagPortVers.isEmpty()) {
			for (PagamentoPortaleVersamento pagamentoPortaleVersamento : allPagPortVers) {
				idPagamentiPortale.add(pagamentoPortaleVersamento.getIdPagamentoPortale().getId());
			}

			newFilter.setIdPagamentiPortale(idPagamentiPortale);
			List<PagamentoPortale> findAll = pagamentiPortaleBD.findAll(newFilter);
			response.setPagamenti(findAll);
		}

		RptBD rptBD = new RptBD(bd);
		RptFilter newFilter2 = rptBD.newFilter();
		newFilter2.setIdPendenza(versamentoIncasso.getCodVersamentoEnte());
		newFilter2.setCodApplicazione(versamentoIncasso.getApplicazione(bd).getCodApplicazione());
		long count = rptBD.count(newFilter2);

		if(count > 0) {
			List<Rpt> findAll = rptBD.findAll(newFilter2);

			for (Rpt rpt : findAll) {
				rpt.getVersamento(bd).getDominio(bd);
				rpt.getVersamento(bd).getUo(bd);
				rpt.getVersamento(bd).getApplicazione(bd);
				rpt.getVersamento(bd).getTipoVersamento(versamentiBD);
				rpt.getVersamento(bd).getTipoVersamentoDominio(versamentiBD);
			}

			response.setRpts(findAll);
		}

		return response;
	}

	private void populateSingoloVersamento(BasicBD bd, SingoloVersamento singoloVersamento) throws ServiceException, NotFoundException {
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

	private void populatePagamento(Pagamento pagamento, BasicBD bd) throws ServiceException, NotFoundException {
		pagamento.getSingoloVersamento(bd).getVersamento(bd).getApplicazione(bd);
		pagamento.getSingoloVersamento(bd).getVersamento(bd).getUo(bd);
		pagamento.getRpt(bd);
		pagamento.getDominio(bd);
		pagamento.getRendicontazioni(bd);
		pagamento.getIncasso(bd);
	}


	private void validaPath(String path) throws ValidationException {
		if(!Arrays.asList(OPERAZIONI_CONSENTITE_PENDENZE).contains(path))
			throw new ValidationException(UtenzaPatchUtils.PATH_NON_VALIDO);
	}

	private void validaValue(Object object) throws ValidationException {
		if(object == null)
			throw new ValidationException(UtenzaPatchUtils.VALUE_NON_VALIDO_PER_IL_PATH);

		String value = (String) object;
		if(StringUtils.isEmpty(value))
			throw new ValidationException(UtenzaPatchUtils.VALUE_NON_VALIDO_PER_IL_PATH);
	}


	public LeggiPendenzaDTOResponse patch(PatchPendenzaDTO patchPendenzaDTO) throws PendenzaNonTrovataException, GovPayException, NotAuthorizedException, NotAuthenticatedException, ValidationException{

		LeggiPendenzaDTOResponse response = new LeggiPendenzaDTOResponse();
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());

			VersamentiBD versamentiBD = new VersamentiBD(bd);
			String idA2A = patchPendenzaDTO.getIdA2a();
			String idPendenza = patchPendenzaDTO.getIdPendenza();
			it.govpay.bd.model.Versamento versamentoLetto = versamentiBD.getVersamento(AnagraficaManager.getApplicazione(bd, idA2A).getId(), idPendenza);

			// controllo che il dominio e tipo versamento siano autorizzati
			if(!AuthorizationManager.isTipoVersamentoDominioAuthorized(patchPendenzaDTO.getUser(), versamentoLetto.getDominio(bd).getCodDominio(), versamentoLetto.getTipoVersamento(bd).getCodTipoVersamento())) {
				throw AuthorizationManager.toNotAuthorizedException(patchPendenzaDTO.getUser(), versamentoLetto.getDominio(bd).getCodDominio(), versamentoLetto.getTipoVersamento(bd).getCodTipoVersamento());
			}

			for(PatchOp op: patchPendenzaDTO.getOp()) {

				// validazione del path richiesto
				this.validaPath(op.getPath());

				// validazione del value
				this.validaValue(op.getValue());

				if(PATH_STATO.equals(op.getPath())) {
					String motivazione = null;
					//cerco il patch di descrizione stato
					for(PatchOp op2: patchPendenzaDTO.getOp()) {
						if(PATH_DESCRIZIONE_STATO.equals(op2.getPath())) {
							try { motivazione = (String) op2.getValue(); } catch (Exception e) {}
						}
					}
					this.patchStato(patchPendenzaDTO.getUser(), versamentoLetto, op, motivazione, bd);
				}

				if(PATH_DESCRIZIONE_STATO.equals(op.getPath())) {
					this.patchDescrizioneStato(versamentoLetto, op);
				}

				if(PATH_ACK.equals(op.getPath())) {
					this.patchAck(versamentoLetto, op);
				}

				if(PATH_NOTA.equals(op.getPath())) {
					if(!op.getOp().equals(OpEnum.ADD)) {
						throw new ValidationException(MessageFormat.format(UtenzaPatchUtils.OP_XX_NON_VALIDO_PER_IL_PATH_YY, op.getOp(), op.getPath()));
					}

					LinkedHashMap<?,?> map = (LinkedHashMap<?,?>) op.getValue();
					versamentoLetto.setDescrizioneStato((String)map.get(UtenzaPatchUtils.OGGETTO_NOTA_KEY));
				}

				// Casi di operazioni patch che implicano una nota:
				// ANNULLAMENTO
				//				if(PATH_DESCRIZIONE_STATO.equals(op.getPath()) && PATH_STATO.equals(op.getPath()) && this.getNuovoStatoVersamento(op).equals(StatoVersamento.ANNULLATO)) {
				//				}
				// RIPRISTINO
				//				if(PATH_DESCRIZIONE_STATO.equals(op.getPath()) && PATH_STATO.equals(op.getPath()) && this.getNuovoStatoVersamento(op).equals(StatoVersamento.NON_ESEGUITO)) {
				//				}

			}

			versamentoLetto.setDataUltimoAggiornamento(new Date());
			versamentiBD.updateVersamento(versamentoLetto);

			// restituisco il versamento
			if(patchPendenzaDTO.isInfoIncasso()) {
				response = this._leggiPendenzaConInfoIncasso(idA2A, idPendenza, response, bd);
			} else {
				response = this._leggiPendenza(idA2A, idPendenza, response, bd);
			}

			return response;

		} catch (ServiceException e) {
			throw new GovPayException(e);
		} catch (NotFoundException e) {
			throw new PendenzaNonTrovataException(e.getMessage(), e);
		} catch (IOException e) {
			throw new GovPayException(e);
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

		if(StringUtils.isEmpty(descrizioneStato))
			throw new ValidationException(MessageFormat.format(UtenzaPatchUtils.VALUE_NON_VALIDO_PER_IL_PATH_XX, op.getPath()));

		versamentoLetto.setDescrizioneStato(descrizioneStato);
	}

	//	private Evento patchStato(Authentication authentication, it.govpay.bd.model.Versamento versamentoLetto, PatchOp op, String motivazione, BasicBD bd) throws ValidationException, IOException {
	private void patchStato(Authentication authentication, it.govpay.bd.model.Versamento versamentoLetto, PatchOp op, String motivazione, BasicBD bd) throws ValidationException, IOException {
		if(!op.getOp().equals(OpEnum.REPLACE)) {
			throw new ValidationException(MessageFormat.format(UtenzaPatchUtils.OP_XX_NON_VALIDO_PER_IL_PATH_YY, op.getOp(), op.getPath()));
		}

		StatoVersamento nuovoStato = this.getNuovoStatoVersamento(op);

		//		GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(authentication);
		//		Evento eventoUtente = new Evento();

		//		eventoUtente.setCategoriaEvento(CategoriaEvento.UTENTE);
		//		eventoUtente.setRuoloEvento(RuoloEvento.CLIENT);
		//		eventoUtente.setTipoEvento("patchPendenza"); 
		//		eventoUtente.setEsitoEvento(EsitoEvento.OK);
		//		eventoUtente.setSottotipoEsito(200);
		//		eventoUtente.setData(new Date());
		//		eventoUtente.setDettaglioEsito(motivazione);
		//		DettaglioRichiesta dettaglioRichiesta = new DettaglioRichiesta();
		//		dettaglioRichiesta.setPrincipal(userDetails.getUtenza().getPrincipal());
		//		dettaglioRichiesta.setUtente(userDetails.getIdentificativo());
		//		dettaglioRichiesta.setDataOraRichiesta(new Date());
		//		dettaglioRichiesta.setPayload(UtenzaPatchUtils.getDettaglioAsString(op));
		//		eventoUtente.setDettaglioRichiesta(dettaglioRichiesta);

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

				//				eventoUtente.setDettaglioEsito("Pendenza annullata");
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

				//				eventoUtente.setDettaglioEsito("Pendenza ripristinata");
			} else {
				throw new ValidationException("Non e' consentito aggiornare lo stato di una pendenza ad NON_ESEGUITO da uno stato diverso da ANNULLATO");
			}
			break;
		default:
			throw new ValidationException(MessageFormat.format(NON_E_CONSENTITO_AGGIORNARE_LO_STATO_DI_UNA_PENDENZA_AD_0, nuovoStato.name()));
		}



		//		return eventoUtente;
	}

	private void patchAck(it.govpay.bd.model.Versamento versamentoLetto, PatchOp op) throws ValidationException {
		if(!op.getOp().equals(OpEnum.REPLACE)) {
			throw new ValidationException(MessageFormat.format(UtenzaPatchUtils.OP_XX_NON_VALIDO_PER_IL_PATH_YY, op.getOp(), op.getPath()));
		}

		Boolean ackVersamento = (Boolean) op.getValue();
		versamentoLetto.setAck(ackVersamento != null ? ackVersamento.booleanValue() : false);
	}

	//	private void patchNxota(Authentication authentication, it.govpay.bd.model.Versamento versamentoLetto, PatchOp op, BasicBD bd) throws ValidationException, ServiceException, NotFoundException, IOException {  
	//		
	////	private Evento patchNota(Authentication authentication, it.govpay.bd.model.Versamento versamentoLetto, PatchOp op, BasicBD bd) throws ValidationException, ServiceException, NotFoundException, IOException {  
	//		if(!op.getOp().equals(OpEnum.ADD)) {
	//			throw new ValidationException(MessageFormat.format(UtenzaPatchUtils.OP_XX_NON_VALIDO_PER_IL_PATH_YY, op.getOp(), op.getPath()));
	//		}
	//		
	//		LinkedHashMap<?,?> map = (LinkedHashMap<?,?>) op.getValue();
	//		versamentoLetto.setDescrizioneStato((String)map.get(UtenzaPatchUtils.OGGETTO_NOTA_KEY));
	////	 	return UtenzaPatchUtils.getNotaFromPatch(authentication, op, bd); 
	//	}

	private StatoVersamento getNuovoStatoVersamento(PatchOp op) throws ValidationException {
		String nuovoStatoPendenzaValue = (String) op.getValue();
		StatoPendenza nuovoStatoPendenza = StatoPendenza.fromValue(nuovoStatoPendenzaValue);

		if(nuovoStatoPendenza == null && StringUtils.isNotEmpty(nuovoStatoPendenzaValue))
			throw new ValidationException(MessageFormat.format(NUOVO_STATO_PENDENZA_NON_VALIDO, op.getPath()));

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

	public PutPendenzaDTOResponse createOrUpdate(PutPendenzaDTO putVersamentoDTO) throws GovPayException, NotAuthorizedException, NotAuthenticatedException, ValidationException{ 
		PutPendenzaDTOResponse createOrUpdatePendenzaResponse = new PutPendenzaDTOResponse();
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());
			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento(bd);

			Versamento chiediVersamento = versamentoBusiness.chiediVersamento(putVersamentoDTO.getVersamento());

			Applicazione applicazioniBD = new Applicazione(bd);
			GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(putVersamentoDTO.getUser());
			it.govpay.bd.model.Applicazione applicazioneAutenticata = details.getApplicazione();
			if(applicazioneAutenticata != null) 
				applicazioniBD.autorizzaApplicazione(putVersamentoDTO.getVersamento().getCodApplicazione(), applicazioneAutenticata, bd);

			createOrUpdatePendenzaResponse.setCreated(false);
			VersamentiBD versamentiBD = new VersamentiBD(bd);

			try {
				versamentiBD.getVersamento(AnagraficaManager.getApplicazione(versamentiBD, putVersamentoDTO.getVersamento().getCodApplicazione()).getId(), putVersamentoDTO.getVersamento().getCodVersamentoEnte());
			}catch(NotFoundException e) {
				createOrUpdatePendenzaResponse.setCreated(true);
			}

			boolean generaIuv = chiediVersamento.getNumeroAvviso() == null && chiediVersamento.getSingoliVersamenti(bd).size() == 1;
			versamentoBusiness.caricaVersamento(chiediVersamento, generaIuv, true);

			// restituisco il versamento creato
			createOrUpdatePendenzaResponse.setVersamento(chiediVersamento);
			createOrUpdatePendenzaResponse.setDominio(chiediVersamento.getDominio(bd));
			createOrUpdatePendenzaResponse.setUo(chiediVersamento.getUo(bd));

			Iuv iuv = IuvUtils.toIuv(chiediVersamento, chiediVersamento.getApplicazione(bd), chiediVersamento.getDominio(bd));

			createOrUpdatePendenzaResponse.setBarCode(iuv.getBarCode() != null ? new String(iuv.getBarCode()) : null);
			createOrUpdatePendenzaResponse.setQrCode(iuv.getQrCode() != null ? new String(iuv.getQrCode()) : null);

			if(putVersamentoDTO.isStampaAvviso()) {
				it.govpay.core.business.AvvisoPagamento avvisoBD = new it.govpay.core.business.AvvisoPagamento(bd);
				PrintAvvisoDTO printAvvisoDTO = new PrintAvvisoDTO();
				printAvvisoDTO.setUpdate(!createOrUpdatePendenzaResponse.isCreated());
				printAvvisoDTO.setCodDominio(chiediVersamento.getDominio(bd).getCodDominio());
				printAvvisoDTO.setIuv(iuv.getIuv());
				printAvvisoDTO.setVersamento(chiediVersamento); 
				PrintAvvisoDTOResponse printAvvisoDTOResponse = avvisoBD.printAvviso(printAvvisoDTO);
				createOrUpdatePendenzaResponse.setPdf(Base64.getEncoder().encodeToString(printAvvisoDTOResponse.getAvviso().getPdf()));
			} else { // non devo fare la stampa.
				if(!createOrUpdatePendenzaResponse.isCreated()) {
					// se ho fatto l'update della pendenza e non voglio aggiornare la stampa la cancello cosi quando verra' letta la prima volta si aggiornera' da sola
					it.govpay.core.business.AvvisoPagamento avvisoBD = new it.govpay.core.business.AvvisoPagamento(bd);
					avvisoBD.cancellaAvviso(chiediVersamento);
				}
			}

		} catch (ServiceException e) {
			throw new GovPayException(e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
		return createOrUpdatePendenzaResponse;
	}

	public PutPendenzaDTOResponse createOrUpdateCustom(PutPendenzaDTO putVersamentoDTO) throws GovPayException, 
		NotAuthorizedException, NotAuthenticatedException, ValidationException, DominioNonTrovatoException, TipoVersamentoNonTrovatoException, EcException, UnitaOperativaNonTrovataException{ 
		PutPendenzaDTOResponse createOrUpdatePendenzaResponse = new PutPendenzaDTOResponse();
		BasicBD bd = null;
		try {
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());

			Dominio dominio = null;
			String codDominio = putVersamentoDTO.getCodDominio(); 
			try {
				dominio = AnagraficaManager.getDominio(bd, codDominio);
			} catch (NotFoundException e1) {
				throw new DominioNonTrovatoException("Dominio ["+codDominio+"] inesistente.", e1);
			}
			
			UnitaOperativa uo = null;
			String codUo = putVersamentoDTO.getCodUo();
			if(codUo != null) {
				try {
					uo = AnagraficaManager.getUnitaOperativa(bd, dominio.getId(), codUo);
				} catch (NotFoundException e1) {
					throw new UnitaOperativaNonTrovataException("Unita' Operativa ["+codUo+"] inesistente per il Dominio ["+codDominio+"].", e1);
				}
			}
			
			// lettura della configurazione TipoVersamentoDominio
			TipoVersamentoDominio tipoVersamentoDominio = null;
			String codTipoVersamento = putVersamentoDTO.getCodTipoVersamento();
			try {
				tipoVersamentoDominio = AnagraficaManager.getTipoVersamentoDominio(bd, dominio.getId(), codTipoVersamento);
			} catch (NotFoundException e1) {
				throw new TipoVersamentoNonTrovatoException("TipoPendenza ["+codTipoVersamento+"] inesistente per il Dominio ["+codDominio+"].", e1);
			}

			this.log.debug("Caricamento pendenza modello 4: elaborazione dell'input ricevuto in corso...");
			String json = putVersamentoDTO.getCustomReq();
			VersamentoUtils.validazioneInputVersamentoModello4(this.log, json, tipoVersamentoDominio);

			MultivaluedMap<String, String> queryParameters = putVersamentoDTO.getQueryParameters(); 
			MultivaluedMap<String, String> pathParameters = putVersamentoDTO.getPathParameters();
			Map<String, String> headers = putVersamentoDTO.getHeaders();
			String trasformazioneDefinizione = tipoVersamentoDominio.getTrasformazioneDefinizione();
			if(trasformazioneDefinizione != null && tipoVersamentoDominio.getTrasformazioneTipo() != null) {
				json = VersamentoUtils.trasformazioneInputVersamentoModello4(log, dominio, tipoVersamentoDominio, uo, json, queryParameters, pathParameters, headers, trasformazioneDefinizione);
			}
			Versamento chiediVersamento = null;
			String codApplicazione = tipoVersamentoDominio.getCodApplicazione();
			if(codApplicazione != null) {
				chiediVersamento =  VersamentoUtils.inoltroInputVersamentoModello4(log, codDominio, codTipoVersamento, codUo, json, bd, codApplicazione);
			} else {
				PendenzaPost pendenzaPost = PendenzaPost.parse(json);
				new PendenzaPostValidator(pendenzaPost).validate();
				
				it.govpay.core.dao.commons.Versamento versamentoCommons = TracciatiConverter.getVersamentoFromPendenza(pendenzaPost);
				((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdPendenza(versamentoCommons.getCodVersamentoEnte());
				((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdA2A(versamentoCommons.getCodApplicazione());
				it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento(bd);
				chiediVersamento = versamentoBusiness.chiediVersamento(versamentoCommons);
			}
			
			Applicazione applicazioniBD = new Applicazione(bd);
			GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(putVersamentoDTO.getUser());
			it.govpay.bd.model.Applicazione applicazioneAutenticata = details.getApplicazione();
			if(applicazioneAutenticata != null) 
				applicazioniBD.autorizzaApplicazione(chiediVersamento.getApplicazione(bd).getCodApplicazione(), applicazioneAutenticata, bd);

			createOrUpdatePendenzaResponse.setCreated(false);
			VersamentiBD versamentiBD = new VersamentiBD(bd);

			try {
				versamentiBD.getVersamento(AnagraficaManager.getApplicazione(versamentiBD, chiediVersamento.getApplicazione(bd).getCodApplicazione()).getId(), chiediVersamento.getCodVersamentoEnte());
			}catch(NotFoundException e) {
				createOrUpdatePendenzaResponse.setCreated(true);
			}
			
			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento(bd);
			boolean generaIuv = chiediVersamento.getNumeroAvviso() == null && chiediVersamento.getSingoliVersamenti(bd).size() == 1;
			versamentoBusiness.caricaVersamento(chiediVersamento, generaIuv, true);

			// restituisco il versamento creato
			createOrUpdatePendenzaResponse.setVersamento(chiediVersamento);
			createOrUpdatePendenzaResponse.setDominio(chiediVersamento.getDominio(bd));
			createOrUpdatePendenzaResponse.setUo(chiediVersamento.getUo(bd));

			Iuv iuv = IuvUtils.toIuv(chiediVersamento, chiediVersamento.getApplicazione(bd), chiediVersamento.getDominio(bd));

			createOrUpdatePendenzaResponse.setBarCode(iuv.getBarCode() != null ? new String(iuv.getBarCode()) : null);
			createOrUpdatePendenzaResponse.setQrCode(iuv.getQrCode() != null ? new String(iuv.getQrCode()) : null);

			if(putVersamentoDTO.isStampaAvviso()) {
				it.govpay.core.business.AvvisoPagamento avvisoBD = new it.govpay.core.business.AvvisoPagamento(bd);
				PrintAvvisoDTO printAvvisoDTO = new PrintAvvisoDTO();
				printAvvisoDTO.setUpdate(!createOrUpdatePendenzaResponse.isCreated());
				printAvvisoDTO.setCodDominio(chiediVersamento.getDominio(bd).getCodDominio());
				printAvvisoDTO.setIuv(iuv.getIuv());
				printAvvisoDTO.setVersamento(chiediVersamento); 
				PrintAvvisoDTOResponse printAvvisoDTOResponse = avvisoBD.printAvviso(printAvvisoDTO);
				createOrUpdatePendenzaResponse.setPdf(Base64.getEncoder().encodeToString(printAvvisoDTOResponse.getAvviso().getPdf()));
			} else { // non devo fare la stampa.
				if(!createOrUpdatePendenzaResponse.isCreated()) {
					// se ho fatto l'update della pendenza e non voglio aggiornare la stampa la cancello cosi quando verra' letta la prima volta si aggiornera' da sola
					it.govpay.core.business.AvvisoPagamento avvisoBD = new it.govpay.core.business.AvvisoPagamento(bd);
					avvisoBD.cancellaAvviso(chiediVersamento);
				}
			}

		} catch (ServiceException e) {
			throw new GovPayException(e);
		}  finally {
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
			bd = BasicBD.newInstance(ContextThreadLocal.get().getTransactionId());

			VersamentiBD versamentiBD = new VersamentiBD(bd);
			versamento = versamentiBD.getVersamentoFromDominioNumeroAvviso(leggiPendenzaDTO.getIdDominio(), leggiPendenzaDTO.getNumeroAvviso()); 

			Dominio dominio = versamento.getDominio(versamentiBD);
			TipoVersamento tipoVersamento = versamento.getTipoVersamento(versamentiBD);

			// controllo che il dominio e tipo versamento siano autorizzati
			if(!AuthorizationManager.isTipoVersamentoDominioAuthorized(leggiPendenzaDTO.getUser(), dominio.getCodDominio(), tipoVersamento.getCodTipoVersamento())) {
				throw AuthorizationManager.toNotAuthorizedException(leggiPendenzaDTO.getUser(),  dominio.getCodDominio(), tipoVersamento.getCodTipoVersamento());
			}

			it.govpay.core.business.AvvisoPagamento avvisoBD = new it.govpay.core.business.AvvisoPagamento(bd);
			PrintAvvisoDTO printAvvisoDTO = new PrintAvvisoDTO();
			printAvvisoDTO.setCodDominio(versamento.getDominio(bd).getCodDominio());
			printAvvisoDTO.setIuv(versamento.getIuvVersamento());
			printAvvisoDTO.setVersamento(versamento); 
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
