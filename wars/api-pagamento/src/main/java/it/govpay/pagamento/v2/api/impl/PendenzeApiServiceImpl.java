package it.govpay.pagamento.v2.api.impl;

import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.openspcoop2.utils.jaxrs.fault.FaultCode;
import org.openspcoop2.utils.jaxrs.impl.BaseImpl;
import org.openspcoop2.utils.jaxrs.impl.ServiceContext;

import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.Rpt;
import it.govpay.core.dao.pagamenti.PendenzeDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTO;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTOResponse;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTO;
import it.govpay.core.dao.pagamenti.dto.ListaPendenzeDTOResponse;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.pagamento.v2.acl.Acl;
import it.govpay.pagamento.v2.acl.AuthorizationRules;
import it.govpay.pagamento.v2.acl.impl.TipoUtenzaOnlyAcl;
import it.govpay.pagamento.v2.api.PendenzeApi;
import it.govpay.pagamento.v2.beans.Pendenza;
import it.govpay.pagamento.v2.beans.Pendenze;
import it.govpay.pagamento.v2.beans.StatoPendenza;
import it.govpay.pagamento.v2.beans.converter.PendenzeConverter;
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

	/**
	 * Elenco delle pendenze
	 *
	 * Fornisce la lista delle pendenze filtrata ed ordinata.
	 *
	 */
	@Override
	public Pendenze findPendenze(Integer offset, Integer limit, String fields, String sort, String idDominio, String iuv, String idA2A, String idPendenza, String idPagatore, StatoPendenza statoPendenza, String idSessionePortale) {
		ServiceContext context = this.getContext();
		try {
			context.getLogger().info("Invocazione in corso ...");     
			getAuthorizationRules().authorize(context);
			context.getLogger().debug("Autorizzazione completata con successo");    

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
			// INIT DAO

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
			throw FaultCode.ERRORE_INTERNO.toException(e);
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
		ServiceContext context = this.getContext();
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
			throw FaultCode.ERRORE_INTERNO.toException(e);
		}
	}

}

