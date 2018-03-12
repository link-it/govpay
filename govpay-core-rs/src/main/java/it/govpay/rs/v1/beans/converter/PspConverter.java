package it.govpay.rs.v1.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.rs.v1.beans.Canale;
import it.govpay.core.rs.v1.beans.Psp;
import it.govpay.core.rs.v1.beans.base.ModelloPagamento;
import it.govpay.core.rs.v1.beans.base.TipoVersamento;
import it.govpay.core.utils.UriBuilderUtils;

public class PspConverter {
	
	public static Psp toRsModel(it.govpay.bd.model.Psp psp) throws ServiceException {
		Psp rsModel = new Psp();
		rsModel.setAbilitato(psp.isAbilitato());
		rsModel.setBollo(psp.isBolloGestito());
		rsModel.setStorno(psp.isStornoGestito());
		rsModel.setIdPsp(psp.getCodPsp());
		rsModel.setRagioneSociale(psp.getRagioneSociale());
		rsModel.setCanali(UriBuilderUtils.getCanali(rsModel.getIdPsp())); 
		
		return rsModel;
	}

	
	public static Canale toCanaleRsModel(it.govpay.bd.model.Canale canale, it.govpay.bd.model.Psp psp) throws ServiceException {
		Canale rsModel = new Canale();
		rsModel.setAbilitato(canale.isAbilitato());
		rsModel.setIdCanale(canale.getCodCanale());
		rsModel.setModelloPagamento(ModelloPagamento.fromValue(canale.getModelloPagamento().getCodifica()+""));
		rsModel.setPsp(UriBuilderUtils.getPsp(psp.getCodPsp()));
		rsModel.setTipoVersamento(TipoVersamento.fromValue(canale.getTipoVersamento().getCodifica()));
		
		return rsModel;
	}
}
