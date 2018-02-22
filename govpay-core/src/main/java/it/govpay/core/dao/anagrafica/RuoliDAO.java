package it.govpay.core.dao.anagrafica;

import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.RuoliBD;
import it.govpay.bd.anagrafica.filters.RuoloFilter;
import it.govpay.model.Ruolo;

public class RuoliDAO extends BasicBD{
	
	public RuoliDAO(BasicBD basicBD) {
		super(basicBD);
	}

	public List<Ruolo> getRuoliRegistrati() throws ServiceException{
		RuoliBD ruoliBD = new RuoliBD(this);
		RuoloFilter filter = ruoliBD.newFilter();
		return ruoliBD.findAll(filter);
	}
}
