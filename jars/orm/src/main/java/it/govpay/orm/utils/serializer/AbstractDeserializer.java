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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import org.openspcoop2.generic_project.exception.DeserializerException;

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
import it.govpay.orm.VistaPagamento;
import it.govpay.orm.VistaPagamentoPortale;
import it.govpay.orm.VistaRendicontazione;
import it.govpay.orm.VistaRiscossioni;
import it.govpay.orm.VistaRptVersamento;
import it.govpay.orm.VistaVersamento;

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
	 Object: TipoVersamento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.TipoVersamento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.TipoVersamento}
	 * @return Object type {@link it.govpay.orm.TipoVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TipoVersamento readTipoVersamento(String fileName) throws DeserializerException {
		return (TipoVersamento) this.xmlToObj(fileName, TipoVersamento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.TipoVersamento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.TipoVersamento}
	 * @return Object type {@link it.govpay.orm.TipoVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TipoVersamento readTipoVersamento(File file) throws DeserializerException {
		return (TipoVersamento) this.xmlToObj(file, TipoVersamento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.TipoVersamento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.TipoVersamento}
	 * @return Object type {@link it.govpay.orm.TipoVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TipoVersamento readTipoVersamento(InputStream in) throws DeserializerException {
		return (TipoVersamento) this.xmlToObj(in, TipoVersamento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.TipoVersamento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.TipoVersamento}
	 * @return Object type {@link it.govpay.orm.TipoVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TipoVersamento readTipoVersamento(byte[] in) throws DeserializerException {
		return (TipoVersamento) this.xmlToObj(in, TipoVersamento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.TipoVersamento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.TipoVersamento}
	 * @return Object type {@link it.govpay.orm.TipoVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TipoVersamento readTipoVersamentoFromString(String in) throws DeserializerException {
		return (TipoVersamento) this.xmlToObj(in.getBytes(), TipoVersamento.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: iuv-search
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IuvSearch}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IuvSearch}
	 * @return Object type {@link it.govpay.orm.IuvSearch}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IuvSearch readIuvSearch(String fileName) throws DeserializerException {
		return (IuvSearch) this.xmlToObj(fileName, IuvSearch.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IuvSearch}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IuvSearch}
	 * @return Object type {@link it.govpay.orm.IuvSearch}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IuvSearch readIuvSearch(File file) throws DeserializerException {
		return (IuvSearch) this.xmlToObj(file, IuvSearch.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IuvSearch}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IuvSearch}
	 * @return Object type {@link it.govpay.orm.IuvSearch}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IuvSearch readIuvSearch(InputStream in) throws DeserializerException {
		return (IuvSearch) this.xmlToObj(in, IuvSearch.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IuvSearch}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IuvSearch}
	 * @return Object type {@link it.govpay.orm.IuvSearch}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IuvSearch readIuvSearch(byte[] in) throws DeserializerException {
		return (IuvSearch) this.xmlToObj(in, IuvSearch.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IuvSearch}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IuvSearch}
	 * @return Object type {@link it.govpay.orm.IuvSearch}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IuvSearch readIuvSearchFromString(String in) throws DeserializerException {
		return (IuvSearch) this.xmlToObj(in.getBytes(), IuvSearch.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Audit
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Audit}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Audit}
	 * @return Object type {@link it.govpay.orm.Audit}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Audit readAudit(String fileName) throws DeserializerException {
		return (Audit) this.xmlToObj(fileName, Audit.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Audit}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Audit}
	 * @return Object type {@link it.govpay.orm.Audit}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Audit readAudit(File file) throws DeserializerException {
		return (Audit) this.xmlToObj(file, Audit.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Audit}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Audit}
	 * @return Object type {@link it.govpay.orm.Audit}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Audit readAudit(InputStream in) throws DeserializerException {
		return (Audit) this.xmlToObj(in, Audit.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Audit}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Audit}
	 * @return Object type {@link it.govpay.orm.Audit}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Audit readAudit(byte[] in) throws DeserializerException {
		return (Audit) this.xmlToObj(in, Audit.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Audit}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Audit}
	 * @return Object type {@link it.govpay.orm.Audit}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Audit readAuditFromString(String in) throws DeserializerException {
		return (Audit) this.xmlToObj(in.getBytes(), Audit.class);
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
	 Object: id-incasso
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdIncasso}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdIncasso}
	 * @return Object type {@link it.govpay.orm.IdIncasso}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIncasso readIdIncasso(String fileName) throws DeserializerException {
		return (IdIncasso) this.xmlToObj(fileName, IdIncasso.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdIncasso}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdIncasso}
	 * @return Object type {@link it.govpay.orm.IdIncasso}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIncasso readIdIncasso(File file) throws DeserializerException {
		return (IdIncasso) this.xmlToObj(file, IdIncasso.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdIncasso}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdIncasso}
	 * @return Object type {@link it.govpay.orm.IdIncasso}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIncasso readIdIncasso(InputStream in) throws DeserializerException {
		return (IdIncasso) this.xmlToObj(in, IdIncasso.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdIncasso}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdIncasso}
	 * @return Object type {@link it.govpay.orm.IdIncasso}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIncasso readIdIncasso(byte[] in) throws DeserializerException {
		return (IdIncasso) this.xmlToObj(in, IdIncasso.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdIncasso}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdIncasso}
	 * @return Object type {@link it.govpay.orm.IdIncasso}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdIncasso readIdIncassoFromString(String in) throws DeserializerException {
		return (IdIncasso) this.xmlToObj(in.getBytes(), IdIncasso.class);
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
	 Object: id-operazione
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdOperazione}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdOperazione}
	 * @return Object type {@link it.govpay.orm.IdOperazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdOperazione readIdOperazione(String fileName) throws DeserializerException {
		return (IdOperazione) this.xmlToObj(fileName, IdOperazione.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdOperazione}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdOperazione}
	 * @return Object type {@link it.govpay.orm.IdOperazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdOperazione readIdOperazione(File file) throws DeserializerException {
		return (IdOperazione) this.xmlToObj(file, IdOperazione.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdOperazione}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdOperazione}
	 * @return Object type {@link it.govpay.orm.IdOperazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdOperazione readIdOperazione(InputStream in) throws DeserializerException {
		return (IdOperazione) this.xmlToObj(in, IdOperazione.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdOperazione}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdOperazione}
	 * @return Object type {@link it.govpay.orm.IdOperazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdOperazione readIdOperazione(byte[] in) throws DeserializerException {
		return (IdOperazione) this.xmlToObj(in, IdOperazione.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdOperazione}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdOperazione}
	 * @return Object type {@link it.govpay.orm.IdOperazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdOperazione readIdOperazioneFromString(String in) throws DeserializerException {
		return (IdOperazione) this.xmlToObj(in.getBytes(), IdOperazione.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-pagamento-portale
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdPagamentoPortale}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdPagamentoPortale}
	 * @return Object type {@link it.govpay.orm.IdPagamentoPortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPagamentoPortale readIdPagamentoPortale(String fileName) throws DeserializerException {
		return (IdPagamentoPortale) this.xmlToObj(fileName, IdPagamentoPortale.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdPagamentoPortale}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdPagamentoPortale}
	 * @return Object type {@link it.govpay.orm.IdPagamentoPortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPagamentoPortale readIdPagamentoPortale(File file) throws DeserializerException {
		return (IdPagamentoPortale) this.xmlToObj(file, IdPagamentoPortale.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdPagamentoPortale}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdPagamentoPortale}
	 * @return Object type {@link it.govpay.orm.IdPagamentoPortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPagamentoPortale readIdPagamentoPortale(InputStream in) throws DeserializerException {
		return (IdPagamentoPortale) this.xmlToObj(in, IdPagamentoPortale.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdPagamentoPortale}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdPagamentoPortale}
	 * @return Object type {@link it.govpay.orm.IdPagamentoPortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPagamentoPortale readIdPagamentoPortale(byte[] in) throws DeserializerException {
		return (IdPagamentoPortale) this.xmlToObj(in, IdPagamentoPortale.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdPagamentoPortale}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdPagamentoPortale}
	 * @return Object type {@link it.govpay.orm.IdPagamentoPortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPagamentoPortale readIdPagamentoPortaleFromString(String in) throws DeserializerException {
		return (IdPagamentoPortale) this.xmlToObj(in.getBytes(), IdPagamentoPortale.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: VistaRptVersamento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.VistaRptVersamento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.VistaRptVersamento}
	 * @return Object type {@link it.govpay.orm.VistaRptVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaRptVersamento readVistaRptVersamento(String fileName) throws DeserializerException {
		return (VistaRptVersamento) this.xmlToObj(fileName, VistaRptVersamento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.VistaRptVersamento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.VistaRptVersamento}
	 * @return Object type {@link it.govpay.orm.VistaRptVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaRptVersamento readVistaRptVersamento(File file) throws DeserializerException {
		return (VistaRptVersamento) this.xmlToObj(file, VistaRptVersamento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.VistaRptVersamento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.VistaRptVersamento}
	 * @return Object type {@link it.govpay.orm.VistaRptVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaRptVersamento readVistaRptVersamento(InputStream in) throws DeserializerException {
		return (VistaRptVersamento) this.xmlToObj(in, VistaRptVersamento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.VistaRptVersamento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.VistaRptVersamento}
	 * @return Object type {@link it.govpay.orm.VistaRptVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaRptVersamento readVistaRptVersamento(byte[] in) throws DeserializerException {
		return (VistaRptVersamento) this.xmlToObj(in, VistaRptVersamento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.VistaRptVersamento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.VistaRptVersamento}
	 * @return Object type {@link it.govpay.orm.VistaRptVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaRptVersamento readVistaRptVersamentoFromString(String in) throws DeserializerException {
		return (VistaRptVersamento) this.xmlToObj(in.getBytes(), VistaRptVersamento.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-tipo-versamento-dominio
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdTipoVersamentoDominio}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdTipoVersamentoDominio}
	 * @return Object type {@link it.govpay.orm.IdTipoVersamentoDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTipoVersamentoDominio readIdTipoVersamentoDominio(String fileName) throws DeserializerException {
		return (IdTipoVersamentoDominio) this.xmlToObj(fileName, IdTipoVersamentoDominio.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdTipoVersamentoDominio}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdTipoVersamentoDominio}
	 * @return Object type {@link it.govpay.orm.IdTipoVersamentoDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTipoVersamentoDominio readIdTipoVersamentoDominio(File file) throws DeserializerException {
		return (IdTipoVersamentoDominio) this.xmlToObj(file, IdTipoVersamentoDominio.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdTipoVersamentoDominio}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdTipoVersamentoDominio}
	 * @return Object type {@link it.govpay.orm.IdTipoVersamentoDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTipoVersamentoDominio readIdTipoVersamentoDominio(InputStream in) throws DeserializerException {
		return (IdTipoVersamentoDominio) this.xmlToObj(in, IdTipoVersamentoDominio.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdTipoVersamentoDominio}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdTipoVersamentoDominio}
	 * @return Object type {@link it.govpay.orm.IdTipoVersamentoDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTipoVersamentoDominio readIdTipoVersamentoDominio(byte[] in) throws DeserializerException {
		return (IdTipoVersamentoDominio) this.xmlToObj(in, IdTipoVersamentoDominio.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdTipoVersamentoDominio}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdTipoVersamentoDominio}
	 * @return Object type {@link it.govpay.orm.IdTipoVersamentoDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTipoVersamentoDominio readIdTipoVersamentoDominioFromString(String in) throws DeserializerException {
		return (IdTipoVersamentoDominio) this.xmlToObj(in.getBytes(), IdTipoVersamentoDominio.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-tipo-versamento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdTipoVersamento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdTipoVersamento}
	 * @return Object type {@link it.govpay.orm.IdTipoVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTipoVersamento readIdTipoVersamento(String fileName) throws DeserializerException {
		return (IdTipoVersamento) this.xmlToObj(fileName, IdTipoVersamento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdTipoVersamento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdTipoVersamento}
	 * @return Object type {@link it.govpay.orm.IdTipoVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTipoVersamento readIdTipoVersamento(File file) throws DeserializerException {
		return (IdTipoVersamento) this.xmlToObj(file, IdTipoVersamento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdTipoVersamento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdTipoVersamento}
	 * @return Object type {@link it.govpay.orm.IdTipoVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTipoVersamento readIdTipoVersamento(InputStream in) throws DeserializerException {
		return (IdTipoVersamento) this.xmlToObj(in, IdTipoVersamento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdTipoVersamento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdTipoVersamento}
	 * @return Object type {@link it.govpay.orm.IdTipoVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTipoVersamento readIdTipoVersamento(byte[] in) throws DeserializerException {
		return (IdTipoVersamento) this.xmlToObj(in, IdTipoVersamento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdTipoVersamento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdTipoVersamento}
	 * @return Object type {@link it.govpay.orm.IdTipoVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTipoVersamento readIdTipoVersamentoFromString(String in) throws DeserializerException {
		return (IdTipoVersamento) this.xmlToObj(in.getBytes(), IdTipoVersamento.class);
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
	 Object: id-uo
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdUo}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdUo}
	 * @return Object type {@link it.govpay.orm.IdUo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdUo readIdUo(String fileName) throws DeserializerException {
		return (IdUo) this.xmlToObj(fileName, IdUo.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdUo}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdUo}
	 * @return Object type {@link it.govpay.orm.IdUo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdUo readIdUo(File file) throws DeserializerException {
		return (IdUo) this.xmlToObj(file, IdUo.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdUo}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdUo}
	 * @return Object type {@link it.govpay.orm.IdUo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdUo readIdUo(InputStream in) throws DeserializerException {
		return (IdUo) this.xmlToObj(in, IdUo.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdUo}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdUo}
	 * @return Object type {@link it.govpay.orm.IdUo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdUo readIdUo(byte[] in) throws DeserializerException {
		return (IdUo) this.xmlToObj(in, IdUo.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdUo}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdUo}
	 * @return Object type {@link it.govpay.orm.IdUo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdUo readIdUoFromString(String in) throws DeserializerException {
		return (IdUo) this.xmlToObj(in.getBytes(), IdUo.class);
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
	 Object: id-documento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdDocumento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdDocumento}
	 * @return Object type {@link it.govpay.orm.IdDocumento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdDocumento readIdDocumento(String fileName) throws DeserializerException {
		return (IdDocumento) this.xmlToObj(fileName, IdDocumento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdDocumento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdDocumento}
	 * @return Object type {@link it.govpay.orm.IdDocumento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdDocumento readIdDocumento(File file) throws DeserializerException {
		return (IdDocumento) this.xmlToObj(file, IdDocumento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdDocumento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdDocumento}
	 * @return Object type {@link it.govpay.orm.IdDocumento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdDocumento readIdDocumento(InputStream in) throws DeserializerException {
		return (IdDocumento) this.xmlToObj(in, IdDocumento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdDocumento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdDocumento}
	 * @return Object type {@link it.govpay.orm.IdDocumento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdDocumento readIdDocumento(byte[] in) throws DeserializerException {
		return (IdDocumento) this.xmlToObj(in, IdDocumento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdDocumento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdDocumento}
	 * @return Object type {@link it.govpay.orm.IdDocumento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdDocumento readIdDocumentoFromString(String in) throws DeserializerException {
		return (IdDocumento) this.xmlToObj(in.getBytes(), IdDocumento.class);
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
	 Object: Promemoria
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Promemoria}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Promemoria}
	 * @return Object type {@link it.govpay.orm.Promemoria}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Promemoria readPromemoria(String fileName) throws DeserializerException {
		return (Promemoria) this.xmlToObj(fileName, Promemoria.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Promemoria}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Promemoria}
	 * @return Object type {@link it.govpay.orm.Promemoria}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Promemoria readPromemoria(File file) throws DeserializerException {
		return (Promemoria) this.xmlToObj(file, Promemoria.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Promemoria}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Promemoria}
	 * @return Object type {@link it.govpay.orm.Promemoria}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Promemoria readPromemoria(InputStream in) throws DeserializerException {
		return (Promemoria) this.xmlToObj(in, Promemoria.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Promemoria}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Promemoria}
	 * @return Object type {@link it.govpay.orm.Promemoria}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Promemoria readPromemoria(byte[] in) throws DeserializerException {
		return (Promemoria) this.xmlToObj(in, Promemoria.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Promemoria}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Promemoria}
	 * @return Object type {@link it.govpay.orm.Promemoria}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Promemoria readPromemoriaFromString(String in) throws DeserializerException {
		return (Promemoria) this.xmlToObj(in.getBytes(), Promemoria.class);
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
	 Object: id-promemoria
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdPromemoria}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdPromemoria}
	 * @return Object type {@link it.govpay.orm.IdPromemoria}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPromemoria readIdPromemoria(String fileName) throws DeserializerException {
		return (IdPromemoria) this.xmlToObj(fileName, IdPromemoria.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdPromemoria}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdPromemoria}
	 * @return Object type {@link it.govpay.orm.IdPromemoria}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPromemoria readIdPromemoria(File file) throws DeserializerException {
		return (IdPromemoria) this.xmlToObj(file, IdPromemoria.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdPromemoria}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdPromemoria}
	 * @return Object type {@link it.govpay.orm.IdPromemoria}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPromemoria readIdPromemoria(InputStream in) throws DeserializerException {
		return (IdPromemoria) this.xmlToObj(in, IdPromemoria.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdPromemoria}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdPromemoria}
	 * @return Object type {@link it.govpay.orm.IdPromemoria}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPromemoria readIdPromemoria(byte[] in) throws DeserializerException {
		return (IdPromemoria) this.xmlToObj(in, IdPromemoria.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdPromemoria}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdPromemoria}
	 * @return Object type {@link it.govpay.orm.IdPromemoria}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPromemoria readIdPromemoriaFromString(String in) throws DeserializerException {
		return (IdPromemoria) this.xmlToObj(in.getBytes(), IdPromemoria.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Operazione
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Operazione}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Operazione}
	 * @return Object type {@link it.govpay.orm.Operazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Operazione readOperazione(String fileName) throws DeserializerException {
		return (Operazione) this.xmlToObj(fileName, Operazione.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Operazione}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Operazione}
	 * @return Object type {@link it.govpay.orm.Operazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Operazione readOperazione(File file) throws DeserializerException {
		return (Operazione) this.xmlToObj(file, Operazione.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Operazione}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Operazione}
	 * @return Object type {@link it.govpay.orm.Operazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Operazione readOperazione(InputStream in) throws DeserializerException {
		return (Operazione) this.xmlToObj(in, Operazione.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Operazione}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Operazione}
	 * @return Object type {@link it.govpay.orm.Operazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Operazione readOperazione(byte[] in) throws DeserializerException {
		return (Operazione) this.xmlToObj(in, Operazione.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Operazione}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Operazione}
	 * @return Object type {@link it.govpay.orm.Operazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Operazione readOperazioneFromString(String in) throws DeserializerException {
		return (Operazione) this.xmlToObj(in.getBytes(), Operazione.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-stampa
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdStampa}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdStampa}
	 * @return Object type {@link it.govpay.orm.IdStampa}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdStampa readIdStampa(String fileName) throws DeserializerException {
		return (IdStampa) this.xmlToObj(fileName, IdStampa.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdStampa}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdStampa}
	 * @return Object type {@link it.govpay.orm.IdStampa}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdStampa readIdStampa(File file) throws DeserializerException {
		return (IdStampa) this.xmlToObj(file, IdStampa.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdStampa}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdStampa}
	 * @return Object type {@link it.govpay.orm.IdStampa}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdStampa readIdStampa(InputStream in) throws DeserializerException {
		return (IdStampa) this.xmlToObj(in, IdStampa.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdStampa}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdStampa}
	 * @return Object type {@link it.govpay.orm.IdStampa}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdStampa readIdStampa(byte[] in) throws DeserializerException {
		return (IdStampa) this.xmlToObj(in, IdStampa.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdStampa}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdStampa}
	 * @return Object type {@link it.govpay.orm.IdStampa}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdStampa readIdStampaFromString(String in) throws DeserializerException {
		return (IdStampa) this.xmlToObj(in.getBytes(), IdStampa.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: PagamentoPortaleVersamento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.PagamentoPortaleVersamento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.PagamentoPortaleVersamento}
	 * @return Object type {@link it.govpay.orm.PagamentoPortaleVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public PagamentoPortaleVersamento readPagamentoPortaleVersamento(String fileName) throws DeserializerException {
		return (PagamentoPortaleVersamento) this.xmlToObj(fileName, PagamentoPortaleVersamento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.PagamentoPortaleVersamento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.PagamentoPortaleVersamento}
	 * @return Object type {@link it.govpay.orm.PagamentoPortaleVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public PagamentoPortaleVersamento readPagamentoPortaleVersamento(File file) throws DeserializerException {
		return (PagamentoPortaleVersamento) this.xmlToObj(file, PagamentoPortaleVersamento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.PagamentoPortaleVersamento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.PagamentoPortaleVersamento}
	 * @return Object type {@link it.govpay.orm.PagamentoPortaleVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public PagamentoPortaleVersamento readPagamentoPortaleVersamento(InputStream in) throws DeserializerException {
		return (PagamentoPortaleVersamento) this.xmlToObj(in, PagamentoPortaleVersamento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.PagamentoPortaleVersamento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.PagamentoPortaleVersamento}
	 * @return Object type {@link it.govpay.orm.PagamentoPortaleVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public PagamentoPortaleVersamento readPagamentoPortaleVersamento(byte[] in) throws DeserializerException {
		return (PagamentoPortaleVersamento) this.xmlToObj(in, PagamentoPortaleVersamento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.PagamentoPortaleVersamento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.PagamentoPortaleVersamento}
	 * @return Object type {@link it.govpay.orm.PagamentoPortaleVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public PagamentoPortaleVersamento readPagamentoPortaleVersamentoFromString(String in) throws DeserializerException {
		return (PagamentoPortaleVersamento) this.xmlToObj(in.getBytes(), PagamentoPortaleVersamento.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Incasso
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Incasso}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Incasso}
	 * @return Object type {@link it.govpay.orm.Incasso}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Incasso readIncasso(String fileName) throws DeserializerException {
		return (Incasso) this.xmlToObj(fileName, Incasso.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Incasso}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Incasso}
	 * @return Object type {@link it.govpay.orm.Incasso}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Incasso readIncasso(File file) throws DeserializerException {
		return (Incasso) this.xmlToObj(file, Incasso.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Incasso}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Incasso}
	 * @return Object type {@link it.govpay.orm.Incasso}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Incasso readIncasso(InputStream in) throws DeserializerException {
		return (Incasso) this.xmlToObj(in, Incasso.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Incasso}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Incasso}
	 * @return Object type {@link it.govpay.orm.Incasso}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Incasso readIncasso(byte[] in) throws DeserializerException {
		return (Incasso) this.xmlToObj(in, Incasso.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Incasso}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Incasso}
	 * @return Object type {@link it.govpay.orm.Incasso}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Incasso readIncassoFromString(String in) throws DeserializerException {
		return (Incasso) this.xmlToObj(in.getBytes(), Incasso.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-acl
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdAcl}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdAcl}
	 * @return Object type {@link it.govpay.orm.IdAcl}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdAcl readIdAcl(String fileName) throws DeserializerException {
		return (IdAcl) this.xmlToObj(fileName, IdAcl.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdAcl}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdAcl}
	 * @return Object type {@link it.govpay.orm.IdAcl}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdAcl readIdAcl(File file) throws DeserializerException {
		return (IdAcl) this.xmlToObj(file, IdAcl.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdAcl}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdAcl}
	 * @return Object type {@link it.govpay.orm.IdAcl}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdAcl readIdAcl(InputStream in) throws DeserializerException {
		return (IdAcl) this.xmlToObj(in, IdAcl.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdAcl}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdAcl}
	 * @return Object type {@link it.govpay.orm.IdAcl}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdAcl readIdAcl(byte[] in) throws DeserializerException {
		return (IdAcl) this.xmlToObj(in, IdAcl.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdAcl}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdAcl}
	 * @return Object type {@link it.govpay.orm.IdAcl}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdAcl readIdAclFromString(String in) throws DeserializerException {
		return (IdAcl) this.xmlToObj(in.getBytes(), IdAcl.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-utenza
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdUtenza}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdUtenza}
	 * @return Object type {@link it.govpay.orm.IdUtenza}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdUtenza readIdUtenza(String fileName) throws DeserializerException {
		return (IdUtenza) this.xmlToObj(fileName, IdUtenza.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdUtenza}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdUtenza}
	 * @return Object type {@link it.govpay.orm.IdUtenza}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdUtenza readIdUtenza(File file) throws DeserializerException {
		return (IdUtenza) this.xmlToObj(file, IdUtenza.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdUtenza}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdUtenza}
	 * @return Object type {@link it.govpay.orm.IdUtenza}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdUtenza readIdUtenza(InputStream in) throws DeserializerException {
		return (IdUtenza) this.xmlToObj(in, IdUtenza.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdUtenza}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdUtenza}
	 * @return Object type {@link it.govpay.orm.IdUtenza}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdUtenza readIdUtenza(byte[] in) throws DeserializerException {
		return (IdUtenza) this.xmlToObj(in, IdUtenza.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdUtenza}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdUtenza}
	 * @return Object type {@link it.govpay.orm.IdUtenza}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdUtenza readIdUtenzaFromString(String in) throws DeserializerException {
		return (IdUtenza) this.xmlToObj(in.getBytes(), IdUtenza.class);
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
	 Object: id-rendicontazione
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdRendicontazione}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdRendicontazione}
	 * @return Object type {@link it.govpay.orm.IdRendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRendicontazione readIdRendicontazione(String fileName) throws DeserializerException {
		return (IdRendicontazione) this.xmlToObj(fileName, IdRendicontazione.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdRendicontazione}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdRendicontazione}
	 * @return Object type {@link it.govpay.orm.IdRendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRendicontazione readIdRendicontazione(File file) throws DeserializerException {
		return (IdRendicontazione) this.xmlToObj(file, IdRendicontazione.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdRendicontazione}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdRendicontazione}
	 * @return Object type {@link it.govpay.orm.IdRendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRendicontazione readIdRendicontazione(InputStream in) throws DeserializerException {
		return (IdRendicontazione) this.xmlToObj(in, IdRendicontazione.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdRendicontazione}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdRendicontazione}
	 * @return Object type {@link it.govpay.orm.IdRendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRendicontazione readIdRendicontazione(byte[] in) throws DeserializerException {
		return (IdRendicontazione) this.xmlToObj(in, IdRendicontazione.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdRendicontazione}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdRendicontazione}
	 * @return Object type {@link it.govpay.orm.IdRendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdRendicontazione readIdRendicontazioneFromString(String in) throws DeserializerException {
		return (IdRendicontazione) this.xmlToObj(in.getBytes(), IdRendicontazione.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-notifica
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdNotifica}
	 * @return Object type {@link it.govpay.orm.IdNotifica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdNotifica readIdNotifica(String fileName) throws DeserializerException {
		return (IdNotifica) this.xmlToObj(fileName, IdNotifica.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdNotifica}
	 * @return Object type {@link it.govpay.orm.IdNotifica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdNotifica readIdNotifica(File file) throws DeserializerException {
		return (IdNotifica) this.xmlToObj(file, IdNotifica.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdNotifica}
	 * @return Object type {@link it.govpay.orm.IdNotifica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdNotifica readIdNotifica(InputStream in) throws DeserializerException {
		return (IdNotifica) this.xmlToObj(in, IdNotifica.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdNotifica}
	 * @return Object type {@link it.govpay.orm.IdNotifica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdNotifica readIdNotifica(byte[] in) throws DeserializerException {
		return (IdNotifica) this.xmlToObj(in, IdNotifica.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdNotifica}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdNotifica}
	 * @return Object type {@link it.govpay.orm.IdNotifica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdNotifica readIdNotificaFromString(String in) throws DeserializerException {
		return (IdNotifica) this.xmlToObj(in.getBytes(), IdNotifica.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: VersamentoIncasso
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.VersamentoIncasso}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.VersamentoIncasso}
	 * @return Object type {@link it.govpay.orm.VersamentoIncasso}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VersamentoIncasso readVersamentoIncasso(String fileName) throws DeserializerException {
		return (VersamentoIncasso) this.xmlToObj(fileName, VersamentoIncasso.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.VersamentoIncasso}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.VersamentoIncasso}
	 * @return Object type {@link it.govpay.orm.VersamentoIncasso}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VersamentoIncasso readVersamentoIncasso(File file) throws DeserializerException {
		return (VersamentoIncasso) this.xmlToObj(file, VersamentoIncasso.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.VersamentoIncasso}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.VersamentoIncasso}
	 * @return Object type {@link it.govpay.orm.VersamentoIncasso}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VersamentoIncasso readVersamentoIncasso(InputStream in) throws DeserializerException {
		return (VersamentoIncasso) this.xmlToObj(in, VersamentoIncasso.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.VersamentoIncasso}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.VersamentoIncasso}
	 * @return Object type {@link it.govpay.orm.VersamentoIncasso}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VersamentoIncasso readVersamentoIncasso(byte[] in) throws DeserializerException {
		return (VersamentoIncasso) this.xmlToObj(in, VersamentoIncasso.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.VersamentoIncasso}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.VersamentoIncasso}
	 * @return Object type {@link it.govpay.orm.VersamentoIncasso}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VersamentoIncasso readVersamentoIncassoFromString(String in) throws DeserializerException {
		return (VersamentoIncasso) this.xmlToObj(in.getBytes(), VersamentoIncasso.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Utenza
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Utenza}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Utenza}
	 * @return Object type {@link it.govpay.orm.Utenza}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Utenza readUtenza(String fileName) throws DeserializerException {
		return (Utenza) this.xmlToObj(fileName, Utenza.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Utenza}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Utenza}
	 * @return Object type {@link it.govpay.orm.Utenza}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Utenza readUtenza(File file) throws DeserializerException {
		return (Utenza) this.xmlToObj(file, Utenza.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Utenza}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Utenza}
	 * @return Object type {@link it.govpay.orm.Utenza}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Utenza readUtenza(InputStream in) throws DeserializerException {
		return (Utenza) this.xmlToObj(in, Utenza.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Utenza}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Utenza}
	 * @return Object type {@link it.govpay.orm.Utenza}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Utenza readUtenza(byte[] in) throws DeserializerException {
		return (Utenza) this.xmlToObj(in, Utenza.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Utenza}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Utenza}
	 * @return Object type {@link it.govpay.orm.Utenza}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Utenza readUtenzaFromString(String in) throws DeserializerException {
		return (Utenza) this.xmlToObj(in.getBytes(), Utenza.class);
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
	 Object: TracciatoNotificaPagamenti
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.TracciatoNotificaPagamenti}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.TracciatoNotificaPagamenti}
	 * @return Object type {@link it.govpay.orm.TracciatoNotificaPagamenti}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TracciatoNotificaPagamenti readTracciatoNotificaPagamenti(String fileName) throws DeserializerException {
		return (TracciatoNotificaPagamenti) this.xmlToObj(fileName, TracciatoNotificaPagamenti.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.TracciatoNotificaPagamenti}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.TracciatoNotificaPagamenti}
	 * @return Object type {@link it.govpay.orm.TracciatoNotificaPagamenti}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TracciatoNotificaPagamenti readTracciatoNotificaPagamenti(File file) throws DeserializerException {
		return (TracciatoNotificaPagamenti) this.xmlToObj(file, TracciatoNotificaPagamenti.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.TracciatoNotificaPagamenti}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.TracciatoNotificaPagamenti}
	 * @return Object type {@link it.govpay.orm.TracciatoNotificaPagamenti}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TracciatoNotificaPagamenti readTracciatoNotificaPagamenti(InputStream in) throws DeserializerException {
		return (TracciatoNotificaPagamenti) this.xmlToObj(in, TracciatoNotificaPagamenti.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.TracciatoNotificaPagamenti}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.TracciatoNotificaPagamenti}
	 * @return Object type {@link it.govpay.orm.TracciatoNotificaPagamenti}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TracciatoNotificaPagamenti readTracciatoNotificaPagamenti(byte[] in) throws DeserializerException {
		return (TracciatoNotificaPagamenti) this.xmlToObj(in, TracciatoNotificaPagamenti.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.TracciatoNotificaPagamenti}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.TracciatoNotificaPagamenti}
	 * @return Object type {@link it.govpay.orm.TracciatoNotificaPagamenti}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TracciatoNotificaPagamenti readTracciatoNotificaPagamentiFromString(String in) throws DeserializerException {
		return (TracciatoNotificaPagamenti) this.xmlToObj(in.getBytes(), TracciatoNotificaPagamenti.class);
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
	 Object: Tracciato
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Tracciato}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Tracciato}
	 * @return Object type {@link it.govpay.orm.Tracciato}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Tracciato readTracciato(String fileName) throws DeserializerException {
		return (Tracciato) this.xmlToObj(fileName, Tracciato.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Tracciato}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Tracciato}
	 * @return Object type {@link it.govpay.orm.Tracciato}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Tracciato readTracciato(File file) throws DeserializerException {
		return (Tracciato) this.xmlToObj(file, Tracciato.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Tracciato}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Tracciato}
	 * @return Object type {@link it.govpay.orm.Tracciato}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Tracciato readTracciato(InputStream in) throws DeserializerException {
		return (Tracciato) this.xmlToObj(in, Tracciato.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Tracciato}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Tracciato}
	 * @return Object type {@link it.govpay.orm.Tracciato}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Tracciato readTracciato(byte[] in) throws DeserializerException {
		return (Tracciato) this.xmlToObj(in, Tracciato.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Tracciato}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Tracciato}
	 * @return Object type {@link it.govpay.orm.Tracciato}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Tracciato readTracciatoFromString(String in) throws DeserializerException {
		return (Tracciato) this.xmlToObj(in.getBytes(), Tracciato.class);
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
	 Object: VistaRendicontazione
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.VistaRendicontazione}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.VistaRendicontazione}
	 * @return Object type {@link it.govpay.orm.VistaRendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaRendicontazione readVistaRendicontazione(String fileName) throws DeserializerException {
		return (VistaRendicontazione) this.xmlToObj(fileName, VistaRendicontazione.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.VistaRendicontazione}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.VistaRendicontazione}
	 * @return Object type {@link it.govpay.orm.VistaRendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaRendicontazione readVistaRendicontazione(File file) throws DeserializerException {
		return (VistaRendicontazione) this.xmlToObj(file, VistaRendicontazione.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.VistaRendicontazione}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.VistaRendicontazione}
	 * @return Object type {@link it.govpay.orm.VistaRendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaRendicontazione readVistaRendicontazione(InputStream in) throws DeserializerException {
		return (VistaRendicontazione) this.xmlToObj(in, VistaRendicontazione.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.VistaRendicontazione}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.VistaRendicontazione}
	 * @return Object type {@link it.govpay.orm.VistaRendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaRendicontazione readVistaRendicontazione(byte[] in) throws DeserializerException {
		return (VistaRendicontazione) this.xmlToObj(in, VistaRendicontazione.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.VistaRendicontazione}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.VistaRendicontazione}
	 * @return Object type {@link it.govpay.orm.VistaRendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaRendicontazione readVistaRendicontazioneFromString(String in) throws DeserializerException {
		return (VistaRendicontazione) this.xmlToObj(in.getBytes(), VistaRendicontazione.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-pagamento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdPagamento}
	 * @return Object type {@link it.govpay.orm.IdPagamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPagamento readIdPagamento(String fileName) throws DeserializerException {
		return (IdPagamento) this.xmlToObj(fileName, IdPagamento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdPagamento}
	 * @return Object type {@link it.govpay.orm.IdPagamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPagamento readIdPagamento(File file) throws DeserializerException {
		return (IdPagamento) this.xmlToObj(file, IdPagamento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdPagamento}
	 * @return Object type {@link it.govpay.orm.IdPagamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPagamento readIdPagamento(InputStream in) throws DeserializerException {
		return (IdPagamento) this.xmlToObj(in, IdPagamento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdPagamento}
	 * @return Object type {@link it.govpay.orm.IdPagamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPagamento readIdPagamento(byte[] in) throws DeserializerException {
		return (IdPagamento) this.xmlToObj(in, IdPagamento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdPagamento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdPagamento}
	 * @return Object type {@link it.govpay.orm.IdPagamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdPagamento readIdPagamentoFromString(String in) throws DeserializerException {
		return (IdPagamento) this.xmlToObj(in.getBytes(), IdPagamento.class);
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
	 Object: NotificaAppIO
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.NotificaAppIO}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.NotificaAppIO}
	 * @return Object type {@link it.govpay.orm.NotificaAppIO}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public NotificaAppIO readNotificaAppIO(String fileName) throws DeserializerException {
		return (NotificaAppIO) this.xmlToObj(fileName, NotificaAppIO.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.NotificaAppIO}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.NotificaAppIO}
	 * @return Object type {@link it.govpay.orm.NotificaAppIO}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public NotificaAppIO readNotificaAppIO(File file) throws DeserializerException {
		return (NotificaAppIO) this.xmlToObj(file, NotificaAppIO.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.NotificaAppIO}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.NotificaAppIO}
	 * @return Object type {@link it.govpay.orm.NotificaAppIO}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public NotificaAppIO readNotificaAppIO(InputStream in) throws DeserializerException {
		return (NotificaAppIO) this.xmlToObj(in, NotificaAppIO.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.NotificaAppIO}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.NotificaAppIO}
	 * @return Object type {@link it.govpay.orm.NotificaAppIO}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public NotificaAppIO readNotificaAppIO(byte[] in) throws DeserializerException {
		return (NotificaAppIO) this.xmlToObj(in, NotificaAppIO.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.NotificaAppIO}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.NotificaAppIO}
	 * @return Object type {@link it.govpay.orm.NotificaAppIO}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public NotificaAppIO readNotificaAppIOFromString(String in) throws DeserializerException {
		return (NotificaAppIO) this.xmlToObj(in.getBytes(), NotificaAppIO.class);
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
	 Object: id-tipo-tributo
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdTipoTributo}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdTipoTributo}
	 * @return Object type {@link it.govpay.orm.IdTipoTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTipoTributo readIdTipoTributo(String fileName) throws DeserializerException {
		return (IdTipoTributo) this.xmlToObj(fileName, IdTipoTributo.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdTipoTributo}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdTipoTributo}
	 * @return Object type {@link it.govpay.orm.IdTipoTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTipoTributo readIdTipoTributo(File file) throws DeserializerException {
		return (IdTipoTributo) this.xmlToObj(file, IdTipoTributo.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdTipoTributo}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdTipoTributo}
	 * @return Object type {@link it.govpay.orm.IdTipoTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTipoTributo readIdTipoTributo(InputStream in) throws DeserializerException {
		return (IdTipoTributo) this.xmlToObj(in, IdTipoTributo.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdTipoTributo}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdTipoTributo}
	 * @return Object type {@link it.govpay.orm.IdTipoTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTipoTributo readIdTipoTributo(byte[] in) throws DeserializerException {
		return (IdTipoTributo) this.xmlToObj(in, IdTipoTributo.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdTipoTributo}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdTipoTributo}
	 * @return Object type {@link it.govpay.orm.IdTipoTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTipoTributo readIdTipoTributoFromString(String in) throws DeserializerException {
		return (IdTipoTributo) this.xmlToObj(in.getBytes(), IdTipoTributo.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: id-batch
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdBatch}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdBatch}
	 * @return Object type {@link it.govpay.orm.IdBatch}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdBatch readIdBatch(String fileName) throws DeserializerException {
		return (IdBatch) this.xmlToObj(fileName, IdBatch.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdBatch}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdBatch}
	 * @return Object type {@link it.govpay.orm.IdBatch}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdBatch readIdBatch(File file) throws DeserializerException {
		return (IdBatch) this.xmlToObj(file, IdBatch.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdBatch}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdBatch}
	 * @return Object type {@link it.govpay.orm.IdBatch}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdBatch readIdBatch(InputStream in) throws DeserializerException {
		return (IdBatch) this.xmlToObj(in, IdBatch.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdBatch}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdBatch}
	 * @return Object type {@link it.govpay.orm.IdBatch}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdBatch readIdBatch(byte[] in) throws DeserializerException {
		return (IdBatch) this.xmlToObj(in, IdBatch.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdBatch}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdBatch}
	 * @return Object type {@link it.govpay.orm.IdBatch}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdBatch readIdBatchFromString(String in) throws DeserializerException {
		return (IdBatch) this.xmlToObj(in.getBytes(), IdBatch.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: VistaPagamentoPortale
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.VistaPagamentoPortale}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.VistaPagamentoPortale}
	 * @return Object type {@link it.govpay.orm.VistaPagamentoPortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaPagamentoPortale readVistaPagamentoPortale(String fileName) throws DeserializerException {
		return (VistaPagamentoPortale) this.xmlToObj(fileName, VistaPagamentoPortale.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.VistaPagamentoPortale}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.VistaPagamentoPortale}
	 * @return Object type {@link it.govpay.orm.VistaPagamentoPortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaPagamentoPortale readVistaPagamentoPortale(File file) throws DeserializerException {
		return (VistaPagamentoPortale) this.xmlToObj(file, VistaPagamentoPortale.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.VistaPagamentoPortale}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.VistaPagamentoPortale}
	 * @return Object type {@link it.govpay.orm.VistaPagamentoPortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaPagamentoPortale readVistaPagamentoPortale(InputStream in) throws DeserializerException {
		return (VistaPagamentoPortale) this.xmlToObj(in, VistaPagamentoPortale.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.VistaPagamentoPortale}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.VistaPagamentoPortale}
	 * @return Object type {@link it.govpay.orm.VistaPagamentoPortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaPagamentoPortale readVistaPagamentoPortale(byte[] in) throws DeserializerException {
		return (VistaPagamentoPortale) this.xmlToObj(in, VistaPagamentoPortale.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.VistaPagamentoPortale}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.VistaPagamentoPortale}
	 * @return Object type {@link it.govpay.orm.VistaPagamentoPortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaPagamentoPortale readVistaPagamentoPortaleFromString(String in) throws DeserializerException {
		return (VistaPagamentoPortale) this.xmlToObj(in.getBytes(), VistaPagamentoPortale.class);
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
	 Object: TipoTributo
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.TipoTributo}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.TipoTributo}
	 * @return Object type {@link it.govpay.orm.TipoTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TipoTributo readTipoTributo(String fileName) throws DeserializerException {
		return (TipoTributo) this.xmlToObj(fileName, TipoTributo.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.TipoTributo}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.TipoTributo}
	 * @return Object type {@link it.govpay.orm.TipoTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TipoTributo readTipoTributo(File file) throws DeserializerException {
		return (TipoTributo) this.xmlToObj(file, TipoTributo.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.TipoTributo}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.TipoTributo}
	 * @return Object type {@link it.govpay.orm.TipoTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TipoTributo readTipoTributo(InputStream in) throws DeserializerException {
		return (TipoTributo) this.xmlToObj(in, TipoTributo.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.TipoTributo}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.TipoTributo}
	 * @return Object type {@link it.govpay.orm.TipoTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TipoTributo readTipoTributo(byte[] in) throws DeserializerException {
		return (TipoTributo) this.xmlToObj(in, TipoTributo.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.TipoTributo}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.TipoTributo}
	 * @return Object type {@link it.govpay.orm.TipoTributo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TipoTributo readTipoTributoFromString(String in) throws DeserializerException {
		return (TipoTributo) this.xmlToObj(in.getBytes(), TipoTributo.class);
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
	 Object: Documento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Documento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Documento}
	 * @return Object type {@link it.govpay.orm.Documento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Documento readDocumento(String fileName) throws DeserializerException {
		return (Documento) this.xmlToObj(fileName, Documento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Documento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Documento}
	 * @return Object type {@link it.govpay.orm.Documento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Documento readDocumento(File file) throws DeserializerException {
		return (Documento) this.xmlToObj(file, Documento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Documento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Documento}
	 * @return Object type {@link it.govpay.orm.Documento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Documento readDocumento(InputStream in) throws DeserializerException {
		return (Documento) this.xmlToObj(in, Documento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Documento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Documento}
	 * @return Object type {@link it.govpay.orm.Documento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Documento readDocumento(byte[] in) throws DeserializerException {
		return (Documento) this.xmlToObj(in, Documento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Documento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Documento}
	 * @return Object type {@link it.govpay.orm.Documento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Documento readDocumentoFromString(String in) throws DeserializerException {
		return (Documento) this.xmlToObj(in.getBytes(), Documento.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Stampa
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Stampa}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Stampa}
	 * @return Object type {@link it.govpay.orm.Stampa}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Stampa readStampa(String fileName) throws DeserializerException {
		return (Stampa) this.xmlToObj(fileName, Stampa.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Stampa}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Stampa}
	 * @return Object type {@link it.govpay.orm.Stampa}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Stampa readStampa(File file) throws DeserializerException {
		return (Stampa) this.xmlToObj(file, Stampa.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Stampa}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Stampa}
	 * @return Object type {@link it.govpay.orm.Stampa}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Stampa readStampa(InputStream in) throws DeserializerException {
		return (Stampa) this.xmlToObj(in, Stampa.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Stampa}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Stampa}
	 * @return Object type {@link it.govpay.orm.Stampa}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Stampa readStampa(byte[] in) throws DeserializerException {
		return (Stampa) this.xmlToObj(in, Stampa.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Stampa}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Stampa}
	 * @return Object type {@link it.govpay.orm.Stampa}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Stampa readStampaFromString(String in) throws DeserializerException {
		return (Stampa) this.xmlToObj(in.getBytes(), Stampa.class);
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
	 Object: VistaRiscossioni
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.VistaRiscossioni}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.VistaRiscossioni}
	 * @return Object type {@link it.govpay.orm.VistaRiscossioni}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaRiscossioni readVistaRiscossioni(String fileName) throws DeserializerException {
		return (VistaRiscossioni) this.xmlToObj(fileName, VistaRiscossioni.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.VistaRiscossioni}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.VistaRiscossioni}
	 * @return Object type {@link it.govpay.orm.VistaRiscossioni}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaRiscossioni readVistaRiscossioni(File file) throws DeserializerException {
		return (VistaRiscossioni) this.xmlToObj(file, VistaRiscossioni.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.VistaRiscossioni}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.VistaRiscossioni}
	 * @return Object type {@link it.govpay.orm.VistaRiscossioni}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaRiscossioni readVistaRiscossioni(InputStream in) throws DeserializerException {
		return (VistaRiscossioni) this.xmlToObj(in, VistaRiscossioni.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.VistaRiscossioni}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.VistaRiscossioni}
	 * @return Object type {@link it.govpay.orm.VistaRiscossioni}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaRiscossioni readVistaRiscossioni(byte[] in) throws DeserializerException {
		return (VistaRiscossioni) this.xmlToObj(in, VistaRiscossioni.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.VistaRiscossioni}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.VistaRiscossioni}
	 * @return Object type {@link it.govpay.orm.VistaRiscossioni}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaRiscossioni readVistaRiscossioniFromString(String in) throws DeserializerException {
		return (VistaRiscossioni) this.xmlToObj(in.getBytes(), VistaRiscossioni.class);
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
	 Object: VistaPagamento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.VistaPagamento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.VistaPagamento}
	 * @return Object type {@link it.govpay.orm.VistaPagamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaPagamento readVistaPagamento(String fileName) throws DeserializerException {
		return (VistaPagamento) this.xmlToObj(fileName, VistaPagamento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.VistaPagamento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.VistaPagamento}
	 * @return Object type {@link it.govpay.orm.VistaPagamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaPagamento readVistaPagamento(File file) throws DeserializerException {
		return (VistaPagamento) this.xmlToObj(file, VistaPagamento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.VistaPagamento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.VistaPagamento}
	 * @return Object type {@link it.govpay.orm.VistaPagamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaPagamento readVistaPagamento(InputStream in) throws DeserializerException {
		return (VistaPagamento) this.xmlToObj(in, VistaPagamento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.VistaPagamento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.VistaPagamento}
	 * @return Object type {@link it.govpay.orm.VistaPagamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaPagamento readVistaPagamento(byte[] in) throws DeserializerException {
		return (VistaPagamento) this.xmlToObj(in, VistaPagamento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.VistaPagamento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.VistaPagamento}
	 * @return Object type {@link it.govpay.orm.VistaPagamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaPagamento readVistaPagamentoFromString(String in) throws DeserializerException {
		return (VistaPagamento) this.xmlToObj(in.getBytes(), VistaPagamento.class);
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
	 Object: id-configurazione
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdConfigurazione}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdConfigurazione}
	 * @return Object type {@link it.govpay.orm.IdConfigurazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdConfigurazione readIdConfigurazione(String fileName) throws DeserializerException {
		return (IdConfigurazione) this.xmlToObj(fileName, IdConfigurazione.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdConfigurazione}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdConfigurazione}
	 * @return Object type {@link it.govpay.orm.IdConfigurazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdConfigurazione readIdConfigurazione(File file) throws DeserializerException {
		return (IdConfigurazione) this.xmlToObj(file, IdConfigurazione.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdConfigurazione}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdConfigurazione}
	 * @return Object type {@link it.govpay.orm.IdConfigurazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdConfigurazione readIdConfigurazione(InputStream in) throws DeserializerException {
		return (IdConfigurazione) this.xmlToObj(in, IdConfigurazione.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdConfigurazione}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdConfigurazione}
	 * @return Object type {@link it.govpay.orm.IdConfigurazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdConfigurazione readIdConfigurazione(byte[] in) throws DeserializerException {
		return (IdConfigurazione) this.xmlToObj(in, IdConfigurazione.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdConfigurazione}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdConfigurazione}
	 * @return Object type {@link it.govpay.orm.IdConfigurazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdConfigurazione readIdConfigurazioneFromString(String in) throws DeserializerException {
		return (IdConfigurazione) this.xmlToObj(in.getBytes(), IdConfigurazione.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Uo
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Uo}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Uo}
	 * @return Object type {@link it.govpay.orm.Uo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Uo readUo(String fileName) throws DeserializerException {
		return (Uo) this.xmlToObj(fileName, Uo.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Uo}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Uo}
	 * @return Object type {@link it.govpay.orm.Uo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Uo readUo(File file) throws DeserializerException {
		return (Uo) this.xmlToObj(file, Uo.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Uo}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Uo}
	 * @return Object type {@link it.govpay.orm.Uo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Uo readUo(InputStream in) throws DeserializerException {
		return (Uo) this.xmlToObj(in, Uo.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Uo}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Uo}
	 * @return Object type {@link it.govpay.orm.Uo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Uo readUo(byte[] in) throws DeserializerException {
		return (Uo) this.xmlToObj(in, Uo.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Uo}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Uo}
	 * @return Object type {@link it.govpay.orm.Uo}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Uo readUoFromString(String in) throws DeserializerException {
		return (Uo) this.xmlToObj(in.getBytes(), Uo.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: ACL
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.ACL}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.ACL}
	 * @return Object type {@link it.govpay.orm.ACL}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ACL readACL(String fileName) throws DeserializerException {
		return (ACL) this.xmlToObj(fileName, ACL.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.ACL}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.ACL}
	 * @return Object type {@link it.govpay.orm.ACL}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ACL readACL(File file) throws DeserializerException {
		return (ACL) this.xmlToObj(file, ACL.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.ACL}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.ACL}
	 * @return Object type {@link it.govpay.orm.ACL}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ACL readACL(InputStream in) throws DeserializerException {
		return (ACL) this.xmlToObj(in, ACL.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.ACL}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.ACL}
	 * @return Object type {@link it.govpay.orm.ACL}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ACL readACL(byte[] in) throws DeserializerException {
		return (ACL) this.xmlToObj(in, ACL.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.ACL}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.ACL}
	 * @return Object type {@link it.govpay.orm.ACL}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public ACL readACLFromString(String in) throws DeserializerException {
		return (ACL) this.xmlToObj(in.getBytes(), ACL.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: TipoVersamentoDominio
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.TipoVersamentoDominio}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.TipoVersamentoDominio}
	 * @return Object type {@link it.govpay.orm.TipoVersamentoDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TipoVersamentoDominio readTipoVersamentoDominio(String fileName) throws DeserializerException {
		return (TipoVersamentoDominio) this.xmlToObj(fileName, TipoVersamentoDominio.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.TipoVersamentoDominio}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.TipoVersamentoDominio}
	 * @return Object type {@link it.govpay.orm.TipoVersamentoDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TipoVersamentoDominio readTipoVersamentoDominio(File file) throws DeserializerException {
		return (TipoVersamentoDominio) this.xmlToObj(file, TipoVersamentoDominio.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.TipoVersamentoDominio}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.TipoVersamentoDominio}
	 * @return Object type {@link it.govpay.orm.TipoVersamentoDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TipoVersamentoDominio readTipoVersamentoDominio(InputStream in) throws DeserializerException {
		return (TipoVersamentoDominio) this.xmlToObj(in, TipoVersamentoDominio.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.TipoVersamentoDominio}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.TipoVersamentoDominio}
	 * @return Object type {@link it.govpay.orm.TipoVersamentoDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TipoVersamentoDominio readTipoVersamentoDominio(byte[] in) throws DeserializerException {
		return (TipoVersamentoDominio) this.xmlToObj(in, TipoVersamentoDominio.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.TipoVersamentoDominio}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.TipoVersamentoDominio}
	 * @return Object type {@link it.govpay.orm.TipoVersamentoDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public TipoVersamentoDominio readTipoVersamentoDominioFromString(String in) throws DeserializerException {
		return (TipoVersamentoDominio) this.xmlToObj(in.getBytes(), TipoVersamentoDominio.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: UtenzaDominio
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.UtenzaDominio}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.UtenzaDominio}
	 * @return Object type {@link it.govpay.orm.UtenzaDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public UtenzaDominio readUtenzaDominio(String fileName) throws DeserializerException {
		return (UtenzaDominio) this.xmlToObj(fileName, UtenzaDominio.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.UtenzaDominio}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.UtenzaDominio}
	 * @return Object type {@link it.govpay.orm.UtenzaDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public UtenzaDominio readUtenzaDominio(File file) throws DeserializerException {
		return (UtenzaDominio) this.xmlToObj(file, UtenzaDominio.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.UtenzaDominio}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.UtenzaDominio}
	 * @return Object type {@link it.govpay.orm.UtenzaDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public UtenzaDominio readUtenzaDominio(InputStream in) throws DeserializerException {
		return (UtenzaDominio) this.xmlToObj(in, UtenzaDominio.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.UtenzaDominio}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.UtenzaDominio}
	 * @return Object type {@link it.govpay.orm.UtenzaDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public UtenzaDominio readUtenzaDominio(byte[] in) throws DeserializerException {
		return (UtenzaDominio) this.xmlToObj(in, UtenzaDominio.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.UtenzaDominio}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.UtenzaDominio}
	 * @return Object type {@link it.govpay.orm.UtenzaDominio}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public UtenzaDominio readUtenzaDominioFromString(String in) throws DeserializerException {
		return (UtenzaDominio) this.xmlToObj(in.getBytes(), UtenzaDominio.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Configurazione
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Configurazione}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Configurazione}
	 * @return Object type {@link it.govpay.orm.Configurazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Configurazione readConfigurazione(String fileName) throws DeserializerException {
		return (Configurazione) this.xmlToObj(fileName, Configurazione.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Configurazione}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Configurazione}
	 * @return Object type {@link it.govpay.orm.Configurazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Configurazione readConfigurazione(File file) throws DeserializerException {
		return (Configurazione) this.xmlToObj(file, Configurazione.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Configurazione}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Configurazione}
	 * @return Object type {@link it.govpay.orm.Configurazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Configurazione readConfigurazione(InputStream in) throws DeserializerException {
		return (Configurazione) this.xmlToObj(in, Configurazione.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Configurazione}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Configurazione}
	 * @return Object type {@link it.govpay.orm.Configurazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Configurazione readConfigurazione(byte[] in) throws DeserializerException {
		return (Configurazione) this.xmlToObj(in, Configurazione.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Configurazione}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Configurazione}
	 * @return Object type {@link it.govpay.orm.Configurazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Configurazione readConfigurazioneFromString(String in) throws DeserializerException {
		return (Configurazione) this.xmlToObj(in.getBytes(), Configurazione.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Notifica
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Notifica}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Notifica}
	 * @return Object type {@link it.govpay.orm.Notifica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Notifica readNotifica(String fileName) throws DeserializerException {
		return (Notifica) this.xmlToObj(fileName, Notifica.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Notifica}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Notifica}
	 * @return Object type {@link it.govpay.orm.Notifica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Notifica readNotifica(File file) throws DeserializerException {
		return (Notifica) this.xmlToObj(file, Notifica.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Notifica}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Notifica}
	 * @return Object type {@link it.govpay.orm.Notifica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Notifica readNotifica(InputStream in) throws DeserializerException {
		return (Notifica) this.xmlToObj(in, Notifica.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Notifica}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Notifica}
	 * @return Object type {@link it.govpay.orm.Notifica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Notifica readNotifica(byte[] in) throws DeserializerException {
		return (Notifica) this.xmlToObj(in, Notifica.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Notifica}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Notifica}
	 * @return Object type {@link it.govpay.orm.Notifica}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Notifica readNotificaFromString(String in) throws DeserializerException {
		return (Notifica) this.xmlToObj(in.getBytes(), Notifica.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Pagamento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Pagamento}
	 * @return Object type {@link it.govpay.orm.Pagamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Pagamento readPagamento(String fileName) throws DeserializerException {
		return (Pagamento) this.xmlToObj(fileName, Pagamento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Pagamento}
	 * @return Object type {@link it.govpay.orm.Pagamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Pagamento readPagamento(File file) throws DeserializerException {
		return (Pagamento) this.xmlToObj(file, Pagamento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Pagamento}
	 * @return Object type {@link it.govpay.orm.Pagamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Pagamento readPagamento(InputStream in) throws DeserializerException {
		return (Pagamento) this.xmlToObj(in, Pagamento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Pagamento}
	 * @return Object type {@link it.govpay.orm.Pagamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Pagamento readPagamento(byte[] in) throws DeserializerException {
		return (Pagamento) this.xmlToObj(in, Pagamento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Pagamento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Pagamento}
	 * @return Object type {@link it.govpay.orm.Pagamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Pagamento readPagamentoFromString(String in) throws DeserializerException {
		return (Pagamento) this.xmlToObj(in.getBytes(), Pagamento.class);
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
	 Object: id-tracciato-notifica-pagamenti
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdTracciatoNotificaPagamenti}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdTracciatoNotificaPagamenti}
	 * @return Object type {@link it.govpay.orm.IdTracciatoNotificaPagamenti}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTracciatoNotificaPagamenti readIdTracciatoNotificaPagamenti(String fileName) throws DeserializerException {
		return (IdTracciatoNotificaPagamenti) this.xmlToObj(fileName, IdTracciatoNotificaPagamenti.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdTracciatoNotificaPagamenti}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdTracciatoNotificaPagamenti}
	 * @return Object type {@link it.govpay.orm.IdTracciatoNotificaPagamenti}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTracciatoNotificaPagamenti readIdTracciatoNotificaPagamenti(File file) throws DeserializerException {
		return (IdTracciatoNotificaPagamenti) this.xmlToObj(file, IdTracciatoNotificaPagamenti.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdTracciatoNotificaPagamenti}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdTracciatoNotificaPagamenti}
	 * @return Object type {@link it.govpay.orm.IdTracciatoNotificaPagamenti}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTracciatoNotificaPagamenti readIdTracciatoNotificaPagamenti(InputStream in) throws DeserializerException {
		return (IdTracciatoNotificaPagamenti) this.xmlToObj(in, IdTracciatoNotificaPagamenti.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdTracciatoNotificaPagamenti}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdTracciatoNotificaPagamenti}
	 * @return Object type {@link it.govpay.orm.IdTracciatoNotificaPagamenti}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTracciatoNotificaPagamenti readIdTracciatoNotificaPagamenti(byte[] in) throws DeserializerException {
		return (IdTracciatoNotificaPagamenti) this.xmlToObj(in, IdTracciatoNotificaPagamenti.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdTracciatoNotificaPagamenti}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdTracciatoNotificaPagamenti}
	 * @return Object type {@link it.govpay.orm.IdTracciatoNotificaPagamenti}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdTracciatoNotificaPagamenti readIdTracciatoNotificaPagamentiFromString(String in) throws DeserializerException {
		return (IdTracciatoNotificaPagamenti) this.xmlToObj(in.getBytes(), IdTracciatoNotificaPagamenti.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: VistaVersamento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.VistaVersamento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.VistaVersamento}
	 * @return Object type {@link it.govpay.orm.VistaVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaVersamento readVistaVersamento(String fileName) throws DeserializerException {
		return (VistaVersamento) this.xmlToObj(fileName, VistaVersamento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.VistaVersamento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.VistaVersamento}
	 * @return Object type {@link it.govpay.orm.VistaVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaVersamento readVistaVersamento(File file) throws DeserializerException {
		return (VistaVersamento) this.xmlToObj(file, VistaVersamento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.VistaVersamento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.VistaVersamento}
	 * @return Object type {@link it.govpay.orm.VistaVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaVersamento readVistaVersamento(InputStream in) throws DeserializerException {
		return (VistaVersamento) this.xmlToObj(in, VistaVersamento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.VistaVersamento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.VistaVersamento}
	 * @return Object type {@link it.govpay.orm.VistaVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaVersamento readVistaVersamento(byte[] in) throws DeserializerException {
		return (VistaVersamento) this.xmlToObj(in, VistaVersamento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.VistaVersamento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.VistaVersamento}
	 * @return Object type {@link it.govpay.orm.VistaVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public VistaVersamento readVistaVersamentoFromString(String in) throws DeserializerException {
		return (VistaVersamento) this.xmlToObj(in.getBytes(), VistaVersamento.class);
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
	 Object: UtenzaTipoVersamento
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.UtenzaTipoVersamento}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.UtenzaTipoVersamento}
	 * @return Object type {@link it.govpay.orm.UtenzaTipoVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public UtenzaTipoVersamento readUtenzaTipoVersamento(String fileName) throws DeserializerException {
		return (UtenzaTipoVersamento) this.xmlToObj(fileName, UtenzaTipoVersamento.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.UtenzaTipoVersamento}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.UtenzaTipoVersamento}
	 * @return Object type {@link it.govpay.orm.UtenzaTipoVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public UtenzaTipoVersamento readUtenzaTipoVersamento(File file) throws DeserializerException {
		return (UtenzaTipoVersamento) this.xmlToObj(file, UtenzaTipoVersamento.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.UtenzaTipoVersamento}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.UtenzaTipoVersamento}
	 * @return Object type {@link it.govpay.orm.UtenzaTipoVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public UtenzaTipoVersamento readUtenzaTipoVersamento(InputStream in) throws DeserializerException {
		return (UtenzaTipoVersamento) this.xmlToObj(in, UtenzaTipoVersamento.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.UtenzaTipoVersamento}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.UtenzaTipoVersamento}
	 * @return Object type {@link it.govpay.orm.UtenzaTipoVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public UtenzaTipoVersamento readUtenzaTipoVersamento(byte[] in) throws DeserializerException {
		return (UtenzaTipoVersamento) this.xmlToObj(in, UtenzaTipoVersamento.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.UtenzaTipoVersamento}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.UtenzaTipoVersamento}
	 * @return Object type {@link it.govpay.orm.UtenzaTipoVersamento}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public UtenzaTipoVersamento readUtenzaTipoVersamentoFromString(String in) throws DeserializerException {
		return (UtenzaTipoVersamento) this.xmlToObj(in.getBytes(), UtenzaTipoVersamento.class);
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
	 Object: Batch
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Batch}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Batch}
	 * @return Object type {@link it.govpay.orm.Batch}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Batch readBatch(String fileName) throws DeserializerException {
		return (Batch) this.xmlToObj(fileName, Batch.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Batch}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Batch}
	 * @return Object type {@link it.govpay.orm.Batch}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Batch readBatch(File file) throws DeserializerException {
		return (Batch) this.xmlToObj(file, Batch.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Batch}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Batch}
	 * @return Object type {@link it.govpay.orm.Batch}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Batch readBatch(InputStream in) throws DeserializerException {
		return (Batch) this.xmlToObj(in, Batch.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Batch}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Batch}
	 * @return Object type {@link it.govpay.orm.Batch}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Batch readBatch(byte[] in) throws DeserializerException {
		return (Batch) this.xmlToObj(in, Batch.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Batch}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Batch}
	 * @return Object type {@link it.govpay.orm.Batch}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Batch readBatchFromString(String in) throws DeserializerException {
		return (Batch) this.xmlToObj(in.getBytes(), Batch.class);
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
	 Object: PagamentoPortale
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.PagamentoPortale}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.PagamentoPortale}
	 * @return Object type {@link it.govpay.orm.PagamentoPortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public PagamentoPortale readPagamentoPortale(String fileName) throws DeserializerException {
		return (PagamentoPortale) this.xmlToObj(fileName, PagamentoPortale.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.PagamentoPortale}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.PagamentoPortale}
	 * @return Object type {@link it.govpay.orm.PagamentoPortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public PagamentoPortale readPagamentoPortale(File file) throws DeserializerException {
		return (PagamentoPortale) this.xmlToObj(file, PagamentoPortale.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.PagamentoPortale}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.PagamentoPortale}
	 * @return Object type {@link it.govpay.orm.PagamentoPortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public PagamentoPortale readPagamentoPortale(InputStream in) throws DeserializerException {
		return (PagamentoPortale) this.xmlToObj(in, PagamentoPortale.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.PagamentoPortale}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.PagamentoPortale}
	 * @return Object type {@link it.govpay.orm.PagamentoPortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public PagamentoPortale readPagamentoPortale(byte[] in) throws DeserializerException {
		return (PagamentoPortale) this.xmlToObj(in, PagamentoPortale.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.PagamentoPortale}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.PagamentoPortale}
	 * @return Object type {@link it.govpay.orm.PagamentoPortale}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public PagamentoPortale readPagamentoPortaleFromString(String in) throws DeserializerException {
		return (PagamentoPortale) this.xmlToObj(in.getBytes(), PagamentoPortale.class);
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
	 Object: id-vista-riscossione
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.IdVistaRiscossione}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdVistaRiscossione}
	 * @return Object type {@link it.govpay.orm.IdVistaRiscossione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdVistaRiscossione readIdVistaRiscossione(String fileName) throws DeserializerException {
		return (IdVistaRiscossione) this.xmlToObj(fileName, IdVistaRiscossione.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.IdVistaRiscossione}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.IdVistaRiscossione}
	 * @return Object type {@link it.govpay.orm.IdVistaRiscossione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdVistaRiscossione readIdVistaRiscossione(File file) throws DeserializerException {
		return (IdVistaRiscossione) this.xmlToObj(file, IdVistaRiscossione.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.IdVistaRiscossione}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.IdVistaRiscossione}
	 * @return Object type {@link it.govpay.orm.IdVistaRiscossione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdVistaRiscossione readIdVistaRiscossione(InputStream in) throws DeserializerException {
		return (IdVistaRiscossione) this.xmlToObj(in, IdVistaRiscossione.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.IdVistaRiscossione}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.IdVistaRiscossione}
	 * @return Object type {@link it.govpay.orm.IdVistaRiscossione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdVistaRiscossione readIdVistaRiscossione(byte[] in) throws DeserializerException {
		return (IdVistaRiscossione) this.xmlToObj(in, IdVistaRiscossione.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.IdVistaRiscossione}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.IdVistaRiscossione}
	 * @return Object type {@link it.govpay.orm.IdVistaRiscossione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public IdVistaRiscossione readIdVistaRiscossioneFromString(String in) throws DeserializerException {
		return (IdVistaRiscossione) this.xmlToObj(in.getBytes(), IdVistaRiscossione.class);
	}	
	
	
	
	/*
	 =================================================================================
	 Object: Rendicontazione
	 =================================================================================
	*/
	
	/**
	 * Transform the xml in <var>fileName</var> in the object type {@link it.govpay.orm.Rendicontazione}
	 * 
	 * @param fileName Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Rendicontazione}
	 * @return Object type {@link it.govpay.orm.Rendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Rendicontazione readRendicontazione(String fileName) throws DeserializerException {
		return (Rendicontazione) this.xmlToObj(fileName, Rendicontazione.class);
	}
	
	/**
	 * Transform the xml in <var>file</var> in the object type {@link it.govpay.orm.Rendicontazione}
	 * 
	 * @param file Xml file to use for the reconstruction of the object type {@link it.govpay.orm.Rendicontazione}
	 * @return Object type {@link it.govpay.orm.Rendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Rendicontazione readRendicontazione(File file) throws DeserializerException {
		return (Rendicontazione) this.xmlToObj(file, Rendicontazione.class);
	}
	
	/**
	 * Transform the input stream <var>in</var> in the object type {@link it.govpay.orm.Rendicontazione}
	 * 
	 * @param in InputStream to use for the reconstruction of the object type {@link it.govpay.orm.Rendicontazione}
	 * @return Object type {@link it.govpay.orm.Rendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Rendicontazione readRendicontazione(InputStream in) throws DeserializerException {
		return (Rendicontazione) this.xmlToObj(in, Rendicontazione.class);
	}	
	
	/**
	 * Transform the byte array <var>in</var> in the object type {@link it.govpay.orm.Rendicontazione}
	 * 
	 * @param in Byte array to use for the reconstruction of the object type {@link it.govpay.orm.Rendicontazione}
	 * @return Object type {@link it.govpay.orm.Rendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Rendicontazione readRendicontazione(byte[] in) throws DeserializerException {
		return (Rendicontazione) this.xmlToObj(in, Rendicontazione.class);
	}	
	
	/**
	 * Transform the String <var>in</var> in the object type {@link it.govpay.orm.Rendicontazione}
	 * 
	 * @param in String to use for the reconstruction of the object type {@link it.govpay.orm.Rendicontazione}
	 * @return Object type {@link it.govpay.orm.Rendicontazione}
	 * @throws DeserializerException The exception that is thrown when an error occurs during deserialization
	 */
	public Rendicontazione readRendicontazioneFromString(String in) throws DeserializerException {
		return (Rendicontazione) this.xmlToObj(in.getBytes(), Rendicontazione.class);
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
