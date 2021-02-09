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
package it.govpay.orm.utils.serializer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;

import javax.xml.bind.JAXBElement;

import org.openspcoop2.generic_project.exception.SerializerException;
import org.openspcoop2.utils.beans.WriteToSerializerType;
import org.openspcoop2.utils.xml.JaxbUtils;

import it.govpay.orm.ACL;
import it.govpay.orm.Applicazione;
import it.govpay.orm.Audit;
import it.govpay.orm.Batch;
import it.govpay.orm.Configurazione;
import it.govpay.orm.Connettore;
import it.govpay.orm.Documento;
import it.govpay.orm.Dominio;
import it.govpay.orm.Evento;
import it.govpay.orm.FR;
import it.govpay.orm.IUV;
import it.govpay.orm.IbanAccredito;
import it.govpay.orm.IdAcl;
import it.govpay.orm.IdAnagrafica;
import it.govpay.orm.IdApplicazione;
import it.govpay.orm.IdBatch;
import it.govpay.orm.IdCarrello;
import it.govpay.orm.IdConfigurazione;
import it.govpay.orm.IdConnettore;
import it.govpay.orm.IdContoAccredito;
import it.govpay.orm.IdDocumento;
import it.govpay.orm.IdDominio;
import it.govpay.orm.IdEr;
import it.govpay.orm.IdEvento;
import it.govpay.orm.IdFr;
import it.govpay.orm.IdIbanAccredito;
import it.govpay.orm.IdIncasso;
import it.govpay.orm.IdIntermediario;
import it.govpay.orm.IdIuv;
import it.govpay.orm.IdMessaggio;
import it.govpay.orm.IdNotifica;
import it.govpay.orm.IdOperatore;
import it.govpay.orm.IdOperazione;
import it.govpay.orm.IdPagamento;
import it.govpay.orm.IdPagamentoPortale;
import it.govpay.orm.IdPromemoria;
import it.govpay.orm.IdRendicontazione;
import it.govpay.orm.IdRpt;
import it.govpay.orm.IdRr;
import it.govpay.orm.IdSingolaRendicontazione;
import it.govpay.orm.IdSingolaRevoca;
import it.govpay.orm.IdSingoloVersamento;
import it.govpay.orm.IdStampa;
import it.govpay.orm.IdStazione;
import it.govpay.orm.IdTabellaControparti;
import it.govpay.orm.IdTipoTributo;
import it.govpay.orm.IdTipoVersamento;
import it.govpay.orm.IdTipoVersamentoDominio;
import it.govpay.orm.IdTracciato;
import it.govpay.orm.IdTracciatoNotificaPagamenti;
import it.govpay.orm.IdTributo;
import it.govpay.orm.IdUo;
import it.govpay.orm.IdUtenza;
import it.govpay.orm.IdVersamento;
import it.govpay.orm.IdVistaRiscossione;
import it.govpay.orm.Incasso;
import it.govpay.orm.Intermediario;
import it.govpay.orm.IuvSearch;
import it.govpay.orm.Notifica;
import it.govpay.orm.NotificaAppIO;
import it.govpay.orm.Operatore;
import it.govpay.orm.Operazione;
import it.govpay.orm.Pagamento;
import it.govpay.orm.PagamentoPortale;
import it.govpay.orm.PagamentoPortaleVersamento;
import it.govpay.orm.Promemoria;
import it.govpay.orm.RPT;
import it.govpay.orm.RR;
import it.govpay.orm.Rendicontazione;
import it.govpay.orm.SingoloVersamento;
import it.govpay.orm.Stampa;
import it.govpay.orm.Stazione;
import it.govpay.orm.TipoTributo;
import it.govpay.orm.TipoVersamento;
import it.govpay.orm.TipoVersamentoDominio;
import it.govpay.orm.Tracciato;
import it.govpay.orm.TracciatoNotificaPagamenti;
import it.govpay.orm.Tributo;
import it.govpay.orm.Uo;
import it.govpay.orm.Utenza;
import it.govpay.orm.UtenzaDominio;
import it.govpay.orm.UtenzaTipoVersamento;
import it.govpay.orm.Versamento;
import it.govpay.orm.VersamentoIncasso;
import it.govpay.orm.VistaPagamentoPortale;
import it.govpay.orm.VistaRendicontazione;
import it.govpay.orm.VistaRiscossioni;
import it.govpay.orm.VistaRptVersamento;

/**     
 * XML Serializer of beans
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */
public abstract class AbstractSerializer {


	protected abstract WriteToSerializerType getType();
	
	protected void _objToXml(OutputStream out, Class<?> c, Object object,
			boolean prettyPrint) throws Exception {
		if(object instanceof JAXBElement){
			// solo per il tipo WriteToSerializerType.JAXB
			JaxbUtils.objToXml(out, c, object, prettyPrint);
		}else{
			Method m = c.getMethod("writeTo", OutputStream.class, WriteToSerializerType.class);
			m.invoke(object, out, this.getType());
		}
	}
	
	protected void objToXml(OutputStream out,Class<?> c,Object object,boolean prettyPrint) throws SerializerException{
		try{
			this._objToXml(out, c, object, prettyPrint);
		}catch(Exception e){
			throw new SerializerException(e.getMessage(), e);
		}
		finally{
			try{
				out.flush();
			}catch(Exception e){}
		}
	}
	protected void objToXml(String fileName,Class<?> c,Object object,boolean prettyPrint) throws SerializerException{
		try{
			this.objToXml(new File(fileName), c, object, prettyPrint);
		}catch(Exception e){
			throw new SerializerException(e.getMessage(), e);
		}
	}
	protected void objToXml(File file,Class<?> c,Object object,boolean prettyPrint) throws SerializerException{
		FileOutputStream fout = null;
		try{
			fout = new FileOutputStream(file);
			this._objToXml(fout, c, object, prettyPrint);
		}catch(Exception e){
			throw new SerializerException(e.getMessage(), e);
		}
		finally{
			try{
				fout.flush();
			}catch(Exception e){}
			try{
				fout.close();
			}catch(Exception e){}
		}
	}
	protected ByteArrayOutputStream objToXml(Class<?> c,Object object,boolean prettyPrint) throws SerializerException{
		ByteArrayOutputStream bout = null;
		try{
			bout = new ByteArrayOutputStream();
			this._objToXml(bout, c, object, prettyPrint);
		}catch(Exception e){
			throw new SerializerException(e.getMessage(), e);
		}
		finally{
			try{
				bout.flush();
			}catch(Exception e){}
			try{
				bout.close();
			}catch(Exception e){}
		}
		return bout;
	}




