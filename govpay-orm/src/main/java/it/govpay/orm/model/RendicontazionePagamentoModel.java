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

import it.govpay.orm.RendicontazionePagamento;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model RendicontazionePagamento 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class RendicontazionePagamentoModel extends AbstractModel<RendicontazionePagamento> {

	public RendicontazionePagamentoModel(){
	
		super();
	
		this.FR = new it.govpay.orm.model.FRModel(new Field("FR",it.govpay.orm.FR.class,"RendicontazionePagamento",RendicontazionePagamento.class));
		this.RENDICONTAZIONE = new it.govpay.orm.model.RendicontazioneModel(new Field("Rendicontazione",it.govpay.orm.Rendicontazione.class,"RendicontazionePagamento",RendicontazionePagamento.class));
		this.PAGAMENTO = new it.govpay.orm.model.PagamentoModel(new Field("Pagamento",it.govpay.orm.Pagamento.class,"RendicontazionePagamento",RendicontazionePagamento.class));
		this.SINGOLO_VERSAMENTO = new it.govpay.orm.model.SingoloVersamentoModel(new Field("SingoloVersamento",it.govpay.orm.SingoloVersamento.class,"RendicontazionePagamento",RendicontazionePagamento.class));
		this.VERSAMENTO = new it.govpay.orm.model.VersamentoModel(new Field("Versamento",it.govpay.orm.Versamento.class,"RendicontazionePagamento",RendicontazionePagamento.class));
	
	}
	
	public RendicontazionePagamentoModel(IField father){
	
		super(father);
	
		this.FR = new it.govpay.orm.model.FRModel(new ComplexField(father,"FR",it.govpay.orm.FR.class,"RendicontazionePagamento",RendicontazionePagamento.class));
		this.RENDICONTAZIONE = new it.govpay.orm.model.RendicontazioneModel(new ComplexField(father,"Rendicontazione",it.govpay.orm.Rendicontazione.class,"RendicontazionePagamento",RendicontazionePagamento.class));
		this.PAGAMENTO = new it.govpay.orm.model.PagamentoModel(new ComplexField(father,"Pagamento",it.govpay.orm.Pagamento.class,"RendicontazionePagamento",RendicontazionePagamento.class));
		this.SINGOLO_VERSAMENTO = new it.govpay.orm.model.SingoloVersamentoModel(new ComplexField(father,"SingoloVersamento",it.govpay.orm.SingoloVersamento.class,"RendicontazionePagamento",RendicontazionePagamento.class));
		this.VERSAMENTO = new it.govpay.orm.model.VersamentoModel(new ComplexField(father,"Versamento",it.govpay.orm.Versamento.class,"RendicontazionePagamento",RendicontazionePagamento.class));
	
	}
	
	

	public it.govpay.orm.model.FRModel FR = null;
	 
	public it.govpay.orm.model.RendicontazioneModel RENDICONTAZIONE = null;
	 
	public it.govpay.orm.model.PagamentoModel PAGAMENTO = null;
	 
	public it.govpay.orm.model.SingoloVersamentoModel SINGOLO_VERSAMENTO = null;
	 
	public it.govpay.orm.model.VersamentoModel VERSAMENTO = null;
	 

	@Override
	public Class<RendicontazionePagamento> getModeledClass(){
		return RendicontazionePagamento.class;
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