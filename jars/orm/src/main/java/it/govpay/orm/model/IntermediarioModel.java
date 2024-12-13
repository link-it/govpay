/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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

import it.govpay.orm.Intermediario;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Intermediario 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class IntermediarioModel extends AbstractModel<Intermediario> {

	public IntermediarioModel(){
	
		super();
	
		this.COD_INTERMEDIARIO = new Field("codIntermediario",java.lang.String.class,"Intermediario",Intermediario.class);
		this.COD_CONNETTORE_PDD = new Field("codConnettorePdd",java.lang.String.class,"Intermediario",Intermediario.class);
		this.COD_CONNETTORE_RECUPERO_RT = new Field("codConnettoreRecuperoRT",java.lang.String.class,"Intermediario",Intermediario.class);
		this.COD_CONNETTORE_FTP = new Field("codConnettoreFtp",java.lang.String.class,"Intermediario",Intermediario.class);
		this.DENOMINAZIONE = new Field("denominazione",java.lang.String.class,"Intermediario",Intermediario.class);
		this.PRINCIPAL = new Field("principal",java.lang.String.class,"Intermediario",Intermediario.class);
		this.PRINCIPAL_ORIGINALE = new Field("principalOriginale",java.lang.String.class,"Intermediario",Intermediario.class);
		this.ABILITATO = new Field("abilitato",boolean.class,"Intermediario",Intermediario.class);
	
	}
	
	public IntermediarioModel(IField father){
	
		super(father);
	
		this.COD_INTERMEDIARIO = new ComplexField(father,"codIntermediario",java.lang.String.class,"Intermediario",Intermediario.class);
		this.COD_CONNETTORE_PDD = new ComplexField(father,"codConnettorePdd",java.lang.String.class,"Intermediario",Intermediario.class);
		this.COD_CONNETTORE_RECUPERO_RT = new ComplexField(father,"codConnettoreRecuperoRT",java.lang.String.class,"Intermediario",Intermediario.class);
		this.COD_CONNETTORE_FTP = new ComplexField(father,"codConnettoreFtp",java.lang.String.class,"Intermediario",Intermediario.class);
		this.DENOMINAZIONE = new ComplexField(father,"denominazione",java.lang.String.class,"Intermediario",Intermediario.class);
		this.PRINCIPAL = new ComplexField(father,"principal",java.lang.String.class,"Intermediario",Intermediario.class);
		this.PRINCIPAL_ORIGINALE = new ComplexField(father,"principalOriginale",java.lang.String.class,"Intermediario",Intermediario.class);
		this.ABILITATO = new ComplexField(father,"abilitato",boolean.class,"Intermediario",Intermediario.class);
	
	}
	
	

	public IField COD_INTERMEDIARIO = null;
	 
	public IField COD_CONNETTORE_PDD = null;
	 
	public IField COD_CONNETTORE_RECUPERO_RT = null;
	 
	public IField COD_CONNETTORE_FTP = null;
	 
	public IField DENOMINAZIONE = null;
	 
	public IField PRINCIPAL = null;
	 
	public IField PRINCIPAL_ORIGINALE = null;
	 
	public IField ABILITATO = null;
	 

	@Override
	public Class<Intermediario> getModeledClass(){
		return Intermediario.class;
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
