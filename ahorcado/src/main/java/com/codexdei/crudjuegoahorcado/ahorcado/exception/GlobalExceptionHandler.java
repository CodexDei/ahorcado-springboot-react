package com.codexdei.crudjuegoahorcado.ahorcado.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

        /*
         * Se ejecuta cuando las credenciales proporcionadas
         * durante el login no son válidas.
         */
        @ExceptionHandler(BadCredentialsException.class)
        public ProblemDetail handleBadCredentials(
                        BadCredentialsException exception) {

                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                                HttpStatus.UNAUTHORIZED,
                                "Username or password is incorrect");

                problem.setTitle("Authentication failed");

                return problem;
        }

        /*
         * Se ejecuta cuando Spring Security no encuentra
         * el usuario solicitado.
         */
        @ExceptionHandler(UsernameNotFoundException.class)
        public ProblemDetail handleUsernameNotFound(
                        UsernameNotFoundException exception) {

                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                                HttpStatus.NOT_FOUND,
                                exception.getMessage());

                problem.setTitle("User not found");

                return problem;
        }

        /*
         * Se ejecuta cuando @Valid detecta errores
         * en los datos enviados por el cliente.
         */
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ProblemDetail handleValidationErrors(
                        MethodArgumentNotValidException exception) {

                ProblemDetail problem = ProblemDetail.forStatus(
                                HttpStatus.BAD_REQUEST);

                problem.setTitle("Validation failed");
                problem.setDetail(
                                "One or more fields contain invalid values.");

                // Añade cada error asociado a su campo.
                exception.getBindingResult()
                                .getFieldErrors()
                                .forEach(error -> problem.setProperty(
                                                error.getField(),
                                                error.getDefaultMessage()));

                return problem;
        }

        /*
         * Se ejecuta cuando una operación viola una
         * restricción de la base de datos, por ejemplo
         * un username único ya existente.
         */
        @ExceptionHandler(DataIntegrityViolationException.class)
        public ProblemDetail handleDataIntegrityViolation(
                        DataIntegrityViolationException exception) {

                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                                HttpStatus.CONFLICT,
                                "The requested operation violates a database constraint.");

                problem.setTitle("Database constraint violation");

                return problem;
        }

        /*
         * Captura errores no contemplados específicamente.
         */
        @ExceptionHandler(Exception.class)
        public ProblemDetail handleGenericException(
                        Exception exception) {

                ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "An unexpected error occurred.");

                problem.setTitle("Internal server error");

                return problem;
        }
}