package dev.anamika.journalApp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.lang.management.PlatformLoggingMXBean;

@Configuration
@EnableTransactionManagement
//To enable @Transational --> we use PlatformTransactionManager Interface --> Implemented by the MongoDatabaseFactory
//This tells our framework --> when it sees @Transational --> use MongoDB transactions
public class TransactionConfig {

    @Bean
    public PlatformTransactionManager manageTransactions(MongoDatabaseFactory dbFactory){
        return new MongoTransactionManager(dbFactory);
    }
}

//Important --> Transactions works on MongoDB only when it is running as a Replica Set, Shared Cluster not on Standalone server
