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
	
		this.TIPO = new Field("tipo",java.lang.String.class,"Tracciato",Tracciato.class);
		this.STATO = new Field("stato",java.lang.String.class,"Tracciato",Tracciato.class);
		this.DESCRIZIONE_STATO = new Field("descrizioneStato",java.lang.String.class,"Tracciato",Tracciato.class);
		this.DATA_CARICAMENTO = new Field("dataCaricamento",java.util.Date.class,"Tracciato",Tracciato.class);
		this.DATA_COMPLETAMENTO = new Field("dataCompletamento",java.util.Date.class,"Tracciato",Tracciato.class);
		this.BEAN_DATI = new Field("beanDati",java.lang.String.class,"Tracciato",Tracciato.class);
		this.FILE_NAME_RICHIESTA = new Field("fileNameRichiesta",java.lang.String.class,"Tracciato",Tracciato.class);
		this.RAW_RICHIESTA = new Field("rawRichiesta",byte[].class,"Tracciato",Tracciato.class);
		this.FILE_NAME_ESITO = new Field("fileNameEsito",java.lang.String.class,"Tracciato",Tracciato.class);
		this.RAW_ESITO = new Field("rawEsito",byte[].class,"Tracciato",Tracciato.class);
	
	}
	
	public TracciatoModel(IField father){
	
		super(father);
	
		this.TIPO = new ComplexField(father,"tipo",java.lang.String.class,"Tracciato",Tracciato.class);
		this.STATO = new ComplexField(father,"stato",java.lang.String.class,"Tracciato",Tracciato.class);
		this.DESCRIZIONE_STATO = new ComplexField(father,"descrizioneStato",java.lang.String.class,"Tracciato",Tracciato.class);
		this.DATA_CARICAMENTO = new ComplexField(father,"dataCaricamento",java.util.Date.class,"Tracciato",Tracciato.class);
		this.DATA_COMPLETAMENTO = new ComplexField(father,"dataCompletamento",java.util.Date.class,"Tracciato",Tracciato.class);
		this.BEAN_DATI = new ComplexField(father,"beanDati",java.lang.String.class,"Tracciato",Tracciato.class);
		this.FILE_NAME_RICHIESTA = new ComplexField(father,"fileNameRichiesta",java.lang.String.class,"Tracciato",Tracciato.class);
		this.RAW_RICHIESTA = new ComplexField(father,"rawRichiesta",byte[].class,"Tracciato",Tracciato.class);
		this.FILE_NAME_ESITO = new ComplexField(father,"fileNameEsito",java.lang.String.class,"Tracciato",Tracciato.class);
		this.RAW_ESITO = new ComplexField(father,"rawEsito",byte[].class,"Tracciato",Tracciato.class);
	
	}
	
	

	public IField TIPO = null;
	 
	public IField STATO = null;
	 
	public IField DESCRIZIONE_STATO = null;
	 
	public IField DATA_CARICAMENTO = null;
	 
	public IField DATA_COMPLETAMENTO = null;
	 
	public IField BEAN_DATI = null;
	 
	public IField FILE_NAME_RICHIESTA = null;
	 
	public IField RAW_RICHIESTA = null;
	 
	public IField FILE_NAME_ESITO = null;
	 
	public IField RAW_ESITO = null;
	 

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