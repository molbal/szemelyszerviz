package hu.idomsoft.szemelyszerviz.rest;

import hu.idomsoft.szemelyszerviz.verification.ValidacioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.io.IOException;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidacioException.class)
    public ResponseEntity<ValidacioHibaErrorResponse> customHandleBadRequest(ValidacioException ex, WebRequest request) throws IOException {

        ValidacioHibaErrorResponse  errorResponse = new ValidacioHibaErrorResponse();
        errorResponse.setHibaLista(ex.getHibaLista());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}