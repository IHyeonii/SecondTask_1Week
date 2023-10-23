package service;

import entity.BusRouteUpdate;
import entity.District;
import entity.TCardUpdate;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DistrictUnit { //행정구역 단위 OD 통행량, 통행 시간
  public void getDistrictUnitInfo() throws Exception {
    District district = new District();
    Map<Integer, District> districtData = district.ReadDistrict();

    // key: 노선ID, 정류장ID, value: BusRouteUpdate
    HashMap<Integer, HashMap<Long, BusRouteUpdate>> busData = BusRouteUpdate.ReadBusData();

    //key: 정류장ID
    Map<Long, BusRouteUpdate> stationInfo = new HashMap<>();

    for(Map.Entry<Integer, HashMap<Long, BusRouteUpdate>> entry : busData.entrySet()) {
      for (int firstKey : busData.keySet()) {
        HashMap<Long, BusRouteUpdate> routeId = busData.get(firstKey);

        for (long secondKey : routeId.keySet()) {
          BusRouteUpdate values = routeId.get(secondKey);

          stationInfo.put(secondKey, values);
        }
      }
    }
//    System.out.println(stationInfo);
//    230018002449=BusRoute [routeId=30300082, seq=0, stationId=230018002449, stationName=은어송마을6단지, districtId=2501053]

    //key: 카드id, 환승id, 환승횟수를 HashMap으로 연결 -> vlaue: TransactionCard
    TCardUpdate transactionCard = new TCardUpdate();
    HashMap<Integer, HashMap<Integer, HashMap<Integer, TCardUpdate>>> cardData = transactionCard.ReadTCardData();

    // key: 출발행정구역id, value: 출발행정구역명
    Map<Integer, String> startDistrict = new HashMap<>();

    // key: 도착행정구역id, value: 도착행정구역명
    Map<Integer, String> endDistrict = new HashMap<>();

    // key: 행정구역id, value: 승객수
    Map<String, Integer> boardCnt = new HashMap<>();
    Map<String, Integer> alightCnt = new HashMap<>();

    for(Map.Entry<Integer, HashMap<Integer, HashMap<Integer, TCardUpdate>>> entry : cardData.entrySet()) {
      Integer key = entry.getKey();
      HashMap<Integer, HashMap<Integer, TCardUpdate>> value = entry.getValue(); // 카드데이터 모든 value

      for(int cardId : cardData.keySet()) {
        // 어떤 카드ID에 대한 환승ID< 환승횟수...
        HashMap<Integer, HashMap<Integer, TCardUpdate>> transData = cardData.get(cardId);

        for(int transId : transData.keySet()) {
          HashMap<Integer, TCardUpdate> transSeqData = transData.get(transId);

          for(int seqId : transSeqData.keySet()) {
            TCardUpdate cardObject = transSeqData.get(seqId);

            Long boardSID = cardObject.getBoardSID();
            Long alightSID = cardObject.getAlightSID();

            // 어디서 승차 어디서 하차 몇명이 승차 몇명이 하차 언제 승차 언제 하차

            // 1. key를 만들어: 행정구역id, 행정구역 명, value: 승객 수

            int boardDistrictId = 0; // 출발 정류장과 일치하는 행정구역 ID
            String boardDistrictName = null; // 출발 행정구역 명

            // stationInfo 키가 정류장id
            if(stationInfo.containsKey(boardSID)) {
              BusRouteUpdate busValue = stationInfo.get(boardSID);
              boardDistrictId = busValue.getDistrictId();
            }

            // 읍면동id랑 행정구역id 일치하면, 일치하는 name 가져와
            if (districtData.containsKey(boardDistrictId)) {
              boardDistrictName = districtData.get(boardDistrictId).getName();
            }

            // key: 출발행정구역id, value: 행정구역명
            startDistrict.put(boardDistrictId, boardDistrictName);

            int alightDistrictId = 0; // 도착 정류장과 일치하는 행정구역ID
            String alightDistrictName = null; // 도착 행정구역 명

            if(stationInfo.containsKey(alightSID)) {
              BusRouteUpdate busValue = stationInfo.get(alightSID);
              alightDistrictId = busValue.getDistrictId();
            }

            // 읍면동id랑 행정구역id 일치하면, 일치하는 name 가져와
            if (districtData.containsKey(alightDistrictId)) {
              alightDistrictName = districtData.get(alightDistrictId).getName();
            }

            // key: 도착행정구역id, value: 행정구역명
            endDistrict.put(alightDistrictId, alightDistrictName);

            //startDistrict, endDistrict를 key로 하는 승객 수 집계
            int ODPassengerCnt = cardObject.getPassengerCnt();

            // key: 행정구역id, 행정구역명 , value: 승객수
            String boardKey = boardDistrictId + "," + boardDistrictName;
            int boardSum = boardCnt.getOrDefault(boardKey, 0);
            boardSum += ODPassengerCnt;
            boardCnt.put(boardKey, boardSum);

            String alightKey = alightDistrictId + "," + alightDistrictName;
            int alightSum = alightCnt.getOrDefault(alightKey, 0);
            alightSum += ODPassengerCnt;
            alightCnt.put(alightKey, alightSum);
          }
        }
      }
    }
//    System.out.println(boardCnt);
//    System.out.println(alightCnt);

    Set<String> set = new HashSet<>();
    set.addAll(boardCnt.keySet());
    set.addAll(alightCnt.keySet());
//
//    StringBuilder sb = new StringBuilder();
//
//    for(String key : set) {
//      Integer boardSum = boardCnt.getOrDefault(key, 0);
//      Integer alightSum = alightCnt.getOrDefault(key, 0);
//
//      // 행정구역id, 행정구역명,
//      sb.append()
//    }

  }
  public static void main(String[] args) throws Exception {
    DistrictUnit districtUnit = new DistrictUnit();
    districtUnit.getDistrictUnitInfo();
  }
}

