package com.example.demo.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.Model.RegisterModel;


public interface RegisterRepo extends MongoRepository<RegisterModel,String> {

}
