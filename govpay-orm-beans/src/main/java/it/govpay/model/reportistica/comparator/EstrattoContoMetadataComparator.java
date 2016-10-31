package it.govpay.model.reportistica.comparator;

import java.util.Comparator;

import it.govpay.model.reportistica.EstrattoContoMetadata;

public class EstrattoContoMetadataComparator implements Comparator<EstrattoContoMetadata>{

	@Override
	public int compare(EstrattoContoMetadata o1, EstrattoContoMetadata o2) {
		// ordinamento per Anno Desc, mese Desc, Iban ASC
		
		Integer anno1 = o1.getAnno();
		Integer anno2 = o2.getAnno();
		
		if(anno1.intValue() == anno2.intValue()){
			Integer mese1 = o1.getMese();
			Integer mese2 = o2.getMese();
			
			if(mese1.intValue() == mese2.intValue()){
				String ibanAccredito1 = o1.getIbanAccredito();
				String ibanAccredito2 = o2.getIbanAccredito();
				
				if(ibanAccredito1 == null)
					return -1;
				
				if(ibanAccredito2 == null)
					return 1;
				
				return ibanAccredito1.compareTo(ibanAccredito2);
			}
			
			return mese2.compareTo(mese1);
		}
		
		return anno2.compareTo(anno1); 
	}
}
