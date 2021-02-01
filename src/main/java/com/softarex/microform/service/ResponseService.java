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
     * @return Page assigned to number
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
        checkValidity(response);

        Response createdResponse = responseRepository.save(response);

        //Sending response via WebSocket
        wsSender.sendResponse(createdResponse);

        return createdResponse;
    }

    /**
     * Checks validity of response and throws exception if it isn't valid
     * @param response - Response to validate
     */
    private void checkValidity(@NonNull final Response response) {
        Set<Field> fields = new HashSet<>(fieldRepository.findAll());
        Map<Long, String> responseBody = response.getBody();

        responseBody.forEach((fieldId, value) -> {
            //Checks if field exist
            if (!fields.contains(new Field(fieldId))) {
                throw new IllegalArgumentException("One of submitted fields doesn't exist");
            }
            //Checks if field isn't blank
            if (StringUtils.isEmptyOrWhitespace(value)) {
                throw new IllegalArgumentException("One of submitted fields is blank");
            }
        });

        fields.forEach(field -> {
            //Checks if all required fields attend
            if (field.isRequired() && !responseBody.containsKey(field.getId())) {
                throw new IllegalArgumentException("Required field is empty");
            }

            //Checks if options in option fields is valid
            if (field.getType().equals(FieldType.COMBOBOX) ||
                    field.getType().equals(FieldType.RADIO_BUTTON)) {

                String value = responseBody.get(field.getId());
                if (value != null && !field.getOptions().contains(value)) {
                    throw new IllegalArgumentException("One of field options is invalid");
                }
            }

            if (field.getType().equals(FieldType.CHECKBOX)) {

                String value = responseBody.get(field.getId());
                if (!value.equals("false") && !value.equals("true")) {
                    throw new IllegalArgumentException("Checkbox value should be only true/false");
                }
            }
        });
    }
}
