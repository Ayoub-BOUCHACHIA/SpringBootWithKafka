package com.cfa.jobs.jobexample.RPW;

import org.springframework.batch.item.ItemWriter;

import com.cfa.objects.lettre.Lettre;  
import com.cfa.objects.lettre.LettreRepository;  
import java.util.List;


public class ItemWriterSimple implements ItemWriter<Lettre> {
      
   private final LettreRepository repository;

   public ItemWriterSimple(LettreRepository repository) {
      this.repository = repository;
   }

   @Override
   public void write(List<? extends Lettre> items) throws Exception {
      System.out.println("writing...");
      System.out.println(items);
      this.repository.saveAll(items);
      System.out.println("saved !");
   }
}


