package com.mhp.api_gateway.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class FallbackController {

    @GetMapping(value = "/fallback/{segment}")
    public ResponseEntity<Object> fallback(@PathVariable String segment) {
        Map<String, String> mapResponse = new HashMap<>();
        mapResponse.put("status", ": "+ StringUtils.upperCase(segment) +" SERVICE IS UNAVAILABLE");
        return new ResponseEntity<>(mapResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}