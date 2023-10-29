package resultData;

import domain.StationBAInfo;
import inputData.BRouteData;
import inputData.BusRouteData;
import inputData.TCardData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class StnTimeResult {// 정류장 단위의 시간대별 승하차
  public static void main(String[] args) throws Exception {
    StnTimeResult stnTimeResult = new StnTimeResult();
    stnTimeResult.createStnTimeResult();
  }
  public void exportData(HashMap<Long, TreeMap<Integer, StationBAInfo>> list) throws Exception {
    String filePath = "C:\\Users\\ihyeon\\Desktop\\data\\output";
//    String fileNm = "StnTimeResult_20201102.txt";
//    String fileNm = "StnTimeResult_20201103.txt";
//    String fileNm = "StnTimeResult_20201104.txt";
//    String fileNm = "StnTimeResult_20201105.txt";
    String fileNm = "StnTimeResult_20201106.txt";

    File file = new File(filePath, fileNm);
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));

    String column = "stationId,stationNm,time,boardPsgr,alightPsgr";
    // header
    bw.write(column);
    bw.newLine();

    // data
    for (Map.Entry<Long, TreeMap<Integer, StationBAInfo>> stData : list.entrySet()) {
      TreeMap<Integer, StationBAInfo> timeData = stData.getValue();

      for (Map.Entry<Integer, StationBAInfo> baData : timeData.entrySet()) {
        StationBAInfo stnBAData = baData.getValue();

        StringBuilder sb = new StringBuilder();

        sb.append(stnBAData.getStationId());
        sb.append(",").append(stnBAData.getStationName());
        sb.append(",").append(stnBAData.getTime());
        sb.append(",").append(stnBAData.getBoardPassenger());
        sb.append(",").append(stnBAData.getAlightPassenger());

        bw.write(sb.toString());
        bw.newLine();
      }
    }
    bw.close();
  }

  public void createStnTimeResult() throws Exception {
    // 결과 데이터 -> Key: 정류장Id, 시간
    HashMap<Long, TreeMap<Integer, StationBAInfo>> result = new HashMap<>();

    // BRS 파일 읽기 -> key: 노선ID, 순번
    // BufferedReader 사용해서 데이터 한 줄씩 읽어옴 & CSVReader -> 배열 형태
    HashMap<Integer, TreeMap<Integer, BusRouteData>> readBRSFile = BusRouteData.ReadBRSFile();

    // 버스데이터의 Key: 정류장ID, Value: 정류장명
    HashMap<Long, String> stationNmData = new HashMap<>();

    for (Map.Entry<Integer, TreeMap<Integer, BusRouteData>> eleRouteData : readBRSFile.entrySet()) {
      Integer routeId = eleRouteData.getKey();
      TreeMap<Integer, BusRouteData> routeSeqData = eleRouteData.getValue();

      for (Map.Entry<Integer, BusRouteData> eleRouteSeq : routeSeqData.entrySet()) {
        BusRouteData routeStnData = eleRouteSeq.getValue();

        Long stationId = routeStnData.getStationId();
        String stationName = routeStnData.getStationName();

        stationNmData.put(stationId, stationName);
      }
    }

    // TCD 파일 읽기 -> key: 카드ID, 환승ID, 환승횟수
    HashMap<Integer, HashMap<Integer, HashMap<Integer, TCardData>>> readTCDFile = TCardData.ReadTCDFile();

    for (Map.Entry<Integer, HashMap<Integer, HashMap<Integer, TCardData>>> outerEntry : readTCDFile.entrySet()) {
      HashMap<Integer, HashMap<Integer, TCardData>> tranData = outerEntry.getValue();

      for (Map.Entry<Integer, HashMap<Integer, TCardData>> innerEntry : tranData.entrySet()) {
        HashMap<Integer, TCardData> transCntData = innerEntry.getValue();

        for (Map.Entry<Integer, TCardData> entry : transCntData.entrySet()) {
          TCardData cardData = entry.getValue(); // 카드데이터 한 줄씩 모두 들고있음

          // 결과데이터 컬럼: 정류장ID, 정류장명, 시간단위, 승차, 하차인원
          Long boardSID = cardData.getBoardSID();
          Long alightSID = cardData.getAlightSID();

          // TCD 정류장ID와 일치하는 BRS의 정류장명 가져오기
          if (boardSID == 0 || alightSID == 0) {
            continue;
          }

          LocalTime boardTime = cardData.getBoardTime();
          LocalTime alightTime = cardData.getAlightTime();

          int boardTimeHour = boardTime.getHour();
          int alightTimeHour = alightTime.getHour();

          int passengerCnt = cardData.getPassengerCnt();

          //승차 정류장 값 업데이트 하기
          // 1. result의 Key에 boardSID가 있으면, boardSid의 값(TreeMap<Integer, StationBAInfo>)을 반환하고, 없으면 TreeMap 생성해
          TreeMap<Integer, StationBAInfo> boardTimeData = result.getOrDefault(boardSID, new TreeMap<Integer, StationBAInfo>());

          // 2. boardSid의 값이 담긴 boardTimeData에서 key가 boardTimeHour인 값을 boardStnData 변수에 담아
          StationBAInfo boardStnData = boardTimeData.get(boardTimeHour);
          if (boardStnData == null) { // 값이 널이면
            boardStnData = new StationBAInfo(); // StationBAInfo 생성해주고
            String stnNm = stationNmData.getOrDefault(boardSID, ""); // stationNmData 에서 키가 boardSID인 값(정류장 명) 가져오고, 키가 없으면 공백을 넣어 ?

            boardStnData.setStationId(boardSID);
            boardStnData.setStationName(stnNm);
            boardStnData.setTime(boardTimeHour);
          }
          boardStnData.setBoardPassenger(boardStnData.getBoardPassenger() + passengerCnt);

          boardTimeData.put(boardTimeHour, boardStnData);
          result.put(boardSID, boardTimeData);

          //하차 정류장 값 업데이트
          //result에 키를 넣어줘서 TreeMap을 반환한다.
          TreeMap<Integer, StationBAInfo> alightTimeData = result.getOrDefault(alightSID, new TreeMap<Integer, StationBAInfo>());

          // 해당 키인 시간대를 넣어주면, 반환값인 StationBAInfo 클래스를 반환한다.
          StationBAInfo alightStnData = alightTimeData.get(alightTimeHour);

          // 만약에, alightStnData가 비어있으면, StationBAInfo 클래스를 생성하고
          if (alightStnData == null) {
            alightStnData = new StationBAInfo();
            String stationName = stationNmData.getOrDefault(alightSID, "");

            alightStnData.setStationId(alightSID);
            alightStnData.setStationName(stationName);
            alightStnData.setTime(alightTimeHour);
          }
          alightStnData.setAlightPassenger(alightStnData.getAlightPassenger() + passengerCnt);

          alightTimeData.put(alightTimeHour, alightStnData);
          result.put(alightSID, alightTimeData);
        }
      }
    }
    exportData(result);
  }
}
