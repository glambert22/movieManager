package com.secureally.demo.dao.entities;

import com.secureally.demo.utils.annotations.Year;
import com.secureally.demo.utils.dto.groups.OnUpdate;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@Entity
@Table(name = "movies")
public class Movies implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @NotNull(groups = {OnUpdate.class})
    Integer id;

    @NotNull
    @NotEmpty
    @Column(name = "name", nullable = false)
    String name;

    @NotBlank(message = "genre required")
    @Column(name = "genre", nullable = false)
    String genre;

    @Year
    @Column(name = "year_released", nullable = false)
    Integer yearReleased;

    @NotNull
    @Column(name = "rating", nullable = true)
    Float rating;
}
