//package com.njht.webyun.publish.common.util;
//
//import com.deepoove.poi.XWPFTemplate;
//import com.deepoove.poi.data.PictureRenderData;
//import com.deepoove.poi.data.PictureType;
//import com.deepoove.poi.data.Pictures;
//import lombok.SneakyThrows;
//import org.apache.poi.util.Units;
//import org.apache.poi.xwpf.usermodel.XWPFDocument;
//import org.apache.poi.xwpf.usermodel.XWPFParagraph;
//import org.apache.poi.xwpf.usermodel.XWPFRun;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.InputStream;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @Author: 代国军
// * @CreateDate: 2021/11/30 20:19
// * @Description: 往word 模板里写数据
// */
//public class WriteWordUtils {
//
//    /**
//     *
//     */
//    @SneakyThrows
//    public static void reportExeResult( File resultWordFile) {
//        // 写图片
//        writeImgToDoc(Arrays.asList("Y:\\EAMIS\\production\\avatar\\base\\1girl.jpg"),resultWordFile.getPath());
//    }
//
//    /**
//     * 往 word 文档里写数据
//     * @param imgPathList
//     * @param wordPath
//     * @throws Exception
//     */
//    @SneakyThrows
//    public static void writeImgToDoc(List<String> imgPathList,String wordPath) {
//        XWPFDocument doc = new XWPFDocument();
//        XWPFParagraph p = doc.createParagraph();
//        XWPFRun xwpfRun = p.createRun();
//        for (String imgFile : imgPathList) {
//            int format=XWPFDocument.PICTURE_TYPE_JPEG;
//            xwpfRun.setText(imgFile);
//            xwpfRun.addBreak();
//            xwpfRun.addPicture (new FileInputStream(imgFile),format,imgFile,Units.toEMU(200), Units.toEMU(200));
//        }
//        FileOutputStream out = new FileOutputStream(wordPath);
//        doc.write(out);
//        out.close();
//    }
//    public static void main(String[] args) {
//        String path = "D:\\applogs\\123.docx";
//        File file = new File(path);
//        if(!file.getParentFile().exists()){
//            file.getParentFile().mkdirs();
//        }
//        reportExeResult(file);
//
//    }
//}
