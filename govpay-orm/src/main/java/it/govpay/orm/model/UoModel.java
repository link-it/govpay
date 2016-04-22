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

import it.govpay.orm.Uo;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Uo 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class UoModel extends AbstractModel<Uo> {

	public UoModel(){
	
		super();
	
		this.COD_UO = new Field("codUo",java.lang.String.class,"Uo",Uo.class);
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new Field("idDominio",it.govpay.orm.IdDominio.class,"Uo",Uo.class));
		this.ABILITATO = new Field("abilitato",boolean.class,"Uo",Uo.class);
		this.UO_CODICE_IDENTIFICATIVO = new Field("uoCodiceIdentificativo",java.lang.String.class,"Uo",Uo.class);
		this.UO_DENOMINAZIONE = new Field("uoDenominazione",java.lang.String.class,"Uo",Uo.class);
		this.UO_INDIRIZZO = new Field("uoIndirizzo",java.lang.String.class,"Uo",Uo.class);
		this.UO_CIVICO = new Field("uoCivico",java.lang.String.class,"Uo",Uo.class);
		this.UO_CAP = new Field("uoCap",java.lang.String.class,"Uo",Uo.class);
		this.UO_LOCALITA = new Field("uoLocalita",java.lang.String.class,"Uo",Uo.class);
		this.UO_PROVINCIA = new Field("uoProvincia",java.lang.String.class,"Uo",Uo.class);
		this.UO_NAZIONE = new Field("uoNazione",java.lang.String.class,"Uo",Uo.class);
	
	}
	
	public UoModel(IField father){
	
		super(father);
	
		this.COD_UO = new ComplexField(father,"codUo",java.lang.String.class,"Uo",Uo.class);
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new ComplexField(father,"idDominio",it.govpay.orm.IdDominio.class,"Uo",Uo.class));
		this.ABILITATO = new ComplexField(father,"abilitato",boolean.class,"Uo",Uo.class);
		this.UO_CODICE_IDENTIFICATIVO = new ComplexField(father,"uoCodiceIdentificativo",java.lang.String.class,"Uo",Uo.class);
		this.UO_DENOMINAZIONE = new ComplexField(father,"uoDenominazione",java.lang.String.class,"Uo",Uo.class);
		this.UO_INDIRIZZO = new ComplexField(father,"uoIndirizzo",java.lang.String.class,"Uo",Uo.class);
		this.UO_CIVICO = new ComplexField(father,"uoCivico",java.lang.String.class,"Uo",Uo.class);
		this.UO_CAP = new ComplexField(father,"uoCap",java.lang.String.class,"Uo",Uo.class);
		this.UO_LOCALITA = new ComplexField(father,"uoLocalita",java.lang.String.class,"Uo",Uo.class);
		this.UO_PROVINCIA = new ComplexField(father,"uoProvincia",java.lang.String.class,"Uo",Uo.class);
		this.UO_NAZIONE = new ComplexField(father,"uoNazione",java.lang.String.class,"Uo",Uo.class);
	
	}
	
	

	public IField COD_UO = null;
	 
	public it.govpay.orm.model.IdDominioModel ID_DOMINIO = null;
	 
	public IField ABILITATO = null;
	 
	public IField UO_CODICE_IDENTIFICATIVO = null;
	 
	public IField UO_DENOMINAZIONE = null;
	 
	public IField UO_INDIRIZZO = null;
	 
	public IField UO_CIVICO = null;
	 
	public IField UO_CAP = null;
	 
	public IField UO_LOCALITA = null;
	 
	public IField UO_PROVINCIA = null;
	 
	public IField UO_NAZIONE = null;
	 

	@Override
	public Class<Uo> getModeledClass(){
		return Uo.class;
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