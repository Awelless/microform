package com.softarex.microform.service;

import com.softarex.microform.domain.field.Field;
import com.softarex.microform.domain.field.FieldType;
import com.softarex.microform.repository.FieldRepository;
import com.softarex.microform.repository.ResponseRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "file:src/test/resources/application.properties")
public class FieldServiceTest {
    @Autowired
    private FieldService fieldService;

    @MockBean
    private FieldRepository fieldRepository;
    @MockBean
    private ResponseRepository responseRepository;

    private Field field;

    @Before
    public void init() {
        field = new Field(
                1L, "Test", FieldType.COMBOBOX,
                true, true, Collections.singletonList("option 1")
        );

        when(fieldRepository.save(any())).then(returnsFirstArg());
        doNothing().when(fieldRepository).delete(any());

        doNothing().when(responseRepository).clearFieldsByFieldId(1L);
    }

    @Test
    public void testCreateShouldCreateWhenValidApplied() {
        Field result = fieldService.create(field);

        Assert.assertEquals(field, result);

        Mockito.verify(fieldRepository, Mockito.times(1)).save(field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateShouldThrowExceptionWhenRequiredUnactiveApplied() {
        field.setActive(false);

        fieldService.create(field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateShouldThrowExceptionWhenEmptyOptionsApplied() {
        field.setOptions(Collections.emptyList());

        fieldService.create(field);
    }

    @Test
    public void testUpdateShouldUpdateWhenCorrectApplied() {
        String newLabel         = "Test2";
        FieldType newType       = FieldType.RADIO_BUTTON;
        List<String> newOptions = Arrays.asList("newOption1", "op2");

        Field newField = new Field(
                null, newLabel, newType,
                false, false, newOptions
        );

        Field result = fieldService.update(field, newField);

        Assert.assertEquals(field, result);

        Assert.assertEquals(newLabel, result.getLabel());
        Assert.assertEquals(newType, result.getType());
        Assert.assertFalse(result.isRequired());
        Assert.assertFalse(result.isActive());
        Assert.assertEquals(newOptions, result.getOptions());

        Mockito.verify(fieldRepository, Mockito.times(1)).save(field);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateShouldUpdateWhenIncorrectOptionsApplied() {
        String newLabel         = "Test2";
        FieldType newType       = FieldType.RADIO_BUTTON;
        List<String> newOptions = new ArrayList<>();

        Field newField = new Field(
                null, newLabel, newType,
                false, false, newOptions
        );

        fieldService.update(field, newField);
    }

    @Test
    public void testDeleteShouldDelete() {
        fieldService.delete(field);

        Mockito.verify(fieldRepository, Mockito.times(1)).delete(field);
        Mockito.verify(responseRepository, Mockito.times(1)).clearFieldsByFieldId(field.getId());
    }
}
