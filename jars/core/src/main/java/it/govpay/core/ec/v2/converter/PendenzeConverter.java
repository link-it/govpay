package it.govpay.core.ec.v2.converter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jaxrs.RawObject;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.gov.digitpa.schemas._2011.pagamenti.CtSoggettoVersante;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Versamento;
import it.govpay.ec.v2.beans.Documento;
import it.govpay.ec.v2.beans.LinguaSecondaria;
import it.govpay.ec.v2.beans.Pendenza;
import it.govpay.ec.v2.beans.ProprietaPendenza;
import it.govpay.ec.v2.beans.Soggetto;
import it.govpay.ec.v2.beans.TipoRiferimentoVocePendenza.TipoBolloEnum;
import it.govpay.ec.v2.beans.TipoSoggetto;
import it.govpay.ec.v2.beans.TipoSogliaVincoloPagamento;
import it.govpay.ec.v2.beans.VincoloPagamento;
import it.govpay.ec.v2.beans.VoceDescrizioneImporto;
import it.govpay.ec.v2.beans.VocePendenza;

public class PendenzeConverter {

	public static Pendenza toRsModel(it.govpay.bd.model.Versamento versamento) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		Pendenza rsModel = new Pendenza();

		if(versamento.getCodAnnoTributario()!= null)
			rsModel.setAnnoRiferimento(new BigDecimal(versamento.getCodAnnoTributario()));

		rsModel.setCartellaPagamento(versamento.getCodLotto());

		if(versamento.getCausaleVersamento()!= null)
			try {
				rsModel.setCausale(versamento.getCausaleVersamento().getSimple());
			} catch (UnsupportedEncodingException e) {
				throw new ServiceException(e); 
			}

		rsModel.setDataScadenza(versamento.getDataScadenza());

		rsModel.setDominio(DominiConverter.toRsModelIndex(versamento.getDominio(configWrapper)));
		rsModel.setIdA2A(versamento.getApplicazione(configWrapper).getCodApplicazione());
		rsModel.setIdPendenza(versamento.getCodVersamentoEnte());
		if(versamento.getDatiAllegati() != null)
			rsModel.setDatiAllegati(new RawObject(versamento.getDatiAllegati()));

		UnitaOperativa uo = versamento.getUo(configWrapper);
		if(uo != null && !uo.getCodUo().equals(it.govpay.model.Dominio.EC)) {
			rsModel.setUnitaOperativa(DominiConverter.toRsModelIndex(uo));
		}

		rsModel.setIdTipoPendenza(versamento.getTipoVersamento(configWrapper).getCodTipoVersamento());
		rsModel.setDirezione(versamento.getDirezione());
		rsModel.setDivisione(versamento.getDivisione());
		rsModel.setTassonomia(versamento.getTassonomia()); 
		rsModel.setUUID(versamento.getIdSessione());
		rsModel.setSoggettoPagatore(toSoggettoRsModel(versamento.getAnagraficaDebitore()));

		rsModel.setDocumento(toDocumentoRsModel(versamento));
		
