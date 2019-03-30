package br.com.sicredi.pautavotacao.api.v1.request;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Data
@EqualsAndHashCode
@ToString
@Schema(name="PautaResponse", description="POJO que representa o objeto Pauta Response.")
public class PautaResponse {

    private String id;

    private String titulo;

    private String descricao;

}
