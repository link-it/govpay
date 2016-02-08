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

import it.govpay.orm.Rilevamento;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Rilevamento 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class RilevamentoModel extends AbstractModel<Rilevamento> {

	public RilevamentoModel(){
	
		super();
	
		this.ID_SLA = new it.govpay.orm.model.IdSlaModel(new Field("idSLA",it.govpay.orm.IdSla.class,"Rilevamento",Rilevamento.class));
		this.ID_APPLICAZIONE = new Field("idApplicazione",long.class,"Rilevamento",Rilevamento.class);
		this.ID_EVENTO_INIZIALE = new it.govpay.orm.model.IdEventoModel(new Field("idEventoIniziale",it.govpay.orm.IdEvento.class,"Rilevamento",Rilevamento.class));
		this.ID_EVENTO_FINALE = new it.govpay.orm.model.IdEventoModel(new Field("idEventoFinale",it.govpay.orm.IdEvento.class,"Rilevamento",Rilevamento.class));
		this.DATA_RILEVAMENTO = new Field("dataRilevamento",java.util.Date.class,"Rilevamento",Rilevamento.class);
		this.DURATA = new Field("durata",long.class,"Rilevamento",Rilevamento.class);
	
	}
	
	public RilevamentoModel(IField father){
	
		super(father);
	
		this.ID_SLA = new it.govpay.orm.model.IdSlaModel(new ComplexField(father,"idSLA",it.govpay.orm.IdSla.class,"Rilevamento",Rilevamento.class));
		this.ID_APPLICAZIONE = new ComplexField(father,"idApplicazione",long.class,"Rilevamento",Rilevamento.class);
		this.ID_EVENTO_INIZIALE = new it.govpay.orm.model.IdEventoModel(new ComplexField(father,"idEventoIniziale",it.govpay.orm.IdEvento.class,"Rilevamento",Rilevamento.class));
		this.ID_EVENTO_FINALE = new it.govpay.orm.model.IdEventoModel(new ComplexField(father,"idEventoFinale",it.govpay.orm.IdEvento.class,"Rilevamento",Rilevamento.class));
		this.DATA_RILEVAMENTO = new ComplexField(father,"dataRilevamento",java.util.Date.class,"Rilevamento",Rilevamento.class);
		this.DURATA = new ComplexField(father,"durata",long.class,"Rilevamento",Rilevamento.class);
	
	}
	
	

	public it.govpay.orm.model.IdSlaModel ID_SLA = null;
	 
	public IField ID_APPLICAZIONE = null;
	 
	public it.govpay.orm.model.IdEventoModel ID_EVENTO_INIZIALE = null;
	 
	public it.govpay.orm.model.IdEventoModel ID_EVENTO_FINALE = null;
	 
	public IField DATA_RILEVAMENTO = null;
	 
	public IField DURATA = null;
	 

	@Override
	public Class<Rilevamento> getModeledClass(){
		return Rilevamento.class;
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