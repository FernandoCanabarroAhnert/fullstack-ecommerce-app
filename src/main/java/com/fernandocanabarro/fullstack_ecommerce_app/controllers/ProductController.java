package com.fernandocanabarro.fullstack_ecommerce_app.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.fernandocanabarro.fullstack_ecommerce_app.dtos.BrandResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.CategoryResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductDetailDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductGridDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductRatingRequestDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.dtos.ProductRatingResponseDTO;
import com.fernandocanabarro.fullstack_ecommerce_app.services.BrandService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.CategoryService;
import com.fernandocanabarro.fullstack_ecommerce_app.services.ProductService;

import jakarta.validation.Valid;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;

    @GetMapping("/products-grid")
    public String productsGrid(Model model) {
        Pageable pageable = PageRequest.of(0,12);
        String name = "";
        Page<ProductGridDTO> pageResponse = productService.findAllForGridPaginated(name, pageable);

        int totalPageItems = pageResponse.getNumberOfElements();
        long totalItems = pageResponse.getTotalElements();
        int currentPage = 1;
        int totalPages = pageResponse.getTotalPages();
        String sort = "id";
        String sortDirection = "asc";

        List<CategoryResponseDTO> categories = categoryService.findAll();
        List<BrandResponseDTO> brands = brandService.findAll();

        model.addAttribute("products", pageResponse.getContent());
        model.addAttribute("totalPageItems", totalPageItems);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sort", sort);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("name", name);
        model.addAttribute("categories", categories);
        model.addAttribute("brands", brands);
        model.addAttribute("minPrice", 0);
        model.addAttribute("maxPrice", 50000);
        model.addAttribute("queryString", "");

        return "site/shop";
    }

    @GetMapping("/products-grid/paginated")
    public String productsGridPaginated(@RequestParam String page,
                                    @RequestParam String sort,
                                    @RequestParam String sortDirection,
                                    @RequestParam String name,
                                    @RequestParam(required = false) List<Long> categories,
                                    @RequestParam(required = false) List<Long> brands,
                                    @RequestParam(required = false) Integer minPrice,
                                    @RequestParam(required = false) Integer maxPrice,
                                    Model model) {
        Pageable pageable = sortDirection.equals("asc")
            ? PageRequest.of(Integer.parseInt(page) - 1,12).withSort(Sort.by(sort).ascending())
            : PageRequest.of(Integer.parseInt(page) - 1,12).withSort(Sort.by(sort).descending());
        Page<ProductGridDTO> pageResponse = productService.findAllForGridPaginatedAndFiltered(categories, brands, minPrice, maxPrice, name, pageable);

        int totalPageItems = pageResponse.getNumberOfElements();
        long totalItems = pageResponse.getTotalElements();
        int currentPage = Integer.parseInt(page);
        int totalPages = pageResponse.getTotalPages();

        List<CategoryResponseDTO> categoriesInput = categoryService.findAll();
        List<BrandResponseDTO> brandsInput = brandService.findAll();

        String categoryParams = (categories != null && !categories.isEmpty()) 
                ? categories.stream().map(id -> "categories=" + id).collect(Collectors.joining("&")) 
                : "";
        String brandParams = (brands != null && !brands.isEmpty()) 
                ? brands.stream().map(id -> "brands=" + id).collect(Collectors.joining("&")) 
                : "";
        String queryString = (categoryParams + "&" + brandParams).replaceAll("^&|&$", "");

        model.addAttribute("queryString", queryString);
        model.addAttribute("products", pageResponse.getContent());
        model.addAttribute("totalPageItems", totalPageItems);
        model.addAttribute("totalItems", totalItems);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("sort", sort);
        model.addAttribute("sortDirection", sortDirection);
        model.addAttribute("reverseSortDirection", sortDirection.equals("asc") ? "desc" : "asc");
        model.addAttribute("name", name);
        model.addAttribute("categories", categoriesInput);
        model.addAttribute("brands", brandsInput);
        model.addAttribute("selectedCategories", categories);
        model.addAttribute("selectedBrands", brands);
        model.addAttribute("minPrice", minPrice != null ? minPrice : 0);
        model.addAttribute("maxPrice", maxPrice != null ? maxPrice : 50000);

        return "site/shop";
    }

    @GetMapping("/products/{id}")
    public String productDetail(@PathVariable Long id,Model model) {
        ProductDetailDTO product = productService.findByIdForProductDetail(id);
        ProductRatingRequestDTO ratingDto = new ProductRatingRequestDTO();
        List<ProductRatingResponseDTO> ratings = productService.getProductRatingsByProductId(id);
        model.addAttribute("product",product);
        model.addAttribute("ratingDto", ratingDto);
        model.addAttribute("ratings", ratings);
        return "site/product-detail";
    }

    @PostMapping("/products/{id}/rate")
    public String rateProduct(@Valid @ModelAttribute("ratingDto") ProductRatingRequestDTO ratingDto, BindingResult bindingResult, @PathVariable Long id, Model model) {
        if (bindingResult.hasErrors()) {
            ProductDetailDTO product = productService.findByIdForProductDetail(id);
            List<ProductRatingResponseDTO> ratings = productService.getProductRatingsByProductId(id);
            model.addAttribute("ratingDto",ratingDto);
            model.addAttribute("product",product);
            model.addAttribute("ratings", ratings);
            return "site/product-detail";
        }
        productService.rateProduct(id, ratingDto);
        ProductDetailDTO product = productService.findByIdForProductDetail(id);
        List<ProductRatingResponseDTO> ratings = productService.getProductRatingsByProductId(id);
        model.addAttribute("ratingDto",new ProductRatingRequestDTO());
        model.addAttribute("product",product);
        model.addAttribute("ratings", ratings);
        return "site/product-detail";
    }

}
