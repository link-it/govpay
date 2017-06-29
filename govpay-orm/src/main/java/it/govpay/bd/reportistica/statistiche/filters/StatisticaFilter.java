package it.govpay.bd.reportistica.statistiche.filters;

import java.util.Date;

import org.openspcoop2.generic_project.dao.IExpressionConstructor;

import it.govpay.bd.AbstractFilter;
import it.govpay.model.reportistica.statistiche.TipoIntervallo;

public abstract class StatisticaFilter extends AbstractFilter {

	public StatisticaFilter(IExpressionConstructor expressionConstructor) {
		super(expressionConstructor);
	}

	private Date data;
	private TipoIntervallo tipoIntervallo;
	private Double soglia;
	
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public TipoIntervallo getTipoIntervallo() {
		return tipoIntervallo;
	}
	public void setTipoIntervallo(TipoIntervallo tipoIntervallo) {
		this.tipoIntervallo = tipoIntervallo;
	}
	public Double getSoglia() {
		return soglia;
	}
	public void setSoglia(Double soglia) {
		this.soglia = soglia;
	}
}
