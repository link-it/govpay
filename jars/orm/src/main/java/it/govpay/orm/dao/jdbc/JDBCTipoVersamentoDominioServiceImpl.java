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
package it.govpay.orm.dao.jdbc;

import java.sql.Connection;

import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.slf4j.Logger;
import org.openspcoop2.generic_project.dao.jdbc.IJDBCServiceCRUDWithId;

import it.govpay.orm.IdTipoVersamento;
import it.govpay.orm.IdTipoVersamentoDominio;
import org.openspcoop2.generic_project.beans.NonNegativeNumber;
import org.openspcoop2.generic_project.beans.UpdateField;
import org.openspcoop2.generic_project.beans.UpdateModel;

import org.openspcoop2.generic_project.dao.jdbc.utils.JDBCUtilities;
import org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.dao.jdbc.JDBCExpression;
import org.openspcoop2.generic_project.dao.jdbc.JDBCPaginatedExpression;

import org.openspcoop2.generic_project.dao.jdbc.JDBCServiceManagerProperties;

import it.govpay.orm.TipoVersamentoDominio;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCTipoVersamentoDominioServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCTipoVersamentoDominioServiceImpl extends JDBCTipoVersamentoDominioServiceSearchImpl
	implements IJDBCServiceCRUDWithId<TipoVersamentoDominio, IdTipoVersamentoDominio, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, TipoVersamentoDominio tipoVersamentoDominio, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				

		// Object _tipoVersamento
		Long id_tipoVersamento = null;
		it.govpay.orm.IdTipoVersamento idLogic_tipoVersamento = new IdTipoVersamento();
		idLogic_tipoVersamento.setCodTipoVersamento(tipoVersamentoDominio.getTipoVersamento().getCodTipoVersamento());
		idLogic_tipoVersamento.setId(tipoVersamentoDominio.getTipoVersamento().getId());
		if(idLogic_tipoVersamento!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_tipoVersamento = ((JDBCTipoVersamentoServiceSearch)(this.getServiceManager().getTipoVersamentoServiceSearch())).findTableId(idLogic_tipoVersamento, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_tipoVersamento = idLogic_tipoVersamento.getId();
				if(id_tipoVersamento==null || id_tipoVersamento<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _dominio
		Long id_dominio = null;
		it.govpay.orm.IdDominio idLogic_dominio = null;
		idLogic_dominio = tipoVersamentoDominio.getIdDominio();
		if(idLogic_dominio!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_dominio = ((JDBCDominioServiceSearch)(this.getServiceManager().getDominioServiceSearch())).findTableId(idLogic_dominio, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_dominio = idLogic_dominio.getId();
				if(id_dominio==null || id_dominio<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object tipoVersamentoDominio
		sqlQueryObjectInsert.addInsertTable(this.getTipoVersamentoDominioFieldConverter().toTable(TipoVersamentoDominio.model()));
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().CODIFICA_IUV,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().TIPO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().PAGA_TERZI,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().ABILITATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().BO_FORM_TIPO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().BO_FORM_DEFINIZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().BO_VALIDAZIONE_DEF,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().BO_TRASFORMAZIONE_TIPO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().BO_TRASFORMAZIONE_DEF,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().BO_COD_APPLICAZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().BO_ABILITATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().PAG_FORM_TIPO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().PAG_FORM_DEFINIZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().PAG_VALIDAZIONE_DEF,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().PAG_TRASFORMAZIONE_TIPO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().PAG_TRASFORMAZIONE_DEF,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().PAG_COD_APPLICAZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().PAG_ABILITATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_ABILITATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_PDF,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_TIPO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_OGGETTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_MESSAGGIO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_ABILITATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_PDF,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_TIPO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_OGGETTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_MESSAGGIO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_ESEGUITI,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_ABILITATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_PREAVVISO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_TIPO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_OGGETTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_MESSAGGIO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().VISUALIZZAZIONE_DEFINIZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().TRAC_CSV_TIPO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().TRAC_CSV_HEADER_RISPOSTA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().TRAC_CSV_TEMPLATE_RICHIESTA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().TRAC_CSV_TEMPLATE_RISPOSTA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().APP_IO_API_KEY,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_ABILITATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_TIPO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_OGGETTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_MESSAGGIO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_ABILITATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_TIPO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_OGGETTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_MESSAGGIO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_ESEGUITI,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_ABILITATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_PREAVVISO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_TIPO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_OGGETTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_MESSAGGIO,false),"?");
		sqlQueryObjectInsert.addInsertField("id_tipo_versamento","?");
		sqlQueryObjectInsert.addInsertField("id_dominio","?");

		// Insert tipoVersamentoDominio
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getTipoVersamentoDominioFetch().getKeyGeneratorObject(TipoVersamentoDominio.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getCodificaIuv(),TipoVersamentoDominio.model().CODIFICA_IUV.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getTipo(),TipoVersamentoDominio.model().TIPO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getPagaTerzi(),TipoVersamentoDominio.model().PAGA_TERZI.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAbilitato(),TipoVersamentoDominio.model().ABILITATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getBoFormTipo(),TipoVersamentoDominio.model().BO_FORM_TIPO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getBoFormDefinizione(),TipoVersamentoDominio.model().BO_FORM_DEFINIZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getBoValidazioneDef(),TipoVersamentoDominio.model().BO_VALIDAZIONE_DEF.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getBoTrasformazioneTipo(),TipoVersamentoDominio.model().BO_TRASFORMAZIONE_TIPO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getBoTrasformazioneDef(),TipoVersamentoDominio.model().BO_TRASFORMAZIONE_DEF.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getBoCodApplicazione(),TipoVersamentoDominio.model().BO_COD_APPLICAZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getBoAbilitato(),TipoVersamentoDominio.model().BO_ABILITATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getPagFormTipo(),TipoVersamentoDominio.model().PAG_FORM_TIPO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getPagFormDefinizione(),TipoVersamentoDominio.model().PAG_FORM_DEFINIZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getPagValidazioneDef(),TipoVersamentoDominio.model().PAG_VALIDAZIONE_DEF.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getPagTrasformazioneTipo(),TipoVersamentoDominio.model().PAG_TRASFORMAZIONE_TIPO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getPagTrasformazioneDef(),TipoVersamentoDominio.model().PAG_TRASFORMAZIONE_DEF.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getPagCodApplicazione(),TipoVersamentoDominio.model().PAG_COD_APPLICAZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getPagAbilitato(),TipoVersamentoDominio.model().PAG_ABILITATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvMailPromAvvAbilitato(),TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_ABILITATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvMailPromAvvPdf(),TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_PDF.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvMailPromAvvTipo(),TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_TIPO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvMailPromAvvOggetto(),TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_OGGETTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvMailPromAvvMessaggio(),TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_MESSAGGIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvMailPromRicAbilitato(),TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_ABILITATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvMailPromRicPdf(),TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_PDF.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvMailPromRicTipo(),TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_TIPO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvMailPromRicOggetto(),TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_OGGETTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvMailPromRicMessaggio(),TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_MESSAGGIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvMailPromRicEseguiti(),TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_ESEGUITI.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvMailPromScadAbilitato(),TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_ABILITATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvMailPromScadPreavviso(),TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_PREAVVISO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvMailPromScadTipo(),TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_TIPO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvMailPromScadOggetto(),TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_OGGETTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvMailPromScadMessaggio(),TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_MESSAGGIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getVisualizzazioneDefinizione(),TipoVersamentoDominio.model().VISUALIZZAZIONE_DEFINIZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getTracCsvTipo(),TipoVersamentoDominio.model().TRAC_CSV_TIPO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getTracCsvHeaderRisposta(),TipoVersamentoDominio.model().TRAC_CSV_HEADER_RISPOSTA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getTracCsvTemplateRichiesta(),TipoVersamentoDominio.model().TRAC_CSV_TEMPLATE_RICHIESTA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getTracCsvTemplateRisposta(),TipoVersamentoDominio.model().TRAC_CSV_TEMPLATE_RISPOSTA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAppIoApiKey(),TipoVersamentoDominio.model().APP_IO_API_KEY.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvAppIoPromAvvAbilitato(),TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_ABILITATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvAppIoPromAvvTipo(),TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_TIPO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvAppIoPromAvvOggetto(),TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_OGGETTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvAppIoPromAvvMessaggio(),TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_MESSAGGIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvAppIoPromRicAbilitato(),TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_ABILITATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvAppIoPromRicTipo(),TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_TIPO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvAppIoPromRicOggetto(),TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_OGGETTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvAppIoPromRicMessaggio(),TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_MESSAGGIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvAppIoPromRicEseguiti(),TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_ESEGUITI.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvAppIoPromScadAbilitato(),TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_ABILITATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvAppIoPromScadPreavviso(),TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_PREAVVISO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvAppIoPromScadTipo(),TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_TIPO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvAppIoPromScadOggetto(),TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_OGGETTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamentoDominio.getAvvAppIoPromScadMessaggio(),TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_MESSAGGIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_tipoVersamento,Long.class),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(id_dominio,Long.class)
		);
		tipoVersamentoDominio.setId(id);

	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTipoVersamentoDominio oldId, TipoVersamentoDominio tipoVersamentoDominio, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdTipoVersamentoDominio(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = tipoVersamentoDominio.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: tipoVersamentoDominio.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			tipoVersamentoDominio.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, tipoVersamentoDominio, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, TipoVersamentoDominio tipoVersamentoDominio, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
		ISQLQueryObject sqlQueryObjectDelete = sqlQueryObjectInsert.newSQLQueryObject();
		ISQLQueryObject sqlQueryObjectGet = sqlQueryObjectDelete.newSQLQueryObject();
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObjectGet.newSQLQueryObject();
		
		boolean setIdMappingResolutionBehaviour = 
			(idMappingResolutionBehaviour==null) ||
			org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) ||
			org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour);
			

		// Object _tipoVersamentoDominio_tipoVersamento
		Long id_tipoVersamentoDominio_tipoVersamento = null;
		it.govpay.orm.IdTipoVersamento idLogic_tipoVersamentoDominio_tipoVersamento = new IdTipoVersamento();
		idLogic_tipoVersamentoDominio_tipoVersamento.setCodTipoVersamento(tipoVersamentoDominio.getTipoVersamento().getCodTipoVersamento());
		idLogic_tipoVersamentoDominio_tipoVersamento.setId(tipoVersamentoDominio.getTipoVersamento().getId());
		if(idLogic_tipoVersamentoDominio_tipoVersamento!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_tipoVersamentoDominio_tipoVersamento = ((JDBCTipoVersamentoServiceSearch)(this.getServiceManager().getTipoVersamentoServiceSearch())).findTableId(idLogic_tipoVersamentoDominio_tipoVersamento, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_tipoVersamentoDominio_tipoVersamento = idLogic_tipoVersamentoDominio_tipoVersamento.getId();
				if(id_tipoVersamentoDominio_tipoVersamento==null || id_tipoVersamentoDominio_tipoVersamento<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}

		// Object _tipoVersamentoDominio_dominio
		Long id_tipoVersamentoDominio_dominio = null;
		it.govpay.orm.IdDominio idLogic_tipoVersamentoDominio_dominio = null;
		idLogic_tipoVersamentoDominio_dominio = tipoVersamentoDominio.getIdDominio();
		if(idLogic_tipoVersamentoDominio_dominio!=null){
			if(idMappingResolutionBehaviour==null ||
				(org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour))){
				id_tipoVersamentoDominio_dominio = ((JDBCDominioServiceSearch)(this.getServiceManager().getDominioServiceSearch())).findTableId(idLogic_tipoVersamentoDominio_dominio, false);
			}
			else if(org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour)){
				id_tipoVersamentoDominio_dominio = idLogic_tipoVersamentoDominio_dominio.getId();
				if(id_tipoVersamentoDominio_dominio==null || id_tipoVersamentoDominio_dominio<=0){
					throw new Exception("Logic id not contains table id");
				}
			}
		}


		// Object tipoVersamentoDominio
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getTipoVersamentoDominioFieldConverter().toTable(TipoVersamentoDominio.model()));
		boolean isUpdate_tipoVersamentoDominio = true;
		java.util.List<JDBCObject> lstObjects_tipoVersamentoDominio = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().CODIFICA_IUV,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getCodificaIuv(), TipoVersamentoDominio.model().CODIFICA_IUV.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().TIPO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getTipo(), TipoVersamentoDominio.model().TIPO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().PAGA_TERZI,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getPagaTerzi(), TipoVersamentoDominio.model().PAGA_TERZI.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().ABILITATO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAbilitato(), TipoVersamentoDominio.model().ABILITATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().BO_FORM_TIPO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getBoFormTipo(), TipoVersamentoDominio.model().BO_FORM_TIPO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().BO_FORM_DEFINIZIONE,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getBoFormDefinizione(), TipoVersamentoDominio.model().BO_FORM_DEFINIZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().BO_VALIDAZIONE_DEF,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getBoValidazioneDef(), TipoVersamentoDominio.model().BO_VALIDAZIONE_DEF.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().BO_TRASFORMAZIONE_TIPO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getBoTrasformazioneTipo(), TipoVersamentoDominio.model().BO_TRASFORMAZIONE_TIPO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().BO_TRASFORMAZIONE_DEF,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getBoTrasformazioneDef(), TipoVersamentoDominio.model().BO_TRASFORMAZIONE_DEF.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().BO_COD_APPLICAZIONE,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getBoCodApplicazione(), TipoVersamentoDominio.model().BO_COD_APPLICAZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().BO_ABILITATO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getBoAbilitato(), TipoVersamentoDominio.model().BO_ABILITATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().PAG_FORM_TIPO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getPagFormTipo(), TipoVersamentoDominio.model().PAG_FORM_TIPO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().PAG_FORM_DEFINIZIONE,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getPagFormDefinizione(), TipoVersamentoDominio.model().PAG_FORM_DEFINIZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().PAG_VALIDAZIONE_DEF,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getPagValidazioneDef(), TipoVersamentoDominio.model().PAG_VALIDAZIONE_DEF.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().PAG_TRASFORMAZIONE_TIPO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getPagTrasformazioneTipo(), TipoVersamentoDominio.model().PAG_TRASFORMAZIONE_TIPO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().PAG_TRASFORMAZIONE_DEF,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getPagTrasformazioneDef(), TipoVersamentoDominio.model().PAG_TRASFORMAZIONE_DEF.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().PAG_COD_APPLICAZIONE,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getPagCodApplicazione(), TipoVersamentoDominio.model().PAG_COD_APPLICAZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().PAG_ABILITATO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getPagAbilitato(), TipoVersamentoDominio.model().PAG_ABILITATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_ABILITATO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvMailPromAvvAbilitato(), TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_ABILITATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_PDF,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvMailPromAvvPdf(), TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_PDF.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_TIPO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvMailPromAvvTipo(), TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_TIPO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_OGGETTO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvMailPromAvvOggetto(), TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_OGGETTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_MESSAGGIO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvMailPromAvvMessaggio(), TipoVersamentoDominio.model().AVV_MAIL_PROM_AVV_MESSAGGIO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_ABILITATO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvMailPromRicAbilitato(), TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_ABILITATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_PDF,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvMailPromRicPdf(), TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_PDF.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_TIPO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvMailPromRicTipo(), TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_TIPO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_OGGETTO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvMailPromRicOggetto(), TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_OGGETTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_MESSAGGIO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvMailPromRicMessaggio(), TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_MESSAGGIO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_ESEGUITI,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvMailPromRicEseguiti(), TipoVersamentoDominio.model().AVV_MAIL_PROM_RIC_ESEGUITI.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_ABILITATO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvMailPromScadAbilitato(), TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_ABILITATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_PREAVVISO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvMailPromScadPreavviso(), TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_PREAVVISO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_TIPO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvMailPromScadTipo(), TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_TIPO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_OGGETTO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvMailPromScadOggetto(), TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_OGGETTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_MESSAGGIO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvMailPromScadMessaggio(), TipoVersamentoDominio.model().AVV_MAIL_PROM_SCAD_MESSAGGIO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().VISUALIZZAZIONE_DEFINIZIONE,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getVisualizzazioneDefinizione(), TipoVersamentoDominio.model().VISUALIZZAZIONE_DEFINIZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().TRAC_CSV_TIPO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getTracCsvTipo(), TipoVersamentoDominio.model().TRAC_CSV_TIPO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().TRAC_CSV_HEADER_RISPOSTA,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getTracCsvHeaderRisposta(), TipoVersamentoDominio.model().TRAC_CSV_HEADER_RISPOSTA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().TRAC_CSV_TEMPLATE_RICHIESTA,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getTracCsvTemplateRichiesta(), TipoVersamentoDominio.model().TRAC_CSV_TEMPLATE_RICHIESTA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().TRAC_CSV_TEMPLATE_RISPOSTA,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getTracCsvTemplateRisposta(), TipoVersamentoDominio.model().TRAC_CSV_TEMPLATE_RISPOSTA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().APP_IO_API_KEY,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAppIoApiKey(), TipoVersamentoDominio.model().APP_IO_API_KEY.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_ABILITATO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvAppIoPromAvvAbilitato(), TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_ABILITATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_TIPO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvAppIoPromAvvTipo(), TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_TIPO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_OGGETTO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvAppIoPromAvvOggetto(), TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_OGGETTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_MESSAGGIO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvAppIoPromAvvMessaggio(), TipoVersamentoDominio.model().AVV_APP_IO_PROM_AVV_MESSAGGIO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_ABILITATO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvAppIoPromRicAbilitato(), TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_ABILITATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_TIPO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvAppIoPromRicTipo(), TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_TIPO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_OGGETTO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvAppIoPromRicOggetto(), TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_OGGETTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_MESSAGGIO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvAppIoPromRicMessaggio(), TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_MESSAGGIO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_ESEGUITI,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvAppIoPromRicEseguiti(), TipoVersamentoDominio.model().AVV_APP_IO_PROM_RIC_ESEGUITI.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_ABILITATO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvAppIoPromScadAbilitato(), TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_ABILITATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_PREAVVISO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvAppIoPromScadPreavviso(), TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_PREAVVISO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_TIPO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvAppIoPromScadTipo(), TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_TIPO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_OGGETTO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvAppIoPromScadOggetto(), TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_OGGETTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoDominioFieldConverter().toColumn(TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_MESSAGGIO,false), "?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tipoVersamentoDominio.getAvvAppIoPromScadMessaggio(), TipoVersamentoDominio.model().AVV_APP_IO_PROM_SCAD_MESSAGGIO.getFieldType()));
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_tipo_versamento","?");
		}
		if(setIdMappingResolutionBehaviour){
			sqlQueryObjectUpdate.addUpdateField("id_dominio","?");
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_tipoVersamentoDominio.add(new JDBCObject(id_tipoVersamentoDominio_tipoVersamento, Long.class));
		}
		if(setIdMappingResolutionBehaviour){
			lstObjects_tipoVersamentoDominio.add(new JDBCObject(id_tipoVersamentoDominio_dominio, Long.class));
		}
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_tipoVersamentoDominio.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_tipoVersamentoDominio) {
			// Update tipoVersamentoDominio
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_tipoVersamentoDominio.toArray(new JDBCObject[]{}));
		}
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTipoVersamentoDominio id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTipoVersamentoDominioFieldConverter().toTable(TipoVersamentoDominio.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getTipoVersamentoDominioFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTipoVersamentoDominio id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTipoVersamentoDominioFieldConverter().toTable(TipoVersamentoDominio.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getTipoVersamentoDominioFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTipoVersamentoDominio id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTipoVersamentoDominioFieldConverter().toTable(TipoVersamentoDominio.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getTipoVersamentoDominioFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTipoVersamentoDominioFieldConverter().toTable(TipoVersamentoDominio.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getTipoVersamentoDominioFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTipoVersamentoDominioFieldConverter().toTable(TipoVersamentoDominio.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getTipoVersamentoDominioFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTipoVersamentoDominioFieldConverter().toTable(TipoVersamentoDominio.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getTipoVersamentoDominioFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTipoVersamentoDominio oldId, TipoVersamentoDominio tipoVersamentoDominio, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, tipoVersamentoDominio,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, tipoVersamentoDominio,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, TipoVersamentoDominio tipoVersamentoDominio, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, tipoVersamentoDominio,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, tipoVersamentoDominio,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, TipoVersamentoDominio tipoVersamentoDominio) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (tipoVersamentoDominio.getId()!=null) && (tipoVersamentoDominio.getId()>0) ){
			longId = tipoVersamentoDominio.getId();
		}
		else{
			IdTipoVersamentoDominio idTipoVersamentoDominio = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,tipoVersamentoDominio);
			longId = this.findIdTipoVersamentoDominio(jdbcProperties,log,connection,sqlQueryObject,idTipoVersamentoDominio,false);
			if(longId == null){
				return; // entry not exists
			}
		}		
		
		this._delete(jdbcProperties, log, connection, sqlQueryObject, longId);
		
	}

	private void _delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Long id) throws NotImplementedException,ServiceException,Exception {
	
		if(id!=null && id.longValue()<=0){
			throw new ServiceException("Id is less equals 0");
		}
		
		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		ISQLQueryObject sqlQueryObjectDelete = sqlQueryObject.newSQLQueryObject();
		

		// Object tipoVersamentoDominio
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getTipoVersamentoDominioFieldConverter().toTable(TipoVersamentoDominio.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete tipoVersamentoDominio
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTipoVersamentoDominio idTipoVersamentoDominio) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdTipoVersamentoDominio(jdbcProperties, log, connection, sqlQueryObject, idTipoVersamentoDominio, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getTipoVersamentoDominioFieldConverter()));

	}

	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, JDBCExpression expression) throws NotImplementedException, ServiceException,Exception {

		java.util.List<Long> lst = this.findAllTableIds(jdbcProperties, log, connection, sqlQueryObject, new JDBCPaginatedExpression(expression));
		
		for(Long id : lst) {
			this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		}
		
		return new NonNegativeNumber(lst.size());
	
	}



	// -- DB
	
	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId) throws ServiceException, NotImplementedException, Exception {
		this._delete(jdbcProperties, log, connection, sqlQueryObject, Long.valueOf(tableId));
	}
}
