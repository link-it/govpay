package it.govpay.model.reportistica.statistiche;

import java.io.Serializable;
import java.util.Date;

public class StatisticaRiscossione extends FiltroRiscossioni implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date data;
	private Long numeroPagamenti;
	private Double importo;
	
	public StatisticaRiscossione() {
	}

	public StatisticaRiscossione(FiltroRiscossioni filtro) {
		this();
		this.setCodApplicazione(filtro.getCodApplicazione());
		this.setCodDominio(filtro.getCodDominio());
		this.setCodTipoVersamento(filtro.getCodTipoVersamento());
		this.setCodUo(filtro.getCodUo());
		this.setDataA(filtro.getDataA());
		this.setDataDa(filtro.getDataDa());
		this.setDirezione(filtro.getDirezione());
		this.setDivisione(filtro.getDivisione());
		this.setIdApplicazione(filtro.getIdApplicazione());
		this.setIdDominio(filtro.getIdDominio());
		this.setIdTipoVersamento(filtro.getIdTipoVersamento());
		this.setIdUo(filtro.getIdUo());
	}
	
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public Long getNumeroPagamenti() {
		return numeroPagamenti;
	}
	public void setNumeroPagamenti(Long numeroPagamenti) {
		this.numeroPagamenti = numeroPagamenti;
	}
	public Double getImporto() {
		return importo;
	}
	public void setImporto(Double importo) {
		this.importo = importo;
	}

}
