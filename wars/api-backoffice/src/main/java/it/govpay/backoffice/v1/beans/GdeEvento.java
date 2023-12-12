/*
 * GovPay - Porta di Accesso al Nodo dei Pagamenti SPC
 * http://www.gov4j.it/govpay
 *
 * Copyright (c) 2014-2023 Link.it srl (http://www.link.it).
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 3, as published by
 * the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package it.govpay.backoffice.v1.beans;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import it.govpay.core.beans.JSONSerializable;
import it.govpay.core.exceptions.IOException;
import it.govpay.core.exceptions.ValidationException;
import it.govpay.core.utils.validator.IValidable;
import it.govpay.core.utils.validator.ValidatorFactory;
@com.fasterxml.jackson.annotation.JsonPropertyOrder({
"log",
"dump",
})
public class GdeEvento extends JSONSerializable implements IValidable{


  /**
   * Gets or Sets log
   */
  public enum LogEnum {




    SEMPRE("sempre"),


    MAI("mai"),


    SOLO_ERRORE("solo errore");




    private String value;

    LogEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    public static LogEnum fromValue(String text) {
      for (LogEnum b : LogEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }



  @JsonProperty("log")
  private String log = null;

  private LogEnum logEnum = null;


  /**
   * Gets or Sets dump
   */
  public enum DumpEnum {




    SEMPRE("sempre"),


    MAI("mai"),


    SOLO_ERRORE("solo errore");




    private String value;

    DumpEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    public static DumpEnum fromValue(String text) {
      for (DumpEnum b : DumpEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }



  @JsonProperty("dump")
  private String dump = null;

  private DumpEnum dumpEnum = null;

  /**
   **/
  public GdeEvento logEnum(LogEnum logEnum) {
    this.logEnum = logEnum;
    return this;
  }

  @JsonIgnore
  public LogEnum getLogEnum() {
    return logEnum;
  }
  public void setLog(LogEnum logEnum) {
    this.logEnum = logEnum;

    if(this.logEnum != null)
    	this.log = this.logEnum.value;
  }

  /**
   **/
  public GdeEvento log(String log) {
    this.log = log;
    return this;
  }

  @JsonProperty("log")
  public String getLog() {
    return log;
  }
  public void setLog(String log) {
    this.log = log;
  }

  /**
   **/
  public GdeEvento dumpEnum(DumpEnum dumpEnum) {
    this.dumpEnum = dumpEnum;
    return this;
  }

  @JsonIgnore
  public DumpEnum getDumpEnum() {
    return dumpEnum;
  }
  public void setDump(DumpEnum dumpEnum) {
    this.dumpEnum = dumpEnum;

    if(this.dumpEnum != null)
    	this.dump = this.dumpEnum.value;
  }

  /**
   **/
  public GdeEvento dump(String dump) {
    this.dump = dump;
    return this;
  }

  @JsonProperty("dump")
  public String getDump() {
    return dump;
  }
  public void setDump(String dump) {
    this.dump = dump;
  }

  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GdeEvento gdeEvento = (GdeEvento) o;
    return Objects.equals(log, gdeEvento.log) &&
        Objects.equals(dump, gdeEvento.dump);
  }

  @Override
  public int hashCode() {
    return Objects.hash(log, dump);
  }

  public static GdeEvento parse(String json) throws IOException {
	    return parse(json, GdeEvento.class);
	  }

  @Override
  public String getJsonIdFilter() {
    return "gdeEvento";
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GdeEvento {\n");

    sb.append("    log: ").append(toIndentedString(log)).append("\n");
    sb.append("    dump: ").append(toIndentedString(dump)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

  @Override
	public void validate() throws ValidationException {
	  ValidatorFactory vf = ValidatorFactory.newInstance();
		vf.getValidator("log", this.log).notNull();
		vf.getValidator("dump", this.dump).notNull();
	}
}



