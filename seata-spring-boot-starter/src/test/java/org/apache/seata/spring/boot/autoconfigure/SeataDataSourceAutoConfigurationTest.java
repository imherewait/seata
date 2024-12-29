package org.apache.seata.spring.boot.autoconfigure;

import org.apache.seata.spring.annotation.datasource.SeataAutoDataSourceProxyCreator;
import org.apache.seata.spring.boot.autoconfigure.mock.MockDataSource;
import org.apache.seata.spring.boot.autoconfigure.properties.SeataProperties;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {SeataDataSourceAutoConfigurationTest.TestConfig.class,SeataDataSourceAutoConfiguration.class})
public class SeataDataSourceAutoConfigurationTest extends AutoConfigurationBaseTest{

    @Autowired
    private SeataAutoDataSourceProxyCreator seataAutoDataSourceProxyCreator;

    @Test
    public void assertDataSourceProxyNotNull() {
        assertNotNull(seataAutoDataSourceProxyCreator);
    }

    @Configuration
    @SpringBootApplication
    static class TestConfig {
        @Bean
        public DataSource mockDataSource() {
            return new MockDataSource();
        }
    }

}
