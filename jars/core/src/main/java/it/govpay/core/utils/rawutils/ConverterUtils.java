package it.govpay.core.utils.rawutils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.serialization.IDeserializer;
import org.openspcoop2.utils.serialization.ISerializer;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;

import it.gov.digitpa.schemas._2011.pagamenti.CtRicevutaTelematica;
import it.gov.digitpa.schemas._2011.pagamenti.CtRichiestaPagamentoTelematico;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaGetPaymentRes;
import it.gov.pagopa.pagopa_api.pa.pafornode.PaSendRTReq;
import it.govpay.bd.model.Rpt;
import it.govpay.core.utils.JaxbUtils;
import it.govpay.core.utils.SimpleDateFormatUtils;

public class ConverterUtils {

	private static Map<String, String> map;
	private static ObjectMapper mapper;
	static {
		map = new HashMap<>();
		map.put("http://www.digitpa.gov.it/schemas/2011/Pagamenti/", "");
		mapper = new ObjectMapper();
		mapper.registerModule(new JaxbAnnotationModule());
		mapper.registerModule(new DateModule());
		mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		mapper.setDateFormat(DateFormatUtils.newSimpleDateFormatSoloData());
	}

	public static String getRptJson(Rpt rpt) throws ServiceException {
		if(rpt.getXmlRpt() == null)
			return null;

		try {
			switch (rpt.getVersione()) {
			case SANP_230:
				CtRichiestaPagamentoTelematico ctRpt = JaxbUtils.toRPT(rpt.getXmlRpt(), false);
				return mapper.writeValueAsString(ctRpt);
			case SANP_240:
				PaGetPaymentRes paGetPaymentRes_RPT = JaxbUtils.toPaGetPaymentRes_RPT(rpt.getXmlRpt(), false);
				return mapper.writeValueAsString(paGetPaymentRes_RPT);
			}
			
			CtRichiestaPagamentoTelematico ctRpt = JaxbUtils.toRPT(rpt.getXmlRpt(), false);
			return mapper.writeValueAsString(ctRpt);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public static String getRtJson(Rpt rpt) throws ServiceException {
		if(rpt.getXmlRt() == null)
			return null;


		try {
			switch (rpt.getVersione()) {
			case SANP_230:
				CtRicevutaTelematica ctRt = JaxbUtils.toRT(rpt.getXmlRt(), false);
				return mapper.writeValueAsString(ctRt);
			case SANP_240:
				PaSendRTReq paSendRTReq_RT = JaxbUtils.toPaSendRTReq_RT(rpt.getXmlRt(), false);
				return mapper.writeValueAsString(paSendRTReq_RT);
			}
			
			CtRicevutaTelematica ctRt = JaxbUtils.toRT(rpt.getXmlRt(), false);
			return mapper.writeValueAsString(ctRt);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}
	
	public static String toJSON(Object obj, String fields) throws ServiceException {
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
	
	public static <T> T parse(String jsonString, Class<T> t) throws ServiceException, ValidationException  {
		SerializationConfig serializationConfig = new SerializationConfig();
		serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
		serializationConfig.setIgnoreNullValues(true);
		return parse(jsonString, t, serializationConfig);
	}
	
	public static <T> T parse(String jsonString, Class<T> t, SerializationConfig serializationConfig) throws ServiceException, ValidationException  {
		try {
			IDeserializer deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
			
			@SuppressWarnings("unchecked")
			T object = (T) deserializer.getObject(jsonString, t);
			return object;
		} catch(org.openspcoop2.utils.serialization.IOException e) {
			throw new ValidationException(e.getMessage(), e);
		}
	}
	
	public static <T> List<T> convertFromJsonToList(String json, TypeReference<List<T>> var)  throws java.io.IOException{
		if(json != null && var != null) {
			SerializationConfig serializationConfig = new SerializationConfig();
			serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinutiSecondi());
			serializationConfig.setIgnoreNullValues(true);

			mapper.setDateFormat(serializationConfig.getDf());
			if(serializationConfig.isSerializeEnumAsString())
				  mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
			
			return mapper.readerFor(var).readValue(json);
		}

		return null;
	}
	
//	public static <T> T parse(String jsonString, Class<T> t) throws ServiceException, ValidationException  {
//		try {
//			return mapper.readValue(jsonString, t);
//		} catch (JsonMappingException | JsonParseException e) {
//			throw new ValidationException(e);
//		} catch (Exception  e) {
//			throw new ServiceException(e);
//		}
//	}
	
}
