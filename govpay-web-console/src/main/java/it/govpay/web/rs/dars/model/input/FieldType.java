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
package it.govpay.web.rs.dars.model.input;

import java.io.Serializable;
import org.openspcoop2.generic_project.beans.IEnumeration;

/**
 * FieldType Enum che elenca i tipi di input disponibili.
 * 
 * @author Pintori Giuliano (pintori@link.it)
 */
public enum FieldType implements IEnumeration , Serializable , Cloneable{

	INPUT_TEXT ("InputText"), INPUT_TEXT_AREA ("InputTextArea"),INPUT_SECRET ("InputSecret") , SELECT_LIST("SelectList"),
	INPUT_DATE("InputDate"), INPUT_NUMBER ("InputNumber") , CHECK_BUTTON ("CheckButton")  
	, RADIO_BUTTON("RadioButton"), MULTI_SELECT_LIST ("MultiSelectList"), INPUT_FILE("InputFile");


	/** Value */
	private String value;
	@Override
	public String getValue()
	{
		return this.value;
	}


	/** Official Constructor */
	FieldType(String value)
	{
		this.value = value;
	}

	@Override
	public String toString(){
		return this.value;
	}
	public boolean equals(FieldType object){
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

	/** Utilities */

	public static String[] toArray(){
		String[] res = new String[values().length];
		int i=0;
		for (FieldType tmp : values()) {
			res[i]=tmp.getValue();
			i++;
		}
		return res;
	}	
	public static String[] toStringArray(){
		String[] res = new String[values().length];
		int i=0;
		for (FieldType tmp : values()) {
			res[i]=tmp.toString();
			i++;
		}
		return res;
	}
	public static String[] toEnumNameArray(){
		String[] res = new String[values().length];
		int i=0;
		for (FieldType tmp : values()) {
			res[i]=tmp.name();
			i++;
		}
		return res;
	}

	public static boolean contains(String value){
		return toEnumConstant(value)!=null;
	}

	public static FieldType toEnumConstant(String value){
		FieldType res = null;
		if(FieldType.INPUT_TEXT.getValue().equals(value)){
			res = FieldType.INPUT_TEXT;
		}else if(FieldType.INPUT_TEXT_AREA.getValue().equals(value)){
			res = FieldType.INPUT_TEXT_AREA;
		}else if(FieldType.INPUT_SECRET.getValue().equals(value)){
			res = FieldType.INPUT_SECRET;
		}else if(FieldType.CHECK_BUTTON.getValue().equals(value)){
			res = FieldType.CHECK_BUTTON;
		}else if(FieldType.INPUT_DATE.getValue().equals(value)){
			res = FieldType.INPUT_DATE;
		}else if(FieldType.INPUT_NUMBER.getValue().equals(value)){
			res = FieldType.INPUT_NUMBER;
		}else if(FieldType.INPUT_FILE.getValue().equals(value)){
			res = FieldType.INPUT_FILE;
		}else if(FieldType.SELECT_LIST.getValue().equals(value)){
			res = FieldType.SELECT_LIST;
		}else if(FieldType.RADIO_BUTTON.getValue().equals(value)){
			res = FieldType.RADIO_BUTTON;
		}else if(FieldType.MULTI_SELECT_LIST.getValue().equals(value)){
			res = FieldType.MULTI_SELECT_LIST;
		} 
		return res;
	}

	public static IEnumeration toEnumConstantFromString(String value){
		FieldType res = null;
		if(FieldType.INPUT_TEXT.toString().equals(value)){
			res = FieldType.INPUT_TEXT;
		}else if(FieldType.INPUT_TEXT_AREA.toString().equals(value)){
			res = FieldType.INPUT_TEXT_AREA;
		}else if(FieldType.INPUT_SECRET.toString().equals(value)){
			res = FieldType.INPUT_SECRET;
		}else if(FieldType.CHECK_BUTTON.toString().equals(value)){
			res = FieldType.CHECK_BUTTON;
		}else if(FieldType.INPUT_DATE.toString().equals(value)){
			res = FieldType.INPUT_DATE;
		}else if(FieldType.INPUT_NUMBER.toString().equals(value)){
			res = FieldType.INPUT_NUMBER;
		}else if(FieldType.SELECT_LIST.toString().equals(value)){
			res = FieldType.SELECT_LIST;
		}else if(FieldType.RADIO_BUTTON.toString().equals(value)){
			res = FieldType.RADIO_BUTTON;
		}else if(FieldType.MULTI_SELECT_LIST.toString().equals(value)){
			res = FieldType.MULTI_SELECT_LIST;
		}else if(FieldType.INPUT_FILE.toString().equals(value)){
			res = FieldType.INPUT_FILE;
		}
		return res;
	}
}
