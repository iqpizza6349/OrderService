package me.iqpizza.batch.config.job;

import me.iqpizza.batch.config.datasource.BatchDatabase;
import me.iqpizza.batch.dto.OrderQuantity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.*;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ComponentScan(basePackageClasses = BatchConfiguration.class)
public class OrderBatchConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    private final DataSource orderDataSource;

    private static final int CHUNK_SIZE = 100;
    private final BatchDatabase batchDatabase;
    private final JdbcTemplate jdbcTemplate;

    public OrderBatchConfiguration(JobBuilderFactory jobBuilderFactory,
                                   StepBuilderFactory stepBuilderFactory,
                                   @Qualifier("orderDataSource") DataSource orderDataSource,
                                   @Qualifier("batchDataSource") DataSource batchDataSource) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.orderDataSource = orderDataSource;
        this.batchDatabase = new BatchDatabase(batchDataSource);
        this.jdbcTemplate = new JdbcTemplate(batchDataSource);
    }

    @Bean
    public Job orderPagingItemReader() throws Exception {
        return jobBuilderFactory.get("orderPagingItemReader")
                .start(orderPagingReaderStep())
                .build();
    }

    @Bean
    public Step orderPagingReaderStep() throws Exception {
        return stepBuilderFactory.get("orderPagingReaderStep")
                .<OrderQuantity, OrderQuantity>chunk(CHUNK_SIZE)
                .reader(itemJdbcPagingItemReader())
                .writer(lineItemItemWriter())
                .build();
    }

    @Bean
    public JdbcPagingItemReader<OrderQuantity> itemJdbcPagingItemReader() throws Exception {
        return new JdbcPagingItemReaderBuilder<OrderQuantity>()
                .pageSize(CHUNK_SIZE)
                .fetchSize(CHUNK_SIZE)
                .dataSource(orderDataSource)
                .rowMapper(new BeanPropertyRowMapper<>(OrderQuantity.class))
                .queryProvider(createQueryProvider())
                .name("itemJdbcPagingItemReader")
                .build();
    }

    @Bean
    public ItemWriter<OrderQuantity> lineItemItemWriter() {
        return items -> {
            System.out.println(items);
            int key = batchDatabase.getAutoMetaData();
            final String sql = "insert into quantity(sales_id, name, qty) VALUES (?, ?, ?)";
            final int[] price = {0};
            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    OrderQuantity orderQuantity = items.get(i);
                    ps.setInt(1, key);
                    ps.setString(2, orderQuantity.getName());
                    ps.setInt(3, orderQuantity.getTotalQuantity());
                    price[0] += orderQuantity.getTotalPrice();
                }

                @Override
                public int getBatchSize() {
                    return items.size();
                }
            });
            int totalPrice = price[0];
            final String salesSQL = "update sales set daily_sales = ? where sales_id = " + key;
            jdbcTemplate.update(salesSQL, totalPrice);
        };
    }

    @Bean
    public PagingQueryProvider createQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProviderFactoryBean
                = new SqlPagingQueryProviderFactoryBean();
        queryProviderFactoryBean.setDataSource(orderDataSource);
        queryProviderFactoryBean.setSelectClause("select name, sum(qty) as totalQuantity, sum(price) as totalPrice");
        queryProviderFactoryBean.setFromClause("from line_item");
        queryProviderFactoryBean.setWhereClause(
                "where line_item.order_id = " +
                        "(select order_id from orders where ordered_at = STR_TO_DATE(NOW(), '%Y-%m-%d'))"
        );
        queryProviderFactoryBean.setGroupClause(
                "group by name"
        );

        Map<String, Order> orderMap = new HashMap<>(1);
        orderMap.put("name", Order.DESCENDING);
        queryProviderFactoryBean.setSortKeys(orderMap);
        return queryProviderFactoryBean.getObject();
    }
}
