package com.example.accountbook.Entity;

import java.sql.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="Account")
@Getter
@Setter
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column
  @NotNull
  private long id;

  @Column(nullable = false)
  private boolean balance;

  @Column(length = 50, nullable = false)
  @NotBlank(message = "名前を入力してください")
  private String name;

  @Column(nullable = false)
  @NotNull(message = "値段を入力してください")
  @Min(value = 0, message = "値段は0円以上で入力してください")
    private Integer price;

  @Column(nullable = false)
  @NotNull(message = "日付を入力してください")
  private Date date;

  @ManyToOne
  @JoinColumn(name = "category_id")
  @NotNull(message = "カテゴリーを追加、選択してください。")
  private Category category;
}