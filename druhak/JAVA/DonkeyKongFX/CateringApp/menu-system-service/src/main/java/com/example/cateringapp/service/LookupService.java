package com.example.cateringapp.service;

import com.example.cateringapp.entity.Project;
import com.example.cateringapp.entity.Template;
import com.example.cateringapp.entity.User;
import com.example.cateringapp.repository.ProjectRepository;
import com.example.cateringapp.repository.TemplateRepository;
import com.example.cateringapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Service
public class LookupService {

    private ProjectRepository projectRepository;
    private UserRepository userRepository;
    private TemplateRepository templateRepository;

    @Autowired
    public LookupService(ProjectRepository projectRepository, UserRepository userRepository, TemplateRepository templateRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
        this.templateRepository = templateRepository;
    }

    public List<Project> getProjects() {
        return projectRepository.findAll();
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public List<Template> getTemplates() {
        return templateRepository.findAll();
    }
}
