package com.cfa.objects.lettre;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "lettre")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Lettre implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;


  @Column(name = "message")
  private String message;

  @Column(name = "creationDate")
  private String creationDate;

  @Column(name = "treatmentDate")
  private String treatmentDate;

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || Hibernate.getClass(this) != Hibernate.getClass(obj)) {
      return false;
    }
    final Lettre lettre = (Lettre) obj;
    return id != null && Objects.equals(id, lettre.id);
  }

}