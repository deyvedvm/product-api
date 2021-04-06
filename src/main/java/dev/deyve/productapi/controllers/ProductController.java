package dev.deyve.productapi.controllers;

import dev.deyve.productapi.dtos.ProductDTO;
import dev.deyve.productapi.models.Product;
import dev.deyve.productapi.services.ProductService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static dev.deyve.productapi.parsers.ProductParser.toProductDTO;

/**
 * Product Controller
 */
@Log4j2
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Get Products
     *
     * @return List<Product>
     */
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getProducts() {

        List<ProductDTO> productDTOList = productService.findProducts();

        log.info("Product DTO List: {} ", productDTOList);

        return ResponseEntity.ok()
                .header("count", String.format("%d", productDTOList.size()))
                .body(productDTOList);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> postProduct(@RequestBody ProductDTO productDTO) {

        ProductDTO productDTOSaved = productService.saveProduct(productDTO);

        log.info("ProductDTO: {} ", productDTOSaved);

        return ResponseEntity.status(HttpStatus.CREATED).body(productDTOSaved);
    }

    /**
     * Get Product
     *
     * @param id UUID
     * @return ProductDTO
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable UUID id) {

        Product product = productService.findByExternalId(id);

        log.info("Product: {} ", product);

        if (product == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(toProductDTO(product), HttpStatus.OK);
    }

    /**
     * Put Product
     *
     * @param id         UUID
     * @param productDTO ProductDTO
     * @return ProductDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> putProduct(@PathVariable UUID id, @RequestBody ProductDTO productDTO) {

        ProductDTO productSaved = productService.updateProduct(id, productDTO);

        if (productSaved == null) {
            return ResponseEntity.notFound().build();
        }

        log.info("ProductDTO: {} ", productSaved);

        return new ResponseEntity<>(productSaved, HttpStatus.OK);
    }

    /**
     * Delete Product
     *
     * @param id UUID
     * @return Void
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {

        Product product = productService.findByExternalId(id);

        if (product == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        productService.deleteProduct(product.getId());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Search Products
     *
     * @param q         Name or Description
     * @param min_price BigDecimal
     * @param max_price BigDecimal
     * @return List<ProductDTO>
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String q, @RequestParam BigDecimal min_price, BigDecimal max_price) {

        List<ProductDTO> productDTOList = productService.searchProducts(q, min_price, max_price);

        log.info("Product DTO Search List: {} ", productDTOList);

        return ResponseEntity.ok()
                .header("count", String.format("%d", productDTOList.size()))
                .body(productDTOList);
    }
}
