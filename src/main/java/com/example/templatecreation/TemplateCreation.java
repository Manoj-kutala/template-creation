package com.example.templatecreation;

import java.io.*;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class TemplateCreation {

    public static void main(String args[]) throws IOException, InterruptedException {
        new TemplateCreation().func("/Users/manoj.kutala/Desktop/template-creation/src/main/resources/Get_Webhooks_Request_fields.xlsx");
    }

    public void func(String fname) throws IOException, InterruptedException {
        File f = new File(fname);
        FileInputStream fis = new FileInputStream(f);
        XSSFWorkbook Workbook = new XSSFWorkbook(fis);
        XSSFSheet workSheet = Workbook.getSheetAt(8);
        String sheetname =workSheet.getSheetName();

        //create a folder with api name
        String outputfolder = "/Users/manoj.kutala/Desktop/template-creation/target/templates/"+sheetname;
        File f1 = new File(outputfolder);
        boolean bool = f1.mkdir();
        if(bool){
            System.out.println(sheetname+" Folder is created successfully");
        }else{
            System.out.println("Error Found! Folder already exists...");
        }

        //create yubi request and response templates
        String fileName1 = outputfolder+"/YUBIRequest.txt";
        FileWriter file1 = new FileWriter(fileName1);
        String fileName2 = outputfolder+"/YUBIResponse.txt";
        FileWriter file2 = new FileWriter(fileName2);

        file1.write("package DCG;\n\nimport java.util.Date;\n\npublic class [(${api_name})]Request {\n");
        for (int i = 1; i <19; i++) {
            XSSFRow row = workSheet.getRow(i);
            String fieldName = row.getCell(2).toString();
            String fieldType = row.getCell(4).toString();
            String mandatorycontent = "\n\tprivate "+fieldType+" [(${"+fieldName+"})];\n" +
                    "\tpublic void set[(${"+fieldName+"})]("+fieldType+" "+fieldName+"){\n" +
                    "\t\tthis.[(${"+fieldName+"})] = "+fieldName+";\n" +
                    "\t}\n" +
                    "\tpublic "+fieldType+" get"+fieldName+"(){\n" +
                    "\t\treturn this.[(${"+fieldName+"})];\n" +
                    "\t}\n";
            String optionalcontent = "\n\t[# th:if = \"${"+fieldName+" != null}\"]" + mandatorycontent + "\t[/]\n";
            if((row.getCell(5).toString()).equals("Mandatory")){
                file1.write(mandatorycontent);
            }
            else{
                file1.write(optionalcontent);
            }

        }
        file1.write("}");
        file1.close();


        file2.write("package DCG;\nimport java.util.Date;\n\npublic class [(${api_name})]Response {\n");
        for (int i = 28; i <28; i++) {
            XSSFRow row = workSheet.getRow(i);
            String fieldName = row.getCell(2).toString();
            String fieldType = row.getCell(4).toString();
            String mandatorycontent = "\n\tprivate "+fieldType+" "+fieldName+";\n" +
                    "\tpublic void set"+fieldName+"("+fieldType+" "+fieldName+"){\n" +
                    "\t\tthis."+fieldName+" = "+fieldName+";\n" +
                    "\t}\n" +
                    "\tpublic "+fieldType+" get[(${"+fieldName+"})](){\n" +
                    "\t\treturn this."+fieldName+";\n" +
                    "\t}\n";
            String optionalcontent = "\n\t[# th:if = \"${"+fieldName+" != null}\"]" + mandatorycontent + "\t[/]\n";
            if((row.getCell(5).toString()).equals("Mandatory")){
                file2.write(mandatorycontent);
            }
            else{
                file2.write(optionalcontent);
            }

        }
        file2.write("}");
        file2.close();

    }
}
