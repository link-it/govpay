package it.govpay.core.dao.pagamenti;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Utenza;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.filters.TracciatoFilter;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiTracciatoDTO;
import it.govpay.core.dao.pagamenti.dto.ListaTracciatiDTO;
import it.govpay.core.dao.pagamenti.dto.ListaTracciatiDTOResponse;
import it.govpay.core.dao.pagamenti.exception.TracciatoNonTrovatoException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.AclEngine;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Tracciato;
import it.govpay.model.Tracciato.TIPO_TRACCIATO;

public class TracciatiDAO extends BaseDAO{

	public TracciatiDAO() {
	}

	public Tracciato leggiTracciato(LeggiTracciatoDTO leggiTracciatoDTO) throws ServiceException,TracciatoNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{

		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			List<String> listaDominiFiltro;
			this.autorizzaRichiesta(leggiTracciatoDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, bd);

			// Autorizzazione sui domini
			listaDominiFiltro = AclEngine.getDominiAutorizzati((Utenza) leggiTracciatoDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA);
			if(listaDominiFiltro == null) {
				throw new NotAuthorizedException("L'utenza autenticata ["+leggiTracciatoDTO.getUser().getPrincipal()+"] non e' autorizzata ai servizi " + Servizio.PAGAMENTI_E_PENDENZE + " per alcun dominio");
			}

			TracciatiBD tracciatoBD = new TracciatiBD(bd);
			Tracciato tracciato = tracciatoBD.getTracciato(leggiTracciatoDTO.getId());
			return tracciato;

		} catch (NotFoundException e) {
			throw new TracciatoNonTrovatoException(e.getMessage(), e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public byte[] leggiRichiestaTracciato(LeggiTracciatoDTO leggiTracciatoDTO) throws ServiceException,TracciatoNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{

		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			List<String> listaDominiFiltro;
			this.autorizzaRichiesta(leggiTracciatoDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, bd);

			// Autorizzazione sui domini
			listaDominiFiltro = AclEngine.getDominiAutorizzati((Utenza) leggiTracciatoDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA);
			if(listaDominiFiltro == null) {
				throw new NotAuthorizedException("L'utenza autenticata ["+leggiTracciatoDTO.getUser().getPrincipal()+"] non e' autorizzata ai servizi " + Servizio.PAGAMENTI_E_PENDENZE + " per alcun dominio");
			}

			TracciatiBD tracciatoBD = new TracciatiBD(bd);

			Tracciato tracciato = tracciatoBD.getTracciato(leggiTracciatoDTO.getId());
			byte[] rawRichiesta = tracciato.getRawRichiesta();
			if(rawRichiesta == null)
				throw new NotFoundException("File di richiesta non salvato");
			return rawRichiesta;

		} catch (NotFoundException e) {
			throw new TracciatoNonTrovatoException(e.getMessage(), e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public byte[] leggiEsitoTracciato(LeggiTracciatoDTO leggiTracciatoDTO) throws ServiceException,TracciatoNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{

		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			List<String> listaDominiFiltro;
			this.autorizzaRichiesta(leggiTracciatoDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, bd);

			// Autorizzazione sui domini
			listaDominiFiltro = AclEngine.getDominiAutorizzati((Utenza) leggiTracciatoDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA);
			if(listaDominiFiltro == null) {
				throw new NotAuthorizedException("L'utenza autenticata ["+leggiTracciatoDTO.getUser().getPrincipal()+"] non e' autorizzata ai servizi " + Servizio.PAGAMENTI_E_PENDENZE + " per alcun dominio");
			}

			TracciatiBD tracciatoBD = new TracciatiBD(bd);

			Tracciato tracciato = tracciatoBD.getTracciato(leggiTracciatoDTO.getId());
			byte[] rawEsito = tracciato.getRawEsito();
			if(rawEsito == null)
				throw new NotFoundException("File di esito non salvato");
			return rawEsito;


		} catch (NotFoundException e) {
			throw new TracciatoNonTrovatoException(e.getMessage(), e);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}
	
	public ListaTracciatiDTOResponse listaTracciati(ListaTracciatiDTO listaTracciatiDTO) throws ServiceException, NotAuthorizedException, NotAuthenticatedException{
		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());

			return listaTracciati(listaTracciatiDTO, bd);
		} finally {
			if(bd != null)
				bd.closeConnection();
		}
	}

	public ListaTracciatiDTOResponse listaTracciati(ListaTracciatiDTO listaTracciatiDTO, BasicBD bd) throws NotAuthenticatedException, NotAuthorizedException, ServiceException {

		List<String> listaDominiFiltro;
		this.autorizzaRichiesta(listaTracciatiDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, bd);

		// Autorizzazione sui domini
		listaDominiFiltro = AclEngine.getDominiAutorizzati((Utenza) listaTracciatiDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA);
		if(listaDominiFiltro == null) {
			throw new NotAuthorizedException("L'utenza autenticata ["+listaTracciatiDTO.getUser().getPrincipal()+"] non e' autorizzata ai servizi " + Servizio.PAGAMENTI_E_PENDENZE + " per alcun dominio");
		}

		TracciatiBD tracciatoBD = new TracciatiBD(bd);
		TracciatoFilter filter = tracciatoBD.newFilter();

		filter.setDomini(listaDominiFiltro);
		List<TIPO_TRACCIATO> tipo = new ArrayList<>();
		tipo.add(TIPO_TRACCIATO.AV);
		tipo.add(TIPO_TRACCIATO.AV_ESITO);
		filter.setTipo(tipo);
		filter.setOffset(listaTracciatiDTO.getOffset());
		filter.setLimit(listaTracciatiDTO.getLimit());

		long count = tracciatoBD.count(filter);

		List<Tracciato> resList = new ArrayList<Tracciato>();
		if(count > 0) {
			resList = tracciatoBD.findAll(filter);
		} 

		return new ListaTracciatiDTOResponse(count, resList);
	}
}
