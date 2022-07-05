package com.example.demo.Repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.demo.Model.FileListModel;

public interface FileRepo extends MongoRepository<FileListModel,String> {
	
}
