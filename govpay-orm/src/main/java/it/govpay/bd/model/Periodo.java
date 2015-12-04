package it.govpay.bd.model;



public class Periodo extends BasicModel {
	private static final long serialVersionUID = 1L;
	private String da;
	private String a;
	
	public Periodo(){}
	
	public String getDa() {
		return da;
	}
	public void setDa(String da) {
		this.da = da;
	}
	public String getA() {
		return a;
	}
	public void setA(String a) {
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
		
		return da.equals(periodo.getDa()) && a.equals(periodo.getA()); 
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return da + "-" + a;
	}
}
