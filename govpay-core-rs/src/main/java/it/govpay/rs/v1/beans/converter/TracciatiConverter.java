package it.govpay.rs.v1.beans.converter;

import java.math.BigDecimal;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.serialization.IDeserializer;
import org.openspcoop2.utils.serialization.IOException;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;

import it.govpay.bd.model.OperazioneAnnullamento;
import it.govpay.bd.model.OperazioneCaricamento;
import it.govpay.core.rs.v1.beans.base.OperazionePendenza;
import it.govpay.core.rs.v1.beans.base.OperazionePendenza.StatoEnum;
import it.govpay.core.rs.v1.beans.base.OperazionePendenza.TipoOperazioneEnum;
import it.govpay.core.rs.v1.beans.base.StatoTracciatoPendenza;
import it.govpay.core.rs.v1.beans.base.Tracciato;
import it.govpay.core.rs.v1.beans.base.TracciatoPendenze;
import it.govpay.core.utils.SimpleDateFormatUtils;

public class TracciatiConverter {


	public static Tracciato toRsModel(it.govpay.bd.model.Tracciato tracciato) throws ServiceException {
		Tracciato rsModel = new Tracciato();

		rsModel.setId(BigDecimal.valueOf(tracciato.getId()));
		rsModel.setCodDominio(tracciato.getCodDominio());
		rsModel.setTipo(tracciato.getTipo().toString());
		rsModel.setStato(tracciato.getStato().toString());
		rsModel.setDescrizioneStato(tracciato.getDescrizioneStato());
		rsModel.setDataCaricamento(tracciato.getDataCaricamento());
		rsModel.setDataCompletamento(tracciato.getDataCompletamento());
		rsModel.setBeanDati(tracciato.getBeanDati());
		rsModel.setFilenameRichiesta(tracciato.getFileNameRichiesta());
		rsModel.setFilenameEsito(tracciato.getFileNameEsito());

		return rsModel;
	}
	
	public static TracciatoPendenze toTracciatoPendenzeRsModel(it.govpay.bd.model.Tracciato tracciato) throws ServiceException {
		TracciatoPendenze rsModel = new TracciatoPendenze();

		rsModel.setId(BigDecimal.valueOf(tracciato.getId()));
		rsModel.setDataOraCaricamento(tracciato.getDataCaricamento());
		rsModel.setNomeFile(tracciato.getFileNameRichiesta());
		
		SerializationConfig config = new SerializationConfig();
		config.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinuti());
		config.setIgnoreNullValues(true);
		
		IDeserializer deserializer;
		try {
			deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, config);
			it.govpay.core.beans.tracciati.Pendenza beanDati = (it.govpay.core.beans.tracciati.Pendenza) deserializer.getObject(tracciato.getBeanDati(), it.govpay.core.beans.tracciati.Pendenza.class);
			
			rsModel.setOperatoreMittente(beanDati.getOperatore());
			rsModel.setNumeroOperazioniEseguite(BigDecimal.valueOf(beanDati.getNumOperazioniOk()));
			rsModel.setNumeroOperazioniFallite(BigDecimal.valueOf(beanDati.getNumOperazioniKo()));
			rsModel.setNumeroOperazioniTotali(BigDecimal.valueOf(beanDati.getNumLineeTotali()));
			rsModel.setDataOraUltimoAggiornamento(beanDati.getDataUltimoAggiornamento());
		} catch (IOException e) {
			throw new ServiceException(e);
		}
		
		if(tracciato.getStato() != null) {
			rsModel.setStato(StatoTracciatoPendenza.valueOf(tracciato.getStato().toString()));
		}

		return rsModel;
	}
	
	public static OperazionePendenza toOperazioneTracciatoPendenzaRsModel(it.govpay.bd.model.Operazione operazione) throws ServiceException {
		OperazionePendenza rsModel = new OperazionePendenza();
		
		rsModel.setApplicazione(operazione.getApplicazione(null).getCodApplicazione());
		
		switch (operazione.getStato()) {
		case ESEGUITO_KO:
			rsModel.setStato(StatoEnum.SCARTATO);
			break;
		case ESEGUITO_OK:
			rsModel.setStato(StatoEnum.ESEGUITO);
			break;
		case NON_VALIDO:
			rsModel.setStato(StatoEnum.NON_VALIDO);
			break;
		}
		
		switch (operazione.getTipoOperazione()) {
		case ADD:
			rsModel.setTipoOperazione(TipoOperazioneEnum.ADD);
			OperazioneCaricamento opCaricamento = (OperazioneCaricamento) operazione;
			rsModel.setEnteCreditore(opCaricamento.getCodDominio());
			rsModel.setDebitore(opCaricamento.getAnagraficaDebitore() + ", "+ opCaricamento.getCfDebitore());
			rsModel.setIdentificativoPendenza(operazione.getCodVersamentoEnte());
			rsModel.setNomePendenza(opCaricamento.getCausale());
			rsModel.setNumeroAvviso(opCaricamento.getNumeroAvviso()); 
			rsModel.setDescrizioneStato(opCaricamento.getDettaglioEsito()); 
			break;
		case DEL:
			rsModel.setTipoOperazione(TipoOperazioneEnum.DEL);
			OperazioneAnnullamento opAnnullamento = (OperazioneAnnullamento) operazione;
			rsModel.setDescrizioneStato(opAnnullamento.getMotivoAnnullamento());
			break;
		
		case N_V:
			rsModel.setTipoOperazione(TipoOperazioneEnum.NON_VALIDA);
			break;
		case INC:
		default:
			rsModel.setTipoOperazione(TipoOperazioneEnum.NON_VALIDA);
			break;
		}

		return rsModel;
	}
}
