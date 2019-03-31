package br.com.sicredi.pautavotacao.api.v1;

import br.com.sicredi.pautavotacao.api.v1.request.PautaRequest;
import br.com.sicredi.pautavotacao.api.v1.request.SessaoRequest;
import br.com.sicredi.pautavotacao.api.v1.request.VotoRequest;
import br.com.sicredi.pautavotacao.api.v1.response.PautaResponse;
import br.com.sicredi.pautavotacao.domain.OpcaoVoto;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

@QuarkusTest
public class PautaResourceITTest {

    private static final Integer PORT = 8081;

    @Test
    public void prepareTestCase() {
        PautaRequest pautaRequest = new PautaRequest();
        pautaRequest.setTitulo("Teste");
        pautaRequest.setDescricao("Teste Descricao");

        given()
                .body(pautaRequest)
                .port(PORT)
                .contentType("application/json")
                .when().post("/v1/pautas")
                .then()
                .statusCode(201);
    }

    @Test
    public void testRetornandoPautas() {
        List<PautaResponse> list = given()
                .contentType("application/json")
                .port(PORT)
                .when()
                .get("/v1/pautas")
                .then()
                .extract().body().jsonPath().getList(".", PautaResponse.class);

        Assertions.assertEquals(1, list.size());
    }

    @Test
    public void testRetornandoPauta() {
        given()
                .when()
                .port(PORT)
                .get("/v1/pautas/1")
                .then()
                .body("titulo", equalTo("Teste"));
    }

    @Test
    public void iniciaUmaVotacaoEIncluiUmVoto() {
        SessaoRequest sessaoRequest = new SessaoRequest();

        given()
                .contentType("application/json")
                .port(PORT)
                .body(sessaoRequest)
                .when().post("/v1/pautas/{idPauta}/iniciar-sessao-votacao", "1")
                .then()
                .statusCode(201);

        VotoRequest votoRequest = new VotoRequest();
        votoRequest.setIdEleitor(1023L);
        votoRequest.setOpcaoVoto(OpcaoVoto.SIM);

        given()
                .contentType("application/json")
                .port(PORT)
                .body(votoRequest)
                .when().post("/v1/pautas/{idPauta}/votar", "1")
                .then()
                .statusCode(201);
    }
}
