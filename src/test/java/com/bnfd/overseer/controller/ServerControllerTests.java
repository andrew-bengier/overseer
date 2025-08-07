package com.bnfd.overseer.controller;

import com.bnfd.overseer.exception.*;
import com.bnfd.overseer.model.api.Action;
import com.bnfd.overseer.model.api.ApiKey;
import com.bnfd.overseer.model.api.Server;
import com.bnfd.overseer.model.api.Setting;
import com.bnfd.overseer.model.constants.ActionType;
import com.bnfd.overseer.model.constants.ApiKeyType;
import com.bnfd.overseer.model.constants.SettingType;
import com.bnfd.overseer.service.ServerService;
import com.bnfd.overseer.service.ValidationService;
import com.bnfd.overseer.utils.Constants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.SerializationUtils;
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

import java.util.*;

@Slf4j
@WebMvcTest(ServerController.class)
@AutoConfigureMockMvc(addFilters = false)
public class ServerControllerTests {
    // region - Class Variables -
    private static final String BASE_MAPPING = "/api/servers";
    private static final String ID_MAPPING = BASE_MAPPING + "/{serverId}";

    @MockitoBean
    private ValidationService validationService;

    @MockitoBean
    private ServerService serverService;

    private Server testServer;

    @Autowired
    private MockMvc mockMvc;
    // endregion - Class Variables -

    // region - Setup | Teardown -
    @BeforeAll
    public static void setup() {
        MDC.put("userId", String.format("User: %s", "ServerControllerTests"));
    }

