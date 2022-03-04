package it.govpay.pagamento.v2.beans.converter;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jaxrs.RawObject;
import org.openspcoop2.utils.service.context.ContextThreadLocal;
import org.springframework.security.core.Authentication;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Versamento;
import it.govpay.core.autorizzazione.beans.GovpayLdapUserDetails;
import it.govpay.core.autorizzazione.utils.AutorizzazioneUtils;
import it.govpay.core.dao.pagamenti.dto.LeggiPendenzaDTOResponse;
import it.govpay.core.utils.UriBuilderUtils;
import it.govpay.model.Utenza.TIPO_UTENZA;
import it.govpay.pagamento.v2.beans.Avviso;
import it.govpay.pagamento.v2.beans.LinguaSecondaria;
import it.govpay.pagamento.v2.beans.PagamentoIndex;
import it.govpay.pagamento.v2.beans.Pendenza;
import it.govpay.pagamento.v2.beans.PendenzaCreata;
import it.govpay.pagamento.v2.beans.PendenzaIndex;
import it.govpay.pagamento.v2.beans.ProprietaPendenza;
import it.govpay.pagamento.v2.beans.RppIndex;
import it.govpay.pagamento.v2.beans.Segnalazione;
import it.govpay.pagamento.v2.beans.Soggetto;
import it.govpay.pagamento.v2.beans.StatoAvviso;
import it.govpay.pagamento.v2.beans.StatoPendenza;
import it.govpay.pagamento.v2.beans.StatoVocePendenza;
import it.govpay.pagamento.v2.beans.TassonomiaAvviso;
import it.govpay.pagamento.v2.beans.TipoContabilita;
import it.govpay.pagamento.v2.beans.VoceDescrizioneImporto;
import it.govpay.pagamento.v2.beans.VocePendenza;
import it.govpay.pagamento.v2.beans.VocePendenza.TipoBolloEnum;

public class PendenzeConverter {
	
	public static Pendenza toRsModel(LeggiPendenzaDTOResponse dto, Authentication user) throws ServiceException {
		return toRsModel(dto.getVersamento(), dto.getPagamenti(), dto.getRpts(),user);
	}
	
	public static Pendenza toRsModel(it.govpay.bd.model.Versamento versamento,List<PagamentoPortale> pagamenti, List<Rpt> rpts, Authentication user) throws ServiceException {
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
		
		rsModel.setDataCaricamento(versamento.getDataCreazione());
		rsModel.setDataPagamento(versamento.getDataPagamento());
		rsModel.setDataScadenza(versamento.getDataScadenza());
		rsModel.setDataValidita(versamento.getDataValidita());
		rsModel.setDominio(DominiConverter.toRsModel(versamento.getDominio(configWrapper)));
		rsModel.setIdTipoPendenza(versamento.getTipoVersamentoDominio(configWrapper).getCodTipoVersamento());
		rsModel.setIdA2A(versamento.getApplicazione(configWrapper).getCodApplicazione());
		rsModel.setIdPendenza(versamento.getCodVersamentoEnte());
		rsModel.setImporto(versamento.getImportoTotale());
		rsModel.setIuvPagamento(versamento.getIuvPagamento());
		rsModel.setIuvAvviso(versamento.getIuvVersamento());
//		rsModel.setNome(versamento.getNome());
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());
		rsModel.setSoggettoPagatore(controlloUtenzaPagatore(AnagraficaConverter.toSoggettoRsModel(versamento.getAnagraficaDebitore()),user));
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
		
		if(versamento.isAnomalo())
			statoPendenza = StatoPendenza.ANOMALA;

