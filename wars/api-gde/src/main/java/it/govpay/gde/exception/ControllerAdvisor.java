package it.govpay.gde.exception;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
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

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		//Get all errors
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());
        ProblemModel buildProblem = buildProblem(ex, HttpStatus.BAD_REQUEST);
        buildProblem.setDetail(StringUtils.join(errors, ", "));

        return new ResponseEntity<>(buildProblem, headers, status);
//		// TODO Auto-generated method stub
//		return super.handleMethodArgumentNotValid(ex, headers, status, request);
	}
	
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ProblemModel handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
//        return buildProblem(ex, HttpStatus.BAD_REQUEST) ;
//    }

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
    
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler( {javax.validation.ConstraintViolationException.class })
    public ProblemModel handleConstraintViolation(javax.validation.ConstraintViolationException ex) {
        return buildProblem(ex, HttpStatus.BAD_REQUEST) ;
    }
}
