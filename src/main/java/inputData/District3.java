package inputData;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class District3 {// 읍면동 테이블
  private int zoneId; // 읍면동Id (행정구역ID)
  private String name; // 삼청동 사직동 부암동 ..
  private String fullName; //서울시 종로구 사직동

  public int getZoneId() {
    return zoneId;
  }
  public void setZoneId(int zoneId) {
    this.zoneId = zoneId;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getFullName() {
    return fullName;
  }
  public void setFullName(String fullName) {
    this.fullName = fullName;
  }
  @Override
  public String toString() {
    return "District{" +
        "zoneId=" + zoneId +
        ", name='" + name + '\'' +
        ", fullName='" + fullName + '\'' +
        '}';
  }

  public Map<Integer, District3> ReadDistrict() throws Exception {
    File targetFile = new File("C:\\Users\\ihyeon\\Desktop\\data\\District3.txt");
    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(targetFile), "UTF-8"));
    CSVReader csvReader = new CSVReader(reader);

    String[] str = null;
    Map<Integer, District3> districData = new HashMap<>();

    String[] header = csvReader.readNext(); //처음 필드명 제외

    while ((str = csvReader.readNext()) != null) {
      District3 district = new District3();
      district.setZoneId(Integer.parseInt(str[0]));
      district.setName(str[1]);
      district.setFullName(str[2]);

      int key = district.getZoneId();

      districData.put(key, district);
    }
    csvReader.close();
    reader.close();

    return districData;
  }
}
