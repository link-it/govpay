package it.govpay.web.ws;

import java.util.HashMap;
import java.util.Map;

import it.govpay.core.utils.GpContext;
import it.govpay.servizi.commons.MetaInfo;
import it.govpay.servizi.commons.MetaInfo.IuvProp;

public class Utils {
	
	public static void loadMetaInfo(GpContext ctx, MetaInfo metaInfo) {
		if(metaInfo != null){
			Map<String, String> iuvProps = new HashMap<String, String>();
			for(IuvProp prop : metaInfo.getIuvProp()){
				iuvProps.put(prop.getNome(), prop.getValue());
			}
			ctx.getPagamentoCtx().setIuvProps(iuvProps);
		}
	}
}
