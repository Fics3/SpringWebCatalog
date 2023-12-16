package org.example.springwebcatalog.Repositories;

import org.example.springwebcatalog.Model.Product.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {


}
