package dev.deyve.productapi.controllers;

import dev.deyve.productapi.dtos.ProductDTO;
import dev.deyve.productapi.exceptions.MessageError;
import dev.deyve.productapi.exceptions.ProductNotFoundException;
import dev.deyve.productapi.models.Product;
import dev.deyve.productapi.services.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static dev.deyve.productapi.parsers.ProductParser.toProductDTO;
import static org.springframework.http.HttpStatus.NO_CONTENT;

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
    @Operation(summary = "Find products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products found",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Products not found", content = @Content)})
    public ResponseEntity<List<ProductDTO>> getProducts() {

        List<ProductDTO> productDTOList = productService.findProducts();

        log.info("Product DTO List: {} ", productDTOList);

        return ResponseEntity.ok()
                .header("count", String.format("%d", productDTOList.size()))
                .body(productDTOList);
    }

    @PostMapping
    @Operation(summary = "Save product")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product saved",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Products not found", content = @Content)})
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
    @Operation(summary = "Get product by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found product",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = MessageError.class))})})
    public ResponseEntity<ProductDTO> getProduct(@PathVariable UUID id) {

        Product product = productService.findByExternalId(id);

        log.info("Product: {} ", product);

        if (product == null) throw new ProductNotFoundException("Product Not Found");

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
    @Operation(summary = "Update product by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated product",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = ProductDTO.class))}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = MessageError.class))})})
    public ResponseEntity<ProductDTO> putProduct(@PathVariable UUID id, @RequestBody ProductDTO productDTO) {

        ProductDTO productSaved = productService.updateProduct(id, productDTO);

        if (productSaved == null) throw new ProductNotFoundException("Product Not Found");

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
    @Operation(summary = "Delete product by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete product"),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Product not found", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = MessageError.class))})})
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {

        Product product = productService.findByExternalId(id);

//        if (product == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found");

        if (product == null) throw new ProductNotFoundException("Product Not Found");

        productService.deleteProduct(product.getId());

        return new ResponseEntity<>(NO_CONTENT);
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
    @Operation(summary = "Search products")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Products found",
                    content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ProductDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Products not found", content = @Content)})
    public ResponseEntity<List<ProductDTO>> searchProducts(@RequestParam String q, @RequestParam BigDecimal min_price, BigDecimal max_price) {

        List<ProductDTO> productDTOList = productService.searchProducts(q, min_price, max_price);

        log.info("Product DTO Search List: {} ", productDTOList);

        return ResponseEntity.ok()
                .header("count", String.format("%d", productDTOList.size()))
                .body(productDTOList);
    }
}
