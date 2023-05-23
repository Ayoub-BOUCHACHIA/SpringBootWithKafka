package com.cfa.objects.person;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping(path = "/person")
@RequiredArgsConstructor
public class PersonController {

  private final PersonRepository repository;

  @GetMapping("/getAll")
  public Collection<Person> getAll() {
    return repository.findAll();
  }

}
