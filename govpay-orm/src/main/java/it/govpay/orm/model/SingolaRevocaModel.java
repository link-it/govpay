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

import it.govpay.orm.SingolaRevoca;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model SingolaRevoca 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class SingolaRevocaModel extends AbstractModel<SingolaRevoca> {

	public SingolaRevocaModel(){
	
		super();
	
		this.ID_RR = new it.govpay.orm.model.IdRrModel(new Field("idRR",it.govpay.orm.IdRr.class,"SingolaRevoca",SingolaRevoca.class));
		this.ID_ER = new Field("idER",long.class,"SingolaRevoca",SingolaRevoca.class);
		this.ID_SINGOLO_VERSAMENTO = new it.govpay.orm.model.IdSingoloVersamentoModel(new Field("idSingoloVersamento",it.govpay.orm.IdSingoloVersamento.class,"SingolaRevoca",SingolaRevoca.class));
		this.CAUSALE_REVOCA = new Field("causaleRevoca",java.lang.String.class,"SingolaRevoca",SingolaRevoca.class);
		this.DATI_AGGIUNTIVI_REVOCA = new Field("datiAggiuntiviRevoca",java.lang.String.class,"SingolaRevoca",SingolaRevoca.class);
		this.SINGOLO_IMPORTO = new Field("singoloImporto",double.class,"SingolaRevoca",SingolaRevoca.class);
		this.SINGOLO_IMPORTO_REVOCATO = new Field("singoloImportoRevocato",java.lang.Double.class,"SingolaRevoca",SingolaRevoca.class);
		this.CAUSALE_ESITO = new Field("causaleEsito",java.lang.String.class,"SingolaRevoca",SingolaRevoca.class);
		this.DATI_AGGIUNTIVI_ESITO = new Field("datiAggiuntiviEsito",java.lang.String.class,"SingolaRevoca",SingolaRevoca.class);
		this.STATO = new Field("stato",java.lang.String.class,"SingolaRevoca",SingolaRevoca.class);
		this.DESCRIZIONE_STATO = new Field("descrizioneStato",java.lang.String.class,"SingolaRevoca",SingolaRevoca.class);
	
	}
	
	public SingolaRevocaModel(IField father){
	
		super(father);
	
		this.ID_RR = new it.govpay.orm.model.IdRrModel(new ComplexField(father,"idRR",it.govpay.orm.IdRr.class,"SingolaRevoca",SingolaRevoca.class));
		this.ID_ER = new ComplexField(father,"idER",long.class,"SingolaRevoca",SingolaRevoca.class);
		this.ID_SINGOLO_VERSAMENTO = new it.govpay.orm.model.IdSingoloVersamentoModel(new ComplexField(father,"idSingoloVersamento",it.govpay.orm.IdSingoloVersamento.class,"SingolaRevoca",SingolaRevoca.class));
		this.CAUSALE_REVOCA = new ComplexField(father,"causaleRevoca",java.lang.String.class,"SingolaRevoca",SingolaRevoca.class);
		this.DATI_AGGIUNTIVI_REVOCA = new ComplexField(father,"datiAggiuntiviRevoca",java.lang.String.class,"SingolaRevoca",SingolaRevoca.class);
		this.SINGOLO_IMPORTO = new ComplexField(father,"singoloImporto",double.class,"SingolaRevoca",SingolaRevoca.class);
		this.SINGOLO_IMPORTO_REVOCATO = new ComplexField(father,"singoloImportoRevocato",java.lang.Double.class,"SingolaRevoca",SingolaRevoca.class);
		this.CAUSALE_ESITO = new ComplexField(father,"causaleEsito",java.lang.String.class,"SingolaRevoca",SingolaRevoca.class);
		this.DATI_AGGIUNTIVI_ESITO = new ComplexField(father,"datiAggiuntiviEsito",java.lang.String.class,"SingolaRevoca",SingolaRevoca.class);
		this.STATO = new ComplexField(father,"stato",java.lang.String.class,"SingolaRevoca",SingolaRevoca.class);
		this.DESCRIZIONE_STATO = new ComplexField(father,"descrizioneStato",java.lang.String.class,"SingolaRevoca",SingolaRevoca.class);
	
	}
	
	

	public it.govpay.orm.model.IdRrModel ID_RR = null;
	 
	public IField ID_ER = null;
	 
	public it.govpay.orm.model.IdSingoloVersamentoModel ID_SINGOLO_VERSAMENTO = null;
	 
	public IField CAUSALE_REVOCA = null;
	 
	public IField DATI_AGGIUNTIVI_REVOCA = null;
	 
	public IField SINGOLO_IMPORTO = null;
	 
	public IField SINGOLO_IMPORTO_REVOCATO = null;
	 
	public IField CAUSALE_ESITO = null;
	 
	public IField DATI_AGGIUNTIVI_ESITO = null;
	 
	public IField STATO = null;
	 
	public IField DESCRIZIONE_STATO = null;
	 

	@Override
	public Class<SingolaRevoca> getModeledClass(){
		return SingolaRevoca.class;
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