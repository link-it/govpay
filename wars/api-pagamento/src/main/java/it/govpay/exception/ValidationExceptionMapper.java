package it.govpay.exception;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {

	@Override
	public Response toResponse(ValidationException exception) {
		Response.Status errorStatus = Response.Status.BAD_REQUEST;
		ProblemValidation problem = new ProblemValidation();
		if (exception instanceof ConstraintViolationException) {
			final ConstraintViolationException constraint = (ConstraintViolationException) exception;

			for (final ConstraintViolation< ? > violation: constraint.getConstraintViolations()) {
				problem.addInvalidParam(violation.getPropertyPath().toString(), violation.getMessage(), violation.getInvalidValue());
			}
		} 
		return Response.status(errorStatus).entity(problem).build();
	}
}

