package br.com.bibliotecavirtual.api.exception;

import br.com.bibliotecavirtual.model.exception.RecursoNaoEncontradoException;
import br.com.bibliotecavirtual.model.exception.RegraNegocioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErroResponse tratarNaoEncontrado(RecursoNaoEncontradoException ex) {
        return montarErro(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(RegraNegocioException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResponse tratarRegraNegocio(RegraNegocioException ex) {
        return montarErro(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResponse tratarValidacao(MethodArgumentNotValidException ex) {
        List<String> erros = ex.getBindingResult().getFieldErrors().stream()
                .map(campo -> campo.getField() + ": " + campo.getDefaultMessage())
                .toList();

        return new ErroResponse(HttpStatus.BAD_REQUEST.value(), "Dados inválidos", erros, LocalDateTime.now());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResponse tratarCorpoInvalido(HttpMessageNotReadableException ex) {
        return montarErro(HttpStatus.BAD_REQUEST, "Corpo da requisição inválido ou mal formatado");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErroResponse tratarParametroInvalido(MethodArgumentTypeMismatchException ex) {
        return montarErro(HttpStatus.BAD_REQUEST,
                "Valor inválido para o parâmetro '" + ex.getName() + "': " + ex.getValue());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErroResponse tratarRotaInexistente(NoResourceFoundException ex) {
        return montarErro(HttpStatus.NOT_FOUND, "Rota não encontrada: /" + ex.getResourcePath());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErroResponse tratarMetodoNaoSuportado(HttpRequestMethodNotSupportedException ex) {
        return montarErro(HttpStatus.METHOD_NOT_ALLOWED,
                "Método " + ex.getMethod() + " não é suportado nesta rota");
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ErroResponse tratarTipoConteudoInvalido(HttpMediaTypeNotSupportedException ex) {
        return montarErro(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Tipo de conteúdo não suportado. Use application/json");
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErroResponse tratarConflito(DataIntegrityViolationException ex) {
        return montarErro(HttpStatus.CONFLICT, "Operação viola uma restrição do banco (registro duplicado ou em uso)");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErroResponse tratarErroInesperado(Exception ex) {
        log.error("Erro inesperado", ex);
        return montarErro(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno no servidor. Tente novamente mais tarde.");
    }

    private ErroResponse montarErro(HttpStatus status, String mensagem) {
        return new ErroResponse(status.value(), mensagem, List.of(), LocalDateTime.now());
    }
}