    @BeforeEach
    public void init() {
        Setting testSetting = new Setting();
        testSetting.setId(UUID.randomUUID().toString());
        testSetting.setType(SettingType.BOOLEAN);
        testSetting.setName(Constants.ACTIVE_SETTING);
        testSetting.setVal(Boolean.TRUE.toString());

        Action testAction = new Action();
        testAction.setId(UUID.randomUUID().toString());
        testAction.setType(ActionType.BOOLEAN);
        testAction.setName("TestAction");
        testAction.setVal(Boolean.TRUE.toString());

        ApiKey testApiKey = new ApiKey();
        testApiKey.setId(UUID.randomUUID().toString());
        testApiKey.setName(ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)]);
        testApiKey.setKey("test");

        testServer = new Server();
        testServer.setId(UUID.randomUUID().toString());
        testServer.setName("test");
        testServer.setApiKey(testApiKey);
        testServer.setSettings(Set.of(testSetting));
        testServer.setActions(Set.of(testAction));
    }

    @AfterEach
    public void tearDown() {
        testServer = null;
    }
    // endregion - Setup | Teardown -

    @Nested
    @DisplayName("Get Servers")
    protected class testGetServers {
        @Test
        @DisplayName("Acceptable Inputs - Expect OK")
        public void testGetServers_acceptableInputs_expectOk() throws Throwable {
            Mockito.doReturn(List.of(testServer)).when(serverService).getAllServers();

            MvcResult results = mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING)
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            List<Server> response = new ObjectMapper().readValue(results.getResponse().getContentAsString(), new TypeReference<List<Server>>() {
            });

            Assertions.assertEquals(response.getFirst().toString(), testServer.toString());
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
    @DisplayName("Get Server By Id")
    protected class testGetServerById {
        @Test
        @DisplayName("Acceptable Inputs - Expect OK")
        public void testGetServerById_acceptableInputs_expectOk() throws Throwable {
            Mockito.doReturn(testServer).when(serverService).getServerById(Mockito.anyString());

            MvcResult results = mockMvc.perform(MockMvcRequestBuilders.get(ID_MAPPING, testServer.getId())
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            Server response = new ObjectMapper().readValue(results.getResponse().getContentAsString(), Server.class);

            Assertions.assertEquals(response.toString(), testServer.toString());
            Mockito.verify(serverService, Mockito.times(1)).getServerById(Mockito.anyString());
        }

        @Test
        @DisplayName("Not Found - Expect Error")
        public void testGetServers_notFound_expectError() throws Throwable {
            Mockito.doThrow(OverseerNotFoundException.class).when(serverService).getServerById(Mockito.anyString());

            mockMvc.perform(MockMvcRequestBuilders.get(ID_MAPPING, testServer.getId())
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());

            Mockito.verify(serverService, Mockito.times(1)).getServerById(Mockito.anyString());
        }
    }

    @Nested
    @DisplayName("Delete Server")
    protected class testRemoveServer {
        @Test
        @DisplayName("Acceptable Inputs - Expect OK")
        public void testRemoveServer_acceptableInputs_expectOk() throws Throwable {
            Mockito.doNothing().when(serverService).removeServer(Mockito.any());

            mockMvc.perform(MockMvcRequestBuilders.delete(ID_MAPPING, UUID.randomUUID().toString())
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());

            Mockito.verify(serverService, Mockito.times(1)).removeServer(Mockito.any());
        }

        @Test
        @DisplayName("Not Found - Expect Error")
        public void testRemoveServer_notFound_expectError() throws Throwable {
            Mockito.doThrow(OverseerNotFoundException.class).when(serverService).removeServer(Mockito.any());

            mockMvc.perform(MockMvcRequestBuilders.delete(ID_MAPPING, UUID.randomUUID().toString())
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());

            Mockito.verify(serverService, Mockito.times(1)).removeServer(Mockito.any());
        }
    }

    @Nested
    @DisplayName("Add Server")
    protected class testAddServer {
        @Test
        @DisplayName("Acceptable Inputs - Expect OK")
        public void testAddServer_acceptableInputs_expectOk() throws Throwable {
            testServer.setId(null);

            Mockito.doNothing().when(validationService).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.doAnswer(invocation -> {
                Server serviceResult = invocation.getArgument(0);
                serviceResult.setId(UUID.randomUUID().toString());
                return serviceResult;
            }).when(serverService).addServer(Mockito.any(Server.class), Mockito.anyBoolean());

            MvcResult results = mockMvc.perform(MockMvcRequestBuilders.post(BASE_MAPPING)
                            .param("includeLibraries", "false")
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
        }

        @Test
        @DisplayName("Invalid Inputs - Expect Error")
        public void testAddServer_invalidInputs_expectError() throws Throwable {
            Mockito.doThrow(OverseerBadRequestException.class).when(validationService).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());

            mockMvc.perform(MockMvcRequestBuilders.post(BASE_MAPPING)
                            .param("includeLibraries", "false")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testServer.toString()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());

            Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verifyNoInteractions(serverService);
        }

        @Test
        @DisplayName("Missing Inputs - Expect Error")
        public void testAddServer_missingInputs_expectError() throws Throwable {
            Mockito.doThrow(OverseerUnprocessableException.class).when(validationService).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());

            mockMvc.perform(MockMvcRequestBuilders.post(BASE_MAPPING)
                            .param("includeLibraries", "false")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testServer.toString()))
                    .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());

            Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verifyNoInteractions(serverService);
        }

        @Test
        @DisplayName("Conflict - Expect Error")
        public void testAddServer_conflict_expectError() throws Throwable {
            Mockito.doNothing().when(validationService).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.doThrow(OverseerConflictException.class).when(serverService).addServer(Mockito.any(Server.class), Mockito.anyBoolean());

            mockMvc.perform(MockMvcRequestBuilders.post(BASE_MAPPING)
                            .param("includeLibraries", "false")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testServer.toString()))
                    .andExpect(MockMvcResultMatchers.status().isConflict());

            Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verify(serverService, Mockito.times(1)).addServer(Mockito.any(Server.class), Mockito.anyBoolean());
        }

        @Test
        @DisplayName("Library Issues - Expect Error")
        public void testAddServer_libraryIssues_expectError() throws Throwable {
            Mockito.doNothing().when(validationService).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.doThrow(OverseerException.class).when(serverService).addServer(Mockito.any(Server.class), Mockito.anyBoolean());

            mockMvc.perform(MockMvcRequestBuilders.post(BASE_MAPPING)
                            .param("includeLibraries", "false")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testServer.toString()))
                    .andExpect(MockMvcResultMatchers.status().is5xxServerError());

            Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verify(serverService, Mockito.times(1)).addServer(Mockito.any(Server.class), Mockito.anyBoolean());
        }
    }

    //    // TODO: add cases for processing from addition endpoint
