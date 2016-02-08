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

import org.openspcoop2.generic_project.exception.DeserializerException;

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

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.File;

/**     
 * XML Deserializer of beans
 *
 * @author Giovanni Bussu (bussu@link.it)
 * @author Lorenzo Nardi (nardi@link.it)
 * @author $Author$
 * @version $Rev$, $Date$
 */

public abstract class AbstractDeserializer {



	protected abstract Object _xmlToObj(InputStream is, Class<?> c) throws Exception;
	
	private Object xmlToObj(InputStream is,Class<?> c) throws DeserializerException{
		try{
			return this._xmlToObj(is, c);
		}catch(Exception e){
			throw new DeserializerException(e.getMessage(), e);
		}
	}
	private Object xmlToObj(String fileName,Class<?> c) throws DeserializerException{
		try{
			return this.xmlToObj(new File(fileName), c);
		}catch(Exception e){
			throw new DeserializerException(e.getMessage(), e);
		}
	}
	private Object xmlToObj(File file,Class<?> c) throws DeserializerException{
		FileInputStream fin = null;
		try{
			fin = new FileInputStream(file);
			return this._xmlToObj(fin, c);
		}catch(Exception e){
			throw new DeserializerException(e.getMessage(),e);
		}finally{
			try{
				fin.close();
			}catch(Exception e){}
		}
	}
	private Object xmlToObj(byte[] file,Class<?> c) throws DeserializerException{
		ByteArrayInputStream fin = null;
		try{
			fin = new ByteArrayInputStream(file);
			return this._xmlToObj(fin, c);
		}catch(Exception e){
			throw new DeserializerException(e.getMessage(),e);
		}finally{
			try{
				fin.close();
			}catch(Exception e){}
		}
	}






