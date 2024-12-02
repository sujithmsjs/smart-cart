package tech.sujith.smartcart.service;

import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import tech.sujith.smartcart.domain.Product;
import tech.sujith.smartcart.domain.ProductQuantity;
import tech.sujith.smartcart.model.ProductDTO;
import tech.sujith.smartcart.repos.ProductQuantityRepository;
import tech.sujith.smartcart.repos.ProductRepository;
import tech.sujith.smartcart.util.NotFoundException;
import tech.sujith.smartcart.util.ReferencedWarning;


@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductQuantityRepository productQuantityRepository;

    public ProductService(final ProductRepository productRepository,
            final ProductQuantityRepository productQuantityRepository) {
        this.productRepository = productRepository;
        this.productQuantityRepository = productQuantityRepository;
    }

    public List<ProductDTO> findAll() {
        final List<Product> products = productRepository.findAll(Sort.by("id"));
        return products.stream()
                .map(product -> mapToDTO(product, new ProductDTO()))
                .toList();
    }

    public ProductDTO get(final UUID id) {
        return productRepository.findById(id)
                .map(product -> mapToDTO(product, new ProductDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final ProductDTO productDTO) {
        final Product product = new Product();
        mapToEntity(productDTO, product);
        return productRepository.save(product).getId();
    }

    public void update(final UUID id, final ProductDTO productDTO) {
        final Product product = productRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(productDTO, product);
        productRepository.save(product);
    }

    public void delete(final UUID id) {
        productRepository.deleteById(id);
    }

    private ProductDTO mapToDTO(final Product product, final ProductDTO productDTO) {
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setCost(product.getCost());
        return productDTO;
    }

    private Product mapToEntity(final ProductDTO productDTO, final Product product) {
        product.setName(productDTO.getName());
        product.setCost(productDTO.getCost());
        return product;
    }

    public ReferencedWarning getReferencedWarning(final UUID id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Product product = productRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final ProductQuantity productProductQuantity = productQuantityRepository.findFirstByProduct(product);
        if (productProductQuantity != null) {
            referencedWarning.setKey("product.productQuantity.product.referenced");
            referencedWarning.addParam(productProductQuantity.getId());
            return referencedWarning;
        }
        return null;
    }

}
