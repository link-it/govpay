package it.govpay.core.utils;

import java.util.HashMap;
import java.util.Map;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.Tributo;
import it.govpay.model.Applicazione;

public class PagamentoContext {
	
	public static final String codUoBeneficiariaKey="uo";
	public static final String codTributoIuvKey="t";
	public static final String codApplicazioneIuvKey="a";
	
	private Applicazione applicazione;
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

	public Map<String,String> getAllIuvProps() {
		Map<String,String> props = new HashMap<String,String>();
		
		if(versamentoCtx != null) {
			props.put(codUoBeneficiariaKey, versamentoCtx.getCodUoBeneficiaria());
			props.put(codTributoIuvKey, versamentoCtx.getCodUoBeneficiaria());
			props.put(codApplicazioneIuvKey, applicazione.getCodApplicazioneIuv());
		}
		
		props.putAll(iuvProps);
		
		return props;
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
