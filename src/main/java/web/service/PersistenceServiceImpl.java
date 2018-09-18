package web.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import search_service.FindNumberResponse;

import javax.sql.DataSource;

@Repository
public class PersistenceServiceImpl implements PersistenceService {

    private JdbcTemplate jdbcTemplate;

    public PersistenceServiceImpl(JdbcTemplate jdbcTemplate, DataSource searchDataSource) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcTemplate.setDataSource(searchDataSource);
    }

    @Override
    public void storeResult(Integer number, FindNumberResponse response) {
        jdbcTemplate.update(
                "INSERT INTO result (code, number, filenames, error) VALUES (?,?,?,?)",
                response.getCode(),
                number,
                String.join(",", response.getFileNames()),
                null
        );
    }
}
