package it.govpay.core.utils.rawutils;

import java.util.Arrays;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.serialization.IDeserializer;
import org.openspcoop2.utils.serialization.ISerializer;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.MessaggiPagoPAUtils;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.bd.model.Rpt;

public class ConverterUtils {

	private static ObjectMapper mapper;
	static {
		mapper = new ObjectMapper();
		mapper.registerModule(new JaxbAnnotationModule());
		mapper.registerModule(new DateModule());
		mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.setDateFormat(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
	}

	public static String getRptJson(Rpt rpt) throws ServiceException {
		return getRptJson(rpt, false);
	}

	public static String getRptJson(Rpt rpt, boolean convertiMessaggioPagoPAV2InPagoPAV1) throws ServiceException {
		if(rpt.getXmlRpt() == null)
			return null;

		try {
			switch (rpt.getVersione()) {
			case SANP_230:
				CtRichiestaPagamentoTelematico ctRpt = JaxbUtils.toRPT(rpt.getXmlRpt(), false);
				return toJSON(ctRpt);
			case SANP_240:
				PaGetPaymentRes paGetPaymentRes_RPT = JaxbUtils.toPaGetPaymentRes_RPT(rpt.getXmlRpt(), false);
				
				if(convertiMessaggioPagoPAV2InPagoPAV1) {
					CtRichiestaPagamentoTelematico ctRpt2 = MessaggiPagoPAUtils.toCtRichiestaPagamentoTelematico(paGetPaymentRes_RPT, rpt);
					return toJSON(ctRpt2);
				}
				
				return toJSON(paGetPaymentRes_RPT.getData());
			}
			
			CtRichiestaPagamentoTelematico ctRpt = JaxbUtils.toRPT(rpt.getXmlRpt(), false);
			return toJSON(ctRpt);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	public static String getRptJson(CtRichiestaPagamentoTelematico ctRpt) throws ServiceException {
		return toJSON(ctRpt);
	}
	
	public static String getRptJson(PaGetPaymentRes paGetPaymentRes_RPT) throws ServiceException {
		return toJSON(paGetPaymentRes_RPT);
	}
	
	public static String getRtJson(Rpt rpt) throws ServiceException {
		return getRtJson(rpt, false);
	}
	
	public static String getRtJson(Rpt rpt, boolean convertiMessaggioPagoPAV2InPagoPAV1) throws ServiceException {
		if(rpt.getXmlRt() == null)
			return null;


		try {
			switch (rpt.getVersione()) {
			case SANP_230:
				CtRicevutaTelematica ctRt = JaxbUtils.toRT(rpt.getXmlRt(), false);
				return toJSON(ctRt);
			case SANP_240:
				PaSendRTReq paSendRTReq_RT = JaxbUtils.toPaSendRTReq_RT(rpt.getXmlRt(), false);
				
				if(convertiMessaggioPagoPAV2InPagoPAV1) {
					CtRicevutaTelematica ctRt2 = MessaggiPagoPAUtils.toCtRicevutaTelematica(paSendRTReq_RT, rpt);
					return toJSON(ctRt2);
				}
				
				return toJSON(paSendRTReq_RT.getReceipt());
			}
			
			CtRicevutaTelematica ctRt = JaxbUtils.toRT(rpt.getXmlRt(), false);
			return toJSON(ctRt);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	public static String getRtJson(CtRicevutaTelematica ctRt ) throws ServiceException {
		return toJSON(ctRt);
	}
	
	public static String getRtJson(PaSendRTReq paSendRTReq_RT ) throws ServiceException {
		if(paSendRTReq_RT == null) return null;
		return toJSON(paSendRTReq_RT.getReceipt());
	}
	
	public static String toJSON(Object obj) throws ServiceException {
		if(obj == null)
			return null;
		
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new ServiceException(e);
		}
	}
	
	public static String toJSON(Object obj, String fields, SerializationConfig serializationConfig) throws ServiceException {
		try {
			if(fields != null && !fields.isEmpty()) {
				serializationConfig.setIncludes(Arrays.asList(fields.split(",")));
				serializationConfig.setExcludes(null); 
			}
			ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
			return serializer.getObject(obj);
		} catch(org.openspcoop2.utils.serialization.IOException e) {
			throw new ServiceException("Errore nella serializzazione della risposta.", e);
		}
	}
	
	public static <T> T parse(String jsonString, Class<T> t) throws ServiceException  {
		SerializationConfig serializationConfig = new SerializationConfig();
		serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
		serializationConfig.setIgnoreNullValues(true);
		return parse(jsonString, t, serializationConfig);
	}
	
	public static <T> T parse(String jsonString, Class<T> t, SerializationConfig serializationConfig) throws ServiceException  {
		try {
			IDeserializer deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
			
			@SuppressWarnings("unchecked")
			T object = (T) deserializer.getObject(jsonString, t);
			return object;
		} catch(org.openspcoop2.utils.serialization.IOException e) {
			throw new ServiceException(e.getMessage(), e);
		}
	}
	
}
