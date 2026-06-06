package com.htht.executor.geoserver;

import com.alibaba.fastjson.JSONObject;
import com.htht.job.core.util.SystemCommandUtil;
import com.htht.job.core.xml.XmlUtils;
import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.decoder.RESTDataStore;
import it.geosolutions.geoserver.rest.decoder.RESTDataStoreList;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import it.geosolutions.geoserver.rest.encoder.GSLayerEncoder;
import it.geosolutions.geoserver.rest.encoder.datastore.GSPostGISDatastoreEncoder;
import it.geosolutions.geoserver.rest.encoder.feature.GSFeatureTypeEncoder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

/**
 * GeoServer自动发布服务
 * @author piesat
 */
@Slf4j
@Component
public class GeoServerPublish implements GeoServerInterface{

    @Value("${geoserver.url}")
    public String geoserverUrl;

    @Value("${geoserver.username}")
    public String username;

    @Value("${geoserver.passwd}")
    public String password;

    @Autowired
    private PostGreSqlDto postGreSql;

    private GeoServerRESTManager manager;

    private GeoServerRESTPublisher geoServerRESTPublisher;

    private GeoServerRESTReader geoServerRESTReader;


    @SneakyThrows
    @PostConstruct
    private void initServerRestObj(){
        URL u = new URL(geoserverUrl);
        manager = new GeoServerRESTManager(u, username, password);
        geoServerRESTPublisher = manager.getPublisher();
        geoServerRESTReader = manager.getReader();
    }


    @SneakyThrows
    @Override
    public Boolean publishGeoTiff (GeoServerDto geoServerDto) {

        String workspace = geoServerDto.getWorkspace();
        String storeName = geoServerDto.getStoreName();
        String coverageName = geoServerDto.getCoverageName();
        File geotiff = geoServerDto.getGeoTiff();

        //workspace是否存在，不存在则新建。
        boolean storeNull = isStoreNull(workspace, storeName);
        if(storeNull){
            boolean result = geoServerRESTPublisher.publishGeoTIFF(workspace,storeName,coverageName,geotiff);
            log.info("数据发布是否成功："+result);
            return result;
        }else{
            log.info("数据已经发布过了，不能重复发布！");
        }
        return false;
    }

    @SneakyThrows
    @Override
    public Boolean publishShapefile(GeoServerDto geoServerDto) {

        //发布zip格式的shp数据(直接把shp等相关数据压缩成zip格式，中间不要加文件夹)
        String workspace = geoServerDto.getWorkspace();
        String datastoreName = geoServerDto.getStoreName();      //必须和zip压缩包的名称一致
        String layerName = geoServerDto.getLayerName();               //必须和压缩包里面shp的名称一致
        File zipFile = geoServerDto.getZipFile();
        String srs = geoServerDto.getProjection(); // 坐标系信息

        //workspace是否存在，不存在则新建。
        boolean storeNull = isStoreNull(workspace, datastoreName);
        if (storeNull) {
            boolean result = geoServerRESTPublisher.publishShp(workspace, datastoreName, layerName, zipFile, srs);
            log.info("数据发布是否成功："+result);
        } else {
            log.info("数据已经发布过了，不能重复发布！");
        }
        return false;
    }

