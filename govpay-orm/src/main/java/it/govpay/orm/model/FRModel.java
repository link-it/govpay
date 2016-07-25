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

import it.govpay.orm.FR;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model FR 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class FRModel extends AbstractModel<FR> {

	public FRModel(){
	
		super();
	
		this.ID_PSP = new it.govpay.orm.model.IdPspModel(new Field("idPsp",it.govpay.orm.IdPsp.class,"FR",FR.class));
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new Field("idDominio",it.govpay.orm.IdDominio.class,"FR",FR.class));
		this.COD_FLUSSO = new Field("codFlusso",java.lang.String.class,"FR",FR.class);
		this.STATO = new Field("stato",java.lang.String.class,"FR",FR.class);
		this.DESCRIZIONE_STATO = new Field("descrizioneStato",java.lang.String.class,"FR",FR.class);
		this.IUR = new Field("iur",java.lang.String.class,"FR",FR.class);
		this.ANNO_RIFERIMENTO = new Field("annoRiferimento",int.class,"FR",FR.class);
		this.DATA_ORA_FLUSSO = new Field("dataOraFlusso",java.util.Date.class,"FR",FR.class);
		this.DATA_REGOLAMENTO = new Field("dataRegolamento",java.util.Date.class,"FR",FR.class);
		this.DATA_ACQUISIZIONE = new Field("dataAcquisizione",java.util.Date.class,"FR",FR.class);
		this.NUMERO_PAGAMENTI = new Field("numeroPagamenti",long.class,"FR",FR.class);
		this.IMPORTO_TOTALE_PAGAMENTI = new Field("importoTotalePagamenti",java.lang.Double.class,"FR",FR.class);
		this.COD_BIC_RIVERSAMENTO = new Field("codBicRiversamento",java.lang.String.class,"FR",FR.class);
		this.XML = new Field("xml",byte[].class,"FR",FR.class);
	
	}
	
	public FRModel(IField father){
	
		super(father);
	
		this.ID_PSP = new it.govpay.orm.model.IdPspModel(new ComplexField(father,"idPsp",it.govpay.orm.IdPsp.class,"FR",FR.class));
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new ComplexField(father,"idDominio",it.govpay.orm.IdDominio.class,"FR",FR.class));
		this.COD_FLUSSO = new ComplexField(father,"codFlusso",java.lang.String.class,"FR",FR.class);
		this.STATO = new ComplexField(father,"stato",java.lang.String.class,"FR",FR.class);
		this.DESCRIZIONE_STATO = new ComplexField(father,"descrizioneStato",java.lang.String.class,"FR",FR.class);
		this.IUR = new ComplexField(father,"iur",java.lang.String.class,"FR",FR.class);
		this.ANNO_RIFERIMENTO = new ComplexField(father,"annoRiferimento",int.class,"FR",FR.class);
		this.DATA_ORA_FLUSSO = new ComplexField(father,"dataOraFlusso",java.util.Date.class,"FR",FR.class);
		this.DATA_REGOLAMENTO = new ComplexField(father,"dataRegolamento",java.util.Date.class,"FR",FR.class);
		this.DATA_ACQUISIZIONE = new ComplexField(father,"dataAcquisizione",java.util.Date.class,"FR",FR.class);
		this.NUMERO_PAGAMENTI = new ComplexField(father,"numeroPagamenti",long.class,"FR",FR.class);
		this.IMPORTO_TOTALE_PAGAMENTI = new ComplexField(father,"importoTotalePagamenti",java.lang.Double.class,"FR",FR.class);
		this.COD_BIC_RIVERSAMENTO = new ComplexField(father,"codBicRiversamento",java.lang.String.class,"FR",FR.class);
		this.XML = new ComplexField(father,"xml",byte[].class,"FR",FR.class);
	
	}
	
	

	public it.govpay.orm.model.IdPspModel ID_PSP = null;
	 
	public it.govpay.orm.model.IdDominioModel ID_DOMINIO = null;
	 
	public IField COD_FLUSSO = null;
	 
	public IField STATO = null;
	 
	public IField DESCRIZIONE_STATO = null;
	 
	public IField IUR = null;
	 
	public IField ANNO_RIFERIMENTO = null;
	 
	public IField DATA_ORA_FLUSSO = null;
	 
	public IField DATA_REGOLAMENTO = null;
	 
	public IField DATA_ACQUISIZIONE = null;
	 
	public IField NUMERO_PAGAMENTI = null;
	 
	public IField IMPORTO_TOTALE_PAGAMENTI = null;
	 
	public IField COD_BIC_RIVERSAMENTO = null;
	 
	public IField XML = null;
	 

	@Override
	public Class<FR> getModeledClass(){
		return FR.class;
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