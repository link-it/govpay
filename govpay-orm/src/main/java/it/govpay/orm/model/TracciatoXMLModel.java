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

import it.govpay.orm.TracciatoXML;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model TracciatoXML 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class TracciatoXMLModel extends AbstractModel<TracciatoXML> {

	public TracciatoXMLModel(){
	
		super();
	
		this.TIPO_TRACCIATO = new Field("tipoTracciato",java.lang.String.class,"TracciatoXML",TracciatoXML.class);
		this.COD_MESSAGGIO = new Field("codMessaggio",java.lang.String.class,"TracciatoXML",TracciatoXML.class);
		this.DATA_ORA_CREAZIONE = new Field("dataOraCreazione",java.util.Date.class,"TracciatoXML",TracciatoXML.class);
		this.XML = new Field("xml",byte[].class,"TracciatoXML",TracciatoXML.class);
	
	}
	
	public TracciatoXMLModel(IField father){
	
		super(father);
	
		this.TIPO_TRACCIATO = new ComplexField(father,"tipoTracciato",java.lang.String.class,"TracciatoXML",TracciatoXML.class);
		this.COD_MESSAGGIO = new ComplexField(father,"codMessaggio",java.lang.String.class,"TracciatoXML",TracciatoXML.class);
		this.DATA_ORA_CREAZIONE = new ComplexField(father,"dataOraCreazione",java.util.Date.class,"TracciatoXML",TracciatoXML.class);
		this.XML = new ComplexField(father,"xml",byte[].class,"TracciatoXML",TracciatoXML.class);
	
	}
	
	

	public IField TIPO_TRACCIATO = null;
	 
	public IField COD_MESSAGGIO = null;
	 
	public IField DATA_ORA_CREAZIONE = null;
	 
	public IField XML = null;
	 

	@Override
	public Class<TracciatoXML> getModeledClass(){
		return TracciatoXML.class;
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