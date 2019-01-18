package it.govpay.pagamento.v2.beans.converter;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import org.joda.time.LocalDate;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jaxrs.RawObject;

import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.pagamento.v2.api.impl.PagamentiApiServiceImpl;
import it.govpay.pagamento.v2.api.impl.PendenzeApiServiceImpl;
import it.govpay.pagamento.v2.api.impl.TransazioniApiServiceImpl;
import it.govpay.pagamento.v2.beans.Avviso;
import it.govpay.pagamento.v2.beans.Pendenza;
import it.govpay.pagamento.v2.beans.PendenzaBase;
import it.govpay.pagamento.v2.beans.PendenzaIndex;
import it.govpay.pagamento.v2.beans.Pendenze;
import it.govpay.pagamento.v2.beans.Segnalazione;
import it.govpay.pagamento.v2.beans.StatoAvviso;
import it.govpay.pagamento.v2.beans.StatoPendenza;
import it.govpay.pagamento.v2.beans.StatoVocePendenza;
import it.govpay.pagamento.v2.beans.TassonomiaAvviso;
import it.govpay.pagamento.v2.beans.TipoContabilita;
import it.govpay.pagamento.v2.beans.TipoRiferimentoVocePendenza;
import it.govpay.pagamento.v2.beans.VocePendenza;

public class PendenzeConverter {
	
	public static Pendenze toRsModel(List<it.govpay.bd.viste.model.VersamentoIncasso> versamenti, UriBuilder requestURI, int offset, int limit, long total) throws ServiceException {
		Pendenze pendenze = new Pendenze();
		
		ConverterUtils.popolaLista(pendenze, requestURI, versamenti.size(), offset, limit, total);
		
		List<PendenzaIndex> items = new ArrayList<PendenzaIndex>();
		for(it.govpay.bd.viste.model.VersamentoIncasso v : versamenti) {
			items.add(PendenzeConverter.toPendenzaIndex(v));
		}
		pendenze.setItems(items);
		return pendenze;
	}
	
	private static PendenzaBase fillPendenzaBase(PendenzaBase rsModel, it.govpay.bd.viste.model.VersamentoIncasso versamento) throws ServiceException {
		if(versamento.getCodAnnoTributario()!= null)
			rsModel.setAnnoRiferimento(new BigDecimal(versamento.getCodAnnoTributario()));
		
		rsModel.setCartellaPagamento(versamento.getCodLotto());
		rsModel.setDataCaricamento(new LocalDate(versamento.getDataCreazione()));
		if(versamento.getDataPagamento() != null)
			rsModel.setDataPagamento(new LocalDate(versamento.getDataPagamento()));
		if(versamento.getDataScadenza() != null)
			rsModel.setDataScadenza(new LocalDate(versamento.getDataScadenza()));
		if(versamento.getDataValidita() != null)
			rsModel.setDataValidita(new LocalDate(versamento.getDataValidita()));
		rsModel.setDatiAllegati(new RawObject(versamento.getDatiAllegati()));
		if(versamento.getCausaleVersamento() != null)
			try {
				rsModel.setDescrizione(versamento.getCausaleVersamento().getSimple());
			} catch (UnsupportedEncodingException e) {
				throw new ServiceException(e);
			}
		rsModel.setDominio(DominiConverter.toRsModel(versamento.getDominio(null)));
		rsModel.setIdA2A(versamento.getApplicazione(null).getCodApplicazione());
		rsModel.setIdPendenza(versamento.getCodVersamentoEnte());
		rsModel.setImporto(versamento.getImportoTotale());
		rsModel.setIuvAvviso(versamento.getIuvVersamento());
		rsModel.setIuvPagamento(versamento.getIuvPagamento());
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());
		rsModel.setSegnalazioni(unmarshall(versamento.getAnomalie()));
		rsModel.setSoggettoPagatore(AnagraficaConverter.toSoggettoRsModel(versamento.getAnagraficaDebitore()));
		
		StatoPendenza statoPendenza = null;

		switch(versamento.getStatoVersamento()) {
		case ANNULLATO: statoPendenza = StatoPendenza.ANNULLATA;
			break;
		case ESEGUITO: statoPendenza = StatoPendenza.PAGATA;
			break;
		case ESEGUITO_ALTRO_CANALE:  statoPendenza = StatoPendenza.PAGATA;
			break;
		case NON_ESEGUITO: if(versamento.getDataScadenza() != null && versamento.getDataScadenza().before(new Date())) {statoPendenza = StatoPendenza.SCADUTA;} else { statoPendenza = StatoPendenza.NON_PAGATA;}
			break;
		case PARZIALMENTE_ESEGUITO:  statoPendenza = StatoPendenza.PARZIALMENTE_PAGATA;
			break;
		default:
			break;
		}

		rsModel.setStato(statoPendenza);
		if(versamento.getTassonomiaAvviso() != null)
			rsModel.setTassonomiaAvviso(TassonomiaAvviso.valueOf(versamento.getTassonomiaAvviso()));
		if(versamento.getUo(null) != null)
			rsModel.setUnitaOperativa(DominiConverter.toUnitaOperativaRsModel(versamento.getUo(null)));
		
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
	
