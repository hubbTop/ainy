package com.hhdb.csadmin.common.csv.writer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 更多例子请参看 https://github.com/osiegmar/FastCSV
 * 
 * @author Tony
 * 
 */

public class CsvWrtieExample {

    /**
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
//	example1();
//	example2() ;
//	example3() ;
    	example4() ;
//    	example5() ;
    }

    public static void example1() throws IOException {
	File file = new File("foo1.csv");
	if(file.exists())file.delete();
	CsvWriter csvWriter = new CsvWriter();

	CsvAppender csvAppender = csvWriter.append(file, StandardCharsets.UTF_8);

	// header
	csvAppender.appendLine("", "header2");
	csvAppender.appendLine(null, null);

	// 1st line in one operation
	csvAppender.appendLine(null, "tony");

//	// 2nd line in split operations
//	csvAppender.appendField("张 \n\r, \"李四\"");
//	
//	char[] big=new char[2048];
//	StringBuffer sb=new StringBuffer();
//	for(int i=0;i<big.length;i++){
//	    sb.append("张 "+i);
//	}
//	
//	csvAppender.appendField(sb.toString());
//	csvAppender.endLine();
	csvAppender.close();

    }

    public static void example2() throws IOException {
	File file = new File("foo2.csv");
	if(file.exists())file.delete();
	CsvWriter csvWriter = new CsvWriter();

	final Collection<String[]> data = new ArrayList<>();
	data.add(new String[] { "header1", "header2" });
	data.add(new String[] { "value1", "value2" });

	csvWriter.write(file, StandardCharsets.UTF_8, data);
    }
    
    public static void example3() throws IOException {
	File file = new File("foo3.csv");
	if(file.exists())file.delete();
	CsvWriter csvWriter = new CsvWriter();
	
	csvWriter.setFieldSeparator(';');
	csvWriter.setTextDelimiter('\'');
	csvWriter.setLineDelimiter("\r\n".toCharArray());
	csvWriter.setAlwaysDelimitText(true);

	final Collection<String[]> data = new ArrayList<>();
	data.add(new String[] { "header1", "header2" });
	data.add(new String[] { "value1", "value2" });

	csvWriter.write(file, StandardCharsets.UTF_8, data);
    }
    
    public static void example4() throws IOException {
    	CsvWriter csvWriter = new CsvWriter();

    	final Collection<String[]> data = new ArrayList<>();
    	data.add(new String[] { "header1", "header2" });
    	data.add(new String[] { "value1", "value2" });

    	StringBuffer sbf = new StringBuffer();
    	csvWriter.writeSbf(sbf, data);
    	System.out.println(sbf.toString());
        }
        
        public static void example5() throws IOException {
    	CsvWriter csvWriter = new CsvWriter();
    	
    	csvWriter.setFieldSeparator(';');
    	csvWriter.setTextDelimiter('\'');
    	csvWriter.setLineDelimiter("\r\n".toCharArray());
    	csvWriter.setAlwaysDelimitText(true);

    	final Collection<String[]> data = new ArrayList<>();
    	data.add(new String[] { "header1", "header2" });
    	data.add(new String[] { "value1", "value2" });

    	StringBuffer sbf = new StringBuffer();
    	csvWriter.writeSbf(sbf, data);
    	System.out.println(sbf.toString());
        }

}
