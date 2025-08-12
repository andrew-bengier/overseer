package com.bnfd.overseer.service;

import com.bnfd.overseer.exception.OverseerConflictException;
import com.bnfd.overseer.exception.OverseerNoContentException;
import com.bnfd.overseer.exception.OverseerNotFoundException;
import com.bnfd.overseer.model.api.ApiKey;
import com.bnfd.overseer.model.constants.ApiKeyType;
import com.bnfd.overseer.model.mapper.OverseerMapper;
import com.bnfd.overseer.model.persistence.ApiKeyEntity;
import com.bnfd.overseer.repository.ApiKeyRepository;
import jakarta.persistence.PersistenceException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.MDC;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;


@Slf4j
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class ApiKeyServiceTests {
    // region - Class Variables -
    @Mock
    private ApiKeyRepository apiKeyRepository;

    @Spy
    private final ModelMapper overseerMapper = new OverseerMapper().mapper();

    private ApiKey testApiKey;

    private ApiKeyEntity testApiKeyEntity;

    @Spy
    @InjectMocks
    private ApiKeyService apiKeyService;
    // endregion - Class Variables -

    // region - Setup | Teardown -
    @BeforeAll
    public static void setup() {
        MDC.put("userId", String.format("User: %s", "ApiKeyServiceTests"));
    }

    @BeforeEach
    public void init() {
        ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];

        testApiKey = new ApiKey();
        testApiKey.setType(type);
        testApiKey.setName("test");
        testApiKey.setKey("test");

        testApiKeyEntity = new ApiKeyEntity();
        testApiKeyEntity.setType(type);
        testApiKeyEntity.setName("test");
        testApiKeyEntity.setKey("test");
    }

    @AfterEach
    public void tearDown() {
        testApiKey = null;
        testApiKeyEntity = null;
    }
    // endregion - Setup | Teardown -

    @Nested
    @DisplayName("Create ApiKey")
    protected class testAddApiKey {
        @Test
        @DisplayName("Acceptable Inputs - Expect Model")
        public void testAddApiKey_acceptableInputs_expectModel() {
            Mockito.when(apiKeyRepository.save(Mockito.any(ApiKeyEntity.class)))
                    .thenAnswer(
                            invocation -> invocation.getArgument(0)
                    );

            ApiKey results = apiKeyService.addApiKey(testApiKey);

            Assertions.assertNotNull(results.getId());
            Assertions.assertNotEquals(testApiKey.getId(), results.getId());
            Assertions.assertEquals(testApiKey.getName(), results.getName());
            Assertions.assertEquals(testApiKey.getKey(), results.getKey());

            Mockito.verify(apiKeyService, Mockito.times(1)).addApiKey(Mockito.any(ApiKey.class));
            Mockito.verify(apiKeyRepository, Mockito.times(1)).save(Mockito.any(ApiKeyEntity.class));
            Mockito.verify(overseerMapper, Mockito.times(1)).map(Mockito.any(ApiKey.class), Mockito.eq(ApiKeyEntity.class));
            Mockito.verify(overseerMapper, Mockito.times(1)).map(Mockito.any(ApiKeyEntity.class), Mockito.eq(ApiKey.class));
        }

        @Test
        @DisplayName("Invalid Duplicate - Expect Error")
        public void testAddApiKey_invalidDuplicate_expectError() {
            Mockito.doThrow(PersistenceException.class).when(apiKeyRepository).save(Mockito.any(ApiKeyEntity.class));

            Assertions.assertThrows(OverseerConflictException.class, () -> apiKeyService.addApiKey(testApiKey));

            Mockito.verify(apiKeyService, Mockito.times(1)).addApiKey(Mockito.any(ApiKey.class));
            Mockito.verify(apiKeyRepository, Mockito.times(1)).save(Mockito.any(ApiKeyEntity.class));
            Mockito.verify(overseerMapper, Mockito.times(1)).map(Mockito.any(ApiKey.class), Mockito.eq(ApiKeyEntity.class));
            Mockito.verify(overseerMapper, Mockito.times(0)).map(Mockito.any(ApiKeyEntity.class), Mockito.eq(ApiKey.class));
        }
    }

    @Nested
    @DisplayName("Read ApiKey")
    protected class testGetApiKey {
        @Nested
        @DisplayName("GetApiKeyById")
        protected class testGetApiKeyById {
            @Test
            @DisplayName("Acceptable Inputs - Expect Model")
            public void testGetApiKeyById_acceptableInputs_expectModel() {
                testApiKeyEntity.setId(UUID.randomUUID().toString());

                Mockito.when(apiKeyRepository.findById(Mockito.anyString())).thenReturn(Optional.of(testApiKeyEntity));

                ApiKey results = apiKeyService.getApiKeyById(testApiKeyEntity.getId());

                Assertions.assertEquals(testApiKeyEntity.getId(), results.getId());
                Assertions.assertEquals(testApiKeyEntity.getName(), results.getName());
                Assertions.assertEquals(testApiKeyEntity.getKey(), results.getKey());

                Mockito.verify(apiKeyService, Mockito.times(1)).getApiKeyById(Mockito.anyString());
                Mockito.verify(apiKeyRepository, Mockito.times(1)).findById(Mockito.anyString());
                Mockito.verify(overseerMapper, Mockito.times(1)).map(Mockito.any(ApiKeyEntity.class), Mockito.eq(ApiKey.class));
            }

            @Test
            @DisplayName("Not Found - Expect Error")
            public void testGetApiKeyById_notFound_expectError() {
                Mockito.when(apiKeyRepository.findById(Mockito.anyString())).thenReturn(null);

                Assertions.assertThrows(RuntimeException.class, () -> apiKeyService.getApiKeyById(UUID.randomUUID().toString()));

                Mockito.verify(apiKeyService, Mockito.times(1)).getApiKeyById(Mockito.anyString());
                Mockito.verify(apiKeyRepository, Mockito.times(1)).findById(Mockito.anyString());
                Mockito.verifyNoInteractions(overseerMapper);
            }
        }

        @Nested
        @DisplayName("GetApiKeyByName")
        protected class testGetApiKeyByName {
            @Test
            @DisplayName("Acceptable Inputs - Expect List")
            public void testGetApiKeyByName_acceptableInputs_expectModel() {
                testApiKeyEntity.setId(UUID.randomUUID().toString());
                List<ApiKeyEntity> testEntities = List.of(testApiKeyEntity);

                Mockito.when(apiKeyRepository.findAllByType(Mockito.any(ApiKeyType.class))).thenReturn(testEntities);

                List<ApiKey> results = apiKeyService.getAllApiKeysByType(testApiKeyEntity.getType());

                Assertions.assertFalse(results.isEmpty());

                Mockito.verify(apiKeyService, Mockito.times(1)).getAllApiKeysByType(Mockito.any(ApiKeyType.class));
                Mockito.verify(apiKeyRepository, Mockito.times(1)).findAllByType(Mockito.any(ApiKeyType.class));
                Mockito.verify(overseerMapper, Mockito.times(1)).map(testEntities, new TypeToken<List<ApiKey>>() {
                }.getType());
            }

            @Test
            @DisplayName("None Found - Expect Error")
            public void testGetApiKeyByName_noneFound_expectError() {
                Mockito.when(apiKeyRepository.findAllByType(Mockito.any())).thenReturn(null);

                Assertions.assertThrows(OverseerNoContentException.class, () -> apiKeyService.getAllApiKeysByType(null));

                Mockito.verify(apiKeyService, Mockito.times(1)).getAllApiKeysByType(Mockito.any());
                Mockito.verify(apiKeyRepository, Mockito.times(1)).findAllByType(Mockito.any());
                Mockito.verifyNoInteractions(overseerMapper);
            }
        }

        @Nested
        @DisplayName("GetAllApiKeys")
        protected class testGetAllApiKeys {
            @Test
            @DisplayName("Acceptable Inputs - Expect List")
            public void testGetAllApiKey_acceptableInputs_expectModel() {
                testApiKeyEntity.setId(UUID.randomUUID().toString());
                List<ApiKeyEntity> testEntities = List.of(testApiKeyEntity);

                Mockito.when(apiKeyRepository.findAll()).thenReturn(testEntities);

                List<ApiKey> results = apiKeyService.getAllApiKeys();

                Assertions.assertFalse(results.isEmpty());

                Mockito.verify(apiKeyService, Mockito.times(1)).getAllApiKeys();
                Mockito.verify(apiKeyRepository, Mockito.times(1)).findAll();
                Mockito.verify(overseerMapper, Mockito.times(1)).map(testEntities, new TypeToken<List<ApiKey>>() {
                }.getType());
            }

            @Test
            @DisplayName("None Found - Expect Error")
            public void testGetAllApiKey_noneFound_expectError() {
                Mockito.when(apiKeyRepository.findAll()).thenReturn(null);

                Assertions.assertThrows(OverseerNoContentException.class, () -> apiKeyService.getAllApiKeys());

                Mockito.verify(apiKeyService, Mockito.times(1)).getAllApiKeys();
                Mockito.verify(apiKeyRepository, Mockito.times(1)).findAll();
                Mockito.verifyNoInteractions(overseerMapper);
            }
        }
    }

    @Nested
    @DisplayName("Update ApiKey")
    protected class testUpdateApiKey {
        @Test
        @DisplayName("Acceptable Inputs - Expect Model")
        public void testUpdateApiKey_acceptableInputs_expectModel() {
            testApiKey.setId(UUID.randomUUID().toString());
            testApiKeyEntity.setId(UUID.randomUUID().toString());

            Mockito.when(apiKeyRepository.findById(Mockito.anyString())).thenReturn(Optional.ofNullable(testApiKeyEntity));
            Mockito.when(apiKeyRepository.save(Mockito.any(ApiKeyEntity.class)))
                    .thenAnswer(
                            invocation -> invocation.getArgument(0)
                    );

            ApiKey results = apiKeyService.updateApiKey(testApiKey);

            Mockito.verify(apiKeyService, Mockito.times(1)).updateApiKey(Mockito.any(ApiKey.class));
            Mockito.verify(apiKeyRepository, Mockito.times(1)).findById(Mockito.anyString());
            Mockito.verify(apiKeyRepository, Mockito.times(1)).save(Mockito.any());
            Mockito.verify(overseerMapper, Mockito.times(1)).map(Mockito.any(ApiKey.class), Mockito.eq(ApiKeyEntity.class));
            Mockito.verify(overseerMapper, Mockito.times(1)).map(Mockito.any(ApiKeyEntity.class), Mockito.eq(ApiKey.class));
        }

        @Test
        @DisplayName("Not Found - Expect Error")
        public void testUpdateApiKey_notFound_expectError() {
            testApiKey.setId(UUID.randomUUID().toString());
            Mockito.when(apiKeyRepository.findById(Mockito.anyString())).thenReturn(Optional.empty());

            Assertions.assertThrows(OverseerNotFoundException.class, () -> apiKeyService.updateApiKey(testApiKey));

            Mockito.verify(apiKeyService, Mockito.times(1)).updateApiKey(Mockito.any(ApiKey.class));
            Mockito.verify(apiKeyRepository, Mockito.times(1)).findById(Mockito.anyString());
            Mockito.verify(apiKeyRepository, Mockito.times(0)).save(Mockito.any());
            Mockito.verifyNoInteractions(overseerMapper);
        }

        @Test
        @DisplayName("Conflict - Expect Error")
        public void testUpdateApiKey_conflict_expectError() {
            testApiKey.setId(UUID.randomUUID().toString());
            testApiKeyEntity.setId(UUID.randomUUID().toString());

            Mockito.when(apiKeyRepository.findById(Mockito.anyString())).thenReturn(Optional.ofNullable(testApiKeyEntity));
            Mockito.doThrow(PersistenceException.class).when(apiKeyRepository).save(Mockito.any(ApiKeyEntity.class));

            Assertions.assertThrows(OverseerConflictException.class, () -> apiKeyService.updateApiKey(testApiKey));

            Mockito.verify(apiKeyService, Mockito.times(1)).updateApiKey(Mockito.any(ApiKey.class));
            Mockito.verify(apiKeyRepository, Mockito.times(1)).findById(Mockito.anyString());
            Mockito.verify(apiKeyRepository, Mockito.times(1)).save(Mockito.any());
            Mockito.verify(overseerMapper, Mockito.times(1)).map(Mockito.any(ApiKey.class), Mockito.eq(ApiKeyEntity.class));
            Mockito.verify(overseerMapper, Mockito.times(0)).map(Mockito.any(ApiKeyEntity.class), Mockito.eq(ApiKey.class));
        }
    }

    @Nested
    @DisplayName("Delete ApiKey")
    protected class testDeleteApiKey {
        @Test
        @DisplayName("Acceptable Inputs - Expect Nothing")
        public void testRemoveApiKey_acceptableInputs_expectNothing() {
            Mockito.doNothing().when(apiKeyRepository).deleteById(Mockito.anyString());

            apiKeyService.removeApiKey(UUID.randomUUID().toString());

            Mockito.verify(apiKeyService, Mockito.times(1)).removeApiKey(Mockito.anyString());
            Mockito.verify(apiKeyRepository, Mockito.times(1)).deleteById(Mockito.anyString());
        }

        @Test
        @DisplayName("Not Found - Expect Error")
        public void testRemoveApiKey_notFound_expectError() {
            Mockito.doThrow(EmptyResultDataAccessException.class).when(apiKeyRepository).deleteById(Mockito.anyString());

            Assertions.assertThrows(RuntimeException.class, () -> apiKeyService.removeApiKey(UUID.randomUUID().toString()));

            Mockito.verify(apiKeyService, Mockito.times(1)).removeApiKey(Mockito.anyString());
            Mockito.verify(apiKeyRepository, Mockito.times(1)).deleteById(Mockito.anyString());
        }
    }
}
