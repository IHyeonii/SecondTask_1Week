package education;

import entity.BusRoute;
import entity.District;
import entity.TransactionCard;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class Boom {
  public void main1() {
    String charSetDistrict = "UTF-8";
    String charSetBRS = "UTF-8";
    String charSetTCD = "UTF-8";

    boolean isHaveFileHeadDistrict = true;
    boolean isHaveFileHeadBRS = true;
    boolean isHaveFileHeadTCD = true;

    // Key: 행정구역ID, Value: 행정구역 명
    String districtFilePath = "";
    //행정구역 정보
    HashMap<Integer, District> districtEmd = new HashMap<>(); //읍면동ID,

    readDistrictFile(isHaveFileHeadDistrict, districtFilePath, charSetDistrict, districtEmd);

    String brsFilePath = "";

    HashMap<Long, HashMap<Long, BusRoute>> busRouteStation = new HashMap<>(); // 노선id-정류장id
    HashMap<Long, BusStationInfo> busStationMaster = new HashMap<>();
    readBRSFile(isHaveFileHeadBRS, brsFilePath, charSetBRS, busRouteStation, busStationMaster);

    String tcdFilePath = "";
    HashMap<Long, HashMap<Long, HashMap<Integer, TransactionCard>>> cardTrnsSeqInfo = new HashMap<>();
    readTCDFile(isHaveFileHeadTCD, tcdFilePath, charSetTCD, cardTrnsSeqInfo);

    HashMap<Long, HashMap<Long, DistrictODResult>> districtODResult = new HashMap<>();
    createDistrictODResult(districtEmd, busRouteStation, cardTrnsSeqInfo, busStationMaster, districtODResult);

    String districtODResulFilePath = "";
    exportDistrcitODResult(districtODResult, districtODResulFilePath);
  }

  private void readDistrictFile(boolean isHaveHead, String districtFilePath, String charSet, HashMap<Integer, District> districtEmd) {
    BufferedReader bufferedReader = null;
    try {
      File file = new File(districtFilePath);
      FileInputStream fileInputStream = new FileInputStream(file);
      InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, charSet);
      BufferedReader br = new BufferedReader(inputStreamReader);

      BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(new File("")), "utf-8"));

      if (isHaveHead) {
        bufferedReader.readLine();
      }

      String strRead = null;
      while ((strRead = bufferedReader.readLine()) != null) {
        String[] values = strRead.split(",", -1);

        String district3Id = values[0]; // 읍면동(행정구역) id
        String emdName = values[1]; // 읍면동 명칭
        String districtFullName = values[2]; // 행정구역 full 명칭 (ex '서울시 중구 명동')

        // 행정구역id 문자열 -> Long 변환
        int numId = Integer.parseInt(district3Id);

        District district3 = new District(); {
          district3.setZoneId(numId);
          district3.setName(emdName);
        }

        districtEmd.put(numId, district3);
        bufferedReader.close();
      }

    } catch (Exception e) {
    }
  }

  private void readBRSFile(boolean isHaveHead, String brsFilePath, String charSet,
                           HashMap<Long, HashMap<Long, BusRoute>> busRouteStation,
                           HashMap<Long, BusStationInfo> busStationMaster) {
  }

  private void readTCDFile(boolean isHaveHead, String tcdFilePath, String charSet, HashMap<Long, HashMap<Long, HashMap<Integer, TransactionCard>>> cardTrnsSeqInfo) {
  }

  private void createDistrictODResult(HashMap<Integer, District> districtEmd,
                                      HashMap<Long, HashMap<Long, BusRoute>> busRouteStation,
                                      HashMap<Long, HashMap<Long, HashMap<Integer, TransactionCard>>> cardTrnsSeqInfo,
                                      HashMap<Long, BusStationInfo> busStationMaster,
                                      HashMap<Long, HashMap<Long, DistrictODResult>> districtODResult) {

    for (Map.Entry<Long, HashMap<Long,HashMap<Integer, TransactionCard>>> entryCardGroup : cardTrnsSeqInfo.entrySet()) {

    }
  }

  private void exportDistrcitODResult(HashMap<Long, HashMap<Long, DistrictODResult>> districtODResult, String resultFilePath) {

  }
}
