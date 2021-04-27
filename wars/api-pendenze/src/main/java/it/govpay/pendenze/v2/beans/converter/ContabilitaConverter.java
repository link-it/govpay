package it.govpay.pendenze.v2.beans.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.serialization.SerializationConfig;

import com.fasterxml.jackson.core.type.TypeReference;

import it.govpay.pendenze.v2.beans.Contabilita;

import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.rawutils.ConverterUtils;

public class ContabilitaConverter {

	public static List<Contabilita> toRsModel(String contabilitaJson) throws java.io.IOException {
		if(contabilitaJson == null)
			return null;
		
		List<it.govpay.model.Contabilita> dto = ConverterUtils.convertFromJsonToList(contabilitaJson, new TypeReference<List<it.govpay.model.Contabilita>>() {});
		
		return toRsModel(dto);
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
	
	
	public static String toStringDTO(List<Contabilita> contabilita) throws ServiceException {
		if(contabilita == null)
			return null;
		
		List<it.govpay.model.Contabilita> dto = toDTO(contabilita);
		
		return getDettaglioAsString(dto);
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
	
	private static String getDettaglioAsString(Object obj) throws ServiceException {
		if(obj != null) {
			SerializationConfig serializationConfig = new SerializationConfig();
			serializationConfig.setExcludes(Arrays.asList("jsonIdFilter"));
			serializationConfig.setDf(SimpleDateFormatUtils.newSimpleDateFormatSoloData());
			serializationConfig.setIgnoreNullValues(true);
			return ConverterUtils.toJSON(obj, null, serializationConfig);
		}
		return null;
	}
}
