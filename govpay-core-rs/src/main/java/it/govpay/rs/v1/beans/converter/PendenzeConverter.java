package it.govpay.rs.v1.beans.converter;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.core.rs.v1.beans.Pendenza;
import it.govpay.core.rs.v1.beans.base.VocePendenza;
import it.govpay.core.rs.v1.beans.Avviso;
import it.govpay.core.rs.v1.beans.base.Avviso.StatoEnum;
import it.govpay.core.rs.v1.beans.base.StatoPendenza;
import it.govpay.core.rs.v1.beans.base.TassonomiaAvviso;
import it.govpay.core.utils.UriBuilderUtils;

public class PendenzeConverter {
	
	public static Pendenza toRsModel(it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.UnitaOperativa unitaOperativa, it.govpay.bd.model.Applicazione applicazione, 
			it.govpay.bd.model.Dominio dominio, List<SingoloVersamento> singoliVersamenti) throws ServiceException {
		return toRsModel(versamento, unitaOperativa, applicazione, dominio, singoliVersamenti, false);
	}

	public static Pendenza toRsModel(it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.UnitaOperativa unitaOperativa, it.govpay.bd.model.Applicazione applicazione, 
				it.govpay.bd.model.Dominio dominio, List<SingoloVersamento> singoliVersamenti,boolean esplodiDominio) throws ServiceException {
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
		if(!esplodiDominio)
			rsModel.setDominio(UriBuilderUtils.getDominio(dominio.getCodDominio()));
		else 
			rsModel.setDominio(DominiConverter.toRsModel(dominio));
		
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
		
//		rsModel.setIncasso(versamento.getIncasso()); //TODO
//		rsModel.setAnomalie(versamento.getAnomalie()); 
		
		if(unitaOperativa != null && !unitaOperativa.getCodUo().equals(Dominio.EC))
			rsModel.setUnitaOperativa(UriBuilderUtils.getUoByDominio(dominio.getCodDominio(), unitaOperativa.getCodUo()));
		
		rsModel.setPagamenti(UriBuilderUtils.getPagamentiByIdA2AIdPendenza(applicazione.getCodApplicazione(),versamento.getCodVersamentoEnte()));
		rsModel.setRpp(UriBuilderUtils.getRppsByIdA2AIdPendenza(applicazione.getCodApplicazione(),versamento.getCodVersamentoEnte()));
		
		List<VocePendenza> v = new ArrayList<VocePendenza>();
		int indice = 1;
		for(SingoloVersamento s: singoliVersamenti) {
			v.add(toVocePendenzaRsModel(s, indice++));
		}
		rsModel.setVoci(v);

		return rsModel;
	}
	
	public static VocePendenza toVocePendenzaRsModel(it.govpay.bd.model.SingoloVersamento singoloVersamento, int indice) throws ServiceException {
		VocePendenza rsModel = new VocePendenza();
		rsModel.setDatiAllegati(singoloVersamento.getDatiAllegati());
		rsModel.setDescrizione(singoloVersamento.getDescrizione());
		
		rsModel.setIdVocePendenza(singoloVersamento.getCodSingoloVersamentoEnte());
		rsModel.setImporto(singoloVersamento.getImportoSingoloVersamento());
		rsModel.setIndice(new BigDecimal(indice));
		
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
				rsModel.setTipoContabilita(singoloVersamento.getTipoContabilita().getCodifica());
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
		case ANOMALO: statoPendenza = StatoEnum.NON_PAGATO;
			break;
		case ESEGUITO: statoPendenza = StatoEnum.PAGATO;
			break;
		case ESEGUITO_SENZA_RPT:  statoPendenza = StatoEnum.PAGATO;
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
