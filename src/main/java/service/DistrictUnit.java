package service;

import entity.BusRoute;
import entity.District;
import entity.TransactionCard;

import java.util.Map;
import java.util.Set;

public class DistrictUnit { //행정구역 단위 통행량, 통행 시간
  //1. 교통카드 데이터: 출발정류장ID와 일치하는 버스정류장 데이터의 행정구역 정보
  //1-1. 교통카드 데이터가 가지고 있는, 승객 수 집계
  //2. 출발 행정구역 정보와 일치하는, 읍면동 데이터의 행정구역명

  // 누적해서 키를 기준으로 컬럼명을 만들어 나간다 ..


  // 노선-정류장별 승차하, 재차인원
  public void getDistrictUnitInfo() throws Exception {

    District district = new District();
    Map<Integer, District> districtData = district.ReadDistrict();

    TransactionCard transactionCard = new TransactionCard();
    Map<String, TransactionCard> tCardInfo = transactionCard.ReadTCardData(); // 카드 데이터를 읽고
    Set<String> cardKey = tCardInfo.keySet(); //Set: 중복을 허용 X

    // 2. 출발정류장id == 버스데이터 정류장id -> 정류장 명 들고온다.
    BusRoute test = new BusRoute();
    Map<String, BusRoute> busData = test.ReadBusData();

    for(String key : cardKey) {
      TransactionCard cardValue = tCardInfo.get(key); // 카드데이터 모든 데이터 포함

      Long routeId = cardValue.getRouteId(); //노선ID
      Long boardSID = cardValue.getBoardSID(); //출발정류장ID
      Long alightSID = cardValue.getAlightSID(); //도착정류장ID

      // 카드데이터의 노선ID, 출발정류장ID 형태의 Key 생성
      String routeAndBoard = routeId + "," + boardSID;
      String routeAndAlight = routeId + "," + alightSID;

      Long startDistrict = 0L; // 출발정류장ID와 일치하는 행정구역ID를 담고있음
      Long endDistrict = 0L;

      if(busData.containsKey(routeAndBoard)) {
        startDistrict = busData.get(routeAndBoard).getDistrictId(); // 행정구역ID
      }

      if(busData.containsKey(routeAndAlight)) {
        endDistrict = busData.get(routeAndAlight).getDistrictId();
      }

      //행정구역ID와 일치하는 행정구역명 가져오기
      String startDstrictName ="";
      String endDstrictName ="";


    }
  }




}

