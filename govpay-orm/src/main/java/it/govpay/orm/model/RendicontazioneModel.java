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

import it.govpay.orm.Rendicontazione;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Rendicontazione 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class RendicontazioneModel extends AbstractModel<Rendicontazione> {

	public RendicontazioneModel(){
	
		super();
	
		this.IUV = new Field("iuv",java.lang.String.class,"Rendicontazione",Rendicontazione.class);
		this.IUR = new Field("iur",java.lang.String.class,"Rendicontazione",Rendicontazione.class);
		this.INDICE_DATI = new Field("indiceDati",int.class,"Rendicontazione",Rendicontazione.class);
		this.IMPORTO_PAGATO = new Field("importoPagato",java.lang.Double.class,"Rendicontazione",Rendicontazione.class);
		this.ESITO = new Field("esito",java.lang.Integer.class,"Rendicontazione",Rendicontazione.class);
		this.DATA = new Field("data",java.util.Date.class,"Rendicontazione",Rendicontazione.class);
		this.STATO = new Field("stato",java.lang.String.class,"Rendicontazione",Rendicontazione.class);
		this.ANOMALIE = new Field("anomalie",java.lang.String.class,"Rendicontazione",Rendicontazione.class);
		this.ID_FR = new it.govpay.orm.model.IdFrModel(new Field("idFR",it.govpay.orm.IdFr.class,"Rendicontazione",Rendicontazione.class));
		this.ID_PAGAMENTO = new it.govpay.orm.model.IdPagamentoModel(new Field("idPagamento",it.govpay.orm.IdPagamento.class,"Rendicontazione",Rendicontazione.class));
	
	}
	
	public RendicontazioneModel(IField father){
	
		super(father);
	
		this.IUV = new ComplexField(father,"iuv",java.lang.String.class,"Rendicontazione",Rendicontazione.class);
		this.IUR = new ComplexField(father,"iur",java.lang.String.class,"Rendicontazione",Rendicontazione.class);
		this.INDICE_DATI = new ComplexField(father,"indiceDati",int.class,"Rendicontazione",Rendicontazione.class);
		this.IMPORTO_PAGATO = new ComplexField(father,"importoPagato",java.lang.Double.class,"Rendicontazione",Rendicontazione.class);
		this.ESITO = new ComplexField(father,"esito",java.lang.Integer.class,"Rendicontazione",Rendicontazione.class);
		this.DATA = new ComplexField(father,"data",java.util.Date.class,"Rendicontazione",Rendicontazione.class);
		this.STATO = new ComplexField(father,"stato",java.lang.String.class,"Rendicontazione",Rendicontazione.class);
		this.ANOMALIE = new ComplexField(father,"anomalie",java.lang.String.class,"Rendicontazione",Rendicontazione.class);
		this.ID_FR = new it.govpay.orm.model.IdFrModel(new ComplexField(father,"idFR",it.govpay.orm.IdFr.class,"Rendicontazione",Rendicontazione.class));
		this.ID_PAGAMENTO = new it.govpay.orm.model.IdPagamentoModel(new ComplexField(father,"idPagamento",it.govpay.orm.IdPagamento.class,"Rendicontazione",Rendicontazione.class));
	
	}
	
	

	public IField IUV = null;
	 
	public IField IUR = null;
	 
	public IField INDICE_DATI = null;
	 
	public IField IMPORTO_PAGATO = null;
	 
	public IField ESITO = null;
	 
	public IField DATA = null;
	 
	public IField STATO = null;
	 
	public IField ANOMALIE = null;
	 
	public it.govpay.orm.model.IdFrModel ID_FR = null;
	 
	public it.govpay.orm.model.IdPagamentoModel ID_PAGAMENTO = null;
	 

	@Override
	public Class<Rendicontazione> getModeledClass(){
		return Rendicontazione.class;
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