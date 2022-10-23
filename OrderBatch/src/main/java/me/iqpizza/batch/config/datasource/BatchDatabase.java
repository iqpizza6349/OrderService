package me.iqpizza.batch.config.datasource;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class BatchDatabase {

    private final DataSource dataSource;

    public BatchDatabase(@Qualifier("batchDataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public int getAutoMetaData() {
        try (final Connection connection = dataSource.getConnection()) {
            final String query = "insert into sales(sales_id, order_at) values (DEFAULT, NOW())";
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
