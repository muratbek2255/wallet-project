package com.example.wallet.repository;

import com.example.wallet.entity.Favour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FavourRepository extends JpaRepository<Favour, Integer> {

    @Query(value = "SELECT * FROM wallet.favours WHERE wallet.favours.id = ?1", nativeQuery = true)
    Favour getByFavourId(@Param("id") int id);
}
