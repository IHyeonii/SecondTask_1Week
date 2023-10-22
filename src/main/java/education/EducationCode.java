package education;

import entity.BusRoute;

import java.util.HashMap;

public class EducationCode {
  public static void main(String[] args) {
  }

  public void ReadBusRouteData(HashMap<String, BusRoute> busRouteInfo) {
    //buffered read 파일읽기
  }
}
class BusStationInfo {
  Long stationId;
  String stnName;
  Long emdId;
}
class DistrictODResult {
  public int runTime; // 단위: 분
  public int passengerCnt; // 승객수
}