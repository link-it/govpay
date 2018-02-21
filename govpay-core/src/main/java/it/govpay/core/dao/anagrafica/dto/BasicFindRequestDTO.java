package it.govpay.core.dao.anagrafica.dto;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.FilterSortWrapper;
import it.govpay.core.exceptions.InternalException;
import it.govpay.core.exceptions.RequestParamException;
import it.govpay.core.exceptions.RequestParamException.FaultType;
import it.govpay.model.IAutorizzato;

public abstract class BasicFindRequestDTO extends BasicRequestDTO {
	
	//private int offset;
	private int limit;
	private String simpleSearch;
	private List<FilterSortWrapper> fieldsSort;
	private int pagina;

	public BasicFindRequestDTO(IAutorizzato user) {
		super(user);
		this.setLimit(50);
		this.setPagina(1);
		this.fieldsSort = new ArrayList<FilterSortWrapper>();
	}

	public int getOffset() {
		return (this.pagina - 1) * this.limit;
	}

//	private void setOffset(int offset) {
//		this.offset = offset;
//	}
	
	public void setPagina(int pagina) {
		this.pagina = pagina;
	}
	
	public int getPagina() {
		return this.pagina;
	}

	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getSimpleSearch() {
		return simpleSearch;
	}
	
	public boolean isSimpleSearch() {
		return simpleSearch != null;
	}
	
	/**
	 * Parametro di ricerca in OR sui campi testuali dell'oggetto.
	 * L'impostazione del parametro simpleSearch rende inefficaci gli altri parametri di ricerca.
	 * 
	 * @param simpleSearch
	 */

	public void setSimpleSearch(String simpleSearch) {
		this.simpleSearch = simpleSearch;
	}
	
	
	protected void addSort(IField field, SortOrder sortOrder) {
		this.fieldsSort.add(new FilterSortWrapper(field, sortOrder));
	}
	
	public List<FilterSortWrapper> getFieldSortList(){
		return fieldsSort;
	}
	
	protected void setOrderBy(Class<? extends Enum<?>> enumType, String orderBy) throws InternalException, RequestParamException {
		
		if(orderBy==null || orderBy.trim().isEmpty()) return;

		String[] split = orderBy.split(",");
		
		for(String fieldname : split) {
			SortOrder sortOrder = SortOrder.ASC;
			fieldname = fieldname.trim();
			if(fieldname.startsWith("-")) {
				sortOrder = SortOrder.DESC;
				fieldname = fieldname.substring(1);
			} else if(fieldname.startsWith("+")) {
				fieldname = fieldname.substring(1);
			}
			
			boolean added = false;
			 
			try {
				for (Enum<?> enum1 : enumType.getEnumConstants()) {
					if(enum1.toString().equals(fieldname)) {
						Method getFieldMethod = enumType.getMethod("getField");
						IField field = (IField) getFieldMethod.invoke(enum1);
						addSort(field, sortOrder);
						added = true;
						continue;
					}
				}
			} catch (Exception e) {
				throw new InternalException(e);
			}
			
			if(!added)
				throw new RequestParamException(FaultType.PARAMETRO_ORDERBY_NON_VALIDO, "Il campo " + fieldname + " non e' valido per ordinare la ricerca in corso. Campi consentiti: " + Arrays.toString(enumType.getEnumConstants()));
		}
	}
}
