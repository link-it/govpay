/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.orm.model;

import it.govpay.orm.Pagamento;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Pagamento 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class PagamentoModel extends AbstractModel<Pagamento> {

	public PagamentoModel(){
	
		super();
	
		this.ID_RPT = new it.govpay.orm.model.IdRptModel(new Field("idRPT",it.govpay.orm.IdRpt.class,"Pagamento",Pagamento.class));
		this.ID_SINGOLO_VERSAMENTO = new it.govpay.orm.model.IdSingoloVersamentoModel(new Field("idSingoloVersamento",it.govpay.orm.IdSingoloVersamento.class,"Pagamento",Pagamento.class));
		this.COD_SINGOLO_VERSAMENTO_ENTE = new Field("codSingoloVersamentoEnte",java.lang.String.class,"Pagamento",Pagamento.class);
		this.IMPORTO_PAGATO = new Field("importoPagato",double.class,"Pagamento",Pagamento.class);
		this.DATA_ACQUISIZIONE = new Field("dataAcquisizione",java.util.Date.class,"Pagamento",Pagamento.class);
		this.IUR = new Field("iur",java.lang.String.class,"Pagamento",Pagamento.class);
		this.DATA_PAGAMENTO = new Field("dataPagamento",java.util.Date.class,"Pagamento",Pagamento.class);
		this.IBAN_ACCREDITO = new Field("ibanAccredito",java.lang.String.class,"Pagamento",Pagamento.class);
		this.COMMISSIONI_PSP = new Field("commissioniPsp",java.lang.Double.class,"Pagamento",Pagamento.class);
		this.TIPO_ALLEGATO = new Field("tipoAllegato",java.lang.String.class,"Pagamento",Pagamento.class);
		this.ALLEGATO = new Field("allegato",byte[].class,"Pagamento",Pagamento.class);
		this.ID_FR_APPLICAZIONE = new it.govpay.orm.model.IdFrApplicazioneModel(new Field("idFrApplicazione",it.govpay.orm.IdFrApplicazione.class,"Pagamento",Pagamento.class));
		this.RENDICONTAZIONE_ESITO = new Field("rendicontazioneEsito",java.lang.Integer.class,"Pagamento",Pagamento.class);
		this.RENDICONTAZIONE_DATA = new Field("rendicontazioneData",java.util.Date.class,"Pagamento",Pagamento.class);
		this.CODFLUSSO_RENDICONTAZIONE = new Field("codflussoRendicontazione",java.lang.String.class,"Pagamento",Pagamento.class);
		this.ANNO_RIFERIMENTO = new Field("annoRiferimento",java.lang.Integer.class,"Pagamento",Pagamento.class);
		this.INDICE_SINGOLO_PAGAMENTO = new Field("indiceSingoloPagamento",java.lang.Integer.class,"Pagamento",Pagamento.class);
		this.ID_RR = new it.govpay.orm.model.IdRrModel(new Field("idRr",it.govpay.orm.IdRr.class,"Pagamento",Pagamento.class));
		this.DATA_ACQUISIZIONE_REVOCA = new Field("dataAcquisizioneRevoca",java.util.Date.class,"Pagamento",Pagamento.class);
		this.CAUSALE_REVOCA = new Field("causaleRevoca",java.lang.String.class,"Pagamento",Pagamento.class);
		this.DATI_REVOCA = new Field("datiRevoca",java.lang.String.class,"Pagamento",Pagamento.class);
		this.IMPORTO_REVOCATO = new Field("importoRevocato",java.lang.Double.class,"Pagamento",Pagamento.class);
		this.ESITO_REVOCA = new Field("esitoRevoca",java.lang.String.class,"Pagamento",Pagamento.class);
		this.DATI_ESITO_REVOCA = new Field("datiEsitoRevoca",java.lang.String.class,"Pagamento",Pagamento.class);
		this.ID_FR_APPLICAZIONE_REVOCA = new it.govpay.orm.model.IdFrApplicazioneModel(new Field("idFrApplicazioneRevoca",it.govpay.orm.IdFrApplicazione.class,"Pagamento",Pagamento.class));
		this.RENDICONTAZIONE_ESITO_REVOCA = new Field("rendicontazioneEsitoRevoca",java.lang.Integer.class,"Pagamento",Pagamento.class);
		this.RENDICONTAZIONE_DATA_REVOCA = new Field("rendicontazioneDataRevoca",java.util.Date.class,"Pagamento",Pagamento.class);
		this.COD_FLUSSO_RENDICONTAZIONE_REVOCA = new Field("codFlussoRendicontazioneRevoca",java.lang.String.class,"Pagamento",Pagamento.class);
		this.ANNO_RIFERIMENTO_REVOCA = new Field("annoRiferimentoRevoca",java.lang.Integer.class,"Pagamento",Pagamento.class);
		this.INDICE_SINGOLO_PAGAMENTO_REVOCA = new Field("indiceSingoloPagamentoRevoca",java.lang.Integer.class,"Pagamento",Pagamento.class);
	
	}
	
	public PagamentoModel(IField father){
	
		super(father);
	
		this.ID_RPT = new it.govpay.orm.model.IdRptModel(new ComplexField(father,"idRPT",it.govpay.orm.IdRpt.class,"Pagamento",Pagamento.class));
		this.ID_SINGOLO_VERSAMENTO = new it.govpay.orm.model.IdSingoloVersamentoModel(new ComplexField(father,"idSingoloVersamento",it.govpay.orm.IdSingoloVersamento.class,"Pagamento",Pagamento.class));
		this.COD_SINGOLO_VERSAMENTO_ENTE = new ComplexField(father,"codSingoloVersamentoEnte",java.lang.String.class,"Pagamento",Pagamento.class);
		this.IMPORTO_PAGATO = new ComplexField(father,"importoPagato",double.class,"Pagamento",Pagamento.class);
		this.DATA_ACQUISIZIONE = new ComplexField(father,"dataAcquisizione",java.util.Date.class,"Pagamento",Pagamento.class);
		this.IUR = new ComplexField(father,"iur",java.lang.String.class,"Pagamento",Pagamento.class);
		this.DATA_PAGAMENTO = new ComplexField(father,"dataPagamento",java.util.Date.class,"Pagamento",Pagamento.class);
		this.IBAN_ACCREDITO = new ComplexField(father,"ibanAccredito",java.lang.String.class,"Pagamento",Pagamento.class);
		this.COMMISSIONI_PSP = new ComplexField(father,"commissioniPsp",java.lang.Double.class,"Pagamento",Pagamento.class);
		this.TIPO_ALLEGATO = new ComplexField(father,"tipoAllegato",java.lang.String.class,"Pagamento",Pagamento.class);
		this.ALLEGATO = new ComplexField(father,"allegato",byte[].class,"Pagamento",Pagamento.class);
		this.ID_FR_APPLICAZIONE = new it.govpay.orm.model.IdFrApplicazioneModel(new ComplexField(father,"idFrApplicazione",it.govpay.orm.IdFrApplicazione.class,"Pagamento",Pagamento.class));
		this.RENDICONTAZIONE_ESITO = new ComplexField(father,"rendicontazioneEsito",java.lang.Integer.class,"Pagamento",Pagamento.class);
		this.RENDICONTAZIONE_DATA = new ComplexField(father,"rendicontazioneData",java.util.Date.class,"Pagamento",Pagamento.class);
		this.CODFLUSSO_RENDICONTAZIONE = new ComplexField(father,"codflussoRendicontazione",java.lang.String.class,"Pagamento",Pagamento.class);
		this.ANNO_RIFERIMENTO = new ComplexField(father,"annoRiferimento",java.lang.Integer.class,"Pagamento",Pagamento.class);
		this.INDICE_SINGOLO_PAGAMENTO = new ComplexField(father,"indiceSingoloPagamento",java.lang.Integer.class,"Pagamento",Pagamento.class);
		this.ID_RR = new it.govpay.orm.model.IdRrModel(new ComplexField(father,"idRr",it.govpay.orm.IdRr.class,"Pagamento",Pagamento.class));
		this.DATA_ACQUISIZIONE_REVOCA = new ComplexField(father,"dataAcquisizioneRevoca",java.util.Date.class,"Pagamento",Pagamento.class);
		this.CAUSALE_REVOCA = new ComplexField(father,"causaleRevoca",java.lang.String.class,"Pagamento",Pagamento.class);
		this.DATI_REVOCA = new ComplexField(father,"datiRevoca",java.lang.String.class,"Pagamento",Pagamento.class);
		this.IMPORTO_REVOCATO = new ComplexField(father,"importoRevocato",java.lang.Double.class,"Pagamento",Pagamento.class);
		this.ESITO_REVOCA = new ComplexField(father,"esitoRevoca",java.lang.String.class,"Pagamento",Pagamento.class);
		this.DATI_ESITO_REVOCA = new ComplexField(father,"datiEsitoRevoca",java.lang.String.class,"Pagamento",Pagamento.class);
		this.ID_FR_APPLICAZIONE_REVOCA = new it.govpay.orm.model.IdFrApplicazioneModel(new ComplexField(father,"idFrApplicazioneRevoca",it.govpay.orm.IdFrApplicazione.class,"Pagamento",Pagamento.class));
		this.RENDICONTAZIONE_ESITO_REVOCA = new ComplexField(father,"rendicontazioneEsitoRevoca",java.lang.Integer.class,"Pagamento",Pagamento.class);
		this.RENDICONTAZIONE_DATA_REVOCA = new ComplexField(father,"rendicontazioneDataRevoca",java.util.Date.class,"Pagamento",Pagamento.class);
		this.COD_FLUSSO_RENDICONTAZIONE_REVOCA = new ComplexField(father,"codFlussoRendicontazioneRevoca",java.lang.String.class,"Pagamento",Pagamento.class);
		this.ANNO_RIFERIMENTO_REVOCA = new ComplexField(father,"annoRiferimentoRevoca",java.lang.Integer.class,"Pagamento",Pagamento.class);
		this.INDICE_SINGOLO_PAGAMENTO_REVOCA = new ComplexField(father,"indiceSingoloPagamentoRevoca",java.lang.Integer.class,"Pagamento",Pagamento.class);
	
	}
	
	

	public it.govpay.orm.model.IdRptModel ID_RPT = null;
	 
	public it.govpay.orm.model.IdSingoloVersamentoModel ID_SINGOLO_VERSAMENTO = null;
	 
	public IField COD_SINGOLO_VERSAMENTO_ENTE = null;
	 
	public IField IMPORTO_PAGATO = null;
	 
	public IField DATA_ACQUISIZIONE = null;
	 
	public IField IUR = null;
	 
	public IField DATA_PAGAMENTO = null;
	 
	public IField IBAN_ACCREDITO = null;
	 
	public IField COMMISSIONI_PSP = null;
	 
	public IField TIPO_ALLEGATO = null;
	 
	public IField ALLEGATO = null;
	 
	public it.govpay.orm.model.IdFrApplicazioneModel ID_FR_APPLICAZIONE = null;
	 
	public IField RENDICONTAZIONE_ESITO = null;
	 
	public IField RENDICONTAZIONE_DATA = null;
	 
	public IField CODFLUSSO_RENDICONTAZIONE = null;
	 
	public IField ANNO_RIFERIMENTO = null;
	 
	public IField INDICE_SINGOLO_PAGAMENTO = null;
	 
	public it.govpay.orm.model.IdRrModel ID_RR = null;
	 
	public IField DATA_ACQUISIZIONE_REVOCA = null;
	 
	public IField CAUSALE_REVOCA = null;
	 
	public IField DATI_REVOCA = null;
	 
	public IField IMPORTO_REVOCATO = null;
	 
	public IField ESITO_REVOCA = null;
	 
	public IField DATI_ESITO_REVOCA = null;
	 
	public it.govpay.orm.model.IdFrApplicazioneModel ID_FR_APPLICAZIONE_REVOCA = null;
	 
	public IField RENDICONTAZIONE_ESITO_REVOCA = null;
	 
	public IField RENDICONTAZIONE_DATA_REVOCA = null;
	 
	public IField COD_FLUSSO_RENDICONTAZIONE_REVOCA = null;
	 
	public IField ANNO_RIFERIMENTO_REVOCA = null;
	 
	public IField INDICE_SINGOLO_PAGAMENTO_REVOCA = null;
	 

	@Override
	public Class<Pagamento> getModeledClass(){
		return Pagamento.class;
	}
	
	@Override
	public String toString(){
		if(this.getModeledClass()!=null){
			return this.getModeledClass().getName();
		}else{
			return "N.D.";
		}
	}

}