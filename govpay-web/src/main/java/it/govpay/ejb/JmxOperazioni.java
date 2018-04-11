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
package it.govpay.ejb;

import java.util.Iterator;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.JMException;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.NotificationBroadcasterSupport;
import javax.management.ReflectionException;

import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.jmx.CostantiJMX;
import org.openspcoop2.utils.jmx.GestoreRisorseJMX;
import org.openspcoop2.utils.jmx.RisorseJMXException;


public class JmxOperazioni extends NotificationBroadcasterSupport implements DynamicMBean {
	
	private static GestoreRisorseJMX gestoreJMX;

	public final static String ACQUISIZIONE_RENDICONTAZIONI = "acquisizioneRendicontazioni";
	public final static String AGGIORNAMENTO_REGISTRO_PSP = "aggiornamentoRegistroPsp";
	public final static String RECUPERO_RPT_PENDENTI = "recuperoRptPendenti";
	public final static String SPEDIZIONE_NOTIFICHE = "spedizioneNotifiche";
//	public final static String RECUPERO_TRACCIATI_PENDENTI = "recuperoTracciatiPendenti";
//	public final static String ATTIVAZIONE_RECUPERO_TRACCIATI_PENDENTI = "attivazioneRecuperoTracciatiPendenti";
	public final static String RESET_CACHE_ANAGRAFICA = "resetCacheAnagrafica";
//	public final static String ESTRATTO_CONTO = "estrattoConto";
	public final static String GENERAZIONE_AVVISI_PAGAMENTO = "generaAvvisiPagamento";
	public final static String ATTIVAZIONE_GENERAZIONE_AVVISI_PAGAMENTO = "attivazioneGenerazioneAvvisiPagamento";

	/** getAttribute */
	@Override
	public Object getAttribute(String attributeName) throws AttributeNotFoundException, MBeanException, ReflectionException{
		throw new AttributeNotFoundException("Attributo "+attributeName+" non trovato");
	}

	/** getAttributes */
	@Override
	public AttributeList getAttributes(String [] attributesNames){
		if(attributesNames==null) throw new IllegalArgumentException("Array nullo");
		AttributeList list = new AttributeList();
		return list;
	}

	/** setAttribute */
	@Override
	public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException {
		if( attribute==null ) throw new IllegalArgumentException("Il nome dell'attributo e' nullo");
		throw new AttributeNotFoundException("Attributo "+attribute.getName()+" non trovato");
	}

	/** setAttributes */
	@Override
	public AttributeList setAttributes(AttributeList list){
		if(list==null) throw new IllegalArgumentException("Lista degli attributi e' nulla");

		AttributeList ret = new AttributeList();
		Iterator<?> it = ret.iterator();

		while(it.hasNext()){
			try{
				Attribute attribute = (Attribute) it.next();
				setAttribute(attribute);
				ret.add(attribute);
			}catch(JMException ex){}
		}
		return ret;
	}

	/** invoke */
	@Override
	public Object invoke(String actionName, Object[]params, String[]signature) throws MBeanException,ReflectionException{

		if( (actionName==null) || (actionName.equals("")) )
			throw new IllegalArgumentException("Nessuna operazione definita");

		if(actionName.equals(ACQUISIZIONE_RENDICONTAZIONI)){
			 return it.govpay.core.business.Operazioni.acquisizioneRendicontazioni("JmxCall");
		}

		if(actionName.equals(AGGIORNAMENTO_REGISTRO_PSP)){
			return it.govpay.core.business.Operazioni.aggiornamentoRegistroPsp("JmxCall");
		}

		if(actionName.equals(RECUPERO_RPT_PENDENTI)){
			return it.govpay.core.business.Operazioni.recuperoRptPendenti("JmxCall");
		}

		if(actionName.equals(RESET_CACHE_ANAGRAFICA)){
			return it.govpay.core.business.Operazioni.resetCacheAnagrafica();
		}
		
		if(actionName.equals(SPEDIZIONE_NOTIFICHE)){
			return it.govpay.core.business.Operazioni.spedizioneNotifiche("JmxCall");
		}

		if(actionName.equals(GENERAZIONE_AVVISI_PAGAMENTO)){
			it.govpay.core.business.Operazioni.setEseguiGenerazioneAvvisi();
			return "Generazione Avvisi Pagamento schedulata";
		}
		
		if(actionName.equals(ATTIVAZIONE_GENERAZIONE_AVVISI_PAGAMENTO)){
			it.govpay.core.business.Operazioni.setEseguiGenerazioneAvvisi();
			return "Generazione Avvisi Pagamento schedulata";
		}

		throw new UnsupportedOperationException("Operazione "+actionName+" sconosciuta");
	}

	/* MBean info */
	@Override
	public MBeanInfo getMBeanInfo(){
		try{
			// Descrizione della classe nel MBean
			String className = this.getClass().getName();
			String description = "Operazioni di amministrazione ";


			// MetaData per l'operazione 
			MBeanOperationInfo acquisizioneRendicontazioniOP
			= new MBeanOperationInfo(ACQUISIZIONE_RENDICONTAZIONI,"Acquisisci rendicontazioni",
					null,
					String.class.getName(),
					MBeanOperationInfo.ACTION);

			// MetaData per l'operazione 
			MBeanOperationInfo aggiornamentoRegistroPspOP
			= new MBeanOperationInfo(AGGIORNAMENTO_REGISTRO_PSP,"Aggiorna registro PSP",
					null,
					String.class.getName(),
					MBeanOperationInfo.ACTION);

			// MetaData per l'operazione 
			MBeanOperationInfo recuperoRptPendentiOP
			= new MBeanOperationInfo(RECUPERO_RPT_PENDENTI,"Recupera RPT Pendenti",
					null,
					String.class.getName(),
					MBeanOperationInfo.ACTION);

			// MetaData per l'operazione 
			MBeanOperationInfo spedizioneEsitiOP
			= new MBeanOperationInfo(SPEDIZIONE_NOTIFICHE,"Spedisci notifiche",
					null,
					String.class.getName(),
					MBeanOperationInfo.ACTION);

			// MetaData per l'operazione 
			MBeanOperationInfo resetCacheAnagraficaOP
			= new MBeanOperationInfo(RESET_CACHE_ANAGRAFICA,"Reset cache anagrafica",
					null,
					String.class.getName(),
					MBeanOperationInfo.ACTION);
			
			// Mbean costruttore
			MBeanConstructorInfo defaultConstructor = new MBeanConstructorInfo("Default Constructor","Crea e inizializza una nuova istanza del MBean",null);

			// Lista Costruttori
			MBeanConstructorInfo[] constructors = new MBeanConstructorInfo[]{defaultConstructor};

			// Lista operazioni
			MBeanOperationInfo[] operations = new MBeanOperationInfo[]{acquisizioneRendicontazioniOP, aggiornamentoRegistroPspOP, recuperoRptPendentiOP, spedizioneEsitiOP, resetCacheAnagraficaOP};

			return new MBeanInfo(className,description,null,constructors,operations,null);

		}catch(Exception e){
			throw new RuntimeException(e.getMessage(),e);
		}
	}
	
	protected static void register() throws RisorseJMXException{
		gestoreJMX = new GestoreRisorseJMX(LoggerWrapperFactory.getLogger(JmxOperazioni.class));
		gestoreJMX.registerMBean(JmxOperazioni.class, "it.govpay.core", CostantiJMX.JMX_TYPE, "operazioni");
	}
	
	protected static void unregister() throws RisorseJMXException{
		gestoreJMX.unregisterMBeans();
	}
	
}
