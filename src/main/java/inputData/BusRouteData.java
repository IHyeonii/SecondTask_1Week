package inputData;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class BusRouteData {// 버스노선-정류장 데이터
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

  public static HashMap<Integer, HashMap<Integer, BusRouteData>> ReadBRSFile() throws Exception {
    File targetFile = new File("C:\\Users\\ihyeon\\Desktop\\data\\BRS_20201102.txt");
    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(targetFile), "UTF-8"));
    CSVReader csvReader = new CSVReader(reader);

    String[] str = null; // 한줄씩 읽어서 String 변수에 담아

    // Key: 노선Id, 순번, Value: BusRoute 객체
    HashMap<Integer, HashMap<Integer, BusRouteData>> busData = new HashMap<>();

    String[] header = csvReader.readNext(); //처음 필드명 제외

    while ((str = csvReader.readNext()) != null) {
      BusRouteData busRoute = new BusRouteData();
      busRoute.setRouteId(Integer.parseInt(str[0]));
      busRoute.setSeq(Integer.parseInt(str[1]));
      busRoute.setStationId(Long.parseLong(str[2]));
      busRoute.setStationName(str[4]);
      busRoute.setDistrictId(Integer.parseInt(str[6]));

      int routeId = busRoute.getRouteId();
      int seq = busRoute.getSeq();

      if(!busData.containsKey(routeId)) {
        busData.put(routeId, new HashMap<>());
      }
      if(!busData.get(routeId).containsKey(seq)) {
        busData.get(routeId).put(seq, busRoute);
      }
    }
    csvReader.close();
    reader.close();

    return busData;
  }
}
