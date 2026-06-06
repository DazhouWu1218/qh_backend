package com.htht.executor.task.util;

import com.htht.executor.product.entity.ProductAnalysisTableInfo;
import com.htht.executor.product.entity.ProductEntity;
import com.htht.executor.product.entity.ProductFileInfoEntity;
import com.htht.executor.product.entity.ProductInfoEntity;
import com.htht.executor.product.service.ProductFileInfoService;
import com.htht.executor.product.service.ProductInfoService;
import com.htht.executor.product.service.ProductService;
import com.htht.executor.task.service.word.WordExecuteService;
import com.htht.job.core.entity.paramtemplate.DynamicParam;
import com.htht.job.core.entity.paramtemplate.ProductParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Transactional(rollbackFor = Exception.class)
@Service("ProductUtil")
@Slf4j
public class ProductUtil {
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductInfoService productInfoService;
    @Autowired
    private ProductFileInfoService productFileInfoService;

    @Autowired
    @Qualifier("mysqlJdbcTemplate")
    private JdbcTemplate mysqlJdbcTemplate;

    /**
     * 保存values数据
     *
     * @param pATInfo
     * @return
     */
    public int saveProductDetail(ProductAnalysisTableInfo pATInfo) {
    	try{
            int count = 0;
            if (pATInfo == null) {
                return count;
            }
            String createTableSql = pATInfo.generateCreateTableSql();
            mysqlJdbcTemplate.execute(createTableSql);
            log.info("saveProductDetail===generateReplaceDataSql2");
            List<String> lst = pATInfo.generateReplaceDataSql2();
            System.out.println("saveProductDetail===lst"+lst.size());
            for(String sql:lst){
                int num = mysqlJdbcTemplate.update(sql);
                count += num;
            }
            log.info("saveProductDetail===count"+count);
            return count;
        
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return 0;
    }

    public ProductInfoEntity saveProductInfo(DynamicParam dynamicParam, ProductEntity p, String regionId,
                                             String issue, String cycle, String mosaicFile,
                                             String inputFileName, Integer isShow, ProductParam productParam) {

        String modelIdentify = dynamicParam.getProductKey();
        List<String> list = Arrays.asList(mosaicFile.split("\\."));
        String mosaicFiles = mosaicFile.replace("." + list.get(list.size() - 1), "");

        ProductInfoEntity pi = new ProductInfoEntity();
        pi.setProductId(p.getId());
        pi.setName(p.getName());
        pi.setMark(modelIdentify);
        pi.setCycle(cycle);
        pi.setMapUrl(p.getMapUrl());
        pi.setProductPath(p.getProductPath());
        pi.setGdbPath(p.getGdbPath());
        pi.setIssue(issue);
        pi.setRegionId(regionId);
        pi.setIsRelease(p.getIsRelease());
        pi.setBz(p.getBz());
        pi.setMosaicFile(mosaicFiles);
        pi.setCreateTime(new Date());
        pi.setModelIdentify(modelIdentify);
        pi.setResolution(productParam.getResolution());
        pi.setInputFileName(inputFileName);
        pi.setIsRelease(isShow);
        pi.setSatellite(productParam.getSatellite());
        pi.setSensor(productParam.getSensor());
        pi.setCreateTime(new Date());
        pi.setUpdateTime(new Date());
        productInfoService.save(pi);
        return pi;
    }

    @Value("${algorithm.exePath}")
    private String exePath;

    @Autowired
    private WordExecuteService wordExecuteService;

    /**
     * 保存ProductInfoFile
     *
     * @param productInfoId
     * @param filePath
     * @param outputPath
     */
    public void saveProductInfoFile(String productId,String productInfoId, String filePath,
                                    String outputPath, String regionId, String issue, String cycle) {
        String fileName = new File(filePath).getName();
        String productType = "file";
        if (fileName.contains("png") || fileName.contains("jpg")) {
            productType = "pic";
        } else if (fileName.contains("doc")
                || fileName.contains("docx")) {
//            Objects.requireNonNull(XxlJobHelper.getTriggerParam()).getTaskParam().getDynamicMap().getOrDefault("wordExePath",exePath);
//            wordExecuteService.wordToPdf(filePath,exePath);
            productType = "doc";
        }else if(fileName.toLowerCase().endsWith(".html")){
        	 productType = "doc";
        	 //doc->html
        	 String htmlName = new File(filePath).getName();
        	 WriteToHtml.saveHtmlToWord(filePath,htmlName, htmlName.replace(".html",".doc"));
        	 
        }
        String relativePath = filePath.replace("\\", "/").replace(outputPath,"");

        ProductFileInfoEntity pfi = new ProductFileInfoEntity();
        pfi.setProductInfoId(productInfoId);
        pfi.setProductId(productId);
        pfi.setFileName(fileName);
        pfi.setFileType(fileName.substring(fileName.lastIndexOf(".") + 1));
        pfi.setProductType(productType);
        pfi.setFilePath(filePath);
        pfi.setFileSize(new File(filePath).length());
        pfi.setRelativePath(relativePath);
        pfi.setIsDel("0");
        pfi.setCreateTime(new Date());
        pfi.setRegion(regionId);
        pfi.setIssue(issue);
        pfi.setCycle(cycle);
        pfi.setCreateTime(new Date());
        pfi.setUpdateTime(new Date());
        pfi.setZt("1");
        productFileInfoService.save(pfi);
    }


}
