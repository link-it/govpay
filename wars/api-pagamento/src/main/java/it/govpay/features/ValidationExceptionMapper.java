package it.govpay.features;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;


import it.govpay.pagamento.v2.beans.Problem;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ValidationException> {

    @Override
    public Response toResponse(ValidationException exception) {
        Response.Status errorStatus = Response.Status.BAD_REQUEST;
        
        Problem problem = new Problem();
    	problem.setStatus(errorStatus.getStatusCode());
    	problem.setTitle(errorStatus.getReasonPhrase());
        
    	StringBuilder responseBody = new StringBuilder();
        if (exception instanceof ConstraintViolationException) {
            final ConstraintViolationException constraint = (ConstraintViolationException) exception;

            for (final ConstraintViolation< ? > violation: constraint.getConstraintViolations()) {
                String message = buildErrorMessage(violation);
                if (responseBody != null) {
                    responseBody.append(message).append("\n");
                }
            }
        } else {
        	responseBody.append(exception.getMessage()).append("\n");
        }
        problem.setDetail(responseBody.toString());	
        
        return Response.status(errorStatus).entity(problem).build();
    }

    protected String buildErrorMessage(ConstraintViolation<?> violation) {
        return "Value "
            + (violation.getInvalidValue() != null ? "'" + violation.getInvalidValue().toString() + "'" : "(null)")
            + " of " + violation.getRootBeanClass().getSimpleName()
            + "." + violation.getPropertyPath()
            + ": " + violation.getMessage();
    }

}

