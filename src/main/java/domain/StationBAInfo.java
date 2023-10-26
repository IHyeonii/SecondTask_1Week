package domain;

import inputData.BusRouteData;
import inputData.TCardData;

import java.sql.Time;
import java.time.LocalTime;

public class StationBAInfo { // 정류장 승-하차 정보
  long stationId; // 정류장ID
  String stationName; //정류장 명
  int time;
  int boardPassenger;
  int alightPassenger;

//  public StationBAInfo(BusRouteData busRouteData, TCardData cardData, Time time) {
//    this.stationId = busRouteData.getStationId();
//    this.stationName = busRouteData.getStationName();
//    this.time = time.getHours();
//    this.boardPassenger = cardData.getPassengerCnt();
//    this.alightPassenger = cardData.getPassengerCnt();
//  }

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
