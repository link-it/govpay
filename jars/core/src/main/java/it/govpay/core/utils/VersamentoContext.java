package it.govpay.core.utils;

import it.govpay.model.Tributo.TipoContabilita;

public class VersamentoContext {
	
	private String codUoBeneficiaria;
	private String codUnivocoDebitore;
	
	//Valorizzati solo se il versamento ha un solo singolo pagamento
	private TipoContabilita tipoContabilita;
	private String codContabilita;
	
	//Valorizzato con la codifica IUV prevista per la tipologia di versamento
	private String codificaIuv;

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

	public String getCodificaIuv() {
		return this.codificaIuv;
	}

	public void setCodificaIuv(String codificaIuv) {
		this.codificaIuv = codificaIuv;
	}

	public String getCodUnivocoDebitore() {
		return this.codUnivocoDebitore;
	}

	public void setCodUnivocoDebitore(String codUnivocoDebitore) {
		this.codUnivocoDebitore = codUnivocoDebitore;
	}
}
