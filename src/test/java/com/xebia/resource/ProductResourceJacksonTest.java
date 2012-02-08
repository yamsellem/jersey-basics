package com.xebia.resource;

import static com.xebia.representation.ProductAssert.assertThat;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.fest.assertions.Assertions.assertThat;

import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.xebia.representation.Product;
import com.xebia.rule.Server;

public class ProductResourceJacksonTest {
    @ClassRule
    public static Server server = Server.create();
    public static Client client;

    private static String uri = server.uri + "/product";

    @Before
    public void createJacksonCustomClient() {
        client = Client.create();
    }

    @Test
    public void shouldPostProductWithJaxb() {
        String entity = json("{'name':'pull','price':10,'sizes':['M']}");
        String product = client.resource(uri).accept(APPLICATION_JSON).entity(entity, APPLICATION_JSON).post(String.class);
        assertThat(product).contains(json("'sizes':'M'"));
    }

    @Test
    public void shouldPostProductWithJackson() {
        String entity = json("{'name':'pull','price':10,'sizes':['M']}");
        String product = client.resource(uri).accept(APPLICATION_JSON).entity(entity, APPLICATION_JSON).post(String.class);
        assertThat(product).contains(json("'sizes':['M']"));
    }

    @Test
    public void crudAsJson() {
        shouldPostProductAsJsonWithJackson();
        shouldGetProductAsJsonWithJackson();
        shouldDeleteProduct();
    }

    public void shouldPostProductAsJsonWithJackson() {
        Product product = client.resource(uri).accept(APPLICATION_JSON).entity(new Product("pull", 10), APPLICATION_JSON).post(Product.class);
        assertThat(product).hasName("pull").hasPrice(10);
    }

    public void shouldGetProductAsJsonWithJackson() {
        Product product = client.resource(uri).path("1").accept(APPLICATION_JSON).get(Product.class);
        assertThat(product).hasName("pull").hasPrice(10);
    }

    public void shouldDeleteProduct() {
        ClientResponse response = client.resource(uri).path("1").delete(ClientResponse.class);
        assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());
    }

    private String json(String quotedJson) {
        return quotedJson.replace('\'', '"');
    }
}
