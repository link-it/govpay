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

import it.govpay.orm.VistaRiscossioniConRpt;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model VistaRiscossioniConRpt 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VistaRiscossioniConRptModel extends AbstractModel<VistaRiscossioniConRpt> {

	public VistaRiscossioniConRptModel(){
	
		super();
	
		this.COD_DOMINIO = new Field("codDominio",java.lang.String.class,"VistaRiscossioniConRpt",VistaRiscossioniConRpt.class);
		this.IUV = new Field("iuv",java.lang.String.class,"VistaRiscossioniConRpt",VistaRiscossioniConRpt.class);
		this.IUR = new Field("iur",java.lang.String.class,"VistaRiscossioniConRpt",VistaRiscossioniConRpt.class);
		this.COD_FLUSSO = new Field("cod_flusso",java.lang.String.class,"VistaRiscossioniConRpt",VistaRiscossioniConRpt.class);
		this.FR_IUR = new Field("fr_iur",java.lang.String.class,"VistaRiscossioniConRpt",VistaRiscossioniConRpt.class);
		this.DATA_REGOLAMENTO = new Field("dataRegolamento",java.util.Date.class,"VistaRiscossioniConRpt",VistaRiscossioniConRpt.class);
		this.NUMERO_PAGAMENTI = new Field("numeroPagamenti",long.class,"VistaRiscossioniConRpt",VistaRiscossioniConRpt.class);
		this.IMPORTO_TOTALE_PAGAMENTI = new Field("importoTotalePagamenti",java.lang.Double.class,"VistaRiscossioniConRpt",VistaRiscossioniConRpt.class);
		this.IMPORTO_PAGATO = new Field("importoPagato",java.lang.Double.class,"VistaRiscossioniConRpt",VistaRiscossioniConRpt.class);
		this.DATA = new Field("data",java.util.Date.class,"VistaRiscossioniConRpt",VistaRiscossioniConRpt.class);
		this.COD_SINGOLO_VERSAMENTO_ENTE = new Field("codSingoloVersamentoEnte",java.lang.String.class,"VistaRiscossioniConRpt",VistaRiscossioniConRpt.class);
		this.INDICE_DATI = new Field("indiceDati",java.lang.Integer.class,"VistaRiscossioniConRpt",VistaRiscossioniConRpt.class);
		this.COD_VERSAMENTO_ENTE = new Field("codVersamentoEnte",java.lang.String.class,"VistaRiscossioniConRpt",VistaRiscossioniConRpt.class);
		this.ID_APPLICAZIONE = new Field("idApplicazione",long.class,"VistaRiscossioniConRpt",VistaRiscossioniConRpt.class);
	
	}
	
	public VistaRiscossioniConRptModel(IField father){
	
		super(father);
	
		this.COD_DOMINIO = new ComplexField(father,"codDominio",java.lang.String.class,"VistaRiscossioniConRpt",VistaRiscossioniConRpt.class);
		this.IUV = new ComplexField(father,"iuv",java.lang.String.class,"VistaRiscossioniConRpt",VistaRiscossioniConRpt.class);
		this.IUR = new ComplexField(father,"iur",java.lang.String.class,"VistaRiscossioniConRpt",VistaRiscossioniConRpt.class);
		this.COD_FLUSSO = new ComplexField(father,"cod_flusso",java.lang.String.class,"VistaRiscossioniConRpt",VistaRiscossioniConRpt.class);
		this.FR_IUR = new ComplexField(father,"fr_iur",java.lang.String.class,"VistaRiscossioniConRpt",VistaRiscossioniConRpt.class);
		this.DATA_REGOLAMENTO = new ComplexField(father,"dataRegolamento",java.util.Date.class,"VistaRiscossioniConRpt",VistaRiscossioniConRpt.class);
		this.NUMERO_PAGAMENTI = new ComplexField(father,"numeroPagamenti",long.class,"VistaRiscossioniConRpt",VistaRiscossioniConRpt.class);
		this.IMPORTO_TOTALE_PAGAMENTI = new ComplexField(father,"importoTotalePagamenti",java.lang.Double.class,"VistaRiscossioniConRpt",VistaRiscossioniConRpt.class);
		this.IMPORTO_PAGATO = new ComplexField(father,"importoPagato",java.lang.Double.class,"VistaRiscossioniConRpt",VistaRiscossioniConRpt.class);
		this.DATA = new ComplexField(father,"data",java.util.Date.class,"VistaRiscossioniConRpt",VistaRiscossioniConRpt.class);
		this.COD_SINGOLO_VERSAMENTO_ENTE = new ComplexField(father,"codSingoloVersamentoEnte",java.lang.String.class,"VistaRiscossioniConRpt",VistaRiscossioniConRpt.class);
		this.INDICE_DATI = new ComplexField(father,"indiceDati",java.lang.Integer.class,"VistaRiscossioniConRpt",VistaRiscossioniConRpt.class);
		this.COD_VERSAMENTO_ENTE = new ComplexField(father,"codVersamentoEnte",java.lang.String.class,"VistaRiscossioniConRpt",VistaRiscossioniConRpt.class);
		this.ID_APPLICAZIONE = new ComplexField(father,"idApplicazione",long.class,"VistaRiscossioniConRpt",VistaRiscossioniConRpt.class);
	
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
	 
	public IField DATA = null;
	 
	public IField COD_SINGOLO_VERSAMENTO_ENTE = null;
	 
	public IField INDICE_DATI = null;
	 
	public IField COD_VERSAMENTO_ENTE = null;
	 
	public IField ID_APPLICAZIONE = null;
	 

	@Override
	public Class<VistaRiscossioniConRpt> getModeledClass(){
		return VistaRiscossioniConRpt.class;
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