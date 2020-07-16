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

import it.govpay.orm.IdOperazione;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model IdOperazione 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class IdOperazioneModel extends AbstractModel<IdOperazione> {

	public IdOperazioneModel(){
	
		super();
	
		this.ID_TRACCIATO = new it.govpay.orm.model.IdTracciatoModel(new Field("idTracciato",it.govpay.orm.IdTracciato.class,"id-operazione",IdOperazione.class));
		this.LINEA_ELABORAZIONE = new Field("lineaElaborazione",long.class,"id-operazione",IdOperazione.class);
		this.STATO = new Field("stato",java.lang.String.class,"id-operazione",IdOperazione.class);
	
	}
	
	public IdOperazioneModel(IField father){
	
		super(father);
	
		this.ID_TRACCIATO = new it.govpay.orm.model.IdTracciatoModel(new ComplexField(father,"idTracciato",it.govpay.orm.IdTracciato.class,"id-operazione",IdOperazione.class));
		this.LINEA_ELABORAZIONE = new ComplexField(father,"lineaElaborazione",long.class,"id-operazione",IdOperazione.class);
		this.STATO = new ComplexField(father,"stato",java.lang.String.class,"id-operazione",IdOperazione.class);
	
	}
	
	

	public it.govpay.orm.model.IdTracciatoModel ID_TRACCIATO = null;
	 
	public IField LINEA_ELABORAZIONE = null;
	 
	public IField STATO = null;
	 

	@Override
	public Class<IdOperazione> getModeledClass(){
		return IdOperazione.class;
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