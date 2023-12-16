package org.example.springwebcatalog.Repositories;

import org.example.springwebcatalog.Model.User.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<CustomUser, UUID> {

    CustomUser findCustomUserByUsername(String username);


}
