package it.govpay.core.utils.rawutils;

import java.util.Arrays;

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
import it.govpay.core.exceptions.IOException;
import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.model.Rpt;
import it.govpay.pagopa.beans.utils.JaxbUtils;

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

	public static String getRptJson(Rpt rpt) throws IOException {
		if(rpt.getXmlRpt() == null)
			return null;

		try {
			switch (rpt.getVersione()) {
			case SANP_230:
				CtRichiestaPagamentoTelematico ctRpt = JaxbUtils.toRPT(rpt.getXmlRpt(), false);
				return toJSON(ctRpt);
			case SANP_240:
				PaGetPaymentRes paGetPaymentRes_RPT = JaxbUtils.toPaGetPaymentRes_RPT(rpt.getXmlRpt(), false);
				return toJSON(paGetPaymentRes_RPT.getData());
			}
			
			CtRichiestaPagamentoTelematico ctRpt = JaxbUtils.toRPT(rpt.getXmlRpt(), false);
			return toJSON(ctRpt);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
	
	public static String getRptJson(CtRichiestaPagamentoTelematico ctRpt) throws IOException {
		return toJSON(ctRpt);
	}
	
	public static String getRptJson(PaGetPaymentRes paGetPaymentRes_RPT) throws IOException {
		return toJSON(paGetPaymentRes_RPT);
	}
	
	public static String getRtJson(Rpt rpt) throws IOException {
		if(rpt.getXmlRt() == null)
			return null;


		try {
			switch (rpt.getVersione()) {
			case SANP_230:
				CtRicevutaTelematica ctRt = JaxbUtils.toRT(rpt.getXmlRt(), false);
				return toJSON(ctRt);
			case SANP_240:
				PaSendRTReq paSendRTReq_RT = JaxbUtils.toPaSendRTReq_RT(rpt.getXmlRt(), false);
				return toJSON(paSendRTReq_RT.getReceipt());
			}
			
			CtRicevutaTelematica ctRt = JaxbUtils.toRT(rpt.getXmlRt(), false);
			return toJSON(ctRt);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}
	
	public static String getRtJson(CtRicevutaTelematica ctRt ) throws IOException {
		return toJSON(ctRt);
	}
	
	public static String toJSON(Object obj) throws IOException {
		if(obj == null)
			return null;
		
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonProcessingException e) {
			throw new IOException(e);
		}
	}
	
	public static String toJSON(Object obj, String fields, SerializationConfig serializationConfig) throws IOException {
		try {
			if(fields != null && !fields.isEmpty()) {
				serializationConfig.setIncludes(Arrays.asList(fields.split(",")));
				serializationConfig.setExcludes(null); 
			}
			ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
			return serializer.getObject(obj);
		} catch(org.openspcoop2.utils.serialization.IOException e) {
			throw new IOException("Errore nella serializzazione della risposta.", e);
		}
	}
	
	public static <T> T parse(String jsonString, Class<T> t) throws IOException  {
		SerializationConfig serializationConfig = new SerializationConfig();
		serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
		serializationConfig.setIgnoreNullValues(true);
		return parse(jsonString, t, serializationConfig);
	}
	
	public static <T> T parse(String jsonString, Class<T> t, SerializationConfig serializationConfig) throws IOException  {
		try {
			IDeserializer deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
			
			@SuppressWarnings("unchecked")
			T object = (T) deserializer.getObject(jsonString, t);
			return object;
		} catch(org.openspcoop2.utils.serialization.IOException e) {
			throw new IOException(e.getMessage(), e);
		}
	}
	
}
