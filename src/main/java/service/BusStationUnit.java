package service;

import entity.BusRoute;
import entity.TransactionCard;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BusStationUnit {// 정류장 단위의 시간대별  승하차 정보 -> txt파일 생성
  public void getStationInfo() throws Exception {
    // 1. 카드데이터의 정류장ID를 통해 버스데이터의 정류장명을 가져오고 싶다.
    // 기준: 카드데이터, 참조: 버스데이터

    TransactionCard transactionCard = new TransactionCard();
    Map<String, TransactionCard> tCardInfo = transactionCard.ReadTCardData(); // 카드 데이터를 읽고
    Set<String> cardKey = tCardInfo.keySet(); //Set: 중복을 허용 X

    // 2. 출발정류장id == 버스데이터 정류장id -> 정류장 명 들고온다.
    BusRoute test = new BusRoute();
    Map<String, BusRoute> readData = test.ReadBusData();

    // 키: 정류장 Id, 값: 정류장 명칭
    // stationInfo 변수를 어디서 어떻게 사용하려고 만든거에요
    // 정류장ID: 정류장명
    // 승차, 하차, 몇명이탔는지, 언제탔는지 정보가 없음

    Map<String, Integer> mapBoardCnt = new HashMap<>();
    Map<String, Integer> mapAlightCnt = new HashMap<>();
    // 카드데이터 담고있는 Map 순회하며 Value: 노선id , 출발정류장 Id 랑 일치하는, 버스노선 데이터의 Key를 찾아서, 그 키의 정류장 명을 가져온다.
    // 카드데이터 키를 다 가져와서
    for (String key : cardKey) {
      TransactionCard value = tCardInfo.get(key); // 카드데이터의 value 다 들고옴

      long routeID = value.getRouteId();
      long boardSID = value.getBoardSID();
      long alightSID = value.getAlightSID();
      int passengerCnt = value.getPassengerCnt();

      String value3 = routeID + "," + boardSID;
      String boardSNm = "";
      if (readData.containsKey(value3)) {
        boardSNm = readData.get(value3).getStationName();
      }

      String value4 = routeID + "," + alightSID;
      String alightSNm = "";
      if(readData.containsKey(value4)) {
        alightSNm = readData.get(value4).getStationName();
      }

      LocalTime sTime = value.getBoardTime();
      int sTimeCode = getTimecode(sTime);
      LocalTime aTime = value.getAlightTime();
      int aTimeCode = getTimecode(aTime);

      // 어디서 승차 어디서 하차 몇명이 승차 몇명이 하차 언제 승차 언제 하차
      String sKey = boardSID + "," + boardSNm + "," + sTimeCode;
      int sumS = mapBoardCnt.getOrDefault(sKey, 0);
      sumS += passengerCnt;
      mapBoardCnt.put(sKey, sumS);

      String aKey = alightSID + "," + alightSNm + "," + aTimeCode;
      int sumA = mapAlightCnt.getOrDefault(aKey, 0);
      sumA += passengerCnt;
      mapAlightCnt.put(aKey, sumA);
    }

    // 승차, 하차 따로 집계함
    Set<String> set = new HashSet<>();
    set.addAll(mapBoardCnt.keySet());
    set.addAll(mapAlightCnt.keySet());

    for(String key : set) {
      int sumB = mapBoardCnt.getOrDefault(key, 0);
      int sumA = mapAlightCnt.getOrDefault(key, 0);

      System.out.println(key + "," + sumB + "," + sumA);
    }
//		for(String key : mapAlightCnt.keySet()) {
//			int sum = mapAlightCnt.get(key);
//			System.out.println(key + "," + sum);
//		}
  }

  public static void main(String[] args) throws Exception {
    // 필요한거 = 정류장Id, 정류장 명칭, 1시간 단위의 시간, 승차인원, 하차인원
    // 입력데이터로 이 값들을 조합해서 txt 파일로 내보내면 끝이야 (기능을 만드는게 아니다.)
    // 승차인원은 집계해야 함
    BusStationUnit busUnit = new BusStationUnit();
    busUnit.getStationInfo();
  }

  public int getTimecode(LocalTime time) {
    int timeCode = time.getHour();

    return timeCode;
  }
}
