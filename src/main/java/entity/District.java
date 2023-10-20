package entity;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class District {// 읍면동 테이블
  private Long districtId; // 행정구역ID
  private String districtName; // 서울특별시
  private String sigunguName; //종로구

  public Long getDistrictId() {
    return districtId;
  }

  public void setDistrictId(Long districtId) {
    this.districtId = districtId;
  }

  public String getDistrictName() {
    return districtName;
  }

  public void setDistrictName(String districtName) {
    this.districtName = districtName;
  }

  public String getSigunguName() {
    return sigunguName;
  }

  public void setSigunguName(String sigunguName) {
    this.sigunguName = sigunguName;
  }

  @Override
  public String toString() {
    return "District{" +
        "districtId=" + districtId +
        ", districtName='" + districtName + '\'' +
        ", sigunguName='" + sigunguName + '\'' +
        '}';
  }

  public Map<Long, District> ReadDistrict() throws Exception {
    File targetFile = new File("C:\\Users\\ihyeon\\Desktop\\data\\District3.txt");
    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(targetFile), "UTF-8"));
    CSVReader csvReader = new CSVReader(reader);

    String[] str = null;
    Map<Long, District> districtInfo = new HashMap<>();

    String[] header = csvReader.readNext(); //처음 필드명 제외

    while ((str = csvReader.readNext()) != null) {
      District district = new District();
      district.setDistrictId(Long.parseLong(str[0]));
      district.setDistrictName(str[5]);
      district.setSigunguName(str[6]);

      Long key = district.getDistrictId();

      districtInfo.put(key, district);
    }
    csvReader.close();
    reader.close();

    return districtInfo;
  }
}
