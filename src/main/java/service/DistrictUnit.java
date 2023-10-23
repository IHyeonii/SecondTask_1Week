package service;

import entity.BusRouteUpdate;
import entity.District;
import entity.TCardUpdate;

import java.time.LocalTime;
import java.util.*;

public class DistrictUnit { //행정구역 단위 OD 통행량, 통행 시간
  public List<StringBuilder> getDistrictUnitInfo() throws Exception {
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
//		    System.out.println(stationInfo);
//		    230018002449=BusRoute [routeId=30300082, seq=0, stationId=230018002449, stationName=은어송마을6단지, districtId=2501053]

    //key: 카드id, 환승id, 환승횟수를 HashMap으로 연결 -> vlaue: TransactionCard
    TCardUpdate transactionCard = new TCardUpdate();
    HashMap<Integer, HashMap<Integer, HashMap<Integer, TCardUpdate>>> cardData = transactionCard.ReadTCardData();
//    System.out.println(cardData);

    // key: 행정구역id, value: 승객수
    Map<String, Integer> boardCnt = new HashMap<>();
    Map<String, Integer> boardTime = new HashMap<>();

    for(Map.Entry<Integer, HashMap<Integer, HashMap<Integer, TCardUpdate>>> entry : cardData.entrySet()) {
      Integer key = entry.getKey();
      HashMap<Integer, HashMap<Integer, TCardUpdate>> value = entry.getValue(); // 카드데이터 모든 value

      // 어떤 카드ID에 대한 환승ID< 환승횟수...
      HashMap<Integer, HashMap<Integer, TCardUpdate>> transData = cardData.get(key);

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
          if (stationInfo.containsKey(boardSID)) {
            BusRouteUpdate busValue = stationInfo.get(boardSID);
            boardDistrictId = busValue.getDistrictId();
          }

          // 읍면동id랑 행정구역id 일치하면, 일치하는 name 가져와
          if (districtData.containsKey(boardDistrictId)) {
            boardDistrictName = districtData.get(boardDistrictId).getName();
          }

          int alightDistrictId = 0; // 도착 정류장과 일치하는 행정구역ID
          String alightDistrictName = null; // 도착 행정구역 명

          if (stationInfo.containsKey(alightSID)) {
            BusRouteUpdate busValue = stationInfo.get(alightSID);
            alightDistrictId = busValue.getDistrictId();
          }

          // 읍면동id랑 행정구역id 일치하면, 일치하는 name 가져와
          if (districtData.containsKey(alightDistrictId)) {
            alightDistrictName = districtData.get(alightDistrictId).getName();
          }

          // 이거 하면 if문으로 전체 안 감싸도 된당
          if (boardDistrictId == 0 || alightDistrictId == 0) {
            continue;
          }

          // 출발정류장 도착정류장 모두 있는 경우만 승객 수 집계해

          //startDistrict, endDistrict를 key로 하는 승객 수 집계
          // 통행량
          int ODPassengerCnt = cardObject.getPassengerCnt();

          // key: 행정구역id, 행정구역명 , value: 승객수 -> 수집 기준이 키야
          String countKey = boardDistrictId + "," + boardDistrictName + "," + alightDistrictId + "," + alightDistrictName;
          int sum = boardCnt.getOrDefault(countKey, 0);
          sum += ODPassengerCnt;

          boardCnt.put(countKey, sum);

          // 이제... 여기다.... 이동시간 집계 추가
          LocalTime bTime = cardObject.getBoardTime();
          LocalTime aTime = cardObject.getAlightTime();

          int bTimeMinutes = (bTime.getHour() * 60) + bTime.getMinute();
          int aTimeMinutes = (aTime.getHour() * 60) + aTime.getMinute();

          // 이동시간
          int timeDifferenceMinutes = aTimeMinutes - bTimeMinutes;

          // 이동시간에 대해서 집계를 해야하는데...
          // 통행량은 sum
          // 이동시간은 전체 이동시간 / 전체 통행량
          // A -> B
          // 3개의 통행이 있음
          // 1) 1분, 2) 3분, 3) 10분
          // key, 1
          // (1 + 3) / 2 = 2 : key, 2
          // (2 + 10) / 3 = 4 : key, 4
          // 14 / 3 : key, 4.6666
          // 이동시간의 합 / 전체 통행량

          // OD의 이동시간을 전체 누적함
          int timeSum = boardTime.getOrDefault(countKey, 0);
          timeSum += timeDifferenceMinutes;

          boardTime.put(countKey, timeSum);

          // 9. 출발행정구역ID와 일치하는 버스정류장 데이터의 승차시간이 존재하는 경우, 교통카드 데이터에서 승차시간 정보를 가져와 LocalTime 변수에 담는다.
          // 10. 도착행정구역ID와 일치하는 버스정류장 데이터의 하차시간이 존재하는 경우, 교통카드 데이터에서 하차시간 정보를 가져와 LocalTime 변수에 담는다.
          // 11. 하차시간 - 승차시간으로 **이동 시간(단위와 형태의 기준 = 분단위**
        }
      }
    }

    Set<String> set = new HashSet<>();
    set.addAll(boardCnt.keySet());
    set.addAll(boardTime.keySet());
    // 한 줄에 대한 정보를 담고 있는 StringBuilder를 만들어서 배열로 결과를 반환한다.
    // 반환받는 곳에서ㄴ는 배열을 순회하며 한 줄씩 써주면 됨
    List<StringBuilder> result = new ArrayList<StringBuilder>();
    for(String key : set) {
      Integer boardSum = boardCnt.getOrDefault(key, 0);
      Integer timeSum = boardTime.getOrDefault(key, 0);

      // 전체 이동시간, 전체 통행량
      // 평균 이동시간을 계산할 수 있음
      int averageTime = timeSum / boardSum;

      // 행정구역id, 행정구역명,
      StringBuilder sb = new StringBuilder();
      sb.append(key).append(",").append(boardSum).append(",").append(averageTime);
//		      System.out.println(key + "," + boardSum + "," + alightSum);
      result.add(sb);
    }
    return result;

  }
  public static void main(String[] args) throws Exception {
    DistrictUnit districtUnit = new DistrictUnit();
    districtUnit.getDistrictUnitInfo();
  }
}

