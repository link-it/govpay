package it.govpay.gde.exception;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import it.govpay.gde.model.ProblemModel;

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

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemModel handleConstraintViolation(ConstraintViolationException ex) {
        return buildProblem(ex, HttpStatus.UNPROCESSABLE_ENTITY) ;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ProblemModel handleNotFound(ResourceNotFoundException ex) {
        return buildProblem(ex, HttpStatus.NOT_FOUND) ;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler( {IllegalArgumentException.class, InternalException.class })
    public ProblemModel handleErroreInterno(IllegalArgumentException ex) {
        return buildProblem(ex, HttpStatus.INTERNAL_SERVER_ERROR) ;
    }
}
