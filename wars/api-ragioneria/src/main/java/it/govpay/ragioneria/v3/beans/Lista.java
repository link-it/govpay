package it.govpay.ragioneria.v3.beans;

import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import it.govpay.core.beans.Costanti;

public class Lista   {
  
  @Schema(example = "100", description = "")
  private BigDecimal numRisultati = null;
  
  @Schema(example = "4", description = "")
  private BigDecimal numPagine = null;
  
  @Schema(example = "25", description = "")
  private BigDecimal risultatiPerPagina = null;
  
  @Schema(example = "1", description = "")
  private BigDecimal pagina = null;
  
  @Schema(example = "/risorsa?pagina=2", description = "")
  private String prossimiRisultati = null;
  
  @Schema(example = "100", description = "")
  private BigDecimal maxRisultati = null;
 /**
   * Get numRisultati
   * @return numRisultati
  **/
  @JsonProperty("numRisultati")
  public BigDecimal getNumRisultati() {
    return numRisultati;
  }

  public void setNumRisultati(BigDecimal numRisultati) {
    this.numRisultati = numRisultati;
  }

  public Lista numRisultati(BigDecimal numRisultati) {
    this.numRisultati = numRisultati;
    return this;
  }

 /**
   * Get numPagine
   * @return numPagine
  **/
  @JsonProperty("numPagine")
  public BigDecimal getNumPagine() {
    return numPagine;
  }

  public void setNumPagine(BigDecimal numPagine) {
    this.numPagine = numPagine;
  }

  public Lista numPagine(BigDecimal numPagine) {
    this.numPagine = numPagine;
    return this;
  }

 /**
   * Get risultatiPerPagina
   * @return risultatiPerPagina
  **/
  @JsonProperty("risultatiPerPagina")
  public BigDecimal getRisultatiPerPagina() {
    return risultatiPerPagina;
  }

  public void setRisultatiPerPagina(BigDecimal risultatiPerPagina) {
    this.risultatiPerPagina = risultatiPerPagina;
  }

  public Lista risultatiPerPagina(BigDecimal risultatiPerPagina) {
    this.risultatiPerPagina = risultatiPerPagina;
    return this;
  }

 /**
   * Get pagina
   * @return pagina
  **/
  @JsonProperty("pagina")
  public BigDecimal getPagina() {
    return pagina;
  }

  public void setPagina(BigDecimal pagina) {
    this.pagina = pagina;
  }

  public Lista pagina(BigDecimal pagina) {
    this.pagina = pagina;
    return this;
  }

 /**
   * Get prossimiRisultati
   * @return prossimiRisultati
  **/
  @JsonProperty("prossimiRisultati")
  public String getProssimiRisultati() {
    return prossimiRisultati;
  }

  public void setProssimiRisultati(String prossimiRisultati) {
    this.prossimiRisultati = prossimiRisultati;
  }

  public Lista prossimiRisultati(String prossimiRisultati) {
    this.prossimiRisultati = prossimiRisultati;
    return this;
  }

 /**
   * Get maxRisultati
   * @return maxRisultati
  **/
  @JsonProperty("maxRisultati")
  public BigDecimal getMaxRisultati() {
    return maxRisultati;
  }

  public void setMaxRisultati(BigDecimal maxRisultati) {
    this.maxRisultati = maxRisultati;
  }

  public Lista maxRisultati(BigDecimal maxRisultati) {
    this.maxRisultati = maxRisultati;
    return this;
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Lista {\n");
    
    sb.append("    numRisultati: ").append(toIndentedString(numRisultati)).append("\n");
    sb.append("    numPagine: ").append(toIndentedString(numPagine)).append("\n");
    sb.append("    risultatiPerPagina: ").append(toIndentedString(risultatiPerPagina)).append("\n");
    sb.append("    pagina: ").append(toIndentedString(pagina)).append("\n");
    sb.append("    prossimiRisultati: ").append(toIndentedString(prossimiRisultati)).append("\n");
    sb.append("    maxRisultati: ").append(toIndentedString(maxRisultati)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private static String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
  
  public Lista() {
		
	}
  
  public Lista(URI requestUri, Long count, Integer pagina, Integer limit) {
	  this(requestUri, count, pagina, limit, null);
  }
  
  public Lista(URI requestUri, Long count, Integer pagina, Integer limit, BigDecimal maxRisultati) {
		this.pagina = pagina != null ? new BigDecimal(pagina) : null;
		this.risultatiPerPagina = limit != null ? new BigDecimal(limit) : null;
		this.numRisultati = count != null ? new BigDecimal(count) : null;
		this.maxRisultati = maxRisultati;
		
		boolean generaLinkProssimiRisultati = false;
		
		URIBuilder builder = new URIBuilder(requestUri);
		
		if(this.risultatiPerPagina != null) {
			builder.setParameter(Costanti.PARAMETRO_RISULTATI_PER_PAGINA, Long.toString(this.risultatiPerPagina.longValue()));
		} else {
			this.risultatiPerPagina = this.numRisultati != null ? this.numRisultati : null;
		}
		
		this.numPagine = null;
		
		if(count != null) {
			if(limit != null) {
				if(limit > 0) {
					this.numPagine = new BigDecimal(count == 0 ? 1L : (long) Math.ceil(count/(double)limit));
				} else {
					this.numPagine = null;
				}
			} else {
				this.numPagine = new BigDecimal(1L);
			}
			
			if(this.pagina != null && this.numPagine != null && this.pagina.intValue() < this.numPagine.intValue()) {
				generaLinkProssimiRisultati = true;
			}
			
		} else {
			this.maxRisultati = null;
			if(limit != null) {
				generaLinkProssimiRisultati = true;
			}
		}
				
		if(generaLinkProssimiRisultati) {
			Integer nextPagina = pagina + 1 ;
			builder.setParameter(Costanti.PARAMETRO_PAGINA, Integer.toString(nextPagina));
			try {
				this.prossimiRisultati = builder.build().toString();
			} catch (URISyntaxException e) { }
		}
	}
}
