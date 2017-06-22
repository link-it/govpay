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

import it.govpay.orm.Tracciato;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Tracciato 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class TracciatoModel extends AbstractModel<Tracciato> {

	public TracciatoModel(){
	
		super();
	
		this.ID_OPERATORE = new it.govpay.orm.model.IdOperatoreModel(new Field("idOperatore",it.govpay.orm.IdOperatore.class,"Tracciato",Tracciato.class));
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new Field("idApplicazione",it.govpay.orm.IdApplicazione.class,"Tracciato",Tracciato.class));
		this.DATA_CARICAMENTO = new Field("dataCaricamento",java.util.Date.class,"Tracciato",Tracciato.class);
		this.DATA_ULTIMO_AGGIORNAMENTO = new Field("dataUltimoAggiornamento",java.util.Date.class,"Tracciato",Tracciato.class);
		this.STATO = new Field("stato",java.lang.String.class,"Tracciato",Tracciato.class);
		this.LINEA_ELABORAZIONE = new Field("lineaElaborazione",long.class,"Tracciato",Tracciato.class);
		this.DESCRIZIONE_STATO = new Field("descrizioneStato",java.lang.String.class,"Tracciato",Tracciato.class);
		this.NUM_LINEE_TOTALI = new Field("numLineeTotali",long.class,"Tracciato",Tracciato.class);
		this.NUM_OPERAZIONI_OK = new Field("numOperazioniOk",long.class,"Tracciato",Tracciato.class);
		this.NUM_OPERAZIONI_KO = new Field("numOperazioniKo",long.class,"Tracciato",Tracciato.class);
		this.NOME_FILE = new Field("nomeFile",java.lang.String.class,"Tracciato",Tracciato.class);
		this.RAW_DATA_RICHIESTA = new Field("rawDataRichiesta",byte[].class,"Tracciato",Tracciato.class);
		this.RAW_DATA_RISPOSTA = new Field("rawDataRisposta",byte[].class,"Tracciato",Tracciato.class);
	
	}
	
	public TracciatoModel(IField father){
	
		super(father);
	
		this.ID_OPERATORE = new it.govpay.orm.model.IdOperatoreModel(new ComplexField(father,"idOperatore",it.govpay.orm.IdOperatore.class,"Tracciato",Tracciato.class));
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new ComplexField(father,"idApplicazione",it.govpay.orm.IdApplicazione.class,"Tracciato",Tracciato.class));
		this.DATA_CARICAMENTO = new ComplexField(father,"dataCaricamento",java.util.Date.class,"Tracciato",Tracciato.class);
		this.DATA_ULTIMO_AGGIORNAMENTO = new ComplexField(father,"dataUltimoAggiornamento",java.util.Date.class,"Tracciato",Tracciato.class);
		this.STATO = new ComplexField(father,"stato",java.lang.String.class,"Tracciato",Tracciato.class);
		this.LINEA_ELABORAZIONE = new ComplexField(father,"lineaElaborazione",long.class,"Tracciato",Tracciato.class);
		this.DESCRIZIONE_STATO = new ComplexField(father,"descrizioneStato",java.lang.String.class,"Tracciato",Tracciato.class);
		this.NUM_LINEE_TOTALI = new ComplexField(father,"numLineeTotali",long.class,"Tracciato",Tracciato.class);
		this.NUM_OPERAZIONI_OK = new ComplexField(father,"numOperazioniOk",long.class,"Tracciato",Tracciato.class);
		this.NUM_OPERAZIONI_KO = new ComplexField(father,"numOperazioniKo",long.class,"Tracciato",Tracciato.class);
		this.NOME_FILE = new ComplexField(father,"nomeFile",java.lang.String.class,"Tracciato",Tracciato.class);
		this.RAW_DATA_RICHIESTA = new ComplexField(father,"rawDataRichiesta",byte[].class,"Tracciato",Tracciato.class);
		this.RAW_DATA_RISPOSTA = new ComplexField(father,"rawDataRisposta",byte[].class,"Tracciato",Tracciato.class);
	
	}
	
	

	public it.govpay.orm.model.IdOperatoreModel ID_OPERATORE = null;
	 
	public it.govpay.orm.model.IdApplicazioneModel ID_APPLICAZIONE = null;
	 
	public IField DATA_CARICAMENTO = null;
	 
	public IField DATA_ULTIMO_AGGIORNAMENTO = null;
	 
	public IField STATO = null;
	 
	public IField LINEA_ELABORAZIONE = null;
	 
	public IField DESCRIZIONE_STATO = null;
	 
	public IField NUM_LINEE_TOTALI = null;
	 
	public IField NUM_OPERAZIONI_OK = null;
	 
	public IField NUM_OPERAZIONI_KO = null;
	 
	public IField NOME_FILE = null;
	 
	public IField RAW_DATA_RICHIESTA = null;
	 
	public IField RAW_DATA_RISPOSTA = null;
	 

	@Override
	public Class<Tracciato> getModeledClass(){
		return Tracciato.class;
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