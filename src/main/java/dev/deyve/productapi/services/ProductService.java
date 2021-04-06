package dev.deyve.productapi.services;

import dev.deyve.productapi.dtos.ProductDTO;
import dev.deyve.productapi.models.Product;
import dev.deyve.productapi.parsers.ProductParser;
import dev.deyve.productapi.repositories.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static dev.deyve.productapi.parsers.ProductParser.toProduct;
import static dev.deyve.productapi.parsers.ProductParser.toProductDTO;

/**
 * Product Service
 */
@Log4j2
@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Find Products
     *
     * @return List<Product>
     */
    public List<ProductDTO> findProducts() {

        List<Product> products = productRepository.findAll();

        log.debug("Product: {} ", products);

        return products.stream()
                .map(ProductParser::toProductDTO)
                .collect(Collectors.toList());
    }

    /**
     * Save Product
     *
     * @param productDTO ProductDTO
     * @return ProductDTO
     */
    public ProductDTO saveProduct(ProductDTO productDTO) {

        Product product = toProduct(productDTO);

        Product productSaved = productRepository.save(product);

        log.debug("Product: {} ", productSaved);

        return toProductDTO(productSaved);
    }

    /**
     * Find Product by Id
     *
     * @param id UUID
     * @return ProductDTO
     */
    public ProductDTO findById(UUID id) {

        Optional<Product> product = productRepository.findByExternalId(id);

        log.debug("Product: {} ", product);

        return product.map(ProductParser::toProductDTO).orElse(null);
    }

    /**
     * Update Product
     *
     * @param id         UUID
     * @param productDTO ProductDTO
     * @return ProductDTO
     */
    public ProductDTO updateProduct(UUID id, ProductDTO productDTO) {

        Optional<Product> optionalProduct = productRepository.findByExternalId(id);

        if (optionalProduct.isEmpty()) {
            return null;
        }

        Product product = toProduct(productDTO);
        product.setId(optionalProduct.get().getId()); // TODO
        product.setExternalId(id);

        Product productSaved = productRepository.save(product);

        return toProductDTO(productSaved);
    }

    /**
     * Delete Product
     *
     * @param id UUID
     */
    public void deleteProduct(UUID id) {

        Optional<Product> product = productRepository.findByExternalId(id);

        product.ifPresent(p -> productRepository.deleteById(p.getId()));
    }

    public List<ProductDTO> searchProducts(String q, BigDecimal min_price, BigDecimal max_price) {

        var productList = productRepository.findByNameContainingOrDescriptionContainingAndPriceGreaterThanAndPriceLessThan(q, q, min_price, max_price);

        return productList.stream()
                .map(ProductParser::toProductDTO)
                .collect(Collectors.toList());
    }
}
