package tech.sujith.smartcart.service;

import jakarta.transaction.Transactional;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tech.sujith.smartcart.domain.Order;
import tech.sujith.smartcart.domain.ProductQuantity;
import tech.sujith.smartcart.domain.User;
import tech.sujith.smartcart.model.OrderDTO;
import tech.sujith.smartcart.repos.OrderRepository;
import tech.sujith.smartcart.repos.ProductQuantityRepository;
import tech.sujith.smartcart.repos.UserRepository;
import tech.sujith.smartcart.util.NotFoundException;


@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductQuantityRepository productQuantityRepository;

    public OrderService(final OrderRepository orderRepository, final UserRepository userRepository,
            final ProductQuantityRepository productQuantityRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productQuantityRepository = productQuantityRepository;
    }

    public List<OrderDTO> findAll() {
        final List<Order> orders = orderRepository.findAll(Sort.by("id"));
        return orders.stream()
                .map(order -> mapToDTO(order, new OrderDTO()))
                .toList();
    }

    public OrderDTO get(final UUID id) {
        return orderRepository.findById(id)
                .map(order -> mapToDTO(order, new OrderDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final OrderDTO orderDTO) {
        final Order order = new Order();
        mapToEntity(orderDTO, order);
        return orderRepository.save(order).getId();
    }

    public void update(final UUID id, final OrderDTO orderDTO) {
        final Order order = orderRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(orderDTO, order);
        orderRepository.save(order);
    }

    public void delete(final UUID id) {
        orderRepository.deleteById(id);
    }

    private OrderDTO mapToDTO(final Order order, final OrderDTO orderDTO) {
        orderDTO.setId(order.getId());
        orderDTO.setPrice(order.getPrice());
        orderDTO.setDiscount(order.getDiscount());
        orderDTO.setAddress(order.getAddress());
        orderDTO.setUser(order.getUser() == null ? null : order.getUser().getId());
        orderDTO.setProducts(order.getProducts().stream()
                .map(productQuantity -> productQuantity.getId())
                .toList());
        return orderDTO;
    }

    private Order mapToEntity(final OrderDTO orderDTO, final Order order) {
        order.setPrice(orderDTO.getPrice());
        order.setDiscount(orderDTO.getDiscount());
        order.setAddress(orderDTO.getAddress());
        final User user = orderDTO.getUser() == null ? null : userRepository.findById(orderDTO.getUser())
                .orElseThrow(() -> new NotFoundException("user not found"));
        order.setUser(user);
        final List<ProductQuantity> products = productQuantityRepository.findAllById(
                orderDTO.getProducts() == null ? Collections.emptyList() : orderDTO.getProducts());
        if (products.size() != (orderDTO.getProducts() == null ? 0 : orderDTO.getProducts().size())) {
            throw new NotFoundException("one of products not found");
        }
        order.setProducts(new HashSet<>(products));
        return order;
    }

}
