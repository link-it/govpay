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

import it.govpay.orm.ACL;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model ACL 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class ACLModel extends AbstractModel<ACL> {

	public ACLModel(){
	
		super();
	
		this.RUOLO = new Field("ruolo",java.lang.String.class,"ACL",ACL.class);
		this.ID_UTENZA = new it.govpay.orm.model.IdUtenzaModel(new Field("idUtenza",it.govpay.orm.IdUtenza.class,"ACL",ACL.class));
		this.SERVIZIO = new Field("servizio",java.lang.String.class,"ACL",ACL.class);
		this.DIRITTI = new Field("diritti",java.lang.String.class,"ACL",ACL.class);
	
	}
	
	public ACLModel(IField father){
	
		super(father);
	
		this.RUOLO = new ComplexField(father,"ruolo",java.lang.String.class,"ACL",ACL.class);
		this.ID_UTENZA = new it.govpay.orm.model.IdUtenzaModel(new ComplexField(father,"idUtenza",it.govpay.orm.IdUtenza.class,"ACL",ACL.class));
		this.SERVIZIO = new ComplexField(father,"servizio",java.lang.String.class,"ACL",ACL.class);
		this.DIRITTI = new ComplexField(father,"diritti",java.lang.String.class,"ACL",ACL.class);
	
	}
	
	

	public IField RUOLO = null;
	 
	public it.govpay.orm.model.IdUtenzaModel ID_UTENZA = null;
	 
	public IField SERVIZIO = null;
	 
	public IField DIRITTI = null;
	 

	@Override
	public Class<ACL> getModeledClass(){
		return ACL.class;
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