		rsModel.setStato(statoPendenza);
		rsModel.setTassonomia(versamento.getTassonomia());
		rsModel.setTassonomiaAvviso(TassonomiaAvviso.fromValue(versamento.getTassonomiaAvviso()));
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());
		rsModel.setDirezione(versamento.getDirezione());
		rsModel.setDivisione(versamento.getDivisione()); 

		rsModel.setSegnalazioni(unmarshall(versamento.getAnomalie()));
		
		UnitaOperativa uo = versamento.getUo(configWrapper);
		if(uo != null && !uo.getCodUo().equals(it.govpay.model.Dominio.EC))
			rsModel.setUnitaOperativa(DominiConverter.toUnitaOperativaRsModel(uo));

		List<VocePendenza> v = new ArrayList<>();
		int indice = 1;
		for(SingoloVersamento s: versamento.getSingoliVersamenti()) {
			v.add(toVocePendenzaRsModel(s, indice++, user, configWrapper));
		}
		rsModel.setVoci(v);
		
		List<PagamentoIndex> listaPagamentoIndex = new ArrayList<>();
		
		if(pagamenti != null && pagamenti.size() > 0) {
			for (PagamentoPortale pagamento : pagamenti) {
				listaPagamentoIndex.add(PagamentiPortaleConverter.toRsModelIndex(pagamento,user));
			}
		}
		rsModel.setPagamenti(listaPagamentoIndex);
		
		List<RppIndex> rpps = new ArrayList<>();
		if(rpts != null && rpts.size() > 0) {
			for (Rpt rpt : rpts) {
				rpps.add(RptConverter.toRsModelIndex(rpt, rpt.getVersamento(), rpt.getVersamento().getApplicazione(configWrapper), user));
			} 
		}
		rsModel.setRpp(rpps); 
		
		if(versamento.getTipo() != null) {
			switch (versamento.getTipo()) {
			case DOVUTO:
				rsModel.setTipo(it.govpay.pagamento.v2.beans.TipoPendenzaTipologia.DOVUTO);
				break;
			case SPONTANEO:
				rsModel.setTipo(it.govpay.pagamento.v2.beans.TipoPendenzaTipologia.SPONTANEO);
				break;
			}
		}
		
		rsModel.setUUID(versamento.getIdSessione());
		
		rsModel.setProprieta(toProprietaPendenzaRsModel(versamento.getProprietaPendenza()));
		
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
	
	public static PendenzaIndex toRsModelIndex(it.govpay.bd.model.Versamento versamento, Authentication user) throws ServiceException {
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
		rsModel.setDataPagamento(versamento.getDataPagamento());
		rsModel.setDataScadenza(versamento.getDataScadenza());
		rsModel.setDataValidita(versamento.getDataValidita());
		
		rsModel.setDominio(DominiConverter.toRsModel(versamento.getDominio(configWrapper)));
		rsModel.setIdTipoPendenza(versamento.getTipoVersamentoDominio(configWrapper).getCodTipoVersamento());
		rsModel.setIdA2A(versamento.getApplicazione(configWrapper).getCodApplicazione());
		
		rsModel.setIdPendenza(versamento.getCodVersamentoEnte());
		rsModel.setImporto(versamento.getImportoTotale());
		rsModel.setIuvPagamento(versamento.getIuvPagamento());
		rsModel.setIuvAvviso(versamento.getIuvVersamento());
//		rsModel.setNome(versamento.getNome());
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());
		rsModel.setSoggettoPagatore(controlloUtenzaPagatore(AnagraficaConverter.toSoggettoRsModel(versamento.getAnagraficaDebitore()),user));
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
		
		if(versamento.isAnomalo())
			statoPendenza = StatoPendenza.ANOMALA;

		rsModel.setStato(statoPendenza);
		rsModel.setTassonomia(versamento.getTassonomia());
		rsModel.setTassonomiaAvviso(TassonomiaAvviso.fromValue(versamento.getTassonomiaAvviso()));
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());
		rsModel.setDirezione(versamento.getDirezione());
		rsModel.setDivisione(versamento.getDivisione()); 
		
		UnitaOperativa uo = versamento.getUo(configWrapper);
		if(uo != null && !uo.getCodUo().equals(it.govpay.model.Dominio.EC))
			rsModel.setUnitaOperativa(DominiConverter.toUnitaOperativaRsModel(uo));
		
		rsModel.setPagamenti(UriBuilderUtils.getPagamentiByIdA2AIdPendenza(versamento.getApplicazione(configWrapper).getCodApplicazione(),versamento.getCodVersamentoEnte()));
		rsModel.setRpp(UriBuilderUtils.getRppsByIdA2AIdPendenza(versamento.getApplicazione(configWrapper).getCodApplicazione(),versamento.getCodVersamentoEnte()));
		
		if(versamento.getTipo() != null) {
			switch (versamento.getTipo()) {
			case DOVUTO:
				rsModel.setTipo(it.govpay.pagamento.v2.beans.TipoPendenzaTipologia.DOVUTO);
				break;
			case SPONTANEO:
				rsModel.setTipo(it.govpay.pagamento.v2.beans.TipoPendenzaTipologia.SPONTANEO);
				break;
			}
		}
		
		rsModel.setUUID(versamento.getIdSessione());
		
		rsModel.setProprieta(toProprietaPendenzaRsModel(versamento.getProprietaPendenza()));

		return rsModel;
	}
	
	public static VocePendenza toVocePendenzaRsModel(it.govpay.bd.model.SingoloVersamento singoloVersamento, int indice, Authentication user, BDConfigWrapper configWrapper ) throws ServiceException {
		VocePendenza rsModel = new VocePendenza();
		
		boolean visualizzaInfoIBAN = true;
		if(user !=null) {
			GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(user);
			
			if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO) || userDetails.getTipoUtenza().equals(TIPO_UTENZA.ANONIMO)) {
				visualizzaInfoIBAN = false;
			}
		}
		
		
		if(singoloVersamento.getDatiAllegati() != null)
			rsModel.setDatiAllegati(new RawObject(singoloVersamento.getDatiAllegati()));
		rsModel.setDescrizione(singoloVersamento.getDescrizione());
		rsModel.setDescrizioneCausaleRPT(singoloVersamento.getDescrizioneCausaleRPT());
		
		rsModel.setIdVocePendenza(singoloVersamento.getCodSingoloVersamentoEnte());
		rsModel.setImporto(singoloVersamento.getImportoSingoloVersamento());
		rsModel.setIndice(new BigDecimal(indice));
		
		switch(singoloVersamento.getStatoSingoloVersamento()) {
		case ESEGUITO:rsModel.setStato(StatoVocePendenza.ESEGUITO);
			break;
		case NON_ESEGUITO:rsModel.setStato(StatoVocePendenza.NON_ESEGUITO);
			break;
		default:
			break;}

		// Definisce i dati di un bollo telematico
		if(singoloVersamento.getHashDocumento() != null && singoloVersamento.getTipoBollo() != null && singoloVersamento.getProvinciaResidenza() != null) {
			rsModel.setHashDocumento(singoloVersamento.getHashDocumento());
			rsModel.setTipoBollo(TipoBolloEnum.fromCodifica(singoloVersamento.getTipoBollo().getCodifica()));
			rsModel.setProvinciaResidenza(singoloVersamento.getProvinciaResidenza());
		} else if(singoloVersamento.getTributo(configWrapper) != null && singoloVersamento.getTributo(configWrapper).getCodTributo() != null) { // Definisce i dettagli di incasso tramite riferimento in anagrafica GovPay.
			rsModel.setCodEntrata(singoloVersamento.getTributo(configWrapper).getCodTributo());
		} else { // Definisce i dettagli di incasso della singola entrata.
			rsModel.setCodiceContabilita(singoloVersamento.getCodContabilita());
			
			if(visualizzaInfoIBAN) {
				rsModel.setIbanAccredito(singoloVersamento.getIbanAccredito(configWrapper).getCodIban());
				if(singoloVersamento.getIbanAppoggio(configWrapper) != null)
					rsModel.setIbanAppoggio(singoloVersamento.getIbanAppoggio(configWrapper).getCodIban());
			}
			rsModel.setTipoContabilita(TipoContabilita.fromValue(singoloVersamento.getTipoContabilita().name()));
		}
		if(singoloVersamento.getDominio(configWrapper) != null) {
			rsModel.setDominio(DominiConverter.toRsModel(singoloVersamento.getDominio(configWrapper)));
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
		rsModel.setDataPagamento(versamento.getDataPagamento());
		rsModel.setDataValidita(versamento.getDataValidita());
		rsModel.setIdDominio(dominio.getCodDominio());
		rsModel.setImporto(versamento.getImportoTotale());
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());
		rsModel.setTassonomiaAvviso(TassonomiaAvviso.fromValue(versamento.getTassonomiaAvviso()));
		rsModel.setBarcode(barCode);
		rsModel.setQrcode(qrCode);
		
		StatoAvviso statoPendenza = null;

		switch(versamento.getStatoVersamento()) {
		case ANNULLATO: statoPendenza = StatoAvviso.ANNULLATA;
			break;
		case ESEGUITO: statoPendenza = StatoAvviso.DUPLICATA;
			break;
		case ESEGUITO_ALTRO_CANALE:  statoPendenza = StatoAvviso.DUPLICATA;
			break;
		case NON_ESEGUITO: if(versamento.getDataScadenza() != null && versamento.getDataScadenza().before(new Date())) {statoPendenza = StatoAvviso.SCADUTA;} else { statoPendenza = StatoAvviso.NON_ESEGUITA;}
			break;
		case PARZIALMENTE_ESEGUITO:  statoPendenza = StatoAvviso.DUPLICATA;
			break;
		default:
			break;
		
		}

		rsModel.setStato(statoPendenza);

		return rsModel;
	}
	
	public static Soggetto controlloUtenzaPagatore(Soggetto soggetto, Authentication user) {
		
		GovpayLdapUserDetails userDetails = AutorizzazioneUtils.getAuthenticationDetails(user);
		
		if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.CITTADINO)) {
//			if(soggetto == null) {
//				soggetto = new Soggetto();
//			}
//			
//			UtenzaCittadino cittadino = (UtenzaCittadino) userDetails.getUtenza();
//			soggetto.setIdentificativo(cittadino.getCodIdentificativo());
//			String nomeCognome = cittadino.getProprieta(SPIDAuthenticationDetailsSource.SPID_HEADER_NAME) + " "
//					+ cittadino.getProprieta(SPIDAuthenticationDetailsSource.SPID_HEADER_FAMILY_NAME);
//			soggetto.setAnagrafica(nomeCognome);
//			soggetto.setEmail(cittadino.getProprieta(SPIDAuthenticationDetailsSource.SPID_HEADER_EMAIL));
//			soggetto.setTipo(TipoEnum.F);
//			soggetto.setCap(null);
//			soggetto.setCellulare(null);
//			soggetto.setCivico(null);
//			soggetto.setIndirizzo(null);
//			soggetto.setLocalita(null);
//			soggetto.setNazione(null);
//			soggetto.setProvincia(null);
		}
		
		if(userDetails.getTipoUtenza().equals(TIPO_UTENZA.ANONIMO)) {
			return null;
		}
		
		return soggetto;
	}
	
	public static PendenzaCreata toRsPendenzaCreataModel(Dominio dominio, Versamento versamento, UnitaOperativa uo, String pdf, Authentication user) throws ServiceException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		PendenzaCreata rsModel = new PendenzaCreata();
		
		rsModel.pdf(pdf);
		
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
		rsModel.setDataPagamento(versamento.getDataPagamento());
		rsModel.setDataScadenza(versamento.getDataScadenza());
		rsModel.setDataValidita(versamento.getDataValidita());
		rsModel.setDominio(DominiConverter.toRsModel(versamento.getDominio(configWrapper)));
		rsModel.setIdTipoPendenza(versamento.getTipoVersamentoDominio(configWrapper).getCodTipoVersamento());
		rsModel.setIdA2A(versamento.getApplicazione(configWrapper).getCodApplicazione());
		rsModel.setIdPendenza(versamento.getCodVersamentoEnte());
		rsModel.setImporto(versamento.getImportoTotale());
