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
		this.COD_DOMINIO = new Field("codDominio",java.lang.String.class,"Versamento",Versamento.class);
		this.IUV = new Field("iuv",java.lang.String.class,"Versamento",Versamento.class);
		this.ID_ENTE = new it.govpay.orm.model.IdEnteModel(new Field("idEnte",it.govpay.orm.IdEnte.class,"Versamento",Versamento.class));
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new Field("idApplicazione",it.govpay.orm.IdApplicazione.class,"Versamento",Versamento.class));
		this.ID_ANAGRAFICA_DEBITORE = new it.govpay.orm.model.IdAnagraficaModel(new Field("idAnagraficaDebitore",it.govpay.orm.IdAnagrafica.class,"Versamento",Versamento.class));
		this.IMPORTO_TOTALE = new Field("importoTotale",double.class,"Versamento",Versamento.class);
		this.STATO_VERSAMENTO = new Field("statoVersamento",java.lang.String.class,"Versamento",Versamento.class);
		this.DESCRIZIONE_STATO = new Field("descrizioneStato",java.lang.String.class,"Versamento",Versamento.class);
		this.STATO_RENDICONTAZIONE = new Field("statoRendicontazione",java.lang.String.class,"Versamento",Versamento.class);
		this.IMPORTO_PAGATO = new Field("importoPagato",java.lang.Double.class,"Versamento",Versamento.class);
		this.DATA_SCADENZA = new Field("dataScadenza",java.util.Date.class,"Versamento",Versamento.class);
		this.DATA_ORA_ULTIMO_AGGIORNAMENTO = new Field("dataOraUltimoAggiornamento",java.util.Date.class,"Versamento",Versamento.class);
	
	}
	
	public VersamentoModel(IField father){
	
		super(father);
	
		this.COD_VERSAMENTO_ENTE = new ComplexField(father,"codVersamentoEnte",java.lang.String.class,"Versamento",Versamento.class);
		this.COD_DOMINIO = new ComplexField(father,"codDominio",java.lang.String.class,"Versamento",Versamento.class);
		this.IUV = new ComplexField(father,"iuv",java.lang.String.class,"Versamento",Versamento.class);
		this.ID_ENTE = new it.govpay.orm.model.IdEnteModel(new ComplexField(father,"idEnte",it.govpay.orm.IdEnte.class,"Versamento",Versamento.class));
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new ComplexField(father,"idApplicazione",it.govpay.orm.IdApplicazione.class,"Versamento",Versamento.class));
		this.ID_ANAGRAFICA_DEBITORE = new it.govpay.orm.model.IdAnagraficaModel(new ComplexField(father,"idAnagraficaDebitore",it.govpay.orm.IdAnagrafica.class,"Versamento",Versamento.class));
		this.IMPORTO_TOTALE = new ComplexField(father,"importoTotale",double.class,"Versamento",Versamento.class);
		this.STATO_VERSAMENTO = new ComplexField(father,"statoVersamento",java.lang.String.class,"Versamento",Versamento.class);
		this.DESCRIZIONE_STATO = new ComplexField(father,"descrizioneStato",java.lang.String.class,"Versamento",Versamento.class);
		this.STATO_RENDICONTAZIONE = new ComplexField(father,"statoRendicontazione",java.lang.String.class,"Versamento",Versamento.class);
		this.IMPORTO_PAGATO = new ComplexField(father,"importoPagato",java.lang.Double.class,"Versamento",Versamento.class);
		this.DATA_SCADENZA = new ComplexField(father,"dataScadenza",java.util.Date.class,"Versamento",Versamento.class);
		this.DATA_ORA_ULTIMO_AGGIORNAMENTO = new ComplexField(father,"dataOraUltimoAggiornamento",java.util.Date.class,"Versamento",Versamento.class);
	
	}
	
	

	public IField COD_VERSAMENTO_ENTE = null;
	 
	public IField COD_DOMINIO = null;
	 
	public IField IUV = null;
	 
	public it.govpay.orm.model.IdEnteModel ID_ENTE = null;
	 
	public it.govpay.orm.model.IdApplicazioneModel ID_APPLICAZIONE = null;
	 
	public it.govpay.orm.model.IdAnagraficaModel ID_ANAGRAFICA_DEBITORE = null;
	 
	public IField IMPORTO_TOTALE = null;
	 
	public IField STATO_VERSAMENTO = null;
	 
	public IField DESCRIZIONE_STATO = null;
	 
	public IField STATO_RENDICONTAZIONE = null;
	 
	public IField IMPORTO_PAGATO = null;
	 
	public IField DATA_SCADENZA = null;
	 
	public IField DATA_ORA_ULTIMO_AGGIORNAMENTO = null;
	 

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