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
package it.govpay.business;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import it.govpay.bd.AbstractFilter;
import it.govpay.bd.BasicBD;
import it.govpay.bd.FilterSortWrapper;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.bd.mail.MailBD;
import it.govpay.bd.model.Ente;
import it.govpay.bd.model.Mail.StatoSpedizione;
import it.govpay.bd.model.Mail.TipoMail;
import it.govpay.bd.model.MailTemplate;
import it.govpay.bd.model.Rpt;
import it.govpay.bd.model.Rt;
import it.govpay.bd.model.Versamento;
import it.govpay.bd.pagamento.TracciatiBD;
import it.govpay.exception.GovPayException;
import it.govpay.exception.GovPayException.GovPayExceptionEnum;
import it.govpay.utils.GovPayConfiguration;
import it.govpay.utils.NotificaParameters;
import it.govpay.utils.RptPlaceholder;
import it.govpay.utils.RtPlaceholder;
import it.govpay.web.wsclient.MailClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openspcoop2.generic_project.exception.ExpressionException;
import org.openspcoop2.generic_project.exception.ExpressionNotImplementedException;
import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.generic_project.expression.IExpression;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.openspcoop2.utils.resources.TemplateUtils;

public class Mail {

	private static Logger log = LogManager.getLogger();
	private BasicBD bd;

	public Mail(BasicBD bd) {
		this.bd = bd;
	}

	public void notificaMail() throws GovPayException {
		log.trace("Invio delle mail di notifica");
		try {
			MailBD mailBD = new MailBD(bd);
			TracciatiBD tracciatoBD = new TracciatiBD(bd);
			GovPayConfiguration gpConfig = GovPayConfiguration.newInstance();
			AbstractFilter filter = new AbstractFilter(bd.getMailService()) {

				@Override
				public IExpression toExpression() throws ServiceException {
					IExpression exp;
					try {
						exp = newExpression();
						exp.equals(it.govpay.orm.Mail.model().STATO_SPEDIZIONE, StatoSpedizione.DA_SPEDIRE.toString());

						IExpression exp2 = newExpression();
						exp2.equals(it.govpay.orm.Mail.model().STATO_SPEDIZIONE, StatoSpedizione.IN_RISPEDIZIONE.toString()).and().lessThan(it.govpay.orm.Mail.model().TENTATIVI_RISPEDIZIONE, GovPayConfiguration.newInstance().getMail_maxRetries());
						IExpression exp3 = newExpression();

						exp3.isNull(it.govpay.orm.Mail.model().DATA_ORA_ULTIMA_SPEDIZIONE).or().lessEquals(it.govpay.orm.Mail.model().DATA_ORA_ULTIMA_SPEDIZIONE, new Date());
						IExpression exp4 = newExpression();

						return exp4.or(exp, exp2).and(exp3);
					} catch (NotImplementedException e) {
						throw new ServiceException(e);
					} catch (ExpressionNotImplementedException e) {
						throw new ServiceException(e);
					} catch (ExpressionException e) {
						throw new ServiceException(e);
					} catch (GovPayException e) {
						throw new ServiceException(e);
					}

				}
			};

			List<FilterSortWrapper> filterSortList = new ArrayList<FilterSortWrapper>();
			FilterSortWrapper wrapper = new FilterSortWrapper();
			wrapper.setField(it.govpay.orm.Mail.model().DATA_ORA_ULTIMA_SPEDIZIONE);
			wrapper.setSortOrder(SortOrder.ASC);
			filterSortList.add(wrapper);
			filter.setFilterSortList(filterSortList);
			filter.setOffset(0);
			filter.setLimit(gpConfig.getMail_limit());

			List<it.govpay.bd.model.Mail> mailLst = mailBD.findAll(filter);

			MailClient mailClient = new MailClient();

			while(mailLst != null && !mailLst.isEmpty()) {
				log.info("Trovate ["+mailLst.size()+"] mail da notificare");
				bd.setAutoCommit(false);
				long mailSpediteConSuccesso = 0;
				long mailSpediteConErrore = 0;
				for(it.govpay.bd.model.Mail mailToSend : mailLst) {
					try {
						byte[] rpt = mailToSend.getIdTracciatoRPT() != null ? tracciatoBD.getTracciato(mailToSend.getIdTracciatoRPT()) : null;
						byte[] rt = mailToSend.getIdTracciatoRT() != null ? tracciatoBD.getTracciato(mailToSend.getIdTracciatoRT()) : null;
						mailClient.send(mailToSend, rpt, rt);

						mailToSend.setStatoSpedizione(StatoSpedizione.SPEDITA);
						mailToSend.setDataOraUltimaSpedizione(new Date());
						mailBD.updateMail(mailToSend);
						mailSpediteConSuccesso++;
					} catch(Exception e) {
						log.error("Errore durante la notifica della mail con id["+mailToSend.getId()+"]:"+e.getMessage(), e);

						boolean isErroreSpedizione = false;
						for(int i = 0; i < gpConfig.getMail_listErroriSpedizione().size() && !isErroreSpedizione; i++) {
							String erroreSpedizione = gpConfig.getMail_listErroriSpedizione().get(i);
							if(e.getMessage().contains(erroreSpedizione)) {
								isErroreSpedizione = true;
								mailToSend.setDettaglioErroreSpedizione(e.getMessage());
								mailToSend.setStatoSpedizione(StatoSpedizione.ERRORE_SPEDIZIONE);
							}
						}

						if(!isErroreSpedizione) {
							mailToSend.setStatoSpedizione(StatoSpedizione.IN_RISPEDIZIONE);

							Long tentativi = mailToSend.getTentativiRispedizione() != null ? mailToSend.getTentativiRispedizione() : 0;
							mailToSend.setTentativiRispedizione(tentativi + 1);
						}

						mailToSend.setDataOraUltimaSpedizione(new Date());
						mailBD.updateMail(mailToSend);
						mailSpediteConErrore++;
					}
					bd.commit();
				}
				log.info("Notificate ["+mailLst.size()+"] mail, di cui ["+mailSpediteConSuccesso+"] con successo e ["+mailSpediteConErrore+"] con errore");
				mailLst = mailBD.findAll(filter);
			}
		} catch (Exception se) {
			if(bd != null) {
				bd.rollback();
			}
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Non è stato possibile inviare le notifiche mail: " + se.getMessage(), se);
		}
	}

