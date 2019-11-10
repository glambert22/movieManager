package com.secureally.demo.dao.repository;

import com.secureally.demo.dao.entities.Movies;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovieRepository extends JpaRepository<Movies,Integer> {
}
