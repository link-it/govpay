package it.govpay.core.utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Tributo;
import it.govpay.bd.model.Applicazione;

public class PagamentoContext {
	
	public static final String codUoBeneficiariaKey="uo";
	public static final String codTributoIuvKey="t";
	public static final String codApplicazioneIuvKey="a";
	public static final String anno4="Y";
	public static final String anno2="y";
	
	private String codSessionePortale;
	private boolean carrello;
	private String codCarrello;
	private String codDominio;
	private String iuv;
	private String ccp;
	private boolean pspRedirect;
	private String pspSessionId;
	private Map<String,String> iuvProps;
	private VersamentoContext versamentoCtx;
	
	public String getCodDominio() {
		return codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
	public String getIuv() {
		return iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public String getCcp() {
		return ccp;
	}
	public void setCcp(String ccp) {
		this.ccp = ccp;
	}
	public boolean isPspRedirect() {
		return pspRedirect;
	}
	public void setPspRedirect(boolean pspRedirect) {
		this.pspRedirect = pspRedirect;
	}
	public String getPspSessionId() {
		return pspSessionId;
	}
	public void setPspSessionId(String pspSessionId) {
		this.pspSessionId = pspSessionId;
	}
	public String getCodSessionePortale() {
		return codSessionePortale;
	}
	public void setCodSessionePortale(String codSessionePortale) {
		this.codSessionePortale = codSessionePortale;
	}
	public boolean isCarrello() {
		return carrello;
	}
	public void setCarrello(boolean carrello) {
		this.carrello = carrello;
	}
	public String getCodCarrello() {
		return codCarrello;
	}
	public void setCodCarrello(String codCarrello) {
		this.codCarrello = codCarrello;
	}
	public Map<String,String> getIuvProps() {
		return iuvProps;
	}
	public void setIuvProps(Map<String,String> iuvProps) {
		this.iuvProps = iuvProps;
	}
	public VersamentoContext getVersamentoCtx() {
		return versamentoCtx;
	}
	public void setVersamentoCtx(VersamentoContext versamentoCtx) {
		this.versamentoCtx = versamentoCtx;
	}
	
	private Map<String,String> getDefaultIuvProps(Applicazione applicazione) {
		Map<String,String> props = new HashMap<String,String>();
		
		if(applicazione != null && applicazione.getCodApplicazioneIuv() != null) {
			props.put(codApplicazioneIuvKey, applicazione.getCodApplicazioneIuv());
		}
		
		if(versamentoCtx != null) {
			if(versamentoCtx.getCodTributoIuv() != null)
				props.put(codTributoIuvKey, versamentoCtx.getCodTributoIuv());
		}
		
		Calendar now = Calendar.getInstance(); 
		int year = now.get(Calendar.YEAR);  
		
		props.put(anno4, year + "");
		props.put(anno2, year%100 + "");
		
		return props;
	}

	public Map<String,String> getAllIuvProps(Applicazione applicazione) {
		Map<String,String> props = getDefaultIuvProps(applicazione);
		
		if(iuvProps != null)
			props.putAll(iuvProps);
		
		return props;
	}
	
	public String getAllIuvPropsString(Applicazione applicazione) {
		StringBuffer sb = new StringBuffer();
		sb.append("Custom Props { ");
		if(iuvProps != null) {
			for(String key : iuvProps.keySet()) {
				sb.append("[" + key + "=" + iuvProps.get(key) + "] ");
			}
		}
		sb.append("} Default Props { ");
		
		Map<String,String> props = getDefaultIuvProps(applicazione);
		if(props != null) {
			for(String key : props.keySet()) {
				sb.append("[" + key + "=" + props.get(key) + "] ");
			}
		}
		sb.append("}");
		return sb.toString();
	}
	
	public void loadVersamentoContext(it.govpay.bd.model.Versamento versamento, BasicBD bd) throws ServiceException {
		versamentoCtx = new VersamentoContext();
		
		versamentoCtx.setCodUoBeneficiaria(versamento.getUo(bd).getCodUo());
		versamentoCtx.setCodUnivocoDebitore(versamento.getAnagraficaDebitore().getCodUnivoco());
		
		if(versamento.getSingoliVersamenti(bd).size() == 1){
			SingoloVersamento sv = versamento.getSingoliVersamenti(bd).get(0);
			
			Tributo t = sv.getTributo(bd);
			if(t != null) {
				versamentoCtx.setCodContabilita(t.getCodContabilita());
				versamentoCtx.setTipoContabilita(t.getTipoContabilita());
				versamentoCtx.setCodTributoIuv(t.getCodTributoIuv());
			} else {
				versamentoCtx.setCodContabilita(sv.getCodContabilita());
				versamentoCtx.setTipoContabilita(sv.getTipoContabilita());
			}
		}
	}
	
}
