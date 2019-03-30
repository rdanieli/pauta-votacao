package br.com.sicredi.pautavotacao.api.v1.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode
@Schema(name = "SessaoRequest")
public class SessaoRequest {

    private LocalDateTime dataFechamento;
}
