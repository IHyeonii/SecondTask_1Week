package entity;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class BusRoute {// 버스노선-정류장 데이터
  private Long routeId; // 노선ID
  private int seq; //정류장 순번
  private Long stationId; // 정류장Id
  private String stationName; //정류장 명
  private Long districtId; //행정구역 정보

//  public BusRoute(Long routeId, Long stationId, String stationName, Long districtId) {
//    this.routeId = routeId;
//    this.stationId = stationId;
//    this.stationName = stationName;
//    this.districtId = districtId;
//  }

  public Long getRouteId() {
    return routeId;
  }
  public void setRouteId(Long routeId) {
    this.routeId = routeId;
  }
  public int getSeq() {
    return seq;
  }
  public void setSeq(int seq) {
    this.seq = seq;
  }
  public Long getStationId() {
    return stationId;
  }
  public void setStationId(Long stationId) {
    this.stationId = stationId;
  }
  public String getStationName() {
    return stationName;
  }
  public void setStationName(String stationName) {
    this.stationName = stationName;
  }
  public Long getDistrictId() {
    return districtId;
  }
  public void setDistrictId(Long districtId) {
    this.districtId = districtId;
  }

  @Override
  public String toString() {
    return "BusRoute [routeId=" + routeId + ", seq=" + seq + ", stationId=" + stationId + ", stationName="
        + stationName + ", districtId=" + districtId + "]";
  }

  public static Map<String, BusRoute> ReadBusData() throws Exception {
    File targetFile = new File("C:\\Users\\ihyeon\\Desktop\\data\\.txt");
    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(targetFile), "UTF-8"));
    CSVReader csvReader = new CSVReader(reader);

    String[] str = null; // 한줄씩 읽어서 String 변수에 담아

    Map<String,  BusRoute> busInfo = new HashMap<>();
    // Key: 노선Id, Value: BusRote 객체

    String[] header = csvReader.readNext(); //처음 필드명 제외
    // 객체를 담을 맵이나 리스트를 반복문 밖에
    while ((str = csvReader.readNext()) != null) {
      BusRoute busRoute = new BusRoute();
      busRoute.setRouteId(Long.parseLong(str[0]));
      busRoute.setStationId(Long.parseLong(str[2]));
      busRoute.setStationName(str[4]);
      busRoute.setDistrictId(Long.parseLong(str[6]));

      Long routeId = busRoute.getRouteId();
      String key1 = String.valueOf(routeId);

      Long stId = busRoute.getStationId();
      String key2 = String.valueOf(stId);

      String key = key1 + "," + key2;

      busInfo.put(key, busRoute);
    }
    csvReader.close();
    reader.close();

    return busInfo;
  }
}
