/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.bd.pagamento;

import java.text.MessageFormat;
import java.util.Date;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.openspcoop2.utils.UtilsException;
import org.openspcoop2.utils.id.serial.IDSerialGeneratorType;
import org.openspcoop2.utils.id.serial.InfoStatistics;
import org.slf4j.Logger;

import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Applicazione;
import it.govpay.bd.model.Dominio;
import it.govpay.bd.pagamento.util.IuvUtils;
import it.govpay.model.Iuv;
import it.govpay.model.Iuv.TipoIUV;

public class IuvBD extends BasicBD {

	private static final String SUPERATO_IL_NUMERO_MASSIMO_DI_IUV_GENERABILI_DOMINIO_0_PREFISSO_1 = "Superato il numero massimo di IUV generabili [Dominio:{0} Prefisso:{1}]";
	private static Logger log = LoggerWrapperFactory.getLogger(IuvBD.class);

	public IuvBD(BasicBD basicBD) {
		super(basicBD);
	}

	public IuvBD(String idTransaction) {
		super(idTransaction);
	}

	public IuvBD(String idTransaction, boolean useCache) {
		super(idTransaction, useCache);
	}

	public IuvBD(BDConfigWrapper configWrapper) {
		super(configWrapper.getTransactionID(), configWrapper.isUseCache());
	}

	public Iuv generaIuv(Applicazione applicazione, Dominio dominio, String codVersamentoEnte, TipoIUV type, String prefix) throws ServiceException {

		String iuv = null;
		long prg = 0;
		try {
			if(type.equals(TipoIUV.ISO11694)) {
				throw new ServiceException("Generazione IUV ISO11694 non supportata.");
			}
			
			if(this.isAtomica()) {
				this.setupConnection(this.getIdTransaction());
			}
			
			prg = this.getNextPrgIuv(dominio.getCodDominio() + prefix, type);

			String check = "";
			String reference = "";
			// Vedo se utilizzare l'application code o il segregation code
			switch (dominio.getAuxDigit()) {
			case 0: 
				reference = prefix + String.format("%0" + (13 - prefix.length()) + "d", prg);
				if(reference.length() > 15) 
					throw new ServiceException(MessageFormat.format(SUPERATO_IL_NUMERO_MASSIMO_DI_IUV_GENERABILI_DOMINIO_0_PREFISSO_1, dominio.getCodDominio(), prefix) );
				check = IuvUtils.getCheckDigit93(reference, dominio.getAuxDigit(), dominio.getStazione().getApplicationCode()); 
				iuv = reference + check;
				break;
			case 1: 
			case 2: 
				reference = prefix + String.format("%0" + (15 - prefix.length()) + "d", prg);
				if(reference.length() > 15) 
					throw new ServiceException(MessageFormat.format(SUPERATO_IL_NUMERO_MASSIMO_DI_IUV_GENERABILI_DOMINIO_0_PREFISSO_1, dominio.getCodDominio(), prefix) );
				check = IuvUtils.getCheckDigit93(reference, dominio.getAuxDigit()); 
				iuv = reference + check;
				break;
			case 3: 
				reference = prefix + String.format("%0" + (13 - prefix.length()) + "d", prg);
				if(reference.length() > 15) 
					throw new ServiceException(MessageFormat.format(SUPERATO_IL_NUMERO_MASSIMO_DI_IUV_GENERABILI_DOMINIO_0_PREFISSO_1, dominio.getCodDominio(), prefix) );
				if(dominio.getSegregationCode() == null)
					throw new ServiceException("Dominio configurato per IUV segregati privo di codice di segregazione [Dominio:"+dominio.getCodDominio()+"]" ); 
				check = IuvUtils.getCheckDigit93(reference, dominio.getAuxDigit(), dominio.getSegregationCode().intValue()); 
				iuv = String.format("%02d", dominio.getSegregationCode()) + reference + check;
				break;
			default: throw new ServiceException("Codice AUX non supportato [Dominio:"+dominio.getCodDominio()+" AuxDigit:"+dominio.getAuxDigit()+"]" ); 
			}
		} finally {
			if(this.isAtomica()) {	
				this.closeConnection();
			}
		}

		Iuv iuvDTO = new Iuv();
		iuvDTO.setIdDominio(dominio.getId());
		iuvDTO.setPrg(prg);
		iuvDTO.setIuv(iuv);
		iuvDTO.setDataGenerazione(new Date());
		iuvDTO.setIdApplicazione(applicazione.getId());
		iuvDTO.setTipo(type);
		iuvDTO.setCodVersamentoEnte(codVersamentoEnte);
		iuvDTO.setAuxDigit(dominio.getAuxDigit());
		iuvDTO.setApplicationCode(dominio.getStazione().getApplicationCode());

		return iuvDTO;
	}


	/**
	 * Crea un nuovo IUV a meno dell'iuv stesso.
	 * Il prg deve essere un progressivo all'interno del DominioEnte fornito
	 * 
	 * @param codDominio
	 * @param idApplicazione
	 * @return prg
	 * @throws ServiceException
	 */
	private long getNextPrgIuv(String codDominio, TipoIUV type) throws ServiceException {
		InfoStatistics infoStat = new InfoStatistics();
		BasicBD bd = null;
		try {
			org.openspcoop2.utils.id.serial.IDSerialGenerator serialGenerator = new org.openspcoop2.utils.id.serial.IDSerialGenerator(infoStat);
			org.openspcoop2.utils.id.serial.IDSerialGeneratorParameter params = new org.openspcoop2.utils.id.serial.IDSerialGeneratorParameter("GovPay");
			params.setSizeBuffer(100);
			params.setTipo(IDSerialGeneratorType.NUMERIC);
			params.setWrap(false);
			params.setInformazioneAssociataAlProgressivo(codDominio+type.toString()); // il progressivo sarà relativo a questa informazione

			java.sql.Connection con = null; 

			// Se sono in transazione aperta, utilizzo una connessione diversa perche' l'utility di generazione non supporta le transazioni.
			if(!this.isAutoCommit()) {
				bd = BasicBD.newInstance(this.getIdTransaction());
				
				bd.setupConnection(this.getIdTransaction()); 
				
				con = bd.getConnection();
			} else {
				con = this.getConnection();
			}
			return serialGenerator.buildIDAsNumber(params, con, this.getJdbcProperties().getDatabase(), log);
		} catch (UtilsException e) {
			log.error("Numero di errori 'access serializable': {}", infoStat.getErrorSerializableAccess());
			for (int i=0; i<infoStat.getExceptionOccurs().size(); i++) {
				Throwable t = infoStat.getExceptionOccurs().get(i);
				log.error("Errore-{} (occurs:{}): {}",(i+1), infoStat.getNumber(t), t.getMessage());
			}
			throw new ServiceException(e);
		} finally {
			if(bd != null) bd.closeConnection();
		}
	}
}