	public it.govpay.bd.model.Mail getMail(final TipoMail tipoMail, final long bundleKey) throws GovPayException, NotFoundException, MultipleResultException{
		try {
			MailBD mailBD = new MailBD(this.bd);
			AbstractFilter filter = new AbstractFilter(this.bd.getMailService()) {

				@Override
				public IExpression toExpression() throws ServiceException {
					try {
						IExpression exp = newExpression();
						exp.equals(it.govpay.orm.Mail.model().BUNDLE_KEY, bundleKey);
						exp.equals(it.govpay.orm.Mail.model().TIPO_MAIL, tipoMail.toString());
						return exp;
					} catch (NotImplementedException e) {
						throw new ServiceException(e);
					} catch (ExpressionNotImplementedException e) {
						throw new ServiceException(e);
					} catch (ExpressionException e) {
						throw new ServiceException(e);
					}
				}
			};

			filter.setLimit(2);
			List<it.govpay.bd.model.Mail> mail = mailBD.findAll(filter);

			if(mail == null || mail.isEmpty()) {
				throw new NotFoundException("Mail con tipoMail["+tipoMail.toString()+"] e bundleKey["+bundleKey+"] non trovata");
			}

			if(mail.size() > 1) {
				throw new MultipleResultException("Piu' di una mail con tipoMail["+tipoMail.toString()+"] e bundleKey["+bundleKey+"] trovata");
			}

			return mail.get(0);
		} catch (ServiceException e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Errore durante la ricerca delle mail:"+e.getMessage(), e);
		}
	}

