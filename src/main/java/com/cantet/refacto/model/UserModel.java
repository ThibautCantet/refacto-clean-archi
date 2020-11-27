package com.cantet.refacto.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "User")
@Getter
@Setter
public class UserModel {

    @Id
    private Integer id;

    public UserModel(int id){
        this.id = id;
    }
}