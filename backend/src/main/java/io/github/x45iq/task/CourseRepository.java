package io.github.x45iq.task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository {
    List<Course> findAll();
    void save(Course course);

    Optional<Course> findById(UUID id);
}
