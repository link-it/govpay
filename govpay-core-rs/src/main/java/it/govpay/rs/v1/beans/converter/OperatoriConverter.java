package it.govpay.rs.v1.beans.converter;

import java.util.ArrayList;
import java.util.List;

import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.core.dao.anagrafica.dto.PutOperatoreDTO;
import it.govpay.core.rs.v1.beans.Operatore;
import it.govpay.core.rs.v1.beans.base.OperatorePost;
import it.govpay.model.IAutorizzato;

public class OperatoriConverter {

	public static PutOperatoreDTO getPutOperatoreDTO(OperatorePost operatoreRequest, String principal,	IAutorizzato user) {
		PutOperatoreDTO putOperatoreDTO = new PutOperatoreDTO(user);
		
		it.govpay.bd.model.Operatore operatore = new it.govpay.bd.model.Operatore();
		it.govpay.bd.model.Utenza utenza = new it.govpay.bd.model.Utenza();
		utenza.setAbilitato(operatoreRequest.isAbilitato());
		utenza.setPrincipal(principal);
		operatore.setUtenza(utenza);
		operatore.setNome(operatoreRequest.getRagioneSociale()); 
		
		// TODO controllare tipi generati
		if(operatoreRequest.getDomini() != null) {
			List<String> idDomini = new ArrayList<>();
			for (Object id : operatoreRequest.getDomini()) {
				idDomini.add(id.toString());
			}
			putOperatoreDTO.setIdDomini(idDomini);
		}
		
		// TODO controllare tipi generati
		if(operatoreRequest.getEntrate() != null) {
			List<String> idTributi = new ArrayList<>();
			for (Object id : operatoreRequest.getEntrate()) {
				idTributi.add(id.toString());
			}
			
			putOperatoreDTO.setIdTributi(idTributi);
		}
		
		putOperatoreDTO.setPrincipal(principal);
		putOperatoreDTO.setOperatore(operatore);
		
		return putOperatoreDTO;
	}
	 
	
	public static Operatore toRsModel(it.govpay.bd.model.Operatore operatore) throws ServiceException {
		Operatore rsModel = new Operatore();
		rsModel.abilitato(operatore.getUtenza().isAbilitato())
		.principal(operatore.getUtenza().getPrincipal())
		.ragioneSociale(operatore.getNome());
		
		return rsModel;
	}
}

