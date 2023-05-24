package com.cfa.jobs.jobexample.RPW;


import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.UnexpectedInputException;

import com.hazelcast.internal.json.ParseException;  

import java.util.Arrays;
import java.util.List;

public class ItemReaderSimple implements ItemReader<String> {  
   public final List<String> items;
   public int index = 0;

   public ItemReaderSimple(List<String> items) {
      this.items = items;
   }

   public ItemReaderSimple() {
      this.items = Arrays.asList("Item 4", "Item 5", "Item 6");
   }

   @Override
   public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException{      
      System.out.println("Read..." ); 
      if (this.items.size() != this.index){
         return this.items.get(this.index++);
      }else{
         index = 0;
         return null;
      }
   }
} 
