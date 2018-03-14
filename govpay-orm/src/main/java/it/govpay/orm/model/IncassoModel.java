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

import it.govpay.orm.Incasso;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Incasso 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class IncassoModel extends AbstractModel<Incasso> {

	public IncassoModel(){
	
		super();
	
		this.TRN = new Field("trn",java.lang.String.class,"Incasso",Incasso.class);
		this.COD_DOMINIO = new Field("codDominio",java.lang.String.class,"Incasso",Incasso.class);
		this.CAUSALE = new Field("causale",java.lang.String.class,"Incasso",Incasso.class);
		this.IMPORTO = new Field("importo",java.lang.Double.class,"Incasso",Incasso.class);
		this.DATA_VALUTA = new Field("dataValuta",java.util.Date.class,"Incasso",Incasso.class);
		this.DATA_CONTABILE = new Field("dataContabile",java.util.Date.class,"Incasso",Incasso.class);
		this.DATA_ORA_INCASSO = new Field("dataOraIncasso",java.util.Date.class,"Incasso",Incasso.class);
		this.NOME_DISPOSITIVO = new Field("nomeDispositivo",java.lang.String.class,"Incasso",Incasso.class);
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new Field("idApplicazione",it.govpay.orm.IdApplicazione.class,"Incasso",Incasso.class));
		this.ID_OPERATORE = new it.govpay.orm.model.IdOperatoreModel(new Field("idOperatore",it.govpay.orm.IdOperatore.class,"Incasso",Incasso.class));
		this.IBAN_ACCREDITO = new Field("ibanAccredito",java.lang.String.class,"Incasso",Incasso.class);
	
	}
	
	public IncassoModel(IField father){
	
		super(father);
	
		this.TRN = new ComplexField(father,"trn",java.lang.String.class,"Incasso",Incasso.class);
		this.COD_DOMINIO = new ComplexField(father,"codDominio",java.lang.String.class,"Incasso",Incasso.class);
		this.CAUSALE = new ComplexField(father,"causale",java.lang.String.class,"Incasso",Incasso.class);
		this.IMPORTO = new ComplexField(father,"importo",java.lang.Double.class,"Incasso",Incasso.class);
		this.DATA_VALUTA = new ComplexField(father,"dataValuta",java.util.Date.class,"Incasso",Incasso.class);
		this.DATA_CONTABILE = new ComplexField(father,"dataContabile",java.util.Date.class,"Incasso",Incasso.class);
		this.DATA_ORA_INCASSO = new ComplexField(father,"dataOraIncasso",java.util.Date.class,"Incasso",Incasso.class);
		this.NOME_DISPOSITIVO = new ComplexField(father,"nomeDispositivo",java.lang.String.class,"Incasso",Incasso.class);
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new ComplexField(father,"idApplicazione",it.govpay.orm.IdApplicazione.class,"Incasso",Incasso.class));
		this.ID_OPERATORE = new it.govpay.orm.model.IdOperatoreModel(new ComplexField(father,"idOperatore",it.govpay.orm.IdOperatore.class,"Incasso",Incasso.class));
		this.IBAN_ACCREDITO = new ComplexField(father,"ibanAccredito",java.lang.String.class,"Incasso",Incasso.class);
	
	}
	
	

	public IField TRN = null;
	 
	public IField COD_DOMINIO = null;
	 
	public IField CAUSALE = null;
	 
	public IField IMPORTO = null;
	 
	public IField DATA_VALUTA = null;
	 
	public IField DATA_CONTABILE = null;
	 
	public IField DATA_ORA_INCASSO = null;
	 
	public IField NOME_DISPOSITIVO = null;
	 
	public it.govpay.orm.model.IdApplicazioneModel ID_APPLICAZIONE = null;
	 
	public it.govpay.orm.model.IdOperatoreModel ID_OPERATORE = null;
	 
	public IField IBAN_ACCREDITO = null;
	 

	@Override
	public Class<Incasso> getModeledClass(){
		return Incasso.class;
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