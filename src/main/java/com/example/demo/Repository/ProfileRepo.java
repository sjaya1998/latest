package com.example.demo.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.Model.ProfileModel;

public interface ProfileRepo extends MongoRepository<ProfileModel,String> {

}
