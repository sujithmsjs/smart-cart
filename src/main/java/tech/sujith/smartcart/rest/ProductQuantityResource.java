package tech.sujith.smartcart.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.sujith.smartcart.model.ProductQuantityDTO;
import tech.sujith.smartcart.service.ProductQuantityService;


@RestController
@RequestMapping(value = "/api/productQuantities", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProductQuantityResource {

    private final ProductQuantityService productQuantityService;

    public ProductQuantityResource(final ProductQuantityService productQuantityService) {
        this.productQuantityService = productQuantityService;
    }

    @GetMapping
    public ResponseEntity<List<ProductQuantityDTO>> getAllProductQuantities() {
        return ResponseEntity.ok(productQuantityService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductQuantityDTO> getProductQuantity(
            @PathVariable(name = "id") final UUID id) {
        return ResponseEntity.ok(productQuantityService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<UUID> createProductQuantity(
            @RequestBody @Valid final ProductQuantityDTO productQuantityDTO) {
        final UUID createdId = productQuantityService.create(productQuantityDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UUID> updateProductQuantity(@PathVariable(name = "id") final UUID id,
            @RequestBody @Valid final ProductQuantityDTO productQuantityDTO) {
        productQuantityService.update(id, productQuantityDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteProductQuantity(@PathVariable(name = "id") final UUID id) {
        productQuantityService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
