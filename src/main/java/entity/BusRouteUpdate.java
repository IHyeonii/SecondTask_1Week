package entity;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class BusRouteUpdate {// 버스노선-정류장 데이터
  private int routeId; // 노선ID
  private int seq; //정류장 순번
  private Long stationId; // 정류장Id
  private String stationName; //정류장 명
  private int districtId; //행정구역 정보

  public int getRouteId() {
    return routeId;
  }
  public void setRouteId(int routeId) {
    this.routeId = routeId;
  }
  public int getDistrictId() {
    return districtId;
  }
  public void setDistrictId(int districtId) {
    this.districtId = districtId;
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

  @Override
  public String toString() {
    return "BusRoute [routeId=" + routeId + ", seq=" + seq + ", stationId=" + stationId + ", stationName="
        + stationName + ", districtId=" + districtId + "]";
  }

  public static HashMap<Integer, HashMap<Long, BusRouteUpdate>> ReadBusData() throws Exception {
    File targetFile = new File("C:\\Users\\ihyeon\\Desktop\\data\\BRS_20201102.txt");
    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(targetFile), "UTF-8"));
    CSVReader csvReader = new CSVReader(reader);

    String[] str = null; // 한줄씩 읽어서 String 변수에 담아

    HashMap<Integer, HashMap<Long, BusRouteUpdate>> busInfo = new HashMap<>();
    // Key: 노선Id, Value: BusRote 객체

    String[] header = csvReader.readNext(); //처음 필드명 제외
    // 객체를 담을 맵이나 리스트를 반복문 밖에
    while ((str = csvReader.readNext()) != null) {
      BusRouteUpdate busRoute = new BusRouteUpdate();
      busRoute.setRouteId(Integer.parseInt(str[0]));
      busRoute.setStationId(Long.parseLong(str[2]));
      busRoute.setStationName(str[4]);
      busRoute.setDistrictId(Integer.parseInt(str[6]));

      int routeId = busRoute.getRouteId();
      Long stationId = busRoute.getStationId();

      if(!busInfo.containsKey(routeId)) {
        busInfo.put(routeId, new HashMap<>());
      }
      if(!busInfo.get(routeId).containsKey(stationId)) {
        busInfo.get(routeId).put(stationId, busRoute);
      }
    }
    csvReader.close();
    reader.close();

    return busInfo;
  }
}
