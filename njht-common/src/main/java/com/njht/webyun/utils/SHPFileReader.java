package com.njht.webyun.utils;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.sql.Date;
import java.sql.Time;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.WKTReader;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geojson.geom.GeometryJSON;

/**
 * @author Administrator
 */
public class SHPFileReader {
    private static byte[] ZIP_HEADER_1 = new byte[] { 80, 75, 3, 4 };
    private static byte[] ZIP_HEADER_2 = new byte[] { 80, 75, 5, 6 };

    /**
     * 获取shape文件feature集合
     *
     * @param shapePath shape文件路径
     * @param charSet   读取shape文件编码
     * @return SimpleFeatureCollection
     */
    public static SimpleFeatureCollection getFeatures(String shapePath, String charSet) {
        SimpleFeatureCollection sfc = null;
        ShapefileDataStoreFactory dataStoreFactory = new ShapefileDataStoreFactory();
        ShapefileDataStore sds = null;
        try {
            sds = (ShapefileDataStore) dataStoreFactory.createDataStore(new File(shapePath).toURI().toURL());
            sds.setCharset(Charset.forName(charSet));
            SimpleFeatureSource featureSource = sds.getFeatureSource();
            sfc = featureSource.getFeatures();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            sds.dispose();
        }
        return sfc;
    }