	public static PendenzaIndex toPendenzaIndex(it.govpay.bd.viste.model.VersamentoIncasso versamento) throws ServiceException {
		PendenzaIndex rsModel = new PendenzaIndex();
		
		fillPendenzaBase(rsModel, versamento);
		String idA2A = versamento.getApplicazione(null).getCodApplicazione();
		String idPendenza = versamento.getCodVersamentoEnte();
		rsModel.setHref(PendenzeApiServiceImpl.basePath.path(idA2A).path(idPendenza).build().toString());
		rsModel.setPagamenti(PagamentiApiServiceImpl.basePath.queryParam("idA2A", idA2A).queryParam("idPendenza", idPendenza).build().toString());
		rsModel.setRpps(TransazioniApiServiceImpl.basePath.queryParam("idA2A", idA2A).queryParam("idPendenza", idPendenza).build().toString());

		return rsModel;
	}
	
	public static Pendenza toPendenza(it.govpay.bd.viste.model.VersamentoIncasso versamento, List<PagamentoPortale> pagamenti, List<Rpt> transazioni) throws ServiceException {
		Pendenza rsModel = new Pendenza();
		
		fillPendenzaBase(rsModel, versamento);
		
		List<VocePendenza> v = new ArrayList<>();
		int indice = 1;
		for(SingoloVersamento s: versamento.getSingoliVersamenti(null)) {
			v.add(toVocePendenza(s, indice++));
		}
		rsModel.setVoci(v);
		
		rsModel.setPagamenti(null); //TODO
		rsModel.setRpps(null); //TODO
		return rsModel;
	}
	
	public static VocePendenza toVocePendenza(it.govpay.bd.model.SingoloVersamento singoloVersamento, int indice) throws ServiceException {
		VocePendenza rsModel = new VocePendenza();
		
		rsModel.setDatiAllegati(new RawObject(singoloVersamento.getDatiAllegati()));
		rsModel.setDescrizione(singoloVersamento.getDescrizione());
		rsModel.setIdVocePendenza(singoloVersamento.getCodSingoloVersamentoEnte());
		rsModel.setImporto(singoloVersamento.getImportoSingoloVersamento());
		rsModel.setIndice(new BigDecimal(indice));
		rsModel.setStato(StatoVocePendenza.fromValue(singoloVersamento.getStatoSingoloVersamento().name()));
		
		if(singoloVersamento.getHashDocumento() != null && singoloVersamento.getTipoBollo() != null && singoloVersamento.getProvinciaResidenza() != null) {
			rsModel.setHashDocumento(singoloVersamento.getHashDocumento());
			rsModel.setTipoBollo(TipoRiferimentoVocePendenza.TipoBolloEnum.BOLLO);
			rsModel.setProvinciaResidenza(singoloVersamento.getProvinciaResidenza());
		} else if(singoloVersamento.getTributo(null) != null && singoloVersamento.getTributo(null).getCodTributo() != null) { 
			rsModel.setCodEntrata(singoloVersamento.getTributo(null).getCodTributo());
		} else { // Definisce i dettagli di incasso della singola entrata.
			rsModel.setCodiceContabilita(singoloVersamento.getCodContabilita());
			rsModel.setIbanAccredito(singoloVersamento.getIbanAccredito(null).getCodIban());
			if(singoloVersamento.getIbanAppoggio(null) != null)
				rsModel.setIbanAppoggio(singoloVersamento.getIbanAppoggio(null).getCodIban());
			rsModel.setTipoContabilita(TipoContabilita.fromValue(singoloVersamento.getTipoContabilita().name()));
		}
		
		
		return rsModel;
	}
	
	
	public static Avviso toAvviso(it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.Dominio dominio, String barCode, String qrCode) throws ServiceException {
		Avviso rsModel = new Avviso();
		
		if(versamento.getCausaleVersamento()!= null)
			try {
				rsModel.setDescrizione(versamento.getCausaleVersamento().getSimple());
			} catch (UnsupportedEncodingException e) {
				throw new ServiceException(e);
			}
		
		if(versamento.getDataScadenza() != null)
			rsModel.setDataScadenza(new LocalDate(versamento.getDataScadenza()));
		if(versamento.getDataValidita() != null)
			rsModel.setDataValidita(new LocalDate(versamento.getDataValidita()));
		rsModel.setDominio(DominiConverter.toRsModel(dominio));
		rsModel.setImporto(versamento.getImportoTotale());
		rsModel.setNumeroAvviso(versamento.getNumeroAvviso());
		rsModel.setTassonomia(TassonomiaAvviso.fromValue(versamento.getTassonomiaAvviso()));
//		rsModel.setBarcode(barCode);
//		rsModel.setQrcode(qrCode);
		
		StatoAvviso statoPendenza = null;

		switch(versamento.getStatoVersamento()) {
		case ANNULLATO: statoPendenza = StatoAvviso.ANNULLATO;
			break;
		case ESEGUITO: statoPendenza = StatoAvviso.PAGATO;
			break;
		case ESEGUITO_ALTRO_CANALE:  statoPendenza = StatoAvviso.PAGATO;
			break;
		case NON_ESEGUITO: if(versamento.getDataScadenza() != null && versamento.getDataScadenza().before(new Date())) {statoPendenza = StatoAvviso.SCADUTO;} else { statoPendenza = StatoAvviso.NON_PAGATO;}
			break;
		case PARZIALMENTE_ESEGUITO:  statoPendenza = StatoAvviso.PAGATO;
			break;
		default:
			break;
		}

		rsModel.setStato(statoPendenza);

		return rsModel;
	}
}
