package com.xebia.representation;

import static com.google.common.collect.Lists.newArrayList;

import java.util.List;

import com.google.common.collect.ImmutableList;

public class Product {
    long id;
    String name;
    int price;
    List<Size> sizes;

    Product() {
        this.sizes = newArrayList();
    }

    public Product(String name, int price) {
        this();
        this.name = name;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void addSize(Size size) {
        sizes.add(size);
    }

    public List<Size> getSizes() {
        if (sizes == null)
            return newArrayList();
        else
            return new ImmutableList.Builder<Size>().addAll(sizes).build();
    }
}
