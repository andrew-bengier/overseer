package com.bnfd.overseer.controller;

import com.bnfd.overseer.exception.*;
import com.bnfd.overseer.model.api.ApiKey;
import com.bnfd.overseer.model.constants.ApiKeyType;
import com.bnfd.overseer.service.ApiKeyService;
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
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Slf4j
@WebMvcTest(ApiKeyController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ApiKeyControllerTests {
    // region - Class Variables -
    private static final String BASE_MAPPING = "/api/apikeys";

    @MockitoBean
    private ValidationService validationService;

    @MockitoBean
    private ApiKeyService apiKeyService;

    @Autowired
    private MockMvc mockMvc;
    // endregion - Class Variables -

    // region - Setup | Teardown -
    @BeforeAll
    public static void setup() {
        MDC.put("userId", String.format("User: %s", "ApiKeyControllerTests"));
    }
    // endregion - Setup | Teardown -

    @Nested
    @DisplayName("Add ApiKey")
    protected class testAddApiKey {
        @Test
        @DisplayName("Acceptable Inputs - Expect OK")
        public void testAddApiKey_acceptableInputs_expectOk() throws Throwable {
            ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
            ApiKey testKey = new ApiKey();
            testKey.setName(type);
            testKey.setKey("test");

            Mockito.doNothing().when(validationService).validateApiKey(Mockito.any(ApiKey.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.doAnswer(invocation -> {
                ApiKey serviceResult = invocation.getArgument(0);
                serviceResult.setId(UUID.randomUUID().toString());
                return serviceResult;
            }).when(apiKeyService).addApiKey(Mockito.any(ApiKey.class));

            MvcResult results = mockMvc.perform(MockMvcRequestBuilders.post(BASE_MAPPING)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testKey.toString()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            ApiKey response = new ObjectMapper().readValue(results.getResponse().getContentAsString(), ApiKey.class);

            Assertions.assertNotEquals(response.getId(), testKey.getId());
            Assertions.assertEquals(response.getName(), testKey.getName());
            Assertions.assertEquals(response.getKey(), testKey.getKey());
            Mockito.verify(validationService, Mockito.times(1)).validateApiKey(Mockito.any(ApiKey.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verify(apiKeyService, Mockito.times(1)).addApiKey(Mockito.any(ApiKey.class));
        }

        @Test
        @DisplayName("Invalid Inputs - Expect Error")
        public void testAddApiKey_invalidInputs_expectError() throws Throwable {
            ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
            ApiKey testKey = new ApiKey();
            testKey.setId(UUID.randomUUID().toString());
            testKey.setName(type);
            testKey.setKey("test");

            Mockito.doThrow(OverseerUnprocessableException.class).when(validationService).validateApiKey(Mockito.any(ApiKey.class), Mockito.any(), Mockito.anyBoolean());

            mockMvc.perform(MockMvcRequestBuilders.post(BASE_MAPPING)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testKey.toString()))
                    .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());

            Mockito.verify(validationService, Mockito.times(1)).validateApiKey(Mockito.any(ApiKey.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verify(apiKeyService, Mockito.times(0)).addApiKey(Mockito.any(ApiKey.class));
        }

        @Test
        @DisplayName("Missing Inputs - Expect Error")
        public void testAddApiKey_missingInputs_expectError() throws Throwable {
            ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
            ApiKey testKey = new ApiKey();
            testKey.setKey("test");

            Mockito.doThrow(OverseerBadRequestException.class).when(validationService).validateApiKey(Mockito.any(ApiKey.class), Mockito.any(), Mockito.anyBoolean());

            mockMvc.perform(MockMvcRequestBuilders.post(BASE_MAPPING)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testKey.toString()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());

            Mockito.verify(validationService, Mockito.times(1)).validateApiKey(Mockito.any(ApiKey.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verify(apiKeyService, Mockito.times(0)).addApiKey(Mockito.any(ApiKey.class));
        }

        @Test
        @DisplayName("Invalid Duplicate - Expect Error")
        public void testAddApiKey_invalidDuplicate_expectError() throws Throwable {
            ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
            ApiKey testKey = new ApiKey();
            testKey.setName(type);
            testKey.setKey("test");

            Mockito.doNothing().when(validationService).validateApiKey(Mockito.any(ApiKey.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.doThrow(OverseerConflictException.class).when(apiKeyService).addApiKey(Mockito.any(ApiKey.class));

            mockMvc.perform(MockMvcRequestBuilders.post(BASE_MAPPING)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testKey.toString()))
                    .andExpect(MockMvcResultMatchers.status().isConflict());

            Mockito.verify(validationService, Mockito.times(1)).validateApiKey(Mockito.any(ApiKey.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verify(apiKeyService, Mockito.times(1)).addApiKey(Mockito.any(ApiKey.class));
        }
    }

    @Nested
    @DisplayName("Get ApiKey")
    protected class testGetApiKey {
        @Test
        @DisplayName("Acceptable Inputs - Expect OK")
        public void testGetApiKey_acceptableInputs_expectOk() throws Throwable {
            String testId = UUID.randomUUID().toString();
            ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
            ApiKey testKey = new ApiKey();
            testKey.setId(testId);
            testKey.setName(type);
            testKey.setKey("test");

            Mockito.doReturn(testKey).when(apiKeyService).getApiKeyById(Mockito.any());

            String uri = BASE_MAPPING + "/{id}";
            MvcResult results = mockMvc.perform(MockMvcRequestBuilders.get(uri, testId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            ApiKey response = new ObjectMapper().readValue(results.getResponse().getContentAsString(), ApiKey.class);

            Assertions.assertEquals(response.toString(), testKey.toString());
            Mockito.verify(apiKeyService, Mockito.times(1)).getApiKeyById(Mockito.any());
        }

        @Test
        @DisplayName("Not Found - Expect Error")
        public void testGetApiKey_notFound_expectError() throws Throwable {
            Mockito.doThrow(OverseerNotFoundException.class).when(apiKeyService).getApiKeyById(Mockito.any());

            String uri = BASE_MAPPING + "/{id}";
            mockMvc.perform(MockMvcRequestBuilders.get(uri, UUID.randomUUID().toString())
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());

            Mockito.verify(apiKeyService, Mockito.times(1)).getApiKeyById(Mockito.any());
        }
    }

    @Nested
    @DisplayName("Get ApiKeys")
    protected class testGetApiKeys {
        @Nested
        @DisplayName("Get All ApiKeys")
        protected class testGetAllApiKeys {
            @Test
            @DisplayName("Acceptable Inputs - Expect OK")
            public void testGetApiKeys_acceptableInputs_expectOk() throws Throwable {
                String testId = UUID.randomUUID().toString();
                ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
                ApiKey testKey = new ApiKey();
                testKey.setId(testId);
                testKey.setName(type);
                testKey.setKey("test");

                Mockito.doReturn(List.of(testKey)).when(apiKeyService).getAllApiKeys();

                MvcResult results = mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

                List<ApiKey> response = new ObjectMapper().readValue(results.getResponse().getContentAsString(), new TypeReference<List<ApiKey>>() {
                });

                Assertions.assertFalse(CollectionUtils.isEmpty(response));
                Assertions.assertEquals(response.get(0).toString(), testKey.toString());
                Mockito.verify(apiKeyService, Mockito.times(1)).getAllApiKeys();
            }

            @Test
            @DisplayName("None Found - Expect Error")
            public void testGetApiKeys_noneFound_expectError() throws Throwable {
                Mockito.doThrow(OverseerNoContentException.class).when(apiKeyService).getAllApiKeys();

                mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING)
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isNoContent());

                Mockito.verify(apiKeyService, Mockito.times(1)).getAllApiKeys();
            }
        }

        @Nested
        @DisplayName("Search ApiKeys")
        protected class testSearchApiKeys {
            @Test
            @DisplayName("Acceptable Inputs - Expect OK")
            public void testSearchApiKeys_acceptableInputs_expectOk() throws Throwable {
                ApiKey testKey = new ApiKey(UUID.randomUUID().toString(), ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)], "test", null);

                Map<String, String> requestParams = Map.of("name", testKey.getName().name());

                Mockito.doReturn(List.of(testKey)).when(apiKeyService).getAllApiKeysByName(Mockito.any(ApiKeyType.class));

                MvcResult results = mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING)
                                .params(MultiValueMap.fromSingleValue(requestParams))
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andReturn();

                List<ApiKey> response = new ObjectMapper().readValue(results.getResponse().getContentAsString(), new TypeReference<List<ApiKey>>() {
                });

                Assertions.assertFalse(CollectionUtils.isEmpty(response));
                Assertions.assertEquals(response.getFirst().toString(), testKey.toString());
                Mockito.verify(apiKeyService, Mockito.times(1)).getAllApiKeysByName(Mockito.any(ApiKeyType.class));
            }

            @Test
            @DisplayName("No Matching Results - Expect Empty List")
            public void testSearchApiKeys_noResults_expectEmptyList() throws Throwable {
                Mockito.doThrow(new OverseerNoContentException("No Matching results found")).when(apiKeyService).getAllApiKeysByName(Mockito.any(ApiKeyType.class));

                mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING)
                                .param("name", ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)].name())
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isNoContent());

                Mockito.verify(apiKeyService, Mockito.times(1)).getAllApiKeysByName(Mockito.any(ApiKeyType.class));
            }

            @Test
            @DisplayName("Missing Search Criteria - Expect Error")
            public void testSearchApiKeys_missingCriteria_expectError() throws Throwable {
                Mockito.doThrow(OverseerBadRequestException.class).when(validationService).validateApiKeySearchParams(Mockito.anyMap());

                mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING)
                                .params(MultiValueMap.fromSingleValue(Map.of("invalidParam", "invalidValue")))
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());

                Mockito.verifyNoInteractions(apiKeyService);
            }

            @Test
            @DisplayName("Invalid Search Criteria - Expect Error")
            public void testSearchApiKeys_invalidCriteria_expectError() throws Throwable {
                Mockito.doThrow(OverseerBadRequestException.class).when(validationService).validateApiKeySearchParams(Mockito.anyMap());

                mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING)
                                .param("name", "invalid")
                                .accept(MediaType.APPLICATION_JSON))
                        .andExpect(MockMvcResultMatchers.status().isBadRequest());

                Mockito.verifyNoInteractions(apiKeyService);
            }
        }
    }

    @Nested
    @DisplayName("Update ApiKey")
    protected class testUpdateApiKey {
        @Test
        @DisplayName("Acceptable Inputs - Expect OK")
        public void testUpdateApiKey_acceptableInputs_expectOk() throws Throwable {
            String testId = UUID.randomUUID().toString();
            ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
            ApiKey testKey = new ApiKey();
            testKey.setId(testId);
            testKey.setName(type);
            testKey.setKey("test");

            Mockito.doNothing().when(validationService).validateApiKey(Mockito.any(ApiKey.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.doReturn(testKey).when(apiKeyService).updateApiKey(Mockito.any(ApiKey.class));

            String uri = BASE_MAPPING + "/{id}";
            MvcResult results = mockMvc.perform(MockMvcRequestBuilders.put(uri, testId)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testKey.toString()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            ApiKey response = new ObjectMapper().readValue(results.getResponse().getContentAsString(), ApiKey.class);

            Assertions.assertEquals(response.toString(), testKey.toString());
            Mockito.verify(validationService, Mockito.times(1)).validateApiKey(Mockito.any(ApiKey.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verify(apiKeyService, Mockito.times(1)).updateApiKey(Mockito.any(ApiKey.class));
        }

        @Test
        @DisplayName("Invalid Inputs - Expect Error")
        public void testAddApiKey_invalidInputs_expectError() throws Throwable {
            String testId = UUID.randomUUID().toString();
            ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
            ApiKey testKey = new ApiKey();
            testKey.setId(testId);
            testKey.setName(type);
            testKey.setKey("test");

            Mockito.doThrow(OverseerUnprocessableException.class).when(validationService).validateApiKey(Mockito.any(ApiKey.class), Mockito.any(), Mockito.anyBoolean());

            String uri = BASE_MAPPING + "/{id}";
            mockMvc.perform(MockMvcRequestBuilders.put(uri, testId)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testKey.toString()))
                    .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());

            Mockito.verify(validationService, Mockito.times(1)).validateApiKey(Mockito.any(ApiKey.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verify(apiKeyService, Mockito.times(0)).updateApiKey(Mockito.any(ApiKey.class));
        }

        @Test
        @DisplayName("Missing Inputs - Expect Error")
        public void testAddApiKey_missingInputs_expectError() throws Throwable {
            String testId = UUID.randomUUID().toString();
            ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
            ApiKey testKey = new ApiKey();
            testKey.setId(testId);
            testKey.setKey("test");

            Mockito.doThrow(OverseerBadRequestException.class).when(validationService).validateApiKey(Mockito.any(ApiKey.class), Mockito.any(), Mockito.anyBoolean());

            String uri = BASE_MAPPING + "/{id}";
            mockMvc.perform(MockMvcRequestBuilders.put(uri, testId)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testKey.toString()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());

            Mockito.verify(validationService, Mockito.times(1)).validateApiKey(Mockito.any(ApiKey.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verify(apiKeyService, Mockito.times(0)).updateApiKey(Mockito.any(ApiKey.class));
        }

        @Test
        @DisplayName("Invalid Duplicate - Expect Error")
        public void testAddApiKey_invalidDuplicate_expectError() throws Throwable {
            String testId = UUID.randomUUID().toString();
            ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
            ApiKey testKey = new ApiKey();
            testKey.setId(testId);
            testKey.setName(type);
            testKey.setKey("test");

            Mockito.doNothing().when(validationService).validateApiKey(Mockito.any(ApiKey.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.doThrow(OverseerConflictException.class).when(apiKeyService).updateApiKey(Mockito.any(ApiKey.class));

            String uri = BASE_MAPPING + "/{id}";
            mockMvc.perform(MockMvcRequestBuilders.put(uri, testId)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testKey.toString()))
                    .andExpect(MockMvcResultMatchers.status().isConflict());

            Mockito.verify(validationService, Mockito.times(1)).validateApiKey(Mockito.any(ApiKey.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verify(apiKeyService, Mockito.times(1)).updateApiKey(Mockito.any(ApiKey.class));
        }

        @Test
        @DisplayName("Not Found - Expect Error")
        public void testAddApiKey_notFound_expectError() throws Throwable {
            String testId = UUID.randomUUID().toString();
            ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
            ApiKey testKey = new ApiKey();
            testKey.setId(testId);
            testKey.setName(type);
            testKey.setKey("test");

            Mockito.doNothing().when(validationService).validateApiKey(Mockito.any(ApiKey.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.doThrow(OverseerNotFoundException.class).when(apiKeyService).updateApiKey(Mockito.any(ApiKey.class));

            String uri = BASE_MAPPING + "/{id}";
            mockMvc.perform(MockMvcRequestBuilders.put(uri, UUID.randomUUID().toString())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testKey.toString()))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());

            Mockito.verify(validationService, Mockito.times(1)).validateApiKey(Mockito.any(ApiKey.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verify(apiKeyService, Mockito.times(1)).updateApiKey(Mockito.any(ApiKey.class));
        }
    }

    @Nested
    @DisplayName("Delete ApiKey")
    protected class testDeleteApiKey {
        @Test
        @DisplayName("Acceptable Inputs - Expect OK")
        public void testGetApiKey_acceptableInputs_expectOk() throws Throwable {
            Mockito.doNothing().when(apiKeyService).removeApiKey(Mockito.any());

            String uri = BASE_MAPPING + "/{id}";
            mockMvc.perform(MockMvcRequestBuilders.delete(uri, UUID.randomUUID().toString())
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());

            Mockito.verify(apiKeyService, Mockito.times(1)).removeApiKey(Mockito.any());
        }

        @Test
        @DisplayName("Not Found - Expect Error")
        public void testGetApiKey_notFound_expectError() throws Throwable {
            Mockito.doThrow(OverseerNotFoundException.class).when(apiKeyService).removeApiKey(Mockito.any());

            String uri = BASE_MAPPING + "/{id}";
            mockMvc.perform(MockMvcRequestBuilders.delete(uri, UUID.randomUUID().toString())
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());

            Mockito.verify(apiKeyService, Mockito.times(1)).removeApiKey(Mockito.any());
        }
    }
}
