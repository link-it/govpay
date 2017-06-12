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
package it.govpay.orm.constants;

import java.io.Serializable;
import java.util.List;

import org.openspcoop2.generic_project.beans.IEnumeration;
import org.openspcoop2.generic_project.exception.NotFoundException;

/**     
 * Enumeration dell'elemento StatoType xsd (tipo:string) 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
@javax.xml.bind.annotation.XmlType(name = "StatoType")
@javax.xml.bind.annotation.XmlEnum(String.class)
public enum StatoType implements IEnumeration , Serializable , Cloneable {

	@javax.xml.bind.annotation.XmlEnumValue("ANNULLATO")
	ANNULLATO ("ANNULLATO"),
	@javax.xml.bind.annotation.XmlEnumValue("NUOVO")
	NUOVO ("NUOVO"),
	@javax.xml.bind.annotation.XmlEnumValue("IN_VALIDAZIONE")
	IN_VALIDAZIONE ("IN_VALIDAZIONE"),
	@javax.xml.bind.annotation.XmlEnumValue("VALIDAZIONE_OK")
	VALIDAZIONE_OK ("VALIDAZIONE_OK"),
	@javax.xml.bind.annotation.XmlEnumValue("VALIDAZIONE_KO")
	VALIDAZIONE_KO ("VALIDAZIONE_KO"),
	@javax.xml.bind.annotation.XmlEnumValue("IN_CARICAMENTO")
	IN_CARICAMENTO ("IN_CARICAMENTO"),
	@javax.xml.bind.annotation.XmlEnumValue("CARICAMENTO_OK")
	CARICAMENTO_OK ("CARICAMENTO_OK"),
	@javax.xml.bind.annotation.XmlEnumValue("CARICAMENTO_KO")
	CARICAMENTO_KO ("CARICAMENTO_KO");
	
	
	/** Value */
	private String value;
	@Override
	public String getValue()
	{
		return this.value;
	}


	/** Official Constructor */
	StatoType(String value)
	{
		this.value = value;
	}


	
	@Override
	public String toString(){
		return this.value;
	}
	public boolean equals(StatoType object){
		if(object==null)
			return false;
		if(object.getValue()==null)
			return false;
		return object.getValue().equals(this.getValue());	
	}
	public boolean equals(String object){
		if(object==null)
			return false;
		return object.equals(this.getValue());	
	}
	
		
	
	/** compatibility with the generated bean (reflection) */
	public boolean equals(Object object,List<String> fieldsNotCheck){
		if( !(object instanceof StatoType) ){
			throw new RuntimeException("Wrong type: "+object.getClass().getName());
		}
		return this.equals(((StatoType)object));
	}
	public String toString(boolean reportHTML){
		return toString();
	}
  	public String toString(boolean reportHTML,List<String> fieldsNotIncluded){
  		return toString();
  	}
  	public String diff(Object object,StringBuffer bf,boolean reportHTML){
		return bf.toString();
	}
	public String diff(Object object,StringBuffer bf,boolean reportHTML,List<String> fieldsNotIncluded){
		return bf.toString();
	}
	
	
	/** Utilities */
	
	public static String[] toArray(){
		String[] res = new String[values().length];
		int i=0;
		for (StatoType tmp : values()) {
			res[i]=tmp.getValue();
			i++;
		}
		return res;
	}	
	public static String[] toStringArray(){
		String[] res = new String[values().length];
		int i=0;
		for (StatoType tmp : values()) {
			res[i]=tmp.toString();
			i++;
		}
		return res;
	}
	public static String[] toEnumNameArray(){
		String[] res = new String[values().length];
		int i=0;
		for (StatoType tmp : values()) {
			res[i]=tmp.name();
			i++;
		}
		return res;
	}
	
	public static boolean contains(String value){
		return toEnumConstant(value)!=null;
	}
	
	public static StatoType toEnumConstant(String value){
		try{
			return toEnumConstant(value,false);
		}catch(NotFoundException notFound){
			return null;
		}
	}
	public static StatoType toEnumConstant(String value, boolean throwNotFoundException) throws NotFoundException{
		StatoType res = null;
		for (StatoType tmp : values()) {
			if(tmp.getValue().equals(value)){
				res = tmp;
				break;
			}
		}
		if(res==null && throwNotFoundException){
			throw new NotFoundException("Enum with value ["+value+"] not found");
		}
		return res;
	}
	
	public static IEnumeration toEnumConstantFromString(String value){
		try{
			return toEnumConstantFromString(value,false);
		}catch(NotFoundException notFound){
			return null;
		}
	}
	public static IEnumeration toEnumConstantFromString(String value, boolean throwNotFoundException) throws NotFoundException{
		StatoType res = null;
		for (StatoType tmp : values()) {
			if(tmp.toString().equals(value)){
				res = tmp;
				break;
			}
		}
		if(res==null && throwNotFoundException){
			throw new NotFoundException("Enum with value ["+value+"] not found");
		}
		return res;
	}
}
