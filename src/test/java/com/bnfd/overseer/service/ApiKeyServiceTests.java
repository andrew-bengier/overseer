package com.bnfd.overseer.service;

import com.bnfd.overseer.exception.*;
import com.bnfd.overseer.model.api.*;
import com.bnfd.overseer.model.mapper.*;
import com.bnfd.overseer.model.persistence.*;
import com.bnfd.overseer.repository.*;
import jakarta.persistence.*;
import lombok.extern.slf4j.*;
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
public class ApiKeyServiceTests {
    // region - Class Variables -
    @Mock
    private ApiKeyRepository apiKeyRepository;

    @Spy
    private final ModelMapper overseerMapper = new OverseerMapper().mapper();

    @Spy
    @InjectMocks
    private ApiKeyService apiKeyService;
    // endregion - Class Variables -

    @Nested
    @DisplayName("Create ApiKey")
    protected class testAddApiKey {
        @Test
        @DisplayName("Acceptable Inputs - Expect Model")
        public void testAddApiKey_acceptableInputs_expectModel() {
            ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
            ApiKey testKey = new ApiKey();
            testKey.setName(type.name());
            testKey.setKey("test");

            Mockito.when(apiKeyRepository.save(Mockito.any(ApiKeyEntity.class)))
                    .thenAnswer(
                            invocation -> {
                                ApiKeyEntity dbResult = invocation.getArgument(0);
                                dbResult.setId(new Random().nextInt());
                                return dbResult;
                            }
                    );

            ApiKey results = apiKeyService.addApiKey(testKey);

            Assertions.assertNotNull(results.getId());
            Assertions.assertEquals(testKey.getName(), results.getName());
            Assertions.assertEquals(testKey.getKey(), results.getKey());
            Assertions.assertEquals(testKey.getUrl(), results.getUrl());

            Mockito.verify(apiKeyService, Mockito.times(1)).addApiKey(Mockito.any(ApiKey.class));
            Mockito.verify(apiKeyRepository, Mockito.times(1)).save(Mockito.any(ApiKeyEntity.class));
            Mockito.verify(overseerMapper, Mockito.times(1)).map(Mockito.any(ApiKey.class), Mockito.eq(ApiKeyEntity.class));
            Mockito.verify(overseerMapper, Mockito.times(1)).map(Mockito.any(ApiKeyEntity.class), Mockito.eq(ApiKey.class));
        }

        @Test
        @DisplayName("Invalid Duplicate - Expect Error")
        public void testAddApiKey_invalidDuplicate_expectError() {
            ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
            ApiKey testKey = new ApiKey();
            testKey.setName(type.name());
            testKey.setKey("test");

            Mockito.doThrow(new PersistenceException()).when(apiKeyRepository).save(Mockito.any(ApiKeyEntity.class));

            Assertions.assertThrows(OverseerConflictException.class, () -> apiKeyService.addApiKey(testKey));

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
                ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
                ApiKeyEntity testKey = new ApiKeyEntity();
                testKey.setId(new Random().nextInt());
                testKey.setName(type.name());
                testKey.setKey("test");

                Mockito.when(apiKeyRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(testKey));

                ApiKey results = apiKeyService.getApiKeyById(testKey.getId());

                Assertions.assertEquals(testKey.getId(), results.getId());
                Assertions.assertEquals(testKey.getName(), results.getName());
                Assertions.assertEquals(testKey.getKey(), results.getKey());
                Assertions.assertEquals(testKey.getUrl(), results.getUrl());

                Mockito.verify(apiKeyService, Mockito.times(1)).getApiKeyById(Mockito.anyInt());
                Mockito.verify(apiKeyRepository, Mockito.times(1)).findById(Mockito.anyInt());
                Mockito.verify(overseerMapper, Mockito.times(1)).map(Mockito.any(ApiKeyEntity.class), Mockito.eq(ApiKey.class));
            }

            @Test
            @DisplayName("Not Found - Expect Error")
            public void testGetApiKeyById_notFound_expectError() {
                Mockito.when(apiKeyRepository.findById(Mockito.anyInt())).thenReturn(null);

                Assertions.assertThrows(RuntimeException.class, () -> apiKeyService.getApiKeyById(new Random().nextInt()));

                Mockito.verify(apiKeyService, Mockito.times(1)).getApiKeyById(Mockito.anyInt());
                Mockito.verify(apiKeyRepository, Mockito.times(1)).findById(Mockito.anyInt());
                Mockito.verifyNoInteractions(overseerMapper);
            }
        }

