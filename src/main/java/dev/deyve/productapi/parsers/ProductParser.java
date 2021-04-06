package dev.deyve.productapi.parsers;

import dev.deyve.productapi.dtos.ProductDTO;
import dev.deyve.productapi.models.Product;

import java.util.UUID;

/**
 * Product Parser
 */
public class ProductParser {

    public static Product toProduct(ProductDTO productDTO) {

        return Product.builder()
                .externalId(UUID.randomUUID())
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .price(productDTO.getPrice())
                .build();
    }

    public static ProductDTO toProductDTO(Product product) {

        return ProductDTO.builder()
                .id(product.getExternalId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
