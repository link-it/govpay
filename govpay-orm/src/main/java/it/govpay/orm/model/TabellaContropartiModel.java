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

import it.govpay.orm.TabellaControparti;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model TabellaControparti 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class TabellaContropartiModel extends AbstractModel<TabellaControparti> {

	public TabellaContropartiModel(){
	
		super();
	
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new Field("idDominio",it.govpay.orm.IdDominio.class,"TabellaControparti",TabellaControparti.class));
		this.ID_FLUSSO = new Field("idFlusso",java.lang.String.class,"TabellaControparti",TabellaControparti.class);
		this.DATA_ORA_PUBBLICAZIONE = new Field("dataOraPubblicazione",java.util.Date.class,"TabellaControparti",TabellaControparti.class);
		this.DATA_ORA_INIZIO_VALIDITA = new Field("dataOraInizioValidita",java.util.Date.class,"TabellaControparti",TabellaControparti.class);
		this.XML = new Field("xml",byte[].class,"TabellaControparti",TabellaControparti.class);
	
	}
	
	public TabellaContropartiModel(IField father){
	
		super(father);
	
		this.ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new ComplexField(father,"idDominio",it.govpay.orm.IdDominio.class,"TabellaControparti",TabellaControparti.class));
		this.ID_FLUSSO = new ComplexField(father,"idFlusso",java.lang.String.class,"TabellaControparti",TabellaControparti.class);
		this.DATA_ORA_PUBBLICAZIONE = new ComplexField(father,"dataOraPubblicazione",java.util.Date.class,"TabellaControparti",TabellaControparti.class);
		this.DATA_ORA_INIZIO_VALIDITA = new ComplexField(father,"dataOraInizioValidita",java.util.Date.class,"TabellaControparti",TabellaControparti.class);
		this.XML = new ComplexField(father,"xml",byte[].class,"TabellaControparti",TabellaControparti.class);
	
	}
	
	

	public it.govpay.orm.model.IdDominioModel ID_DOMINIO = null;
	 
	public IField ID_FLUSSO = null;
	 
	public IField DATA_ORA_PUBBLICAZIONE = null;
	 
	public IField DATA_ORA_INIZIO_VALIDITA = null;
	 
	public IField XML = null;
	 

	@Override
	public Class<TabellaControparti> getModeledClass(){
		return TabellaControparti.class;
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