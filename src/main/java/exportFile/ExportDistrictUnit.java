package exportFile;

import service.DistrictUnit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ExportDistrictUnit {
  public static void main(String[] args) throws Exception{
    File filePath = new File("C:\\Users\\ihyeon\\Desktop\\data\\output\\ExportDistrictUnit.txt");
    BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, StandardCharsets.UTF_8));

    DistrictUnit districtUnit = new DistrictUnit();
    List<StringBuilder> districtOD = districtUnit.getDistrictUnitInfo();

    String column = "BoardDistrictId,BoardDistrictName,AlightDistirictId,AlightDistirictName,PassengerCnt,Time";
    // header
    bw.write(column);
    bw.newLine();

    // data
    for(StringBuilder value : districtOD) {
      bw.write(value.toString());
      bw.newLine();
    }

    bw.flush();
    bw.close();
  }
}
