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

import it.govpay.orm.FrApplicazione;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model FrApplicazione 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class FrApplicazioneModel extends AbstractModel<FrApplicazione> {

	public FrApplicazioneModel(){
	
		super();
	
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new Field("idApplicazione",it.govpay.orm.IdApplicazione.class,"FrApplicazione",FrApplicazione.class));
		this.ID_FR = new it.govpay.orm.model.IdFrModel(new Field("idFr",it.govpay.orm.IdFr.class,"FrApplicazione",FrApplicazione.class));
		this.NUMERO_PAGAMENTI = new Field("numeroPagamenti",long.class,"FrApplicazione",FrApplicazione.class);
		this.IMPORTO_TOTALE_PAGAMENTI = new Field("importoTotalePagamenti",double.class,"FrApplicazione",FrApplicazione.class);
	
	}
	
	public FrApplicazioneModel(IField father){
	
		super(father);
	
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new ComplexField(father,"idApplicazione",it.govpay.orm.IdApplicazione.class,"FrApplicazione",FrApplicazione.class));
		this.ID_FR = new it.govpay.orm.model.IdFrModel(new ComplexField(father,"idFr",it.govpay.orm.IdFr.class,"FrApplicazione",FrApplicazione.class));
		this.NUMERO_PAGAMENTI = new ComplexField(father,"numeroPagamenti",long.class,"FrApplicazione",FrApplicazione.class);
		this.IMPORTO_TOTALE_PAGAMENTI = new ComplexField(father,"importoTotalePagamenti",double.class,"FrApplicazione",FrApplicazione.class);
	
	}
	
	

	public it.govpay.orm.model.IdApplicazioneModel ID_APPLICAZIONE = null;
	 
	public it.govpay.orm.model.IdFrModel ID_FR = null;
	 
	public IField NUMERO_PAGAMENTI = null;
	 
	public IField IMPORTO_TOTALE_PAGAMENTI = null;
	 

	@Override
	public Class<FrApplicazione> getModeledClass(){
		return FrApplicazione.class;
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