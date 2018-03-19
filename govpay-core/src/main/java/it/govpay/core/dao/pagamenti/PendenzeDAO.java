package it.govpay.core.dao.pagamenti;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Utenza;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.filters.VersamentoFilter;
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
import it.govpay.core.utils.AclEngine;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Versamento.StatoVersamento;
import it.govpay.servizi.commons.EsitoOperazione;

public class PendenzeDAO extends BaseDAO{

	public PendenzeDAO() {
	}

	public ListaPendenzeDTOResponse listaPendenze(ListaPendenzeDTO listaPendenzaDTO) throws ServiceException,PendenzaNonTrovataException, NotAuthorizedException, NotAuthenticatedException{
		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());

		try {
			this.autorizzaRichiesta(listaPendenzaDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, bd);
			// Autorizzazione sui domini
			List<Long> idDomini = AclEngine.getIdDominiAutorizzati((Utenza) listaPendenzaDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA);
			if(idDomini == null) {
				throw new NotAuthorizedException("L'utenza autenticata ["+listaPendenzaDTO.getUser().getPrincipal()+"] non e' autorizzata ai servizi " + Servizio.PAGAMENTI_E_PENDENZE + " per alcun dominio");
			}
			
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			VersamentoFilter filter = versamentiBD.newFilter();
			
			if(idDomini != null && idDomini.size() > 0)
				filter.setIdDomini(idDomini);

			filter.setOffset(listaPendenzaDTO.getOffset());
			filter.setLimit(listaPendenzaDTO.getLimit());
			filter.setDataInizio(listaPendenzaDTO.getDataDa());
			filter.setDataFine(listaPendenzaDTO.getDataA());
			filter.setStatoVersamento(listaPendenzaDTO.getStato());
			filter.setCodDominio(listaPendenzaDTO.getIdDominio() );
			filter.setCodPagamentoPortale(listaPendenzaDTO.getIdPagamento());
			filter.setCodUnivocoDebitore(listaPendenzaDTO.getIdDebitore());
			filter.setCodApplicazione(listaPendenzaDTO.getIdA2A());
			filter.setFilterSortList(listaPendenzaDTO.getFieldSortList());

			long count = versamentiBD.count(filter);

			List<LeggiPendenzaDTOResponse> resList = new ArrayList<LeggiPendenzaDTOResponse>();
			if(count > 0) {
				List<Versamento> findAll = versamentiBD.findAll(filter);

				for (Versamento versamento : findAll) {
					LeggiPendenzaDTOResponse elem = new LeggiPendenzaDTOResponse();
					elem.setVersamento(versamento);
					elem.setApplicazione(versamento.getApplicazione(versamentiBD));
					elem.setDominio(versamento.getDominio(versamentiBD));
					elem.setUnitaOperativa(versamento.getUo(versamentiBD));
					List<SingoloVersamento> singoliVersamenti = versamento.getSingoliVersamenti(versamentiBD);
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
		}finally {
			bd.closeConnection();
		}
	}

	public LeggiPendenzaDTOResponse leggiPendenza(LeggiPendenzaDTO leggiPendenzaDTO) throws ServiceException,PendenzaNonTrovataException, NotAuthorizedException, NotAuthenticatedException{
		BasicBD bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
		LeggiPendenzaDTOResponse response = new LeggiPendenzaDTOResponse();

		VersamentiBD versamentiBD = new VersamentiBD(bd);
		Versamento versamento;
		try {
			this.autorizzaRichiesta(leggiPendenzaDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, bd);

			versamento = versamentiBD.getVersamento(AnagraficaManager.getApplicazione(versamentiBD, leggiPendenzaDTO.getCodA2A()).getId(), leggiPendenzaDTO.getCodPendenza());
			
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
			bd.closeConnection();
		}
		return response;
	}
	
	
	public PatchPendenzaDTOResponse cambioStato(PatchPendenzaDTO patchPendenzaDTO) throws PendenzaNonTrovataException, GovPayException, NotAuthorizedException, NotAuthenticatedException{
		
		PatchPendenzaDTOResponse response = new PatchPendenzaDTOResponse();
		BasicBD bd = null;
		
		try {
			this.autorizzaRichiesta(patchPendenzaDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.SCRITTURA, bd);
			
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());

			VersamentiBD versamentiBD = new VersamentiBD(bd);
			it.govpay.bd.model.Versamento versamentoLetto = versamentiBD.getVersamento(AnagraficaManager.getApplicazione(bd, patchPendenzaDTO.getIdA2a()).getId(), patchPendenzaDTO.getIdPendenza());
			
			// controllo che il dominio sia autorizzato
			this.autorizzaRichiesta(patchPendenzaDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.SCRITTURA, versamentoLetto.getDominio(bd).getCodDominio(), null, bd);
			
			StatoVersamento current = null;
			switch(patchPendenzaDTO.getStato()) {
			case ANNULLATO: current = StatoVersamento.ANNULLATO;
				break;
			case DA_PAGARE: current = StatoVersamento.NON_ESEGUITO;
				break;
			default:
				break;
			}
			
			StatoVersamento previous = current == StatoVersamento.ANNULLATO ? StatoVersamento.NON_ESEGUITO : StatoVersamento.ANNULLATO;
			
			// Se è già nello stesso stato non devo far nulla.
			if(versamentoLetto.getStatoVersamento().equals(current)) {
				return response;
			}
			
			// Se è in stato NON_ESEGUITO lo annullo
			if(versamentoLetto.getStatoVersamento().equals(previous)) {
				versamentoLetto.setStatoVersamento(current);
				versamentoLetto.setDescrizioneStato(patchPendenzaDTO.getDescrizioneStato());
				versamentiBD.updateVersamento(versamentoLetto);
				return response;
			}
			
			// Se non è nello stato corretto non posso aggiornarne lo stato
			throw new GovPayException(EsitoOperazione.VER_009, patchPendenzaDTO.getIdA2a(), versamentoLetto.getCodVersamentoEnte(), versamentoLetto.getStatoVersamento().toString());

		} catch (ServiceException e) {
			throw new GovPayException(e);
		} catch (NotFoundException e) {
			throw new PendenzaNonTrovataException(e.getMessage(), e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}

	}
	
	public PutPendenzaDTOResponse createOrUpdate(PutPendenzaDTO putVersamentoDTO) throws PendenzaNonTrovataException, GovPayException, NotAuthorizedException, NotAuthenticatedException{ 
		PutPendenzaDTOResponse dominioDTOResponse = new PutPendenzaDTOResponse();
		BasicBD bd = null;
		try {
			this.autorizzaRichiesta(putVersamentoDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.SCRITTURA, bd);
			
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());

			
			it.govpay.core.business.Versamento versamentoBusiness = new it.govpay.core.business.Versamento(bd);
			Versamento chiediVersamento = versamentoBusiness.chiediVersamento(putVersamentoDTO.getVersamento());
			
			// controllo che il dominio sia autorizzato
			this.autorizzaRichiesta(putVersamentoDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.SCRITTURA, chiediVersamento.getDominio(bd).getCodDominio(), null, bd);
			
			versamentoBusiness.caricaVersamento(chiediVersamento, false, true);

		} catch (ServiceException e) {
			throw new GovPayException(e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
		return dominioDTOResponse;
	}

}
