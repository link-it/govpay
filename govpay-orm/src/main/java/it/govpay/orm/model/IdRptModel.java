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

import it.govpay.orm.IdRpt;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model IdRpt 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class IdRptModel extends AbstractModel<IdRpt> {

	public IdRptModel(){
	
		super();
	
		this.COD_MSG_RICHIESTA = new Field("codMsgRichiesta",java.lang.String.class,"id-rpt",IdRpt.class);
		this.COD_DOMINIO = new Field("codDominio",java.lang.String.class,"id-rpt",IdRpt.class);
		this.IUV = new Field("iuv",java.lang.String.class,"id-rpt",IdRpt.class);
	
	}
	
	public IdRptModel(IField father){
	
		super(father);
	
		this.COD_MSG_RICHIESTA = new ComplexField(father,"codMsgRichiesta",java.lang.String.class,"id-rpt",IdRpt.class);
		this.COD_DOMINIO = new ComplexField(father,"codDominio",java.lang.String.class,"id-rpt",IdRpt.class);
		this.IUV = new ComplexField(father,"iuv",java.lang.String.class,"id-rpt",IdRpt.class);
	
	}
	
	

	public IField COD_MSG_RICHIESTA = null;
	 
	public IField COD_DOMINIO = null;
	 
	public IField IUV = null;
	 

	@Override
	public Class<IdRpt> getModeledClass(){
		return IdRpt.class;
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