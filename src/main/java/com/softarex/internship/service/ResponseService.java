package com.softarex.internship.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softarex.internship.domain.Response;
import com.softarex.internship.domain.field.Field;
import com.softarex.internship.domain.field.FieldType;
import com.softarex.internship.repository.FieldRepository;
import com.softarex.internship.repository.ResponseRepository;
import com.softarex.internship.util.WebSocketSender;
import lombok.AllArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ResponseService {
    private final ResponseRepository responseRepository;
    private final FieldRepository fieldRepository;
    private final WebSocketSender wsSender;

    public List<Response> getAll() {
        return responseRepository.findAll();
    }

    /**
     * Validates and creates new Response
     * @param response The Response which is created
     * @return Created Response from DB
     */
    public Response create(@NonNull final Response response) throws JsonProcessingException {
        if (isInvalid(response)) {
            throw new IllegalArgumentException("Response is invalid");
        }

        Response createdResponse = responseRepository.save(response);

        //Sending response via WebSocket
        wsSender.sendResponse(createdResponse);

        return createdResponse;
    }

    private boolean isInvalid(@NonNull final Response response) {
        List<Field> fields = fieldRepository.findAll();
        Map<Long, String> responseBody = response.getBody();

        fields.forEach(field -> {
                    //Check on required fields
                    if (field.isRequired() && !responseBody.containsKey(field.getId())) {
                        throw new IllegalArgumentException("Required field is empty");
                    }
                    //Check on valid options in option fields
                    if (field.getType().equals(FieldType.COMBOBOX) || field.getType().equals(FieldType.RADIO_BUTTON)) {

                        String value = responseBody.get(field.getId());
                        if (!field.getOptions().contains(value)) {
                            throw new IllegalArgumentException("Field option is invalid");
                        }
                    }
                });

        return false;
    }
}
