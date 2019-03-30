package br.com.sicredi.pautavotacao.api;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class BaseResource {

    @Inject
    Validator validator;

    public <T> void validate(T obj) {
        Set<ConstraintViolation<T>> violations = validator.validate(obj);

        if (!violations.isEmpty()) {

            throw new WebApplicationException(violations.stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining("\n")), Response.Status.BAD_REQUEST);
        }
    }
}
