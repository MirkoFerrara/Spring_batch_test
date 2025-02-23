package batch.toDatabase;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.batch.item.database.ItemPreparedStatementSetter;

public class MyTablePreparedStatementSetter implements ItemPreparedStatementSetter<MyTable> {
    public void setValues(MyTable item, PreparedStatement ps) throws SQLException {
        ps.setInt(1, item.getId());
        ps.setString(2, item.getName());
    }
}