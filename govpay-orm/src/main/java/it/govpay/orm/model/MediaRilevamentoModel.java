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

import it.govpay.orm.MediaRilevamento;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model MediaRilevamento 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class MediaRilevamentoModel extends AbstractModel<MediaRilevamento> {

	public MediaRilevamentoModel(){
	
		super();
	
		this.ID_SLA = new it.govpay.orm.model.IdSlaModel(new Field("idSLA",it.govpay.orm.IdSla.class,"MediaRilevamento",MediaRilevamento.class));
		this.ID_APPLICAZIONE = new Field("idApplicazione",long.class,"MediaRilevamento",MediaRilevamento.class);
		this.DATA_OSSERVAZIONE = new Field("dataOsservazione",java.util.Date.class,"MediaRilevamento",MediaRilevamento.class);
		this.NUM_RILEVAMENTI_A = new Field("numRilevamentiA",long.class,"MediaRilevamento",MediaRilevamento.class);
		this.PERCENTUALE_A = new Field("percentualeA",double.class,"MediaRilevamento",MediaRilevamento.class);
		this.NUM_RILEVAMENTI_B = new Field("numRilevamentiB",long.class,"MediaRilevamento",MediaRilevamento.class);
		this.PERCENTUALE_B = new Field("percentualeB",double.class,"MediaRilevamento",MediaRilevamento.class);
		this.NUM_RILEVAMENTI_OVER = new Field("numRilevamentiOver",long.class,"MediaRilevamento",MediaRilevamento.class);
	
	}
	
	public MediaRilevamentoModel(IField father){
	
		super(father);
	
		this.ID_SLA = new it.govpay.orm.model.IdSlaModel(new ComplexField(father,"idSLA",it.govpay.orm.IdSla.class,"MediaRilevamento",MediaRilevamento.class));
		this.ID_APPLICAZIONE = new ComplexField(father,"idApplicazione",long.class,"MediaRilevamento",MediaRilevamento.class);
		this.DATA_OSSERVAZIONE = new ComplexField(father,"dataOsservazione",java.util.Date.class,"MediaRilevamento",MediaRilevamento.class);
		this.NUM_RILEVAMENTI_A = new ComplexField(father,"numRilevamentiA",long.class,"MediaRilevamento",MediaRilevamento.class);
		this.PERCENTUALE_A = new ComplexField(father,"percentualeA",double.class,"MediaRilevamento",MediaRilevamento.class);
		this.NUM_RILEVAMENTI_B = new ComplexField(father,"numRilevamentiB",long.class,"MediaRilevamento",MediaRilevamento.class);
		this.PERCENTUALE_B = new ComplexField(father,"percentualeB",double.class,"MediaRilevamento",MediaRilevamento.class);
		this.NUM_RILEVAMENTI_OVER = new ComplexField(father,"numRilevamentiOver",long.class,"MediaRilevamento",MediaRilevamento.class);
	
	}
	
	

	public it.govpay.orm.model.IdSlaModel ID_SLA = null;
	 
	public IField ID_APPLICAZIONE = null;
	 
	public IField DATA_OSSERVAZIONE = null;
	 
	public IField NUM_RILEVAMENTI_A = null;
	 
	public IField PERCENTUALE_A = null;
	 
	public IField NUM_RILEVAMENTI_B = null;
	 
	public IField PERCENTUALE_B = null;
	 
	public IField NUM_RILEVAMENTI_OVER = null;
	 

	@Override
	public Class<MediaRilevamento> getModeledClass(){
		return MediaRilevamento.class;
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