package com.bnfd.overseer.controller;

import com.bnfd.overseer.exception.OverseerBadRequestException;
import com.bnfd.overseer.exception.OverseerNoContentException;
import com.bnfd.overseer.exception.OverseerUnprocessableException;
import com.bnfd.overseer.model.api.BuilderOption;
import com.bnfd.overseer.model.constants.BuilderCategory;
import com.bnfd.overseer.model.constants.BuilderType;
import com.bnfd.overseer.service.LookupService;
import com.bnfd.overseer.service.ValidationService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Slf4j
@WebMvcTest(LookupController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LookupControllerTests {
    // region - Class Variables -
    private static final String BASE_MAPPING = "/api/lookups";

    @MockitoBean
    private LookupService lookupService;

    @MockitoBean
    private ValidationService validationService;

    @Autowired
    private MockMvc mockMvc;
    // endregion - Class Variables -

    // region - Setup | Teardown -
    @BeforeAll
    public static void setup() {
        MDC.put("userId", String.format("User: %s", "LookupControllerTests"));
    }
    // endregion - Setup | Teardown -

    @Nested
    @DisplayName("Settings")
    protected class testSettings {
        @Nested
        @DisplayName("Get Settings - Defaults")
        protected class testGetSettings {
            @Test
            @DisplayName("Get Defaults - Expect OK")
            public void testGetDefaultSettings_defaults_expectOk() throws Throwable {
                mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING + "/settings/defaults")
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isOk());
            }

            @Test
            @DisplayName("Get Defaults By Level - Expect OK")
            public void testGetDefaultSettings_defaultsByLevel_expectOk() throws Throwable {
                mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING + "/settings/defaults")
                                .param("level", "SERVER")
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isOk());
            }
        }

        @Test
        @DisplayName("Get Levels - Expect OK")
        public void testGetDefaultSettings_levels_expectOk() throws Throwable {
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING + "/settings/levels")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Test
        @DisplayName("Get types - Expect OK")
        public void testGetDefaultSettings_defaultsByLevel_expectOk() throws Throwable {
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING + "/settings/types")
                            .param("level", "SERVER")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Nested
        @DisplayName("Update Settings - Defaults")
        protected class testUpdateSettingDefaults {
            @Test
            @DisplayName("Acceptable Inputs - Expect OK")
            public void testUpdateDefaultSettings_defaults_expectOk() throws Throwable {
                Map<String, String> testSettings = Map.of(
                        "Active", "true"
                );

                Mockito.doNothing().when(validationService).validateDefaultSettingsUpdateRequest(Mockito.anyString(), Mockito.anySet());

                mockMvc.perform(MockMvcRequestBuilders.put(BASE_MAPPING + "/settings/defaults")
                                .param("level", "DEFAULT")
                                .content(new ObjectMapper().writeValueAsString(testSettings))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isOk());
            }

            @Test
            @DisplayName("Unprocessable Inputs - Expect Error")
            public void testUpdateDefaultSettings_unproecssableInput_expectError() throws Throwable {
                Map<String, String> testSettings = Map.of(
                        "Active", "true"
                );

                Mockito.doThrow(OverseerUnprocessableException.class).when(validationService).validateDefaultSettingsUpdateRequest(Mockito.anyString(), Mockito.anySet());

                mockMvc.perform(MockMvcRequestBuilders.put(BASE_MAPPING + "/settings/defaults")
                                .param("level", "DEFAULT")
                                .content(new ObjectMapper().writeValueAsString(testSettings))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());
            }

            @Test
            @DisplayName("Invalid Inputs - Expect Error")
            public void testUpdateDefaultSettings_invalidInput_expectError() throws Throwable {
                Map<String, String> testSettings = Map.of(
                        "Active", "true"
                );

                Mockito.doThrow(OverseerBadRequestException.class).when(validationService).validateDefaultSettingsUpdateRequest(Mockito.anyString(), Mockito.anySet());

                mockMvc.perform(MockMvcRequestBuilders.put(BASE_MAPPING + "/settings/defaults")
                                .param("level", "DEFAULT")
                                .content(new ObjectMapper().writeValueAsString(testSettings))
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());
            }
        }

        @Test
        @DisplayName("Get Setting Levels - Expect OK")
        public void testGetSettingLevels_expectOk() throws Throwable {
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING + "/settings/levels")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Test
        @DisplayName("Get Setting Types - Expect OK")
        public void testGetSettingTypes_expectOk() throws Throwable {
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING + "/settings/types")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Nested
    @DisplayName("Builders")
    protected class testBuilders {
        @Nested
        @DisplayName("Get Builder Options")
        protected class testBuilderOptions {
            @Test
            @DisplayName("Get Builder Options - Expect OK")
            public void testGetBuilders_expectOk() throws Throwable {
                List<BuilderOption> testOptions = List.of(
                        new BuilderOption(
                                UUID.randomUUID().toString(),
                                BuilderType.values()[new Random().nextInt(BuilderType.values().length)],
                                BuilderCategory.values()[new Random().nextInt(BuilderCategory.values().length)],
                                null,
                                null)
                );

                Mockito.doReturn(testOptions).when(lookupService).getAllBuilderOptions();

                MvcResult results = mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING + "/builders/options")
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

                List<BuilderOption> response = new ObjectMapper().readValue(results.getResponse().getContentAsString(), new TypeReference<List<BuilderOption>>() {
                });

                Assertions.assertFalse(CollectionUtils.isEmpty(response));
                Assertions.assertEquals(response.getFirst().toString(), testOptions.getFirst().toString());
                Mockito.verify(lookupService, Mockito.times(1)).getAllBuilderOptions();
            }

            @Test
            @DisplayName("Get Builder Options - Expect None")
            public void testGetBuilders_expectNone() throws Throwable {
                Mockito.doThrow(OverseerNoContentException.class).when(lookupService).getAllBuilderOptions();

                mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING + "/builders/options")
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isNoContent());

                Mockito.verify(lookupService, Mockito.times(1)).getAllBuilderOptions();
            }
        }

        @Test
        @DisplayName("Get Builder Types - Expect OK")
        public void testGetBuilderTypes_expectOk() throws Throwable {
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING + "/builders/types")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Test
        @DisplayName("Get Builder Categories - Expect OK")
        public void testGetBuilderCategories_expectOk() throws Throwable {
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING + "/builders/categories")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Nested
    @DisplayName("Actions")
    protected class testActions {
        @Test
        @DisplayName("Get Action Categories - Expect OK")
        public void testGetActionCategories_expectOk() throws Throwable {
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING + "/actions/categories")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }

        @Test
        @DisplayName("Get Action Types - Expect OK")
        public void testGetActionTypes_expectOk() throws Throwable {
            mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING + "/actions/types")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());
        }
    }

    @Test
    @DisplayName("Get ApiKey Types - Expect OK")
    public void testGetApiKeyTypes_expectOk() throws Throwable {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING + "/apikeys/types")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Get Collection Tracking Types - Expect OK")
    public void testGetCollectionTrackingTypes_expectOk() throws Throwable {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING + "/collections/trackingTypes")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Get Media Types - Expect OK")
    public void testGetMediaTypes_expectOk() throws Throwable {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING + "/media/types")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Get Media Id Types - Expect OK")
    public void testGetMediaIdTypes_expectOk() throws Throwable {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING + "/mediaIds/types")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Get Media Image Types - Expect OK")
    public void testGetMediaImageTypes_expectOk() throws Throwable {
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING + "/mediaImages/types")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
