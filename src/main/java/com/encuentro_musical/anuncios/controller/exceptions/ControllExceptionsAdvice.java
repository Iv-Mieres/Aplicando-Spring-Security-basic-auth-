package com.encuentro_musical.anuncios.controller.exceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpSession;
import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.encuentro_musical.anuncios.model.UserModel;
import com.encuentro_musical.anuncios.model.exceptions.BadRequestException;
import com.encuentro_musical.anuncios.model.exceptions.ErrorDetails;


@RestControllerAdvice
public class ControllExceptionsAdvice {

	// Controla Exceptions de Spring Validation

	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> methodArgumentNotValidException(MethodArgumentNotValidException ex) {

		Map<String, String> mjError = new HashMap<>();

		for (FieldError fError : ex.getBindingResult().getFieldErrors()) {
			mjError.put(fError.getField(), fError.getDefaultMessage());
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mjError);
	}
	
	// Controla BadRequest y Exceptions generales 
	
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ExceptionHandler({ConstraintViolationException.class,  BadRequestException.class, UsernameNotFoundException.class, Exception.class})
	public ResponseEntity<ErrorDetails> valoresUnicosExceptions(Exception ex) {

		ErrorDetails errorMj = new ErrorDetails();
		errorMj.setStatus(HttpStatus.BAD_REQUEST.value() + " BAD_REQUEST");
		errorMj.setMessage(ex.getLocalizedMessage());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMj);
	}
	
	// Controla errores de tipeo al ingresar datos en la URI
	
		@ResponseStatus(value = HttpStatus.NOT_FOUND)
		@ExceptionHandler(MethodArgumentTypeMismatchException.class)
		public ResponseEntity<ErrorDetails> errorTipeoExceptions() {

			ErrorDetails errorMj = new ErrorDetails();
			errorMj.setStatus(HttpStatus.NOT_FOUND.value() + " NOT_FOUND");
			errorMj.setMessage("ERROR DE TIPEO: Revise los datos ingresados.");

			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMj);
		}
		
		
		// ========= SPRING SECURITY EXCEPTIONS ===========
	
	// Controla que las credenciales de acceso sean correctas
	
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED)
	@ExceptionHandler(BadCredentialsException.class)
	public ResponseEntity<ErrorDetails> badCredetialsException() {

		ErrorDetails errorMj = new ErrorDetails();
		errorMj.setStatus(HttpStatus.UNAUTHORIZED.value() + " UNAUTHORIZED");
		errorMj.setMessage("LOGIN ERROR: username o password incorrecto. Revise sus datos.");

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMj);
	}
	
	// Control de acceso a los recursos
	
	@SuppressWarnings("static-access")
	@ResponseStatus(value = HttpStatus.UNAUTHORIZED.FORBIDDEN)
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorDetails> unauthorizedException(HttpSession session) {
		UserModel user = (UserModel) session.getAttribute("usersession");	
		ErrorDetails errorMj = new ErrorDetails();
			
		if(Objects.isNull(user)) {
			
			// Controla el acceso a los recursos si el usuario no está logueado
			errorMj.setStatus(HttpStatus.UNAUTHORIZED.value() + " UNAUTHORIZED");
			errorMj.setMessage("ACCESO DENEGADO: Debe estar logueado para ver el contenido.");
		}else {
			
			// Controla el acceso a los recursos si el usuario está logueado
			errorMj.setStatus(HttpStatus.FORBIDDEN.value() + " FORBIDDEN");
			errorMj.setMessage("ACCESO DENEGADO: No tiene los permisos necesarios para utilizar este recurso. ");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorMj);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorMj);
	}  

	

}
