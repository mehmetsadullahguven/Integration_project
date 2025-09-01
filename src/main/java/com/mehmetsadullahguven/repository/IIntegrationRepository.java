package com.mehmetsadullahguven.repository;

import com.mehmetsadullahguven.model.Integration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IIntegrationRepository extends CrudRepository<Integration, Long> {

    List<Integration> findAllBySlug(String slug);

    Optional<Integration> findBySlugAndR2sProductListId(String slug, String productListId);
}
