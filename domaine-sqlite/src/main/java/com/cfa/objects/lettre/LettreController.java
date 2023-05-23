package com.cfa.objects.lettre;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;

@RestController
@RequestMapping(path = "/lettre")
@RequiredArgsConstructor
public class LettreController {

  private final LettreRepository repository;

  // curl -X GET -H "Content-Type: application/json" http://127.0.0.1:9623/lettre/getAll/
  @GetMapping("/getAll")
  public Collection<Lettre> getAll() {
    return repository.findAll();
  }

  // curl -X GET -H "Content-Type: application/json" http://127.0.0.1:9623/lettre/getMessages?message=Example
  @GetMapping("/getMessages")
  public Collection<Lettre> getMessages(@RequestParam(value = "message", defaultValue = "") String message) {
    return repository.getByMessage(message);
  }

  /*
  curl -X POST -H "Content-Type: application/json" -d '{
          "message": "Example Lettre",
          "creationDate": "12/06/2012",
          "treatmentDate": "12/06/2022"
  }' http://127.0.0.1:9623/lettre/save-lettre/
   */
  @PostMapping("/save-lettre")
  public Lettre addLettre(@RequestBody Lettre lettre) {
      return repository.save(lettre);
  }

  /*
  curl -X PUT -H "Content-Type: application/json" -d '{
          "message": "Example Lettre 2"
  }' http://127.0.0.1:9623/lettre/update-lettre/1
   */
  @PutMapping("/update-lettre/{id}")
  public Lettre updateLettre(@PathVariable(value = "id") int id, @RequestBody Lettre newLettre) {
      return repository.findById(id)
      .map(lettre -> {
  
        if( newLettre.getMessage() != null ){ 
          lettre.setMessage(newLettre.getMessage()); 
        }else{ 
          lettre.setMessage(lettre.getCreationDate());        
        }
        if( newLettre.getCreationDate() != null ){ 
          lettre.setCreationDate(newLettre.getCreationDate()); 
        }else{ 
          lettre.setCreationDate(lettre.getCreationDate());
        }
        if( newLettre.getTreatmentDate() != null ){
           lettre.setTreatmentDate(newLettre.getTreatmentDate()); 
        }else{
          lettre.setTreatmentDate(lettre.getTreatmentDate()); 
        }
        
        return repository.save(lettre);
      })
      .orElseGet(() -> {
          return repository.save(newLettre);
      });
  }
}
