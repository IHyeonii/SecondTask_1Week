package exportFile;

import service.BusStationUnit;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.nio.charset.StandardCharsets;

public class ExportBustStationUnit {
  public static void main(String[] args) throws Exception{
    File filePath = new File("C:\\Users\\ihyeon\\Desktop\\data\\output\\ExportBustStationUnit.txt");
    BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, StandardCharsets.UTF_8));

    BusStationUnit busStationUnit = new BusStationUnit();
    StringBuilder stationInfo = busStationUnit.getStationInfo();

    String column = "StationID, StationName, Time, BoardPassenger, AlightPassenger\n";
    bw.write(column);
    bw.write(String.valueOf(stationInfo));
    bw.flush();
    bw.close();
  }
}
