package it.govpay.bd.configurazione;

import org.openspcoop2.generic_project.exception.MultipleResultException;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.NotImplementedException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.model.Configurazione;
import it.govpay.bd.model.converter.ConfigurazioneConverter;

public class ConfigurazioneBD extends BasicBD {

	public ConfigurazioneBD(BasicBD basicBD) {
		super(basicBD);
	}

	public Configurazione getConfigurazione() throws ServiceException, NotFoundException {
		try {
			it.govpay.orm.Configurazione configurazioneVO = this.getConfigurazioneService().get();
			return ConfigurazioneConverter.toDTO(configurazioneVO);
		} catch (NotImplementedException | MultipleResultException e) {
			throw new ServiceException(e);
		}
	}
	
	public void salvaConfigurazione(Configurazione configurazione) throws ServiceException {
		try {
			it.govpay.orm.Configurazione configurazioneVO = ConfigurazioneConverter.toVO(configurazione);
			this.getConfigurazioneService().updateOrCreate(configurazioneVO);
		} catch (NotImplementedException e) {
			throw new ServiceException(e);
		}
	}
}
