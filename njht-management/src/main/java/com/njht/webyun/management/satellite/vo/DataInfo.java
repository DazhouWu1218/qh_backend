package com.njht.webyun.management.satellite.vo;


public class DataInfo {

  private String id;
  private String satelliteName;
  private String satelliteTleId;
  private String orbitTimeStr;
  private String orbitTime;
  private String createTime;
  private String geom;
  private String geomBuffer;
  private String geomDay;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getSatelliteName() {
    return satelliteName;
  }

  public void setSatelliteName(String satelliteName) {
    this.satelliteName = satelliteName;
  }

  public String getSatelliteTleId() {
    return satelliteTleId;
  }

  public void setSatelliteTleId(String satelliteTleId) {
    this.satelliteTleId = satelliteTleId;
  }

  public String getOrbitTimeStr() {
    return orbitTimeStr;
  }

  public void setOrbitTimeStr(String orbitTimeStr) {
    this.orbitTimeStr = orbitTimeStr;
  }

  public String getOrbitTime() {
    return orbitTime;
  }

  public void setOrbitTime(String orbitTime) {
    this.orbitTime = orbitTime;
  }

  public String getCreateTime() {
    return createTime;
  }

  public void setCreateTime(String createTime) {
    this.createTime = createTime;
  }

  public String getGeom() {
    return geom;
  }

  public void setGeom(String geom) {
    this.geom = geom;
  }

  public String getGeomBuffer() {
    return geomBuffer;
  }

  public void setGeomBuffer(String geomBuffer) {
    this.geomBuffer = geomBuffer;
  }

  public String getGeomDay() {
    return geomDay;
  }

  public void setGeomDay(String geomDay) {
    this.geomDay = geomDay;
  }

  @Override
  public String toString() {
    return "DataInfo{" +
            "id='" + id + '\'' +
            ", satelliteName='" + satelliteName + '\'' +
            ", satelliteTleId='" + satelliteTleId + '\'' +
            ", orbitTimeStr='" + orbitTimeStr + '\'' +
            ", orbitTime='" + orbitTime + '\'' +
            ", createTime='" + createTime + '\'' +
            ", geom='" + geom + '\'' +
            ", geomBuffer='" + geomBuffer + '\'' +
            ", geomDay='" + geomDay + '\'' +
            '}';
  }
}
