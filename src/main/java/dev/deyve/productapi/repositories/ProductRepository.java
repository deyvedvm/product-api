package dev.deyve.productapi.repositories;

import dev.deyve.productapi.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Product Repository
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByExternalId(UUID uuid);

    @Query("select p from Product p where p.price between ?1 and ?2 and (p.name like %?3% or p.description like %?4%)")
    List<Product> findByPriceBetweenAndNameContainingAndDescriptionContaining(BigDecimal min_price, BigDecimal max_price, String name, String description);

}
