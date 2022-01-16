package com.shiryaeva.wyrgorod.repository;

import com.shiryaeva.wyrgorod.model.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    Artist findByNameLikeIgnoreCase(String name);

    @Modifying
    @Query("update Artist u set u.name = :newName where u.name = :oldName")
    void updateName(@Param(value = "oldName") String oldName, @Param(value = "newName") String newName);

    long deleteByNameLikeIgnoreCase(String name);



}