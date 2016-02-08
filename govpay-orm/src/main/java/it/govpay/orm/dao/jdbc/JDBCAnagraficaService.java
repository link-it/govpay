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

import org.openspcoop2.generic_project.dao.jdbc.IJDBCServiceCRUDWithId;
import it.govpay.orm.IdAnagrafica;

import org.openspcoop2.generic_project.beans.NonNegativeNumber;
import org.openspcoop2.generic_project.beans.UpdateField;
import org.openspcoop2.generic_project.beans.UpdateModel;
import org.openspcoop2.generic_project.dao.jdbc.JDBCProperties;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.exception.ValidationException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.dao.jdbc.JDBCExpression;

import it.govpay.orm.dao.jdbc.JDBCServiceManager;
import it.govpay.orm.Anagrafica;
import it.govpay.orm.dao.IDBAnagraficaService;
import it.govpay.orm.utils.ProjectInfo;

import java.sql.Connection;

import org.openspcoop2.utils.sql.ISQLQueryObject;

/**     
 * Service can be used to search for and manage the backend objects of type {@link it.govpay.orm.Anagrafica} 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */

public class JDBCAnagraficaService extends JDBCAnagraficaServiceSearch  implements IDBAnagraficaService {


	private IJDBCServiceCRUDWithId<Anagrafica, IdAnagrafica, JDBCServiceManager> serviceCRUD = null;
	public JDBCAnagraficaService(JDBCServiceManager jdbcServiceManager) throws ServiceException {
		super(jdbcServiceManager);
		this.log.debug(JDBCAnagraficaService.class.getName()+ " initialized");
		this.serviceCRUD = JDBCProperties.getInstance(ProjectInfo.getInstance()).getServiceCRUD("anagrafica");
		this.serviceCRUD.setServiceManager(new JDBCLimitedServiceManager(this.jdbcServiceManager));
	}

	
	@Override
	public void create(Anagrafica anagrafica) throws ServiceException, NotImplementedException {
		try{
			this.create(anagrafica, false, null);
		}catch(ValidationException vE){
			// not possible
			throw new ServiceException(vE.getMessage(), vE);
		}
	}
	
