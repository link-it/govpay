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
package it.govpay.orm.utils.serializer;

import org.openspcoop2.generic_project.exception.SerializerException;
import org.openspcoop2.utils.beans.WriteToSerializerType;
import org.openspcoop2.utils.xml.JaxbUtils;

import it.govpay.orm.IdPsp;
import it.govpay.orm.Canale;
import it.govpay.orm.IdSla;
import it.govpay.orm.Rilevamento;
import it.govpay.orm.IdEvento;
import it.govpay.orm.IdIntermediario;
import it.govpay.orm.Stazione;
import it.govpay.orm.Anagrafica;
import it.govpay.orm.IdRilevamento;
import it.govpay.orm.Disponibilita;
import it.govpay.orm.IdDominio;
import it.govpay.orm.IdCanale;
import it.govpay.orm.Intermediario;
import it.govpay.orm.IdTracciato;
import it.govpay.orm.FR;
import it.govpay.orm.IdEnte;
import it.govpay.orm.OperatoreEnte;
import it.govpay.orm.IdMediaRilevamento;
import it.govpay.orm.Psp;
import it.govpay.orm.IdRpt;
import it.govpay.orm.RT;
import it.govpay.orm.IdAnagrafica;
import it.govpay.orm.IdRt;
import it.govpay.orm.RR;
import it.govpay.orm.Esito;
import it.govpay.orm.IdApplicazione;
import it.govpay.orm.IdRr;
import it.govpay.orm.SingolaRevoca;
import it.govpay.orm.IdSingoloVersamento;
import it.govpay.orm.Operatore;
import it.govpay.orm.OperatoreApplicazione;
import it.govpay.orm.IdStazione;
import it.govpay.orm.Dominio;
import it.govpay.orm.IdVersamento;
import it.govpay.orm.Carrello;
import it.govpay.orm.IdIbanAccredito;
import it.govpay.orm.MailTemplate;
import it.govpay.orm.IdPortale;
import it.govpay.orm.Mail;
import it.govpay.orm.ER;
import it.govpay.orm.Evento;
import it.govpay.orm.PortaleApplicazione;
import it.govpay.orm.IdTributo;
import it.govpay.orm.IdTabellaControparti;
import it.govpay.orm.IdContoAccredito;
import it.govpay.orm.Tributo;
import it.govpay.orm.IUV;
import it.govpay.orm.IdConnettore;
import it.govpay.orm.Portale;
import it.govpay.orm.ContoAccredito;
import it.govpay.orm.Connettore;
import it.govpay.orm.ApplicazioneTributo;
import it.govpay.orm.MediaRilevamento;
import it.govpay.orm.SLA;
import it.govpay.orm.IdFr;
import it.govpay.orm.SingolaRendicontazione;
import it.govpay.orm.IdSingolaRendicontazione;
import it.govpay.orm.TracciatoXML;
import it.govpay.orm.IbanAccredito;
import it.govpay.orm.IdMailTemplate;
import it.govpay.orm.IdCarrello;
import it.govpay.orm.IdMessaggio;
import it.govpay.orm.IdSingolaRevoca;
import it.govpay.orm.IdMail;
import it.govpay.orm.Versamento;
import it.govpay.orm.IdEsito;
import it.govpay.orm.SingoloVersamento;
import it.govpay.orm.IdIuv;
import it.govpay.orm.IdEr;
import it.govpay.orm.Applicazione;
import it.govpay.orm.TabellaControparti;
import it.govpay.orm.Ente;
import it.govpay.orm.IdOperatore;
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
	 Object: Rilevamento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>rilevamento</var> of type {@link it.govpay.orm.Rilevamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>rilevamento</var>
	 * @param rilevamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Rilevamento rilevamento) throws SerializerException {
		this.objToXml(fileName, Rilevamento.class, rilevamento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>rilevamento</var> of type {@link it.govpay.orm.Rilevamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>rilevamento</var>
	 * @param rilevamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Rilevamento rilevamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Rilevamento.class, rilevamento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>rilevamento</var> of type {@link it.govpay.orm.Rilevamento}
	 * 
	 * @param file Xml file to serialize the object <var>rilevamento</var>
	 * @param rilevamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Rilevamento rilevamento) throws SerializerException {
		this.objToXml(file, Rilevamento.class, rilevamento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>rilevamento</var> of type {@link it.govpay.orm.Rilevamento}
	 * 
	 * @param file Xml file to serialize the object <var>rilevamento</var>
	 * @param rilevamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Rilevamento rilevamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Rilevamento.class, rilevamento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>rilevamento</var> of type {@link it.govpay.orm.Rilevamento}
	 * 
	 * @param out OutputStream to serialize the object <var>rilevamento</var>
	 * @param rilevamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Rilevamento rilevamento) throws SerializerException {
		this.objToXml(out, Rilevamento.class, rilevamento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>rilevamento</var> of type {@link it.govpay.orm.Rilevamento}
	 * 
	 * @param out OutputStream to serialize the object <var>rilevamento</var>
	 * @param rilevamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Rilevamento rilevamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Rilevamento.class, rilevamento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>rilevamento</var> of type {@link it.govpay.orm.Rilevamento}
	 * 
	 * @param rilevamento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Rilevamento rilevamento) throws SerializerException {
		return this.objToXml(Rilevamento.class, rilevamento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>rilevamento</var> of type {@link it.govpay.orm.Rilevamento}
	 * 
	 * @param rilevamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Rilevamento rilevamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Rilevamento.class, rilevamento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>rilevamento</var> of type {@link it.govpay.orm.Rilevamento}
	 * 
	 * @param rilevamento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Rilevamento rilevamento) throws SerializerException {
		return this.objToXml(Rilevamento.class, rilevamento, false).toString();
	}
	/**
	 * Serialize to String the object <var>rilevamento</var> of type {@link it.govpay.orm.Rilevamento}
	 * 
	 * @param rilevamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Rilevamento rilevamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Rilevamento.class, rilevamento, prettyPrint).toString();
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
	 Object: Anagrafica
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>anagrafica</var> of type {@link it.govpay.orm.Anagrafica}
	 * 
	 * @param fileName Xml file to serialize the object <var>anagrafica</var>
	 * @param anagrafica Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Anagrafica anagrafica) throws SerializerException {
		this.objToXml(fileName, Anagrafica.class, anagrafica, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>anagrafica</var> of type {@link it.govpay.orm.Anagrafica}
	 * 
	 * @param fileName Xml file to serialize the object <var>anagrafica</var>
	 * @param anagrafica Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Anagrafica anagrafica,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Anagrafica.class, anagrafica, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>anagrafica</var> of type {@link it.govpay.orm.Anagrafica}
	 * 
	 * @param file Xml file to serialize the object <var>anagrafica</var>
	 * @param anagrafica Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Anagrafica anagrafica) throws SerializerException {
		this.objToXml(file, Anagrafica.class, anagrafica, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>anagrafica</var> of type {@link it.govpay.orm.Anagrafica}
	 * 
	 * @param file Xml file to serialize the object <var>anagrafica</var>
	 * @param anagrafica Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Anagrafica anagrafica,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Anagrafica.class, anagrafica, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>anagrafica</var> of type {@link it.govpay.orm.Anagrafica}
	 * 
	 * @param out OutputStream to serialize the object <var>anagrafica</var>
	 * @param anagrafica Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Anagrafica anagrafica) throws SerializerException {
		this.objToXml(out, Anagrafica.class, anagrafica, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>anagrafica</var> of type {@link it.govpay.orm.Anagrafica}
	 * 
	 * @param out OutputStream to serialize the object <var>anagrafica</var>
	 * @param anagrafica Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Anagrafica anagrafica,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Anagrafica.class, anagrafica, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>anagrafica</var> of type {@link it.govpay.orm.Anagrafica}
	 * 
	 * @param anagrafica Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Anagrafica anagrafica) throws SerializerException {
		return this.objToXml(Anagrafica.class, anagrafica, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>anagrafica</var> of type {@link it.govpay.orm.Anagrafica}
	 * 
	 * @param anagrafica Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Anagrafica anagrafica,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Anagrafica.class, anagrafica, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>anagrafica</var> of type {@link it.govpay.orm.Anagrafica}
	 * 
	 * @param anagrafica Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Anagrafica anagrafica) throws SerializerException {
		return this.objToXml(Anagrafica.class, anagrafica, false).toString();
	}
	/**
	 * Serialize to String the object <var>anagrafica</var> of type {@link it.govpay.orm.Anagrafica}
	 * 
	 * @param anagrafica Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Anagrafica anagrafica,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Anagrafica.class, anagrafica, prettyPrint).toString();
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
	 Object: Disponibilita
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>disponibilita</var> of type {@link it.govpay.orm.Disponibilita}
	 * 
	 * @param fileName Xml file to serialize the object <var>disponibilita</var>
	 * @param disponibilita Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Disponibilita disponibilita) throws SerializerException {
		this.objToXml(fileName, Disponibilita.class, disponibilita, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>disponibilita</var> of type {@link it.govpay.orm.Disponibilita}
	 * 
	 * @param fileName Xml file to serialize the object <var>disponibilita</var>
	 * @param disponibilita Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Disponibilita disponibilita,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Disponibilita.class, disponibilita, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>disponibilita</var> of type {@link it.govpay.orm.Disponibilita}
	 * 
	 * @param file Xml file to serialize the object <var>disponibilita</var>
	 * @param disponibilita Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Disponibilita disponibilita) throws SerializerException {
		this.objToXml(file, Disponibilita.class, disponibilita, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>disponibilita</var> of type {@link it.govpay.orm.Disponibilita}
	 * 
	 * @param file Xml file to serialize the object <var>disponibilita</var>
	 * @param disponibilita Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Disponibilita disponibilita,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Disponibilita.class, disponibilita, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>disponibilita</var> of type {@link it.govpay.orm.Disponibilita}
	 * 
	 * @param out OutputStream to serialize the object <var>disponibilita</var>
	 * @param disponibilita Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Disponibilita disponibilita) throws SerializerException {
		this.objToXml(out, Disponibilita.class, disponibilita, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>disponibilita</var> of type {@link it.govpay.orm.Disponibilita}
	 * 
	 * @param out OutputStream to serialize the object <var>disponibilita</var>
	 * @param disponibilita Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Disponibilita disponibilita,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Disponibilita.class, disponibilita, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>disponibilita</var> of type {@link it.govpay.orm.Disponibilita}
	 * 
	 * @param disponibilita Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Disponibilita disponibilita) throws SerializerException {
		return this.objToXml(Disponibilita.class, disponibilita, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>disponibilita</var> of type {@link it.govpay.orm.Disponibilita}
	 * 
	 * @param disponibilita Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Disponibilita disponibilita,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Disponibilita.class, disponibilita, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>disponibilita</var> of type {@link it.govpay.orm.Disponibilita}
	 * 
	 * @param disponibilita Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Disponibilita disponibilita) throws SerializerException {
		return this.objToXml(Disponibilita.class, disponibilita, false).toString();
	}
	/**
	 * Serialize to String the object <var>disponibilita</var> of type {@link it.govpay.orm.Disponibilita}
	 * 
	 * @param disponibilita Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Disponibilita disponibilita,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Disponibilita.class, disponibilita, prettyPrint).toString();
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
	 Object: id-ente
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idEnte</var> of type {@link it.govpay.orm.IdEnte}
	 * 
	 * @param fileName Xml file to serialize the object <var>idEnte</var>
	 * @param idEnte Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdEnte idEnte) throws SerializerException {
		this.objToXml(fileName, IdEnte.class, idEnte, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idEnte</var> of type {@link it.govpay.orm.IdEnte}
	 * 
	 * @param fileName Xml file to serialize the object <var>idEnte</var>
	 * @param idEnte Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdEnte idEnte,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdEnte.class, idEnte, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idEnte</var> of type {@link it.govpay.orm.IdEnte}
	 * 
	 * @param file Xml file to serialize the object <var>idEnte</var>
	 * @param idEnte Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdEnte idEnte) throws SerializerException {
		this.objToXml(file, IdEnte.class, idEnte, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idEnte</var> of type {@link it.govpay.orm.IdEnte}
	 * 
	 * @param file Xml file to serialize the object <var>idEnte</var>
	 * @param idEnte Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdEnte idEnte,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdEnte.class, idEnte, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idEnte</var> of type {@link it.govpay.orm.IdEnte}
	 * 
	 * @param out OutputStream to serialize the object <var>idEnte</var>
	 * @param idEnte Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdEnte idEnte) throws SerializerException {
		this.objToXml(out, IdEnte.class, idEnte, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idEnte</var> of type {@link it.govpay.orm.IdEnte}
	 * 
	 * @param out OutputStream to serialize the object <var>idEnte</var>
	 * @param idEnte Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdEnte idEnte,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdEnte.class, idEnte, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idEnte</var> of type {@link it.govpay.orm.IdEnte}
	 * 
	 * @param idEnte Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdEnte idEnte) throws SerializerException {
		return this.objToXml(IdEnte.class, idEnte, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idEnte</var> of type {@link it.govpay.orm.IdEnte}
	 * 
	 * @param idEnte Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdEnte idEnte,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdEnte.class, idEnte, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idEnte</var> of type {@link it.govpay.orm.IdEnte}
	 * 
	 * @param idEnte Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdEnte idEnte) throws SerializerException {
		return this.objToXml(IdEnte.class, idEnte, false).toString();
	}
	/**
	 * Serialize to String the object <var>idEnte</var> of type {@link it.govpay.orm.IdEnte}
	 * 
	 * @param idEnte Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdEnte idEnte,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdEnte.class, idEnte, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: OperatoreEnte
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>operatoreEnte</var> of type {@link it.govpay.orm.OperatoreEnte}
	 * 
	 * @param fileName Xml file to serialize the object <var>operatoreEnte</var>
	 * @param operatoreEnte Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,OperatoreEnte operatoreEnte) throws SerializerException {
		this.objToXml(fileName, OperatoreEnte.class, operatoreEnte, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>operatoreEnte</var> of type {@link it.govpay.orm.OperatoreEnte}
	 * 
	 * @param fileName Xml file to serialize the object <var>operatoreEnte</var>
	 * @param operatoreEnte Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,OperatoreEnte operatoreEnte,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, OperatoreEnte.class, operatoreEnte, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>operatoreEnte</var> of type {@link it.govpay.orm.OperatoreEnte}
	 * 
	 * @param file Xml file to serialize the object <var>operatoreEnte</var>
	 * @param operatoreEnte Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,OperatoreEnte operatoreEnte) throws SerializerException {
		this.objToXml(file, OperatoreEnte.class, operatoreEnte, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>operatoreEnte</var> of type {@link it.govpay.orm.OperatoreEnte}
	 * 
	 * @param file Xml file to serialize the object <var>operatoreEnte</var>
	 * @param operatoreEnte Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,OperatoreEnte operatoreEnte,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, OperatoreEnte.class, operatoreEnte, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>operatoreEnte</var> of type {@link it.govpay.orm.OperatoreEnte}
	 * 
	 * @param out OutputStream to serialize the object <var>operatoreEnte</var>
	 * @param operatoreEnte Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,OperatoreEnte operatoreEnte) throws SerializerException {
		this.objToXml(out, OperatoreEnte.class, operatoreEnte, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>operatoreEnte</var> of type {@link it.govpay.orm.OperatoreEnte}
	 * 
	 * @param out OutputStream to serialize the object <var>operatoreEnte</var>
	 * @param operatoreEnte Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,OperatoreEnte operatoreEnte,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, OperatoreEnte.class, operatoreEnte, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>operatoreEnte</var> of type {@link it.govpay.orm.OperatoreEnte}
	 * 
	 * @param operatoreEnte Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(OperatoreEnte operatoreEnte) throws SerializerException {
		return this.objToXml(OperatoreEnte.class, operatoreEnte, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>operatoreEnte</var> of type {@link it.govpay.orm.OperatoreEnte}
	 * 
	 * @param operatoreEnte Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(OperatoreEnte operatoreEnte,boolean prettyPrint) throws SerializerException {
		return this.objToXml(OperatoreEnte.class, operatoreEnte, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>operatoreEnte</var> of type {@link it.govpay.orm.OperatoreEnte}
	 * 
	 * @param operatoreEnte Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(OperatoreEnte operatoreEnte) throws SerializerException {
		return this.objToXml(OperatoreEnte.class, operatoreEnte, false).toString();
	}
	/**
	 * Serialize to String the object <var>operatoreEnte</var> of type {@link it.govpay.orm.OperatoreEnte}
	 * 
	 * @param operatoreEnte Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(OperatoreEnte operatoreEnte,boolean prettyPrint) throws SerializerException {
		return this.objToXml(OperatoreEnte.class, operatoreEnte, prettyPrint).toString();
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
	 Object: RT
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>rt</var> of type {@link it.govpay.orm.RT}
	 * 
	 * @param fileName Xml file to serialize the object <var>rt</var>
	 * @param rt Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,RT rt) throws SerializerException {
		this.objToXml(fileName, RT.class, rt, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>rt</var> of type {@link it.govpay.orm.RT}
	 * 
	 * @param fileName Xml file to serialize the object <var>rt</var>
	 * @param rt Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,RT rt,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, RT.class, rt, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>rt</var> of type {@link it.govpay.orm.RT}
	 * 
	 * @param file Xml file to serialize the object <var>rt</var>
	 * @param rt Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,RT rt) throws SerializerException {
		this.objToXml(file, RT.class, rt, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>rt</var> of type {@link it.govpay.orm.RT}
	 * 
	 * @param file Xml file to serialize the object <var>rt</var>
	 * @param rt Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,RT rt,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, RT.class, rt, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>rt</var> of type {@link it.govpay.orm.RT}
	 * 
	 * @param out OutputStream to serialize the object <var>rt</var>
	 * @param rt Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,RT rt) throws SerializerException {
		this.objToXml(out, RT.class, rt, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>rt</var> of type {@link it.govpay.orm.RT}
	 * 
	 * @param out OutputStream to serialize the object <var>rt</var>
	 * @param rt Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,RT rt,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, RT.class, rt, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>rt</var> of type {@link it.govpay.orm.RT}
	 * 
	 * @param rt Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(RT rt) throws SerializerException {
		return this.objToXml(RT.class, rt, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>rt</var> of type {@link it.govpay.orm.RT}
	 * 
	 * @param rt Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(RT rt,boolean prettyPrint) throws SerializerException {
		return this.objToXml(RT.class, rt, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>rt</var> of type {@link it.govpay.orm.RT}
	 * 
	 * @param rt Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(RT rt) throws SerializerException {
		return this.objToXml(RT.class, rt, false).toString();
	}
	/**
	 * Serialize to String the object <var>rt</var> of type {@link it.govpay.orm.RT}
	 * 
	 * @param rt Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(RT rt,boolean prettyPrint) throws SerializerException {
		return this.objToXml(RT.class, rt, prettyPrint).toString();
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
	 Object: id-rt
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idRt</var> of type {@link it.govpay.orm.IdRt}
	 * 
	 * @param fileName Xml file to serialize the object <var>idRt</var>
	 * @param idRt Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdRt idRt) throws SerializerException {
		this.objToXml(fileName, IdRt.class, idRt, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idRt</var> of type {@link it.govpay.orm.IdRt}
	 * 
	 * @param fileName Xml file to serialize the object <var>idRt</var>
	 * @param idRt Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdRt idRt,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdRt.class, idRt, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idRt</var> of type {@link it.govpay.orm.IdRt}
	 * 
	 * @param file Xml file to serialize the object <var>idRt</var>
	 * @param idRt Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdRt idRt) throws SerializerException {
		this.objToXml(file, IdRt.class, idRt, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idRt</var> of type {@link it.govpay.orm.IdRt}
	 * 
	 * @param file Xml file to serialize the object <var>idRt</var>
	 * @param idRt Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdRt idRt,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdRt.class, idRt, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idRt</var> of type {@link it.govpay.orm.IdRt}
	 * 
	 * @param out OutputStream to serialize the object <var>idRt</var>
	 * @param idRt Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdRt idRt) throws SerializerException {
		this.objToXml(out, IdRt.class, idRt, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idRt</var> of type {@link it.govpay.orm.IdRt}
	 * 
	 * @param out OutputStream to serialize the object <var>idRt</var>
	 * @param idRt Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdRt idRt,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdRt.class, idRt, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idRt</var> of type {@link it.govpay.orm.IdRt}
	 * 
	 * @param idRt Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdRt idRt) throws SerializerException {
		return this.objToXml(IdRt.class, idRt, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idRt</var> of type {@link it.govpay.orm.IdRt}
	 * 
	 * @param idRt Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdRt idRt,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdRt.class, idRt, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idRt</var> of type {@link it.govpay.orm.IdRt}
	 * 
	 * @param idRt Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdRt idRt) throws SerializerException {
		return this.objToXml(IdRt.class, idRt, false).toString();
	}
	/**
	 * Serialize to String the object <var>idRt</var> of type {@link it.govpay.orm.IdRt}
	 * 
	 * @param idRt Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdRt idRt,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdRt.class, idRt, prettyPrint).toString();
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
	 Object: Esito
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>esito</var> of type {@link it.govpay.orm.Esito}
	 * 
	 * @param fileName Xml file to serialize the object <var>esito</var>
	 * @param esito Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Esito esito) throws SerializerException {
		this.objToXml(fileName, Esito.class, esito, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>esito</var> of type {@link it.govpay.orm.Esito}
	 * 
	 * @param fileName Xml file to serialize the object <var>esito</var>
	 * @param esito Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Esito esito,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Esito.class, esito, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>esito</var> of type {@link it.govpay.orm.Esito}
	 * 
	 * @param file Xml file to serialize the object <var>esito</var>
	 * @param esito Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Esito esito) throws SerializerException {
		this.objToXml(file, Esito.class, esito, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>esito</var> of type {@link it.govpay.orm.Esito}
	 * 
	 * @param file Xml file to serialize the object <var>esito</var>
	 * @param esito Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Esito esito,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Esito.class, esito, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>esito</var> of type {@link it.govpay.orm.Esito}
	 * 
	 * @param out OutputStream to serialize the object <var>esito</var>
	 * @param esito Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Esito esito) throws SerializerException {
		this.objToXml(out, Esito.class, esito, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>esito</var> of type {@link it.govpay.orm.Esito}
	 * 
	 * @param out OutputStream to serialize the object <var>esito</var>
	 * @param esito Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Esito esito,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Esito.class, esito, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>esito</var> of type {@link it.govpay.orm.Esito}
	 * 
	 * @param esito Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Esito esito) throws SerializerException {
		return this.objToXml(Esito.class, esito, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>esito</var> of type {@link it.govpay.orm.Esito}
	 * 
	 * @param esito Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Esito esito,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Esito.class, esito, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>esito</var> of type {@link it.govpay.orm.Esito}
	 * 
	 * @param esito Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Esito esito) throws SerializerException {
		return this.objToXml(Esito.class, esito, false).toString();
	}
	/**
	 * Serialize to String the object <var>esito</var> of type {@link it.govpay.orm.Esito}
	 * 
	 * @param esito Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Esito esito,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Esito.class, esito, prettyPrint).toString();
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
	 Object: SingolaRevoca
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>singolaRevoca</var> of type {@link it.govpay.orm.SingolaRevoca}
	 * 
	 * @param fileName Xml file to serialize the object <var>singolaRevoca</var>
	 * @param singolaRevoca Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,SingolaRevoca singolaRevoca) throws SerializerException {
		this.objToXml(fileName, SingolaRevoca.class, singolaRevoca, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>singolaRevoca</var> of type {@link it.govpay.orm.SingolaRevoca}
	 * 
	 * @param fileName Xml file to serialize the object <var>singolaRevoca</var>
	 * @param singolaRevoca Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,SingolaRevoca singolaRevoca,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, SingolaRevoca.class, singolaRevoca, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>singolaRevoca</var> of type {@link it.govpay.orm.SingolaRevoca}
	 * 
	 * @param file Xml file to serialize the object <var>singolaRevoca</var>
	 * @param singolaRevoca Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,SingolaRevoca singolaRevoca) throws SerializerException {
		this.objToXml(file, SingolaRevoca.class, singolaRevoca, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>singolaRevoca</var> of type {@link it.govpay.orm.SingolaRevoca}
	 * 
	 * @param file Xml file to serialize the object <var>singolaRevoca</var>
	 * @param singolaRevoca Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,SingolaRevoca singolaRevoca,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, SingolaRevoca.class, singolaRevoca, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>singolaRevoca</var> of type {@link it.govpay.orm.SingolaRevoca}
	 * 
	 * @param out OutputStream to serialize the object <var>singolaRevoca</var>
	 * @param singolaRevoca Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,SingolaRevoca singolaRevoca) throws SerializerException {
		this.objToXml(out, SingolaRevoca.class, singolaRevoca, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>singolaRevoca</var> of type {@link it.govpay.orm.SingolaRevoca}
	 * 
	 * @param out OutputStream to serialize the object <var>singolaRevoca</var>
	 * @param singolaRevoca Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,SingolaRevoca singolaRevoca,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, SingolaRevoca.class, singolaRevoca, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>singolaRevoca</var> of type {@link it.govpay.orm.SingolaRevoca}
	 * 
	 * @param singolaRevoca Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(SingolaRevoca singolaRevoca) throws SerializerException {
		return this.objToXml(SingolaRevoca.class, singolaRevoca, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>singolaRevoca</var> of type {@link it.govpay.orm.SingolaRevoca}
	 * 
	 * @param singolaRevoca Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(SingolaRevoca singolaRevoca,boolean prettyPrint) throws SerializerException {
		return this.objToXml(SingolaRevoca.class, singolaRevoca, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>singolaRevoca</var> of type {@link it.govpay.orm.SingolaRevoca}
	 * 
	 * @param singolaRevoca Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(SingolaRevoca singolaRevoca) throws SerializerException {
		return this.objToXml(SingolaRevoca.class, singolaRevoca, false).toString();
	}
	/**
	 * Serialize to String the object <var>singolaRevoca</var> of type {@link it.govpay.orm.SingolaRevoca}
	 * 
	 * @param singolaRevoca Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(SingolaRevoca singolaRevoca,boolean prettyPrint) throws SerializerException {
		return this.objToXml(SingolaRevoca.class, singolaRevoca, prettyPrint).toString();
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
	 Object: OperatoreApplicazione
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>operatoreApplicazione</var> of type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>operatoreApplicazione</var>
	 * @param operatoreApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,OperatoreApplicazione operatoreApplicazione) throws SerializerException {
		this.objToXml(fileName, OperatoreApplicazione.class, operatoreApplicazione, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>operatoreApplicazione</var> of type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>operatoreApplicazione</var>
	 * @param operatoreApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,OperatoreApplicazione operatoreApplicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, OperatoreApplicazione.class, operatoreApplicazione, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>operatoreApplicazione</var> of type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param file Xml file to serialize the object <var>operatoreApplicazione</var>
	 * @param operatoreApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,OperatoreApplicazione operatoreApplicazione) throws SerializerException {
		this.objToXml(file, OperatoreApplicazione.class, operatoreApplicazione, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>operatoreApplicazione</var> of type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param file Xml file to serialize the object <var>operatoreApplicazione</var>
	 * @param operatoreApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,OperatoreApplicazione operatoreApplicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, OperatoreApplicazione.class, operatoreApplicazione, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>operatoreApplicazione</var> of type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param out OutputStream to serialize the object <var>operatoreApplicazione</var>
	 * @param operatoreApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,OperatoreApplicazione operatoreApplicazione) throws SerializerException {
		this.objToXml(out, OperatoreApplicazione.class, operatoreApplicazione, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>operatoreApplicazione</var> of type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param out OutputStream to serialize the object <var>operatoreApplicazione</var>
	 * @param operatoreApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,OperatoreApplicazione operatoreApplicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, OperatoreApplicazione.class, operatoreApplicazione, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>operatoreApplicazione</var> of type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param operatoreApplicazione Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(OperatoreApplicazione operatoreApplicazione) throws SerializerException {
		return this.objToXml(OperatoreApplicazione.class, operatoreApplicazione, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>operatoreApplicazione</var> of type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param operatoreApplicazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(OperatoreApplicazione operatoreApplicazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(OperatoreApplicazione.class, operatoreApplicazione, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>operatoreApplicazione</var> of type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param operatoreApplicazione Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(OperatoreApplicazione operatoreApplicazione) throws SerializerException {
		return this.objToXml(OperatoreApplicazione.class, operatoreApplicazione, false).toString();
	}
	/**
	 * Serialize to String the object <var>operatoreApplicazione</var> of type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param operatoreApplicazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(OperatoreApplicazione operatoreApplicazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(OperatoreApplicazione.class, operatoreApplicazione, prettyPrint).toString();
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
	 Object: Carrello
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>carrello</var> of type {@link it.govpay.orm.Carrello}
	 * 
	 * @param fileName Xml file to serialize the object <var>carrello</var>
	 * @param carrello Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Carrello carrello) throws SerializerException {
		this.objToXml(fileName, Carrello.class, carrello, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>carrello</var> of type {@link it.govpay.orm.Carrello}
	 * 
	 * @param fileName Xml file to serialize the object <var>carrello</var>
	 * @param carrello Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Carrello carrello,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Carrello.class, carrello, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>carrello</var> of type {@link it.govpay.orm.Carrello}
	 * 
	 * @param file Xml file to serialize the object <var>carrello</var>
	 * @param carrello Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Carrello carrello) throws SerializerException {
		this.objToXml(file, Carrello.class, carrello, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>carrello</var> of type {@link it.govpay.orm.Carrello}
	 * 
	 * @param file Xml file to serialize the object <var>carrello</var>
	 * @param carrello Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Carrello carrello,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Carrello.class, carrello, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>carrello</var> of type {@link it.govpay.orm.Carrello}
	 * 
	 * @param out OutputStream to serialize the object <var>carrello</var>
	 * @param carrello Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Carrello carrello) throws SerializerException {
		this.objToXml(out, Carrello.class, carrello, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>carrello</var> of type {@link it.govpay.orm.Carrello}
	 * 
	 * @param out OutputStream to serialize the object <var>carrello</var>
	 * @param carrello Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Carrello carrello,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Carrello.class, carrello, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>carrello</var> of type {@link it.govpay.orm.Carrello}
	 * 
	 * @param carrello Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Carrello carrello) throws SerializerException {
		return this.objToXml(Carrello.class, carrello, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>carrello</var> of type {@link it.govpay.orm.Carrello}
	 * 
	 * @param carrello Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Carrello carrello,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Carrello.class, carrello, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>carrello</var> of type {@link it.govpay.orm.Carrello}
	 * 
	 * @param carrello Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Carrello carrello) throws SerializerException {
		return this.objToXml(Carrello.class, carrello, false).toString();
	}
	/**
	 * Serialize to String the object <var>carrello</var> of type {@link it.govpay.orm.Carrello}
	 * 
	 * @param carrello Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Carrello carrello,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Carrello.class, carrello, prettyPrint).toString();
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
	 Object: MailTemplate
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>mailTemplate</var> of type {@link it.govpay.orm.MailTemplate}
	 * 
	 * @param fileName Xml file to serialize the object <var>mailTemplate</var>
	 * @param mailTemplate Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,MailTemplate mailTemplate) throws SerializerException {
		this.objToXml(fileName, MailTemplate.class, mailTemplate, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>mailTemplate</var> of type {@link it.govpay.orm.MailTemplate}
	 * 
	 * @param fileName Xml file to serialize the object <var>mailTemplate</var>
	 * @param mailTemplate Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,MailTemplate mailTemplate,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, MailTemplate.class, mailTemplate, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>mailTemplate</var> of type {@link it.govpay.orm.MailTemplate}
	 * 
	 * @param file Xml file to serialize the object <var>mailTemplate</var>
	 * @param mailTemplate Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,MailTemplate mailTemplate) throws SerializerException {
		this.objToXml(file, MailTemplate.class, mailTemplate, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>mailTemplate</var> of type {@link it.govpay.orm.MailTemplate}
	 * 
	 * @param file Xml file to serialize the object <var>mailTemplate</var>
	 * @param mailTemplate Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,MailTemplate mailTemplate,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, MailTemplate.class, mailTemplate, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>mailTemplate</var> of type {@link it.govpay.orm.MailTemplate}
	 * 
	 * @param out OutputStream to serialize the object <var>mailTemplate</var>
	 * @param mailTemplate Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,MailTemplate mailTemplate) throws SerializerException {
		this.objToXml(out, MailTemplate.class, mailTemplate, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>mailTemplate</var> of type {@link it.govpay.orm.MailTemplate}
	 * 
	 * @param out OutputStream to serialize the object <var>mailTemplate</var>
	 * @param mailTemplate Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,MailTemplate mailTemplate,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, MailTemplate.class, mailTemplate, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>mailTemplate</var> of type {@link it.govpay.orm.MailTemplate}
	 * 
	 * @param mailTemplate Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(MailTemplate mailTemplate) throws SerializerException {
		return this.objToXml(MailTemplate.class, mailTemplate, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>mailTemplate</var> of type {@link it.govpay.orm.MailTemplate}
	 * 
	 * @param mailTemplate Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(MailTemplate mailTemplate,boolean prettyPrint) throws SerializerException {
		return this.objToXml(MailTemplate.class, mailTemplate, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>mailTemplate</var> of type {@link it.govpay.orm.MailTemplate}
	 * 
	 * @param mailTemplate Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(MailTemplate mailTemplate) throws SerializerException {
		return this.objToXml(MailTemplate.class, mailTemplate, false).toString();
	}
	/**
	 * Serialize to String the object <var>mailTemplate</var> of type {@link it.govpay.orm.MailTemplate}
	 * 
	 * @param mailTemplate Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(MailTemplate mailTemplate,boolean prettyPrint) throws SerializerException {
		return this.objToXml(MailTemplate.class, mailTemplate, prettyPrint).toString();
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
	 Object: Mail
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>mail</var> of type {@link it.govpay.orm.Mail}
	 * 
	 * @param fileName Xml file to serialize the object <var>mail</var>
	 * @param mail Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Mail mail) throws SerializerException {
		this.objToXml(fileName, Mail.class, mail, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>mail</var> of type {@link it.govpay.orm.Mail}
	 * 
	 * @param fileName Xml file to serialize the object <var>mail</var>
	 * @param mail Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Mail mail,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Mail.class, mail, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>mail</var> of type {@link it.govpay.orm.Mail}
	 * 
	 * @param file Xml file to serialize the object <var>mail</var>
	 * @param mail Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Mail mail) throws SerializerException {
		this.objToXml(file, Mail.class, mail, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>mail</var> of type {@link it.govpay.orm.Mail}
	 * 
	 * @param file Xml file to serialize the object <var>mail</var>
	 * @param mail Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Mail mail,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Mail.class, mail, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>mail</var> of type {@link it.govpay.orm.Mail}
	 * 
	 * @param out OutputStream to serialize the object <var>mail</var>
	 * @param mail Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Mail mail) throws SerializerException {
		this.objToXml(out, Mail.class, mail, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>mail</var> of type {@link it.govpay.orm.Mail}
	 * 
	 * @param out OutputStream to serialize the object <var>mail</var>
	 * @param mail Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Mail mail,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Mail.class, mail, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>mail</var> of type {@link it.govpay.orm.Mail}
	 * 
	 * @param mail Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Mail mail) throws SerializerException {
		return this.objToXml(Mail.class, mail, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>mail</var> of type {@link it.govpay.orm.Mail}
	 * 
	 * @param mail Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Mail mail,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Mail.class, mail, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>mail</var> of type {@link it.govpay.orm.Mail}
	 * 
	 * @param mail Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Mail mail) throws SerializerException {
		return this.objToXml(Mail.class, mail, false).toString();
	}
	/**
	 * Serialize to String the object <var>mail</var> of type {@link it.govpay.orm.Mail}
	 * 
	 * @param mail Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Mail mail,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Mail.class, mail, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: ER
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>er</var> of type {@link it.govpay.orm.ER}
	 * 
	 * @param fileName Xml file to serialize the object <var>er</var>
	 * @param er Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,ER er) throws SerializerException {
		this.objToXml(fileName, ER.class, er, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>er</var> of type {@link it.govpay.orm.ER}
	 * 
	 * @param fileName Xml file to serialize the object <var>er</var>
	 * @param er Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,ER er,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, ER.class, er, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>er</var> of type {@link it.govpay.orm.ER}
	 * 
	 * @param file Xml file to serialize the object <var>er</var>
	 * @param er Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,ER er) throws SerializerException {
		this.objToXml(file, ER.class, er, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>er</var> of type {@link it.govpay.orm.ER}
	 * 
	 * @param file Xml file to serialize the object <var>er</var>
	 * @param er Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,ER er,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, ER.class, er, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>er</var> of type {@link it.govpay.orm.ER}
	 * 
	 * @param out OutputStream to serialize the object <var>er</var>
	 * @param er Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,ER er) throws SerializerException {
		this.objToXml(out, ER.class, er, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>er</var> of type {@link it.govpay.orm.ER}
	 * 
	 * @param out OutputStream to serialize the object <var>er</var>
	 * @param er Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,ER er,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, ER.class, er, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>er</var> of type {@link it.govpay.orm.ER}
	 * 
	 * @param er Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(ER er) throws SerializerException {
		return this.objToXml(ER.class, er, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>er</var> of type {@link it.govpay.orm.ER}
	 * 
	 * @param er Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(ER er,boolean prettyPrint) throws SerializerException {
		return this.objToXml(ER.class, er, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>er</var> of type {@link it.govpay.orm.ER}
	 * 
	 * @param er Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(ER er) throws SerializerException {
		return this.objToXml(ER.class, er, false).toString();
	}
	/**
	 * Serialize to String the object <var>er</var> of type {@link it.govpay.orm.ER}
	 * 
	 * @param er Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(ER er,boolean prettyPrint) throws SerializerException {
		return this.objToXml(ER.class, er, prettyPrint).toString();
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
	 Object: PortaleApplicazione
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>portaleApplicazione</var> of type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>portaleApplicazione</var>
	 * @param portaleApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,PortaleApplicazione portaleApplicazione) throws SerializerException {
		this.objToXml(fileName, PortaleApplicazione.class, portaleApplicazione, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>portaleApplicazione</var> of type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>portaleApplicazione</var>
	 * @param portaleApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,PortaleApplicazione portaleApplicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, PortaleApplicazione.class, portaleApplicazione, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>portaleApplicazione</var> of type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param file Xml file to serialize the object <var>portaleApplicazione</var>
	 * @param portaleApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,PortaleApplicazione portaleApplicazione) throws SerializerException {
		this.objToXml(file, PortaleApplicazione.class, portaleApplicazione, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>portaleApplicazione</var> of type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param file Xml file to serialize the object <var>portaleApplicazione</var>
	 * @param portaleApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,PortaleApplicazione portaleApplicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, PortaleApplicazione.class, portaleApplicazione, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>portaleApplicazione</var> of type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param out OutputStream to serialize the object <var>portaleApplicazione</var>
	 * @param portaleApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,PortaleApplicazione portaleApplicazione) throws SerializerException {
		this.objToXml(out, PortaleApplicazione.class, portaleApplicazione, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>portaleApplicazione</var> of type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param out OutputStream to serialize the object <var>portaleApplicazione</var>
	 * @param portaleApplicazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,PortaleApplicazione portaleApplicazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, PortaleApplicazione.class, portaleApplicazione, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>portaleApplicazione</var> of type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param portaleApplicazione Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(PortaleApplicazione portaleApplicazione) throws SerializerException {
		return this.objToXml(PortaleApplicazione.class, portaleApplicazione, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>portaleApplicazione</var> of type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param portaleApplicazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(PortaleApplicazione portaleApplicazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(PortaleApplicazione.class, portaleApplicazione, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>portaleApplicazione</var> of type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param portaleApplicazione Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(PortaleApplicazione portaleApplicazione) throws SerializerException {
		return this.objToXml(PortaleApplicazione.class, portaleApplicazione, false).toString();
	}
	/**
	 * Serialize to String the object <var>portaleApplicazione</var> of type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param portaleApplicazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(PortaleApplicazione portaleApplicazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(PortaleApplicazione.class, portaleApplicazione, prettyPrint).toString();
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
	 Object: ContoAccredito
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>contoAccredito</var> of type {@link it.govpay.orm.ContoAccredito}
	 * 
	 * @param fileName Xml file to serialize the object <var>contoAccredito</var>
	 * @param contoAccredito Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,ContoAccredito contoAccredito) throws SerializerException {
		this.objToXml(fileName, ContoAccredito.class, contoAccredito, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>contoAccredito</var> of type {@link it.govpay.orm.ContoAccredito}
	 * 
	 * @param fileName Xml file to serialize the object <var>contoAccredito</var>
	 * @param contoAccredito Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,ContoAccredito contoAccredito,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, ContoAccredito.class, contoAccredito, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>contoAccredito</var> of type {@link it.govpay.orm.ContoAccredito}
	 * 
	 * @param file Xml file to serialize the object <var>contoAccredito</var>
	 * @param contoAccredito Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,ContoAccredito contoAccredito) throws SerializerException {
		this.objToXml(file, ContoAccredito.class, contoAccredito, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>contoAccredito</var> of type {@link it.govpay.orm.ContoAccredito}
	 * 
	 * @param file Xml file to serialize the object <var>contoAccredito</var>
	 * @param contoAccredito Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,ContoAccredito contoAccredito,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, ContoAccredito.class, contoAccredito, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>contoAccredito</var> of type {@link it.govpay.orm.ContoAccredito}
	 * 
	 * @param out OutputStream to serialize the object <var>contoAccredito</var>
	 * @param contoAccredito Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,ContoAccredito contoAccredito) throws SerializerException {
		this.objToXml(out, ContoAccredito.class, contoAccredito, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>contoAccredito</var> of type {@link it.govpay.orm.ContoAccredito}
	 * 
	 * @param out OutputStream to serialize the object <var>contoAccredito</var>
	 * @param contoAccredito Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,ContoAccredito contoAccredito,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, ContoAccredito.class, contoAccredito, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>contoAccredito</var> of type {@link it.govpay.orm.ContoAccredito}
	 * 
	 * @param contoAccredito Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(ContoAccredito contoAccredito) throws SerializerException {
		return this.objToXml(ContoAccredito.class, contoAccredito, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>contoAccredito</var> of type {@link it.govpay.orm.ContoAccredito}
	 * 
	 * @param contoAccredito Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(ContoAccredito contoAccredito,boolean prettyPrint) throws SerializerException {
		return this.objToXml(ContoAccredito.class, contoAccredito, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>contoAccredito</var> of type {@link it.govpay.orm.ContoAccredito}
	 * 
	 * @param contoAccredito Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(ContoAccredito contoAccredito) throws SerializerException {
		return this.objToXml(ContoAccredito.class, contoAccredito, false).toString();
	}
	/**
	 * Serialize to String the object <var>contoAccredito</var> of type {@link it.govpay.orm.ContoAccredito}
	 * 
	 * @param contoAccredito Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(ContoAccredito contoAccredito,boolean prettyPrint) throws SerializerException {
		return this.objToXml(ContoAccredito.class, contoAccredito, prettyPrint).toString();
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
	 Object: ApplicazioneTributo
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>applicazioneTributo</var> of type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param fileName Xml file to serialize the object <var>applicazioneTributo</var>
	 * @param applicazioneTributo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,ApplicazioneTributo applicazioneTributo) throws SerializerException {
		this.objToXml(fileName, ApplicazioneTributo.class, applicazioneTributo, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>applicazioneTributo</var> of type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param fileName Xml file to serialize the object <var>applicazioneTributo</var>
	 * @param applicazioneTributo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,ApplicazioneTributo applicazioneTributo,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, ApplicazioneTributo.class, applicazioneTributo, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>applicazioneTributo</var> of type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param file Xml file to serialize the object <var>applicazioneTributo</var>
	 * @param applicazioneTributo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,ApplicazioneTributo applicazioneTributo) throws SerializerException {
		this.objToXml(file, ApplicazioneTributo.class, applicazioneTributo, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>applicazioneTributo</var> of type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param file Xml file to serialize the object <var>applicazioneTributo</var>
	 * @param applicazioneTributo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,ApplicazioneTributo applicazioneTributo,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, ApplicazioneTributo.class, applicazioneTributo, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>applicazioneTributo</var> of type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param out OutputStream to serialize the object <var>applicazioneTributo</var>
	 * @param applicazioneTributo Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,ApplicazioneTributo applicazioneTributo) throws SerializerException {
		this.objToXml(out, ApplicazioneTributo.class, applicazioneTributo, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>applicazioneTributo</var> of type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param out OutputStream to serialize the object <var>applicazioneTributo</var>
	 * @param applicazioneTributo Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,ApplicazioneTributo applicazioneTributo,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, ApplicazioneTributo.class, applicazioneTributo, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>applicazioneTributo</var> of type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param applicazioneTributo Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(ApplicazioneTributo applicazioneTributo) throws SerializerException {
		return this.objToXml(ApplicazioneTributo.class, applicazioneTributo, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>applicazioneTributo</var> of type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param applicazioneTributo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(ApplicazioneTributo applicazioneTributo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(ApplicazioneTributo.class, applicazioneTributo, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>applicazioneTributo</var> of type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param applicazioneTributo Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(ApplicazioneTributo applicazioneTributo) throws SerializerException {
		return this.objToXml(ApplicazioneTributo.class, applicazioneTributo, false).toString();
	}
	/**
	 * Serialize to String the object <var>applicazioneTributo</var> of type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param applicazioneTributo Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(ApplicazioneTributo applicazioneTributo,boolean prettyPrint) throws SerializerException {
		return this.objToXml(ApplicazioneTributo.class, applicazioneTributo, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: MediaRilevamento
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>mediaRilevamento</var> of type {@link it.govpay.orm.MediaRilevamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>mediaRilevamento</var>
	 * @param mediaRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,MediaRilevamento mediaRilevamento) throws SerializerException {
		this.objToXml(fileName, MediaRilevamento.class, mediaRilevamento, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>mediaRilevamento</var> of type {@link it.govpay.orm.MediaRilevamento}
	 * 
	 * @param fileName Xml file to serialize the object <var>mediaRilevamento</var>
	 * @param mediaRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,MediaRilevamento mediaRilevamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, MediaRilevamento.class, mediaRilevamento, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>mediaRilevamento</var> of type {@link it.govpay.orm.MediaRilevamento}
	 * 
	 * @param file Xml file to serialize the object <var>mediaRilevamento</var>
	 * @param mediaRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,MediaRilevamento mediaRilevamento) throws SerializerException {
		this.objToXml(file, MediaRilevamento.class, mediaRilevamento, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>mediaRilevamento</var> of type {@link it.govpay.orm.MediaRilevamento}
	 * 
	 * @param file Xml file to serialize the object <var>mediaRilevamento</var>
	 * @param mediaRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,MediaRilevamento mediaRilevamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, MediaRilevamento.class, mediaRilevamento, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>mediaRilevamento</var> of type {@link it.govpay.orm.MediaRilevamento}
	 * 
	 * @param out OutputStream to serialize the object <var>mediaRilevamento</var>
	 * @param mediaRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,MediaRilevamento mediaRilevamento) throws SerializerException {
		this.objToXml(out, MediaRilevamento.class, mediaRilevamento, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>mediaRilevamento</var> of type {@link it.govpay.orm.MediaRilevamento}
	 * 
	 * @param out OutputStream to serialize the object <var>mediaRilevamento</var>
	 * @param mediaRilevamento Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,MediaRilevamento mediaRilevamento,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, MediaRilevamento.class, mediaRilevamento, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>mediaRilevamento</var> of type {@link it.govpay.orm.MediaRilevamento}
	 * 
	 * @param mediaRilevamento Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(MediaRilevamento mediaRilevamento) throws SerializerException {
		return this.objToXml(MediaRilevamento.class, mediaRilevamento, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>mediaRilevamento</var> of type {@link it.govpay.orm.MediaRilevamento}
	 * 
	 * @param mediaRilevamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(MediaRilevamento mediaRilevamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(MediaRilevamento.class, mediaRilevamento, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>mediaRilevamento</var> of type {@link it.govpay.orm.MediaRilevamento}
	 * 
	 * @param mediaRilevamento Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(MediaRilevamento mediaRilevamento) throws SerializerException {
		return this.objToXml(MediaRilevamento.class, mediaRilevamento, false).toString();
	}
	/**
	 * Serialize to String the object <var>mediaRilevamento</var> of type {@link it.govpay.orm.MediaRilevamento}
	 * 
	 * @param mediaRilevamento Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(MediaRilevamento mediaRilevamento,boolean prettyPrint) throws SerializerException {
		return this.objToXml(MediaRilevamento.class, mediaRilevamento, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: SLA
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>sla</var> of type {@link it.govpay.orm.SLA}
	 * 
	 * @param fileName Xml file to serialize the object <var>sla</var>
	 * @param sla Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,SLA sla) throws SerializerException {
		this.objToXml(fileName, SLA.class, sla, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>sla</var> of type {@link it.govpay.orm.SLA}
	 * 
	 * @param fileName Xml file to serialize the object <var>sla</var>
	 * @param sla Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,SLA sla,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, SLA.class, sla, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>sla</var> of type {@link it.govpay.orm.SLA}
	 * 
	 * @param file Xml file to serialize the object <var>sla</var>
	 * @param sla Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,SLA sla) throws SerializerException {
		this.objToXml(file, SLA.class, sla, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>sla</var> of type {@link it.govpay.orm.SLA}
	 * 
	 * @param file Xml file to serialize the object <var>sla</var>
	 * @param sla Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,SLA sla,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, SLA.class, sla, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>sla</var> of type {@link it.govpay.orm.SLA}
	 * 
	 * @param out OutputStream to serialize the object <var>sla</var>
	 * @param sla Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,SLA sla) throws SerializerException {
		this.objToXml(out, SLA.class, sla, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>sla</var> of type {@link it.govpay.orm.SLA}
	 * 
	 * @param out OutputStream to serialize the object <var>sla</var>
	 * @param sla Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,SLA sla,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, SLA.class, sla, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>sla</var> of type {@link it.govpay.orm.SLA}
	 * 
	 * @param sla Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(SLA sla) throws SerializerException {
		return this.objToXml(SLA.class, sla, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>sla</var> of type {@link it.govpay.orm.SLA}
	 * 
	 * @param sla Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(SLA sla,boolean prettyPrint) throws SerializerException {
		return this.objToXml(SLA.class, sla, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>sla</var> of type {@link it.govpay.orm.SLA}
	 * 
	 * @param sla Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(SLA sla) throws SerializerException {
		return this.objToXml(SLA.class, sla, false).toString();
	}
	/**
	 * Serialize to String the object <var>sla</var> of type {@link it.govpay.orm.SLA}
	 * 
	 * @param sla Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(SLA sla,boolean prettyPrint) throws SerializerException {
		return this.objToXml(SLA.class, sla, prettyPrint).toString();
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
	 Object: SingolaRendicontazione
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>singolaRendicontazione</var> of type {@link it.govpay.orm.SingolaRendicontazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>singolaRendicontazione</var>
	 * @param singolaRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,SingolaRendicontazione singolaRendicontazione) throws SerializerException {
		this.objToXml(fileName, SingolaRendicontazione.class, singolaRendicontazione, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>singolaRendicontazione</var> of type {@link it.govpay.orm.SingolaRendicontazione}
	 * 
	 * @param fileName Xml file to serialize the object <var>singolaRendicontazione</var>
	 * @param singolaRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,SingolaRendicontazione singolaRendicontazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, SingolaRendicontazione.class, singolaRendicontazione, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>singolaRendicontazione</var> of type {@link it.govpay.orm.SingolaRendicontazione}
	 * 
	 * @param file Xml file to serialize the object <var>singolaRendicontazione</var>
	 * @param singolaRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,SingolaRendicontazione singolaRendicontazione) throws SerializerException {
		this.objToXml(file, SingolaRendicontazione.class, singolaRendicontazione, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>singolaRendicontazione</var> of type {@link it.govpay.orm.SingolaRendicontazione}
	 * 
	 * @param file Xml file to serialize the object <var>singolaRendicontazione</var>
	 * @param singolaRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,SingolaRendicontazione singolaRendicontazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, SingolaRendicontazione.class, singolaRendicontazione, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>singolaRendicontazione</var> of type {@link it.govpay.orm.SingolaRendicontazione}
	 * 
	 * @param out OutputStream to serialize the object <var>singolaRendicontazione</var>
	 * @param singolaRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,SingolaRendicontazione singolaRendicontazione) throws SerializerException {
		this.objToXml(out, SingolaRendicontazione.class, singolaRendicontazione, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>singolaRendicontazione</var> of type {@link it.govpay.orm.SingolaRendicontazione}
	 * 
	 * @param out OutputStream to serialize the object <var>singolaRendicontazione</var>
	 * @param singolaRendicontazione Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,SingolaRendicontazione singolaRendicontazione,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, SingolaRendicontazione.class, singolaRendicontazione, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>singolaRendicontazione</var> of type {@link it.govpay.orm.SingolaRendicontazione}
	 * 
	 * @param singolaRendicontazione Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(SingolaRendicontazione singolaRendicontazione) throws SerializerException {
		return this.objToXml(SingolaRendicontazione.class, singolaRendicontazione, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>singolaRendicontazione</var> of type {@link it.govpay.orm.SingolaRendicontazione}
	 * 
	 * @param singolaRendicontazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(SingolaRendicontazione singolaRendicontazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(SingolaRendicontazione.class, singolaRendicontazione, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>singolaRendicontazione</var> of type {@link it.govpay.orm.SingolaRendicontazione}
	 * 
	 * @param singolaRendicontazione Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(SingolaRendicontazione singolaRendicontazione) throws SerializerException {
		return this.objToXml(SingolaRendicontazione.class, singolaRendicontazione, false).toString();
	}
	/**
	 * Serialize to String the object <var>singolaRendicontazione</var> of type {@link it.govpay.orm.SingolaRendicontazione}
	 * 
	 * @param singolaRendicontazione Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(SingolaRendicontazione singolaRendicontazione,boolean prettyPrint) throws SerializerException {
		return this.objToXml(SingolaRendicontazione.class, singolaRendicontazione, prettyPrint).toString();
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
	 Object: TracciatoXML
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>tracciatoXML</var> of type {@link it.govpay.orm.TracciatoXML}
	 * 
	 * @param fileName Xml file to serialize the object <var>tracciatoXML</var>
	 * @param tracciatoXML Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,TracciatoXML tracciatoXML) throws SerializerException {
		this.objToXml(fileName, TracciatoXML.class, tracciatoXML, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>tracciatoXML</var> of type {@link it.govpay.orm.TracciatoXML}
	 * 
	 * @param fileName Xml file to serialize the object <var>tracciatoXML</var>
	 * @param tracciatoXML Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,TracciatoXML tracciatoXML,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, TracciatoXML.class, tracciatoXML, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>tracciatoXML</var> of type {@link it.govpay.orm.TracciatoXML}
	 * 
	 * @param file Xml file to serialize the object <var>tracciatoXML</var>
	 * @param tracciatoXML Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,TracciatoXML tracciatoXML) throws SerializerException {
		this.objToXml(file, TracciatoXML.class, tracciatoXML, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>tracciatoXML</var> of type {@link it.govpay.orm.TracciatoXML}
	 * 
	 * @param file Xml file to serialize the object <var>tracciatoXML</var>
	 * @param tracciatoXML Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,TracciatoXML tracciatoXML,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, TracciatoXML.class, tracciatoXML, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>tracciatoXML</var> of type {@link it.govpay.orm.TracciatoXML}
	 * 
	 * @param out OutputStream to serialize the object <var>tracciatoXML</var>
	 * @param tracciatoXML Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,TracciatoXML tracciatoXML) throws SerializerException {
		this.objToXml(out, TracciatoXML.class, tracciatoXML, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>tracciatoXML</var> of type {@link it.govpay.orm.TracciatoXML}
	 * 
	 * @param out OutputStream to serialize the object <var>tracciatoXML</var>
	 * @param tracciatoXML Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,TracciatoXML tracciatoXML,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, TracciatoXML.class, tracciatoXML, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>tracciatoXML</var> of type {@link it.govpay.orm.TracciatoXML}
	 * 
	 * @param tracciatoXML Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(TracciatoXML tracciatoXML) throws SerializerException {
		return this.objToXml(TracciatoXML.class, tracciatoXML, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>tracciatoXML</var> of type {@link it.govpay.orm.TracciatoXML}
	 * 
	 * @param tracciatoXML Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(TracciatoXML tracciatoXML,boolean prettyPrint) throws SerializerException {
		return this.objToXml(TracciatoXML.class, tracciatoXML, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>tracciatoXML</var> of type {@link it.govpay.orm.TracciatoXML}
	 * 
	 * @param tracciatoXML Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(TracciatoXML tracciatoXML) throws SerializerException {
		return this.objToXml(TracciatoXML.class, tracciatoXML, false).toString();
	}
	/**
	 * Serialize to String the object <var>tracciatoXML</var> of type {@link it.govpay.orm.TracciatoXML}
	 * 
	 * @param tracciatoXML Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(TracciatoXML tracciatoXML,boolean prettyPrint) throws SerializerException {
		return this.objToXml(TracciatoXML.class, tracciatoXML, prettyPrint).toString();
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
	 Object: id-esito
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idEsito</var> of type {@link it.govpay.orm.IdEsito}
	 * 
	 * @param fileName Xml file to serialize the object <var>idEsito</var>
	 * @param idEsito Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdEsito idEsito) throws SerializerException {
		this.objToXml(fileName, IdEsito.class, idEsito, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>idEsito</var> of type {@link it.govpay.orm.IdEsito}
	 * 
	 * @param fileName Xml file to serialize the object <var>idEsito</var>
	 * @param idEsito Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,IdEsito idEsito,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, IdEsito.class, idEsito, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>idEsito</var> of type {@link it.govpay.orm.IdEsito}
	 * 
	 * @param file Xml file to serialize the object <var>idEsito</var>
	 * @param idEsito Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdEsito idEsito) throws SerializerException {
		this.objToXml(file, IdEsito.class, idEsito, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>idEsito</var> of type {@link it.govpay.orm.IdEsito}
	 * 
	 * @param file Xml file to serialize the object <var>idEsito</var>
	 * @param idEsito Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,IdEsito idEsito,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, IdEsito.class, idEsito, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>idEsito</var> of type {@link it.govpay.orm.IdEsito}
	 * 
	 * @param out OutputStream to serialize the object <var>idEsito</var>
	 * @param idEsito Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdEsito idEsito) throws SerializerException {
		this.objToXml(out, IdEsito.class, idEsito, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>idEsito</var> of type {@link it.govpay.orm.IdEsito}
	 * 
	 * @param out OutputStream to serialize the object <var>idEsito</var>
	 * @param idEsito Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,IdEsito idEsito,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, IdEsito.class, idEsito, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>idEsito</var> of type {@link it.govpay.orm.IdEsito}
	 * 
	 * @param idEsito Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdEsito idEsito) throws SerializerException {
		return this.objToXml(IdEsito.class, idEsito, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>idEsito</var> of type {@link it.govpay.orm.IdEsito}
	 * 
	 * @param idEsito Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(IdEsito idEsito,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdEsito.class, idEsito, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>idEsito</var> of type {@link it.govpay.orm.IdEsito}
	 * 
	 * @param idEsito Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdEsito idEsito) throws SerializerException {
		return this.objToXml(IdEsito.class, idEsito, false).toString();
	}
	/**
	 * Serialize to String the object <var>idEsito</var> of type {@link it.govpay.orm.IdEsito}
	 * 
	 * @param idEsito Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(IdEsito idEsito,boolean prettyPrint) throws SerializerException {
		return this.objToXml(IdEsito.class, idEsito, prettyPrint).toString();
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
	 Object: TabellaControparti
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>tabellaControparti</var> of type {@link it.govpay.orm.TabellaControparti}
	 * 
	 * @param fileName Xml file to serialize the object <var>tabellaControparti</var>
	 * @param tabellaControparti Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,TabellaControparti tabellaControparti) throws SerializerException {
		this.objToXml(fileName, TabellaControparti.class, tabellaControparti, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>tabellaControparti</var> of type {@link it.govpay.orm.TabellaControparti}
	 * 
	 * @param fileName Xml file to serialize the object <var>tabellaControparti</var>
	 * @param tabellaControparti Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,TabellaControparti tabellaControparti,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, TabellaControparti.class, tabellaControparti, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>tabellaControparti</var> of type {@link it.govpay.orm.TabellaControparti}
	 * 
	 * @param file Xml file to serialize the object <var>tabellaControparti</var>
	 * @param tabellaControparti Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,TabellaControparti tabellaControparti) throws SerializerException {
		this.objToXml(file, TabellaControparti.class, tabellaControparti, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>tabellaControparti</var> of type {@link it.govpay.orm.TabellaControparti}
	 * 
	 * @param file Xml file to serialize the object <var>tabellaControparti</var>
	 * @param tabellaControparti Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,TabellaControparti tabellaControparti,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, TabellaControparti.class, tabellaControparti, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>tabellaControparti</var> of type {@link it.govpay.orm.TabellaControparti}
	 * 
	 * @param out OutputStream to serialize the object <var>tabellaControparti</var>
	 * @param tabellaControparti Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,TabellaControparti tabellaControparti) throws SerializerException {
		this.objToXml(out, TabellaControparti.class, tabellaControparti, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>tabellaControparti</var> of type {@link it.govpay.orm.TabellaControparti}
	 * 
	 * @param out OutputStream to serialize the object <var>tabellaControparti</var>
	 * @param tabellaControparti Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,TabellaControparti tabellaControparti,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, TabellaControparti.class, tabellaControparti, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>tabellaControparti</var> of type {@link it.govpay.orm.TabellaControparti}
	 * 
	 * @param tabellaControparti Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(TabellaControparti tabellaControparti) throws SerializerException {
		return this.objToXml(TabellaControparti.class, tabellaControparti, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>tabellaControparti</var> of type {@link it.govpay.orm.TabellaControparti}
	 * 
	 * @param tabellaControparti Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(TabellaControparti tabellaControparti,boolean prettyPrint) throws SerializerException {
		return this.objToXml(TabellaControparti.class, tabellaControparti, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>tabellaControparti</var> of type {@link it.govpay.orm.TabellaControparti}
	 * 
	 * @param tabellaControparti Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(TabellaControparti tabellaControparti) throws SerializerException {
		return this.objToXml(TabellaControparti.class, tabellaControparti, false).toString();
	}
	/**
	 * Serialize to String the object <var>tabellaControparti</var> of type {@link it.govpay.orm.TabellaControparti}
	 * 
	 * @param tabellaControparti Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(TabellaControparti tabellaControparti,boolean prettyPrint) throws SerializerException {
		return this.objToXml(TabellaControparti.class, tabellaControparti, prettyPrint).toString();
	}
	
	
	
	/*
	 =================================================================================
	 Object: Ente
	 =================================================================================
	*/
	
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>ente</var> of type {@link it.govpay.orm.Ente}
	 * 
	 * @param fileName Xml file to serialize the object <var>ente</var>
	 * @param ente Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Ente ente) throws SerializerException {
		this.objToXml(fileName, Ente.class, ente, false);
	}
	/**
	 * Serialize to file system in <var>fileName</var> the object <var>ente</var> of type {@link it.govpay.orm.Ente}
	 * 
	 * @param fileName Xml file to serialize the object <var>ente</var>
	 * @param ente Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(String fileName,Ente ente,boolean prettyPrint) throws SerializerException {
		this.objToXml(fileName, Ente.class, ente, prettyPrint);
	}
	
	/**
	 * Serialize to file system in <var>file</var> the object <var>ente</var> of type {@link it.govpay.orm.Ente}
	 * 
	 * @param file Xml file to serialize the object <var>ente</var>
	 * @param ente Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Ente ente) throws SerializerException {
		this.objToXml(file, Ente.class, ente, false);
	}
	/**
	 * Serialize to file system in <var>file</var> the object <var>ente</var> of type {@link it.govpay.orm.Ente}
	 * 
	 * @param file Xml file to serialize the object <var>ente</var>
	 * @param ente Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(File file,Ente ente,boolean prettyPrint) throws SerializerException {
		this.objToXml(file, Ente.class, ente, prettyPrint);
	}
	
	/**
	 * Serialize to output stream <var>out</var> the object <var>ente</var> of type {@link it.govpay.orm.Ente}
	 * 
	 * @param out OutputStream to serialize the object <var>ente</var>
	 * @param ente Object to be serialized in xml file <var>fileName</var>
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Ente ente) throws SerializerException {
		this.objToXml(out, Ente.class, ente, false);
	}
	/**
	 * Serialize to output stream <var>out</var> the object <var>ente</var> of type {@link it.govpay.orm.Ente}
	 * 
	 * @param out OutputStream to serialize the object <var>ente</var>
	 * @param ente Object to be serialized in xml file <var>fileName</var>
	 * @param prettyPrint if true output the XML with indenting
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public void write(OutputStream out,Ente ente,boolean prettyPrint) throws SerializerException {
		this.objToXml(out, Ente.class, ente, prettyPrint);
	}
			
	/**
	 * Serialize to byte array the object <var>ente</var> of type {@link it.govpay.orm.Ente}
	 * 
	 * @param ente Object to be serialized
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Ente ente) throws SerializerException {
		return this.objToXml(Ente.class, ente, false).toByteArray();
	}
	/**
	 * Serialize to byte array the object <var>ente</var> of type {@link it.govpay.orm.Ente}
	 * 
	 * @param ente Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized in byte array
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public byte[] toByteArray(Ente ente,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Ente.class, ente, prettyPrint).toByteArray();
	}
	
	/**
	 * Serialize to String the object <var>ente</var> of type {@link it.govpay.orm.Ente}
	 * 
	 * @param ente Object to be serialized
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Ente ente) throws SerializerException {
		return this.objToXml(Ente.class, ente, false).toString();
	}
	/**
	 * Serialize to String the object <var>ente</var> of type {@link it.govpay.orm.Ente}
	 * 
	 * @param ente Object to be serialized
	 * @param prettyPrint if true output the XML with indenting
	 * @return Object to be serialized as String
	 * @throws SerializerException The exception that is thrown when an error occurs during serialization
	 */
	public String toString(Ente ente,boolean prettyPrint) throws SerializerException {
		return this.objToXml(Ente.class, ente, prettyPrint).toString();
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
