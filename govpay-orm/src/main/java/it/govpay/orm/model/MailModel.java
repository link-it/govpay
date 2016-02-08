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

import it.govpay.orm.Mail;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Mail 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class MailModel extends AbstractModel<Mail> {

	public MailModel(){
	
		super();
	
		this.TIPO_MAIL = new Field("tipoMail",java.lang.String.class,"Mail",Mail.class);
		this.BUNDLE_KEY = new Field("bundleKey",long.class,"Mail",Mail.class);
		this.ID_VERSAMENTO = new Field("idVersamento",java.lang.Long.class,"Mail",Mail.class);
		this.MITTENTE = new Field("mittente",java.lang.String.class,"Mail",Mail.class);
		this.DESTINATARIO = new Field("destinatario",java.lang.String.class,"Mail",Mail.class);
		this.CC = new Field("cc",java.lang.String.class,"Mail",Mail.class);
		this.OGGETTO = new Field("oggetto",java.lang.String.class,"Mail",Mail.class);
		this.MESSAGGIO = new Field("messaggio",java.lang.String.class,"Mail",Mail.class);
		this.STATO_SPEDIZIONE = new Field("statoSpedizione",java.lang.String.class,"Mail",Mail.class);
		this.DETTAGLIO_ERRORE_SPEDIZIONE = new Field("dettaglioErroreSpedizione",java.lang.String.class,"Mail",Mail.class);
		this.DATA_ORA_ULTIMA_SPEDIZIONE = new Field("dataOraUltimaSpedizione",java.util.Date.class,"Mail",Mail.class);
		this.TENTATIVI_RISPEDIZIONE = new Field("tentativiRispedizione",java.lang.Long.class,"Mail",Mail.class);
		this.ID_TRACCIATO_RPT = new it.govpay.orm.model.IdTracciatoModel(new Field("idTracciatoRPT",it.govpay.orm.IdTracciato.class,"Mail",Mail.class));
		this.ID_TRACCIATO_RT = new it.govpay.orm.model.IdTracciatoModel(new Field("idTracciatoRT",it.govpay.orm.IdTracciato.class,"Mail",Mail.class));
	
	}
	
	public MailModel(IField father){
	
		super(father);
	
		this.TIPO_MAIL = new ComplexField(father,"tipoMail",java.lang.String.class,"Mail",Mail.class);
		this.BUNDLE_KEY = new ComplexField(father,"bundleKey",long.class,"Mail",Mail.class);
		this.ID_VERSAMENTO = new ComplexField(father,"idVersamento",java.lang.Long.class,"Mail",Mail.class);
		this.MITTENTE = new ComplexField(father,"mittente",java.lang.String.class,"Mail",Mail.class);
		this.DESTINATARIO = new ComplexField(father,"destinatario",java.lang.String.class,"Mail",Mail.class);
		this.CC = new ComplexField(father,"cc",java.lang.String.class,"Mail",Mail.class);
		this.OGGETTO = new ComplexField(father,"oggetto",java.lang.String.class,"Mail",Mail.class);
		this.MESSAGGIO = new ComplexField(father,"messaggio",java.lang.String.class,"Mail",Mail.class);
		this.STATO_SPEDIZIONE = new ComplexField(father,"statoSpedizione",java.lang.String.class,"Mail",Mail.class);
		this.DETTAGLIO_ERRORE_SPEDIZIONE = new ComplexField(father,"dettaglioErroreSpedizione",java.lang.String.class,"Mail",Mail.class);
		this.DATA_ORA_ULTIMA_SPEDIZIONE = new ComplexField(father,"dataOraUltimaSpedizione",java.util.Date.class,"Mail",Mail.class);
		this.TENTATIVI_RISPEDIZIONE = new ComplexField(father,"tentativiRispedizione",java.lang.Long.class,"Mail",Mail.class);
		this.ID_TRACCIATO_RPT = new it.govpay.orm.model.IdTracciatoModel(new ComplexField(father,"idTracciatoRPT",it.govpay.orm.IdTracciato.class,"Mail",Mail.class));
		this.ID_TRACCIATO_RT = new it.govpay.orm.model.IdTracciatoModel(new ComplexField(father,"idTracciatoRT",it.govpay.orm.IdTracciato.class,"Mail",Mail.class));
	
	}
	
	

	public IField TIPO_MAIL = null;
	 
	public IField BUNDLE_KEY = null;
	 
	public IField ID_VERSAMENTO = null;
	 
	public IField MITTENTE = null;
	 
	public IField DESTINATARIO = null;
	 
	public IField CC = null;
	 
	public IField OGGETTO = null;
	 
	public IField MESSAGGIO = null;
	 
	public IField STATO_SPEDIZIONE = null;
	 
	public IField DETTAGLIO_ERRORE_SPEDIZIONE = null;
	 
	public IField DATA_ORA_ULTIMA_SPEDIZIONE = null;
	 
	public IField TENTATIVI_RISPEDIZIONE = null;
	 
	public it.govpay.orm.model.IdTracciatoModel ID_TRACCIATO_RPT = null;
	 
	public it.govpay.orm.model.IdTracciatoModel ID_TRACCIATO_RT = null;
	 

	@Override
	public Class<Mail> getModeledClass(){
		return Mail.class;
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