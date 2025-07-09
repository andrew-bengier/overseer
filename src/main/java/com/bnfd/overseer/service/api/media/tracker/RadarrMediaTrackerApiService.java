//package com.bnfd.overseer.service.api.media.tracker;
//
//import com.bnfd.overseer.exception.OverseerPreConditionRequiredException;
//import com.bnfd.overseer.model.api.ApiKey;
//import com.bnfd.overseer.model.constants.ApiKeyType;
//import com.bnfd.overseer.service.ApiKeyService;
//import jakarta.persistence.PersistenceException;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.stereotype.Service;
//
//import java.util.NoSuchElementException;
//
//@Service
//public class RadarrMediaTrackerApiService implements MediaTrackerApiService {
//    // region - Class Variables -
//    private final ModelMapper overseerMapper;
//    private final ApiKey apiKey;
//    // endregion - Class Variables -
//
//    // region - Constructors -
//    @Autowired
//    public RadarrMediaTrackerApiService(@Qualifier("overseer-mapper") ModelMapper overseerMapper, ApiKeyService apiKeyService) {
//        try {
//            apiKey = apiKeyService.getAllApiKeysByName(ApiKeyType.RADARR).getFirst();
//            this.overseerMapper = overseerMapper;
//        } catch (PersistenceException | NoSuchElementException exception) {
//            throw new OverseerPreConditionRequiredException("Radarr API keys not found");
//        }
//    }
//    // endregion - Constructors -
//}
