package it.govpay.web.rs.caricatore;

import it.govpay.bd.BasicBD;
import it.govpay.bd.pagamento.IuvBD;
import it.govpay.core.exceptions.GovPayException;
import it.govpay.core.utils.GpContext;
import it.govpay.core.utils.GpThreadLocal;
import it.govpay.core.utils.IuvUtils;
import it.govpay.model.Applicazione;
import it.govpay.model.Iuv.TipoIUV;
import it.govpay.servizi.commons.IuvGenerato;
import it.govpay.web.rs.converter.VersamentoConverter;
import it.govpay.web.rs.model.Versamento;
import it.govpay.web.rs.model.VersamentoResponse;
import it.govpay.web.rs.utils.ValidationUtils;
import it.govpay.web.rs.utils.VersamentoUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.math.BigDecimal;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriInfo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.utils.logger.beans.Property;

public class CaricatoreImpl implements ICaricatore {

	private Logger log = LogManager.getLogger();

	@Override
	public String caricaVersamento(InputStream is, UriInfo uriInfo, HttpHeaders httpHeaders, BasicBD bd, Applicazione applicazioneAutenticata) throws Exception {
		String methodName = "CaricaVersamento"; 

		it.govpay.core.business.Versamento versamentoBusiness = null;
		it.govpay.servizi.commons.Versamento versamentoCommons = null;
		it.govpay.model.Versamento versamentoModel = null;
		GpContext ctx = null; 
		VersamentoResponse versamentoResponse = new VersamentoResponse();

		Versamento request = VersamentoUtils.readVersamentoFromRequest(log, is, methodName);
		ctx =  GpThreadLocal.get();

		log.info("Validazione della entry in corso...");
		// validazione richiesta
		ValidationUtils.validaRichiestaCaricaVersamento(request);
		log.info("Validazione della entry completata.");

		versamentoBusiness = new it.govpay.core.business.Versamento(bd);

		boolean generaIuv = true;
		boolean aggiornaSeEsiste = true;

		this.log.info("Caricamento del Versamento ["+request.getIdentificativoVersamento()+"] in corso...");

		it.govpay.model.Iuv iuv = null;
		try{
			ctx.getContext().getRequest().addGenericProperty(new Property("identificativoVersamento", request.getIdentificativoVersamento())); 
			ctx.getContext().getRequest().addGenericProperty(new Property("codiceCreditore", request.getCodiceCreditore()));
			ctx.log("rest.richiestaVersamento");
			versamentoResponse.setIdOperazione(ctx.getTransactionId());
			// porto il versamento alla versione definita nei servizi.
			versamentoCommons = VersamentoConverter.toVersamentoCommons(request,applicazioneAutenticata, false, bd);
			// porto il versamento alla versione model, utilizzando tutti i controlli previsti nella versione commons.
			versamentoModel = it.govpay.core.utils.VersamentoUtils.toVersamentoModel(versamentoCommons, bd);
			iuv = versamentoBusiness.caricaVersamento(applicazioneAutenticata, versamentoModel, generaIuv , aggiornaSeEsiste );

			if(iuv != null) {
				Double importoVersamento = request.getImporto();
				BigDecimal importoVersamentoAsBigDecimal  = new BigDecimal(importoVersamento.doubleValue());
				IuvGenerato iuvGenerato = IuvUtils.toIuvGenerato(versamentoModel.getApplicazione(bd), versamentoModel.getUo(bd).getDominio(bd), iuv, importoVersamentoAsBigDecimal);
				versamentoResponse.setBarCode(new String(iuvGenerato.getBarCode()));
				versamentoResponse.setQrCode(new String(iuvGenerato.getQrCode()));
				versamentoResponse.setIuv(iuvGenerato.getIuv());
			}

			versamentoResponse.setIdOperazione(ctx.getTransactionId());
			versamentoResponse.setEsito("OK");

			ctx.getContext().getRequest().addGenericProperty(new Property("iuv", versamentoResponse.getIuv()));
			ctx.log("rest.versamentoOk");
		} catch (GovPayException e) {
			e.log(log);
			versamentoResponse.setEsito(e.getCodEsito().name() + ": " + e.getMessage());
			ctx.log("rest.versamentoKo",e.getMessage());
		} catch (Exception e) {
			GovPayException ge = new GovPayException(e);
			ge.log(log);
			versamentoResponse.setEsito(ge.getCodEsito().name() + ": " + ge.getMessage());
			ctx.log("rest.versamentoKo",ge.getMessage());
		}
		// Se ho rilevato un eccezione provo a recuperare comunque lo iuv
		if(iuv == null && versamentoModel != null) {
			IuvBD iuvBD = new IuvBD(bd);
			try {
				iuv = iuvBD.getIuv(versamentoModel.getIdApplicazione(), versamentoModel.getCodVersamentoEnte(), TipoIUV.NUMERICO);
				versamentoResponse.setIuv(iuv.getIuv()); 
			} catch (NotFoundException e) {
				versamentoResponse.setIuv(null);
			}
		}

		this.log.info("Caricamento del Versamento ["+request.getIdentificativoVersamento()+"] completato con esito ["+versamentoResponse.getEsito()+"].");
		ByteArrayOutputStream baos = VersamentoUtils.writeVersamentoResponse(log, versamentoResponse, methodName);
		return baos.toString();
	}
}
