package org.example.springwebcatalog.Model.Product;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Setter
@Getter
public class Tag {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;


    public Tag(String name) {
        this.name = name;
    }

    public Tag() {

    }

    @Override
    public String toString() {
        return name;
    }
}
