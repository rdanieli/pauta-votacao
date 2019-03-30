package br.com.sicredi.pautavotacao.api.v1.response;

import br.com.sicredi.pautavotacao.domain.OpcaoVoto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.eclipse.microprofile.openapi.annotations.media.Schema;

import java.util.Map;

@Data
@EqualsAndHashCode
@ToString
@Schema(name="PautaResponse", description="POJO que representa o objeto Pauta Response.")
public class PautaResponse {

    private String id;

    private String titulo;

    private String descricao;

    private Map<OpcaoVoto, Long> resultado;
}
