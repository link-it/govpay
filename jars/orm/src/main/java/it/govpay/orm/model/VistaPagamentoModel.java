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

import it.govpay.orm.VistaPagamento;

import org.openspcoop2.generic_project.beans.AbstractModel;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.Field;
import org.openspcoop2.generic_project.beans.ComplexField;


/**     
 * Model VistaPagamento 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class VistaPagamentoModel extends AbstractModel<VistaPagamento> {

	public VistaPagamentoModel(){
	
		super();
	
		this.COD_DOMINIO = new Field("codDominio",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.IUV = new Field("iuv",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.INDICE_DATI = new Field("indiceDati",int.class,"VistaPagamento",VistaPagamento.class);
		this.IMPORTO_PAGATO = new Field("importoPagato",double.class,"VistaPagamento",VistaPagamento.class);
		this.DATA_ACQUISIZIONE = new Field("dataAcquisizione",java.util.Date.class,"VistaPagamento",VistaPagamento.class);
		this.IUR = new Field("iur",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.DATA_PAGAMENTO = new Field("dataPagamento",java.util.Date.class,"VistaPagamento",VistaPagamento.class);
		this.COMMISSIONI_PSP = new Field("commissioniPsp",java.lang.Double.class,"VistaPagamento",VistaPagamento.class);
		this.TIPO_ALLEGATO = new Field("tipoAllegato",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.ALLEGATO = new Field("allegato",byte[].class,"VistaPagamento",VistaPagamento.class);
		this.DATA_ACQUISIZIONE_REVOCA = new Field("dataAcquisizioneRevoca",java.util.Date.class,"VistaPagamento",VistaPagamento.class);
		this.CAUSALE_REVOCA = new Field("causaleRevoca",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.DATI_REVOCA = new Field("datiRevoca",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.IMPORTO_REVOCATO = new Field("importoRevocato",java.lang.Double.class,"VistaPagamento",VistaPagamento.class);
		this.ESITO_REVOCA = new Field("esitoRevoca",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.DATI_ESITO_REVOCA = new Field("datiEsitoRevoca",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.STATO = new Field("stato",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.TIPO = new Field("tipo",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.ID_RPT = new it.govpay.orm.model.IdRptModel(new Field("idRPT",it.govpay.orm.IdRpt.class,"VistaPagamento",VistaPagamento.class));
		this.ID_SINGOLO_VERSAMENTO = new it.govpay.orm.model.IdSingoloVersamentoModel(new Field("idSingoloVersamento",it.govpay.orm.IdSingoloVersamento.class,"VistaPagamento",VistaPagamento.class));
		this.ID_RR = new it.govpay.orm.model.IdRrModel(new Field("idRr",it.govpay.orm.IdRr.class,"VistaPagamento",VistaPagamento.class));
		this.ID_INCASSO = new it.govpay.orm.model.IdIncassoModel(new Field("idIncasso",it.govpay.orm.IdIncasso.class,"VistaPagamento",VistaPagamento.class));
		this.VRS_ID = new Field("vrsId",long.class,"VistaPagamento",VistaPagamento.class);
		this.VRS_COD_VERSAMENTO_ENTE = new Field("vrsCodVersamentoEnte",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.VRS_ID_TIPO_VERSAMENTO_DOMINIO = new it.govpay.orm.model.IdTipoVersamentoDominioModel(new Field("vrsIdTipoVersamentoDominio",it.govpay.orm.IdTipoVersamentoDominio.class,"VistaPagamento",VistaPagamento.class));
		this.VRS_ID_TIPO_VERSAMENTO = new it.govpay.orm.model.IdTipoVersamentoModel(new Field("vrsIdTipoVersamento",it.govpay.orm.IdTipoVersamento.class,"VistaPagamento",VistaPagamento.class));
		this.VRS_ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new Field("vrsIdDominio",it.govpay.orm.IdDominio.class,"VistaPagamento",VistaPagamento.class));
		this.VRS_ID_UO = new it.govpay.orm.model.IdUoModel(new Field("vrsIdUo",it.govpay.orm.IdUo.class,"VistaPagamento",VistaPagamento.class));
		this.VRS_ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new Field("vrsIdApplicazione",it.govpay.orm.IdApplicazione.class,"VistaPagamento",VistaPagamento.class));
		this.VRS_ID_DOCUMENTO = new it.govpay.orm.model.IdDocumentoModel(new Field("vrsIdDocumento",it.govpay.orm.IdDocumento.class,"VistaPagamento",VistaPagamento.class));
		this.VRS_TASSONOMIA = new Field("vrsTassonomia",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.VRS_DIVISIONE = new Field("vrsDivisione",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.VRS_DIREZIONE = new Field("vrsDirezione",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.SNG_COD_SING_VERS_ENTE = new Field("sngCodSingVersEnte",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.RPT_IUV = new Field("rptIuv",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.RPT_CCP = new Field("rptCcp",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.RNC_TRN = new Field("rncTrn",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
	
	}
	
	public VistaPagamentoModel(IField father){
	
		super(father);
	
		this.COD_DOMINIO = new ComplexField(father,"codDominio",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.IUV = new ComplexField(father,"iuv",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.INDICE_DATI = new ComplexField(father,"indiceDati",int.class,"VistaPagamento",VistaPagamento.class);
		this.IMPORTO_PAGATO = new ComplexField(father,"importoPagato",double.class,"VistaPagamento",VistaPagamento.class);
		this.DATA_ACQUISIZIONE = new ComplexField(father,"dataAcquisizione",java.util.Date.class,"VistaPagamento",VistaPagamento.class);
		this.IUR = new ComplexField(father,"iur",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.DATA_PAGAMENTO = new ComplexField(father,"dataPagamento",java.util.Date.class,"VistaPagamento",VistaPagamento.class);
		this.COMMISSIONI_PSP = new ComplexField(father,"commissioniPsp",java.lang.Double.class,"VistaPagamento",VistaPagamento.class);
		this.TIPO_ALLEGATO = new ComplexField(father,"tipoAllegato",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.ALLEGATO = new ComplexField(father,"allegato",byte[].class,"VistaPagamento",VistaPagamento.class);
		this.DATA_ACQUISIZIONE_REVOCA = new ComplexField(father,"dataAcquisizioneRevoca",java.util.Date.class,"VistaPagamento",VistaPagamento.class);
		this.CAUSALE_REVOCA = new ComplexField(father,"causaleRevoca",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.DATI_REVOCA = new ComplexField(father,"datiRevoca",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.IMPORTO_REVOCATO = new ComplexField(father,"importoRevocato",java.lang.Double.class,"VistaPagamento",VistaPagamento.class);
		this.ESITO_REVOCA = new ComplexField(father,"esitoRevoca",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.DATI_ESITO_REVOCA = new ComplexField(father,"datiEsitoRevoca",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.STATO = new ComplexField(father,"stato",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.TIPO = new ComplexField(father,"tipo",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.ID_RPT = new it.govpay.orm.model.IdRptModel(new ComplexField(father,"idRPT",it.govpay.orm.IdRpt.class,"VistaPagamento",VistaPagamento.class));
		this.ID_SINGOLO_VERSAMENTO = new it.govpay.orm.model.IdSingoloVersamentoModel(new ComplexField(father,"idSingoloVersamento",it.govpay.orm.IdSingoloVersamento.class,"VistaPagamento",VistaPagamento.class));
		this.ID_RR = new it.govpay.orm.model.IdRrModel(new ComplexField(father,"idRr",it.govpay.orm.IdRr.class,"VistaPagamento",VistaPagamento.class));
		this.ID_INCASSO = new it.govpay.orm.model.IdIncassoModel(new ComplexField(father,"idIncasso",it.govpay.orm.IdIncasso.class,"VistaPagamento",VistaPagamento.class));
		this.VRS_ID = new ComplexField(father,"vrsId",long.class,"VistaPagamento",VistaPagamento.class);
		this.VRS_COD_VERSAMENTO_ENTE = new ComplexField(father,"vrsCodVersamentoEnte",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.VRS_ID_TIPO_VERSAMENTO_DOMINIO = new it.govpay.orm.model.IdTipoVersamentoDominioModel(new ComplexField(father,"vrsIdTipoVersamentoDominio",it.govpay.orm.IdTipoVersamentoDominio.class,"VistaPagamento",VistaPagamento.class));
		this.VRS_ID_TIPO_VERSAMENTO = new it.govpay.orm.model.IdTipoVersamentoModel(new ComplexField(father,"vrsIdTipoVersamento",it.govpay.orm.IdTipoVersamento.class,"VistaPagamento",VistaPagamento.class));
		this.VRS_ID_DOMINIO = new it.govpay.orm.model.IdDominioModel(new ComplexField(father,"vrsIdDominio",it.govpay.orm.IdDominio.class,"VistaPagamento",VistaPagamento.class));
		this.VRS_ID_UO = new it.govpay.orm.model.IdUoModel(new ComplexField(father,"vrsIdUo",it.govpay.orm.IdUo.class,"VistaPagamento",VistaPagamento.class));
		this.VRS_ID_APPLICAZIONE = new it.govpay.orm.model.IdApplicazioneModel(new ComplexField(father,"vrsIdApplicazione",it.govpay.orm.IdApplicazione.class,"VistaPagamento",VistaPagamento.class));
		this.VRS_ID_DOCUMENTO = new it.govpay.orm.model.IdDocumentoModel(new ComplexField(father,"vrsIdDocumento",it.govpay.orm.IdDocumento.class,"VistaPagamento",VistaPagamento.class));
		this.VRS_TASSONOMIA = new ComplexField(father,"vrsTassonomia",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.VRS_DIVISIONE = new ComplexField(father,"vrsDivisione",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.VRS_DIREZIONE = new ComplexField(father,"vrsDirezione",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.SNG_COD_SING_VERS_ENTE = new ComplexField(father,"sngCodSingVersEnte",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.RPT_IUV = new ComplexField(father,"rptIuv",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.RPT_CCP = new ComplexField(father,"rptCcp",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
		this.RNC_TRN = new ComplexField(father,"rncTrn",java.lang.String.class,"VistaPagamento",VistaPagamento.class);
	
	}
	
	

	public IField COD_DOMINIO = null;
	 
	public IField IUV = null;
	 
	public IField INDICE_DATI = null;
	 
	public IField IMPORTO_PAGATO = null;
	 
	public IField DATA_ACQUISIZIONE = null;
	 
	public IField IUR = null;
	 
	public IField DATA_PAGAMENTO = null;
	 
	public IField COMMISSIONI_PSP = null;
	 
	public IField TIPO_ALLEGATO = null;
	 
	public IField ALLEGATO = null;
	 
	public IField DATA_ACQUISIZIONE_REVOCA = null;
	 
	public IField CAUSALE_REVOCA = null;
	 
	public IField DATI_REVOCA = null;
	 
	public IField IMPORTO_REVOCATO = null;
	 
	public IField ESITO_REVOCA = null;
	 
	public IField DATI_ESITO_REVOCA = null;
	 
	public IField STATO = null;
	 
	public IField TIPO = null;
	 
	public it.govpay.orm.model.IdRptModel ID_RPT = null;
	 
	public it.govpay.orm.model.IdSingoloVersamentoModel ID_SINGOLO_VERSAMENTO = null;
	 
	public it.govpay.orm.model.IdRrModel ID_RR = null;
	 
	public it.govpay.orm.model.IdIncassoModel ID_INCASSO = null;
	 
	public IField VRS_ID = null;
	 
	public IField VRS_COD_VERSAMENTO_ENTE = null;
	 
	public it.govpay.orm.model.IdTipoVersamentoDominioModel VRS_ID_TIPO_VERSAMENTO_DOMINIO = null;
	 
	public it.govpay.orm.model.IdTipoVersamentoModel VRS_ID_TIPO_VERSAMENTO = null;
	 
	public it.govpay.orm.model.IdDominioModel VRS_ID_DOMINIO = null;
	 
	public it.govpay.orm.model.IdUoModel VRS_ID_UO = null;
	 
	public it.govpay.orm.model.IdApplicazioneModel VRS_ID_APPLICAZIONE = null;
	 
	public it.govpay.orm.model.IdDocumentoModel VRS_ID_DOCUMENTO = null;
	 
	public IField VRS_TASSONOMIA = null;
	 
	public IField VRS_DIVISIONE = null;
	 
	public IField VRS_DIREZIONE = null;
	 
	public IField SNG_COD_SING_VERS_ENTE = null;
	 
	public IField RPT_IUV = null;
	 
	public IField RPT_CCP = null;
	 
	public IField RNC_TRN = null;
	 

	@Override
	public Class<VistaPagamento> getModeledClass(){
		return VistaPagamento.class;
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
