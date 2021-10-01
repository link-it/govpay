package it.govpay.core.dao.anagrafica.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openspcoop2.generic_project.beans.IField;
import org.openspcoop2.generic_project.expression.SortOrder;
import org.springframework.security.core.Authentication;

import it.govpay.bd.FilterSortWrapper;
import it.govpay.core.exceptions.RequestParamException;
import it.govpay.core.exceptions.RequestParamException.FaultType;

public abstract class BasicFindRequestDTO extends BasicRequestDTO {
	
	protected boolean ricercaAnagrafica = false;
	private Integer limit;
	private String simpleSearch;
	private List<FilterSortWrapper> fieldsSort;
	private Map<String, IField> fieldMap;
	private Integer offset;
	private List<FilterSortWrapper> defaultSort = null;
	
	public final static int DEFAULT_LIMIT = 50;
	public final static int DEFAULT_MAX_LIMIT = 200;
	
	private boolean eseguiCount = true;
	private boolean eseguiCountConLimit = true;
	private boolean eseguiFindAll = true;

	public BasicFindRequestDTO(Authentication authentication) {
		super(authentication);
		this.setLimit(DEFAULT_LIMIT);
		this.setPagina(1);
		this.fieldsSort = new ArrayList<>();
		this.defaultSort = new ArrayList<>();
		this.fieldMap = new HashMap<>();
	}

	public void addSortField(String key, IField value) {
		this.fieldMap.put(key, value);
	}
	
	public void clearFieldMap() {
		this.fieldMap.clear();
	}
	
	public int getOffset() {
		return offset;
	}
	
	public void setOffset(Integer offset) {
		this.offset = offset != null ? offset : 0;
	}

	public void setPagina(int pagina) {
		this.offset = this.limit != null ?  ((pagina - 1) * this.limit) : 0;
	}
	
	public int getPagina() {
		if(this.offset != null && this.limit != null)
			return Math.floorDiv(this.offset, this.limit);
		else return 1;
	}

	public Integer getLimit() {
		return this.limit;
	}
	public void setLimit(Integer limit) {
		if(!this.ricercaAnagrafica) { 
			this.limit = limit != null ?  limit : DEFAULT_LIMIT;
			if(this.limit < 0)
				this.limit = 0;
			
			if(this.limit == 0) {
				this.eseguiFindAll = false;
			}
		} else {
			this.limit = limit;
			
			if(this.limit != null) {
				if(this.limit < 0)
					this.limit = 0;
				
				if(this.limit == 0) {
					this.eseguiFindAll = false;
				}
			}
		}
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
	
	public List<FilterSortWrapper> getDefaultSort() {
		return this.defaultSort;
	}

	public void addDefaultSort(IField field, SortOrder sortOrder) {
		this.defaultSort.add(new FilterSortWrapper(field, sortOrder));
	}

	private void addSort(IField field, SortOrder sortOrder) {
		this.fieldsSort.add(new FilterSortWrapper(field, sortOrder));
	}
	
	public void addSortField(IField field, SortOrder sortOrder) {
		this.fieldsSort.add(new FilterSortWrapper(field, sortOrder));
	}
	
	public List<FilterSortWrapper> getFieldSortList(){
		return this.fieldsSort.isEmpty() ? this.defaultSort : this.fieldsSort;
	}

	public void setOrderBy(String orderBy) throws RequestParamException {
		resetSort();
		
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
			 
			for (String key : this.fieldMap.keySet()) {
				if(key.equals(fieldname)) {
					this.addSort(this.fieldMap.get(key), sortOrder);
					added = true;
					continue;
				}
			}
			
			if(!added)
				throw new RequestParamException(FaultType.PARAMETRO_ORDERBY_NON_VALIDO, "Il campo " + fieldname + " non e' valido per ordinare la ricerca in corso. Campi consentiti: " + Arrays.toString(this.fieldMap.keySet().toArray()));
		}
	}

	public void resetSort() {
		this.fieldsSort = new ArrayList<>();
	}

	public boolean isOrderEnabled() {
		return !this.fieldsSort.isEmpty();
	}

	public boolean isEseguiCount() {
		return eseguiCount;
	}

	public void setEseguiCount(boolean eseguiCount) {
		this.eseguiCount = eseguiCount;
	}

	public boolean isEseguiCountConLimit() {
		return eseguiCountConLimit;
	}

	public void setEseguiCountConLimit(boolean eseguiCountConLimit) {
		this.eseguiCountConLimit = eseguiCountConLimit;
	}

	public boolean isEseguiFindAll() {
		return eseguiFindAll;
	}

	public void setEseguiFindAll(boolean eseguiFindAll) {
		this.eseguiFindAll = eseguiFindAll;
	}
	
}
