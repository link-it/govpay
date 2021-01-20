package it.govpay.core.dao.anagrafica.dto;

import java.util.List;

import it.govpay.bd.model.Acl;

/**
 * @author Bussu Giovanni (bussu@link.it)
 * @author  $Author: bussu $
 * @version $ Rev: 12563 $, $Date: 01 feb 2018 $
 * 
 */
public class LeggiRuoloDTOResponse extends BasicFindResponseDTO<Acl>{

	/**
	 * @param totalResults
	 * @param results
	 */
	public LeggiRuoloDTOResponse(Long totalResults, List<Acl> results) {
		super(totalResults, results);
	}


}
