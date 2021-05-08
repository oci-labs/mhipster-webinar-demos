package com.mhipster.demo.crm.web.rest;

import com.mhipster.demo.crm.domain.Product;
import com.mhipster.demo.crm.repository.ProductRepository;
import com.mhipster.demo.crm.web.rest.errors.BadRequestAlertException;

import com.mhipster.demo.crm.util.HeaderUtil;
import io.micronaut.context.annotation.Value;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.scheduling.TaskExecutors;
import io.micronaut.scheduling.annotation.ExecuteOn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mhipster.demo.crm.domain.Product}.
 */
@Controller("/api")
public class ProductResource {

    private final Logger log = LoggerFactory.getLogger(ProductResource.class);

    private static final String ENTITY_NAME = "crmProduct";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductRepository productRepository;

    public ProductResource(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * {@code POST  /products} : Create a new product.
     *
     * @param product the product to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new product, or with status {@code 400 (Bad Request)} if the product has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/products")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<Product> createProduct(@Body Product product) throws URISyntaxException {
        log.debug("REST request to save Product : {}", product);
        if (product.getId() != null) {
            throw new BadRequestAlertException("A new product cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Product result = productRepository.save(product);
        URI location = new URI("/api/products/" + result.getId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
            HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
        });
    }

    /**
     * {@code PUT  /products} : Updates an existing product.
     *
     * @param product the product to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated product,
     * or with status {@code 400 (Bad Request)} if the product is not valid,
     * or with status {@code 500 (Internal Server Error)} if the product couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/products")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<Product> updateProduct(@Body Product product) throws URISyntaxException {
        log.debug("REST request to update Product : {}", product);
        if (product.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Product result = productRepository.update(product);
        return HttpResponse.ok(result).headers(headers ->
            HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, product.getId().toString()));
    }

    /**
     * {@code GET  /products} : get all the products.
     *
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of products in body.
     */
    @Get("/products")
    @ExecuteOn(TaskExecutors.IO)
    public Iterable<Product> getAllProducts(HttpRequest request) {
        log.debug("REST request to get all Products");
        return productRepository.findAll();
    }

    /**
     * {@code GET  /products/:id} : get the "id" product.
     *
     * @param id the id of the product to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the product, or with status {@code 404 (Not Found)}.
     */
    @Get("/products/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<Product> getProduct(@PathVariable Long id) {
        log.debug("REST request to get Product : {}", id);
        
        return productRepository.findById(id);
    }

    /**
     * {@code DELETE  /products/:id} : delete the "id" product.
     *
     * @param id the id of the product to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/products/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteProduct(@PathVariable Long id) {
        log.debug("REST request to delete Product : {}", id);
        productRepository.deleteById(id);
        return HttpResponse.noContent().headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
