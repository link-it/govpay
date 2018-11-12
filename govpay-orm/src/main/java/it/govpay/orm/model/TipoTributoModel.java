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

import it.govpay.orm.TipoTributo;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model TipoTributo 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class TipoTributoModel extends AbstractModel<TipoTributo> {

	public TipoTributoModel(){
	
		super();
	
		this.COD_TRIBUTO = new Field("codTributo",java.lang.String.class,"TipoTributo",TipoTributo.class);
		this.DESCRIZIONE = new Field("descrizione",java.lang.String.class,"TipoTributo",TipoTributo.class);
		this.TIPO_CONTABILITA = new Field("tipoContabilita",java.lang.String.class,"TipoTributo",TipoTributo.class);
		this.COD_CONTABILITA = new Field("codContabilita",java.lang.String.class,"TipoTributo",TipoTributo.class);
		this.COD_TRIBUTO_IUV = new Field("codTributoIuv",java.lang.String.class,"TipoTributo",TipoTributo.class);
		this.ON_LINE = new Field("onLine",boolean.class,"TipoTributo",TipoTributo.class);
		this.PAGA_TERZI = new Field("pagaTerzi",boolean.class,"TipoTributo",TipoTributo.class);
	
	}
	
	public TipoTributoModel(IField father){
	
		super(father);
	
		this.COD_TRIBUTO = new ComplexField(father,"codTributo",java.lang.String.class,"TipoTributo",TipoTributo.class);
		this.DESCRIZIONE = new ComplexField(father,"descrizione",java.lang.String.class,"TipoTributo",TipoTributo.class);
		this.TIPO_CONTABILITA = new ComplexField(father,"tipoContabilita",java.lang.String.class,"TipoTributo",TipoTributo.class);
		this.COD_CONTABILITA = new ComplexField(father,"codContabilita",java.lang.String.class,"TipoTributo",TipoTributo.class);
		this.COD_TRIBUTO_IUV = new ComplexField(father,"codTributoIuv",java.lang.String.class,"TipoTributo",TipoTributo.class);
		this.ON_LINE = new ComplexField(father,"onLine",boolean.class,"TipoTributo",TipoTributo.class);
		this.PAGA_TERZI = new ComplexField(father,"pagaTerzi",boolean.class,"TipoTributo",TipoTributo.class);
	
	}
	
	

	public IField COD_TRIBUTO = null;
	 
	public IField DESCRIZIONE = null;
	 
	public IField TIPO_CONTABILITA = null;
	 
	public IField COD_CONTABILITA = null;
	 
	public IField COD_TRIBUTO_IUV = null;
	 
	public IField ON_LINE = null;
	 
	public IField PAGA_TERZI = null;
	 

	@Override
	public Class<TipoTributo> getModeledClass(){
		return TipoTributo.class;
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
