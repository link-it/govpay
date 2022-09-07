package it.govpay.ragioneria.v2.beans.converter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.jaxrs.RawObject;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.service.context.ContextThreadLocal;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.model.Allegato;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.ragioneria.v2.Allegati;
import it.govpay.ragioneria.v2.beans.AllegatoPendenza;
import it.govpay.ragioneria.v2.beans.Pendenza;
import it.govpay.ragioneria.v2.beans.TassonomiaAvviso;
import it.govpay.ragioneria.v2.beans.VocePendenza;

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

		rsModel.setDataCaricamento(versamento.getDataCreazione());
		rsModel.setDataScadenza(versamento.getDataScadenza());
		rsModel.setDataValidita(versamento.getDataValidita());
		rsModel.setIdDominio(versamento.getDominio(configWrapper).getCodDominio());
		rsModel.setIdA2A(versamento.getApplicazione(configWrapper).getCodApplicazione());
		rsModel.setIdPendenza(versamento.getCodVersamentoEnte());
		if(versamento.getDatiAllegati() != null)
			rsModel.setDatiAllegati(new RawObject(versamento.getDatiAllegati()));

		rsModel.setTassonomiaAvviso(TassonomiaAvviso.fromValue(versamento.getTassonomiaAvviso()));

		UnitaOperativa uo = versamento.getUo(configWrapper);
		if(uo != null && !uo.getCodUo().equals(it.govpay.model.Dominio.EC))
			rsModel.setIdUnitaOperativa(uo.getCodUo());
		
		rsModel.setIdTipoPendenza(versamento.getTipoVersamento(configWrapper).getCodTipoVersamento());
		rsModel.setDirezione(versamento.getDirezione());
		rsModel.setDivisione(versamento.getDivisione());
		rsModel.setTassonomia(versamento.getTassonomia()); 
		rsModel.setUUID(versamento.getIdSessione());
		rsModel.setAllegati(toAllegatiRsModel(versamento.getAllegati()));
		
		return rsModel;
	}

	public static VocePendenza toRsModelVocePendenza(SingoloVersamento singoloVersamento, int indice) throws ServiceException, IOException, ValidationException {
		BDConfigWrapper configWrapper = new BDConfigWrapper(ContextThreadLocal.get().getTransactionId(), true);
		VocePendenza rsModel = new VocePendenza();
		
		if(singoloVersamento.getDatiAllegati() != null)
			rsModel.setDatiAllegati(new RawObject(singoloVersamento.getDatiAllegati()));
		rsModel.setDescrizione(singoloVersamento.getDescrizione());
		rsModel.setDescrizioneCausaleRPT(singoloVersamento.getDescrizioneCausaleRPT());

		rsModel.setIdVocePendenza(singoloVersamento.getCodSingoloVersamentoEnte());
		rsModel.setImporto(singoloVersamento.getImportoSingoloVersamento());
		rsModel.setIndice(new BigDecimal(indice));
		rsModel.setPendenza(toRsModel(singoloVersamento.getVersamentoBD(null)));
		rsModel.setContabilita(ContabilitaConverter.toRsModel(singoloVersamento.getContabilita()));
		if(singoloVersamento.getDominio(configWrapper) != null) {
			rsModel.setDominio(DominiConverter.toRsModelIndex(singoloVersamento.getDominio(configWrapper)));
		}
		return rsModel;
	}

	private static List<AllegatoPendenza> toAllegatiRsModel(List<Allegato> allegati) { 
		List<AllegatoPendenza> rsModel = null;
		
		if(allegati != null && allegati.size() > 0) {
			rsModel = new ArrayList<>();
			
			for (Allegato allegato : allegati) {
				AllegatoPendenza allegatoRsModel = new AllegatoPendenza();
				
				allegatoRsModel.setNome(allegato.getNome());
				allegatoRsModel.setTipo(allegato.getTipo());
				allegatoRsModel.setDescrizione(allegato.getDescrizione());
				allegatoRsModel.setContenuto(MessageFormat.format(Allegati.DETTAGLIO_PATH_PATTERN, allegato.getId()));
				
				rsModel.add(allegatoRsModel);
			}
		}
		
		return rsModel;
	}
}
