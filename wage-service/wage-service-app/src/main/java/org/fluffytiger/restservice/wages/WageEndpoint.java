package org.fluffytiger.restservice.wages;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Submit request with a wage without taxes.")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PostMapping
    public void create(@Valid @RequestBody Wage wage) {
        eventProducerService.sendWageRequest(wage);
    }
}
