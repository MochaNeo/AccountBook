package com.example.accountbook.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="Category")
@Getter
@Setter
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column
  @NotNull
  private long id;

  @Column(length = 50, nullable = false)
  @NotBlank
  private String categoryName;
}
