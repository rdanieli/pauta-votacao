package br.com.sicredi.pautavotacao.exceptions;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Error {
    private String message;
    private Integer code;
    private LocalDateTime localDateTime;
}
