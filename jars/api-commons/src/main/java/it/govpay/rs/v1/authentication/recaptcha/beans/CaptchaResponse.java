package it.govpay.rs.v1.authentication.recaptcha.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import it.govpay.core.beans.JSONSerializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonPropertyOrder({
	"success",
	"score",
	"action",
	"challenge_ts",
	"hostname",
	"error-codes"
})
public class CaptchaResponse extends JSONSerializable{

	@JsonProperty("success")
	private boolean success;

	@JsonProperty("score")
	private BigDecimal score;

	@JsonProperty("action")
	private String action;

	@JsonProperty("challenge_ts")
	private Date challengeTs;

	@JsonProperty("hostname")
	private String hostname;

	@JsonProperty("error-codes")
	private ErrorCode[] errorCodes;

	@JsonIgnore
	public boolean hasClientError() {
		ErrorCode[] errors = getErrorCodes();
		if(errors == null) {
			return false;
		}
		for(ErrorCode error : errors) {
			switch(error) {
			case InvalidResponse:
			case MissingResponse:
				return true;
			default:
				return false;
			}
		}
		return false;
	}

	static enum ErrorCode {
		MissingSecret,     InvalidSecret,
		MissingResponse,   InvalidResponse,
		BadRequest,		TimeoutOrDuplicate;

		private static Map<String, ErrorCode> errorsMap = new HashMap<>(4);

		static {
			errorsMap.put("missing-input-secret",   MissingSecret);
			errorsMap.put("invalid-input-secret",   InvalidSecret);
			errorsMap.put("missing-input-response", MissingResponse);
			errorsMap.put("invalid-input-response", InvalidResponse);
			errorsMap.put("bad-request", BadRequest);
			errorsMap.put("timeout-or-duplicate", TimeoutOrDuplicate);
		}

		@JsonCreator
		public static ErrorCode forValue(String value) {
			return errorsMap.get(value.toLowerCase());
		}
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public Date getChallengeTs() {
		return challengeTs;
	}

	public void setChallengeTs(Date challengeTs) {
		this.challengeTs = challengeTs;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public ErrorCode[] getErrorCodes() {
		return errorCodes;
	}

	public void setErrorCodes(ErrorCode[] errorCodes) {
		this.errorCodes = errorCodes;
	}

	public BigDecimal getScore() {
		return score;
	}

	public void setScore(BigDecimal score) {
		this.score = score;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	@Override
	public String getJsonIdFilter() {
		return "captchaResponse";
	}
	
}