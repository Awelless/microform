package com.softarex.microform.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softarex.microform.domain.Response;
import com.softarex.microform.dto.PageDto;
import com.softarex.microform.service.ResponseService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/responses")
@AllArgsConstructor
public class ResponseController {
    private final ResponseService responseService;

    @GetMapping
    public PageDto<Response> getAllResponses(
            @PageableDefault(
                    sort = "id",
                    direction = Sort.Direction.DESC,
                    value = ControllerUtils.PAGE_SIZE
            ) Pageable pageable
    ) {
        return responseService.getAll(pageable);
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
