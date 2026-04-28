module menu.system.domain {
    requires jakarta.persistence;

    requires org.hibernate.orm.core;

    exports com.example.cateringapp.entity;
    // Open entities to all modules, including ALL-UNNAMED used by Spring Boot classpath runs.
    opens com.example.cateringapp.entity;
}
