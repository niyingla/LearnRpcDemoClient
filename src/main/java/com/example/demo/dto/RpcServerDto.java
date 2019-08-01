package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @author tangsg
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RpcServerDto {

    private String name;
    private List<Example> examples = new ArrayList<>();

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class Example{
        private String ip;
        private String port;
    }

    public RpcServerDto(String name) {
        this.name = name;
    }

    public void addExample(String ip ,String port){
        Example example = new Example(ip,port);
        examples.add(example);

    }
}
