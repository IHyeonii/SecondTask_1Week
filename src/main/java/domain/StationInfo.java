package domain;

public class StationInfo {
  int boardDistrict = 0;
  String bDistrictNm ="";
  int alightDistrict = 0;
  String aDistrictNm ="";
  int passengerCnt = 0;
  int time = 0;
  @Override
  public String toString() {
    return "StationInfo{" +
        "boardDistrict=" + boardDistrict +
        ", bDistrictNm='" + bDistrictNm + '\'' +
        ", alightDistrict=" + alightDistrict +
        ", aDistrictNm='" + aDistrictNm + '\'' +
        ", passengerCnt=" + passengerCnt +
        ", time=" + time +
        '}';
  }

  public int getBoardDistrict() {
    return boardDistrict;
  }

  public void setBoardDistrict(int boardDistrict) {
    this.boardDistrict = boardDistrict;
  }

  public String getbDistrictNm() {
    return bDistrictNm;
  }

  public void setbDistrictNm(String bDistrictNm) {
    this.bDistrictNm = bDistrictNm;
  }

  public int getAlightDistrict() {
    return alightDistrict;
  }

  public void setAlightDistrict(int alightDistrict) {
    this.alightDistrict = alightDistrict;
  }

  public String getaDistrictNm() {
    return aDistrictNm;
  }

  public void setaDistrictNm(String aDistrictNm) {
    this.aDistrictNm = aDistrictNm;
  }

  public int getPassengerCnt() {
    return passengerCnt;
  }

  public void setPassengerCnt(int passengerCnt) {
    this.passengerCnt = passengerCnt;
  }

  public int getTime() {
    return time;
  }

  public void setTime(int time) {
    this.time = time;
  }
}
