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

import it.govpay.orm.UtenzaTipoVersamento;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model UtenzaTipoVersamento 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class UtenzaTipoVersamentoModel extends AbstractModel<UtenzaTipoVersamento> {

	public UtenzaTipoVersamentoModel(){
	
		super();
	
		this.ID_UTENZA = new it.govpay.orm.model.IdUtenzaModel(new Field("idUtenza",it.govpay.orm.IdUtenza.class,"UtenzaTipoVersamento",UtenzaTipoVersamento.class));
		this.ID_TIPO_VERSAMENTO = new it.govpay.orm.model.IdTipoVersamentoModel(new Field("idTipoVersamento",it.govpay.orm.IdTipoVersamento.class,"UtenzaTipoVersamento",UtenzaTipoVersamento.class));
	
	}
	
	public UtenzaTipoVersamentoModel(IField father){
	
		super(father);
	
		this.ID_UTENZA = new it.govpay.orm.model.IdUtenzaModel(new ComplexField(father,"idUtenza",it.govpay.orm.IdUtenza.class,"UtenzaTipoVersamento",UtenzaTipoVersamento.class));
		this.ID_TIPO_VERSAMENTO = new it.govpay.orm.model.IdTipoVersamentoModel(new ComplexField(father,"idTipoVersamento",it.govpay.orm.IdTipoVersamento.class,"UtenzaTipoVersamento",UtenzaTipoVersamento.class));
	
	}
	
	

	public it.govpay.orm.model.IdUtenzaModel ID_UTENZA = null;
	 
	public it.govpay.orm.model.IdTipoVersamentoModel ID_TIPO_VERSAMENTO = null;
	 

	@Override
	public Class<UtenzaTipoVersamento> getModeledClass(){
		return UtenzaTipoVersamento.class;
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