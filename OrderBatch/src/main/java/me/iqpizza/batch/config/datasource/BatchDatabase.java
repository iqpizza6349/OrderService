package me.iqpizza.batch.config.datasource;

import org.springframework.beans.factory.annotation.Qualifier;

import javax.sql.DataSource;
import java.sql.*;

public class BatchDatabase {

    private final DataSource dataSource;

    public BatchDatabase(@Qualifier("staffDataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int getAutoMetaData() {
        try (final Connection connection = dataSource.getConnection()) {
            final String query = "insert into sales(id, orderAt) values (DEFAULT, NOW())";
            try (final PreparedStatement statement = connection
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                statement.executeUpdate();
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    System.out.println(rs.getInt(1));
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return 0;
    }

}
