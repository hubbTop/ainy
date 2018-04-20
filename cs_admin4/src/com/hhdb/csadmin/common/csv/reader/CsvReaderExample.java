package com.hhdb.csadmin.common.csv.reader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import com.hhdb.csadmin.common.csv.writer.CsvWrtieExample;



public class CsvReaderExample {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
	CsvWrtieExample.example1();
	File file = new File("foo1.csv");
	CsvReader csvReader = new CsvReader();
	CsvParser csvParser = csvReader.parse(file, StandardCharsets.UTF_8);
		 CsvRow row;
	    while ((row = csvParser.nextRow()) != null) {
	        System.out.println("Read line: " + row);
	        System.out.println("First column of line: " + row.getField(0));
	    }


    }
}
