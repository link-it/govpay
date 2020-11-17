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

import it.govpay.orm.IdFr;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model IdFr 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class IdFrModel extends AbstractModel<IdFr> {

	public IdFrModel(){
	
		super();
	
		this.COD_FLUSSO = new Field("codFlusso",java.lang.String.class,"id-fr",IdFr.class);
		this.COD_DOMINIO = new Field("codDominio",java.lang.String.class,"id-fr",IdFr.class);
		this.DATA_ORA_FLUSSO = new Field("dataOraFlusso",java.util.Date.class,"id-fr",IdFr.class);
		this.COD_PSP = new Field("codPsp",java.lang.String.class,"id-fr",IdFr.class);
		this.STATO = new Field("stato",java.lang.String.class,"id-fr",IdFr.class);
		this.DESCRIZIONE_STATO = new Field("descrizioneStato",java.lang.String.class,"id-fr",IdFr.class);
		this.IUR = new Field("iur",java.lang.String.class,"id-fr",IdFr.class);
		this.DATA_REGOLAMENTO = new Field("dataRegolamento",java.util.Date.class,"id-fr",IdFr.class);
		this.DATA_ACQUISIZIONE = new Field("dataAcquisizione",java.util.Date.class,"id-fr",IdFr.class);
		this.NUMERO_PAGAMENTI = new Field("numeroPagamenti",long.class,"id-fr",IdFr.class);
		this.IMPORTO_TOTALE_PAGAMENTI = new Field("importoTotalePagamenti",java.lang.Double.class,"id-fr",IdFr.class);
		this.COD_BIC_RIVERSAMENTO = new Field("codBicRiversamento",java.lang.String.class,"id-fr",IdFr.class);
		this.RAGIONE_SOCIALE_PSP = new Field("ragioneSocialePsp",java.lang.String.class,"id-fr",IdFr.class);
		this.RAGIONE_SOCIALE_DOMINIO = new Field("ragioneSocialeDominio",java.lang.String.class,"id-fr",IdFr.class);
		this.OBSOLETO = new Field("obsoleto",Boolean.class,"id-fr",IdFr.class);
	
	}
	
	public IdFrModel(IField father){
	
		super(father);
	
		this.COD_FLUSSO = new ComplexField(father,"codFlusso",java.lang.String.class,"id-fr",IdFr.class);
		this.COD_DOMINIO = new ComplexField(father,"codDominio",java.lang.String.class,"id-fr",IdFr.class);
		this.DATA_ORA_FLUSSO = new ComplexField(father,"dataOraFlusso",java.util.Date.class,"id-fr",IdFr.class);
		this.COD_PSP = new ComplexField(father,"codPsp",java.lang.String.class,"id-fr",IdFr.class);
		this.STATO = new ComplexField(father,"stato",java.lang.String.class,"id-fr",IdFr.class);
		this.DESCRIZIONE_STATO = new ComplexField(father,"descrizioneStato",java.lang.String.class,"id-fr",IdFr.class);
		this.IUR = new ComplexField(father,"iur",java.lang.String.class,"id-fr",IdFr.class);
		this.DATA_REGOLAMENTO = new ComplexField(father,"dataRegolamento",java.util.Date.class,"id-fr",IdFr.class);
		this.DATA_ACQUISIZIONE = new ComplexField(father,"dataAcquisizione",java.util.Date.class,"id-fr",IdFr.class);
		this.NUMERO_PAGAMENTI = new ComplexField(father,"numeroPagamenti",long.class,"id-fr",IdFr.class);
		this.IMPORTO_TOTALE_PAGAMENTI = new ComplexField(father,"importoTotalePagamenti",java.lang.Double.class,"id-fr",IdFr.class);
		this.COD_BIC_RIVERSAMENTO = new ComplexField(father,"codBicRiversamento",java.lang.String.class,"id-fr",IdFr.class);
		this.RAGIONE_SOCIALE_PSP = new ComplexField(father,"ragioneSocialePsp",java.lang.String.class,"id-fr",IdFr.class);
		this.RAGIONE_SOCIALE_DOMINIO = new ComplexField(father,"ragioneSocialeDominio",java.lang.String.class,"id-fr",IdFr.class);
		this.OBSOLETO = new ComplexField(father,"obsoleto",Boolean.class,"id-fr",IdFr.class);
	
	}
	
	

	public IField COD_FLUSSO = null;
	 
	public IField COD_DOMINIO = null;
	 
	public IField DATA_ORA_FLUSSO = null;
	 
	public IField COD_PSP = null;
	 
	public IField STATO = null;
	 
	public IField DESCRIZIONE_STATO = null;
	 
	public IField IUR = null;
	 
	public IField DATA_REGOLAMENTO = null;
	 
	public IField DATA_ACQUISIZIONE = null;
	 
	public IField NUMERO_PAGAMENTI = null;
	 
	public IField IMPORTO_TOTALE_PAGAMENTI = null;
	 
	public IField COD_BIC_RIVERSAMENTO = null;
	 
	public IField RAGIONE_SOCIALE_PSP = null;
	 
	public IField RAGIONE_SOCIALE_DOMINIO = null;
	 
	public IField OBSOLETO = null;
	 

	@Override
	public Class<IdFr> getModeledClass(){
		return IdFr.class;
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
