package tech.sujith.smartcart.repos;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import tech.sujith.smartcart.domain.User;


public interface UserRepository extends JpaRepository<User, UUID> {
}
