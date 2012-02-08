package com.xebia.resource;

import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import com.xebia.data.Products;
import com.xebia.representation.Product;

@Path("product")
public class ProductResource {
    @GET
    @Path("{id}")
    public Product get(@PathParam("id") long id) {
        return Products.get(id);
    }

    @POST
    public Product post(Product product) {
        Products.put(product);
        return product;
    }

    @DELETE
    @Path("{id}")
    public void delete(@PathParam("id") long id) {
        Products.delete(id);
    }

    @GET
    public Response get() {
        List<Product> products = Products.get();
        return Response.ok(products).build();
    }
}