//    // TODO: add processServerById Tests

    @Nested
    @DisplayName("Update Server")
    protected class testUpdateServer {
        @Test
        @DisplayName("Acceptable Inputs - Expect OK")
        public void testUpdateServer_acceptableInputs_expectOk() throws Throwable {
            Mockito.doNothing().when(validationService).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.doAnswer(invocation -> {
                Server serviceResult = invocation.getArgument(0);
                serviceResult.setName("Updated");
                return serviceResult;
            }).when(serverService).updateServer(Mockito.any(Server.class));

            MvcResult results = mockMvc.perform(MockMvcRequestBuilders.put(ID_MAPPING, testServer.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testServer.toString()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            Server response = new ObjectMapper().readValue(results.getResponse().getContentAsString(), Server.class);

            Assertions.assertEquals(response.getId(), testServer.getId());
            Assertions.assertNotEquals(response.getName(), testServer.getName());
            Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verify(serverService, Mockito.times(1)).updateServer(Mockito.any(Server.class));
        }

        @Test
        @DisplayName("Not Found - Expect Error")
        public void testUpdateServer_notFound_expectError() throws Throwable {
            Mockito.doNothing().when(validationService).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.doThrow(OverseerNotFoundException.class).when(serverService).updateServer(Mockito.any(Server.class));

            mockMvc.perform(MockMvcRequestBuilders.put(ID_MAPPING, testServer.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testServer.toString()))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());

            Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verify(serverService, Mockito.times(1)).updateServer(Mockito.any(Server.class));
        }

        @Test
        @DisplayName("Invalid Inputs - Expect Error")
        public void testUpdateServer_invalidInputs_expectError() throws Throwable {
            Mockito.doThrow(OverseerBadRequestException.class).when(validationService).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());

            mockMvc.perform(MockMvcRequestBuilders.put(ID_MAPPING, testServer.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testServer.toString()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());

            Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verifyNoInteractions(serverService);
        }

        @Test
        @DisplayName("Missing Inputs - Expect Error")
        public void testUpdateServer_missingInputs_expectError() throws Throwable {
            Mockito.doThrow(OverseerUnprocessableException.class).when(validationService).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());

            mockMvc.perform(MockMvcRequestBuilders.put(ID_MAPPING, testServer.getId())
                            .param("includeLibraries", "false")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testServer.toString()))
                    .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());

            Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verifyNoInteractions(serverService);
        }

        @Test
        @DisplayName("Conflict - Expect Error")
        public void testUpdateServer_conflict_expectError() throws Throwable {
            Mockito.doNothing().when(validationService).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.doThrow(OverseerConflictException.class).when(serverService).updateServer(Mockito.any(Server.class));

            mockMvc.perform(MockMvcRequestBuilders.put(ID_MAPPING, testServer.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testServer.toString()))
                    .andExpect(MockMvcResultMatchers.status().isConflict());

            Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verify(serverService, Mockito.times(1)).updateServer(Mockito.any(Server.class));
        }
    }

    @Nested
    @DisplayName("Update Server Settings")
    protected class testUpdateServerSettings {
        String settingsUri = ID_MAPPING + "/settings";

        @Test
        @DisplayName("Acceptable Inputs - Expect OK")
        public void testUpdateServerSettings_acceptableInputs_expectOk() throws Throwable {
            Setting testSetting = new Setting();
            testSetting.setId(UUID.randomUUID().toString());
            testSetting.setType(SettingType.BOOLEAN);
            testSetting.setName("TestSetting");
            testSetting.setVal(Boolean.TRUE.toString());

            Set<Setting> testSettings = new HashSet<Setting>();
            testSettings.add(testSetting);
            testSettings.addAll(testServer.getSettings());

            Server updatedServer = SerializationUtils.clone(testServer);
            updatedServer.setSettings(testSettings);

            Mockito.doNothing().when(validationService).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.doReturn(updatedServer).when(serverService).updateServerSettings(Mockito.anyString(), Mockito.anySet());

            MvcResult results = mockMvc.perform(MockMvcRequestBuilders.put(settingsUri, testServer.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testServer.toString()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            Server response = new ObjectMapper().readValue(results.getResponse().getContentAsString(), Server.class);

            Assertions.assertEquals(response.getId(), testServer.getId());
            Assertions.assertNotEquals(response.getSettings().size(), testServer.getSettings().size());
            Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verify(serverService, Mockito.times(1)).updateServerSettings(Mockito.anyString(), Mockito.anySet());
        }

        @Test
        @DisplayName("Invalid Inputs - Expect Error")
        public void testUpdateServerSettings_invalidInputs_expectError() throws Throwable {
            Mockito.doThrow(OverseerBadRequestException.class).when(validationService).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());

            mockMvc.perform(MockMvcRequestBuilders.put(settingsUri, testServer.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testServer.toString()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());

            Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verifyNoInteractions(serverService);
        }

        @Test
        @DisplayName("Missing Inputs - Expect Error")
        public void testUpdateServerSettings_missingInputs_expectError() throws Throwable {
            Mockito.doThrow(OverseerUnprocessableException.class).when(validationService).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());

            mockMvc.perform(MockMvcRequestBuilders.put(settingsUri, testServer.getId())
                            .param("includeLibraries", "false")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testServer.toString()))
                    .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());

            Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verifyNoInteractions(serverService);
        }

        @Test
        @DisplayName("Not Found - Expect Error")
        public void testUpdateServerSettings_notFound_expectError() throws Throwable {
            Mockito.doNothing().when(validationService).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.doThrow(OverseerNotFoundException.class).when(serverService).updateServerSettings(Mockito.anyString(), Mockito.anySet());

            mockMvc.perform(MockMvcRequestBuilders.put(settingsUri, testServer.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testServer.toString()))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());

            Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verify(serverService, Mockito.times(1)).updateServerSettings(Mockito.anyString(), Mockito.anySet());
        }

        @Test
        @DisplayName("Conflict - Expect Error")
        public void testUpdateServerSettings_conflict_expectError() throws Throwable {
            Mockito.doNothing().when(validationService).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.doThrow(OverseerConflictException.class).when(serverService).updateServerSettings(Mockito.anyString(), Mockito.anySet());

            mockMvc.perform(MockMvcRequestBuilders.put(settingsUri, testServer.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testServer.toString()))
                    .andExpect(MockMvcResultMatchers.status().isConflict());

            Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verify(serverService, Mockito.times(1)).updateServerSettings(Mockito.anyString(), Mockito.anySet());
        }
    }

    @Nested
    @DisplayName("Update Server Actions")
    protected class testUpdateServerActions {
        String actionsUri = ID_MAPPING + "/actions";

        @Test
        @DisplayName("Acceptable Inputs - Expect OK")
        public void testUpdateServerActions_acceptableInputs_expectOk() throws Throwable {
            Action testAction = new Action();
            testAction.setId(UUID.randomUUID().toString());
            testAction.setType(ActionType.BOOLEAN);
            testAction.setName("TestAction2");
            testAction.setVal(Boolean.TRUE.toString());

            Set<Action> testActions = new HashSet<Action>();
            testActions.add(testAction);
            testActions.addAll(testServer.getActions());

            Server updatedServer = SerializationUtils.clone(testServer);
            updatedServer.setActions(testActions);

            Mockito.doNothing().when(validationService).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.doReturn(updatedServer).when(serverService).updateServerActions(Mockito.anyString(), Mockito.any());

            MvcResult results = mockMvc.perform(MockMvcRequestBuilders.put(actionsUri, testServer.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testServer.toString()))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            Server response = new ObjectMapper().readValue(results.getResponse().getContentAsString(), Server.class);

            Assertions.assertEquals(response.getId(), testServer.getId());
            Assertions.assertNotEquals(response.getActions().size(), testServer.getActions().size());
            Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verify(serverService, Mockito.times(1)).updateServerActions(Mockito.anyString(), Mockito.any());
        }

        @Test
        @DisplayName("Invalid Inputs - Expect Error")
        public void testUpdateServerActions_invalidInputs_expectError() throws Throwable {
            Mockito.doThrow(OverseerBadRequestException.class).when(validationService).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());

            mockMvc.perform(MockMvcRequestBuilders.put(actionsUri, testServer.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testServer.toString()))
                    .andExpect(MockMvcResultMatchers.status().isBadRequest());

            Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verifyNoInteractions(serverService);
        }

        @Test
        @DisplayName("Missing Inputs - Expect Error")
        public void testUpdateServerActions_missingInputs_expectError() throws Throwable {
            Mockito.doThrow(OverseerUnprocessableException.class).when(validationService).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());

            mockMvc.perform(MockMvcRequestBuilders.put(actionsUri, testServer.getId())
                            .param("includeLibraries", "false")
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testServer.toString()))
                    .andExpect(MockMvcResultMatchers.status().isUnprocessableEntity());

            Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verifyNoInteractions(serverService);
        }

        @Test
        @DisplayName("Not Found - Expect Error")
        public void testUpdateServerActions_notFound_expectError() throws Throwable {
            Mockito.doNothing().when(validationService).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.doThrow(OverseerNotFoundException.class).when(serverService).updateServerActions(Mockito.anyString(), Mockito.any());

            mockMvc.perform(MockMvcRequestBuilders.put(actionsUri, testServer.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testServer.toString()))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());

            Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verify(serverService, Mockito.times(1)).updateServerActions(Mockito.anyString(), Mockito.any());
        }

        @Test
        @DisplayName("Conflict - Expect Error")
        public void testUpdateServerActions_conflict_expectError() throws Throwable {
            Mockito.doNothing().when(validationService).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.doThrow(OverseerConflictException.class).when(serverService).updateServerActions(Mockito.anyString(), Mockito.any());

            mockMvc.perform(MockMvcRequestBuilders.put(actionsUri, testServer.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(testServer.toString()))
                    .andExpect(MockMvcResultMatchers.status().isConflict());

            Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
            Mockito.verify(serverService, Mockito.times(1)).updateServerActions(Mockito.anyString(), Mockito.any());
        }
    }

    @Nested
    @DisplayName("Update Server Settings - Active")
    protected class testUpdateServerActive {
        String activeUri = ID_MAPPING + "/active";

        @Test
        @DisplayName("Acceptable Inputs - Expect OK")
        public void testUpdateServerActive_acceptableInputs_expectOk() throws Throwable {
            Setting testSetting = new Setting();
            testSetting.setId(UUID.randomUUID().toString());
            testSetting.setType(SettingType.BOOLEAN);
            testSetting.setName(Constants.ACTIVE_SETTING);
            testSetting.setVal(Boolean.FALSE.toString());

            Server updatedServer = SerializationUtils.clone(testServer);
            updatedServer.setSettings(Set.of(testSetting));

            Mockito.doReturn(updatedServer).when(serverService).updateServerActiveSetting(Mockito.anyString(), Mockito.anyBoolean());

            MvcResult results = mockMvc.perform(MockMvcRequestBuilders.patch(activeUri, testServer.getId())
                            .param("active", Boolean.FALSE.toString())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            Server response = new ObjectMapper().readValue(results.getResponse().getContentAsString(), Server.class);
            Setting actualSetting = response.getSettings().stream().filter(s -> s.getName().equals(Constants.ACTIVE_SETTING)).findFirst().orElseThrow(OverseerNotFoundException::new);
            Setting expectedSetting = testServer.getSettings().stream().filter(s -> s.getName().equals(Constants.ACTIVE_SETTING)).findFirst().orElseThrow(OverseerNotFoundException::new);

            Assertions.assertEquals(response.getId(), testServer.getId());
            Assertions.assertNotEquals(actualSetting.getVal(), expectedSetting.getVal());
            Mockito.verify(serverService, Mockito.times(1)).updateServerActiveSetting(Mockito.anyString(), Mockito.anyBoolean());
        }

        @Test
        @DisplayName("Not Found - Expect Error")
        public void testUpdateServerActive_notFound_expectError() throws Throwable {
            Mockito.doThrow(OverseerNotFoundException.class).when(serverService).updateServerActiveSetting(Mockito.anyString(), Mockito.anyBoolean());

            mockMvc.perform(MockMvcRequestBuilders.patch(activeUri, testServer.getId())
                            .param("active", Boolean.FALSE.toString())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());

            Mockito.verify(serverService, Mockito.times(1)).updateServerActiveSetting(Mockito.anyString(), Mockito.anyBoolean());
        }

        @Test
        @DisplayName("Conflict - Expect Error")
        public void testUpdateServerActive_conflict_expectError() throws Throwable {
            Mockito.doThrow(OverseerConflictException.class).when(serverService).updateServerActiveSetting(Mockito.anyString(), Mockito.anyBoolean());

            mockMvc.perform(MockMvcRequestBuilders.patch(activeUri, testServer.getId())
                            .param("active", Boolean.FALSE.toString())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isConflict());

            Mockito.verify(serverService, Mockito.times(1)).updateServerActiveSetting(Mockito.anyString(), Mockito.anyBoolean());
        }
    }

    @Nested
    @DisplayName("Resync Server")
    protected class testResyncServer {
        @Test
        @DisplayName("Acceptable Inputs - Expect OK")
        public void testResyncServer_acceptableInputs_expectOk() throws Throwable {
            Mockito.doReturn(testServer).when(serverService).resyncServer(Mockito.anyString());

            MvcResult results = mockMvc.perform(MockMvcRequestBuilders.put(ID_MAPPING + "/resync", testServer.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            Server response = new ObjectMapper().readValue(results.getResponse().getContentAsString(), Server.class);

            Assertions.assertEquals(response.getId(), testServer.getId());
            Mockito.verify(serverService, Mockito.times(1)).resyncServer(Mockito.anyString());
        }

        @Test
        @DisplayName("Not Found - Expect Error")
        public void testResyncServer_notFound_expectError() throws Throwable {
            Mockito.doThrow(OverseerNotFoundException.class).when(serverService).resyncServer(Mockito.anyString());

            mockMvc.perform(MockMvcRequestBuilders.put(ID_MAPPING + "/resync", testServer.getId())
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNotFound());

            Mockito.verify(serverService, Mockito.times(1)).resyncServer(Mockito.anyString());
        }
    }
}
