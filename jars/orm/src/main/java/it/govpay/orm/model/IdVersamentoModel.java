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

import it.govpay.orm.IdVersamento;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model IdVersamento 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class IdVersamentoModel extends AbstractModel<IdVersamento> {

	public IdVersamentoModel(){
	
		super();
	
		this.COD_VERSAMENTO_ENTE = new Field("codVersamentoEnte",java.lang.String.class,"id-versamento",IdVersamento.class);
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new Field("idApplicazione",it.govpay.orm.IdApplicazione.class,"id-versamento",IdVersamento.class));
		this.SRC_DEBITORE_IDENTIFICATIVO = new Field("srcDebitoreIdentificativo",java.lang.String.class,"id-versamento",IdVersamento.class);
		this.DEBITORE_ANAGRAFICA = new Field("debitoreAnagrafica",java.lang.String.class,"id-versamento",IdVersamento.class);
		this.COD_VERSAMENTO_LOTTO = new Field("codVersamentoLotto",java.lang.String.class,"id-versamento",IdVersamento.class);
		this.COD_ANNO_TRIBUTARIO = new Field("codAnnoTributario",java.lang.String.class,"id-versamento",IdVersamento.class);
		this.IMPORTO_TOTALE = new Field("importoTotale",double.class,"id-versamento",IdVersamento.class);
		this.CAUSALE_VERSAMENTO = new Field("causaleVersamento",java.lang.String.class,"id-versamento",IdVersamento.class);
		this.STATO_VERSAMENTO = new Field("statoVersamento",java.lang.String.class,"id-versamento",IdVersamento.class);
		this.ID_UO = new it.govpay.orm.model.IdUoModel(new Field("idUo",it.govpay.orm.IdUo.class,"id-versamento",IdVersamento.class));
		this.ID_TIPO_VERSAMENTO = new it.govpay.orm.model.IdTipoVersamentoModel(new Field("idTipoVersamento",it.govpay.orm.IdTipoVersamento.class,"id-versamento",IdVersamento.class));
		this.DIVISIONE = new Field("divisione",java.lang.String.class,"id-versamento",IdVersamento.class);
		this.DIREZIONE = new Field("direzione",java.lang.String.class,"id-versamento",IdVersamento.class);
		this.TASSONOMIA = new Field("tassonomia",java.lang.String.class,"id-versamento",IdVersamento.class);
	
	}
	
	public IdVersamentoModel(IField father){
	
		super(father);
	
		this.COD_VERSAMENTO_ENTE = new ComplexField(father,"codVersamentoEnte",java.lang.String.class,"id-versamento",IdVersamento.class);
		this.ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new ComplexField(father,"idApplicazione",it.govpay.orm.IdApplicazione.class,"id-versamento",IdVersamento.class));
		this.SRC_DEBITORE_IDENTIFICATIVO = new ComplexField(father,"srcDebitoreIdentificativo",java.lang.String.class,"id-versamento",IdVersamento.class);
		this.DEBITORE_ANAGRAFICA = new ComplexField(father,"debitoreAnagrafica",java.lang.String.class,"id-versamento",IdVersamento.class);
		this.COD_VERSAMENTO_LOTTO = new ComplexField(father,"codVersamentoLotto",java.lang.String.class,"id-versamento",IdVersamento.class);
		this.COD_ANNO_TRIBUTARIO = new ComplexField(father,"codAnnoTributario",java.lang.String.class,"id-versamento",IdVersamento.class);
		this.IMPORTO_TOTALE = new ComplexField(father,"importoTotale",double.class,"id-versamento",IdVersamento.class);
		this.CAUSALE_VERSAMENTO = new ComplexField(father,"causaleVersamento",java.lang.String.class,"id-versamento",IdVersamento.class);
		this.STATO_VERSAMENTO = new ComplexField(father,"statoVersamento",java.lang.String.class,"id-versamento",IdVersamento.class);
		this.ID_UO = new it.govpay.orm.model.IdUoModel(new ComplexField(father,"idUo",it.govpay.orm.IdUo.class,"id-versamento",IdVersamento.class));
		this.ID_TIPO_VERSAMENTO = new it.govpay.orm.model.IdTipoVersamentoModel(new ComplexField(father,"idTipoVersamento",it.govpay.orm.IdTipoVersamento.class,"id-versamento",IdVersamento.class));
		this.DIVISIONE = new ComplexField(father,"divisione",java.lang.String.class,"id-versamento",IdVersamento.class);
		this.DIREZIONE = new ComplexField(father,"direzione",java.lang.String.class,"id-versamento",IdVersamento.class);
		this.TASSONOMIA = new ComplexField(father,"tassonomia",java.lang.String.class,"id-versamento",IdVersamento.class);
	
	}
	
	

	public IField COD_VERSAMENTO_ENTE = null;
	 
	public it.govpay.orm.model.IdApplicazioneModel ID_APPLICAZIONE = null;
	 
	public IField SRC_DEBITORE_IDENTIFICATIVO = null;
	 
	public IField DEBITORE_ANAGRAFICA = null;
	 
	public IField COD_VERSAMENTO_LOTTO = null;
	 
	public IField COD_ANNO_TRIBUTARIO = null;
	 
	public IField IMPORTO_TOTALE = null;
	 
	public IField CAUSALE_VERSAMENTO = null;
	 
	public IField STATO_VERSAMENTO = null;
	 
	public it.govpay.orm.model.IdUoModel ID_UO = null;
	 
	public it.govpay.orm.model.IdTipoVersamentoModel ID_TIPO_VERSAMENTO = null;
	 
	public IField DIVISIONE = null;
	 
	public IField DIREZIONE = null;
	 
	public IField TASSONOMIA = null;
	 

	@Override
	public Class<IdVersamento> getModeledClass(){
		return IdVersamento.class;
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
