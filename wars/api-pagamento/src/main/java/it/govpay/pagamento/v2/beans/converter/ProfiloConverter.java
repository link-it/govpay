package it.govpay.pagamento.v2.beans.converter;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.model.Dominio;
import it.govpay.bd.model.Tributo;
import it.govpay.bd.model.Utenza;
import it.govpay.bd.model.UtenzaCittadino;
import it.govpay.core.dao.anagrafica.dto.LeggiProfiloDTOResponse;
import it.govpay.pagamento.v2.beans.Acl;
import it.govpay.pagamento.v2.beans.Profilo;
import it.govpay.pagamento.v2.beans.Soggetto;
import it.govpay.pagamento.v2.beans.TipoEntrata;

public class ProfiloConverter {

	/**
	 * @param user
	 * @return
	 * @throws ServiceException 
	 */
	public static Profilo getProfilo(LeggiProfiloDTOResponse leggiProfilo) throws ServiceException {
		Profilo profilo = new Profilo();
		
		Utenza user = leggiProfilo.getUtente();
		if(user.getAcls()!=null) {
			List<Acl> aclLst = new ArrayList<>();
			for(it.govpay.model.Acl acl: user.getAcls()) {
				aclLst.add(AclConverter.toRsModel(acl));

			}
			profilo.setAcl(aclLst);
		}
		profilo.setNome(leggiProfilo.getNome());
		if(leggiProfilo.getDomini()!=null) {
			List<it.govpay.pagamento.v2.beans.Dominio> dominiLst = new ArrayList<>();
			for(Dominio dominio: leggiProfilo.getDomini()) {
				dominiLst.add(DominiConverter.toRsModel(dominio));
			}
			profilo.setDomini(dominiLst);
		}
		if(leggiProfilo.getTributi()!=null) {
			List<TipoEntrata> entrateLst = new ArrayList<>();
			for(Tributo tributo: leggiProfilo.getTributi()) {
				entrateLst.add(EntrateConverter.toTipoEntrataRsModel(tributo));
			}
			profilo.setEntrate(entrateLst);
		}
		
		switch(user.getTipoUtenza()) {
		case ANONIMO:
			break;
		case APPLICAZIONE:
			break;
		case CITTADINO:
			Soggetto anagrafica = popolaAnagraficaCittadino((UtenzaCittadino) user);
			profilo.setAnagrafica(anagrafica);
			break;
		case OPERATORE:
			break;
		default:
			break;
		}
		
		return profilo;
	}

	private static Soggetto popolaAnagraficaCittadino(UtenzaCittadino cittadino) {
		Soggetto anagrafica = new Soggetto();
		
		anagrafica.setIdentificativo(cittadino.getCodIdentificativo());
		String nomeCognome = cittadino.getProprieta("X-SPID-NAME") + " " + cittadino.getProprieta("X-SPID-FAMILYNAME");
		anagrafica.setAnagrafica(nomeCognome);
		anagrafica.setEmail(cittadino.getProprieta("X-SPID-EMAIL"));
		// TODO completare informazioni utente
		
		return anagrafica;
	}
}
