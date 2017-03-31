/**
 * 
 */
package it.govpay.web.rs.sonde;

import java.util.Date;

import org.openspcoop2.utils.sonde.Sonda;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 07/mar/2017 $
 * 
 */
public class DettaglioSonda {
	
	public enum TipoSonda {
		Coda, Batch, Sconosciuto
	}

	private String nome;
	private Integer stato;
	private String descrizioneStato;
	private long durataStatoValue;
	private String durataStato;
	private String sogliaWarn;
	private String sogliaError;
	private long sogliaWarnValue;
	private long sogliaErrorValue;
	private Date dataUltimoCheck;
	private TipoSonda tipo;
	
	public DettaglioSonda(TipoSonda tipo) {
		this.tipo = tipo;
	}
	
	public DettaglioSonda(Class<? extends Sonda> class1) {
		if(class1.equals(org.openspcoop2.utils.sonde.impl.SondaBatch.class)) {
			this.tipo = TipoSonda.Batch;
			return;
		}
		
		if(class1.equals(org.openspcoop2.utils.sonde.impl.SondaCoda.class)) {
			this.tipo = TipoSonda.Coda;
			return;
		}
		
		this.tipo = TipoSonda.Sconosciuto;
	}

	public long getSogliaWarnValue() {
		return sogliaWarnValue;
	}
	
	public String getSogliaWarn() {
		return sogliaWarn;
	}
	
	public void setSogliaWarn(long sogliaWarn) {
		this.sogliaWarnValue = sogliaWarn;
		
		switch (tipo) {
		case Batch:
			this.sogliaWarn = "Lasso di tempo senza esecuzioni con successo: " + toString(sogliaWarn);
			break;
		case Coda:
			this.sogliaWarn = "Numero di elementi accodati: " + sogliaWarn;
			break;
		default:
			
			break;
		}
	}
	
	public long getSogliaErrorValue() {
		return sogliaErrorValue;
	}
	
	public String getSogliaError() {
		return sogliaError;
	}
	
	public void setSogliaError(long sogliaError) {
		this.sogliaErrorValue = sogliaError;
		
		switch (tipo) {
		case Batch:
			this.sogliaError = "Lasso di tempo senza esecuzioni con successo: " + toString(sogliaError);
			break;
		case Coda:
			this.sogliaError = "Numero di elementi accodati: " + sogliaError;
			break;
		default:
			
			break;
		}
	}
	
	public Date getDataUltimoCheck() {
		return dataUltimoCheck;
	}
	public void setDataUltimoCheck(Date dataUltimoCheck) {
		this.dataUltimoCheck = dataUltimoCheck;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Integer getStato() {
		return stato;
	}
	public void setStato(Integer stato) {
		this.stato = stato;
	}
	public String getDescrizioneStato() {
		return descrizioneStato;
	}
	public void setDescrizioneStato(String descrizioneStato) {
		this.descrizioneStato = descrizioneStato;
	}
	
	public String getDurataStato() {
		return durataStato;
	}

	public void setDurataStato(Date inizioStato) {
		if(inizioStato != null) {
			this.durataStatoValue = new Date().getTime() - inizioStato.getTime();
			this.durataStato = toString(this.durataStatoValue);
		} else {
			this.durataStatoValue = 0l;
			this.durataStato = null;
		}
	}
	
	public TipoSonda getTipo() {
		return tipo;
	}
	
	private String toString(long millis) {
		long second = (millis / 1000) % 60;
		long minute = (millis / (1000 * 60)) % 60;
		long hour = (millis / (1000 * 60 * 60)) % 24;
		long days = (millis / (1000 * 60 * 60)) / 24;

		StringBuffer sb = new StringBuffer();
		if(days > 0) sb.append(days + " gg ");
		if(hour > 0) sb.append(hour + " h ");
		if(minute > 0) sb.append(minute + " m ");
		if(second > 0) sb.append(second + " s ");
		if((millis % 1000) > 0) sb.append((millis % 1000) + " millis");
		return sb.toString();
	}
}
