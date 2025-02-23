package com.bnfd.overseer.controller;

import com.bnfd.overseer.exception.*;
import com.bnfd.overseer.model.api.*;
import com.bnfd.overseer.model.constants.*;
import com.bnfd.overseer.service.*;
import com.bnfd.overseer.utils.*;
import com.fasterxml.jackson.core.type.*;
import com.fasterxml.jackson.databind.*;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.autoconfigure.web.servlet.*;
import org.springframework.http.*;
import org.springframework.test.context.bean.override.mockito.*;
import org.springframework.test.web.servlet.*;
import org.springframework.test.web.servlet.request.*;
import org.springframework.test.web.servlet.result.*;
import org.springframework.util.*;

import java.util.*;

@WebMvcTest(ServerController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ServerControllerTests {
    // region - Class Variables -
    private static final String BASE_MAPPING = "/api/servers";

    @MockitoBean
    private ValidationService validationService;

    @MockitoBean
    private ServerService serverService;

    @Autowired
    private MockMvc mockMvc;
    // endregion - Class Variables -

    // region - Setup | Teardown -
    @BeforeAll
    public static void setup() {
        MDC.put("userId", String.format("User: %s", "ServerControllerTests"));
    }
    // endregion - Setup | Teardown -

    @Nested
    @DisplayName("Add Server")
    protected class testAddServer {
        @Test
        @DisplayName("Acceptable Inputs - Expect OK")
        public void testAddServer_acceptableInputs_expectOk() throws Throwable {
            Setting testSetting = new Setting();
            testSetting.setType(SettingType.BOOLEAN);
            testSetting.setName(Constants.ACTIVE_SETTING);
            testSetting.setVal(Boolean.TRUE.toString());

            ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
            ApiKey testApiKey = new ApiKey();
            testApiKey.setId(UUID.randomUUID().toString());
            testApiKey.setName(type.name());
            testApiKey.setKey("test");

            Server testServer = new Server();
            testServer.setName("test");
            testServer.setApiKey(testApiKey);
            testServer.setSettings(Set.of(testSetting));

            Mockito.doNothing().when(validationService).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.doAnswer(invocation -> {
                Server serviceResult = invocation.getArgument(0);
                serviceResult.setId(UUID.randomUUID().toString());
                return serviceResult;
            }).when(serverService).addServer(Mockito.any(Server.class), Mockito.anyBoolean());

            MvcResult results = mockMvc.perform(MockMvcRequestBuilders.post(BASE_MAPPING)
                            .param("process", "false")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testServer.toString()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            Server response = new ObjectMapper().readValue(results.getResponse().getContentAsString(), Server.class);

            Assertions.assertNotNull(response.getId());
            Assertions.assertEquals(response.getName(), testServer.getName());
            Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verify(serverService, Mockito.times(1)).addServer(Mockito.any(Server.class), Mockito.anyBoolean());
            Mockito.verify(serverService, Mockito.times(0)).processServerById(Mockito.any(), Mockito.anyBoolean());
        }

        @Test
        @DisplayName("Missing Acceptable Inputs - Expect OK")
        public void testAddServer_missingAcceptableInputs_expectOk() throws Throwable {
            Setting testSetting = new Setting();
            testSetting.setType(SettingType.BOOLEAN);
            testSetting.setName(Constants.ACTIVE_SETTING);
            testSetting.setVal(Boolean.TRUE.toString());

            ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
            ApiKey testApiKey = new ApiKey();
            testApiKey.setId(UUID.randomUUID().toString());
            testApiKey.setName(type.name());
            testApiKey.setKey("test");

            Server testServer = new Server();
            testServer.setName("test");
            testServer.setApiKey(testApiKey);

            Mockito.doNothing().when(validationService).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.doAnswer(invocation -> {
                Server serviceResult = invocation.getArgument(0);
                serviceResult.setId(UUID.randomUUID().toString());
                serviceResult.setSettings(Set.of(testSetting));
                return serviceResult;
            }).when(serverService).addServer(Mockito.any(Server.class), Mockito.anyBoolean());

            MvcResult results = mockMvc.perform(MockMvcRequestBuilders.post(BASE_MAPPING)
                            .param("process", "false")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testServer.toString()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            Server response = new ObjectMapper().readValue(results.getResponse().getContentAsString(), Server.class);

            Assertions.assertNotNull(response.getId());
            Assertions.assertEquals(response.getName(), testServer.getName());
            Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verify(serverService, Mockito.times(1)).addServer(Mockito.any(Server.class), Mockito.anyBoolean());
            Mockito.verify(serverService, Mockito.times(0)).processServerById(Mockito.any(), Mockito.anyBoolean());
        }

        @Test
        @DisplayName("Missing Inputs - Expect Error")
        public void testAddServer_missingInputs_expectError() throws Throwable {
            Server testServer = new Server();
            testServer.setName("test");

            Mockito.doThrow(OverseerBadRequestException.class).when(validationService).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());

            mockMvc.perform(MockMvcRequestBuilders.post(BASE_MAPPING)
                            .param("process", "false")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testServer.toString()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());

            Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verify(serverService, Mockito.times(0)).addServer(Mockito.any(Server.class), Mockito.anyBoolean());
            Mockito.verify(serverService, Mockito.times(0)).processServerById(Mockito.any(), Mockito.anyBoolean());
        }
    }

    @Test
    @DisplayName("Invalid Inputs - Expect Error")
    public void testAddServer_invalidInputs_expectError() throws Throwable {
        Server testServer = new Server();
        testServer.setName("test");

        Mockito.doThrow(OverseerUnprocessableException.class).when(validationService).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());

        mockMvc.perform(MockMvcRequestBuilders.post(BASE_MAPPING)
                        .param("process", "false")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(testServer.toString()))
                .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());

        Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
        Mockito.verify(serverService, Mockito.times(0)).addServer(Mockito.any(Server.class), Mockito.anyBoolean());
        Mockito.verify(serverService, Mockito.times(0)).processServerById(Mockito.any(), Mockito.anyBoolean());
    }

    // TODO: add cases for processing from addition endpoint
    // TODO: add processServerById Tests

    @Nested
    @DisplayName("Get Server")
    protected class testGetServer {
        @Test
        @DisplayName("Acceptable Inputs - Expect OK")
        public void testGetApiKey_acceptableInputs_expectOk() throws Throwable {
            String testId = UUID.randomUUID().toString();
            Setting testSetting = new Setting();
            testSetting.setId(UUID.randomUUID().toString());
            testSetting.setType(SettingType.BOOLEAN);
            testSetting.setName(Constants.ACTIVE_SETTING);
            testSetting.setVal(Boolean.TRUE.toString());

            ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
            ApiKey testApiKey = new ApiKey();
            testApiKey.setId(UUID.randomUUID().toString());
            testApiKey.setName(type.name());
            testApiKey.setKey("test");

            Server testServer = new Server();
            testApiKey.setId(testId);
            testServer.setName("test");
            testServer.setApiKey(testApiKey);
            testServer.setSettings(Set.of(testSetting));

            Mockito.doReturn(testServer).when(serverService).getServerById(Mockito.any());

            String uri = BASE_MAPPING + "/{id}";
            MvcResult results = mockMvc.perform(MockMvcRequestBuilders.get(uri, testId)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            Server response = new ObjectMapper().readValue(results.getResponse().getContentAsString(), Server.class);

            Assertions.assertEquals(response.toString(), testServer.toString());
            Mockito.verify(serverService, Mockito.times(1)).getServerById(Mockito.any());
        }

        @Test
        @DisplayName("Not Found - Expect Error")
        public void testGetApiKey_notFound_expectError() throws Throwable {
            Mockito.doThrow(OverseerNotFoundException.class).when(serverService).getServerById(Mockito.any());

            String uri = BASE_MAPPING + "/{id}";
            mockMvc.perform(MockMvcRequestBuilders.get(uri, UUID.randomUUID().toString())
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());

            Mockito.verify(serverService, Mockito.times(1)).getServerById(Mockito.any());
        }
    }

    @Nested
    @DisplayName("Get All Servers")
    protected class testGetAllServers {
        @Test
        @DisplayName("Acceptable Inputs - Expect OK")
        public void testGetServers_acceptableInputs_expectOk() throws Throwable {
            Setting testSetting = new Setting();
            testSetting.setType(SettingType.BOOLEAN);
            testSetting.setName(Constants.ACTIVE_SETTING);
            testSetting.setVal(Boolean.TRUE.toString());

            ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
            ApiKey testApiKey = new ApiKey();
            testApiKey.setId(UUID.randomUUID().toString());
            testApiKey.setName(type.name());
            testApiKey.setKey("test");

            Server testServer = new Server();
            testServer.setName("test");
            testServer.setApiKey(testApiKey);
            testServer.setSettings(Set.of(testSetting));

            Mockito.doReturn(List.of(testServer)).when(serverService).getAllServers();

            MvcResult results = mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            List<Server> response = new ObjectMapper().readValue(results.getResponse().getContentAsString(), new TypeReference<List<Server>>() {
            });

            Assertions.assertFalse(CollectionUtils.isEmpty(response));
            Assertions.assertEquals(response.get(0).toString(), testServer.toString());
            Mockito.verify(serverService, Mockito.times(1)).getAllServers();
        }

        @Test
        @DisplayName("None Found - Expect Error")
        public void testGetServers_noneFound_expectError() throws Throwable {
            Mockito.doThrow(OverseerNoContentException.class).when(serverService).getAllServers();

            mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNoContent());

            Mockito.verify(serverService, Mockito.times(1)).getAllServers();
        }
    }

    @Nested
    @DisplayName("Update Server")
    protected class testUpdateServer {
    }

    @Nested
    @DisplayName("Update Server Settings")
    protected class testUpdateServerSettings {
    }

    @Nested
    @DisplayName("Update Server Settings - Active")
    protected class testUpdateServerActive {
    }

    @Nested
    @DisplayName("Delete Server")
    protected class testRemoveServer {
        @Test
        @DisplayName("Acceptable Inputs - Expect OK")
        public void testRemoveServer_acceptableInputs_expectOk() throws Throwable {
            Mockito.doNothing().when(serverService).removeServer(Mockito.any());

            String uri = BASE_MAPPING + "/{id}";
            mockMvc.perform(MockMvcRequestBuilders.delete(uri, UUID.randomUUID().toString())
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());

            Mockito.verify(serverService, Mockito.times(1)).removeServer(Mockito.any());
        }

        @Test
        @DisplayName("Not Found - Expect Error")
        public void testRemoveServer_notFound_expectError() throws Throwable {
            Mockito.doThrow(OverseerNotFoundException.class).when(serverService).removeServer(Mockito.any());

            String uri = BASE_MAPPING + "/{id}";
            mockMvc.perform(MockMvcRequestBuilders.delete(uri, UUID.randomUUID().toString())
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());

            Mockito.verify(serverService, Mockito.times(1)).removeServer(Mockito.any());
        }
    }
}
