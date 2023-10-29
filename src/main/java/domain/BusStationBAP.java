package domain;

public class BusStationBAP {
  int routeId = 0; // 노선ID
  int seq = 0; //정류장 순번
  Long stationId = 0L; // 정류장Id
  int bPassenger = 0; //승차인원
  int aPassenger = 0; // 하차인원
  int reaminPassenger = 0; // 재차인원

  @Override
  public String toString() {
    return "BusStationBAP{" +
        "routeId=" + routeId +
        ", seq=" + seq +
        ", stationId=" + stationId +
        ", bPassenger=" + bPassenger +
        ", aPassenger=" + aPassenger +
        ", reaminPassenger=" + reaminPassenger +
        '}';
  }
  public int getRouteId() {
    return routeId;
  }

  public void setRouteId(int routeId) {
    this.routeId = routeId;
  }

  public int getSeq() {
    return seq;
  }

  public void setSeq(int seq) {
    this.seq = seq;
  }

  public Long getStationId() {
    return stationId;
  }

  public void setStationId(Long stationId) {
    this.stationId = stationId;
  }

  public int getbPassenger() {
    return bPassenger;
  }

  public void setbPassenger(int bPassenger) {
    this.bPassenger = bPassenger;
  }

  public int getaPassenger() {
    return aPassenger;
  }

  public void setaPassenger(int aPassenger) {
    this.aPassenger = aPassenger;
  }

  public int getReaminPassenger() {
    return reaminPassenger;
  }

  public void setReaminPassenger(int reaminPassenger) {
    this.reaminPassenger = reaminPassenger;
  }
}
