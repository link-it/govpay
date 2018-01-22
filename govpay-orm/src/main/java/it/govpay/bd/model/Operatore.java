/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC 
 * http://www.gov4j.it/govpay
 * 
 * Copyright (c) 2014-2017 Link.it srl (http://www.link.it).
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package it.govpay.bd.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.openspcoop2.generic_project.exception.NotFoundException;
import org.openspcoop2.generic_project.exception.ServiceException;

import it.govpay.bd.BasicBD;
import it.govpay.bd.anagrafica.AnagraficaManager;
import it.govpay.model.Acl;
import it.govpay.model.IAutorizzato;
import it.govpay.model.Ruolo;

public class Operatore extends it.govpay.model.Operatore implements IAutorizzato {
	
	private static final long serialVersionUID = 1L;
	private transient List<Ruolo> ruoli;
	private transient List<Acl> acls;
	
	public Operatore() {
		super();
	}
	
	public Operatore(BasicBD bd, List<String> ruoli) throws ServiceException {
		super();
		
		super.setRuoli(ruoli);
		this.ruoli = new ArrayList<Ruolo>();
		if(super.getRuoli() != null) {
			for(String codRuolo : super.getRuoli()) {
				try {
					if(StringUtils.isNotEmpty(codRuolo) && !codRuolo.equals(it.govpay.model.Operatore.RUOLO_SYSTEM))
						this.ruoli.add(AnagraficaManager.getRuolo(bd, codRuolo));
				} catch (NotFoundException e) {
					throw new ServiceException(e);
				}
			}
		}
		
		acls = new ArrayList<Acl>();
		for(Ruolo ruolo : this.ruoli) {
			acls.addAll(ruolo.getAcls());
		}
		
	}
	
	
	@Override
	public List<Acl> getAcls() {
		return acls;
	}
}

