package it.govpay.bd.model;

import java.util.Date;


public class Periodo extends BasicModel {
	private static final long serialVersionUID = 1L;
	private Date da;
	private Date a;
	
	public Periodo(){}
	
	public Date getDa() {
		return da;
	}
	public void setDa(Date da) {
		this.da = da;
	}
	public Date getA() {
		return a;
	}
	public void setA(Date a) {
		this.a = a;
	}
	
	@Override
	public boolean equals(Object obj) {
		Periodo periodo = null;
		if(obj instanceof Periodo) {
			periodo = (Periodo) obj;
		} else {
			return false;
		}
		
		return da.getTime() == periodo.getDa().getTime() && a.getTime() == periodo.getA().getTime(); 
	}
}
