package tech.sujith.smartcart.repos;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.sujith.smartcart.domain.Order;
import tech.sujith.smartcart.domain.ProductQuantity;
import tech.sujith.smartcart.domain.User;


public interface OrderRepository extends JpaRepository<Order, UUID> {

    Order findFirstByUser(User user);

    Order findFirstByProducts(ProductQuantity productQuantity);

    List<Order> findAllByProducts(ProductQuantity productQuantity);

}
