package it.govpay.rs.v1.beans.ragioneria.converter;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Dominio;
import it.govpay.core.rs.v1.beans.ragioneria.PendenzaIndex;
import it.govpay.core.rs.v1.beans.ragioneria.StatoPendenza;
import it.govpay.core.rs.v1.beans.ragioneria.TassonomiaAvviso;
import it.govpay.rs.v1.controllers.ragioneria.AnagraficaConverter;
import it.govpay.rs.v1.controllers.ragioneria.UnitaOperativaConverter;

public class PendenzeConverter {
	
	public static PendenzaIndex toRsModel(it.govpay.bd.model.Versamento versamento) throws ServiceException {
		PendenzaIndex rsModel = new PendenzaIndex();
	
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
	rsModel.setDominio(DominiConverter.toRsIndexModel(versamento.getDominio(null)));
	
	rsModel.setIdA2A(versamento.getApplicazione(null).getCodApplicazione());
	rsModel.setIdPendenza(versamento.getCodVersamentoEnte());
	rsModel.setImporto(versamento.getImportoTotale());
	rsModel.setNome(versamento.getNome());
	rsModel.setNumeroAvviso(versamento.getNumeroAvviso());
	rsModel.setSoggettoPagatore(AnagraficaConverter.toSoggettoRsModel(versamento.getAnagraficaDebitore()));
	rsModel.setDatiAllegati(versamento.getDatiAllegati());
	
	StatoPendenza statoPendenza = null;

	switch(versamento.getStatoVersamento()) {
	case ANNULLATO: statoPendenza = StatoPendenza.ANNULLATA;
		break;
	case ANOMALO: statoPendenza = StatoPendenza.NON_ESEGUITA;
		break;
	case ESEGUITO: statoPendenza = StatoPendenza.ESEGUITA;
		break;
	case ESEGUITO_SENZA_RPT:  statoPendenza = StatoPendenza.ESEGUITA;
		break;
	case NON_ESEGUITO: if(versamento.getDataScadenza() != null && versamento.getDataScadenza().before(new Date())) {statoPendenza = StatoPendenza.SCADUTA;} else { statoPendenza = StatoPendenza.NON_ESEGUITA;}
		break;
	case PARZIALMENTE_ESEGUITO:  statoPendenza = StatoPendenza.ESEGUITA_PARZIALE;
		break;
	default:
		break;
	
	}

	rsModel.setStato(statoPendenza);
	rsModel.setTassonomia(versamento.getTassonomia());
	rsModel.setTassonomiaAvviso(TassonomiaAvviso.fromValue(versamento.getTassonomiaAvviso()));
	rsModel.setNumeroAvviso(versamento.getNumeroAvviso());
	
	if(versamento.getUo(null) != null && !versamento.getUo(null).getCodUo().equals(Dominio.EC))
		rsModel.setUnitaOperativa(UnitaOperativaConverter.toRsModel(versamento.getUo(null)));
	
	return rsModel;
}
	public static PendenzaIndex toRsIndexModel(it.govpay.bd.model.Versamento versamento) throws ServiceException {
	PendenzaIndex rsModel = new PendenzaIndex();
	
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
	rsModel.setDominio(DominiConverter.toRsIndexModel(versamento.getDominio(null)));
	
	rsModel.setIdA2A(versamento.getApplicazione(null).getCodApplicazione());
	rsModel.setIdPendenza(versamento.getCodVersamentoEnte());
	rsModel.setImporto(versamento.getImportoTotale());
	rsModel.setNome(versamento.getNome());
	rsModel.setNumeroAvviso(versamento.getNumeroAvviso());
	rsModel.setSoggettoPagatore(AnagraficaConverter.toSoggettoRsModel(versamento.getAnagraficaDebitore()));
	rsModel.setDatiAllegati(versamento.getDatiAllegati());
	
	StatoPendenza statoPendenza = null;

	switch(versamento.getStatoVersamento()) {
	case ANNULLATO: statoPendenza = StatoPendenza.ANNULLATA;
		break;
	case ANOMALO: statoPendenza = StatoPendenza.NON_ESEGUITA;
		break;
	case ESEGUITO: statoPendenza = StatoPendenza.ESEGUITA;
		break;
	case ESEGUITO_SENZA_RPT:  statoPendenza = StatoPendenza.ESEGUITA;
		break;
	case NON_ESEGUITO: if(versamento.getDataScadenza() != null && versamento.getDataScadenza().before(new Date())) {statoPendenza = StatoPendenza.SCADUTA;} else { statoPendenza = StatoPendenza.NON_ESEGUITA;}
		break;
	case PARZIALMENTE_ESEGUITO:  statoPendenza = StatoPendenza.ESEGUITA_PARZIALE;
		break;
	default:
		break;
	
	}

	rsModel.setStato(statoPendenza);
	rsModel.setTassonomia(versamento.getTassonomia());
	rsModel.setTassonomiaAvviso(TassonomiaAvviso.fromValue(versamento.getTassonomiaAvviso()));
	rsModel.setNumeroAvviso(versamento.getNumeroAvviso());
	
	if(versamento.getUo(null) != null && !versamento.getUo(null).getCodUo().equals(Dominio.EC))
		rsModel.setUnitaOperativa(UnitaOperativaConverter.toRsModel(versamento.getUo(null)));
	
	return rsModel;
}

}
