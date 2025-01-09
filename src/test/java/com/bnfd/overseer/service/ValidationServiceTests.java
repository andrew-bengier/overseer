package com.bnfd.overseer.service;

import com.bnfd.overseer.exception.*;
import com.bnfd.overseer.model.api.*;
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
            testKey.setId(new Random().nextInt());
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
            testKey.setId(new Random().nextInt());
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

            Assertions.assertThrows(OverseerBadRequestException.class, () -> validationService.validateApiKey(testKey, new Random().nextInt(), false));

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
}
