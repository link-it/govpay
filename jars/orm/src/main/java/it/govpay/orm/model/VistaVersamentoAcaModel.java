/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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

import it.govpay.orm.VistaVersamentoAca;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model VistaVersamentoAca 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VistaVersamentoAcaModel extends AbstractModel<VistaVersamentoAca> {

	public VistaVersamentoAcaModel(){
	
		super();
	
		this.COD_VERSAMENTO_ENTE = new Field("codVersamentoEnte",java.lang.String.class,"VistaVersamentoAca",VistaVersamentoAca.class);
		this.COD_DOMINIO = new Field("codDominio",java.lang.String.class,"VistaVersamentoAca",VistaVersamentoAca.class);
		this.COD_APPLICAZIONE = new Field("codApplicazione",java.lang.String.class,"VistaVersamentoAca",VistaVersamentoAca.class);
		this.IMPORTO_TOTALE = new Field("importoTotale",double.class,"VistaVersamentoAca",VistaVersamentoAca.class);
		this.STATO_VERSAMENTO = new Field("statoVersamento",java.lang.String.class,"VistaVersamentoAca",VistaVersamentoAca.class);
		this.DATA_VALIDITA = new Field("dataValidita",java.util.Date.class,"VistaVersamentoAca",VistaVersamentoAca.class);
		this.DATA_SCADENZA = new Field("dataScadenza",java.util.Date.class,"VistaVersamentoAca",VistaVersamentoAca.class);
		this.CAUSALE_VERSAMENTO = new Field("causaleVersamento",java.lang.String.class,"VistaVersamentoAca",VistaVersamentoAca.class);
		this.DEBITORE_TIPO = new Field("debitoreTipo",java.lang.String.class,"VistaVersamentoAca",VistaVersamentoAca.class);
		this.DEBITORE_IDENTIFICATIVO = new Field("debitoreIdentificativo",java.lang.String.class,"VistaVersamentoAca",VistaVersamentoAca.class);
		this.DEBITORE_ANAGRAFICA = new Field("debitoreAnagrafica",java.lang.String.class,"VistaVersamentoAca",VistaVersamentoAca.class);
		this.IUV_VERSAMENTO = new Field("iuvVersamento",java.lang.String.class,"VistaVersamentoAca",VistaVersamentoAca.class);
		this.NUMERO_AVVISO = new Field("numeroAvviso",java.lang.String.class,"VistaVersamentoAca",VistaVersamentoAca.class);
		this.DATA_ULTIMA_MODIFICA_ACA = new Field("dataUltimaModificaAca",java.util.Date.class,"VistaVersamentoAca",VistaVersamentoAca.class);
		this.DATA_ULTIMA_COMUNICAZIONE_ACA = new Field("dataUltimaComunicazioneAca",java.util.Date.class,"VistaVersamentoAca",VistaVersamentoAca.class);
	
	}
	
	public VistaVersamentoAcaModel(IField father){
	
		super(father);
	
		this.COD_VERSAMENTO_ENTE = new ComplexField(father,"codVersamentoEnte",java.lang.String.class,"VistaVersamentoAca",VistaVersamentoAca.class);
		this.COD_DOMINIO = new ComplexField(father,"codDominio",java.lang.String.class,"VistaVersamentoAca",VistaVersamentoAca.class);
		this.COD_APPLICAZIONE = new ComplexField(father,"codApplicazione",java.lang.String.class,"VistaVersamentoAca",VistaVersamentoAca.class);
		this.IMPORTO_TOTALE = new ComplexField(father,"importoTotale",double.class,"VistaVersamentoAca",VistaVersamentoAca.class);
		this.STATO_VERSAMENTO = new ComplexField(father,"statoVersamento",java.lang.String.class,"VistaVersamentoAca",VistaVersamentoAca.class);
		this.DATA_VALIDITA = new ComplexField(father,"dataValidita",java.util.Date.class,"VistaVersamentoAca",VistaVersamentoAca.class);
		this.DATA_SCADENZA = new ComplexField(father,"dataScadenza",java.util.Date.class,"VistaVersamentoAca",VistaVersamentoAca.class);
		this.CAUSALE_VERSAMENTO = new ComplexField(father,"causaleVersamento",java.lang.String.class,"VistaVersamentoAca",VistaVersamentoAca.class);
		this.DEBITORE_TIPO = new ComplexField(father,"debitoreTipo",java.lang.String.class,"VistaVersamentoAca",VistaVersamentoAca.class);
		this.DEBITORE_IDENTIFICATIVO = new ComplexField(father,"debitoreIdentificativo",java.lang.String.class,"VistaVersamentoAca",VistaVersamentoAca.class);
		this.DEBITORE_ANAGRAFICA = new ComplexField(father,"debitoreAnagrafica",java.lang.String.class,"VistaVersamentoAca",VistaVersamentoAca.class);
		this.IUV_VERSAMENTO = new ComplexField(father,"iuvVersamento",java.lang.String.class,"VistaVersamentoAca",VistaVersamentoAca.class);
		this.NUMERO_AVVISO = new ComplexField(father,"numeroAvviso",java.lang.String.class,"VistaVersamentoAca",VistaVersamentoAca.class);
		this.DATA_ULTIMA_MODIFICA_ACA = new ComplexField(father,"dataUltimaModificaAca",java.util.Date.class,"VistaVersamentoAca",VistaVersamentoAca.class);
		this.DATA_ULTIMA_COMUNICAZIONE_ACA = new ComplexField(father,"dataUltimaComunicazioneAca",java.util.Date.class,"VistaVersamentoAca",VistaVersamentoAca.class);
	
	}
	
	

	public IField COD_VERSAMENTO_ENTE = null;
	 
	public IField COD_DOMINIO = null;
	 
	public IField COD_APPLICAZIONE = null;
	 
	public IField IMPORTO_TOTALE = null;
	 
	public IField STATO_VERSAMENTO = null;
	 
	public IField DATA_VALIDITA = null;
	 
	public IField DATA_SCADENZA = null;
	 
	public IField CAUSALE_VERSAMENTO = null;
	 
	public IField DEBITORE_TIPO = null;
	 
	public IField DEBITORE_IDENTIFICATIVO = null;
	 
	public IField DEBITORE_ANAGRAFICA = null;
	 
	public IField IUV_VERSAMENTO = null;
	 
	public IField NUMERO_AVVISO = null;
	 
	public IField DATA_ULTIMA_MODIFICA_ACA = null;
	 
	public IField DATA_ULTIMA_COMUNICAZIONE_ACA = null;
	 

	@Override
	public Class<VistaVersamentoAca> getModeledClass(){
		return VistaVersamentoAca.class;
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