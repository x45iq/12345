package io.github.x45iq.task;

import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JdbcOperationsCourseRepository implements CourseRepository, RowMapper<Course> {
    private final JdbcOperations jdbcOperations;

    public JdbcOperationsCourseRepository(JdbcOperations jdbcOperations) {
        this.jdbcOperations = jdbcOperations;
    }

    @Override
    public List<Course> findAll() {
        return this.jdbcOperations.query("select * from t_course",this);
    }

    @Override
    public void save(Course course) {
        this.jdbcOperations.update("""
                insert into t_course(id,c_title) values (?, ?)
                """, course.id(),course.title());
    }

    @Override
    public Optional<Course> findById(UUID id) {
        return jdbcOperations.query("select * from t_course where id=?",new Object[]{id},this).stream().findFirst();
    }

    @Override
    public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Course(rs.getObject("id", UUID.class),rs.getString("c_title"));
    }
}
