package it.govpay.core.utils;

import it.govpay.model.Tributo.TipoContabilita;

public class VersamentoContext {
	
	private String codUoBeneficiaria;
	private String codUnivocoDebitore;
	
	//Valorizzati solo se il versamento ha un solo singolo pagamento
	private TipoContabilita tipoContabilita;
	private String codContabilita;
	
	//Valorizzato solo se il versamento ha un solo singolo pagamento associato ad un tributo censito
	private String codTributoIuv;

	public String getCodUoBeneficiaria() {
		return this.codUoBeneficiaria;
	}

	public void setCodUoBeneficiaria(String codUoBeneficiaria) {
		this.codUoBeneficiaria = codUoBeneficiaria;
	}

	public TipoContabilita getTipoContabilita() {
		return this.tipoContabilita;
	}

	public void setTipoContabilita(TipoContabilita tipoContabilita) {
		this.tipoContabilita = tipoContabilita;
	}

	public String getCodContabilita() {
		return this.codContabilita;
	}

	public void setCodContabilita(String codContabilita) {
		this.codContabilita = codContabilita;
	}

	public String getCodTributoIuv() {
		return this.codTributoIuv;
	}

	public void setCodTributoIuv(String codTributoIuv) {
		this.codTributoIuv = codTributoIuv;
	}

	public String getCodUnivocoDebitore() {
		return this.codUnivocoDebitore;
	}

	public void setCodUnivocoDebitore(String codUnivocoDebitore) {
		this.codUnivocoDebitore = codUnivocoDebitore;
	}
}
