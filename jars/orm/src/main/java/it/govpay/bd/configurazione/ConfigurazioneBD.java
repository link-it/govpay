package it.govpay.bd.configurazione;

import java.util.List;

import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;
import org.openspcoop2.utils.serialization.IOException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Configurazione;
import it.govpay.bd.model.converter.ConfigurazioneConverter;

public class ConfigurazioneBD extends BasicBD {

	public ConfigurazioneBD(BasicBD basicBD) {
		super(basicBD);
	}

	public Configurazione getConfigurazione() throws NotFoundException, ServiceException{
		try {
			List<it.govpay.orm.Configurazione> voList = this.getConfigurazioneService().findAll(this.getConfigurazioneService().newPaginatedExpression());
			
			if(voList == null || voList.size() == 0)
				throw new NotFoundException("Configurazione Vuota");
			
			return ConfigurazioneConverter.toDTO(voList);
		} catch (NotImplementedException  e) {
			throw new ServiceException(e);
		}
	}
	
	public void salvaConfigurazione(Configurazione configurazione) throws ServiceException {
		try {
			this.getConfigurazioneService().deleteAll();
			
			List<it.govpay.orm.Configurazione> voList = ConfigurazioneConverter.toVOList(configurazione);
			
			for (it.govpay.orm.Configurazione vo : voList) {
				this.getConfigurazioneService().create(vo);
			}
		} catch (NotImplementedException | IOException e) {
			throw new ServiceException(e);
		}
	}
}
