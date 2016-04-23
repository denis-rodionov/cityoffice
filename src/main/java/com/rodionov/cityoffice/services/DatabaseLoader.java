package com.rodionov.cityoffice.services;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseLoader {

    @PostConstruct
    private void initDatabase() {
    }
}