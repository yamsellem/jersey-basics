package com.xebia.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_XML;
import static org.fest.assertions.Assertions.assertThat;

import org.junit.ClassRule;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.xebia.rule.Server;

public class ProductResourceXmlTest {
    @ClassRule
    public static Server server = Server.create();
    public static Client client = Client.create();

    private static String uri = server.uri + "/product";

    @Test
    public void crud() {
        shouldPostProductAsXml();
        shouldGetProductAsXml();
    }

    public void shouldPostProductAsXml() {
        String entity = "<product><name>pull</name><price>10</price></product>";
        String product = client.resource(uri).accept(APPLICATION_XML).entity(entity, APPLICATION_XML).post(String.class);
        assertThat(product).contains("<id>1</id>");
    }

    public void shouldGetProductAsXml() {
        String product = client.resource(uri).path("1").accept(APPLICATION_XML).get(String.class);
        assertThat(product).contains("<id>1</id>");
    }
}
