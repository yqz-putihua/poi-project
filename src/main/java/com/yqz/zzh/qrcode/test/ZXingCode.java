package com.yqz.zzh.qrcode.test;



import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.lowagie.text.*;
import com.lowagie.text.Image;
import com.lowagie.text.rtf.RtfWriter2;
import com.lowagie.text.rtf.document.output.RtfDataCache;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZXingCode {

    /**
     * @author 信息二班郄先生
     * @date 2019-3-24 14:34:47
     * 二维码生成
     */
        /**
         * 颜色
         */
        private static final int QRCOLOR = 0xFF000000;
        /**
         * 背景颜色
         */
        private static final int BGWHITE = 0xFFFFFFFF;

    /**
     * 生成二维码方法
     *
     * @param data 索书号
     * @param belowText 书名
     * @return
     */
    public static Image getQRCode(String data, String belowText) {
        Image imageByte =null;
        try {
            ZXingCode zp = new ZXingCode();
            BufferedImage bim = zp.generateQRCodeBufferedImage(data, BarcodeFormat.QR_CODE, 230, 230, zp.getDecodeHintType());
            //字节数组
            imageByte = zp.addTextForQRCode(bim, belowText);

//            return imageByte;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageByte;
    }


    /**
     * @param bim       放在内存中图片
     * @param belowText 二维码下方显示文字
     * @return
     */
    public Image addTextForQRCode(BufferedImage bim, String belowText) {
        try {
            BufferedImage image = bim;
            if (belowText != null && !belowText.equals("")) {
                BufferedImage outImage = new BufferedImage(230, 245, BufferedImage.TYPE_4BYTE_ABGR);
                Graphics2D outg = outImage.createGraphics();
                outg.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
                outg.setColor(Color.BLACK);
                //获得本地字体列表
//                String[] fonts = this.getFontName();
                outg.setFont(new Font("SansSerif", Font.PLAIN, 18));
                int strWidth = outg.getFontMetrics().stringWidth(belowText);
                outg.drawString(belowText, 100 - strWidth / 2 + 8, image.getHeight() + (outImage.getHeight() - image.getHeight()) / 2 + 5);
                outg.dispose();
                outImage.flush();
                image = outImage;
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            baos.flush();
            ImageIO.write(image, "png", baos);
            BufferedImage newBufferedImage = new BufferedImage(
                    image.getWidth(), image.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            newBufferedImage.createGraphics().drawImage(image, 0, 0,
                    Color.WHITE, null);
//            存放本地
//            ImageIO.write(newBufferedImage, "png", new File(CODEPATH + "SZ-" + System.currentTimeMillis() + "code.png"));
            com.lowagie.text.Image imageByte = com.lowagie.text.Image.getInstance(baos.toByteArray());
            baos.close();
            image.flush();
            return imageByte;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 绘制二维码，不带文字
     *
     * @param content       扫描内容
     * @param barcodeFormat 格式
     * @param width
     * @param height
     * @param hints         二维码属性设置
     * @return 放到内存中，后续再二维码下方添加文字
     */
    public BufferedImage generateQRCodeBufferedImage(String content, BarcodeFormat barcodeFormat, int width, int height, Map<EncodeHintType, ?> hints) {
        MultiFormatWriter multiFormatWriter = null;
        BitMatrix bm = null;
        BufferedImage image = null;
        try {
            multiFormatWriter = new MultiFormatWriter();
            bm = multiFormatWriter.encode(content, barcodeFormat, width, height, hints);
            int w = bm.getWidth();
            int h = bm.getHeight();
            image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    image.setRGB(x, y, bm.get(x, y) ? QRCOLOR : BGWHITE);
                }
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return image;
    }

    /**
     * 设置二维码属性
     *
     * @return
     */
    public Map<EncodeHintType, Object> getDecodeHintType() {
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.MARGIN, 0);
        hints.put(EncodeHintType.MAX_SIZE, 350);
        hints.put(EncodeHintType.MIN_SIZE, 100);
        return hints;
    }

    /**
     * 获得本地字体列表
     * @return 字体数组
     */
    public String[] getFontName(){
        GraphicsEnvironment e = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fontName = e.getAvailableFontFamilyNames();
        return fontName;
    }

    /**
     *
     * @param imageList 图片字节流集合
     * @param response 返回word
     * @throws DocumentException 文档错误抛出
     * @throws IOException 输入输出错误抛出
     */
    public static void createDocContext(List<Image> imageList, HttpServletResponse response,String path) throws DocumentException, IOException {
        File file1 = new File(path);
//        judeFileExists(file1);
        Document document = new Document();
        // 建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中
        RtfWriter2 instance = RtfWriter2.getInstance(document, new FileOutputStream(file1));
        instance.getDocumentSettings().setDataCacheStyle(RtfDataCache.CACHE_DISK);

        // Specify that all \n are translated into soft linebreaks.
        instance.getDocumentSettings().setAlwaysGenerateSoftLinebreaks(true);

        document.open();
        Paragraph title = new Paragraph("图书索书号二维码");
        //设置标题格式对齐方式
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        // 设置 Table 表格
        //设置表格，将图片加到表格中进行方便定位
        Table aTable = new Table(4);
        // 设置每列所占比例
        // 占页面宽度 90%
        aTable.setWidth(100);
        // 自动填满
         aTable.setAutoFillEmptyCells(true);
        //这里是imagelist集合，就是图片字节流的集合，图片从流中去获取放到word中
        for (int i = 0; i < imageList.size(); i++) {
            //设置图片等比例缩小
            imageList.get(i).scalePercent(55);
            aTable.addCell(new Cell(imageList.get(i)));
        }
        aTable.setAlignment(Element.ALIGN_CENTER);
        document.add(aTable);
        document.add(new Paragraph("\n"));
        System.out.println("word----success");
        document.close();
        //响应浏览器 返回下载
        response.setContentType("applicaiton/x-download");
        response.addHeader("Content-Disposition", "attachment;filename=" + "TwoCodeImage.doc");
        InputStream is = null;
        OutputStream os = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        is = new FileInputStream(new File(path));
        bis = new BufferedInputStream(is);
        os = response.getOutputStream();
        bos = new BufferedOutputStream(os);
        byte[] b = new byte[1024];
        int len = 0;
        while ((len = bis.read(b)) != -1) {
            bos.write(b, 0, len);
        }
        bis.close();
        is.close();
        bos.close();
        os.close();
        //删除本地临时文件
//        file1.delete();
    }

    public static void main(String[] args) {
        getQRCode1("啥贝贝","扫扫有惊喜");
//        CreateWord createWord = new CreateWord();
//
//        HttpServletResponse httpServletResponse = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
//        try {
//            Image qrCode = getQRCode("傻呗呗", "扫一扫有惊喜");
//            List<Image> imageList = new ArrayList<>();
//            imageList.add(qrCode);
//            createWord.createDocContext(imageList, httpServletResponse);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public static void getQRCode1(String data, String belowText) {
        Image imageByte = null;
        try {
            ZXingCode zp = new ZXingCode();
            BufferedImage bim = zp.generateQRCodeBufferedImage(data, BarcodeFormat.QR_CODE, 230, 230, zp.getDecodeHintType());
            ImageIO.write(bim,"png",new File("D:\\temp\\abc.png"));
//            return imageByte;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return;
    }

}