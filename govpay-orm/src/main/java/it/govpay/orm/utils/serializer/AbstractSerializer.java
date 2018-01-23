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

import org.openspcoop2.generic_project.exception.SerializerException;
import org.openspcoop2.utils.beans.WriteToSerializerType;
import org.openspcoop2.utils.xml.JaxbUtils;

import it.govpay.orm.Ruolo;
import it.govpay.orm.IdPsp;
import it.govpay.orm.Canale;
import it.govpay.orm.IuvSearch;
import it.govpay.orm.Audit;
import it.govpay.orm.IdOperatore;
import it.govpay.orm.IdIncasso;
import it.govpay.orm.FR;
import it.govpay.orm.RendicontazionePagamento;
import it.govpay.orm.Rendicontazione;
import it.govpay.orm.Pagamento;
import it.govpay.orm.SingoloVersamento;
import it.govpay.orm.Versamento;
import it.govpay.orm.IdIntermediario;
import it.govpay.orm.Stazione;
import it.govpay.orm.IdTracciato;
import it.govpay.orm.Operazione;
import it.govpay.orm.IdApplicazione;
import it.govpay.orm.IdRilevamento;
import it.govpay.orm.IdPagamentoPortale;
import it.govpay.orm.PagamentoPortaleVersamento;
import it.govpay.orm.IdVersamento;
import it.govpay.orm.Incasso;
import it.govpay.orm.IdDominio;
import it.govpay.orm.IdAcl;
import it.govpay.orm.IdCanale;
import it.govpay.orm.Intermediario;
import it.govpay.orm.IdPagamento;
import it.govpay.orm.IdNotifica;
import it.govpay.orm.IdSla;
import it.govpay.orm.IdMediaRilevamento;
import it.govpay.orm.Psp;
import it.govpay.orm.IdRpt;
import it.govpay.orm.RR;
import it.govpay.orm.Operatore;
import it.govpay.orm.Tracciato;
import it.govpay.orm.IdStazione;
import it.govpay.orm.Dominio;
import it.govpay.orm.IdSingoloVersamento;
import it.govpay.orm.IdTributo;
import it.govpay.orm.IdIbanAccredito;
import it.govpay.orm.IdPortale;
import it.govpay.orm.Evento;
import it.govpay.orm.IdUo;
import it.govpay.orm.IdTipoTributo;
import it.govpay.orm.IdBatch;
import it.govpay.orm.IdTabellaControparti;
import it.govpay.orm.TipoTributo;
import it.govpay.orm.IdContoAccredito;
import it.govpay.orm.Tributo;
import it.govpay.orm.IUV;
import it.govpay.orm.IdConnettore;
import it.govpay.orm.Portale;
import it.govpay.orm.IdEvento;
import it.govpay.orm.Connettore;
import it.govpay.orm.IdRuolo;
import it.govpay.orm.Uo;
import it.govpay.orm.ACL;
import it.govpay.orm.Notifica;
import it.govpay.orm.IdRr;
import it.govpay.orm.IdSingolaRendicontazione;
import it.govpay.orm.IbanAccredito;
import it.govpay.orm.IdMailTemplate;
import it.govpay.orm.IdFr;
import it.govpay.orm.IdRendicontazione;
import it.govpay.orm.IdCarrello;
import it.govpay.orm.Batch;
import it.govpay.orm.IdMessaggio;
import it.govpay.orm.IdSingolaRevoca;
import it.govpay.orm.IdMail;
import it.govpay.orm.PagamentoPortale;
import it.govpay.orm.IdIuv;
import it.govpay.orm.IdEr;
import it.govpay.orm.Applicazione;
import it.govpay.orm.IdAnagrafica;
import it.govpay.orm.RPT;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.File;
import java.lang.reflect.Method;

