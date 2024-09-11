package com.Exceptions;

import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.dto.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler{
//	@ExceptionHandler(M)
//	public ResponseEntity<Map<String,String>> handlemethodAgrsNotvlidException(MethodArgumentNotValidException ex ){
//		Map<String, String> resp = new HashMap<String, String>();
//		ex.getBindingResult().getAllErrors().forEach((error)->{
//			String feildName = ((FieldError) error).getField();
//			String msg = error.getDefaultMessage();
//			resp.put(feildName, msg);
//		});
//		
//		return new ResponseEntity<Map<String,String>>(resp,HttpStatus.BAD_REQUEST);
//	}
	
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> exceptionhandle(Exception ex){
		String msg = ex.getMessage();
		return new ResponseEntity<String>(msg,HttpStatus.BAD_REQUEST);
		
	}

	protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status,
			final WebRequest request) {
		logger.error("400 Status Code", ex);
		final BindingResult result = ex.getBindingResult();

		String error = result.getAllErrors().stream().map(e -> {
			if (e instanceof FieldError) {
				return ((FieldError) e).getField() + " : " + e.getDefaultMessage();
			} else {
				return e.getObjectName() + " : " + e.getDefaultMessage();
			}
		}).collect(Collectors.joining(", "));
		return handleExceptionInternal(ex, new ApiResponse(false, error), new org.springframework.http.HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	

}
