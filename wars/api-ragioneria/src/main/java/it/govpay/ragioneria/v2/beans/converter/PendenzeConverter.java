package it.govpay.ragioneria.v2.beans.converter;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jaxrs.RawObject;

import it.govpay.bd.model.SingoloVersamento;
import it.govpay.ragioneria.v2.beans.Pendenza;
import it.govpay.ragioneria.v2.beans.TassonomiaAvviso;
import it.govpay.ragioneria.v2.beans.VocePendenza;

public class PendenzeConverter {

	public static Pendenza toRsModel(it.govpay.bd.model.Versamento versamento) throws ServiceException {
		Pendenza rsModel = new Pendenza();

		if(versamento.getCodAnnoTributario()!= null)
			rsModel.setAnnoRiferimento(new BigDecimal(versamento.getCodAnnoTributario()));

		if(versamento.getCausaleVersamento()!= null)
			try {
				rsModel.setCausale(versamento.getCausaleVersamento().getSimple());
			} catch (UnsupportedEncodingException e) {
				throw new ServiceException(e); 
			}

		rsModel.setDataCaricamento(versamento.getDataCreazione());
		rsModel.setDataScadenza(versamento.getDataScadenza());
		rsModel.setDataValidita(versamento.getDataValidita());
		rsModel.setIdDominio(versamento.getDominio(null).getCodDominio());
		rsModel.setIdA2A(versamento.getApplicazione(null).getCodApplicazione());
		rsModel.setIdPendenza(versamento.getCodVersamentoEnte());
		if(versamento.getDatiAllegati() != null)
			rsModel.setDatiAllegati(new RawObject(versamento.getDatiAllegati()));

		rsModel.setTassonomiaAvviso(TassonomiaAvviso.fromValue(versamento.getTassonomiaAvviso()));

		if(versamento.getUo(null) != null && !versamento.getUo(null).getCodUo().equals(it.govpay.model.Dominio.EC))
			rsModel.setIdUnitaOperativa(versamento.getUo(null).getCodUo());
		
		rsModel.setIdTipoPendenza(versamento.getTipoVersamento(null).getCodTipoVersamento());
		rsModel.setDirezione(versamento.getDirezione());
		rsModel.setDivisione(versamento.getDivisione());
		rsModel.setTassonomia(versamento.getTassonomia()); 
		
		return rsModel;
	}

	public static VocePendenza toRsModelVocePendenza(SingoloVersamento singoloVersamento, int indice) throws ServiceException {
		VocePendenza rsModel = new VocePendenza();
		
		if(singoloVersamento.getDatiAllegati() != null)
			rsModel.setDatiAllegati(new RawObject(singoloVersamento.getDatiAllegati()));
		rsModel.setDescrizione(singoloVersamento.getDescrizione());
		rsModel.setDescrizioneCausaleRPT(singoloVersamento.getDescrizioneCausaleRPT());

		rsModel.setIdVocePendenza(singoloVersamento.getCodSingoloVersamentoEnte());
		rsModel.setImporto(singoloVersamento.getImportoSingoloVersamento());
		rsModel.setIndice(new BigDecimal(indice));
		rsModel.setPendenza(toRsModel(singoloVersamento.getVersamento(null)));
		return rsModel;
	}

}