//		rsModel.setNome(versamento.getNome());
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());
		rsModel.setSoggettoPagatore(controlloUtenzaPagatore(AnagraficaConverter.toSoggettoRsModel(versamento.getAnagraficaDebitore()),user));
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
		
		if(versamento.isAnomalo())
			statoPendenza = StatoPendenza.ANOMALA;

		rsModel.setStato(statoPendenza);
		rsModel.setTassonomia(versamento.getTassonomia());
		rsModel.setTassonomiaAvviso(TassonomiaAvviso.fromValue(versamento.getTassonomiaAvviso()));
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());
		rsModel.setDirezione(versamento.getDirezione());
		rsModel.setDivisione(versamento.getDivisione()); 

		rsModel.setSegnalazioni(unmarshall(versamento.getAnomalie()));
		
		if(uo != null && !uo.getCodUo().equals(it.govpay.model.Dominio.EC))
			rsModel.setUnitaOperativa(DominiConverter.toUnitaOperativaRsModel(uo));

		List<VocePendenza> v = new ArrayList<>();
		int indice = 1;
		for(SingoloVersamento s: versamento.getSingoliVersamenti()) {
			v.add(toVocePendenzaRsModel(s, indice++, user, configWrapper));
		}
		rsModel.setVoci(v);
		
		List<PagamentoIndex> listaPagamentoIndex = new ArrayList<>();
		
