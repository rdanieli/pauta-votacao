package br.com.sicredi.pautavotacao.api.v1;

import br.com.sicredi.pautavotacao.api.BaseResource;
import br.com.sicredi.pautavotacao.api.v1.request.PautaRequest;
import br.com.sicredi.pautavotacao.api.v1.request.PautaResponse;
import br.com.sicredi.pautavotacao.api.v1.request.SessaoRequest;
import br.com.sicredi.pautavotacao.api.v1.request.VotoRequest;
import br.com.sicredi.pautavotacao.domain.Pauta;
import br.com.sicredi.pautavotacao.domain.Voto;
import br.com.sicredi.pautavotacao.service.PautaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/v1/pautas")
@ApplicationScoped
@Produces("application/json")
@Consumes("application/json")
public class PautaResource extends BaseResource {

    @Inject
    Logger logger;

    @Inject
    PautaService pautaService;

    @Inject
    ObjectMapper objectMapper;

    @POST
    public Response criarPauta(@RequestBody(description = "Criar uma nova pauta.",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = PautaRequest.class)))
                                       PautaRequest pautaRequest) {
        validate(pautaRequest);

        logger.info("Chamada para criar pauta: {}.", pautaRequest);

        Pauta pauta = objectMapper.convertValue(pautaRequest, Pauta.class);

        pauta = pautaService.criarNovaPauta(pauta);

        logger.info("Pauta criada com sucesso.");

        return Response.ok(objectMapper.convertValue(pauta, PautaResponse.class))
                .status(HttpStatus.SC_CREATED)
                .build();
    }

    @GET
    public List<PautaResponse> getPautas() {
        logger.info("Retornando pautas.");
        return pautaService.getPautas().stream()
                .map(pauta -> objectMapper.convertValue(pauta, PautaResponse.class))
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{idPauta}")
    public PautaResponse getPauta(@PathParam("idPauta")
                                  String idPauta) {
        logger.info("Buscando pauta id {}.", idPauta);

        return objectMapper.convertValue(pautaService.getPauta(idPauta), PautaResponse.class);
    }

    @POST
    @Path("/{idPauta}/iniciar-sessao-votacao")
    public Response iniciarSessaoVotacao(@PathParam("idPauta") String idPauta,
                                  SessaoRequest abrirSessaoRequest) {
        logger.info("Pauta {} abertura de sessão.", idPauta);

        pautaService.iniciarSessaoVotacao(idPauta, abrirSessaoRequest.getDataFechamento());

        logger.info("Sessão de votação iniciada com sucesso.");

        return Response.ok().build();
    }

    @POST
    @Path("/{idPauta}/votar")
    public Response votar(@PathParam("idPauta") String idPauta,
                          VotoRequest votoRequest) {
        logger.info("Pauta {} adicionando voto {}.", idPauta, votoRequest);

        validate(votoRequest);

        pautaService.votar(idPauta, objectMapper.convertValue(votoRequest, Voto.class));

        logger.info("Voto realizado com sucesso.");

        return Response.ok().build();
    }
}
