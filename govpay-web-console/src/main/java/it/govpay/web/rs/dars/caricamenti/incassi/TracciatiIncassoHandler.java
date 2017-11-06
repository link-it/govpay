package it.govpay.web.rs.dars.caricamenti.incassi;

import org.apache.logging.log4j.Logger;

import it.govpay.bd.model.Tracciato;
import it.govpay.model.Tracciato.TipoTracciatoType;
import it.govpay.web.rs.dars.base.DarsService;
import it.govpay.web.rs.dars.caricamenti.BaseTracciatiHandler;
import it.govpay.web.rs.dars.handler.IDarsHandler;

public class TracciatiIncassoHandler extends BaseTracciatiHandler implements IDarsHandler<Tracciato>{

	public TracciatiIncassoHandler(Logger log, DarsService darsService) { 
		super(log, darsService,TipoTracciatoType.INCASSI);
	}
}
