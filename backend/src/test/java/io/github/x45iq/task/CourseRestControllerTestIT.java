package io.github.x45iq.task;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@SpringBootTest(classes = TestBeans.class)
@AutoConfigureMockMvc(printOnlyOnFailure = false)
@Sql({"/sql/task_rest_controller/test_data.sql"})
public class CourseRestControllerTestIT {

    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser
    void handleGetAllCourses_ReturnsValidResponseEntity() throws Exception {
        //given
        var requestBuilder = MockMvcRequestBuilders.get("/api/v1");

        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andExpectAll(
                        status().isOk(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        content().json("""
                                [
                                {
                                  "id": "520ca403-bc46-40cb-a38e-6b2c490e25f2",
                                  "title": "title_1"
                                },
                                {
                                  "id": "03aba9fb-08bb-4fb3-9d19-4237160504db",
                                  "title": "title_2"
                                }
                                ]
                                """)
                );
    }
    @Test
    @WithMockUser
    void handleCreateNewCourse_PayloadIsValid_ReturnsValidResponseEntity() throws Exception{
        //given
        var requestBuilder = MockMvcRequestBuilders.post("/api/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                        "title": "Course"
                        }
                        """);
        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andExpectAll(
                        status().isCreated(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        header().exists(HttpHeaders.LOCATION),
                        content().json("""
                                {
                                "title": "Course"
                                }
                                """),
                        jsonPath("$.id").exists()
                );
    }
    @Test
    @WithMockUser
    void handleCreateNewCourse_PayloadIsInvalid_ReturnsValidResponseEntity() throws Exception{
        //given
        var requestBuilder = MockMvcRequestBuilders.post("/api/v1")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.ACCEPT_LANGUAGE, "en")
                .content("""
                        {
                        "title": null
                        }
                        """);
        //when
        this.mockMvc.perform(requestBuilder)
                //then
                .andExpectAll(
                        status().isBadRequest(),
                        content().contentType(MediaType.APPLICATION_JSON),
                        header().doesNotExist(HttpHeaders.LOCATION),
                        content().json("""
                                {
                                "errors": ["Title must be set"]
                                }
                                """),
                        jsonPath("$.id").doesNotExist()
                );
    }
}
