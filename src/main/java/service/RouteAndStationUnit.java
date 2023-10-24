package service;

import entity.BusRouteUpdate;
import entity.TCardUpdate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RouteAndStationUnit { //노선-정류장별 승하차, 재차인원
  public static void main(String[] args) throws Exception {
    RouteAndStationUnit stationUnit = new RouteAndStationUnit();
    stationUnit.getPassengerByType();
  }
  public void getPassengerByType() throws Exception {
    // 1. key: (노선ID,순번)형태 / value: BusRouteUpdate
    Map<String, BusRouteUpdate> busData = BusRouteUpdate.AddSeqBusData();

//    Map<String, Integer> busInfo = new HashMap<>();
//
//    for(Map.Entry<String, BusRouteUpdate> map : busData.entrySet()) {
//      String routeAndSeq = map.getKey();
//      BusRouteUpdate value = map.getValue();
//      int districtId = value.getDistrictId();
//
//      busInfo.put(routeAndSeq, districtId);
//    }

    // busData 값이랑 일치하는 출발, 도착정류장ID 가져오기
    TCardUpdate transactionCard = new TCardUpdate();
    HashMap<Integer, HashMap<Integer, HashMap<Integer, TCardUpdate>>> cardData = transactionCard.ReadTCardData();

    // 승차, 하차, 재차 승객 수 집계
    Map<String, Integer> boardCnt = new HashMap<>();
    Map<String, Integer> alightCnt = new HashMap<>();
    Map<String, Integer> remainCnt = new HashMap<>();

    for (Map.Entry<Integer, HashMap<Integer, HashMap<Integer, TCardUpdate>>> outerEntry : cardData.entrySet()) {
      Integer outerKey = outerEntry.getKey(); // 카드id
      HashMap<Integer, HashMap<Integer, TCardUpdate>> innerMap = outerEntry.getValue();

      // 내부 맵 순회
      for (Map.Entry<Integer, HashMap<Integer, TCardUpdate>> transData : innerMap.entrySet()) {
        Integer innerKey = transData.getKey(); // 트랜잭션id
        HashMap<Integer, TCardUpdate> value = transData.getValue();

        for (Map.Entry<Integer, TCardUpdate> transCnt : value.entrySet()) {
          Integer transCntKey = transCnt.getKey(); // 환승횟수
          TCardUpdate cardObject = transCnt.getValue(); // TCardUpdate 객체

          Long boardSID = cardObject.getBoardSID();
          Long alightSID = cardObject.getAlightSID();

          // 출발, 도착 둘 다 있는 경우만 집계할거야
          if (boardSID == 0 || alightSID == 0) {
            continue;
          }

          // 2. 카드 데이터 -> busData value(정류장Id)와 일치하는 출발,도착 정류장ID 승객 수
          // 이게 결국....
          // 4. 재차인원 : 2-3값의 누적

          Set<String> boardKey = null; // 노선ID, 순번
          Set<String> alightKey = null;

          // Map은 (Key, Value)
          // Map에서 containsValue: value에 파라미터로 넘어오는 값이 존재하는가? 여부를 확인
          // Map에서의 Value의 자료형과 containsValue의 파라미터의 자료형은 같아야됨
          // BusRouteUpdate... 랑 Long랑 비교를 했음....
          // Key: 노선ID, 정류장ID
          // 같은 노선에서 중복된 정류장ID가 나올 수 있음
          // 노선ID 기준으로 순번이 고려된 정류장 정보
          if (busData.containsValue(boardSID)) {
            boardKey = busData.keySet();
          }

          if (busData.containsValue(alightSID)) {
            alightKey = busData.keySet();
          }

          // 수집기준(key): 노선ID, 순번, 정류장ID
          // value: 승차인원, 하차인원, 재차인원
          int objectPassenger = cardObject.getPassengerCnt();

          String key1 = boardKey + "," + boardSID;
          int boardSum = boardCnt.getOrDefault(key1, 0);
          boardSum += objectPassenger;

          boardCnt.put(key1, boardSum);

          String key2 = alightKey + "," + alightSID;
          int alightSum = alightCnt.getOrDefault(key2, 0);
          alightSum += objectPassenger;

          alightCnt.put(key2, alightSum);

          // 재차인원
          // 여기서 집계만 하고
          int remainPassenger = boardSum - alightSum;
          int remainSum = remainCnt.getOrDefault(key1, 0);
          remainSum += remainPassenger;

          remainCnt.put(key1,remainSum); // 아마 아닐듯 !!!!!!
        }
      }
    } // for문 끝

    //여기서 set
  } // 메소드 끝
}