//		if(pagamenti != null && pagamenti.size() > 0) {
//			for (PagamentoPortale pagamento : pagamenti) {
//				listaPagamentoIndex.add(PagamentiPortaleConverter.toRsModelIndex(pagamento,user));
//			}
//		}
		rsModel.setPagamenti(listaPagamentoIndex);
		
		List<RppIndex> rpps = new ArrayList<>();
//		if(rpts != null && rpts.size() > 0) {
//			for (Rpt rpt : rpts) {
//				rpps.add(RptConverter.toRsModelIndex(rpt, rpt.getVersamento(null), rpt.getVersamento(null).getApplicazione(configWrapper), user));
//			} 
//		}
		rsModel.setRpp(rpps); 
		
		if(versamento.getTipo() != null) {
			switch (versamento.getTipo()) {
			case DOVUTO:
				rsModel.setTipo(it.govpay.pagamento.v2.beans.TipoPendenzaTipologia.DOVUTO);
				break;
			case SPONTANEO:
				rsModel.setTipo(it.govpay.pagamento.v2.beans.TipoPendenzaTipologia.SPONTANEO);
				break;
			}
		}
		
		rsModel.setUUID(versamento.getIdSessione());
		
		return rsModel;
	}
	
	public static it.govpay.core.beans.tracciati.ProprietaPendenza toProprietaPendenzaDTO(ProprietaPendenza proprieta) {
		it.govpay.core.beans.tracciati.ProprietaPendenza dto = null;
		if(proprieta != null) {
			dto = new it.govpay.core.beans.tracciati.ProprietaPendenza();
			
			if(proprieta.getDescrizioneImporto() != null && !proprieta.getDescrizioneImporto().isEmpty()) {
				List<it.govpay.core.beans.tracciati.VoceDescrizioneImporto> descrizioneImporto = new ArrayList<it.govpay.core.beans.tracciati.VoceDescrizioneImporto>();
				for (VoceDescrizioneImporto vdI : proprieta.getDescrizioneImporto()) {
					it.govpay.core.beans.tracciati.VoceDescrizioneImporto voce = new it.govpay.core.beans.tracciati.VoceDescrizioneImporto();
					
					voce.setVoce(vdI.getVoce());
					voce.setImporto(vdI.getImporto());
					
					descrizioneImporto.add(voce);
				}
				dto.setDescrizioneImporto(descrizioneImporto);
			}
			dto.setLineaTestoRicevuta1(proprieta.getLineaTestoRicevuta1());
			dto.setLineaTestoRicevuta2(proprieta.getLineaTestoRicevuta2());
			if(proprieta.getLinguaSecondaria() != null) {
				switch(LinguaSecondaria.fromValue(proprieta.getLinguaSecondaria())) {
				case DE:
					dto.setLinguaSecondaria(it.govpay.core.beans.tracciati.LinguaSecondaria.DE);
					break;
				case EN:
					dto.setLinguaSecondaria(it.govpay.core.beans.tracciati.LinguaSecondaria.EN);
					break;
				case FALSE:
					dto.setLinguaSecondaria(it.govpay.core.beans.tracciati.LinguaSecondaria.FALSE);
					break;
				case FR:
					dto.setLinguaSecondaria(it.govpay.core.beans.tracciati.LinguaSecondaria.FR);
					break;
				case SL:
					dto.setLinguaSecondaria(it.govpay.core.beans.tracciati.LinguaSecondaria.SL);
					break;
				}				
			}
			dto.setLinguaSecondariaCausale(proprieta.getLinguaSecondariaCausale());
		}
		
		return dto;
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
