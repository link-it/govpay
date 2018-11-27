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

import it.govpay.orm.Notifica;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Notifica 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class NotificaModel extends AbstractModel<Notifica> {

	public NotificaModel(){
	
		super();
	
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new Field("idApplicazione",it.govpay.orm.IdApplicazione.class,"Notifica",Notifica.class));
		this.ID_RPT = new it.govpay.orm.model.IdRptModel(new Field("idRpt",it.govpay.orm.IdRpt.class,"Notifica",Notifica.class));
		this.ID_RR = new it.govpay.orm.model.IdRrModel(new Field("idRr",it.govpay.orm.IdRr.class,"Notifica",Notifica.class));
		this.TIPO_ESITO = new Field("tipoEsito",java.lang.String.class,"Notifica",Notifica.class);
		this.DATA_CREAZIONE = new Field("dataCreazione",java.util.Date.class,"Notifica",Notifica.class);
		this.STATO = new Field("stato",java.lang.String.class,"Notifica",Notifica.class);
		this.DESCRIZIONE_STATO = new Field("descrizioneStato",java.lang.String.class,"Notifica",Notifica.class);
		this.DATA_AGGIORNAMENTO_STATO = new Field("dataAggiornamentoStato",java.util.Date.class,"Notifica",Notifica.class);
		this.DATA_PROSSIMA_SPEDIZIONE = new Field("dataProssimaSpedizione",java.util.Date.class,"Notifica",Notifica.class);
		this.TENTATIVI_SPEDIZIONE = new Field("tentativiSpedizione",java.lang.Long.class,"Notifica",Notifica.class);
	
	}
	
	public NotificaModel(IField father){
	
		super(father);
	
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new ComplexField(father,"idApplicazione",it.govpay.orm.IdApplicazione.class,"Notifica",Notifica.class));
		this.ID_RPT = new it.govpay.orm.model.IdRptModel(new ComplexField(father,"idRpt",it.govpay.orm.IdRpt.class,"Notifica",Notifica.class));
		this.ID_RR = new it.govpay.orm.model.IdRrModel(new ComplexField(father,"idRr",it.govpay.orm.IdRr.class,"Notifica",Notifica.class));
		this.TIPO_ESITO = new ComplexField(father,"tipoEsito",java.lang.String.class,"Notifica",Notifica.class);
		this.DATA_CREAZIONE = new ComplexField(father,"dataCreazione",java.util.Date.class,"Notifica",Notifica.class);
		this.STATO = new ComplexField(father,"stato",java.lang.String.class,"Notifica",Notifica.class);
		this.DESCRIZIONE_STATO = new ComplexField(father,"descrizioneStato",java.lang.String.class,"Notifica",Notifica.class);
		this.DATA_AGGIORNAMENTO_STATO = new ComplexField(father,"dataAggiornamentoStato",java.util.Date.class,"Notifica",Notifica.class);
		this.DATA_PROSSIMA_SPEDIZIONE = new ComplexField(father,"dataProssimaSpedizione",java.util.Date.class,"Notifica",Notifica.class);
		this.TENTATIVI_SPEDIZIONE = new ComplexField(father,"tentativiSpedizione",java.lang.Long.class,"Notifica",Notifica.class);
	
	}
	
	

	public it.govpay.orm.model.IdApplicazioneModel ID_APPLICAZIONE = null;
	 
	public it.govpay.orm.model.IdRptModel ID_RPT = null;
	 
	public it.govpay.orm.model.IdRrModel ID_RR = null;
	 
	public IField TIPO_ESITO = null;
	 
	public IField DATA_CREAZIONE = null;
	 
	public IField STATO = null;
	 
	public IField DESCRIZIONE_STATO = null;
	 
	public IField DATA_AGGIORNAMENTO_STATO = null;
	 
	public IField DATA_PROSSIMA_SPEDIZIONE = null;
	 
	public IField TENTATIVI_SPEDIZIONE = null;
	 

	@Override
	public Class<Notifica> getModeledClass(){
		return Notifica.class;
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