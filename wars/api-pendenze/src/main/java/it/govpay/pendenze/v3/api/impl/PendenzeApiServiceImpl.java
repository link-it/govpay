package it.govpay.pendenze.v3.api.impl;

import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.openspcoop2.utils.service.BaseImpl;
import org.openspcoop2.utils.service.context.IContext;

import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.Rpt;
import it.govpay.core.autorizzazione.AuthorizationManager;
import it.govpay.core.dao.anagrafica.dto.BasicFindRequestDTO;
import it.govpay.core.dao.pagamenti.PendenzeDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTOResponse;
import it.govpay.exception.WebApplicationExceptionMapper;
import it.govpay.model.TipoVersamento;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.pendenze.v3.api.PendenzeApi;
import it.govpay.pendenze.v3.beans.ModalitaAvvisaturaDigitale;
import it.govpay.pendenze.v3.beans.NuovaPendenza;
import it.govpay.pendenze.v3.beans.PatchOp;
import it.govpay.pendenze.v3.beans.Pendenza;
import it.govpay.pendenze.v3.beans.PendenzaCreata;
import it.govpay.pendenze.v3.beans.Pendenze;
import it.govpay.pendenze.v3.beans.StatoPendenza;
import it.govpay.pendenze.v3.beans.converter.PendenzeConverter;
import it.govpay.rs.v3.acl.Acl;
import it.govpay.rs.v3.acl.AuthorizationRules;
import it.govpay.rs.v3.acl.impl.TipoUtenzaOnlyAcl;
/**
 * GovPay - API Pagamento
 */
public class PendenzeApiServiceImpl extends BaseImpl implements PendenzeApi {

	public static UriBuilder basePath = UriBuilder.fromPath("/pendenze");

	public PendenzeApiServiceImpl(){
		super(org.slf4j.LoggerFactory.getLogger(PendenzeApiServiceImpl.class));
	}

	private AuthorizationRules getAuthorizationRules() throws Exception{
		AuthorizationRules ac = new AuthorizationRules();

		/*
		 * Utenti anonimi possono chiamare: nessun servizio
		 */

		/*
		 * Utenti CITTADINO e APPLICAZIONE possono chiamare tutte le operazioni:
		 */
		{
			TIPO_UTENZA[] tipiUtenza = { TIPO_UTENZA.CITTADINO, TIPO_UTENZA.APPLICAZIONE };
			Acl acl = new TipoUtenzaOnlyAcl(tipiUtenza);
			ac.addAcl(acl);
		}

		return ac;
	}
	
	@Override
	public PendenzaCreata addPendenza(String idA2A, String idPendenza, NuovaPendenza body, Boolean stampaAvviso,
			Boolean avvisaturaDigitale, ModalitaAvvisaturaDigitale modalitaAvvisaturaDigitale) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Elenco delle pendenze
	 *
	 * Fornisce la lista delle pendenze filtrata ed ordinata.
	 *
	 */
	@Override
	public Pendenze findPendenze(Integer offset, Integer limit, String fields, String sort, String idDominio, String iuv, String idA2A, String idPendenza, String idPagatore, StatoPendenza statoPendenza, String idSessionePortale) {
		IContext context = this.getContext();
		try {
			context.getLogger().info("Invocazione in corso ...");     
			getAuthorizationRules().authorize(context);
			context.getLogger().debug("Autorizzazione completata con successo");    

			/* default values */
			if(offset == null || offset < 0) offset = 0;
			if(limit == null || limit < 0 || limit > 100) limit = BasicFindRequestDTO.DEFAULT_LIMIT;
			// Parametri - > DTO Input

			ListaPendenzeDTO listaPendenzeDTO = new ListaPendenzeDTO(context.getAuthentication());

			listaPendenzeDTO.setOffset(offset);
			listaPendenzeDTO.setLimit(limit);

			if(statoPendenza != null)
				listaPendenzeDTO.setStato(statoPendenza.name());

			listaPendenzeDTO.setIuv(iuv);
			listaPendenzeDTO.setIdDominio(idDominio);
			listaPendenzeDTO.setIdA2A(idA2A);
			listaPendenzeDTO.setIdDebitore(idPagatore);
			listaPendenzeDTO.setIdPagamento(idSessionePortale);
			listaPendenzeDTO.setIdPendenza(idPendenza);
			listaPendenzeDTO.setOrderBy(sort);
			
			// Autorizzazione sui domini
			List<Long> idDomini = AuthorizationManager.getIdDominiAutorizzati(context.getAuthentication());
			if(idDomini == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunDominioAutorizzato(context.getAuthentication());
			}
			listaPendenzeDTO.setIdDomini(idDomini);
			// autorizzazione sui tipi pendenza
			List<Long> idTipiVersamento = AuthorizationManager.getIdTipiVersamentoAutorizzati(context.getAuthentication());
			if(idTipiVersamento == null) {
				throw AuthorizationManager.toNotAuthorizedExceptionNessunTipoVersamentoAutorizzato(context.getAuthentication());
			}
			listaPendenzeDTO.setIdTipiVersamento(idTipiVersamento);

			PendenzeDAO pendenzeDAO = new PendenzeDAO(); 

			// CHIAMATA AL DAO

			ListaPendenzeDTOResponse listaPendenzeDTOResponse = pendenzeDAO.listaPendenze(listaPendenzeDTO);

			Pendenze pendenze = PendenzeConverter.toRsModel(listaPendenzeDTOResponse.getResults(), context.getUriInfo(), offset, limit, listaPendenzeDTOResponse.getTotalResults());
			context.getLogger().info("Invocazione completata con successo");

			return pendenze;

		}
		catch(javax.ws.rs.WebApplicationException e) {
			context.getLogger().error("Invocazione terminata con errore '4xx': %s",e, e.getMessage());
			throw e;
		}
		catch(Throwable e) {
			context.getLogger().error("Invocazione terminata con errore: %s",e, e.getMessage());
			throw WebApplicationExceptionMapper.handleException(e);
//			throw FaultCode.ERRORE_INTERNO.toException(e);
		}
	}

