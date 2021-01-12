package com.softarex.internship.controller;

import com.softarex.internship.domain.Response;
import com.softarex.internship.service.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/response")
@AllArgsConstructor
public class ResponseController {
    private final ResponseService responseService;

    @GetMapping
    public List<Response> getAllResponses() {
        return responseService.getAll();
    }

    @PostMapping
    public Response createResponse(@RequestBody Response response) {
        return responseService.create(response);
    }
}
