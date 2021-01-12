package com.softarex.internship.service;

import com.softarex.internship.domain.Response;
import com.softarex.internship.repository.ResponseRepository;
import com.softarex.internship.util.WebSocketSender;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ResponseService {
    private final ResponseRepository responseRepository;
    private final WebSocketSender wsSender;

    public List<Response> getAll() {
        return responseRepository.findAll();
    }

    public Response create(@NonNull final Response response) {
        Response createdResponse = responseRepository.save(response);

        wsSender.sendResponse(createdResponse);

        return createdResponse;
    }
}
