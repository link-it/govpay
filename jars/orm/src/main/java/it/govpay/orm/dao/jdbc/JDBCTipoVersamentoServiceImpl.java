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

import it.govpay.orm.TipoVersamento;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCTipoVersamentoServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCTipoVersamentoServiceImpl extends JDBCTipoVersamentoServiceSearchImpl
	implements IJDBCServiceCRUDWithId<TipoVersamento, IdTipoVersamento, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, TipoVersamento tipoVersamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				


		// Object tipoVersamento
		sqlQueryObjectInsert.addInsertTable(this.getTipoVersamentoFieldConverter().toTable(TipoVersamento.model()));
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().COD_TIPO_VERSAMENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().DESCRIZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().CODIFICA_IUV,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().TIPO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().PAGA_TERZI,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().ABILITATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().FORM_TIPO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().FORM_DEFINIZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().VALIDAZIONE_DEFINIZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().TRASFORMAZIONE_TIPO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().TRASFORMAZIONE_DEFINIZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().COD_APPLICAZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().PROMEMORIA_AVVISO_ABILITATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().PROMEMORIA_AVVISO_PDF,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().PROMEMORIA_AVVISO_TIPO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().PROMEMORIA_AVVISO_OGGETTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().PROMEMORIA_AVVISO_MESSAGGIO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().PROMEMORIA_RICEVUTA_ABILITATO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().PROMEMORIA_RICEVUTA_TIPO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().PROMEMORIA_RICEVUTA_PDF,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().PROMEMORIA_RICEVUTA_OGGETTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().PROMEMORIA_RICEVUTA_MESSAGGIO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().VISUALIZZAZIONE_DEFINIZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().TRAC_CSV_TIPO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().TRAC_CSV_HEADER_RISPOSTA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().TRAC_CSV_TEMPLATE_RICHIESTA,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().TRAC_CSV_TEMPLATE_RISPOSTA,false),"?");

		// Insert tipoVersamento
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getTipoVersamentoFetch().getKeyGeneratorObject(TipoVersamento.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamento.getCodTipoVersamento(),TipoVersamento.model().COD_TIPO_VERSAMENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamento.getDescrizione(),TipoVersamento.model().DESCRIZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamento.getCodificaIuv(),TipoVersamento.model().CODIFICA_IUV.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamento.getTipo(),TipoVersamento.model().TIPO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamento.getPagaTerzi(),TipoVersamento.model().PAGA_TERZI.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamento.getAbilitato(),TipoVersamento.model().ABILITATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamento.getFormTipo(),TipoVersamento.model().FORM_TIPO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamento.getFormDefinizione(),TipoVersamento.model().FORM_DEFINIZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamento.getValidazioneDefinizione(),TipoVersamento.model().VALIDAZIONE_DEFINIZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamento.getTrasformazioneTipo(),TipoVersamento.model().TRASFORMAZIONE_TIPO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamento.getTrasformazioneDefinizione(),TipoVersamento.model().TRASFORMAZIONE_DEFINIZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamento.getCodApplicazione(),TipoVersamento.model().COD_APPLICAZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamento.getPromemoriaAvvisoAbilitato(),TipoVersamento.model().PROMEMORIA_AVVISO_ABILITATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamento.getPromemoriaAvvisoPdf(),TipoVersamento.model().PROMEMORIA_AVVISO_PDF.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamento.getPromemoriaAvvisoTipo(),TipoVersamento.model().PROMEMORIA_AVVISO_TIPO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamento.getPromemoriaAvvisoOggetto(),TipoVersamento.model().PROMEMORIA_AVVISO_OGGETTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamento.getPromemoriaAvvisoMessaggio(),TipoVersamento.model().PROMEMORIA_AVVISO_MESSAGGIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamento.getPromemoriaRicevutaAbilitato(),TipoVersamento.model().PROMEMORIA_RICEVUTA_ABILITATO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamento.getPromemoriaRicevutaTipo(),TipoVersamento.model().PROMEMORIA_RICEVUTA_TIPO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamento.getPromemoriaRicevutaPdf(),TipoVersamento.model().PROMEMORIA_RICEVUTA_PDF.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamento.getPromemoriaRicevutaOggetto(),TipoVersamento.model().PROMEMORIA_RICEVUTA_OGGETTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamento.getPromemoriaRicevutaMessaggio(),TipoVersamento.model().PROMEMORIA_RICEVUTA_MESSAGGIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamento.getVisualizzazioneDefinizione(),TipoVersamento.model().VISUALIZZAZIONE_DEFINIZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamento.getTracCsvTipo(),TipoVersamento.model().TRAC_CSV_TIPO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamento.getTracCsvHeaderRisposta(),TipoVersamento.model().TRAC_CSV_HEADER_RISPOSTA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamento.getTracCsvTemplateRichiesta(),TipoVersamento.model().TRAC_CSV_TEMPLATE_RICHIESTA.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(tipoVersamento.getTracCsvTemplateRisposta(),TipoVersamento.model().TRAC_CSV_TEMPLATE_RISPOSTA.getFieldType())
		);
		tipoVersamento.setId(id);

		
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTipoVersamento oldId, TipoVersamento tipoVersamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdTipoVersamento(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = tipoVersamento.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: tipoVersamento.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			tipoVersamento.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}

		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, tipoVersamento, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, TipoVersamento tipoVersamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
	
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
		
