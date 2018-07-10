package it.govpay.core.dao.pagamenti;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.bd.pagamento.filters.TracciatoFilter;
import it.govpay.core.dao.commons.BaseDAO;
import it.govpay.core.dao.pagamenti.dto.LeggiTracciatoDTO;
import it.govpay.core.dao.pagamenti.dto.ListaTracciatiDTO;
import it.govpay.core.dao.pagamenti.dto.ListaTracciatiDTOResponse;
import it.govpay.core.dao.pagamenti.exception.TracciatoNonTrovatoException;
import it.govpay.core.exceptions.NotAuthenticatedException;
import it.govpay.core.exceptions.NotAuthorizedException;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.model.Acl.Diritti;
import it.govpay.model.Acl.Servizio;
import it.govpay.model.Tracciato;

public class TracciatiDAO extends BaseDAO{

	public TracciatiDAO() {
	}

	public Tracciato leggiTracciato(LeggiTracciatoDTO leggiTracciatoDTO) throws ServiceException,TracciatoNonTrovatoException, NotAuthorizedException, NotAuthenticatedException{

		BasicBD bd = null;

		try {
			bd = BasicBD.newInstance(GpThreadLocal.get().getTransactionId());
			// controllo che il dominio sia autorizzato
//			this.autorizzaRichiesta(leggiTracciatoDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, leggiTracciatoDTO.getId(), null, bd);

			TracciatiBD tracciatoBD = new TracciatiBD(bd);
			return tracciatoBD.getTracciato(leggiTracciatoDTO.getId());

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
			// controllo che il dominio sia autorizzato
//			this.autorizzaRichiesta(leggiTracciatoDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, leggiTracciatoDTO.getId(), null, bd);

			TracciatiBD tracciatoBD = new TracciatiBD(bd);
			byte[] rawRichiesta = tracciatoBD.getTracciato(leggiTracciatoDTO.getId()).getRawRichiesta();
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
			// controllo che il dominio sia autorizzato
//			this.autorizzaRichiesta(leggiTracciatoDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, leggiTracciatoDTO.getId(), null, bd);

			TracciatiBD tracciatoBD = new TracciatiBD(bd);
			byte[] rawEsito = tracciatoBD.getTracciato(leggiTracciatoDTO.getId()).getRawEsito();
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
		this.autorizzaRichiesta(listaTracciatiDTO.getUser(), Servizio.PAGAMENTI_E_PENDENZE, Diritti.LETTURA, bd);

		TracciatiBD tracciatoBD = new TracciatiBD(bd);
		TracciatoFilter filter = tracciatoBD.newFilter();

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
