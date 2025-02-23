package batch.toDatabase;

import java.sql.BatchUpdateException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyTableRowMapper implements org.springframework.jdbc.core.RowMapper {
    private static final Logger logger = LoggerFactory.getLogger(MyTableRowMapper.class);

    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        try {
            MyTable dto = new MyTable();
            String name = rs.getString("name");
            dto.setName(name != null ? name : "");
            dto.setId(rs.getInt("id"));
            logger.info("Reading record - ID: {}, Name: {}", dto.getId(), dto.getName());
            return dto;
        }catch ( BatchUpdateException e ){
            throw e;
        } catch (SQLException e) {
            logger.error("Error while mapping row", e);
            throw e;  // Rilancia l'eccezione per permettere al processo di batch di continuare correttamente
        }
    }
}