package it.govpay.core.business.model.tracciati;

import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import it.govpay.core.utils.trasformazioni.Costanti;
import it.govpay.model.Operazione.TipoOperazioneType;

public class TrasformazioneDTOResponse {
	private String output;
	private Map<String, Object> dynamicMap;

	public TrasformazioneDTOResponse(String output, Map<String, Object> dynamicMap) {
		this.dynamicMap = dynamicMap;
		this.output = output;
	}

	public String getOutput() {
		return output;
	}

	public Map<String, Object> getDynamicMap() {
		return dynamicMap;
	}

	@SuppressWarnings("unchecked")
	public Boolean getAvvisatura() {
		Object object = this.getDynamicMap().get(Costanti.MAP_CTX_OBJECT);
		if(object != null) {
			Hashtable<String, Object> ctx = (Hashtable<String, Object>) object;
			return (Boolean) ctx.get("avvisatura");
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public Date getDataAvvisatura() {
		Object object = this.getDynamicMap().get(Costanti.MAP_CTX_OBJECT);
		if(object != null) {
			Hashtable<String, Object> ctx = (Hashtable<String, Object>) object;
			return  (Date) ctx.get("dataAvvisatura");
		}

		return null;
	}
	
	@SuppressWarnings("unchecked")
	public TipoOperazioneType getTipoOperazione() {
		Object object = this.getDynamicMap().get(Costanti.MAP_CTX_OBJECT);
		if(object != null) {
			Hashtable<String, Object> ctx = (Hashtable<String, Object>) object;
			Object object2 = ctx.get("tipoOperazione");
			if(object2 != null)
				return TipoOperazioneType.valueOf((String) object2);
		}
		return null;
	}
}