	/*
	 =================================================================================
	 Object: id-psp
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdPsp}
	 * @return Object type {@link it.govpay.orm.IdPsp}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPsp readIdPsp(String fileName) throws DeserializerException {
		return (IdPsp) this.xmlToObj(fileName, IdPsp.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdPsp}
	 * @return Object type {@link it.govpay.orm.IdPsp}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPsp readIdPsp(File file) throws DeserializerException {
		return (IdPsp) this.xmlToObj(file, IdPsp.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdPsp}
	 * @return Object type {@link it.govpay.orm.IdPsp}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPsp readIdPsp(InputStream in) throws DeserializerException {
		return (IdPsp) this.xmlToObj(in, IdPsp.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdPsp}
	 * @return Object type {@link it.govpay.orm.IdPsp}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPsp readIdPsp(byte[] in) throws DeserializerException {
		return (IdPsp) this.xmlToObj(in, IdPsp.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdPsp}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdPsp}
	 * @return Object type {@link it.govpay.orm.IdPsp}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPsp readIdPspFromString(String in) throws DeserializerException {
		return (IdPsp) this.xmlToObj(in.getBytes(), IdPsp.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Canale
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Canale}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Canale}
	 * @return Object type {@link it.govpay.orm.Canale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Canale readCanale(String fileName) throws DeserializerException {
		return (Canale) this.xmlToObj(fileName, Canale.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Canale}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Canale}
	 * @return Object type {@link it.govpay.orm.Canale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Canale readCanale(File file) throws DeserializerException {
		return (Canale) this.xmlToObj(file, Canale.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Canale}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Canale}
	 * @return Object type {@link it.govpay.orm.Canale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Canale readCanale(InputStream in) throws DeserializerException {
		return (Canale) this.xmlToObj(in, Canale.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Canale}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Canale}
	 * @return Object type {@link it.govpay.orm.Canale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Canale readCanale(byte[] in) throws DeserializerException {
		return (Canale) this.xmlToObj(in, Canale.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Canale}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Canale}
	 * @return Object type {@link it.govpay.orm.Canale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Canale readCanaleFromString(String in) throws DeserializerException {
		return (Canale) this.xmlToObj(in.getBytes(), Canale.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-sla
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdSla}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdSla}
	 * @return Object type {@link it.govpay.orm.IdSla}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSla readIdSla(String fileName) throws DeserializerException {
		return (IdSla) this.xmlToObj(fileName, IdSla.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdSla}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdSla}
	 * @return Object type {@link it.govpay.orm.IdSla}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSla readIdSla(File file) throws DeserializerException {
		return (IdSla) this.xmlToObj(file, IdSla.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdSla}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdSla}
	 * @return Object type {@link it.govpay.orm.IdSla}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSla readIdSla(InputStream in) throws DeserializerException {
		return (IdSla) this.xmlToObj(in, IdSla.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdSla}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdSla}
	 * @return Object type {@link it.govpay.orm.IdSla}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSla readIdSla(byte[] in) throws DeserializerException {
		return (IdSla) this.xmlToObj(in, IdSla.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdSla}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdSla}
	 * @return Object type {@link it.govpay.orm.IdSla}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSla readIdSlaFromString(String in) throws DeserializerException {
		return (IdSla) this.xmlToObj(in.getBytes(), IdSla.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Rilevamento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Rilevamento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Rilevamento}
	 * @return Object type {@link it.govpay.orm.Rilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Rilevamento readRilevamento(String fileName) throws DeserializerException {
		return (Rilevamento) this.xmlToObj(fileName, Rilevamento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Rilevamento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Rilevamento}
	 * @return Object type {@link it.govpay.orm.Rilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Rilevamento readRilevamento(File file) throws DeserializerException {
		return (Rilevamento) this.xmlToObj(file, Rilevamento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Rilevamento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Rilevamento}
	 * @return Object type {@link it.govpay.orm.Rilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Rilevamento readRilevamento(InputStream in) throws DeserializerException {
		return (Rilevamento) this.xmlToObj(in, Rilevamento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Rilevamento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Rilevamento}
	 * @return Object type {@link it.govpay.orm.Rilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Rilevamento readRilevamento(byte[] in) throws DeserializerException {
		return (Rilevamento) this.xmlToObj(in, Rilevamento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Rilevamento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Rilevamento}
	 * @return Object type {@link it.govpay.orm.Rilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Rilevamento readRilevamentoFromString(String in) throws DeserializerException {
		return (Rilevamento) this.xmlToObj(in.getBytes(), Rilevamento.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-evento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdEvento}
	 * @return Object type {@link it.govpay.orm.IdEvento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEvento readIdEvento(String fileName) throws DeserializerException {
		return (IdEvento) this.xmlToObj(fileName, IdEvento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdEvento}
	 * @return Object type {@link it.govpay.orm.IdEvento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEvento readIdEvento(File file) throws DeserializerException {
		return (IdEvento) this.xmlToObj(file, IdEvento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdEvento}
	 * @return Object type {@link it.govpay.orm.IdEvento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEvento readIdEvento(InputStream in) throws DeserializerException {
		return (IdEvento) this.xmlToObj(in, IdEvento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdEvento}
	 * @return Object type {@link it.govpay.orm.IdEvento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEvento readIdEvento(byte[] in) throws DeserializerException {
		return (IdEvento) this.xmlToObj(in, IdEvento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdEvento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdEvento}
	 * @return Object type {@link it.govpay.orm.IdEvento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEvento readIdEventoFromString(String in) throws DeserializerException {
		return (IdEvento) this.xmlToObj(in.getBytes(), IdEvento.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-intermediario
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdIntermediario}
	 * @return Object type {@link it.govpay.orm.IdIntermediario}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIntermediario readIdIntermediario(String fileName) throws DeserializerException {
		return (IdIntermediario) this.xmlToObj(fileName, IdIntermediario.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdIntermediario}
	 * @return Object type {@link it.govpay.orm.IdIntermediario}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIntermediario readIdIntermediario(File file) throws DeserializerException {
		return (IdIntermediario) this.xmlToObj(file, IdIntermediario.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdIntermediario}
	 * @return Object type {@link it.govpay.orm.IdIntermediario}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIntermediario readIdIntermediario(InputStream in) throws DeserializerException {
		return (IdIntermediario) this.xmlToObj(in, IdIntermediario.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdIntermediario}
	 * @return Object type {@link it.govpay.orm.IdIntermediario}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIntermediario readIdIntermediario(byte[] in) throws DeserializerException {
		return (IdIntermediario) this.xmlToObj(in, IdIntermediario.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdIntermediario}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdIntermediario}
	 * @return Object type {@link it.govpay.orm.IdIntermediario}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIntermediario readIdIntermediarioFromString(String in) throws DeserializerException {
		return (IdIntermediario) this.xmlToObj(in.getBytes(), IdIntermediario.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Stazione
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Stazione}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Stazione}
	 * @return Object type {@link it.govpay.orm.Stazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Stazione readStazione(String fileName) throws DeserializerException {
		return (Stazione) this.xmlToObj(fileName, Stazione.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Stazione}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Stazione}
	 * @return Object type {@link it.govpay.orm.Stazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Stazione readStazione(File file) throws DeserializerException {
		return (Stazione) this.xmlToObj(file, Stazione.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Stazione}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Stazione}
	 * @return Object type {@link it.govpay.orm.Stazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Stazione readStazione(InputStream in) throws DeserializerException {
		return (Stazione) this.xmlToObj(in, Stazione.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Stazione}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Stazione}
	 * @return Object type {@link it.govpay.orm.Stazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Stazione readStazione(byte[] in) throws DeserializerException {
		return (Stazione) this.xmlToObj(in, Stazione.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Stazione}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Stazione}
	 * @return Object type {@link it.govpay.orm.Stazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Stazione readStazioneFromString(String in) throws DeserializerException {
		return (Stazione) this.xmlToObj(in.getBytes(), Stazione.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Anagrafica
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Anagrafica}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Anagrafica}
	 * @return Object type {@link it.govpay.orm.Anagrafica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Anagrafica readAnagrafica(String fileName) throws DeserializerException {
		return (Anagrafica) this.xmlToObj(fileName, Anagrafica.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Anagrafica}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Anagrafica}
	 * @return Object type {@link it.govpay.orm.Anagrafica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Anagrafica readAnagrafica(File file) throws DeserializerException {
		return (Anagrafica) this.xmlToObj(file, Anagrafica.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Anagrafica}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Anagrafica}
	 * @return Object type {@link it.govpay.orm.Anagrafica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Anagrafica readAnagrafica(InputStream in) throws DeserializerException {
		return (Anagrafica) this.xmlToObj(in, Anagrafica.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Anagrafica}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Anagrafica}
	 * @return Object type {@link it.govpay.orm.Anagrafica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Anagrafica readAnagrafica(byte[] in) throws DeserializerException {
		return (Anagrafica) this.xmlToObj(in, Anagrafica.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Anagrafica}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Anagrafica}
	 * @return Object type {@link it.govpay.orm.Anagrafica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Anagrafica readAnagraficaFromString(String in) throws DeserializerException {
		return (Anagrafica) this.xmlToObj(in.getBytes(), Anagrafica.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-rilevamento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdRilevamento}
	 * @return Object type {@link it.govpay.orm.IdRilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRilevamento readIdRilevamento(String fileName) throws DeserializerException {
		return (IdRilevamento) this.xmlToObj(fileName, IdRilevamento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdRilevamento}
	 * @return Object type {@link it.govpay.orm.IdRilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRilevamento readIdRilevamento(File file) throws DeserializerException {
		return (IdRilevamento) this.xmlToObj(file, IdRilevamento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdRilevamento}
	 * @return Object type {@link it.govpay.orm.IdRilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRilevamento readIdRilevamento(InputStream in) throws DeserializerException {
		return (IdRilevamento) this.xmlToObj(in, IdRilevamento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdRilevamento}
	 * @return Object type {@link it.govpay.orm.IdRilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRilevamento readIdRilevamento(byte[] in) throws DeserializerException {
		return (IdRilevamento) this.xmlToObj(in, IdRilevamento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdRilevamento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdRilevamento}
	 * @return Object type {@link it.govpay.orm.IdRilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRilevamento readIdRilevamentoFromString(String in) throws DeserializerException {
		return (IdRilevamento) this.xmlToObj(in.getBytes(), IdRilevamento.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Disponibilita
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Disponibilita}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Disponibilita}
	 * @return Object type {@link it.govpay.orm.Disponibilita}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Disponibilita readDisponibilita(String fileName) throws DeserializerException {
		return (Disponibilita) this.xmlToObj(fileName, Disponibilita.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Disponibilita}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Disponibilita}
	 * @return Object type {@link it.govpay.orm.Disponibilita}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Disponibilita readDisponibilita(File file) throws DeserializerException {
		return (Disponibilita) this.xmlToObj(file, Disponibilita.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Disponibilita}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Disponibilita}
	 * @return Object type {@link it.govpay.orm.Disponibilita}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Disponibilita readDisponibilita(InputStream in) throws DeserializerException {
		return (Disponibilita) this.xmlToObj(in, Disponibilita.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Disponibilita}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Disponibilita}
	 * @return Object type {@link it.govpay.orm.Disponibilita}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Disponibilita readDisponibilita(byte[] in) throws DeserializerException {
		return (Disponibilita) this.xmlToObj(in, Disponibilita.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Disponibilita}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Disponibilita}
	 * @return Object type {@link it.govpay.orm.Disponibilita}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Disponibilita readDisponibilitaFromString(String in) throws DeserializerException {
		return (Disponibilita) this.xmlToObj(in.getBytes(), Disponibilita.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-dominio
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdDominio}
	 * @return Object type {@link it.govpay.orm.IdDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdDominio readIdDominio(String fileName) throws DeserializerException {
		return (IdDominio) this.xmlToObj(fileName, IdDominio.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdDominio}
	 * @return Object type {@link it.govpay.orm.IdDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdDominio readIdDominio(File file) throws DeserializerException {
		return (IdDominio) this.xmlToObj(file, IdDominio.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdDominio}
	 * @return Object type {@link it.govpay.orm.IdDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdDominio readIdDominio(InputStream in) throws DeserializerException {
		return (IdDominio) this.xmlToObj(in, IdDominio.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdDominio}
	 * @return Object type {@link it.govpay.orm.IdDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdDominio readIdDominio(byte[] in) throws DeserializerException {
		return (IdDominio) this.xmlToObj(in, IdDominio.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdDominio}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdDominio}
	 * @return Object type {@link it.govpay.orm.IdDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdDominio readIdDominioFromString(String in) throws DeserializerException {
		return (IdDominio) this.xmlToObj(in.getBytes(), IdDominio.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-canale
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdCanale}
	 * @return Object type {@link it.govpay.orm.IdCanale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdCanale readIdCanale(String fileName) throws DeserializerException {
		return (IdCanale) this.xmlToObj(fileName, IdCanale.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdCanale}
	 * @return Object type {@link it.govpay.orm.IdCanale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdCanale readIdCanale(File file) throws DeserializerException {
		return (IdCanale) this.xmlToObj(file, IdCanale.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdCanale}
	 * @return Object type {@link it.govpay.orm.IdCanale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdCanale readIdCanale(InputStream in) throws DeserializerException {
		return (IdCanale) this.xmlToObj(in, IdCanale.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdCanale}
	 * @return Object type {@link it.govpay.orm.IdCanale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdCanale readIdCanale(byte[] in) throws DeserializerException {
		return (IdCanale) this.xmlToObj(in, IdCanale.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdCanale}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdCanale}
	 * @return Object type {@link it.govpay.orm.IdCanale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdCanale readIdCanaleFromString(String in) throws DeserializerException {
		return (IdCanale) this.xmlToObj(in.getBytes(), IdCanale.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Intermediario
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Intermediario}
	 * @return Object type {@link it.govpay.orm.Intermediario}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Intermediario readIntermediario(String fileName) throws DeserializerException {
		return (Intermediario) this.xmlToObj(fileName, Intermediario.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Intermediario}
	 * @return Object type {@link it.govpay.orm.Intermediario}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Intermediario readIntermediario(File file) throws DeserializerException {
		return (Intermediario) this.xmlToObj(file, Intermediario.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Intermediario}
	 * @return Object type {@link it.govpay.orm.Intermediario}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Intermediario readIntermediario(InputStream in) throws DeserializerException {
		return (Intermediario) this.xmlToObj(in, Intermediario.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Intermediario}
	 * @return Object type {@link it.govpay.orm.Intermediario}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Intermediario readIntermediario(byte[] in) throws DeserializerException {
		return (Intermediario) this.xmlToObj(in, Intermediario.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Intermediario}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Intermediario}
	 * @return Object type {@link it.govpay.orm.Intermediario}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Intermediario readIntermediarioFromString(String in) throws DeserializerException {
		return (Intermediario) this.xmlToObj(in.getBytes(), Intermediario.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-tracciato
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdTracciato}
	 * @return Object type {@link it.govpay.orm.IdTracciato}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTracciato readIdTracciato(String fileName) throws DeserializerException {
		return (IdTracciato) this.xmlToObj(fileName, IdTracciato.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdTracciato}
	 * @return Object type {@link it.govpay.orm.IdTracciato}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTracciato readIdTracciato(File file) throws DeserializerException {
		return (IdTracciato) this.xmlToObj(file, IdTracciato.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdTracciato}
	 * @return Object type {@link it.govpay.orm.IdTracciato}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTracciato readIdTracciato(InputStream in) throws DeserializerException {
		return (IdTracciato) this.xmlToObj(in, IdTracciato.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdTracciato}
	 * @return Object type {@link it.govpay.orm.IdTracciato}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTracciato readIdTracciato(byte[] in) throws DeserializerException {
		return (IdTracciato) this.xmlToObj(in, IdTracciato.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdTracciato}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdTracciato}
	 * @return Object type {@link it.govpay.orm.IdTracciato}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTracciato readIdTracciatoFromString(String in) throws DeserializerException {
		return (IdTracciato) this.xmlToObj(in.getBytes(), IdTracciato.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: FR
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.FR}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.FR}
	 * @return Object type {@link it.govpay.orm.FR}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public FR readFR(String fileName) throws DeserializerException {
		return (FR) this.xmlToObj(fileName, FR.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.FR}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.FR}
	 * @return Object type {@link it.govpay.orm.FR}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public FR readFR(File file) throws DeserializerException {
		return (FR) this.xmlToObj(file, FR.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.FR}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.FR}
	 * @return Object type {@link it.govpay.orm.FR}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public FR readFR(InputStream in) throws DeserializerException {
		return (FR) this.xmlToObj(in, FR.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.FR}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.FR}
	 * @return Object type {@link it.govpay.orm.FR}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public FR readFR(byte[] in) throws DeserializerException {
		return (FR) this.xmlToObj(in, FR.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.FR}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.FR}
	 * @return Object type {@link it.govpay.orm.FR}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public FR readFRFromString(String in) throws DeserializerException {
		return (FR) this.xmlToObj(in.getBytes(), FR.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-ente
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdEnte}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdEnte}
	 * @return Object type {@link it.govpay.orm.IdEnte}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEnte readIdEnte(String fileName) throws DeserializerException {
		return (IdEnte) this.xmlToObj(fileName, IdEnte.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdEnte}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdEnte}
	 * @return Object type {@link it.govpay.orm.IdEnte}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEnte readIdEnte(File file) throws DeserializerException {
		return (IdEnte) this.xmlToObj(file, IdEnte.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdEnte}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdEnte}
	 * @return Object type {@link it.govpay.orm.IdEnte}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEnte readIdEnte(InputStream in) throws DeserializerException {
		return (IdEnte) this.xmlToObj(in, IdEnte.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdEnte}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdEnte}
	 * @return Object type {@link it.govpay.orm.IdEnte}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEnte readIdEnte(byte[] in) throws DeserializerException {
		return (IdEnte) this.xmlToObj(in, IdEnte.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdEnte}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdEnte}
	 * @return Object type {@link it.govpay.orm.IdEnte}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEnte readIdEnteFromString(String in) throws DeserializerException {
		return (IdEnte) this.xmlToObj(in.getBytes(), IdEnte.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: OperatoreEnte
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.OperatoreEnte}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.OperatoreEnte}
	 * @return Object type {@link it.govpay.orm.OperatoreEnte}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public OperatoreEnte readOperatoreEnte(String fileName) throws DeserializerException {
		return (OperatoreEnte) this.xmlToObj(fileName, OperatoreEnte.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.OperatoreEnte}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.OperatoreEnte}
	 * @return Object type {@link it.govpay.orm.OperatoreEnte}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public OperatoreEnte readOperatoreEnte(File file) throws DeserializerException {
		return (OperatoreEnte) this.xmlToObj(file, OperatoreEnte.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.OperatoreEnte}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.OperatoreEnte}
	 * @return Object type {@link it.govpay.orm.OperatoreEnte}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public OperatoreEnte readOperatoreEnte(InputStream in) throws DeserializerException {
		return (OperatoreEnte) this.xmlToObj(in, OperatoreEnte.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.OperatoreEnte}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.OperatoreEnte}
	 * @return Object type {@link it.govpay.orm.OperatoreEnte}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public OperatoreEnte readOperatoreEnte(byte[] in) throws DeserializerException {
		return (OperatoreEnte) this.xmlToObj(in, OperatoreEnte.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.OperatoreEnte}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.OperatoreEnte}
	 * @return Object type {@link it.govpay.orm.OperatoreEnte}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public OperatoreEnte readOperatoreEnteFromString(String in) throws DeserializerException {
		return (OperatoreEnte) this.xmlToObj(in.getBytes(), OperatoreEnte.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-media-rilevamento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdMediaRilevamento}
	 * @return Object type {@link it.govpay.orm.IdMediaRilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMediaRilevamento readIdMediaRilevamento(String fileName) throws DeserializerException {
		return (IdMediaRilevamento) this.xmlToObj(fileName, IdMediaRilevamento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdMediaRilevamento}
	 * @return Object type {@link it.govpay.orm.IdMediaRilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMediaRilevamento readIdMediaRilevamento(File file) throws DeserializerException {
		return (IdMediaRilevamento) this.xmlToObj(file, IdMediaRilevamento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdMediaRilevamento}
	 * @return Object type {@link it.govpay.orm.IdMediaRilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMediaRilevamento readIdMediaRilevamento(InputStream in) throws DeserializerException {
		return (IdMediaRilevamento) this.xmlToObj(in, IdMediaRilevamento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdMediaRilevamento}
	 * @return Object type {@link it.govpay.orm.IdMediaRilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMediaRilevamento readIdMediaRilevamento(byte[] in) throws DeserializerException {
		return (IdMediaRilevamento) this.xmlToObj(in, IdMediaRilevamento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdMediaRilevamento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdMediaRilevamento}
	 * @return Object type {@link it.govpay.orm.IdMediaRilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMediaRilevamento readIdMediaRilevamentoFromString(String in) throws DeserializerException {
		return (IdMediaRilevamento) this.xmlToObj(in.getBytes(), IdMediaRilevamento.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Psp
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Psp}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Psp}
	 * @return Object type {@link it.govpay.orm.Psp}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Psp readPsp(String fileName) throws DeserializerException {
		return (Psp) this.xmlToObj(fileName, Psp.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Psp}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Psp}
	 * @return Object type {@link it.govpay.orm.Psp}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Psp readPsp(File file) throws DeserializerException {
		return (Psp) this.xmlToObj(file, Psp.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Psp}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Psp}
	 * @return Object type {@link it.govpay.orm.Psp}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Psp readPsp(InputStream in) throws DeserializerException {
		return (Psp) this.xmlToObj(in, Psp.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Psp}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Psp}
	 * @return Object type {@link it.govpay.orm.Psp}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Psp readPsp(byte[] in) throws DeserializerException {
		return (Psp) this.xmlToObj(in, Psp.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Psp}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Psp}
	 * @return Object type {@link it.govpay.orm.Psp}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Psp readPspFromString(String in) throws DeserializerException {
		return (Psp) this.xmlToObj(in.getBytes(), Psp.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-rpt
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdRpt}
	 * @return Object type {@link it.govpay.orm.IdRpt}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRpt readIdRpt(String fileName) throws DeserializerException {
		return (IdRpt) this.xmlToObj(fileName, IdRpt.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdRpt}
	 * @return Object type {@link it.govpay.orm.IdRpt}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRpt readIdRpt(File file) throws DeserializerException {
		return (IdRpt) this.xmlToObj(file, IdRpt.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdRpt}
	 * @return Object type {@link it.govpay.orm.IdRpt}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRpt readIdRpt(InputStream in) throws DeserializerException {
		return (IdRpt) this.xmlToObj(in, IdRpt.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdRpt}
	 * @return Object type {@link it.govpay.orm.IdRpt}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRpt readIdRpt(byte[] in) throws DeserializerException {
		return (IdRpt) this.xmlToObj(in, IdRpt.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdRpt}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdRpt}
	 * @return Object type {@link it.govpay.orm.IdRpt}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRpt readIdRptFromString(String in) throws DeserializerException {
		return (IdRpt) this.xmlToObj(in.getBytes(), IdRpt.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: RT
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.RT}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.RT}
	 * @return Object type {@link it.govpay.orm.RT}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RT readRT(String fileName) throws DeserializerException {
		return (RT) this.xmlToObj(fileName, RT.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.RT}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.RT}
	 * @return Object type {@link it.govpay.orm.RT}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RT readRT(File file) throws DeserializerException {
		return (RT) this.xmlToObj(file, RT.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.RT}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.RT}
	 * @return Object type {@link it.govpay.orm.RT}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RT readRT(InputStream in) throws DeserializerException {
		return (RT) this.xmlToObj(in, RT.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.RT}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.RT}
	 * @return Object type {@link it.govpay.orm.RT}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RT readRT(byte[] in) throws DeserializerException {
		return (RT) this.xmlToObj(in, RT.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.RT}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.RT}
	 * @return Object type {@link it.govpay.orm.RT}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RT readRTFromString(String in) throws DeserializerException {
		return (RT) this.xmlToObj(in.getBytes(), RT.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-anagrafica
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdAnagrafica}
	 * @return Object type {@link it.govpay.orm.IdAnagrafica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdAnagrafica readIdAnagrafica(String fileName) throws DeserializerException {
		return (IdAnagrafica) this.xmlToObj(fileName, IdAnagrafica.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdAnagrafica}
	 * @return Object type {@link it.govpay.orm.IdAnagrafica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdAnagrafica readIdAnagrafica(File file) throws DeserializerException {
		return (IdAnagrafica) this.xmlToObj(file, IdAnagrafica.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdAnagrafica}
	 * @return Object type {@link it.govpay.orm.IdAnagrafica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdAnagrafica readIdAnagrafica(InputStream in) throws DeserializerException {
		return (IdAnagrafica) this.xmlToObj(in, IdAnagrafica.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdAnagrafica}
	 * @return Object type {@link it.govpay.orm.IdAnagrafica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdAnagrafica readIdAnagrafica(byte[] in) throws DeserializerException {
		return (IdAnagrafica) this.xmlToObj(in, IdAnagrafica.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdAnagrafica}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdAnagrafica}
	 * @return Object type {@link it.govpay.orm.IdAnagrafica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdAnagrafica readIdAnagraficaFromString(String in) throws DeserializerException {
		return (IdAnagrafica) this.xmlToObj(in.getBytes(), IdAnagrafica.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-rt
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdRt}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdRt}
	 * @return Object type {@link it.govpay.orm.IdRt}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRt readIdRt(String fileName) throws DeserializerException {
		return (IdRt) this.xmlToObj(fileName, IdRt.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdRt}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdRt}
	 * @return Object type {@link it.govpay.orm.IdRt}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRt readIdRt(File file) throws DeserializerException {
		return (IdRt) this.xmlToObj(file, IdRt.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdRt}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdRt}
	 * @return Object type {@link it.govpay.orm.IdRt}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRt readIdRt(InputStream in) throws DeserializerException {
		return (IdRt) this.xmlToObj(in, IdRt.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdRt}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdRt}
	 * @return Object type {@link it.govpay.orm.IdRt}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRt readIdRt(byte[] in) throws DeserializerException {
		return (IdRt) this.xmlToObj(in, IdRt.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdRt}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdRt}
	 * @return Object type {@link it.govpay.orm.IdRt}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRt readIdRtFromString(String in) throws DeserializerException {
		return (IdRt) this.xmlToObj(in.getBytes(), IdRt.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: RR
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.RR}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.RR}
	 * @return Object type {@link it.govpay.orm.RR}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RR readRR(String fileName) throws DeserializerException {
		return (RR) this.xmlToObj(fileName, RR.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.RR}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.RR}
	 * @return Object type {@link it.govpay.orm.RR}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RR readRR(File file) throws DeserializerException {
		return (RR) this.xmlToObj(file, RR.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.RR}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.RR}
	 * @return Object type {@link it.govpay.orm.RR}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RR readRR(InputStream in) throws DeserializerException {
		return (RR) this.xmlToObj(in, RR.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.RR}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.RR}
	 * @return Object type {@link it.govpay.orm.RR}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RR readRR(byte[] in) throws DeserializerException {
		return (RR) this.xmlToObj(in, RR.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.RR}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.RR}
	 * @return Object type {@link it.govpay.orm.RR}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RR readRRFromString(String in) throws DeserializerException {
		return (RR) this.xmlToObj(in.getBytes(), RR.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Esito
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Esito}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Esito}
	 * @return Object type {@link it.govpay.orm.Esito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Esito readEsito(String fileName) throws DeserializerException {
		return (Esito) this.xmlToObj(fileName, Esito.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Esito}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Esito}
	 * @return Object type {@link it.govpay.orm.Esito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Esito readEsito(File file) throws DeserializerException {
		return (Esito) this.xmlToObj(file, Esito.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Esito}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Esito}
	 * @return Object type {@link it.govpay.orm.Esito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Esito readEsito(InputStream in) throws DeserializerException {
		return (Esito) this.xmlToObj(in, Esito.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Esito}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Esito}
	 * @return Object type {@link it.govpay.orm.Esito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Esito readEsito(byte[] in) throws DeserializerException {
		return (Esito) this.xmlToObj(in, Esito.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Esito}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Esito}
	 * @return Object type {@link it.govpay.orm.Esito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Esito readEsitoFromString(String in) throws DeserializerException {
		return (Esito) this.xmlToObj(in.getBytes(), Esito.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-applicazione
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdApplicazione}
	 * @return Object type {@link it.govpay.orm.IdApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdApplicazione readIdApplicazione(String fileName) throws DeserializerException {
		return (IdApplicazione) this.xmlToObj(fileName, IdApplicazione.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdApplicazione}
	 * @return Object type {@link it.govpay.orm.IdApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdApplicazione readIdApplicazione(File file) throws DeserializerException {
		return (IdApplicazione) this.xmlToObj(file, IdApplicazione.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdApplicazione}
	 * @return Object type {@link it.govpay.orm.IdApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdApplicazione readIdApplicazione(InputStream in) throws DeserializerException {
		return (IdApplicazione) this.xmlToObj(in, IdApplicazione.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdApplicazione}
	 * @return Object type {@link it.govpay.orm.IdApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdApplicazione readIdApplicazione(byte[] in) throws DeserializerException {
		return (IdApplicazione) this.xmlToObj(in, IdApplicazione.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdApplicazione}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdApplicazione}
	 * @return Object type {@link it.govpay.orm.IdApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdApplicazione readIdApplicazioneFromString(String in) throws DeserializerException {
		return (IdApplicazione) this.xmlToObj(in.getBytes(), IdApplicazione.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-rr
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdRr}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdRr}
	 * @return Object type {@link it.govpay.orm.IdRr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRr readIdRr(String fileName) throws DeserializerException {
		return (IdRr) this.xmlToObj(fileName, IdRr.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdRr}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdRr}
	 * @return Object type {@link it.govpay.orm.IdRr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRr readIdRr(File file) throws DeserializerException {
		return (IdRr) this.xmlToObj(file, IdRr.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdRr}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdRr}
	 * @return Object type {@link it.govpay.orm.IdRr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRr readIdRr(InputStream in) throws DeserializerException {
		return (IdRr) this.xmlToObj(in, IdRr.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdRr}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdRr}
	 * @return Object type {@link it.govpay.orm.IdRr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRr readIdRr(byte[] in) throws DeserializerException {
		return (IdRr) this.xmlToObj(in, IdRr.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdRr}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdRr}
	 * @return Object type {@link it.govpay.orm.IdRr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRr readIdRrFromString(String in) throws DeserializerException {
		return (IdRr) this.xmlToObj(in.getBytes(), IdRr.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: SingolaRevoca
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.SingolaRevoca}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.SingolaRevoca}
	 * @return Object type {@link it.govpay.orm.SingolaRevoca}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public SingolaRevoca readSingolaRevoca(String fileName) throws DeserializerException {
		return (SingolaRevoca) this.xmlToObj(fileName, SingolaRevoca.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.SingolaRevoca}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.SingolaRevoca}
	 * @return Object type {@link it.govpay.orm.SingolaRevoca}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public SingolaRevoca readSingolaRevoca(File file) throws DeserializerException {
		return (SingolaRevoca) this.xmlToObj(file, SingolaRevoca.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.SingolaRevoca}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.SingolaRevoca}
	 * @return Object type {@link it.govpay.orm.SingolaRevoca}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public SingolaRevoca readSingolaRevoca(InputStream in) throws DeserializerException {
		return (SingolaRevoca) this.xmlToObj(in, SingolaRevoca.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.SingolaRevoca}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.SingolaRevoca}
	 * @return Object type {@link it.govpay.orm.SingolaRevoca}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public SingolaRevoca readSingolaRevoca(byte[] in) throws DeserializerException {
		return (SingolaRevoca) this.xmlToObj(in, SingolaRevoca.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.SingolaRevoca}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.SingolaRevoca}
	 * @return Object type {@link it.govpay.orm.SingolaRevoca}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public SingolaRevoca readSingolaRevocaFromString(String in) throws DeserializerException {
		return (SingolaRevoca) this.xmlToObj(in.getBytes(), SingolaRevoca.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-singolo-versamento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdSingoloVersamento}
	 * @return Object type {@link it.govpay.orm.IdSingoloVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingoloVersamento readIdSingoloVersamento(String fileName) throws DeserializerException {
		return (IdSingoloVersamento) this.xmlToObj(fileName, IdSingoloVersamento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdSingoloVersamento}
	 * @return Object type {@link it.govpay.orm.IdSingoloVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingoloVersamento readIdSingoloVersamento(File file) throws DeserializerException {
		return (IdSingoloVersamento) this.xmlToObj(file, IdSingoloVersamento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdSingoloVersamento}
	 * @return Object type {@link it.govpay.orm.IdSingoloVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingoloVersamento readIdSingoloVersamento(InputStream in) throws DeserializerException {
		return (IdSingoloVersamento) this.xmlToObj(in, IdSingoloVersamento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdSingoloVersamento}
	 * @return Object type {@link it.govpay.orm.IdSingoloVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingoloVersamento readIdSingoloVersamento(byte[] in) throws DeserializerException {
		return (IdSingoloVersamento) this.xmlToObj(in, IdSingoloVersamento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdSingoloVersamento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdSingoloVersamento}
	 * @return Object type {@link it.govpay.orm.IdSingoloVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingoloVersamento readIdSingoloVersamentoFromString(String in) throws DeserializerException {
		return (IdSingoloVersamento) this.xmlToObj(in.getBytes(), IdSingoloVersamento.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Operatore
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Operatore}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Operatore}
	 * @return Object type {@link it.govpay.orm.Operatore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Operatore readOperatore(String fileName) throws DeserializerException {
		return (Operatore) this.xmlToObj(fileName, Operatore.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Operatore}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Operatore}
	 * @return Object type {@link it.govpay.orm.Operatore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Operatore readOperatore(File file) throws DeserializerException {
		return (Operatore) this.xmlToObj(file, Operatore.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Operatore}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Operatore}
	 * @return Object type {@link it.govpay.orm.Operatore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Operatore readOperatore(InputStream in) throws DeserializerException {
		return (Operatore) this.xmlToObj(in, Operatore.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Operatore}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Operatore}
	 * @return Object type {@link it.govpay.orm.Operatore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Operatore readOperatore(byte[] in) throws DeserializerException {
		return (Operatore) this.xmlToObj(in, Operatore.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Operatore}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Operatore}
	 * @return Object type {@link it.govpay.orm.Operatore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Operatore readOperatoreFromString(String in) throws DeserializerException {
		return (Operatore) this.xmlToObj(in.getBytes(), Operatore.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: OperatoreApplicazione
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.OperatoreApplicazione}
	 * @return Object type {@link it.govpay.orm.OperatoreApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public OperatoreApplicazione readOperatoreApplicazione(String fileName) throws DeserializerException {
		return (OperatoreApplicazione) this.xmlToObj(fileName, OperatoreApplicazione.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.OperatoreApplicazione}
	 * @return Object type {@link it.govpay.orm.OperatoreApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public OperatoreApplicazione readOperatoreApplicazione(File file) throws DeserializerException {
		return (OperatoreApplicazione) this.xmlToObj(file, OperatoreApplicazione.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.OperatoreApplicazione}
	 * @return Object type {@link it.govpay.orm.OperatoreApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public OperatoreApplicazione readOperatoreApplicazione(InputStream in) throws DeserializerException {
		return (OperatoreApplicazione) this.xmlToObj(in, OperatoreApplicazione.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.OperatoreApplicazione}
	 * @return Object type {@link it.govpay.orm.OperatoreApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public OperatoreApplicazione readOperatoreApplicazione(byte[] in) throws DeserializerException {
		return (OperatoreApplicazione) this.xmlToObj(in, OperatoreApplicazione.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.OperatoreApplicazione}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.OperatoreApplicazione}
	 * @return Object type {@link it.govpay.orm.OperatoreApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public OperatoreApplicazione readOperatoreApplicazioneFromString(String in) throws DeserializerException {
		return (OperatoreApplicazione) this.xmlToObj(in.getBytes(), OperatoreApplicazione.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-stazione
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdStazione}
	 * @return Object type {@link it.govpay.orm.IdStazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdStazione readIdStazione(String fileName) throws DeserializerException {
		return (IdStazione) this.xmlToObj(fileName, IdStazione.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdStazione}
	 * @return Object type {@link it.govpay.orm.IdStazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdStazione readIdStazione(File file) throws DeserializerException {
		return (IdStazione) this.xmlToObj(file, IdStazione.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdStazione}
	 * @return Object type {@link it.govpay.orm.IdStazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdStazione readIdStazione(InputStream in) throws DeserializerException {
		return (IdStazione) this.xmlToObj(in, IdStazione.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdStazione}
	 * @return Object type {@link it.govpay.orm.IdStazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdStazione readIdStazione(byte[] in) throws DeserializerException {
		return (IdStazione) this.xmlToObj(in, IdStazione.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdStazione}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdStazione}
	 * @return Object type {@link it.govpay.orm.IdStazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdStazione readIdStazioneFromString(String in) throws DeserializerException {
		return (IdStazione) this.xmlToObj(in.getBytes(), IdStazione.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Dominio
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Dominio}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Dominio}
	 * @return Object type {@link it.govpay.orm.Dominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Dominio readDominio(String fileName) throws DeserializerException {
		return (Dominio) this.xmlToObj(fileName, Dominio.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Dominio}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Dominio}
	 * @return Object type {@link it.govpay.orm.Dominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Dominio readDominio(File file) throws DeserializerException {
		return (Dominio) this.xmlToObj(file, Dominio.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Dominio}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Dominio}
	 * @return Object type {@link it.govpay.orm.Dominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Dominio readDominio(InputStream in) throws DeserializerException {
		return (Dominio) this.xmlToObj(in, Dominio.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Dominio}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Dominio}
	 * @return Object type {@link it.govpay.orm.Dominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Dominio readDominio(byte[] in) throws DeserializerException {
		return (Dominio) this.xmlToObj(in, Dominio.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Dominio}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Dominio}
	 * @return Object type {@link it.govpay.orm.Dominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Dominio readDominioFromString(String in) throws DeserializerException {
		return (Dominio) this.xmlToObj(in.getBytes(), Dominio.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-versamento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdVersamento}
	 * @return Object type {@link it.govpay.orm.IdVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdVersamento readIdVersamento(String fileName) throws DeserializerException {
		return (IdVersamento) this.xmlToObj(fileName, IdVersamento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdVersamento}
	 * @return Object type {@link it.govpay.orm.IdVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdVersamento readIdVersamento(File file) throws DeserializerException {
		return (IdVersamento) this.xmlToObj(file, IdVersamento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdVersamento}
	 * @return Object type {@link it.govpay.orm.IdVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdVersamento readIdVersamento(InputStream in) throws DeserializerException {
		return (IdVersamento) this.xmlToObj(in, IdVersamento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdVersamento}
	 * @return Object type {@link it.govpay.orm.IdVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdVersamento readIdVersamento(byte[] in) throws DeserializerException {
		return (IdVersamento) this.xmlToObj(in, IdVersamento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdVersamento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdVersamento}
	 * @return Object type {@link it.govpay.orm.IdVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdVersamento readIdVersamentoFromString(String in) throws DeserializerException {
		return (IdVersamento) this.xmlToObj(in.getBytes(), IdVersamento.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Carrello
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Carrello}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Carrello}
	 * @return Object type {@link it.govpay.orm.Carrello}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Carrello readCarrello(String fileName) throws DeserializerException {
		return (Carrello) this.xmlToObj(fileName, Carrello.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Carrello}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Carrello}
	 * @return Object type {@link it.govpay.orm.Carrello}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Carrello readCarrello(File file) throws DeserializerException {
		return (Carrello) this.xmlToObj(file, Carrello.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Carrello}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Carrello}
	 * @return Object type {@link it.govpay.orm.Carrello}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Carrello readCarrello(InputStream in) throws DeserializerException {
		return (Carrello) this.xmlToObj(in, Carrello.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Carrello}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Carrello}
	 * @return Object type {@link it.govpay.orm.Carrello}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Carrello readCarrello(byte[] in) throws DeserializerException {
		return (Carrello) this.xmlToObj(in, Carrello.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Carrello}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Carrello}
	 * @return Object type {@link it.govpay.orm.Carrello}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Carrello readCarrelloFromString(String in) throws DeserializerException {
		return (Carrello) this.xmlToObj(in.getBytes(), Carrello.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-iban-accredito
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdIbanAccredito}
	 * @return Object type {@link it.govpay.orm.IdIbanAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIbanAccredito readIdIbanAccredito(String fileName) throws DeserializerException {
		return (IdIbanAccredito) this.xmlToObj(fileName, IdIbanAccredito.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdIbanAccredito}
	 * @return Object type {@link it.govpay.orm.IdIbanAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIbanAccredito readIdIbanAccredito(File file) throws DeserializerException {
		return (IdIbanAccredito) this.xmlToObj(file, IdIbanAccredito.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdIbanAccredito}
	 * @return Object type {@link it.govpay.orm.IdIbanAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIbanAccredito readIdIbanAccredito(InputStream in) throws DeserializerException {
		return (IdIbanAccredito) this.xmlToObj(in, IdIbanAccredito.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdIbanAccredito}
	 * @return Object type {@link it.govpay.orm.IdIbanAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIbanAccredito readIdIbanAccredito(byte[] in) throws DeserializerException {
		return (IdIbanAccredito) this.xmlToObj(in, IdIbanAccredito.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdIbanAccredito}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdIbanAccredito}
	 * @return Object type {@link it.govpay.orm.IdIbanAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIbanAccredito readIdIbanAccreditoFromString(String in) throws DeserializerException {
		return (IdIbanAccredito) this.xmlToObj(in.getBytes(), IdIbanAccredito.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: MailTemplate
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.MailTemplate}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.MailTemplate}
	 * @return Object type {@link it.govpay.orm.MailTemplate}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public MailTemplate readMailTemplate(String fileName) throws DeserializerException {
		return (MailTemplate) this.xmlToObj(fileName, MailTemplate.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.MailTemplate}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.MailTemplate}
	 * @return Object type {@link it.govpay.orm.MailTemplate}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public MailTemplate readMailTemplate(File file) throws DeserializerException {
		return (MailTemplate) this.xmlToObj(file, MailTemplate.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.MailTemplate}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.MailTemplate}
	 * @return Object type {@link it.govpay.orm.MailTemplate}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public MailTemplate readMailTemplate(InputStream in) throws DeserializerException {
		return (MailTemplate) this.xmlToObj(in, MailTemplate.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.MailTemplate}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.MailTemplate}
	 * @return Object type {@link it.govpay.orm.MailTemplate}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public MailTemplate readMailTemplate(byte[] in) throws DeserializerException {
		return (MailTemplate) this.xmlToObj(in, MailTemplate.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.MailTemplate}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.MailTemplate}
	 * @return Object type {@link it.govpay.orm.MailTemplate}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public MailTemplate readMailTemplateFromString(String in) throws DeserializerException {
		return (MailTemplate) this.xmlToObj(in.getBytes(), MailTemplate.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-portale
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdPortale}
	 * @return Object type {@link it.govpay.orm.IdPortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPortale readIdPortale(String fileName) throws DeserializerException {
		return (IdPortale) this.xmlToObj(fileName, IdPortale.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdPortale}
	 * @return Object type {@link it.govpay.orm.IdPortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPortale readIdPortale(File file) throws DeserializerException {
		return (IdPortale) this.xmlToObj(file, IdPortale.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdPortale}
	 * @return Object type {@link it.govpay.orm.IdPortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPortale readIdPortale(InputStream in) throws DeserializerException {
		return (IdPortale) this.xmlToObj(in, IdPortale.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdPortale}
	 * @return Object type {@link it.govpay.orm.IdPortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPortale readIdPortale(byte[] in) throws DeserializerException {
		return (IdPortale) this.xmlToObj(in, IdPortale.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdPortale}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdPortale}
	 * @return Object type {@link it.govpay.orm.IdPortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPortale readIdPortaleFromString(String in) throws DeserializerException {
		return (IdPortale) this.xmlToObj(in.getBytes(), IdPortale.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Mail
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Mail}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Mail}
	 * @return Object type {@link it.govpay.orm.Mail}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Mail readMail(String fileName) throws DeserializerException {
		return (Mail) this.xmlToObj(fileName, Mail.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Mail}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Mail}
	 * @return Object type {@link it.govpay.orm.Mail}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Mail readMail(File file) throws DeserializerException {
		return (Mail) this.xmlToObj(file, Mail.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Mail}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Mail}
	 * @return Object type {@link it.govpay.orm.Mail}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Mail readMail(InputStream in) throws DeserializerException {
		return (Mail) this.xmlToObj(in, Mail.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Mail}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Mail}
	 * @return Object type {@link it.govpay.orm.Mail}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Mail readMail(byte[] in) throws DeserializerException {
		return (Mail) this.xmlToObj(in, Mail.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Mail}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Mail}
	 * @return Object type {@link it.govpay.orm.Mail}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Mail readMailFromString(String in) throws DeserializerException {
		return (Mail) this.xmlToObj(in.getBytes(), Mail.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: ER
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.ER}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.ER}
	 * @return Object type {@link it.govpay.orm.ER}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ER readER(String fileName) throws DeserializerException {
		return (ER) this.xmlToObj(fileName, ER.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.ER}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.ER}
	 * @return Object type {@link it.govpay.orm.ER}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ER readER(File file) throws DeserializerException {
		return (ER) this.xmlToObj(file, ER.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.ER}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.ER}
	 * @return Object type {@link it.govpay.orm.ER}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ER readER(InputStream in) throws DeserializerException {
		return (ER) this.xmlToObj(in, ER.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.ER}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.ER}
	 * @return Object type {@link it.govpay.orm.ER}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ER readER(byte[] in) throws DeserializerException {
		return (ER) this.xmlToObj(in, ER.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.ER}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.ER}
	 * @return Object type {@link it.govpay.orm.ER}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ER readERFromString(String in) throws DeserializerException {
		return (ER) this.xmlToObj(in.getBytes(), ER.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Evento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Evento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Evento}
	 * @return Object type {@link it.govpay.orm.Evento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Evento readEvento(String fileName) throws DeserializerException {
		return (Evento) this.xmlToObj(fileName, Evento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Evento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Evento}
	 * @return Object type {@link it.govpay.orm.Evento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Evento readEvento(File file) throws DeserializerException {
		return (Evento) this.xmlToObj(file, Evento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Evento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Evento}
	 * @return Object type {@link it.govpay.orm.Evento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Evento readEvento(InputStream in) throws DeserializerException {
		return (Evento) this.xmlToObj(in, Evento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Evento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Evento}
	 * @return Object type {@link it.govpay.orm.Evento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Evento readEvento(byte[] in) throws DeserializerException {
		return (Evento) this.xmlToObj(in, Evento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Evento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Evento}
	 * @return Object type {@link it.govpay.orm.Evento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Evento readEventoFromString(String in) throws DeserializerException {
		return (Evento) this.xmlToObj(in.getBytes(), Evento.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: PortaleApplicazione
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.PortaleApplicazione}
	 * @return Object type {@link it.govpay.orm.PortaleApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public PortaleApplicazione readPortaleApplicazione(String fileName) throws DeserializerException {
		return (PortaleApplicazione) this.xmlToObj(fileName, PortaleApplicazione.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.PortaleApplicazione}
	 * @return Object type {@link it.govpay.orm.PortaleApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public PortaleApplicazione readPortaleApplicazione(File file) throws DeserializerException {
		return (PortaleApplicazione) this.xmlToObj(file, PortaleApplicazione.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.PortaleApplicazione}
	 * @return Object type {@link it.govpay.orm.PortaleApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public PortaleApplicazione readPortaleApplicazione(InputStream in) throws DeserializerException {
		return (PortaleApplicazione) this.xmlToObj(in, PortaleApplicazione.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.PortaleApplicazione}
	 * @return Object type {@link it.govpay.orm.PortaleApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public PortaleApplicazione readPortaleApplicazione(byte[] in) throws DeserializerException {
		return (PortaleApplicazione) this.xmlToObj(in, PortaleApplicazione.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.PortaleApplicazione}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.PortaleApplicazione}
	 * @return Object type {@link it.govpay.orm.PortaleApplicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public PortaleApplicazione readPortaleApplicazioneFromString(String in) throws DeserializerException {
		return (PortaleApplicazione) this.xmlToObj(in.getBytes(), PortaleApplicazione.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-tributo
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdTributo}
	 * @return Object type {@link it.govpay.orm.IdTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTributo readIdTributo(String fileName) throws DeserializerException {
		return (IdTributo) this.xmlToObj(fileName, IdTributo.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdTributo}
	 * @return Object type {@link it.govpay.orm.IdTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTributo readIdTributo(File file) throws DeserializerException {
		return (IdTributo) this.xmlToObj(file, IdTributo.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdTributo}
	 * @return Object type {@link it.govpay.orm.IdTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTributo readIdTributo(InputStream in) throws DeserializerException {
		return (IdTributo) this.xmlToObj(in, IdTributo.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdTributo}
	 * @return Object type {@link it.govpay.orm.IdTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTributo readIdTributo(byte[] in) throws DeserializerException {
		return (IdTributo) this.xmlToObj(in, IdTributo.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdTributo}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdTributo}
	 * @return Object type {@link it.govpay.orm.IdTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTributo readIdTributoFromString(String in) throws DeserializerException {
		return (IdTributo) this.xmlToObj(in.getBytes(), IdTributo.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-tabella-controparti
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdTabellaControparti}
	 * @return Object type {@link it.govpay.orm.IdTabellaControparti}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTabellaControparti readIdTabellaControparti(String fileName) throws DeserializerException {
		return (IdTabellaControparti) this.xmlToObj(fileName, IdTabellaControparti.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdTabellaControparti}
	 * @return Object type {@link it.govpay.orm.IdTabellaControparti}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTabellaControparti readIdTabellaControparti(File file) throws DeserializerException {
		return (IdTabellaControparti) this.xmlToObj(file, IdTabellaControparti.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdTabellaControparti}
	 * @return Object type {@link it.govpay.orm.IdTabellaControparti}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTabellaControparti readIdTabellaControparti(InputStream in) throws DeserializerException {
		return (IdTabellaControparti) this.xmlToObj(in, IdTabellaControparti.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdTabellaControparti}
	 * @return Object type {@link it.govpay.orm.IdTabellaControparti}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTabellaControparti readIdTabellaControparti(byte[] in) throws DeserializerException {
		return (IdTabellaControparti) this.xmlToObj(in, IdTabellaControparti.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdTabellaControparti}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdTabellaControparti}
	 * @return Object type {@link it.govpay.orm.IdTabellaControparti}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTabellaControparti readIdTabellaContropartiFromString(String in) throws DeserializerException {
		return (IdTabellaControparti) this.xmlToObj(in.getBytes(), IdTabellaControparti.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-conto-accredito
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdContoAccredito}
	 * @return Object type {@link it.govpay.orm.IdContoAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdContoAccredito readIdContoAccredito(String fileName) throws DeserializerException {
		return (IdContoAccredito) this.xmlToObj(fileName, IdContoAccredito.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdContoAccredito}
	 * @return Object type {@link it.govpay.orm.IdContoAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdContoAccredito readIdContoAccredito(File file) throws DeserializerException {
		return (IdContoAccredito) this.xmlToObj(file, IdContoAccredito.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdContoAccredito}
	 * @return Object type {@link it.govpay.orm.IdContoAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdContoAccredito readIdContoAccredito(InputStream in) throws DeserializerException {
		return (IdContoAccredito) this.xmlToObj(in, IdContoAccredito.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdContoAccredito}
	 * @return Object type {@link it.govpay.orm.IdContoAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdContoAccredito readIdContoAccredito(byte[] in) throws DeserializerException {
		return (IdContoAccredito) this.xmlToObj(in, IdContoAccredito.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdContoAccredito}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdContoAccredito}
	 * @return Object type {@link it.govpay.orm.IdContoAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdContoAccredito readIdContoAccreditoFromString(String in) throws DeserializerException {
		return (IdContoAccredito) this.xmlToObj(in.getBytes(), IdContoAccredito.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Tributo
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Tributo}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Tributo}
	 * @return Object type {@link it.govpay.orm.Tributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Tributo readTributo(String fileName) throws DeserializerException {
		return (Tributo) this.xmlToObj(fileName, Tributo.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Tributo}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Tributo}
	 * @return Object type {@link it.govpay.orm.Tributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Tributo readTributo(File file) throws DeserializerException {
		return (Tributo) this.xmlToObj(file, Tributo.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Tributo}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Tributo}
	 * @return Object type {@link it.govpay.orm.Tributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Tributo readTributo(InputStream in) throws DeserializerException {
		return (Tributo) this.xmlToObj(in, Tributo.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Tributo}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Tributo}
	 * @return Object type {@link it.govpay.orm.Tributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Tributo readTributo(byte[] in) throws DeserializerException {
		return (Tributo) this.xmlToObj(in, Tributo.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Tributo}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Tributo}
	 * @return Object type {@link it.govpay.orm.Tributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Tributo readTributoFromString(String in) throws DeserializerException {
		return (Tributo) this.xmlToObj(in.getBytes(), Tributo.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: IUV
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IUV}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IUV}
	 * @return Object type {@link it.govpay.orm.IUV}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IUV readIUV(String fileName) throws DeserializerException {
		return (IUV) this.xmlToObj(fileName, IUV.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IUV}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IUV}
	 * @return Object type {@link it.govpay.orm.IUV}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IUV readIUV(File file) throws DeserializerException {
		return (IUV) this.xmlToObj(file, IUV.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IUV}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IUV}
	 * @return Object type {@link it.govpay.orm.IUV}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IUV readIUV(InputStream in) throws DeserializerException {
		return (IUV) this.xmlToObj(in, IUV.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IUV}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IUV}
	 * @return Object type {@link it.govpay.orm.IUV}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IUV readIUV(byte[] in) throws DeserializerException {
		return (IUV) this.xmlToObj(in, IUV.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IUV}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IUV}
	 * @return Object type {@link it.govpay.orm.IUV}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IUV readIUVFromString(String in) throws DeserializerException {
		return (IUV) this.xmlToObj(in.getBytes(), IUV.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-connettore
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdConnettore}
	 * @return Object type {@link it.govpay.orm.IdConnettore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdConnettore readIdConnettore(String fileName) throws DeserializerException {
		return (IdConnettore) this.xmlToObj(fileName, IdConnettore.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdConnettore}
	 * @return Object type {@link it.govpay.orm.IdConnettore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdConnettore readIdConnettore(File file) throws DeserializerException {
		return (IdConnettore) this.xmlToObj(file, IdConnettore.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdConnettore}
	 * @return Object type {@link it.govpay.orm.IdConnettore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdConnettore readIdConnettore(InputStream in) throws DeserializerException {
		return (IdConnettore) this.xmlToObj(in, IdConnettore.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdConnettore}
	 * @return Object type {@link it.govpay.orm.IdConnettore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdConnettore readIdConnettore(byte[] in) throws DeserializerException {
		return (IdConnettore) this.xmlToObj(in, IdConnettore.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdConnettore}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdConnettore}
	 * @return Object type {@link it.govpay.orm.IdConnettore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdConnettore readIdConnettoreFromString(String in) throws DeserializerException {
		return (IdConnettore) this.xmlToObj(in.getBytes(), IdConnettore.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Portale
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Portale}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Portale}
	 * @return Object type {@link it.govpay.orm.Portale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Portale readPortale(String fileName) throws DeserializerException {
		return (Portale) this.xmlToObj(fileName, Portale.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Portale}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Portale}
	 * @return Object type {@link it.govpay.orm.Portale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Portale readPortale(File file) throws DeserializerException {
		return (Portale) this.xmlToObj(file, Portale.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Portale}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Portale}
	 * @return Object type {@link it.govpay.orm.Portale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Portale readPortale(InputStream in) throws DeserializerException {
		return (Portale) this.xmlToObj(in, Portale.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Portale}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Portale}
	 * @return Object type {@link it.govpay.orm.Portale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Portale readPortale(byte[] in) throws DeserializerException {
		return (Portale) this.xmlToObj(in, Portale.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Portale}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Portale}
	 * @return Object type {@link it.govpay.orm.Portale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Portale readPortaleFromString(String in) throws DeserializerException {
		return (Portale) this.xmlToObj(in.getBytes(), Portale.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: ContoAccredito
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.ContoAccredito}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.ContoAccredito}
	 * @return Object type {@link it.govpay.orm.ContoAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ContoAccredito readContoAccredito(String fileName) throws DeserializerException {
		return (ContoAccredito) this.xmlToObj(fileName, ContoAccredito.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.ContoAccredito}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.ContoAccredito}
	 * @return Object type {@link it.govpay.orm.ContoAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ContoAccredito readContoAccredito(File file) throws DeserializerException {
		return (ContoAccredito) this.xmlToObj(file, ContoAccredito.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.ContoAccredito}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.ContoAccredito}
	 * @return Object type {@link it.govpay.orm.ContoAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ContoAccredito readContoAccredito(InputStream in) throws DeserializerException {
		return (ContoAccredito) this.xmlToObj(in, ContoAccredito.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.ContoAccredito}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.ContoAccredito}
	 * @return Object type {@link it.govpay.orm.ContoAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ContoAccredito readContoAccredito(byte[] in) throws DeserializerException {
		return (ContoAccredito) this.xmlToObj(in, ContoAccredito.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.ContoAccredito}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.ContoAccredito}
	 * @return Object type {@link it.govpay.orm.ContoAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ContoAccredito readContoAccreditoFromString(String in) throws DeserializerException {
		return (ContoAccredito) this.xmlToObj(in.getBytes(), ContoAccredito.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Connettore
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Connettore}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Connettore}
	 * @return Object type {@link it.govpay.orm.Connettore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Connettore readConnettore(String fileName) throws DeserializerException {
		return (Connettore) this.xmlToObj(fileName, Connettore.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Connettore}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Connettore}
	 * @return Object type {@link it.govpay.orm.Connettore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Connettore readConnettore(File file) throws DeserializerException {
		return (Connettore) this.xmlToObj(file, Connettore.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Connettore}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Connettore}
	 * @return Object type {@link it.govpay.orm.Connettore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Connettore readConnettore(InputStream in) throws DeserializerException {
		return (Connettore) this.xmlToObj(in, Connettore.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Connettore}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Connettore}
	 * @return Object type {@link it.govpay.orm.Connettore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Connettore readConnettore(byte[] in) throws DeserializerException {
		return (Connettore) this.xmlToObj(in, Connettore.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Connettore}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Connettore}
	 * @return Object type {@link it.govpay.orm.Connettore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Connettore readConnettoreFromString(String in) throws DeserializerException {
		return (Connettore) this.xmlToObj(in.getBytes(), Connettore.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: ApplicazioneTributo
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.ApplicazioneTributo}
	 * @return Object type {@link it.govpay.orm.ApplicazioneTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ApplicazioneTributo readApplicazioneTributo(String fileName) throws DeserializerException {
		return (ApplicazioneTributo) this.xmlToObj(fileName, ApplicazioneTributo.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.ApplicazioneTributo}
	 * @return Object type {@link it.govpay.orm.ApplicazioneTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ApplicazioneTributo readApplicazioneTributo(File file) throws DeserializerException {
		return (ApplicazioneTributo) this.xmlToObj(file, ApplicazioneTributo.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.ApplicazioneTributo}
	 * @return Object type {@link it.govpay.orm.ApplicazioneTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ApplicazioneTributo readApplicazioneTributo(InputStream in) throws DeserializerException {
		return (ApplicazioneTributo) this.xmlToObj(in, ApplicazioneTributo.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.ApplicazioneTributo}
	 * @return Object type {@link it.govpay.orm.ApplicazioneTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ApplicazioneTributo readApplicazioneTributo(byte[] in) throws DeserializerException {
		return (ApplicazioneTributo) this.xmlToObj(in, ApplicazioneTributo.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.ApplicazioneTributo}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.ApplicazioneTributo}
	 * @return Object type {@link it.govpay.orm.ApplicazioneTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ApplicazioneTributo readApplicazioneTributoFromString(String in) throws DeserializerException {
		return (ApplicazioneTributo) this.xmlToObj(in.getBytes(), ApplicazioneTributo.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: MediaRilevamento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.MediaRilevamento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.MediaRilevamento}
	 * @return Object type {@link it.govpay.orm.MediaRilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public MediaRilevamento readMediaRilevamento(String fileName) throws DeserializerException {
		return (MediaRilevamento) this.xmlToObj(fileName, MediaRilevamento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.MediaRilevamento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.MediaRilevamento}
	 * @return Object type {@link it.govpay.orm.MediaRilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public MediaRilevamento readMediaRilevamento(File file) throws DeserializerException {
		return (MediaRilevamento) this.xmlToObj(file, MediaRilevamento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.MediaRilevamento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.MediaRilevamento}
	 * @return Object type {@link it.govpay.orm.MediaRilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public MediaRilevamento readMediaRilevamento(InputStream in) throws DeserializerException {
		return (MediaRilevamento) this.xmlToObj(in, MediaRilevamento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.MediaRilevamento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.MediaRilevamento}
	 * @return Object type {@link it.govpay.orm.MediaRilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public MediaRilevamento readMediaRilevamento(byte[] in) throws DeserializerException {
		return (MediaRilevamento) this.xmlToObj(in, MediaRilevamento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.MediaRilevamento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.MediaRilevamento}
	 * @return Object type {@link it.govpay.orm.MediaRilevamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public MediaRilevamento readMediaRilevamentoFromString(String in) throws DeserializerException {
		return (MediaRilevamento) this.xmlToObj(in.getBytes(), MediaRilevamento.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: SLA
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.SLA}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.SLA}
	 * @return Object type {@link it.govpay.orm.SLA}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public SLA readSLA(String fileName) throws DeserializerException {
		return (SLA) this.xmlToObj(fileName, SLA.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.SLA}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.SLA}
	 * @return Object type {@link it.govpay.orm.SLA}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public SLA readSLA(File file) throws DeserializerException {
		return (SLA) this.xmlToObj(file, SLA.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.SLA}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.SLA}
	 * @return Object type {@link it.govpay.orm.SLA}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public SLA readSLA(InputStream in) throws DeserializerException {
		return (SLA) this.xmlToObj(in, SLA.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.SLA}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.SLA}
	 * @return Object type {@link it.govpay.orm.SLA}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public SLA readSLA(byte[] in) throws DeserializerException {
		return (SLA) this.xmlToObj(in, SLA.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.SLA}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.SLA}
	 * @return Object type {@link it.govpay.orm.SLA}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public SLA readSLAFromString(String in) throws DeserializerException {
		return (SLA) this.xmlToObj(in.getBytes(), SLA.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-fr
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdFr}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdFr}
	 * @return Object type {@link it.govpay.orm.IdFr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdFr readIdFr(String fileName) throws DeserializerException {
		return (IdFr) this.xmlToObj(fileName, IdFr.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdFr}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdFr}
	 * @return Object type {@link it.govpay.orm.IdFr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdFr readIdFr(File file) throws DeserializerException {
		return (IdFr) this.xmlToObj(file, IdFr.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdFr}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdFr}
	 * @return Object type {@link it.govpay.orm.IdFr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdFr readIdFr(InputStream in) throws DeserializerException {
		return (IdFr) this.xmlToObj(in, IdFr.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdFr}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdFr}
	 * @return Object type {@link it.govpay.orm.IdFr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdFr readIdFr(byte[] in) throws DeserializerException {
		return (IdFr) this.xmlToObj(in, IdFr.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdFr}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdFr}
	 * @return Object type {@link it.govpay.orm.IdFr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdFr readIdFrFromString(String in) throws DeserializerException {
		return (IdFr) this.xmlToObj(in.getBytes(), IdFr.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: SingolaRendicontazione
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.SingolaRendicontazione}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.SingolaRendicontazione}
	 * @return Object type {@link it.govpay.orm.SingolaRendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public SingolaRendicontazione readSingolaRendicontazione(String fileName) throws DeserializerException {
		return (SingolaRendicontazione) this.xmlToObj(fileName, SingolaRendicontazione.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.SingolaRendicontazione}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.SingolaRendicontazione}
	 * @return Object type {@link it.govpay.orm.SingolaRendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public SingolaRendicontazione readSingolaRendicontazione(File file) throws DeserializerException {
		return (SingolaRendicontazione) this.xmlToObj(file, SingolaRendicontazione.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.SingolaRendicontazione}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.SingolaRendicontazione}
	 * @return Object type {@link it.govpay.orm.SingolaRendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public SingolaRendicontazione readSingolaRendicontazione(InputStream in) throws DeserializerException {
		return (SingolaRendicontazione) this.xmlToObj(in, SingolaRendicontazione.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.SingolaRendicontazione}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.SingolaRendicontazione}
	 * @return Object type {@link it.govpay.orm.SingolaRendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public SingolaRendicontazione readSingolaRendicontazione(byte[] in) throws DeserializerException {
		return (SingolaRendicontazione) this.xmlToObj(in, SingolaRendicontazione.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.SingolaRendicontazione}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.SingolaRendicontazione}
	 * @return Object type {@link it.govpay.orm.SingolaRendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public SingolaRendicontazione readSingolaRendicontazioneFromString(String in) throws DeserializerException {
		return (SingolaRendicontazione) this.xmlToObj(in.getBytes(), SingolaRendicontazione.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-singola-rendicontazione
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * @return Object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingolaRendicontazione readIdSingolaRendicontazione(String fileName) throws DeserializerException {
		return (IdSingolaRendicontazione) this.xmlToObj(fileName, IdSingolaRendicontazione.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * @return Object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingolaRendicontazione readIdSingolaRendicontazione(File file) throws DeserializerException {
		return (IdSingolaRendicontazione) this.xmlToObj(file, IdSingolaRendicontazione.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * @return Object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingolaRendicontazione readIdSingolaRendicontazione(InputStream in) throws DeserializerException {
		return (IdSingolaRendicontazione) this.xmlToObj(in, IdSingolaRendicontazione.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * @return Object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingolaRendicontazione readIdSingolaRendicontazione(byte[] in) throws DeserializerException {
		return (IdSingolaRendicontazione) this.xmlToObj(in, IdSingolaRendicontazione.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * @return Object type {@link it.govpay.orm.IdSingolaRendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingolaRendicontazione readIdSingolaRendicontazioneFromString(String in) throws DeserializerException {
		return (IdSingolaRendicontazione) this.xmlToObj(in.getBytes(), IdSingolaRendicontazione.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: TracciatoXML
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.TracciatoXML}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.TracciatoXML}
	 * @return Object type {@link it.govpay.orm.TracciatoXML}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TracciatoXML readTracciatoXML(String fileName) throws DeserializerException {
		return (TracciatoXML) this.xmlToObj(fileName, TracciatoXML.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.TracciatoXML}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.TracciatoXML}
	 * @return Object type {@link it.govpay.orm.TracciatoXML}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TracciatoXML readTracciatoXML(File file) throws DeserializerException {
		return (TracciatoXML) this.xmlToObj(file, TracciatoXML.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.TracciatoXML}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.TracciatoXML}
	 * @return Object type {@link it.govpay.orm.TracciatoXML}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TracciatoXML readTracciatoXML(InputStream in) throws DeserializerException {
		return (TracciatoXML) this.xmlToObj(in, TracciatoXML.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.TracciatoXML}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.TracciatoXML}
	 * @return Object type {@link it.govpay.orm.TracciatoXML}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TracciatoXML readTracciatoXML(byte[] in) throws DeserializerException {
		return (TracciatoXML) this.xmlToObj(in, TracciatoXML.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.TracciatoXML}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.TracciatoXML}
	 * @return Object type {@link it.govpay.orm.TracciatoXML}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TracciatoXML readTracciatoXMLFromString(String in) throws DeserializerException {
		return (TracciatoXML) this.xmlToObj(in.getBytes(), TracciatoXML.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: IbanAccredito
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IbanAccredito}
	 * @return Object type {@link it.govpay.orm.IbanAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IbanAccredito readIbanAccredito(String fileName) throws DeserializerException {
		return (IbanAccredito) this.xmlToObj(fileName, IbanAccredito.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IbanAccredito}
	 * @return Object type {@link it.govpay.orm.IbanAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IbanAccredito readIbanAccredito(File file) throws DeserializerException {
		return (IbanAccredito) this.xmlToObj(file, IbanAccredito.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IbanAccredito}
	 * @return Object type {@link it.govpay.orm.IbanAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IbanAccredito readIbanAccredito(InputStream in) throws DeserializerException {
		return (IbanAccredito) this.xmlToObj(in, IbanAccredito.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IbanAccredito}
	 * @return Object type {@link it.govpay.orm.IbanAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IbanAccredito readIbanAccredito(byte[] in) throws DeserializerException {
		return (IbanAccredito) this.xmlToObj(in, IbanAccredito.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IbanAccredito}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IbanAccredito}
	 * @return Object type {@link it.govpay.orm.IbanAccredito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IbanAccredito readIbanAccreditoFromString(String in) throws DeserializerException {
		return (IbanAccredito) this.xmlToObj(in.getBytes(), IbanAccredito.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-mail-template
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdMailTemplate}
	 * @return Object type {@link it.govpay.orm.IdMailTemplate}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMailTemplate readIdMailTemplate(String fileName) throws DeserializerException {
		return (IdMailTemplate) this.xmlToObj(fileName, IdMailTemplate.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdMailTemplate}
	 * @return Object type {@link it.govpay.orm.IdMailTemplate}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMailTemplate readIdMailTemplate(File file) throws DeserializerException {
		return (IdMailTemplate) this.xmlToObj(file, IdMailTemplate.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdMailTemplate}
	 * @return Object type {@link it.govpay.orm.IdMailTemplate}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMailTemplate readIdMailTemplate(InputStream in) throws DeserializerException {
		return (IdMailTemplate) this.xmlToObj(in, IdMailTemplate.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdMailTemplate}
	 * @return Object type {@link it.govpay.orm.IdMailTemplate}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMailTemplate readIdMailTemplate(byte[] in) throws DeserializerException {
		return (IdMailTemplate) this.xmlToObj(in, IdMailTemplate.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdMailTemplate}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdMailTemplate}
	 * @return Object type {@link it.govpay.orm.IdMailTemplate}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMailTemplate readIdMailTemplateFromString(String in) throws DeserializerException {
		return (IdMailTemplate) this.xmlToObj(in.getBytes(), IdMailTemplate.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-carrello
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdCarrello}
	 * @return Object type {@link it.govpay.orm.IdCarrello}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdCarrello readIdCarrello(String fileName) throws DeserializerException {
		return (IdCarrello) this.xmlToObj(fileName, IdCarrello.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdCarrello}
	 * @return Object type {@link it.govpay.orm.IdCarrello}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdCarrello readIdCarrello(File file) throws DeserializerException {
		return (IdCarrello) this.xmlToObj(file, IdCarrello.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdCarrello}
	 * @return Object type {@link it.govpay.orm.IdCarrello}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdCarrello readIdCarrello(InputStream in) throws DeserializerException {
		return (IdCarrello) this.xmlToObj(in, IdCarrello.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdCarrello}
	 * @return Object type {@link it.govpay.orm.IdCarrello}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdCarrello readIdCarrello(byte[] in) throws DeserializerException {
		return (IdCarrello) this.xmlToObj(in, IdCarrello.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdCarrello}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdCarrello}
	 * @return Object type {@link it.govpay.orm.IdCarrello}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdCarrello readIdCarrelloFromString(String in) throws DeserializerException {
		return (IdCarrello) this.xmlToObj(in.getBytes(), IdCarrello.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-messaggio
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdMessaggio}
	 * @return Object type {@link it.govpay.orm.IdMessaggio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMessaggio readIdMessaggio(String fileName) throws DeserializerException {
		return (IdMessaggio) this.xmlToObj(fileName, IdMessaggio.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdMessaggio}
	 * @return Object type {@link it.govpay.orm.IdMessaggio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMessaggio readIdMessaggio(File file) throws DeserializerException {
		return (IdMessaggio) this.xmlToObj(file, IdMessaggio.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdMessaggio}
	 * @return Object type {@link it.govpay.orm.IdMessaggio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMessaggio readIdMessaggio(InputStream in) throws DeserializerException {
		return (IdMessaggio) this.xmlToObj(in, IdMessaggio.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdMessaggio}
	 * @return Object type {@link it.govpay.orm.IdMessaggio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMessaggio readIdMessaggio(byte[] in) throws DeserializerException {
		return (IdMessaggio) this.xmlToObj(in, IdMessaggio.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdMessaggio}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdMessaggio}
	 * @return Object type {@link it.govpay.orm.IdMessaggio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMessaggio readIdMessaggioFromString(String in) throws DeserializerException {
		return (IdMessaggio) this.xmlToObj(in.getBytes(), IdMessaggio.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-singola-revoca
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdSingolaRevoca}
	 * @return Object type {@link it.govpay.orm.IdSingolaRevoca}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingolaRevoca readIdSingolaRevoca(String fileName) throws DeserializerException {
		return (IdSingolaRevoca) this.xmlToObj(fileName, IdSingolaRevoca.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdSingolaRevoca}
	 * @return Object type {@link it.govpay.orm.IdSingolaRevoca}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingolaRevoca readIdSingolaRevoca(File file) throws DeserializerException {
		return (IdSingolaRevoca) this.xmlToObj(file, IdSingolaRevoca.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdSingolaRevoca}
	 * @return Object type {@link it.govpay.orm.IdSingolaRevoca}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingolaRevoca readIdSingolaRevoca(InputStream in) throws DeserializerException {
		return (IdSingolaRevoca) this.xmlToObj(in, IdSingolaRevoca.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdSingolaRevoca}
	 * @return Object type {@link it.govpay.orm.IdSingolaRevoca}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingolaRevoca readIdSingolaRevoca(byte[] in) throws DeserializerException {
		return (IdSingolaRevoca) this.xmlToObj(in, IdSingolaRevoca.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdSingolaRevoca}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdSingolaRevoca}
	 * @return Object type {@link it.govpay.orm.IdSingolaRevoca}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdSingolaRevoca readIdSingolaRevocaFromString(String in) throws DeserializerException {
		return (IdSingolaRevoca) this.xmlToObj(in.getBytes(), IdSingolaRevoca.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-mail
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdMail}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdMail}
	 * @return Object type {@link it.govpay.orm.IdMail}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMail readIdMail(String fileName) throws DeserializerException {
		return (IdMail) this.xmlToObj(fileName, IdMail.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdMail}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdMail}
	 * @return Object type {@link it.govpay.orm.IdMail}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMail readIdMail(File file) throws DeserializerException {
		return (IdMail) this.xmlToObj(file, IdMail.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdMail}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdMail}
	 * @return Object type {@link it.govpay.orm.IdMail}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMail readIdMail(InputStream in) throws DeserializerException {
		return (IdMail) this.xmlToObj(in, IdMail.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdMail}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdMail}
	 * @return Object type {@link it.govpay.orm.IdMail}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMail readIdMail(byte[] in) throws DeserializerException {
		return (IdMail) this.xmlToObj(in, IdMail.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdMail}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdMail}
	 * @return Object type {@link it.govpay.orm.IdMail}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdMail readIdMailFromString(String in) throws DeserializerException {
		return (IdMail) this.xmlToObj(in.getBytes(), IdMail.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Versamento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Versamento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Versamento}
	 * @return Object type {@link it.govpay.orm.Versamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Versamento readVersamento(String fileName) throws DeserializerException {
		return (Versamento) this.xmlToObj(fileName, Versamento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Versamento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Versamento}
	 * @return Object type {@link it.govpay.orm.Versamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Versamento readVersamento(File file) throws DeserializerException {
		return (Versamento) this.xmlToObj(file, Versamento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Versamento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Versamento}
	 * @return Object type {@link it.govpay.orm.Versamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Versamento readVersamento(InputStream in) throws DeserializerException {
		return (Versamento) this.xmlToObj(in, Versamento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Versamento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Versamento}
	 * @return Object type {@link it.govpay.orm.Versamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Versamento readVersamento(byte[] in) throws DeserializerException {
		return (Versamento) this.xmlToObj(in, Versamento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Versamento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Versamento}
	 * @return Object type {@link it.govpay.orm.Versamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Versamento readVersamentoFromString(String in) throws DeserializerException {
		return (Versamento) this.xmlToObj(in.getBytes(), Versamento.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-esito
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdEsito}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdEsito}
	 * @return Object type {@link it.govpay.orm.IdEsito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEsito readIdEsito(String fileName) throws DeserializerException {
		return (IdEsito) this.xmlToObj(fileName, IdEsito.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdEsito}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdEsito}
	 * @return Object type {@link it.govpay.orm.IdEsito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEsito readIdEsito(File file) throws DeserializerException {
		return (IdEsito) this.xmlToObj(file, IdEsito.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdEsito}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdEsito}
	 * @return Object type {@link it.govpay.orm.IdEsito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEsito readIdEsito(InputStream in) throws DeserializerException {
		return (IdEsito) this.xmlToObj(in, IdEsito.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdEsito}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdEsito}
	 * @return Object type {@link it.govpay.orm.IdEsito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEsito readIdEsito(byte[] in) throws DeserializerException {
		return (IdEsito) this.xmlToObj(in, IdEsito.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdEsito}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdEsito}
	 * @return Object type {@link it.govpay.orm.IdEsito}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEsito readIdEsitoFromString(String in) throws DeserializerException {
		return (IdEsito) this.xmlToObj(in.getBytes(), IdEsito.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: SingoloVersamento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.SingoloVersamento}
	 * @return Object type {@link it.govpay.orm.SingoloVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public SingoloVersamento readSingoloVersamento(String fileName) throws DeserializerException {
		return (SingoloVersamento) this.xmlToObj(fileName, SingoloVersamento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.SingoloVersamento}
	 * @return Object type {@link it.govpay.orm.SingoloVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public SingoloVersamento readSingoloVersamento(File file) throws DeserializerException {
		return (SingoloVersamento) this.xmlToObj(file, SingoloVersamento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.SingoloVersamento}
	 * @return Object type {@link it.govpay.orm.SingoloVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public SingoloVersamento readSingoloVersamento(InputStream in) throws DeserializerException {
		return (SingoloVersamento) this.xmlToObj(in, SingoloVersamento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.SingoloVersamento}
	 * @return Object type {@link it.govpay.orm.SingoloVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public SingoloVersamento readSingoloVersamento(byte[] in) throws DeserializerException {
		return (SingoloVersamento) this.xmlToObj(in, SingoloVersamento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.SingoloVersamento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.SingoloVersamento}
	 * @return Object type {@link it.govpay.orm.SingoloVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public SingoloVersamento readSingoloVersamentoFromString(String in) throws DeserializerException {
		return (SingoloVersamento) this.xmlToObj(in.getBytes(), SingoloVersamento.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-iuv
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdIuv}
	 * @return Object type {@link it.govpay.orm.IdIuv}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIuv readIdIuv(String fileName) throws DeserializerException {
		return (IdIuv) this.xmlToObj(fileName, IdIuv.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdIuv}
	 * @return Object type {@link it.govpay.orm.IdIuv}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIuv readIdIuv(File file) throws DeserializerException {
		return (IdIuv) this.xmlToObj(file, IdIuv.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdIuv}
	 * @return Object type {@link it.govpay.orm.IdIuv}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIuv readIdIuv(InputStream in) throws DeserializerException {
		return (IdIuv) this.xmlToObj(in, IdIuv.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdIuv}
	 * @return Object type {@link it.govpay.orm.IdIuv}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIuv readIdIuv(byte[] in) throws DeserializerException {
		return (IdIuv) this.xmlToObj(in, IdIuv.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdIuv}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdIuv}
	 * @return Object type {@link it.govpay.orm.IdIuv}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIuv readIdIuvFromString(String in) throws DeserializerException {
		return (IdIuv) this.xmlToObj(in.getBytes(), IdIuv.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-er
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdEr}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdEr}
	 * @return Object type {@link it.govpay.orm.IdEr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEr readIdEr(String fileName) throws DeserializerException {
		return (IdEr) this.xmlToObj(fileName, IdEr.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdEr}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdEr}
	 * @return Object type {@link it.govpay.orm.IdEr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEr readIdEr(File file) throws DeserializerException {
		return (IdEr) this.xmlToObj(file, IdEr.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdEr}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdEr}
	 * @return Object type {@link it.govpay.orm.IdEr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEr readIdEr(InputStream in) throws DeserializerException {
		return (IdEr) this.xmlToObj(in, IdEr.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdEr}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdEr}
	 * @return Object type {@link it.govpay.orm.IdEr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEr readIdEr(byte[] in) throws DeserializerException {
		return (IdEr) this.xmlToObj(in, IdEr.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdEr}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdEr}
	 * @return Object type {@link it.govpay.orm.IdEr}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdEr readIdErFromString(String in) throws DeserializerException {
		return (IdEr) this.xmlToObj(in.getBytes(), IdEr.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Applicazione
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Applicazione}
	 * @return Object type {@link it.govpay.orm.Applicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Applicazione readApplicazione(String fileName) throws DeserializerException {
		return (Applicazione) this.xmlToObj(fileName, Applicazione.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Applicazione}
	 * @return Object type {@link it.govpay.orm.Applicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Applicazione readApplicazione(File file) throws DeserializerException {
		return (Applicazione) this.xmlToObj(file, Applicazione.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Applicazione}
	 * @return Object type {@link it.govpay.orm.Applicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Applicazione readApplicazione(InputStream in) throws DeserializerException {
		return (Applicazione) this.xmlToObj(in, Applicazione.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Applicazione}
	 * @return Object type {@link it.govpay.orm.Applicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Applicazione readApplicazione(byte[] in) throws DeserializerException {
		return (Applicazione) this.xmlToObj(in, Applicazione.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Applicazione}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Applicazione}
	 * @return Object type {@link it.govpay.orm.Applicazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Applicazione readApplicazioneFromString(String in) throws DeserializerException {
		return (Applicazione) this.xmlToObj(in.getBytes(), Applicazione.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: TabellaControparti
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.TabellaControparti}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.TabellaControparti}
	 * @return Object type {@link it.govpay.orm.TabellaControparti}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TabellaControparti readTabellaControparti(String fileName) throws DeserializerException {
		return (TabellaControparti) this.xmlToObj(fileName, TabellaControparti.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.TabellaControparti}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.TabellaControparti}
	 * @return Object type {@link it.govpay.orm.TabellaControparti}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TabellaControparti readTabellaControparti(File file) throws DeserializerException {
		return (TabellaControparti) this.xmlToObj(file, TabellaControparti.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.TabellaControparti}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.TabellaControparti}
	 * @return Object type {@link it.govpay.orm.TabellaControparti}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TabellaControparti readTabellaControparti(InputStream in) throws DeserializerException {
		return (TabellaControparti) this.xmlToObj(in, TabellaControparti.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.TabellaControparti}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.TabellaControparti}
	 * @return Object type {@link it.govpay.orm.TabellaControparti}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TabellaControparti readTabellaControparti(byte[] in) throws DeserializerException {
		return (TabellaControparti) this.xmlToObj(in, TabellaControparti.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.TabellaControparti}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.TabellaControparti}
	 * @return Object type {@link it.govpay.orm.TabellaControparti}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TabellaControparti readTabellaContropartiFromString(String in) throws DeserializerException {
		return (TabellaControparti) this.xmlToObj(in.getBytes(), TabellaControparti.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Ente
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Ente}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Ente}
	 * @return Object type {@link it.govpay.orm.Ente}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Ente readEnte(String fileName) throws DeserializerException {
		return (Ente) this.xmlToObj(fileName, Ente.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Ente}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Ente}
	 * @return Object type {@link it.govpay.orm.Ente}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Ente readEnte(File file) throws DeserializerException {
		return (Ente) this.xmlToObj(file, Ente.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Ente}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Ente}
	 * @return Object type {@link it.govpay.orm.Ente}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Ente readEnte(InputStream in) throws DeserializerException {
		return (Ente) this.xmlToObj(in, Ente.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Ente}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Ente}
	 * @return Object type {@link it.govpay.orm.Ente}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Ente readEnte(byte[] in) throws DeserializerException {
		return (Ente) this.xmlToObj(in, Ente.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Ente}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Ente}
	 * @return Object type {@link it.govpay.orm.Ente}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Ente readEnteFromString(String in) throws DeserializerException {
		return (Ente) this.xmlToObj(in.getBytes(), Ente.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-operatore
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdOperatore}
	 * @return Object type {@link it.govpay.orm.IdOperatore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdOperatore readIdOperatore(String fileName) throws DeserializerException {
		return (IdOperatore) this.xmlToObj(fileName, IdOperatore.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdOperatore}
	 * @return Object type {@link it.govpay.orm.IdOperatore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdOperatore readIdOperatore(File file) throws DeserializerException {
		return (IdOperatore) this.xmlToObj(file, IdOperatore.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdOperatore}
	 * @return Object type {@link it.govpay.orm.IdOperatore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdOperatore readIdOperatore(InputStream in) throws DeserializerException {
		return (IdOperatore) this.xmlToObj(in, IdOperatore.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdOperatore}
	 * @return Object type {@link it.govpay.orm.IdOperatore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdOperatore readIdOperatore(byte[] in) throws DeserializerException {
		return (IdOperatore) this.xmlToObj(in, IdOperatore.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdOperatore}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdOperatore}
	 * @return Object type {@link it.govpay.orm.IdOperatore}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdOperatore readIdOperatoreFromString(String in) throws DeserializerException {
		return (IdOperatore) this.xmlToObj(in.getBytes(), IdOperatore.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: RPT
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.RPT}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.RPT}
	 * @return Object type {@link it.govpay.orm.RPT}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RPT readRPT(String fileName) throws DeserializerException {
		return (RPT) this.xmlToObj(fileName, RPT.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.RPT}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.RPT}
	 * @return Object type {@link it.govpay.orm.RPT}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RPT readRPT(File file) throws DeserializerException {
		return (RPT) this.xmlToObj(file, RPT.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.RPT}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.RPT}
	 * @return Object type {@link it.govpay.orm.RPT}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RPT readRPT(InputStream in) throws DeserializerException {
		return (RPT) this.xmlToObj(in, RPT.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.RPT}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.RPT}
	 * @return Object type {@link it.govpay.orm.RPT}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RPT readRPT(byte[] in) throws DeserializerException {
		return (RPT) this.xmlToObj(in, RPT.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.RPT}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.RPT}
	 * @return Object type {@link it.govpay.orm.RPT}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public RPT readRPTFromString(String in) throws DeserializerException {
		return (RPT) this.xmlToObj(in.getBytes(), RPT.class);
	}	
	
	
	

}
