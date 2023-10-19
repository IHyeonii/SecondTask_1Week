package service;

import entity.BusRoute;
import entity.TransactionCard;

import java.time.LocalTime;
import java.util.List;

public class BusStationUnit {// 정류장 단위의 시간대별  승하차 정보 -> txt파일 생성
  public static void main(String[] args) throws Exception {
    // 필요한거 = 정류장Id, 정류장 명칭, 1시간 단위의 시간, 승차인원, 하차인원
    // 입력데이터로 이 값들을 조합해서 텍스트파일로 내보내면 끝이야 (기능을 만드는게 아니다.)

    // 승차인원을 집계
    // 일단, 1일치 자료로만 만들어봐 -> 그 다음 5일치 데이터 처리 방법에 대해 생각


    for (int i = 5; i < 23; i++) {
      LocalTime start = LocalTime.of(i, 00);
      LocalTime end = LocalTime.of(i+1, 59);
//      System.out.println(start + " ~ " + end); // 05:00 ~ 06:59 ... 22:00 ~ 23:59

    }

    BusRoute test = new BusRoute();
    List<BusRoute> busRoute = test.ReadBusData();

    BusRoute busRoute1 = busRoute.get(0);
    Long routeId = busRoute1.getRouteId();
    System.out.println(busRoute1);
    System.out.println(routeId);



    TransactionCard transactionCard = new TransactionCard();

//    List<TransactionCard> transactionCards = transactionCard.ReadTCardData();

//    System.out.println(transactionCards);


    // 내가 지금 하려는게 뭔지 모르겠어 ㅠㅠㅠㅠㅠㅠㅠㅠ


  }
}
