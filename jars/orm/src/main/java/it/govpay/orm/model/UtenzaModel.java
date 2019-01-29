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

import it.govpay.orm.Utenza;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Utenza 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class UtenzaModel extends AbstractModel<Utenza> {

	public UtenzaModel(){
	
		super();
	
		this.PRINCIPAL = new Field("principal",java.lang.String.class,"Utenza",Utenza.class);
		this.PRINCIPAL_ORIGINALE = new Field("principalOriginale",java.lang.String.class,"Utenza",Utenza.class);
		this.ABILITATO = new Field("abilitato",boolean.class,"Utenza",Utenza.class);
		this.AUTORIZZAZIONE_DOMINI_STAR = new Field("autorizzazione_domini_star",boolean.class,"Utenza",Utenza.class);
		this.AUTORIZZAZIONE_TRIBUTI_STAR = new Field("autorizzazione_tributi_star",boolean.class,"Utenza",Utenza.class);
	
	}
	
	public UtenzaModel(IField father){
	
		super(father);
	
		this.PRINCIPAL = new ComplexField(father,"principal",java.lang.String.class,"Utenza",Utenza.class);
		this.PRINCIPAL_ORIGINALE = new ComplexField(father,"principalOriginale",java.lang.String.class,"Utenza",Utenza.class);
		this.ABILITATO = new ComplexField(father,"abilitato",boolean.class,"Utenza",Utenza.class);
		this.AUTORIZZAZIONE_DOMINI_STAR = new ComplexField(father,"autorizzazione_domini_star",boolean.class,"Utenza",Utenza.class);
		this.AUTORIZZAZIONE_TRIBUTI_STAR = new ComplexField(father,"autorizzazione_tributi_star",boolean.class,"Utenza",Utenza.class);
	
	}
	
	

	public IField PRINCIPAL = null;
	 
	public IField PRINCIPAL_ORIGINALE = null;
	 
	public IField ABILITATO = null;
	 
	public IField AUTORIZZAZIONE_DOMINI_STAR = null;
	 
	public IField AUTORIZZAZIONE_TRIBUTI_STAR = null;
	 

	@Override
	public Class<Utenza> getModeledClass(){
		return Utenza.class;
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
