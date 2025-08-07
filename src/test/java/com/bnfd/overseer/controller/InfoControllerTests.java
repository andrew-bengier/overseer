package com.bnfd.overseer.controller;

import com.bnfd.overseer.exception.OverseerException;
import com.bnfd.overseer.exception.OverseerNoContentException;
import com.bnfd.overseer.model.api.info.GitReleaseNote;
import com.bnfd.overseer.model.api.info.LogFile;
import com.bnfd.overseer.service.LogService;
import com.bnfd.overseer.service.api.GitApiService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@WebMvcTest(InfoController.class)
@AutoConfigureMockMvc(addFilters = false)
public class InfoControllerTests {
    // region - Class Variables -
    private static final String BASE_MAPPING = "/api/info";

    @Autowired
    private Environment environment;

    @MockitoBean
    private BuildProperties buildProperties;

    @MockitoBean
    private GitApiService gitApiService;

    @MockitoBean
    private LogService logService;

    @Autowired
    private MockMvc mockMvc;
    // endregion - Class Variables -

    // region - Setup | Teardown -
    @BeforeAll
    public static void setup() {
        MDC.put("userId", String.format("User: %s", "InfoControllerTests"));
    }
    // endregion - Setup | Teardown -

    @Test
    @DisplayName("Retrieve ApiData - Expect OK")
    public void testGetApiData_retrieve_expectOk() throws Throwable {
        // NOTE: This test doesn't care about what is inside the apiData, just that it is being returned.
        mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("Retrieve GitInfo - Expect OK")
    public void testGetGitInfo_retrieve_expectOk() throws Throwable {
        GitReleaseNote testGitInfo = new GitReleaseNote();
        testGitInfo.setId(UUID.randomUUID().toString());

        Mockito.doReturn(Set.of(testGitInfo)).when(gitApiService).getGitRelease();

        MvcResult results = mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING + "/git")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        List<GitReleaseNote> response = new ObjectMapper().readValue(results.getResponse().getContentAsString(), new TypeReference<List<GitReleaseNote>>() {
        });

        Assertions.assertFalse(CollectionUtils.isEmpty(response));
        Assertions.assertEquals(response.get(0).getId(), testGitInfo.getId());
        Mockito.verify(gitApiService, Mockito.times(1)).getGitRelease();
    }

    @Nested
    @DisplayName("Retrieve Log Files")
    protected class testGetLogFiles {
        @Test
        @DisplayName("Acceptable Inputs - Expect OK")
        public void testGetLogFiles_acceptableInputs_expectOk() throws Throwable {
            LogFile testLogFile = new LogFile("current", Instant.now().minus(1, ChronoUnit.DAYS), Instant.now(), null);

            Mockito.doReturn(List.of(testLogFile)).when(logService).getAllLogFiles(Mockito.any(), Mockito.any());

            MvcResult results = mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING + "/logFiles")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            List response = new ObjectMapper().readValue(results.getResponse().getContentAsString(), List.class);

            Assertions.assertFalse(CollectionUtils.isEmpty(response));
            Mockito.verify(logService, Mockito.times(1)).getAllLogFiles(Mockito.any(), Mockito.any());
        }

        @Test
        @DisplayName("Dates Provided - Expect OK")
        public void testGetLogFiles_providedDates_expectOk() throws Throwable {
            LogFile testLogFile = new LogFile("current", Instant.now().minus(1, ChronoUnit.DAYS), Instant.now(), null);

            Mockito.doReturn(List.of(testLogFile)).when(logService).getAllLogFiles(Mockito.any(Instant.class), Mockito.any(Instant.class));

            MvcResult results = mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING + "/logFiles")
                            .param("startDate", Instant.now().toString())
                            .param("endDate", Instant.now().toString())
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            List response = new ObjectMapper().readValue(results.getResponse().getContentAsString(), List.class);

            Assertions.assertFalse(CollectionUtils.isEmpty(response));
            Mockito.verify(logService, Mockito.times(1)).getAllLogFiles(Mockito.any(Instant.class), Mockito.any(Instant.class));
        }

        @Test
        @DisplayName("StartDate Provided - Expect OK")
        public void testGetLogFiles_providedStartDate_expectOk() throws Throwable {
            LogFile testLogFile = new LogFile("current", Instant.now().minus(1, ChronoUnit.DAYS), Instant.now(), null);

            Mockito.doReturn(List.of(testLogFile)).when(logService).getAllLogFiles(Mockito.any(Instant.class), Mockito.any());

            MvcResult results = mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING + "/logFiles")
                            .param("startDate", Instant.now().toString())
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            List response = new ObjectMapper().readValue(results.getResponse().getContentAsString(), List.class);

            Assertions.assertFalse(CollectionUtils.isEmpty(response));
            Mockito.verify(logService, Mockito.times(1)).getAllLogFiles(Mockito.any(Instant.class), Mockito.any());
        }

        @Test
        @DisplayName("EndDate Provided - Expect OK")
        public void testGetLogFiles_providedEndDate_expectOk() throws Throwable {
            LogFile testLogFile = new LogFile("current", Instant.now().minus(1, ChronoUnit.DAYS), Instant.now(), null);

            Mockito.doReturn(List.of(testLogFile)).when(logService).getAllLogFiles(Mockito.any(), Mockito.any(Instant.class));

            MvcResult results = mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING + "/logFiles")
                            .param("endDate", Instant.now().toString())
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            List response = new ObjectMapper().readValue(results.getResponse().getContentAsString(), List.class);

            Assertions.assertFalse(CollectionUtils.isEmpty(response));
            Mockito.verify(logService, Mockito.times(1)).getAllLogFiles(Mockito.any(), Mockito.any(Instant.class));
        }

        @Test
        @DisplayName("None Found - Expect Error")
        public void testGetLogFiles_noneFound_expectError() throws Throwable {
            Mockito.doThrow(OverseerNoContentException.class).when(logService).getAllLogFiles(Mockito.any(), Mockito.any());

            mockMvc.perform(MockMvcRequestBuilders.get(BASE_MAPPING + "/logFiles")
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isNoContent());

            Mockito.verify(logService, Mockito.times(1)).getAllLogFiles(Mockito.any(), Mockito.any());
        }
    }

    @Nested
    @DisplayName("Retrieve Log File")
    protected class testGetLogFile {
        @Test
        @DisplayName("Acceptable Inputs - Expect OK")
        public void testGetLogFile_acceptableInputs_expectOk() throws Throwable {
            byte[] testContent = "test".getBytes();

            Mockito.doReturn(testContent).when(logService).getLogFileContent(Mockito.anyString());

            MvcResult results = mockMvc.perform(MockMvcRequestBuilders.post(BASE_MAPPING + "/logFiles/file")
                            .content("test")
                            .accept(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andReturn();

            byte[] response = results.getResponse().getContentAsByteArray();

            Assertions.assertArrayEquals(response, testContent);
            Mockito.verify(logService, Mockito.times(1)).getLogFileContent(Mockito.anyString());
        }

        @Test
        @DisplayName("Not Found - Expect Error")
        public void testGetLogFile_notFound_expectError() throws Throwable {
            Mockito.doThrow(OverseerException.class).when(logService).getLogFileContent(Mockito.anyString());

            mockMvc.perform(MockMvcRequestBuilders.post(BASE_MAPPING + "/logFiles/file")
                            .content("notFound")
                            .accept(MediaType.APPLICATION_OCTET_STREAM_VALUE))
                    .andExpect(MockMvcResultMatchers.status().is5xxServerError());

            Mockito.verify(logService, Mockito.times(1)).getLogFileContent(Mockito.anyString());
        }
    }
}
