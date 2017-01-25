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

import it.govpay.orm.RendicontazionePagamentoSenzaRPT;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model RendicontazionePagamentoSenzaRPT 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class RendicontazionePagamentoSenzaRPTModel extends AbstractModel<RendicontazionePagamentoSenzaRPT> {

	public RendicontazionePagamentoSenzaRPTModel(){
	
		super();
	
		this.FR = new it.govpay.orm.model.FRModel(new Field("FR",it.govpay.orm.FR.class,"RendicontazionePagamentoSenzaRPT",RendicontazionePagamentoSenzaRPT.class));
		this.FR_APPLICAZIONE = new it.govpay.orm.model.FrApplicazioneModel(new Field("FrApplicazione",it.govpay.orm.FrApplicazione.class,"RendicontazionePagamentoSenzaRPT",RendicontazionePagamentoSenzaRPT.class));
		this.RENDICONTAZIONE_SENZA_RPT = new it.govpay.orm.model.RendicontazioneSenzaRPTModel(new Field("RendicontazioneSenzaRPT",it.govpay.orm.RendicontazioneSenzaRPT.class,"RendicontazionePagamentoSenzaRPT",RendicontazionePagamentoSenzaRPT.class));
		this.SINGOLO_VERSAMENTO = new it.govpay.orm.model.SingoloVersamentoModel(new Field("SingoloVersamento",it.govpay.orm.SingoloVersamento.class,"RendicontazionePagamentoSenzaRPT",RendicontazionePagamentoSenzaRPT.class));
		this.VERSAMENTO = new it.govpay.orm.model.VersamentoModel(new Field("Versamento",it.govpay.orm.Versamento.class,"RendicontazionePagamentoSenzaRPT",RendicontazionePagamentoSenzaRPT.class));
	
	}
	
	public RendicontazionePagamentoSenzaRPTModel(IField father){
	
		super(father);
	
		this.FR = new it.govpay.orm.model.FRModel(new ComplexField(father,"FR",it.govpay.orm.FR.class,"RendicontazionePagamentoSenzaRPT",RendicontazionePagamentoSenzaRPT.class));
		this.FR_APPLICAZIONE = new it.govpay.orm.model.FrApplicazioneModel(new ComplexField(father,"FrApplicazione",it.govpay.orm.FrApplicazione.class,"RendicontazionePagamentoSenzaRPT",RendicontazionePagamentoSenzaRPT.class));
		this.RENDICONTAZIONE_SENZA_RPT = new it.govpay.orm.model.RendicontazioneSenzaRPTModel(new ComplexField(father,"RendicontazioneSenzaRPT",it.govpay.orm.RendicontazioneSenzaRPT.class,"RendicontazionePagamentoSenzaRPT",RendicontazionePagamentoSenzaRPT.class));
		this.SINGOLO_VERSAMENTO = new it.govpay.orm.model.SingoloVersamentoModel(new ComplexField(father,"SingoloVersamento",it.govpay.orm.SingoloVersamento.class,"RendicontazionePagamentoSenzaRPT",RendicontazionePagamentoSenzaRPT.class));
		this.VERSAMENTO = new it.govpay.orm.model.VersamentoModel(new ComplexField(father,"Versamento",it.govpay.orm.Versamento.class,"RendicontazionePagamentoSenzaRPT",RendicontazionePagamentoSenzaRPT.class));
	
	}
	
	

	public it.govpay.orm.model.FRModel FR = null;
	 
	public it.govpay.orm.model.FrApplicazioneModel FR_APPLICAZIONE = null;
	 
	public it.govpay.orm.model.RendicontazioneSenzaRPTModel RENDICONTAZIONE_SENZA_RPT = null;
	 
	public it.govpay.orm.model.SingoloVersamentoModel SINGOLO_VERSAMENTO = null;
	 
	public it.govpay.orm.model.VersamentoModel VERSAMENTO = null;
	 

	@Override
	public Class<RendicontazionePagamentoSenzaRPT> getModeledClass(){
		return RendicontazionePagamentoSenzaRPT.class;
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
