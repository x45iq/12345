package io.github.x45iq.task;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CourseRestControllerTest {
    @Mock
    CourseRepository courseRepository;

    @Mock
    MessageSource messageSource;

    @InjectMocks
    CourseRestController controller;

    @Test
    @DisplayName("GET /api/v1 возвращает HTTP ответ со статусом 200 OK и списком курсов")
    void handleGetAllCourses_ReturnsValidResponseEntity(){
        //given
        var courses = List.of(new Course(UUID.randomUUID(),"Course_1"),new Course(UUID.randomUUID(),"Course_2"));
        Mockito.doReturn(courses).when(this.courseRepository).findAll();

        //when
        var response = controller.handleGetAllCourses();

        //then
        assertNotNull(response);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON,response.getHeaders().getContentType());
        assertEquals(response.getBody(),courses);
    }
    @Test
    @DisplayName("POST /api/v1 возвращает HTTP ответ со статусом 201 CREATED и созданным курсом, сохраняет курс в репозиторий и возвращает локацию созданного курса")
    void handleCreateNewCourse_PayloadIsValid_ReturnsValidResponseEntity(){
        //given
        var title = "Курс по каканию";
        //when
        var responseEntity = controller.handleCreateNewCourse(new NewCoursePayload(title),
                UriComponentsBuilder.fromUriString("http://localhost:8080"), Locale.ENGLISH);
        //then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED,responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON,responseEntity.getHeaders().getContentType());
        if(responseEntity.getBody() instanceof Course course){
            assertNotNull(course.id());
            assertEquals(title,course.title());
            assertEquals(URI.create("http://localhost:8080/api/v1/" + course.id()),responseEntity.getHeaders().getLocation());

            Mockito.verify(this.courseRepository).save(course);
        }else{
            assertInstanceOf(Course.class,responseEntity.getBody());
        }
        Mockito.verifyNoMoreInteractions(this.courseRepository);
    }
    @Test
    @DisplayName("POST /api/v1 возвращает HTTP ответ со статусом 404 BAD_REQUEST и сообщением об ошибке")
    void handleCreateNewCourse_PayloadIsInvalid_ReturnsValidResponseEntity(){
        //given
        var title = "    ";
        var locale = Locale.ENGLISH;
        var errorMessage = "Title must be set";
        Mockito.doReturn(errorMessage).when(this.messageSource).getMessage("course.create.title.error.not_set", new Object[0], locale);
        //when
        var responseEntity = controller.handleCreateNewCourse(new NewCoursePayload(title),UriComponentsBuilder.fromUriString("http://localhost:8080"),locale);
        //then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST,responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON,responseEntity.getHeaders().getContentType());
        assertEquals(new ErrorsPresentation(List.of(errorMessage)),responseEntity.getBody());

        Mockito.verifyNoInteractions(this.courseRepository);
    }

    @Test
    @DisplayName("GET /api/v1/{courseId} возвращает HTTP ответ со статусом 200 OK и курсом")
    void handleGetCourse_CourseIsExists_ReturnsValidResponseEntity(){
        //given
        UUID uuid = UUID.randomUUID();
        var course = new Course(uuid,"tit");
        Mockito.doReturn(Optional.of(course)).when(this.courseRepository).findById(uuid);
        //when
        var responseEntity = controller.handleGetCourse(uuid);
        //then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON,responseEntity.getHeaders().getContentType());
        assertEquals(course,responseEntity.getBody());

        Mockito.verifyNoMoreInteractions(this.courseRepository);
    }

    @Test
    @DisplayName("GET /api/v1/{courseId} возвращает HTTP ответ со статусом 404 NOT_FOUND")
    void handleGetCourse_CourseIsNotExists_ReturnsValidResponseEntity(){
        //given
        var uuid = UUID.randomUUID();
        Mockito.doReturn(Optional.empty()).when(this.courseRepository).findById(uuid);
        //when
        var responseEntity = controller.handleGetCourse(uuid);
        //then
        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND,responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        Mockito.verifyNoMoreInteractions(this.courseRepository);
    }
}