/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2026 Link.it srl (http://www.link.it).
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
package it.govpay.core.business;

import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.LoggerWrapperFactory;
import org.slf4j.Logger;

import gov.telematici.pagamenti.ws.rpt.FaultBean;
import it.govpay.bd.BDConfigWrapper;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.filters.RptFilter;
import it.govpay.core.utils.LogUtils;
import it.govpay.model.Canale.ModelloPagamento;
import it.govpay.model.Rpt.StatoRpt;

public class Rpt {

	private static Logger log = LoggerWrapperFactory.getLogger(Rpt.class);

	public Rpt() {
		//nothing
	}

	public void annullaRPTPendenti(String codDominio, String iuv, BDConfigWrapper configWrapper)  throws ServiceException {
		RptBD rptBD = null;
		try {
			rptBD = new RptBD(configWrapper);

			rptBD.setupConnection(configWrapper.getTransactionID());

			rptBD.setAtomica(false);

			rptBD.setAutoCommit(false);

			rptBD.enableSelectForUpdate();

			RptFilter filter = rptBD.newFilter();
			filter.setCodDominio(codDominio);
			filter.setIuv(iuv);
			filter.setStato(it.govpay.model.Rpt.getStatiPendenti());
			filter.setModelloPagamento(ModelloPagamento.ATTIVATO_PRESSO_PSP.getCodifica()+"");


			List<it.govpay.bd.model.Rpt> listaRptDaAnnullare = rptBD.findAll(filter );

			for (it.govpay.bd.model.Rpt rpt : listaRptDaAnnullare) {
				rpt.setStato(StatoRpt.RPT_ANNULLATA);
				rpt.setDescrizioneStato("Ricevuta richiesta di un nuovo tentativo di pagamento");

				try {
					rptBD.updateRpt(rpt);
					rptBD.commit();
					log.info("RPT [idDominio:{}][iuv:{}][ccp:{}] annullata con successo.", rpt.getCodDominio(), rpt.getIuv(), rpt.getCcp());
				} catch(ServiceException e) {
					rptBD.rollback();
					LogUtils.logError(log, "Errore durante l'annullamento della RPT [idDominio:"+rpt.getCodDominio()+"][iuv:"+rpt.getIuv()+"][ccp:"+rpt.getCcp()+"]: " +e .getMessage(), e);
					throw e;
				} 
			}

		}finally {
			rptBD.disableSelectForUpdate();

			// ripristino autocommit
			if(!rptBD.isAutoCommit() ) {
				rptBD.setAutoCommit(true);
			}

			rptBD.closeConnection();
		}
	}

	public static it.govpay.core.exceptions.FaultBean toFaultBean(FaultBean faultBean) {
		if(faultBean == null) return null;

		it.govpay.core.exceptions.FaultBean toRet = new it.govpay.core.exceptions.FaultBean();

		toRet.setDescription(faultBean.getDescription());
		toRet.setFaultCode(faultBean.getFaultCode());
		toRet.setFaultString(faultBean.getFaultString());
		toRet.setId(faultBean.getId());
		toRet.setOriginalDescription(faultBean.getOriginalDescription());
		toRet.setOriginalFaultCode(faultBean.getOriginalFaultCode());
		toRet.setOriginalFaultString(faultBean.getOriginalFaultString());
		toRet.setSerial(faultBean.getSerial());
		return toRet;
	}
}
