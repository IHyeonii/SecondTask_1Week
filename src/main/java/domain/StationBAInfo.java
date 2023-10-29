package domain;


public class StationBAInfo { // 정류장 승-하차 정보
  long stationId = 0; // 정류장ID
  String stationName = ""; //정류장 명
  int time = 0;
  int boardPassenger = 0;
  int alightPassenger = 0;

  @Override
  public String toString() {
    return "StationBAInfo{" +
        "stationId=" + stationId +
        ", stationName='" + stationName + '\'' +
        ", time=" + time +
        ", boardPassenger=" + boardPassenger +
        ", alightPassenger=" + alightPassenger +
        '}';
  }

  public long getStationId() {
    return stationId;
  }

  public void setStationId(long stationId) {
    this.stationId = stationId;
  }

  public String getStationName() {
    return stationName;
  }

  public void setStationName(String stationName) {
    this.stationName = stationName;
  }

  public int getTime() {
    return time;
  }

  public void setTime(int time) {
    this.time = time;
  }

  public int getBoardPassenger() {
    return boardPassenger;
  }

  public void setBoardPassenger(int boardPassenger) {
    this.boardPassenger = boardPassenger;
  }

  public int getAlightPassenger() {
    return alightPassenger;
  }

  public void setAlightPassenger(int alightPassenger) {
    this.alightPassenger = alightPassenger;
  }
}
