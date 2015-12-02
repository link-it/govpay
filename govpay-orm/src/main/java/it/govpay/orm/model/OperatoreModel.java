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

import it.govpay.orm.Operatore;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Operatore 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class OperatoreModel extends AbstractModel<Operatore> {

	public OperatoreModel(){
	
		super();
	
		this.ID_ANAGRAFICA = new it.govpay.orm.model.IdAnagraficaModel(new Field("idAnagrafica",it.govpay.orm.IdAnagrafica.class,"Operatore",Operatore.class));
		this.PRINCIPAL = new Field("principal",java.lang.String.class,"Operatore",Operatore.class);
		this.PROFILO = new Field("profilo",java.lang.String.class,"Operatore",Operatore.class);
		this.ABILITATO = new Field("abilitato",boolean.class,"Operatore",Operatore.class);
		this.OPERATORE_ENTE = new it.govpay.orm.model.OperatoreEnteModel(new Field("OperatoreEnte",it.govpay.orm.OperatoreEnte.class,"Operatore",Operatore.class));
		this.OPERATORE_APPLICAZIONE = new it.govpay.orm.model.OperatoreApplicazioneModel(new Field("OperatoreApplicazione",it.govpay.orm.OperatoreApplicazione.class,"Operatore",Operatore.class));
	
	}
	
	public OperatoreModel(IField father){
	
		super(father);
	
		this.ID_ANAGRAFICA = new it.govpay.orm.model.IdAnagraficaModel(new ComplexField(father,"idAnagrafica",it.govpay.orm.IdAnagrafica.class,"Operatore",Operatore.class));
		this.PRINCIPAL = new ComplexField(father,"principal",java.lang.String.class,"Operatore",Operatore.class);
		this.PROFILO = new ComplexField(father,"profilo",java.lang.String.class,"Operatore",Operatore.class);
		this.ABILITATO = new ComplexField(father,"abilitato",boolean.class,"Operatore",Operatore.class);
		this.OPERATORE_ENTE = new it.govpay.orm.model.OperatoreEnteModel(new ComplexField(father,"OperatoreEnte",it.govpay.orm.OperatoreEnte.class,"Operatore",Operatore.class));
		this.OPERATORE_APPLICAZIONE = new it.govpay.orm.model.OperatoreApplicazioneModel(new ComplexField(father,"OperatoreApplicazione",it.govpay.orm.OperatoreApplicazione.class,"Operatore",Operatore.class));
	
	}
	
	

	public it.govpay.orm.model.IdAnagraficaModel ID_ANAGRAFICA = null;
	 
	public IField PRINCIPAL = null;
	 
	public IField PROFILO = null;
	 
	public IField ABILITATO = null;
	 
	public it.govpay.orm.model.OperatoreEnteModel OPERATORE_ENTE = null;
	 
	public it.govpay.orm.model.OperatoreApplicazioneModel OPERATORE_APPLICAZIONE = null;
	 

	@Override
	public Class<Operatore> getModeledClass(){
		return Operatore.class;
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