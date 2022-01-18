package com.shiryaeva.wyrgorod.controller;

import com.shiryaeva.wyrgorod.config.HeaderUtil;
import com.shiryaeva.wyrgorod.config.PaginationUtil;
import com.shiryaeva.wyrgorod.config.ResponseUtil;
import com.shiryaeva.wyrgorod.exception.BadRequestAlertException;
import com.shiryaeva.wyrgorod.model.NewsItem;
import com.shiryaeva.wyrgorod.repository.NewsItemRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
public class NewsItemController {

    private static final String ENTITY_NAME = "newsItem";

    private String applicationName = "newsItem";

    private final NewsItemRepository newsItemRepository;

    public NewsItemController(NewsItemRepository newsItemRepository) {
        this.newsItemRepository = newsItemRepository;
    }

    /**
     * {@code POST  /newsItems} : Create a new newsItem.
     *
     * @param newsItem the newsItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new newsItem, or with status {@code 400 (Bad Request)} if the newsItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/news-items")
    public ResponseEntity<NewsItem> createNewsItem(@Valid @RequestBody NewsItem newsItem) throws URISyntaxException {
        if (newsItem.getId() != null) {
            throw new BadRequestAlertException("A new newsItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NewsItem result = newsItemRepository.save(newsItem);
        return ResponseEntity
                .created(new URI("/api/newsItems/" + result.getId()))
                .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
                .body(result);
    }

    /**
     * {@code PUT  /newsItems/:id} : Updates an existing newsItem.
     *
     * @param id the id of the newsItem to save.
     * @param newsItem the newsItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated newsItem,
     * or with status {@code 400 (Bad Request)} if the newsItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the newsItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/news-items/{id}")
    public ResponseEntity<NewsItem> updateNewsItem(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody NewsItem newsItem)
            throws URISyntaxException {
        if (newsItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, newsItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!newsItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NewsItem result = newsItemRepository.save(newsItem);
        return ResponseEntity
                .ok()
                .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, newsItem.getId().toString()))
                .body(result);
    }

    /**
     * {@code PATCH  /newsItems/:id} : Partial updates given fields of an existing newsItem, field will ignore if it is null
     *
     * @param id the id of the newsItem to save.
     * @param newsItem the newsItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated newsItem,
     * or with status {@code 400 (Bad Request)} if the newsItem is not valid,
     * or with status {@code 404 (Not Found)} if the newsItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the newsItem couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/news-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NewsItem> partialUpdateNewsItem(
            @PathVariable(value = "id", required = false) final Long id,
            @NotNull @RequestBody NewsItem newsItem
    ) throws URISyntaxException {
        if (newsItem.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, newsItem.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!newsItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NewsItem> result = newsItemRepository
                .findById(newsItem.getId())
                .map(existingNewsItem -> {
                    if (newsItem.getTitle() != null) {
                        existingNewsItem.setTitle(newsItem.getTitle());
                    }

                    return existingNewsItem;
                })
                .map(newsItemRepository::save);

        return ResponseUtil.wrapOrNotFound(
                result,
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, newsItem.getId().toString())
        );
    }

    /**
     * {@code GET  /newsItems} : get all the newsItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of newsItems in body.
     */
    @GetMapping("/news-items")
    public ResponseEntity<List<NewsItem>> getAllNewsItems(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        Page<NewsItem> page = newsItemRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /newsItems/:id} : get the "id" newsItem.
     *
     * @param id the id of the news-items to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the newsItem, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/newsItems/{id}")
    public ResponseEntity<NewsItem> getNewsItem(@PathVariable Long id) {
        Optional<NewsItem> newsItem = newsItemRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(newsItem);
    }

    /**
     * {@code DELETE  /news-items/:id} : delete the "id" newsItem.
     *
     * @param id the id of the newsItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/news-items/{id}")
    public ResponseEntity<Void> deleteNewsItem(@PathVariable Long id) {
        newsItemRepository.deleteById(id);
        return ResponseEntity
                .noContent()
                .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
                .build();
    }
}