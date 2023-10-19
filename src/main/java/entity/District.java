package entity;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class District {// 읍면동 테이블
  private Long zoneId; // 행정구역ID
  private String sidoName; // 서울특별시
  private String sigunguName; //종로구

  @Override
  public String toString() {
    return "District{" +
        "zoneId=" + zoneId +
        ", sidoName='" + sidoName + '\'' +
        ", sigunguName='" + sigunguName + '\'' +
        '}';
  }

  public void ReadDistrict() throws Exception {
    File targetFile = new File("C:\\Users\\qbic\\Desktop\\data\\District3_test.txt");
    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(targetFile), "euc-kr"));
    CSVReader csvReader = new CSVReader(reader);

    String[] str = null;
    List<District> list = new ArrayList<>();

    String[] header = csvReader.readNext(); //처음 필드명 제외

    while ((str = csvReader.readNext()) != null) {
      District district = new District();
      district.zoneId = Long.parseLong(str[0]);
      district.sidoName = str[5];
      district.sigunguName = str[6];

      list.add(district);
    }
    System.out.println(list);
    reader.close();
  }
}
