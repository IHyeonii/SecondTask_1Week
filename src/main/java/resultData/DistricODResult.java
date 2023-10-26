package resultData;

import inputData.BusRouteData;
import inputData.TCardData;
import org.javatuples.Pair;

import java.util.HashMap;
import java.util.Map;

public class DistricODResult { //행정구역 단위 OD 통행량, 통행 시간
  public void getDistrictUnitInfo() throws Exception {
    // BRS 파일 읽기 -> key: 노선ID, 순번
    // BufferedReader 사용해서 데이터 한 줄씩 읽어옴 & CSVReader -> 배열 형태
    HashMap<Integer, HashMap<Integer, BusRouteData>> readBRSFile = BusRouteData.ReadBRSFile();

    // Key: 노선ID, 정류장ID, value: seq
    HashMap<Pair<Integer, Long>, TCardData> tcdData = new HashMap<>();

    for(Map.Entry<Integer, HashMap<Integer, BusRouteData>> outerEntry : readBRSFile.entrySet()) {
      Integer routeId = outerEntry.getKey();
      HashMap<Integer, BusRouteData> entryValue = outerEntry.getValue();

      for(Map.Entry<Integer, BusRouteData> innerEntry : entryValue.entrySet()) {
        Integer seq = innerEntry.getKey();
        BusRouteData innerValue = innerEntry.getValue();

        int districtId = innerValue.getDistrictId();



      }
    }
  }

}

