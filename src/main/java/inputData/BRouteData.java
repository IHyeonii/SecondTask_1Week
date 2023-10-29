package inputData;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.TreeMap;

public class BRouteData {// 버스노선-정류장 데이터
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

  public static HashMap<Integer, TreeMap<Integer, BRouteData>> ReadBRSFile() throws Exception {
    String filePath = "C:\\Users\\ihyeon\\Desktop\\data\\TRIPS_inputData\\";
    int dir = 20201102;

    BufferedReader reader = null;

    for (int days = 0; days < 5; days++) {
      String dirs = String.valueOf(dir + days);

      StringBuilder sb = new StringBuilder();
      sb.append("BRS_").append(dirs).append(".txt");

      File file = new File(filePath + dirs, String.valueOf(sb));
      reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
    }

    CSVReader csvReader = new CSVReader(reader);

    String[] str = null; // 한줄씩 읽어서 String 변수에 담아

    // Key: 노선Id, 순번, Value: BusRoute 객체
    HashMap<Integer, TreeMap<Integer, BRouteData>> busData = new HashMap<>();

    String[] header = csvReader.readNext(); //처음 필드명 제외

    while ((str = csvReader.readNext()) != null) {
      BRouteData busRoute = new BRouteData();
      busRoute.setRouteId(Integer.parseInt(str[0]));
      busRoute.setSeq(Integer.parseInt(str[1]));
      busRoute.setStationId(Long.parseLong(str[2]));
      busRoute.setStationName(str[4]);
      busRoute.setDistrictId(Integer.parseInt(str[6]));

      int routeId = busRoute.getRouteId();
      int seq = busRoute.getSeq();

      TreeMap<Integer, BRouteData> seqMap = busData.getOrDefault(routeId, new TreeMap<>());
      seqMap.put(seq, busRoute);
      busData.put(routeId, seqMap);
    }
    csvReader.close();
    reader.close();

    return busData;
  }
}
