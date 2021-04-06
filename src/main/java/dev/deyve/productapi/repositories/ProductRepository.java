package dev.deyve.productapi.repositories;

import dev.deyve.productapi.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Product Repository
 */
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByExternalId(UUID uuid);

    List<Product> findByNameContainingOrDescriptionContainingAndPriceGreaterThanAndPriceLessThan(String name, String description, BigDecimal min_price, BigDecimal max_price);

}
