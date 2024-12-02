package tech.sujith.smartcart.repos;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.sujith.smartcart.domain.Product;


public interface ProductRepository extends JpaRepository<Product, UUID> {
}
