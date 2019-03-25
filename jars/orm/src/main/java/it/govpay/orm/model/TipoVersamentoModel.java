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

import it.govpay.orm.TipoVersamento;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model TipoVersamento 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class TipoVersamentoModel extends AbstractModel<TipoVersamento> {

	public TipoVersamentoModel(){
	
		super();
	
		this.COD_TIPO_VERSAMENTO = new Field("codTipoVersamento",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.DESCRIZIONE = new Field("descrizione",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.CODIFICA_IUV = new Field("codificaIuv",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.TIPO = new Field("tipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PAGA_TERZI = new Field("pagaTerzi",boolean.class,"TipoVersamento",TipoVersamento.class);
	
	}
	
	public TipoVersamentoModel(IField father){
	
		super(father);
	
		this.COD_TIPO_VERSAMENTO = new ComplexField(father,"codTipoVersamento",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.DESCRIZIONE = new ComplexField(father,"descrizione",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.CODIFICA_IUV = new ComplexField(father,"codificaIuv",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.TIPO = new ComplexField(father,"tipo",java.lang.String.class,"TipoVersamento",TipoVersamento.class);
		this.PAGA_TERZI = new ComplexField(father,"pagaTerzi",boolean.class,"TipoVersamento",TipoVersamento.class);
	
	}
	
	

	public IField COD_TIPO_VERSAMENTO = null;
	 
	public IField DESCRIZIONE = null;
	 
	public IField CODIFICA_IUV = null;
	 
	public IField TIPO = null;
	 
	public IField PAGA_TERZI = null;
	 

	@Override
	public Class<TipoVersamento> getModeledClass(){
		return TipoVersamento.class;
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
