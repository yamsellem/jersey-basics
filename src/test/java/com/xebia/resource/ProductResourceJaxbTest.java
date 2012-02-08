package com.xebia.resource;

import static com.xebia.representation.ProductAssert.assertThat;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static org.fest.assertions.Assertions.assertThat;

import java.util.List;

import org.junit.ClassRule;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.ClientResponse.Status;
import com.xebia.representation.Product;
import com.xebia.rule.Server;

public class ProductResourceJaxbTest {
    @ClassRule
    public static Server server = Server.create();
    public static Client client = Client.create();

    private static String uri = server.uri + "/product";

    @Test
    public void crudAsXml() {
        shouldPostProductAsXmlWithJaxb();
        shouldGetProductAsXmlWithJaxb();
        shouldGetAllProductsAsXmlWithJaxb();
    }

    @Test
    public void crudAsJson() {
        shouldPostProductAsJsonWithJaxb();
        shouldGetProductAsJsonWithJaxb();
        shouldGetAllProductsAsJsonWithJaxb();
        shouldDeleteProduct();
    }

    @Test
    public void shouldNotDeleteUnexistingProduct() {
        ClientResponse response = client.resource(uri).path("666").delete(ClientResponse.class);
        assertThat(response.getStatus()).isEqualTo(Status.NOT_FOUND.getStatusCode());
    }

    public void shouldPostProductAsXmlWithJaxb() {
        shouldPostProductWithJaxb(APPLICATION_XML);
    }

    public void shouldPostProductAsJsonWithJaxb() {
        shouldPostProductWithJaxb(APPLICATION_JSON);
    }

    private void shouldPostProductWithJaxb(String media) {
        Product product = client.resource(uri).accept(media).entity(new Product("pull", 10), media).post(Product.class);
        assertThat(product).hasId().hasName("pull").hasPrice(10);
    }

    public void shouldGetProductAsXmlWithJaxb() {
        shouldGetProductWithJaxb(APPLICATION_XML);
    }

    public void shouldGetProductAsJsonWithJaxb() {
        shouldGetProductWithJaxb(APPLICATION_JSON);
    }

    private void shouldGetProductWithJaxb(String media) {
        Product product = client.resource(uri).path("1").accept(media).get(Product.class);
        assertThat(product).hasId().hasName("pull").hasPrice(10);
    }

    private void shouldGetAllProductsAsXmlWithJaxb() {
        shouldGetAllProductsWithJaxb(APPLICATION_XML);
    }

    private void shouldGetAllProductsAsJsonWithJaxb() {
        shouldGetAllProductsWithJaxb(APPLICATION_JSON);
    }

    @SuppressWarnings("unchecked")
    private void shouldGetAllProductsWithJaxb(String media) {
        ClientResponse clientResponse = client.resource(uri).accept(media).get(ClientResponse.class);
        List<Product> products = clientResponse.getEntity(List.class);
        assertThat(products).isNotEmpty();
    }

    public void shouldDeleteProduct() {
        ClientResponse response = client.resource(uri).path("1").delete(ClientResponse.class);
        assertThat(response.getStatus()).isEqualTo(Status.OK.getStatusCode());
    }
}
