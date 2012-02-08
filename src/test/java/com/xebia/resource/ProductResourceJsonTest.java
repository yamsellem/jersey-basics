package com.xebia.resource;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.fest.assertions.Assertions.assertThat;

import org.junit.ClassRule;
import org.junit.Test;

import com.sun.jersey.api.client.Client;
import com.xebia.rule.Server;

public class ProductResourceJsonTest {
    @ClassRule
    public static Server server = Server.create();
    public static Client client = Client.create();

    private static String uri = server.uri + "/product";

    @Test
    public void crud() {
        shouldPostProductAsJson();
        shouldGetProductAsJson();
    }

    public void shouldPostProductAsJson() {
        String entity = json("{'name':'pull','price':10,'sizes':['M']}");
        String product = client.resource(uri).accept(APPLICATION_JSON).entity(entity, APPLICATION_JSON).post(String.class);
        assertThat(product).contains(json("'id':'1'"));
    }

    public void shouldGetProductAsJson() {
        String product = client.resource(uri).path("1").accept(APPLICATION_JSON).get(String.class);
        assertThat(product).contains(json("'id':'1'"));
    }

    private String json(String quotedJson) {
        return quotedJson.replace('\'', '"');
    }
}
