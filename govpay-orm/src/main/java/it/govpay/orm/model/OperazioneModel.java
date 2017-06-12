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

import it.govpay.orm.Operazione;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Operazione 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class OperazioneModel extends AbstractModel<Operazione> {

	public OperazioneModel(){
	
		super();
	
		this.ID_TRACCIATO = new it.govpay.orm.model.IdTracciatoModel(new Field("idTracciato",it.govpay.orm.IdTracciato.class,"Operazione",Operazione.class));
		this.LINEA_ELABORAZIONE = new Field("lineaElaborazione",long.class,"Operazione",Operazione.class);
		this.STATO = new Field("stato",java.lang.String.class,"Operazione",Operazione.class);
		this.DATI_RICHIESTA = new Field("datiRichiesta",byte[].class,"Operazione",Operazione.class);
		this.DATI_RISPOSTA = new Field("datiRisposta",byte[].class,"Operazione",Operazione.class);
		this.ESITO = new Field("esito",java.lang.String.class,"Operazione",Operazione.class);
		this.DETTAGLIO_ESITO = new Field("dettaglioEsito",java.lang.String.class,"Operazione",Operazione.class);
		this.TIPO_OPERAZIONE = new Field("tipoOperazione",java.lang.String.class,"Operazione",Operazione.class);
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new Field("idApplicazione",it.govpay.orm.IdApplicazione.class,"Operazione",Operazione.class));
		this.COD_VERSAMENTO_ENTE = new Field("codVersamentoEnte",java.lang.String.class,"Operazione",Operazione.class);
	
	}
	
	public OperazioneModel(IField father){
	
		super(father);
	
		this.ID_TRACCIATO = new it.govpay.orm.model.IdTracciatoModel(new ComplexField(father,"idTracciato",it.govpay.orm.IdTracciato.class,"Operazione",Operazione.class));
		this.LINEA_ELABORAZIONE = new ComplexField(father,"lineaElaborazione",long.class,"Operazione",Operazione.class);
		this.STATO = new ComplexField(father,"stato",java.lang.String.class,"Operazione",Operazione.class);
		this.DATI_RICHIESTA = new ComplexField(father,"datiRichiesta",byte[].class,"Operazione",Operazione.class);
		this.DATI_RISPOSTA = new ComplexField(father,"datiRisposta",byte[].class,"Operazione",Operazione.class);
		this.ESITO = new ComplexField(father,"esito",java.lang.String.class,"Operazione",Operazione.class);
		this.DETTAGLIO_ESITO = new ComplexField(father,"dettaglioEsito",java.lang.String.class,"Operazione",Operazione.class);
		this.TIPO_OPERAZIONE = new ComplexField(father,"tipoOperazione",java.lang.String.class,"Operazione",Operazione.class);
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new ComplexField(father,"idApplicazione",it.govpay.orm.IdApplicazione.class,"Operazione",Operazione.class));
		this.COD_VERSAMENTO_ENTE = new ComplexField(father,"codVersamentoEnte",java.lang.String.class,"Operazione",Operazione.class);
	
	}
	
	

	public it.govpay.orm.model.IdTracciatoModel ID_TRACCIATO = null;
	 
	public IField LINEA_ELABORAZIONE = null;
	 
	public IField STATO = null;
	 
	public IField DATI_RICHIESTA = null;
	 
	public IField DATI_RISPOSTA = null;
	 
	public IField ESITO = null;
	 
	public IField DETTAGLIO_ESITO = null;
	 
	public IField TIPO_OPERAZIONE = null;
	 
	public it.govpay.orm.model.IdApplicazioneModel ID_APPLICAZIONE = null;
	 
	public IField COD_VERSAMENTO_ENTE = null;
	 

	@Override
	public Class<Operazione> getModeledClass(){
		return Operazione.class;
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