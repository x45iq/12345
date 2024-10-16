package io.github.x45iq.task;

import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("api/v1")
public class CourseRestController {
    private final CourseRepository courseRepository;
    private final MessageSource messageSource;

    public CourseRestController(CourseRepository courseRepository, MessageSource messageSource) {
        this.courseRepository = courseRepository;
        this.messageSource = messageSource;
    }

    @GetMapping
    public ResponseEntity<List<Course>> handleGetAllCourses() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(courseRepository.findAll());
    }
    @GetMapping("/greeting")
    public String greeting(){
        return "Hello world!";
    }

    @PostMapping
    @Transactional
    public ResponseEntity<?> handleCreateNewCourse(@RequestBody NewCoursePayload coursePayload,
                                                   UriComponentsBuilder uriComponentsBuilder,
                                                   Locale locale) {
        if (coursePayload.title() == null || coursePayload.title().isBlank()) {
            String message = messageSource.getMessage("course.create.title.error.not_set", new Object[0], locale);
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(new ErrorsPresentation(List.of(message)));
        } else {
            final var course = new Course(coursePayload.title());
            courseRepository.save(course);
            return ResponseEntity.created(uriComponentsBuilder.path("api/v1/{courseId}").build(Map.of("courseId", course.id())))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(course);
        }
    }

    @GetMapping(path="{id}")
    public ResponseEntity<?> handleGetCourse(@PathVariable("id") UUID id) {
        return courseRepository.findById(id).map(course -> ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(course)).orElse(ResponseEntity.notFound().build());
    }
}
