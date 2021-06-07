/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
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

import it.govpay.orm.VistaRiscossioni;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model VistaRiscossioni 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VistaRiscossioniModel extends AbstractModel<VistaRiscossioni> {

	public VistaRiscossioniModel(){
	
		super();
	
		this.COD_DOMINIO = new Field("codDominio",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.IUV = new Field("iuv",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.IUR = new Field("iur",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.COD_FLUSSO = new Field("cod_flusso",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.FR_IUR = new Field("fr_iur",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.DATA_REGOLAMENTO = new Field("dataRegolamento",java.util.Date.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.NUMERO_PAGAMENTI = new Field("numeroPagamenti",long.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.IMPORTO_TOTALE_PAGAMENTI = new Field("importoTotalePagamenti",java.lang.Double.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.IMPORTO_PAGATO = new Field("importoPagato",java.lang.Double.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.COD_SINGOLO_VERSAMENTO_ENTE = new Field("codSingoloVersamentoEnte",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.INDICE_DATI = new Field("indiceDati",java.lang.Integer.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.COD_VERSAMENTO_ENTE = new Field("codVersamentoEnte",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.COD_APPLICAZIONE = new Field("codApplicazione",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.DATA_PAGAMENTO = new Field("dataPagamento",java.util.Date.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.COD_TIPO_VERSAMENTO = new Field("codTipoVersamento",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.COD_ENTRATA = new Field("codEntrata",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.IDENTIFICATIVO_DEBITORE = new Field("identificativoDebitore",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.DEBITORE_ANAGRAFICA = new Field("debitoreAnagrafica",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.ANNO = new Field("anno",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.DESCR_TIPO_VERSAMENTO = new Field("descrTipoVersamento",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.COD_PSP = new Field("codPsp",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.RAGIONE_SOCIALE_PSP = new Field("ragioneSocialePsp",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.COD_RATA = new Field("codRata",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.ID_DOCUMENTO = new it.govpay.orm.model.IdDocumentoModel(new Field("idDocumento",it.govpay.orm.IdDocumento.class,"VistaRiscossioni",VistaRiscossioni.class));
		this.CAUSALE_VERSAMENTO = new Field("causaleVersamento",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.IMPORTO_VERSAMENTO = new Field("importoVersamento",double.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.NUMERO_AVVISO = new Field("numeroAvviso",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.IUV_PAGAMENTO = new Field("iuvPagamento",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.DATA_CREAZIONE = new Field("dataCreazione",java.util.Date.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.DATA_SCADENZA = new Field("dataScadenza",java.util.Date.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.CONTABILITA = new Field("contabilita",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
	
	}
	
	public VistaRiscossioniModel(IField father){
	
		super(father);
	
		this.COD_DOMINIO = new ComplexField(father,"codDominio",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.IUV = new ComplexField(father,"iuv",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.IUR = new ComplexField(father,"iur",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.COD_FLUSSO = new ComplexField(father,"cod_flusso",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.FR_IUR = new ComplexField(father,"fr_iur",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.DATA_REGOLAMENTO = new ComplexField(father,"dataRegolamento",java.util.Date.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.NUMERO_PAGAMENTI = new ComplexField(father,"numeroPagamenti",long.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.IMPORTO_TOTALE_PAGAMENTI = new ComplexField(father,"importoTotalePagamenti",java.lang.Double.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.IMPORTO_PAGATO = new ComplexField(father,"importoPagato",java.lang.Double.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.COD_SINGOLO_VERSAMENTO_ENTE = new ComplexField(father,"codSingoloVersamentoEnte",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.INDICE_DATI = new ComplexField(father,"indiceDati",java.lang.Integer.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.COD_VERSAMENTO_ENTE = new ComplexField(father,"codVersamentoEnte",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.COD_APPLICAZIONE = new ComplexField(father,"codApplicazione",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.DATA_PAGAMENTO = new ComplexField(father,"dataPagamento",java.util.Date.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.COD_TIPO_VERSAMENTO = new ComplexField(father,"codTipoVersamento",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.COD_ENTRATA = new ComplexField(father,"codEntrata",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.IDENTIFICATIVO_DEBITORE = new ComplexField(father,"identificativoDebitore",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.DEBITORE_ANAGRAFICA = new ComplexField(father,"debitoreAnagrafica",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.ANNO = new ComplexField(father,"anno",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.DESCR_TIPO_VERSAMENTO = new ComplexField(father,"descrTipoVersamento",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.COD_PSP = new ComplexField(father,"codPsp",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.RAGIONE_SOCIALE_PSP = new ComplexField(father,"ragioneSocialePsp",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.COD_RATA = new ComplexField(father,"codRata",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.ID_DOCUMENTO = new it.govpay.orm.model.IdDocumentoModel(new ComplexField(father,"idDocumento",it.govpay.orm.IdDocumento.class,"VistaRiscossioni",VistaRiscossioni.class));
		this.CAUSALE_VERSAMENTO = new ComplexField(father,"causaleVersamento",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.IMPORTO_VERSAMENTO = new ComplexField(father,"importoVersamento",double.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.NUMERO_AVVISO = new ComplexField(father,"numeroAvviso",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.IUV_PAGAMENTO = new ComplexField(father,"iuvPagamento",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.DATA_CREAZIONE = new ComplexField(father,"dataCreazione",java.util.Date.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.DATA_SCADENZA = new ComplexField(father,"dataScadenza",java.util.Date.class,"VistaRiscossioni",VistaRiscossioni.class);
		this.CONTABILITA = new ComplexField(father,"contabilita",java.lang.String.class,"VistaRiscossioni",VistaRiscossioni.class);
	
	}
	
	

	public IField COD_DOMINIO = null;
	 
	public IField IUV = null;
	 
	public IField IUR = null;
	 
	public IField COD_FLUSSO = null;
	 
	public IField FR_IUR = null;
	 
	public IField DATA_REGOLAMENTO = null;
	 
	public IField NUMERO_PAGAMENTI = null;
	 
	public IField IMPORTO_TOTALE_PAGAMENTI = null;
	 
	public IField IMPORTO_PAGATO = null;
	 
	public IField COD_SINGOLO_VERSAMENTO_ENTE = null;
	 
	public IField INDICE_DATI = null;
	 
	public IField COD_VERSAMENTO_ENTE = null;
	 
	public IField COD_APPLICAZIONE = null;
	 
	public IField DATA_PAGAMENTO = null;
	 
	public IField COD_TIPO_VERSAMENTO = null;
	 
	public IField COD_ENTRATA = null;
	 
	public IField IDENTIFICATIVO_DEBITORE = null;
	 
	public IField DEBITORE_ANAGRAFICA = null;
	 
	public IField ANNO = null;
	 
	public IField DESCR_TIPO_VERSAMENTO = null;
	 
	public IField COD_PSP = null;
	 
	public IField RAGIONE_SOCIALE_PSP = null;
	 
	public IField COD_RATA = null;
	 
	public it.govpay.orm.model.IdDocumentoModel ID_DOCUMENTO = null;
	 
	public IField CAUSALE_VERSAMENTO = null;
	 
	public IField IMPORTO_VERSAMENTO = null;
	 
	public IField NUMERO_AVVISO = null;
	 
	public IField IUV_PAGAMENTO = null;
	 
	public IField DATA_CREAZIONE = null;
	 
	public IField DATA_SCADENZA = null;
	 
	public IField CONTABILITA = null;
	 

	@Override
	public Class<VistaRiscossioni> getModeledClass(){
		return VistaRiscossioni.class;
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
