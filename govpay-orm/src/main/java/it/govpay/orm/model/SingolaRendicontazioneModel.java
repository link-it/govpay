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

import it.govpay.orm.SingolaRendicontazione;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model SingolaRendicontazione 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class SingolaRendicontazioneModel extends AbstractModel<SingolaRendicontazione> {

	public SingolaRendicontazioneModel(){
	
		super();
	
		this.ID_FR = new it.govpay.orm.model.IdFrModel(new Field("idFr",it.govpay.orm.IdFr.class,"SingolaRendicontazione",SingolaRendicontazione.class));
		this.ID_SINGOLO_VERSAMENTO = new it.govpay.orm.model.IdSingoloVersamentoModel(new Field("idSingoloVersamento",it.govpay.orm.IdSingoloVersamento.class,"SingolaRendicontazione",SingolaRendicontazione.class));
		this.IUV = new Field("iuv",java.lang.String.class,"SingolaRendicontazione",SingolaRendicontazione.class);
		this.IUR = new Field("iur",java.lang.String.class,"SingolaRendicontazione",SingolaRendicontazione.class);
		this.SINGOLO_IMPORTO = new Field("singoloImporto",double.class,"SingolaRendicontazione",SingolaRendicontazione.class);
		this.CODICE_ESITO = new Field("codiceEsito",int.class,"SingolaRendicontazione",SingolaRendicontazione.class);
		this.DATA_ESITO = new Field("dataEsito",java.util.Date.class,"SingolaRendicontazione",SingolaRendicontazione.class);
	
	}
	
	public SingolaRendicontazioneModel(IField father){
	
		super(father);
	
		this.ID_FR = new it.govpay.orm.model.IdFrModel(new ComplexField(father,"idFr",it.govpay.orm.IdFr.class,"SingolaRendicontazione",SingolaRendicontazione.class));
		this.ID_SINGOLO_VERSAMENTO = new it.govpay.orm.model.IdSingoloVersamentoModel(new ComplexField(father,"idSingoloVersamento",it.govpay.orm.IdSingoloVersamento.class,"SingolaRendicontazione",SingolaRendicontazione.class));
		this.IUV = new ComplexField(father,"iuv",java.lang.String.class,"SingolaRendicontazione",SingolaRendicontazione.class);
		this.IUR = new ComplexField(father,"iur",java.lang.String.class,"SingolaRendicontazione",SingolaRendicontazione.class);
		this.SINGOLO_IMPORTO = new ComplexField(father,"singoloImporto",double.class,"SingolaRendicontazione",SingolaRendicontazione.class);
		this.CODICE_ESITO = new ComplexField(father,"codiceEsito",int.class,"SingolaRendicontazione",SingolaRendicontazione.class);
		this.DATA_ESITO = new ComplexField(father,"dataEsito",java.util.Date.class,"SingolaRendicontazione",SingolaRendicontazione.class);
	
	}
	
	

	public it.govpay.orm.model.IdFrModel ID_FR = null;
	 
	public it.govpay.orm.model.IdSingoloVersamentoModel ID_SINGOLO_VERSAMENTO = null;
	 
	public IField IUV = null;
	 
	public IField IUR = null;
	 
	public IField SINGOLO_IMPORTO = null;
	 
	public IField CODICE_ESITO = null;
	 
	public IField DATA_ESITO = null;
	 

	@Override
	public Class<SingolaRendicontazione> getModeledClass(){
		return SingolaRendicontazione.class;
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