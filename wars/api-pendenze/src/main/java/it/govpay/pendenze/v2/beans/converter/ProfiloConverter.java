/**
 * 
 */
package it.govpay.pendenze.v2.beans.converter;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Dominio;
import it.govpay.core.dao.anagrafica.dto.LeggiProfiloDTOResponse;
import it.govpay.model.TipoVersamento;
import it.govpay.pendenze.v2.beans.Profilo;
import it.govpay.pendenze.v2.beans.TipoPendenza;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 12 giu 2018 $
 * 
 */
public class ProfiloConverter {

	/**
	 * @param user
	 * @return
	 * @throws ServiceException 
	 */
	public static Profilo getProfilo(LeggiProfiloDTOResponse leggiProfilo) throws ServiceException {
		Profilo profilo = new Profilo();
		
		profilo.setNome(leggiProfilo.getNome());
		if(leggiProfilo.getDomini()!=null) {
			List<it.govpay.pendenze.v2.beans.Dominio> dominiLst = new ArrayList<>();
			for(Dominio dominio: leggiProfilo.getDomini()) {
				dominiLst.add(DominiConverter.toRsModel(dominio));
			}
			profilo.setDomini(dominiLst); 
		}
		if(leggiProfilo.getTipiVersamento()!=null) {
			List<TipoPendenza> tipiPendenzaLst = new ArrayList<>();
			for(TipoVersamento tipoPendenza: leggiProfilo.getTipiVersamento()) {
				tipiPendenzaLst.add(TipiPendenzaConverter.toTipoPendenzaRsModel(tipoPendenza));
			}
			profilo.setTipiPendenza(tipiPendenzaLst);
		}
		return profilo;
	}

}
