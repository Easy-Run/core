package com.easyrun.database;

import java.io.File;
import java.util.Properties;
import javax.sql.DataSource;
import com.easyrun.environment.EnvironmentInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.sqlite.SQLiteDataSource;

@Configuration
public class DatabaseConfig {

    /**
     * Configure le DataSource pour SQLite.
     */
    @Bean
    public DataSource dataSource() {
        SQLiteDataSource dataSource = new SQLiteDataSource();
        // Récupère le chemin du répertoire de la base de données
        String coreDbDir = EnvironmentInitializer.getCoreDbDir();
        // S'assure que le répertoire existe (sinon, le créer)
        new File(coreDbDir).mkdirs();
        // Construit le chemin complet vers le fichier de base de données
        String dbFilePath = coreDbDir + File.separator + "easyrun.db";
        dataSource.setUrl("jdbc:sqlite:" + dbFilePath);
        return dataSource;
    }

    /**
     * Configure l'EntityManagerFactory en scannant uniquement les packages d'entités souhaités.
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean emfb = new LocalContainerEntityManagerFactoryBean();
        emfb.setDataSource(dataSource());
        // Liste explicite des packages contenant des entités JPA
        emfb.setPackagesToScan(
            "com.easyrun.database.pda.model",
            "com.easyrun.database.git.model"
        );

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        emfb.setJpaVendorAdapter(vendorAdapter);

        Properties jpaProperties = new Properties();
        jpaProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.SQLiteDialect");
        jpaProperties.setProperty("hibernate.hbm2ddl.auto", "update");
        emfb.setJpaProperties(jpaProperties);

        return emfb;
    }

    /**
     * Configure le gestionnaire de transaction pour JPA.
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager txnManager = new JpaTransactionManager();
        txnManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return txnManager;
    }

    /**
     * Permet de traduire les exceptions JPA en exceptions Spring.
     */
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }
}