    @SneakyThrows
    @Override
    public Boolean publishPostGIS(GeoServerDto geoServerDto) {

        String ws = geoServerDto.getWorkspace() ;     //待创建和发布图层的工作区名称workspace
        String store_name = geoServerDto.getStoreName() ; //待创建和发布图层的数据存储名称store
        String table_name = geoServerDto.getTableName() ; // 数据库要发布的表名称,后面图层名称和表名保持一致


        List<String> workspaces = geoServerRESTReader.getWorkspaceNames();
        if(!workspaces.contains(ws)){
            boolean createWs = geoServerRESTPublisher.createWorkspace(ws);
            log.info("create ws : " + createWs);
        }else {
            log.info("workspace已经存在了,ws :" + ws);
        }

        //判断数据存储（datastore）是否已经存在，不存在则创建
        RESTDataStore restStore = geoServerRESTReader.getDatastore(ws, store_name);
        if(restStore == null){
            GSPostGISDatastoreEncoder store = new GSPostGISDatastoreEncoder(store_name);
            store.setHost(postGreSql.host);//设置url
            store.setPort(postGreSql.port);//设置端口
            store.setUser(postGreSql.username);// 数据库的用户名
            store.setPassword(postGreSql.password);// 数据库的密码
            store.setDatabase(postGreSql.database);// 那个数据库;
            store.setSchema(postGreSql.schema); //当前先默认使用public这个schema
            store.setConnectionTimeout(20);// 超时设置
            store.setMaxConnections(20); // 最大连接数
            store.setMinConnections(1);     // 最小连接数
            store.setExposePrimaryKeys(true);
            boolean createStore = manager.getStoreManager().create(ws, store);
            log.info("create store : " + createStore);
        } else {
            log.info("数据存储已经存在了,store:" + store_name);
        }

        //判断图层是否已经存在，不存在则创建并发布
        RESTLayer layer = geoServerRESTReader.getLayer(ws, table_name);
        if(layer == null){
            GSFeatureTypeEncoder pds = new GSFeatureTypeEncoder();
            pds.setTitle(table_name);
            pds.setName(table_name);
            pds.setSRS(geoServerDto.getProjection());
            GSLayerEncoder layerEncoder = new GSLayerEncoder();
            boolean publish = geoServerRESTPublisher.publishDBLayer(ws, store_name,  pds, layerEncoder);
            log.info("publish : " + publish);
        }else {
            log.info("表已经发布过了,table:" + table_name);
        }
        return false;
    }

    @Override
    public Boolean publishImageMosaic(GeoServerDto geoServerDto) {

        String workspace = geoServerDto.getWorkspace();
        String storeName = geoServerDto.getStoreName();
        File file = geoServerDto.getZipFile();
        //workspace是否存在，不存在则新建。
        boolean storeNull = isStoreNull(workspace, storeName);
        if(storeNull){
            try {
                // 发布文件为zip 压缩包的情况
                if (file.exists() && file.isFile()) {
                    boolean result = geoServerRESTPublisher.publishImageMosaic(workspace, storeName, file);
                    log.info("数据发布是否成功：" + result);
                    return result;
                }
                // 发布为文件夹路径的情况
                if (file.exists() && file.isDirectory()){
                    String path = file.getPath();
                    String [] params = {"curl","-v","-u",username+":"+password,"-XPUT","-H","Content-type: text/plain","-d","file:"+path,geoserverUrl+"/rest/workspaces/"+workspace+"/coveragestores/"+storeName+"/external.imagemosaic"};
                    String xml = SystemCommandUtil.execute(String.join(" ",params));
                    JSONObject callObj = XmlUtils.xmlToJsonObject(xml);
                    if (!Objects.isNull(callObj)){
                        if (callObj.size()>0){
                            log.info("ImageMosaic数据发布成功");
                            return true;
                        }
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            }
        }else{
            log.info("数据已经发布过了，不能重复发布！");
        }
        return false;
    }

    /**
     * 检查存储空间是否存在
     * @param workspace
     * @param storeName
     * @return
     */
    private boolean isStoreNull(String workspace, String storeName) {
        List<String> workspacesList = geoServerRESTReader.getWorkspaceNames();
        boolean wsNull = !workspacesList.contains(workspace);
        if (wsNull) {
            geoServerRESTPublisher.createWorkspace(workspace);
        }
        //store是否存在，不存在则新建并发布数据。
        RESTDataStoreList dataStoresList = geoServerRESTReader.getDatastores(workspace);
        List<String> datastoreNameList = dataStoresList.getNames();
        return !datastoreNameList.contains(storeName);
    }
}