import javax.xml.bind.JAXBElement;

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
	 Object: Ruolo
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>ruolo</var> of type {@link it.govpay.orm.Ruolo}
	 * 
	 * @param fileName Xml file to serialize the object <var>ruolo</var>
	 * @param ruolo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Ruolo ruolo) throws SerializerException {
		this.objToXml(fileName, Ruolo.class, ruolo, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>ruolo</var> of type {@link it.govpay.orm.Ruolo}
	 * 
	 * @param fileName Xml file to serialize the object <var>ruolo</var>
	 * @param ruolo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Ruolo ruolo,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Ruolo.class, ruolo, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>ruolo</var> of type {@link it.govpay.orm.Ruolo}
	 * 
	 * @param file Xml file to serialize the object <var>ruolo</var>
	 * @param ruolo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Ruolo ruolo) throws SerializerException {
		this.objToXml(file, Ruolo.class, ruolo, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>ruolo</var> of type {@link it.govpay.orm.Ruolo}
	 * 
	 * @param file Xml file to serialize the object <var>ruolo</var>
	 * @param ruolo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Ruolo ruolo,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Ruolo.class, ruolo, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>ruolo</var> of type {@link it.govpay.orm.Ruolo}
	 * 
	 * @param out OutputStream to serialize the object <var>ruolo</var>
	 * @param ruolo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Ruolo ruolo) throws SerializerException {
		this.objToXml(out, Ruolo.class, ruolo, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>ruolo</var> of type {@link it.govpay.orm.Ruolo}
	 * 
	 * @param out OutputStream to serialize the object <var>ruolo</var>
	 * @param ruolo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Ruolo ruolo,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Ruolo.class, ruolo, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>ruolo</var> of type {@link it.govpay.orm.Ruolo}
	 * 
	 * @param ruolo Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Ruolo ruolo) throws SerializerException {
		return this.objToXml(Ruolo.class, ruolo, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>ruolo</var> of type {@link it.govpay.orm.Ruolo}
	 * 
	 * @param ruolo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Ruolo ruolo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Ruolo.class, ruolo, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>ruolo</var> of type {@link it.govpay.orm.Ruolo}
	 * 
	 * @param ruolo Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Ruolo ruolo) throws SerializerException {
		return this.objToXml(Ruolo.class, ruolo, false).toString();
	}
	/**
	 * Serialize to String the object <var>ruolo</var> of type {@link it.govpay.orm.Ruolo}
	 * 
	 * @param ruolo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Ruolo ruolo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Ruolo.class, ruolo, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-psp
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idPsp</var> of type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param fileName Xml file to serialize the object <var>idPsp</var>
	 * @param idPsp Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdPsp idPsp) throws SerializerException {
		this.objToXml(fileName, IdPsp.class, idPsp, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idPsp</var> of type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param fileName Xml file to serialize the object <var>idPsp</var>
	 * @param idPsp Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdPsp idPsp,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdPsp.class, idPsp, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idPsp</var> of type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param file Xml file to serialize the object <var>idPsp</var>
	 * @param idPsp Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdPsp idPsp) throws SerializerException {
		this.objToXml(file, IdPsp.class, idPsp, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idPsp</var> of type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param file Xml file to serialize the object <var>idPsp</var>
	 * @param idPsp Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdPsp idPsp,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdPsp.class, idPsp, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idPsp</var> of type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param out OutputStream to serialize the object <var>idPsp</var>
	 * @param idPsp Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdPsp idPsp) throws SerializerException {
		this.objToXml(out, IdPsp.class, idPsp, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idPsp</var> of type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param out OutputStream to serialize the object <var>idPsp</var>
	 * @param idPsp Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdPsp idPsp,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdPsp.class, idPsp, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idPsp</var> of type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param idPsp Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdPsp idPsp) throws SerializerException {
		return this.objToXml(IdPsp.class, idPsp, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idPsp</var> of type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param idPsp Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdPsp idPsp,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdPsp.class, idPsp, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idPsp</var> of type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param idPsp Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdPsp idPsp) throws SerializerException {
		return this.objToXml(IdPsp.class, idPsp, false).toString();
	}
	/**
	 * Serialize to String the object <var>idPsp</var> of type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param idPsp Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdPsp idPsp,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdPsp.class, idPsp, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Canale
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>canale</var> of type {@link it.govpay.orm.Canale}
	 * 
	 * @param fileName Xml file to serialize the object <var>canale</var>
	 * @param canale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Canale canale) throws SerializerException {
		this.objToXml(fileName, Canale.class, canale, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>canale</var> of type {@link it.govpay.orm.Canale}
	 * 
	 * @param fileName Xml file to serialize the object <var>canale</var>
	 * @param canale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Canale canale,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Canale.class, canale, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>canale</var> of type {@link it.govpay.orm.Canale}
	 * 
	 * @param file Xml file to serialize the object <var>canale</var>
	 * @param canale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Canale canale) throws SerializerException {
		this.objToXml(file, Canale.class, canale, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>canale</var> of type {@link it.govpay.orm.Canale}
	 * 
	 * @param file Xml file to serialize the object <var>canale</var>
	 * @param canale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Canale canale,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Canale.class, canale, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>canale</var> of type {@link it.govpay.orm.Canale}
	 * 
	 * @param out OutputStream to serialize the object <var>canale</var>
	 * @param canale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Canale canale) throws SerializerException {
		this.objToXml(out, Canale.class, canale, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>canale</var> of type {@link it.govpay.orm.Canale}
	 * 
	 * @param out OutputStream to serialize the object <var>canale</var>
	 * @param canale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Canale canale,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Canale.class, canale, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>canale</var> of type {@link it.govpay.orm.Canale}
	 * 
	 * @param canale Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Canale canale) throws SerializerException {
		return this.objToXml(Canale.class, canale, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>canale</var> of type {@link it.govpay.orm.Canale}
	 * 
	 * @param canale Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Canale canale,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Canale.class, canale, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>canale</var> of type {@link it.govpay.orm.Canale}
	 * 
	 * @param canale Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Canale canale) throws SerializerException {
		return this.objToXml(Canale.class, canale, false).toString();
	}
	/**
	 * Serialize to String the object <var>canale</var> of type {@link it.govpay.orm.Canale}
	 * 
	 * @param canale Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Canale canale,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Canale.class, canale, prettyPrint).toString();
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
	 Object: RendicontazionePagamento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>rendicontazionePagamento</var> of type {@link it.govpay.orm.RendicontazionePagamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>rendicontazionePagamento</var>
	 * @param rendicontazionePagamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,RendicontazionePagamento rendicontazionePagamento) throws SerializerException {
		this.objToXml(fileName, RendicontazionePagamento.class, rendicontazionePagamento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>rendicontazionePagamento</var> of type {@link it.govpay.orm.RendicontazionePagamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>rendicontazionePagamento</var>
	 * @param rendicontazionePagamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,RendicontazionePagamento rendicontazionePagamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, RendicontazionePagamento.class, rendicontazionePagamento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>rendicontazionePagamento</var> of type {@link it.govpay.orm.RendicontazionePagamento}
	 * 
	 * @param file Xml file to serialize the object <var>rendicontazionePagamento</var>
	 * @param rendicontazionePagamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,RendicontazionePagamento rendicontazionePagamento) throws SerializerException {
		this.objToXml(file, RendicontazionePagamento.class, rendicontazionePagamento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>rendicontazionePagamento</var> of type {@link it.govpay.orm.RendicontazionePagamento}
	 * 
	 * @param file Xml file to serialize the object <var>rendicontazionePagamento</var>
	 * @param rendicontazionePagamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,RendicontazionePagamento rendicontazionePagamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, RendicontazionePagamento.class, rendicontazionePagamento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>rendicontazionePagamento</var> of type {@link it.govpay.orm.RendicontazionePagamento}
	 * 
	 * @param out OutputStream to serialize the object <var>rendicontazionePagamento</var>
	 * @param rendicontazionePagamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,RendicontazionePagamento rendicontazionePagamento) throws SerializerException {
		this.objToXml(out, RendicontazionePagamento.class, rendicontazionePagamento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>rendicontazionePagamento</var> of type {@link it.govpay.orm.RendicontazionePagamento}
	 * 
	 * @param out OutputStream to serialize the object <var>rendicontazionePagamento</var>
	 * @param rendicontazionePagamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,RendicontazionePagamento rendicontazionePagamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, RendicontazionePagamento.class, rendicontazionePagamento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>rendicontazionePagamento</var> of type {@link it.govpay.orm.RendicontazionePagamento}
	 * 
	 * @param rendicontazionePagamento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(RendicontazionePagamento rendicontazionePagamento) throws SerializerException {
		return this.objToXml(RendicontazionePagamento.class, rendicontazionePagamento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>rendicontazionePagamento</var> of type {@link it.govpay.orm.RendicontazionePagamento}
	 * 
	 * @param rendicontazionePagamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(RendicontazionePagamento rendicontazionePagamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(RendicontazionePagamento.class, rendicontazionePagamento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>rendicontazionePagamento</var> of type {@link it.govpay.orm.RendicontazionePagamento}
	 * 
	 * @param rendicontazionePagamento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(RendicontazionePagamento rendicontazionePagamento) throws SerializerException {
		return this.objToXml(RendicontazionePagamento.class, rendicontazionePagamento, false).toString();
	}
	/**
	 * Serialize to String the object <var>rendicontazionePagamento</var> of type {@link it.govpay.orm.RendicontazionePagamento}
	 * 
	 * @param rendicontazionePagamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(RendicontazionePagamento rendicontazionePagamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(RendicontazionePagamento.class, rendicontazionePagamento, prettyPrint).toString();
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
	 Object: id-rilevamento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idRilevamento</var> of type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>idRilevamento</var>
	 * @param idRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdRilevamento idRilevamento) throws SerializerException {
		this.objToXml(fileName, IdRilevamento.class, idRilevamento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idRilevamento</var> of type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>idRilevamento</var>
	 * @param idRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdRilevamento idRilevamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdRilevamento.class, idRilevamento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idRilevamento</var> of type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param file Xml file to serialize the object <var>idRilevamento</var>
	 * @param idRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdRilevamento idRilevamento) throws SerializerException {
		this.objToXml(file, IdRilevamento.class, idRilevamento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idRilevamento</var> of type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param file Xml file to serialize the object <var>idRilevamento</var>
	 * @param idRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdRilevamento idRilevamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdRilevamento.class, idRilevamento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idRilevamento</var> of type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param out OutputStream to serialize the object <var>idRilevamento</var>
	 * @param idRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdRilevamento idRilevamento) throws SerializerException {
		this.objToXml(out, IdRilevamento.class, idRilevamento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idRilevamento</var> of type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param out OutputStream to serialize the object <var>idRilevamento</var>
	 * @param idRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdRilevamento idRilevamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdRilevamento.class, idRilevamento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idRilevamento</var> of type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param idRilevamento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdRilevamento idRilevamento) throws SerializerException {
		return this.objToXml(IdRilevamento.class, idRilevamento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idRilevamento</var> of type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param idRilevamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdRilevamento idRilevamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdRilevamento.class, idRilevamento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idRilevamento</var> of type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param idRilevamento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdRilevamento idRilevamento) throws SerializerException {
		return this.objToXml(IdRilevamento.class, idRilevamento, false).toString();
	}
	/**
	 * Serialize to String the object <var>idRilevamento</var> of type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param idRilevamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdRilevamento idRilevamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdRilevamento.class, idRilevamento, prettyPrint).toString();
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
	 Object: id-canale
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idCanale</var> of type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param fileName Xml file to serialize the object <var>idCanale</var>
	 * @param idCanale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdCanale idCanale) throws SerializerException {
		this.objToXml(fileName, IdCanale.class, idCanale, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idCanale</var> of type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param fileName Xml file to serialize the object <var>idCanale</var>
	 * @param idCanale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdCanale idCanale,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdCanale.class, idCanale, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idCanale</var> of type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param file Xml file to serialize the object <var>idCanale</var>
	 * @param idCanale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdCanale idCanale) throws SerializerException {
		this.objToXml(file, IdCanale.class, idCanale, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idCanale</var> of type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param file Xml file to serialize the object <var>idCanale</var>
	 * @param idCanale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdCanale idCanale,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdCanale.class, idCanale, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idCanale</var> of type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param out OutputStream to serialize the object <var>idCanale</var>
	 * @param idCanale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdCanale idCanale) throws SerializerException {
		this.objToXml(out, IdCanale.class, idCanale, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idCanale</var> of type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param out OutputStream to serialize the object <var>idCanale</var>
	 * @param idCanale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdCanale idCanale,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdCanale.class, idCanale, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idCanale</var> of type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param idCanale Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdCanale idCanale) throws SerializerException {
		return this.objToXml(IdCanale.class, idCanale, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idCanale</var> of type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param idCanale Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdCanale idCanale,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdCanale.class, idCanale, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idCanale</var> of type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param idCanale Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdCanale idCanale) throws SerializerException {
		return this.objToXml(IdCanale.class, idCanale, false).toString();
	}
	/**
	 * Serialize to String the object <var>idCanale</var> of type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param idCanale Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdCanale idCanale,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdCanale.class, idCanale, prettyPrint).toString();
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
	 Object: id-sla
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idSla</var> of type {@link it.govpay.orm.IdSla}
	 * 
	 * @param fileName Xml file to serialize the object <var>idSla</var>
	 * @param idSla Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdSla idSla) throws SerializerException {
		this.objToXml(fileName, IdSla.class, idSla, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idSla</var> of type {@link it.govpay.orm.IdSla}
	 * 
	 * @param fileName Xml file to serialize the object <var>idSla</var>
	 * @param idSla Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdSla idSla,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdSla.class, idSla, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idSla</var> of type {@link it.govpay.orm.IdSla}
	 * 
	 * @param file Xml file to serialize the object <var>idSla</var>
	 * @param idSla Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdSla idSla) throws SerializerException {
		this.objToXml(file, IdSla.class, idSla, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idSla</var> of type {@link it.govpay.orm.IdSla}
	 * 
	 * @param file Xml file to serialize the object <var>idSla</var>
	 * @param idSla Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdSla idSla,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdSla.class, idSla, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idSla</var> of type {@link it.govpay.orm.IdSla}
	 * 
	 * @param out OutputStream to serialize the object <var>idSla</var>
	 * @param idSla Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdSla idSla) throws SerializerException {
		this.objToXml(out, IdSla.class, idSla, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idSla</var> of type {@link it.govpay.orm.IdSla}
	 * 
	 * @param out OutputStream to serialize the object <var>idSla</var>
	 * @param idSla Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdSla idSla,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdSla.class, idSla, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idSla</var> of type {@link it.govpay.orm.IdSla}
	 * 
	 * @param idSla Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdSla idSla) throws SerializerException {
		return this.objToXml(IdSla.class, idSla, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idSla</var> of type {@link it.govpay.orm.IdSla}
	 * 
	 * @param idSla Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdSla idSla,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdSla.class, idSla, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idSla</var> of type {@link it.govpay.orm.IdSla}
	 * 
	 * @param idSla Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdSla idSla) throws SerializerException {
		return this.objToXml(IdSla.class, idSla, false).toString();
	}
	/**
	 * Serialize to String the object <var>idSla</var> of type {@link it.govpay.orm.IdSla}
	 * 
	 * @param idSla Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdSla idSla,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdSla.class, idSla, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: id-media-rilevamento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idMediaRilevamento</var> of type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>idMediaRilevamento</var>
	 * @param idMediaRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdMediaRilevamento idMediaRilevamento) throws SerializerException {
		this.objToXml(fileName, IdMediaRilevamento.class, idMediaRilevamento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idMediaRilevamento</var> of type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>idMediaRilevamento</var>
	 * @param idMediaRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdMediaRilevamento idMediaRilevamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdMediaRilevamento.class, idMediaRilevamento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idMediaRilevamento</var> of type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param file Xml file to serialize the object <var>idMediaRilevamento</var>
	 * @param idMediaRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdMediaRilevamento idMediaRilevamento) throws SerializerException {
		this.objToXml(file, IdMediaRilevamento.class, idMediaRilevamento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idMediaRilevamento</var> of type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param file Xml file to serialize the object <var>idMediaRilevamento</var>
	 * @param idMediaRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdMediaRilevamento idMediaRilevamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdMediaRilevamento.class, idMediaRilevamento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idMediaRilevamento</var> of type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param out OutputStream to serialize the object <var>idMediaRilevamento</var>
	 * @param idMediaRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdMediaRilevamento idMediaRilevamento) throws SerializerException {
		this.objToXml(out, IdMediaRilevamento.class, idMediaRilevamento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idMediaRilevamento</var> of type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param out OutputStream to serialize the object <var>idMediaRilevamento</var>
	 * @param idMediaRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdMediaRilevamento idMediaRilevamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdMediaRilevamento.class, idMediaRilevamento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idMediaRilevamento</var> of type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param idMediaRilevamento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdMediaRilevamento idMediaRilevamento) throws SerializerException {
		return this.objToXml(IdMediaRilevamento.class, idMediaRilevamento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idMediaRilevamento</var> of type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param idMediaRilevamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdMediaRilevamento idMediaRilevamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdMediaRilevamento.class, idMediaRilevamento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idMediaRilevamento</var> of type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param idMediaRilevamento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdMediaRilevamento idMediaRilevamento) throws SerializerException {
		return this.objToXml(IdMediaRilevamento.class, idMediaRilevamento, false).toString();
	}
	/**
	 * Serialize to String the object <var>idMediaRilevamento</var> of type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param idMediaRilevamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdMediaRilevamento idMediaRilevamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdMediaRilevamento.class, idMediaRilevamento, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Psp
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>psp</var> of type {@link it.govpay.orm.Psp}
	 * 
	 * @param fileName Xml file to serialize the object <var>psp</var>
	 * @param psp Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Psp psp) throws SerializerException {
		this.objToXml(fileName, Psp.class, psp, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>psp</var> of type {@link it.govpay.orm.Psp}
	 * 
	 * @param fileName Xml file to serialize the object <var>psp</var>
	 * @param psp Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Psp psp,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Psp.class, psp, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>psp</var> of type {@link it.govpay.orm.Psp}
	 * 
	 * @param file Xml file to serialize the object <var>psp</var>
	 * @param psp Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Psp psp) throws SerializerException {
		this.objToXml(file, Psp.class, psp, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>psp</var> of type {@link it.govpay.orm.Psp}
	 * 
	 * @param file Xml file to serialize the object <var>psp</var>
	 * @param psp Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Psp psp,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Psp.class, psp, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>psp</var> of type {@link it.govpay.orm.Psp}
	 * 
	 * @param out OutputStream to serialize the object <var>psp</var>
	 * @param psp Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Psp psp) throws SerializerException {
		this.objToXml(out, Psp.class, psp, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>psp</var> of type {@link it.govpay.orm.Psp}
	 * 
	 * @param out OutputStream to serialize the object <var>psp</var>
	 * @param psp Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Psp psp,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Psp.class, psp, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>psp</var> of type {@link it.govpay.orm.Psp}
	 * 
	 * @param psp Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Psp psp) throws SerializerException {
		return this.objToXml(Psp.class, psp, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>psp</var> of type {@link it.govpay.orm.Psp}
	 * 
	 * @param psp Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Psp psp,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Psp.class, psp, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>psp</var> of type {@link it.govpay.orm.Psp}
	 * 
	 * @param psp Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Psp psp) throws SerializerException {
		return this.objToXml(Psp.class, psp, false).toString();
	}
	/**
	 * Serialize to String the object <var>psp</var> of type {@link it.govpay.orm.Psp}
	 * 
	 * @param psp Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Psp psp,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Psp.class, psp, prettyPrint).toString();
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
	 Object: id-portale
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idPortale</var> of type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param fileName Xml file to serialize the object <var>idPortale</var>
	 * @param idPortale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdPortale idPortale) throws SerializerException {
		this.objToXml(fileName, IdPortale.class, idPortale, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idPortale</var> of type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param fileName Xml file to serialize the object <var>idPortale</var>
	 * @param idPortale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdPortale idPortale,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdPortale.class, idPortale, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idPortale</var> of type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param file Xml file to serialize the object <var>idPortale</var>
	 * @param idPortale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdPortale idPortale) throws SerializerException {
		this.objToXml(file, IdPortale.class, idPortale, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idPortale</var> of type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param file Xml file to serialize the object <var>idPortale</var>
	 * @param idPortale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdPortale idPortale,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdPortale.class, idPortale, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idPortale</var> of type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param out OutputStream to serialize the object <var>idPortale</var>
	 * @param idPortale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdPortale idPortale) throws SerializerException {
		this.objToXml(out, IdPortale.class, idPortale, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idPortale</var> of type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param out OutputStream to serialize the object <var>idPortale</var>
	 * @param idPortale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdPortale idPortale,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdPortale.class, idPortale, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idPortale</var> of type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param idPortale Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdPortale idPortale) throws SerializerException {
		return this.objToXml(IdPortale.class, idPortale, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idPortale</var> of type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param idPortale Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdPortale idPortale,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdPortale.class, idPortale, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idPortale</var> of type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param idPortale Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdPortale idPortale) throws SerializerException {
		return this.objToXml(IdPortale.class, idPortale, false).toString();
	}
	/**
	 * Serialize to String the object <var>idPortale</var> of type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param idPortale Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdPortale idPortale,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdPortale.class, idPortale, prettyPrint).toString();
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
	 Object: Portale
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>portale</var> of type {@link it.govpay.orm.Portale}
	 * 
	 * @param fileName Xml file to serialize the object <var>portale</var>
	 * @param portale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Portale portale) throws SerializerException {
		this.objToXml(fileName, Portale.class, portale, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>portale</var> of type {@link it.govpay.orm.Portale}
	 * 
	 * @param fileName Xml file to serialize the object <var>portale</var>
	 * @param portale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Portale portale,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Portale.class, portale, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>portale</var> of type {@link it.govpay.orm.Portale}
	 * 
	 * @param file Xml file to serialize the object <var>portale</var>
	 * @param portale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Portale portale) throws SerializerException {
		this.objToXml(file, Portale.class, portale, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>portale</var> of type {@link it.govpay.orm.Portale}
	 * 
	 * @param file Xml file to serialize the object <var>portale</var>
	 * @param portale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Portale portale,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Portale.class, portale, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>portale</var> of type {@link it.govpay.orm.Portale}
	 * 
	 * @param out OutputStream to serialize the object <var>portale</var>
	 * @param portale Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Portale portale) throws SerializerException {
		this.objToXml(out, Portale.class, portale, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>portale</var> of type {@link it.govpay.orm.Portale}
	 * 
	 * @param out OutputStream to serialize the object <var>portale</var>
	 * @param portale Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Portale portale,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Portale.class, portale, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>portale</var> of type {@link it.govpay.orm.Portale}
	 * 
	 * @param portale Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Portale portale) throws SerializerException {
		return this.objToXml(Portale.class, portale, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>portale</var> of type {@link it.govpay.orm.Portale}
	 * 
	 * @param portale Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Portale portale,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Portale.class, portale, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>portale</var> of type {@link it.govpay.orm.Portale}
	 * 
	 * @param portale Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Portale portale) throws SerializerException {
		return this.objToXml(Portale.class, portale, false).toString();
	}
	/**
	 * Serialize to String the object <var>portale</var> of type {@link it.govpay.orm.Portale}
	 * 
	 * @param portale Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Portale portale,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Portale.class, portale, prettyPrint).toString();
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
	 Object: id-ruolo
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idRuolo</var> of type {@link it.govpay.orm.IdRuolo}
	 * 
	 * @param fileName Xml file to serialize the object <var>idRuolo</var>
	 * @param idRuolo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdRuolo idRuolo) throws SerializerException {
		this.objToXml(fileName, IdRuolo.class, idRuolo, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idRuolo</var> of type {@link it.govpay.orm.IdRuolo}
	 * 
	 * @param fileName Xml file to serialize the object <var>idRuolo</var>
	 * @param idRuolo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdRuolo idRuolo,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdRuolo.class, idRuolo, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idRuolo</var> of type {@link it.govpay.orm.IdRuolo}
	 * 
	 * @param file Xml file to serialize the object <var>idRuolo</var>
	 * @param idRuolo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdRuolo idRuolo) throws SerializerException {
		this.objToXml(file, IdRuolo.class, idRuolo, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idRuolo</var> of type {@link it.govpay.orm.IdRuolo}
	 * 
	 * @param file Xml file to serialize the object <var>idRuolo</var>
	 * @param idRuolo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdRuolo idRuolo,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdRuolo.class, idRuolo, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idRuolo</var> of type {@link it.govpay.orm.IdRuolo}
	 * 
	 * @param out OutputStream to serialize the object <var>idRuolo</var>
	 * @param idRuolo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdRuolo idRuolo) throws SerializerException {
		this.objToXml(out, IdRuolo.class, idRuolo, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idRuolo</var> of type {@link it.govpay.orm.IdRuolo}
	 * 
	 * @param out OutputStream to serialize the object <var>idRuolo</var>
	 * @param idRuolo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdRuolo idRuolo,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdRuolo.class, idRuolo, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idRuolo</var> of type {@link it.govpay.orm.IdRuolo}
	 * 
	 * @param idRuolo Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdRuolo idRuolo) throws SerializerException {
		return this.objToXml(IdRuolo.class, idRuolo, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idRuolo</var> of type {@link it.govpay.orm.IdRuolo}
	 * 
	 * @param idRuolo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdRuolo idRuolo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdRuolo.class, idRuolo, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idRuolo</var> of type {@link it.govpay.orm.IdRuolo}
	 * 
	 * @param idRuolo Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdRuolo idRuolo) throws SerializerException {
		return this.objToXml(IdRuolo.class, idRuolo, false).toString();
	}
	/**
	 * Serialize to String the object <var>idRuolo</var> of type {@link it.govpay.orm.IdRuolo}
	 * 
	 * @param idRuolo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdRuolo idRuolo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdRuolo.class, idRuolo, prettyPrint).toString();
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
	 Object: id-mail-template
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idMailTemplate</var> of type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param fileName Xml file to serialize the object <var>idMailTemplate</var>
	 * @param idMailTemplate Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdMailTemplate idMailTemplate) throws SerializerException {
		this.objToXml(fileName, IdMailTemplate.class, idMailTemplate, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idMailTemplate</var> of type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param fileName Xml file to serialize the object <var>idMailTemplate</var>
	 * @param idMailTemplate Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdMailTemplate idMailTemplate,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdMailTemplate.class, idMailTemplate, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idMailTemplate</var> of type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param file Xml file to serialize the object <var>idMailTemplate</var>
	 * @param idMailTemplate Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdMailTemplate idMailTemplate) throws SerializerException {
		this.objToXml(file, IdMailTemplate.class, idMailTemplate, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idMailTemplate</var> of type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param file Xml file to serialize the object <var>idMailTemplate</var>
	 * @param idMailTemplate Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdMailTemplate idMailTemplate,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdMailTemplate.class, idMailTemplate, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idMailTemplate</var> of type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param out OutputStream to serialize the object <var>idMailTemplate</var>
	 * @param idMailTemplate Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdMailTemplate idMailTemplate) throws SerializerException {
		this.objToXml(out, IdMailTemplate.class, idMailTemplate, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idMailTemplate</var> of type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param out OutputStream to serialize the object <var>idMailTemplate</var>
	 * @param idMailTemplate Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdMailTemplate idMailTemplate,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdMailTemplate.class, idMailTemplate, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idMailTemplate</var> of type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param idMailTemplate Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdMailTemplate idMailTemplate) throws SerializerException {
		return this.objToXml(IdMailTemplate.class, idMailTemplate, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idMailTemplate</var> of type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param idMailTemplate Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdMailTemplate idMailTemplate,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdMailTemplate.class, idMailTemplate, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idMailTemplate</var> of type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param idMailTemplate Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdMailTemplate idMailTemplate) throws SerializerException {
		return this.objToXml(IdMailTemplate.class, idMailTemplate, false).toString();
	}
	/**
	 * Serialize to String the object <var>idMailTemplate</var> of type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param idMailTemplate Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdMailTemplate idMailTemplate,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdMailTemplate.class, idMailTemplate, prettyPrint).toString();
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
	 Object: id-mail
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idMail</var> of type {@link it.govpay.orm.IdMail}
	 * 
	 * @param fileName Xml file to serialize the object <var>idMail</var>
	 * @param idMail Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdMail idMail) throws SerializerException {
		this.objToXml(fileName, IdMail.class, idMail, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idMail</var> of type {@link it.govpay.orm.IdMail}
	 * 
	 * @param fileName Xml file to serialize the object <var>idMail</var>
	 * @param idMail Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdMail idMail,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdMail.class, idMail, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idMail</var> of type {@link it.govpay.orm.IdMail}
	 * 
	 * @param file Xml file to serialize the object <var>idMail</var>
	 * @param idMail Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdMail idMail) throws SerializerException {
		this.objToXml(file, IdMail.class, idMail, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idMail</var> of type {@link it.govpay.orm.IdMail}
	 * 
	 * @param file Xml file to serialize the object <var>idMail</var>
	 * @param idMail Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdMail idMail,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdMail.class, idMail, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idMail</var> of type {@link it.govpay.orm.IdMail}
	 * 
	 * @param out OutputStream to serialize the object <var>idMail</var>
	 * @param idMail Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdMail idMail) throws SerializerException {
		this.objToXml(out, IdMail.class, idMail, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idMail</var> of type {@link it.govpay.orm.IdMail}
	 * 
	 * @param out OutputStream to serialize the object <var>idMail</var>
	 * @param idMail Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdMail idMail,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdMail.class, idMail, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idMail</var> of type {@link it.govpay.orm.IdMail}
	 * 
	 * @param idMail Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdMail idMail) throws SerializerException {
		return this.objToXml(IdMail.class, idMail, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idMail</var> of type {@link it.govpay.orm.IdMail}
	 * 
	 * @param idMail Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdMail idMail,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdMail.class, idMail, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idMail</var> of type {@link it.govpay.orm.IdMail}
	 * 
	 * @param idMail Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdMail idMail) throws SerializerException {
		return this.objToXml(IdMail.class, idMail, false).toString();
	}
	/**
	 * Serialize to String the object <var>idMail</var> of type {@link it.govpay.orm.IdMail}
	 * 
	 * @param idMail Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdMail idMail,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdMail.class, idMail, prettyPrint).toString();
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
