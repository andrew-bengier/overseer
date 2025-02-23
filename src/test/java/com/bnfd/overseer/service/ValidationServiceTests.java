package com.bnfd.overseer.service;

import com.bnfd.overseer.exception.*;
import com.bnfd.overseer.model.api.*;
import com.bnfd.overseer.model.constants.*;
import com.bnfd.overseer.utils.*;
import lombok.extern.slf4j.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.mockito.*;
import org.mockito.junit.jupiter.*;
import org.springframework.test.context.*;

import java.util.*;

@Slf4j
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ValidationServiceTests {
    // region - Class Variables -
    @Spy
    @InjectMocks
    private ValidationService validationService;
    // endregion - Class Variables -

    @Nested
    @DisplayName("Validate ApiKey")
    protected class testValidateApiKey {
        @Test
        @DisplayName("Acceptable Inputs - Expect Nothing")
        public void testValidateApiKey_acceptableInputs_expectNothing() throws Throwable {
            ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
            ApiKey testKey = new ApiKey();
            testKey.setId(UUID.randomUUID().toString());
            testKey.setName(type.name());
            testKey.setKey("test");

            validationService.validateApiKey(testKey, testKey.getId(), false);

            Mockito.verify(validationService, Mockito.times(1)).validateApiKey(Mockito.any(ApiKey.class), Mockito.any(), Mockito.anyBoolean());
        }

        @Test
        @DisplayName("Invalid Id Create - Expect Error")
        public void testValidateApiKey_invalidIdCreate_expectError() throws Throwable {
            ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
            ApiKey testKey = new ApiKey();
            testKey.setId(UUID.randomUUID().toString());
            testKey.setName(type.name());
            testKey.setKey("test");

            Assertions.assertThrows(OverseerUnprocessableException.class, () -> validationService.validateApiKey(testKey, null, true));

            Mockito.verify(validationService, Mockito.times(1)).validateApiKey(Mockito.any(ApiKey.class), Mockito.any(), Mockito.anyBoolean());
        }

        @Test
        @DisplayName("Missing Id Update - Expect Error")
        public void testValidateApiKey_invalidIdUpdate_expectError() throws Throwable {
            ApiKey testKey = new ApiKey();
            testKey.setName("invalid");
            testKey.setKey("test");

            Assertions.assertThrows(OverseerBadRequestException.class, () -> validationService.validateApiKey(testKey, UUID.randomUUID().toString(), false));

            Mockito.verify(validationService, Mockito.times(1)).validateApiKey(Mockito.any(ApiKey.class), Mockito.any(), Mockito.anyBoolean());
        }

        @Test
        @DisplayName("Invalid Type - Expect Error")
        public void testValidateApiKey_invalidType_expectError() throws Throwable {
            ApiKey testKey = new ApiKey();
            testKey.setName("invalid");
            testKey.setKey("test");

            Assertions.assertThrows(OverseerUnprocessableException.class, () -> validationService.validateApiKey(testKey, null, true));

            Mockito.verify(validationService, Mockito.times(1)).validateApiKey(Mockito.any(ApiKey.class), Mockito.any(), Mockito.anyBoolean());
        }

        @Test
        @DisplayName("Missing Type - Expect Error")
        public void testValidateApiKey_missingType_expectError() throws Throwable {
            ApiKey testKey = new ApiKey();
            testKey.setKey("test");

            Assertions.assertThrows(OverseerBadRequestException.class, () -> validationService.validateApiKey(testKey, null, true));

            Mockito.verify(validationService, Mockito.times(1)).validateApiKey(Mockito.any(ApiKey.class), Mockito.any(), Mockito.anyBoolean());
        }

        @Test
        @DisplayName("Missing Key - Expect Error")
        public void testValidateApiKey_missingKey_expectError() throws Throwable {
            ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
            ApiKey testKey = new ApiKey();
            testKey.setName(type.name());

            Assertions.assertThrows(OverseerBadRequestException.class, () -> validationService.validateApiKey(testKey, null, true));

            Mockito.verify(validationService, Mockito.times(1)).validateApiKey(Mockito.any(ApiKey.class), Mockito.any(), Mockito.anyBoolean());
        }
    }

    @Nested
    @DisplayName("Validate Server")
    protected class testValidateServer {
        @Test
        @DisplayName("Acceptable Inputs - Expect Nothing")
        public void testValidateServer_acceptableInputs_expectNothing() throws Throwable {
            String serverId = UUID.randomUUID().toString();
            ApiKey testApiKey = new ApiKey(UUID.randomUUID().toString(), "test", "test", null);
            Setting testActiveSetting = new Setting();
            testActiveSetting.setId(UUID.randomUUID().toString());
            testActiveSetting.setType(SettingType.BOOLEAN);
            testActiveSetting.setName(Constants.ACTIVE_SETTING);
            testActiveSetting.setVal(Boolean.TRUE.toString());
            Server testServer = new Server();
            testServer.setId(serverId);
            testServer.setName("test");
            testServer.setApiKey(testApiKey);
            testServer.setSettings(Set.of(testActiveSetting));

            validationService.validateServer(testServer, serverId, false);

            Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
        }

        @Test
        @DisplayName("Invalid Id Create - Expect Error")
        public void testValidateServer_invalidIdCreate_expectError() throws Throwable {
            ApiKey testApiKey = new ApiKey(UUID.randomUUID().toString(), "test", "test", null);
            Server testServer = new Server();
            testServer.setId(UUID.randomUUID().toString());
            testServer.setName("test");
            testServer.setApiKey(testApiKey);

            Assertions.assertThrows(OverseerUnprocessableException.class, () -> validationService.validateServer(testServer, null, true));

            Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
        }

        @Test
        @DisplayName("Missing Id Update - Expect Error")
        public void testValidateServer_invalidIdUpdate_expectError() throws Throwable {
            ApiKey testApiKey = new ApiKey(UUID.randomUUID().toString(), "test", "test", null);
            Server testServer = new Server();
            testServer.setName("test");
            testServer.setApiKey(testApiKey);

            Assertions.assertThrows(OverseerBadRequestException.class, () -> validationService.validateServer(testServer, UUID.randomUUID().toString(), false));

            Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
        }

        @Test
        @DisplayName("Missing Name - Expect Error")
        public void testValidateServer_missingName_expectError() throws Throwable {
            ApiKey testApiKey = new ApiKey(UUID.randomUUID().toString(), "test", "test", null);
            Server testServer = new Server();
            testServer.setApiKey(testApiKey);

            Assertions.assertThrows(OverseerBadRequestException.class, () -> validationService.validateServer(testServer, null, true));

            Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
        }

        @Test
        @DisplayName("Missing ApiKey - Expect Error")
        public void testValidateServer_missingKey_expectError() throws Throwable {
            Server testServer = new Server();
            testServer.setName("test");

            Assertions.assertThrows(OverseerBadRequestException.class, () -> validationService.validateServer(testServer, null, true));

            Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
        }

        @Test
        @DisplayName("Missing Setting - Active - Expect Error")
        public void testValidateServer_missingSetting_active_expectError() throws Throwable {
            String serverId = UUID.randomUUID().toString();
            ApiKey testApiKey = new ApiKey(UUID.randomUUID().toString(), "test", "test", null);
            Setting testActiveSetting = new Setting();
            testActiveSetting.setId(UUID.randomUUID().toString());
            testActiveSetting.setType(SettingType.BOOLEAN);
            testActiveSetting.setName("test");
            testActiveSetting.setVal("false");
            Server testServer = new Server();
            testServer.setId(serverId);
            testServer.setName("test");
            testServer.setApiKey(testApiKey);
            testServer.setSettings(Set.of(testActiveSetting));

            Assertions.assertThrows(OverseerBadRequestException.class, () -> validationService.validateServer(testServer, serverId, false));

            Mockito.verify(validationService, Mockito.times(1)).validateServer(Mockito.any(Server.class), Mockito.any(), Mockito.anyBoolean());
        }
    }
}
