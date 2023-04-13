package boing.controllers;

import boing.model.Chicken;
import boing.model.ChickenCoop;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * HTTP responses don't have to be just web pages! You often
 * see APIs instead giving JSON responses instead, i.e. just
 * the data.
 */
@RestController // Using this annotation instead can handle automatic serialisation into JSON
public class JSONController {
    @GetMapping("/coop")
    public ResponseEntity<List<Chicken>> getCoop(
            @RequestParam(value = "coop", defaultValue = "ChookTown") String coopName
    ) {
        // This ResponseEntity type also lets us include status codes with our response
        return ResponseEntity.status(HttpStatus.OK).body(ChickenCoop.getCoop(coopName).getChooks());
    }
}
