/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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

import it.govpay.orm.Versamento;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Versamento 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VersamentoModel extends AbstractModel<Versamento> {

	public VersamentoModel(){
	
		super();
	
		this.COD_VERSAMENTO_ENTE = new Field("codVersamentoEnte",java.lang.String.class,"Versamento",Versamento.class);
		this.ID_UO = new it.govpay.orm.model.IdUoModel(new Field("idUo",it.govpay.orm.IdUo.class,"Versamento",Versamento.class));
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new Field("idApplicazione",it.govpay.orm.IdApplicazione.class,"Versamento",Versamento.class));
		this.IMPORTO_TOTALE = new Field("importoTotale",double.class,"Versamento",Versamento.class);
		this.STATO_VERSAMENTO = new Field("statoVersamento",java.lang.String.class,"Versamento",Versamento.class);
		this.DESCRIZIONE_STATO = new Field("descrizioneStato",java.lang.String.class,"Versamento",Versamento.class);
		this.AGGIORNABILE = new Field("aggiornabile",boolean.class,"Versamento",Versamento.class);
		this.DATA_CREAZIONE = new Field("dataCreazione",java.util.Date.class,"Versamento",Versamento.class);
		this.DATA_SCADENZA = new Field("dataScadenza",java.util.Date.class,"Versamento",Versamento.class);
		this.DATA_ORA_ULTIMO_AGGIORNAMENTO = new Field("dataOraUltimoAggiornamento",java.util.Date.class,"Versamento",Versamento.class);
		this.CAUSALE_VERSAMENTO = new Field("causaleVersamento",java.lang.String.class,"Versamento",Versamento.class);
		this.DEBITORE_IDENTIFICATIVO = new Field("debitoreIdentificativo",java.lang.String.class,"Versamento",Versamento.class);
		this.DEBITORE_ANAGRAFICA = new Field("debitoreAnagrafica",java.lang.String.class,"Versamento",Versamento.class);
		this.DEBITORE_INDIRIZZO = new Field("debitoreIndirizzo",java.lang.String.class,"Versamento",Versamento.class);
		this.DEBITORE_CIVICO = new Field("debitoreCivico",java.lang.String.class,"Versamento",Versamento.class);
		this.DEBITORE_CAP = new Field("debitoreCap",java.lang.String.class,"Versamento",Versamento.class);
		this.DEBITORE_LOCALITA = new Field("debitoreLocalita",java.lang.String.class,"Versamento",Versamento.class);
		this.DEBITORE_PROVINCIA = new Field("debitoreProvincia",java.lang.String.class,"Versamento",Versamento.class);
		this.DEBITORE_NAZIONE = new Field("debitoreNazione",java.lang.String.class,"Versamento",Versamento.class);
	
	}
	
	public VersamentoModel(IField father){
	
		super(father);
	
		this.COD_VERSAMENTO_ENTE = new ComplexField(father,"codVersamentoEnte",java.lang.String.class,"Versamento",Versamento.class);
		this.ID_UO = new it.govpay.orm.model.IdUoModel(new ComplexField(father,"idUo",it.govpay.orm.IdUo.class,"Versamento",Versamento.class));
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new ComplexField(father,"idApplicazione",it.govpay.orm.IdApplicazione.class,"Versamento",Versamento.class));
		this.IMPORTO_TOTALE = new ComplexField(father,"importoTotale",double.class,"Versamento",Versamento.class);
		this.STATO_VERSAMENTO = new ComplexField(father,"statoVersamento",java.lang.String.class,"Versamento",Versamento.class);
		this.DESCRIZIONE_STATO = new ComplexField(father,"descrizioneStato",java.lang.String.class,"Versamento",Versamento.class);
		this.AGGIORNABILE = new ComplexField(father,"aggiornabile",boolean.class,"Versamento",Versamento.class);
		this.DATA_CREAZIONE = new ComplexField(father,"dataCreazione",java.util.Date.class,"Versamento",Versamento.class);
		this.DATA_SCADENZA = new ComplexField(father,"dataScadenza",java.util.Date.class,"Versamento",Versamento.class);
		this.DATA_ORA_ULTIMO_AGGIORNAMENTO = new ComplexField(father,"dataOraUltimoAggiornamento",java.util.Date.class,"Versamento",Versamento.class);
		this.CAUSALE_VERSAMENTO = new ComplexField(father,"causaleVersamento",java.lang.String.class,"Versamento",Versamento.class);
		this.DEBITORE_IDENTIFICATIVO = new ComplexField(father,"debitoreIdentificativo",java.lang.String.class,"Versamento",Versamento.class);
		this.DEBITORE_ANAGRAFICA = new ComplexField(father,"debitoreAnagrafica",java.lang.String.class,"Versamento",Versamento.class);
		this.DEBITORE_INDIRIZZO = new ComplexField(father,"debitoreIndirizzo",java.lang.String.class,"Versamento",Versamento.class);
		this.DEBITORE_CIVICO = new ComplexField(father,"debitoreCivico",java.lang.String.class,"Versamento",Versamento.class);
		this.DEBITORE_CAP = new ComplexField(father,"debitoreCap",java.lang.String.class,"Versamento",Versamento.class);
		this.DEBITORE_LOCALITA = new ComplexField(father,"debitoreLocalita",java.lang.String.class,"Versamento",Versamento.class);
		this.DEBITORE_PROVINCIA = new ComplexField(father,"debitoreProvincia",java.lang.String.class,"Versamento",Versamento.class);
		this.DEBITORE_NAZIONE = new ComplexField(father,"debitoreNazione",java.lang.String.class,"Versamento",Versamento.class);
	
	}
	
	

	public IField COD_VERSAMENTO_ENTE = null;
	 
	public it.govpay.orm.model.IdUoModel ID_UO = null;
	 
	public it.govpay.orm.model.IdApplicazioneModel ID_APPLICAZIONE = null;
	 
	public IField IMPORTO_TOTALE = null;
	 
	public IField STATO_VERSAMENTO = null;
	 
	public IField DESCRIZIONE_STATO = null;
	 
	public IField AGGIORNABILE = null;
	 
	public IField DATA_CREAZIONE = null;
	 
	public IField DATA_SCADENZA = null;
	 
	public IField DATA_ORA_ULTIMO_AGGIORNAMENTO = null;
	 
	public IField CAUSALE_VERSAMENTO = null;
	 
	public IField DEBITORE_IDENTIFICATIVO = null;
	 
	public IField DEBITORE_ANAGRAFICA = null;
	 
	public IField DEBITORE_INDIRIZZO = null;
	 
	public IField DEBITORE_CIVICO = null;
	 
	public IField DEBITORE_CAP = null;
	 
	public IField DEBITORE_LOCALITA = null;
	 
	public IField DEBITORE_PROVINCIA = null;
	 
	public IField DEBITORE_NAZIONE = null;
	 

	@Override
	public Class<Versamento> getModeledClass(){
		return Versamento.class;
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