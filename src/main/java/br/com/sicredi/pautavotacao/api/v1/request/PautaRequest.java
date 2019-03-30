package br.com.sicredi.pautavotacao.api.v1.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@EqualsAndHashCode
@ToString
@Schema(name="PautaRequest", description="POJO que representa o objeto Pauta Request.")
public class PautaRequest {

    @NotNull(message = "Título deve ser informado.")
    @NotBlank(message = "Título não pode ser vázio.")
    @Schema(required = true)
    private String titulo;

    @NotBlank(message = "Descrição não pode ser vázio.")
    private String descricao;
}
