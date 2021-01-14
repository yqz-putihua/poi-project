package com.yqz.zzh.qrcode.test1;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class CustomXWPFDocument extends XWPFDocument {

    public CustomXWPFDocument(InputStream in) throws IOException {
        super(in);
    }

    public CustomXWPFDocument() {
        super();
    }

    public CustomXWPFDocument(OPCPackage pkg) throws IOException {
        super(pkg);
    }

    public static void main(String[] args) throws IOException {
        Map<String,Object> map = new HashMap<String, Object>();
        map.put("proName","合规性分析测试");
        map.put("proType","总体规划");
        map.put("nonConstrArea","25438834.17");
        map.put("constrArea","0.00");
        Map<String,Object> header = new HashMap<String, Object>();
        header.put("width", 556);
        header.put("height", 230);
        header.put("type", "png");
        header.put("content", "D:\\temp\\QrCode.png");//图片路径
        map.put("picture",header);
        XWPFDocument doc = WordUtil.generateWord(map, "C:\\Users\\zhuzhenhua\\Desktop\\test1.docx");
        FileOutputStream fopts = new FileOutputStream("D:\\temp\\test.docx");
        doc.write(fopts);
        fopts.close();
    }
}