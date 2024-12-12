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
import java.util.List;
import java.util.Map;

import org.openspcoop2.generic_project.beans.FunctionField;
import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.beans.InUse;
import org.openspcoop2.generic_project.beans.NonNegativeNumber;
import org.openspcoop2.generic_project.beans.Union;
import org.openspcoop2.generic_project.beans.UnionExpression;
import org.openspcoop2.generic_project.dao.IDBServiceUtilities;
import org.openspcoop2.generic_project.dao.jdbc.IJDBCServiceSearchWithId;
import org.openspcoop2.generic_project.dao.jdbc.JDBCExpression;
import org.openspcoop2.generic_project.dao.jdbc.JDBCPaginatedExpression;
import org.openspcoop2.generic_project.dao.jdbc.JDBCProperties;
import org.openspcoop2.generic_project.dao.jdbc.JDBCServiceManagerProperties;
import org.openspcoop2.generic_project.dao.jdbc.utils.IJDBCFetch;
import org.openspcoop2.generic_project.dao.jdbc.utils.JDBC_SQLObjectFactory;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.exception.ValidationException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.IPaginatedExpression;
import org.openspcoop2.generic_project.expression.impl.sql.ISQLFieldConverter;
import org.openspcoop2.utils.sql.ISQLQueryObject;
import org.slf4j.Logger;

import it.govpay.core.exceptions.ParametroErratoException;
import it.govpay.core.exceptions.ParametroObbligatorioException;
import it.govpay.orm.ACL;
import it.govpay.orm.IdAcl;
import it.govpay.orm.constants.Costanti;
import it.govpay.orm.dao.IDBACLServiceSearch;
import it.govpay.orm.utils.ProjectInfo;

/**     
 * Service can be used to search for the backend objects of type {@link it.govpay.orm.ACL} 
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
*/
public class JDBCACLServiceSearch implements IDBACLServiceSearch, IDBServiceUtilities<ACL> {


	protected JDBCServiceManagerProperties jdbcProperties = null;
	protected JDBCServiceManager jdbcServiceManager = null;
	protected Logger log = null;
	protected IJDBCServiceSearchWithId<ACL, IdAcl, JDBCServiceManager> serviceSearch = null;
	protected JDBC_SQLObjectFactory jdbcSqlObjectFactory = null;
	private String modelClassName = ACL.class.getName();
	private String idModelClassName = IdAcl.class.getName();
	public JDBCACLServiceSearch(JDBCServiceManager jdbcServiceManager) throws ServiceException {
		this.jdbcServiceManager = jdbcServiceManager;
		this.jdbcProperties = jdbcServiceManager.getJdbcProperties();
		this.log = jdbcServiceManager.getLog();
		this.serviceSearch = JDBCProperties.getInstance(ProjectInfo.getInstance()).getServiceSearch("acl");
		this.serviceSearch.setServiceManager(new JDBCLimitedServiceManager(this.jdbcServiceManager));
		this.jdbcSqlObjectFactory = new JDBC_SQLObjectFactory();
	}
	
	@Override
	public void validate(ACL acl) throws ServiceException,
			ValidationException, NotImplementedException {
		org.openspcoop2.generic_project.utils.XSDValidator.validate(acl, this.log, 
				it.govpay.orm.utils.XSDValidator.getXSDValidator(this.log));
	}
	
	@Override
	public IJDBCFetch getFetch() {
		return this.serviceSearch.getFetch();
	}

	@Override
	public ISQLFieldConverter getFieldConverter() {
		return this.serviceSearch.getFieldConverter();
	}
	
