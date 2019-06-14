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
	
	public static final String codUoBeneficiariaKey="u";
	public static final String codificaIuvKey="t";
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
		return this.codDominio;
	}
	public void setCodDominio(String codDominio) {
		this.codDominio = codDominio;
	}
	public String getIuv() {
		return this.iuv;
	}
	public void setIuv(String iuv) {
		this.iuv = iuv;
	}
	public String getCcp() {
		return this.ccp;
	}
	public void setCcp(String ccp) {
		this.ccp = ccp;
	}
	public boolean isPspRedirect() {
		return this.pspRedirect;
	}
	public void setPspRedirect(boolean pspRedirect) {
		this.pspRedirect = pspRedirect;
	}
	public String getPspSessionId() {
		return this.pspSessionId;
	}
	public void setPspSessionId(String pspSessionId) {
		this.pspSessionId = pspSessionId;
	}
	public String getCodSessionePortale() {
		return this.codSessionePortale;
	}
	public void setCodSessionePortale(String codSessionePortale) {
		this.codSessionePortale = codSessionePortale;
	}
	public boolean isCarrello() {
		return this.carrello;
	}
	public void setCarrello(boolean carrello) {
		this.carrello = carrello;
	}
	public String getCodCarrello() {
		return this.codCarrello;
	}
	public void setCodCarrello(String codCarrello) {
		this.codCarrello = codCarrello;
	}
	public Map<String,String> getIuvProps() {
		return this.iuvProps;
	}
	public void setIuvProps(Map<String,String> iuvProps) {
		this.iuvProps = iuvProps;
	}
	public VersamentoContext getVersamentoCtx() {
		return this.versamentoCtx;
	}
	public void setVersamentoCtx(VersamentoContext versamentoCtx) {
		this.versamentoCtx = versamentoCtx;
	}
	
	private Map<String,String> getDefaultIuvProps(Applicazione applicazione) {
		Map<String,String> props = new HashMap<>();
		
		if(applicazione != null && applicazione.getCodApplicazioneIuv() != null) {
			props.put(codApplicazioneIuvKey, applicazione.getCodApplicazioneIuv());
		}
		
		if(this.versamentoCtx != null) {
			if(this.versamentoCtx.getCodificaIuv() != null)
				props.put(codificaIuvKey, this.versamentoCtx.getCodificaIuv());
		}
		
		Calendar now = Calendar.getInstance(); 
		int year = now.get(Calendar.YEAR);  
		
		props.put(anno4, year + "");
		props.put(anno2, year%100 + "");
		
		return props;
	}

	public Map<String,String> getAllIuvProps(Applicazione applicazione) {
		Map<String,String> props = this.getDefaultIuvProps(applicazione);
		
		if(this.iuvProps != null)
			props.putAll(this.iuvProps);
		
		return props;
	}
	
	public String getAllIuvPropsString(Applicazione applicazione) {
		StringBuffer sb = new StringBuffer();
		sb.append("Custom Props { ");
		if(this.iuvProps != null) {
			for(String key : this.iuvProps.keySet()) {
				sb.append("[" + key + "=" + this.iuvProps.get(key) + "] ");
			}
		}
		sb.append("} Default Props { ");
		
		Map<String,String> props = this.getDefaultIuvProps(applicazione);
		if(props != null) {
			for(String key : props.keySet()) {
				sb.append("[" + key + "=" + props.get(key) + "] ");
			}
		}
		sb.append("}");
		return sb.toString();
	}
	
	public void loadVersamentoContext(it.govpay.bd.model.Versamento versamento, BasicBD bd) throws ServiceException {
		this.versamentoCtx = new VersamentoContext();
		
		this.versamentoCtx.setCodUoBeneficiaria(versamento.getUo(bd).getCodUo());
		this.versamentoCtx.setCodUnivocoDebitore(versamento.getAnagraficaDebitore().getCodUnivoco());
		this.versamentoCtx.setCodificaIuv(versamento.getTipoVersamentoDominio(bd).getCodificaIuv());
		
		if(versamento.getSingoliVersamenti(bd).size() == 1){
			SingoloVersamento sv = versamento.getSingoliVersamenti(bd).get(0);
			
			Tributo t = sv.getTributo(bd);
			if(t != null) {
				this.versamentoCtx.setCodContabilita(t.getCodContabilita());
				this.versamentoCtx.setTipoContabilita(t.getTipoContabilita());
			} else {
				this.versamentoCtx.setCodContabilita(sv.getCodContabilita());
				this.versamentoCtx.setTipoContabilita(sv.getTipoContabilita());
			}
		}
	}
	
}
