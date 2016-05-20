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

import it.govpay.orm.RendicontazioneSenzaRPT;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model RendicontazioneSenzaRPT 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class RendicontazioneSenzaRPTModel extends AbstractModel<RendicontazioneSenzaRPT> {

	public RendicontazioneSenzaRPTModel(){
	
		super();
	
		this.ID_FR_APPLICAZIONE = new it.govpay.orm.model.IdFrApplicazioneModel(new Field("idFrApplicazione",it.govpay.orm.IdFrApplicazione.class,"RendicontazioneSenzaRPT",RendicontazioneSenzaRPT.class));
		this.IMPORTO_PAGATO = new Field("importoPagato",double.class,"RendicontazioneSenzaRPT",RendicontazioneSenzaRPT.class);
		this.ID_IUV = new it.govpay.orm.model.IdIuvModel(new Field("idIuv",it.govpay.orm.IdIuv.class,"RendicontazioneSenzaRPT",RendicontazioneSenzaRPT.class));
		this.IUR = new Field("iur",java.lang.String.class,"RendicontazioneSenzaRPT",RendicontazioneSenzaRPT.class);
		this.RENDICONTAZIONE_DATA = new Field("rendicontazioneData",java.util.Date.class,"RendicontazioneSenzaRPT",RendicontazioneSenzaRPT.class);
		this.ID_SINGOLO_VERSAMENTO = new it.govpay.orm.model.IdSingoloVersamentoModel(new Field("idSingoloVersamento",it.govpay.orm.IdSingoloVersamento.class,"RendicontazioneSenzaRPT",RendicontazioneSenzaRPT.class));
	
	}
	
	public RendicontazioneSenzaRPTModel(IField father){
	
		super(father);
	
		this.ID_FR_APPLICAZIONE = new it.govpay.orm.model.IdFrApplicazioneModel(new ComplexField(father,"idFrApplicazione",it.govpay.orm.IdFrApplicazione.class,"RendicontazioneSenzaRPT",RendicontazioneSenzaRPT.class));
		this.IMPORTO_PAGATO = new ComplexField(father,"importoPagato",double.class,"RendicontazioneSenzaRPT",RendicontazioneSenzaRPT.class);
		this.ID_IUV = new it.govpay.orm.model.IdIuvModel(new ComplexField(father,"idIuv",it.govpay.orm.IdIuv.class,"RendicontazioneSenzaRPT",RendicontazioneSenzaRPT.class));
		this.IUR = new ComplexField(father,"iur",java.lang.String.class,"RendicontazioneSenzaRPT",RendicontazioneSenzaRPT.class);
		this.RENDICONTAZIONE_DATA = new ComplexField(father,"rendicontazioneData",java.util.Date.class,"RendicontazioneSenzaRPT",RendicontazioneSenzaRPT.class);
		this.ID_SINGOLO_VERSAMENTO = new it.govpay.orm.model.IdSingoloVersamentoModel(new ComplexField(father,"idSingoloVersamento",it.govpay.orm.IdSingoloVersamento.class,"RendicontazioneSenzaRPT",RendicontazioneSenzaRPT.class));
	
	}
	
	

	public it.govpay.orm.model.IdFrApplicazioneModel ID_FR_APPLICAZIONE = null;
	 
	public IField IMPORTO_PAGATO = null;
	 
	public it.govpay.orm.model.IdIuvModel ID_IUV = null;
	 
	public IField IUR = null;
	 
	public IField RENDICONTAZIONE_DATA = null;
	 
	public it.govpay.orm.model.IdSingoloVersamentoModel ID_SINGOLO_VERSAMENTO = null;
	 

	@Override
	public Class<RendicontazioneSenzaRPT> getModeledClass(){
		return RendicontazioneSenzaRPT.class;
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