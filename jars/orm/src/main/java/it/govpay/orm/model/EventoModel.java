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
		this.CATEGORIA_EVENTO = new Field("categoriaEvento",java.lang.String.class,"Evento",Evento.class);
		this.TIPO_EVENTO = new Field("tipoEvento",java.lang.String.class,"Evento",Evento.class);
		this.SOTTOTIPO_EVENTO = new Field("sottotipoEvento",java.lang.String.class,"Evento",Evento.class);
		this.DATA = new Field("data",java.util.Date.class,"Evento",Evento.class);
		this.INTERVALLO = new Field("intervallo",long.class,"Evento",Evento.class);
		this.CLASSNAME_DETTAGLIO = new Field("classnameDettaglio",java.lang.String.class,"Evento",Evento.class);
		this.DETTAGLIO = new Field("dettaglio",java.lang.String.class,"Evento",Evento.class);
		this.ID_VERSAMENTO = new it.govpay.orm.model.IdVersamentoModel(new Field("idVersamento",it.govpay.orm.IdVersamento.class,"Evento",Evento.class));
		this.ID_PAGAMENTO_PORTALE = new it.govpay.orm.model.IdPagamentoPortaleModel(new Field("idPagamentoPortale",it.govpay.orm.IdPagamentoPortale.class,"Evento",Evento.class));
	
	}
	
	public EventoModel(IField father){
	
		super(father);
	
		this.COD_DOMINIO = new ComplexField(father,"codDominio",java.lang.String.class,"Evento",Evento.class);
		this.IUV = new ComplexField(father,"iuv",java.lang.String.class,"Evento",Evento.class);
		this.CCP = new ComplexField(father,"ccp",java.lang.String.class,"Evento",Evento.class);
		this.CATEGORIA_EVENTO = new ComplexField(father,"categoriaEvento",java.lang.String.class,"Evento",Evento.class);
		this.TIPO_EVENTO = new ComplexField(father,"tipoEvento",java.lang.String.class,"Evento",Evento.class);
		this.SOTTOTIPO_EVENTO = new ComplexField(father,"sottotipoEvento",java.lang.String.class,"Evento",Evento.class);
		this.DATA = new ComplexField(father,"data",java.util.Date.class,"Evento",Evento.class);
		this.INTERVALLO = new ComplexField(father,"intervallo",long.class,"Evento",Evento.class);
		this.CLASSNAME_DETTAGLIO = new ComplexField(father,"classnameDettaglio",java.lang.String.class,"Evento",Evento.class);
		this.DETTAGLIO = new ComplexField(father,"dettaglio",java.lang.String.class,"Evento",Evento.class);
		this.ID_VERSAMENTO = new it.govpay.orm.model.IdVersamentoModel(new ComplexField(father,"idVersamento",it.govpay.orm.IdVersamento.class,"Evento",Evento.class));
		this.ID_PAGAMENTO_PORTALE = new it.govpay.orm.model.IdPagamentoPortaleModel(new ComplexField(father,"idPagamentoPortale",it.govpay.orm.IdPagamentoPortale.class,"Evento",Evento.class));
	
	}
	
	

	public IField COD_DOMINIO = null;
	 
	public IField IUV = null;
	 
	public IField CCP = null;
	 
	public IField CATEGORIA_EVENTO = null;
	 
	public IField TIPO_EVENTO = null;
	 
	public IField SOTTOTIPO_EVENTO = null;
	 
	public IField DATA = null;
	 
	public IField INTERVALLO = null;
	 
	public IField CLASSNAME_DETTAGLIO = null;
	 
	public IField DETTAGLIO = null;
	 
	public it.govpay.orm.model.IdVersamentoModel ID_VERSAMENTO = null;
	 
	public it.govpay.orm.model.IdPagamentoPortaleModel ID_PAGAMENTO_PORTALE = null;
	 

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
