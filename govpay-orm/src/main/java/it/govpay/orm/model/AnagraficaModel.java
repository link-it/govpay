/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
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

import it.govpay.orm.Anagrafica;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Anagrafica 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class AnagraficaModel extends AbstractModel<Anagrafica> {

	public AnagraficaModel(){
	
		super();
	
		this.RAGIONE_SOCIALE = new Field("ragioneSociale",java.lang.String.class,"Anagrafica",Anagrafica.class);
		this.COD_UNIVOCO = new Field("codUnivoco",java.lang.String.class,"Anagrafica",Anagrafica.class);
		this.INDIRIZZO = new Field("indirizzo",java.lang.String.class,"Anagrafica",Anagrafica.class);
		this.CIVICO = new Field("civico",java.lang.String.class,"Anagrafica",Anagrafica.class);
		this.CAP = new Field("cap",java.lang.String.class,"Anagrafica",Anagrafica.class);
		this.LOCALITA = new Field("localita",java.lang.String.class,"Anagrafica",Anagrafica.class);
		this.PROVINCIA = new Field("provincia",java.lang.String.class,"Anagrafica",Anagrafica.class);
		this.NAZIONE = new Field("nazione",java.lang.String.class,"Anagrafica",Anagrafica.class);
		this.EMAIL = new Field("email",java.lang.String.class,"Anagrafica",Anagrafica.class);
		this.TELEFONO = new Field("telefono",java.lang.String.class,"Anagrafica",Anagrafica.class);
		this.CELLULARE = new Field("cellulare",java.lang.String.class,"Anagrafica",Anagrafica.class);
		this.FAX = new Field("fax",java.lang.String.class,"Anagrafica",Anagrafica.class);
	
	}
	
	public AnagraficaModel(IField father){
	
		super(father);
	
		this.RAGIONE_SOCIALE = new ComplexField(father,"ragioneSociale",java.lang.String.class,"Anagrafica",Anagrafica.class);
		this.COD_UNIVOCO = new ComplexField(father,"codUnivoco",java.lang.String.class,"Anagrafica",Anagrafica.class);
		this.INDIRIZZO = new ComplexField(father,"indirizzo",java.lang.String.class,"Anagrafica",Anagrafica.class);
		this.CIVICO = new ComplexField(father,"civico",java.lang.String.class,"Anagrafica",Anagrafica.class);
		this.CAP = new ComplexField(father,"cap",java.lang.String.class,"Anagrafica",Anagrafica.class);
		this.LOCALITA = new ComplexField(father,"localita",java.lang.String.class,"Anagrafica",Anagrafica.class);
		this.PROVINCIA = new ComplexField(father,"provincia",java.lang.String.class,"Anagrafica",Anagrafica.class);
		this.NAZIONE = new ComplexField(father,"nazione",java.lang.String.class,"Anagrafica",Anagrafica.class);
		this.EMAIL = new ComplexField(father,"email",java.lang.String.class,"Anagrafica",Anagrafica.class);
		this.TELEFONO = new ComplexField(father,"telefono",java.lang.String.class,"Anagrafica",Anagrafica.class);
		this.CELLULARE = new ComplexField(father,"cellulare",java.lang.String.class,"Anagrafica",Anagrafica.class);
		this.FAX = new ComplexField(father,"fax",java.lang.String.class,"Anagrafica",Anagrafica.class);
	
	}
	
	

	public IField RAGIONE_SOCIALE = null;
	 
	public IField COD_UNIVOCO = null;
	 
	public IField INDIRIZZO = null;
	 
	public IField CIVICO = null;
	 
	public IField CAP = null;
	 
	public IField LOCALITA = null;
	 
	public IField PROVINCIA = null;
	 
	public IField NAZIONE = null;
	 
	public IField EMAIL = null;
	 
	public IField TELEFONO = null;
	 
	public IField CELLULARE = null;
	 
	public IField FAX = null;
	 

	@Override
	public Class<Anagrafica> getModeledClass(){
		return Anagrafica.class;
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