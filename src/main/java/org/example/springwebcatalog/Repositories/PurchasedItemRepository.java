package org.example.springwebcatalog.Repositories;

import org.example.springwebcatalog.Model.User.UserProducts.PurchasedItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchasedItemRepository extends JpaRepository<PurchasedItem, Long> {

}