//		boolean setIdMappingResolutionBehaviour = 
//			(idMappingResolutionBehaviour==null) ||
//			org.openspcoop2.generic_project.beans.IDMappingBehaviour.ENABLED.equals(idMappingResolutionBehaviour) ||
//			org.openspcoop2.generic_project.beans.IDMappingBehaviour.USE_TABLE_ID.equals(idMappingResolutionBehaviour);
			


		// Object tipoVersamento
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getTipoVersamentoFieldConverter().toTable(TipoVersamento.model()));
		boolean isUpdate_tipoVersamento = true;
		java.util.List<JDBCObject> lstObjects_tipoVersamento = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().COD_TIPO_VERSAMENTO,false), "?");
		lstObjects_tipoVersamento.add(new JDBCObject(tipoVersamento.getCodTipoVersamento(), TipoVersamento.model().COD_TIPO_VERSAMENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().DESCRIZIONE,false), "?");
		lstObjects_tipoVersamento.add(new JDBCObject(tipoVersamento.getDescrizione(), TipoVersamento.model().DESCRIZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().CODIFICA_IUV,false), "?");
		lstObjects_tipoVersamento.add(new JDBCObject(tipoVersamento.getCodificaIuv(), TipoVersamento.model().CODIFICA_IUV.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().TIPO,false), "?");
		lstObjects_tipoVersamento.add(new JDBCObject(tipoVersamento.getTipo(), TipoVersamento.model().TIPO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().PAGA_TERZI,false), "?");
		lstObjects_tipoVersamento.add(new JDBCObject(tipoVersamento.getPagaTerzi(), TipoVersamento.model().PAGA_TERZI.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().ABILITATO,false), "?");
		lstObjects_tipoVersamento.add(new JDBCObject(tipoVersamento.getAbilitato(), TipoVersamento.model().ABILITATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().FORM_TIPO,false), "?");
		lstObjects_tipoVersamento.add(new JDBCObject(tipoVersamento.getFormTipo(), TipoVersamento.model().FORM_TIPO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().FORM_DEFINIZIONE,false), "?");
		lstObjects_tipoVersamento.add(new JDBCObject(tipoVersamento.getFormDefinizione(), TipoVersamento.model().FORM_DEFINIZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().VALIDAZIONE_DEFINIZIONE,false), "?");
		lstObjects_tipoVersamento.add(new JDBCObject(tipoVersamento.getValidazioneDefinizione(), TipoVersamento.model().VALIDAZIONE_DEFINIZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().TRASFORMAZIONE_TIPO,false), "?");
		lstObjects_tipoVersamento.add(new JDBCObject(tipoVersamento.getTrasformazioneTipo(), TipoVersamento.model().TRASFORMAZIONE_TIPO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().TRASFORMAZIONE_DEFINIZIONE,false), "?");
		lstObjects_tipoVersamento.add(new JDBCObject(tipoVersamento.getTrasformazioneDefinizione(), TipoVersamento.model().TRASFORMAZIONE_DEFINIZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().COD_APPLICAZIONE,false), "?");
		lstObjects_tipoVersamento.add(new JDBCObject(tipoVersamento.getCodApplicazione(), TipoVersamento.model().COD_APPLICAZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().PROMEMORIA_AVVISO_ABILITATO,false), "?");
		lstObjects_tipoVersamento.add(new JDBCObject(tipoVersamento.getPromemoriaAvvisoAbilitato(), TipoVersamento.model().PROMEMORIA_AVVISO_ABILITATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().PROMEMORIA_AVVISO_PDF,false), "?");
		lstObjects_tipoVersamento.add(new JDBCObject(tipoVersamento.getPromemoriaAvvisoPdf(), TipoVersamento.model().PROMEMORIA_AVVISO_PDF.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().PROMEMORIA_AVVISO_TIPO,false), "?");
		lstObjects_tipoVersamento.add(new JDBCObject(tipoVersamento.getPromemoriaAvvisoTipo(), TipoVersamento.model().PROMEMORIA_AVVISO_TIPO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().PROMEMORIA_AVVISO_OGGETTO,false), "?");
		lstObjects_tipoVersamento.add(new JDBCObject(tipoVersamento.getPromemoriaAvvisoOggetto(), TipoVersamento.model().PROMEMORIA_AVVISO_OGGETTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().PROMEMORIA_AVVISO_MESSAGGIO,false), "?");
		lstObjects_tipoVersamento.add(new JDBCObject(tipoVersamento.getPromemoriaAvvisoMessaggio(), TipoVersamento.model().PROMEMORIA_AVVISO_MESSAGGIO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().PROMEMORIA_RICEVUTA_ABILITATO,false), "?");
		lstObjects_tipoVersamento.add(new JDBCObject(tipoVersamento.getPromemoriaRicevutaAbilitato(), TipoVersamento.model().PROMEMORIA_RICEVUTA_ABILITATO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().PROMEMORIA_RICEVUTA_TIPO,false), "?");
		lstObjects_tipoVersamento.add(new JDBCObject(tipoVersamento.getPromemoriaRicevutaTipo(), TipoVersamento.model().PROMEMORIA_RICEVUTA_TIPO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().PROMEMORIA_RICEVUTA_PDF,false), "?");
		lstObjects_tipoVersamento.add(new JDBCObject(tipoVersamento.getPromemoriaRicevutaPdf(), TipoVersamento.model().PROMEMORIA_RICEVUTA_PDF.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().PROMEMORIA_RICEVUTA_OGGETTO,false), "?");
		lstObjects_tipoVersamento.add(new JDBCObject(tipoVersamento.getPromemoriaRicevutaOggetto(), TipoVersamento.model().PROMEMORIA_RICEVUTA_OGGETTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().PROMEMORIA_RICEVUTA_MESSAGGIO,false), "?");
		lstObjects_tipoVersamento.add(new JDBCObject(tipoVersamento.getPromemoriaRicevutaMessaggio(), TipoVersamento.model().PROMEMORIA_RICEVUTA_MESSAGGIO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().VISUALIZZAZIONE_DEFINIZIONE,false), "?");
		lstObjects_tipoVersamento.add(new JDBCObject(tipoVersamento.getVisualizzazioneDefinizione(), TipoVersamento.model().VISUALIZZAZIONE_DEFINIZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().TRAC_CSV_TIPO,false), "?");
		lstObjects_tipoVersamento.add(new JDBCObject(tipoVersamento.getTracCsvTipo(), TipoVersamento.model().TRAC_CSV_TIPO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().TRAC_CSV_HEADER_RISPOSTA,false), "?");
		lstObjects_tipoVersamento.add(new JDBCObject(tipoVersamento.getTracCsvHeaderRisposta(), TipoVersamento.model().TRAC_CSV_HEADER_RISPOSTA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().TRAC_CSV_TEMPLATE_RICHIESTA,false), "?");
		lstObjects_tipoVersamento.add(new JDBCObject(tipoVersamento.getTracCsvTemplateRichiesta(), TipoVersamento.model().TRAC_CSV_TEMPLATE_RICHIESTA.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getTipoVersamentoFieldConverter().toColumn(TipoVersamento.model().TRAC_CSV_TEMPLATE_RISPOSTA,false), "?");
		lstObjects_tipoVersamento.add(new JDBCObject(tipoVersamento.getTracCsvTemplateRisposta(), TipoVersamento.model().TRAC_CSV_TEMPLATE_RISPOSTA.getFieldType()));
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_tipoVersamento.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_tipoVersamento) {
			// Update tipoVersamento
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_tipoVersamento.toArray(new JDBCObject[]{}));
		}


	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTipoVersamento id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTipoVersamentoFieldConverter().toTable(TipoVersamento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getTipoVersamentoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTipoVersamento id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTipoVersamentoFieldConverter().toTable(TipoVersamento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getTipoVersamentoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTipoVersamento id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTipoVersamentoFieldConverter().toTable(TipoVersamento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getTipoVersamentoFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTipoVersamentoFieldConverter().toTable(TipoVersamento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getTipoVersamentoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTipoVersamentoFieldConverter().toTable(TipoVersamento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getTipoVersamentoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getTipoVersamentoFieldConverter().toTable(TipoVersamento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getTipoVersamentoFieldConverter(), this, updateModels);
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTipoVersamento oldId, TipoVersamento tipoVersamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, tipoVersamento,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, tipoVersamento,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, TipoVersamento tipoVersamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, tipoVersamento,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, tipoVersamento,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, TipoVersamento tipoVersamento) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (tipoVersamento.getId()!=null) && (tipoVersamento.getId()>0) ){
			longId = tipoVersamento.getId();
		}
		else{
			IdTipoVersamento idTipoVersamento = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,tipoVersamento);
			longId = this.findIdTipoVersamento(jdbcProperties,log,connection,sqlQueryObject,idTipoVersamento,false);
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
		

		// Object tipoVersamento
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getTipoVersamentoFieldConverter().toTable(TipoVersamento.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete tipoVersamento
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdTipoVersamento idTipoVersamento) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdTipoVersamento(jdbcProperties, log, connection, sqlQueryObject, idTipoVersamento, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getTipoVersamentoFieldConverter()));

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
