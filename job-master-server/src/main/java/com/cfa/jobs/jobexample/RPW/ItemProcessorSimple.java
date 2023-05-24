package com.cfa.jobs.jobexample.RPW;


import org.springframework.batch.item.ItemProcessor;

import com.cfa.objects.lettre.Lettre;
import java.util.ArrayList;
import java.util.List;

public class ItemProcessorSimple implements ItemProcessor<String, Lettre> {  
   
   @Override 
   public Lettre process(String item) throws Exception {  
      System.out.println("Processing..." + item); 
      
      Lettre lettre = new Lettre();
      lettre.setMessage(item);
      lettre.setTreatmentDate("");
      lettre.setCreationDate("");
         
      return lettre; 
   } 
} 
