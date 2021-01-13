package com.softarex.internship.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softarex.internship.domain.Response;
import com.softarex.internship.service.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/responses")
@AllArgsConstructor
public class ResponseController {
    private final ResponseService responseService;

    @GetMapping
    public List<Response> getAllResponses() {
        return responseService.getAll();
    }

    @PostMapping
    public ResponseEntity<?> createResponse(@RequestBody Response response) {

        //Response validation is handled in the responseService
        try {
            Response responseFromDb = responseService.create(response);
            return ResponseEntity.ok(responseFromDb);

        } catch (IllegalArgumentException | JsonProcessingException e) {
            return ControllerUtils.getErrorResponse(e, HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
