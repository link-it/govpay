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

import it.govpay.orm.Batch;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Batch 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class BatchModel extends AbstractModel<Batch> {

	public BatchModel(){
	
		super();
	
		this.COD_BATCH = new Field("codBatch",java.lang.String.class,"Batch",Batch.class);
		this.NODO = new Field("nodo",java.lang.String.class,"Batch",Batch.class);
		this.INIZIO = new Field("inizio",java.util.Date.class,"Batch",Batch.class);
		this.AGGIORNAMENTO = new Field("aggiornamento",java.util.Date.class,"Batch",Batch.class);
	
	}
	
	public BatchModel(IField father){
	
		super(father);
	
		this.COD_BATCH = new ComplexField(father,"codBatch",java.lang.String.class,"Batch",Batch.class);
		this.NODO = new ComplexField(father,"nodo",java.lang.String.class,"Batch",Batch.class);
		this.INIZIO = new ComplexField(father,"inizio",java.util.Date.class,"Batch",Batch.class);
		this.AGGIORNAMENTO = new ComplexField(father,"aggiornamento",java.util.Date.class,"Batch",Batch.class);
	
	}
	
	

	public IField COD_BATCH = null;
	 
	public IField NODO = null;
	 
	public IField INIZIO = null;
	 
	public IField AGGIORNAMENTO = null;
	 

	@Override
	public Class<Batch> getModeledClass(){
		return Batch.class;
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