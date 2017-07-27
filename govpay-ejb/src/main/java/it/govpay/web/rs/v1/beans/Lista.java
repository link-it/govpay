package it.govpay.web.rs.v1.beans;

import java.net.URI;
import java.net.URISyntaxException;


import org.apache.http.client.utils.URIBuilder;
import org.codehaus.jackson.map.annotate.JsonFilter;

@JsonFilter(value="lista") 
public abstract class Lista extends JSONSerializable {

	private long start;
	private long count;
	private long totalCount;
	private String prevResults;
	private String nextResults;
	
	@Override
	public String getJsonIdFilter() {
		return "lista";
	}
	
	public Lista(URI requestUri, long count, long totalCount, long offset, long limit) {
		this.start = offset;
		this.count = count;
		this.totalCount = totalCount;
		
		URIBuilder builder = new URIBuilder(requestUri);
		builder.setParameter("limit", Long.toString(limit));
		if(offset > 0) {
			long prevOffset = offset - limit;
			if(prevOffset < 0) prevOffset = 0;
			
			builder.setParameter("offset", Long.toString(prevOffset));
			try {
				this.prevResults = builder.build().toString();
			} catch (URISyntaxException e) { }
		}
		
		if((offset + limit) < totalCount) {
			long nextOffset = offset + limit;
			builder.setParameter("offset", Long.toString(nextOffset));
			try {
				this.nextResults = builder.build().toString();
			} catch (URISyntaxException e) { }
		}
	}
	
	public String getPrevResults() {
		return prevResults;
	}
	public void setPrevResults(String prevResults) {
		this.prevResults = prevResults;
	}
	public String getNextResults() {
		return nextResults;
	}
	public void setNextResults(String nextResults) {
		this.nextResults = nextResults;
	}
	public long getStart() {
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}
	public long getCount() {
		return count;
	}
	public void setCount(long count) {
		this.count = count;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	
}