	@Override
	public IdAcl convertToId(ACL obj)
			throws ServiceException, NotImplementedException {
		
		Connection connection = null;
		try{
			
			// check parameters
			if(obj==null){
				throw new ParametroObbligatorioException(this.modelClassName, Costanti.PARAMETER_OBJ);
			}
			
			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();
		
			return this.serviceSearch.convertToId(this.jdbcProperties,this.log,connection,sqlQueryObject,obj);
		
		}catch(ServiceException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("ConvertToId not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
		
	}
		
	@Override
	public ACL get(IdAcl id) throws ServiceException, NotFoundException,MultipleResultException, NotImplementedException {
    
		Connection connection = null;
		try{
			
			// check parameters
			if(id==null){
				throw new ParametroObbligatorioException(this.idModelClassName, Costanti.PARAMETER_ID);
			}
			
			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();
		
			return this.serviceSearch.get(this.jdbcProperties,this.log,connection,sqlQueryObject,id,null);
		
		}catch(ServiceException | NotFoundException | MultipleResultException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("Get not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
	
	}

	@Override
	public ACL get(IdAcl id, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws ServiceException, NotFoundException,MultipleResultException, NotImplementedException {
		Connection connection = null;
		try{
			
			// check parameters
			if(id==null){
				throw new ParametroObbligatorioException(this.idModelClassName, Costanti.PARAMETER_ID);
			}
			if(idMappingResolutionBehaviour==null){
				throw new ParametroObbligatorioException(org.openspcoop2.generic_project.beans.IDMappingBehaviour.class.getName(), Costanti.PARAMETER_ID_MAPPING_RESOLUTION_BEHAVIOUR);
			}
			
			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();
		
			return this.serviceSearch.get(this.jdbcProperties,this.log,connection,sqlQueryObject,id,idMappingResolutionBehaviour);
		
		}catch(ServiceException | NotFoundException | MultipleResultException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("Get (idMappingResolutionBehaviour) not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
	
	}

	@Override
	public boolean exists(IdAcl id) throws MultipleResultException,ServiceException,NotImplementedException {

		Connection connection = null;
		try{
			
			// check parameters
			if(id==null){
				throw new ParametroObbligatorioException(this.idModelClassName, Costanti.PARAMETER_ID);
			}

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();

			return this.serviceSearch.exists(this.jdbcProperties,this.log,connection,sqlQueryObject,id);
	
		}catch(MultipleResultException | ServiceException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("Exists not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
		
	}
	
	@Override
	public List<IdAcl> findAllIds(IPaginatedExpression expression) throws ServiceException, NotImplementedException {

		Connection connection = null;
		try{
			
			// check parameters
			if(expression==null){
				throw new ParametroObbligatorioException(IPaginatedExpression.class.getName(), Costanti.PARAMETER_EXPRESSION);
			}
			if( ! (expression instanceof JDBCPaginatedExpression) ){
				throw new ParametroErratoException(expression.getClass().getName(), Costanti.PARAMETER_EXPRESSION, MessageFormat.format(Costanti.HAS_WRONG_TYPE_EXPECT_0, JDBCPaginatedExpression.class.getName()));
			}
			JDBCPaginatedExpression jdbcPaginatedExpression = (JDBCPaginatedExpression) expression;
			this.log.debug("sql = {}", jdbcPaginatedExpression.toSql());

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();
			
			return this.serviceSearch.findAllIds(this.jdbcProperties,this.log,connection,sqlQueryObject,jdbcPaginatedExpression,null);
	
		}catch(ServiceException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("FindAllIds not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
		
	}
	
	@Override
	public List<IdAcl> findAllIds(IPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws ServiceException, NotImplementedException {

		Connection connection = null;
		try{
			
			// check parameters
			if(idMappingResolutionBehaviour==null){
				throw new ParametroObbligatorioException(org.openspcoop2.generic_project.beans.IDMappingBehaviour.class.getName(), Costanti.PARAMETER_ID_MAPPING_RESOLUTION_BEHAVIOUR);
			}
			if(expression==null){
				throw new ParametroObbligatorioException(IPaginatedExpression.class.getName(), Costanti.PARAMETER_EXPRESSION);
			}
			if( ! (expression instanceof JDBCPaginatedExpression) ){
				throw new ParametroErratoException(expression.getClass().getName(), Costanti.PARAMETER_EXPRESSION, MessageFormat.format(Costanti.HAS_WRONG_TYPE_EXPECT_0, JDBCPaginatedExpression.class.getName()));
			}
			JDBCPaginatedExpression jdbcPaginatedExpression = (JDBCPaginatedExpression) expression;
			this.log.debug("sql = {}", jdbcPaginatedExpression.toSql());

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();
			
			return this.serviceSearch.findAllIds(this.jdbcProperties,this.log,connection,sqlQueryObject,jdbcPaginatedExpression,idMappingResolutionBehaviour);
	
		}catch(ServiceException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("FindAllIds not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
		
	}

	@Override
	public List<ACL> findAll(IPaginatedExpression expression) throws ServiceException, NotImplementedException {

		Connection connection = null;
		try{
			
			// check parameters
			if(expression==null){
				throw new ParametroObbligatorioException(IPaginatedExpression.class.getName(), Costanti.PARAMETER_EXPRESSION);
			}
			if( ! (expression instanceof JDBCPaginatedExpression) ){
				throw new ParametroErratoException(expression.getClass().getName(), Costanti.PARAMETER_EXPRESSION, MessageFormat.format(Costanti.HAS_WRONG_TYPE_EXPECT_0, JDBCPaginatedExpression.class.getName()));
			}
			JDBCPaginatedExpression jdbcPaginatedExpression = (JDBCPaginatedExpression) expression;
			this.log.debug("sql = {}", jdbcPaginatedExpression.toSql());

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();

			return this.serviceSearch.findAll(this.jdbcProperties,this.log,connection,sqlQueryObject,jdbcPaginatedExpression,null);			
	
		}catch(ServiceException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("FindAll not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
		
	}
	
	@Override
	public List<ACL> findAll(IPaginatedExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws ServiceException, NotImplementedException {

		Connection connection = null;
		try{
			
			// check parameters
			if(idMappingResolutionBehaviour==null){
				throw new ParametroObbligatorioException(org.openspcoop2.generic_project.beans.IDMappingBehaviour.class.getName(), Costanti.PARAMETER_ID_MAPPING_RESOLUTION_BEHAVIOUR);
			}
			if(expression==null){
				throw new ParametroObbligatorioException(IPaginatedExpression.class.getName(), Costanti.PARAMETER_EXPRESSION);
			}
			if( ! (expression instanceof JDBCPaginatedExpression) ){
				throw new ParametroErratoException(expression.getClass().getName(), Costanti.PARAMETER_EXPRESSION, MessageFormat.format(Costanti.HAS_WRONG_TYPE_EXPECT_0, JDBCPaginatedExpression.class.getName()));
			}
			JDBCPaginatedExpression jdbcPaginatedExpression = (JDBCPaginatedExpression) expression;
			this.log.debug("sql = {}", jdbcPaginatedExpression.toSql());

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();

			return this.serviceSearch.findAll(this.jdbcProperties,this.log,connection,sqlQueryObject,jdbcPaginatedExpression,idMappingResolutionBehaviour);			
	
		}catch(ServiceException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("FindAll not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
		
	}

	@Override
	public ACL find(IExpression expression) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException {

		Connection connection = null;
		try{
			
			// check parameters
			if(expression==null){
				throw new ParametroObbligatorioException(IExpression.class.getName(), Costanti.PARAMETER_EXPRESSION);
			}
			if( ! (expression instanceof JDBCExpression) ){
				throw new ParametroErratoException(expression.getClass().getName(), Costanti.PARAMETER_EXPRESSION, MessageFormat.format(Costanti.HAS_WRONG_TYPE_EXPECT_0, JDBCExpression.class.getName()));
			}
			JDBCExpression jdbcExpression = (JDBCExpression) expression;
			this.log.debug("sql = {}", jdbcExpression.toSql());

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();

			return this.serviceSearch.find(this.jdbcProperties,this.log,connection,sqlQueryObject,jdbcExpression,null);			

		}catch(ServiceException | NotFoundException | MultipleResultException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("Find not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
		
	}
	
	@Override
	public ACL find(IExpression expression, org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException {

		Connection connection = null;
		try{
			
			// check parameters
			if(idMappingResolutionBehaviour==null){
				throw new ParametroObbligatorioException(org.openspcoop2.generic_project.beans.IDMappingBehaviour.class.getName(), Costanti.PARAMETER_ID_MAPPING_RESOLUTION_BEHAVIOUR);
			}
			if(expression==null){
				throw new ParametroObbligatorioException(IExpression.class.getName(), Costanti.PARAMETER_EXPRESSION);
			}
			if( ! (expression instanceof JDBCExpression) ){
				throw new ParametroErratoException(expression.getClass().getName(), Costanti.PARAMETER_EXPRESSION, MessageFormat.format(Costanti.HAS_WRONG_TYPE_EXPECT_0, JDBCExpression.class.getName()));
			}
			JDBCExpression jdbcExpression = (JDBCExpression) expression;
			this.log.debug("sql = {}", jdbcExpression.toSql());

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();

			return this.serviceSearch.find(this.jdbcProperties,this.log,connection,sqlQueryObject,jdbcExpression,idMappingResolutionBehaviour);			

		}catch(ServiceException | NotFoundException | MultipleResultException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("Find not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
		
	}

	@Override
	public NonNegativeNumber count(IExpression expression) throws ServiceException, NotImplementedException {

		Connection connection = null;
		try{
			
			// check parameters
			if(expression==null){
				throw new ParametroObbligatorioException(IPaginatedExpression.class.getName(), Costanti.PARAMETER_EXPRESSION);
			}
			if( ! (expression instanceof JDBCExpression) ){
				throw new ParametroErratoException(expression.getClass().getName(), Costanti.PARAMETER_EXPRESSION, MessageFormat.format(Costanti.HAS_WRONG_TYPE_EXPECT_0, JDBCExpression.class.getName()));
			}
			JDBCExpression jdbcExpression = (JDBCExpression) expression;
			this.log.debug("sql = {}", jdbcExpression.toSql());
			
			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();

			return this.serviceSearch.count(this.jdbcProperties,this.log,connection,sqlQueryObject,jdbcExpression);
	
		}catch(ServiceException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("Count not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
		
	}

	@Override
	public InUse inUse(IdAcl id) throws ServiceException, NotFoundException,NotImplementedException {

		Connection connection = null;
		try{
			
			// check parameters
			if(id==null){
				throw new ParametroObbligatorioException(this.idModelClassName, Costanti.PARAMETER_ID);
			}
			
			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();

			return this.serviceSearch.inUse(this.jdbcProperties,this.log,connection,sqlQueryObject,id);	
	
		}catch(ServiceException | NotFoundException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("InUse not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
		
	}
	
	@Override
	public List<Object> select(IPaginatedExpression paginatedExpression, IField field) throws ServiceException,NotFoundException,NotImplementedException {
	
		Connection connection = null;
		try{
			
			// check parameters
			if(paginatedExpression==null){
				throw new ParametroObbligatorioException(IPaginatedExpression.class.getName(), Costanti.PARAMETER_PAGINATED_EXPRESSION);
			}
			if( ! (paginatedExpression instanceof JDBCPaginatedExpression) ){
				throw new ParametroErratoException(paginatedExpression.getClass().getName(), Costanti.PARAMETER_PAGINATED_EXPRESSION, MessageFormat.format(Costanti.HAS_WRONG_TYPE_EXPECT_0, JDBCPaginatedExpression.class.getName()));
			}
			JDBCPaginatedExpression jdbcPaginatedExpression = (JDBCPaginatedExpression) paginatedExpression;
			this.log.debug("sql = {}", jdbcPaginatedExpression.toSql());

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();

			return this.serviceSearch.select(this.jdbcProperties,this.log,connection,sqlQueryObject,jdbcPaginatedExpression,field);			
	
		}catch(ServiceException | NotFoundException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("Select not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
	
	}
	
	@Override
	public List<Object> select(IPaginatedExpression paginatedExpression, boolean distinct, IField field) throws ServiceException,NotFoundException,NotImplementedException {

		Connection connection = null;
		try{
			
			// check parameters
			if(paginatedExpression==null){
				throw new ParametroObbligatorioException(IPaginatedExpression.class.getName(), Costanti.PARAMETER_PAGINATED_EXPRESSION);
			}
			if( ! (paginatedExpression instanceof JDBCPaginatedExpression) ){
				throw new ParametroErratoException(paginatedExpression.getClass().getName(), Costanti.PARAMETER_PAGINATED_EXPRESSION, MessageFormat.format(Costanti.HAS_WRONG_TYPE_EXPECT_0, JDBCPaginatedExpression.class.getName()));
			}
			JDBCPaginatedExpression jdbcPaginatedExpression = (JDBCPaginatedExpression) paginatedExpression;
			this.log.debug("sql = {}", jdbcPaginatedExpression.toSql());

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();

			return this.serviceSearch.select(this.jdbcProperties,this.log,connection,sqlQueryObject,jdbcPaginatedExpression,distinct,field);			
	
		}catch(ServiceException | NotFoundException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("Select not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
		
	}
	
	@Override
	public List<Map<String,Object>> select(IPaginatedExpression paginatedExpression, IField ... field) throws ServiceException,NotFoundException,NotImplementedException {
	
		Connection connection = null;
		try{
			
			// check parameters
			if(paginatedExpression==null){
				throw new ParametroObbligatorioException(IPaginatedExpression.class.getName(), Costanti.PARAMETER_PAGINATED_EXPRESSION);
			}
			if( ! (paginatedExpression instanceof JDBCPaginatedExpression) ){
				throw new ParametroErratoException(paginatedExpression.getClass().getName(), Costanti.PARAMETER_PAGINATED_EXPRESSION, MessageFormat.format(Costanti.HAS_WRONG_TYPE_EXPECT_0, JDBCPaginatedExpression.class.getName()));
			}
			JDBCPaginatedExpression jdbcPaginatedExpression = (JDBCPaginatedExpression) paginatedExpression;
			this.log.debug("sql = {}", jdbcPaginatedExpression.toSql());

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();

			return this.serviceSearch.select(this.jdbcProperties,this.log,connection,sqlQueryObject,jdbcPaginatedExpression,field);			
	
		}catch(ServiceException | NotFoundException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("Select not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
	
	}
	@Override
	public List<Map<String,Object>> select(IPaginatedExpression paginatedExpression, boolean distinct, IField ... field) throws ServiceException,NotFoundException,NotImplementedException {

		Connection connection = null;
		try{
			
			// check parameters
			if(paginatedExpression==null){
				throw new ParametroObbligatorioException(IPaginatedExpression.class.getName(), Costanti.PARAMETER_PAGINATED_EXPRESSION);
			}
			if( ! (paginatedExpression instanceof JDBCPaginatedExpression) ){
				throw new ParametroErratoException(paginatedExpression.getClass().getName(), Costanti.PARAMETER_PAGINATED_EXPRESSION, MessageFormat.format(Costanti.HAS_WRONG_TYPE_EXPECT_0, JDBCPaginatedExpression.class.getName()));
			}
			JDBCPaginatedExpression jdbcPaginatedExpression = (JDBCPaginatedExpression) paginatedExpression;
			this.log.debug("sql = {}", jdbcPaginatedExpression.toSql());

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();

			return this.serviceSearch.select(this.jdbcProperties,this.log,connection,sqlQueryObject,jdbcPaginatedExpression,distinct,field);			
	
		}catch(ServiceException | NotFoundException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("Select not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
		
	}
	
	@Override
	public Object aggregate(IExpression expression, FunctionField functionField) throws ServiceException,NotFoundException,NotImplementedException {

		Connection connection = null;
		try{
			
			// check parameters
			if(expression==null){
				throw new ParametroObbligatorioException(IExpression.class.getName(), Costanti.PARAMETER_EXPRESSION);
			}
			if( ! (expression instanceof JDBCExpression) ){
				throw new ParametroErratoException(expression.getClass().getName(), Costanti.PARAMETER_EXPRESSION, MessageFormat.format(Costanti.HAS_WRONG_TYPE_EXPECT_0, JDBCExpression.class.getName()));
			}
			JDBCExpression jdbcExpression = (JDBCExpression) expression;
			this.log.debug("sql = {}", jdbcExpression.toSql());

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();

			return this.serviceSearch.aggregate(this.jdbcProperties,this.log,connection,sqlQueryObject,jdbcExpression,functionField);			
	
		}catch(ServiceException | NotFoundException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("Aggregate not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
		
	}
	
	@Override
	public Map<String,Object> aggregate(IExpression expression, FunctionField ... functionField) throws ServiceException,NotFoundException,NotImplementedException {

		Connection connection = null;
		try{
			
			// check parameters
			if(expression==null){
				throw new ParametroObbligatorioException(IExpression.class.getName(), Costanti.PARAMETER_EXPRESSION);
			}
			if( ! (expression instanceof JDBCExpression) ){
				throw new ParametroErratoException(expression.getClass().getName(), Costanti.PARAMETER_EXPRESSION, MessageFormat.format(Costanti.HAS_WRONG_TYPE_EXPECT_0, JDBCExpression.class.getName()));
			}
			JDBCExpression jdbcExpression = (JDBCExpression) expression;
			this.log.debug("sql = {}", jdbcExpression.toSql());

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();

			return this.serviceSearch.aggregate(this.jdbcProperties,this.log,connection,sqlQueryObject,jdbcExpression,functionField);			
	
		}catch(ServiceException | NotFoundException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("Aggregate not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
		
	}
	
	@Override
	public List<Map<String,Object>> groupBy(IExpression expression, FunctionField ... functionField) throws ServiceException,NotFoundException,NotImplementedException {

		Connection connection = null;
		try{
			
			// check parameters
			if(expression==null){
				throw new ParametroObbligatorioException(IExpression.class.getName(), Costanti.PARAMETER_EXPRESSION);
			}
			if( ! (expression instanceof JDBCExpression) ){
				throw new ParametroErratoException(expression.getClass().getName(), Costanti.PARAMETER_EXPRESSION, MessageFormat.format(Costanti.HAS_WRONG_TYPE_EXPECT_0, JDBCExpression.class.getName())); 
			}
			JDBCExpression jdbcExpression = (JDBCExpression) expression;
			this.log.debug("sql = {}", jdbcExpression.toSql());

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();

			return this.serviceSearch.groupBy(this.jdbcProperties,this.log,connection,sqlQueryObject,jdbcExpression,functionField);			
	
		}catch(ServiceException | NotFoundException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("GroupBy not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
		
	}
	
	@Override
	public List<Map<String,Object>> groupBy(IPaginatedExpression paginatedExpression, FunctionField ... functionField) throws ServiceException,NotFoundException,NotImplementedException {

		Connection connection = null;
		try{
			
			// check parameters
			if(paginatedExpression==null){
				throw new ParametroObbligatorioException(IPaginatedExpression.class.getName(), Costanti.PARAMETER_PAGINATED_EXPRESSION);
			}
			if( ! (paginatedExpression instanceof JDBCPaginatedExpression) ){
				throw new ParametroErratoException(paginatedExpression.getClass().getName(), Costanti.PARAMETER_PAGINATED_EXPRESSION, MessageFormat.format(Costanti.HAS_WRONG_TYPE_EXPECT_0, JDBCPaginatedExpression.class.getName())); 
			}
			JDBCPaginatedExpression jdbcPaginatedExpression = (JDBCPaginatedExpression) paginatedExpression;
			this.log.debug("sql = {}", jdbcPaginatedExpression.toSql());

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();

			return this.serviceSearch.groupBy(this.jdbcProperties,this.log,connection,sqlQueryObject,jdbcPaginatedExpression,functionField);			
	
		}catch(ServiceException | NotFoundException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("GroupBy not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
		
	}
	
	@Override
	public List<Map<String,Object>> union(Union union, UnionExpression ... unionExpression) throws ServiceException,NotFoundException,NotImplementedException {

		Connection connection = null;
		try{
			
			// check parameters
			if(unionExpression==null){
				throw new ParametroObbligatorioException(UnionExpression.class.getName(), Costanti.PARAMETER_UNION_EXPRESSION);
			}
			
			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();

			return this.serviceSearch.union(this.jdbcProperties,this.log,connection,sqlQueryObject,union,unionExpression);			
	
		}catch(ServiceException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(NotFoundException e){
			this.log.debug(e.getMessage(),e); throw e;
		}catch(NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("Union not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
		
	}
	
	@Override
	public NonNegativeNumber unionCount(Union union, UnionExpression ... unionExpression) throws ServiceException,NotFoundException,NotImplementedException {

		Connection connection = null;
		try{
			
			// check parameters
			if(unionExpression==null){
				throw new ParametroObbligatorioException(UnionExpression.class.getName(), Costanti.PARAMETER_UNION_EXPRESSION);
			}
			
			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();

			return this.serviceSearch.unionCount(this.jdbcProperties,this.log,connection,sqlQueryObject,union,unionExpression);			
	
		}catch(ServiceException | NotFoundException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("UnionCount not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
		
	}

	@Override
	public IExpression newExpression() throws ServiceException,NotImplementedException {

		return this.serviceSearch.newExpression(this.log);

	}

	@Override
	public IPaginatedExpression newPaginatedExpression() throws ServiceException, NotImplementedException {

		return this.serviceSearch.newPaginatedExpression(this.log);

	}
	
	@Override
	public IExpression toExpression(IPaginatedExpression paginatedExpression) throws ServiceException,NotImplementedException {

		return this.serviceSearch.toExpression((JDBCPaginatedExpression)paginatedExpression,this.log);

	}

	@Override
	public IPaginatedExpression toPaginatedExpression(IExpression expression) throws ServiceException, NotImplementedException {

		return this.serviceSearch.toPaginatedExpression((JDBCExpression)expression,this.log);

	}
	

	// -- DB
	
	@Override
	public void mappingTableIds(IdAcl id, ACL obj) throws ServiceException,NotFoundException,NotImplementedException{
		Connection connection = null;
		try{
			
			// check parameters
			if(id==null){
				throw new ParametroObbligatorioException(this.idModelClassName, Costanti.PARAMETER_ID);
			}
			if(obj==null){
				throw new ParametroObbligatorioException(this.modelClassName, Costanti.PARAMETER_OBJ);
			}
			
			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();
		
			this.serviceSearch.mappingTableIds(this.jdbcProperties,this.log,connection,sqlQueryObject,id,obj);
		
		}catch(ServiceException | NotFoundException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("mappingIds(IdObject) not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
	}
	
	@Override
	public void mappingTableIds(long tableId, ACL obj) throws ServiceException,NotFoundException,NotImplementedException{
		Connection connection = null;
		try{
			
			// check parameters
			if(tableId<=0){
				throw new ParametroObbligatorioException(long.class.getName(), Costanti.PARAMETER_TABLE_ID, Costanti.ERROR_MSG_IS_LESS_EQUALS_0);
			}
			if(obj==null){
				throw new ParametroObbligatorioException(this.modelClassName, Costanti.PARAMETER_OBJ);
			}
			
			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();
		
			this.serviceSearch.mappingTableIds(this.jdbcProperties,this.log,connection,sqlQueryObject,tableId,obj);
		
		}catch(ServiceException | NotFoundException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("mappingIds(tableId) not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
	}
		
	@Override
	public ACL get(long tableId) throws ServiceException, NotFoundException,MultipleResultException, NotImplementedException {
    
		Connection connection = null;
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
		
			return this.serviceSearch.get(this.jdbcProperties,this.log,connection,sqlQueryObject,tableId,null);
		
		}catch(ServiceException | NotFoundException | MultipleResultException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("Get(tableId) not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
	
	}
	
	@Override
	public ACL get(long tableId,org.openspcoop2.generic_project.beans.IDMappingBehaviour idMappingResolutionBehaviour) throws ServiceException, NotFoundException,MultipleResultException, NotImplementedException {
    
		Connection connection = null;
		try{
			
			// check parameters
			if(tableId<=0){
				throw new ParametroObbligatorioException(long.class.getName(), Costanti.PARAMETER_TABLE_ID, Costanti.ERROR_MSG_IS_LESS_EQUALS_0);
			}
			if(idMappingResolutionBehaviour==null){
				throw new ParametroObbligatorioException(org.openspcoop2.generic_project.beans.IDMappingBehaviour.class.getName(), Costanti.PARAMETER_ID_MAPPING_RESOLUTION_BEHAVIOUR);
			}
			
			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();
		
			return this.serviceSearch.get(this.jdbcProperties,this.log,connection,sqlQueryObject,tableId,idMappingResolutionBehaviour);
		
		}catch(ServiceException | NotFoundException | MultipleResultException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("Get(tableId,idMappingResolutionBehaviour) not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
	
	}
	
	@Override
	public boolean exists(long tableId) throws MultipleResultException,ServiceException,NotImplementedException {

		Connection connection = null;
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

			return this.serviceSearch.exists(this.jdbcProperties,this.log,connection,sqlQueryObject,tableId);			
	
		}catch(MultipleResultException | ServiceException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("Exists(tableId) not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
		
	}
	
	@Override
	public List<Long> findAllTableIds(IPaginatedExpression expression) throws ServiceException, NotImplementedException {
		
		Connection connection = null;
		try{
			
			// check parameters
			if(expression==null){
				throw new ParametroObbligatorioException(IPaginatedExpression.class.getName(), Costanti.PARAMETER_EXPRESSION);
			}
			if( ! (expression instanceof JDBCPaginatedExpression) ){
				throw new ParametroErratoException(expression.getClass().getName(), Costanti.PARAMETER_EXPRESSION, MessageFormat.format(Costanti.HAS_WRONG_TYPE_EXPECT_0, JDBCPaginatedExpression.class.getName()));
			}
			JDBCPaginatedExpression jdbcPaginatedExpression = (JDBCPaginatedExpression) expression;
			this.log.debug("sql = {}", jdbcPaginatedExpression.toSql());

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();
			
			return this.serviceSearch.findAllTableIds(this.jdbcProperties,this.log,connection,sqlQueryObject,jdbcPaginatedExpression);
	
		}catch(ServiceException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("findAllTableIds not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
		
	}
	
	@Override
	public long findTableId(IExpression expression) throws ServiceException, NotFoundException, MultipleResultException, NotImplementedException {
	
		Connection connection = null;
		try{
			
			// check parameters
			if(expression==null){
				throw new ParametroObbligatorioException(IExpression.class.getName(), Costanti.PARAMETER_EXPRESSION);
			}
			if( ! (expression instanceof JDBCExpression) ){
				throw new ParametroErratoException(expression.getClass().getName(), Costanti.PARAMETER_EXPRESSION, MessageFormat.format(Costanti.HAS_WRONG_TYPE_EXPECT_0, JDBCExpression.class.getName())); 
			}
			JDBCExpression jdbcExpression = (JDBCExpression) expression;
			this.log.debug("sql = {}", jdbcExpression.toSql());

			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();

			return this.serviceSearch.findTableId(this.jdbcProperties,this.log,connection,sqlQueryObject,jdbcExpression);			

		}catch(ServiceException | NotFoundException | MultipleResultException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("findTableId not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
	
	}
	
	@Override
	public InUse inUse(long tableId) throws ServiceException, NotFoundException, NotImplementedException {
	
		Connection connection = null;
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

			return this.serviceSearch.inUse(this.jdbcProperties,this.log,connection,sqlQueryObject,tableId);		
	
		}catch(ServiceException | NotFoundException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("InUse(tableId) not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
	
	}
	
	@Override
	public IdAcl findId(long tableId, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException {
		
		Connection connection = null;
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

			return this.serviceSearch.findId(this.jdbcProperties,this.log,connection,sqlQueryObject,tableId,throwNotFound);		
	
		}catch(ServiceException | NotFoundException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("findId(tableId,throwNotFound) not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
		
	}

	@Override
	public Long findTableId(IdAcl id, boolean throwNotFound)
			throws NotFoundException, ServiceException, NotImplementedException {
		
		Connection connection = null;
		try{
			
			// check parameters
			if(id==null){
				throw new ParametroObbligatorioException(this.idModelClassName, Costanti.PARAMETER_ID);
			}
			
			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();

			return this.serviceSearch.findTableId(this.jdbcProperties,this.log,connection,sqlQueryObject,id,throwNotFound);		
	
		}catch(ServiceException | NotFoundException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("findId(tableId,throwNotFound) not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
	}
	
	@Override
	public void disableSelectForUpdate() throws ServiceException,NotImplementedException {
		this.jdbcSqlObjectFactory.setSelectForUpdate(false);
	}

	@Override
	public void enableSelectForUpdate() throws ServiceException,NotImplementedException {
		this.jdbcSqlObjectFactory.setSelectForUpdate(true);
	}
	
	
	@Override
	public List<List<Object>> nativeQuery(String sql,List<Class<?>> returnClassTypes,Object ... param) throws ServiceException,NotFoundException,NotImplementedException{
	
		Connection connection = null;
		try{
			
			// check parameters
			if(returnClassTypes==null || returnClassTypes.isEmpty()){
				throw new ParametroObbligatorioException("", Costanti.PARAMETER_RETURN_CLASS_TYPES, Costanti.ERROR_MSG_IS_LESS_EQUALS_0);
			}
			
			// ISQLQueryObject
			ISQLQueryObject sqlQueryObject = this.jdbcSqlObjectFactory.createSQLQueryObject(this.jdbcProperties.getDatabase());
			sqlQueryObject.setANDLogicOperator(true);
			// Connection sql
			connection = this.jdbcServiceManager.getConnection();

			return this.serviceSearch.nativeQuery(this.jdbcProperties,this.log,connection,sqlQueryObject,sql,returnClassTypes,param);		
	
		}catch(ServiceException | NotFoundException | NotImplementedException e){
			this.log.error(e.getMessage(),e); throw e;
		}catch(Exception e){
			this.log.error(e.getMessage(),e); throw new ServiceException("nativeQuery not completed: "+e.getMessage(),e);
		}finally{
			if(connection!=null){
				this.jdbcServiceManager.closeConnection(connection);
			}
		}
	
	}
	
}
