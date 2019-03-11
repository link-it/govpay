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

import it.govpay.orm.UtenzaTipoTributo;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model UtenzaTipoTributo 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class UtenzaTipoTributoModel extends AbstractModel<UtenzaTipoTributo> {

	public UtenzaTipoTributoModel(){
	
		super();
	
		this.ID_UTENZA = new it.govpay.orm.model.IdUtenzaModel(new Field("idUtenza",it.govpay.orm.IdUtenza.class,"UtenzaTipoTributo",UtenzaTipoTributo.class));
		this.ID_TIPO_TRIBUTO = new it.govpay.orm.model.IdTipoTributoModel(new Field("idTipoTributo",it.govpay.orm.IdTipoTributo.class,"UtenzaTipoTributo",UtenzaTipoTributo.class));
	
	}
	
	public UtenzaTipoTributoModel(IField father){
	
		super(father);
	
		this.ID_UTENZA = new it.govpay.orm.model.IdUtenzaModel(new ComplexField(father,"idUtenza",it.govpay.orm.IdUtenza.class,"UtenzaTipoTributo",UtenzaTipoTributo.class));
		this.ID_TIPO_TRIBUTO = new it.govpay.orm.model.IdTipoTributoModel(new ComplexField(father,"idTipoTributo",it.govpay.orm.IdTipoTributo.class,"UtenzaTipoTributo",UtenzaTipoTributo.class));
	
	}
	
	

	public it.govpay.orm.model.IdUtenzaModel ID_UTENZA = null;
	 
	public it.govpay.orm.model.IdTipoTributoModel ID_TIPO_TRIBUTO = null;
	 

	@Override
	public Class<UtenzaTipoTributo> getModeledClass(){
		return UtenzaTipoTributo.class;
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