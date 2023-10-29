package resultData;

import domain.StationInfo;
import inputData.BusRouteData;
import inputData.District3;
import inputData.TCardData;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class DistricODResult { //행정구역 단위 OD 통행량, 통행 시간
  public static void main(String[] args) throws Exception {
    DistricODResult odResult = new DistricODResult();
    odResult.createDistricODResult();
  }

  public void exportDistrictODFile(HashMap<Integer, HashMap<Integer, StationInfo>> result) throws Exception {
    String filePath = "C:\\Users\\ihyeon\\Desktop\\data\\output";
//    String fileNm = "DistricODResult_20201102.txt";
//    String fileNm = "DistricODResult_20201103.txt";
//    String fileNm = "DistricODResult_20201104.txt";
//    String fileNm = "DistricODResult_20201105.txt";
    String fileNm = "DistricODResult_20201106.txt";

    File file = new File(filePath, fileNm);
    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "utf-8"));

    String column = "bDistrictId,bDistrictNm,aDistrictId,aDistrictNm,psgrCnt,time";
    // header
    bw.write(column);
    bw.newLine();

    // data
    for (Map.Entry<Integer, HashMap<Integer, StationInfo>> dDstrictData : result.entrySet()) {
      HashMap<Integer, StationInfo> aDistrictData = dDstrictData.getValue();

      for (Map.Entry<Integer, StationInfo> baData : aDistrictData.entrySet()) {
        StationInfo odData = baData.getValue();

        StringBuilder sb = new StringBuilder();

        sb.append(odData.getBoardDistrict());
        sb.append(",").append(odData.getbDistrictNm());
        sb.append(",").append(odData.getAlightDistrict());
        sb.append(",").append(odData.getaDistrictNm());
        sb.append(",").append(odData.getPassengerCnt());
        sb.append(",").append(odData.getTime() / odData.getPassengerCnt());

        bw.write(sb.toString());
        bw.newLine();
      }
    }
    bw.close();
  }

  public void createDistricODResult() throws Exception {
    // 결과 데이터
    HashMap<Integer, HashMap<Integer, StationInfo>> result = new HashMap<>();

    // District3 파일 읽기, Key:
    District3 district = new District3();
    Map<Integer, District3> readDistrict = district.ReadDistrict();

    // BRS 파일 읽기 -> key: 노선ID, 순번
    HashMap<Integer, TreeMap<Integer, BusRouteData>> readBRSFile = BusRouteData.ReadBRSFile();

    // BRS -> key: 정류장ID, value: 행정구역정보
    HashMap<Long, Integer> districtData = new HashMap<>();

    for (Map.Entry<Integer, TreeMap<Integer, BusRouteData>> eleRouteData : readBRSFile.entrySet()) {
      Integer routeId = eleRouteData.getKey();
      TreeMap<Integer, BusRouteData> routeSeqData = eleRouteData.getValue();

      for (Map.Entry<Integer, BusRouteData> eleRouteSeq : routeSeqData.entrySet()) {
        BusRouteData routeStnData = eleRouteSeq.getValue();

        Long stationId = routeStnData.getStationId();
        int districtId = routeStnData.getDistrictId();

        districtData.put(stationId, districtId);
      }
    }

    // TCD 파일 읽기 -> key: 카드ID, 환승ID, 환승횟수
    HashMap<Integer, HashMap<Integer, HashMap<Integer, TCardData>>> readTCDFile = TCardData.ReadTCDFile();

    for (Map.Entry<Integer, HashMap<Integer, HashMap<Integer, TCardData>>> cardData : readTCDFile.entrySet()) {
      Integer cardId = cardData.getKey();
      HashMap<Integer, HashMap<Integer, TCardData>> tranData = cardData.getValue();

      for (Map.Entry<Integer, HashMap<Integer, TCardData>> seqData : tranData.entrySet()) {
        Integer transId = seqData.getKey();
        HashMap<Integer, TCardData> transCntData = seqData.getValue();

        for (Map.Entry<Integer, TCardData> tCardData : transCntData.entrySet()) {
          Integer transCnt = tCardData.getKey();
          TCardData tCardValue = tCardData.getValue();

          Long boardSID = tCardValue.getBoardSID();
          int bDistrictId = 0;
          String bDistrictNm = "";

          if (districtData.containsKey(boardSID)) {
            bDistrictId = districtData.get(boardSID); // 출발 행정구역Id
            District3 district3 = readDistrict.get(bDistrictId);
            bDistrictNm = district3.getName(); // 출발 행정구역명
          }

          Long alightSID = tCardValue.getAlightSID();
          int aDistrictId = 0;
          String aDistrictNm = "";

          if (districtData.containsKey(alightSID)) {
            aDistrictId = districtData.get(alightSID); // 도착 행정구역Id
            District3 district3 = readDistrict.get(aDistrictId);
            aDistrictNm = district3.getName(); // 도착 행정구역명
          }

          int passengerCnt = tCardValue.getPassengerCnt();

          LocalTime boardTime = tCardValue.getBoardTime();
          LocalTime alightTime = tCardValue.getAlightTime();

          // 분 단위로 변경
          int bTimeMinutes = (boardTime.getHour() * 60) + boardTime.getMinute();
          int aTimeMinutes = (alightTime.getHour() * 60) + alightTime.getMinute();

          // 총 이동시간
          int totalTime = aTimeMinutes - bTimeMinutes;

          if (boardSID == 0 || alightSID == 0) {
            continue;
          }

          // HashMap<Integer, HashMap<Integer, StationInfo>> result
          // Key: 출발 행정구역ID, 도착 행정구역ID, value: StationInfo 객체
          HashMap<Integer, StationInfo> emdData = result.getOrDefault(bDistrictId,
              new HashMap<Integer, StationInfo>());
          StationInfo stnData = emdData.get(aDistrictId);

          if (stnData == null) {
            stnData = new StationInfo();

            stnData.setBoardDistrict(bDistrictId);
            stnData.setbDistrictNm(bDistrictNm);
            stnData.setAlightDistrict(aDistrictId);
            stnData.setaDistrictNm(aDistrictNm);
          }
          stnData.setPassengerCnt(stnData.getPassengerCnt() + passengerCnt);
          stnData.setTime(stnData.getTime() + (totalTime * passengerCnt));

          emdData.put(aDistrictId, stnData);
          result.put(bDistrictId, emdData);
        }
      }
    }
    exportDistrictODFile(result);
  }
}

