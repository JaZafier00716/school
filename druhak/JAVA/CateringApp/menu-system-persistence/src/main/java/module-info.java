module menu.system.persistence {
    requires spring.context;
    requires spring.core;
    requires spring.beans;
    requires spring.data.commons;
    requires spring.data.jpa;
    requires menu.system.domain;

    exports com.example.cateringapp.repository;
    opens com.example.cateringapp.repository to spring.core, spring.beans;
}
