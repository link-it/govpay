package it.govpay.ragioneria.v3.beans.converter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.json.ValidationException;
import org.openspcoop2.utils.serialization.SerializationConfig;

import it.govpay.core.utils.SimpleDateFormatUtils;
import it.govpay.core.utils.rawutils.ConverterUtils;
import it.govpay.ragioneria.v3.beans.Contabilita;
import it.govpay.ragioneria.v3.beans.QuotaContabilita;

public class ContabilitaConverter {

	public static Contabilita toRsModel(String contabilitaJson) throws java.io.IOException, ServiceException, ValidationException {
		if(contabilitaJson == null)
			return null;
		
		it.govpay.model.Contabilita dto = ConverterUtils.parse(contabilitaJson, it.govpay.model.Contabilita.class);
		
		return toRsModel(dto);
	}
	
	
	public static List<QuotaContabilita> toRsModel(List<it.govpay.model.QuotaContabilita> dto) {
		if(dto != null) {
			List<QuotaContabilita> rsModel = new ArrayList<QuotaContabilita>();
			for (it.govpay.model.QuotaContabilita contabilita : dto) {
				rsModel.add(toRsModel(contabilita));
			}
			
			return rsModel;
		}
		
		return null;
	}
	
	public static QuotaContabilita toRsModel(it.govpay.model.QuotaContabilita dto) {
		QuotaContabilita rsModel = new QuotaContabilita();
		
		rsModel.setAccertamento(dto.getAccertamento());
		rsModel.setAnnoEsercizio(new BigDecimal(dto.getAnnoEsercizio()));
		rsModel.setCapitolo(dto.getCapitolo());
		rsModel.setImporto(dto.getImporto());
		rsModel.setProprietaCustom(dto.getProprietaCustom());
		
		return rsModel;
	}

	public static Contabilita toRsModel(it.govpay.model.Contabilita dto) {
		Contabilita rsModel = new Contabilita();
		
		rsModel.setQuote(toRsModel(dto.getQuote()));
		rsModel.setProprietaCustom(dto.getProprietaCustom());
		
		return rsModel;
	}
	
	
	public static String toStringDTO(Contabilita contabilita) throws ServiceException {
		if(contabilita == null)
			return null;
		
		it.govpay.model.Contabilita dto = toDTO(contabilita);
		
		return getDettaglioAsString(dto);
	}
	
	
	public static List<it.govpay.model.QuotaContabilita> toDTO(List<QuotaContabilita> dto) throws ServiceException {
		if(dto != null) {
			List<it.govpay.model.QuotaContabilita> rsModel = new ArrayList<it.govpay.model.QuotaContabilita>();
			for (QuotaContabilita contabilita : dto) {
				rsModel.add(toDTO(contabilita));
			}
			
			return rsModel;
		}
		
		return null;
	}

	public static it.govpay.model.Contabilita toDTO(Contabilita dto) throws ServiceException {
		it.govpay.model.Contabilita rsModel = new it.govpay.model.Contabilita();
		
		rsModel.setQuote(toDTO(dto.getQuote()));
		rsModel.setProprietaCustom(dto.getProprietaCustom());
		
		
		return rsModel;
	}
	
	public static it.govpay.model.QuotaContabilita toDTO(QuotaContabilita dto) throws ServiceException {
		it.govpay.model.QuotaContabilita rsModel = new it.govpay.model.QuotaContabilita();
		
		rsModel.setAccertamento(dto.getAccertamento());
		rsModel.setAnnoEsercizio(dto.getAnnoEsercizio().intValue());
		rsModel.setCapitolo(dto.getCapitolo());
		rsModel.setImporto(dto.getImporto());
		rsModel.setProprietaCustom(dto.getProprietaCustom());
		
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
