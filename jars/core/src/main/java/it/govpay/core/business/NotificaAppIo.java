package it.govpay.core.business;

import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.pagamento.NotificheAppIoBD;

public class NotificaAppIo extends BasicBD{
	
	public NotificaAppIo(BasicBD basicBD) {
		super(basicBD);
	}

	public List<it.govpay.bd.model.NotificaAppIo> findNotificheDaSpedire(Integer offset, Integer limit) throws ServiceException{
		NotificheAppIoBD notificheBD = new NotificheAppIoBD(this);
		return notificheBD.findNotificheDaSpedire(offset, limit);
	}
}
