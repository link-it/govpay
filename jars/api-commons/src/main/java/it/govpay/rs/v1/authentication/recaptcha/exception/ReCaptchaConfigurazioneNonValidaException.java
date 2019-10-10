package it.govpay.rs.v1.authentication.recaptcha.exception;

public final class ReCaptchaConfigurazioneNonValidaException extends Exception {

    private static final long serialVersionUID = 5861310537366287163L;

    public ReCaptchaConfigurazioneNonValidaException() {
        super();
    }

    public ReCaptchaConfigurazioneNonValidaException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ReCaptchaConfigurazioneNonValidaException(final String message) {
        super(message);
    }

    public ReCaptchaConfigurazioneNonValidaException(final Throwable cause) {
        super(cause);
    }

}
