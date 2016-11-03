package it.govpay.core.utils;

import it.govpay.model.Tributo.TipoContabilta;

public class VersamentoContext {
	
	private String codUoBeneficiaria;
	private String codUnivocoDebitore;
	
	//Valorizzati solo se il versamento ha un solo singolo pagamento
	private TipoContabilta tipoContabilita;
	private String codContabilita;
	
	//Valorizzato solo se il versamento ha un solo singolo pagamento associato ad un tributo censito
	private String codTributoIuv;

	public String getCodUoBeneficiaria() {
		return codUoBeneficiaria;
	}

	public void setCodUoBeneficiaria(String codUoBeneficiaria) {
		this.codUoBeneficiaria = codUoBeneficiaria;
	}

	public TipoContabilta getTipoContabilita() {
		return tipoContabilita;
	}

	public void setTipoContabilita(TipoContabilta tipoContabilita) {
		this.tipoContabilita = tipoContabilita;
	}

	public String getCodContabilita() {
		return codContabilita;
	}

	public void setCodContabilita(String codContabilita) {
		this.codContabilita = codContabilita;
	}

	public String getCodTributoIuv() {
		return codTributoIuv;
	}

	public void setCodTributoIuv(String codTributoIuv) {
		this.codTributoIuv = codTributoIuv;
	}

	public String getCodUnivocoDebitore() {
		return codUnivocoDebitore;
	}

	public void setCodUnivocoDebitore(String codUnivocoDebitore) {
		this.codUnivocoDebitore = codUnivocoDebitore;
	}
	
	

}
