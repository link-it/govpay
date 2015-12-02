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
	
		this.DATA_ORA_EVENTO = new Field("dataOraEvento",java.util.Date.class,"Evento",Evento.class);
		this.COD_DOMINIO = new Field("codDominio",java.lang.String.class,"Evento",Evento.class);
		this.IUV = new Field("iuv",java.lang.String.class,"Evento",Evento.class);
		this.ID_APPLICAZIONE = new Field("idApplicazione",java.lang.Long.class,"Evento",Evento.class);
		this.CCP = new Field("ccp",java.lang.String.class,"Evento",Evento.class);
		this.COD_PSP = new Field("codPsp",java.lang.String.class,"Evento",Evento.class);
		this.TIPO_VERSAMENTO = new Field("tipoVersamento",java.lang.String.class,"Evento",Evento.class);
		this.COMPONENTE = new Field("componente",java.lang.String.class,"Evento",Evento.class);
		this.CATEGORIA_EVENTO = new Field("categoriaEvento",java.lang.String.class,"Evento",Evento.class);
		this.TIPO_EVENTO = new Field("tipoEvento",java.lang.String.class,"Evento",Evento.class);
		this.SOTTOTIPO_EVENTO = new Field("sottotipoEvento",java.lang.String.class,"Evento",Evento.class);
		this.COD_FRUITORE = new Field("codFruitore",java.lang.String.class,"Evento",Evento.class);
		this.COD_EROGATORE = new Field("codErogatore",java.lang.String.class,"Evento",Evento.class);
		this.COD_STAZIONE = new Field("codStazione",java.lang.String.class,"Evento",Evento.class);
		this.CANALE_PAGAMENTO = new Field("canalePagamento",java.lang.String.class,"Evento",Evento.class);
		this.ALTRI_PARAMETRI = new Field("altriParametri",java.lang.String.class,"Evento",Evento.class);
		this.ESITO = new Field("esito",java.lang.String.class,"Evento",Evento.class);
	
	}
	
	public EventoModel(IField father){
	
		super(father);
	
		this.DATA_ORA_EVENTO = new ComplexField(father,"dataOraEvento",java.util.Date.class,"Evento",Evento.class);
		this.COD_DOMINIO = new ComplexField(father,"codDominio",java.lang.String.class,"Evento",Evento.class);
		this.IUV = new ComplexField(father,"iuv",java.lang.String.class,"Evento",Evento.class);
		this.ID_APPLICAZIONE = new ComplexField(father,"idApplicazione",java.lang.Long.class,"Evento",Evento.class);
		this.CCP = new ComplexField(father,"ccp",java.lang.String.class,"Evento",Evento.class);
		this.COD_PSP = new ComplexField(father,"codPsp",java.lang.String.class,"Evento",Evento.class);
		this.TIPO_VERSAMENTO = new ComplexField(father,"tipoVersamento",java.lang.String.class,"Evento",Evento.class);
		this.COMPONENTE = new ComplexField(father,"componente",java.lang.String.class,"Evento",Evento.class);
		this.CATEGORIA_EVENTO = new ComplexField(father,"categoriaEvento",java.lang.String.class,"Evento",Evento.class);
		this.TIPO_EVENTO = new ComplexField(father,"tipoEvento",java.lang.String.class,"Evento",Evento.class);
		this.SOTTOTIPO_EVENTO = new ComplexField(father,"sottotipoEvento",java.lang.String.class,"Evento",Evento.class);
		this.COD_FRUITORE = new ComplexField(father,"codFruitore",java.lang.String.class,"Evento",Evento.class);
		this.COD_EROGATORE = new ComplexField(father,"codErogatore",java.lang.String.class,"Evento",Evento.class);
		this.COD_STAZIONE = new ComplexField(father,"codStazione",java.lang.String.class,"Evento",Evento.class);
		this.CANALE_PAGAMENTO = new ComplexField(father,"canalePagamento",java.lang.String.class,"Evento",Evento.class);
		this.ALTRI_PARAMETRI = new ComplexField(father,"altriParametri",java.lang.String.class,"Evento",Evento.class);
		this.ESITO = new ComplexField(father,"esito",java.lang.String.class,"Evento",Evento.class);
	
	}
	
	

	public IField DATA_ORA_EVENTO = null;
	 
	public IField COD_DOMINIO = null;
	 
	public IField IUV = null;
	 
	public IField ID_APPLICAZIONE = null;
	 
	public IField CCP = null;
	 
	public IField COD_PSP = null;
	 
	public IField TIPO_VERSAMENTO = null;
	 
	public IField COMPONENTE = null;
	 
	public IField CATEGORIA_EVENTO = null;
	 
	public IField TIPO_EVENTO = null;
	 
	public IField SOTTOTIPO_EVENTO = null;
	 
	public IField COD_FRUITORE = null;
	 
	public IField COD_EROGATORE = null;
	 
	public IField COD_STAZIONE = null;
	 
	public IField CANALE_PAGAMENTO = null;
	 
	public IField ALTRI_PARAMETRI = null;
	 
	public IField ESITO = null;
	 

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