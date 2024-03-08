package com.colak;

import com.colak.jpa.Pet;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.stat.Statistics;

@Slf4j
public class L2CacheTest {

    public static void main(String[] args) {
        // Create session factory - second level cache belongs to instance of session factory
        SessionFactory sessionFactory;
        StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            log.info("Could not create session factory: " + e.getMessage());
            StandardServiceRegistryBuilder.destroy( registry );
            return;
        }

        // Get statistics of session factory (including second-level cache statistics)
        Statistics statistics = sessionFactory.getStatistics();


        try (Session session = sessionFactory.openSession()) {
            Transaction transaction = session.beginTransaction();
            Pet originalPet = new Pet(1L, "pet1");
            session.merge(originalPet);
            session.flush();
            transaction.commit();

            // First session - fetch entity from the database
            Pet pet1 = session.find(Pet.class, 1L);
            printStatistics(statistics);
            log.info("Entity fetched: " + pet1 + " " + pet1.getName());
        }

        // Second session - fetch the same entity
        try(Session secondSession = sessionFactory.openSession()) {
            Pet pet2 = secondSession.find(Pet.class, 1L);
            printStatistics(statistics);
            log.info("Entity fetched: " + pet2 + " " + pet2.getName());
        }

        sessionFactory.close();
    }

    public static void printStatistics(Statistics statistics) {
        log.info("Misses in second level cache:" + statistics.getSecondLevelCacheMissCount());
        log.info("Added to second level cache:" + statistics.getSecondLevelCachePutCount());
        log.info("Found in second level cache:" + statistics.getSecondLevelCacheHitCount());
        statistics.clear();
    }
}