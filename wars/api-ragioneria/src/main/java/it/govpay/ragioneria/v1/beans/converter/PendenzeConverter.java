package it.govpay.ragioneria.v1.beans.converter;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.Date;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jaxrs.RawObject;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.ragioneria.v1.beans.PendenzaIndex;
import it.govpay.ragioneria.v1.beans.StatoPendenza;
import it.govpay.ragioneria.v1.beans.TassonomiaAvviso;
import it.govpay.ragioneria.v1.beans.TipoContabilita;
import it.govpay.ragioneria.v1.beans.VocePendenza;

public class PendenzeConverter {

	public static PendenzaIndex toRsModel(it.govpay.bd.model.Versamento versamento) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		PendenzaIndex rsModel = new PendenzaIndex();

		if(versamento.getCodAnnoTributario()!= null)
			rsModel.setAnnoRiferimento(new BigDecimal(versamento.getCodAnnoTributario()));
		
		rsModel.setCartellaPagamento(versamento.getCodLotto());

		if(versamento.getCausaleVersamento()!= null)
			try {
				rsModel.setCausale(versamento.getCausaleVersamento().getSimple());
			} catch (UnsupportedEncodingException e) {
				throw new ServiceException(e);
			}

		rsModel.setDataCaricamento(versamento.getDataCreazione());
		rsModel.setDataScadenza(versamento.getDataScadenza());
		rsModel.setDataValidita(versamento.getDataValidita());
		rsModel.setDominio(DominiConverter.toRsModelIndex(versamento.getDominio(configWrapper)));

		rsModel.setIdA2A(versamento.getApplicazione(configWrapper).getCodApplicazione());
		rsModel.setIdPendenza(versamento.getCodVersamentoEnte());
		rsModel.setImporto(versamento.getImportoTotale());
		rsModel.setNome(versamento.getNome());
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());
		rsModel.setSoggettoPagatore(AnagraficaConverter.toSoggettoRsModel(versamento.getAnagraficaDebitore()));
		if(versamento.getDatiAllegati() != null)
			rsModel.setDatiAllegati(new RawObject(versamento.getDatiAllegati()));

		StatoPendenza statoPendenza = null;

		switch(versamento.getStatoVersamento()) {
		case ANNULLATO: statoPendenza = StatoPendenza.ANNULLATA;
		break;
		case ESEGUITO: statoPendenza = StatoPendenza.ESEGUITA;
		break;
		case ESEGUITO_ALTRO_CANALE:  statoPendenza = StatoPendenza.ESEGUITA;
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

		UnitaOperativa uo = versamento.getUo(configWrapper);
		if(uo != null && !uo.getCodUo().equals(it.govpay.model.Dominio.EC))
			rsModel.setUnitaOperativa(UnitaOperativaConverter.toRsModel(uo));
		
		return rsModel;
	}
	public static PendenzaIndex toRsIndexModel(it.govpay.bd.model.Versamento versamento) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		PendenzaIndex rsModel = new PendenzaIndex();

		if(versamento.getCodAnnoTributario()!= null)
			rsModel.setAnnoRiferimento(new BigDecimal(versamento.getCodAnnoTributario()));
		
		rsModel.setCartellaPagamento(versamento.getCodLotto());

		if(versamento.getCausaleVersamento()!= null)
			try {
				rsModel.setCausale(versamento.getCausaleVersamento().getSimple());
			} catch (UnsupportedEncodingException e) {
				throw new ServiceException(e);
			}

		rsModel.setDataCaricamento(versamento.getDataCreazione());
		rsModel.setDataScadenza(versamento.getDataScadenza());
		rsModel.setDataValidita(versamento.getDataValidita());
		rsModel.setDominio(DominiConverter.toRsModelIndex(versamento.getDominio(configWrapper)));

		rsModel.setIdA2A(versamento.getApplicazione(configWrapper).getCodApplicazione());
		rsModel.setIdPendenza(versamento.getCodVersamentoEnte());
		rsModel.setImporto(versamento.getImportoTotale());
		rsModel.setNome(versamento.getNome());
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());
		rsModel.setSoggettoPagatore(AnagraficaConverter.toSoggettoRsModel(versamento.getAnagraficaDebitore()));
		if(versamento.getDatiAllegati() != null)
			rsModel.setDatiAllegati(new RawObject(versamento.getDatiAllegati()));

		StatoPendenza statoPendenza = null;

		switch(versamento.getStatoVersamento()) {
		case ANNULLATO: statoPendenza = StatoPendenza.ANNULLATA;
		break;
		case ESEGUITO: statoPendenza = StatoPendenza.ESEGUITA;
		break;
		case ESEGUITO_ALTRO_CANALE:  statoPendenza = StatoPendenza.ESEGUITA;
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

		UnitaOperativa uo = versamento.getUo(configWrapper);
		if(uo != null && !uo.getCodUo().equals(it.govpay.model.Dominio.EC))
			rsModel.setUnitaOperativa(UnitaOperativaConverter.toRsModel(uo));

		return rsModel;
	}

	public static VocePendenza toRsModelVocePendenza(SingoloVersamento singoloVersamento, int indice, BDConfigWrapper configWrapper) throws ServiceException {
		VocePendenza rsModel = new VocePendenza();
		
		if(singoloVersamento.getDatiAllegati() != null)
			rsModel.setDatiAllegati(new RawObject(singoloVersamento.getDatiAllegati()));
		rsModel.setDescrizione(singoloVersamento.getDescrizione());
		rsModel.setDescrizioneCausaleRPT(singoloVersamento.getDescrizioneCausaleRPT());

		rsModel.setIdVocePendenza(singoloVersamento.getCodSingoloVersamentoEnte());
		rsModel.setImporto(singoloVersamento.getImportoSingoloVersamento());
		rsModel.setIndice(new BigDecimal(indice));


		switch(singoloVersamento.getStatoSingoloVersamento()) {
		case ESEGUITO:rsModel.setStato(VocePendenza.StatoEnum.ESEGUITO);
		break;
		case NON_ESEGUITO:rsModel.setStato(VocePendenza.StatoEnum.NON_ESEGUITO);
		break;
		default:
			break;}

		// Definisce i dati di un bollo telematico
		if(singoloVersamento.getHashDocumento() != null && singoloVersamento.getTipoBollo() != null && singoloVersamento.getProvinciaResidenza() != null) {
			rsModel.setHashDocumento(singoloVersamento.getHashDocumento());
			rsModel.setTipoBollo(singoloVersamento.getTipoBollo().getCodifica());
			rsModel.setProvinciaResidenza(singoloVersamento.getProvinciaResidenza());
		} else if(singoloVersamento.getTributo(configWrapper) != null && singoloVersamento.getTributo(configWrapper).getCodTributo() != null) { // Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.
			rsModel.setCodEntrata(singoloVersamento.getTributo(configWrapper).getCodTributo());
		} else { // Definisce i dettagli di incasso della singola entrata.
			rsModel.setCodiceContabilita(singoloVersamento.getCodContabilita());
			rsModel.setIbanAccredito(singoloVersamento.getIbanAccredito(configWrapper).getCodIban());
			if(singoloVersamento.getTipoContabilita() != null)
				rsModel.setTipoContabilita(TipoContabilita.valueOf(singoloVersamento.getTipoContabilita().name()));
		}


		return rsModel;
	}

}