	/*
	 =================================================================================
	 Object: TipoVersamento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>tipoVersamento</var> of type {@link it.govpay.orm.TipoVersamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>tipoVersamento</var>
	 * @param tipoVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,TipoVersamento tipoVersamento) throws SerializerException {
		this.objToXml(fileName, TipoVersamento.class, tipoVersamento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>tipoVersamento</var> of type {@link it.govpay.orm.TipoVersamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>tipoVersamento</var>
	 * @param tipoVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,TipoVersamento tipoVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, TipoVersamento.class, tipoVersamento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>tipoVersamento</var> of type {@link it.govpay.orm.TipoVersamento}
	 * 
	 * @param file Xml file to serialize the object <var>tipoVersamento</var>
	 * @param tipoVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,TipoVersamento tipoVersamento) throws SerializerException {
		this.objToXml(file, TipoVersamento.class, tipoVersamento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>tipoVersamento</var> of type {@link it.govpay.orm.TipoVersamento}
	 * 
	 * @param file Xml file to serialize the object <var>tipoVersamento</var>
	 * @param tipoVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,TipoVersamento tipoVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, TipoVersamento.class, tipoVersamento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>tipoVersamento</var> of type {@link it.govpay.orm.TipoVersamento}
	 * 
	 * @param out OutputStream to serialize the object <var>tipoVersamento</var>
	 * @param tipoVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,TipoVersamento tipoVersamento) throws SerializerException {
		this.objToXml(out, TipoVersamento.class, tipoVersamento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>tipoVersamento</var> of type {@link it.govpay.orm.TipoVersamento}
	 * 
	 * @param out OutputStream to serialize the object <var>tipoVersamento</var>
	 * @param tipoVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,TipoVersamento tipoVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, TipoVersamento.class, tipoVersamento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>tipoVersamento</var> of type {@link it.govpay.orm.TipoVersamento}
	 * 
	 * @param tipoVersamento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(TipoVersamento tipoVersamento) throws SerializerException {
		return this.objToXml(TipoVersamento.class, tipoVersamento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>tipoVersamento</var> of type {@link it.govpay.orm.TipoVersamento}
	 * 
	 * @param tipoVersamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(TipoVersamento tipoVersamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(TipoVersamento.class, tipoVersamento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>tipoVersamento</var> of type {@link it.govpay.orm.TipoVersamento}
	 * 
	 * @param tipoVersamento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(TipoVersamento tipoVersamento) throws SerializerException {
		return this.objToXml(TipoVersamento.class, tipoVersamento, false).toString();
	}
	/**
	 * Serialize to String the object <var>tipoVersamento</var> of type {@link it.govpay.orm.TipoVersamento}
	 * 
	 * @param tipoVersamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(TipoVersamento tipoVersamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(TipoVersamento.class, tipoVersamento, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: iuv-search
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>iuvSearch</var> of type {@link it.govpay.orm.IuvSearch}
	 * 
	 * @param fileName Xml file to serialize the object <var>iuvSearch</var>
	 * @param iuvSearch Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IuvSearch iuvSearch) throws SerializerException {
		this.objToXml(fileName, IuvSearch.class, iuvSearch, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>iuvSearch</var> of type {@link it.govpay.orm.IuvSearch}
	 * 
	 * @param fileName Xml file to serialize the object <var>iuvSearch</var>
	 * @param iuvSearch Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IuvSearch iuvSearch,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IuvSearch.class, iuvSearch, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>iuvSearch</var> of type {@link it.govpay.orm.IuvSearch}
	 * 
	 * @param file Xml file to serialize the object <var>iuvSearch</var>
	 * @param iuvSearch Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IuvSearch iuvSearch) throws SerializerException {
		this.objToXml(file, IuvSearch.class, iuvSearch, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>iuvSearch</var> of type {@link it.govpay.orm.IuvSearch}
	 * 
	 * @param file Xml file to serialize the object <var>iuvSearch</var>
	 * @param iuvSearch Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IuvSearch iuvSearch,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IuvSearch.class, iuvSearch, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>iuvSearch</var> of type {@link it.govpay.orm.IuvSearch}
	 * 
	 * @param out OutputStream to serialize the object <var>iuvSearch</var>
	 * @param iuvSearch Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IuvSearch iuvSearch) throws SerializerException {
		this.objToXml(out, IuvSearch.class, iuvSearch, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>iuvSearch</var> of type {@link it.govpay.orm.IuvSearch}
	 * 
	 * @param out OutputStream to serialize the object <var>iuvSearch</var>
	 * @param iuvSearch Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IuvSearch iuvSearch,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IuvSearch.class, iuvSearch, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>iuvSearch</var> of type {@link it.govpay.orm.IuvSearch}
	 * 
	 * @param iuvSearch Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IuvSearch iuvSearch) throws SerializerException {
		return this.objToXml(IuvSearch.class, iuvSearch, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>iuvSearch</var> of type {@link it.govpay.orm.IuvSearch}
	 * 
	 * @param iuvSearch Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IuvSearch iuvSearch,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IuvSearch.class, iuvSearch, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>iuvSearch</var> of type {@link it.govpay.orm.IuvSearch}
	 * 
	 * @param iuvSearch Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IuvSearch iuvSearch) throws SerializerException {
		return this.objToXml(IuvSearch.class, iuvSearch, false).toString();
	}
	/**
	 * Serialize to String the object <var>iuvSearch</var> of type {@link it.govpay.orm.IuvSearch}
	 * 
	 * @param iuvSearch Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IuvSearch iuvSearch,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IuvSearch.class, iuvSearch, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Audit
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>audit</var> of type {@link it.govpay.orm.Audit}
	 * 
	 * @param fileName Xml file to serialize the object <var>audit</var>
	 * @param audit Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Audit audit) throws SerializerException {
		this.objToXml(fileName, Audit.class, audit, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>audit</var> of type {@link it.govpay.orm.Audit}
	 * 
	 * @param fileName Xml file to serialize the object <var>audit</var>
	 * @param audit Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Audit audit,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Audit.class, audit, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>audit</var> of type {@link it.govpay.orm.Audit}
	 * 
	 * @param file Xml file to serialize the object <var>audit</var>
	 * @param audit Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Audit audit) throws SerializerException {
		this.objToXml(file, Audit.class, audit, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>audit</var> of type {@link it.govpay.orm.Audit}
	 * 
	 * @param file Xml file to serialize the object <var>audit</var>
	 * @param audit Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Audit audit,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Audit.class, audit, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>audit</var> of type {@link it.govpay.orm.Audit}
	 * 
	 * @param out OutputStream to serialize the object <var>audit</var>
	 * @param audit Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Audit audit) throws SerializerException {
		this.objToXml(out, Audit.class, audit, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>audit</var> of type {@link it.govpay.orm.Audit}
	 * 
	 * @param out OutputStream to serialize the object <var>audit</var>
	 * @param audit Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Audit audit,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Audit.class, audit, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>audit</var> of type {@link it.govpay.orm.Audit}
	 * 
	 * @param audit Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Audit audit) throws SerializerException {
		return this.objToXml(Audit.class, audit, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>audit</var> of type {@link it.govpay.orm.Audit}
	 * 
	 * @param audit Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Audit audit,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Audit.class, audit, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>audit</var> of type {@link it.govpay.orm.Audit}
	 * 
	 * @param audit Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Audit audit) throws SerializerException {
		return this.objToXml(Audit.class, audit, false).toString();
	}
	/**
	 * Serialize to String the object <var>audit</var> of type {@link it.govpay.orm.Audit}
	 * 
	 * @param audit Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Audit audit,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Audit.class, audit, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-operatore
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idOperatore</var> of type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param fileName Xml file to serialize the object <var>idOperatore</var>
	 * @param idOperatore Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdOperatore idOperatore) throws SerializerException {
		this.objToXml(fileName, IdOperatore.class, idOperatore, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idOperatore</var> of type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param fileName Xml file to serialize the object <var>idOperatore</var>
	 * @param idOperatore Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdOperatore idOperatore,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdOperatore.class, idOperatore, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idOperatore</var> of type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param file Xml file to serialize the object <var>idOperatore</var>
	 * @param idOperatore Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdOperatore idOperatore) throws SerializerException {
		this.objToXml(file, IdOperatore.class, idOperatore, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idOperatore</var> of type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param file Xml file to serialize the object <var>idOperatore</var>
	 * @param idOperatore Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdOperatore idOperatore,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdOperatore.class, idOperatore, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idOperatore</var> of type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param out OutputStream to serialize the object <var>idOperatore</var>
	 * @param idOperatore Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdOperatore idOperatore) throws SerializerException {
		this.objToXml(out, IdOperatore.class, idOperatore, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idOperatore</var> of type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param out OutputStream to serialize the object <var>idOperatore</var>
	 * @param idOperatore Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdOperatore idOperatore,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdOperatore.class, idOperatore, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idOperatore</var> of type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param idOperatore Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdOperatore idOperatore) throws SerializerException {
		return this.objToXml(IdOperatore.class, idOperatore, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idOperatore</var> of type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param idOperatore Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdOperatore idOperatore,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdOperatore.class, idOperatore, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idOperatore</var> of type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param idOperatore Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdOperatore idOperatore) throws SerializerException {
		return this.objToXml(IdOperatore.class, idOperatore, false).toString();
	}
	/**
	 * Serialize to String the object <var>idOperatore</var> of type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param idOperatore Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdOperatore idOperatore,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdOperatore.class, idOperatore, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-incasso
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idIncasso</var> of type {@link it.govpay.orm.IdIncasso}
	 * 
	 * @param fileName Xml file to serialize the object <var>idIncasso</var>
	 * @param idIncasso Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdIncasso idIncasso) throws SerializerException {
		this.objToXml(fileName, IdIncasso.class, idIncasso, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idIncasso</var> of type {@link it.govpay.orm.IdIncasso}
	 * 
	 * @param fileName Xml file to serialize the object <var>idIncasso</var>
	 * @param idIncasso Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdIncasso idIncasso,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdIncasso.class, idIncasso, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idIncasso</var> of type {@link it.govpay.orm.IdIncasso}
	 * 
	 * @param file Xml file to serialize the object <var>idIncasso</var>
	 * @param idIncasso Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdIncasso idIncasso) throws SerializerException {
		this.objToXml(file, IdIncasso.class, idIncasso, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idIncasso</var> of type {@link it.govpay.orm.IdIncasso}
	 * 
	 * @param file Xml file to serialize the object <var>idIncasso</var>
	 * @param idIncasso Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdIncasso idIncasso,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdIncasso.class, idIncasso, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idIncasso</var> of type {@link it.govpay.orm.IdIncasso}
	 * 
	 * @param out OutputStream to serialize the object <var>idIncasso</var>
	 * @param idIncasso Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdIncasso idIncasso) throws SerializerException {
		this.objToXml(out, IdIncasso.class, idIncasso, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idIncasso</var> of type {@link it.govpay.orm.IdIncasso}
	 * 
	 * @param out OutputStream to serialize the object <var>idIncasso</var>
	 * @param idIncasso Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdIncasso idIncasso,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdIncasso.class, idIncasso, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idIncasso</var> of type {@link it.govpay.orm.IdIncasso}
	 * 
	 * @param idIncasso Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdIncasso idIncasso) throws SerializerException {
		return this.objToXml(IdIncasso.class, idIncasso, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idIncasso</var> of type {@link it.govpay.orm.IdIncasso}
	 * 
	 * @param idIncasso Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdIncasso idIncasso,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdIncasso.class, idIncasso, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idIncasso</var> of type {@link it.govpay.orm.IdIncasso}
	 * 
	 * @param idIncasso Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdIncasso idIncasso) throws SerializerException {
		return this.objToXml(IdIncasso.class, idIncasso, false).toString();
	}
	/**
	 * Serialize to String the object <var>idIncasso</var> of type {@link it.govpay.orm.IdIncasso}
	 * 
	 * @param idIncasso Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdIncasso idIncasso,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdIncasso.class, idIncasso, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-tracciato
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idTracciato</var> of type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param fileName Xml file to serialize the object <var>idTracciato</var>
	 * @param idTracciato Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdTracciato idTracciato) throws SerializerException {
		this.objToXml(fileName, IdTracciato.class, idTracciato, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idTracciato</var> of type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param fileName Xml file to serialize the object <var>idTracciato</var>
	 * @param idTracciato Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdTracciato idTracciato,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdTracciato.class, idTracciato, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idTracciato</var> of type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param file Xml file to serialize the object <var>idTracciato</var>
	 * @param idTracciato Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdTracciato idTracciato) throws SerializerException {
		this.objToXml(file, IdTracciato.class, idTracciato, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idTracciato</var> of type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param file Xml file to serialize the object <var>idTracciato</var>
	 * @param idTracciato Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdTracciato idTracciato,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdTracciato.class, idTracciato, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idTracciato</var> of type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param out OutputStream to serialize the object <var>idTracciato</var>
	 * @param idTracciato Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdTracciato idTracciato) throws SerializerException {
		this.objToXml(out, IdTracciato.class, idTracciato, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idTracciato</var> of type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param out OutputStream to serialize the object <var>idTracciato</var>
	 * @param idTracciato Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdTracciato idTracciato,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdTracciato.class, idTracciato, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idTracciato</var> of type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param idTracciato Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdTracciato idTracciato) throws SerializerException {
		return this.objToXml(IdTracciato.class, idTracciato, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idTracciato</var> of type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param idTracciato Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdTracciato idTracciato,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdTracciato.class, idTracciato, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idTracciato</var> of type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param idTracciato Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdTracciato idTracciato) throws SerializerException {
		return this.objToXml(IdTracciato.class, idTracciato, false).toString();
	}
	/**
	 * Serialize to String the object <var>idTracciato</var> of type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param idTracciato Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdTracciato idTracciato,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdTracciato.class, idTracciato, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-operazione
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idOperazione</var> of type {@link it.govpay.orm.IdOperazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>idOperazione</var>
	 * @param idOperazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdOperazione idOperazione) throws SerializerException {
		this.objToXml(fileName, IdOperazione.class, idOperazione, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idOperazione</var> of type {@link it.govpay.orm.IdOperazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>idOperazione</var>
	 * @param idOperazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdOperazione idOperazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdOperazione.class, idOperazione, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idOperazione</var> of type {@link it.govpay.orm.IdOperazione}
	 * 
	 * @param file Xml file to serialize the object <var>idOperazione</var>
	 * @param idOperazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdOperazione idOperazione) throws SerializerException {
		this.objToXml(file, IdOperazione.class, idOperazione, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idOperazione</var> of type {@link it.govpay.orm.IdOperazione}
	 * 
	 * @param file Xml file to serialize the object <var>idOperazione</var>
	 * @param idOperazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdOperazione idOperazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdOperazione.class, idOperazione, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idOperazione</var> of type {@link it.govpay.orm.IdOperazione}
	 * 
	 * @param out OutputStream to serialize the object <var>idOperazione</var>
	 * @param idOperazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdOperazione idOperazione) throws SerializerException {
		this.objToXml(out, IdOperazione.class, idOperazione, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idOperazione</var> of type {@link it.govpay.orm.IdOperazione}
	 * 
	 * @param out OutputStream to serialize the object <var>idOperazione</var>
	 * @param idOperazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdOperazione idOperazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdOperazione.class, idOperazione, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idOperazione</var> of type {@link it.govpay.orm.IdOperazione}
	 * 
	 * @param idOperazione Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdOperazione idOperazione) throws SerializerException {
		return this.objToXml(IdOperazione.class, idOperazione, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idOperazione</var> of type {@link it.govpay.orm.IdOperazione}
	 * 
	 * @param idOperazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdOperazione idOperazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdOperazione.class, idOperazione, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idOperazione</var> of type {@link it.govpay.orm.IdOperazione}
	 * 
	 * @param idOperazione Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdOperazione idOperazione) throws SerializerException {
		return this.objToXml(IdOperazione.class, idOperazione, false).toString();
	}
	/**
	 * Serialize to String the object <var>idOperazione</var> of type {@link it.govpay.orm.IdOperazione}
	 * 
	 * @param idOperazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdOperazione idOperazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdOperazione.class, idOperazione, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: FR
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>fr</var> of type {@link it.govpay.orm.FR}
	 * 
	 * @param fileName Xml file to serialize the object <var>fr</var>
	 * @param fr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,FR fr) throws SerializerException {
		this.objToXml(fileName, FR.class, fr, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>fr</var> of type {@link it.govpay.orm.FR}
	 * 
	 * @param fileName Xml file to serialize the object <var>fr</var>
	 * @param fr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,FR fr,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, FR.class, fr, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>fr</var> of type {@link it.govpay.orm.FR}
	 * 
	 * @param file Xml file to serialize the object <var>fr</var>
	 * @param fr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,FR fr) throws SerializerException {
		this.objToXml(file, FR.class, fr, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>fr</var> of type {@link it.govpay.orm.FR}
	 * 
	 * @param file Xml file to serialize the object <var>fr</var>
	 * @param fr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,FR fr,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, FR.class, fr, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>fr</var> of type {@link it.govpay.orm.FR}
	 * 
	 * @param out OutputStream to serialize the object <var>fr</var>
	 * @param fr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,FR fr) throws SerializerException {
		this.objToXml(out, FR.class, fr, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>fr</var> of type {@link it.govpay.orm.FR}
	 * 
	 * @param out OutputStream to serialize the object <var>fr</var>
	 * @param fr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,FR fr,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, FR.class, fr, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>fr</var> of type {@link it.govpay.orm.FR}
	 * 
	 * @param fr Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(FR fr) throws SerializerException {
		return this.objToXml(FR.class, fr, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>fr</var> of type {@link it.govpay.orm.FR}
	 * 
	 * @param fr Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(FR fr,boolean prettyPrint) throws SerializerException {
		return this.objToXml(FR.class, fr, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>fr</var> of type {@link it.govpay.orm.FR}
	 * 
	 * @param fr Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(FR fr) throws SerializerException {
		return this.objToXml(FR.class, fr, false).toString();
	}
	/**
	 * Serialize to String the object <var>fr</var> of type {@link it.govpay.orm.FR}
	 * 
	 * @param fr Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(FR fr,boolean prettyPrint) throws SerializerException {
		return this.objToXml(FR.class, fr, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Rendicontazione
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>rendicontazione</var> of type {@link it.govpay.orm.Rendicontazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>rendicontazione</var>
	 * @param rendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Rendicontazione rendicontazione) throws SerializerException {
		this.objToXml(fileName, Rendicontazione.class, rendicontazione, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>rendicontazione</var> of type {@link it.govpay.orm.Rendicontazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>rendicontazione</var>
	 * @param rendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Rendicontazione rendicontazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Rendicontazione.class, rendicontazione, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>rendicontazione</var> of type {@link it.govpay.orm.Rendicontazione}
	 * 
	 * @param file Xml file to serialize the object <var>rendicontazione</var>
	 * @param rendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Rendicontazione rendicontazione) throws SerializerException {
		this.objToXml(file, Rendicontazione.class, rendicontazione, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>rendicontazione</var> of type {@link it.govpay.orm.Rendicontazione}
	 * 
	 * @param file Xml file to serialize the object <var>rendicontazione</var>
	 * @param rendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Rendicontazione rendicontazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Rendicontazione.class, rendicontazione, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>rendicontazione</var> of type {@link it.govpay.orm.Rendicontazione}
	 * 
	 * @param out OutputStream to serialize the object <var>rendicontazione</var>
	 * @param rendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Rendicontazione rendicontazione) throws SerializerException {
		this.objToXml(out, Rendicontazione.class, rendicontazione, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>rendicontazione</var> of type {@link it.govpay.orm.Rendicontazione}
	 * 
	 * @param out OutputStream to serialize the object <var>rendicontazione</var>
	 * @param rendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Rendicontazione rendicontazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Rendicontazione.class, rendicontazione, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>rendicontazione</var> of type {@link it.govpay.orm.Rendicontazione}
	 * 
	 * @param rendicontazione Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Rendicontazione rendicontazione) throws SerializerException {
		return this.objToXml(Rendicontazione.class, rendicontazione, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>rendicontazione</var> of type {@link it.govpay.orm.Rendicontazione}
	 * 
	 * @param rendicontazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Rendicontazione rendicontazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Rendicontazione.class, rendicontazione, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>rendicontazione</var> of type {@link it.govpay.orm.Rendicontazione}
	 * 
	 * @param rendicontazione Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Rendicontazione rendicontazione) throws SerializerException {
		return this.objToXml(Rendicontazione.class, rendicontazione, false).toString();
	}
	/**
	 * Serialize to String the object <var>rendicontazione</var> of type {@link it.govpay.orm.Rendicontazione}
	 * 
	 * @param rendicontazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Rendicontazione rendicontazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Rendicontazione.class, rendicontazione, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Pagamento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>pagamento</var> of type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>pagamento</var>
	 * @param pagamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Pagamento pagamento) throws SerializerException {
		this.objToXml(fileName, Pagamento.class, pagamento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>pagamento</var> of type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>pagamento</var>
	 * @param pagamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Pagamento pagamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Pagamento.class, pagamento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>pagamento</var> of type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param file Xml file to serialize the object <var>pagamento</var>
	 * @param pagamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Pagamento pagamento) throws SerializerException {
		this.objToXml(file, Pagamento.class, pagamento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>pagamento</var> of type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param file Xml file to serialize the object <var>pagamento</var>
	 * @param pagamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Pagamento pagamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Pagamento.class, pagamento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>pagamento</var> of type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param out OutputStream to serialize the object <var>pagamento</var>
	 * @param pagamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Pagamento pagamento) throws SerializerException {
		this.objToXml(out, Pagamento.class, pagamento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>pagamento</var> of type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param out OutputStream to serialize the object <var>pagamento</var>
	 * @param pagamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Pagamento pagamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Pagamento.class, pagamento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>pagamento</var> of type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param pagamento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Pagamento pagamento) throws SerializerException {
		return this.objToXml(Pagamento.class, pagamento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>pagamento</var> of type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param pagamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Pagamento pagamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Pagamento.class, pagamento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>pagamento</var> of type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param pagamento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Pagamento pagamento) throws SerializerException {
		return this.objToXml(Pagamento.class, pagamento, false).toString();
	}
	/**
	 * Serialize to String the object <var>pagamento</var> of type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param pagamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Pagamento pagamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Pagamento.class, pagamento, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: SingoloVersamento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>singoloVersamento</var> of type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>singoloVersamento</var>
	 * @param singoloVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,SingoloVersamento singoloVersamento) throws SerializerException {
		this.objToXml(fileName, SingoloVersamento.class, singoloVersamento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>singoloVersamento</var> of type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>singoloVersamento</var>
	 * @param singoloVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,SingoloVersamento singoloVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, SingoloVersamento.class, singoloVersamento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>singoloVersamento</var> of type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param file Xml file to serialize the object <var>singoloVersamento</var>
	 * @param singoloVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,SingoloVersamento singoloVersamento) throws SerializerException {
		this.objToXml(file, SingoloVersamento.class, singoloVersamento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>singoloVersamento</var> of type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param file Xml file to serialize the object <var>singoloVersamento</var>
	 * @param singoloVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,SingoloVersamento singoloVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, SingoloVersamento.class, singoloVersamento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>singoloVersamento</var> of type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param out OutputStream to serialize the object <var>singoloVersamento</var>
	 * @param singoloVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,SingoloVersamento singoloVersamento) throws SerializerException {
		this.objToXml(out, SingoloVersamento.class, singoloVersamento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>singoloVersamento</var> of type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param out OutputStream to serialize the object <var>singoloVersamento</var>
	 * @param singoloVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,SingoloVersamento singoloVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, SingoloVersamento.class, singoloVersamento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>singoloVersamento</var> of type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param singoloVersamento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(SingoloVersamento singoloVersamento) throws SerializerException {
		return this.objToXml(SingoloVersamento.class, singoloVersamento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>singoloVersamento</var> of type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param singoloVersamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(SingoloVersamento singoloVersamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(SingoloVersamento.class, singoloVersamento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>singoloVersamento</var> of type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param singoloVersamento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(SingoloVersamento singoloVersamento) throws SerializerException {
		return this.objToXml(SingoloVersamento.class, singoloVersamento, false).toString();
	}
	/**
	 * Serialize to String the object <var>singoloVersamento</var> of type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param singoloVersamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(SingoloVersamento singoloVersamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(SingoloVersamento.class, singoloVersamento, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Versamento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>versamento</var> of type {@link it.govpay.orm.Versamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>versamento</var>
	 * @param versamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Versamento versamento) throws SerializerException {
		this.objToXml(fileName, Versamento.class, versamento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>versamento</var> of type {@link it.govpay.orm.Versamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>versamento</var>
	 * @param versamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Versamento versamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Versamento.class, versamento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>versamento</var> of type {@link it.govpay.orm.Versamento}
	 * 
	 * @param file Xml file to serialize the object <var>versamento</var>
	 * @param versamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Versamento versamento) throws SerializerException {
		this.objToXml(file, Versamento.class, versamento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>versamento</var> of type {@link it.govpay.orm.Versamento}
	 * 
	 * @param file Xml file to serialize the object <var>versamento</var>
	 * @param versamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Versamento versamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Versamento.class, versamento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>versamento</var> of type {@link it.govpay.orm.Versamento}
	 * 
	 * @param out OutputStream to serialize the object <var>versamento</var>
	 * @param versamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Versamento versamento) throws SerializerException {
		this.objToXml(out, Versamento.class, versamento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>versamento</var> of type {@link it.govpay.orm.Versamento}
	 * 
	 * @param out OutputStream to serialize the object <var>versamento</var>
	 * @param versamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Versamento versamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Versamento.class, versamento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>versamento</var> of type {@link it.govpay.orm.Versamento}
	 * 
	 * @param versamento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Versamento versamento) throws SerializerException {
		return this.objToXml(Versamento.class, versamento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>versamento</var> of type {@link it.govpay.orm.Versamento}
	 * 
	 * @param versamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Versamento versamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Versamento.class, versamento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>versamento</var> of type {@link it.govpay.orm.Versamento}
	 * 
	 * @param versamento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Versamento versamento) throws SerializerException {
		return this.objToXml(Versamento.class, versamento, false).toString();
	}
	/**
	 * Serialize to String the object <var>versamento</var> of type {@link it.govpay.orm.Versamento}
	 * 
	 * @param versamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Versamento versamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Versamento.class, versamento, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-pagamento-portale
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idPagamentoPortale</var> of type {@link it.govpay.orm.IdPagamentoPortale}
	 * 
	 * @param fileName Xml file to serialize the object <var>idPagamentoPortale</var>
	 * @param idPagamentoPortale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdPagamentoPortale idPagamentoPortale) throws SerializerException {
		this.objToXml(fileName, IdPagamentoPortale.class, idPagamentoPortale, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idPagamentoPortale</var> of type {@link it.govpay.orm.IdPagamentoPortale}
	 * 
	 * @param fileName Xml file to serialize the object <var>idPagamentoPortale</var>
	 * @param idPagamentoPortale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdPagamentoPortale idPagamentoPortale,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdPagamentoPortale.class, idPagamentoPortale, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idPagamentoPortale</var> of type {@link it.govpay.orm.IdPagamentoPortale}
	 * 
	 * @param file Xml file to serialize the object <var>idPagamentoPortale</var>
	 * @param idPagamentoPortale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdPagamentoPortale idPagamentoPortale) throws SerializerException {
		this.objToXml(file, IdPagamentoPortale.class, idPagamentoPortale, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idPagamentoPortale</var> of type {@link it.govpay.orm.IdPagamentoPortale}
	 * 
	 * @param file Xml file to serialize the object <var>idPagamentoPortale</var>
	 * @param idPagamentoPortale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdPagamentoPortale idPagamentoPortale,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdPagamentoPortale.class, idPagamentoPortale, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idPagamentoPortale</var> of type {@link it.govpay.orm.IdPagamentoPortale}
	 * 
	 * @param out OutputStream to serialize the object <var>idPagamentoPortale</var>
	 * @param idPagamentoPortale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdPagamentoPortale idPagamentoPortale) throws SerializerException {
		this.objToXml(out, IdPagamentoPortale.class, idPagamentoPortale, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idPagamentoPortale</var> of type {@link it.govpay.orm.IdPagamentoPortale}
	 * 
	 * @param out OutputStream to serialize the object <var>idPagamentoPortale</var>
	 * @param idPagamentoPortale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdPagamentoPortale idPagamentoPortale,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdPagamentoPortale.class, idPagamentoPortale, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idPagamentoPortale</var> of type {@link it.govpay.orm.IdPagamentoPortale}
	 * 
	 * @param idPagamentoPortale Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdPagamentoPortale idPagamentoPortale) throws SerializerException {
		return this.objToXml(IdPagamentoPortale.class, idPagamentoPortale, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idPagamentoPortale</var> of type {@link it.govpay.orm.IdPagamentoPortale}
	 * 
	 * @param idPagamentoPortale Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdPagamentoPortale idPagamentoPortale,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdPagamentoPortale.class, idPagamentoPortale, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idPagamentoPortale</var> of type {@link it.govpay.orm.IdPagamentoPortale}
	 * 
	 * @param idPagamentoPortale Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdPagamentoPortale idPagamentoPortale) throws SerializerException {
		return this.objToXml(IdPagamentoPortale.class, idPagamentoPortale, false).toString();
	}
	/**
	 * Serialize to String the object <var>idPagamentoPortale</var> of type {@link it.govpay.orm.IdPagamentoPortale}
	 * 
	 * @param idPagamentoPortale Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdPagamentoPortale idPagamentoPortale,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdPagamentoPortale.class, idPagamentoPortale, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: VistaRptVersamento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>vistaRptVersamento</var> of type {@link it.govpay.orm.VistaRptVersamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>vistaRptVersamento</var>
	 * @param vistaRptVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,VistaRptVersamento vistaRptVersamento) throws SerializerException {
		this.objToXml(fileName, VistaRptVersamento.class, vistaRptVersamento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>vistaRptVersamento</var> of type {@link it.govpay.orm.VistaRptVersamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>vistaRptVersamento</var>
	 * @param vistaRptVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,VistaRptVersamento vistaRptVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, VistaRptVersamento.class, vistaRptVersamento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>vistaRptVersamento</var> of type {@link it.govpay.orm.VistaRptVersamento}
	 * 
	 * @param file Xml file to serialize the object <var>vistaRptVersamento</var>
	 * @param vistaRptVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,VistaRptVersamento vistaRptVersamento) throws SerializerException {
		this.objToXml(file, VistaRptVersamento.class, vistaRptVersamento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>vistaRptVersamento</var> of type {@link it.govpay.orm.VistaRptVersamento}
	 * 
	 * @param file Xml file to serialize the object <var>vistaRptVersamento</var>
	 * @param vistaRptVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,VistaRptVersamento vistaRptVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, VistaRptVersamento.class, vistaRptVersamento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>vistaRptVersamento</var> of type {@link it.govpay.orm.VistaRptVersamento}
	 * 
	 * @param out OutputStream to serialize the object <var>vistaRptVersamento</var>
	 * @param vistaRptVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,VistaRptVersamento vistaRptVersamento) throws SerializerException {
		this.objToXml(out, VistaRptVersamento.class, vistaRptVersamento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>vistaRptVersamento</var> of type {@link it.govpay.orm.VistaRptVersamento}
	 * 
	 * @param out OutputStream to serialize the object <var>vistaRptVersamento</var>
	 * @param vistaRptVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,VistaRptVersamento vistaRptVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, VistaRptVersamento.class, vistaRptVersamento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>vistaRptVersamento</var> of type {@link it.govpay.orm.VistaRptVersamento}
	 * 
	 * @param vistaRptVersamento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(VistaRptVersamento vistaRptVersamento) throws SerializerException {
		return this.objToXml(VistaRptVersamento.class, vistaRptVersamento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>vistaRptVersamento</var> of type {@link it.govpay.orm.VistaRptVersamento}
	 * 
	 * @param vistaRptVersamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(VistaRptVersamento vistaRptVersamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(VistaRptVersamento.class, vistaRptVersamento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>vistaRptVersamento</var> of type {@link it.govpay.orm.VistaRptVersamento}
	 * 
	 * @param vistaRptVersamento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(VistaRptVersamento vistaRptVersamento) throws SerializerException {
		return this.objToXml(VistaRptVersamento.class, vistaRptVersamento, false).toString();
	}
	/**
	 * Serialize to String the object <var>vistaRptVersamento</var> of type {@link it.govpay.orm.VistaRptVersamento}
	 * 
	 * @param vistaRptVersamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(VistaRptVersamento vistaRptVersamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(VistaRptVersamento.class, vistaRptVersamento, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-tipo-versamento-dominio
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idTipoVersamentoDominio</var> of type {@link it.govpay.orm.IdTipoVersamentoDominio}
	 * 
	 * @param fileName Xml file to serialize the object <var>idTipoVersamentoDominio</var>
	 * @param idTipoVersamentoDominio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdTipoVersamentoDominio idTipoVersamentoDominio) throws SerializerException {
		this.objToXml(fileName, IdTipoVersamentoDominio.class, idTipoVersamentoDominio, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idTipoVersamentoDominio</var> of type {@link it.govpay.orm.IdTipoVersamentoDominio}
	 * 
	 * @param fileName Xml file to serialize the object <var>idTipoVersamentoDominio</var>
	 * @param idTipoVersamentoDominio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdTipoVersamentoDominio idTipoVersamentoDominio,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdTipoVersamentoDominio.class, idTipoVersamentoDominio, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idTipoVersamentoDominio</var> of type {@link it.govpay.orm.IdTipoVersamentoDominio}
	 * 
	 * @param file Xml file to serialize the object <var>idTipoVersamentoDominio</var>
	 * @param idTipoVersamentoDominio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdTipoVersamentoDominio idTipoVersamentoDominio) throws SerializerException {
		this.objToXml(file, IdTipoVersamentoDominio.class, idTipoVersamentoDominio, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idTipoVersamentoDominio</var> of type {@link it.govpay.orm.IdTipoVersamentoDominio}
	 * 
	 * @param file Xml file to serialize the object <var>idTipoVersamentoDominio</var>
	 * @param idTipoVersamentoDominio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdTipoVersamentoDominio idTipoVersamentoDominio,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdTipoVersamentoDominio.class, idTipoVersamentoDominio, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idTipoVersamentoDominio</var> of type {@link it.govpay.orm.IdTipoVersamentoDominio}
	 * 
	 * @param out OutputStream to serialize the object <var>idTipoVersamentoDominio</var>
	 * @param idTipoVersamentoDominio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdTipoVersamentoDominio idTipoVersamentoDominio) throws SerializerException {
		this.objToXml(out, IdTipoVersamentoDominio.class, idTipoVersamentoDominio, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idTipoVersamentoDominio</var> of type {@link it.govpay.orm.IdTipoVersamentoDominio}
	 * 
	 * @param out OutputStream to serialize the object <var>idTipoVersamentoDominio</var>
	 * @param idTipoVersamentoDominio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdTipoVersamentoDominio idTipoVersamentoDominio,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdTipoVersamentoDominio.class, idTipoVersamentoDominio, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idTipoVersamentoDominio</var> of type {@link it.govpay.orm.IdTipoVersamentoDominio}
	 * 
	 * @param idTipoVersamentoDominio Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdTipoVersamentoDominio idTipoVersamentoDominio) throws SerializerException {
		return this.objToXml(IdTipoVersamentoDominio.class, idTipoVersamentoDominio, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idTipoVersamentoDominio</var> of type {@link it.govpay.orm.IdTipoVersamentoDominio}
	 * 
	 * @param idTipoVersamentoDominio Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdTipoVersamentoDominio idTipoVersamentoDominio,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdTipoVersamentoDominio.class, idTipoVersamentoDominio, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idTipoVersamentoDominio</var> of type {@link it.govpay.orm.IdTipoVersamentoDominio}
	 * 
	 * @param idTipoVersamentoDominio Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdTipoVersamentoDominio idTipoVersamentoDominio) throws SerializerException {
		return this.objToXml(IdTipoVersamentoDominio.class, idTipoVersamentoDominio, false).toString();
	}
	/**
	 * Serialize to String the object <var>idTipoVersamentoDominio</var> of type {@link it.govpay.orm.IdTipoVersamentoDominio}
	 * 
	 * @param idTipoVersamentoDominio Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdTipoVersamentoDominio idTipoVersamentoDominio,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdTipoVersamentoDominio.class, idTipoVersamentoDominio, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-tipo-versamento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idTipoVersamento</var> of type {@link it.govpay.orm.IdTipoVersamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>idTipoVersamento</var>
	 * @param idTipoVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdTipoVersamento idTipoVersamento) throws SerializerException {
		this.objToXml(fileName, IdTipoVersamento.class, idTipoVersamento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idTipoVersamento</var> of type {@link it.govpay.orm.IdTipoVersamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>idTipoVersamento</var>
	 * @param idTipoVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdTipoVersamento idTipoVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdTipoVersamento.class, idTipoVersamento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idTipoVersamento</var> of type {@link it.govpay.orm.IdTipoVersamento}
	 * 
	 * @param file Xml file to serialize the object <var>idTipoVersamento</var>
	 * @param idTipoVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdTipoVersamento idTipoVersamento) throws SerializerException {
		this.objToXml(file, IdTipoVersamento.class, idTipoVersamento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idTipoVersamento</var> of type {@link it.govpay.orm.IdTipoVersamento}
	 * 
	 * @param file Xml file to serialize the object <var>idTipoVersamento</var>
	 * @param idTipoVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdTipoVersamento idTipoVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdTipoVersamento.class, idTipoVersamento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idTipoVersamento</var> of type {@link it.govpay.orm.IdTipoVersamento}
	 * 
	 * @param out OutputStream to serialize the object <var>idTipoVersamento</var>
	 * @param idTipoVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdTipoVersamento idTipoVersamento) throws SerializerException {
		this.objToXml(out, IdTipoVersamento.class, idTipoVersamento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idTipoVersamento</var> of type {@link it.govpay.orm.IdTipoVersamento}
	 * 
	 * @param out OutputStream to serialize the object <var>idTipoVersamento</var>
	 * @param idTipoVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdTipoVersamento idTipoVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdTipoVersamento.class, idTipoVersamento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idTipoVersamento</var> of type {@link it.govpay.orm.IdTipoVersamento}
	 * 
	 * @param idTipoVersamento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdTipoVersamento idTipoVersamento) throws SerializerException {
		return this.objToXml(IdTipoVersamento.class, idTipoVersamento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idTipoVersamento</var> of type {@link it.govpay.orm.IdTipoVersamento}
	 * 
	 * @param idTipoVersamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdTipoVersamento idTipoVersamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdTipoVersamento.class, idTipoVersamento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idTipoVersamento</var> of type {@link it.govpay.orm.IdTipoVersamento}
	 * 
	 * @param idTipoVersamento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdTipoVersamento idTipoVersamento) throws SerializerException {
		return this.objToXml(IdTipoVersamento.class, idTipoVersamento, false).toString();
	}
	/**
	 * Serialize to String the object <var>idTipoVersamento</var> of type {@link it.govpay.orm.IdTipoVersamento}
	 * 
	 * @param idTipoVersamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdTipoVersamento idTipoVersamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdTipoVersamento.class, idTipoVersamento, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-dominio
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idDominio</var> of type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param fileName Xml file to serialize the object <var>idDominio</var>
	 * @param idDominio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdDominio idDominio) throws SerializerException {
		this.objToXml(fileName, IdDominio.class, idDominio, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idDominio</var> of type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param fileName Xml file to serialize the object <var>idDominio</var>
	 * @param idDominio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdDominio idDominio,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdDominio.class, idDominio, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idDominio</var> of type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param file Xml file to serialize the object <var>idDominio</var>
	 * @param idDominio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdDominio idDominio) throws SerializerException {
		this.objToXml(file, IdDominio.class, idDominio, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idDominio</var> of type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param file Xml file to serialize the object <var>idDominio</var>
	 * @param idDominio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdDominio idDominio,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdDominio.class, idDominio, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idDominio</var> of type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param out OutputStream to serialize the object <var>idDominio</var>
	 * @param idDominio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdDominio idDominio) throws SerializerException {
		this.objToXml(out, IdDominio.class, idDominio, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idDominio</var> of type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param out OutputStream to serialize the object <var>idDominio</var>
	 * @param idDominio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdDominio idDominio,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdDominio.class, idDominio, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idDominio</var> of type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param idDominio Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdDominio idDominio) throws SerializerException {
		return this.objToXml(IdDominio.class, idDominio, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idDominio</var> of type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param idDominio Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdDominio idDominio,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdDominio.class, idDominio, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idDominio</var> of type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param idDominio Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdDominio idDominio) throws SerializerException {
		return this.objToXml(IdDominio.class, idDominio, false).toString();
	}
	/**
	 * Serialize to String the object <var>idDominio</var> of type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param idDominio Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdDominio idDominio,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdDominio.class, idDominio, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-uo
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idUo</var> of type {@link it.govpay.orm.IdUo}
	 * 
	 * @param fileName Xml file to serialize the object <var>idUo</var>
	 * @param idUo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdUo idUo) throws SerializerException {
		this.objToXml(fileName, IdUo.class, idUo, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idUo</var> of type {@link it.govpay.orm.IdUo}
	 * 
	 * @param fileName Xml file to serialize the object <var>idUo</var>
	 * @param idUo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdUo idUo,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdUo.class, idUo, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idUo</var> of type {@link it.govpay.orm.IdUo}
	 * 
	 * @param file Xml file to serialize the object <var>idUo</var>
	 * @param idUo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdUo idUo) throws SerializerException {
		this.objToXml(file, IdUo.class, idUo, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idUo</var> of type {@link it.govpay.orm.IdUo}
	 * 
	 * @param file Xml file to serialize the object <var>idUo</var>
	 * @param idUo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdUo idUo,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdUo.class, idUo, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idUo</var> of type {@link it.govpay.orm.IdUo}
	 * 
	 * @param out OutputStream to serialize the object <var>idUo</var>
	 * @param idUo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdUo idUo) throws SerializerException {
		this.objToXml(out, IdUo.class, idUo, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idUo</var> of type {@link it.govpay.orm.IdUo}
	 * 
	 * @param out OutputStream to serialize the object <var>idUo</var>
	 * @param idUo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdUo idUo,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdUo.class, idUo, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idUo</var> of type {@link it.govpay.orm.IdUo}
	 * 
	 * @param idUo Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdUo idUo) throws SerializerException {
		return this.objToXml(IdUo.class, idUo, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idUo</var> of type {@link it.govpay.orm.IdUo}
	 * 
	 * @param idUo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdUo idUo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdUo.class, idUo, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idUo</var> of type {@link it.govpay.orm.IdUo}
	 * 
	 * @param idUo Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdUo idUo) throws SerializerException {
		return this.objToXml(IdUo.class, idUo, false).toString();
	}
	/**
	 * Serialize to String the object <var>idUo</var> of type {@link it.govpay.orm.IdUo}
	 * 
	 * @param idUo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdUo idUo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdUo.class, idUo, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-applicazione
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idApplicazione</var> of type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>idApplicazione</var>
	 * @param idApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdApplicazione idApplicazione) throws SerializerException {
		this.objToXml(fileName, IdApplicazione.class, idApplicazione, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idApplicazione</var> of type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>idApplicazione</var>
	 * @param idApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdApplicazione idApplicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdApplicazione.class, idApplicazione, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idApplicazione</var> of type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param file Xml file to serialize the object <var>idApplicazione</var>
	 * @param idApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdApplicazione idApplicazione) throws SerializerException {
		this.objToXml(file, IdApplicazione.class, idApplicazione, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idApplicazione</var> of type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param file Xml file to serialize the object <var>idApplicazione</var>
	 * @param idApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdApplicazione idApplicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdApplicazione.class, idApplicazione, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idApplicazione</var> of type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param out OutputStream to serialize the object <var>idApplicazione</var>
	 * @param idApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdApplicazione idApplicazione) throws SerializerException {
		this.objToXml(out, IdApplicazione.class, idApplicazione, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idApplicazione</var> of type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param out OutputStream to serialize the object <var>idApplicazione</var>
	 * @param idApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdApplicazione idApplicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdApplicazione.class, idApplicazione, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idApplicazione</var> of type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param idApplicazione Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdApplicazione idApplicazione) throws SerializerException {
		return this.objToXml(IdApplicazione.class, idApplicazione, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idApplicazione</var> of type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param idApplicazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdApplicazione idApplicazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdApplicazione.class, idApplicazione, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idApplicazione</var> of type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param idApplicazione Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdApplicazione idApplicazione) throws SerializerException {
		return this.objToXml(IdApplicazione.class, idApplicazione, false).toString();
	}
	/**
	 * Serialize to String the object <var>idApplicazione</var> of type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param idApplicazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdApplicazione idApplicazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdApplicazione.class, idApplicazione, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-documento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idDocumento</var> of type {@link it.govpay.orm.IdDocumento}
	 * 
	 * @param fileName Xml file to serialize the object <var>idDocumento</var>
	 * @param idDocumento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdDocumento idDocumento) throws SerializerException {
		this.objToXml(fileName, IdDocumento.class, idDocumento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idDocumento</var> of type {@link it.govpay.orm.IdDocumento}
	 * 
	 * @param fileName Xml file to serialize the object <var>idDocumento</var>
	 * @param idDocumento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdDocumento idDocumento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdDocumento.class, idDocumento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idDocumento</var> of type {@link it.govpay.orm.IdDocumento}
	 * 
	 * @param file Xml file to serialize the object <var>idDocumento</var>
	 * @param idDocumento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdDocumento idDocumento) throws SerializerException {
		this.objToXml(file, IdDocumento.class, idDocumento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idDocumento</var> of type {@link it.govpay.orm.IdDocumento}
	 * 
	 * @param file Xml file to serialize the object <var>idDocumento</var>
	 * @param idDocumento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdDocumento idDocumento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdDocumento.class, idDocumento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idDocumento</var> of type {@link it.govpay.orm.IdDocumento}
	 * 
	 * @param out OutputStream to serialize the object <var>idDocumento</var>
	 * @param idDocumento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdDocumento idDocumento) throws SerializerException {
		this.objToXml(out, IdDocumento.class, idDocumento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idDocumento</var> of type {@link it.govpay.orm.IdDocumento}
	 * 
	 * @param out OutputStream to serialize the object <var>idDocumento</var>
	 * @param idDocumento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdDocumento idDocumento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdDocumento.class, idDocumento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idDocumento</var> of type {@link it.govpay.orm.IdDocumento}
	 * 
	 * @param idDocumento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdDocumento idDocumento) throws SerializerException {
		return this.objToXml(IdDocumento.class, idDocumento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idDocumento</var> of type {@link it.govpay.orm.IdDocumento}
	 * 
	 * @param idDocumento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdDocumento idDocumento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdDocumento.class, idDocumento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idDocumento</var> of type {@link it.govpay.orm.IdDocumento}
	 * 
	 * @param idDocumento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdDocumento idDocumento) throws SerializerException {
		return this.objToXml(IdDocumento.class, idDocumento, false).toString();
	}
	/**
	 * Serialize to String the object <var>idDocumento</var> of type {@link it.govpay.orm.IdDocumento}
	 * 
	 * @param idDocumento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdDocumento idDocumento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdDocumento.class, idDocumento, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-versamento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idVersamento</var> of type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>idVersamento</var>
	 * @param idVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdVersamento idVersamento) throws SerializerException {
		this.objToXml(fileName, IdVersamento.class, idVersamento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idVersamento</var> of type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>idVersamento</var>
	 * @param idVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdVersamento idVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdVersamento.class, idVersamento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idVersamento</var> of type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param file Xml file to serialize the object <var>idVersamento</var>
	 * @param idVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdVersamento idVersamento) throws SerializerException {
		this.objToXml(file, IdVersamento.class, idVersamento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idVersamento</var> of type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param file Xml file to serialize the object <var>idVersamento</var>
	 * @param idVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdVersamento idVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdVersamento.class, idVersamento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idVersamento</var> of type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param out OutputStream to serialize the object <var>idVersamento</var>
	 * @param idVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdVersamento idVersamento) throws SerializerException {
		this.objToXml(out, IdVersamento.class, idVersamento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idVersamento</var> of type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param out OutputStream to serialize the object <var>idVersamento</var>
	 * @param idVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdVersamento idVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdVersamento.class, idVersamento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idVersamento</var> of type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param idVersamento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdVersamento idVersamento) throws SerializerException {
		return this.objToXml(IdVersamento.class, idVersamento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idVersamento</var> of type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param idVersamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdVersamento idVersamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdVersamento.class, idVersamento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idVersamento</var> of type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param idVersamento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdVersamento idVersamento) throws SerializerException {
		return this.objToXml(IdVersamento.class, idVersamento, false).toString();
	}
	/**
	 * Serialize to String the object <var>idVersamento</var> of type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param idVersamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdVersamento idVersamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdVersamento.class, idVersamento, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Promemoria
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>promemoria</var> of type {@link it.govpay.orm.Promemoria}
	 * 
	 * @param fileName Xml file to serialize the object <var>promemoria</var>
	 * @param promemoria Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Promemoria promemoria) throws SerializerException {
		this.objToXml(fileName, Promemoria.class, promemoria, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>promemoria</var> of type {@link it.govpay.orm.Promemoria}
	 * 
	 * @param fileName Xml file to serialize the object <var>promemoria</var>
	 * @param promemoria Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Promemoria promemoria,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Promemoria.class, promemoria, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>promemoria</var> of type {@link it.govpay.orm.Promemoria}
	 * 
	 * @param file Xml file to serialize the object <var>promemoria</var>
	 * @param promemoria Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Promemoria promemoria) throws SerializerException {
		this.objToXml(file, Promemoria.class, promemoria, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>promemoria</var> of type {@link it.govpay.orm.Promemoria}
	 * 
	 * @param file Xml file to serialize the object <var>promemoria</var>
	 * @param promemoria Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Promemoria promemoria,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Promemoria.class, promemoria, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>promemoria</var> of type {@link it.govpay.orm.Promemoria}
	 * 
	 * @param out OutputStream to serialize the object <var>promemoria</var>
	 * @param promemoria Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Promemoria promemoria) throws SerializerException {
		this.objToXml(out, Promemoria.class, promemoria, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>promemoria</var> of type {@link it.govpay.orm.Promemoria}
	 * 
	 * @param out OutputStream to serialize the object <var>promemoria</var>
	 * @param promemoria Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Promemoria promemoria,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Promemoria.class, promemoria, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>promemoria</var> of type {@link it.govpay.orm.Promemoria}
	 * 
	 * @param promemoria Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Promemoria promemoria) throws SerializerException {
		return this.objToXml(Promemoria.class, promemoria, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>promemoria</var> of type {@link it.govpay.orm.Promemoria}
	 * 
	 * @param promemoria Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Promemoria promemoria,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Promemoria.class, promemoria, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>promemoria</var> of type {@link it.govpay.orm.Promemoria}
	 * 
	 * @param promemoria Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Promemoria promemoria) throws SerializerException {
		return this.objToXml(Promemoria.class, promemoria, false).toString();
	}
	/**
	 * Serialize to String the object <var>promemoria</var> of type {@link it.govpay.orm.Promemoria}
	 * 
	 * @param promemoria Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Promemoria promemoria,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Promemoria.class, promemoria, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-rpt
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idRpt</var> of type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param fileName Xml file to serialize the object <var>idRpt</var>
	 * @param idRpt Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdRpt idRpt) throws SerializerException {
		this.objToXml(fileName, IdRpt.class, idRpt, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idRpt</var> of type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param fileName Xml file to serialize the object <var>idRpt</var>
	 * @param idRpt Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdRpt idRpt,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdRpt.class, idRpt, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idRpt</var> of type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param file Xml file to serialize the object <var>idRpt</var>
	 * @param idRpt Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdRpt idRpt) throws SerializerException {
		this.objToXml(file, IdRpt.class, idRpt, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idRpt</var> of type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param file Xml file to serialize the object <var>idRpt</var>
	 * @param idRpt Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdRpt idRpt,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdRpt.class, idRpt, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idRpt</var> of type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param out OutputStream to serialize the object <var>idRpt</var>
	 * @param idRpt Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdRpt idRpt) throws SerializerException {
		this.objToXml(out, IdRpt.class, idRpt, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idRpt</var> of type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param out OutputStream to serialize the object <var>idRpt</var>
	 * @param idRpt Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdRpt idRpt,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdRpt.class, idRpt, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idRpt</var> of type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param idRpt Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdRpt idRpt) throws SerializerException {
		return this.objToXml(IdRpt.class, idRpt, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idRpt</var> of type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param idRpt Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdRpt idRpt,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdRpt.class, idRpt, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idRpt</var> of type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param idRpt Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdRpt idRpt) throws SerializerException {
		return this.objToXml(IdRpt.class, idRpt, false).toString();
	}
	/**
	 * Serialize to String the object <var>idRpt</var> of type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param idRpt Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdRpt idRpt,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdRpt.class, idRpt, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-intermediario
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idIntermediario</var> of type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param fileName Xml file to serialize the object <var>idIntermediario</var>
	 * @param idIntermediario Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdIntermediario idIntermediario) throws SerializerException {
		this.objToXml(fileName, IdIntermediario.class, idIntermediario, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idIntermediario</var> of type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param fileName Xml file to serialize the object <var>idIntermediario</var>
	 * @param idIntermediario Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdIntermediario idIntermediario,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdIntermediario.class, idIntermediario, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idIntermediario</var> of type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param file Xml file to serialize the object <var>idIntermediario</var>
	 * @param idIntermediario Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdIntermediario idIntermediario) throws SerializerException {
		this.objToXml(file, IdIntermediario.class, idIntermediario, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idIntermediario</var> of type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param file Xml file to serialize the object <var>idIntermediario</var>
	 * @param idIntermediario Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdIntermediario idIntermediario,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdIntermediario.class, idIntermediario, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idIntermediario</var> of type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param out OutputStream to serialize the object <var>idIntermediario</var>
	 * @param idIntermediario Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdIntermediario idIntermediario) throws SerializerException {
		this.objToXml(out, IdIntermediario.class, idIntermediario, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idIntermediario</var> of type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param out OutputStream to serialize the object <var>idIntermediario</var>
	 * @param idIntermediario Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdIntermediario idIntermediario,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdIntermediario.class, idIntermediario, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idIntermediario</var> of type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param idIntermediario Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdIntermediario idIntermediario) throws SerializerException {
		return this.objToXml(IdIntermediario.class, idIntermediario, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idIntermediario</var> of type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param idIntermediario Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdIntermediario idIntermediario,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdIntermediario.class, idIntermediario, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idIntermediario</var> of type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param idIntermediario Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdIntermediario idIntermediario) throws SerializerException {
		return this.objToXml(IdIntermediario.class, idIntermediario, false).toString();
	}
	/**
	 * Serialize to String the object <var>idIntermediario</var> of type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param idIntermediario Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdIntermediario idIntermediario,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdIntermediario.class, idIntermediario, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Stazione
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>stazione</var> of type {@link it.govpay.orm.Stazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>stazione</var>
	 * @param stazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Stazione stazione) throws SerializerException {
		this.objToXml(fileName, Stazione.class, stazione, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>stazione</var> of type {@link it.govpay.orm.Stazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>stazione</var>
	 * @param stazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Stazione stazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Stazione.class, stazione, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>stazione</var> of type {@link it.govpay.orm.Stazione}
	 * 
	 * @param file Xml file to serialize the object <var>stazione</var>
	 * @param stazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Stazione stazione) throws SerializerException {
		this.objToXml(file, Stazione.class, stazione, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>stazione</var> of type {@link it.govpay.orm.Stazione}
	 * 
	 * @param file Xml file to serialize the object <var>stazione</var>
	 * @param stazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Stazione stazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Stazione.class, stazione, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>stazione</var> of type {@link it.govpay.orm.Stazione}
	 * 
	 * @param out OutputStream to serialize the object <var>stazione</var>
	 * @param stazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Stazione stazione) throws SerializerException {
		this.objToXml(out, Stazione.class, stazione, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>stazione</var> of type {@link it.govpay.orm.Stazione}
	 * 
	 * @param out OutputStream to serialize the object <var>stazione</var>
	 * @param stazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Stazione stazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Stazione.class, stazione, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>stazione</var> of type {@link it.govpay.orm.Stazione}
	 * 
	 * @param stazione Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Stazione stazione) throws SerializerException {
		return this.objToXml(Stazione.class, stazione, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>stazione</var> of type {@link it.govpay.orm.Stazione}
	 * 
	 * @param stazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Stazione stazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Stazione.class, stazione, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>stazione</var> of type {@link it.govpay.orm.Stazione}
	 * 
	 * @param stazione Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Stazione stazione) throws SerializerException {
		return this.objToXml(Stazione.class, stazione, false).toString();
	}
	/**
	 * Serialize to String the object <var>stazione</var> of type {@link it.govpay.orm.Stazione}
	 * 
	 * @param stazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Stazione stazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Stazione.class, stazione, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-promemoria
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idPromemoria</var> of type {@link it.govpay.orm.IdPromemoria}
	 * 
	 * @param fileName Xml file to serialize the object <var>idPromemoria</var>
	 * @param idPromemoria Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdPromemoria idPromemoria) throws SerializerException {
		this.objToXml(fileName, IdPromemoria.class, idPromemoria, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idPromemoria</var> of type {@link it.govpay.orm.IdPromemoria}
	 * 
	 * @param fileName Xml file to serialize the object <var>idPromemoria</var>
	 * @param idPromemoria Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdPromemoria idPromemoria,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdPromemoria.class, idPromemoria, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idPromemoria</var> of type {@link it.govpay.orm.IdPromemoria}
	 * 
	 * @param file Xml file to serialize the object <var>idPromemoria</var>
	 * @param idPromemoria Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdPromemoria idPromemoria) throws SerializerException {
		this.objToXml(file, IdPromemoria.class, idPromemoria, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idPromemoria</var> of type {@link it.govpay.orm.IdPromemoria}
	 * 
	 * @param file Xml file to serialize the object <var>idPromemoria</var>
	 * @param idPromemoria Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdPromemoria idPromemoria,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdPromemoria.class, idPromemoria, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idPromemoria</var> of type {@link it.govpay.orm.IdPromemoria}
	 * 
	 * @param out OutputStream to serialize the object <var>idPromemoria</var>
	 * @param idPromemoria Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdPromemoria idPromemoria) throws SerializerException {
		this.objToXml(out, IdPromemoria.class, idPromemoria, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idPromemoria</var> of type {@link it.govpay.orm.IdPromemoria}
	 * 
	 * @param out OutputStream to serialize the object <var>idPromemoria</var>
	 * @param idPromemoria Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdPromemoria idPromemoria,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdPromemoria.class, idPromemoria, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idPromemoria</var> of type {@link it.govpay.orm.IdPromemoria}
	 * 
	 * @param idPromemoria Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdPromemoria idPromemoria) throws SerializerException {
		return this.objToXml(IdPromemoria.class, idPromemoria, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idPromemoria</var> of type {@link it.govpay.orm.IdPromemoria}
	 * 
	 * @param idPromemoria Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdPromemoria idPromemoria,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdPromemoria.class, idPromemoria, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idPromemoria</var> of type {@link it.govpay.orm.IdPromemoria}
	 * 
	 * @param idPromemoria Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdPromemoria idPromemoria) throws SerializerException {
		return this.objToXml(IdPromemoria.class, idPromemoria, false).toString();
	}
	/**
	 * Serialize to String the object <var>idPromemoria</var> of type {@link it.govpay.orm.IdPromemoria}
	 * 
	 * @param idPromemoria Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdPromemoria idPromemoria,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdPromemoria.class, idPromemoria, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Operazione
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>operazione</var> of type {@link it.govpay.orm.Operazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>operazione</var>
	 * @param operazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Operazione operazione) throws SerializerException {
		this.objToXml(fileName, Operazione.class, operazione, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>operazione</var> of type {@link it.govpay.orm.Operazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>operazione</var>
	 * @param operazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Operazione operazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Operazione.class, operazione, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>operazione</var> of type {@link it.govpay.orm.Operazione}
	 * 
	 * @param file Xml file to serialize the object <var>operazione</var>
	 * @param operazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Operazione operazione) throws SerializerException {
		this.objToXml(file, Operazione.class, operazione, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>operazione</var> of type {@link it.govpay.orm.Operazione}
	 * 
	 * @param file Xml file to serialize the object <var>operazione</var>
	 * @param operazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Operazione operazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Operazione.class, operazione, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>operazione</var> of type {@link it.govpay.orm.Operazione}
	 * 
	 * @param out OutputStream to serialize the object <var>operazione</var>
	 * @param operazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Operazione operazione) throws SerializerException {
		this.objToXml(out, Operazione.class, operazione, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>operazione</var> of type {@link it.govpay.orm.Operazione}
	 * 
	 * @param out OutputStream to serialize the object <var>operazione</var>
	 * @param operazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Operazione operazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Operazione.class, operazione, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>operazione</var> of type {@link it.govpay.orm.Operazione}
	 * 
	 * @param operazione Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Operazione operazione) throws SerializerException {
		return this.objToXml(Operazione.class, operazione, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>operazione</var> of type {@link it.govpay.orm.Operazione}
	 * 
	 * @param operazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Operazione operazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Operazione.class, operazione, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>operazione</var> of type {@link it.govpay.orm.Operazione}
	 * 
	 * @param operazione Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Operazione operazione) throws SerializerException {
		return this.objToXml(Operazione.class, operazione, false).toString();
	}
	/**
	 * Serialize to String the object <var>operazione</var> of type {@link it.govpay.orm.Operazione}
	 * 
	 * @param operazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Operazione operazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Operazione.class, operazione, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-stampa
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idStampa</var> of type {@link it.govpay.orm.IdStampa}
	 * 
	 * @param fileName Xml file to serialize the object <var>idStampa</var>
	 * @param idStampa Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdStampa idStampa) throws SerializerException {
		this.objToXml(fileName, IdStampa.class, idStampa, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idStampa</var> of type {@link it.govpay.orm.IdStampa}
	 * 
	 * @param fileName Xml file to serialize the object <var>idStampa</var>
	 * @param idStampa Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdStampa idStampa,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdStampa.class, idStampa, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idStampa</var> of type {@link it.govpay.orm.IdStampa}
	 * 
	 * @param file Xml file to serialize the object <var>idStampa</var>
	 * @param idStampa Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdStampa idStampa) throws SerializerException {
		this.objToXml(file, IdStampa.class, idStampa, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idStampa</var> of type {@link it.govpay.orm.IdStampa}
	 * 
	 * @param file Xml file to serialize the object <var>idStampa</var>
	 * @param idStampa Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdStampa idStampa,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdStampa.class, idStampa, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idStampa</var> of type {@link it.govpay.orm.IdStampa}
	 * 
	 * @param out OutputStream to serialize the object <var>idStampa</var>
	 * @param idStampa Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdStampa idStampa) throws SerializerException {
		this.objToXml(out, IdStampa.class, idStampa, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idStampa</var> of type {@link it.govpay.orm.IdStampa}
	 * 
	 * @param out OutputStream to serialize the object <var>idStampa</var>
	 * @param idStampa Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdStampa idStampa,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdStampa.class, idStampa, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idStampa</var> of type {@link it.govpay.orm.IdStampa}
	 * 
	 * @param idStampa Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdStampa idStampa) throws SerializerException {
		return this.objToXml(IdStampa.class, idStampa, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idStampa</var> of type {@link it.govpay.orm.IdStampa}
	 * 
	 * @param idStampa Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdStampa idStampa,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdStampa.class, idStampa, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idStampa</var> of type {@link it.govpay.orm.IdStampa}
	 * 
	 * @param idStampa Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdStampa idStampa) throws SerializerException {
		return this.objToXml(IdStampa.class, idStampa, false).toString();
	}
	/**
	 * Serialize to String the object <var>idStampa</var> of type {@link it.govpay.orm.IdStampa}
	 * 
	 * @param idStampa Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdStampa idStampa,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdStampa.class, idStampa, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: PagamentoPortaleVersamento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>pagamentoPortaleVersamento</var> of type {@link it.govpay.orm.PagamentoPortaleVersamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>pagamentoPortaleVersamento</var>
	 * @param pagamentoPortaleVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,PagamentoPortaleVersamento pagamentoPortaleVersamento) throws SerializerException {
		this.objToXml(fileName, PagamentoPortaleVersamento.class, pagamentoPortaleVersamento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>pagamentoPortaleVersamento</var> of type {@link it.govpay.orm.PagamentoPortaleVersamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>pagamentoPortaleVersamento</var>
	 * @param pagamentoPortaleVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,PagamentoPortaleVersamento pagamentoPortaleVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, PagamentoPortaleVersamento.class, pagamentoPortaleVersamento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>pagamentoPortaleVersamento</var> of type {@link it.govpay.orm.PagamentoPortaleVersamento}
	 * 
	 * @param file Xml file to serialize the object <var>pagamentoPortaleVersamento</var>
	 * @param pagamentoPortaleVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,PagamentoPortaleVersamento pagamentoPortaleVersamento) throws SerializerException {
		this.objToXml(file, PagamentoPortaleVersamento.class, pagamentoPortaleVersamento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>pagamentoPortaleVersamento</var> of type {@link it.govpay.orm.PagamentoPortaleVersamento}
	 * 
	 * @param file Xml file to serialize the object <var>pagamentoPortaleVersamento</var>
	 * @param pagamentoPortaleVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,PagamentoPortaleVersamento pagamentoPortaleVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, PagamentoPortaleVersamento.class, pagamentoPortaleVersamento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>pagamentoPortaleVersamento</var> of type {@link it.govpay.orm.PagamentoPortaleVersamento}
	 * 
	 * @param out OutputStream to serialize the object <var>pagamentoPortaleVersamento</var>
	 * @param pagamentoPortaleVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,PagamentoPortaleVersamento pagamentoPortaleVersamento) throws SerializerException {
		this.objToXml(out, PagamentoPortaleVersamento.class, pagamentoPortaleVersamento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>pagamentoPortaleVersamento</var> of type {@link it.govpay.orm.PagamentoPortaleVersamento}
	 * 
	 * @param out OutputStream to serialize the object <var>pagamentoPortaleVersamento</var>
	 * @param pagamentoPortaleVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,PagamentoPortaleVersamento pagamentoPortaleVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, PagamentoPortaleVersamento.class, pagamentoPortaleVersamento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>pagamentoPortaleVersamento</var> of type {@link it.govpay.orm.PagamentoPortaleVersamento}
	 * 
	 * @param pagamentoPortaleVersamento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(PagamentoPortaleVersamento pagamentoPortaleVersamento) throws SerializerException {
		return this.objToXml(PagamentoPortaleVersamento.class, pagamentoPortaleVersamento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>pagamentoPortaleVersamento</var> of type {@link it.govpay.orm.PagamentoPortaleVersamento}
	 * 
	 * @param pagamentoPortaleVersamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(PagamentoPortaleVersamento pagamentoPortaleVersamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(PagamentoPortaleVersamento.class, pagamentoPortaleVersamento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>pagamentoPortaleVersamento</var> of type {@link it.govpay.orm.PagamentoPortaleVersamento}
	 * 
	 * @param pagamentoPortaleVersamento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(PagamentoPortaleVersamento pagamentoPortaleVersamento) throws SerializerException {
		return this.objToXml(PagamentoPortaleVersamento.class, pagamentoPortaleVersamento, false).toString();
	}
	/**
	 * Serialize to String the object <var>pagamentoPortaleVersamento</var> of type {@link it.govpay.orm.PagamentoPortaleVersamento}
	 * 
	 * @param pagamentoPortaleVersamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(PagamentoPortaleVersamento pagamentoPortaleVersamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(PagamentoPortaleVersamento.class, pagamentoPortaleVersamento, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Incasso
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>incasso</var> of type {@link it.govpay.orm.Incasso}
	 * 
	 * @param fileName Xml file to serialize the object <var>incasso</var>
	 * @param incasso Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Incasso incasso) throws SerializerException {
		this.objToXml(fileName, Incasso.class, incasso, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>incasso</var> of type {@link it.govpay.orm.Incasso}
	 * 
	 * @param fileName Xml file to serialize the object <var>incasso</var>
	 * @param incasso Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Incasso incasso,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Incasso.class, incasso, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>incasso</var> of type {@link it.govpay.orm.Incasso}
	 * 
	 * @param file Xml file to serialize the object <var>incasso</var>
	 * @param incasso Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Incasso incasso) throws SerializerException {
		this.objToXml(file, Incasso.class, incasso, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>incasso</var> of type {@link it.govpay.orm.Incasso}
	 * 
	 * @param file Xml file to serialize the object <var>incasso</var>
	 * @param incasso Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Incasso incasso,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Incasso.class, incasso, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>incasso</var> of type {@link it.govpay.orm.Incasso}
	 * 
	 * @param out OutputStream to serialize the object <var>incasso</var>
	 * @param incasso Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Incasso incasso) throws SerializerException {
		this.objToXml(out, Incasso.class, incasso, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>incasso</var> of type {@link it.govpay.orm.Incasso}
	 * 
	 * @param out OutputStream to serialize the object <var>incasso</var>
	 * @param incasso Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Incasso incasso,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Incasso.class, incasso, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>incasso</var> of type {@link it.govpay.orm.Incasso}
	 * 
	 * @param incasso Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Incasso incasso) throws SerializerException {
		return this.objToXml(Incasso.class, incasso, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>incasso</var> of type {@link it.govpay.orm.Incasso}
	 * 
	 * @param incasso Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Incasso incasso,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Incasso.class, incasso, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>incasso</var> of type {@link it.govpay.orm.Incasso}
	 * 
	 * @param incasso Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Incasso incasso) throws SerializerException {
		return this.objToXml(Incasso.class, incasso, false).toString();
	}
	/**
	 * Serialize to String the object <var>incasso</var> of type {@link it.govpay.orm.Incasso}
	 * 
	 * @param incasso Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Incasso incasso,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Incasso.class, incasso, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-acl
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idAcl</var> of type {@link it.govpay.orm.IdAcl}
	 * 
	 * @param fileName Xml file to serialize the object <var>idAcl</var>
	 * @param idAcl Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdAcl idAcl) throws SerializerException {
		this.objToXml(fileName, IdAcl.class, idAcl, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idAcl</var> of type {@link it.govpay.orm.IdAcl}
	 * 
	 * @param fileName Xml file to serialize the object <var>idAcl</var>
	 * @param idAcl Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdAcl idAcl,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdAcl.class, idAcl, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idAcl</var> of type {@link it.govpay.orm.IdAcl}
	 * 
	 * @param file Xml file to serialize the object <var>idAcl</var>
	 * @param idAcl Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdAcl idAcl) throws SerializerException {
		this.objToXml(file, IdAcl.class, idAcl, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idAcl</var> of type {@link it.govpay.orm.IdAcl}
	 * 
	 * @param file Xml file to serialize the object <var>idAcl</var>
	 * @param idAcl Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdAcl idAcl,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdAcl.class, idAcl, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idAcl</var> of type {@link it.govpay.orm.IdAcl}
	 * 
	 * @param out OutputStream to serialize the object <var>idAcl</var>
	 * @param idAcl Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdAcl idAcl) throws SerializerException {
		this.objToXml(out, IdAcl.class, idAcl, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idAcl</var> of type {@link it.govpay.orm.IdAcl}
	 * 
	 * @param out OutputStream to serialize the object <var>idAcl</var>
	 * @param idAcl Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdAcl idAcl,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdAcl.class, idAcl, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idAcl</var> of type {@link it.govpay.orm.IdAcl}
	 * 
	 * @param idAcl Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdAcl idAcl) throws SerializerException {
		return this.objToXml(IdAcl.class, idAcl, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idAcl</var> of type {@link it.govpay.orm.IdAcl}
	 * 
	 * @param idAcl Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdAcl idAcl,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdAcl.class, idAcl, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idAcl</var> of type {@link it.govpay.orm.IdAcl}
	 * 
	 * @param idAcl Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdAcl idAcl) throws SerializerException {
		return this.objToXml(IdAcl.class, idAcl, false).toString();
	}
	/**
	 * Serialize to String the object <var>idAcl</var> of type {@link it.govpay.orm.IdAcl}
	 * 
	 * @param idAcl Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdAcl idAcl,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdAcl.class, idAcl, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-utenza
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idUtenza</var> of type {@link it.govpay.orm.IdUtenza}
	 * 
	 * @param fileName Xml file to serialize the object <var>idUtenza</var>
	 * @param idUtenza Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdUtenza idUtenza) throws SerializerException {
		this.objToXml(fileName, IdUtenza.class, idUtenza, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idUtenza</var> of type {@link it.govpay.orm.IdUtenza}
	 * 
	 * @param fileName Xml file to serialize the object <var>idUtenza</var>
	 * @param idUtenza Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdUtenza idUtenza,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdUtenza.class, idUtenza, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idUtenza</var> of type {@link it.govpay.orm.IdUtenza}
	 * 
	 * @param file Xml file to serialize the object <var>idUtenza</var>
	 * @param idUtenza Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdUtenza idUtenza) throws SerializerException {
		this.objToXml(file, IdUtenza.class, idUtenza, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idUtenza</var> of type {@link it.govpay.orm.IdUtenza}
	 * 
	 * @param file Xml file to serialize the object <var>idUtenza</var>
	 * @param idUtenza Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdUtenza idUtenza,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdUtenza.class, idUtenza, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idUtenza</var> of type {@link it.govpay.orm.IdUtenza}
	 * 
	 * @param out OutputStream to serialize the object <var>idUtenza</var>
	 * @param idUtenza Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdUtenza idUtenza) throws SerializerException {
		this.objToXml(out, IdUtenza.class, idUtenza, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idUtenza</var> of type {@link it.govpay.orm.IdUtenza}
	 * 
	 * @param out OutputStream to serialize the object <var>idUtenza</var>
	 * @param idUtenza Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdUtenza idUtenza,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdUtenza.class, idUtenza, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idUtenza</var> of type {@link it.govpay.orm.IdUtenza}
	 * 
	 * @param idUtenza Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdUtenza idUtenza) throws SerializerException {
		return this.objToXml(IdUtenza.class, idUtenza, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idUtenza</var> of type {@link it.govpay.orm.IdUtenza}
	 * 
	 * @param idUtenza Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdUtenza idUtenza,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdUtenza.class, idUtenza, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idUtenza</var> of type {@link it.govpay.orm.IdUtenza}
	 * 
	 * @param idUtenza Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdUtenza idUtenza) throws SerializerException {
		return this.objToXml(IdUtenza.class, idUtenza, false).toString();
	}
	/**
	 * Serialize to String the object <var>idUtenza</var> of type {@link it.govpay.orm.IdUtenza}
	 * 
	 * @param idUtenza Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdUtenza idUtenza,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdUtenza.class, idUtenza, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Intermediario
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>intermediario</var> of type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param fileName Xml file to serialize the object <var>intermediario</var>
	 * @param intermediario Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Intermediario intermediario) throws SerializerException {
		this.objToXml(fileName, Intermediario.class, intermediario, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>intermediario</var> of type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param fileName Xml file to serialize the object <var>intermediario</var>
	 * @param intermediario Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Intermediario intermediario,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Intermediario.class, intermediario, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>intermediario</var> of type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param file Xml file to serialize the object <var>intermediario</var>
	 * @param intermediario Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Intermediario intermediario) throws SerializerException {
		this.objToXml(file, Intermediario.class, intermediario, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>intermediario</var> of type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param file Xml file to serialize the object <var>intermediario</var>
	 * @param intermediario Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Intermediario intermediario,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Intermediario.class, intermediario, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>intermediario</var> of type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param out OutputStream to serialize the object <var>intermediario</var>
	 * @param intermediario Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Intermediario intermediario) throws SerializerException {
		this.objToXml(out, Intermediario.class, intermediario, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>intermediario</var> of type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param out OutputStream to serialize the object <var>intermediario</var>
	 * @param intermediario Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Intermediario intermediario,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Intermediario.class, intermediario, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>intermediario</var> of type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param intermediario Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Intermediario intermediario) throws SerializerException {
		return this.objToXml(Intermediario.class, intermediario, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>intermediario</var> of type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param intermediario Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Intermediario intermediario,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Intermediario.class, intermediario, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>intermediario</var> of type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param intermediario Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Intermediario intermediario) throws SerializerException {
		return this.objToXml(Intermediario.class, intermediario, false).toString();
	}
	/**
	 * Serialize to String the object <var>intermediario</var> of type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param intermediario Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Intermediario intermediario,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Intermediario.class, intermediario, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-singolo-versamento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idSingoloVersamento</var> of type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>idSingoloVersamento</var>
	 * @param idSingoloVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdSingoloVersamento idSingoloVersamento) throws SerializerException {
		this.objToXml(fileName, IdSingoloVersamento.class, idSingoloVersamento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idSingoloVersamento</var> of type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>idSingoloVersamento</var>
	 * @param idSingoloVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdSingoloVersamento idSingoloVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdSingoloVersamento.class, idSingoloVersamento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idSingoloVersamento</var> of type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param file Xml file to serialize the object <var>idSingoloVersamento</var>
	 * @param idSingoloVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdSingoloVersamento idSingoloVersamento) throws SerializerException {
		this.objToXml(file, IdSingoloVersamento.class, idSingoloVersamento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idSingoloVersamento</var> of type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param file Xml file to serialize the object <var>idSingoloVersamento</var>
	 * @param idSingoloVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdSingoloVersamento idSingoloVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdSingoloVersamento.class, idSingoloVersamento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idSingoloVersamento</var> of type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param out OutputStream to serialize the object <var>idSingoloVersamento</var>
	 * @param idSingoloVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdSingoloVersamento idSingoloVersamento) throws SerializerException {
		this.objToXml(out, IdSingoloVersamento.class, idSingoloVersamento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idSingoloVersamento</var> of type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param out OutputStream to serialize the object <var>idSingoloVersamento</var>
	 * @param idSingoloVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdSingoloVersamento idSingoloVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdSingoloVersamento.class, idSingoloVersamento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idSingoloVersamento</var> of type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param idSingoloVersamento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdSingoloVersamento idSingoloVersamento) throws SerializerException {
		return this.objToXml(IdSingoloVersamento.class, idSingoloVersamento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idSingoloVersamento</var> of type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param idSingoloVersamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdSingoloVersamento idSingoloVersamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdSingoloVersamento.class, idSingoloVersamento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idSingoloVersamento</var> of type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param idSingoloVersamento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdSingoloVersamento idSingoloVersamento) throws SerializerException {
		return this.objToXml(IdSingoloVersamento.class, idSingoloVersamento, false).toString();
	}
	/**
	 * Serialize to String the object <var>idSingoloVersamento</var> of type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param idSingoloVersamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdSingoloVersamento idSingoloVersamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdSingoloVersamento.class, idSingoloVersamento, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-rendicontazione
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idRendicontazione</var> of type {@link it.govpay.orm.IdRendicontazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>idRendicontazione</var>
	 * @param idRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdRendicontazione idRendicontazione) throws SerializerException {
		this.objToXml(fileName, IdRendicontazione.class, idRendicontazione, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idRendicontazione</var> of type {@link it.govpay.orm.IdRendicontazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>idRendicontazione</var>
	 * @param idRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdRendicontazione idRendicontazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdRendicontazione.class, idRendicontazione, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idRendicontazione</var> of type {@link it.govpay.orm.IdRendicontazione}
	 * 
	 * @param file Xml file to serialize the object <var>idRendicontazione</var>
	 * @param idRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdRendicontazione idRendicontazione) throws SerializerException {
		this.objToXml(file, IdRendicontazione.class, idRendicontazione, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idRendicontazione</var> of type {@link it.govpay.orm.IdRendicontazione}
	 * 
	 * @param file Xml file to serialize the object <var>idRendicontazione</var>
	 * @param idRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdRendicontazione idRendicontazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdRendicontazione.class, idRendicontazione, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idRendicontazione</var> of type {@link it.govpay.orm.IdRendicontazione}
	 * 
	 * @param out OutputStream to serialize the object <var>idRendicontazione</var>
	 * @param idRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdRendicontazione idRendicontazione) throws SerializerException {
		this.objToXml(out, IdRendicontazione.class, idRendicontazione, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idRendicontazione</var> of type {@link it.govpay.orm.IdRendicontazione}
	 * 
	 * @param out OutputStream to serialize the object <var>idRendicontazione</var>
	 * @param idRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdRendicontazione idRendicontazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdRendicontazione.class, idRendicontazione, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idRendicontazione</var> of type {@link it.govpay.orm.IdRendicontazione}
	 * 
	 * @param idRendicontazione Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdRendicontazione idRendicontazione) throws SerializerException {
		return this.objToXml(IdRendicontazione.class, idRendicontazione, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idRendicontazione</var> of type {@link it.govpay.orm.IdRendicontazione}
	 * 
	 * @param idRendicontazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdRendicontazione idRendicontazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdRendicontazione.class, idRendicontazione, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idRendicontazione</var> of type {@link it.govpay.orm.IdRendicontazione}
	 * 
	 * @param idRendicontazione Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdRendicontazione idRendicontazione) throws SerializerException {
		return this.objToXml(IdRendicontazione.class, idRendicontazione, false).toString();
	}
	/**
	 * Serialize to String the object <var>idRendicontazione</var> of type {@link it.govpay.orm.IdRendicontazione}
	 * 
	 * @param idRendicontazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdRendicontazione idRendicontazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdRendicontazione.class, idRendicontazione, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-notifica
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idNotifica</var> of type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param fileName Xml file to serialize the object <var>idNotifica</var>
	 * @param idNotifica Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdNotifica idNotifica) throws SerializerException {
		this.objToXml(fileName, IdNotifica.class, idNotifica, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idNotifica</var> of type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param fileName Xml file to serialize the object <var>idNotifica</var>
	 * @param idNotifica Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdNotifica idNotifica,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdNotifica.class, idNotifica, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idNotifica</var> of type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param file Xml file to serialize the object <var>idNotifica</var>
	 * @param idNotifica Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdNotifica idNotifica) throws SerializerException {
		this.objToXml(file, IdNotifica.class, idNotifica, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idNotifica</var> of type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param file Xml file to serialize the object <var>idNotifica</var>
	 * @param idNotifica Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdNotifica idNotifica,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdNotifica.class, idNotifica, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idNotifica</var> of type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param out OutputStream to serialize the object <var>idNotifica</var>
	 * @param idNotifica Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdNotifica idNotifica) throws SerializerException {
		this.objToXml(out, IdNotifica.class, idNotifica, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idNotifica</var> of type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param out OutputStream to serialize the object <var>idNotifica</var>
	 * @param idNotifica Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdNotifica idNotifica,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdNotifica.class, idNotifica, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idNotifica</var> of type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param idNotifica Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdNotifica idNotifica) throws SerializerException {
		return this.objToXml(IdNotifica.class, idNotifica, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idNotifica</var> of type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param idNotifica Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdNotifica idNotifica,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdNotifica.class, idNotifica, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idNotifica</var> of type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param idNotifica Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdNotifica idNotifica) throws SerializerException {
		return this.objToXml(IdNotifica.class, idNotifica, false).toString();
	}
	/**
	 * Serialize to String the object <var>idNotifica</var> of type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param idNotifica Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdNotifica idNotifica,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdNotifica.class, idNotifica, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: VersamentoIncasso
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>versamentoIncasso</var> of type {@link it.govpay.orm.VersamentoIncasso}
	 * 
	 * @param fileName Xml file to serialize the object <var>versamentoIncasso</var>
	 * @param versamentoIncasso Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,VersamentoIncasso versamentoIncasso) throws SerializerException {
		this.objToXml(fileName, VersamentoIncasso.class, versamentoIncasso, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>versamentoIncasso</var> of type {@link it.govpay.orm.VersamentoIncasso}
	 * 
	 * @param fileName Xml file to serialize the object <var>versamentoIncasso</var>
	 * @param versamentoIncasso Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,VersamentoIncasso versamentoIncasso,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, VersamentoIncasso.class, versamentoIncasso, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>versamentoIncasso</var> of type {@link it.govpay.orm.VersamentoIncasso}
	 * 
	 * @param file Xml file to serialize the object <var>versamentoIncasso</var>
	 * @param versamentoIncasso Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,VersamentoIncasso versamentoIncasso) throws SerializerException {
		this.objToXml(file, VersamentoIncasso.class, versamentoIncasso, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>versamentoIncasso</var> of type {@link it.govpay.orm.VersamentoIncasso}
	 * 
	 * @param file Xml file to serialize the object <var>versamentoIncasso</var>
	 * @param versamentoIncasso Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,VersamentoIncasso versamentoIncasso,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, VersamentoIncasso.class, versamentoIncasso, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>versamentoIncasso</var> of type {@link it.govpay.orm.VersamentoIncasso}
	 * 
	 * @param out OutputStream to serialize the object <var>versamentoIncasso</var>
	 * @param versamentoIncasso Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,VersamentoIncasso versamentoIncasso) throws SerializerException {
		this.objToXml(out, VersamentoIncasso.class, versamentoIncasso, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>versamentoIncasso</var> of type {@link it.govpay.orm.VersamentoIncasso}
	 * 
	 * @param out OutputStream to serialize the object <var>versamentoIncasso</var>
	 * @param versamentoIncasso Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,VersamentoIncasso versamentoIncasso,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, VersamentoIncasso.class, versamentoIncasso, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>versamentoIncasso</var> of type {@link it.govpay.orm.VersamentoIncasso}
	 * 
	 * @param versamentoIncasso Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(VersamentoIncasso versamentoIncasso) throws SerializerException {
		return this.objToXml(VersamentoIncasso.class, versamentoIncasso, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>versamentoIncasso</var> of type {@link it.govpay.orm.VersamentoIncasso}
	 * 
	 * @param versamentoIncasso Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(VersamentoIncasso versamentoIncasso,boolean prettyPrint) throws SerializerException {
		return this.objToXml(VersamentoIncasso.class, versamentoIncasso, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>versamentoIncasso</var> of type {@link it.govpay.orm.VersamentoIncasso}
	 * 
	 * @param versamentoIncasso Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(VersamentoIncasso versamentoIncasso) throws SerializerException {
		return this.objToXml(VersamentoIncasso.class, versamentoIncasso, false).toString();
	}
	/**
	 * Serialize to String the object <var>versamentoIncasso</var> of type {@link it.govpay.orm.VersamentoIncasso}
	 * 
	 * @param versamentoIncasso Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(VersamentoIncasso versamentoIncasso,boolean prettyPrint) throws SerializerException {
		return this.objToXml(VersamentoIncasso.class, versamentoIncasso, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Utenza
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>utenza</var> of type {@link it.govpay.orm.Utenza}
	 * 
	 * @param fileName Xml file to serialize the object <var>utenza</var>
	 * @param utenza Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Utenza utenza) throws SerializerException {
		this.objToXml(fileName, Utenza.class, utenza, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>utenza</var> of type {@link it.govpay.orm.Utenza}
	 * 
	 * @param fileName Xml file to serialize the object <var>utenza</var>
	 * @param utenza Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Utenza utenza,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Utenza.class, utenza, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>utenza</var> of type {@link it.govpay.orm.Utenza}
	 * 
	 * @param file Xml file to serialize the object <var>utenza</var>
	 * @param utenza Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Utenza utenza) throws SerializerException {
		this.objToXml(file, Utenza.class, utenza, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>utenza</var> of type {@link it.govpay.orm.Utenza}
	 * 
	 * @param file Xml file to serialize the object <var>utenza</var>
	 * @param utenza Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Utenza utenza,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Utenza.class, utenza, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>utenza</var> of type {@link it.govpay.orm.Utenza}
	 * 
	 * @param out OutputStream to serialize the object <var>utenza</var>
	 * @param utenza Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Utenza utenza) throws SerializerException {
		this.objToXml(out, Utenza.class, utenza, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>utenza</var> of type {@link it.govpay.orm.Utenza}
	 * 
	 * @param out OutputStream to serialize the object <var>utenza</var>
	 * @param utenza Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Utenza utenza,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Utenza.class, utenza, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>utenza</var> of type {@link it.govpay.orm.Utenza}
	 * 
	 * @param utenza Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Utenza utenza) throws SerializerException {
		return this.objToXml(Utenza.class, utenza, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>utenza</var> of type {@link it.govpay.orm.Utenza}
	 * 
	 * @param utenza Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Utenza utenza,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Utenza.class, utenza, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>utenza</var> of type {@link it.govpay.orm.Utenza}
	 * 
	 * @param utenza Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Utenza utenza) throws SerializerException {
		return this.objToXml(Utenza.class, utenza, false).toString();
	}
	/**
	 * Serialize to String the object <var>utenza</var> of type {@link it.govpay.orm.Utenza}
	 * 
	 * @param utenza Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Utenza utenza,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Utenza.class, utenza, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: RR
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>rr</var> of type {@link it.govpay.orm.RR}
	 * 
	 * @param fileName Xml file to serialize the object <var>rr</var>
	 * @param rr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,RR rr) throws SerializerException {
		this.objToXml(fileName, RR.class, rr, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>rr</var> of type {@link it.govpay.orm.RR}
	 * 
	 * @param fileName Xml file to serialize the object <var>rr</var>
	 * @param rr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,RR rr,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, RR.class, rr, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>rr</var> of type {@link it.govpay.orm.RR}
	 * 
	 * @param file Xml file to serialize the object <var>rr</var>
	 * @param rr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,RR rr) throws SerializerException {
		this.objToXml(file, RR.class, rr, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>rr</var> of type {@link it.govpay.orm.RR}
	 * 
	 * @param file Xml file to serialize the object <var>rr</var>
	 * @param rr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,RR rr,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, RR.class, rr, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>rr</var> of type {@link it.govpay.orm.RR}
	 * 
	 * @param out OutputStream to serialize the object <var>rr</var>
	 * @param rr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,RR rr) throws SerializerException {
		this.objToXml(out, RR.class, rr, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>rr</var> of type {@link it.govpay.orm.RR}
	 * 
	 * @param out OutputStream to serialize the object <var>rr</var>
	 * @param rr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,RR rr,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, RR.class, rr, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>rr</var> of type {@link it.govpay.orm.RR}
	 * 
	 * @param rr Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(RR rr) throws SerializerException {
		return this.objToXml(RR.class, rr, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>rr</var> of type {@link it.govpay.orm.RR}
	 * 
	 * @param rr Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(RR rr,boolean prettyPrint) throws SerializerException {
		return this.objToXml(RR.class, rr, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>rr</var> of type {@link it.govpay.orm.RR}
	 * 
	 * @param rr Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(RR rr) throws SerializerException {
		return this.objToXml(RR.class, rr, false).toString();
	}
	/**
	 * Serialize to String the object <var>rr</var> of type {@link it.govpay.orm.RR}
	 * 
	 * @param rr Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(RR rr,boolean prettyPrint) throws SerializerException {
		return this.objToXml(RR.class, rr, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: TracciatoNotificaPagamenti
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>tracciatoNotificaPagamenti</var> of type {@link it.govpay.orm.TracciatoNotificaPagamenti}
	 * 
	 * @param fileName Xml file to serialize the object <var>tracciatoNotificaPagamenti</var>
	 * @param tracciatoNotificaPagamenti Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,TracciatoNotificaPagamenti tracciatoNotificaPagamenti) throws SerializerException {
		this.objToXml(fileName, TracciatoNotificaPagamenti.class, tracciatoNotificaPagamenti, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>tracciatoNotificaPagamenti</var> of type {@link it.govpay.orm.TracciatoNotificaPagamenti}
	 * 
	 * @param fileName Xml file to serialize the object <var>tracciatoNotificaPagamenti</var>
	 * @param tracciatoNotificaPagamenti Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,TracciatoNotificaPagamenti tracciatoNotificaPagamenti,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, TracciatoNotificaPagamenti.class, tracciatoNotificaPagamenti, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>tracciatoNotificaPagamenti</var> of type {@link it.govpay.orm.TracciatoNotificaPagamenti}
	 * 
	 * @param file Xml file to serialize the object <var>tracciatoNotificaPagamenti</var>
	 * @param tracciatoNotificaPagamenti Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,TracciatoNotificaPagamenti tracciatoNotificaPagamenti) throws SerializerException {
		this.objToXml(file, TracciatoNotificaPagamenti.class, tracciatoNotificaPagamenti, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>tracciatoNotificaPagamenti</var> of type {@link it.govpay.orm.TracciatoNotificaPagamenti}
	 * 
	 * @param file Xml file to serialize the object <var>tracciatoNotificaPagamenti</var>
	 * @param tracciatoNotificaPagamenti Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,TracciatoNotificaPagamenti tracciatoNotificaPagamenti,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, TracciatoNotificaPagamenti.class, tracciatoNotificaPagamenti, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>tracciatoNotificaPagamenti</var> of type {@link it.govpay.orm.TracciatoNotificaPagamenti}
	 * 
	 * @param out OutputStream to serialize the object <var>tracciatoNotificaPagamenti</var>
	 * @param tracciatoNotificaPagamenti Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,TracciatoNotificaPagamenti tracciatoNotificaPagamenti) throws SerializerException {
		this.objToXml(out, TracciatoNotificaPagamenti.class, tracciatoNotificaPagamenti, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>tracciatoNotificaPagamenti</var> of type {@link it.govpay.orm.TracciatoNotificaPagamenti}
	 * 
	 * @param out OutputStream to serialize the object <var>tracciatoNotificaPagamenti</var>
	 * @param tracciatoNotificaPagamenti Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,TracciatoNotificaPagamenti tracciatoNotificaPagamenti,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, TracciatoNotificaPagamenti.class, tracciatoNotificaPagamenti, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>tracciatoNotificaPagamenti</var> of type {@link it.govpay.orm.TracciatoNotificaPagamenti}
	 * 
	 * @param tracciatoNotificaPagamenti Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(TracciatoNotificaPagamenti tracciatoNotificaPagamenti) throws SerializerException {
		return this.objToXml(TracciatoNotificaPagamenti.class, tracciatoNotificaPagamenti, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>tracciatoNotificaPagamenti</var> of type {@link it.govpay.orm.TracciatoNotificaPagamenti}
	 * 
	 * @param tracciatoNotificaPagamenti Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(TracciatoNotificaPagamenti tracciatoNotificaPagamenti,boolean prettyPrint) throws SerializerException {
		return this.objToXml(TracciatoNotificaPagamenti.class, tracciatoNotificaPagamenti, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>tracciatoNotificaPagamenti</var> of type {@link it.govpay.orm.TracciatoNotificaPagamenti}
	 * 
	 * @param tracciatoNotificaPagamenti Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(TracciatoNotificaPagamenti tracciatoNotificaPagamenti) throws SerializerException {
		return this.objToXml(TracciatoNotificaPagamenti.class, tracciatoNotificaPagamenti, false).toString();
	}
	/**
	 * Serialize to String the object <var>tracciatoNotificaPagamenti</var> of type {@link it.govpay.orm.TracciatoNotificaPagamenti}
	 * 
	 * @param tracciatoNotificaPagamenti Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(TracciatoNotificaPagamenti tracciatoNotificaPagamenti,boolean prettyPrint) throws SerializerException {
		return this.objToXml(TracciatoNotificaPagamenti.class, tracciatoNotificaPagamenti, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Operatore
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>operatore</var> of type {@link it.govpay.orm.Operatore}
	 * 
	 * @param fileName Xml file to serialize the object <var>operatore</var>
	 * @param operatore Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Operatore operatore) throws SerializerException {
		this.objToXml(fileName, Operatore.class, operatore, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>operatore</var> of type {@link it.govpay.orm.Operatore}
	 * 
	 * @param fileName Xml file to serialize the object <var>operatore</var>
	 * @param operatore Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Operatore operatore,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Operatore.class, operatore, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>operatore</var> of type {@link it.govpay.orm.Operatore}
	 * 
	 * @param file Xml file to serialize the object <var>operatore</var>
	 * @param operatore Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Operatore operatore) throws SerializerException {
		this.objToXml(file, Operatore.class, operatore, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>operatore</var> of type {@link it.govpay.orm.Operatore}
	 * 
	 * @param file Xml file to serialize the object <var>operatore</var>
	 * @param operatore Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Operatore operatore,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Operatore.class, operatore, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>operatore</var> of type {@link it.govpay.orm.Operatore}
	 * 
	 * @param out OutputStream to serialize the object <var>operatore</var>
	 * @param operatore Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Operatore operatore) throws SerializerException {
		this.objToXml(out, Operatore.class, operatore, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>operatore</var> of type {@link it.govpay.orm.Operatore}
	 * 
	 * @param out OutputStream to serialize the object <var>operatore</var>
	 * @param operatore Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Operatore operatore,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Operatore.class, operatore, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>operatore</var> of type {@link it.govpay.orm.Operatore}
	 * 
	 * @param operatore Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Operatore operatore) throws SerializerException {
		return this.objToXml(Operatore.class, operatore, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>operatore</var> of type {@link it.govpay.orm.Operatore}
	 * 
	 * @param operatore Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Operatore operatore,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Operatore.class, operatore, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>operatore</var> of type {@link it.govpay.orm.Operatore}
	 * 
	 * @param operatore Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Operatore operatore) throws SerializerException {
		return this.objToXml(Operatore.class, operatore, false).toString();
	}
	/**
	 * Serialize to String the object <var>operatore</var> of type {@link it.govpay.orm.Operatore}
	 * 
	 * @param operatore Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Operatore operatore,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Operatore.class, operatore, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Tracciato
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>tracciato</var> of type {@link it.govpay.orm.Tracciato}
	 * 
	 * @param fileName Xml file to serialize the object <var>tracciato</var>
	 * @param tracciato Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Tracciato tracciato) throws SerializerException {
		this.objToXml(fileName, Tracciato.class, tracciato, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>tracciato</var> of type {@link it.govpay.orm.Tracciato}
	 * 
	 * @param fileName Xml file to serialize the object <var>tracciato</var>
	 * @param tracciato Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Tracciato tracciato,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Tracciato.class, tracciato, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>tracciato</var> of type {@link it.govpay.orm.Tracciato}
	 * 
	 * @param file Xml file to serialize the object <var>tracciato</var>
	 * @param tracciato Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Tracciato tracciato) throws SerializerException {
		this.objToXml(file, Tracciato.class, tracciato, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>tracciato</var> of type {@link it.govpay.orm.Tracciato}
	 * 
	 * @param file Xml file to serialize the object <var>tracciato</var>
	 * @param tracciato Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Tracciato tracciato,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Tracciato.class, tracciato, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>tracciato</var> of type {@link it.govpay.orm.Tracciato}
	 * 
	 * @param out OutputStream to serialize the object <var>tracciato</var>
	 * @param tracciato Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Tracciato tracciato) throws SerializerException {
		this.objToXml(out, Tracciato.class, tracciato, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>tracciato</var> of type {@link it.govpay.orm.Tracciato}
	 * 
	 * @param out OutputStream to serialize the object <var>tracciato</var>
	 * @param tracciato Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Tracciato tracciato,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Tracciato.class, tracciato, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>tracciato</var> of type {@link it.govpay.orm.Tracciato}
	 * 
	 * @param tracciato Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Tracciato tracciato) throws SerializerException {
		return this.objToXml(Tracciato.class, tracciato, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>tracciato</var> of type {@link it.govpay.orm.Tracciato}
	 * 
	 * @param tracciato Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Tracciato tracciato,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Tracciato.class, tracciato, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>tracciato</var> of type {@link it.govpay.orm.Tracciato}
	 * 
	 * @param tracciato Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Tracciato tracciato) throws SerializerException {
		return this.objToXml(Tracciato.class, tracciato, false).toString();
	}
	/**
	 * Serialize to String the object <var>tracciato</var> of type {@link it.govpay.orm.Tracciato}
	 * 
	 * @param tracciato Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Tracciato tracciato,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Tracciato.class, tracciato, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-stazione
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idStazione</var> of type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>idStazione</var>
	 * @param idStazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdStazione idStazione) throws SerializerException {
		this.objToXml(fileName, IdStazione.class, idStazione, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idStazione</var> of type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>idStazione</var>
	 * @param idStazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdStazione idStazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdStazione.class, idStazione, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idStazione</var> of type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param file Xml file to serialize the object <var>idStazione</var>
	 * @param idStazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdStazione idStazione) throws SerializerException {
		this.objToXml(file, IdStazione.class, idStazione, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idStazione</var> of type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param file Xml file to serialize the object <var>idStazione</var>
	 * @param idStazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdStazione idStazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdStazione.class, idStazione, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idStazione</var> of type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param out OutputStream to serialize the object <var>idStazione</var>
	 * @param idStazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdStazione idStazione) throws SerializerException {
		this.objToXml(out, IdStazione.class, idStazione, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idStazione</var> of type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param out OutputStream to serialize the object <var>idStazione</var>
	 * @param idStazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdStazione idStazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdStazione.class, idStazione, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idStazione</var> of type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param idStazione Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdStazione idStazione) throws SerializerException {
		return this.objToXml(IdStazione.class, idStazione, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idStazione</var> of type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param idStazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdStazione idStazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdStazione.class, idStazione, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idStazione</var> of type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param idStazione Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdStazione idStazione) throws SerializerException {
		return this.objToXml(IdStazione.class, idStazione, false).toString();
	}
	/**
	 * Serialize to String the object <var>idStazione</var> of type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param idStazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdStazione idStazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdStazione.class, idStazione, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Dominio
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>dominio</var> of type {@link it.govpay.orm.Dominio}
	 * 
	 * @param fileName Xml file to serialize the object <var>dominio</var>
	 * @param dominio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Dominio dominio) throws SerializerException {
		this.objToXml(fileName, Dominio.class, dominio, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>dominio</var> of type {@link it.govpay.orm.Dominio}
	 * 
	 * @param fileName Xml file to serialize the object <var>dominio</var>
	 * @param dominio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Dominio dominio,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Dominio.class, dominio, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>dominio</var> of type {@link it.govpay.orm.Dominio}
	 * 
	 * @param file Xml file to serialize the object <var>dominio</var>
	 * @param dominio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Dominio dominio) throws SerializerException {
		this.objToXml(file, Dominio.class, dominio, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>dominio</var> of type {@link it.govpay.orm.Dominio}
	 * 
	 * @param file Xml file to serialize the object <var>dominio</var>
	 * @param dominio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Dominio dominio,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Dominio.class, dominio, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>dominio</var> of type {@link it.govpay.orm.Dominio}
	 * 
	 * @param out OutputStream to serialize the object <var>dominio</var>
	 * @param dominio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Dominio dominio) throws SerializerException {
		this.objToXml(out, Dominio.class, dominio, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>dominio</var> of type {@link it.govpay.orm.Dominio}
	 * 
	 * @param out OutputStream to serialize the object <var>dominio</var>
	 * @param dominio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Dominio dominio,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Dominio.class, dominio, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>dominio</var> of type {@link it.govpay.orm.Dominio}
	 * 
	 * @param dominio Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Dominio dominio) throws SerializerException {
		return this.objToXml(Dominio.class, dominio, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>dominio</var> of type {@link it.govpay.orm.Dominio}
	 * 
	 * @param dominio Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Dominio dominio,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Dominio.class, dominio, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>dominio</var> of type {@link it.govpay.orm.Dominio}
	 * 
	 * @param dominio Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Dominio dominio) throws SerializerException {
		return this.objToXml(Dominio.class, dominio, false).toString();
	}
	/**
	 * Serialize to String the object <var>dominio</var> of type {@link it.govpay.orm.Dominio}
	 * 
	 * @param dominio Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Dominio dominio,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Dominio.class, dominio, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: VistaRendicontazione
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>vistaRendicontazione</var> of type {@link it.govpay.orm.VistaRendicontazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>vistaRendicontazione</var>
	 * @param vistaRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,VistaRendicontazione vistaRendicontazione) throws SerializerException {
		this.objToXml(fileName, VistaRendicontazione.class, vistaRendicontazione, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>vistaRendicontazione</var> of type {@link it.govpay.orm.VistaRendicontazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>vistaRendicontazione</var>
	 * @param vistaRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,VistaRendicontazione vistaRendicontazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, VistaRendicontazione.class, vistaRendicontazione, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>vistaRendicontazione</var> of type {@link it.govpay.orm.VistaRendicontazione}
	 * 
	 * @param file Xml file to serialize the object <var>vistaRendicontazione</var>
	 * @param vistaRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,VistaRendicontazione vistaRendicontazione) throws SerializerException {
		this.objToXml(file, VistaRendicontazione.class, vistaRendicontazione, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>vistaRendicontazione</var> of type {@link it.govpay.orm.VistaRendicontazione}
	 * 
	 * @param file Xml file to serialize the object <var>vistaRendicontazione</var>
	 * @param vistaRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,VistaRendicontazione vistaRendicontazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, VistaRendicontazione.class, vistaRendicontazione, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>vistaRendicontazione</var> of type {@link it.govpay.orm.VistaRendicontazione}
	 * 
	 * @param out OutputStream to serialize the object <var>vistaRendicontazione</var>
	 * @param vistaRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,VistaRendicontazione vistaRendicontazione) throws SerializerException {
		this.objToXml(out, VistaRendicontazione.class, vistaRendicontazione, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>vistaRendicontazione</var> of type {@link it.govpay.orm.VistaRendicontazione}
	 * 
	 * @param out OutputStream to serialize the object <var>vistaRendicontazione</var>
	 * @param vistaRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,VistaRendicontazione vistaRendicontazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, VistaRendicontazione.class, vistaRendicontazione, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>vistaRendicontazione</var> of type {@link it.govpay.orm.VistaRendicontazione}
	 * 
	 * @param vistaRendicontazione Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(VistaRendicontazione vistaRendicontazione) throws SerializerException {
		return this.objToXml(VistaRendicontazione.class, vistaRendicontazione, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>vistaRendicontazione</var> of type {@link it.govpay.orm.VistaRendicontazione}
	 * 
	 * @param vistaRendicontazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(VistaRendicontazione vistaRendicontazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(VistaRendicontazione.class, vistaRendicontazione, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>vistaRendicontazione</var> of type {@link it.govpay.orm.VistaRendicontazione}
	 * 
	 * @param vistaRendicontazione Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(VistaRendicontazione vistaRendicontazione) throws SerializerException {
		return this.objToXml(VistaRendicontazione.class, vistaRendicontazione, false).toString();
	}
	/**
	 * Serialize to String the object <var>vistaRendicontazione</var> of type {@link it.govpay.orm.VistaRendicontazione}
	 * 
	 * @param vistaRendicontazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(VistaRendicontazione vistaRendicontazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(VistaRendicontazione.class, vistaRendicontazione, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-pagamento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idPagamento</var> of type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>idPagamento</var>
	 * @param idPagamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdPagamento idPagamento) throws SerializerException {
		this.objToXml(fileName, IdPagamento.class, idPagamento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idPagamento</var> of type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>idPagamento</var>
	 * @param idPagamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdPagamento idPagamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdPagamento.class, idPagamento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idPagamento</var> of type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param file Xml file to serialize the object <var>idPagamento</var>
	 * @param idPagamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdPagamento idPagamento) throws SerializerException {
		this.objToXml(file, IdPagamento.class, idPagamento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idPagamento</var> of type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param file Xml file to serialize the object <var>idPagamento</var>
	 * @param idPagamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdPagamento idPagamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdPagamento.class, idPagamento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idPagamento</var> of type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param out OutputStream to serialize the object <var>idPagamento</var>
	 * @param idPagamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdPagamento idPagamento) throws SerializerException {
		this.objToXml(out, IdPagamento.class, idPagamento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idPagamento</var> of type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param out OutputStream to serialize the object <var>idPagamento</var>
	 * @param idPagamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdPagamento idPagamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdPagamento.class, idPagamento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idPagamento</var> of type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param idPagamento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdPagamento idPagamento) throws SerializerException {
		return this.objToXml(IdPagamento.class, idPagamento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idPagamento</var> of type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param idPagamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdPagamento idPagamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdPagamento.class, idPagamento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idPagamento</var> of type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param idPagamento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdPagamento idPagamento) throws SerializerException {
		return this.objToXml(IdPagamento.class, idPagamento, false).toString();
	}
	/**
	 * Serialize to String the object <var>idPagamento</var> of type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param idPagamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdPagamento idPagamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdPagamento.class, idPagamento, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-tributo
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idTributo</var> of type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param fileName Xml file to serialize the object <var>idTributo</var>
	 * @param idTributo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdTributo idTributo) throws SerializerException {
		this.objToXml(fileName, IdTributo.class, idTributo, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idTributo</var> of type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param fileName Xml file to serialize the object <var>idTributo</var>
	 * @param idTributo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdTributo idTributo,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdTributo.class, idTributo, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idTributo</var> of type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param file Xml file to serialize the object <var>idTributo</var>
	 * @param idTributo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdTributo idTributo) throws SerializerException {
		this.objToXml(file, IdTributo.class, idTributo, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idTributo</var> of type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param file Xml file to serialize the object <var>idTributo</var>
	 * @param idTributo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdTributo idTributo,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdTributo.class, idTributo, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idTributo</var> of type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param out OutputStream to serialize the object <var>idTributo</var>
	 * @param idTributo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdTributo idTributo) throws SerializerException {
		this.objToXml(out, IdTributo.class, idTributo, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idTributo</var> of type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param out OutputStream to serialize the object <var>idTributo</var>
	 * @param idTributo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdTributo idTributo,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdTributo.class, idTributo, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idTributo</var> of type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param idTributo Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdTributo idTributo) throws SerializerException {
		return this.objToXml(IdTributo.class, idTributo, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idTributo</var> of type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param idTributo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdTributo idTributo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdTributo.class, idTributo, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idTributo</var> of type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param idTributo Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdTributo idTributo) throws SerializerException {
		return this.objToXml(IdTributo.class, idTributo, false).toString();
	}
	/**
	 * Serialize to String the object <var>idTributo</var> of type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param idTributo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdTributo idTributo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdTributo.class, idTributo, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-iban-accredito
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idIbanAccredito</var> of type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param fileName Xml file to serialize the object <var>idIbanAccredito</var>
	 * @param idIbanAccredito Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdIbanAccredito idIbanAccredito) throws SerializerException {
		this.objToXml(fileName, IdIbanAccredito.class, idIbanAccredito, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idIbanAccredito</var> of type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param fileName Xml file to serialize the object <var>idIbanAccredito</var>
	 * @param idIbanAccredito Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdIbanAccredito idIbanAccredito,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdIbanAccredito.class, idIbanAccredito, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idIbanAccredito</var> of type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param file Xml file to serialize the object <var>idIbanAccredito</var>
	 * @param idIbanAccredito Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdIbanAccredito idIbanAccredito) throws SerializerException {
		this.objToXml(file, IdIbanAccredito.class, idIbanAccredito, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idIbanAccredito</var> of type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param file Xml file to serialize the object <var>idIbanAccredito</var>
	 * @param idIbanAccredito Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdIbanAccredito idIbanAccredito,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdIbanAccredito.class, idIbanAccredito, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idIbanAccredito</var> of type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param out OutputStream to serialize the object <var>idIbanAccredito</var>
	 * @param idIbanAccredito Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdIbanAccredito idIbanAccredito) throws SerializerException {
		this.objToXml(out, IdIbanAccredito.class, idIbanAccredito, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idIbanAccredito</var> of type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param out OutputStream to serialize the object <var>idIbanAccredito</var>
	 * @param idIbanAccredito Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdIbanAccredito idIbanAccredito,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdIbanAccredito.class, idIbanAccredito, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idIbanAccredito</var> of type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param idIbanAccredito Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdIbanAccredito idIbanAccredito) throws SerializerException {
		return this.objToXml(IdIbanAccredito.class, idIbanAccredito, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idIbanAccredito</var> of type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param idIbanAccredito Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdIbanAccredito idIbanAccredito,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdIbanAccredito.class, idIbanAccredito, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idIbanAccredito</var> of type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param idIbanAccredito Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdIbanAccredito idIbanAccredito) throws SerializerException {
		return this.objToXml(IdIbanAccredito.class, idIbanAccredito, false).toString();
	}
	/**
	 * Serialize to String the object <var>idIbanAccredito</var> of type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param idIbanAccredito Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdIbanAccredito idIbanAccredito,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdIbanAccredito.class, idIbanAccredito, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: NotificaAppIO
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>notificaAppIO</var> of type {@link it.govpay.orm.NotificaAppIO}
	 * 
	 * @param fileName Xml file to serialize the object <var>notificaAppIO</var>
	 * @param notificaAppIO Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,NotificaAppIO notificaAppIO) throws SerializerException {
		this.objToXml(fileName, NotificaAppIO.class, notificaAppIO, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>notificaAppIO</var> of type {@link it.govpay.orm.NotificaAppIO}
	 * 
	 * @param fileName Xml file to serialize the object <var>notificaAppIO</var>
	 * @param notificaAppIO Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,NotificaAppIO notificaAppIO,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, NotificaAppIO.class, notificaAppIO, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>notificaAppIO</var> of type {@link it.govpay.orm.NotificaAppIO}
	 * 
	 * @param file Xml file to serialize the object <var>notificaAppIO</var>
	 * @param notificaAppIO Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,NotificaAppIO notificaAppIO) throws SerializerException {
		this.objToXml(file, NotificaAppIO.class, notificaAppIO, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>notificaAppIO</var> of type {@link it.govpay.orm.NotificaAppIO}
	 * 
	 * @param file Xml file to serialize the object <var>notificaAppIO</var>
	 * @param notificaAppIO Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,NotificaAppIO notificaAppIO,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, NotificaAppIO.class, notificaAppIO, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>notificaAppIO</var> of type {@link it.govpay.orm.NotificaAppIO}
	 * 
	 * @param out OutputStream to serialize the object <var>notificaAppIO</var>
	 * @param notificaAppIO Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,NotificaAppIO notificaAppIO) throws SerializerException {
		this.objToXml(out, NotificaAppIO.class, notificaAppIO, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>notificaAppIO</var> of type {@link it.govpay.orm.NotificaAppIO}
	 * 
	 * @param out OutputStream to serialize the object <var>notificaAppIO</var>
	 * @param notificaAppIO Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,NotificaAppIO notificaAppIO,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, NotificaAppIO.class, notificaAppIO, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>notificaAppIO</var> of type {@link it.govpay.orm.NotificaAppIO}
	 * 
	 * @param notificaAppIO Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(NotificaAppIO notificaAppIO) throws SerializerException {
		return this.objToXml(NotificaAppIO.class, notificaAppIO, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>notificaAppIO</var> of type {@link it.govpay.orm.NotificaAppIO}
	 * 
	 * @param notificaAppIO Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(NotificaAppIO notificaAppIO,boolean prettyPrint) throws SerializerException {
		return this.objToXml(NotificaAppIO.class, notificaAppIO, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>notificaAppIO</var> of type {@link it.govpay.orm.NotificaAppIO}
	 * 
	 * @param notificaAppIO Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(NotificaAppIO notificaAppIO) throws SerializerException {
		return this.objToXml(NotificaAppIO.class, notificaAppIO, false).toString();
	}
	/**
	 * Serialize to String the object <var>notificaAppIO</var> of type {@link it.govpay.orm.NotificaAppIO}
	 * 
	 * @param notificaAppIO Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(NotificaAppIO notificaAppIO,boolean prettyPrint) throws SerializerException {
		return this.objToXml(NotificaAppIO.class, notificaAppIO, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Evento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>evento</var> of type {@link it.govpay.orm.Evento}
	 * 
	 * @param fileName Xml file to serialize the object <var>evento</var>
	 * @param evento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Evento evento) throws SerializerException {
		this.objToXml(fileName, Evento.class, evento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>evento</var> of type {@link it.govpay.orm.Evento}
	 * 
	 * @param fileName Xml file to serialize the object <var>evento</var>
	 * @param evento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Evento evento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Evento.class, evento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>evento</var> of type {@link it.govpay.orm.Evento}
	 * 
	 * @param file Xml file to serialize the object <var>evento</var>
	 * @param evento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Evento evento) throws SerializerException {
		this.objToXml(file, Evento.class, evento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>evento</var> of type {@link it.govpay.orm.Evento}
	 * 
	 * @param file Xml file to serialize the object <var>evento</var>
	 * @param evento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Evento evento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Evento.class, evento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>evento</var> of type {@link it.govpay.orm.Evento}
	 * 
	 * @param out OutputStream to serialize the object <var>evento</var>
	 * @param evento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Evento evento) throws SerializerException {
		this.objToXml(out, Evento.class, evento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>evento</var> of type {@link it.govpay.orm.Evento}
	 * 
	 * @param out OutputStream to serialize the object <var>evento</var>
	 * @param evento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Evento evento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Evento.class, evento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>evento</var> of type {@link it.govpay.orm.Evento}
	 * 
	 * @param evento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Evento evento) throws SerializerException {
		return this.objToXml(Evento.class, evento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>evento</var> of type {@link it.govpay.orm.Evento}
	 * 
	 * @param evento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Evento evento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Evento.class, evento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>evento</var> of type {@link it.govpay.orm.Evento}
	 * 
	 * @param evento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Evento evento) throws SerializerException {
		return this.objToXml(Evento.class, evento, false).toString();
	}
	/**
	 * Serialize to String the object <var>evento</var> of type {@link it.govpay.orm.Evento}
	 * 
	 * @param evento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Evento evento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Evento.class, evento, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-fr
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idFr</var> of type {@link it.govpay.orm.IdFr}
	 * 
	 * @param fileName Xml file to serialize the object <var>idFr</var>
	 * @param idFr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdFr idFr) throws SerializerException {
		this.objToXml(fileName, IdFr.class, idFr, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idFr</var> of type {@link it.govpay.orm.IdFr}
	 * 
	 * @param fileName Xml file to serialize the object <var>idFr</var>
	 * @param idFr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdFr idFr,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdFr.class, idFr, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idFr</var> of type {@link it.govpay.orm.IdFr}
	 * 
	 * @param file Xml file to serialize the object <var>idFr</var>
	 * @param idFr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdFr idFr) throws SerializerException {
		this.objToXml(file, IdFr.class, idFr, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idFr</var> of type {@link it.govpay.orm.IdFr}
	 * 
	 * @param file Xml file to serialize the object <var>idFr</var>
	 * @param idFr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdFr idFr,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdFr.class, idFr, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idFr</var> of type {@link it.govpay.orm.IdFr}
	 * 
	 * @param out OutputStream to serialize the object <var>idFr</var>
	 * @param idFr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdFr idFr) throws SerializerException {
		this.objToXml(out, IdFr.class, idFr, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idFr</var> of type {@link it.govpay.orm.IdFr}
	 * 
	 * @param out OutputStream to serialize the object <var>idFr</var>
	 * @param idFr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdFr idFr,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdFr.class, idFr, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idFr</var> of type {@link it.govpay.orm.IdFr}
	 * 
	 * @param idFr Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdFr idFr) throws SerializerException {
		return this.objToXml(IdFr.class, idFr, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idFr</var> of type {@link it.govpay.orm.IdFr}
	 * 
	 * @param idFr Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdFr idFr,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdFr.class, idFr, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idFr</var> of type {@link it.govpay.orm.IdFr}
	 * 
	 * @param idFr Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdFr idFr) throws SerializerException {
		return this.objToXml(IdFr.class, idFr, false).toString();
	}
	/**
	 * Serialize to String the object <var>idFr</var> of type {@link it.govpay.orm.IdFr}
	 * 
	 * @param idFr Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdFr idFr,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdFr.class, idFr, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-tipo-tributo
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idTipoTributo</var> of type {@link it.govpay.orm.IdTipoTributo}
	 * 
	 * @param fileName Xml file to serialize the object <var>idTipoTributo</var>
	 * @param idTipoTributo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdTipoTributo idTipoTributo) throws SerializerException {
		this.objToXml(fileName, IdTipoTributo.class, idTipoTributo, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idTipoTributo</var> of type {@link it.govpay.orm.IdTipoTributo}
	 * 
	 * @param fileName Xml file to serialize the object <var>idTipoTributo</var>
	 * @param idTipoTributo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdTipoTributo idTipoTributo,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdTipoTributo.class, idTipoTributo, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idTipoTributo</var> of type {@link it.govpay.orm.IdTipoTributo}
	 * 
	 * @param file Xml file to serialize the object <var>idTipoTributo</var>
	 * @param idTipoTributo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdTipoTributo idTipoTributo) throws SerializerException {
		this.objToXml(file, IdTipoTributo.class, idTipoTributo, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idTipoTributo</var> of type {@link it.govpay.orm.IdTipoTributo}
	 * 
	 * @param file Xml file to serialize the object <var>idTipoTributo</var>
	 * @param idTipoTributo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdTipoTributo idTipoTributo,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdTipoTributo.class, idTipoTributo, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idTipoTributo</var> of type {@link it.govpay.orm.IdTipoTributo}
	 * 
	 * @param out OutputStream to serialize the object <var>idTipoTributo</var>
	 * @param idTipoTributo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdTipoTributo idTipoTributo) throws SerializerException {
		this.objToXml(out, IdTipoTributo.class, idTipoTributo, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idTipoTributo</var> of type {@link it.govpay.orm.IdTipoTributo}
	 * 
	 * @param out OutputStream to serialize the object <var>idTipoTributo</var>
	 * @param idTipoTributo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdTipoTributo idTipoTributo,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdTipoTributo.class, idTipoTributo, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idTipoTributo</var> of type {@link it.govpay.orm.IdTipoTributo}
	 * 
	 * @param idTipoTributo Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdTipoTributo idTipoTributo) throws SerializerException {
		return this.objToXml(IdTipoTributo.class, idTipoTributo, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idTipoTributo</var> of type {@link it.govpay.orm.IdTipoTributo}
	 * 
	 * @param idTipoTributo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdTipoTributo idTipoTributo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdTipoTributo.class, idTipoTributo, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idTipoTributo</var> of type {@link it.govpay.orm.IdTipoTributo}
	 * 
	 * @param idTipoTributo Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdTipoTributo idTipoTributo) throws SerializerException {
		return this.objToXml(IdTipoTributo.class, idTipoTributo, false).toString();
	}
	/**
	 * Serialize to String the object <var>idTipoTributo</var> of type {@link it.govpay.orm.IdTipoTributo}
	 * 
	 * @param idTipoTributo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdTipoTributo idTipoTributo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdTipoTributo.class, idTipoTributo, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-batch
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idBatch</var> of type {@link it.govpay.orm.IdBatch}
	 * 
	 * @param fileName Xml file to serialize the object <var>idBatch</var>
	 * @param idBatch Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdBatch idBatch) throws SerializerException {
		this.objToXml(fileName, IdBatch.class, idBatch, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idBatch</var> of type {@link it.govpay.orm.IdBatch}
	 * 
	 * @param fileName Xml file to serialize the object <var>idBatch</var>
	 * @param idBatch Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdBatch idBatch,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdBatch.class, idBatch, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idBatch</var> of type {@link it.govpay.orm.IdBatch}
	 * 
	 * @param file Xml file to serialize the object <var>idBatch</var>
	 * @param idBatch Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdBatch idBatch) throws SerializerException {
		this.objToXml(file, IdBatch.class, idBatch, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idBatch</var> of type {@link it.govpay.orm.IdBatch}
	 * 
	 * @param file Xml file to serialize the object <var>idBatch</var>
	 * @param idBatch Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdBatch idBatch,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdBatch.class, idBatch, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idBatch</var> of type {@link it.govpay.orm.IdBatch}
	 * 
	 * @param out OutputStream to serialize the object <var>idBatch</var>
	 * @param idBatch Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdBatch idBatch) throws SerializerException {
		this.objToXml(out, IdBatch.class, idBatch, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idBatch</var> of type {@link it.govpay.orm.IdBatch}
	 * 
	 * @param out OutputStream to serialize the object <var>idBatch</var>
	 * @param idBatch Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdBatch idBatch,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdBatch.class, idBatch, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idBatch</var> of type {@link it.govpay.orm.IdBatch}
	 * 
	 * @param idBatch Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdBatch idBatch) throws SerializerException {
		return this.objToXml(IdBatch.class, idBatch, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idBatch</var> of type {@link it.govpay.orm.IdBatch}
	 * 
	 * @param idBatch Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdBatch idBatch,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdBatch.class, idBatch, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idBatch</var> of type {@link it.govpay.orm.IdBatch}
	 * 
	 * @param idBatch Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdBatch idBatch) throws SerializerException {
		return this.objToXml(IdBatch.class, idBatch, false).toString();
	}
	/**
	 * Serialize to String the object <var>idBatch</var> of type {@link it.govpay.orm.IdBatch}
	 * 
	 * @param idBatch Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdBatch idBatch,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdBatch.class, idBatch, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: VistaPagamentoPortale
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>vistaPagamentoPortale</var> of type {@link it.govpay.orm.VistaPagamentoPortale}
	 * 
	 * @param fileName Xml file to serialize the object <var>vistaPagamentoPortale</var>
	 * @param vistaPagamentoPortale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,VistaPagamentoPortale vistaPagamentoPortale) throws SerializerException {
		this.objToXml(fileName, VistaPagamentoPortale.class, vistaPagamentoPortale, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>vistaPagamentoPortale</var> of type {@link it.govpay.orm.VistaPagamentoPortale}
	 * 
	 * @param fileName Xml file to serialize the object <var>vistaPagamentoPortale</var>
	 * @param vistaPagamentoPortale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,VistaPagamentoPortale vistaPagamentoPortale,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, VistaPagamentoPortale.class, vistaPagamentoPortale, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>vistaPagamentoPortale</var> of type {@link it.govpay.orm.VistaPagamentoPortale}
	 * 
	 * @param file Xml file to serialize the object <var>vistaPagamentoPortale</var>
	 * @param vistaPagamentoPortale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,VistaPagamentoPortale vistaPagamentoPortale) throws SerializerException {
		this.objToXml(file, VistaPagamentoPortale.class, vistaPagamentoPortale, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>vistaPagamentoPortale</var> of type {@link it.govpay.orm.VistaPagamentoPortale}
	 * 
	 * @param file Xml file to serialize the object <var>vistaPagamentoPortale</var>
	 * @param vistaPagamentoPortale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,VistaPagamentoPortale vistaPagamentoPortale,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, VistaPagamentoPortale.class, vistaPagamentoPortale, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>vistaPagamentoPortale</var> of type {@link it.govpay.orm.VistaPagamentoPortale}
	 * 
	 * @param out OutputStream to serialize the object <var>vistaPagamentoPortale</var>
	 * @param vistaPagamentoPortale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,VistaPagamentoPortale vistaPagamentoPortale) throws SerializerException {
		this.objToXml(out, VistaPagamentoPortale.class, vistaPagamentoPortale, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>vistaPagamentoPortale</var> of type {@link it.govpay.orm.VistaPagamentoPortale}
	 * 
	 * @param out OutputStream to serialize the object <var>vistaPagamentoPortale</var>
	 * @param vistaPagamentoPortale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,VistaPagamentoPortale vistaPagamentoPortale,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, VistaPagamentoPortale.class, vistaPagamentoPortale, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>vistaPagamentoPortale</var> of type {@link it.govpay.orm.VistaPagamentoPortale}
	 * 
	 * @param vistaPagamentoPortale Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(VistaPagamentoPortale vistaPagamentoPortale) throws SerializerException {
		return this.objToXml(VistaPagamentoPortale.class, vistaPagamentoPortale, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>vistaPagamentoPortale</var> of type {@link it.govpay.orm.VistaPagamentoPortale}
	 * 
	 * @param vistaPagamentoPortale Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(VistaPagamentoPortale vistaPagamentoPortale,boolean prettyPrint) throws SerializerException {
		return this.objToXml(VistaPagamentoPortale.class, vistaPagamentoPortale, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>vistaPagamentoPortale</var> of type {@link it.govpay.orm.VistaPagamentoPortale}
	 * 
	 * @param vistaPagamentoPortale Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(VistaPagamentoPortale vistaPagamentoPortale) throws SerializerException {
		return this.objToXml(VistaPagamentoPortale.class, vistaPagamentoPortale, false).toString();
	}
	/**
	 * Serialize to String the object <var>vistaPagamentoPortale</var> of type {@link it.govpay.orm.VistaPagamentoPortale}
	 * 
	 * @param vistaPagamentoPortale Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(VistaPagamentoPortale vistaPagamentoPortale,boolean prettyPrint) throws SerializerException {
		return this.objToXml(VistaPagamentoPortale.class, vistaPagamentoPortale, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-tabella-controparti
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idTabellaControparti</var> of type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param fileName Xml file to serialize the object <var>idTabellaControparti</var>
	 * @param idTabellaControparti Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdTabellaControparti idTabellaControparti) throws SerializerException {
		this.objToXml(fileName, IdTabellaControparti.class, idTabellaControparti, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idTabellaControparti</var> of type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param fileName Xml file to serialize the object <var>idTabellaControparti</var>
	 * @param idTabellaControparti Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdTabellaControparti idTabellaControparti,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdTabellaControparti.class, idTabellaControparti, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idTabellaControparti</var> of type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param file Xml file to serialize the object <var>idTabellaControparti</var>
	 * @param idTabellaControparti Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdTabellaControparti idTabellaControparti) throws SerializerException {
		this.objToXml(file, IdTabellaControparti.class, idTabellaControparti, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idTabellaControparti</var> of type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param file Xml file to serialize the object <var>idTabellaControparti</var>
	 * @param idTabellaControparti Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdTabellaControparti idTabellaControparti,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdTabellaControparti.class, idTabellaControparti, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idTabellaControparti</var> of type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param out OutputStream to serialize the object <var>idTabellaControparti</var>
	 * @param idTabellaControparti Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdTabellaControparti idTabellaControparti) throws SerializerException {
		this.objToXml(out, IdTabellaControparti.class, idTabellaControparti, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idTabellaControparti</var> of type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param out OutputStream to serialize the object <var>idTabellaControparti</var>
	 * @param idTabellaControparti Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdTabellaControparti idTabellaControparti,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdTabellaControparti.class, idTabellaControparti, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idTabellaControparti</var> of type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param idTabellaControparti Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdTabellaControparti idTabellaControparti) throws SerializerException {
		return this.objToXml(IdTabellaControparti.class, idTabellaControparti, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idTabellaControparti</var> of type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param idTabellaControparti Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdTabellaControparti idTabellaControparti,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdTabellaControparti.class, idTabellaControparti, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idTabellaControparti</var> of type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param idTabellaControparti Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdTabellaControparti idTabellaControparti) throws SerializerException {
		return this.objToXml(IdTabellaControparti.class, idTabellaControparti, false).toString();
	}
	/**
	 * Serialize to String the object <var>idTabellaControparti</var> of type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param idTabellaControparti Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdTabellaControparti idTabellaControparti,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdTabellaControparti.class, idTabellaControparti, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: TipoTributo
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>tipoTributo</var> of type {@link it.govpay.orm.TipoTributo}
	 * 
	 * @param fileName Xml file to serialize the object <var>tipoTributo</var>
	 * @param tipoTributo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,TipoTributo tipoTributo) throws SerializerException {
		this.objToXml(fileName, TipoTributo.class, tipoTributo, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>tipoTributo</var> of type {@link it.govpay.orm.TipoTributo}
	 * 
	 * @param fileName Xml file to serialize the object <var>tipoTributo</var>
	 * @param tipoTributo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,TipoTributo tipoTributo,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, TipoTributo.class, tipoTributo, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>tipoTributo</var> of type {@link it.govpay.orm.TipoTributo}
	 * 
	 * @param file Xml file to serialize the object <var>tipoTributo</var>
	 * @param tipoTributo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,TipoTributo tipoTributo) throws SerializerException {
		this.objToXml(file, TipoTributo.class, tipoTributo, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>tipoTributo</var> of type {@link it.govpay.orm.TipoTributo}
	 * 
	 * @param file Xml file to serialize the object <var>tipoTributo</var>
	 * @param tipoTributo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,TipoTributo tipoTributo,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, TipoTributo.class, tipoTributo, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>tipoTributo</var> of type {@link it.govpay.orm.TipoTributo}
	 * 
	 * @param out OutputStream to serialize the object <var>tipoTributo</var>
	 * @param tipoTributo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,TipoTributo tipoTributo) throws SerializerException {
		this.objToXml(out, TipoTributo.class, tipoTributo, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>tipoTributo</var> of type {@link it.govpay.orm.TipoTributo}
	 * 
	 * @param out OutputStream to serialize the object <var>tipoTributo</var>
	 * @param tipoTributo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,TipoTributo tipoTributo,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, TipoTributo.class, tipoTributo, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>tipoTributo</var> of type {@link it.govpay.orm.TipoTributo}
	 * 
	 * @param tipoTributo Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(TipoTributo tipoTributo) throws SerializerException {
		return this.objToXml(TipoTributo.class, tipoTributo, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>tipoTributo</var> of type {@link it.govpay.orm.TipoTributo}
	 * 
	 * @param tipoTributo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(TipoTributo tipoTributo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(TipoTributo.class, tipoTributo, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>tipoTributo</var> of type {@link it.govpay.orm.TipoTributo}
	 * 
	 * @param tipoTributo Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(TipoTributo tipoTributo) throws SerializerException {
		return this.objToXml(TipoTributo.class, tipoTributo, false).toString();
	}
	/**
	 * Serialize to String the object <var>tipoTributo</var> of type {@link it.govpay.orm.TipoTributo}
	 * 
	 * @param tipoTributo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(TipoTributo tipoTributo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(TipoTributo.class, tipoTributo, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-conto-accredito
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idContoAccredito</var> of type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param fileName Xml file to serialize the object <var>idContoAccredito</var>
	 * @param idContoAccredito Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdContoAccredito idContoAccredito) throws SerializerException {
		this.objToXml(fileName, IdContoAccredito.class, idContoAccredito, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idContoAccredito</var> of type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param fileName Xml file to serialize the object <var>idContoAccredito</var>
	 * @param idContoAccredito Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdContoAccredito idContoAccredito,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdContoAccredito.class, idContoAccredito, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idContoAccredito</var> of type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param file Xml file to serialize the object <var>idContoAccredito</var>
	 * @param idContoAccredito Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdContoAccredito idContoAccredito) throws SerializerException {
		this.objToXml(file, IdContoAccredito.class, idContoAccredito, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idContoAccredito</var> of type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param file Xml file to serialize the object <var>idContoAccredito</var>
	 * @param idContoAccredito Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdContoAccredito idContoAccredito,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdContoAccredito.class, idContoAccredito, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idContoAccredito</var> of type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param out OutputStream to serialize the object <var>idContoAccredito</var>
	 * @param idContoAccredito Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdContoAccredito idContoAccredito) throws SerializerException {
		this.objToXml(out, IdContoAccredito.class, idContoAccredito, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idContoAccredito</var> of type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param out OutputStream to serialize the object <var>idContoAccredito</var>
	 * @param idContoAccredito Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdContoAccredito idContoAccredito,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdContoAccredito.class, idContoAccredito, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idContoAccredito</var> of type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param idContoAccredito Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdContoAccredito idContoAccredito) throws SerializerException {
		return this.objToXml(IdContoAccredito.class, idContoAccredito, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idContoAccredito</var> of type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param idContoAccredito Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdContoAccredito idContoAccredito,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdContoAccredito.class, idContoAccredito, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idContoAccredito</var> of type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param idContoAccredito Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdContoAccredito idContoAccredito) throws SerializerException {
		return this.objToXml(IdContoAccredito.class, idContoAccredito, false).toString();
	}
	/**
	 * Serialize to String the object <var>idContoAccredito</var> of type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param idContoAccredito Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdContoAccredito idContoAccredito,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdContoAccredito.class, idContoAccredito, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Tributo
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>tributo</var> of type {@link it.govpay.orm.Tributo}
	 * 
	 * @param fileName Xml file to serialize the object <var>tributo</var>
	 * @param tributo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Tributo tributo) throws SerializerException {
		this.objToXml(fileName, Tributo.class, tributo, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>tributo</var> of type {@link it.govpay.orm.Tributo}
	 * 
	 * @param fileName Xml file to serialize the object <var>tributo</var>
	 * @param tributo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Tributo tributo,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Tributo.class, tributo, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>tributo</var> of type {@link it.govpay.orm.Tributo}
	 * 
	 * @param file Xml file to serialize the object <var>tributo</var>
	 * @param tributo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Tributo tributo) throws SerializerException {
		this.objToXml(file, Tributo.class, tributo, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>tributo</var> of type {@link it.govpay.orm.Tributo}
	 * 
	 * @param file Xml file to serialize the object <var>tributo</var>
	 * @param tributo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Tributo tributo,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Tributo.class, tributo, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>tributo</var> of type {@link it.govpay.orm.Tributo}
	 * 
	 * @param out OutputStream to serialize the object <var>tributo</var>
	 * @param tributo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Tributo tributo) throws SerializerException {
		this.objToXml(out, Tributo.class, tributo, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>tributo</var> of type {@link it.govpay.orm.Tributo}
	 * 
	 * @param out OutputStream to serialize the object <var>tributo</var>
	 * @param tributo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Tributo tributo,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Tributo.class, tributo, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>tributo</var> of type {@link it.govpay.orm.Tributo}
	 * 
	 * @param tributo Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Tributo tributo) throws SerializerException {
		return this.objToXml(Tributo.class, tributo, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>tributo</var> of type {@link it.govpay.orm.Tributo}
	 * 
	 * @param tributo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Tributo tributo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Tributo.class, tributo, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>tributo</var> of type {@link it.govpay.orm.Tributo}
	 * 
	 * @param tributo Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Tributo tributo) throws SerializerException {
		return this.objToXml(Tributo.class, tributo, false).toString();
	}
	/**
	 * Serialize to String the object <var>tributo</var> of type {@link it.govpay.orm.Tributo}
	 * 
	 * @param tributo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Tributo tributo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Tributo.class, tributo, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: IUV
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>iuv</var> of type {@link it.govpay.orm.IUV}
	 * 
	 * @param fileName Xml file to serialize the object <var>iuv</var>
	 * @param iuv Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IUV iuv) throws SerializerException {
		this.objToXml(fileName, IUV.class, iuv, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>iuv</var> of type {@link it.govpay.orm.IUV}
	 * 
	 * @param fileName Xml file to serialize the object <var>iuv</var>
	 * @param iuv Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IUV iuv,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IUV.class, iuv, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>iuv</var> of type {@link it.govpay.orm.IUV}
	 * 
	 * @param file Xml file to serialize the object <var>iuv</var>
	 * @param iuv Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IUV iuv) throws SerializerException {
		this.objToXml(file, IUV.class, iuv, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>iuv</var> of type {@link it.govpay.orm.IUV}
	 * 
	 * @param file Xml file to serialize the object <var>iuv</var>
	 * @param iuv Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IUV iuv,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IUV.class, iuv, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>iuv</var> of type {@link it.govpay.orm.IUV}
	 * 
	 * @param out OutputStream to serialize the object <var>iuv</var>
	 * @param iuv Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IUV iuv) throws SerializerException {
		this.objToXml(out, IUV.class, iuv, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>iuv</var> of type {@link it.govpay.orm.IUV}
	 * 
	 * @param out OutputStream to serialize the object <var>iuv</var>
	 * @param iuv Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IUV iuv,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IUV.class, iuv, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>iuv</var> of type {@link it.govpay.orm.IUV}
	 * 
	 * @param iuv Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IUV iuv) throws SerializerException {
		return this.objToXml(IUV.class, iuv, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>iuv</var> of type {@link it.govpay.orm.IUV}
	 * 
	 * @param iuv Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IUV iuv,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IUV.class, iuv, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>iuv</var> of type {@link it.govpay.orm.IUV}
	 * 
	 * @param iuv Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IUV iuv) throws SerializerException {
		return this.objToXml(IUV.class, iuv, false).toString();
	}
	/**
	 * Serialize to String the object <var>iuv</var> of type {@link it.govpay.orm.IUV}
	 * 
	 * @param iuv Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IUV iuv,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IUV.class, iuv, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Documento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>documento</var> of type {@link it.govpay.orm.Documento}
	 * 
	 * @param fileName Xml file to serialize the object <var>documento</var>
	 * @param documento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Documento documento) throws SerializerException {
		this.objToXml(fileName, Documento.class, documento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>documento</var> of type {@link it.govpay.orm.Documento}
	 * 
	 * @param fileName Xml file to serialize the object <var>documento</var>
	 * @param documento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Documento documento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Documento.class, documento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>documento</var> of type {@link it.govpay.orm.Documento}
	 * 
	 * @param file Xml file to serialize the object <var>documento</var>
	 * @param documento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Documento documento) throws SerializerException {
		this.objToXml(file, Documento.class, documento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>documento</var> of type {@link it.govpay.orm.Documento}
	 * 
	 * @param file Xml file to serialize the object <var>documento</var>
	 * @param documento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Documento documento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Documento.class, documento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>documento</var> of type {@link it.govpay.orm.Documento}
	 * 
	 * @param out OutputStream to serialize the object <var>documento</var>
	 * @param documento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Documento documento) throws SerializerException {
		this.objToXml(out, Documento.class, documento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>documento</var> of type {@link it.govpay.orm.Documento}
	 * 
	 * @param out OutputStream to serialize the object <var>documento</var>
	 * @param documento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Documento documento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Documento.class, documento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>documento</var> of type {@link it.govpay.orm.Documento}
	 * 
	 * @param documento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Documento documento) throws SerializerException {
		return this.objToXml(Documento.class, documento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>documento</var> of type {@link it.govpay.orm.Documento}
	 * 
	 * @param documento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Documento documento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Documento.class, documento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>documento</var> of type {@link it.govpay.orm.Documento}
	 * 
	 * @param documento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Documento documento) throws SerializerException {
		return this.objToXml(Documento.class, documento, false).toString();
	}
	/**
	 * Serialize to String the object <var>documento</var> of type {@link it.govpay.orm.Documento}
	 * 
	 * @param documento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Documento documento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Documento.class, documento, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Stampa
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>stampa</var> of type {@link it.govpay.orm.Stampa}
	 * 
	 * @param fileName Xml file to serialize the object <var>stampa</var>
	 * @param stampa Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Stampa stampa) throws SerializerException {
		this.objToXml(fileName, Stampa.class, stampa, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>stampa</var> of type {@link it.govpay.orm.Stampa}
	 * 
	 * @param fileName Xml file to serialize the object <var>stampa</var>
	 * @param stampa Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Stampa stampa,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Stampa.class, stampa, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>stampa</var> of type {@link it.govpay.orm.Stampa}
	 * 
	 * @param file Xml file to serialize the object <var>stampa</var>
	 * @param stampa Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Stampa stampa) throws SerializerException {
		this.objToXml(file, Stampa.class, stampa, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>stampa</var> of type {@link it.govpay.orm.Stampa}
	 * 
	 * @param file Xml file to serialize the object <var>stampa</var>
	 * @param stampa Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Stampa stampa,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Stampa.class, stampa, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>stampa</var> of type {@link it.govpay.orm.Stampa}
	 * 
	 * @param out OutputStream to serialize the object <var>stampa</var>
	 * @param stampa Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Stampa stampa) throws SerializerException {
		this.objToXml(out, Stampa.class, stampa, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>stampa</var> of type {@link it.govpay.orm.Stampa}
	 * 
	 * @param out OutputStream to serialize the object <var>stampa</var>
	 * @param stampa Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Stampa stampa,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Stampa.class, stampa, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>stampa</var> of type {@link it.govpay.orm.Stampa}
	 * 
	 * @param stampa Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Stampa stampa) throws SerializerException {
		return this.objToXml(Stampa.class, stampa, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>stampa</var> of type {@link it.govpay.orm.Stampa}
	 * 
	 * @param stampa Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Stampa stampa,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Stampa.class, stampa, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>stampa</var> of type {@link it.govpay.orm.Stampa}
	 * 
	 * @param stampa Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Stampa stampa) throws SerializerException {
		return this.objToXml(Stampa.class, stampa, false).toString();
	}
	/**
	 * Serialize to String the object <var>stampa</var> of type {@link it.govpay.orm.Stampa}
	 * 
	 * @param stampa Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Stampa stampa,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Stampa.class, stampa, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-connettore
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idConnettore</var> of type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param fileName Xml file to serialize the object <var>idConnettore</var>
	 * @param idConnettore Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdConnettore idConnettore) throws SerializerException {
		this.objToXml(fileName, IdConnettore.class, idConnettore, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idConnettore</var> of type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param fileName Xml file to serialize the object <var>idConnettore</var>
	 * @param idConnettore Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdConnettore idConnettore,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdConnettore.class, idConnettore, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idConnettore</var> of type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param file Xml file to serialize the object <var>idConnettore</var>
	 * @param idConnettore Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdConnettore idConnettore) throws SerializerException {
		this.objToXml(file, IdConnettore.class, idConnettore, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idConnettore</var> of type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param file Xml file to serialize the object <var>idConnettore</var>
	 * @param idConnettore Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdConnettore idConnettore,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdConnettore.class, idConnettore, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idConnettore</var> of type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param out OutputStream to serialize the object <var>idConnettore</var>
	 * @param idConnettore Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdConnettore idConnettore) throws SerializerException {
		this.objToXml(out, IdConnettore.class, idConnettore, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idConnettore</var> of type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param out OutputStream to serialize the object <var>idConnettore</var>
	 * @param idConnettore Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdConnettore idConnettore,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdConnettore.class, idConnettore, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idConnettore</var> of type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param idConnettore Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdConnettore idConnettore) throws SerializerException {
		return this.objToXml(IdConnettore.class, idConnettore, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idConnettore</var> of type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param idConnettore Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdConnettore idConnettore,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdConnettore.class, idConnettore, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idConnettore</var> of type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param idConnettore Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdConnettore idConnettore) throws SerializerException {
		return this.objToXml(IdConnettore.class, idConnettore, false).toString();
	}
	/**
	 * Serialize to String the object <var>idConnettore</var> of type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param idConnettore Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdConnettore idConnettore,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdConnettore.class, idConnettore, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: VistaRiscossioni
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>vistaRiscossioni</var> of type {@link it.govpay.orm.VistaRiscossioni}
	 * 
	 * @param fileName Xml file to serialize the object <var>vistaRiscossioni</var>
	 * @param vistaRiscossioni Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,VistaRiscossioni vistaRiscossioni) throws SerializerException {
		this.objToXml(fileName, VistaRiscossioni.class, vistaRiscossioni, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>vistaRiscossioni</var> of type {@link it.govpay.orm.VistaRiscossioni}
	 * 
	 * @param fileName Xml file to serialize the object <var>vistaRiscossioni</var>
	 * @param vistaRiscossioni Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,VistaRiscossioni vistaRiscossioni,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, VistaRiscossioni.class, vistaRiscossioni, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>vistaRiscossioni</var> of type {@link it.govpay.orm.VistaRiscossioni}
	 * 
	 * @param file Xml file to serialize the object <var>vistaRiscossioni</var>
	 * @param vistaRiscossioni Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,VistaRiscossioni vistaRiscossioni) throws SerializerException {
		this.objToXml(file, VistaRiscossioni.class, vistaRiscossioni, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>vistaRiscossioni</var> of type {@link it.govpay.orm.VistaRiscossioni}
	 * 
	 * @param file Xml file to serialize the object <var>vistaRiscossioni</var>
	 * @param vistaRiscossioni Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,VistaRiscossioni vistaRiscossioni,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, VistaRiscossioni.class, vistaRiscossioni, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>vistaRiscossioni</var> of type {@link it.govpay.orm.VistaRiscossioni}
	 * 
	 * @param out OutputStream to serialize the object <var>vistaRiscossioni</var>
	 * @param vistaRiscossioni Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,VistaRiscossioni vistaRiscossioni) throws SerializerException {
		this.objToXml(out, VistaRiscossioni.class, vistaRiscossioni, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>vistaRiscossioni</var> of type {@link it.govpay.orm.VistaRiscossioni}
	 * 
	 * @param out OutputStream to serialize the object <var>vistaRiscossioni</var>
	 * @param vistaRiscossioni Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,VistaRiscossioni vistaRiscossioni,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, VistaRiscossioni.class, vistaRiscossioni, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>vistaRiscossioni</var> of type {@link it.govpay.orm.VistaRiscossioni}
	 * 
	 * @param vistaRiscossioni Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(VistaRiscossioni vistaRiscossioni) throws SerializerException {
		return this.objToXml(VistaRiscossioni.class, vistaRiscossioni, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>vistaRiscossioni</var> of type {@link it.govpay.orm.VistaRiscossioni}
	 * 
	 * @param vistaRiscossioni Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(VistaRiscossioni vistaRiscossioni,boolean prettyPrint) throws SerializerException {
		return this.objToXml(VistaRiscossioni.class, vistaRiscossioni, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>vistaRiscossioni</var> of type {@link it.govpay.orm.VistaRiscossioni}
	 * 
	 * @param vistaRiscossioni Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(VistaRiscossioni vistaRiscossioni) throws SerializerException {
		return this.objToXml(VistaRiscossioni.class, vistaRiscossioni, false).toString();
	}
	/**
	 * Serialize to String the object <var>vistaRiscossioni</var> of type {@link it.govpay.orm.VistaRiscossioni}
	 * 
	 * @param vistaRiscossioni Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(VistaRiscossioni vistaRiscossioni,boolean prettyPrint) throws SerializerException {
		return this.objToXml(VistaRiscossioni.class, vistaRiscossioni, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-evento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idEvento</var> of type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param fileName Xml file to serialize the object <var>idEvento</var>
	 * @param idEvento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdEvento idEvento) throws SerializerException {
		this.objToXml(fileName, IdEvento.class, idEvento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idEvento</var> of type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param fileName Xml file to serialize the object <var>idEvento</var>
	 * @param idEvento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdEvento idEvento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdEvento.class, idEvento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idEvento</var> of type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param file Xml file to serialize the object <var>idEvento</var>
	 * @param idEvento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdEvento idEvento) throws SerializerException {
		this.objToXml(file, IdEvento.class, idEvento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idEvento</var> of type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param file Xml file to serialize the object <var>idEvento</var>
	 * @param idEvento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdEvento idEvento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdEvento.class, idEvento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idEvento</var> of type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param out OutputStream to serialize the object <var>idEvento</var>
	 * @param idEvento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdEvento idEvento) throws SerializerException {
		this.objToXml(out, IdEvento.class, idEvento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idEvento</var> of type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param out OutputStream to serialize the object <var>idEvento</var>
	 * @param idEvento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdEvento idEvento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdEvento.class, idEvento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idEvento</var> of type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param idEvento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdEvento idEvento) throws SerializerException {
		return this.objToXml(IdEvento.class, idEvento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idEvento</var> of type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param idEvento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdEvento idEvento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdEvento.class, idEvento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idEvento</var> of type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param idEvento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdEvento idEvento) throws SerializerException {
		return this.objToXml(IdEvento.class, idEvento, false).toString();
	}
	/**
	 * Serialize to String the object <var>idEvento</var> of type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param idEvento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdEvento idEvento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdEvento.class, idEvento, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Connettore
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>connettore</var> of type {@link it.govpay.orm.Connettore}
	 * 
	 * @param fileName Xml file to serialize the object <var>connettore</var>
	 * @param connettore Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Connettore connettore) throws SerializerException {
		this.objToXml(fileName, Connettore.class, connettore, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>connettore</var> of type {@link it.govpay.orm.Connettore}
	 * 
	 * @param fileName Xml file to serialize the object <var>connettore</var>
	 * @param connettore Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Connettore connettore,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Connettore.class, connettore, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>connettore</var> of type {@link it.govpay.orm.Connettore}
	 * 
	 * @param file Xml file to serialize the object <var>connettore</var>
	 * @param connettore Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Connettore connettore) throws SerializerException {
		this.objToXml(file, Connettore.class, connettore, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>connettore</var> of type {@link it.govpay.orm.Connettore}
	 * 
	 * @param file Xml file to serialize the object <var>connettore</var>
	 * @param connettore Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Connettore connettore,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Connettore.class, connettore, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>connettore</var> of type {@link it.govpay.orm.Connettore}
	 * 
	 * @param out OutputStream to serialize the object <var>connettore</var>
	 * @param connettore Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Connettore connettore) throws SerializerException {
		this.objToXml(out, Connettore.class, connettore, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>connettore</var> of type {@link it.govpay.orm.Connettore}
	 * 
	 * @param out OutputStream to serialize the object <var>connettore</var>
	 * @param connettore Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Connettore connettore,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Connettore.class, connettore, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>connettore</var> of type {@link it.govpay.orm.Connettore}
	 * 
	 * @param connettore Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Connettore connettore) throws SerializerException {
		return this.objToXml(Connettore.class, connettore, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>connettore</var> of type {@link it.govpay.orm.Connettore}
	 * 
	 * @param connettore Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Connettore connettore,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Connettore.class, connettore, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>connettore</var> of type {@link it.govpay.orm.Connettore}
	 * 
	 * @param connettore Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Connettore connettore) throws SerializerException {
		return this.objToXml(Connettore.class, connettore, false).toString();
	}
	/**
	 * Serialize to String the object <var>connettore</var> of type {@link it.govpay.orm.Connettore}
	 * 
	 * @param connettore Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Connettore connettore,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Connettore.class, connettore, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-configurazione
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idConfigurazione</var> of type {@link it.govpay.orm.IdConfigurazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>idConfigurazione</var>
	 * @param idConfigurazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdConfigurazione idConfigurazione) throws SerializerException {
		this.objToXml(fileName, IdConfigurazione.class, idConfigurazione, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idConfigurazione</var> of type {@link it.govpay.orm.IdConfigurazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>idConfigurazione</var>
	 * @param idConfigurazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdConfigurazione idConfigurazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdConfigurazione.class, idConfigurazione, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idConfigurazione</var> of type {@link it.govpay.orm.IdConfigurazione}
	 * 
	 * @param file Xml file to serialize the object <var>idConfigurazione</var>
	 * @param idConfigurazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdConfigurazione idConfigurazione) throws SerializerException {
		this.objToXml(file, IdConfigurazione.class, idConfigurazione, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idConfigurazione</var> of type {@link it.govpay.orm.IdConfigurazione}
	 * 
	 * @param file Xml file to serialize the object <var>idConfigurazione</var>
	 * @param idConfigurazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdConfigurazione idConfigurazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdConfigurazione.class, idConfigurazione, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idConfigurazione</var> of type {@link it.govpay.orm.IdConfigurazione}
	 * 
	 * @param out OutputStream to serialize the object <var>idConfigurazione</var>
	 * @param idConfigurazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdConfigurazione idConfigurazione) throws SerializerException {
		this.objToXml(out, IdConfigurazione.class, idConfigurazione, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idConfigurazione</var> of type {@link it.govpay.orm.IdConfigurazione}
	 * 
	 * @param out OutputStream to serialize the object <var>idConfigurazione</var>
	 * @param idConfigurazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdConfigurazione idConfigurazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdConfigurazione.class, idConfigurazione, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idConfigurazione</var> of type {@link it.govpay.orm.IdConfigurazione}
	 * 
	 * @param idConfigurazione Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdConfigurazione idConfigurazione) throws SerializerException {
		return this.objToXml(IdConfigurazione.class, idConfigurazione, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idConfigurazione</var> of type {@link it.govpay.orm.IdConfigurazione}
	 * 
	 * @param idConfigurazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdConfigurazione idConfigurazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdConfigurazione.class, idConfigurazione, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idConfigurazione</var> of type {@link it.govpay.orm.IdConfigurazione}
	 * 
	 * @param idConfigurazione Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdConfigurazione idConfigurazione) throws SerializerException {
		return this.objToXml(IdConfigurazione.class, idConfigurazione, false).toString();
	}
	/**
	 * Serialize to String the object <var>idConfigurazione</var> of type {@link it.govpay.orm.IdConfigurazione}
	 * 
	 * @param idConfigurazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdConfigurazione idConfigurazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdConfigurazione.class, idConfigurazione, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Uo
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>uo</var> of type {@link it.govpay.orm.Uo}
	 * 
	 * @param fileName Xml file to serialize the object <var>uo</var>
	 * @param uo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Uo uo) throws SerializerException {
		this.objToXml(fileName, Uo.class, uo, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>uo</var> of type {@link it.govpay.orm.Uo}
	 * 
	 * @param fileName Xml file to serialize the object <var>uo</var>
	 * @param uo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Uo uo,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Uo.class, uo, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>uo</var> of type {@link it.govpay.orm.Uo}
	 * 
	 * @param file Xml file to serialize the object <var>uo</var>
	 * @param uo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Uo uo) throws SerializerException {
		this.objToXml(file, Uo.class, uo, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>uo</var> of type {@link it.govpay.orm.Uo}
	 * 
	 * @param file Xml file to serialize the object <var>uo</var>
	 * @param uo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Uo uo,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Uo.class, uo, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>uo</var> of type {@link it.govpay.orm.Uo}
	 * 
	 * @param out OutputStream to serialize the object <var>uo</var>
	 * @param uo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Uo uo) throws SerializerException {
		this.objToXml(out, Uo.class, uo, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>uo</var> of type {@link it.govpay.orm.Uo}
	 * 
	 * @param out OutputStream to serialize the object <var>uo</var>
	 * @param uo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Uo uo,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Uo.class, uo, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>uo</var> of type {@link it.govpay.orm.Uo}
	 * 
	 * @param uo Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Uo uo) throws SerializerException {
		return this.objToXml(Uo.class, uo, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>uo</var> of type {@link it.govpay.orm.Uo}
	 * 
	 * @param uo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Uo uo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Uo.class, uo, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>uo</var> of type {@link it.govpay.orm.Uo}
	 * 
	 * @param uo Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Uo uo) throws SerializerException {
		return this.objToXml(Uo.class, uo, false).toString();
	}
	/**
	 * Serialize to String the object <var>uo</var> of type {@link it.govpay.orm.Uo}
	 * 
	 * @param uo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Uo uo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Uo.class, uo, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: ACL
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>acl</var> of type {@link it.govpay.orm.ACL}
	 * 
	 * @param fileName Xml file to serialize the object <var>acl</var>
	 * @param acl Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,ACL acl) throws SerializerException {
		this.objToXml(fileName, ACL.class, acl, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>acl</var> of type {@link it.govpay.orm.ACL}
	 * 
	 * @param fileName Xml file to serialize the object <var>acl</var>
	 * @param acl Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,ACL acl,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, ACL.class, acl, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>acl</var> of type {@link it.govpay.orm.ACL}
	 * 
	 * @param file Xml file to serialize the object <var>acl</var>
	 * @param acl Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,ACL acl) throws SerializerException {
		this.objToXml(file, ACL.class, acl, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>acl</var> of type {@link it.govpay.orm.ACL}
	 * 
	 * @param file Xml file to serialize the object <var>acl</var>
	 * @param acl Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,ACL acl,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, ACL.class, acl, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>acl</var> of type {@link it.govpay.orm.ACL}
	 * 
	 * @param out OutputStream to serialize the object <var>acl</var>
	 * @param acl Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,ACL acl) throws SerializerException {
		this.objToXml(out, ACL.class, acl, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>acl</var> of type {@link it.govpay.orm.ACL}
	 * 
	 * @param out OutputStream to serialize the object <var>acl</var>
	 * @param acl Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,ACL acl,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, ACL.class, acl, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>acl</var> of type {@link it.govpay.orm.ACL}
	 * 
	 * @param acl Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(ACL acl) throws SerializerException {
		return this.objToXml(ACL.class, acl, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>acl</var> of type {@link it.govpay.orm.ACL}
	 * 
	 * @param acl Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(ACL acl,boolean prettyPrint) throws SerializerException {
		return this.objToXml(ACL.class, acl, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>acl</var> of type {@link it.govpay.orm.ACL}
	 * 
	 * @param acl Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(ACL acl) throws SerializerException {
		return this.objToXml(ACL.class, acl, false).toString();
	}
	/**
	 * Serialize to String the object <var>acl</var> of type {@link it.govpay.orm.ACL}
	 * 
	 * @param acl Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(ACL acl,boolean prettyPrint) throws SerializerException {
		return this.objToXml(ACL.class, acl, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: TipoVersamentoDominio
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>tipoVersamentoDominio</var> of type {@link it.govpay.orm.TipoVersamentoDominio}
	 * 
	 * @param fileName Xml file to serialize the object <var>tipoVersamentoDominio</var>
	 * @param tipoVersamentoDominio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,TipoVersamentoDominio tipoVersamentoDominio) throws SerializerException {
		this.objToXml(fileName, TipoVersamentoDominio.class, tipoVersamentoDominio, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>tipoVersamentoDominio</var> of type {@link it.govpay.orm.TipoVersamentoDominio}
	 * 
	 * @param fileName Xml file to serialize the object <var>tipoVersamentoDominio</var>
	 * @param tipoVersamentoDominio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,TipoVersamentoDominio tipoVersamentoDominio,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, TipoVersamentoDominio.class, tipoVersamentoDominio, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>tipoVersamentoDominio</var> of type {@link it.govpay.orm.TipoVersamentoDominio}
	 * 
	 * @param file Xml file to serialize the object <var>tipoVersamentoDominio</var>
	 * @param tipoVersamentoDominio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,TipoVersamentoDominio tipoVersamentoDominio) throws SerializerException {
		this.objToXml(file, TipoVersamentoDominio.class, tipoVersamentoDominio, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>tipoVersamentoDominio</var> of type {@link it.govpay.orm.TipoVersamentoDominio}
	 * 
	 * @param file Xml file to serialize the object <var>tipoVersamentoDominio</var>
	 * @param tipoVersamentoDominio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,TipoVersamentoDominio tipoVersamentoDominio,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, TipoVersamentoDominio.class, tipoVersamentoDominio, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>tipoVersamentoDominio</var> of type {@link it.govpay.orm.TipoVersamentoDominio}
	 * 
	 * @param out OutputStream to serialize the object <var>tipoVersamentoDominio</var>
	 * @param tipoVersamentoDominio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,TipoVersamentoDominio tipoVersamentoDominio) throws SerializerException {
		this.objToXml(out, TipoVersamentoDominio.class, tipoVersamentoDominio, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>tipoVersamentoDominio</var> of type {@link it.govpay.orm.TipoVersamentoDominio}
	 * 
	 * @param out OutputStream to serialize the object <var>tipoVersamentoDominio</var>
	 * @param tipoVersamentoDominio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,TipoVersamentoDominio tipoVersamentoDominio,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, TipoVersamentoDominio.class, tipoVersamentoDominio, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>tipoVersamentoDominio</var> of type {@link it.govpay.orm.TipoVersamentoDominio}
	 * 
	 * @param tipoVersamentoDominio Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(TipoVersamentoDominio tipoVersamentoDominio) throws SerializerException {
		return this.objToXml(TipoVersamentoDominio.class, tipoVersamentoDominio, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>tipoVersamentoDominio</var> of type {@link it.govpay.orm.TipoVersamentoDominio}
	 * 
	 * @param tipoVersamentoDominio Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(TipoVersamentoDominio tipoVersamentoDominio,boolean prettyPrint) throws SerializerException {
		return this.objToXml(TipoVersamentoDominio.class, tipoVersamentoDominio, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>tipoVersamentoDominio</var> of type {@link it.govpay.orm.TipoVersamentoDominio}
	 * 
	 * @param tipoVersamentoDominio Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(TipoVersamentoDominio tipoVersamentoDominio) throws SerializerException {
		return this.objToXml(TipoVersamentoDominio.class, tipoVersamentoDominio, false).toString();
	}
	/**
	 * Serialize to String the object <var>tipoVersamentoDominio</var> of type {@link it.govpay.orm.TipoVersamentoDominio}
	 * 
	 * @param tipoVersamentoDominio Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(TipoVersamentoDominio tipoVersamentoDominio,boolean prettyPrint) throws SerializerException {
		return this.objToXml(TipoVersamentoDominio.class, tipoVersamentoDominio, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: UtenzaDominio
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>utenzaDominio</var> of type {@link it.govpay.orm.UtenzaDominio}
	 * 
	 * @param fileName Xml file to serialize the object <var>utenzaDominio</var>
	 * @param utenzaDominio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,UtenzaDominio utenzaDominio) throws SerializerException {
		this.objToXml(fileName, UtenzaDominio.class, utenzaDominio, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>utenzaDominio</var> of type {@link it.govpay.orm.UtenzaDominio}
	 * 
	 * @param fileName Xml file to serialize the object <var>utenzaDominio</var>
	 * @param utenzaDominio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,UtenzaDominio utenzaDominio,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, UtenzaDominio.class, utenzaDominio, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>utenzaDominio</var> of type {@link it.govpay.orm.UtenzaDominio}
	 * 
	 * @param file Xml file to serialize the object <var>utenzaDominio</var>
	 * @param utenzaDominio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,UtenzaDominio utenzaDominio) throws SerializerException {
		this.objToXml(file, UtenzaDominio.class, utenzaDominio, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>utenzaDominio</var> of type {@link it.govpay.orm.UtenzaDominio}
	 * 
	 * @param file Xml file to serialize the object <var>utenzaDominio</var>
	 * @param utenzaDominio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,UtenzaDominio utenzaDominio,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, UtenzaDominio.class, utenzaDominio, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>utenzaDominio</var> of type {@link it.govpay.orm.UtenzaDominio}
	 * 
	 * @param out OutputStream to serialize the object <var>utenzaDominio</var>
	 * @param utenzaDominio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,UtenzaDominio utenzaDominio) throws SerializerException {
		this.objToXml(out, UtenzaDominio.class, utenzaDominio, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>utenzaDominio</var> of type {@link it.govpay.orm.UtenzaDominio}
	 * 
	 * @param out OutputStream to serialize the object <var>utenzaDominio</var>
	 * @param utenzaDominio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,UtenzaDominio utenzaDominio,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, UtenzaDominio.class, utenzaDominio, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>utenzaDominio</var> of type {@link it.govpay.orm.UtenzaDominio}
	 * 
	 * @param utenzaDominio Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(UtenzaDominio utenzaDominio) throws SerializerException {
		return this.objToXml(UtenzaDominio.class, utenzaDominio, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>utenzaDominio</var> of type {@link it.govpay.orm.UtenzaDominio}
	 * 
	 * @param utenzaDominio Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(UtenzaDominio utenzaDominio,boolean prettyPrint) throws SerializerException {
		return this.objToXml(UtenzaDominio.class, utenzaDominio, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>utenzaDominio</var> of type {@link it.govpay.orm.UtenzaDominio}
	 * 
	 * @param utenzaDominio Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(UtenzaDominio utenzaDominio) throws SerializerException {
		return this.objToXml(UtenzaDominio.class, utenzaDominio, false).toString();
	}
	/**
	 * Serialize to String the object <var>utenzaDominio</var> of type {@link it.govpay.orm.UtenzaDominio}
	 * 
	 * @param utenzaDominio Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(UtenzaDominio utenzaDominio,boolean prettyPrint) throws SerializerException {
		return this.objToXml(UtenzaDominio.class, utenzaDominio, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Configurazione
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>configurazione</var> of type {@link it.govpay.orm.Configurazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>configurazione</var>
	 * @param configurazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Configurazione configurazione) throws SerializerException {
		this.objToXml(fileName, Configurazione.class, configurazione, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>configurazione</var> of type {@link it.govpay.orm.Configurazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>configurazione</var>
	 * @param configurazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Configurazione configurazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Configurazione.class, configurazione, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>configurazione</var> of type {@link it.govpay.orm.Configurazione}
	 * 
	 * @param file Xml file to serialize the object <var>configurazione</var>
	 * @param configurazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Configurazione configurazione) throws SerializerException {
		this.objToXml(file, Configurazione.class, configurazione, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>configurazione</var> of type {@link it.govpay.orm.Configurazione}
	 * 
	 * @param file Xml file to serialize the object <var>configurazione</var>
	 * @param configurazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Configurazione configurazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Configurazione.class, configurazione, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>configurazione</var> of type {@link it.govpay.orm.Configurazione}
	 * 
	 * @param out OutputStream to serialize the object <var>configurazione</var>
	 * @param configurazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Configurazione configurazione) throws SerializerException {
		this.objToXml(out, Configurazione.class, configurazione, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>configurazione</var> of type {@link it.govpay.orm.Configurazione}
	 * 
	 * @param out OutputStream to serialize the object <var>configurazione</var>
	 * @param configurazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Configurazione configurazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Configurazione.class, configurazione, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>configurazione</var> of type {@link it.govpay.orm.Configurazione}
	 * 
	 * @param configurazione Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Configurazione configurazione) throws SerializerException {
		return this.objToXml(Configurazione.class, configurazione, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>configurazione</var> of type {@link it.govpay.orm.Configurazione}
	 * 
	 * @param configurazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Configurazione configurazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Configurazione.class, configurazione, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>configurazione</var> of type {@link it.govpay.orm.Configurazione}
	 * 
	 * @param configurazione Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Configurazione configurazione) throws SerializerException {
		return this.objToXml(Configurazione.class, configurazione, false).toString();
	}
	/**
	 * Serialize to String the object <var>configurazione</var> of type {@link it.govpay.orm.Configurazione}
	 * 
	 * @param configurazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Configurazione configurazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Configurazione.class, configurazione, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Notifica
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>notifica</var> of type {@link it.govpay.orm.Notifica}
	 * 
	 * @param fileName Xml file to serialize the object <var>notifica</var>
	 * @param notifica Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Notifica notifica) throws SerializerException {
		this.objToXml(fileName, Notifica.class, notifica, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>notifica</var> of type {@link it.govpay.orm.Notifica}
	 * 
	 * @param fileName Xml file to serialize the object <var>notifica</var>
	 * @param notifica Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Notifica notifica,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Notifica.class, notifica, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>notifica</var> of type {@link it.govpay.orm.Notifica}
	 * 
	 * @param file Xml file to serialize the object <var>notifica</var>
	 * @param notifica Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Notifica notifica) throws SerializerException {
		this.objToXml(file, Notifica.class, notifica, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>notifica</var> of type {@link it.govpay.orm.Notifica}
	 * 
	 * @param file Xml file to serialize the object <var>notifica</var>
	 * @param notifica Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Notifica notifica,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Notifica.class, notifica, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>notifica</var> of type {@link it.govpay.orm.Notifica}
	 * 
	 * @param out OutputStream to serialize the object <var>notifica</var>
	 * @param notifica Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Notifica notifica) throws SerializerException {
		this.objToXml(out, Notifica.class, notifica, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>notifica</var> of type {@link it.govpay.orm.Notifica}
	 * 
	 * @param out OutputStream to serialize the object <var>notifica</var>
	 * @param notifica Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Notifica notifica,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Notifica.class, notifica, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>notifica</var> of type {@link it.govpay.orm.Notifica}
	 * 
	 * @param notifica Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Notifica notifica) throws SerializerException {
		return this.objToXml(Notifica.class, notifica, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>notifica</var> of type {@link it.govpay.orm.Notifica}
	 * 
	 * @param notifica Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Notifica notifica,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Notifica.class, notifica, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>notifica</var> of type {@link it.govpay.orm.Notifica}
	 * 
	 * @param notifica Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Notifica notifica) throws SerializerException {
		return this.objToXml(Notifica.class, notifica, false).toString();
	}
	/**
	 * Serialize to String the object <var>notifica</var> of type {@link it.govpay.orm.Notifica}
	 * 
	 * @param notifica Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Notifica notifica,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Notifica.class, notifica, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-rr
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idRr</var> of type {@link it.govpay.orm.IdRr}
	 * 
	 * @param fileName Xml file to serialize the object <var>idRr</var>
	 * @param idRr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdRr idRr) throws SerializerException {
		this.objToXml(fileName, IdRr.class, idRr, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idRr</var> of type {@link it.govpay.orm.IdRr}
	 * 
	 * @param fileName Xml file to serialize the object <var>idRr</var>
	 * @param idRr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdRr idRr,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdRr.class, idRr, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idRr</var> of type {@link it.govpay.orm.IdRr}
	 * 
	 * @param file Xml file to serialize the object <var>idRr</var>
	 * @param idRr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdRr idRr) throws SerializerException {
		this.objToXml(file, IdRr.class, idRr, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idRr</var> of type {@link it.govpay.orm.IdRr}
	 * 
	 * @param file Xml file to serialize the object <var>idRr</var>
	 * @param idRr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdRr idRr,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdRr.class, idRr, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idRr</var> of type {@link it.govpay.orm.IdRr}
	 * 
	 * @param out OutputStream to serialize the object <var>idRr</var>
	 * @param idRr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdRr idRr) throws SerializerException {
		this.objToXml(out, IdRr.class, idRr, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idRr</var> of type {@link it.govpay.orm.IdRr}
	 * 
	 * @param out OutputStream to serialize the object <var>idRr</var>
	 * @param idRr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdRr idRr,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdRr.class, idRr, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idRr</var> of type {@link it.govpay.orm.IdRr}
	 * 
	 * @param idRr Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdRr idRr) throws SerializerException {
		return this.objToXml(IdRr.class, idRr, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idRr</var> of type {@link it.govpay.orm.IdRr}
	 * 
	 * @param idRr Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdRr idRr,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdRr.class, idRr, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idRr</var> of type {@link it.govpay.orm.IdRr}
	 * 
	 * @param idRr Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdRr idRr) throws SerializerException {
		return this.objToXml(IdRr.class, idRr, false).toString();
	}
	/**
	 * Serialize to String the object <var>idRr</var> of type {@link it.govpay.orm.IdRr}
	 * 
	 * @param idRr Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdRr idRr,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdRr.class, idRr, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-singola-rendicontazione
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idSingolaRendicontazione</var> of type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>idSingolaRendicontazione</var>
	 * @param idSingolaRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdSingolaRendicontazione idSingolaRendicontazione) throws SerializerException {
		this.objToXml(fileName, IdSingolaRendicontazione.class, idSingolaRendicontazione, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idSingolaRendicontazione</var> of type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>idSingolaRendicontazione</var>
	 * @param idSingolaRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdSingolaRendicontazione idSingolaRendicontazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdSingolaRendicontazione.class, idSingolaRendicontazione, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idSingolaRendicontazione</var> of type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param file Xml file to serialize the object <var>idSingolaRendicontazione</var>
	 * @param idSingolaRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdSingolaRendicontazione idSingolaRendicontazione) throws SerializerException {
		this.objToXml(file, IdSingolaRendicontazione.class, idSingolaRendicontazione, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idSingolaRendicontazione</var> of type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param file Xml file to serialize the object <var>idSingolaRendicontazione</var>
	 * @param idSingolaRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdSingolaRendicontazione idSingolaRendicontazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdSingolaRendicontazione.class, idSingolaRendicontazione, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idSingolaRendicontazione</var> of type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param out OutputStream to serialize the object <var>idSingolaRendicontazione</var>
	 * @param idSingolaRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdSingolaRendicontazione idSingolaRendicontazione) throws SerializerException {
		this.objToXml(out, IdSingolaRendicontazione.class, idSingolaRendicontazione, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idSingolaRendicontazione</var> of type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param out OutputStream to serialize the object <var>idSingolaRendicontazione</var>
	 * @param idSingolaRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdSingolaRendicontazione idSingolaRendicontazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdSingolaRendicontazione.class, idSingolaRendicontazione, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idSingolaRendicontazione</var> of type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param idSingolaRendicontazione Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdSingolaRendicontazione idSingolaRendicontazione) throws SerializerException {
		return this.objToXml(IdSingolaRendicontazione.class, idSingolaRendicontazione, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idSingolaRendicontazione</var> of type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param idSingolaRendicontazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdSingolaRendicontazione idSingolaRendicontazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdSingolaRendicontazione.class, idSingolaRendicontazione, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idSingolaRendicontazione</var> of type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param idSingolaRendicontazione Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdSingolaRendicontazione idSingolaRendicontazione) throws SerializerException {
		return this.objToXml(IdSingolaRendicontazione.class, idSingolaRendicontazione, false).toString();
	}
	/**
	 * Serialize to String the object <var>idSingolaRendicontazione</var> of type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param idSingolaRendicontazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdSingolaRendicontazione idSingolaRendicontazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdSingolaRendicontazione.class, idSingolaRendicontazione, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-tracciato-notifica-pagamenti
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idTracciatoNotificaPagamenti</var> of type {@link it.govpay.orm.IdTracciatoNotificaPagamenti}
	 * 
	 * @param fileName Xml file to serialize the object <var>idTracciatoNotificaPagamenti</var>
	 * @param idTracciatoNotificaPagamenti Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdTracciatoNotificaPagamenti idTracciatoNotificaPagamenti) throws SerializerException {
		this.objToXml(fileName, IdTracciatoNotificaPagamenti.class, idTracciatoNotificaPagamenti, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idTracciatoNotificaPagamenti</var> of type {@link it.govpay.orm.IdTracciatoNotificaPagamenti}
	 * 
	 * @param fileName Xml file to serialize the object <var>idTracciatoNotificaPagamenti</var>
	 * @param idTracciatoNotificaPagamenti Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdTracciatoNotificaPagamenti idTracciatoNotificaPagamenti,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdTracciatoNotificaPagamenti.class, idTracciatoNotificaPagamenti, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idTracciatoNotificaPagamenti</var> of type {@link it.govpay.orm.IdTracciatoNotificaPagamenti}
	 * 
	 * @param file Xml file to serialize the object <var>idTracciatoNotificaPagamenti</var>
	 * @param idTracciatoNotificaPagamenti Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdTracciatoNotificaPagamenti idTracciatoNotificaPagamenti) throws SerializerException {
		this.objToXml(file, IdTracciatoNotificaPagamenti.class, idTracciatoNotificaPagamenti, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idTracciatoNotificaPagamenti</var> of type {@link it.govpay.orm.IdTracciatoNotificaPagamenti}
	 * 
	 * @param file Xml file to serialize the object <var>idTracciatoNotificaPagamenti</var>
	 * @param idTracciatoNotificaPagamenti Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdTracciatoNotificaPagamenti idTracciatoNotificaPagamenti,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdTracciatoNotificaPagamenti.class, idTracciatoNotificaPagamenti, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idTracciatoNotificaPagamenti</var> of type {@link it.govpay.orm.IdTracciatoNotificaPagamenti}
	 * 
	 * @param out OutputStream to serialize the object <var>idTracciatoNotificaPagamenti</var>
	 * @param idTracciatoNotificaPagamenti Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdTracciatoNotificaPagamenti idTracciatoNotificaPagamenti) throws SerializerException {
		this.objToXml(out, IdTracciatoNotificaPagamenti.class, idTracciatoNotificaPagamenti, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idTracciatoNotificaPagamenti</var> of type {@link it.govpay.orm.IdTracciatoNotificaPagamenti}
	 * 
	 * @param out OutputStream to serialize the object <var>idTracciatoNotificaPagamenti</var>
	 * @param idTracciatoNotificaPagamenti Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdTracciatoNotificaPagamenti idTracciatoNotificaPagamenti,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdTracciatoNotificaPagamenti.class, idTracciatoNotificaPagamenti, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idTracciatoNotificaPagamenti</var> of type {@link it.govpay.orm.IdTracciatoNotificaPagamenti}
	 * 
	 * @param idTracciatoNotificaPagamenti Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdTracciatoNotificaPagamenti idTracciatoNotificaPagamenti) throws SerializerException {
		return this.objToXml(IdTracciatoNotificaPagamenti.class, idTracciatoNotificaPagamenti, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idTracciatoNotificaPagamenti</var> of type {@link it.govpay.orm.IdTracciatoNotificaPagamenti}
	 * 
	 * @param idTracciatoNotificaPagamenti Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdTracciatoNotificaPagamenti idTracciatoNotificaPagamenti,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdTracciatoNotificaPagamenti.class, idTracciatoNotificaPagamenti, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idTracciatoNotificaPagamenti</var> of type {@link it.govpay.orm.IdTracciatoNotificaPagamenti}
	 * 
	 * @param idTracciatoNotificaPagamenti Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdTracciatoNotificaPagamenti idTracciatoNotificaPagamenti) throws SerializerException {
		return this.objToXml(IdTracciatoNotificaPagamenti.class, idTracciatoNotificaPagamenti, false).toString();
	}
	/**
	 * Serialize to String the object <var>idTracciatoNotificaPagamenti</var> of type {@link it.govpay.orm.IdTracciatoNotificaPagamenti}
	 * 
	 * @param idTracciatoNotificaPagamenti Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdTracciatoNotificaPagamenti idTracciatoNotificaPagamenti,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdTracciatoNotificaPagamenti.class, idTracciatoNotificaPagamenti, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: IbanAccredito
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>ibanAccredito</var> of type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param fileName Xml file to serialize the object <var>ibanAccredito</var>
	 * @param ibanAccredito Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IbanAccredito ibanAccredito) throws SerializerException {
		this.objToXml(fileName, IbanAccredito.class, ibanAccredito, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>ibanAccredito</var> of type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param fileName Xml file to serialize the object <var>ibanAccredito</var>
	 * @param ibanAccredito Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IbanAccredito ibanAccredito,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IbanAccredito.class, ibanAccredito, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>ibanAccredito</var> of type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param file Xml file to serialize the object <var>ibanAccredito</var>
	 * @param ibanAccredito Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IbanAccredito ibanAccredito) throws SerializerException {
		this.objToXml(file, IbanAccredito.class, ibanAccredito, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>ibanAccredito</var> of type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param file Xml file to serialize the object <var>ibanAccredito</var>
	 * @param ibanAccredito Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IbanAccredito ibanAccredito,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IbanAccredito.class, ibanAccredito, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>ibanAccredito</var> of type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param out OutputStream to serialize the object <var>ibanAccredito</var>
	 * @param ibanAccredito Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IbanAccredito ibanAccredito) throws SerializerException {
		this.objToXml(out, IbanAccredito.class, ibanAccredito, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>ibanAccredito</var> of type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param out OutputStream to serialize the object <var>ibanAccredito</var>
	 * @param ibanAccredito Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IbanAccredito ibanAccredito,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IbanAccredito.class, ibanAccredito, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>ibanAccredito</var> of type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param ibanAccredito Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IbanAccredito ibanAccredito) throws SerializerException {
		return this.objToXml(IbanAccredito.class, ibanAccredito, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>ibanAccredito</var> of type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param ibanAccredito Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IbanAccredito ibanAccredito,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IbanAccredito.class, ibanAccredito, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>ibanAccredito</var> of type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param ibanAccredito Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IbanAccredito ibanAccredito) throws SerializerException {
		return this.objToXml(IbanAccredito.class, ibanAccredito, false).toString();
	}
	/**
	 * Serialize to String the object <var>ibanAccredito</var> of type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param ibanAccredito Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IbanAccredito ibanAccredito,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IbanAccredito.class, ibanAccredito, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: UtenzaTipoVersamento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>utenzaTipoVersamento</var> of type {@link it.govpay.orm.UtenzaTipoVersamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>utenzaTipoVersamento</var>
	 * @param utenzaTipoVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,UtenzaTipoVersamento utenzaTipoVersamento) throws SerializerException {
		this.objToXml(fileName, UtenzaTipoVersamento.class, utenzaTipoVersamento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>utenzaTipoVersamento</var> of type {@link it.govpay.orm.UtenzaTipoVersamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>utenzaTipoVersamento</var>
	 * @param utenzaTipoVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,UtenzaTipoVersamento utenzaTipoVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, UtenzaTipoVersamento.class, utenzaTipoVersamento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>utenzaTipoVersamento</var> of type {@link it.govpay.orm.UtenzaTipoVersamento}
	 * 
	 * @param file Xml file to serialize the object <var>utenzaTipoVersamento</var>
	 * @param utenzaTipoVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,UtenzaTipoVersamento utenzaTipoVersamento) throws SerializerException {
		this.objToXml(file, UtenzaTipoVersamento.class, utenzaTipoVersamento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>utenzaTipoVersamento</var> of type {@link it.govpay.orm.UtenzaTipoVersamento}
	 * 
	 * @param file Xml file to serialize the object <var>utenzaTipoVersamento</var>
	 * @param utenzaTipoVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,UtenzaTipoVersamento utenzaTipoVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, UtenzaTipoVersamento.class, utenzaTipoVersamento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>utenzaTipoVersamento</var> of type {@link it.govpay.orm.UtenzaTipoVersamento}
	 * 
	 * @param out OutputStream to serialize the object <var>utenzaTipoVersamento</var>
	 * @param utenzaTipoVersamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,UtenzaTipoVersamento utenzaTipoVersamento) throws SerializerException {
		this.objToXml(out, UtenzaTipoVersamento.class, utenzaTipoVersamento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>utenzaTipoVersamento</var> of type {@link it.govpay.orm.UtenzaTipoVersamento}
	 * 
	 * @param out OutputStream to serialize the object <var>utenzaTipoVersamento</var>
	 * @param utenzaTipoVersamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,UtenzaTipoVersamento utenzaTipoVersamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, UtenzaTipoVersamento.class, utenzaTipoVersamento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>utenzaTipoVersamento</var> of type {@link it.govpay.orm.UtenzaTipoVersamento}
	 * 
	 * @param utenzaTipoVersamento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(UtenzaTipoVersamento utenzaTipoVersamento) throws SerializerException {
		return this.objToXml(UtenzaTipoVersamento.class, utenzaTipoVersamento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>utenzaTipoVersamento</var> of type {@link it.govpay.orm.UtenzaTipoVersamento}
	 * 
	 * @param utenzaTipoVersamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(UtenzaTipoVersamento utenzaTipoVersamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(UtenzaTipoVersamento.class, utenzaTipoVersamento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>utenzaTipoVersamento</var> of type {@link it.govpay.orm.UtenzaTipoVersamento}
	 * 
	 * @param utenzaTipoVersamento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(UtenzaTipoVersamento utenzaTipoVersamento) throws SerializerException {
		return this.objToXml(UtenzaTipoVersamento.class, utenzaTipoVersamento, false).toString();
	}
	/**
	 * Serialize to String the object <var>utenzaTipoVersamento</var> of type {@link it.govpay.orm.UtenzaTipoVersamento}
	 * 
	 * @param utenzaTipoVersamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(UtenzaTipoVersamento utenzaTipoVersamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(UtenzaTipoVersamento.class, utenzaTipoVersamento, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-carrello
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idCarrello</var> of type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param fileName Xml file to serialize the object <var>idCarrello</var>
	 * @param idCarrello Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdCarrello idCarrello) throws SerializerException {
		this.objToXml(fileName, IdCarrello.class, idCarrello, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idCarrello</var> of type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param fileName Xml file to serialize the object <var>idCarrello</var>
	 * @param idCarrello Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdCarrello idCarrello,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdCarrello.class, idCarrello, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idCarrello</var> of type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param file Xml file to serialize the object <var>idCarrello</var>
	 * @param idCarrello Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdCarrello idCarrello) throws SerializerException {
		this.objToXml(file, IdCarrello.class, idCarrello, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idCarrello</var> of type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param file Xml file to serialize the object <var>idCarrello</var>
	 * @param idCarrello Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdCarrello idCarrello,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdCarrello.class, idCarrello, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idCarrello</var> of type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param out OutputStream to serialize the object <var>idCarrello</var>
	 * @param idCarrello Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdCarrello idCarrello) throws SerializerException {
		this.objToXml(out, IdCarrello.class, idCarrello, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idCarrello</var> of type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param out OutputStream to serialize the object <var>idCarrello</var>
	 * @param idCarrello Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdCarrello idCarrello,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdCarrello.class, idCarrello, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idCarrello</var> of type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param idCarrello Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdCarrello idCarrello) throws SerializerException {
		return this.objToXml(IdCarrello.class, idCarrello, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idCarrello</var> of type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param idCarrello Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdCarrello idCarrello,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdCarrello.class, idCarrello, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idCarrello</var> of type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param idCarrello Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdCarrello idCarrello) throws SerializerException {
		return this.objToXml(IdCarrello.class, idCarrello, false).toString();
	}
	/**
	 * Serialize to String the object <var>idCarrello</var> of type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param idCarrello Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdCarrello idCarrello,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdCarrello.class, idCarrello, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Batch
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>batch</var> of type {@link it.govpay.orm.Batch}
	 * 
	 * @param fileName Xml file to serialize the object <var>batch</var>
	 * @param batch Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Batch batch) throws SerializerException {
		this.objToXml(fileName, Batch.class, batch, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>batch</var> of type {@link it.govpay.orm.Batch}
	 * 
	 * @param fileName Xml file to serialize the object <var>batch</var>
	 * @param batch Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Batch batch,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Batch.class, batch, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>batch</var> of type {@link it.govpay.orm.Batch}
	 * 
	 * @param file Xml file to serialize the object <var>batch</var>
	 * @param batch Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Batch batch) throws SerializerException {
		this.objToXml(file, Batch.class, batch, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>batch</var> of type {@link it.govpay.orm.Batch}
	 * 
	 * @param file Xml file to serialize the object <var>batch</var>
	 * @param batch Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Batch batch,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Batch.class, batch, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>batch</var> of type {@link it.govpay.orm.Batch}
	 * 
	 * @param out OutputStream to serialize the object <var>batch</var>
	 * @param batch Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Batch batch) throws SerializerException {
		this.objToXml(out, Batch.class, batch, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>batch</var> of type {@link it.govpay.orm.Batch}
	 * 
	 * @param out OutputStream to serialize the object <var>batch</var>
	 * @param batch Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Batch batch,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Batch.class, batch, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>batch</var> of type {@link it.govpay.orm.Batch}
	 * 
	 * @param batch Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Batch batch) throws SerializerException {
		return this.objToXml(Batch.class, batch, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>batch</var> of type {@link it.govpay.orm.Batch}
	 * 
	 * @param batch Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Batch batch,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Batch.class, batch, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>batch</var> of type {@link it.govpay.orm.Batch}
	 * 
	 * @param batch Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Batch batch) throws SerializerException {
		return this.objToXml(Batch.class, batch, false).toString();
	}
	/**
	 * Serialize to String the object <var>batch</var> of type {@link it.govpay.orm.Batch}
	 * 
	 * @param batch Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Batch batch,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Batch.class, batch, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-messaggio
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idMessaggio</var> of type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param fileName Xml file to serialize the object <var>idMessaggio</var>
	 * @param idMessaggio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdMessaggio idMessaggio) throws SerializerException {
		this.objToXml(fileName, IdMessaggio.class, idMessaggio, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idMessaggio</var> of type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param fileName Xml file to serialize the object <var>idMessaggio</var>
	 * @param idMessaggio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdMessaggio idMessaggio,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdMessaggio.class, idMessaggio, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idMessaggio</var> of type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param file Xml file to serialize the object <var>idMessaggio</var>
	 * @param idMessaggio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdMessaggio idMessaggio) throws SerializerException {
		this.objToXml(file, IdMessaggio.class, idMessaggio, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idMessaggio</var> of type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param file Xml file to serialize the object <var>idMessaggio</var>
	 * @param idMessaggio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdMessaggio idMessaggio,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdMessaggio.class, idMessaggio, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idMessaggio</var> of type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param out OutputStream to serialize the object <var>idMessaggio</var>
	 * @param idMessaggio Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdMessaggio idMessaggio) throws SerializerException {
		this.objToXml(out, IdMessaggio.class, idMessaggio, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idMessaggio</var> of type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param out OutputStream to serialize the object <var>idMessaggio</var>
	 * @param idMessaggio Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdMessaggio idMessaggio,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdMessaggio.class, idMessaggio, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idMessaggio</var> of type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param idMessaggio Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdMessaggio idMessaggio) throws SerializerException {
		return this.objToXml(IdMessaggio.class, idMessaggio, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idMessaggio</var> of type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param idMessaggio Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdMessaggio idMessaggio,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdMessaggio.class, idMessaggio, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idMessaggio</var> of type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param idMessaggio Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdMessaggio idMessaggio) throws SerializerException {
		return this.objToXml(IdMessaggio.class, idMessaggio, false).toString();
	}
	/**
	 * Serialize to String the object <var>idMessaggio</var> of type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param idMessaggio Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdMessaggio idMessaggio,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdMessaggio.class, idMessaggio, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-singola-revoca
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idSingolaRevoca</var> of type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param fileName Xml file to serialize the object <var>idSingolaRevoca</var>
	 * @param idSingolaRevoca Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdSingolaRevoca idSingolaRevoca) throws SerializerException {
		this.objToXml(fileName, IdSingolaRevoca.class, idSingolaRevoca, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idSingolaRevoca</var> of type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param fileName Xml file to serialize the object <var>idSingolaRevoca</var>
	 * @param idSingolaRevoca Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdSingolaRevoca idSingolaRevoca,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdSingolaRevoca.class, idSingolaRevoca, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idSingolaRevoca</var> of type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param file Xml file to serialize the object <var>idSingolaRevoca</var>
	 * @param idSingolaRevoca Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdSingolaRevoca idSingolaRevoca) throws SerializerException {
		this.objToXml(file, IdSingolaRevoca.class, idSingolaRevoca, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idSingolaRevoca</var> of type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param file Xml file to serialize the object <var>idSingolaRevoca</var>
	 * @param idSingolaRevoca Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdSingolaRevoca idSingolaRevoca,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdSingolaRevoca.class, idSingolaRevoca, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idSingolaRevoca</var> of type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param out OutputStream to serialize the object <var>idSingolaRevoca</var>
	 * @param idSingolaRevoca Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdSingolaRevoca idSingolaRevoca) throws SerializerException {
		this.objToXml(out, IdSingolaRevoca.class, idSingolaRevoca, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idSingolaRevoca</var> of type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param out OutputStream to serialize the object <var>idSingolaRevoca</var>
	 * @param idSingolaRevoca Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdSingolaRevoca idSingolaRevoca,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdSingolaRevoca.class, idSingolaRevoca, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idSingolaRevoca</var> of type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param idSingolaRevoca Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdSingolaRevoca idSingolaRevoca) throws SerializerException {
		return this.objToXml(IdSingolaRevoca.class, idSingolaRevoca, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idSingolaRevoca</var> of type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param idSingolaRevoca Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdSingolaRevoca idSingolaRevoca,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdSingolaRevoca.class, idSingolaRevoca, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idSingolaRevoca</var> of type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param idSingolaRevoca Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdSingolaRevoca idSingolaRevoca) throws SerializerException {
		return this.objToXml(IdSingolaRevoca.class, idSingolaRevoca, false).toString();
	}
	/**
	 * Serialize to String the object <var>idSingolaRevoca</var> of type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param idSingolaRevoca Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdSingolaRevoca idSingolaRevoca,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdSingolaRevoca.class, idSingolaRevoca, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: PagamentoPortale
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>pagamentoPortale</var> of type {@link it.govpay.orm.PagamentoPortale}
	 * 
	 * @param fileName Xml file to serialize the object <var>pagamentoPortale</var>
	 * @param pagamentoPortale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,PagamentoPortale pagamentoPortale) throws SerializerException {
		this.objToXml(fileName, PagamentoPortale.class, pagamentoPortale, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>pagamentoPortale</var> of type {@link it.govpay.orm.PagamentoPortale}
	 * 
	 * @param fileName Xml file to serialize the object <var>pagamentoPortale</var>
	 * @param pagamentoPortale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,PagamentoPortale pagamentoPortale,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, PagamentoPortale.class, pagamentoPortale, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>pagamentoPortale</var> of type {@link it.govpay.orm.PagamentoPortale}
	 * 
	 * @param file Xml file to serialize the object <var>pagamentoPortale</var>
	 * @param pagamentoPortale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,PagamentoPortale pagamentoPortale) throws SerializerException {
		this.objToXml(file, PagamentoPortale.class, pagamentoPortale, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>pagamentoPortale</var> of type {@link it.govpay.orm.PagamentoPortale}
	 * 
	 * @param file Xml file to serialize the object <var>pagamentoPortale</var>
	 * @param pagamentoPortale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,PagamentoPortale pagamentoPortale,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, PagamentoPortale.class, pagamentoPortale, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>pagamentoPortale</var> of type {@link it.govpay.orm.PagamentoPortale}
	 * 
	 * @param out OutputStream to serialize the object <var>pagamentoPortale</var>
	 * @param pagamentoPortale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,PagamentoPortale pagamentoPortale) throws SerializerException {
		this.objToXml(out, PagamentoPortale.class, pagamentoPortale, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>pagamentoPortale</var> of type {@link it.govpay.orm.PagamentoPortale}
	 * 
	 * @param out OutputStream to serialize the object <var>pagamentoPortale</var>
	 * @param pagamentoPortale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,PagamentoPortale pagamentoPortale,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, PagamentoPortale.class, pagamentoPortale, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>pagamentoPortale</var> of type {@link it.govpay.orm.PagamentoPortale}
	 * 
	 * @param pagamentoPortale Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(PagamentoPortale pagamentoPortale) throws SerializerException {
		return this.objToXml(PagamentoPortale.class, pagamentoPortale, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>pagamentoPortale</var> of type {@link it.govpay.orm.PagamentoPortale}
	 * 
	 * @param pagamentoPortale Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(PagamentoPortale pagamentoPortale,boolean prettyPrint) throws SerializerException {
		return this.objToXml(PagamentoPortale.class, pagamentoPortale, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>pagamentoPortale</var> of type {@link it.govpay.orm.PagamentoPortale}
	 * 
	 * @param pagamentoPortale Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(PagamentoPortale pagamentoPortale) throws SerializerException {
		return this.objToXml(PagamentoPortale.class, pagamentoPortale, false).toString();
	}
	/**
	 * Serialize to String the object <var>pagamentoPortale</var> of type {@link it.govpay.orm.PagamentoPortale}
	 * 
	 * @param pagamentoPortale Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(PagamentoPortale pagamentoPortale,boolean prettyPrint) throws SerializerException {
		return this.objToXml(PagamentoPortale.class, pagamentoPortale, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-iuv
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idIuv</var> of type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param fileName Xml file to serialize the object <var>idIuv</var>
	 * @param idIuv Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdIuv idIuv) throws SerializerException {
		this.objToXml(fileName, IdIuv.class, idIuv, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idIuv</var> of type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param fileName Xml file to serialize the object <var>idIuv</var>
	 * @param idIuv Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdIuv idIuv,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdIuv.class, idIuv, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idIuv</var> of type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param file Xml file to serialize the object <var>idIuv</var>
	 * @param idIuv Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdIuv idIuv) throws SerializerException {
		this.objToXml(file, IdIuv.class, idIuv, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idIuv</var> of type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param file Xml file to serialize the object <var>idIuv</var>
	 * @param idIuv Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdIuv idIuv,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdIuv.class, idIuv, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idIuv</var> of type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param out OutputStream to serialize the object <var>idIuv</var>
	 * @param idIuv Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdIuv idIuv) throws SerializerException {
		this.objToXml(out, IdIuv.class, idIuv, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idIuv</var> of type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param out OutputStream to serialize the object <var>idIuv</var>
	 * @param idIuv Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdIuv idIuv,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdIuv.class, idIuv, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idIuv</var> of type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param idIuv Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdIuv idIuv) throws SerializerException {
		return this.objToXml(IdIuv.class, idIuv, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idIuv</var> of type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param idIuv Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdIuv idIuv,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdIuv.class, idIuv, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idIuv</var> of type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param idIuv Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdIuv idIuv) throws SerializerException {
		return this.objToXml(IdIuv.class, idIuv, false).toString();
	}
	/**
	 * Serialize to String the object <var>idIuv</var> of type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param idIuv Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdIuv idIuv,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdIuv.class, idIuv, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-vista-riscossione
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idVistaRiscossione</var> of type {@link it.govpay.orm.IdVistaRiscossione}
	 * 
	 * @param fileName Xml file to serialize the object <var>idVistaRiscossione</var>
	 * @param idVistaRiscossione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdVistaRiscossione idVistaRiscossione) throws SerializerException {
		this.objToXml(fileName, IdVistaRiscossione.class, idVistaRiscossione, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idVistaRiscossione</var> of type {@link it.govpay.orm.IdVistaRiscossione}
	 * 
	 * @param fileName Xml file to serialize the object <var>idVistaRiscossione</var>
	 * @param idVistaRiscossione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdVistaRiscossione idVistaRiscossione,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdVistaRiscossione.class, idVistaRiscossione, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idVistaRiscossione</var> of type {@link it.govpay.orm.IdVistaRiscossione}
	 * 
	 * @param file Xml file to serialize the object <var>idVistaRiscossione</var>
	 * @param idVistaRiscossione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdVistaRiscossione idVistaRiscossione) throws SerializerException {
		this.objToXml(file, IdVistaRiscossione.class, idVistaRiscossione, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idVistaRiscossione</var> of type {@link it.govpay.orm.IdVistaRiscossione}
	 * 
	 * @param file Xml file to serialize the object <var>idVistaRiscossione</var>
	 * @param idVistaRiscossione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdVistaRiscossione idVistaRiscossione,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdVistaRiscossione.class, idVistaRiscossione, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idVistaRiscossione</var> of type {@link it.govpay.orm.IdVistaRiscossione}
	 * 
	 * @param out OutputStream to serialize the object <var>idVistaRiscossione</var>
	 * @param idVistaRiscossione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdVistaRiscossione idVistaRiscossione) throws SerializerException {
		this.objToXml(out, IdVistaRiscossione.class, idVistaRiscossione, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idVistaRiscossione</var> of type {@link it.govpay.orm.IdVistaRiscossione}
	 * 
	 * @param out OutputStream to serialize the object <var>idVistaRiscossione</var>
	 * @param idVistaRiscossione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdVistaRiscossione idVistaRiscossione,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdVistaRiscossione.class, idVistaRiscossione, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idVistaRiscossione</var> of type {@link it.govpay.orm.IdVistaRiscossione}
	 * 
	 * @param idVistaRiscossione Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdVistaRiscossione idVistaRiscossione) throws SerializerException {
		return this.objToXml(IdVistaRiscossione.class, idVistaRiscossione, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idVistaRiscossione</var> of type {@link it.govpay.orm.IdVistaRiscossione}
	 * 
	 * @param idVistaRiscossione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdVistaRiscossione idVistaRiscossione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdVistaRiscossione.class, idVistaRiscossione, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idVistaRiscossione</var> of type {@link it.govpay.orm.IdVistaRiscossione}
	 * 
	 * @param idVistaRiscossione Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdVistaRiscossione idVistaRiscossione) throws SerializerException {
		return this.objToXml(IdVistaRiscossione.class, idVistaRiscossione, false).toString();
	}
	/**
	 * Serialize to String the object <var>idVistaRiscossione</var> of type {@link it.govpay.orm.IdVistaRiscossione}
	 * 
	 * @param idVistaRiscossione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdVistaRiscossione idVistaRiscossione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdVistaRiscossione.class, idVistaRiscossione, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-er
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idEr</var> of type {@link it.govpay.orm.IdEr}
	 * 
	 * @param fileName Xml file to serialize the object <var>idEr</var>
	 * @param idEr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdEr idEr) throws SerializerException {
		this.objToXml(fileName, IdEr.class, idEr, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idEr</var> of type {@link it.govpay.orm.IdEr}
	 * 
	 * @param fileName Xml file to serialize the object <var>idEr</var>
	 * @param idEr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdEr idEr,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdEr.class, idEr, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idEr</var> of type {@link it.govpay.orm.IdEr}
	 * 
	 * @param file Xml file to serialize the object <var>idEr</var>
	 * @param idEr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdEr idEr) throws SerializerException {
		this.objToXml(file, IdEr.class, idEr, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idEr</var> of type {@link it.govpay.orm.IdEr}
	 * 
	 * @param file Xml file to serialize the object <var>idEr</var>
	 * @param idEr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdEr idEr,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdEr.class, idEr, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idEr</var> of type {@link it.govpay.orm.IdEr}
	 * 
	 * @param out OutputStream to serialize the object <var>idEr</var>
	 * @param idEr Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdEr idEr) throws SerializerException {
		this.objToXml(out, IdEr.class, idEr, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idEr</var> of type {@link it.govpay.orm.IdEr}
	 * 
	 * @param out OutputStream to serialize the object <var>idEr</var>
	 * @param idEr Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdEr idEr,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdEr.class, idEr, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idEr</var> of type {@link it.govpay.orm.IdEr}
	 * 
	 * @param idEr Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdEr idEr) throws SerializerException {
		return this.objToXml(IdEr.class, idEr, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idEr</var> of type {@link it.govpay.orm.IdEr}
	 * 
	 * @param idEr Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdEr idEr,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdEr.class, idEr, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idEr</var> of type {@link it.govpay.orm.IdEr}
	 * 
	 * @param idEr Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdEr idEr) throws SerializerException {
		return this.objToXml(IdEr.class, idEr, false).toString();
	}
	/**
	 * Serialize to String the object <var>idEr</var> of type {@link it.govpay.orm.IdEr}
	 * 
	 * @param idEr Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdEr idEr,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdEr.class, idEr, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Applicazione
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>applicazione</var> of type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>applicazione</var>
	 * @param applicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Applicazione applicazione) throws SerializerException {
		this.objToXml(fileName, Applicazione.class, applicazione, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>applicazione</var> of type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>applicazione</var>
	 * @param applicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Applicazione applicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Applicazione.class, applicazione, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>applicazione</var> of type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param file Xml file to serialize the object <var>applicazione</var>
	 * @param applicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Applicazione applicazione) throws SerializerException {
		this.objToXml(file, Applicazione.class, applicazione, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>applicazione</var> of type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param file Xml file to serialize the object <var>applicazione</var>
	 * @param applicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Applicazione applicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Applicazione.class, applicazione, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>applicazione</var> of type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param out OutputStream to serialize the object <var>applicazione</var>
	 * @param applicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Applicazione applicazione) throws SerializerException {
		this.objToXml(out, Applicazione.class, applicazione, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>applicazione</var> of type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param out OutputStream to serialize the object <var>applicazione</var>
	 * @param applicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Applicazione applicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Applicazione.class, applicazione, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>applicazione</var> of type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param applicazione Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Applicazione applicazione) throws SerializerException {
		return this.objToXml(Applicazione.class, applicazione, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>applicazione</var> of type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param applicazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Applicazione applicazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Applicazione.class, applicazione, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>applicazione</var> of type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param applicazione Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Applicazione applicazione) throws SerializerException {
		return this.objToXml(Applicazione.class, applicazione, false).toString();
	}
	/**
	 * Serialize to String the object <var>applicazione</var> of type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param applicazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Applicazione applicazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Applicazione.class, applicazione, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-anagrafica
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idAnagrafica</var> of type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param fileName Xml file to serialize the object <var>idAnagrafica</var>
	 * @param idAnagrafica Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdAnagrafica idAnagrafica) throws SerializerException {
		this.objToXml(fileName, IdAnagrafica.class, idAnagrafica, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idAnagrafica</var> of type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param fileName Xml file to serialize the object <var>idAnagrafica</var>
	 * @param idAnagrafica Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdAnagrafica idAnagrafica,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdAnagrafica.class, idAnagrafica, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idAnagrafica</var> of type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param file Xml file to serialize the object <var>idAnagrafica</var>
	 * @param idAnagrafica Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdAnagrafica idAnagrafica) throws SerializerException {
		this.objToXml(file, IdAnagrafica.class, idAnagrafica, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idAnagrafica</var> of type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param file Xml file to serialize the object <var>idAnagrafica</var>
	 * @param idAnagrafica Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdAnagrafica idAnagrafica,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdAnagrafica.class, idAnagrafica, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idAnagrafica</var> of type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param out OutputStream to serialize the object <var>idAnagrafica</var>
	 * @param idAnagrafica Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdAnagrafica idAnagrafica) throws SerializerException {
		this.objToXml(out, IdAnagrafica.class, idAnagrafica, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idAnagrafica</var> of type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param out OutputStream to serialize the object <var>idAnagrafica</var>
	 * @param idAnagrafica Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdAnagrafica idAnagrafica,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdAnagrafica.class, idAnagrafica, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idAnagrafica</var> of type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param idAnagrafica Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdAnagrafica idAnagrafica) throws SerializerException {
		return this.objToXml(IdAnagrafica.class, idAnagrafica, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idAnagrafica</var> of type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param idAnagrafica Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdAnagrafica idAnagrafica,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdAnagrafica.class, idAnagrafica, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idAnagrafica</var> of type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param idAnagrafica Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdAnagrafica idAnagrafica) throws SerializerException {
		return this.objToXml(IdAnagrafica.class, idAnagrafica, false).toString();
	}
	/**
	 * Serialize to String the object <var>idAnagrafica</var> of type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param idAnagrafica Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdAnagrafica idAnagrafica,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdAnagrafica.class, idAnagrafica, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: RPT
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>rpt</var> of type {@link it.govpay.orm.RPT}
	 * 
	 * @param fileName Xml file to serialize the object <var>rpt</var>
	 * @param rpt Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,RPT rpt) throws SerializerException {
		this.objToXml(fileName, RPT.class, rpt, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>rpt</var> of type {@link it.govpay.orm.RPT}
	 * 
	 * @param fileName Xml file to serialize the object <var>rpt</var>
	 * @param rpt Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,RPT rpt,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, RPT.class, rpt, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>rpt</var> of type {@link it.govpay.orm.RPT}
	 * 
	 * @param file Xml file to serialize the object <var>rpt</var>
	 * @param rpt Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,RPT rpt) throws SerializerException {
		this.objToXml(file, RPT.class, rpt, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>rpt</var> of type {@link it.govpay.orm.RPT}
	 * 
	 * @param file Xml file to serialize the object <var>rpt</var>
	 * @param rpt Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,RPT rpt,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, RPT.class, rpt, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>rpt</var> of type {@link it.govpay.orm.RPT}
	 * 
	 * @param out OutputStream to serialize the object <var>rpt</var>
	 * @param rpt Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,RPT rpt) throws SerializerException {
		this.objToXml(out, RPT.class, rpt, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>rpt</var> of type {@link it.govpay.orm.RPT}
	 * 
	 * @param out OutputStream to serialize the object <var>rpt</var>
	 * @param rpt Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,RPT rpt,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, RPT.class, rpt, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>rpt</var> of type {@link it.govpay.orm.RPT}
	 * 
	 * @param rpt Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(RPT rpt) throws SerializerException {
		return this.objToXml(RPT.class, rpt, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>rpt</var> of type {@link it.govpay.orm.RPT}
	 * 
	 * @param rpt Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(RPT rpt,boolean prettyPrint) throws SerializerException {
		return this.objToXml(RPT.class, rpt, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>rpt</var> of type {@link it.govpay.orm.RPT}
	 * 
	 * @param rpt Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(RPT rpt) throws SerializerException {
		return this.objToXml(RPT.class, rpt, false).toString();
	}
	/**
	 * Serialize to String the object <var>rpt</var> of type {@link it.govpay.orm.RPT}
	 * 
	 * @param rpt Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(RPT rpt,boolean prettyPrint) throws SerializerException {
		return this.objToXml(RPT.class, rpt, prettyPrint).toString();
	}
	
	
	

}
