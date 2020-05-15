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

import it.govpay.orm.Stazione;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Stazione 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class StazioneModel extends AbstractModel<Stazione> {

	public StazioneModel(){
	
		super();
	
		this.ID_INTERMEDIARIO = new it.govpay.orm.model.IdIntermediarioModel(new Field("idIntermediario",it.govpay.orm.IdIntermediario.class,"Stazione",Stazione.class));
		this.COD_STAZIONE = new Field("codStazione",java.lang.String.class,"Stazione",Stazione.class);
		this.PASSWORD = new Field("password",java.lang.String.class,"Stazione",Stazione.class);
		this.ABILITATO = new Field("abilitato",boolean.class,"Stazione",Stazione.class);
		this.APPLICATION_CODE = new Field("applicationCode",java.lang.Integer.class,"Stazione",Stazione.class);
	
	}
	
	public StazioneModel(IField father){
	
		super(father);
	
		this.ID_INTERMEDIARIO = new it.govpay.orm.model.IdIntermediarioModel(new ComplexField(father,"idIntermediario",it.govpay.orm.IdIntermediario.class,"Stazione",Stazione.class));
		this.COD_STAZIONE = new ComplexField(father,"codStazione",java.lang.String.class,"Stazione",Stazione.class);
		this.PASSWORD = new ComplexField(father,"password",java.lang.String.class,"Stazione",Stazione.class);
		this.ABILITATO = new ComplexField(father,"abilitato",boolean.class,"Stazione",Stazione.class);
		this.APPLICATION_CODE = new ComplexField(father,"applicationCode",java.lang.Integer.class,"Stazione",Stazione.class);
	
	}
	
	

	public it.govpay.orm.model.IdIntermediarioModel ID_INTERMEDIARIO = null;
	 
	public IField COD_STAZIONE = null;
	 
	public IField PASSWORD = null;
	 
	public IField ABILITATO = null;
	 
	public IField APPLICATION_CODE = null;
	 

	@Override
	public Class<Stazione> getModeledClass(){
		return Stazione.class;
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
