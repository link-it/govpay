package it.govpay.backoffice.v1.beans.converter;

import java.util.ArrayList;
import java.util.List;

import it.govpay.model.PatchOp;

public class PatchOpConverter {

	public static List<PatchOp> toModel(List<it.govpay.backoffice.v1.beans.PatchOp> lstOp) {
		List<PatchOp> list = new ArrayList<>();
		for(it.govpay.backoffice.v1.beans.PatchOp op : lstOp) {
			PatchOp e = new PatchOp();
			e.setOp(PatchOp.OpEnum.fromValue(op.getOp().name()));
			e.setPath(op.getPath());
			e.setValue(op.getValue());
			list.add(e);
		}
		return list;
	}

}
