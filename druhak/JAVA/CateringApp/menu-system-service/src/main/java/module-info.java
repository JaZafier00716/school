module menu.system.service {
    requires spring.context;
    requires spring.core;
    requires spring.beans;
    requires spring.tx;
    requires spring.aop;
    requires jakarta.validation;
    requires static lombok;
    requires menu.system.persistence;
    requires transitive menu.system.domain;

    exports com.example.cateringapp.service;
    exports com.example.cateringapp.dto;
    exports com.example.cateringapp.service.exception;

    opens com.example.cateringapp.service to spring.core, spring.beans, spring.aop;
    opens com.example.cateringapp.dto to spring.core, com.fasterxml.jackson.databind;
}
