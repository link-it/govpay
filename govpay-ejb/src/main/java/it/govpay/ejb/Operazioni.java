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

import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.ejb.AccessTimeout;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Timer;
import javax.ejb.TimerService;

import it.govpay.core.utils.GovpayConfig;


@Singleton
public class Operazioni{

	@Resource
	TimerService timerservice;

	@Schedule(hour="4,12,18", persistent=false)
	@AccessTimeout(value=30, unit=TimeUnit.MINUTES)
	public static String acquisizioneRendicontazioni(){
		if(!GovpayConfig.getInstance().isBatchOn()) {
			return "Batch non attivi";
		}
		return it.govpay.core.business.Operazioni.acquisizioneRendicontazioni("Batch");
	}

	@Schedule(hour="2", persistent=false)
	@AccessTimeout(value=10, unit=TimeUnit.MINUTES)
	public static String aggiornamentoRegistroPsp(){
		if(!GovpayConfig.getInstance().isBatchOn()) {
			return "Batch non attivi";
		}
		return it.govpay.core.business.Operazioni.aggiornamentoRegistroPsp("Batch");
	}

	@Schedule(hour="*", persistent=false)
	@AccessTimeout(value=15, unit=TimeUnit.MINUTES)
	public static String recuperoRptPendenti(){
		if(!GovpayConfig.getInstance().isBatchOn()) {
			return "Batch non attivi";
		}
		return it.govpay.core.business.Operazioni.recuperoRptPendenti("Batch");
	}

	@Schedule(hour="*", minute="*", persistent=false)
	@AccessTimeout(value=20, unit=TimeUnit.MINUTES)
	public static String spedizioneNotifiche(){
		if(!GovpayConfig.getInstance().isBatchOn()) {
			return "Batch non attivi";
		}
		return it.govpay.core.business.Operazioni.spedizioneNotifiche("Batch");
	}

	@Schedule(hour="*", minute="*", second="*/5", persistent=false)
	@AccessTimeout(value=20, unit=TimeUnit.MINUTES)
	public static String caricamentoTracciati(){
		if(!GovpayConfig.getInstance().isBatchOn()) {
			return "Batch non attivi";
		}
		return it.govpay.core.business.Operazioni.caricamentoTracciati("Batch");
	}

	public static String resetCacheAnagrafica(){
		if(!GovpayConfig.getInstance().isBatchOn()) {
			return "Batch non attivi";
		}
		return it.govpay.core.business.Operazioni.resetCacheAnagrafica();
	}
	
	@Schedule(hour="0", persistent=false)
	@AccessTimeout(value=5, unit=TimeUnit.MINUTES)
	public void generaEstrattoConto(Timer timer) {
		if(!GovpayConfig.getInstance().isBatchOn()) {
			return ;
		}
		it.govpay.core.business.Operazioni.estrattoConto("Batch");
	}

	@Schedule(hour="3", persistent=false)
	@AccessTimeout(value=90, unit=TimeUnit.MINUTES)
	public static String richiestaConservazioneRt(){
		if(!GovpayConfig.getInstance().isBatchOn()) {
			return "Batch non attivi";
		}
		return it.govpay.core.business.Operazioni.richiestaConservazioneRt("Batch");
	}
	
	@Schedule(hour="5", persistent=false)
	@AccessTimeout(value=60, unit=TimeUnit.MINUTES)
	public static String esitoConservazioneRt(){
		if(!GovpayConfig.getInstance().isBatchOn()) {
			return "Batch non attivi";
		}
		return it.govpay.core.business.Operazioni.esitoConservazioneRt("Batch");
	}
}
