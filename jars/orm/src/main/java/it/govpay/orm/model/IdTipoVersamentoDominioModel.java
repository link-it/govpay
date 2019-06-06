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

import it.govpay.orm.IdTipoVersamentoDominio;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model IdTipoVersamentoDominio 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class IdTipoVersamentoDominioModel extends AbstractModel<IdTipoVersamentoDominio> {

	public IdTipoVersamentoDominioModel(){
	
		super();
	
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new Field("idDominio",it.govpay.orm.IdDominio.class,"id-tipo-versamento-dominio",IdTipoVersamentoDominio.class));
		this.ID_TIPO_VERSAMENTO = new it.govpay.orm.model.IdTipoVersamentoModel(new Field("idTipoVersamento",it.govpay.orm.IdTipoVersamento.class,"id-tipo-versamento-dominio",IdTipoVersamentoDominio.class));
	
	}
	
	public IdTipoVersamentoDominioModel(IField father){
	
		super(father);
	
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new ComplexField(father,"idDominio",it.govpay.orm.IdDominio.class,"id-tipo-versamento-dominio",IdTipoVersamentoDominio.class));
		this.ID_TIPO_VERSAMENTO = new it.govpay.orm.model.IdTipoVersamentoModel(new ComplexField(father,"idTipoVersamento",it.govpay.orm.IdTipoVersamento.class,"id-tipo-versamento-dominio",IdTipoVersamentoDominio.class));
	
	}
	
	

	public it.govpay.orm.model.IdDominioModel ID_DOMINIO = null;
	 
	public it.govpay.orm.model.IdTipoVersamentoModel ID_TIPO_VERSAMENTO = null;
	 

	@Override
	public Class<IdTipoVersamentoDominio> getModeledClass(){
		return IdTipoVersamentoDominio.class;
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