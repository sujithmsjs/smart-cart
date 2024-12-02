package tech.sujith.smartcart.service;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tech.sujith.smartcart.domain.Order;
import tech.sujith.smartcart.domain.User;
import tech.sujith.smartcart.model.UserDTO;
import tech.sujith.smartcart.repos.OrderRepository;
import tech.sujith.smartcart.repos.UserRepository;
import tech.sujith.smartcart.util.NotFoundException;
import tech.sujith.smartcart.util.ReferencedWarning;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public UserService(final UserRepository userRepository, final OrderRepository orderRepository) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
    }

    public List<UserDTO> findAll() {
        final List<User> users = userRepository.findAll(Sort.by("id"));
        return users.stream()
                .map(user -> mapToDTO(user, new UserDTO()))
                .toList();
    }

    public UserDTO get(final UUID id) {
        return userRepository.findById(id)
                .map(user -> mapToDTO(user, new UserDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final UserDTO userDTO) {
        final User user = new User();
        mapToEntity(userDTO, user);
        return userRepository.save(user).getId();
    }

    public void update(final UUID id, final UserDTO userDTO) {
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userDTO, user);
        userRepository.save(user);
    }

    public void delete(final UUID id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(final User user, final UserDTO userDTO) {
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setCity(user.getCity());
        return userDTO;
    }

    private User mapToEntity(final UserDTO userDTO, final User user) {
        user.setName(userDTO.getName());
        user.setCity(userDTO.getCity());
        return user;
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final User user = userRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Order userOrder = orderRepository.findFirstByUser(user);
        if (userOrder != null) {
            referencedWarning.setKey("user.order.user.referenced");
            referencedWarning.addParam(userOrder.getId());
            return referencedWarning;
        }
        return null;
    }

}
