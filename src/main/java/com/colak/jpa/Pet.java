package com.colak.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.util.Date;

@Entity
@Cache(region = "petCache", usage = CacheConcurrencyStrategy.READ_WRITE)

@Getter
@Setter

@AllArgsConstructor
@NoArgsConstructor
public class Pet {

    @Id
    private Long id;

    private String name;

}
