/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2015 Link.it srl (http://www.link.it).
 * Copyright (c) 2014-2015 TAS S.p.A. (http://www.tasgroup.it).
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
package it.govpay.ndp.ejb;

import it.govpay.ndp.model.impl.ERModel;
import it.govpay.ndp.model.impl.RPTModel;
import it.govpay.ndp.model.impl.RRModel;
import it.govpay.ndp.model.impl.RTModel;
import it.govpay.ndp.model.impl.RxModel;
import it.govpay.orm.gde.GdeDocumentiNdp;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.logging.log4j.Logger;

@Stateless
public class DocumentiEJB {

	@PersistenceContext(unitName = "GovPayJta")
	private EntityManager entityManager;
	
	@Inject 
	private transient Logger log;

	public void archiviaDocumento(RxModel documento) {
		log.info("Archiviazione in corso. RxModel [" + documento.getIdDominio() + "][" + documento.getIuv() + "][" + documento.getCcp() + "][" + documento.getTipoDocumento().name() + "]");
		GdeDocumentiNdp documentiNdp = new GdeDocumentiNdp();
		documentiNdp.setCodiceContestoPagamento(documento.getCcp());
		documentiNdp.setDimensione(documento.getBytes().length);
		documentiNdp.setDocumento(documento.getBytes());
		documentiNdp.setIdDominio(documento.getIdDominio());
		documentiNdp.setIdUnivocoVersamento(documento.getIuv());
		// TODO: [ST] tentativo come?
		documentiNdp.setTentativo(0);
		documentiNdp.setTipo(documento.getTipoDocumento().name());
		entityManager.persist(documentiNdp);
	}
	
	private RxModel recuperaDocumento(RxModel.TipoDocumento tipoDocumento, String idDominio, String iuv, String ccp) {
		log.info("Recupero in corso. [" + idDominio + "][" + iuv + "][" + ccp + "][" + tipoDocumento.name() + "]");
		TypedQuery<GdeDocumentiNdp> query = entityManager.createNamedQuery("getGdeDocNdp", GdeDocumentiNdp.class);
		query.setParameter("idDominio", idDominio);
		query.setParameter("iuv", iuv);
		query.setParameter("ccp", ccp);
		query.setParameter("tipo", tipoDocumento.name());
		query.setMaxResults(1);

		try {
			GdeDocumentiNdp docNdp = query.getSingleResult();
			switch (tipoDocumento) {
			case RP:
				return RxModel.buildRxDocument(RPTModel.class, idDominio, iuv, ccp, docNdp.getDocumento());
			case RT:
				return RxModel.buildRxDocument(RTModel.class, idDominio, iuv, ccp, docNdp.getDocumento());
			case RR:
				return RxModel.buildRxDocument(RRModel.class, idDominio, iuv, ccp, docNdp.getDocumento());
			case ER:
				return RxModel.buildRxDocument(ERModel.class, idDominio, iuv, ccp, docNdp.getDocumento());
			}
		} catch (NoResultException e) {
			return null;
		}
		return null;
	}
	
	public RPTModel recuperaRPT(String idDominio, String iuv, String ccp) {
		log.info("Recupero RPT in corso. [" + idDominio + "][" + iuv + "][" + ccp + "]");
		return (RPTModel) recuperaDocumento(RxModel.TipoDocumento.RP, idDominio, iuv, ccp);
	}
	
	public RTModel recuperaRT(String idDominio, String iuv, String ccp) {
		log.info("Recupero RPT in corso. [" + idDominio + "][" + iuv + "][" + ccp + "]");
		return (RTModel) recuperaDocumento(RxModel.TipoDocumento.RT, idDominio, iuv, ccp);
	}
	
	public RRModel recuperaRR(String idDominio, String iuv, String ccp) {
		log.info("Recupero RPT in corso. [" + idDominio + "][" + iuv + "][" + ccp + "]");
		return (RRModel) recuperaDocumento(RxModel.TipoDocumento.RR, idDominio, iuv, ccp);
	}
}
