/**
 * 
 */
package it.govpay.core.dao.pagamenti.dto;

import java.util.List;

import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.PagamentoPortale;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.SingoloVersamento;
import it.govpay.bd.model.UnitaOperativa;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.viste.model.VersamentoIncasso;
import it.govpay.bd.model.Applicazione;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class LeggiPendenzaDTOResponse {

	private VersamentoIncasso versamentoIncasso;
	private Versamento versamento;
	private List<SingoloVersamento> lstSingoliVersamenti;
	private UnitaOperativa unitaOperativa;
	private Dominio dominio;
	private Applicazione applicazione;
	private byte[] avvisoPdf;
	private List<PagamentoPortale> pagamenti;
	private List<Rpt> rpts;

	public Versamento getVersamento() {
		return this.versamento;
	}

	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
	}

	public Applicazione getApplicazione() {
		return this.applicazione;
	}

	public void setApplicazione(Applicazione applicazione) {
		this.applicazione = applicazione;
	}

	public Dominio getDominio() {
		return this.dominio;
	}

	public void setDominio(Dominio dominio) {
		this.dominio = dominio;
	}

	public UnitaOperativa getUnitaOperativa() {
		return this.unitaOperativa;
	}

	public void setUnitaOperativa(UnitaOperativa unitaOperativa) {
		this.unitaOperativa = unitaOperativa;
	}

	public List<SingoloVersamento> getLstSingoliVersamenti() {
		return this.lstSingoliVersamenti;
	}

	public void setLstSingoliVersamenti(List<SingoloVersamento> lstSingoliVersamenti) {
		this.lstSingoliVersamenti = lstSingoliVersamenti;
	}

	public byte[] getAvvisoPdf() {
		return this.avvisoPdf;
	}

	public void setAvvisoPdf(byte[] avvisoPdf) {
		this.avvisoPdf = avvisoPdf;
	}

	public VersamentoIncasso getVersamentoIncasso() {
		return versamentoIncasso;
	}

	public void setVersamentoIncasso(VersamentoIncasso versamentoIncasso) {
		this.versamentoIncasso = versamentoIncasso;
	}

	public List<PagamentoPortale> getPagamenti() {
		return pagamenti;
	}

	public void setPagamenti(List<PagamentoPortale> pagamenti) {
		this.pagamenti = pagamenti;
	}

	public List<Rpt> getRpts() {
		return rpts;
	}

	public void setRpts(List<Rpt> rpts) {
		this.rpts = rpts;
	}
	
}
