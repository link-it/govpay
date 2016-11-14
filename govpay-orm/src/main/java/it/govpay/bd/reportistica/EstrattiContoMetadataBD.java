package it.govpay.bd.reportistica;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

import it.govpay.model.reportistica.EstrattoContoMetadata;

public class EstrattiContoMetadataBD {

	private SimpleDateFormat f3 = new SimpleDateFormat("yyyy/MM");

	public List<EstrattoContoMetadata> getListaEstrattoContoMetadata(String codDominio, String formatoFile) throws Exception {
		List<EstrattoContoMetadata> lst = new ArrayList<EstrattoContoMetadata>();
		return lst;
	}

	public EstrattoContoMetadata getEstrattoContoMetadata(String codDominio, Long id, String fileName,String formatoFile) throws Exception{
		EstrattoContoMetadata estrattoConto = new EstrattoContoMetadata();
		estrattoConto.setCodDominio(codDominio);
		estrattoConto.setNomeFile(fileName);
		estrattoConto.setId(id);

		try{
			// decodificare l'estensione del file
			String extension = FilenameUtils.getExtension(fileName);
			estrattoConto.setFormato(extension);
			fileName = FilenameUtils.removeExtension(fileName);

			String[] split = fileName.split("_");

			if(split != null && split.length > 0 ){
				boolean formatoOk = false;
				// formato Nome file codDominio_anno_mese				
				if(split.length == 3){
					estrattoConto.setAnno(Integer.parseInt(split[1]));
					estrattoConto.setMese(Integer.parseInt(split[2]));
					formatoOk = true;
				}

				// formato Nome file codDominio_iban_anno_mese
				if(split.length == 4){
					estrattoConto.setIbanAccredito(split[1]); 
					estrattoConto.setAnno(Integer.parseInt(split[2]));
					estrattoConto.setMese(Integer.parseInt(split[3]));
					formatoOk = true;
				}

				if(formatoOk){
					Calendar cal = Calendar.getInstance();
					cal.set(Calendar.MILLISECOND, 0);
					cal.set(Calendar.SECOND, 0);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.HOUR_OF_DAY, 0);
					cal.set(Calendar.DAY_OF_MONTH, 1);
					cal.set(Calendar.MONTH, estrattoConto.getMese() -1);
					cal.set(Calendar.YEAR, estrattoConto.getAnno());

					estrattoConto.setMeseAnno(this.f3.format(cal.getTime()));

				} 
			}
		} catch(Exception e){
			throw e;
		}
		return estrattoConto;
	}

}
