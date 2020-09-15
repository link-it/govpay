package it.govpay.bd.model;

import java.util.Date;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.pagamento.DocumentiBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.VersamentiBD;

public class Promemoria extends it.govpay.model.Promemoria {
	
	public Promemoria() {}
	
	public Promemoria(Versamento versamento, TipoPromemoria tipoPromemoria) {
		this.setVersamento(versamento);
		long adesso = new Date().getTime();
		this.setDataAggiornamento(new Date(adesso));
		this.setDataCreazione(new Date(adesso));
		this.setDataProssimaSpedizione(new Date(adesso + 10000 ));
		this.setDescrizioneStato(null);
		this.setStato(StatoSpedizione.DA_SPEDIRE);
		this.setTentativiSpedizione(0l);
		this.setTipo(tipoPromemoria);
	}
	
	public Promemoria(Documento documento, TipoPromemoria tipoPromemoria) {
		this.setDocumento(documento);
		long adesso = new Date().getTime();
		this.setDataAggiornamento(new Date(adesso));
		this.setDataCreazione(new Date(adesso));
		this.setDataProssimaSpedizione(new Date(adesso + 10000 ));
		this.setDescrizioneStato(null);
		this.setStato(StatoSpedizione.DA_SPEDIRE);
		this.setTentativiSpedizione(0l);
		this.setTipo(tipoPromemoria);
	}
	
	public Promemoria(Rpt rpt, Versamento versamento, TipoPromemoria tipoPromemoria) {
		this.setVersamento(versamento);
		this.setRpt(rpt);
		long adesso = new Date().getTime();
		this.setDataAggiornamento(new Date(adesso));
		this.setDataCreazione(new Date(adesso));
		this.setDataProssimaSpedizione(new Date(adesso + 10000 ));
		this.setDescrizioneStato(null);
		this.setStato(StatoSpedizione.DA_SPEDIRE);
		this.setTentativiSpedizione(0l);
		this.setTipo(tipoPromemoria);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private transient Documento documento;
	private transient Versamento versamento;
	private transient Rpt rpt;
	
	public void setDocumento(Documento documento) {
		this.documento = documento;
		if(documento.getId() != null)
			this.setIdDocumento(documento.getId());
	}
	
	public Documento getDocumento(BasicBD bd) throws ServiceException {
		if(this.getIdDocumento() != null && this.documento == null) {
			DocumentiBD documentiBD = new DocumentiBD(bd);
			try {
				this.documento = documentiBD.getDocumento(this.getIdDocumento());
			} catch (NotFoundException e) {
			}
		}
		return this.documento;
	}
	
	public void setVersamento(Versamento versamento) {
		this.versamento = versamento;
		if(versamento.getId() != null)
			this.setIdVersamento(versamento.getId());
	}
	
	public Versamento getVersamento() {
		return this.versamento;
	}
	
	public Versamento getVersamento(BasicBD bd) throws ServiceException {
		if(this.getIdVersamento() != null && this.versamento == null) {
			VersamentiBD versamentiBD = new VersamentiBD(bd);
			this.versamento = versamentiBD.getVersamento(this.getIdVersamento());
		}
		return this.versamento;
	}
	
	public void setRpt(Rpt rpt) {
		this.rpt = rpt;
		this.setIdRpt(rpt.getId());
	}
	
	public Rpt getRpt() {
		return this.rpt;
	}
	
	public Rpt getRpt(BasicBD bd) throws ServiceException {
		if(this.rpt == null && this.getIdRpt() != null) {
			RptBD rptBD = new RptBD(bd);
			this.rpt = rptBD.getRpt(this.getIdRpt());
		}
			
		return this.rpt;
	}
}
