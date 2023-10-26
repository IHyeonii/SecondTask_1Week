package resultData;

import entity.BRouteData;
import entity.TCardUpdate;
import org.javatuples.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class RouteStationResult {
  public static void main(String[] args) throws Exception {
    RouteStationResult stationUnit = new RouteStationResult();
    stationUnit.getPassengerByType();
  }

  // 1. 노선의 승하차부터 전부 구하고
  // 2. 재차는 새로운 반복문에서 구해

  public void getPassengerByType() throws Exception {
    // 같은 노선에서 중복된 정류장ID가 나올 수 있음 -> 버스가 돌아가는 경우
    // 노선ID 기준으로 순번이 고려된 정류장 정보가 필요하다.
//		HashMap<Pair<Integer, Integer> ,       HashSet<Integer>>> bbb;
//		                          //  노선ID  , 정류장ID                      Seq
//
//		HashMap<Integer, HashMap<Integer. BusStationBAP>> aaa;
//		              //   노선ID                         Seq          Data
//
//		BusStationBAP

    // 1. 버스데이터 읽어서
    HashMap<Integer, HashMap<Long, BRouteData>> readBusData = BRouteData.ReadBRSData();

    // 2. 노선, 정류장을 키에 담고
    HashMap<Pair<Integer, Long>, HashSet<Integer>> stationInfo = new HashMap<>(); // 노선ID, 정류장ID - value: seq

    //stationInfo에 데이터 추가하고, 꺼내고, 저장...

    //  key: 노선ID, 정류장ID - value: 객체
//		Map<String, BusRouteUpdate> stationIfo = new HashMap<>();

    for (Map.Entry<Integer, HashMap<Long, BRouteData>> outerEntry : readBusData.entrySet()) {
      Integer outerKey = outerEntry.getKey(); // 노선ID
      HashMap<Long, BRouteData> innerMap = outerEntry.getValue(); //

      for (Map.Entry<Long, BRouteData> innerEntry : innerMap.entrySet()) {
        Long innerKey = innerEntry.getKey(); // 정류장ID
        BRouteData busObject = innerEntry.getValue();

        int seq  = busObject.getSeq();

        // 이렇게 데이터 넣기
//        HashSet<Inetger> aaa = stationInfo.getOrDefault(new Pair<Integer, Long>(outerKey, innerKey), new HashSet<>());
//        aaa.add(seq);


      }
    }

//		System.out.println(stationIfo);

    TCardUpdate transactionCard = new TCardUpdate();
    HashMap<Integer, HashMap<Integer, HashMap<Integer, TCardUpdate>>> cardData = transactionCard.ReadTCardData();

    // 최종은 노선ID <정류장ID , 승하차를 sum>
    HashMap<Integer, HashMap<Long, Integer>> typeInfo = new HashMap<>();

    Map<String, Integer> mapBoardCnt = new HashMap<>();
    Map<String, Integer> mapAlightCnt = new HashMap<>();

    for (Map.Entry<Integer, HashMap<Integer, HashMap<Integer, TCardUpdate>>> outerEntry : cardData.entrySet()) {
      Integer outerKey = outerEntry.getKey(); // 카드ID
      HashMap<Integer, HashMap<Integer, TCardUpdate>> innerEntry = outerEntry.getValue();

      for (Map.Entry<Integer, HashMap<Integer, TCardUpdate>> inner : innerEntry.entrySet()) {
        Integer innerKey = inner.getKey(); // 트랜잭션ID
        HashMap<Integer, TCardUpdate> transCnt = inner.getValue();

        for (Map.Entry<Integer, TCardUpdate> object : transCnt.entrySet()) {
          Integer objectKey = object.getKey(); // 환승횟수
          TCardUpdate objectValues = object.getValue(); // 이걸로 카드데이터 값 들고올 수 있음

          int routeId = objectValues.getRouteId();
          long boardSID = objectValues.getBoardSID();
          long alightSID = objectValues.getAlightSID();

          // 출발, 도착정류장 모두 있는 경우
          if (boardSID == 0 || alightSID == 0) {
            continue;
          }

          // 노선별 승차, 하차인원 먼저 구해
          //stationIfo ->  key: 노선ID, 정류장ID . value: 객체

          String boardKey = routeId + "," + boardSID;
          String alightKey = routeId + "," + alightSID;

          int boardSeq = 0; // 이거랑 노선아이디 조합해서 또 키로 만들어야 하나
          int alightSeq = 0;
//
//          if(stationIfo.containsKey(boardKey)) {
//            boardSeq = stationIfo.get(boardKey).getSeq();
//          }
//
//          if(stationIfo.containsKey(alightKey)) {
//            alightSeq = stationIfo.get(alightKey).getSeq();
//          }

          int passenger = objectValues.getPassengerCnt();

          // 노선Id,  순번, 정류장ID로 키 만들어서 승하차 집계
          String boardCntKey = routeId + "," + boardSeq + "," +boardSID;
          String alightCntKey = routeId + "," + alightSeq + "," +alightSID;

          int boardSum = mapBoardCnt.getOrDefault(boardCntKey, 0);
          boardSum += passenger;
          mapBoardCnt.put(boardCntKey, boardSum);

          int alightSum = mapAlightCnt.getOrDefault(alightCntKey, 0);
          alightSum += passenger;
          mapAlightCnt.put(alightCntKey,  alightSum);

        }
      }
    } // for문 끝 ~~

  }
}
