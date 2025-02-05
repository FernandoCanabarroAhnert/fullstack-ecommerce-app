package com.fernandocanabarro.fullstack_ecommerce_app.services.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductRequestDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductDetailDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductGridDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductImageDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductRatingRequestDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductRatingResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductReportDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.Brand;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.Category;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.Product;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.ProductImage;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.ProductRating;
import com.fernandocanabarro.fullstack_ecommerce_app.entities.User;
import com.fernandocanabarro.fullstack_ecommerce_app.projections.ProductProjection;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.BrandRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.CategoryRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.ProductImageRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.ProductRatingRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.repositories.ProductRepository;
import com.fernandocanabarro.fullstack_ecommerce_app.services.AuthService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.ProductService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.BadRequestException;
import com.fernandocanabarro.fullstack_ecommerce_app.services.exceptions.ResourceNotFoundException;
import com.fernandocanabarro.fullstack_ecommerce_app.utils.Utils;

import lombok.RequiredArgsConstructor;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ProductRatingRepository productRatingRepository;
    private final AuthService authService;

    @Override
    @Transactional(readOnly = true)
    public List<ProductReportDTO> findAllForReport() {
        return productRepository.findAll()
            .stream().map(ProductReportDTO::new)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductReportDTO> findAllForReportPaginated(String name,Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(name,pageable)
            .map(ProductReportDTO::new);
    }

    @Override
    @Transactional
    public ProductRequestDTO findProductByIdToUpdate(Long id) {
        return productRepository.findById(id)
            .map(ProductRequestDTO::new)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
    }

    @Override
    @Transactional
    public void createProduct(ProductRequestDTO dto) {

        Product product = new Product();

        product.setName(dto.getName());
        product.setDescription(dto.getDescription());

        double price = Double.parseDouble(dto.getPrice().replace(",", "."));
        product.setPrice(BigDecimal.valueOf(price));

        product.setViews(0);
        product.setStockQuantity(dto.getStockQuantity());
        product.setProductRatings(new ArrayList<>(Arrays.asList()));

        product.setIsInOffer(false);
        product.setOfferDiscountPercentage(BigDecimal.valueOf(0.0));
        product.setOfferExpirationDateTime(null);

        Brand brand = brandRepository.findById(dto.getBrand())
            .orElseThrow(() -> new ResourceNotFoundException("Marca de Produto não encontrada"));
        product.setBrand(brand);

        dto.getImages().forEach(image -> product.getImages().add(generateBase64ImageAndSave(image)));

        dto.getCategories().forEach(categoryId -> {
            Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
            product.getCategories().add(category);
        });

        productRepository.save(product);
        product.getImages().forEach(image -> {
            image.setProduct(product);
            productImageRepository.save(image);
        });

    }

    @Override
    @Transactional
    public void updateProduct(Long id, ProductRequestDTO dto) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        double price = Double.parseDouble(dto.getPrice().replace(",", "."));
        product.setPrice(BigDecimal.valueOf(price));
        product.setStockQuantity(dto.getStockQuantity());
        Brand brand = brandRepository.findById(dto.getBrand())
            .orElseThrow(() -> new ResourceNotFoundException("Marca de Produto não encontrada"));
        product.setBrand(brand);

        if (dto.getIsInOffer()) {
            product.setIsInOffer(true);
            product.setOfferDiscountPercentage(dto.getOfferDiscountPercentage());
            if (dto.getOfferExpirationDateTime() != null) {
                product.setOfferExpirationDateTime(dto.getOfferExpirationDateTime());
            }
        }
        else {
            product.setIsInOffer(false);
            product.setOfferDiscountPercentage(BigDecimal.valueOf(0.0));
            product.setOfferExpirationDateTime(null);
        }

        dto.getImages().forEach(image -> {
            if (!image.isEmpty()) {
                ProductImage productImage = generateBase64ImageAndSave(image);
                product.getImages().add(productImage);
                
                productImage.setProduct(product);
                productImageRepository.save(productImage);
            }
        });
        product.getCategories().clear();
        dto.getCategories().forEach(categoryId -> {
            Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
            product.getCategories().add(category);
        });
        productRepository.save(product);
    }

    @Transactional
    private ProductImage generateBase64ImageAndSave(MultipartFile image) {
        try {
            String base64Prefix = "data:image/png;base64,";
            String imageBase64 = base64Prefix + Base64.getEncoder().encodeToString(image.getBytes());

            ProductImage productImage = new ProductImage();
            productImage.setImage(imageBase64);
            productImageRepository.save(productImage);

            return productImage;
        }
        catch (IOException e) {
            throw new BadRequestException("Ocorreu um erro ao fazer o upload da imagem");
        }
    }

    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto não encontrado");
        }
        productRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void increaseProductStockQuantity(Long id, Integer quantity) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        product.increaseStockQuantity(quantity);
        productRepository.save(product);
    }

    @Override
    @Transactional
    public void decreaseProductStockQuantity(Long id, Integer quantity) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        product.decreaseStockQuantity(quantity);
        productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductImageDTO> findProductImagesByProductId(Long productId) {
        return productImageRepository.findProductImagesByProductId(productId)
            .stream().map(ProductImageDTO::new).toList();
    }

    @Override
    @Transactional
    public void deleteProductImageById(Long id) {
        if (!productImageRepository.existsById(id)) {
            throw new ResourceNotFoundException("Imagem não encontrada");
        }
        productImageRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductGridDTO> findAllForGridPaginated(String name, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(name,pageable)
            .map(ProductGridDTO::new);
    }

    @Override
    @Transactional
    public ProductDetailDTO findByIdForProductDetail(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        product.setViews(product.getViews() + 1);
        return new ProductDetailDTO(product);
    }

    @SuppressWarnings("unchecked")
    @Transactional(readOnly = true)
    public Page<ProductGridDTO> findAllForGridPaginatedAndFiltered(List<Long> categoriesIds, List<Long> brandsIds,Integer minPrice, Integer maxPrice, String name, Pageable pageable){
        Page<ProductProjection> page = productRepository.searchProducts(
            categoriesIds != null ? categoriesIds : Arrays.asList(),
            brandsIds != null ? brandsIds : Arrays.asList(),
            minPrice != null ? BigDecimal.valueOf(minPrice) : BigDecimal.ZERO,
            maxPrice != null ? BigDecimal.valueOf(maxPrice) : productRepository.findMaxPrice(), 
            name.trim(), pageable);

        List<Long> productIds = page.map(ProductProjection::getId).toList();
        List<Product> entities = productRepository.searchProductsWithCategories(productIds);
        entities = (List<Product>) Utils.replace(page.getContent(), entities);

        List<ProductGridDTO> dtos = entities.stream().map(ProductGridDTO::new).toList();
        return new PageImpl<>(dtos,page.getPageable(),page.getTotalElements());
    }

    @Transactional
    @Scheduled(fixedRate = 30000)
    public void updateProductsWithExpirationDateTimeExpired(){
        productRepository.findAll().stream()
            .filter(product -> product.getIsInOffer() && product.getOfferExpirationDateTime().isBefore(LocalDateTime.now()))
            .forEach(product -> {
                product.setIsInOffer(false);
                product.setOfferDiscountPercentage(BigDecimal.valueOf(0.0));
                product.setOfferExpirationDateTime(null);
                productRepository.save(product);
            });
    }

    @Override
    @Transactional
    public void rateProduct(Long productId, ProductRatingRequestDTO dto) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        User user = authService.getConnectedUser();
        ProductRating productRating = new ProductRating();
        productRating.setUser(user);
        productRating.setProduct(product);
        productRating.setRating(dto.getRating());
        productRating.setDescription(dto.getDescription());
        productRatingRepository.save(productRating);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductRatingResponseDTO> getProductRatingsByProductId(Long id) {
        Product product = productRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        return product.getProductRatings().stream().map(ProductRatingResponseDTO::new).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductGridDTO> getProductsInOffer() {
        return productRepository.findProductsInOffer().stream().map(ProductGridDTO::new).toList();
    }
}
