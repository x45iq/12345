package io.github.x45iq.security.repository;

import io.github.x45iq.security.models.User;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.MappingSqlQuery;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JdbcUserRepository extends MappingSqlQuery<User> implements UserRepository {

    public JdbcUserRepository(DataSource ds) {
        super(ds, """
                select
                u.id,
                u.c_username,
                up.c_password
                from t_user u
                left join t_user_password up on up.id_user = u.id
                where u.c_username = :username
                group by u.id, up.id
                """);
        this.declareParameter(new SqlParameter("username", Types.VARCHAR));
        this.compile();
    }
    public Optional<User> findUserByUsername(String username){
        return Optional.ofNullable(this.findObjectByNamedParam(Map.of("username",username)));
    }

    @Override
    public User mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new User(rs.getObject("id", UUID.class),rs.getString("c_username"),rs.getString("c_password"));
    }
}
