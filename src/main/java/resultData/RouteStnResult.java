package resultData;

import domain.BusStationBAP;
import inputData.BusRouteData;
import inputData.TCardData;
import org.javatuples.Pair;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.*;

public class RouteStnResult { // 노선-정류장별 승하차, 재차
  public static void main(String[] args) throws Exception {
    RouteStnResult stnResult = new RouteStnResult();
    stnResult.createRouteStnResult();
  }
  public void exportRouteStnFile(HashMap<Integer, TreeMap<Integer, BusStationBAP>> result) throws Exception {
    String filePath = "C:\\Users\\ihyeon\\Desktop\\data\\output";
//    String fileNm = "RouteStnResult_20201102.txt";
//    String fileNm = "RouteStnResult_20201103.txt";
//    String fileNm = "RouteStnResult_20201104.txt";
//    String fileNm = "RouteStnResult_20201105.txt";
    String fileNm = "RouteStnResult_20201106.txt";

    File file = new File(filePath, fileNm);
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));

    String column = "routeId,seq,stationId,boardPsgr,alightPsgr,totalPsgr";
    // header
    bw.write(column);
    bw.newLine();

    // data
    for (Map.Entry<Integer, TreeMap<Integer, BusStationBAP>> routeData : result.entrySet()) {
      TreeMap<Integer, BusStationBAP> stnData = routeData.getValue();

      for (Map.Entry<Integer, BusStationBAP> bapData : stnData.entrySet()) {
        BusStationBAP object = bapData.getValue();

        StringBuilder sb = new StringBuilder();

        sb.append(object.getRouteId());
        sb.append(",").append(object.getSeq());
        sb.append(",").append(object.getStationId());
        sb.append(",").append(object.getbPassenger());
        sb.append(",").append(object.getaPassenger());
        sb.append(",").append(object.getReaminPassenger());

        bw.write(sb.toString());
        bw.newLine();
      }
    }
    bw.close();
  }

  public void createRouteStnResult() throws Exception {
    // 결과데이터 Key: 노선Id , TreeMap Key: 순번
    HashMap<Integer, TreeMap<Integer, BusStationBAP>> result = new HashMap<>();

    // BRS 파일 읽기 -> key: 노선ID, 순번
    HashMap<Integer, TreeMap<Integer, BusRouteData>> readBRSFile = BusRouteData.ReadBRSFile();

    // Key: 노선Id, 정류장Id - value: seq
    HashMap<Pair<Integer, Long>, Integer> bRouteData = new HashMap<>();

    for (Map.Entry<Integer, TreeMap<Integer, BusRouteData>> eleRouteData : readBRSFile.entrySet()) {
      int routeId = eleRouteData.getKey();

      TreeMap<Integer, BusRouteData> routeSeqData = eleRouteData.getValue();
      for (Map.Entry<Integer, BusRouteData> eleRouteSeq : routeSeqData.entrySet()) {

        int seq = eleRouteSeq.getKey();
        BusRouteData brsData = eleRouteSeq.getValue();

        Long stationId = brsData.getStationId();

        Pair<Integer, Long> key = new Pair<>(routeId, stationId);
        bRouteData.put(key, seq);

        TreeMap<Integer, BusStationBAP> frame = result.getOrDefault(routeId, new TreeMap<>()); //frame :: 맘에 안들어

        BusStationBAP bap = frame.getOrDefault(seq, new BusStationBAP());  // 이유는 생각해 보네숑
        bap.setRouteId(routeId);
        bap.setSeq(seq);
        bap.setStationId(stationId);

        frame.put(seq, bap);
        result.put(routeId, frame);
      }
    }

    // TCD 파일 읽기 -> key: 카드ID, 환승ID, 환승횟수
    HashMap<Integer, HashMap<Integer, HashMap<Integer, TCardData>>> readTCDFile = TCardData.ReadTCDFile();

    for (Map.Entry<Integer, HashMap<Integer, HashMap<Integer, TCardData>>> cardData : readTCDFile.entrySet()) {
      HashMap<Integer, HashMap<Integer, TCardData>> eleCardData = cardData.getValue();

      for (Map.Entry<Integer, HashMap<Integer, TCardData>> eleTranData : eleCardData.entrySet()) {
        HashMap<Integer, TCardData> transCntData = eleTranData.getValue();

        for (Map.Entry<Integer, TCardData> tranCnt : transCntData.entrySet()) {
          TCardData tCardData = tranCnt.getValue();

          int routeId = tCardData.getRouteId();
          Long boardSID = tCardData.getBoardSID();
          Long alightSID = tCardData.getAlightSID();
          int passenger = tCardData.getPassengerCnt();

          // TCD 정류장ID와 일치하는 BRS의 정류장명 가져오기
          if (boardSID == 0 || alightSID == 0) {
            continue;
          }

          // 버스노선ID, 정류장ID 와 일치하는 카드데이터의 승하차 인원 누적
          Pair<Integer, Long> boardKey = new Pair<>(routeId, boardSID);
          Pair<Integer, Long> alightKey = new Pair<>(routeId, alightSID);

          // bRouteData -> Key: Pair(노선Id, 정류장ID) - value: 순번
          int bSeq = 0;
          int aSeq = 0;

          if (bRouteData.containsKey(boardKey)) {
            bSeq = bRouteData.get(boardKey);
          }

          if (bRouteData.containsKey(alightKey)) {
            aSeq = bRouteData.get(alightKey);
          }

          if(bSeq ==0 || aSeq == 0) {
            continue;
          }

          //0만잘못된게 아니라
          //BRS 파일  안에 없는 노선 ID도 잘못된 데이터이다. !!!!!!!!!!!!!!!!!!!

          // 노선ID, 순번이 집계 기준
          TreeMap<Integer, BusStationBAP> sequence = result.get(routeId);

          if(sequence == null) {
            continue;
          }

          BusStationBAP bCount = sequence.get(bSeq);
          if(bCount == null )  {
            continue;
          }
          bCount.setbPassenger(bCount.getbPassenger() + passenger); // 승차인원 누적
          bCount.setReaminPassenger(bCount.getbPassenger());
          sequence.put(bSeq, bCount);

          BusStationBAP aCount = sequence.get(aSeq);
          if( aCount == null )  {
            continue;
          }
          aCount.setaPassenger(aCount.getaPassenger() + passenger); // 하차인원 누적
          sequence.put(aSeq, aCount);
          result.put(routeId, sequence);
        }
      }
    }
    reaminPassengerCnt(result, readBRSFile);
//		exportRouteAndStnFile(result);
  }

  public void reaminPassengerCnt(HashMap<Integer, TreeMap<Integer, BusStationBAP>> result, HashMap<Integer, TreeMap<Integer, BusRouteData>> readBRSFile) throws Exception{
    for (Map.Entry<Integer, TreeMap<Integer, BusRouteData>> eleRouteData : readBRSFile.entrySet()) {
      int routeId = eleRouteData.getKey();
      TreeMap<Integer, BusRouteData> routeSeqData = eleRouteData.getValue();

      for (Map.Entry<Integer, BusRouteData> eleRouteSeq : routeSeqData.entrySet()) {
        int seq = eleRouteSeq.getKey();
        BusRouteData brsData = eleRouteSeq.getValue();

        TreeMap<Integer, BusStationBAP> frame = result.get(routeId);
        BusStationBAP bap = frame.get(seq);
        int aCnt = bap.getaPassenger();
        int remainCnt = bap.getReaminPassenger();

        BusStationBAP bap2 = frame.get(seq-1);
        if( bap2 == null )  {
          continue;
        }

        int beforPassenger = bap2.getReaminPassenger();

        bap.setReaminPassenger((remainCnt + beforPassenger) - aCnt);

        frame.put(seq, bap);
        result.put(routeId, frame);
      }
    }
    exportRouteStnFile(result);
  }
}