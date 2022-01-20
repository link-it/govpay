package it.govpay.web.utils;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

import it.govpay.core.utils.GpContext;
import it.govpay.servizi.commons.EsitoOperazione;
import it.govpay.servizi.commons.MetaInfo;
import it.govpay.servizi.commons.MetaInfo.IuvProp;

public class Utils {
	
	public static SimpleDateFormat simpleDateFormatAnno = new SimpleDateFormat("yyyy");
	
	public static void loadMetaInfo(GpContext ctx, MetaInfo metaInfo) {
		if(metaInfo != null){
			Map<String, String> iuvProps = new HashMap<String, String>();
			for(IuvProp prop : metaInfo.getIuvProp()){
				iuvProps.put(prop.getNome(), prop.getValue());
			}
			ctx.getPagamentoCtx().setIuvProps(iuvProps);
		}
	}
	
	public static EsitoOperazione fromEsitoOperazioneGovpay(it.govpay.core.beans.EsitoOperazione esito) {
		EsitoOperazione toRet = EsitoOperazione.OK;
		
		
		
		return toRet;
	}
}
