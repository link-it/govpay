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

import it.govpay.orm.Applicazione;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Applicazione 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class ApplicazioneModel extends AbstractModel<Applicazione> {

	public ApplicazioneModel(){
	
		super();
	
		this.ID_UTENZA = new it.govpay.orm.model.IdUtenzaModel(new Field("idUtenza",it.govpay.orm.IdUtenza.class,"Applicazione",Applicazione.class));
		this.COD_APPLICAZIONE = new Field("codApplicazione",java.lang.String.class,"Applicazione",Applicazione.class);
		this.AUTO_IUV = new Field("autoIUV",boolean.class,"Applicazione",Applicazione.class);
		this.ABILITATO = new Field("abilitato",boolean.class,"Applicazione",Applicazione.class);
		this.FIRMA_RICEVUTA = new Field("firmaRicevuta",java.lang.String.class,"Applicazione",Applicazione.class);
		this.COD_CONNETTORE_ESITO = new Field("codConnettoreEsito",java.lang.String.class,"Applicazione",Applicazione.class);
		this.COD_CONNETTORE_VERIFICA = new Field("codConnettoreVerifica",java.lang.String.class,"Applicazione",Applicazione.class);
		this.VERSIONE = new Field("versione",java.lang.String.class,"Applicazione",Applicazione.class);
		this.TRUSTED = new Field("trusted",boolean.class,"Applicazione",Applicazione.class);
		this.COD_APPLICAZIONE_IUV = new Field("codApplicazioneIuv",java.lang.String.class,"Applicazione",Applicazione.class);
		this.REG_EXP = new Field("regExp",java.lang.String.class,"Applicazione",Applicazione.class);
	
	}
	
	public ApplicazioneModel(IField father){
	
		super(father);
	
		this.ID_UTENZA = new it.govpay.orm.model.IdUtenzaModel(new ComplexField(father,"idUtenza",it.govpay.orm.IdUtenza.class,"Applicazione",Applicazione.class));
		this.COD_APPLICAZIONE = new ComplexField(father,"codApplicazione",java.lang.String.class,"Applicazione",Applicazione.class);
		this.AUTO_IUV = new ComplexField(father,"autoIUV",boolean.class,"Applicazione",Applicazione.class);
		this.ABILITATO = new ComplexField(father,"abilitato",boolean.class,"Applicazione",Applicazione.class);
		this.FIRMA_RICEVUTA = new ComplexField(father,"firmaRicevuta",java.lang.String.class,"Applicazione",Applicazione.class);
		this.COD_CONNETTORE_ESITO = new ComplexField(father,"codConnettoreEsito",java.lang.String.class,"Applicazione",Applicazione.class);
		this.COD_CONNETTORE_VERIFICA = new ComplexField(father,"codConnettoreVerifica",java.lang.String.class,"Applicazione",Applicazione.class);
		this.VERSIONE = new ComplexField(father,"versione",java.lang.String.class,"Applicazione",Applicazione.class);
		this.TRUSTED = new ComplexField(father,"trusted",boolean.class,"Applicazione",Applicazione.class);
		this.COD_APPLICAZIONE_IUV = new ComplexField(father,"codApplicazioneIuv",java.lang.String.class,"Applicazione",Applicazione.class);
		this.REG_EXP = new ComplexField(father,"regExp",java.lang.String.class,"Applicazione",Applicazione.class);
	
	}
	
	

	public it.govpay.orm.model.IdUtenzaModel ID_UTENZA = null;
	 
	public IField COD_APPLICAZIONE = null;
	 
	public IField AUTO_IUV = null;
	 
	public IField ABILITATO = null;
	 
	public IField FIRMA_RICEVUTA = null;
	 
	public IField COD_CONNETTORE_ESITO = null;
	 
	public IField COD_CONNETTORE_VERIFICA = null;
	 
	public IField VERSIONE = null;
	 
	public IField TRUSTED = null;
	 
	public IField COD_APPLICAZIONE_IUV = null;
	 
	public IField REG_EXP = null;
	 

	@Override
	public Class<Applicazione> getModeledClass(){
		return Applicazione.class;
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