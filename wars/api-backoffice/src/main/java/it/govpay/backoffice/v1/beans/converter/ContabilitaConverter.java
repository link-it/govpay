package it.govpay.backoffice.v1.beans.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openspcoop2.utils.serialization.IDeserializer;
import org.openspcoop2.utils.serialization.IOException;
import org.openspcoop2.utils.serialization.ISerializer;
import org.openspcoop2.utils.serialization.SerializationConfig;
import org.openspcoop2.utils.serialization.SerializationFactory;
import org.openspcoop2.utils.serialization.SerializationFactory.SERIALIZATION_TYPE;

import it.govpay.backoffice.v1.beans.Contabilita;
import it.govpay.core.utils.SimpleDateFormatUtils;

public class ContabilitaConverter {
	
	public static List<Contabilita> toRsModel(String contabilitaJson) {
		if(contabilitaJson == null)
			return null;
		
//		getDettaglioObject(contabilitaJson, List<it.govpay.model.Contabilita>);
		return null;
	}
	
	
	public static List<Contabilita> toRsModel(List<it.govpay.model.Contabilita> dto) {
		if(dto != null) {
			List<Contabilita> rsModel = new ArrayList<Contabilita>();
			for (it.govpay.model.Contabilita contabilita : dto) {
				rsModel.add(toRsModel(contabilita));
			}
			
			return rsModel;
		}
		
		return null;
	}

	public static Contabilita toRsModel(it.govpay.model.Contabilita dto) {
		Contabilita rsModel = new Contabilita();
		
		rsModel.setAccertamento(dto.getAccertamento());
		if(dto.getAnnoAccertamento() != null) {
			rsModel.setAnnoAccertamento(new BigDecimal(dto.getAnnoAccertamento()));
		}
		rsModel.setAnnoEsercizio(new BigDecimal(dto.getAnnoEsercizio()));
		rsModel.setCapitolo(dto.getCapitolo());
		rsModel.setCodGestionaleEnte(dto.getCodGestionaleEnte());
		rsModel.setImporto(dto.getImporto());
		rsModel.setSiope(dto.getSiope());
		rsModel.setSubAccertamento(dto.getSubAccertamento());
		rsModel.setUfficio(dto.getUfficio());
		
		return rsModel;
	}
	
	
	public static String toStringDTO(List<Contabilita> contabilita) throws IOException {
		if(contabilita == null)
			return null;
		
		return getDettaglioAsString(contabilita);
	}
	
	
	public static List<it.govpay.model.Contabilita> toDTO(List<Contabilita> dto) {
		if(dto != null) {
			List<it.govpay.model.Contabilita> rsModel = new ArrayList<it.govpay.model.Contabilita>();
			for (Contabilita contabilita : dto) {
				rsModel.add(toDTO(contabilita));
			}
			
			return rsModel;
		}
		
		return null;
	}

	public static it.govpay.model.Contabilita toDTO(Contabilita dto) {
		it.govpay.model.Contabilita rsModel = new it.govpay.model.Contabilita();
		
		rsModel.setAccertamento(dto.getAccertamento());
		if(dto.getAnnoAccertamento() != null) {
			rsModel.setAnnoAccertamento(dto.getAnnoAccertamento().intValue());
		}
		rsModel.setAnnoEsercizio(dto.getAnnoEsercizio().intValue());
		rsModel.setCapitolo(dto.getCapitolo());
		rsModel.setCodGestionaleEnte(dto.getCodGestionaleEnte());
		rsModel.setImporto(dto.getImporto());
		rsModel.setSiope(dto.getSiope());
		rsModel.setSubAccertamento(dto.getSubAccertamento());
		rsModel.setUfficio(dto.getUfficio());
		
		return rsModel;
	}
	
	
	public static <T> T getDettaglioObject(String json, Class<T> tClass) throws IOException {
		if(json != null && tClass != null) {
			SerializationConfig serializationConfig = new SerializationConfig();
			serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinutiSecondi());
			serializationConfig.setIgnoreNullValues(true);
			IDeserializer deserializer = SerializationFactory.getDeserializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
			return tClass.cast(deserializer.getObject(json, tClass));
		}

		return null;
	}

	public static String getDettaglioAsString(Object obj) throws IOException {
		if(obj != null) {
			SerializationConfig serializationConfig = new SerializationConfig();
			serializationConfig.setExcludes(Arrays.asList("jsonIdFilter"));
			serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatDataOreMinutiSecondi());
			ISerializer serializer = SerializationFactory.getSerializer(SERIALIZATION_TYPE.JSON_JACKSON, serializationConfig);
			return serializer.getObject(obj); 
		}
		return null;
	}
	
}
