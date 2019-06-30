package com.programmer.gate;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

// based on the source from: https://www.programmergate.com/how-to-read-xml-file-in-java/
public class MyReadXMLWithDOM {

    //private static int fileProcessed = 0;

    public static void main( String[] args ) throws Exception
    {
        //String rootPath = "D:\\work\\02-03.workspace-idea\\ReadXML";
        String rootPath ="C:\\Users\\RAVEN_V01\\Downloads\\data\\data\\24";

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
                        +"====file processed: %d.",
                hour, minute, second,
                fileProcessed);

        System.out.println(info);
    }

    private static int processFiles(String rootPath) throws Exception{
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
                processFile(sourcefile.getAbsolutePath(), outputFile);
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

        System.out.println("processing file: " + filename);

        //1、<test-record>\<header>\<serial-number>，取第12位开始的10位
        NodeList headerNodes = doc.getElementsByTagName("header");
        if (headerNodes.getLength()<1) {
            System.out.println("Can't find node:[header]");
            return;
        }
        Node headerNode = headerNodes.item(0);
        if (headerNode.getNodeType() != Node.ELEMENT_NODE) {
            System.out.println("[Header] is not a element node");
            return;
        }

        //Element headerElement = (Element) headerNode;
        NodeList snNodes = ((Element) headerNode).getElementsByTagName("serial-number");
        if (snNodes.getLength() < 1) {
            System.out.println("Can't find element: [serial-number");
            return;
        }
        String serialNumber = snNodes.item(0).getTextContent();
        System.out.println("serial-number is:" + serialNumber);
        //取第12位开始的10位
        String trimmedSn = serialNumber.substring(11,21);
        System.out.println("trimed serial-number is:" + trimmedSn);

        //2、取得<test-record>\<features>下每一个<feature>下的<actual-value>
        NodeList featuresNodes = doc.getElementsByTagName("features");
        if (featuresNodes.getLength()<1) {
            System.out.println("Can't find node:[features]");
            return;
        }

        NodeList featureNodes = featuresNodes.item(0).getChildNodes();
        if (featureNodes.getLength()<1) {
            System.out.println("Can't find node:[feature]");
            return;
        }
        //System.out.println("1."+featureNodes.item(0).getNodeName());

        for (int i=0; i<featureNodes.getLength(); i++) {
            Node featureNode = featureNodes.item(i);
            if (featureNode.getNodeType() != Node.ELEMENT_NODE) {
                //System.out.println("[feature] is not a element node");
                //return;
                continue;
            }

            NodeList actualValueNodes = ((Element) featureNode).getElementsByTagName("actual-value");
            if (actualValueNodes.getLength() < 1) {
                System.out.println("Can't find element: [actual-value]");
                return;
            }

            String actualValue = actualValueNodes.item(0).getTextContent();
            System.out.print(actualValue+"\t");
        }
        System.out.println("\n");

    }


}
