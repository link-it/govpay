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

import it.govpay.orm.Psp;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Psp 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class PspModel extends AbstractModel<Psp> {

	public PspModel(){
	
		super();
	
		this.COD_PSP = new Field("codPsp",java.lang.String.class,"Psp",Psp.class);
		this.RAGIONE_SOCIALE = new Field("ragioneSociale",java.lang.String.class,"Psp",Psp.class);
		this.URL_INFO = new Field("urlInfo",java.lang.String.class,"Psp",Psp.class);
		this.ABILITATO = new Field("abilitato",boolean.class,"Psp",Psp.class);
		this.STORNO = new Field("storno",boolean.class,"Psp",Psp.class);
		this.MARCA_BOLLO = new Field("marcaBollo",boolean.class,"Psp",Psp.class);
	
	}
	
	public PspModel(IField father){
	
		super(father);
	
		this.COD_PSP = new ComplexField(father,"codPsp",java.lang.String.class,"Psp",Psp.class);
		this.RAGIONE_SOCIALE = new ComplexField(father,"ragioneSociale",java.lang.String.class,"Psp",Psp.class);
		this.URL_INFO = new ComplexField(father,"urlInfo",java.lang.String.class,"Psp",Psp.class);
		this.ABILITATO = new ComplexField(father,"abilitato",boolean.class,"Psp",Psp.class);
		this.STORNO = new ComplexField(father,"storno",boolean.class,"Psp",Psp.class);
		this.MARCA_BOLLO = new ComplexField(father,"marcaBollo",boolean.class,"Psp",Psp.class);
	
	}
	
	

	public IField COD_PSP = null;
	 
	public IField RAGIONE_SOCIALE = null;
	 
	public IField URL_INFO = null;
	 
	public IField ABILITATO = null;
	 
	public IField STORNO = null;
	 
	public IField MARCA_BOLLO = null;
	 

	@Override
	public Class<Psp> getModeledClass(){
		return Psp.class;
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