package it.govpay.rs.v1.beans.converter;

import java.util.ArrayList;
import java.util.List;

import it.govpay.bd.model.Operatore;
import it.govpay.bd.model.Utenza;
import it.govpay.core.dao.anagrafica.dto.PutOperatoreDTO;
import it.govpay.model.IAutorizzato;
import it.govpay.rs.v1.beans.base.OperatorePost;

public class OperatoriConverter {

	public static PutOperatoreDTO getPutOperatoreDTO(OperatorePost operatoreRequest, String principal,	IAutorizzato user) {
		PutOperatoreDTO putOperatoreDTO = new PutOperatoreDTO(user);
		
		Operatore operatore = new Operatore();;
		Utenza utenza = new Utenza();
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
	 
}

