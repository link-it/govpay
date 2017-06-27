package it.govpay.web.rs.dars.base;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.Logger;

import it.govpay.bd.BasicBD;
import it.govpay.bd.reportistica.statistiche.filters.StatisticaFilter;
import it.govpay.model.reportistica.statistiche.TipoIntervallo;
import it.govpay.web.rs.dars.exception.ConsoleException;
import it.govpay.web.rs.dars.handler.IStatisticaDarsHandler;
import it.govpay.web.rs.dars.model.input.ParamField;
import it.govpay.web.rs.dars.model.statistiche.PaginaGrafico;
import it.govpay.web.utils.ConsoleProperties;
import it.govpay.web.utils.Utils;

public abstract class StatisticaDarsHandler<T> extends BaseDarsHandler<T> implements IStatisticaDarsHandler<T> {

	public StatisticaDarsHandler(Logger log, BaseDarsService darsService) {
		super(log, darsService);
	}

	@Override
	public abstract PaginaGrafico getGrafico(UriInfo uriInfo, BasicBD bd) throws WebApplicationException, ConsoleException;
	
	@Override
	public Map<String, ParamField<?>> getInfoGrafico(UriInfo uriInfo, BasicBD bd ) throws ConsoleException{
		return this.getInfoGrafico(uriInfo, bd, true);
	}

	@Override
	public Map<String, ParamField<?>> getInfoGrafico(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca) throws ConsoleException{
		return this.getInfoGrafico(uriInfo, bd, visualizzaRicerca, null);
	}

	@Override
	public Map<String, ParamField<?>> getInfoGrafico(UriInfo uriInfo, BasicBD bd, Map<String, String> parameters)
			throws ConsoleException {
		return this.getInfoGrafico(uriInfo, bd, true, parameters);
	}

	@Override
	public abstract Map<String, ParamField<?>> getInfoGrafico(UriInfo uriInfo, BasicBD bd, boolean visualizzaRicerca, Map<String,String> parameters ) throws ConsoleException;

	
	protected Date calcolaAvanzamento(Date data, int avanzamento, TipoIntervallo tipoIntervallo) throws ConsoleException{
		if(avanzamento != 0){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(data);
			switch (tipoIntervallo) {
			case MENSILE:
				calendar.add(Calendar.MONTH, avanzamento);
				break;
			case GIORNALIERO:
				calendar.add(Calendar.DATE, avanzamento);
				break;
			case ORARIO:
				calendar.add(Calendar.HOUR, avanzamento);
				break;
			}
			
			return calendar.getTime();
		}
		
		return data;
	}
	
	protected StatisticaFilter popoloFiltroStatistiche(UriInfo uriInfo,BasicBD bd, StatisticaFilter filter) throws ConsoleException, Exception {
		Date data = new Date();
		String dataId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("statistiche.data.id");
		String dataS = this.getParameter(uriInfo, dataId, String.class);
		if(StringUtils.isNotEmpty(dataS)){
			data = this.convertJsonStringToDate(dataS);
		}
		filter.setData(data);

		String colonneId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("statistiche.colonne.id");
		String colonneS = this.getParameter(uriInfo, colonneId, String.class);
		int colonne = 0;
		if(StringUtils.isNotEmpty(colonneS)){
			try{
				colonne = Integer.parseInt(colonneS);
			}catch(Exception e){

			}
		}
		filter.setLimit(colonne);
		
		String avanzamentoId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("statistiche.avanzamento.id");
		String avanzamentoS = this.getParameter(uriInfo, avanzamentoId, String.class);
		int avanzamento = 0;
		if(StringUtils.isNotEmpty(avanzamentoS)){
			try{
				avanzamento = Integer.parseInt(avanzamentoS);
			}catch(Exception e){
				avanzamento = 0;
			}
		}
		filter.setAvanzamento(avanzamento); 
		
		String tipoIntervalloId = Utils.getInstance(this.getLanguage()).getMessageFromResourceBundle("statistiche.tipoIntervallo.id");
		String tipoIntervalloS = this.getParameter(uriInfo, tipoIntervalloId, String.class);
		TipoIntervallo tipoIntervallo= TipoIntervallo.GIORNALIERO;
		if(StringUtils.isNotEmpty(tipoIntervalloS)){
			tipoIntervallo = TipoIntervallo.valueOf(tipoIntervalloS);
		}
		filter.setTipoIntervallo(tipoIntervallo);
		
		// soglia minima percentuale
		double soglia = ConsoleProperties.getInstance().getSogliaPercentualeMinimaGraficoTorta() / 100;
		filter.setSoglia(soglia);
		
		// applica avanzamento
		
		filter.setData(this.calcolaAvanzamento(filter.getData(), filter.getAvanzamento(), filter.getTipoIntervallo()));  
		
		
		return filter;
	}
}
