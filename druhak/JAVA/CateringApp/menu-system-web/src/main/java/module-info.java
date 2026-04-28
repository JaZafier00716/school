module menu.system.web {
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.context;
    requires spring.core;
    requires spring.beans;
    requires spring.web;
    requires spring.webmvc;
    requires spring.data.jpa;
    requires spring.expression;
    requires jakarta.validation;
    requires menu.system.domain;
    requires menu.system.persistence;
    requires menu.system.service;

    requires jakarta.servlet;
    requires thymeleaf;
    requires thymeleaf.spring6;

    exports com.example.cateringapp;
    exports com.example.cateringapp.controller.api;
    exports com.example.cateringapp.controller.web;

    opens com.example.cateringapp to spring.core, spring.beans;
    opens com.example.cateringapp.controller.api to spring.core, spring.beans, spring.web;
    opens com.example.cateringapp.controller.web to spring.core, spring.beans, spring.web;
    opens com.example.cateringapp.web.error to spring.core, spring.beans, spring.web;
}
