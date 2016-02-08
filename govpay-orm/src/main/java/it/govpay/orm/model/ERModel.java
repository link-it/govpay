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

import it.govpay.orm.ER;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model ER 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class ERModel extends AbstractModel<ER> {

	public ERModel(){
	
		super();
	
		this.ID_RR = new it.govpay.orm.model.IdRrModel(new Field("idRR",it.govpay.orm.IdRr.class,"ER",ER.class));
		this.ID_TRACCIATO_XML = new it.govpay.orm.model.IdTracciatoModel(new Field("idTracciatoXML",it.govpay.orm.IdTracciato.class,"ER",ER.class));
		this.COD_MSG_ESITO = new Field("codMsgEsito",java.lang.String.class,"ER",ER.class);
		this.DATA_ORA_MSG_ESITO = new Field("dataOraMsgEsito",java.util.Date.class,"ER",ER.class);
		this.IMPORTO_TOTALE_REVOCATO = new Field("importoTotaleRevocato",double.class,"ER",ER.class);
		this.STATO = new Field("stato",java.lang.String.class,"ER",ER.class);
		this.DESCRIZIONE_STATO = new Field("descrizioneStato",java.lang.String.class,"ER",ER.class);
		this.DATA_ORA_CREAZIONE = new Field("dataOraCreazione",java.util.Date.class,"ER",ER.class);
	
	}
	
	public ERModel(IField father){
	
		super(father);
	
		this.ID_RR = new it.govpay.orm.model.IdRrModel(new ComplexField(father,"idRR",it.govpay.orm.IdRr.class,"ER",ER.class));
		this.ID_TRACCIATO_XML = new it.govpay.orm.model.IdTracciatoModel(new ComplexField(father,"idTracciatoXML",it.govpay.orm.IdTracciato.class,"ER",ER.class));
		this.COD_MSG_ESITO = new ComplexField(father,"codMsgEsito",java.lang.String.class,"ER",ER.class);
		this.DATA_ORA_MSG_ESITO = new ComplexField(father,"dataOraMsgEsito",java.util.Date.class,"ER",ER.class);
		this.IMPORTO_TOTALE_REVOCATO = new ComplexField(father,"importoTotaleRevocato",double.class,"ER",ER.class);
		this.STATO = new ComplexField(father,"stato",java.lang.String.class,"ER",ER.class);
		this.DESCRIZIONE_STATO = new ComplexField(father,"descrizioneStato",java.lang.String.class,"ER",ER.class);
		this.DATA_ORA_CREAZIONE = new ComplexField(father,"dataOraCreazione",java.util.Date.class,"ER",ER.class);
	
	}
	
	

	public it.govpay.orm.model.IdRrModel ID_RR = null;
	 
	public it.govpay.orm.model.IdTracciatoModel ID_TRACCIATO_XML = null;
	 
	public IField COD_MSG_ESITO = null;
	 
	public IField DATA_ORA_MSG_ESITO = null;
	 
	public IField IMPORTO_TOTALE_REVOCATO = null;
	 
	public IField STATO = null;
	 
	public IField DESCRIZIONE_STATO = null;
	 
	public IField DATA_ORA_CREAZIONE = null;
	 

	@Override
	public Class<ER> getModeledClass(){
		return ER.class;
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