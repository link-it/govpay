package it.govpay.pagopa.v2.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import it.govpay.pagopa.v2.authentication.model.ProblemModel;


@ControllerAdvice
@ResponseBody
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

	private ProblemModel buildProblem(Exception ex, HttpStatus status) {
		return ProblemModel.builder()
				.status(status.value())
				.title(status.getReasonPhrase())
				.detail(ex.getLocalizedMessage())
				.build();
				
	}

	@ExceptionHandler({ AuthenticationException.class })
    @ResponseBody
    public ResponseEntity<Object> handleAuthenticationException(Exception ex) {
		ProblemModel problem = this.buildProblem(ex, HttpStatus.UNAUTHORIZED);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(problem);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler( {IllegalArgumentException.class, InternalException.class })
    public ProblemModel handleErroreInterno(IllegalArgumentException ex) {
        return buildProblem(ex, HttpStatus.INTERNAL_SERVER_ERROR) ;
    }
}
