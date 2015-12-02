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

import it.govpay.orm.RT;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model RT 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class RTModel extends AbstractModel<RT> {

	public RTModel(){
	
		super();
	
		this.ID_RPT = new it.govpay.orm.model.IdRptModel(new Field("idRPT",it.govpay.orm.IdRpt.class,"RT",RT.class));
		this.ID_TRACCIATO = new it.govpay.orm.model.IdTracciatoModel(new Field("idTracciato",it.govpay.orm.IdTracciato.class,"RT",RT.class));
		this.COD_MSG_RICEVUTA = new Field("codMsgRicevuta",java.lang.String.class,"RT",RT.class);
		this.DATA_ORA_MSG_RICEVUTA = new Field("dataOraMsgRicevuta",java.util.Date.class,"RT",RT.class);
		this.ID_ANAGRAFICA_ATTESTANTE = new it.govpay.orm.model.IdAnagraficaModel(new Field("idAnagraficaAttestante",it.govpay.orm.IdAnagrafica.class,"RT",RT.class));
		this.COD_ESITO_PAGAMENTO = new Field("codEsitoPagamento",int.class,"RT",RT.class);
		this.IMPORTO_TOTALE_PAGATO = new Field("importoTotalePagato",double.class,"RT",RT.class);
		this.STATO = new Field("stato",java.lang.String.class,"RT",RT.class);
		this.DESCRIZIONE_STATO = new Field("descrizioneStato",java.lang.String.class,"RT",RT.class);
	
	}
	
	public RTModel(IField father){
	
		super(father);
	
		this.ID_RPT = new it.govpay.orm.model.IdRptModel(new ComplexField(father,"idRPT",it.govpay.orm.IdRpt.class,"RT",RT.class));
		this.ID_TRACCIATO = new it.govpay.orm.model.IdTracciatoModel(new ComplexField(father,"idTracciato",it.govpay.orm.IdTracciato.class,"RT",RT.class));
		this.COD_MSG_RICEVUTA = new ComplexField(father,"codMsgRicevuta",java.lang.String.class,"RT",RT.class);
		this.DATA_ORA_MSG_RICEVUTA = new ComplexField(father,"dataOraMsgRicevuta",java.util.Date.class,"RT",RT.class);
		this.ID_ANAGRAFICA_ATTESTANTE = new it.govpay.orm.model.IdAnagraficaModel(new ComplexField(father,"idAnagraficaAttestante",it.govpay.orm.IdAnagrafica.class,"RT",RT.class));
		this.COD_ESITO_PAGAMENTO = new ComplexField(father,"codEsitoPagamento",int.class,"RT",RT.class);
		this.IMPORTO_TOTALE_PAGATO = new ComplexField(father,"importoTotalePagato",double.class,"RT",RT.class);
		this.STATO = new ComplexField(father,"stato",java.lang.String.class,"RT",RT.class);
		this.DESCRIZIONE_STATO = new ComplexField(father,"descrizioneStato",java.lang.String.class,"RT",RT.class);
	
	}
	
	

	public it.govpay.orm.model.IdRptModel ID_RPT = null;
	 
	public it.govpay.orm.model.IdTracciatoModel ID_TRACCIATO = null;
	 
	public IField COD_MSG_RICEVUTA = null;
	 
	public IField DATA_ORA_MSG_RICEVUTA = null;
	 
	public it.govpay.orm.model.IdAnagraficaModel ID_ANAGRAFICA_ATTESTANTE = null;
	 
	public IField COD_ESITO_PAGAMENTO = null;
	 
	public IField IMPORTO_TOTALE_PAGATO = null;
	 
	public IField STATO = null;
	 
	public IField DESCRIZIONE_STATO = null;
	 

	@Override
	public Class<RT> getModeledClass(){
		return RT.class;
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