    /**
     * 读取ShapeFile中的空间数据
     *
     * @param shapeFilePath
     * @return List<Geometry>
     */
    public static List<Geometry> getGeometries(String shapeFilePath) {
        List<Geometry> result = new ArrayList<Geometry>();
        ShapefileReader reader =null;
        try {
            ShpFiles file = new ShpFiles(shapeFilePath);
            reader = new ShapefileReader(file, false, false, new GeometryFactory());
            while (reader.hasNext()) {
                result.add((Geometry) reader.nextRecord().shape());
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    public static String readPrj(String filePath) {
        String string = "";
        try {
            File file = new File(filePath + ".prj");
            if (file.isFile() && file.exists()) {
                InputStreamReader isr = new InputStreamReader(new FileInputStream(file), "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String lineTxt = null;
                StringBuffer sBuffer = new StringBuffer();
                while ((lineTxt = br.readLine()) != null) {
                    System.out.println(lineTxt);
                    sBuffer.append(lineTxt);
                }

                String[] coor = sBuffer.toString().split("\"");

                br.close();
            } else {
                System.out.println("文件不存在!");
            }
        } catch (Exception e) {
            System.out.println("文件读取错误!");
        }
        return string;
    }

    public static String geoToJson(String wkt) {
        String json = null;
        try {
            WKTReader reader = new WKTReader();
            Geometry geometry = reader.read(wkt);
            StringWriter writer = new StringWriter();
            GeometryJSON g = new GeometryJSON();
            g.write(geometry, writer);
            json = writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public static Geometry wktToGeo(String wkt) {
        Geometry geometry = null;
        try {
            WKTReader reader = new WKTReader();
            geometry = reader.read(wkt);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(wkt);
        }
        return geometry;
    }

    public static String geoToJson(Geometry geometry) {
        String json = null;
        try {
            StringWriter writer = new StringWriter();
            GeometryJSON g = new GeometryJSON();
            g.write(geometry, writer);
            json = writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public static String jsonToWkt(String geoJson) {
        String wkt = null;

        GeometryJSON gjson = new GeometryJSON();
        Reader reader = new StringReader(geoJson);
        try {
            Geometry geometry = gjson.read(reader);
            wkt = geometry.toText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return wkt;
    }

    public static Geometry evaluate(String geoJson) {
        Geometry geometry = null;
        GeometryJSON gjson = new GeometryJSON();
        Reader reader = new StringReader(geoJson);
        try {
            geometry = gjson.read(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return geometry;
    }

    /**
     * 缓冲器大小
     */
    private static final int BUFFER = 512;

    /**
     * 解压文件到指定目录
     */
    @SuppressWarnings({ "rawtypes", "resource" })
    public static void unZipFiles(String zipPath, String descDir) throws IOException {
        InputStream in =null;
        OutputStream out =null;
        try{
            File zipFile = new File(zipPath);
            if(!zipFile.exists()){
                throw new IOException("需解压文件不存在.");
            }
            File pathFile = new File(descDir);
            if (!pathFile.exists()) {
                pathFile.mkdirs();
            }
            ZipFile zip = new ZipFile(zipFile, Charset.forName("GBK"));
            for (Enumeration entries = zip.entries(); entries.hasMoreElements();) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                in = zip.getInputStream(entry);
                String outPath = (descDir + File.separator + zipEntryName).replaceAll("\\*", "/");
                // 判断路径是否存在,不存在则创建文件路径
                File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
                if (!file.exists()) {
                    file.mkdirs();
                }
                // 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
                if (new File(outPath).isDirectory()) {
                    continue;
                }
                // 输出文件路径信息
                out = new FileOutputStream(outPath);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
                in.close();
                out.close();
            }
        }catch(Exception e){
            throw new IOException(e);
        }finally {
            if (in!=null){
                in.close();
            }
            if (out!=null){
                out.close();
            }
        }
    }




    /**
     * 获取单个文件的MD5值！
     * @param file
     * @return
     */
    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte[] buffer = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    /**
     * @param destPath 解压目标路径
     * @param fileName 解压文件的相对路径
     */
    public static File createFile(String destPath, String fileName) {
        String[] dirs = fileName.split("/");//将文件名的各级目录分解
        File file = new File(destPath);
        if (dirs.length > 1) {//文件有上级目录
            for (int i = 0; i < dirs.length - 1; i++) {
                file = new File(file, dirs[i]);//依次创建文件对象知道文件的上一级目录
            }
            if (!file.exists()) {
                file.mkdirs();//文件对应目录若不存在，则创建
                try {
                    System.out.println("mkdirs: " + file.getCanonicalPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            file = new File(file, dirs[dirs.length - 1]);//创建文件
            return file;
        } else {
            if (!file.exists()) {//若目标路径的目录不存在，则创建
                file.mkdirs();
                try {
                    System.out.println("mkdirs: " + file.getCanonicalPath());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            file = new File(file, dirs[0]);//创建文件
            return file;
        }

    }

    /**
     * 遍历rootFile当前目录下的文件
     * @param rootFile
     * @param fileRegex
     * @return
     */
    public static List<File> iteratorFile(File rootFile, String fileRegex) {

        List<File> fileList = new ArrayList<File>();
        if (!rootFile.exists()) {
            return fileList;
        }

        if (fileRegex == null||rootFile.getName().matches(fileRegex)) {
            fileList.add(rootFile);
        }

        if (rootFile.isDirectory()) {
            File[] fileArr = rootFile.listFiles();
            for (File file : fileArr) {
                fileList.addAll(iteratorFile(file,fileRegex));
            }
        }

        return fileList;
    }

    public static Geometry unionGeo(Geometry a,Geometry b){
        return a.union(b);
    }


    public static void delFile(File file) {
        File[] files = file.listFiles();
        if (files != null && files.length != 0) {
            for (int i = 0;i<files.length;i++) {
                delFile(files[i]);
            }
        }
        file.delete();
    }


    /**
     * 判断文件是否为一个压缩文件
     *
     * @param file
     * @return
     */
    public static boolean isArchiveFile(File file) {
        if(file == null){
            return false;
        }
        if (file.isDirectory()) {
            return false;
        }
        boolean isArchive = false;
        InputStream input = null;
        try {
            input = new FileInputStream(file);
            byte[] buffer = new byte[4];
            int length = input.read(buffer, 0, 4);
            if (length == 4) {
                isArchive = (Arrays.equals(ZIP_HEADER_1, buffer)) || (Arrays.equals(ZIP_HEADER_2, buffer));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                }
            }
        }

        return isArchive;
    }


    public static void main(String[] args) throws Exception {
        getFeatures("C:\\Users\\atom\\Desktop\\zpg\\shanghai\\shanghai_polygon.shp","UTF-8");
    }
}
