/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2016 Link.it srl (http://www.link.it).
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

import it.govpay.orm.Evento;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Evento 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class EventoModel extends AbstractModel<Evento> {

	public EventoModel(){
	
		super();
	
		this.COD_DOMINIO = new Field("codDominio",java.lang.String.class,"Evento",Evento.class);
		this.IUV = new Field("iuv",java.lang.String.class,"Evento",Evento.class);
		this.CCP = new Field("ccp",java.lang.String.class,"Evento",Evento.class);
		this.COD_PSP = new Field("codPsp",java.lang.String.class,"Evento",Evento.class);
		this.TIPO_VERSAMENTO = new Field("tipoVersamento",java.lang.String.class,"Evento",Evento.class);
		this.COMPONENTE = new Field("componente",java.lang.String.class,"Evento",Evento.class);
		this.CATEGORIA_EVENTO = new Field("categoriaEvento",java.lang.String.class,"Evento",Evento.class);
		this.TIPO_EVENTO = new Field("tipoEvento",java.lang.String.class,"Evento",Evento.class);
		this.SOTTOTIPO_EVENTO = new Field("sottotipoEvento",java.lang.String.class,"Evento",Evento.class);
		this.EROGATORE = new Field("erogatore",java.lang.String.class,"Evento",Evento.class);
		this.FRUITORE = new Field("fruitore",java.lang.String.class,"Evento",Evento.class);
		this.COD_STAZIONE = new Field("codStazione",java.lang.String.class,"Evento",Evento.class);
		this.COD_CANALE = new Field("codCanale",java.lang.String.class,"Evento",Evento.class);
		this.PARAMETRI_1 = new Field("parametri1",java.lang.String.class,"Evento",Evento.class);
		this.PARAMETRI_2 = new Field("parametri2",java.lang.String.class,"Evento",Evento.class);
		this.ESITO = new Field("esito",java.lang.String.class,"Evento",Evento.class);
		this.DATA_1 = new Field("data1",java.util.Date.class,"Evento",Evento.class);
		this.DATA_2 = new Field("data2",java.util.Date.class,"Evento",Evento.class);
	
	}
	
	public EventoModel(IField father){
	
		super(father);
	
		this.COD_DOMINIO = new ComplexField(father,"codDominio",java.lang.String.class,"Evento",Evento.class);
		this.IUV = new ComplexField(father,"iuv",java.lang.String.class,"Evento",Evento.class);
		this.CCP = new ComplexField(father,"ccp",java.lang.String.class,"Evento",Evento.class);
		this.COD_PSP = new ComplexField(father,"codPsp",java.lang.String.class,"Evento",Evento.class);
		this.TIPO_VERSAMENTO = new ComplexField(father,"tipoVersamento",java.lang.String.class,"Evento",Evento.class);
		this.COMPONENTE = new ComplexField(father,"componente",java.lang.String.class,"Evento",Evento.class);
		this.CATEGORIA_EVENTO = new ComplexField(father,"categoriaEvento",java.lang.String.class,"Evento",Evento.class);
		this.TIPO_EVENTO = new ComplexField(father,"tipoEvento",java.lang.String.class,"Evento",Evento.class);
		this.SOTTOTIPO_EVENTO = new ComplexField(father,"sottotipoEvento",java.lang.String.class,"Evento",Evento.class);
		this.EROGATORE = new ComplexField(father,"erogatore",java.lang.String.class,"Evento",Evento.class);
		this.FRUITORE = new ComplexField(father,"fruitore",java.lang.String.class,"Evento",Evento.class);
		this.COD_STAZIONE = new ComplexField(father,"codStazione",java.lang.String.class,"Evento",Evento.class);
		this.COD_CANALE = new ComplexField(father,"codCanale",java.lang.String.class,"Evento",Evento.class);
		this.PARAMETRI_1 = new ComplexField(father,"parametri1",java.lang.String.class,"Evento",Evento.class);
		this.PARAMETRI_2 = new ComplexField(father,"parametri2",java.lang.String.class,"Evento",Evento.class);
		this.ESITO = new ComplexField(father,"esito",java.lang.String.class,"Evento",Evento.class);
		this.DATA_1 = new ComplexField(father,"data1",java.util.Date.class,"Evento",Evento.class);
		this.DATA_2 = new ComplexField(father,"data2",java.util.Date.class,"Evento",Evento.class);
	
	}
	
	

	public IField COD_DOMINIO = null;
	 
	public IField IUV = null;
	 
	public IField CCP = null;
	 
	public IField COD_PSP = null;
	 
	public IField TIPO_VERSAMENTO = null;
	 
	public IField COMPONENTE = null;
	 
	public IField CATEGORIA_EVENTO = null;
	 
	public IField TIPO_EVENTO = null;
	 
	public IField SOTTOTIPO_EVENTO = null;
	 
	public IField EROGATORE = null;
	 
	public IField FRUITORE = null;
	 
	public IField COD_STAZIONE = null;
	 
	public IField COD_CANALE = null;
	 
	public IField PARAMETRI_1 = null;
	 
	public IField PARAMETRI_2 = null;
	 
	public IField ESITO = null;
	 
	public IField DATA_1 = null;
	 
	public IField DATA_2 = null;
	 

	@Override
	public Class<Evento> getModeledClass(){
		return Evento.class;
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