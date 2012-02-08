package com.xebia.resource;

import static com.sun.jersey.api.json.JSONConfiguration.FEATURE_POJO_MAPPING;
import static com.xebia.representation.ProductAssert.assertThat;
import static java.lang.Boolean.TRUE;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.xebia.provider.JacksonMapperProvider;
import com.xebia.representation.Product;
import com.xebia.rule.Server;

public class ProductResourceHeaderTest {
    @ClassRule
    public static Server server = Server.create();
    public static Client client;

    private static String uri = server.uri + "/product";

    @Before
    public void createJacksonClient() {
        ClientConfig cc = new DefaultClientConfig(JacksonMapperProvider.class);
        cc.getFeatures().put(FEATURE_POJO_MAPPING, TRUE);
        client = Client.create(cc);
    }

    @Test
    public void crud() {
        shouldPostProductAsJsonWithJackson();
        shouldGetProductAsJsonWithJackson();
    }

    public void shouldPostProductAsJsonWithJackson() {
        ClientResponse response = client.resource(uri).accept(APPLICATION_JSON).entity(new Product("pull", 10), APPLICATION_JSON).post(ClientResponse.class);
        assertThat(response.getHeaders().get("Cache-Control")).containsExactly("max-age=600");

        Product product = response.getEntity(Product.class);
        assertThat(product).hasName("pull").hasPrice(10);
    }

    public void shouldGetProductAsJsonWithJackson() {
        ClientResponse response = client.resource(uri).path("1").accept(APPLICATION_JSON).get(ClientResponse.class);
        assertThat(response.getHeaders().get("Cache-Control")).containsExactly("max-age=600");

        Product product = response.getEntity(Product.class);
        assertThat(product).hasName("pull").hasPrice(10);
    }
}
