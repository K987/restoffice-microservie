package hu.restoffice.commons.error;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 *
 */
@RestControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {

    /*
     * (non-Javadoc)
     *
     * @see org.springframework.web.servlet.mvc.method.annotation.
     * ResponseEntityExceptionHandler#handleMethodArgumentNotValid(org.
     * springframework.web.bind.MethodArgumentNotValidException,
     * org.springframework.http.HttpHeaders, org.springframework.http.HttpStatus,
     * org.springframework.web.context.request.WebRequest)
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
            final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        return handleExceptionInternal(ex,
                new ErrorBody(status, ex.getBindingResult().getObjectName(),
                        ex.getBindingResult().getFieldError().getRejectedValue()),
                headers,
                status, request);
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<?> handleMethodArgumentTypeMismatchException(final MethodArgumentTypeMismatchException ex,
            final WebRequest request) {

        return handleExceptionInternal(ex,
                new ErrorBody(HttpStatus.BAD_REQUEST, ex.getName(), ex.getRequiredType().getSimpleName()),
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = ServiceException.class)
    protected ResponseEntity<?> handleServiceException(final ServiceException ex, final WebRequest request) {
        HttpStatus status;
        switch(ex.getType()) {
            case ALREADY_EXISTS:
                status = HttpStatus.BAD_REQUEST;
                break;
            case NOT_EXISTS:
                status = HttpStatus.NOT_FOUND;
                break;
            case UNSUPPORTED:
                status = HttpStatus.METHOD_NOT_ALLOWED;
                break;
            case UNKNOWN:
            default:
                status = HttpStatus.INTERNAL_SERVER_ERROR;
                break;
        }
        return handleExceptionInternal(ex,
                new ErrorBody(status, ex.getMessage(), ex.getErrorObject().orElse("no description")),
                new HttpHeaders(), status, request);
    }

    protected final static class ErrorBody {

        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        private Object description;

        private ErrorBody(final HttpStatus status, final String message,
                final Object description) {
            timestamp = LocalDateTime.now();
            this.status = status.value();
            error = status.getReasonPhrase();
            this.message = message;
            this.description = description;
        }


        /**
         * @return the timeStamp
         */
        public LocalDateTime getTimestamp() {
            return timestamp;
        }

        /**
         * @return the status
         */
        public int getStatus() {
            return status;
        }

        /**
         * @return the message
         */
        public String getMessage() {
            return message;
        }

        /**
         * @return the error
         */
        public String getError() {
            return error;
        }

        /**
         * @return the description
         */
        public String getDescription() {
            return description.toString();
        }
    }
}
