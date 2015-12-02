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

import it.govpay.orm.SLA;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model SLA 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class SLAModel extends AbstractModel<SLA> {

	public SLAModel(){
	
		super();
	
		this.DESCRIZIONE = new Field("descrizione",java.lang.String.class,"SLA",SLA.class);
		this.TIPO_EVENTO_INIZIALE = new Field("tipoEventoIniziale",java.lang.String.class,"SLA",SLA.class);
		this.SOTTOTIPO_EVENTO_INIZIALE = new Field("sottotipoEventoIniziale",java.lang.String.class,"SLA",SLA.class);
		this.TIPO_EVENTO_FINALE = new Field("tipoEventoFinale",java.lang.String.class,"SLA",SLA.class);
		this.SOTTOTIPO_EVENTO_FINALE = new Field("sottotipoEventoFinale",java.lang.String.class,"SLA",SLA.class);
		this.TEMPO_A = new Field("tempoA",long.class,"SLA",SLA.class);
		this.TEMPO_B = new Field("tempoB",long.class,"SLA",SLA.class);
		this.TOLLERANZA_A = new Field("tolleranzaA",double.class,"SLA",SLA.class);
		this.TOLLERANZA_B = new Field("tolleranzaB",double.class,"SLA",SLA.class);
	
	}
	
	public SLAModel(IField father){
	
		super(father);
	
		this.DESCRIZIONE = new ComplexField(father,"descrizione",java.lang.String.class,"SLA",SLA.class);
		this.TIPO_EVENTO_INIZIALE = new ComplexField(father,"tipoEventoIniziale",java.lang.String.class,"SLA",SLA.class);
		this.SOTTOTIPO_EVENTO_INIZIALE = new ComplexField(father,"sottotipoEventoIniziale",java.lang.String.class,"SLA",SLA.class);
		this.TIPO_EVENTO_FINALE = new ComplexField(father,"tipoEventoFinale",java.lang.String.class,"SLA",SLA.class);
		this.SOTTOTIPO_EVENTO_FINALE = new ComplexField(father,"sottotipoEventoFinale",java.lang.String.class,"SLA",SLA.class);
		this.TEMPO_A = new ComplexField(father,"tempoA",long.class,"SLA",SLA.class);
		this.TEMPO_B = new ComplexField(father,"tempoB",long.class,"SLA",SLA.class);
		this.TOLLERANZA_A = new ComplexField(father,"tolleranzaA",double.class,"SLA",SLA.class);
		this.TOLLERANZA_B = new ComplexField(father,"tolleranzaB",double.class,"SLA",SLA.class);
	
	}
	
	

	public IField DESCRIZIONE = null;
	 
	public IField TIPO_EVENTO_INIZIALE = null;
	 
	public IField SOTTOTIPO_EVENTO_INIZIALE = null;
	 
	public IField TIPO_EVENTO_FINALE = null;
	 
	public IField SOTTOTIPO_EVENTO_FINALE = null;
	 
	public IField TEMPO_A = null;
	 
	public IField TEMPO_B = null;
	 
	public IField TOLLERANZA_A = null;
	 
	public IField TOLLERANZA_B = null;
	 

	@Override
	public Class<SLA> getModeledClass(){
		return SLA.class;
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