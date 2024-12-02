package tech.sujith.smartcart.repos;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.sujith.smartcart.domain.Product;
import tech.sujith.smartcart.domain.ProductQuantity;


public interface ProductQuantityRepository extends JpaRepository<ProductQuantity, UUID> {

    ProductQuantity findFirstByProduct(Product product);

}
