/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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

import it.govpay.orm.JppaConfig;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model JppaConfig 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JppaConfigModel extends AbstractModel<JppaConfig> {

	public JppaConfigModel(){
	
		super();
	
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new Field("idDominio",it.govpay.orm.IdDominio.class,"JppaConfig",JppaConfig.class));
		this.COD_DOMINIO = new Field("codDominio",java.lang.String.class,"JppaConfig",JppaConfig.class);
		this.COD_CONNETTORE = new Field("codConnettore",java.lang.String.class,"JppaConfig",JppaConfig.class);
		this.ABILITATO = new Field("abilitato",boolean.class,"JppaConfig",JppaConfig.class);
		this.DATA_ULTIMA_RT = new Field("dataUltimaRt",java.util.Date.class,"JppaConfig",JppaConfig.class);
	
	}
	
	public JppaConfigModel(IField father){
	
		super(father);
	
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new ComplexField(father,"idDominio",it.govpay.orm.IdDominio.class,"JppaConfig",JppaConfig.class));
		this.COD_DOMINIO = new ComplexField(father,"codDominio",java.lang.String.class,"JppaConfig",JppaConfig.class);
		this.COD_CONNETTORE = new ComplexField(father,"codConnettore",java.lang.String.class,"JppaConfig",JppaConfig.class);
		this.ABILITATO = new ComplexField(father,"abilitato",boolean.class,"JppaConfig",JppaConfig.class);
		this.DATA_ULTIMA_RT = new ComplexField(father,"dataUltimaRt",java.util.Date.class,"JppaConfig",JppaConfig.class);
	
	}
	
	

	public it.govpay.orm.model.IdDominioModel ID_DOMINIO = null;
	 
	public IField COD_DOMINIO = null;
	 
	public IField COD_CONNETTORE = null;
	 
	public IField ABILITATO = null;
	 
	public IField DATA_ULTIMA_RT = null;
	 

	@Override
	public Class<JppaConfig> getModeledClass(){
		return JppaConfig.class;
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