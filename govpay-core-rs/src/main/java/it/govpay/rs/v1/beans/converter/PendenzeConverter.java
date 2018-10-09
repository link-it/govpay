package it.govpay.rs.v1.beans.converter;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Pagamento;
import it.govpay.bd.model.Rendicontazione;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.core.rs.v1.beans.base.Avviso;
import it.govpay.core.rs.v1.beans.base.Avviso.StatoEnum;
import it.govpay.core.rs.v1.beans.base.Nota;
import it.govpay.core.rs.v1.beans.base.Pendenza;
import it.govpay.core.rs.v1.beans.base.PendenzaIndex;
import it.govpay.core.rs.v1.beans.base.Riscossione;
import it.govpay.core.rs.v1.beans.base.Segnalazione;
import it.govpay.core.rs.v1.beans.base.StatoPendenza;
import it.govpay.core.rs.v1.beans.base.TassonomiaAvviso;
import it.govpay.core.rs.v1.beans.base.TipoContabilita;
import it.govpay.core.rs.v1.beans.base.VocePendenza;
import it.govpay.core.utils.UriBuilderUtils;

public class PendenzeConverter {
	
	public static Pendenza toRsModelConInfoIncasso(it.govpay.bd.viste.model.VersamentoIncasso versamento, it.govpay.bd.model.UnitaOperativa unitaOperativa, it.govpay.bd.model.Applicazione applicazione, 
			it.govpay.bd.model.Dominio dominio, List<SingoloVersamento> singoliVersamenti) throws ServiceException {
		Pendenza rsModel = toRsModel(versamento, unitaOperativa, applicazione, dominio, singoliVersamenti,true);
		
		StatoPendenza statoPendenza = null;

		switch(versamento.getStatoVersamento()) {
		case ANNULLATO: statoPendenza = StatoPendenza.ANNULLATA;
			break;
		case ESEGUITO: 
		case ESEGUITO_ALTRO_CANALE:  
			statoPendenza = StatoPendenza.ESEGUITA;
			if(versamento.getStatoPagamento() != null) {
				switch (versamento.getStatoPagamento()) {
				case INCASSATO:
					statoPendenza = StatoPendenza.INCASSATA;
					break;
				case NON_PAGATO:
				case PAGATO:
				default:
					break;
				}
			}
			break;
		case NON_ESEGUITO: if(versamento.getDataScadenza() != null && versamento.getDataScadenza().before(new Date())) {statoPendenza = StatoPendenza.SCADUTA;} else { statoPendenza = StatoPendenza.NON_ESEGUITA;}
			break;
		case PARZIALMENTE_ESEGUITO:  statoPendenza = StatoPendenza.ESEGUITA_PARZIALE;
			break;
		default:
			break;
		
		}

		rsModel.setStato(statoPendenza);
		rsModel.setDataPagamento(versamento.getDataPagamento());
		rsModel.setImportoIncassato(versamento.getImportoIncassato());
		rsModel.setImportoPagato(versamento.getImportoPagato()); 
		
		return rsModel;
	}
	
	public static Pendenza toRsModel(it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.UnitaOperativa unitaOperativa, it.govpay.bd.model.Applicazione applicazione, 
			it.govpay.bd.model.Dominio dominio, List<SingoloVersamento> singoliVersamenti) throws ServiceException {
		return toRsModel(versamento, unitaOperativa, applicazione, dominio, singoliVersamenti, false);
	}
	
	public static Pendenza toRsModel(it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.UnitaOperativa unitaOperativa, it.govpay.bd.model.Applicazione applicazione, 
			it.govpay.bd.model.Dominio dominio, List<SingoloVersamento> singoliVersamenti, boolean addInfoIncasso) throws ServiceException {
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
		rsModel.setDataUltimoAggiornamento(versamento.getDataUltimoAggiornamento());
		rsModel.setDominio(DominiConverter.toRsModelIndex(dominio));
		
		rsModel.setIdA2A(applicazione.getCodApplicazione());
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
		rsModel.setDescrizioneStato(versamento.getDescrizioneStato());
		rsModel.setTassonomia(versamento.getTassonomia());
		rsModel.setTassonomiaAvviso(TassonomiaAvviso.fromValue(versamento.getTassonomiaAvviso()));
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());

		rsModel.setSegnalazioni(unmarshall(versamento.getAnomalie()));
		
