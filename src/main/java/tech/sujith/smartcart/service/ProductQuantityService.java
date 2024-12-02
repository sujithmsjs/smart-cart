package tech.sujith.smartcart.service;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tech.sujith.smartcart.domain.Product;
import tech.sujith.smartcart.domain.ProductQuantity;
import tech.sujith.smartcart.model.ProductQuantityDTO;
import tech.sujith.smartcart.repos.OrderRepository;
import tech.sujith.smartcart.repos.ProductQuantityRepository;
import tech.sujith.smartcart.repos.ProductRepository;
import tech.sujith.smartcart.util.NotFoundException;


@Service
@Transactional
public class ProductQuantityService {

    private final ProductQuantityRepository productQuantityRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public ProductQuantityService(final ProductQuantityRepository productQuantityRepository,
            final ProductRepository productRepository, final OrderRepository orderRepository) {
        this.productQuantityRepository = productQuantityRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public List<ProductQuantityDTO> findAll() {
        final List<ProductQuantity> productQuantities = productQuantityRepository.findAll(Sort.by("id"));
        return productQuantities.stream()
                .map(productQuantity -> mapToDTO(productQuantity, new ProductQuantityDTO()))
                .toList();
    }

    public ProductQuantityDTO get(final UUID id) {
        return productQuantityRepository.findById(id)
                .map(productQuantity -> mapToDTO(productQuantity, new ProductQuantityDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final ProductQuantityDTO productQuantityDTO) {
        final ProductQuantity productQuantity = new ProductQuantity();
        mapToEntity(productQuantityDTO, productQuantity);
        return productQuantityRepository.save(productQuantity).getId();
    }

    public void update(final UUID id, final ProductQuantityDTO productQuantityDTO) {
        final ProductQuantity productQuantity = productQuantityRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(productQuantityDTO, productQuantity);
        productQuantityRepository.save(productQuantity);
    }

    public void delete(final UUID id) {
        final ProductQuantity productQuantity = productQuantityRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        // remove many-to-many relations at owning side
        orderRepository.findAllByProducts(productQuantity)
                .forEach(order -> order.getProducts().remove(productQuantity));
        productQuantityRepository.delete(productQuantity);
    }

    private ProductQuantityDTO mapToDTO(final ProductQuantity productQuantity,
            final ProductQuantityDTO productQuantityDTO) {
        productQuantityDTO.setId(productQuantity.getId());
        productQuantityDTO.setQuantity(productQuantity.getQuantity());
        productQuantityDTO.setCost(productQuantity.getCost());
        productQuantityDTO.setProduct(productQuantity.getProduct() == null ? null : productQuantity.getProduct().getId());
        return productQuantityDTO;
    }

    private ProductQuantity mapToEntity(final ProductQuantityDTO productQuantityDTO,
            final ProductQuantity productQuantity) {
        productQuantity.setQuantity(productQuantityDTO.getQuantity());
        productQuantity.setCost(productQuantityDTO.getCost());
        final Product product = productQuantityDTO.getProduct() == null ? null : productRepository.findById(productQuantityDTO.getProduct())
                .orElseThrow(() -> new NotFoundException("product not found"));
        productQuantity.setProduct(product);
        return productQuantity;
    }

}
