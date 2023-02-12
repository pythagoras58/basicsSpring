package com.example.basicsspring;

import com.example.basicsspring.Model.Customer;
import com.example.basicsspring.Service.CustomerService;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class CustomerServiceImp implements CustomerService {

    private final RowMapper<Customer> rowMapper = (rs,i)->new Customer(rs.getLong("id"), rs.getString("NAME"));

    private final JdbcTemplate jdbcTemplate;

    protected CustomerServiceImp(DataSource ds){
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Override
    public Collection<Customer> save(String... names) {

        List<Customer> customerList = new ArrayList<>();

        for(String name : names){
            KeyHolder keyHolder = new GeneratedKeyHolder();

            this.jdbcTemplate.update(connection->{
                PreparedStatement ps = connection.prepareStatement("insert into CUSTOMERS (name) values(?)",
                        Statement.RETURN_GENERATED_KEYS);
                ps.setString(1, name);
                return ps;
            }, keyHolder);

            Long KeyHolderKey = Objects.requireNonNull(keyHolder.getKey()).longValue();
            Customer customer = this.findById(KeyHolderKey);
            Assert.notNull(name, "the name given must not be null");
            customerList.add(customer);
        }
        return customerList;
    }

    @Override
    public Customer findById(Long id) {
        String sql = "select * from CUSTOMERS where id=?";
        return this.jdbcTemplate.queryForObject(sql,this.rowMapper, id);
    }

    @Override
    public Collection<Customer> findAll() {
        return this.jdbcTemplate.query("select * from CUSTOMERS", rowMapper);
    }
}