		if(unitaOperativa != null && !unitaOperativa.getCodUo().equals(it.govpay.model.Dominio.EC))
			rsModel.setUnitaOperativa(DominiConverter.toUnitaOperativaRsModel(unitaOperativa));

		List<VocePendenza> v = new ArrayList<>();
		for(SingoloVersamento s: singoliVersamenti) {
			v.add(toVocePendenzaRsModel(s,addInfoIncasso));
		}
		rsModel.setVoci(v);
		
		if(versamento.getNote()!=null && !versamento.getNote().isEmpty()) {
			List<Nota> note = new ArrayList<>();
			for(it.govpay.bd.model.Nota nota: versamento.getNote()) {
				note.add(NoteConverter.toRsModel(nota));
			}
			rsModel.setNote(note);
		}
		
		rsModel.setVerificato(versamento.isAck());
		rsModel.setAnomalo(versamento.isAnomalo());

		return rsModel;
	}
	
	private static List<Segnalazione> unmarshall(String anomalie) {
		List<Segnalazione> list = new ArrayList<>();
		
		if(anomalie == null || anomalie.isEmpty()) return list;
		
		String[] split = anomalie.split("\\|");
		for(String s : split){
			String[] split2 = s.split("#");
			Segnalazione a = new Segnalazione();
			a.setCodice(split2[0]);;
			a.setDescrizione(split2[1]);
			list.add(a);
		}
		return list;
	}

	public static PendenzaIndex toRsModelIndexConInfoIncasso(it.govpay.bd.viste.model.VersamentoIncasso versamento) throws ServiceException {
		PendenzaIndex pIndex = toRsModelIndex(versamento);
		
		StatoPendenza statoPendenza = null;

		switch(versamento.getStatoVersamento()) {
		case ANNULLATO: statoPendenza = StatoPendenza.ANNULLATA;
			break;
		case ESEGUITO: 
		case ESEGUITO_ALTRO_CANALE:  
			statoPendenza = StatoPendenza.ESEGUITA;
			if(versamento.getStatoPagamento() != null) {
				switch (versamento.getStatoPagamento()) {
				case INCASSATO:
					statoPendenza = StatoPendenza.INCASSATA;
					break;
				case NON_PAGATO:
				case PAGATO:
				default:
					break;
				}
			}
			break;
		case NON_ESEGUITO: if(versamento.getDataScadenza() != null && versamento.getDataScadenza().before(new Date())) {statoPendenza = StatoPendenza.SCADUTA;} else { statoPendenza = StatoPendenza.NON_ESEGUITA;}
			break;
		case PARZIALMENTE_ESEGUITO:  statoPendenza = StatoPendenza.ESEGUITA_PARZIALE;
			break;
		default:
			break;
		
		}

		pIndex.setStato(statoPendenza);
		
		return pIndex;
	}

	public static PendenzaIndex toRsModelIndex(it.govpay.bd.model.Versamento versamento) throws ServiceException {
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
		rsModel.setDominio(DominiConverter.toRsModelIndex(versamento.getDominio(null)));
		
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
		
		if(versamento.getUo(null) != null && !versamento.getUo(null).getCodUo().equals(it.govpay.model.Dominio.EC))
			rsModel.setUnitaOperativa(DominiConverter.toUnitaOperativaRsModel(versamento.getUo(null)));
		
		rsModel.setPagamenti(UriBuilderUtils.getPagamentiByIdA2AIdPendenza(versamento.getApplicazione(null).getCodApplicazione(),versamento.getCodVersamentoEnte()));
		rsModel.setRpp(UriBuilderUtils.getRppsByIdA2AIdPendenza(versamento.getApplicazione(null).getCodApplicazione(),versamento.getCodVersamentoEnte()));

		
		if(versamento.getNote()!=null && !versamento.getNote().isEmpty()) {
			List<Nota> note = new ArrayList<>();
			for(it.govpay.bd.model.Nota nota: versamento.getNote()) {
				note.add(NoteConverter.toRsModel(nota));
			}
			rsModel.setNote(note);
		}
		
		rsModel.setVerificato(versamento.isAck());
		rsModel.setAnomalo(versamento.isAnomalo());
		
		return rsModel;
	}
	
	public static VocePendenza toVocePendenzaRsModel(it.govpay.bd.model.SingoloVersamento singoloVersamento, boolean addInfoIncasso) throws ServiceException {
		VocePendenza rsModel = new VocePendenza();
		rsModel.setDatiAllegati(singoloVersamento.getDatiAllegati());
		rsModel.setDescrizione(singoloVersamento.getDescrizione());
		
		rsModel.setIdVocePendenza(singoloVersamento.getCodSingoloVersamentoEnte());
		rsModel.setImporto(singoloVersamento.getImportoSingoloVersamento());
		rsModel.setIndice(BigDecimal.valueOf(singoloVersamento.getIndiceDati().longValue())); 
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
		} else if(singoloVersamento.getTributo(null) != null && singoloVersamento.getTributo(null).getCodTributo() != null) { // Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.
			rsModel.setCodEntrata(singoloVersamento.getTributo(null).getCodTributo());
		} else { // Definisce i dettagli di incasso della singola entrata.
			rsModel.setCodiceContabilita(singoloVersamento.getCodContabilita());
			rsModel.setIbanAccredito(singoloVersamento.getIbanAccredito(null).getCodIban());
			if(singoloVersamento.getTipoContabilita() != null)
				rsModel.setTipoContabilita(TipoContabilita.valueOf(singoloVersamento.getTipoContabilita().name()));
		}
		
		if(addInfoIncasso) {
			List<Rendicontazione> rendicontazioni = singoloVersamento.getRendicontazioni(null);

			
			if(rendicontazioni != null && !rendicontazioni.isEmpty()) {
				List<it.govpay.core.rs.v1.beans.base.Rendicontazione> rendicontazioniRsModel = new ArrayList<>();
				for (Rendicontazione rendicontazione : rendicontazioni) {
					it.govpay.core.rs.v1.beans.base.Rendicontazione rendicontazioneRsModel = FlussiRendicontazioneConverter.toRendicontazioneRsModel(rendicontazione);
					rendicontazioniRsModel.add(rendicontazioneRsModel);
				}
				rsModel.setRendicontazioni(rendicontazioniRsModel);
			}
			
			List<Pagamento> riscossioni = singoloVersamento.getPagamenti(null);
			
			if(riscossioni != null && !riscossioni.isEmpty()) {
				List<it.govpay.core.rs.v1.beans.base.Riscossione> riscossioniRsModel = new ArrayList<>();
				for (Pagamento pagamento : riscossioni) {
					Riscossione riscossioneRsModel = RiscossioniConverter.toRsModel(pagamento);
					riscossioniRsModel.add(riscossioneRsModel);
				}
				rsModel.setRiscossioni(riscossioniRsModel);
			}
		}
		
		
		return rsModel;
	}
	
	
	public static Avviso toAvvisoRsModel(it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.Dominio dominio, String barCode, String qrCode) throws ServiceException {
		Avviso rsModel = new Avviso();
		
		if(versamento.getCausaleVersamento()!= null)
			try {
				rsModel.setDescrizione(versamento.getCausaleVersamento().getSimple());
			} catch (UnsupportedEncodingException e) {
				throw new ServiceException(e);
			}
		
		rsModel.setDataScadenza(versamento.getDataScadenza());
		rsModel.setDataValidita(versamento.getDataValidita());
		rsModel.setIdDominio(dominio.getCodDominio());
		rsModel.setImporto(versamento.getImportoTotale());
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());
		rsModel.setTassonomiaAvviso(versamento.getTassonomiaAvviso());
		rsModel.setBarcode(barCode);
		rsModel.setQrcode(qrCode);
		
		StatoEnum statoPendenza = null;

		switch(versamento.getStatoVersamento()) {
		case ANNULLATO: statoPendenza = StatoEnum.ANNULLATO;
			break;
		case ESEGUITO: statoPendenza = StatoEnum.PAGATO;
			break;
		case ESEGUITO_ALTRO_CANALE:  statoPendenza = StatoEnum.PAGATO;
			break;
		case NON_ESEGUITO: if(versamento.getDataScadenza() != null && versamento.getDataScadenza().before(new Date())) {statoPendenza = StatoEnum.SCADUTO;} else { statoPendenza = StatoEnum.NON_PAGATO;}
			break;
		case PARZIALMENTE_ESEGUITO:  statoPendenza = StatoEnum.PAGATO;
			break;
		default:
			break;
		
		}

		rsModel.setStato(statoPendenza);

		return rsModel;
	}
}
