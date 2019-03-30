package br.com.sicredi.pautavotacao.exceptions.mapper;

import br.com.sicredi.pautavotacao.exceptions.BussinesException;
import br.com.sicredi.pautavotacao.exceptions.PautaNaoEncontradaException;
import br.com.sicredi.pautavotacao.exceptions.SessaoNaoEncontradaException;
import org.apache.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.time.LocalDateTime;

@Provider
public final class ErrorMapper implements ExceptionMapper<Exception> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ErrorMapper.class);

    @Override
    public Response toResponse(Exception exception) {
        int code = HttpStatus.SC_INTERNAL_SERVER_ERROR;

        if (exception instanceof WebApplicationException) {

            code = ((WebApplicationException) exception).getResponse().getStatus();

        } else if (exception instanceof PautaNaoEncontradaException || exception instanceof SessaoNaoEncontradaException) {

            code = HttpStatus.SC_NOT_FOUND;

        } else if (exception instanceof BussinesException) {

            code = HttpStatus.SC_BAD_REQUEST;

        } else {
            LOGGER.error("Unexpected Error", exception);
        }

        Error error = Error.builder()
                .code(code)
                .localDateTime(LocalDateTime.now())
                .message(exception.getMessage())
                .build();

        return Response.status(code)
                .entity(error).build();
    }

}