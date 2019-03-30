package br.com.sicredi.pautavotacao;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Properties;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @APIResponses(
            value = {
                    @APIResponse(
                            responseCode = "200",
                            description = "Success",
                            content = @Content(mediaType = MediaType.TEXT_PLAIN,
                            schema = @Schema(implementation = String.class)))})
    @Operation(
            summary = "Get JVM system properties for particular host",
            description = "Retrieves and returns the JVM system properties from the system "
                    + "service running on the particular host.")
    public String hello() {
        return "hello";
    }
}