package org.fluffytiger.restservice.wages;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/wages")
public class WageEndpoint {
    private final WageEventProducerService eventProducerService;

    public WageEndpoint(WageEventProducerService eventProducerService) {
        this.eventProducerService = eventProducerService;
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping
    public void create(@Valid @RequestBody Wage wage) {
        eventProducerService.sendWageRequest(wage);
    }
}
