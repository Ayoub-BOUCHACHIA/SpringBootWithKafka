package com.cfa.objects.lettre;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LettreRepository extends JpaRepository<Lettre, Integer> {

  List<Lettre> getByMessage(final String message);

}
