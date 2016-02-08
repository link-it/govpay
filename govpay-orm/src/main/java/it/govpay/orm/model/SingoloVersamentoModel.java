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

import it.govpay.orm.SingoloVersamento;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model SingoloVersamento 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class SingoloVersamentoModel extends AbstractModel<SingoloVersamento> {

	public SingoloVersamentoModel(){
	
		super();
	
		this.ID_VERSAMENTO = new it.govpay.orm.model.IdVersamentoModel(new Field("idVersamento",it.govpay.orm.IdVersamento.class,"SingoloVersamento",SingoloVersamento.class));
		this.INDICE = new Field("indice",int.class,"SingoloVersamento",SingoloVersamento.class);
		this.COD_SINGOLO_VERSAMENTO_ENTE = new Field("codSingoloVersamentoEnte",java.lang.String.class,"SingoloVersamento",SingoloVersamento.class);
		this.ID_TRIBUTO = new it.govpay.orm.model.IdTributoModel(new Field("idTributo",it.govpay.orm.IdTributo.class,"SingoloVersamento",SingoloVersamento.class));
		this.ANNO_RIFERIMENTO = new Field("annoRiferimento",int.class,"SingoloVersamento",SingoloVersamento.class);
		this.IBAN_ACCREDITO = new Field("ibanAccredito",java.lang.String.class,"SingoloVersamento",SingoloVersamento.class);
		this.IMPORTO_SINGOLO_VERSAMENTO = new Field("importoSingoloVersamento",double.class,"SingoloVersamento",SingoloVersamento.class);
		this.IMPORTO_COMMISSIONI_PA = new Field("importoCommissioniPA",java.lang.Double.class,"SingoloVersamento",SingoloVersamento.class);
		this.SINGOLO_IMPORTO_PAGATO = new Field("singoloImportoPagato",java.lang.Double.class,"SingoloVersamento",SingoloVersamento.class);
		this.CAUSALE_VERSAMENTO = new Field("causaleVersamento",java.lang.String.class,"SingoloVersamento",SingoloVersamento.class);
		this.DATI_SPECIFICI_RISCOSSIONE = new Field("datiSpecificiRiscossione",java.lang.String.class,"SingoloVersamento",SingoloVersamento.class);
		this.STATO_SINGOLO_VERSAMENTO = new Field("statoSingoloVersamento",java.lang.String.class,"SingoloVersamento",SingoloVersamento.class);
		this.ESITO_SINGOLO_PAGAMENTO = new Field("esitoSingoloPagamento",java.lang.String.class,"SingoloVersamento",SingoloVersamento.class);
		this.DATA_ESITO_SINGOLO_PAGAMENTO = new Field("dataEsitoSingoloPagamento",java.util.Date.class,"SingoloVersamento",SingoloVersamento.class);
		this.IUR = new Field("iur",java.lang.String.class,"SingoloVersamento",SingoloVersamento.class);
	
	}
	
	public SingoloVersamentoModel(IField father){
	
		super(father);
	
		this.ID_VERSAMENTO = new it.govpay.orm.model.IdVersamentoModel(new ComplexField(father,"idVersamento",it.govpay.orm.IdVersamento.class,"SingoloVersamento",SingoloVersamento.class));
		this.INDICE = new ComplexField(father,"indice",int.class,"SingoloVersamento",SingoloVersamento.class);
		this.COD_SINGOLO_VERSAMENTO_ENTE = new ComplexField(father,"codSingoloVersamentoEnte",java.lang.String.class,"SingoloVersamento",SingoloVersamento.class);
		this.ID_TRIBUTO = new it.govpay.orm.model.IdTributoModel(new ComplexField(father,"idTributo",it.govpay.orm.IdTributo.class,"SingoloVersamento",SingoloVersamento.class));
		this.ANNO_RIFERIMENTO = new ComplexField(father,"annoRiferimento",int.class,"SingoloVersamento",SingoloVersamento.class);
		this.IBAN_ACCREDITO = new ComplexField(father,"ibanAccredito",java.lang.String.class,"SingoloVersamento",SingoloVersamento.class);
		this.IMPORTO_SINGOLO_VERSAMENTO = new ComplexField(father,"importoSingoloVersamento",double.class,"SingoloVersamento",SingoloVersamento.class);
		this.IMPORTO_COMMISSIONI_PA = new ComplexField(father,"importoCommissioniPA",java.lang.Double.class,"SingoloVersamento",SingoloVersamento.class);
		this.SINGOLO_IMPORTO_PAGATO = new ComplexField(father,"singoloImportoPagato",java.lang.Double.class,"SingoloVersamento",SingoloVersamento.class);
		this.CAUSALE_VERSAMENTO = new ComplexField(father,"causaleVersamento",java.lang.String.class,"SingoloVersamento",SingoloVersamento.class);
		this.DATI_SPECIFICI_RISCOSSIONE = new ComplexField(father,"datiSpecificiRiscossione",java.lang.String.class,"SingoloVersamento",SingoloVersamento.class);
		this.STATO_SINGOLO_VERSAMENTO = new ComplexField(father,"statoSingoloVersamento",java.lang.String.class,"SingoloVersamento",SingoloVersamento.class);
		this.ESITO_SINGOLO_PAGAMENTO = new ComplexField(father,"esitoSingoloPagamento",java.lang.String.class,"SingoloVersamento",SingoloVersamento.class);
		this.DATA_ESITO_SINGOLO_PAGAMENTO = new ComplexField(father,"dataEsitoSingoloPagamento",java.util.Date.class,"SingoloVersamento",SingoloVersamento.class);
		this.IUR = new ComplexField(father,"iur",java.lang.String.class,"SingoloVersamento",SingoloVersamento.class);
	
	}
	
	

	public it.govpay.orm.model.IdVersamentoModel ID_VERSAMENTO = null;
	 
	public IField INDICE = null;
	 
	public IField COD_SINGOLO_VERSAMENTO_ENTE = null;
	 
	public it.govpay.orm.model.IdTributoModel ID_TRIBUTO = null;
	 
	public IField ANNO_RIFERIMENTO = null;
	 
	public IField IBAN_ACCREDITO = null;
	 
	public IField IMPORTO_SINGOLO_VERSAMENTO = null;
	 
	public IField IMPORTO_COMMISSIONI_PA = null;
	 
	public IField SINGOLO_IMPORTO_PAGATO = null;
	 
	public IField CAUSALE_VERSAMENTO = null;
	 
	public IField DATI_SPECIFICI_RISCOSSIONE = null;
	 
	public IField STATO_SINGOLO_VERSAMENTO = null;
	 
	public IField ESITO_SINGOLO_PAGAMENTO = null;
	 
	public IField DATA_ESITO_SINGOLO_PAGAMENTO = null;
	 
	public IField IUR = null;
	 

	@Override
	public Class<SingoloVersamento> getModeledClass(){
		return SingoloVersamento.class;
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