	public it.govpay.bd.model.Mail generaNotificaRPT(Ente ente, Versamento versamento, Rpt rpt) throws GovPayException{
		
		if(!ente.isInvioMailRptAbilitato()) return null;
		Long template = ente.getIdMailTemplateRPT();
		if(template == null || template <=0) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "L'Ente ["+ente.getId()+"] non ha un template per le notifiche RPT: ");
		}
		
		RptPlaceholder rptPlaceholder = new RptPlaceholder();
		rptPlaceholder.setImportoTotale(versamento.getImportoTotale().toString());
		rptPlaceholder.setIUV(versamento.getIuv());

		String destinatario = null;
		String[] cc = null;
		if(rpt.getAnagraficaVersante() == null) {
			destinatario = versamento.getAnagraficaDebitore().getEmail();
		} else {
			if(rpt.getAnagraficaVersante().getEmail() != null) {
				destinatario = rpt.getAnagraficaVersante().getEmail();
				cc = new String[]{versamento.getAnagraficaDebitore().getEmail()};
			}
			destinatario = versamento.getAnagraficaDebitore().getEmail();
		}

		if(destinatario == null) {
			log.warn("Versante e debitore non riportano l'indirizzo email nell'anagrafica per la spedizione della mail RPT");
		} else {
			NotificaParameters param = new NotificaParameters();
			param.setEnte(ente);
			param.setIdTracciato(rpt.getIdTracciatoXML());
			param.setDestinatario(destinatario);
			param.setCc(cc);
			param.setIdVersamento(versamento.getId());
			param.setBundleKey(rpt.getId());
			param.setValori(rptPlaceholder);

			try {
				
				MailTemplate mailTemplate;
				try {
					mailTemplate = AnagraficaManager.getMailTemplate(bd, template);
				} catch (NotFoundException e) {
					throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "MailTemplate ["+template+"] non trovato: " + e.getMessage(), e);
				} catch (MultipleResultException e) {
					throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Molteplici MailTemplate con id["+template+"] trovati: " + e.getMessage(), e);
				}
				it.govpay.bd.model.Mail mailDaSpedire = this.getMail(mailTemplate, param.getDestinatario(), param.getCc(), param.getValori(), TipoMail.NOTIFICA_RPT, param.getBundleKey(), param.getIdVersamento());
				mailDaSpedire.setIdTracciatoRPT(param.getIdTracciato());
				new MailBD(bd).insertMail(mailDaSpedire);
				return mailDaSpedire;
			} catch(ServiceException e){
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			} catch (IOException e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			} catch (TemplateException e) {
				throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
			}
		}
		
		return null;
	}
	
	
	public it.govpay.bd.model.Mail generaNotificaRT(Ente ente, Versamento versamento, Rpt rpt, Rt rt) throws GovPayException{
		
		if(!ente.isInvioMailRtAbilitato()) return null;
		
		Long templateId = ente.getIdMailTemplateRT();

		if(templateId == null || templateId <=0) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "L'Ente ["+ente.getId()+"] non ha un template per le notifiche RT: ");
		}
		
		RtPlaceholder rtPlaceholder = new RtPlaceholder();
		rtPlaceholder.setEsito(rt.getEsitoPagamento().toString());
		rtPlaceholder.setImportoPagato(versamento.getImportoPagato().toString());
		rtPlaceholder.setImportoTotale(versamento.getImportoTotale().toString());
		rtPlaceholder.setIUV(versamento.getIuv());
		
		try {
			String destinatario = null;
			String[] cc = null;
			if(rpt.getAnagraficaVersante() == null) {
				destinatario = versamento.getAnagraficaDebitore().getEmail();
			} else {
				if(rpt.getAnagraficaVersante().getEmail() != null) {
					destinatario = rpt.getAnagraficaVersante().getEmail();
					cc = new String[]{versamento.getAnagraficaDebitore().getEmail()};
				}
				destinatario = versamento.getAnagraficaDebitore().getEmail();
			}
			
			if(destinatario == null) {
				log.warn("Versante e debitore non riportano l'indirizzo email nell'anagrafica per la spedizione della mail RPT");
			} else {
			
				NotificaParameters param = new NotificaParameters();
				param.setIdEnte(versamento.getIdEnte());
				param.setIdTracciato(rpt.getIdTracciatoXML());
				param.setDestinatario(destinatario);
				param.setIdVersamento(versamento.getId());
				param.setBundleKey(rt.getId());
				param.setCc(cc);
				param.setValori(rtPlaceholder);
			

				MailTemplate mailTemplate;
				try {
					mailTemplate = AnagraficaManager.getMailTemplate(bd, templateId);
				} catch (NotFoundException e) {
					throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "MailTemplate ["+templateId+"] non trovato: " + e.getMessage(), e);
				} catch (MultipleResultException e) {
					throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, "Molteplici MailTemplate con id["+templateId+"] trovato: " + e.getMessage(), e);
				}
	
				it.govpay.bd.model.Mail mailDaSpedire = this.getMail(mailTemplate, param.getDestinatario(), param.getCc(), param.getValori(), TipoMail.NOTIFICA_RT, param.getBundleKey(), param.getIdVersamento());
				mailDaSpedire.setIdTracciatoRT(param.getIdTracciato());
				return mailDaSpedire;
			}
		} catch(ServiceException e){
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		} catch (IOException e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		} catch (TemplateException e) {
			throw new GovPayException(GovPayExceptionEnum.ERRORE_INTERNO, e);
		}

		return null;
	}

	private it.govpay.bd.model.Mail getMail(MailTemplate template, String destinatario, String[] cc, RptPlaceholder valori, TipoMail tipoMail, long bundleKey, Long idVersamento) throws IOException, TemplateException {
		Template templateSubject = TemplateUtils.buildTemplate("Oggetto", template.getTemplateOggetto().getBytes());
		Template templateBody = TemplateUtils.buildTemplate("Messaggio", template.getTemplateMessaggio().getBytes());
		String subject = TemplateUtils.toString(templateSubject, valori.getValuesMap());
		String body = TemplateUtils.toString(templateBody, valori.getValuesMap());
		it.govpay.bd.model.Mail mail = new it.govpay.bd.model.Mail();
		mail.setMittente(template.getMittente());
		mail.setTipoMail(tipoMail);
		mail.setDestinatario(destinatario);
		mail.setIdVersamento(idVersamento);
		mail.setBundleKey(bundleKey);
		if(cc != null && cc.length > 0)
			mail.setCc(Arrays.asList(cc));
		mail.setOggetto(subject);
		mail.setMessaggio(body);
		mail.setStatoSpedizione(StatoSpedizione.DA_SPEDIRE);
		return mail;
	}


}
