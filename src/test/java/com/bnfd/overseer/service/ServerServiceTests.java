package com.bnfd.overseer.service;

import com.bnfd.overseer.exception.*;
import com.bnfd.overseer.model.api.*;
import com.bnfd.overseer.model.constants.*;
import com.bnfd.overseer.model.mapper.*;
import com.bnfd.overseer.model.persistence.apikeys.*;
import com.bnfd.overseer.model.persistence.servers.*;
import com.bnfd.overseer.repository.*;
import com.bnfd.overseer.utils.*;
import jakarta.persistence.*;
import lombok.extern.slf4j.*;
import org.apache.commons.lang3.ObjectUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import org.modelmapper.*;
import org.springframework.dao.*;
import org.springframework.test.context.*;
import org.springframework.util.*;

import java.util.*;

@Slf4j
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ServerServiceTests {
    // region - Class Variables -
    @Mock
    private ServerRepository serverRepository;

    @Mock
    private LibraryService libraryService;

    @Mock
    private ServerSettingRepository serverSettingRepository;

    @Spy
    private final ModelMapper overseerMapper = new OverseerMapper().mapper();

    @Spy
    @InjectMocks
    private ServerService serverService;
    // endregion - Class Variables -

    @Nested
    @DisplayName("Create Server")
    protected class testAddServer {
        MockedStatic<ValidationUtils> mockedValidationUtils;

        @BeforeEach
        public void preTestSetup() {
            mockedValidationUtils = Mockito.mockStatic(ValidationUtils.class);

            mockedValidationUtils.when(() -> ValidationUtils.convertStringToBoolean(Mockito.any())).thenCallRealMethod();
        }

        @AfterEach
        public void posTestTeardown() {
            mockedValidationUtils.close();
            mockedValidationUtils = null;
        }

        @Test
        @DisplayName("Acceptable Inputs - Expect Model")
        public void testAddServer_acceptableInputs_expectModel() {
            ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
            ApiKey testApiKey = new ApiKey();
            testApiKey.setId(UUID.randomUUID().toString());
            testApiKey.setName(type.name());
            testApiKey.setKey("test");

            ApiKeyEntity apiKeyEntity = new ApiKeyEntity();
            apiKeyEntity.setId(UUID.randomUUID().toString());
            apiKeyEntity.setName(type.name());
            apiKeyEntity.setKey("test");

            ServerSettingEntity settingEntity = new ServerSettingEntity();
            settingEntity.setId(UUID.randomUUID().toString());
            settingEntity.setType(SettingType.BOOLEAN.name());
            settingEntity.setName(Constants.ACTIVE_SETTING);
            settingEntity.setVal(Boolean.TRUE.toString());

            Server testServer = new Server();
            testServer.setId(UUID.randomUUID().toString());
            testServer.setName("test");
            testServer.setApiKey(testApiKey);

            ServerEntity serverEntity = new ServerEntity();
            serverEntity.setName("test");
            serverEntity.setApiKey(apiKeyEntity);
            serverEntity.setSettings(Set.of(settingEntity));

            Mockito.doReturn(serverEntity).when(serverService).configureNewServer(Mockito.any());
            Mockito.when(serverRepository.save(Mockito.any(ServerEntity.class)))
                    .thenAnswer(
                            invocation -> {
                                ServerEntity dbResult = invocation.getArgument(0);
                                dbResult.setId(UUID.randomUUID().toString());
                                return dbResult;
                            }
                    );
            Server results = serverService.addServer(testServer, false);

            Assertions.assertNotNull(results);

            Mockito.verify(serverService, Mockito.times(1)).addServer(Mockito.any(Server.class), Mockito.anyBoolean());
            Mockito.verify(serverService, Mockito.times(1)).configureNewServer(Mockito.any(ServerEntity.class));
            Mockito.verify(serverRepository, Mockito.times(1)).save(Mockito.any(ServerEntity.class));
            Mockito.verify(serverService, Mockito.times(0)).processServerById(Mockito.any(), Mockito.anyBoolean());
            Mockito.verify(overseerMapper, Mockito.times(1)).map(Mockito.any(Server.class), Mockito.eq(ServerEntity.class));
            Mockito.verify(overseerMapper, Mockito.times(1)).map(Mockito.any(ServerEntity.class), Mockito.eq(Server.class));
        }

        @Test
        @DisplayName("Conflict - Expect Error")
        public void testAddServer_conflict_expectError() {
            ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
            ApiKey testApiKey = new ApiKey();
            testApiKey.setId(UUID.randomUUID().toString());
            testApiKey.setName(type.name());
            testApiKey.setKey("test");

            ApiKeyEntity apiKeyEntity = new ApiKeyEntity();
            apiKeyEntity.setId(UUID.randomUUID().toString());
            apiKeyEntity.setName(type.name());
            apiKeyEntity.setKey("test");

            ServerSettingEntity settingEntity = new ServerSettingEntity();
            settingEntity.setId(UUID.randomUUID().toString());
            settingEntity.setType(SettingType.BOOLEAN.name());
            settingEntity.setName(Constants.ACTIVE_SETTING);
            settingEntity.setVal(Boolean.TRUE.toString());

            Server testServer = new Server();
            testServer.setId(UUID.randomUUID().toString());
            testServer.setName("test");
            testServer.setApiKey(testApiKey);

            ServerEntity serverEntity = new ServerEntity();
            serverEntity.setName("test");
            serverEntity.setApiKey(apiKeyEntity);
            serverEntity.setSettings(Set.of(settingEntity));

            Mockito.doReturn(serverEntity).when(serverService).configureNewServer(Mockito.any());
            Mockito.doThrow(new OverseerConflictException()).when(serverRepository).save(Mockito.any());

            Assertions.assertThrows(OverseerConflictException.class, () -> serverService.addServer(testServer, false));

            Mockito.verify(serverService, Mockito.times(1)).addServer(Mockito.any(Server.class), Mockito.anyBoolean());
            Mockito.verify(serverService, Mockito.times(1)).configureNewServer(Mockito.any(ServerEntity.class));
            Mockito.verify(serverRepository, Mockito.times(1)).save(Mockito.any(ServerEntity.class));
            Mockito.verify(serverService, Mockito.times(0)).updateSettings(Mockito.any(ServerEntity.class), Mockito.anySet());
            Mockito.verify(serverService, Mockito.times(0)).processServerById(Mockito.any(), Mockito.anyBoolean());
            Mockito.verify(overseerMapper, Mockito.times(1)).map(Mockito.any(Server.class), Mockito.eq(ServerEntity.class));
            Mockito.verify(overseerMapper, Mockito.times(0)).map(Mockito.any(ServerEntity.class), Mockito.eq(Server.class));
        }
        // TODO: add cases for processing here
    }

    // TODO: add processServerById test here

    @Nested
    @DisplayName("Get Server By Id")
    protected class testGetServerById {
        @Test
        @DisplayName("Acceptable Inputs - Expect Model")
        public void testGetServerById_acceptableInputs_expectModel() {
            String testId = UUID.randomUUID().toString();
            ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
            ApiKeyEntity apiKeyEntity = new ApiKeyEntity();
            apiKeyEntity.setId(UUID.randomUUID().toString());
            apiKeyEntity.setName(type.name());
            apiKeyEntity.setKey("test");

            ServerSettingEntity settingEntity = new ServerSettingEntity();
            settingEntity.setId(UUID.randomUUID().toString());
            settingEntity.setType(SettingType.BOOLEAN.name());
            settingEntity.setName(Constants.ACTIVE_SETTING);
            settingEntity.setVal(Boolean.TRUE.toString());

            ServerEntity serverEntity = new ServerEntity();
            serverEntity.setId(testId);
            serverEntity.setName("test");
            serverEntity.setApiKey(apiKeyEntity);
            serverEntity.setSettings(Set.of(settingEntity));

            Mockito.when(serverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(serverEntity));

            Server results = serverService.getServerById(testId);

            Assertions.assertTrue(ObjectUtils.isNotEmpty(results));

            Mockito.verify(serverService, Mockito.times(1)).getServerById(Mockito.anyString());
            Mockito.verify(serverRepository, Mockito.times(1)).findById(Mockito.anyString());
            Mockito.verify(overseerMapper, Mockito.times(1)).map(Mockito.any(ServerEntity.class), Mockito.eq(Server.class));
        }

        @Test
        @DisplayName("Not Found - Expect Error")
        public void testGetServerById_notFound_expectError() {
            Mockito.doThrow(new OverseerNotFoundException()).when(serverRepository).findById(Mockito.anyString());

            Assertions.assertThrows(OverseerNotFoundException.class, () -> serverService.getServerById(UUID.randomUUID().toString()));

            Mockito.verify(serverService, Mockito.times(1)).getServerById(Mockito.anyString());
            Mockito.verify(serverRepository, Mockito.times(1)).findById(Mockito.anyString());
            Mockito.verify(overseerMapper, Mockito.times(0)).map(Mockito.any(ServerEntity.class), Mockito.eq(Server.class));
        }
    }

    @Nested
    @DisplayName("Get All Servers")
    protected class testGetAllServers {
        @Test
        @DisplayName("Acceptable Inputs - Expect Model")
        public void testGetAllServers_acceptableInputs_expectModel() {
            ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
            ApiKeyEntity apiKeyEntity = new ApiKeyEntity();
            apiKeyEntity.setId(UUID.randomUUID().toString());
            apiKeyEntity.setName(type.name());
            apiKeyEntity.setKey("test");

            ServerSettingEntity settingEntity = new ServerSettingEntity();
            settingEntity.setId(UUID.randomUUID().toString());
            settingEntity.setType(SettingType.BOOLEAN.name());
            settingEntity.setName(Constants.ACTIVE_SETTING);
            settingEntity.setVal(Boolean.TRUE.toString());

            ServerEntity serverEntity = new ServerEntity();
            serverEntity.setId(UUID.randomUUID().toString());
            serverEntity.setName("test");
            serverEntity.setApiKey(apiKeyEntity);
            serverEntity.setSettings(Set.of(settingEntity));

            Mockito.when(serverRepository.findAll()).thenReturn(List.of(serverEntity));

            List<Server> results = serverService.getAllServers();

            Assertions.assertFalse(CollectionUtils.isEmpty(results));

            Mockito.verify(serverService, Mockito.times(1)).getAllServers();
            Mockito.verify(serverRepository, Mockito.times(1)).findAll();
            Mockito.verify(overseerMapper, Mockito.times(1)).map(Mockito.anyList(), Mockito.eq(new TypeToken<List<Server>>() {
            }.getType()));
        }

        @Test
        @DisplayName("None Found - Expect Error")
        public void testGetAllServers_noneFound_expectError() {
            Mockito.when(serverRepository.findAll()).thenReturn(null);

            Assertions.assertThrows(OverseerNoContentException.class, () -> serverService.getAllServers());

            Mockito.verify(serverService, Mockito.times(1)).getAllServers();
            Mockito.verify(serverRepository, Mockito.times(1)).findAll();
            Mockito.verify(overseerMapper, Mockito.times(0)).map(Mockito.anyList(), Mockito.eq(new TypeToken<List<Server>>() {
            }.getType()));
        }
    }

    @Nested
    @DisplayName("Update Server")
    protected class testUpdateServer {
        @Test
        @DisplayName("Acceptable Inputs - Expect Model")
        public void testUpdateServer_acceptableInputs_expectModel() {
            ServerSettingEntity settingEntity = new ServerSettingEntity();
            settingEntity.setId(UUID.randomUUID().toString());
            settingEntity.setType(SettingType.BOOLEAN.name());
            settingEntity.setName(Constants.ACTIVE_SETTING);
            settingEntity.setVal(Boolean.TRUE.toString());

            Server testServer = new Server();
            testServer.setId(UUID.randomUUID().toString());
            testServer.setName("test");

            ServerEntity serverEntity = new ServerEntity();
            serverEntity.setName("test");
            serverEntity.setSettings(Set.of(settingEntity));

            Mockito.when(serverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(serverEntity));
            Mockito.when(serverRepository.save(Mockito.any(ServerEntity.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
            Mockito.doReturn(Set.of(settingEntity)).when(serverService).updateSettings(Mockito.any(), Mockito.any());

            Server results = serverService.updateServer(testServer);

            Assertions.assertNotNull(results);

            Mockito.verify(serverService, Mockito.times(1)).updateServer(Mockito.any(Server.class));
            Mockito.verify(serverRepository, Mockito.times(1)).findById(Mockito.anyString());
            Mockito.verify(serverRepository, Mockito.times(1)).save(Mockito.any(ServerEntity.class));
            Mockito.verify(serverService, Mockito.times(1)).updateSettings(Mockito.any(ServerEntity.class), Mockito.any());
            Mockito.verify(overseerMapper, Mockito.times(1)).map(Mockito.any(Server.class), Mockito.eq(ServerEntity.class));
            Mockito.verify(overseerMapper, Mockito.times(1)).map(Mockito.any(ServerEntity.class), Mockito.eq(Server.class));
        }

        @Test
        @DisplayName("Not Found - Expect Error")
        public void testUpdateServer_notFound_expectError() {
            Server testServer = new Server();
            testServer.setId(UUID.randomUUID().toString());
            testServer.setName("test");

            Mockito.when(serverRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());

            Assertions.assertThrows(OverseerNotFoundException.class, () -> serverService.updateServer(testServer));

            Mockito.verify(serverService, Mockito.times(1)).updateServer(Mockito.any(Server.class));
            Mockito.verify(serverRepository, Mockito.times(1)).findById(Mockito.anyString());
            Mockito.verify(serverRepository, Mockito.times(0)).save(Mockito.any(ServerEntity.class));
            Mockito.verify(serverService, Mockito.times(0)).updateSettings(Mockito.any(ServerEntity.class), Mockito.anySet());
            Mockito.verify(overseerMapper, Mockito.times(0)).map(Mockito.any(Server.class), Mockito.eq(ServerEntity.class));
            Mockito.verify(overseerMapper, Mockito.times(0)).map(Mockito.any(ServerEntity.class), Mockito.eq(Server.class));
        }

        @Test
        @DisplayName("Conflict with settings - Expect Error")
        public void testUpdateServer_conflictWithSettings_expectError() {
            ServerSettingEntity settingEntity = new ServerSettingEntity();
            settingEntity.setId(UUID.randomUUID().toString());
            settingEntity.setType(SettingType.BOOLEAN.name());
            settingEntity.setName(Constants.ACTIVE_SETTING);
            settingEntity.setVal(Boolean.TRUE.toString());

            Server testServer = new Server();
            testServer.setId(UUID.randomUUID().toString());
            testServer.setName("test");

            ServerEntity serverEntity = new ServerEntity();
            serverEntity.setName("test");
            serverEntity.setSettings(Set.of(settingEntity));

            Mockito.when(serverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(serverEntity));
            Mockito.when(serverRepository.save(Mockito.any(ServerEntity.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());
            Mockito.doThrow(new OverseerConflictException()).when(serverService).updateSettings(Mockito.any(), Mockito.any());

            Assertions.assertThrows(OverseerConflictException.class, () -> serverService.updateServer(testServer));

            Mockito.verify(serverService, Mockito.times(1)).updateServer(Mockito.any(Server.class));
            Mockito.verify(serverRepository, Mockito.times(1)).save(Mockito.any(ServerEntity.class));
            Mockito.verify(serverService, Mockito.times(1)).updateSettings(Mockito.any(), Mockito.any());
            Mockito.verify(overseerMapper, Mockito.times(1)).map(Mockito.any(Server.class), Mockito.eq(ServerEntity.class));
            Mockito.verify(overseerMapper, Mockito.times(0)).map(Mockito.any(ServerEntity.class), Mockito.eq(Server.class));
        }
    }

    @Nested
    @DisplayName("Update Server Settings")
    protected class testUpdateServerSettings {
        @Test
        @DisplayName("Acceptable Inputs - Expect Model")
        public void testUpdateServerSettings_acceptableInputs_expectModel() {
            ServerSettingEntity settingEntity = new ServerSettingEntity();
            settingEntity.setId(UUID.randomUUID().toString());
            settingEntity.setType(SettingType.BOOLEAN.name());
            settingEntity.setName(Constants.ACTIVE_SETTING);
            settingEntity.setVal(Boolean.TRUE.toString());

            Setting setting = new Setting();
            setting.setId(UUID.randomUUID().toString());
            setting.setType(SettingType.BOOLEAN);
            setting.setName(Constants.ACTIVE_SETTING);
            setting.setVal(Boolean.TRUE.toString());

            Server testServer = new Server();
            testServer.setId(UUID.randomUUID().toString());
            testServer.setName("test");

            ServerEntity serverEntity = new ServerEntity();
            serverEntity.setId(testServer.getId());
            serverEntity.setName("test");

            Mockito.when(serverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(serverEntity));
            Mockito.doReturn(Set.of(settingEntity)).when(serverService).updateSettings(Mockito.any(), Mockito.anySet());

            Server results = serverService.updateServerSettings(testServer.getId(), Set.of(setting));

            Assertions.assertNotNull(results);

            Mockito.verify(serverService, Mockito.times(1)).updateServerSettings(Mockito.anyString(), Mockito.anySet());
            Mockito.verify(serverRepository, Mockito.times(1)).findById(Mockito.anyString());
            Mockito.verify(serverService, Mockito.times(1)).updateSettings(Mockito.any(ServerEntity.class), Mockito.anySet());
            Mockito.verify(overseerMapper, Mockito.times(1)).map(Mockito.any(), Mockito.eq(new TypeToken<Set<ServerSettingEntity>>() {
            }.getType()));
            Mockito.verify(overseerMapper, Mockito.times(1)).map(Mockito.any(ServerEntity.class), Mockito.eq(Server.class));
        }

        @Test
        @DisplayName("Not Found - Expect Error")
        public void testUpdateServerSettings_notFound_expectError() {
            Server testServer = new Server();
            testServer.setId(UUID.randomUUID().toString());
            testServer.setName("test");

            Mockito.when(serverRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());

            Assertions.assertThrows(OverseerNotFoundException.class, () -> serverService.updateServerSettings(UUID.randomUUID().toString(), Set.of(new Setting())));

            Mockito.verify(serverService, Mockito.times(1)).updateServerSettings(Mockito.anyString(), Mockito.anySet());
            Mockito.verify(serverRepository, Mockito.times(1)).findById(Mockito.anyString());
            Mockito.verify(serverService, Mockito.times(0)).updateSettings(Mockito.any(ServerEntity.class), Mockito.anySet());
            Mockito.verify(overseerMapper, Mockito.times(0)).map(Mockito.anyList(), Mockito.eq(new TypeToken<Set<ServerSettingEntity>>() {
            }.getType()));
            Mockito.verify(overseerMapper, Mockito.times(0)).map(Mockito.any(ServerEntity.class), Mockito.eq(Server.class));
        }

        @Test
        @DisplayName("Conflict - Expect Error")
        public void testUpdateServerSettings_conflict_expectError() {
            Setting setting = new Setting();
            setting.setId(UUID.randomUUID().toString());
            setting.setType(SettingType.BOOLEAN);
            setting.setName(Constants.ACTIVE_SETTING);
            setting.setVal(Boolean.TRUE.toString());

            Server testServer = new Server();
            testServer.setId(UUID.randomUUID().toString());
            testServer.setName("test");

            ServerEntity serverEntity = new ServerEntity();
            serverEntity.setName("test");

            Mockito.when(serverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(serverEntity));
            Mockito.doThrow(new OverseerConflictException()).when(serverService).updateSettings(Mockito.any(), Mockito.anySet());

            Assertions.assertThrows(OverseerConflictException.class, () -> serverService.updateServerSettings(UUID.randomUUID().toString(), Set.of(setting)));

            Mockito.verify(serverService, Mockito.times(1)).updateServerSettings(Mockito.anyString(), Mockito.anySet());
            Mockito.verify(serverRepository, Mockito.times(1)).findById(Mockito.anyString());
            Mockito.verify(serverService, Mockito.times(1)).updateSettings(Mockito.any(ServerEntity.class), Mockito.anySet());
            Mockito.verify(overseerMapper, Mockito.times(1)).map(Mockito.any(), Mockito.eq(new TypeToken<Set<ServerSettingEntity>>() {
            }.getType()));
            Mockito.verify(overseerMapper, Mockito.times(0)).map(Mockito.any(ServerEntity.class), Mockito.eq(Server.class));
        }
    }

    @Nested
    @DisplayName("Update Server Settings - Active")
    protected class testUpdateServerActiveS {
        @Test
        @DisplayName("Acceptable Inputs - Expect Nothing")
        public void testUpdateServerActive_acceptableInputs_expectNothing() {
            ServerSettingEntity settingEntity = new ServerSettingEntity();
            settingEntity.setId(UUID.randomUUID().toString());
            settingEntity.setType(SettingType.BOOLEAN.name());
            settingEntity.setName(Constants.ACTIVE_SETTING);
            settingEntity.setVal(Boolean.TRUE.toString());

            ServerEntity serverEntity = new ServerEntity();
            serverEntity.setId(UUID.randomUUID().toString());
            serverEntity.setName("test");
            serverEntity.setSettings(Set.of(settingEntity));

            Mockito.when(serverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(serverEntity));
            Mockito.when(serverSettingRepository.save(Mockito.any(ServerSettingEntity.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

            serverService.updateServerActive(serverEntity.getId(), new Random().nextBoolean());

            Mockito.verify(serverService, Mockito.times(1)).updateServerActive(Mockito.anyString(), Mockito.anyBoolean());
            Mockito.verify(serverRepository, Mockito.times(1)).findById(Mockito.anyString());
            Mockito.verify(serverSettingRepository, Mockito.times(1)).save(Mockito.any(ServerSettingEntity.class));
        }

        @Test
        @DisplayName("Acceptable (Missing Active) - Expect Nothing")
        public void testUpdateServerActive_missingActive_expectNothing() {
            ServerEntity serverEntity = new ServerEntity();
            serverEntity.setId(UUID.randomUUID().toString());
            serverEntity.setName("test");

            Mockito.when(serverRepository.findById(Mockito.anyString())).thenReturn(Optional.of(serverEntity));
            Mockito.when(serverSettingRepository.save(Mockito.any(ServerSettingEntity.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

            serverService.updateServerActive(serverEntity.getId(), new Random().nextBoolean());

            Mockito.verify(serverService, Mockito.times(1)).updateServerActive(Mockito.anyString(), Mockito.anyBoolean());
            Mockito.verify(serverRepository, Mockito.times(1)).findById(Mockito.anyString());
            Mockito.verify(serverSettingRepository, Mockito.times(1)).save(Mockito.any(ServerSettingEntity.class));
        }

        @Test
        @DisplayName("Not Found - Expect Error")
        public void testUpdateServerActive_notFound_expectError() {
            ServerEntity serverEntity = new ServerEntity();
            serverEntity.setId(UUID.randomUUID().toString());
            serverEntity.setName("test");

            Mockito.when(serverRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());

            Assertions.assertThrows(OverseerNotFoundException.class, () -> serverService.updateServerActive(serverEntity.getId(), new Random().nextBoolean()));

            Mockito.verify(serverService, Mockito.times(1)).updateServerActive(Mockito.anyString(), Mockito.anyBoolean());
            Mockito.verify(serverRepository, Mockito.times(1)).findById(Mockito.anyString());
            Mockito.verify(serverSettingRepository, Mockito.times(0)).save(Mockito.any(ServerSettingEntity.class));
        }
    }

    @Nested
    @DisplayName("Delete Server")
    protected class testDeleteServer {
        @Test
        @DisplayName("Acceptable Inputs - Expect Nothing")
        public void testRemoveApiKey_acceptableInputs_expectNothing() {
            Mockito.doNothing().when(serverRepository).deleteById(Mockito.anyString());

            serverService.removeServer(UUID.randomUUID().toString());

            Mockito.verify(serverService, Mockito.times(1)).removeServer(Mockito.anyString());
            Mockito.verify(serverRepository, Mockito.times(1)).deleteById(Mockito.anyString());
        }

        @Test
        @DisplayName("Not Found - Expect Error")
        public void testRemoveServer_notFound_expectError() {
            Mockito.doThrow(EmptyResultDataAccessException.class).when(serverRepository).deleteById(Mockito.anyString());

            Assertions.assertThrows(OverseerNotFoundException.class, () -> serverService.removeServer(UUID.randomUUID().toString()));

            Mockito.verify(serverService, Mockito.times(1)).removeServer(Mockito.anyString());
            Mockito.verify(serverRepository, Mockito.times(1)).deleteById(Mockito.anyString());
        }
    }

    @Nested
    @DisplayName("Protected - Configure New Server")
    protected class testConfigureNewServer {
        MockedStatic<ValidationUtils> mockedValidationUtils;

        @BeforeEach
        public void preTestSetup() {
            mockedValidationUtils = Mockito.mockStatic(ValidationUtils.class);

            mockedValidationUtils.when(() -> ValidationUtils.convertStringToBoolean(Mockito.any())).thenCallRealMethod();
        }

        @AfterEach
        public void posTestTeardown() {
            mockedValidationUtils.close();
            mockedValidationUtils = null;
        }

        @Test
        @DisplayName("Acceptable Inputs - Expect Entity")
        public void testConfigureNewServer_acceptableInputs_expectEntity() {
            ServerSettingEntity settingEntity = new ServerSettingEntity();
            settingEntity.setId(UUID.randomUUID().toString());
            settingEntity.setType(SettingType.BOOLEAN.name());
            settingEntity.setName(Constants.ACTIVE_SETTING);
            settingEntity.setVal(Boolean.FALSE.toString());

            ServerEntity serverEntity = new ServerEntity();
            serverEntity.setId(UUID.randomUUID().toString());
            serverEntity.setName("test");
            serverEntity.setSettings(Set.of(settingEntity));

            ServerEntity results = serverService.configureNewServer(serverEntity);

            Assertions.assertTrue(results.getSettings().stream().filter(setting -> setting.getName().equalsIgnoreCase(Constants.ACTIVE_SETTING)).findFirst().isPresent());

            Mockito.verify(serverService, Mockito.times(1)).configureNewServer(Mockito.any(ServerEntity.class));
        }

        @Test
        @DisplayName("Acceptable (Missing Active) - Expect Entity")
        public void testConfigureNewServer_missingActive_expectEntity() {
            ServerEntity serverEntity = new ServerEntity();
            serverEntity.setId(UUID.randomUUID().toString());
            serverEntity.setName("test");

            ServerEntity results = serverService.configureNewServer(serverEntity);

            Assertions.assertTrue(results.getSettings().stream().filter(setting -> setting.getName().equalsIgnoreCase(Constants.ACTIVE_SETTING)).findFirst().isPresent());

            Mockito.verify(serverService, Mockito.times(1)).configureNewServer(Mockito.any(ServerEntity.class));
        }
    }

    @Nested
    @DisplayName("Protected - Update Settings")
    protected class testUpdateSettings {
        @Test
        @DisplayName("Acceptable Inputs - Expect Set")
        public void testUpdateSettings_acceptableInputs_expectSet() {
            ServerSettingEntity settingEntity = new ServerSettingEntity();
            settingEntity.setId(UUID.randomUUID().toString());
            settingEntity.setType(SettingType.BOOLEAN.name());
            settingEntity.setName(Constants.ACTIVE_SETTING);
            settingEntity.setVal(Boolean.FALSE.toString());

            ServerSettingEntity testSettingEntity = new ServerSettingEntity();
            testSettingEntity.setId(UUID.randomUUID().toString());
            testSettingEntity.setType(SettingType.BOOLEAN.name());
            testSettingEntity.setName(Constants.ACTIVE_SETTING);
            testSettingEntity.setVal(Boolean.TRUE.toString());

            ServerEntity serverEntity = new ServerEntity();
            serverEntity.setId(UUID.randomUUID().toString());
            serverEntity.setName("test");
            serverEntity.setSettings(Set.of(settingEntity));

            Mockito.when(serverSettingRepository.saveAll(Mockito.any())).thenAnswer(invocation -> new ArrayList<>(invocation.getArgument(0)));

            Set<ServerSettingEntity> results = serverService.updateSettings(serverEntity, Set.of(testSettingEntity));

            Assertions.assertFalse(CollectionUtils.isEmpty(results));
            Assertions.assertEquals(1, results.size());
            Assertions.assertTrue(results.stream().filter(setting -> setting.getName().equalsIgnoreCase(Constants.ACTIVE_SETTING)).findFirst().isPresent());
            Assertions.assertEquals(testSettingEntity.getVal(), results.stream().filter(setting -> setting.getName().equalsIgnoreCase(Constants.ACTIVE_SETTING)).findFirst().get().getVal());

            Mockito.verify(serverService, Mockito.times(1)).updateSettings(Mockito.any(ServerEntity.class), Mockito.anySet());
            Mockito.verify(serverSettingRepository, Mockito.times(1)).saveAll(Mockito.any());
        }

        @Test
        @DisplayName("Acceptable (No Matching) - Expect Set")
        public void testUpdateSettings_noMatching_expectSet() {
            ServerSettingEntity settingEntity = new ServerSettingEntity();
            settingEntity.setId(UUID.randomUUID().toString());
            settingEntity.setType(SettingType.BOOLEAN.name());
            settingEntity.setName(Constants.ACTIVE_SETTING);
            settingEntity.setVal(Boolean.FALSE.toString());

            ServerSettingEntity testSettingEntity = new ServerSettingEntity();
            testSettingEntity.setId(UUID.randomUUID().toString());
            testSettingEntity.setType(SettingType.BOOLEAN.name());
            testSettingEntity.setName("test");
            testSettingEntity.setVal(Boolean.TRUE.toString());

            ServerEntity serverEntity = new ServerEntity();
            serverEntity.setId(UUID.randomUUID().toString());
            serverEntity.setName("test");
            serverEntity.setSettings(Set.of(settingEntity));

            Mockito.when(serverSettingRepository.saveAll(Mockito.any())).thenAnswer(invocation -> new ArrayList<>(invocation.getArgument(0)));

            Set<ServerSettingEntity> results = serverService.updateSettings(serverEntity, Set.of(testSettingEntity));

            Assertions.assertFalse(CollectionUtils.isEmpty(results));
            Assertions.assertEquals(2, results.size());
            Assertions.assertTrue(results.stream().filter(setting -> setting.getName().equalsIgnoreCase(Constants.ACTIVE_SETTING)).findFirst().isPresent());
            Assertions.assertEquals(Boolean.FALSE.toString(), results.stream().filter(setting -> setting.getName().equalsIgnoreCase(Constants.ACTIVE_SETTING)).findFirst().get().getVal());

            Mockito.verify(serverService, Mockito.times(1)).updateSettings(Mockito.any(ServerEntity.class), Mockito.anySet());
            Mockito.verify(serverSettingRepository, Mockito.times(1)).saveAll(Mockito.any());
        }

        @Test
        @DisplayName("Acceptable (No Updates) - Expect Set")
        public void testUpdateSettings_noUpdates_expectSet() {
            ServerSettingEntity settingEntity = new ServerSettingEntity();
            settingEntity.setId(UUID.randomUUID().toString());
            settingEntity.setType(SettingType.BOOLEAN.name());
            settingEntity.setName(Constants.ACTIVE_SETTING);
            settingEntity.setVal(Boolean.FALSE.toString());

            ServerEntity serverEntity = new ServerEntity();
            serverEntity.setId(UUID.randomUUID().toString());
            serverEntity.setName("test");
            serverEntity.setSettings(Set.of(settingEntity));

            Mockito.when(serverSettingRepository.saveAll(Mockito.any())).thenAnswer(invocation -> new ArrayList<>(invocation.getArgument(0)));

            Set<ServerSettingEntity> results = serverService.updateSettings(serverEntity, Collections.EMPTY_SET);

            Assertions.assertFalse(CollectionUtils.isEmpty(results));
            Assertions.assertEquals(1, results.size());
            Assertions.assertTrue(results.stream().filter(setting -> setting.getName().equalsIgnoreCase(Constants.ACTIVE_SETTING)).findFirst().isPresent());
            Assertions.assertEquals(Boolean.FALSE.toString(), results.stream().filter(setting -> setting.getName().equalsIgnoreCase(Constants.ACTIVE_SETTING)).findFirst().get().getVal());

            Mockito.verify(serverService, Mockito.times(1)).updateSettings(Mockito.any(ServerEntity.class), Mockito.anySet());
            Mockito.verify(serverSettingRepository, Mockito.times(1)).saveAll(Mockito.any());
        }

        @Test
        @DisplayName("Conflict - Expect Error")
        public void testUpdateSettings_conflict_expectError() {
            ServerSettingEntity settingEntity = new ServerSettingEntity();
            settingEntity.setId(UUID.randomUUID().toString());
            settingEntity.setType(SettingType.BOOLEAN.name());
            settingEntity.setName(Constants.ACTIVE_SETTING);
            settingEntity.setVal(Boolean.FALSE.toString());

            ServerEntity serverEntity = new ServerEntity();
            serverEntity.setId(UUID.randomUUID().toString());
            serverEntity.setName("test");
            serverEntity.setSettings(Set.of(settingEntity));

            Mockito.doThrow(new PersistenceException()).when(serverSettingRepository).saveAll(Mockito.any());

            Assertions.assertThrows(OverseerConflictException.class, () -> serverService.updateSettings(serverEntity, Collections.EMPTY_SET));

            Mockito.verify(serverService, Mockito.times(1)).updateSettings(Mockito.any(ServerEntity.class), Mockito.any());
            Mockito.verify(serverSettingRepository, Mockito.times(1)).saveAll(Mockito.any());
        }
    }
}
