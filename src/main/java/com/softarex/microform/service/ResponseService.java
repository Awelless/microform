package com.softarex.microform.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softarex.microform.domain.Response;
import com.softarex.microform.domain.field.Field;
import com.softarex.microform.domain.field.FieldType;
import com.softarex.microform.dto.PageDto;
import com.softarex.microform.repository.FieldRepository;
import com.softarex.microform.repository.ResponseRepository;
import com.softarex.microform.util.WebSocketSender;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
public class ResponseService {
    private final ResponseRepository responseRepository;
    private final FieldRepository fieldRepository;
    private final WebSocketSender wsSender;

    /**
     * @param pageable
     * @return Page assigned to pageable
     */
    public PageDto<Response> getAll(Pageable pageable) {
        Page<Response> page = responseRepository.findAll(pageable);

        return new PageDto<>(
                page.getContent(),
                pageable.getPageNumber(),
                page.getTotalPages()
        );
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
        Set<Field> fields = new HashSet<>(fieldRepository.findAll());
        Map<Long, String> responseBody = response.getBody();

        responseBody.forEach((fieldId, value) -> {
                if (!fields.contains(new Field(fieldId))) {
                    throw new IllegalArgumentException("One of fields is invalid");
                }
            });

        fields.forEach(field -> {
                    //Check on required fields
                    if (field.isRequired() && !responseBody.containsKey(field.getId())) {
                        throw new IllegalArgumentException("Required field is empty");
                    }
                    //Check on valid options in option fields
                    if (field.getType().equals(FieldType.COMBOBOX) || field.getType().equals(FieldType.RADIO_BUTTON)) {

                        String value = responseBody.get(field.getId());
                        if ((field.isRequired() || !StringUtils.isEmpty(value)) && !field.getOptions().contains(value)) {
                            throw new IllegalArgumentException("One of Field options is invalid");
                        }
                    }
                });

        return false;
    }
}
