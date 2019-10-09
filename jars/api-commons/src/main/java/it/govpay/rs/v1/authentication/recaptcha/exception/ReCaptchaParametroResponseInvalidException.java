package it.govpay.rs.v1.authentication.recaptcha.exception;

public final class ReCaptchaParametroResponseInvalidException extends Exception {

    private static final long serialVersionUID = 5861310537366287163L;

    public ReCaptchaParametroResponseInvalidException() {
        super();
    }

    public ReCaptchaParametroResponseInvalidException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ReCaptchaParametroResponseInvalidException(final String message) {
        super(message);
    }

    public ReCaptchaParametroResponseInvalidException(final Throwable cause) {
        super(cause);
    }

}
