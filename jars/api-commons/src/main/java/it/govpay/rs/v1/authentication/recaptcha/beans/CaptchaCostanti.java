package it.govpay.rs.v1.authentication.recaptcha.beans;

import java.util.regex.Pattern;

public class CaptchaCostanti {

	public static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");
	public static final String PAYLOAD_TEMPLATE = "secret=%s&response=%s&remoteip=%s";
	
	public static final String RE_CAPTCHA_ALIAS = "reCaptchaResponse";
	public static final String RE_CAPTCHA_RESPONSE = "g-recaptcha-response";
}