        @Nested
        @DisplayName("GetAllApiKeysByName")
        protected class testGetAllApiKeysByName {
            @Test
            @DisplayName("Acceptable Inputs - Expect Model")
            public void testGetAllApiKeysByName_acceptableInputs_expectModel() {
                ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
                ApiKeyEntity testKey = new ApiKeyEntity();
                testKey.setId(new Random().nextInt());
                testKey.setName(type.name());
                testKey.setKey("test");

                Mockito.when(apiKeyRepository.findAllByName(Mockito.anyString())).thenReturn(List.of(testKey));

                List<ApiKey> results = apiKeyService.getAllApiKeysByName(testKey.getName());

                Assertions.assertFalse(CollectionUtils.isEmpty(results));

                Mockito.verify(apiKeyService, Mockito.times(1)).getAllApiKeysByName(Mockito.anyString());
                Mockito.verify(apiKeyRepository, Mockito.times(1)).findAllByName(Mockito.anyString());
                Mockito.verify(overseerMapper, Mockito.times(1)).map(Mockito.anyList(), Mockito.eq(new TypeToken<List<ApiKey>>() {
                }.getType()));
            }

            @Test
            @DisplayName("Not Found - Expect Error")
            public void testGetALlApiKeysByName_notFound_expectError() {
                Mockito.when(apiKeyRepository.findAllByName(Mockito.anyString())).thenReturn(null);

                Assertions.assertThrows(RuntimeException.class, () -> apiKeyService.getAllApiKeysByName("none"));

                Mockito.verify(apiKeyService, Mockito.times(1)).getAllApiKeysByName(Mockito.anyString());
                Mockito.verify(apiKeyRepository, Mockito.times(1)).findAllByName(Mockito.anyString());
                Mockito.verifyNoInteractions(overseerMapper);
            }
        }

        @Nested
        @DisplayName("GetAllApiKeys")
        protected class testGetAllApiKeys {
            @Test
            @DisplayName("Acceptable Inputs - Expect Model")
            public void testGetAllApiKeys_acceptableInputs_expectModel() {
                ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
                ApiKeyEntity testKey = new ApiKeyEntity();
                testKey.setId(new Random().nextInt());
                testKey.setName(type.name());
                testKey.setKey("test");

                Mockito.when(apiKeyRepository.findAll()).thenReturn(List.of(testKey));

                List<ApiKey> results = apiKeyService.getAllApiKeys();

                Assertions.assertFalse(CollectionUtils.isEmpty(results));

                Mockito.verify(apiKeyService, Mockito.times(1)).getAllApiKeys();
                Mockito.verify(apiKeyRepository, Mockito.times(1)).findAll();
                Mockito.verify(overseerMapper, Mockito.times(1)).map(Mockito.anyList(), Mockito.eq(new TypeToken<List<ApiKey>>() {
                }.getType()));
            }

            @Test
            @DisplayName("Not Found - Expect Error")
            public void testGetALlApiKeys_notFound_expectError() {
                Mockito.when(apiKeyRepository.findAll()).thenReturn(null);

                Assertions.assertThrows(RuntimeException.class, () -> apiKeyService.getAllApiKeys());

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
            Integer testId = new Random().nextInt();
            ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];

            ApiKeyEntity testEntity = new ApiKeyEntity();
            testEntity.setId(testId);
            testEntity.setName(type.name());
            testEntity.setKey("test");

            ApiKey testKey = new ApiKey();
            testKey.setId(testId);
            testKey.setName(type.name());
            testKey.setKey("updatedTest");

            Mockito.when(apiKeyRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(testEntity));
            Mockito.when(apiKeyRepository.save(Mockito.any(ApiKeyEntity.class))).thenAnswer(AdditionalAnswers.returnsFirstArg());

            ApiKey results = apiKeyService.updateApiKey(testKey);

            Assertions.assertEquals(testKey.getId(), results.getId());
            Assertions.assertEquals(testKey.getName(), results.getName());
            Assertions.assertEquals(testKey.getKey(), results.getKey());
            Assertions.assertEquals(testKey.getUrl(), results.getUrl());

