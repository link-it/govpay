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
package it.govpay.dars.bd;

import it.govpay.bd.AbstractFilter;
import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.EntiBD;
import it.govpay.bd.anagrafica.PspBD;
import it.govpay.bd.mail.MailBD;
import it.govpay.bd.model.Ente;
import it.govpay.bd.model.Esito;
import it.govpay.bd.model.Mail;
import it.govpay.bd.model.Psp;
import it.govpay.bd.model.Psp.Canale;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rt;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.EsitiBD;
import it.govpay.bd.pagamento.RptBD;
import it.govpay.bd.pagamento.RtBD;
import it.govpay.bd.pagamento.VersamentiBD;
import it.govpay.bd.pagamento.VersamentoFilter;
import it.govpay.bd.registro.EventiBD;
import it.govpay.dars.model.ListaApplicazioniEntry;
import it.govpay.dars.model.ListaPagamentiEntry;
import it.govpay.dars.model.Pagamento;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.SortOrder;

public class PagamentiBD extends BasicBD {

	public PagamentiBD(BasicBD basicBD) {
		super(basicBD);
	}

	public List<ListaPagamentiEntry> findAll(VersamentoFilter filter) throws ServiceException {
		try {
			List<it.govpay.orm.Ente> lstenteVO = this.getEnteService().findAll(this.getEnteService().newPaginatedExpression());
			List<ListaPagamentiEntry> versamentoLst = new ArrayList<ListaPagamentiEntry>();
			List<it.govpay.orm.Versamento> versamentoVOLst = this.getVersamentoService().findAll(filter.toPaginatedExpression()); 
			for(it.govpay.orm.Versamento versamentoVO: versamentoVOLst) {
				ListaPagamentiEntry entry = new ListaPagamentiEntry();
				entry.setDataOraUltimoAggiornamento(versamentoVO.getDataOraUltimoAggiornamento());
				entry.setId(versamentoVO.getId());
				for(it.govpay.orm.Ente ente : lstenteVO)
					if(ente.getId() == versamentoVO.getIdEnte().getId()) entry.setEnte(ente.getCodEnte());
				entry.setIuv(versamentoVO.getCodVersamentoEnte());
				entry.setStato(versamentoVO.getStatoVersamento());
				entry.setStatoRendicontazione(versamentoVO.getStatoRendicontazione());
				versamentoLst.add(entry);
			}
			return versamentoLst;
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}

	public Pagamento get(long idVersamento) throws ServiceException, NotFoundException {
		Pagamento pagamento = new Pagamento();
		VersamentiBD versamentiBD = new VersamentiBD(this);
		EntiBD entiBD = new EntiBD(this);
		EventiBD eventiBD = new EventiBD(this);
		final Versamento versamento;
		try {
			versamento = versamentiBD.getVersamento(idVersamento);
			Ente ente = entiBD.getEnte(versamento.getIdEnte());
			pagamento.setEnte(ente);
			AbstractFilter filter = new AbstractFilter(this.getEventoService()) {

				@Override
				public IExpression toExpression() throws ServiceException {
					try {
						IExpression exp = newExpression();
						exp.equals(it.govpay.orm.Evento.model().COD_DOMINIO, versamento.getCodDominio());
						exp.equals(it.govpay.orm.Evento.model().IUV, versamento.getIuv());

						return exp;
					} catch (ExpressionNotImplementedException e) {
						throw new ServiceException(e);
					} catch (ExpressionException e) {
						throw new ServiceException(e);
					} catch (NotImplementedException e) {
						throw new ServiceException(e);
					}
				}
			};
			List<FilterSortWrapper> filterSortList = new ArrayList<FilterSortWrapper>();
			FilterSortWrapper e = new FilterSortWrapper();
			e.setField(it.govpay.orm.Evento.model().DATA_ORA_EVENTO);
			e.setSortOrder(SortOrder.ASC);
			filterSortList.add(e);
			filter.setFilterSortList(filterSortList);
			pagamento.setEventi(eventiBD.findAll(filter));
		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}
		pagamento.setVersamento(versamento);


		try {
			RptBD rptBD = new RptBD(this);
			Rpt rpt = rptBD.getLastRpt(idVersamento);
			pagamento.setRpt(rpt);

			if(rpt.getIdCanale() != null && rpt.getIdCanale() > 0) {
				PspBD pspBD = new PspBD(this);
				Canale canale = pspBD.getCanale(rpt.getIdCanale());
				pagamento.setCanale(canale);
			}

			RtBD rtBD = new RtBD(this);
			Rt rt = rtBD.getLastRt(rpt.getId());
			pagamento.setRt(rt);

		} catch (NotFoundException nfe) {

		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}

		try {
			Rpt rpt = pagamento.getRpt();

			if(rpt != null && rpt.getIdPsp() > 0) {
				PspBD pspBD = new PspBD(this);
				Psp psp = pspBD.getPsp(rpt.getIdPsp());
				// Inserisco i canali vuoti, ho gia' impostato quello usato
				psp.setCanali(new ArrayList<Psp.Canale>()); 
				pagamento.setPsp(psp);
			}
		} catch (NotFoundException nfe) {

		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		} 

		try {
			long idApplicazione = pagamento.getVersamento().getIdApplicazione();

			if(idApplicazione > 0) {
				ApplicazioniBD applicazioniBD = new ApplicazioniBD(this);
				ListaApplicazioniEntry listaApplicazioniEntry = applicazioniBD.getListaApplicazioniEntry(idApplicazione);
				pagamento.setApplicazione(listaApplicazioniEntry);
			}
		} catch (NotFoundException nfe) {

		} catch (MultipleResultException e) {
			throw new ServiceException(e);
		}

		EsitiBD esitiBD = new EsitiBD(this);
		AbstractFilter esitiFilter = new AbstractFilter(this.getEsitoService()) {

			@Override
			public IExpression toExpression() throws ServiceException {
				try {
					IExpression exp = this.newExpression();
					exp.equals(it.govpay.orm.Esito.model().COD_DOMINIO, versamento.getCodDominio());
					exp.equals(it.govpay.orm.Esito.model().IUV, versamento.getIuv());
					return exp;
				} catch(ExpressionException e) {
					throw new ServiceException(e);
				} catch (ExpressionNotImplementedException e) {
					throw new ServiceException(e);
				} catch (NotImplementedException e) {
					throw new ServiceException(e);
				}

			}
		};

		List<FilterSortWrapper> filterSortList = new ArrayList<FilterSortWrapper>();
		FilterSortWrapper sortWrapper = new FilterSortWrapper();
		sortWrapper.setField(it.govpay.orm.Esito.model().DATA_ORA_CREAZIONE);
		sortWrapper.setSortOrder(SortOrder.ASC);
		filterSortList.add(sortWrapper);
		esitiFilter.setFilterSortList(filterSortList);
		List<Esito> esiti = esitiBD.findAll(esitiFilter);
		pagamento.setEsiti(esiti);

		MailBD mailBD = new MailBD(this);
		AbstractFilter mailFilter = new AbstractFilter(this.getMailService()) {

			@Override
			public IExpression toExpression() throws ServiceException {
				try {
					IExpression exp = this.newExpression();
					exp.equals(it.govpay.orm.Mail.model().ID_VERSAMENTO, versamento.getId());
					return exp;
				} catch(ExpressionException e) {
					throw new ServiceException(e);
				} catch (ExpressionNotImplementedException e) {
					throw new ServiceException(e);
				} catch (NotImplementedException e) {
					throw new ServiceException(e);
				}

			}
		};

		List<FilterSortWrapper> mailFilterSortList = new ArrayList<FilterSortWrapper>();
		FilterSortWrapper mailSortWrapper = new FilterSortWrapper();
		mailSortWrapper.setField(it.govpay.orm.Mail.model().DATA_ORA_ULTIMA_SPEDIZIONE); //TODO inserire data creazione
		mailSortWrapper.setSortOrder(SortOrder.ASC);
		mailFilterSortList.add(mailSortWrapper);
		mailFilter.setFilterSortList(mailFilterSortList);
		List<Mail> mail = mailBD.findAll(mailFilter);
		pagamento.setMail(mail);



		return pagamento;
	}


	public VersamentoFilter newFilter() throws ServiceException {
		return new VersamentoFilter(this.getVersamentoService());
	}
}