		rsModel.setImporto(versamento.getImportoTotale());
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());
		rsModel.setDataValidita(versamento.getDataValidita());
		rsModel.setProprieta(toProprietaPendenzaRsModel(versamento.getProprietaPendenza()));
		return rsModel;
	}

	private static Documento toDocumentoRsModel(Versamento versamento) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		if(versamento.getDocumento(configWrapper) != null) {
			it.govpay.bd.model.Documento documento = versamento.getDocumento(configWrapper);

			Documento rsModel = new Documento();

			rsModel.setDescrizione(documento.getDescrizione());
			rsModel.setIdentificativo(documento.getCodDocumento());
			if(versamento.getNumeroRata() != null)
				rsModel.setRata(new BigDecimal(versamento.getNumeroRata()));
			if(versamento.getTipoSoglia() != null && versamento.getGiorniSoglia() != null) {
				VincoloPagamento soglia = new VincoloPagamento();
				soglia.setGiorni(new BigDecimal(versamento.getGiorniSoglia()));

				switch(versamento.getTipoSoglia()) {
				case ENTRO:
					soglia.setTipo(TipoSogliaVincoloPagamento.ENTRO.toString());
					break;
				case OLTRE:
					soglia.setTipo(TipoSogliaVincoloPagamento.OLTRE.toString());
					break;
				}
				rsModel.setSoglia(soglia );
			}
			return rsModel;
		}
		return null;
	}

	public static VocePendenza toRsModelVocePendenza(SingoloVersamento singoloVersamento, int indice) throws ServiceException, IOException, ValidationException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		return toRsModelVocePendenza(singoloVersamento, indice, singoloVersamento.getVersamento(configWrapper));
	}

	public static VocePendenza toRsModelVocePendenza(SingoloVersamento singoloVersamento, int indice, Versamento versamento) throws ServiceException, ValidationException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		VocePendenza rsModel = new VocePendenza();

		if(singoloVersamento.getDatiAllegati() != null)
			rsModel.setDatiAllegati(new RawObject(singoloVersamento.getDatiAllegati()));
		rsModel.setDescrizione(singoloVersamento.getDescrizione());
		rsModel.setDescrizioneCausaleRPT(singoloVersamento.getDescrizioneCausaleRPT());

		if(singoloVersamento.getDominio(configWrapper) != null) {
			rsModel.setDominio(DominiConverter.toRsModelIndex(singoloVersamento.getDominio(configWrapper)));
		}

		rsModel.setIdVocePendenza(singoloVersamento.getCodSingoloVersamentoEnte());
		//		rsModel.setImporto(singoloVersamento.getImportoSingoloVersamento());
		//		rsModel.setIndice(new BigDecimal(indice));
		rsModel.setPendenza(toRsModel(versamento));
		rsModel.setContabilita(ContabilitaConverter.toRsModel(singoloVersamento.getContabilita()));
		
		
		// Definisce i dati di un bollo telematico
		if(singoloVersamento.getHashDocumento() != null && singoloVersamento.getTipoBollo() != null && singoloVersamento.getProvinciaResidenza() != null) {
			rsModel.setHashDocumento(singoloVersamento.getHashDocumento());
			
			switch(singoloVersamento.getTipoBollo()) {
			case IMPOSTA_BOLLO:
				rsModel.setTipoBollo(TipoBolloEnum._01);
				break;
			}
			rsModel.setProvinciaResidenza(singoloVersamento.getProvinciaResidenza());
			if(singoloVersamento.getTipoContabilita() != null && singoloVersamento.getCodContabilita() != null)
				rsModel.setCodiceTassonomicoPagoPA(singoloVersamento.getTipoContabilita().getCodifica() + "/"+ singoloVersamento.getCodContabilita());
		} else if(singoloVersamento.getTributo(configWrapper) != null && singoloVersamento.getTributo(configWrapper).getCodTributo() != null) { // Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.
			rsModel.setCodEntrata(singoloVersamento.getTributo(configWrapper).getCodTributo());
		} else { // Definisce i dettagli di incasso della singola entrata.
			rsModel.setCodiceTassonomicoPagoPA(singoloVersamento.getCodContabilita());
			rsModel.setIbanAccredito(singoloVersamento.getIbanAccredito(configWrapper).getCodIban());
			if(singoloVersamento.getIbanAppoggio(configWrapper) !=null)
				rsModel.setIbanAccredito(singoloVersamento.getIbanAppoggio(configWrapper).getCodIban());
			if(singoloVersamento.getTipoContabilita() != null && singoloVersamento.getCodContabilita() != null)
				rsModel.setCodiceTassonomicoPagoPA(singoloVersamento.getTipoContabilita().getCodifica() + "/"+ singoloVersamento.getCodContabilita());
		}
		
		return rsModel;
	}

	public static Soggetto toSoggettoRsModel(it.govpay.model.Anagrafica anagrafica) {
		if(anagrafica == null) return null;
		Soggetto rsModel = new Soggetto();

		if(anagrafica.getTipo() != null)
			rsModel.setTipo(TipoSoggetto.fromValue(anagrafica.getTipo().toString()));

		rsModel.setIdentificativo(anagrafica.getCodUnivoco());
		rsModel.setAnagrafica(anagrafica.getRagioneSociale());
		rsModel.setIndirizzo(anagrafica.getIndirizzo());
		rsModel.setCivico(anagrafica.getCivico());
		rsModel.setCap(anagrafica.getCap());
		rsModel.setLocalita(anagrafica.getLocalita());
		rsModel.setProvincia(anagrafica.getProvincia());
		rsModel.setNazione(anagrafica.getNazione());
		rsModel.setEmail(anagrafica.getEmail());
		rsModel.setCellulare(anagrafica.getCellulare());

		return rsModel;
	}

	public static Soggetto toSoggettoRsModel(CtSoggettoVersante soggettoVersante) {
		if(soggettoVersante == null) return null;
		Soggetto rsModel = new Soggetto();

		if(soggettoVersante.getIdentificativoUnivocoVersante().getTipoIdentificativoUnivoco() != null)
			rsModel.setTipo(TipoSoggetto.fromValue(soggettoVersante.getIdentificativoUnivocoVersante().getTipoIdentificativoUnivoco().toString()));

		rsModel.setIdentificativo(soggettoVersante.getIdentificativoUnivocoVersante().getCodiceIdentificativoUnivoco());
		rsModel.setAnagrafica(soggettoVersante.getAnagraficaVersante());
		rsModel.setIndirizzo(soggettoVersante.getIndirizzoVersante());
		rsModel.setCivico(soggettoVersante.getCivicoVersante());
		rsModel.setCap(soggettoVersante.getCapVersante());
		rsModel.setLocalita(soggettoVersante.getLocalitaVersante());
		rsModel.setProvincia(soggettoVersante.getProvinciaVersante());
		rsModel.setNazione(soggettoVersante.getNazioneVersante());
		rsModel.setEmail(soggettoVersante.getEMailVersante());
		//		rsModel.setCellulare(soggettoVersante.getCellulare());

		return rsModel;
	}
	
	public static ProprietaPendenza toProprietaPendenzaRsModel(it.govpay.core.beans.tracciati.ProprietaPendenza proprieta) {
		ProprietaPendenza rsModel = null;
		if(proprieta != null) {
			rsModel = new ProprietaPendenza();
			
			if(proprieta.getDescrizioneImporto() != null && !proprieta.getDescrizioneImporto().isEmpty()) {
				List<VoceDescrizioneImporto> descrizioneImporto = new ArrayList<VoceDescrizioneImporto>();
				for (it.govpay.core.beans.tracciati.VoceDescrizioneImporto vdI : proprieta.getDescrizioneImporto()) {
					VoceDescrizioneImporto voce = new VoceDescrizioneImporto();
					
					voce.setVoce(vdI.getVoce());
					voce.setImporto(vdI.getImporto());
					
					descrizioneImporto.add(voce);
				}
				rsModel.setDescrizioneImporto(descrizioneImporto);
			}
			rsModel.setLineaTestoRicevuta1(proprieta.getLineaTestoRicevuta1());
			rsModel.setLineaTestoRicevuta2(proprieta.getLineaTestoRicevuta2());
			if(proprieta.getLinguaSecondaria() != null) {
				switch(proprieta.getLinguaSecondaria()) {
				case DE:
					rsModel.setLinguaSecondariaEnum(LinguaSecondaria.DE);
					break;
				case EN:
					rsModel.setLinguaSecondariaEnum(LinguaSecondaria.EN);
					break;
				case FALSE:
					rsModel.setLinguaSecondariaEnum(LinguaSecondaria.FALSE);
					break;
				case FR:
					rsModel.setLinguaSecondariaEnum(LinguaSecondaria.FR);
					break;
				case SL:
					rsModel.setLinguaSecondariaEnum(LinguaSecondaria.SL);
					break;
				}
				
				if(rsModel.getLinguaSecondariaEnum() != null)
					rsModel.setLinguaSecondaria(rsModel.getLinguaSecondariaEnum().toString());
			}
			rsModel.setLinguaSecondariaCausale(proprieta.getLinguaSecondariaCausale());
		}
		
		return rsModel;
	}
}