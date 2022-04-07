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
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.serialization.IOException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.springframework.security.core.Authentication;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Dominio;
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
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.beans.tracciati.PendenzaPost;
import it.govpay.core.business.Applicazione;
import it.govpay.core.business.model.Iuv;
import it.govpay.core.business.model.PrintAvvisoDTOResponse;
import it.govpay.core.business.model.PrintAvvisoVersamentoDTO;
import it.govpay.core.dao.anagrafica.exception.ApplicazioneNonTrovataException;
import it.govpay.core.dao.anagrafica.exception.DominioNonTrovatoException;
import it.govpay.core.dao.anagrafica.exception.TipoVersamentoNonTrovatoException;
import it.govpay.core.dao.anagrafica.exception.UnitaOperativaNonTrovataException;
import it.govpay.core.dao.anagrafica.utils.UtenzaPatchUtils;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeSmartOrderDTO;
import it.govpay.core.dao.pagamenti.dto.PatchPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.PutPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.PutPendenzaDTOResponse;
import it.govpay.core.dao.pagamenti.exception.PendenzaNonTrovataException;
import it.govpay.core.exceptions.EcException;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.exceptions.UnprocessableEntityException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.IuvUtils;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.TracciatiConverter;
import it.govpay.core.utils.VersamentoUtils;
import it.govpay.core.utils.tracciati.validator.PendenzaPostValidator;
import it.govpay.model.PatchOp;
import it.govpay.model.PatchOp.OpEnum;
import it.govpay.model.StatoPendenza;
import it.govpay.model.TipoVersamento;
import it.govpay.model.Utenza.TIPO_UTENZA;
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

	public ListaPendenzeDTOResponse countPendenze(ListaPendenzeDTO listaPendenzaDTO) throws ServiceException,PendenzaNonTrovataException, NotAuthorizedException, NotAuthenticatedException, ValidationException{
		VersamentiBD versamentiBD = null;

		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(listaPendenzaDTO.getUser());
			versamentiBD = new VersamentiBD(configWrapper);
			VersamentoFilter filter = versamentiBD.newFilter();

			filter.setIdDomini(listaPendenzaDTO.getIdDomini());
			filter.setIdTipiVersamento(listaPendenzaDTO.getIdTipiVersamento());
			filter.setEseguiCountConLimit(listaPendenzaDTO.isEseguiCountConLimit());

			if(listaPendenzaDTO.getUnitaOperative() != null) {
				filter.setIdUo(listaPendenzaDTO.getUnitaOperative());
			}

			filter.setOffset(listaPendenzaDTO.getOffset());
			filter.setLimit(listaPendenzaDTO.getLimit());
			filter.setDataInizio(listaPendenzaDTO.getDataDa());
			filter.setDataFine(listaPendenzaDTO.getDataA());
			if(listaPendenzaDTO.getStato()!=null) {
				try {
					it.govpay.bd.model.Versamento.StatoVersamento statoVersamento = null;

					switch(listaPendenzaDTO.getStato()) {
					case ANNULLATA: statoVersamento = it.govpay.bd.model.Versamento.StatoVersamento.ANNULLATO;
					break;
					case ESEGUITA: statoVersamento = it.govpay.bd.model.Versamento.StatoVersamento.ESEGUITO;
					break;
					case ESEGUITA_PARZIALE: statoVersamento = it.govpay.bd.model.Versamento.StatoVersamento.PARZIALMENTE_ESEGUITO;
					break;
					case NON_ESEGUITA: {
						statoVersamento = it.govpay.bd.model.Versamento.StatoVersamento.NON_ESEGUITO;
						filter.setAbilitaFiltroNonScaduto(true);
					}
					break;
					case SCADUTA: {
						statoVersamento = it.govpay.bd.model.Versamento.StatoVersamento.NON_ESEGUITO;
						filter.setAbilitaFiltroScaduto(true);
					}
					break;
					case INCASSATA: statoVersamento = it.govpay.bd.model.Versamento.StatoVersamento.INCASSATO;
					default:
						break;

					}
					filter.setStatoVersamento(statoVersamento);
				} catch(Exception e) {
					return new ListaPendenzeDTOResponse(0L, new ArrayList<LeggiPendenzaDTOResponse>());
				}
			}
			filter.setCodDominio(listaPendenzaDTO.getIdDominio() );
			filter.setCodPagamentoPortale(listaPendenzaDTO.getIdPagamento());
			filter.setCodUnivocoDebitore(listaPendenzaDTO.getIdDebitore());
			filter.setCodApplicazione(listaPendenzaDTO.getIdA2A());
			filter.setCodVersamento(listaPendenzaDTO.getIdPendenza());
			filter.setAbilitaFiltroCittadino(listaPendenzaDTO.isAbilitaFiltroCittadino());
			filter.setFilterSortList(listaPendenzaDTO.getFieldSortList());
			if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
				filter.setCfCittadino(userDetails.getIdentificativo()); 
				filter.setAbilitaFiltroCittadino(true);
			}
			filter.setCodTipoVersamento(listaPendenzaDTO.getIdTipoVersamento());
			filter.setDivisione(listaPendenzaDTO.getDivisione());
			filter.setDirezione(listaPendenzaDTO.getDirezione()); 
			if(listaPendenzaDTO.getIuv() != null) {
				if(listaPendenzaDTO.getIuv().length() == 18) {
					filter.setIuv(IuvUtils.toIuv(listaPendenzaDTO.getIuv()));
				} else {
					filter.setIuv(listaPendenzaDTO.getIuv());
				}
			} 
			filter.setMostraSpontaneiNonPagati(listaPendenzaDTO.getMostraSpontaneiNonPagati());

			long count = versamentiBD.count(filter);

			return new ListaPendenzeDTOResponse(count, new ArrayList<>());
		}finally {
			if(versamentiBD != null)
				versamentiBD.closeConnection();
		}
	}

	public ListaPendenzeDTOResponse listaPendenze(ListaPendenzeDTO listaPendenzaDTO) throws ServiceException,PendenzaNonTrovataException, NotAuthorizedException, NotAuthenticatedException, ValidationException{
		it.govpay.bd.viste.VersamentiBD versamentiBD = null;
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);

		try {
			versamentiBD = new it.govpay.bd.viste.VersamentiBD(configWrapper);
			return this.listaPendenze(listaPendenzaDTO, versamentiBD);
		}finally {
			if(versamentiBD != null)
				versamentiBD.closeConnection();
		}
	}

	public ListaPendenzeDTOResponse listaPendenze(ListaPendenzeDTO listaPendenzaDTO, it.govpay.bd.viste.VersamentiBD versamentiBD) throws NotAuthenticatedException, NotAuthorizedException, ServiceException, ValidationException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);

		it.govpay.bd.viste.filters.VersamentoFilter filter = versamentiBD.newFilter();

		filter.setIdDomini(listaPendenzaDTO.getIdDomini());
		filter.setIdTipiVersamento(listaPendenzaDTO.getIdTipiVersamento());
		filter.setEseguiCountConLimit(listaPendenzaDTO.isEseguiCountConLimit());

		if(listaPendenzaDTO.getUnitaOperative() != null) {
			filter.setIdUo(listaPendenzaDTO.getUnitaOperative());
		}

		filter.setOffset(listaPendenzaDTO.getOffset());
		filter.setLimit(listaPendenzaDTO.getLimit());
		filter.setDataInizio(listaPendenzaDTO.getDataDa());
		filter.setDataFine(listaPendenzaDTO.getDataA());
		if(listaPendenzaDTO.getStato()!=null) {
			try {
				it.govpay.bd.model.Versamento.StatoVersamento statoVersamento = null;

				switch(listaPendenzaDTO.getStato()) {
				case ANNULLATA: statoVersamento = it.govpay.bd.model.Versamento.StatoVersamento.ANNULLATO;
				break;
				case ESEGUITA: statoVersamento = it.govpay.bd.model.Versamento.StatoVersamento.ESEGUITO;
				break;
				case ESEGUITA_PARZIALE: statoVersamento = it.govpay.bd.model.Versamento.StatoVersamento.PARZIALMENTE_ESEGUITO;
				break;
				case NON_ESEGUITA: {
					statoVersamento = it.govpay.bd.model.Versamento.StatoVersamento.NON_ESEGUITO;
					filter.setAbilitaFiltroNonScaduto(true);
				}
				break;
				case SCADUTA: {
					statoVersamento = it.govpay.bd.model.Versamento.StatoVersamento.NON_ESEGUITO;
					filter.setAbilitaFiltroScaduto(true);
				}
				break;
				case INCASSATA: statoVersamento = it.govpay.bd.model.Versamento.StatoVersamento.INCASSATO;
				default:
					break;

				}
				filter.setStatoVersamento(statoVersamento);
			} catch(Exception e) {
				return new ListaPendenzeDTOResponse(0L, new ArrayList<LeggiPendenzaDTOResponse>());
			}
		}
		filter.setCodDominio(listaPendenzaDTO.getIdDominio() );
		filter.setCodPagamentoPortale(listaPendenzaDTO.getIdPagamento());
		filter.setCodUnivocoDebitore(listaPendenzaDTO.getIdDebitore());
		filter.setCodApplicazione(listaPendenzaDTO.getIdA2A());
		filter.setCodVersamento(listaPendenzaDTO.getIdPendenza());
		filter.setAbilitaFiltroCittadino(listaPendenzaDTO.isAbilitaFiltroCittadino());
		filter.setFilterSortList(listaPendenzaDTO.getFieldSortList());
		if(listaPendenzaDTO.getCfCittadino() != null) {
			filter.setCfCittadino(listaPendenzaDTO.getCfCittadino()); 
			filter.setAbilitaFiltroCittadino(true);
		}
		filter.setCodTipoVersamento(listaPendenzaDTO.getIdTipoVersamento());
		filter.setDivisione(listaPendenzaDTO.getDivisione());
		filter.setDirezione(listaPendenzaDTO.getDirezione());
		if(listaPendenzaDTO.getIuv() != null) {
			if(listaPendenzaDTO.getIuv().length() == 18) {
				filter.setIuv(IuvUtils.toIuv(listaPendenzaDTO.getIuv()));
			} else {
				filter.setIuv(listaPendenzaDTO.getIuv());
			}
		} 
		filter.setMostraSpontaneiNonPagati(listaPendenzaDTO.getMostraSpontaneiNonPagati());

		Long count = null;
		
		if(listaPendenzaDTO.isEseguiCount()) {
			 count = versamentiBD.count(filter);
		}
		
		List<LeggiPendenzaDTOResponse> resList = new ArrayList<>();

		if(listaPendenzaDTO.isEseguiFindAll()) {
			List<Versamento> findAll = versamentiBD.findAll(filter);

			for (Versamento versamento : findAll) {
				LeggiPendenzaDTOResponse elem = new LeggiPendenzaDTOResponse();
				elem.setVersamento(versamento);
				elem.setApplicazione(versamento.getApplicazione(configWrapper));
				elem.setDominio(versamento.getDominio(configWrapper));
				elem.setUnitaOperativa(versamento.getUo(configWrapper));
				versamento.getTipoVersamentoDominio(configWrapper);
				versamento.getTipoVersamento(configWrapper);
				List<SingoloVersamento> singoliVersamenti = null;
				elem.setLstSingoliVersamenti(singoliVersamenti);

				resList.add(elem);
			}
		} 

		return new ListaPendenzeDTOResponse(count, resList);
	}

	public ListaPendenzeDTOResponse listaPendenzeSmartOrder(ListaPendenzeSmartOrderDTO listaPendenzaDTO) throws ServiceException,PendenzaNonTrovataException, NotAuthorizedException, NotAuthenticatedException, ValidationException{
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);

		VersamentiIncassiBD versamentiBD = null;
		try {
			versamentiBD = new VersamentiIncassiBD(configWrapper);
			VersamentoIncassoFilter filter = versamentiBD.newFilter();

			filter.setIdDomini(listaPendenzaDTO.getIdDomini());
			filter.setIdTipiVersamento(listaPendenzaDTO.getIdTipiVersamento());
			filter.setEseguiCountConLimit(listaPendenzaDTO.isEseguiCountConLimit());

			if(listaPendenzaDTO.getUnitaOperative() != null) {
				filter.setIdUo(listaPendenzaDTO.getUnitaOperative());
			}

			filter.setOffset(listaPendenzaDTO.getOffset());
			filter.setLimit(listaPendenzaDTO.getLimit());
			filter.setDataInizio(listaPendenzaDTO.getDataDa());
			filter.setDataFine(listaPendenzaDTO.getDataA());
			if(listaPendenzaDTO.getStato()!=null) {
				try {
					it.govpay.bd.model.Versamento.StatoVersamento statoVersamento = null;

					switch(listaPendenzaDTO.getStato()) {
					case ANNULLATA: statoVersamento = it.govpay.bd.model.Versamento.StatoVersamento.ANNULLATO;
					break;
					case ESEGUITA: statoVersamento = it.govpay.bd.model.Versamento.StatoVersamento.ESEGUITO;
					break;
					case ESEGUITA_PARZIALE: statoVersamento = it.govpay.bd.model.Versamento.StatoVersamento.PARZIALMENTE_ESEGUITO;
					break;
					case NON_ESEGUITA: {
						statoVersamento = it.govpay.bd.model.Versamento.StatoVersamento.NON_ESEGUITO;
						filter.setAbilitaFiltroNonScaduto(true);
					}
					break;
					case SCADUTA: {
						statoVersamento = it.govpay.bd.model.Versamento.StatoVersamento.NON_ESEGUITO;
						filter.setAbilitaFiltroScaduto(true);
					}
					break;
					case INCASSATA: statoVersamento = it.govpay.bd.model.Versamento.StatoVersamento.INCASSATO;
					default:
						break;

					}
					filter.setStatoVersamento(statoVersamento);
				} catch(Exception e) {
					return new ListaPendenzeDTOResponse(0L, new ArrayList<LeggiPendenzaDTOResponse>());
				}
			}
			filter.setCodDominio(listaPendenzaDTO.getIdDominio() );
			filter.setCodPagamentoPortale(listaPendenzaDTO.getIdPagamento());
			filter.setCodUnivocoDebitore(listaPendenzaDTO.getIdDebitore());
			filter.setCodApplicazione(listaPendenzaDTO.getIdA2A());
			filter.setCodVersamento(listaPendenzaDTO.getIdPendenza());
			filter.setAbilitaFiltroCittadino(listaPendenzaDTO.isAbilitaFiltroCittadino());
			filter.setFilterSortList(listaPendenzaDTO.getFieldSortList());
			if(listaPendenzaDTO.getCfCittadino() != null) {
				filter.setCfCittadino(listaPendenzaDTO.getCfCittadino()); 
				filter.setAbilitaFiltroCittadino(true);
			}
			filter.setCodTipoVersamento(listaPendenzaDTO.getIdTipoVersamento());
			filter.setDivisione(listaPendenzaDTO.getDivisione());
			filter.setDirezione(listaPendenzaDTO.getDirezione());
			if(listaPendenzaDTO.getIuv() != null) {
				if(listaPendenzaDTO.getIuv().length() == 18) {
					filter.setIuv(IuvUtils.toIuv(listaPendenzaDTO.getIuv()));
				} else {
					filter.setIuv(listaPendenzaDTO.getIuv());
				}
			} 
			filter.setMostraSpontaneiNonPagati(listaPendenzaDTO.getMostraSpontaneiNonPagati());

			Long count = null;
			
			if(listaPendenzaDTO.isEseguiCount()) {
				 count = versamentiBD.count(filter);
			}

			List<LeggiPendenzaDTOResponse> resList = new ArrayList<>();
			if(listaPendenzaDTO.isEseguiFindAll()) {
				List<Versamento> findAll = versamentiBD.findAll(filter);

				for (Versamento versamento : findAll) {
					LeggiPendenzaDTOResponse elem = new LeggiPendenzaDTOResponse();
					elem.setVersamento(versamento);
					elem.setApplicazione(versamento.getApplicazione(configWrapper));
					elem.setDominio(versamento.getDominio(configWrapper));
					elem.setUnitaOperativa(versamento.getUo(configWrapper));
					versamento.getTipoVersamentoDominio(configWrapper);
					versamento.getTipoVersamento(configWrapper);
					List<SingoloVersamento> singoliVersamenti = null;

					//				singoliVersamenti = versamento.getSingoliVersamenti(versamentiBD);
					//				for (SingoloVersamento singoloVersamento : singoliVersamenti) {
					//					singoloVersamento.getCodContabilita(bd);
					//					singoloVersamento.getIbanAccredito(bd);
					//					singoloVersamento.getTipoContabilita(bd);
					//					singoloVersamento.getTributo(bd);
					//
					//				}
					elem.setLstSingoliVersamenti(singoliVersamenti);

					resList.add(elem);
				}
			} 

			return new ListaPendenzeDTOResponse(count, resList);
		}finally {
			if(versamentiBD != null)
				versamentiBD.closeConnection();
		}
	}

	public LeggiPendenzaDTOResponse leggiPendenza(LeggiPendenzaDTO leggiPendenzaDTO) throws ServiceException,PendenzaNonTrovataException, NotAuthorizedException, NotAuthenticatedException, GovPayException{
		LeggiPendenzaDTOResponse response = new LeggiPendenzaDTOResponse();

		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		VersamentiBD versamentiBD = null;
		try {
			versamentiBD = new VersamentiBD(configWrapper);

			versamentiBD.setupConnection(configWrapper.getTransactionID());

			versamentiBD.setAtomica(false);

			String idA2A = leggiPendenzaDTO.getCodA2A();
			String idPendenza = leggiPendenzaDTO.getCodPendenza();
			response = _leggiPendenza(idA2A,idPendenza, response, versamentiBD);
		} catch (NotFoundException e) {
			throw new PendenzaNonTrovataException(e.getMessage(), e);
		} finally {
			if(versamentiBD != null)
				versamentiBD.closeConnection();
		}

		return response;
	}

	private LeggiPendenzaDTOResponse _leggiPendenza(String idA2A, String idPendenza, LeggiPendenzaDTOResponse response, VersamentiBD versamentiBD) throws NotFoundException, ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		try {
			Versamento versamento = versamentiBD.getVersamento(AnagraficaManager.getApplicazione(configWrapper, idA2A).getId(), idPendenza);

			Dominio dominio = versamento.getDominio(configWrapper);
			TipoVersamento tipoVersamento = versamento.getTipoVersamento(configWrapper);
			versamento.getTipoVersamentoDominio(configWrapper);
			versamento.getDocumento(versamentiBD);

			response.setVersamento(versamento);
			response.setApplicazione(versamento.getApplicazione(configWrapper));
			response.setTipoVersamento(tipoVersamento);
			response.setDominio(dominio);
			response.setUnitaOperativa(versamento.getUo(configWrapper));
			List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(versamentiBD);
			response.setLstSingoliVersamenti(singoliVersamenti);
			for (SingoloVersamento singoloVersamento : singoliVersamenti) {
				populateSingoloVersamento(versamentiBD, configWrapper, singoloVersamento, versamento);
			}

			PagamentiPortaleBD pagamentiPortaleBD = new PagamentiPortaleBD(versamentiBD);
			pagamentiPortaleBD.setAtomica(false);
			PagamentoPortaleFilter newFilter = pagamentiPortaleBD.newFilter();
			List<PagamentoPortaleVersamento> allPagPortVers = pagamentiPortaleBD.getAllPagPortVers(versamento.getId());
			List<Long> idPagamentiPortale = new ArrayList<>();

			if(allPagPortVers != null && !allPagPortVers.isEmpty()) {
				for (PagamentoPortaleVersamento pagamentoPortaleVersamento : allPagPortVers) {
					idPagamentiPortale.add(pagamentoPortaleVersamento.getIdPagamentoPortale().getId());
				}

				newFilter.setIdPagamentiPortale(idPagamentiPortale);
				List<PagamentoPortale> findAll = pagamentiPortaleBD.findAll(newFilter);
				response.setPagamenti(findAll);
			}

			RptBD rptBD = new RptBD(versamentiBD);
			rptBD.setAtomica(false);
			RptFilter newFilter2 = rptBD.newFilter();
			newFilter2.setIdPendenza(versamento.getCodVersamentoEnte());
			newFilter2.setCodApplicazione(versamento.getApplicazione(configWrapper).getCodApplicazione());
			FilterSortWrapper ordinamentoRPT = new FilterSortWrapper(it.govpay.orm.RPT.model().DATA_MSG_RICHIESTA,SortOrder.ASC);
			newFilter2.addFilterSort(ordinamentoRPT);
			long count = rptBD.count(newFilter2);

			if(count > 0) {
				List<Rpt> findAll = rptBD.findAll(newFilter2);

				for (Rpt rpt : findAll) {
					rpt.setVersamento(versamento);
				}

				response.setRpts(findAll);
			}
			
			versamento.getAllegati(versamentiBD);

			return response;
		} finally {
		}
	}

	public LeggiPendenzaDTOResponse leggiPendenzaByRiferimentoAvviso(LeggiPendenzaDTO leggiPendenzaDTO) throws ServiceException,PendenzaNonTrovataException, NotAuthorizedException, NotAuthenticatedException{
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		LeggiPendenzaDTOResponse response = new LeggiPendenzaDTOResponse();
		Versamento versamento;
		VersamentiBD versamentiBD = null;

		try {
			versamentiBD = new VersamentiBD(configWrapper);
			
			versamentiBD.setupConnection(configWrapper.getTransactionID());
			
			versamentiBD.setAtomica(false);
			
			versamento = versamentiBD.getVersamentoByDominioIuv(AnagraficaManager.getDominio(configWrapper, leggiPendenzaDTO.getIdDominio()).getId(), IuvUtils.toIuv(leggiPendenzaDTO.getNumeroAvviso())); 

			Dominio dominio = versamento.getDominio(configWrapper);
			TipoVersamento tipoVersamento = versamento.getTipoVersamento(configWrapper);
			versamento.getTipoVersamentoDominio(configWrapper);
			versamento.getDocumento(versamentiBD);

			response.setVersamento(versamento);
			response.setApplicazione(versamento.getApplicazione(configWrapper));
			response.setTipoVersamento(tipoVersamento);
			response.setDominio(dominio);
			response.setUnitaOperativa(versamento.getUo(configWrapper));
			List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(versamentiBD);
			response.setLstSingoliVersamenti(singoliVersamenti);
			for (SingoloVersamento singoloVersamento : singoliVersamenti) {
				populateSingoloVersamento(versamentiBD, configWrapper, singoloVersamento, versamento);
			}

			PagamentiPortaleBD pagamentiPortaleBD = new PagamentiPortaleBD(versamentiBD);
			pagamentiPortaleBD.setAtomica(false);
			PagamentoPortaleFilter newFilter = pagamentiPortaleBD.newFilter();
			List<PagamentoPortaleVersamento> allPagPortVers = pagamentiPortaleBD.getAllPagPortVers(versamento.getId());
			List<Long> idPagamentiPortale = new ArrayList<>();

			if(allPagPortVers != null && !allPagPortVers.isEmpty()) {
				for (PagamentoPortaleVersamento pagamentoPortaleVersamento : allPagPortVers) {
					idPagamentiPortale.add(pagamentoPortaleVersamento.getIdPagamentoPortale().getId());
				}

				newFilter.setIdPagamentiPortale(idPagamentiPortale);
				List<PagamentoPortale> findAll = pagamentiPortaleBD.findAll(newFilter);
				response.setPagamenti(findAll);
			}

			RptBD rptBD = new RptBD(versamentiBD);
			rptBD.setAtomica(false);
			RptFilter newFilter2 = rptBD.newFilter();
			newFilter2.setIdPendenza(versamento.getCodVersamentoEnte());
			newFilter2.setCodApplicazione(versamento.getApplicazione(configWrapper).getCodApplicazione());
			long count = rptBD.count(newFilter2);

			if(count > 0) {
				List<Rpt> findAll = rptBD.findAll(newFilter2);

				for (Rpt rpt : findAll) {
					Versamento versamento2 = rpt.getVersamento(versamentiBD);
					versamento2.getDominio(configWrapper);
					versamento2.getUo(configWrapper);
					versamento2.getApplicazione(configWrapper);
					versamento2.getTipoVersamento(configWrapper);
					versamento2.getTipoVersamentoDominio(configWrapper);
				}

				response.setRpts(findAll);
			}
			
			versamento.getAllegati(versamentiBD);

		} catch (NotFoundException e) {
			throw new PendenzaNonTrovataException(e.getMessage(), e);
		} catch (ValidationException e) {
			throw new PendenzaNonTrovataException(e.getMessage(), e);
		}  finally {
			if(versamentiBD != null)
				versamentiBD.closeConnection();
		}
		return response;
	}

	private void populateSingoloVersamento(BasicBD bd, BDConfigWrapper configWrapper, SingoloVersamento singoloVersamento, Versamento versamento) throws ServiceException, NotFoundException {
		singoloVersamento.getCodContabilita(configWrapper);
		singoloVersamento.getIbanAccredito(configWrapper);
		singoloVersamento.getTipoContabilita(configWrapper);
		singoloVersamento.getTributo(configWrapper);
		singoloVersamento.setVersamento(versamento);
		List<Pagamento> pagamenti = singoloVersamento.getPagamenti(bd);
		for (Pagamento pagamento: pagamenti) {
			this.populatePagamento(pagamento, singoloVersamento, bd, configWrapper);
		}

		List<Rendicontazione> rendicontazioni = singoloVersamento.getRendicontazioni(bd); 
		if(rendicontazioni != null) {
			for(Rendicontazione rend: rendicontazioni) {
				Pagamento pagamento = rend.getPagamento(bd);
				if(pagamento != null) {
					pagamento.setSingoloVersamento(singoloVersamento);
					pagamento.getDominio(configWrapper);
					pagamento.getRpt(bd);
					pagamento.getIncasso(bd);
				}
			}
		}
	}

	private void populatePagamento(Pagamento pagamento, SingoloVersamento singoloVersamento, BasicBD bd, BDConfigWrapper configWrapper) throws ServiceException, NotFoundException {
		pagamento.setSingoloVersamento(singoloVersamento);
		pagamento.getRpt(bd);
		pagamento.getDominio(configWrapper);
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
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		LeggiPendenzaDTOResponse response = new LeggiPendenzaDTOResponse();
		VersamentiBD versamentiBD = null;
		try {
			versamentiBD = new VersamentiBD(configWrapper);
			String idA2A = patchPendenzaDTO.getIdA2a();
			String idPendenza = patchPendenzaDTO.getIdPendenza();
			it.govpay.bd.model.Versamento versamentoLetto = versamentiBD.getVersamento(AnagraficaManager.getApplicazione(configWrapper, idA2A).getId(), idPendenza);

			// controllo che il dominio e tipo versamento siano autorizzati
			if(!AuthorizationManager.isTipoVersamentoDominioAuthorized(patchPendenzaDTO.getUser(), versamentoLetto.getDominio(configWrapper).getCodDominio(), versamentoLetto.getTipoVersamento(configWrapper).getCodTipoVersamento())) {
				throw AuthorizationManager.toNotAuthorizedException(patchPendenzaDTO.getUser(), versamentoLetto.getDominio(configWrapper).getCodDominio(), versamentoLetto.getTipoVersamento(configWrapper).getCodTipoVersamento());
			}

			for(PatchOp op: patchPendenzaDTO.getOp()) {

				// validazione del path richiesto
				this.validaPath(op.getPath());

				if(PATH_STATO.equals(op.getPath())) {
					// validazione del value
					this.validaValue(op.getValue());

					String motivazione = null;
					//cerco il patch di descrizione stato
					for(PatchOp op2: patchPendenzaDTO.getOp()) {
						if(PATH_DESCRIZIONE_STATO.equals(op2.getPath())) {
							try { motivazione = (String) op2.getValue(); } catch (Exception e) {}
						}
					}
					this.patchStato(patchPendenzaDTO.getUser(), versamentoLetto, op, motivazione);
				} else if(PATH_DESCRIZIONE_STATO.equals(op.getPath())) {
					// validazione del value
					this.validaValue(op.getValue());

					this.patchDescrizioneStato(versamentoLetto, op);
				} else if(PATH_ACK.equals(op.getPath())) {
					this.patchAck(versamentoLetto, op);
				} else if(PATH_NOTA.equals(op.getPath())) {
					if(!op.getOp().equals(OpEnum.ADD)) {
						throw new ValidationException(MessageFormat.format(UtenzaPatchUtils.OP_XX_NON_VALIDO_PER_IL_PATH_YY, op.getOp(), op.getPath()));
					}

					LinkedHashMap<?,?> map = (LinkedHashMap<?,?>) op.getValue();
					versamentoLetto.setDescrizioneStato((String)map.get(UtenzaPatchUtils.OGGETTO_NOTA_KEY));
				} else {
					throw new ServiceException("Path '"+op.getPath()+"' non valido");
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

			versamentiBD.setupConnection(configWrapper.getTransactionID());

			versamentiBD.setAtomica(false);

			// restituisco il versamento
			response = this._leggiPendenza(idA2A, idPendenza, response, versamentiBD);

			return response;

		} catch (ServiceException e) {
			throw new GovPayException(e);
		} catch (NotFoundException e) {
			throw new PendenzaNonTrovataException(e.getMessage(), e);
		} catch (IOException e) {
			throw new GovPayException(e);
		} finally {
			if(versamentiBD != null)
				versamentiBD.closeConnection();
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
	private void patchStato(Authentication authentication, it.govpay.bd.model.Versamento versamentoLetto, PatchOp op, String motivazione) throws ValidationException, IOException {
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
			if(versamentoLetto.getStatoVersamento().equals(StatoVersamento.NON_ESEGUITO)
					|| versamentoLetto.getStatoVersamento().equals(StatoVersamento.ANNULLATO)) {
				versamentoLetto.setStatoVersamento(StatoVersamento.ANNULLATO);
				versamentoLetto.setAvvisoNotificato(null);
				versamentoLetto.setAvvAppIOPromemoriaScadenzaNotificato(null); 
				versamentoLetto.setAvvMailPromemoriaScadenzaNotificato(null);
				//				eventoUtente.setDettaglioEsito("Pendenza annullata");
			} else {
				throw new ValidationException("Non e' consentito aggiornare lo stato di una pendenza ad ANNULLATO da uno stato diverso da NON_ESEGUITO o ANNULLATO");
			}
			break;
		case NON_ESEGUITO:
			if(versamentoLetto.getStatoVersamento().equals(StatoVersamento.ANNULLATO)) {
				versamentoLetto.setStatoVersamento(StatoVersamento.NON_ESEGUITO);

				versamentoLetto.setAvvisoNotificato(null);
				if(versamentoLetto.getDataNotificaAvviso() != null)
					versamentoLetto.setAvvisoNotificato(false);

				versamentoLetto.setAvvAppIOPromemoriaScadenzaNotificato(null);
				if(versamentoLetto.getAvvAppIODataPromemoriaScadenza() != null)
					versamentoLetto.setAvvAppIOPromemoriaScadenzaNotificato(false);

				versamentoLetto.setAvvMailPromemoriaScadenzaNotificato(null);
				if(versamentoLetto.getAvvMailDataPromemoriaScadenza() != null)
					versamentoLetto.setAvvMailPromemoriaScadenzaNotificato(false);

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
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		PutPendenzaDTOResponse createOrUpdatePendenzaResponse = new PutPendenzaDTOResponse();
		try {
			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento();

			Versamento versamento = versamentoBusiness.chiediVersamento(putVersamentoDTO.getVersamento());

			//inserisco il tipo
			versamento.setTipo(putVersamentoDTO.getTipo());

			Applicazione applicazioniBD = new Applicazione();
			GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(putVersamentoDTO.getUser());
			it.govpay.bd.model.Applicazione applicazioneAutenticata = details.getApplicazione();
			if(applicazioneAutenticata != null) 
				applicazioniBD.autorizzaApplicazione(putVersamentoDTO.getVersamento().getCodApplicazione(), applicazioneAutenticata, configWrapper);


			boolean generaIuv = VersamentoUtils.generaIUV(versamento, configWrapper);
			versamento = versamentoBusiness.caricaVersamento(versamento, generaIuv, true, putVersamentoDTO.getAvvisatura(), putVersamentoDTO.getDataAvvisatura(),null);
			createOrUpdatePendenzaResponse.setCreated(versamento.isCreated());
			createOrUpdatePendenzaResponse.setVersamento(versamento);
			createOrUpdatePendenzaResponse.setDominio(versamento.getDominio(configWrapper));
			createOrUpdatePendenzaResponse.setUo(versamento.getUo(configWrapper));

			Iuv iuv = IuvUtils.toIuv(versamento, versamento.getApplicazione(configWrapper), versamento.getDominio(configWrapper));

			createOrUpdatePendenzaResponse.setBarCode(iuv.getBarCode() != null ? new String(iuv.getBarCode()) : null);
			createOrUpdatePendenzaResponse.setQrCode(iuv.getQrCode() != null ? new String(iuv.getQrCode()) : null);

			if(putVersamentoDTO.isStampaAvviso()) {
				it.govpay.core.business.AvvisoPagamento avvisoBD = new it.govpay.core.business.AvvisoPagamento();
				PrintAvvisoVersamentoDTO printAvvisoDTO = new PrintAvvisoVersamentoDTO();
				printAvvisoDTO.setUpdate(!createOrUpdatePendenzaResponse.isCreated());
				printAvvisoDTO.setCodDominio(versamento.getDominio(configWrapper).getCodDominio());
				printAvvisoDTO.setIuv(iuv.getIuv());
				printAvvisoDTO.setVersamento(versamento); 
				printAvvisoDTO.setSalvaSuDB(false);
				printAvvisoDTO.setSdfDataScadenza(SimpleDateFormatUtils.newSimpleDateFormatGGMMAAAA());
				PrintAvvisoDTOResponse printAvvisoDTOResponse = avvisoBD.printAvvisoVersamento(printAvvisoDTO);
				createOrUpdatePendenzaResponse.setPdf(Base64.getEncoder().encodeToString(printAvvisoDTOResponse.getAvviso().getPdf()));
			} else { // non devo fare la stampa.
				if(!createOrUpdatePendenzaResponse.isCreated()) {
					// se ho fatto l'update della pendenza e non voglio aggiornare la stampa la cancello cosi quando verra' letta la prima volta si aggiornera' da sola
					it.govpay.core.business.AvvisoPagamento avvisoBD = new it.govpay.core.business.AvvisoPagamento();
					avvisoBD.cancellaAvviso(versamento);
				}
			}

		} catch (ServiceException e) {
			throw new GovPayException(e);
		} finally {
		}
		return createOrUpdatePendenzaResponse;
	}

	public PutPendenzaDTOResponse createOrUpdateCustom(PutPendenzaDTO putVersamentoDTO) throws GovPayException, 
	NotAuthorizedException, NotAuthenticatedException, ValidationException, DominioNonTrovatoException, TipoVersamentoNonTrovatoException, EcException, UnitaOperativaNonTrovataException, ApplicazioneNonTrovataException, UnprocessableEntityException{ 
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		PutPendenzaDTOResponse createOrUpdatePendenzaResponse = new PutPendenzaDTOResponse();
		VersamentiBD versamentiBD = null;
		try {
			Dominio dominio = null;
			String codDominio = putVersamentoDTO.getCodDominio(); 
			try {
				dominio = AnagraficaManager.getDominio(configWrapper, codDominio);
			} catch (NotFoundException e1) {
				throw new DominioNonTrovatoException("Dominio ["+codDominio+"] inesistente.", e1);
			}

			UnitaOperativa uo = null;
			String codUo = putVersamentoDTO.getCodUo();
			if(codUo != null) {
				try {
					uo = AnagraficaManager.getUnitaOperativa(configWrapper, dominio.getId(), codUo);
				} catch (NotFoundException e1) {
					throw new UnitaOperativaNonTrovataException("Unita' Operativa ["+codUo+"] inesistente per il Dominio ["+codDominio+"].", e1);
				}
			}

			// lettura della configurazione TipoVersamentoDominio
			TipoVersamentoDominio tipoVersamentoDominio = null;
			String codTipoVersamento = putVersamentoDTO.getCodTipoVersamento();
			try {
				tipoVersamentoDominio = AnagraficaManager.getTipoVersamentoDominio(configWrapper, dominio.getId(), codTipoVersamento);
			} catch (NotFoundException e1) {
				throw new TipoVersamentoNonTrovatoException("TipoPendenza ["+codTipoVersamento+"] inesistente per il Dominio ["+codDominio+"].", e1);
			}

			this.log.debug("Caricamento pendenza modello 4: elaborazione dell'input ricevuto in corso...");
			String json = putVersamentoDTO.getCustomReq();

			String validazioneDefinizione = tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeValidazioneDefinizione();
			String trasformazioneDefinizione = tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizione();
			String trasformazioneTipo = tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeTrasformazioneTipo();
			String codApplicazione = tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeCodApplicazione();

			boolean checkParametriUpdate = false;
			switch (putVersamentoDTO.getTipo()) {
			case SPONTANEO:
				checkParametriUpdate = true;
				validazioneDefinizione = tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoValidazioneDefinizione();
				trasformazioneDefinizione = tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoTrasformazioneDefinizione();
				trasformazioneTipo = tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoTrasformazioneTipo();
				codApplicazione = tipoVersamentoDominio.getCaricamentoPendenzePortalePagamentoCodApplicazione();
				break;
			case DOVUTO:
				validazioneDefinizione = tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeValidazioneDefinizione();
				trasformazioneDefinizione = tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeTrasformazioneDefinizione();
				trasformazioneTipo = tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeTrasformazioneTipo();
				codApplicazione = tipoVersamentoDominio.getCaricamentoPendenzePortaleBackofficeCodApplicazione();
				break;
			}

			VersamentoUtils.validazioneInputVersamentoModello4(this.log, json, validazioneDefinizione);

			MultivaluedMap<String, String> queryParameters = putVersamentoDTO.getQueryParameters(); 
			MultivaluedMap<String, String> pathParameters = putVersamentoDTO.getPathParameters();
			Map<String, String> headers = putVersamentoDTO.getHeaders();

			if(trasformazioneDefinizione != null && trasformazioneTipo != null) {
				json = VersamentoUtils.trasformazioneInputVersamentoModello4(log, dominio, codTipoVersamento, trasformazioneTipo, uo, json, queryParameters, pathParameters, headers, trasformazioneDefinizione);
			}
			Versamento chiediVersamento = null;
			
			log.debug("Json di input dopo validazione e trasformazione: ["+json+"]");

			if(codApplicazione != null) {
				chiediVersamento =  VersamentoUtils.inoltroInputVersamentoModello4(log, codDominio, codTipoVersamento, codUo, codApplicazione, json);
			} else {
				PendenzaPost pendenzaPost = PendenzaPost.parse(json);
				
				// imposto i dati idDominio, idTipoVersamento e idUnitaOperativa fornite nella URL di richiesta, sovrascrivendo eventuali valori impostati dalla trasformazione.
				pendenzaPost.setIdDominio(codDominio);
				pendenzaPost.setIdTipoPendenza(codTipoVersamento);
				pendenzaPost.setIdUnitaOperativa(codUo);
				
				new PendenzaPostValidator(pendenzaPost).validate();

				it.govpay.core.dao.commons.Versamento versamentoCommons = TracciatiConverter.getVersamentoFromPendenza(pendenzaPost);
				((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdPendenza(versamentoCommons.getCodVersamentoEnte());
				((GpContext) (ContextThreadLocal.get()).getApplicationContext()).getEventoCtx().setIdA2A(versamentoCommons.getCodApplicazione());
				it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento();
				chiediVersamento = versamentoBusiness.chiediVersamento(versamentoCommons);
			}

			Applicazione applicazioniBD = new Applicazione();
			GovpayLdapUserDetails details = AutorizzazioneUtils.getAuthenticationDetails(putVersamentoDTO.getUser());
			it.govpay.bd.model.Applicazione applicazioneAutenticata = details.getApplicazione();
			it.govpay.bd.model.Applicazione applicazione = chiediVersamento.getApplicazione(configWrapper); 
			if(applicazioneAutenticata != null) 
				applicazioniBD.autorizzaApplicazione(applicazione.getCodApplicazione(), applicazioneAutenticata, configWrapper);

			if(checkParametriUpdate) {
				if(putVersamentoDTO.getCodApplicazione() != null && putVersamentoDTO.getCodVersamentoEnte() != null) {
					try {
						chiediVersamento.setApplicazione(putVersamentoDTO.getCodApplicazione(), configWrapper);
					} catch (NotFoundException e) {
						throw new ApplicazioneNonTrovataException("Applicazione ["+putVersamentoDTO.getCodApplicazione()+"] non trovata.", e);
					}
					chiediVersamento.setCodVersamentoEnte(putVersamentoDTO.getCodVersamentoEnte());
				}
			}

			createOrUpdatePendenzaResponse.setCreated(false);
			versamentiBD = new VersamentiBD(configWrapper);

			try {
				versamentiBD.getVersamento(applicazione.getId(), chiediVersamento.getCodVersamentoEnte());
			}catch(NotFoundException e) {
				createOrUpdatePendenzaResponse.setCreated(true);
			}

			//inserisco il tipo
			chiediVersamento.setTipo(putVersamentoDTO.getTipo());

			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento();
			boolean generaIuv = VersamentoUtils.generaIUV(chiediVersamento, configWrapper);
			versamentoBusiness.caricaVersamento(chiediVersamento, generaIuv, true, putVersamentoDTO.getAvvisatura(), putVersamentoDTO.getDataAvvisatura(),null, putVersamentoDTO.isInserimentoDB());

			// restituisco il versamento creato
			createOrUpdatePendenzaResponse.setVersamento(chiediVersamento);
			createOrUpdatePendenzaResponse.setDominio(chiediVersamento.getDominio(configWrapper));
			createOrUpdatePendenzaResponse.setUo(chiediVersamento.getUo(configWrapper));

			Iuv iuv = IuvUtils.toIuv(chiediVersamento, applicazione, chiediVersamento.getDominio(configWrapper));

			createOrUpdatePendenzaResponse.setBarCode(iuv.getBarCode() != null ? new String(iuv.getBarCode()) : null);
			createOrUpdatePendenzaResponse.setQrCode(iuv.getQrCode() != null ? new String(iuv.getQrCode()) : null);

			if(putVersamentoDTO.isStampaAvviso()) {
				it.govpay.core.business.AvvisoPagamento avvisoBD = new it.govpay.core.business.AvvisoPagamento();
				PrintAvvisoVersamentoDTO printAvvisoDTO = new PrintAvvisoVersamentoDTO();
				printAvvisoDTO.setUpdate(!createOrUpdatePendenzaResponse.isCreated());
				printAvvisoDTO.setCodDominio(chiediVersamento.getDominio(configWrapper).getCodDominio());
				printAvvisoDTO.setIuv(iuv.getIuv());
				printAvvisoDTO.setVersamento(chiediVersamento); 
				printAvvisoDTO.setSalvaSuDB(false);
				printAvvisoDTO.setSdfDataScadenza(SimpleDateFormatUtils.newSimpleDateFormatGGMMAAAA());
				PrintAvvisoDTOResponse printAvvisoDTOResponse = avvisoBD.printAvvisoVersamento(printAvvisoDTO);
				createOrUpdatePendenzaResponse.setPdf(Base64.getEncoder().encodeToString(printAvvisoDTOResponse.getAvviso().getPdf()));
			} else { // non devo fare la stampa.
				if(!createOrUpdatePendenzaResponse.isCreated()) {
					// se ho fatto l'update della pendenza e non voglio aggiornare la stampa la cancello cosi quando verra' letta la prima volta si aggiornera' da sola
					it.govpay.core.business.AvvisoPagamento avvisoBD = new it.govpay.core.business.AvvisoPagamento();
					avvisoBD.cancellaAvviso(chiediVersamento);
				}
			}

		} catch (ServiceException e) {
			throw new GovPayException(e);
		}  finally {
			if(versamentiBD != null)
				versamentiBD.closeConnection();
		}
		return createOrUpdatePendenzaResponse;
	}

	public LeggiPendenzaDTOResponse leggiAvvisoPagamento(LeggiPendenzaDTO leggiPendenzaDTO) throws ServiceException,PendenzaNonTrovataException, NotAuthorizedException, NotAuthenticatedException{
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), this.useCacheData);
		LeggiPendenzaDTOResponse response = new LeggiPendenzaDTOResponse();
		Versamento versamento;
		VersamentiBD versamentiBD = null;

		try {
			versamentiBD = new VersamentiBD(configWrapper);
			versamento = versamentiBD.getVersamentoByDominioIuv(AnagraficaManager.getDominio(configWrapper, leggiPendenzaDTO.getIdDominio()).getId(), IuvUtils.toIuv(leggiPendenzaDTO.getNumeroAvviso())); 

			Dominio dominio = versamento.getDominio(configWrapper);
			TipoVersamento tipoVersamento = versamento.getTipoVersamento(configWrapper);

			// controllo che il dominio e tipo versamento siano autorizzati
			if(!AuthorizationManager.isTipoVersamentoDominioAuthorized(leggiPendenzaDTO.getUser(), dominio.getCodDominio(), tipoVersamento.getCodTipoVersamento())) {
				throw AuthorizationManager.toNotAuthorizedException(leggiPendenzaDTO.getUser(),  dominio.getCodDominio(), tipoVersamento.getCodTipoVersamento());
			}

			it.govpay.core.business.AvvisoPagamento avvisoBD = new it.govpay.core.business.AvvisoPagamento();
			PrintAvvisoVersamentoDTO printAvvisoDTO = new PrintAvvisoVersamentoDTO();
			printAvvisoDTO.setCodDominio(versamento.getDominio(configWrapper).getCodDominio());
			printAvvisoDTO.setIuv(versamento.getIuvVersamento());
			printAvvisoDTO.setVersamento(versamento); 
			printAvvisoDTO.setSalvaSuDB(false);
			printAvvisoDTO.setSdfDataScadenza(SimpleDateFormatUtils.newSimpleDateFormatGGMMAAAA());
			PrintAvvisoDTOResponse printAvvisoDTOResponse = avvisoBD.printAvvisoVersamento(printAvvisoDTO);
			response.setAvvisoPdf(printAvvisoDTOResponse.getAvviso().getPdf());
		} catch (NotFoundException e) {
			throw new PendenzaNonTrovataException(e.getMessage(), e);
		} catch (ValidationException e) {
			throw new PendenzaNonTrovataException(e.getMessage(), e);
		}  finally {
			if(versamentiBD != null)
				versamentiBD.closeConnection();
		}
		return response;
	}
}