	/**
	 * Dettaglio di una pendenza per identificativo
	 *
	 * Acquisisce il dettaglio di una pendenza, comprensivo dei dati di pagamento.
	 *
	 */
	@Override
	public Pendenza getPendenza(String idA2A, String idPendenza) {
		IContext context = this.getContext();
		try {
			context.getLogger().info("Invocazione in corso ...");     
			getAuthorizationRules().authorize(context);
			context.getLogger().debug("Autorizzazione completata con successo");     

			LeggiPendenzaDTO leggiPendenzaDTO = new LeggiPendenzaDTO(context.getAuthentication());
			leggiPendenzaDTO.setCodA2A(idA2A);
			leggiPendenzaDTO.setCodPendenza(idPendenza);
			leggiPendenzaDTO.setInfoIncasso(true); 

			PendenzeDAO pendenzeDAO = new PendenzeDAO(); 

			LeggiPendenzaDTOResponse ricevutaDTOResponse = pendenzeDAO.leggiPendenza(leggiPendenzaDTO);
			
 			Dominio dominio = ricevutaDTOResponse.getDominio();
			TipoVersamento tipoVersamento = ricevutaDTOResponse.getTipoVersamento();
			// controllo che il dominio e tipo versamento siano autorizzati
			if(!AuthorizationManager.isTipoVersamentoDominioAuthorized(leggiPendenzaDTO.getUser(), dominio.getCodDominio(), tipoVersamento.getCodTipoVersamento())) {
				throw AuthorizationManager.toNotAuthorizedException(leggiPendenzaDTO.getUser(), dominio.getCodDominio(), tipoVersamento.getCodTipoVersamento());
			}

			List<PagamentoPortale> pagamenti = ricevutaDTOResponse.getPagamenti();
			List<Rpt> transazioni = ricevutaDTOResponse.getRpts();

			Pendenza pendenza = PendenzeConverter.toPendenza(ricevutaDTOResponse.getVersamentoIncasso(), pagamenti, transazioni); 
			context.getLogger().info("Invocazione completata con successo");
			return pendenza;

		}
		catch(javax.ws.rs.WebApplicationException e) {
			context.getLogger().error("Invocazione terminata con errore '4xx': %s",e, e.getMessage());
			throw e;
		}
		catch(Throwable e) {
			context.getLogger().error("Invocazione terminata con errore: %s",e, e.getMessage());
			throw WebApplicationExceptionMapper.handleException(e);
//			throw FaultCode.ERRORE_INTERNO.toException(e);
		}
	}
	
	@Override
	public void updatePendenza(String idA2A, String idPendenza, List<PatchOp> body) {
		// TODO Auto-generated method stub
	}

//	@Override
//	public TipiPendenza findTipiPendenza(Integer offset, Integer limit, String fields, String sort, String idDominio) {
//		throw FaultCode.ERRORE_INTERNO.toException("TODO");
//	}

}

