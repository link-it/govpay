/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
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

import it.govpay.orm.Disponibilita;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Disponibilita 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class DisponibilitaModel extends AbstractModel<Disponibilita> {

	public DisponibilitaModel(){
	
		super();
	
		this.TIPO_PERIODO = new Field("tipoPeriodo",java.lang.String.class,"Disponibilita",Disponibilita.class);
		this.GIORNO = new Field("giorno",java.lang.String.class,"Disponibilita",Disponibilita.class);
		this.FASCE_ORARIE = new Field("fasceOrarie",java.lang.String.class,"Disponibilita",Disponibilita.class);
		this.TIPO_DISPONIBILITA = new Field("tipoDisponibilita",java.lang.String.class,"Disponibilita",Disponibilita.class);
	
	}
	
	public DisponibilitaModel(IField father){
	
		super(father);
	
		this.TIPO_PERIODO = new ComplexField(father,"tipoPeriodo",java.lang.String.class,"Disponibilita",Disponibilita.class);
		this.GIORNO = new ComplexField(father,"giorno",java.lang.String.class,"Disponibilita",Disponibilita.class);
		this.FASCE_ORARIE = new ComplexField(father,"fasceOrarie",java.lang.String.class,"Disponibilita",Disponibilita.class);
		this.TIPO_DISPONIBILITA = new ComplexField(father,"tipoDisponibilita",java.lang.String.class,"Disponibilita",Disponibilita.class);
	
	}
	
	

	public IField TIPO_PERIODO = null;
	 
	public IField GIORNO = null;
	 
	public IField FASCE_ORARIE = null;
	 
	public IField TIPO_DISPONIBILITA = null;
	 

	@Override
	public Class<Disponibilita> getModeledClass(){
		return Disponibilita.class;
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