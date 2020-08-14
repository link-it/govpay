package it.govpay.core.business;

import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.pagamento.NotificheAppIoBD;

public class NotificaAppIo {
	
	public NotificaAppIo( ) {
	}

	public List<it.govpay.bd.model.NotificaAppIo> findNotificheDaSpedire(Integer offset, Integer limit) throws ServiceException{
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		NotificheAppIoBD notificheBD = new NotificheAppIoBD(configWrapper);
		return notificheBD.findNotificheDaSpedire(offset, limit);
	}
}
