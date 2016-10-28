package it.govpay.web.business.reportistica.comparator;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;

import it.govpay.model.reportistica.EstrattoContoMetadata;
import it.govpay.model.reportistica.comparator.EstrattoContoMetadataComparator;


public class EstrattoContoMetadataEntryComparator implements Comparator<Map.Entry<Long, EstrattoContoMetadata>> {

	@Override
	public int compare(Entry<Long, EstrattoContoMetadata> o1, Entry<Long, EstrattoContoMetadata> o2) {
		EstrattoContoMetadataComparator ecComparator = new EstrattoContoMetadataComparator();
		return ecComparator.compare(o1.getValue(), o2.getValue()); 
	}
}
