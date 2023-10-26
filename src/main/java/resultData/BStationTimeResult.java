package resultData;

import domain.StationBAInfo;
import inputData.BusRouteData;
import inputData.TCardData;
import org.javatuples.Pair;

import java.time.LocalTime;
import java.util.*;

public class BStationTimeResult {// 정류장 단위의 시간대별 승하차

  public static void main(String[] args) throws Exception {
    BStationTimeResult result = new BStationTimeResult();
    result.createStationTimeResult();
  }


  public void expertData(List<StationBAInfo> list) {

    for (int i = 0; i < list.size(); i++) {
      System.out.println(list.get(i));
    }


  }

  public void createStationTimeResult() throws Exception {
    // BRS 파일 읽기 -> key: 노선ID, 순번
    // BufferedReader 사용해서 데이터 한 줄씩 읽어옴 & CSVReader -> 배열 형태
    HashMap<Integer, HashMap<Integer, BusRouteData>> readBRSFile = BusRouteData.ReadBRSFile();
    // Key: 노선ID, 정류장ID, value: 정류장정보 or 정류장 명칭만 필요해
    HashMap<Pair<Integer, Long>, String> busData = new HashMap<>();
    // 출력 -> [30300001, 230018001332]=선비마을
    List<StationBAInfo> vecResult = new ArrayList<StationBAInfo>();

    for (Map.Entry<Integer, HashMap<Integer, BusRouteData>> outerEntry : readBRSFile.entrySet()) {
      Integer routeId = outerEntry.getKey();
      HashMap<Integer, BusRouteData> entryValue = outerEntry.getValue();

      for (Map.Entry<Integer, BusRouteData> innerEntry : entryValue.entrySet()) {
        BusRouteData innerValue = innerEntry.getValue();

        Long stationId = innerValue.getStationId();
        String stationName = innerValue.getStationName();

        Pair<Integer, Long> key = new Pair<>(routeId, stationId);
        busData.getOrDefault(key, "0");
        busData.put(key, stationName);
      }
    }

    // TCD 파일 읽기 -> key: 카드ID, 환승ID, 환승횟수
    HashMap<Integer, HashMap<Integer, HashMap<Integer, TCardData>>> readTCDFile = TCardData.ReadTCDFile();

    // Key: 정류장Id, 시간 , TreeMap: 키는 정렬된 순서로 저장
    HashMap<Long, TreeMap<Integer, StationBAInfo>> result = new HashMap<>();

    // Key: 노선Id, 정류장ID, value: 정류장명
    Map<Long, String> stationName = new HashMap<>();

    // Key: 정류장ID, 시간대, value: 승하차 인원
    Map<Long, Integer> boardPassengerCnt = new HashMap<>();
    Map<Long, Integer> alightPassengerCnt = new HashMap<>();

    for (Map.Entry<Integer, HashMap<Integer, HashMap<Integer, TCardData>>> outerEntry : readTCDFile.entrySet()) {
      HashMap<Integer, HashMap<Integer, TCardData>> tranData = outerEntry.getValue();

      for (Map.Entry<Integer, HashMap<Integer, TCardData>> innerEntry : tranData.entrySet()) {
        HashMap<Integer, TCardData> transCntData = innerEntry.getValue();

        for (Map.Entry<Integer, TCardData> entry : transCntData.entrySet()) {
          StationBAInfo stationBAInfo = new StationBAInfo();
          TCardData object = entry.getValue(); // 카드데이터 한 줄씩 모두 들고있음

          // 결과데이터 컬럼: 정류장ID, 정류장명, 시간단위, 승차, 하차인원
          int routeId = object.getRouteId();
          Long boardSID = object.getBoardSID();
          Long alightSID = object.getAlightSID();

          // TCD: 노선ID, 정류장ID를 Key로 조합해서 BRS의 일치하는 정류장명 가져오기

          if (boardSID == 0 || alightSID == 0) {
            continue;
          }

          // 1. 노선ID, 정류장ID Key 만들기
          Pair<Integer, Long> boardKey = new Pair<>(routeId, boardSID);
          Pair<Integer, Long> alightKey = new Pair<>(routeId, alightSID);

          LocalTime boardTime = object.getBoardTime();
          LocalTime alightTime = object.getAlightTime();

          int boardTimeHour = boardTime.getHour();
          int alightTimeHour = alightTime.getHour();

          int passengerCnt = object.getPassengerCnt();

          // Key: 정류장ID, value: 승차 누적 // 여기 Key를 시간으로 잡음 안 되나
          int boardSum = boardPassengerCnt.getOrDefault(boardSID, 0);
          boardSum += passengerCnt;
          boardPassengerCnt.put(boardSID, boardSum);
          // boardPassengerCnt 출력 -> 230018002449=48...

          if (busData.containsKey(boardKey)) {
            String BStationName = busData.get(boardKey);
            stationName.put(boardSID, BStationName);
          }

          int alightSum = alightPassengerCnt.getOrDefault(alightSID, 0);
          alightSum += passengerCnt;
          alightPassengerCnt.put(alightSID, alightSum);

          if (busData.containsKey(alightKey)) {
            String AStationName = busData.get(alightKey);
            stationName.put(alightSID, AStationName);

            stationBAInfo.setStationId(alightSID);
            stationBAInfo.setStationName(AStationName);
            stationBAInfo.setTime(alightTimeHour);
            stationBAInfo.setAlightPassenger(alightSum);
            stationBAInfo.setBoardPassenger(boardSum);
          }
          vecResult.add(stationBAInfo);
        }
      }
    }
    expertData(vecResult);
  } // for문
//    System.out.println(result);

}
