package br.com.sicredi.pautavotacao.api.v1;

import br.com.sicredi.pautavotacao.api.BaseResource;
import br.com.sicredi.pautavotacao.api.v1.request.PautaRequest;
import br.com.sicredi.pautavotacao.api.v1.request.SessaoRequest;
import br.com.sicredi.pautavotacao.api.v1.request.VotoRequest;
import br.com.sicredi.pautavotacao.api.v1.response.PautaResponse;
import br.com.sicredi.pautavotacao.domain.Pauta;
import br.com.sicredi.pautavotacao.domain.Voto;
import br.com.sicredi.pautavotacao.exceptions.PautaNaoEncontradaException;
import br.com.sicredi.pautavotacao.exceptions.mapper.Error;
import br.com.sicredi.pautavotacao.service.PautaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpStatus;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.slf4j.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

import static javax.ws.rs.core.Response.ok;

@Path("/v1/pautas")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PautaResource extends BaseResource {

    @Inject
    Logger logger;

    @Inject
    PautaService pautaService;

    @Inject
    ObjectMapper objectMapper;

    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "201",
                            description = "Created",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                                    schema = @Schema(implementation = PautaResponse.class)))})
    @Operation(summary = "Criar um pauta de votacao")
    @POST
    public Response criarPauta(@RequestBody(description = "Criar uma nova pauta.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON,
                    schema = @Schema(implementation = PautaRequest.class)))
                                       PautaRequest pautaRequest) {
        validate(pautaRequest);

        logger.info("Chamada para criar pauta: {}.", pautaRequest);

        Pauta pauta = objectMapper.convertValue(pautaRequest, Pauta.class);

        pauta = pautaService.criarNovaPauta(pauta);

        logger.info("Pauta criada com sucesso.");

        return ok(objectMapper.convertValue(pauta, PautaResponse.class))
                .status(HttpStatus.SC_CREATED)
                .build();
    }

    @APIResponses(value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = PautaResponse.class, type = SchemaType.ARRAY)))})
    @Operation(summary = "Consulta todas as pautas")
    @GET
    public List<PautaResponse> getPautas() {
        logger.info("Retornando pautas.");
        return pautaService.getPautas().stream()
                .map(this::getPautaResponse)
                .collect(Collectors.toList());
    }

    @APIResponses(value = {
            @APIResponse(
                    responseCode = "404",
                    description = "NOT_FOUND",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Error.class))),
            @APIResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = PautaResponse.class)))})
    @Operation(summary = "Consulta uma pauta especifica")
    @GET
    @Path("/{idPauta}")
    public PautaResponse getPauta(@PathParam("idPauta")
                                  String idPauta) {
        logger.info("Buscando pauta id {}.", idPauta);

        return getPautaResponse(pautaService.getPauta(idPauta).orElseThrow(() -> new PautaNaoEncontradaException(idPauta)));
    }

    private PautaResponse getPautaResponse(Pauta pauta) {
        PautaResponse pautaResponse = objectMapper.convertValue(pauta, PautaResponse.class);
        pautaResponse.setResultado(pautaService.result(pauta));

        return pautaResponse;
    }

    @APIResponses(value = {
            @APIResponse(
                    responseCode = "400",
                    description = "BAD_REQUEST",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Error.class))),
            @APIResponse(
                    responseCode = "404",
                    description = "NOT_FOUND",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Error.class))),
            @APIResponse(
                    responseCode = "201",
                    description = "CREATED",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Response.class)))})
    @Operation(summary = "iniciar uma sessao de votacao")
    @POST
    @Path("/{idPauta}/iniciar-sessao-votacao")
    public Response iniciarSessaoVotacao(@PathParam("idPauta") String idPauta,
                                  SessaoRequest abrirSessaoRequest) {
        logger.info("Pauta {} abertura de sessão.", idPauta);

        pautaService.iniciarSessaoVotacao(idPauta, abrirSessaoRequest != null ? abrirSessaoRequest.getDataFechamento() : null);

        logger.info("Sessão de votação iniciada com sucesso.");

        return ok().status(HttpStatus.SC_CREATED).build();
    }

    @APIResponses(value = {
            @APIResponse(
                    responseCode = "404",
                    description = "NOT_FOUND",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Error.class))),
            @APIResponse(
                    responseCode = "400",
                    description = "BAD_REQUEST",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Error.class))),
            @APIResponse(
                    responseCode = "201",
                    description = "CREATED",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = Response.class)))})
    @Operation(summary = "Efetuar a votacao em uma pauta especifica e sessao existente")
    @POST
    @Path("/{idPauta}/votar")
    public Response votar(@PathParam("idPauta") String idPauta,
                          VotoRequest votoRequest) {
        logger.info("Pauta {} adicionando voto {}.", idPauta, votoRequest);

        validate(votoRequest);

        pautaService.votar(idPauta, objectMapper.convertValue(votoRequest, Voto.class));

        logger.info("Voto realizado com sucesso.");

        return ok().status(HttpStatus.SC_CREATED).build();
    }
}
