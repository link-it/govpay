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
package it.govpay.orm.dao.jdbc;

import java.sql.Connection;

import org.openspcoop2.utils.sql.ISQLQueryObject;

import org.apache.log4j.Logger;

import org.openspcoop2.generic_project.dao.jdbc.IJDBCServiceCRUDWithId;
import it.govpay.orm.IdEvento;
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

import it.govpay.orm.Evento;
import it.govpay.orm.dao.jdbc.JDBCServiceManager;

/**     
 * JDBCEventoServiceImpl
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public class JDBCEventoServiceImpl extends JDBCEventoServiceSearchImpl
	implements IJDBCServiceCRUDWithId<Evento, IdEvento, JDBCServiceManager> {

	@Override
	public void create(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Evento evento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {

		org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities jdbcUtilities = 
				new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCPreparedStatementUtilities(sqlQueryObject.getTipoDatabaseOpenSPCoop2(), log, connection);
		
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		ISQLQueryObject sqlQueryObjectInsert = sqlQueryObject.newSQLQueryObject();
				


		// Object evento
		sqlQueryObjectInsert.addInsertTable(this.getEventoFieldConverter().toTable(Evento.model()));
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().DATA_ORA_EVENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().COD_DOMINIO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().IUV,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().ID_APPLICAZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().CCP,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().COD_PSP,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().TIPO_VERSAMENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().COMPONENTE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().CATEGORIA_EVENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().TIPO_EVENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().SOTTOTIPO_EVENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().COD_FRUITORE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().COD_EROGATORE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().COD_STAZIONE,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().CANALE_PAGAMENTO,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().ALTRI_PARAMETRI,false),"?");
		sqlQueryObjectInsert.addInsertField(this.getEventoFieldConverter().toColumn(Evento.model().ESITO,false),"?");

		// Insert evento
		org.openspcoop2.utils.jdbc.IKeyGeneratorObject keyGenerator = this.getEventoFetch().getKeyGeneratorObject(Evento.model());
		long id = jdbcUtilities.insertAndReturnGeneratedKey(sqlQueryObjectInsert, keyGenerator, jdbcProperties.isShowSql(),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getDataOraEvento(),Evento.model().DATA_ORA_EVENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getCodDominio(),Evento.model().COD_DOMINIO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getIuv(),Evento.model().IUV.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getIdApplicazione(),Evento.model().ID_APPLICAZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getCcp(),Evento.model().CCP.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getCodPsp(),Evento.model().COD_PSP.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getTipoVersamento(),Evento.model().TIPO_VERSAMENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getComponente(),Evento.model().COMPONENTE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getCategoriaEvento(),Evento.model().CATEGORIA_EVENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getTipoEvento(),Evento.model().TIPO_EVENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getSottotipoEvento(),Evento.model().SOTTOTIPO_EVENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getCodFruitore(),Evento.model().COD_FRUITORE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getCodErogatore(),Evento.model().COD_EROGATORE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getCodStazione(),Evento.model().COD_STAZIONE.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getCanalePagamento(),Evento.model().CANALE_PAGAMENTO.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getAltriParametri(),Evento.model().ALTRI_PARAMETRI.getFieldType()),
			new org.openspcoop2.generic_project.dao.jdbc.utils.JDBCObject(evento.getEsito(),Evento.model().ESITO.getFieldType())
		);
		evento.setId(id);

		
	}

	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdEvento oldId, Evento evento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		ISQLQueryObject sqlQueryObjectUpdate = sqlQueryObject.newSQLQueryObject();
		Long longIdByLogicId = this.findIdEvento(jdbcProperties, log, connection, sqlQueryObjectUpdate.newSQLQueryObject(), oldId, true);
		Long tableId = evento.getId();
		if(tableId != null && tableId.longValue() > 0) {
			if(tableId.longValue() != longIdByLogicId.longValue()) {
				throw new Exception("Ambiguous parameter: evento.id ["+tableId+"] does not match logic id ["+longIdByLogicId+"]");
			}
		} else {
			tableId = longIdByLogicId;
			evento.setId(tableId);
		}
		if(tableId==null || tableId<=0){
			throw new Exception("Retrieve tableId failed");
		}
		
		this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, evento, idMappingResolutionBehaviour);
	}
	@Override
	public void update(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Evento evento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotFoundException, NotImplementedException, ServiceException, Exception {

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
		

		// Object evento
		sqlQueryObjectUpdate.setANDLogicOperator(true);
		sqlQueryObjectUpdate.addUpdateTable(this.getEventoFieldConverter().toTable(Evento.model()));
		boolean isUpdate_evento = true;
		java.util.List<JDBCObject> lstObjects_evento = new java.util.ArrayList<JDBCObject>();
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().DATA_ORA_EVENTO,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getDataOraEvento(), Evento.model().DATA_ORA_EVENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().COD_DOMINIO,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getCodDominio(), Evento.model().COD_DOMINIO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().IUV,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getIuv(), Evento.model().IUV.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().ID_APPLICAZIONE,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getIdApplicazione(), Evento.model().ID_APPLICAZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().CCP,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getCcp(), Evento.model().CCP.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().COD_PSP,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getCodPsp(), Evento.model().COD_PSP.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().TIPO_VERSAMENTO,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getTipoVersamento(), Evento.model().TIPO_VERSAMENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().COMPONENTE,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getComponente(), Evento.model().COMPONENTE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().CATEGORIA_EVENTO,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getCategoriaEvento(), Evento.model().CATEGORIA_EVENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().TIPO_EVENTO,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getTipoEvento(), Evento.model().TIPO_EVENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().SOTTOTIPO_EVENTO,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getSottotipoEvento(), Evento.model().SOTTOTIPO_EVENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().COD_FRUITORE,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getCodFruitore(), Evento.model().COD_FRUITORE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().COD_EROGATORE,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getCodErogatore(), Evento.model().COD_EROGATORE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().COD_STAZIONE,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getCodStazione(), Evento.model().COD_STAZIONE.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().CANALE_PAGAMENTO,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getCanalePagamento(), Evento.model().CANALE_PAGAMENTO.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().ALTRI_PARAMETRI,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getAltriParametri(), Evento.model().ALTRI_PARAMETRI.getFieldType()));
		sqlQueryObjectUpdate.addUpdateField(this.getEventoFieldConverter().toColumn(Evento.model().ESITO,false), "?");
		lstObjects_evento.add(new JDBCObject(evento.getEsito(), Evento.model().ESITO.getFieldType()));
		sqlQueryObjectUpdate.addWhereCondition("id=?");
		lstObjects_evento.add(new JDBCObject(tableId, Long.class));

		if(isUpdate_evento) {
			// Update evento
			jdbcUtilities.executeUpdate(sqlQueryObjectUpdate.createSQLUpdate(), jdbcProperties.isShowSql(), 
				lstObjects_evento.toArray(new JDBCObject[]{}));
		}


	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdEvento id, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getEventoFieldConverter().toTable(Evento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getEventoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdEvento id, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getEventoFieldConverter().toTable(Evento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getEventoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdEvento id, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getEventoFieldConverter().toTable(Evento.model()), 
				this._getMapTableToPKColumn(), 
				this._getRootTablePrimaryKeyValues(jdbcProperties, log, connection, sqlQueryObject, id),
				this.getEventoFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getEventoFieldConverter().toTable(Evento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getEventoFieldConverter(), this, null, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, IExpression condition, UpdateField ... updateFields) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getEventoFieldConverter().toTable(Evento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getEventoFieldConverter(), this, condition, updateFields);
	}
	
	@Override
	public void updateFields(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, UpdateModel ... updateModels) throws NotFoundException, NotImplementedException, ServiceException, Exception {
		java.util.List<Object> ids = new java.util.ArrayList<Object>();
		ids.add(tableId);
		JDBCUtilities.updateFields(jdbcProperties, log, connection, sqlQueryObject, 
				this.getEventoFieldConverter().toTable(Evento.model()), 
				this._getMapTableToPKColumn(), 
				ids,
				this.getEventoFieldConverter(), this, updateModels);
	}	
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdEvento oldId, Evento evento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
	
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, oldId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, oldId, evento,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, evento,idMappingResolutionBehaviour);
		}
		
	}
	
	@Override
	public void updateOrCreate(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, long tableId, Evento evento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws NotImplementedException,ServiceException,Exception {
		// default behaviour (id-mapping)
		if(idMappingResolutionBehaviour==null){
			idMappingResolutionBehaviour = org.openspcoop2.generic_project.beans.IDMappingBehaviour.valueOf("USE_TABLE_ID");
		}
		
		if(this.exists(jdbcProperties, log, connection, sqlQueryObject, tableId)) {
			this.update(jdbcProperties, log, connection, sqlQueryObject, tableId, evento,idMappingResolutionBehaviour);
		} else {
			this.create(jdbcProperties, log, connection, sqlQueryObject, evento,idMappingResolutionBehaviour);
		}
	}
	
	@Override
	public void delete(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, Evento evento) throws NotImplementedException,ServiceException,Exception {
		
		
		Long longId = null;
		if( (evento.getId()!=null) && (evento.getId()>0) ){
			longId = evento.getId();
		}
		else{
			IdEvento idEvento = this.convertToId(jdbcProperties,log,connection,sqlQueryObject,evento);
			longId = this.findIdEvento(jdbcProperties,log,connection,sqlQueryObject,idEvento,false);
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
		

		// Object evento
		sqlQueryObjectDelete.setANDLogicOperator(true);
		sqlQueryObjectDelete.addDeleteTable(this.getEventoFieldConverter().toTable(Evento.model()));
		if(id != null)
			sqlQueryObjectDelete.addWhereCondition("id=?");

		// Delete evento
		jdbcUtilities.execute(sqlQueryObjectDelete.createSQLDelete(), jdbcProperties.isShowSql(), 
			new JDBCObject(id,Long.class));

	}

	@Override
	public void deleteById(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject, IdEvento idEvento) throws NotImplementedException,ServiceException,Exception {

		Long id = null;
		try{
			id = this.findIdEvento(jdbcProperties, log, connection, sqlQueryObject, idEvento, true);
		}catch(NotFoundException notFound){
			return;
		}
		this._delete(jdbcProperties, log, connection, sqlQueryObject, id);
		
	}
	
	@Override
	public NonNegativeNumber deleteAll(JDBCServiceManagerProperties jdbcProperties, Logger log, Connection connection, ISQLQueryObject sqlQueryObject) throws NotImplementedException,ServiceException,Exception {
		
		return this.deleteAll(jdbcProperties, log, connection, sqlQueryObject, new JDBCExpression(this.getEventoFieldConverter()));

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
