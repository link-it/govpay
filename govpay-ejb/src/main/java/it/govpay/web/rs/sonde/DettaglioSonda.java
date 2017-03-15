/**
 * 
 */
package it.govpay.web.rs.sonde;

import java.util.Date;
import java.util.Properties;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 07/mar/2017 $
 * 
 */
public class DettaglioSonda {

	private String nome;
	private Integer stato;
	private String descrizioneStato;
	private long sogliaWarn;
	private long sogliaError;
	private Date dataOk;
	private Date dataWarn;
	private Date dataError;
	private Date dataUltimoCheck;
	private Properties datiCheck;
	
	public long getSogliaWarn() {
		return sogliaWarn;
	}
	public void setSogliaWarn(long sogliaWarn) {
		this.sogliaWarn = sogliaWarn;
	}
	public long getSogliaError() {
		return sogliaError;
	}
	public void setSogliaError(long sogliaError) {
		this.sogliaError = sogliaError;
	}
	public Date getDataOk() {
		return dataOk;
	}
	public void setDataOk(Date dataOk) {
		this.dataOk = dataOk;
	}
	public Date getDataWarn() {
		return dataWarn;
	}
	public void setDataWarn(Date dataWarn) {
		this.dataWarn = dataWarn;
	}
	public Date getDataError() {
		return dataError;
	}
	public void setDataError(Date dataError) {
		this.dataError = dataError;
	}
	public Date getDataUltimoCheck() {
		return dataUltimoCheck;
	}
	public void setDataUltimoCheck(Date dataUltimoCheck) {
		this.dataUltimoCheck = dataUltimoCheck;
	}
	public Properties getDatiCheck() {
		return datiCheck;
	}
	public void setDatiCheck(Properties datiCheck) {
		this.datiCheck = datiCheck;
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
}
