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
		this.COD_DOMINIO = new Field("codDominio",java.lang.String.class,"Pagamento",Pagamento.class);
		this.IUV = new Field("iuv",java.lang.String.class,"Pagamento",Pagamento.class);
		this.INDICE_DATI = new Field("indiceDati",int.class,"Pagamento",Pagamento.class);
		this.IMPORTO_PAGATO = new Field("importoPagato",double.class,"Pagamento",Pagamento.class);
		this.DATA_ACQUISIZIONE = new Field("dataAcquisizione",java.util.Date.class,"Pagamento",Pagamento.class);
		this.IUR = new Field("iur",java.lang.String.class,"Pagamento",Pagamento.class);
		this.DATA_PAGAMENTO = new Field("dataPagamento",java.util.Date.class,"Pagamento",Pagamento.class);
		this.COMMISSIONI_PSP = new Field("commissioniPsp",java.lang.Double.class,"Pagamento",Pagamento.class);
		this.TIPO_ALLEGATO = new Field("tipoAllegato",java.lang.String.class,"Pagamento",Pagamento.class);
		this.ALLEGATO = new Field("allegato",byte[].class,"Pagamento",Pagamento.class);
		this.ID_RR = new it.govpay.orm.model.IdRrModel(new Field("idRr",it.govpay.orm.IdRr.class,"Pagamento",Pagamento.class));
		this.DATA_ACQUISIZIONE_REVOCA = new Field("dataAcquisizioneRevoca",java.util.Date.class,"Pagamento",Pagamento.class);
		this.CAUSALE_REVOCA = new Field("causaleRevoca",java.lang.String.class,"Pagamento",Pagamento.class);
		this.DATI_REVOCA = new Field("datiRevoca",java.lang.String.class,"Pagamento",Pagamento.class);
		this.IMPORTO_REVOCATO = new Field("importoRevocato",java.lang.Double.class,"Pagamento",Pagamento.class);
		this.ESITO_REVOCA = new Field("esitoRevoca",java.lang.String.class,"Pagamento",Pagamento.class);
		this.DATI_ESITO_REVOCA = new Field("datiEsitoRevoca",java.lang.String.class,"Pagamento",Pagamento.class);
		this.STATO = new Field("stato",java.lang.String.class,"Pagamento",Pagamento.class);
		this.ID_INCASSO = new it.govpay.orm.model.IdIncassoModel(new Field("idIncasso",it.govpay.orm.IdIncasso.class,"Pagamento",Pagamento.class));
		this.TIPO = new Field("tipo",java.lang.String.class,"Pagamento",Pagamento.class);
	
	}
	
	public PagamentoModel(IField father){
	
		super(father);
	
		this.ID_RPT = new it.govpay.orm.model.IdRptModel(new ComplexField(father,"idRPT",it.govpay.orm.IdRpt.class,"Pagamento",Pagamento.class));
		this.ID_SINGOLO_VERSAMENTO = new it.govpay.orm.model.IdSingoloVersamentoModel(new ComplexField(father,"idSingoloVersamento",it.govpay.orm.IdSingoloVersamento.class,"Pagamento",Pagamento.class));
		this.COD_DOMINIO = new ComplexField(father,"codDominio",java.lang.String.class,"Pagamento",Pagamento.class);
		this.IUV = new ComplexField(father,"iuv",java.lang.String.class,"Pagamento",Pagamento.class);
		this.INDICE_DATI = new ComplexField(father,"indiceDati",int.class,"Pagamento",Pagamento.class);
		this.IMPORTO_PAGATO = new ComplexField(father,"importoPagato",double.class,"Pagamento",Pagamento.class);
		this.DATA_ACQUISIZIONE = new ComplexField(father,"dataAcquisizione",java.util.Date.class,"Pagamento",Pagamento.class);
		this.IUR = new ComplexField(father,"iur",java.lang.String.class,"Pagamento",Pagamento.class);
		this.DATA_PAGAMENTO = new ComplexField(father,"dataPagamento",java.util.Date.class,"Pagamento",Pagamento.class);
		this.COMMISSIONI_PSP = new ComplexField(father,"commissioniPsp",java.lang.Double.class,"Pagamento",Pagamento.class);
		this.TIPO_ALLEGATO = new ComplexField(father,"tipoAllegato",java.lang.String.class,"Pagamento",Pagamento.class);
		this.ALLEGATO = new ComplexField(father,"allegato",byte[].class,"Pagamento",Pagamento.class);
		this.ID_RR = new it.govpay.orm.model.IdRrModel(new ComplexField(father,"idRr",it.govpay.orm.IdRr.class,"Pagamento",Pagamento.class));
		this.DATA_ACQUISIZIONE_REVOCA = new ComplexField(father,"dataAcquisizioneRevoca",java.util.Date.class,"Pagamento",Pagamento.class);
		this.CAUSALE_REVOCA = new ComplexField(father,"causaleRevoca",java.lang.String.class,"Pagamento",Pagamento.class);
		this.DATI_REVOCA = new ComplexField(father,"datiRevoca",java.lang.String.class,"Pagamento",Pagamento.class);
		this.IMPORTO_REVOCATO = new ComplexField(father,"importoRevocato",java.lang.Double.class,"Pagamento",Pagamento.class);
		this.ESITO_REVOCA = new ComplexField(father,"esitoRevoca",java.lang.String.class,"Pagamento",Pagamento.class);
		this.DATI_ESITO_REVOCA = new ComplexField(father,"datiEsitoRevoca",java.lang.String.class,"Pagamento",Pagamento.class);
		this.STATO = new ComplexField(father,"stato",java.lang.String.class,"Pagamento",Pagamento.class);
		this.ID_INCASSO = new it.govpay.orm.model.IdIncassoModel(new ComplexField(father,"idIncasso",it.govpay.orm.IdIncasso.class,"Pagamento",Pagamento.class));
		this.TIPO = new ComplexField(father,"tipo",java.lang.String.class,"Pagamento",Pagamento.class);
	
	}
	
	

	public it.govpay.orm.model.IdRptModel ID_RPT = null;
	 
	public it.govpay.orm.model.IdSingoloVersamentoModel ID_SINGOLO_VERSAMENTO = null;
	 
	public IField COD_DOMINIO = null;
	 
	public IField IUV = null;
	 
	public IField INDICE_DATI = null;
	 
	public IField IMPORTO_PAGATO = null;
	 
	public IField DATA_ACQUISIZIONE = null;
	 
	public IField IUR = null;
	 
	public IField DATA_PAGAMENTO = null;
	 
	public IField COMMISSIONI_PSP = null;
	 
	public IField TIPO_ALLEGATO = null;
	 
	public IField ALLEGATO = null;
	 
	public it.govpay.orm.model.IdRrModel ID_RR = null;
	 
	public IField DATA_ACQUISIZIONE_REVOCA = null;
	 
	public IField CAUSALE_REVOCA = null;
	 
	public IField DATI_REVOCA = null;
	 
	public IField IMPORTO_REVOCATO = null;
	 
	public IField ESITO_REVOCA = null;
	 
	public IField DATI_ESITO_REVOCA = null;
	 
	public IField STATO = null;
	 
	public it.govpay.orm.model.IdIncassoModel ID_INCASSO = null;
	 
	public IField TIPO = null;
	 

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