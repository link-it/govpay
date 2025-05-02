/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2025 Link.it srl (http://www.link.it).
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
package it.govpay.backoffice.v1.beans.converter;

import java.util.ArrayList;
import java.util.List;

import it.govpay.backoffice.v1.beans.DominioProfiloPost;
import it.govpay.core.beans.commons.Dominio;
import it.govpay.model.PatchOp;

public class PatchOpConverter {
	
	private PatchOpConverter() {}

	public static List<PatchOp> toModel(List<it.govpay.backoffice.v1.beans.PatchOp> lstOp) {
		List<PatchOp> list = new ArrayList<>();
		for(it.govpay.backoffice.v1.beans.PatchOp op : lstOp) {
			PatchOp e = new PatchOp();
			e.setOp(PatchOp.OpEnum.fromValue(op.getOp().name()));
			e.setPath(op.getPath());
			setValue(op, e);
			list.add(e);
		}
		return list;
	}

	private static void setValue(it.govpay.backoffice.v1.beans.PatchOp op, PatchOp e) {

		if(op.getValue() != null && op.getValue() instanceof DominioProfiloPost dominioProfiloPost) {
			Dominio dominioCommons = DominiConverter.getDominioCommons(dominioProfiloPost);
			e.setValue(dominioCommons);
			return;
		}

		e.setValue(op.getValue());
	}

}
