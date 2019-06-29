package com.programmer.gate;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

// based on the source from: https://www.programmergate.com/how-to-read-xml-file-in-java/
public class MyReadXMLWithDOM {

    //private static int fileProcessed = 0;

    public static void main( String[] args ) throws IOException
    {
        String rootPath = "D:\\work\\02-03.workspace-idea\\ReadXML";

        if (args.length>0 && StringUtils.isNotBlank(args[0])) {
            rootPath = args[0];
        }

        File file = new File(rootPath);

        if (!file.exists()) {
            System.out.println("Source folder [" + rootPath+ "] not exists." );
            return;
        }

        //如果处理路径的最后一个字符不是'\'，添加一个'\'
        if (!StringUtils.right(rootPath, 1).equals(File.separator)) {
            rootPath += File.separator;
        }

        long start = System.currentTimeMillis();
        int fileProcessed = processFiles(rootPath);
        long end = System.currentTimeMillis();

        long seconds = (end-start)/1000;
        long hour = seconds/3600;
        long minute = (seconds%3600)/60;
        long second = (seconds%3600)%60;

        String info = String.format("====ALL DONE===\n"
                        +"====time elapsed: %d hours, %d minutes, %d secondes。\n"
                        +"====folder processed: %d.",
                hour, minute, second,
                fileProcessed);

        System.out.println(info);
    }

    private static int processFiles(String rootPath) throws IOException{
        int count = 0;
        String defaultFileName = "output.csv";

        File outputFile = new File(rootPath + defaultFileName);
        if (outputFile.exists()) {
            outputFile.delete();
        }
        else {
            outputFile.createNewFile();
        }

        File dir = new File(rootPath);
        //仅处理xml文件
        File[] files = dir.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                String filename = pathname.getName().toLowerCase();
                if (filename.endsWith(".xml")) {
                    return true;
                }
                else {
                    return false;
                }
            }
        });

        if (files == null) {
            return 0;
        }

        for (int i=0; i<files.length; i++) {
            File sourcefile = files[i];

            //如果是文件夹
            if (sourcefile.isDirectory()) {
                continue;
            }
            //如果是文件
            else {
                processFile(sourcefile.getName(), outputFile);
                count++;
            }
        }

        return count;
    }

    private static void processFile(String filename, File outputFile) throws Exception{
        File xmlFile = new File(filename);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(xmlFile);

//        getStudentById(doc,"id", "2");
//        getAllStudents(doc);
//        getGraduatedStudents(doc, "graduated", "yes");
//        parseWholeXML(doc.getDocumentElement());

        //1、<test-record>\<header>\<serial-number>，取第12位开始的10位
        //2、取得<features>下每一个<feature>下的<actual-value>
    }


}
