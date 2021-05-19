package com.ag04.pluginplatform.controller;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@Controller
@RequestMapping("/introspect")
public class IntrospectionController {

    private final RequestMappingHandlerMapping handlerMapping;

    public IntrospectionController(RequestMappingHandlerMapping handlerMapping) {
        this.handlerMapping = handlerMapping;
    }

    @GetMapping("/endpoints")
    @ResponseBody
    public List<String> getEndpoints() {
        return handlerMapping.getHandlerMethods()
                .keySet().stream().map(key -> Objects.requireNonNull(key.getPatternsCondition()).getPatterns())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

}
