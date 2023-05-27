package com.cfa.jobs.jobexample.RPW;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.core.io.FileSystemResource;

public class ItemFileReaderSimple extends FlatFileItemReader<String> {  

   public ItemFileReaderSimple(String inputFile) {
      super();
      this.setResource(new FileSystemResource(inputFile));
      this.setLineMapper((line, lineNumber) -> line);
   }

}

