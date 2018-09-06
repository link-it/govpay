package it.govpay.core.dao.anagrafica.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.expression.SortOrder;

import it.govpay.bd.FilterSortWrapper;
import it.govpay.core.exceptions.InternalException;
import it.govpay.core.exceptions.RequestParamException;
import it.govpay.core.exceptions.RequestParamException.FaultType;
import it.govpay.model.IAutorizzato;

public abstract class BasicFindRequestDTO extends BasicRequestDTO {
	
	private int limit;
	private String simpleSearch;
	private List<FilterSortWrapper> fieldsSort;
	private Map<String, IField> fieldMap;
	private int pagina;
	private FilterSortWrapper defaultSort = null;

	public BasicFindRequestDTO(IAutorizzato user) {
		super(user);
		this.setLimit(50);
		this.setPagina(1);
		this.fieldsSort = new ArrayList<>();
		this.fieldMap = new HashMap<>();
	}

	public void addSortField(String key, IField value) {
		this.fieldMap.put(key, value);
	}
	public int getOffset() {
		return (this.pagina - 1) * this.limit;
	}

	public void setPagina(int pagina) {
		this.pagina = pagina;
	}
	
	public int getPagina() {
		return this.pagina;
	}

	public int getLimit() {
		return this.limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getSimpleSearch() {
		return this.simpleSearch;
	}
	
	public boolean isSimpleSearch() {
		return this.simpleSearch != null;
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
	
	public FilterSortWrapper getDefaultSort() {
		return this.defaultSort;
	}

	public void setDefaultSort(IField field, SortOrder sortOrder) {
		this.defaultSort = new FilterSortWrapper(field, sortOrder);
	}

	protected void addSort(IField field, SortOrder sortOrder) {
		this.fieldsSort.add(new FilterSortWrapper(field, sortOrder));
	}
	
	public List<FilterSortWrapper> getFieldSortList(){
		return (this.fieldsSort.size() == 0 && this.defaultSort != null) ? Arrays.asList(this.defaultSort) : this.fieldsSort;
	}

	public void setOrderBy(String orderBy) throws RequestParamException, InternalException {
		
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
				for (String key : this.fieldMap.keySet()) {
					if(key.equals(fieldname)) {
						this.addSort(this.fieldMap.get(key), sortOrder);
						added = true;
						continue;
					}
				}
			} catch (Exception e) {
				throw new InternalException(e);
			}
			
			if(!added)
				throw new RequestParamException(FaultType.PARAMETRO_ORDERBY_NON_VALIDO, "Il campo " + fieldname + " non e' valido per ordinare la ricerca in corso. Campi consentiti: " + Arrays.toString(this.fieldMap.keySet().toArray()));
		}
	}
}
