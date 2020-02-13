package it.govpay.rs.v1.authentication.recaptcha.exception;

public final class ReCaptchaScoreNonValidoException extends Exception {

    private static final long serialVersionUID = 5861310537366287163L;

    public ReCaptchaScoreNonValidoException() {
        super();
    }

    public ReCaptchaScoreNonValidoException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ReCaptchaScoreNonValidoException(final String message) {
        super(message);
    }

    public ReCaptchaScoreNonValidoException(final Throwable cause) {
        super(cause);
    }

}