	@Override
	public void create(Anagrafica anagrafica, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws ServiceException, NotImplementedException {
		try{
			this.create(anagrafica, false, idMappingResolutionBehaviour);
		}catch(ValidationException vE){
			// not possible
			throw new ServiceException(vE.getMessage(), vE);
		}
	}
	
	@Override
	public void create(Anagrafica anagrafica, boolean validate) throws ServiceException, NotImplementedException, ValidationException {
		this.create(anagrafica, validate, null);
	}
	
	@Override
	public void create(Anagrafica anagrafica, boolean validate, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws ServiceException, NotImplementedException, ValidationException {
		
		Connection connection = null;
		boolean oldValueAutoCommit = false;
		boolean rollback = false;
		try{
			
			// check parameters
			if(anagrafica==null){
				throw new Exception("Parameter (type:"+Anagrafica.class.getName()+") 'anagrafica' is null");
			}
			
			// validate
			if(validate){
				this.validate(anagrafica);
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
		
			this.serviceCRUD.create(this.jdbcProperties,this.log,connection,sqlQueryObject,anagrafica,idMappingResolutionBehaviour);			

		}catch(ServiceException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(NotImplementedException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(ValidationException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e,e); throw new ServiceException("Create not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){}
			}
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}

	}

	@Override
	public void update(IdAnagrafica oldId, Anagrafica anagrafica) throws ServiceException, NotFoundException, NotImplementedException {
		try{
			this.update(oldId, anagrafica, false, null);
		}catch(ValidationException vE){
			// not possible
			throw new ServiceException(vE.getMessage(), vE);
		}
	}
	
	@Override
	public void update(IdAnagrafica oldId, Anagrafica anagrafica, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws ServiceException, NotFoundException, NotImplementedException {
		try{
			this.update(oldId, anagrafica, false, idMappingResolutionBehaviour);
		}catch(ValidationException vE){
			// not possible
			throw new ServiceException(vE.getMessage(), vE);
		}
	}
	
	@Override
	public void update(IdAnagrafica oldId, Anagrafica anagrafica, boolean validate) throws ServiceException, NotFoundException, NotImplementedException, ValidationException {
		this.update(oldId, anagrafica, validate, null);
	}
		
	@Override
	public void update(IdAnagrafica oldId, Anagrafica anagrafica, boolean validate, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws ServiceException, NotFoundException, NotImplementedException, ValidationException {
	
		Connection connection = null;
		boolean oldValueAutoCommit = false;
		boolean rollback = false;
		try{
			
			// check parameters
			if(anagrafica==null){
				throw new Exception("Parameter (type:"+Anagrafica.class.getName()+") 'anagrafica' is null");
			}
			if(oldId==null){
				throw new Exception("Parameter (type:"+IdAnagrafica.class.getName()+") 'oldId' is null");
			}

			// validate
			if(validate){
				this.validate(anagrafica);
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

			this.serviceCRUD.update(this.jdbcProperties,this.log,connection,sqlQueryObject,oldId,anagrafica,idMappingResolutionBehaviour);
			
		}catch(ServiceException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(NotFoundException e){
			rollback = true;
			this.log.debug(e,e); throw e;
		}catch(NotImplementedException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(ValidationException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e,e); throw new ServiceException("Update not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){}
			}
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}

	}
	
	@Override
	public void update(long tableId, Anagrafica anagrafica) throws ServiceException, NotFoundException, NotImplementedException {
		try{
			this.update(tableId, anagrafica, false, null);
		}catch(ValidationException vE){
			// not possible
			throw new ServiceException(vE.getMessage(), vE);
		}
	}
	
	@Override
	public void update(long tableId, Anagrafica anagrafica, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws ServiceException, NotFoundException, NotImplementedException {
		try{
			this.update(tableId, anagrafica, false, idMappingResolutionBehaviour);
		}catch(ValidationException vE){
			// not possible
			throw new ServiceException(vE.getMessage(), vE);
		}
	}
	
	@Override
	public void update(long tableId, Anagrafica anagrafica, boolean validate) throws ServiceException, NotFoundException, NotImplementedException, ValidationException {
		this.update(tableId, anagrafica, validate, null);
	}
		
	@Override
	public void update(long tableId, Anagrafica anagrafica, boolean validate, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws ServiceException, NotFoundException, NotImplementedException, ValidationException {
	
		Connection connection = null;
		boolean oldValueAutoCommit = false;
		boolean rollback = false;
		try{
			
			// check parameters
			if(anagrafica==null){
				throw new Exception("Parameter (type:"+Anagrafica.class.getName()+") 'anagrafica' is null");
			}
			if(tableId<=0){
				throw new Exception("Parameter (type:"+long.class.getName()+") 'tableId' is less equals 0");
			}

			// validate
			if(validate){
				this.validate(anagrafica);
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

			this.serviceCRUD.update(this.jdbcProperties,this.log,connection,sqlQueryObject,tableId,anagrafica,idMappingResolutionBehaviour);
			
		}catch(ServiceException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(NotFoundException e){
			rollback = true;
			this.log.debug(e,e); throw e;
		}catch(NotImplementedException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(ValidationException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e,e); throw new ServiceException("Update not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){}
			}
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}

	}
	
	@Override
	public void updateFields(IdAnagrafica id, UpdateField ... updateFields) throws ServiceException, NotFoundException, NotImplementedException {
	
		Connection connection = null;
		boolean oldValueAutoCommit = false;
		boolean rollback = false;
		try{
			
			// check parameters
			if(id==null){
				throw new Exception("Parameter (type:"+IdAnagrafica.class.getName()+") 'id' is null");
			}
			if(updateFields==null){
				throw new Exception("Parameter (type:"+UpdateField.class.getName()+") 'updateFields' is null");
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

			this.serviceCRUD.updateFields(this.jdbcProperties,this.log,connection,sqlQueryObject,id,updateFields);
			
		}catch(ServiceException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(NotFoundException e){
			rollback = true;
			this.log.debug(e,e); throw e;
		}catch(NotImplementedException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e,e); throw new ServiceException("UpdateFields not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){}
			}
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}

	}
	
	@Override
	public void updateFields(IdAnagrafica id, IExpression condition, UpdateField ... updateFields) throws ServiceException, NotFoundException, NotImplementedException {
	
		Connection connection = null;
		boolean oldValueAutoCommit = false;
		boolean rollback = false;
		try{
			
			// check parameters
			if(id==null){
				throw new Exception("Parameter (type:"+IdAnagrafica.class.getName()+") 'id' is null");
			}
			if(condition==null){
				throw new Exception("Parameter (type:"+IExpression.class.getName()+") 'condition' is null");
			}
			if(updateFields==null){
				throw new Exception("Parameter (type:"+UpdateField.class.getName()+") 'updateFields' is null");
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

			this.serviceCRUD.updateFields(this.jdbcProperties,this.log,connection,sqlQueryObject,id,condition,updateFields);
			
		}catch(ServiceException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(NotFoundException e){
			rollback = true;
			this.log.debug(e,e); throw e;
		}catch(NotImplementedException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e,e); throw new ServiceException("UpdateFields not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){}
			}
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}

	}

	@Override
	public void updateFields(IdAnagrafica id, UpdateModel ... updateModels) throws ServiceException, NotFoundException, NotImplementedException {
	
		Connection connection = null;
		boolean oldValueAutoCommit = false;
		boolean rollback = false;
		try{
			
			// check parameters
			if(id==null){
				throw new Exception("Parameter (type:"+IdAnagrafica.class.getName()+") 'id' is null");
			}
			if(updateModels==null){
				throw new Exception("Parameter (type:"+UpdateModel.class.getName()+") 'updateModels' is null");
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

			this.serviceCRUD.updateFields(this.jdbcProperties,this.log,connection,sqlQueryObject,id,updateModels);
			
		}catch(ServiceException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(NotFoundException e){
			rollback = true;
			this.log.debug(e,e); throw e;
		}catch(NotImplementedException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e,e); throw new ServiceException("UpdateFields not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){}
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
				throw new Exception("Parameter (type:"+long.class.getName()+") 'tableId' is less equals 0");
			}
			if(updateFields==null){
				throw new Exception("Parameter (type:"+UpdateField.class.getName()+") 'updateFields' is null");
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
			
		}catch(ServiceException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(NotFoundException e){
			rollback = true;
			this.log.debug(e,e); throw e;
		}catch(NotImplementedException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e,e); throw new ServiceException("UpdateFields not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){}
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
				throw new Exception("Parameter (type:"+long.class.getName()+") 'tableId' is less equals 0");
			}
			if(condition==null){
				throw new Exception("Parameter (type:"+IExpression.class.getName()+") 'condition' is null");
			}
			if(updateFields==null){
				throw new Exception("Parameter (type:"+UpdateField.class.getName()+") 'updateFields' is null");
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
			
		}catch(ServiceException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(NotFoundException e){
			rollback = true;
			this.log.debug(e,e); throw e;
		}catch(NotImplementedException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e,e); throw new ServiceException("UpdateFields not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){}
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
				throw new Exception("Parameter (type:"+long.class.getName()+") 'tableId' is less equals 0");
			}
			if(updateModels==null){
				throw new Exception("Parameter (type:"+UpdateModel.class.getName()+") 'updateModels' is null");
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
			
		}catch(ServiceException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(NotFoundException e){
			rollback = true;
			this.log.debug(e,e); throw e;
		}catch(NotImplementedException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e,e); throw new ServiceException("UpdateFields not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){}
			}
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}

	}

	@Override
	public void updateOrCreate(IdAnagrafica oldId, Anagrafica anagrafica) throws ServiceException, NotImplementedException {
		try{
			this.updateOrCreate(oldId, anagrafica, false, null);
		}catch(ValidationException vE){
			// not possible
			throw new ServiceException(vE.getMessage(), vE);
		}
	}
	
	@Override
	public void updateOrCreate(IdAnagrafica oldId, Anagrafica anagrafica, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws ServiceException, NotImplementedException {
		try{
			this.updateOrCreate(oldId, anagrafica, false, idMappingResolutionBehaviour);
		}catch(ValidationException vE){
			// not possible
			throw new ServiceException(vE.getMessage(), vE);
		}
	}

	@Override
	public void updateOrCreate(IdAnagrafica oldId, Anagrafica anagrafica, boolean validate) throws ServiceException, NotImplementedException, ValidationException {
		this.updateOrCreate(oldId, anagrafica, validate, null);
	}

	@Override
	public void updateOrCreate(IdAnagrafica oldId, Anagrafica anagrafica, boolean validate, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws ServiceException, NotImplementedException, ValidationException {
	
		Connection connection = null;
		boolean oldValueAutoCommit = false;
		boolean rollback = false;
		try{
			
			// check parameters
			if(anagrafica==null){
				throw new Exception("Parameter (type:"+Anagrafica.class.getName()+") 'anagrafica' is null");
			}
			if(oldId==null){
				throw new Exception("Parameter (type:"+IdAnagrafica.class.getName()+") 'oldId' is null");
			}

			// validate
			if(validate){
				this.validate(anagrafica);
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

			this.serviceCRUD.updateOrCreate(this.jdbcProperties,this.log,connection,sqlQueryObject,oldId,anagrafica,idMappingResolutionBehaviour);
			
		}catch(ServiceException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(NotImplementedException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(ValidationException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e,e); throw new ServiceException("UpdateOrCreate not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){}
			}
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}

	}
	
	@Override
	public void updateOrCreate(long tableId, Anagrafica anagrafica) throws ServiceException, NotImplementedException {
		try{
			this.updateOrCreate(tableId, anagrafica, false, null);
		}catch(ValidationException vE){
			// not possible
			throw new ServiceException(vE.getMessage(), vE);
		}
	}
	
	@Override
	public void updateOrCreate(long tableId, Anagrafica anagrafica, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws ServiceException, NotImplementedException {
		try{
			this.updateOrCreate(tableId, anagrafica, false, idMappingResolutionBehaviour);
		}catch(ValidationException vE){
			// not possible
			throw new ServiceException(vE.getMessage(), vE);
		}
	}

	@Override
	public void updateOrCreate(long tableId, Anagrafica anagrafica, boolean validate) throws ServiceException, NotImplementedException, ValidationException {
		this.updateOrCreate(tableId, anagrafica, validate, null);
	}

	@Override
	public void updateOrCreate(long tableId, Anagrafica anagrafica, boolean validate, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws ServiceException, NotImplementedException, ValidationException {

		Connection connection = null;
		boolean oldValueAutoCommit = false;
		boolean rollback = false;
		try{
			
			// check parameters
			if(anagrafica==null){
				throw new Exception("Parameter (type:"+Anagrafica.class.getName()+") 'anagrafica' is null");
			}
			if(tableId<=0){
				throw new Exception("Parameter (type:"+long.class.getName()+") 'tableId' is less equals 0");
			}

			// validate
			if(validate){
				this.validate(anagrafica);
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

			this.serviceCRUD.updateOrCreate(this.jdbcProperties,this.log,connection,sqlQueryObject,tableId,anagrafica,idMappingResolutionBehaviour);

		}catch(ServiceException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(NotImplementedException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(ValidationException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e,e); throw new ServiceException("UpdateOrCreate not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){}
			}
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}

	}
	
	@Override
	public void delete(Anagrafica anagrafica) throws ServiceException,NotImplementedException {
		
		Connection connection = null;
		boolean oldValueAutoCommit = false;
		boolean rollback = false;
		try{
			
			// check parameters
			if(anagrafica==null){
				throw new Exception("Parameter (type:"+Anagrafica.class.getName()+") 'anagrafica' is null");
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

			this.serviceCRUD.delete(this.jdbcProperties,this.log,connection,sqlQueryObject,anagrafica);	

		}catch(ServiceException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(NotImplementedException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e,e); throw new ServiceException("Delete not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){}
			}
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
	
	}
	

	@Override
	public void deleteById(IdAnagrafica id) throws ServiceException, NotImplementedException {

		Connection connection = null;
		boolean oldValueAutoCommit = false;
		boolean rollback = false;
		try{
			
			// check parameters
			if(id==null){
				throw new Exception("Parameter (type:"+IdAnagrafica.class.getName()+") 'id' is null");
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

			this.serviceCRUD.deleteById(this.jdbcProperties,this.log,connection,sqlQueryObject,id);			

		}catch(ServiceException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(NotImplementedException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e,e); throw new ServiceException("DeleteById not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){}
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

		}catch(ServiceException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(NotImplementedException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e,e); throw new ServiceException("DeleteAll not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){}
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
				throw new Exception("Parameter (type:"+IExpression.class.getName()+") 'expression' is null");
			}
			if( ! (expression instanceof JDBCExpression) ){
				throw new Exception("Parameter (type:"+expression.getClass().getName()+") 'expression' has wrong type, expect "+JDBCExpression.class.getName());
			}
			JDBCExpression jdbcExpression = (JDBCExpression) expression;
			this.log.debug("sql = "+jdbcExpression.toSql());
		
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
	
		}catch(ServiceException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(NotImplementedException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e,e); throw new ServiceException("DeleteAll(expression) not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){}
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
				throw new Exception("Parameter 'tableId' is less equals 0");
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
	
		}catch(ServiceException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(NotImplementedException e){
			rollback = true;
			this.log.error(e,e); throw e;
		}catch(Exception e){
			rollback = true;
			this.log.error(e,e); throw new ServiceException("DeleteById(tableId) not completed: "+e.getMessage(),e);
		}finally{
			if(this.jdbcProperties.isAutomaticTransactionManagement()){
				if(rollback){
					try{
						if(connection!=null)
							connection.rollback();
					}catch(Exception eIgnore){}
				}else{
					try{
						if(connection!=null)
							connection.commit();
					}catch(Exception eIgnore){}
				}
				try{
					if(connection!=null)
						connection.setAutoCommit(oldValueAutoCommit);
				}catch(Exception eIgnore){}
			}
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
	
	}
	
}
