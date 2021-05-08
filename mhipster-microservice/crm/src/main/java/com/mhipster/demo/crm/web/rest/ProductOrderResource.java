package com.mhipster.demo.crm.web.rest;

import com.mhipster.demo.crm.domain.ProductOrder;
import com.mhipster.demo.crm.repository.ProductOrderRepository;
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
 * REST controller for managing {@link com.mhipster.demo.crm.domain.ProductOrder}.
 */
@Controller("/api")
public class ProductOrderResource {

    private final Logger log = LoggerFactory.getLogger(ProductOrderResource.class);

    private static final String ENTITY_NAME = "crmProductOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProductOrderRepository productOrderRepository;

    public ProductOrderResource(ProductOrderRepository productOrderRepository) {
        this.productOrderRepository = productOrderRepository;
    }

    /**
     * {@code POST  /product-orders} : Create a new productOrder.
     *
     * @param productOrder the productOrder to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new productOrder, or with status {@code 400 (Bad Request)} if the productOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/product-orders")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<ProductOrder> createProductOrder(@Body ProductOrder productOrder) throws URISyntaxException {
        log.debug("REST request to save ProductOrder : {}", productOrder);
        if (productOrder.getId() != null) {
            throw new BadRequestAlertException("A new productOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProductOrder result = productOrderRepository.save(productOrder);
        URI location = new URI("/api/product-orders/" + result.getId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
            HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
        });
    }

    /**
     * {@code PUT  /product-orders} : Updates an existing productOrder.
     *
     * @param productOrder the productOrder to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated productOrder,
     * or with status {@code 400 (Bad Request)} if the productOrder is not valid,
     * or with status {@code 500 (Internal Server Error)} if the productOrder couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/product-orders")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<ProductOrder> updateProductOrder(@Body ProductOrder productOrder) throws URISyntaxException {
        log.debug("REST request to update ProductOrder : {}", productOrder);
        if (productOrder.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ProductOrder result = productOrderRepository.update(productOrder);
        return HttpResponse.ok(result).headers(headers ->
            HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, productOrder.getId().toString()));
    }

    /**
     * {@code GET  /product-orders} : get all the productOrders.
     *
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of productOrders in body.
     */
    @Get("/product-orders")
    @ExecuteOn(TaskExecutors.IO)
    public Iterable<ProductOrder> getAllProductOrders(HttpRequest request) {
        log.debug("REST request to get all ProductOrders");
        return productOrderRepository.findAll();
    }

    /**
     * {@code GET  /product-orders/:id} : get the "id" productOrder.
     *
     * @param id the id of the productOrder to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the productOrder, or with status {@code 404 (Not Found)}.
     */
    @Get("/product-orders/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<ProductOrder> getProductOrder(@PathVariable Long id) {
        log.debug("REST request to get ProductOrder : {}", id);
        
        return productOrderRepository.findById(id);
    }

    /**
     * {@code DELETE  /product-orders/:id} : delete the "id" productOrder.
     *
     * @param id the id of the productOrder to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/product-orders/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteProductOrder(@PathVariable Long id) {
        log.debug("REST request to delete ProductOrder : {}", id);
        productOrderRepository.deleteById(id);
        return HttpResponse.noContent().headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
