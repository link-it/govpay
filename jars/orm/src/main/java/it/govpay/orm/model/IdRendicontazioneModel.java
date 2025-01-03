/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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

import it.govpay.orm.IdRendicontazione;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model IdRendicontazione 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class IdRendicontazioneModel extends AbstractModel<IdRendicontazione> {

	public IdRendicontazioneModel(){
	
		super();
	
		this.ID_RENDICONTAZIONE = new Field("idRendicontazione",long.class,"id-rendicontazione",IdRendicontazione.class);
		this.IUV = new Field("iuv",java.lang.String.class,"id-rendicontazione",IdRendicontazione.class);
	
	}
	
	public IdRendicontazioneModel(IField father){
	
		super(father);
	
		this.ID_RENDICONTAZIONE = new ComplexField(father,"idRendicontazione",long.class,"id-rendicontazione",IdRendicontazione.class);
		this.IUV = new ComplexField(father,"iuv",java.lang.String.class,"id-rendicontazione",IdRendicontazione.class);
	
	}
	
	

	public IField ID_RENDICONTAZIONE = null;
	 
	public IField IUV = null;
	 

	@Override
	public Class<IdRendicontazione> getModeledClass(){
		return IdRendicontazione.class;
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