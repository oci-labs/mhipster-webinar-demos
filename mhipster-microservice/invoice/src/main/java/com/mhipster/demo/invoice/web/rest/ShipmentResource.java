package com.mhipster.demo.invoice.web.rest;

import com.mhipster.demo.invoice.domain.Shipment;
import com.mhipster.demo.invoice.repository.ShipmentRepository;
import com.mhipster.demo.invoice.web.rest.errors.BadRequestAlertException;

import com.mhipster.demo.invoice.util.HeaderUtil;
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
 * REST controller for managing {@link com.mhipster.demo.invoice.domain.Shipment}.
 */
@Controller("/api")
public class ShipmentResource {

    private final Logger log = LoggerFactory.getLogger(ShipmentResource.class);

    private static final String ENTITY_NAME = "invoiceShipment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ShipmentRepository shipmentRepository;

    public ShipmentResource(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    /**
     * {@code POST  /shipments} : Create a new shipment.
     *
     * @param shipment the shipment to create.
     * @return the {@link HttpResponse} with status {@code 201 (Created)} and with body the new shipment, or with status {@code 400 (Bad Request)} if the shipment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Post("/shipments")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<Shipment> createShipment(@Body Shipment shipment) throws URISyntaxException {
        log.debug("REST request to save Shipment : {}", shipment);
        if (shipment.getId() != null) {
            throw new BadRequestAlertException("A new shipment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Shipment result = shipmentRepository.save(shipment);
        URI location = new URI("/api/shipments/" + result.getId());
        return HttpResponse.created(result).headers(headers -> {
            headers.location(location);
            HeaderUtil.createEntityCreationAlert(headers, applicationName, true, ENTITY_NAME, result.getId().toString());
        });
    }

    /**
     * {@code PUT  /shipments} : Updates an existing shipment.
     *
     * @param shipment the shipment to update.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the updated shipment,
     * or with status {@code 400 (Bad Request)} if the shipment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the shipment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @Put("/shipments")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse<Shipment> updateShipment(@Body Shipment shipment) throws URISyntaxException {
        log.debug("REST request to update Shipment : {}", shipment);
        if (shipment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Shipment result = shipmentRepository.update(shipment);
        return HttpResponse.ok(result).headers(headers ->
            HeaderUtil.createEntityUpdateAlert(headers, applicationName, true, ENTITY_NAME, shipment.getId().toString()));
    }

    /**
     * {@code GET  /shipments} : get all the shipments.
     *
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and the list of shipments in body.
     */
    @Get("/shipments")
    @ExecuteOn(TaskExecutors.IO)
    public Iterable<Shipment> getAllShipments(HttpRequest request) {
        log.debug("REST request to get all Shipments");
        return shipmentRepository.findAll();
    }

    /**
     * {@code GET  /shipments/:id} : get the "id" shipment.
     *
     * @param id the id of the shipment to retrieve.
     * @return the {@link HttpResponse} with status {@code 200 (OK)} and with body the shipment, or with status {@code 404 (Not Found)}.
     */
    @Get("/shipments/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public Optional<Shipment> getShipment(@PathVariable Long id) {
        log.debug("REST request to get Shipment : {}", id);
        
        return shipmentRepository.findById(id);
    }

    /**
     * {@code DELETE  /shipments/:id} : delete the "id" shipment.
     *
     * @param id the id of the shipment to delete.
     * @return the {@link HttpResponse} with status {@code 204 (NO_CONTENT)}.
     */
    @Delete("/shipments/{id}")
    @ExecuteOn(TaskExecutors.IO)
    public HttpResponse deleteShipment(@PathVariable Long id) {
        log.debug("REST request to delete Shipment : {}", id);
        shipmentRepository.deleteById(id);
        return HttpResponse.noContent().headers(headers -> HeaderUtil.createEntityDeletionAlert(headers, applicationName, true, ENTITY_NAME, id.toString()));
    }
}
