package it.govpay.rs.v1.beans.converter;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.core.rs.v1.beans.Pagamento;

public class PagamentiConverter {

	
	public  static Pagamento toRsModel(it.govpay.bd.model.Pagamento p, BasicBD bd) throws ServiceException {
		Pagamento rsModel = new Pagamento();
		rsModel.setDominio(p.getCodDominio());
		rsModel.setIuv(p.getIuv());
		rsModel.setIur(p.getIur());
		rsModel.setImporto(p.getImportoPagato());
		rsModel.setData_pagamento(p.getDataPagamento());
		rsModel.setId_applicazione(p.getSingoloVersamento(bd).getVersamento(bd).getApplicazione(bd).getCodApplicazione());
		rsModel.setId_versamento_ente(p.getSingoloVersamento(bd).getVersamento(bd).getCodVersamentoEnte());
		rsModel.setId_singolo_versamento_ente(p.getSingoloVersamento(bd).getCodSingoloVersamentoEnte());
		
		return rsModel;
	}
}
