package it.govpay.core.dao.anagrafica.dto;

import java.util.List;

import it.govpay.bd.model.Acl;

public class ListaAclDTOResponse  extends BasicFindResponseDTO<Acl> {

		public ListaAclDTOResponse(Long totalResults, List<Acl> results) {
			super(totalResults, results);
		}


}
