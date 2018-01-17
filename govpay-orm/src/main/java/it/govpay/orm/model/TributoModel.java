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

import it.govpay.orm.Tributo;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Tributo 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class TributoModel extends AbstractModel<Tributo> {

	public TributoModel(){
	
		super();
	
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new Field("idDominio",it.govpay.orm.IdDominio.class,"Tributo",Tributo.class));
		this.ABILITATO = new Field("abilitato",boolean.class,"Tributo",Tributo.class);
		this.ID_IBAN_ACCREDITO = new it.govpay.orm.model.IdIbanAccreditoModel(new Field("idIbanAccredito",it.govpay.orm.IdIbanAccredito.class,"Tributo",Tributo.class));
		this.ID_IBAN_ACCREDITO_ALTERNATIVO = new it.govpay.orm.model.IdIbanAccreditoModel(new Field("idIbanAccreditoAlternativo",it.govpay.orm.IdIbanAccredito.class,"Tributo",Tributo.class));
		this.TIPO_CONTABILITA = new Field("tipoContabilita",java.lang.String.class,"Tributo",Tributo.class);
		this.CODICE_CONTABILITA = new Field("codiceContabilita",java.lang.String.class,"Tributo",Tributo.class);
		this.TIPO_TRIBUTO = new it.govpay.orm.model.TipoTributoModel(new Field("tipoTributo",it.govpay.orm.TipoTributo.class,"Tributo",Tributo.class));
		this.COD_TRIBUTO_IUV = new Field("codTributoIuv",java.lang.String.class,"Tributo",Tributo.class);
	
	}
	
	public TributoModel(IField father){
	
		super(father);
	
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new ComplexField(father,"idDominio",it.govpay.orm.IdDominio.class,"Tributo",Tributo.class));
		this.ABILITATO = new ComplexField(father,"abilitato",boolean.class,"Tributo",Tributo.class);
		this.ID_IBAN_ACCREDITO = new it.govpay.orm.model.IdIbanAccreditoModel(new ComplexField(father,"idIbanAccredito",it.govpay.orm.IdIbanAccredito.class,"Tributo",Tributo.class));
		this.ID_IBAN_ACCREDITO_ALTERNATIVO = new it.govpay.orm.model.IdIbanAccreditoModel(new ComplexField(father,"idIbanAccreditoAlternativo",it.govpay.orm.IdIbanAccredito.class,"Tributo",Tributo.class));
		this.TIPO_CONTABILITA = new ComplexField(father,"tipoContabilita",java.lang.String.class,"Tributo",Tributo.class);
		this.CODICE_CONTABILITA = new ComplexField(father,"codiceContabilita",java.lang.String.class,"Tributo",Tributo.class);
		this.TIPO_TRIBUTO = new it.govpay.orm.model.TipoTributoModel(new ComplexField(father,"tipoTributo",it.govpay.orm.TipoTributo.class,"Tributo",Tributo.class));
		this.COD_TRIBUTO_IUV = new ComplexField(father,"codTributoIuv",java.lang.String.class,"Tributo",Tributo.class);
	
	}
	
	

	public it.govpay.orm.model.IdDominioModel ID_DOMINIO = null;
	 
	public IField ABILITATO = null;
	 
	public it.govpay.orm.model.IdIbanAccreditoModel ID_IBAN_ACCREDITO = null;
	 
	public it.govpay.orm.model.IdIbanAccreditoModel ID_IBAN_ACCREDITO_ALTERNATIVO = null;
	 
	public IField TIPO_CONTABILITA = null;
	 
	public IField CODICE_CONTABILITA = null;
	 
	public it.govpay.orm.model.TipoTributoModel TIPO_TRIBUTO = null;
	 
	public IField COD_TRIBUTO_IUV = null;
	 

	@Override
	public Class<Tributo> getModeledClass(){
		return Tributo.class;
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