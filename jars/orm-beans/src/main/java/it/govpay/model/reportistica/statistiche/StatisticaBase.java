package it.govpay.model.reportistica.statistiche;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public abstract class StatisticaBase implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Date data;
	private Long numeroPagamenti;
	private BigDecimal importo;
	
	public StatisticaBase() {
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
	public BigDecimal getImporto() {
		return importo;
	}
	public void setImporto(BigDecimal importo) {
		this.importo = importo;
	}
}
