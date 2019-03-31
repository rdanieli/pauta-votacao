package br.com.sicredi.pautavotacao;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import org.junit.jupiter.api.Test;
import org.wildfly.common.Assert;

@QuarkusTestResource(H2DatabaseTestResource.class)
public class GreetingResourceTest {

    @Test
    public void testHelloEndpoint() {
        Assert.assertTrue(true);
    }

}