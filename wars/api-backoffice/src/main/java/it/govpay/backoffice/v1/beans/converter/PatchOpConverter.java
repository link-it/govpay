package it.govpay.backoffice.v1.beans.converter;

import java.util.ArrayList;
import java.util.List;

import it.govpay.backoffice.v1.beans.DominioProfiloPost;
import it.govpay.core.dao.commons.Dominio;
import it.govpay.model.PatchOp;

public class PatchOpConverter {

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
		
		if(op.getValue() != null && op.getValue() instanceof DominioProfiloPost) {
			DominioProfiloPost dominioProfiloPost = (DominioProfiloPost) op.getValue();
			Dominio dominioCommons = DominiConverter.getDominioCommons(dominioProfiloPost);
			e.setValue(dominioCommons);
			return;
		}
		
		e.setValue(op.getValue());
	}

}
