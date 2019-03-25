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

import it.govpay.orm.TipoVersamentoDominio;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model TipoVersamentoDominio 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class TipoVersamentoDominioModel extends AbstractModel<TipoVersamentoDominio> {

	public TipoVersamentoDominioModel(){
	
		super();
	
		this.TIPO_VERSAMENTO = new it.govpay.orm.model.TipoVersamentoModel(new Field("TipoVersamento",it.govpay.orm.TipoVersamento.class,"TipoVersamentoDominio",TipoVersamentoDominio.class));
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new Field("idDominio",it.govpay.orm.IdDominio.class,"TipoVersamentoDominio",TipoVersamentoDominio.class));
		this.CODIFICA_IUV = new Field("codificaIuv",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.TIPO = new Field("tipo",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.PAGA_TERZI = new Field("pagaTerzi",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
	
	}
	
	public TipoVersamentoDominioModel(IField father){
	
		super(father);
	
		this.TIPO_VERSAMENTO = new it.govpay.orm.model.TipoVersamentoModel(new ComplexField(father,"TipoVersamento",it.govpay.orm.TipoVersamento.class,"TipoVersamentoDominio",TipoVersamentoDominio.class));
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new ComplexField(father,"idDominio",it.govpay.orm.IdDominio.class,"TipoVersamentoDominio",TipoVersamentoDominio.class));
		this.CODIFICA_IUV = new ComplexField(father,"codificaIuv",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.TIPO = new ComplexField(father,"tipo",java.lang.String.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
		this.PAGA_TERZI = new ComplexField(father,"pagaTerzi",Boolean.class,"TipoVersamentoDominio",TipoVersamentoDominio.class);
	
	}
	
	

	public it.govpay.orm.model.TipoVersamentoModel TIPO_VERSAMENTO = null;
	 
	public it.govpay.orm.model.IdDominioModel ID_DOMINIO = null;
	 
	public IField CODIFICA_IUV = null;
	 
	public IField TIPO = null;
	 
	public IField PAGA_TERZI = null;
	 

	@Override
	public Class<TipoVersamentoDominio> getModeledClass(){
		return TipoVersamentoDominio.class;
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
