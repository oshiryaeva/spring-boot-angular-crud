package com.shiryaeva.wyrgorod.controller;

import com.shiryaeva.wyrgorod.config.HeaderUtil;
import com.shiryaeva.wyrgorod.config.PaginationUtil;
import com.shiryaeva.wyrgorod.config.ResponseUtil;
import com.shiryaeva.wyrgorod.exception.BadRequestAlertException;
import com.shiryaeva.wyrgorod.model.Publisher;
import com.shiryaeva.wyrgorod.model.Publisher;
import com.shiryaeva.wyrgorod.repository.PublisherRepository;
import com.shiryaeva.wyrgorod.repository.PublisherRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@Transactional
@CrossOrigin(origins = "http://localhost:9000", allowedHeaders = "*")
public class PublisherController {

    private static final String ENTITY_NAME = "publisher";

    private String applicationName = "publisher";

    private final PublisherRepository publisherRepository;

    public PublisherController(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    /**
     * {@code POST  /publishers} : Create a new publisher.
     *
     * @param publisher the publisher to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new publisher, or with status {@code 400 (Bad Request)} if the publisher has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/publishers")
    public ResponseEntity<Publisher> createPublisher(@Valid @RequestBody Publisher publisher) throws URISyntaxException {
        if (publisher.getId() != null) {
            throw new BadRequestAlertException("A new publisher cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Publisher result = publisherRepository.save(publisher);
        return ResponseEntity
                .created(new URI("/api/publishers/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /publishers/:id} : Updates an existing publisher.
     *
     * @param id the id of the publisher to save.
     * @param publisher the publisher to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated publisher,
     * or with status {@code 400 (Bad Request)} if the publisher is not valid,
     * or with status {@code 500 (Internal Server Error)} if the publisher couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/publishers/{id}")
    public ResponseEntity<Publisher> updatePublisher(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Publisher publisher)
            throws URISyntaxException {
        if (publisher.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, publisher.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!publisherRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Publisher result = publisherRepository.save(publisher);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, publisher.getId().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /publishers/:id} : Partial updates given fields of an existing publisher, field will ignore if it is null
     *
     * @param id the id of the publisher to save.
     * @param publisher the publisher to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated publisher,
     * or with status {@code 400 (Bad Request)} if the publisher is not valid,
     * or with status {@code 404 (Not Found)} if the publisher is not found,
     * or with status {@code 500 (Internal Server Error)} if the publisher couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/publishers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Publisher> partialUpdatePublisher(
            @PathVariable(value = "id", required = false) final Long id,
            @NotNull @RequestBody Publisher publisher
    ) throws URISyntaxException {
        if (publisher.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, publisher.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!publisherRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Publisher> result = publisherRepository
                .findById(publisher.getId())
                .map(existingPublisher -> {
                    if (publisher.getName() != null) {
                        existingPublisher.setName(publisher.getName());
                    }

                    return existingPublisher;
                })
                .map(publisherRepository::save);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, publisher.getId().toString())
        );
    }

    /**
     * {@code GET  /publishers} : get all the publishers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of publishers in body.
     */
    @GetMapping("/publishers")
    public ResponseEntity<List<Publisher>> getAllPublishers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        Page<Publisher> page = publisherRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /publishers/:id} : get the "id" publisher.
     *
     * @param id the id of the publisher to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the publisher, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/publishers/{id}")
    public ResponseEntity<Publisher> getPublisher(@PathVariable Long id) {
        Optional<Publisher> publisher = publisherRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(publisher);
    }

    /**
     * {@code DELETE  /publishers/:id} : delete the "id" publisher.
     *
     * @param id the id of the publisher to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/publishers/{id}")
    public ResponseEntity<Void> deletePublisher(@PathVariable Long id) {
        publisherRepository.deleteById(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                .build();
    }
}