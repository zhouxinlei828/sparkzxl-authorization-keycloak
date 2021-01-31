package com.github.sparkzxl.authorization.domain.model.aggregates;

import lombok.Data;

import java.util.List;

@Data
public class City {

    private int code;

    private String name;

    private List<City> children;
}
