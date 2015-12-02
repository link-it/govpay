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

import it.govpay.orm.Esito;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model Esito 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class EsitoModel extends AbstractModel<Esito> {

	public EsitoModel(){
	
		super();
	
		this.COD_DOMINIO = new Field("codDominio",java.lang.String.class,"Esito",Esito.class);
		this.IUV = new Field("iuv",java.lang.String.class,"Esito",Esito.class);
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new Field("idApplicazione",it.govpay.orm.IdApplicazione.class,"Esito",Esito.class));
		this.STATO_SPEDIZIONE = new Field("statoSpedizione",java.lang.String.class,"Esito",Esito.class);
		this.DETTAGLIO_SPEDIZIONE = new Field("dettaglioSpedizione",java.lang.String.class,"Esito",Esito.class);
		this.TENTATIVI_SPEDIZIONE = new Field("tentativiSpedizione",java.lang.Long.class,"Esito",Esito.class);
		this.DATA_ORA_CREAZIONE = new Field("dataOraCreazione",java.util.Date.class,"Esito",Esito.class);
		this.DATA_ORA_ULTIMA_SPEDIZIONE = new Field("dataOraUltimaSpedizione",java.util.Date.class,"Esito",Esito.class);
		this.DATA_ORA_PROSSIMA_SPEDIZIONE = new Field("dataOraProssimaSpedizione",java.util.Date.class,"Esito",Esito.class);
		this.XML = new Field("xml",byte[].class,"Esito",Esito.class);
	
	}
	
	public EsitoModel(IField father){
	
		super(father);
	
		this.COD_DOMINIO = new ComplexField(father,"codDominio",java.lang.String.class,"Esito",Esito.class);
		this.IUV = new ComplexField(father,"iuv",java.lang.String.class,"Esito",Esito.class);
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new ComplexField(father,"idApplicazione",it.govpay.orm.IdApplicazione.class,"Esito",Esito.class));
		this.STATO_SPEDIZIONE = new ComplexField(father,"statoSpedizione",java.lang.String.class,"Esito",Esito.class);
		this.DETTAGLIO_SPEDIZIONE = new ComplexField(father,"dettaglioSpedizione",java.lang.String.class,"Esito",Esito.class);
		this.TENTATIVI_SPEDIZIONE = new ComplexField(father,"tentativiSpedizione",java.lang.Long.class,"Esito",Esito.class);
		this.DATA_ORA_CREAZIONE = new ComplexField(father,"dataOraCreazione",java.util.Date.class,"Esito",Esito.class);
		this.DATA_ORA_ULTIMA_SPEDIZIONE = new ComplexField(father,"dataOraUltimaSpedizione",java.util.Date.class,"Esito",Esito.class);
		this.DATA_ORA_PROSSIMA_SPEDIZIONE = new ComplexField(father,"dataOraProssimaSpedizione",java.util.Date.class,"Esito",Esito.class);
		this.XML = new ComplexField(father,"xml",byte[].class,"Esito",Esito.class);
	
	}
	
	

	public IField COD_DOMINIO = null;
	 
	public IField IUV = null;
	 
	public it.govpay.orm.model.IdApplicazioneModel ID_APPLICAZIONE = null;
	 
	public IField STATO_SPEDIZIONE = null;
	 
	public IField DETTAGLIO_SPEDIZIONE = null;
	 
	public IField TENTATIVI_SPEDIZIONE = null;
	 
	public IField DATA_ORA_CREAZIONE = null;
	 
	public IField DATA_ORA_ULTIMA_SPEDIZIONE = null;
	 
	public IField DATA_ORA_PROSSIMA_SPEDIZIONE = null;
	 
	public IField XML = null;
	 

	@Override
	public Class<Esito> getModeledClass(){
		return Esito.class;
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