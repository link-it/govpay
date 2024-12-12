/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2024 Link.it srl (http://www.link.it).
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
import java.text.MessageFormat;

import org.openspcoop2.generic_project.beans.NonNegativeNumber;
import org.openspcoop2.generic_project.beans.UpdateField;
import org.openspcoop2.generic_project.beans.UpdateModel;
import org.openspcoop2.generic_project.dao.jdbc.IJDBCServiceCRUDWithoutId;
import org.openspcoop2.generic_project.dao.jdbc.JDBCExpression;
import org.openspcoop2.generic_project.dao.jdbc.JDBCProperties;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.exception.ValidationException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.utils.sql.ISQLQueryObject;

import it.govpay.core.exceptions.ParametroErratoException;
import it.govpay.core.exceptions.ParametroObbligatorioException;
import it.govpay.orm.Evento;
import it.govpay.orm.constants.Costanti;
import it.govpay.orm.dao.IDBVistaEventiPagamentoService;
import it.govpay.orm.utils.ProjectInfo;

/**     
 * Service can be used to search for and manage the backend objects of type {@link it.govpay.orm.Evento} 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */

public class JDBCVistaEventiPagamentoService extends JDBCVistaEventiPagamentoServiceSearch  implements IDBVistaEventiPagamentoService {


	private IJDBCServiceCRUDWithoutId<Evento, JDBCServiceManager> serviceCRUD = null;
	private String modelName = Costanti.MODEL_VISTA_EVENTI_PAGAMENTO;
	private String modelClassName = Evento.class.getName();
	
	public JDBCVistaEventiPagamentoService(JDBCServiceManager jdbcServiceManager) throws ServiceException {
		super(jdbcServiceManager);
		this.serviceCRUD = JDBCProperties.getInstance(ProjectInfo.getInstance()).getServiceCRUD(this.modelName);
		this.serviceCRUD.setServiceManager(new JDBCLimitedServiceManager(this.jdbcServiceManager));
	}

	
	@Override
	public void create(Evento vistaEventiPagamento) throws ServiceException, NotImplementedException {
		try{
			this.create(vistaEventiPagamento, false, null);
		}catch(ValidationException vE){
			// not possible
			throw new ServiceException(vE.getMessage(), vE);
		}
	}
	
	@Override
	public void create(Evento vistaEventiPagamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws ServiceException, NotImplementedException {
		try{
			this.create(vistaEventiPagamento, false, idMappingResolutionBehaviour);
		}catch(ValidationException vE){
			// not possible
			throw new ServiceException(vE.getMessage(), vE);
		}
	}
	
	@Override
	public void create(Evento vistaEventiPagamento, boolean validate) throws ServiceException, NotImplementedException, ValidationException {
		this.create(vistaEventiPagamento, validate, null);
	}
	
	@Override
	public void create(Evento vistaEventiPagamento, boolean validate, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws ServiceException, NotImplementedException, ValidationException {
		
		Connection connection = null;
		boolean oldValueAutoCommit = false;
		boolean rollback = false;
		try{
			
			// check parameters
			if(vistaEventiPagamento==null){
				throw new ParametroObbligatorioException(this.modelClassName, this.modelName);
			}
			
			// validate
			if(validate){
				this.validate(vistaEventiPagamento);
			}

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();
	
			// transaction
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				oldValueAutoCommit = connection.getAutoCommit();
				connection.setAutoCommit(false);
			}
		
			this.serviceCRUD.create(this.jdbcProperties,this.log,connection,sqlQueryObject,vistaEventiPagamento,idMappingResolutionBehaviour);			

		}catch(ServiceException | NotImplementedException | ValidationException e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw new ServiceException("Create not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){
						 //donothing
					}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){
						 //donothing
					}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){
					 //donothing
				}
			}
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}

	}

	@Override
	public void update(Evento vistaEventiPagamento) throws ServiceException, NotFoundException, NotImplementedException {
		try{
			this.update(vistaEventiPagamento, false, null);
		}catch(ValidationException vE){
			// not possible
			throw new ServiceException(vE.getMessage(), vE);
		}
	}
	
	@Override
	public void update(Evento vistaEventiPagamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws ServiceException, NotFoundException, NotImplementedException {
		try{
			this.update(vistaEventiPagamento, false, idMappingResolutionBehaviour);
		}catch(ValidationException vE){
			// not possible
			throw new ServiceException(vE.getMessage(), vE);
		}
	}
	
	@Override
	public void update(Evento vistaEventiPagamento, boolean validate) throws ServiceException, NotFoundException, NotImplementedException, ValidationException {
		this.update(vistaEventiPagamento, validate, null);
	}
		
	@Override
	public void update(Evento vistaEventiPagamento, boolean validate, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws ServiceException, NotFoundException, NotImplementedException, ValidationException {
	
		Connection connection = null;
		boolean oldValueAutoCommit = false;
		boolean rollback = false;
		try{
			
			// check parameters
			if(vistaEventiPagamento==null){
				throw new ParametroObbligatorioException(this.modelClassName, this.modelName);
			}

			// validate
			if(validate){
				this.validate(vistaEventiPagamento);
			}

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();
		
			// transaction
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				oldValueAutoCommit = connection.getAutoCommit();
				connection.setAutoCommit(false);
			}

			this.serviceCRUD.update(this.jdbcProperties,this.log,connection,sqlQueryObject,vistaEventiPagamento,idMappingResolutionBehaviour);
			
		}catch(ServiceException | NotFoundException | NotImplementedException | ValidationException e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw new ServiceException("Update not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){
						 //donothing
					}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){
						 //donothing
					}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){
					 //donothing
				}
			}
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}

	}
	
	@Override
	public void update(long tableId, Evento vistaEventiPagamento) throws ServiceException, NotFoundException, NotImplementedException {
		try{
			this.update(tableId, vistaEventiPagamento, false, null);
		}catch(ValidationException vE){
			// not possible
			throw new ServiceException(vE.getMessage(), vE);
		}
	}
	
	@Override
	public void update(long tableId, Evento vistaEventiPagamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws ServiceException, NotFoundException, NotImplementedException {
		try{
			this.update(tableId, vistaEventiPagamento, false, idMappingResolutionBehaviour);
		}catch(ValidationException vE){
			// not possible
			throw new ServiceException(vE.getMessage(), vE);
		}
	}
	
	@Override
	public void update(long tableId, Evento vistaEventiPagamento, boolean validate) throws ServiceException, NotFoundException, NotImplementedException, ValidationException {
		this.update(tableId, vistaEventiPagamento, validate, null);
	}
		
	@Override
	public void update(long tableId, Evento vistaEventiPagamento, boolean validate, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws ServiceException, NotFoundException, NotImplementedException, ValidationException {
	
		Connection connection = null;
		boolean oldValueAutoCommit = false;
		boolean rollback = false;
		try{
			
			// check parameters
			if(vistaEventiPagamento==null){
				throw new ParametroObbligatorioException(this.modelClassName, this.modelName); 
			}
			if(tableId<=0){
				throw new ParametroObbligatorioException(long.class.getName(), Costanti.PARAMETER_TABLE_ID, Costanti.ERROR_MSG_IS_LESS_EQUALS_0); 
			}

			// validate
			if(validate){
				this.validate(vistaEventiPagamento);
			}

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();
		
			// transaction
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				oldValueAutoCommit = connection.getAutoCommit();
				connection.setAutoCommit(false);
			}

			this.serviceCRUD.update(this.jdbcProperties,this.log,connection,sqlQueryObject,tableId,vistaEventiPagamento,idMappingResolutionBehaviour);
			
		}catch(ServiceException | NotFoundException | NotImplementedException | ValidationException e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw new ServiceException("Update not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){
						 //donothing
					}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){
						 //donothing
					}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){
					 //donothing
				}
			}
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}

	}
	
	@Override
	public void updateFields(Evento vistaEventiPagamento, UpdateField ... updateFields) throws ServiceException, NotFoundException, NotImplementedException {
	
		Connection connection = null;
		boolean oldValueAutoCommit = false;
		boolean rollback = false;
		try{
			
			// check parameters
			if(vistaEventiPagamento==null){
				throw new ParametroObbligatorioException(this.modelClassName, this.modelName);
			}
			if(updateFields==null){
				throw new ParametroObbligatorioException(UpdateField.class.getName(), Costanti.PARAMETER_UPDATE_FIELDS);
			}

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();
		
			// transaction
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				oldValueAutoCommit = connection.getAutoCommit();
				connection.setAutoCommit(false);
			}

			this.serviceCRUD.updateFields(this.jdbcProperties,this.log,connection,sqlQueryObject,vistaEventiPagamento,updateFields);
			
		}catch(ServiceException | NotFoundException | NotImplementedException e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw new ServiceException("UpdateFields not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){
						 //donothing
					}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){
						 //donothing
					}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){
					 //donothing
				}
			}
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}

	}
	
	@Override
	public void updateFields(Evento vistaEventiPagamento, IExpression condition, UpdateField ... updateFields) throws ServiceException, NotFoundException, NotImplementedException {
	
		Connection connection = null;
		boolean oldValueAutoCommit = false;
		boolean rollback = false;
		try{
			
			// check parameters
			if(vistaEventiPagamento==null){
				throw new ParametroObbligatorioException(this.modelClassName, this.modelName);
			}
			if(condition==null){
				throw new ParametroObbligatorioException(IExpression.class.getName(), Costanti.PARAMETER_CONDITION);
			}
			if(updateFields==null){
				throw new ParametroObbligatorioException(UpdateField.class.getName(), Costanti.PARAMETER_UPDATE_FIELDS);
			}

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();
		
			// transaction
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				oldValueAutoCommit = connection.getAutoCommit();
				connection.setAutoCommit(false);
			}

			this.serviceCRUD.updateFields(this.jdbcProperties,this.log,connection,sqlQueryObject,vistaEventiPagamento,condition,updateFields);
			
		}catch(ServiceException | NotFoundException | NotImplementedException e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw new ServiceException("UpdateFields not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){
						 //donothing
					}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){
						 //donothing
					}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){
					 //donothing
				}
			}
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}

	}

	@Override
	public void updateFields(Evento vistaEventiPagamento, UpdateModel ... updateModels) throws ServiceException, NotFoundException, NotImplementedException {
	
		Connection connection = null;
		boolean oldValueAutoCommit = false;
		boolean rollback = false;
		try{
			
			// check parameters
			if(vistaEventiPagamento==null){
				throw new ParametroObbligatorioException(this.modelClassName, this.modelName);
			}
			if(updateModels==null){
				throw new ParametroObbligatorioException(UpdateModel.class.getName(), Costanti.PARAMETER_UPDATE_MODELS);
			}

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();
		
			// transaction
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				oldValueAutoCommit = connection.getAutoCommit();
				connection.setAutoCommit(false);
			}

			this.serviceCRUD.updateFields(this.jdbcProperties,this.log,connection,sqlQueryObject,vistaEventiPagamento,updateModels);
			
		}catch(ServiceException | NotFoundException | NotImplementedException e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw new ServiceException("UpdateFields not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){
						 //donothing
					}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){
						 //donothing
					}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){
					 //donothing
				}
			}
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}

	}

	@Override
	public void updateFields(long tableId, UpdateField ... updateFields) throws ServiceException, NotFoundException, NotImplementedException {
	
		Connection connection = null;
		boolean oldValueAutoCommit = false;
		boolean rollback = false;
		try{
			
			// check parameters
			if(tableId<=0){
				throw new ParametroObbligatorioException(long.class.getName(), Costanti.PARAMETER_TABLE_ID, Costanti.ERROR_MSG_IS_LESS_EQUALS_0);
			}
			if(updateFields==null){
				throw new ParametroObbligatorioException(UpdateField.class.getName(), Costanti.PARAMETER_UPDATE_FIELDS);
			}

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();
		
			// transaction
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				oldValueAutoCommit = connection.getAutoCommit();
				connection.setAutoCommit(false);
			}

			this.serviceCRUD.updateFields(this.jdbcProperties,this.log,connection,sqlQueryObject,tableId,updateFields);	
			
		}catch(ServiceException | NotFoundException | NotImplementedException e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw new ServiceException("UpdateFields not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){
						 //donothing
					}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){
						 //donothing
					}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){
					 //donothing
				}
			}
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}

	}
	
	@Override
	public void updateFields(long tableId, IExpression condition, UpdateField ... updateFields) throws ServiceException, NotFoundException, NotImplementedException {
	
		Connection connection = null;
		boolean oldValueAutoCommit = false;
		boolean rollback = false;
		try{
			
			// check parameters
			if(tableId<=0){
				throw new ParametroObbligatorioException(long.class.getName(), Costanti.PARAMETER_TABLE_ID, Costanti.ERROR_MSG_IS_LESS_EQUALS_0);
			}
			if(condition==null){
				throw new ParametroObbligatorioException(IExpression.class.getName(), Costanti.PARAMETER_CONDITION);
			}
			if(updateFields==null){
				throw new ParametroObbligatorioException(UpdateField.class.getName(), Costanti.PARAMETER_UPDATE_FIELDS);
			}

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();
		
			// transaction
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				oldValueAutoCommit = connection.getAutoCommit();
				connection.setAutoCommit(false);
			}

			this.serviceCRUD.updateFields(this.jdbcProperties,this.log,connection,sqlQueryObject,tableId,condition,updateFields);
			
		}catch(ServiceException | NotFoundException | NotImplementedException e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw new ServiceException("UpdateFields not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){
						 //donothing
					}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){
						 //donothing
					}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){
					 //donothing
				}
			}
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}

	}

	@Override
	public void updateFields(long tableId, UpdateModel ... updateModels) throws ServiceException, NotFoundException, NotImplementedException {
	
		Connection connection = null;
		boolean oldValueAutoCommit = false;
		boolean rollback = false;
		try{
			
			// check parameters
			if(tableId<=0){
				throw new ParametroObbligatorioException(long.class.getName(), Costanti.PARAMETER_TABLE_ID, Costanti.ERROR_MSG_IS_LESS_EQUALS_0);
			}
			if(updateModels==null){
				throw new ParametroObbligatorioException(UpdateModel.class.getName(), Costanti.PARAMETER_UPDATE_MODELS);
			}

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();
		
			// transaction
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				oldValueAutoCommit = connection.getAutoCommit();
				connection.setAutoCommit(false);
			}

			this.serviceCRUD.updateFields(this.jdbcProperties,this.log,connection,sqlQueryObject,tableId,updateModels);
			
		}catch(ServiceException | NotFoundException | NotImplementedException e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw new ServiceException("UpdateFields not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){
						 //donothing
					}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){
						 //donothing
					}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){
					 //donothing
				}
			}
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}

	}

	@Override
	public void updateOrCreate(Evento vistaEventiPagamento) throws ServiceException, NotImplementedException {
		try{
			this.updateOrCreate(vistaEventiPagamento, false, null);
		}catch(ValidationException vE){
			// not possible
			throw new ServiceException(vE.getMessage(), vE);
		}
	}
	
	@Override
	public void updateOrCreate(Evento vistaEventiPagamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws ServiceException, NotImplementedException {
		try{
			this.updateOrCreate(vistaEventiPagamento, false, idMappingResolutionBehaviour);
		}catch(ValidationException vE){
			// not possible
			throw new ServiceException(vE.getMessage(), vE);
		}
	}

	@Override
	public void updateOrCreate(Evento vistaEventiPagamento, boolean validate) throws ServiceException, NotImplementedException, ValidationException {
		this.updateOrCreate(vistaEventiPagamento, validate, null);
	}

	@Override
	public void updateOrCreate(Evento vistaEventiPagamento, boolean validate, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws ServiceException, NotImplementedException, ValidationException {
	
		Connection connection = null;
		boolean oldValueAutoCommit = false;
		boolean rollback = false;
		try{
			
			// check parameters
			if(vistaEventiPagamento==null){
				throw new ParametroObbligatorioException(this.modelClassName, this.modelName);
			}

			// validate
			if(validate){
				this.validate(vistaEventiPagamento);
			}

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();
		
			// transaction
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				oldValueAutoCommit = connection.getAutoCommit();
				connection.setAutoCommit(false);
			}

			this.serviceCRUD.updateOrCreate(this.jdbcProperties,this.log,connection,sqlQueryObject,vistaEventiPagamento,idMappingResolutionBehaviour);
			
		}catch(ServiceException | NotImplementedException | ValidationException e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw new ServiceException("UpdateOrCreate not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){
						 //donothing
					}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){
						 //donothing
					}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){
					 //donothing
				}
			}
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}

	}
	
	@Override
	public void updateOrCreate(long tableId, Evento vistaEventiPagamento) throws ServiceException, NotImplementedException {
		try{
			this.updateOrCreate(tableId, vistaEventiPagamento, false, null);
		}catch(ValidationException vE){
			// not possible
			throw new ServiceException(vE.getMessage(), vE);
		}
	}
	
	@Override
	public void updateOrCreate(long tableId, Evento vistaEventiPagamento, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws ServiceException, NotImplementedException {
		try{
			this.updateOrCreate(tableId, vistaEventiPagamento, false, idMappingResolutionBehaviour);
		}catch(ValidationException vE){
			// not possible
			throw new ServiceException(vE.getMessage(), vE);
		}
	}

	@Override
	public void updateOrCreate(long tableId, Evento vistaEventiPagamento, boolean validate) throws ServiceException, NotImplementedException, ValidationException {
		this.updateOrCreate(tableId, vistaEventiPagamento, validate, null);
	}

	@Override
	public void updateOrCreate(long tableId, Evento vistaEventiPagamento, boolean validate, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws ServiceException, NotImplementedException, ValidationException {

		Connection connection = null;
		boolean oldValueAutoCommit = false;
		boolean rollback = false;
		try{
			
			// check parameters
			if(vistaEventiPagamento==null){
				throw new ParametroObbligatorioException(this.modelClassName, this.modelName);
			}
			if(tableId<=0){
				throw new ParametroObbligatorioException(long.class.getName(), Costanti.PARAMETER_TABLE_ID, Costanti.ERROR_MSG_IS_LESS_EQUALS_0);
			}

			// validate
			if(validate){
				this.validate(vistaEventiPagamento);
			}

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();
		
			// transaction
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				oldValueAutoCommit = connection.getAutoCommit();
				connection.setAutoCommit(false);
			}

			this.serviceCRUD.updateOrCreate(this.jdbcProperties,this.log,connection,sqlQueryObject,tableId,vistaEventiPagamento,idMappingResolutionBehaviour);

		}catch(ServiceException | NotImplementedException | ValidationException e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw new ServiceException("UpdateOrCreate not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){
						 //donothing
					}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){
						 //donothing
					}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){
					 //donothing
				}
			}
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}

	}
	
	@Override
	public void delete(Evento vistaEventiPagamento) throws ServiceException,NotImplementedException {
		
		Connection connection = null;
		boolean oldValueAutoCommit = false;
		boolean rollback = false;
		try{
			
			// check parameters
			if(vistaEventiPagamento==null){
				throw new ParametroObbligatorioException(this.modelClassName, this.modelName);
			}

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();

			// transaction
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				oldValueAutoCommit = connection.getAutoCommit();
				connection.setAutoCommit(false);
			}

			this.serviceCRUD.delete(this.jdbcProperties,this.log,connection,sqlQueryObject,vistaEventiPagamento);	

		}catch(ServiceException | NotImplementedException e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw new ServiceException("Delete not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){
						 //donothing
					}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){
						 //donothing
					}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){
					 //donothing
				}
			}
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
	
	}
	


	@Override
	public NonNegativeNumber deleteAll() throws ServiceException, NotImplementedException {

		Connection connection = null;
		boolean oldValueAutoCommit = false;
		boolean rollback = false;
		try{

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();
		
			// transaction
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				oldValueAutoCommit = connection.getAutoCommit();
				connection.setAutoCommit(false);
			}

			return this.serviceCRUD.deleteAll(this.jdbcProperties,this.log,connection,sqlQueryObject);	

		}catch(ServiceException | NotImplementedException e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw new ServiceException("DeleteAll not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){
						 //donothing
					}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){
						 //donothing
					}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){
					 //donothing
				}
			}
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
	
	}

	@Override
	public NonNegativeNumber deleteAll(IExpression expression) throws ServiceException, NotImplementedException {
		
		Connection connection = null;
		boolean oldValueAutoCommit = false;
		boolean rollback = false;
		try{
			
			// check parameters
			if(expression==null){
				throw new ParametroObbligatorioException(IExpression.class.getName(), Costanti.PARAMETER_EXPRESSION);
			}
			if( ! (expression instanceof JDBCExpression) ){
				throw new ParametroErratoException(expression.getClass().getName(), Costanti.PARAMETER_EXPRESSION, MessageFormat.format(Costanti.HAS_WRONG_TYPE_EXPECT_0,JDBCExpression.class.getName())); 
			}
			JDBCExpression jdbcExpression = (JDBCExpression) expression;
			this.log.debug("sql = {}", jdbcExpression.toSql());
		
			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();

			// transaction
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				oldValueAutoCommit = connection.getAutoCommit();
				connection.setAutoCommit(false);
			}

			return this.serviceCRUD.deleteAll(this.jdbcProperties,this.log,connection,sqlQueryObject,jdbcExpression);
	
		}catch(ServiceException | NotImplementedException e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw new ServiceException("DeleteAll(expression) not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){
						 //donothing
					}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){
						 //donothing
					}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){
					 //donothing
				}
			}
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
	
	}
	
	// -- DB
	
	@Override
	public void deleteById(long tableId) throws ServiceException, NotImplementedException {
		
		Connection connection = null;
		boolean oldValueAutoCommit = false;
		boolean rollback = false;
		try{
			
			// check parameters
			if(tableId<=0){
				throw new ParametroObbligatorioException(long.class.getName(), Costanti.PARAMETER_TABLE_ID, Costanti.ERROR_MSG_IS_LESS_EQUALS_0);
			}
		
			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();

			// transaction
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				oldValueAutoCommit = connection.getAutoCommit();
				connection.setAutoCommit(false);
			}

			this.serviceCRUD.deleteById(this.jdbcProperties,this.log,connection,sqlQueryObject,tableId);
	
		}catch(ServiceException | NotImplementedException e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e.getMessage(),e); throw new ServiceException("DeleteById(tableId) not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){
						 //donothing
					}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){
						 //donothing
					}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){
					 //donothing
				}
			}
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
	
	}
	
}
