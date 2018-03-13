package it.govpay.rs.v1.beans.converter;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.SingoloVersamento;
import it.govpay.core.rs.v1.beans.Pendenza;
import it.govpay.core.rs.v1.beans.base.VocePendenza;
import it.govpay.core.rs.v1.beans.base.StatoPendenza;
import it.govpay.core.rs.v1.beans.base.TassonomiaAvviso;
import it.govpay.core.utils.UriBuilderUtils;

public class PendenzeConverter {

	public static Pendenza toRsModel(it.govpay.bd.model.Versamento versamento, it.govpay.bd.model.UnitaOperativa unitaOperativa, it.govpay.bd.model.Applicazione applicazione, it.govpay.bd.model.Dominio dominio, List<SingoloVersamento> singoliVersamenti) throws ServiceException {
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
		rsModel.setDominio(UriBuilderUtils.getDominio(dominio.getCodDominio()));
		rsModel.setIdA2A(applicazione.getCodApplicazione());
		rsModel.setIdPendenza(versamento.getCodVersamentoEnte());
		rsModel.setImporto(versamento.getImportoTotale());
		rsModel.setNome(versamento.getNome());
		rsModel.setNumeroAvviso(versamento.getIuvProposto());
		rsModel.setSoggettoPagatore(AnagraficaConverter.toSoggettoRsModel(versamento.getAnagraficaDebitore()));
		
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
		if(unitaOperativa != null)
			rsModel.setUnitaOperativa(UriBuilderUtils.getUoByDominio(dominio.getCodDominio(), unitaOperativa.getCodUo()));
		
		rsModel.setPagamenti(UriBuilderUtils.getPagamentiByPendenza(versamento.getCodVersamentoEnte()));
		rsModel.setRpp(UriBuilderUtils.getRptsByPendenza(versamento.getCodVersamentoEnte()));
		
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
		rsModel.setDatiAllegati(singoloVersamento.getNote());
//		rsModel.setDescrizione(singoloVersamento.getDescrizione()); //aggiungere
		rsModel.setIdVocePendenza(singoloVersamento.getCodSingoloVersamentoEnte());
		rsModel.setImporto(singoloVersamento.getImportoSingoloVersamento());
		rsModel.setIndice(new BigDecimal(indice));
		
		return rsModel;
	}
}