            Mockito.verify(apiKeyService, Mockito.times(1)).updateApiKey(Mockito.any(ApiKey.class));
            Mockito.verify(apiKeyRepository, Mockito.times(1)).findById(Mockito.anyInt());
            Mockito.verify(overseerMapper, Mockito.times(1)).map(Mockito.any(ApiKey.class), Mockito.eq(ApiKeyEntity.class));
            Mockito.verify(apiKeyRepository, Mockito.times(1)).save(Mockito.any(ApiKeyEntity.class));
            Mockito.verify(overseerMapper, Mockito.times(1)).map(Mockito.any(ApiKeyEntity.class), Mockito.eq(ApiKey.class));
        }

        @Test
        @DisplayName("Not Found - Expect Error")
        public void testUpdateApiKey_notFound_expectModel() {
            ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
            ApiKey testKey = new ApiKey();
            testKey.setId(new Random().nextInt());
            testKey.setName(type.name());
            testKey.setKey("test");

            Mockito.when(apiKeyRepository.findById(Mockito.anyInt())).thenReturn(null);

            Assertions.assertThrows(RuntimeException.class, () -> apiKeyService.updateApiKey(testKey));

            Mockito.verify(apiKeyService, Mockito.times(1)).updateApiKey(Mockito.any(ApiKey.class));
            Mockito.verify(apiKeyRepository, Mockito.times(1)).findById(Mockito.anyInt());
            Mockito.verify(apiKeyRepository, Mockito.times(0)).save(Mockito.any(ApiKeyEntity.class));
            Mockito.verifyNoInteractions(overseerMapper);
        }

        @Test
        @DisplayName("Invalid Duplicate - Expect Error")
        public void testAddApiKey_invalidDuplicate_expectError() {
            Integer testId = new Random().nextInt();
            ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];

            ApiKeyEntity testEntity = new ApiKeyEntity();
            testEntity.setId(testId);
            testEntity.setName(type.name());
            testEntity.setKey("test");

            ApiKey testKey = new ApiKey();
            testKey.setId(testId);
            testKey.setName(type.name());
            testKey.setKey("updatedTest");

            Mockito.when(apiKeyRepository.findById(Mockito.anyInt())).thenReturn(Optional.of(testEntity));

            Mockito.doThrow(new PersistenceException()).when(apiKeyRepository).save(Mockito.any(ApiKeyEntity.class));

            Assertions.assertThrows(OverseerConflictException.class, () -> apiKeyService.updateApiKey(testKey));

            Mockito.verify(apiKeyService, Mockito.times(1)).updateApiKey(Mockito.any(ApiKey.class));
            Mockito.verify(apiKeyRepository, Mockito.times(1)).findById(Mockito.anyInt());
            Mockito.verify(overseerMapper, Mockito.times(1)).map(Mockito.any(ApiKey.class), Mockito.eq(ApiKeyEntity.class));
            Mockito.verify(apiKeyRepository, Mockito.times(1)).save(Mockito.any(ApiKeyEntity.class));
            Mockito.verify(overseerMapper, Mockito.times(0)).map(Mockito.any(ApiKeyEntity.class), Mockito.eq(ApiKey.class));
        }
    }

    @Nested
    @DisplayName("Delete ApiKey")
    protected class testDeleteApiKey {
        @Test
        @DisplayName("Acceptable Inputs - Expect Nothing")
        public void testRemoveApiKey_acceptableInputs_expectNothing() {
            ApiKeyType type = ApiKeyType.values()[new Random().nextInt(ApiKeyType.values().length)];
            ApiKeyEntity testKey = new ApiKeyEntity();
            testKey.setId(new Random().nextInt());
            testKey.setName(type.name());
            testKey.setKey("test");

            Mockito.doNothing().when(apiKeyRepository).deleteById(Mockito.anyInt());

            apiKeyService.removeApiKey(testKey.getId());

            Mockito.verify(apiKeyService, Mockito.times(1)).removeApiKey(Mockito.anyInt());
            Mockito.verify(apiKeyRepository, Mockito.times(1)).deleteById(Mockito.anyInt());
        }

        @Test
        @DisplayName("Not Found - Expect Error")
        public void testRemoveApiKey_notFound_expectError() {
            Mockito.doThrow(EmptyResultDataAccessException.class).when(apiKeyRepository).deleteById(Mockito.anyInt());

            Assertions.assertThrows(RuntimeException.class, () -> apiKeyService.removeApiKey(new Random().nextInt()));

            Mockito.verify(apiKeyService, Mockito.times(1)).removeApiKey(Mockito.anyInt());
            Mockito.verify(apiKeyRepository, Mockito.times(1)).deleteById(Mockito.anyInt());
        }
    }
}
