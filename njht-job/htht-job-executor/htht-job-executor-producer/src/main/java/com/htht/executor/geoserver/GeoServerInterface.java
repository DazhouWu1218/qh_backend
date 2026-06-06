package com.htht.executor.geoserver;

public interface GeoServerInterface {

    /**
     * 发布GeoTiff 方法
     * @param geoServerDto
     * @return
     */
   Boolean publishGeoTiff (GeoServerDto geoServerDto);

    /**
     * 发布Shp数据
     * @param geoServerDto
     * @return
     */
   Boolean publishShapefile(GeoServerDto geoServerDto);

    /**
     * 发布PostGIS表数据
     * @param geoServerDto
     * @return
     */
   Boolean publishPostGIS(GeoServerDto geoServerDto);


    /**
     * 发布同一区域多时相数据
     * @param geoServerDto
     * @return
     */
   Boolean publishImageMosaic (GeoServerDto geoServerDto);
}
