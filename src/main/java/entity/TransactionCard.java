package entity;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TransactionCard {
  private Long cardId; //카드ID
  private LocalTime boardTime; //승차시간
  private LocalTime alightTime; //하차시간
  private Long routeId; //노선ID
  private Long boardSID; //출발 정류장
  private Long alightSID; //도착 정류장
  private int passengerCnt; //승객 수

  public Long getCardId() {
    return cardId;
  }

  public void setCardId(Long cardId) {
    this.cardId = cardId;
  }

  public LocalTime getBoardTime() {
    return boardTime;
  }

  public void setBoardTime(LocalTime boardTime) {
    this.boardTime = boardTime;
  }

  public LocalTime getAlightTime() {
    return alightTime;
  }

  public void setAlightTime(LocalTime alightTime) {
    this.alightTime = alightTime;
  }

  public Long getRouteId() {
    return routeId;
  }

  public void setRouteId(Long routeId) {
    this.routeId = routeId;
  }

  public Long getBoardSID() {
    return boardSID;
  }

  public void setBoardSID(Long boardSID) {
    this.boardSID = boardSID;
  }

  public Long getAlightSID() {
    return alightSID;
  }

  public void setAlightSID(Long alightSID) {
    this.alightSID = alightSID;
  }

  public int getPassengerCnt() {
    return passengerCnt;
  }

  public void setPassengerCnt(int passengerCnt) {
    this.passengerCnt = passengerCnt;
  }

  @Override
  public String toString() {
    return "TransactionCard{" +
        "cardId=" + cardId +
        ", boardTime=" + boardTime +
        ", alightTime=" + alightTime +
        ", routeId=" + routeId +
        ", boardSID=" + boardSID +
        ", alightSID=" + alightSID +
        ", passengerCnt=" + passengerCnt +
        '}';
  }

  public List<TransactionCard> ReadTCardData() throws Exception {
    File targetFile = new File("C:\\Users\\ihyeon\\Desktop\\data\\TCD_test.txt");
    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(targetFile), "euc-kr"));
    CSVReader csvReader = new CSVReader(reader);

    String[] str = null; // 한줄씩 읽어서 String 변수에 담아
    List<TransactionCard> list = new ArrayList<>();

    String[] header = csvReader.readNext(); //처음 필드명 제외

    while ((str = csvReader.readNext()) != null) {
      TransactionCard tCard = new TransactionCard();
      tCard.setCardId(Long.parseLong(str[0]));

      // 승차시간  20201102|130301 -> hour: 13 / minute:3 / second:1
      int hour = Integer.parseInt(str[3].substring(8, 10));
      int minute = Integer.parseInt(str[3].substring(10, 12));
      int second = Integer.parseInt(str[3].substring(12));

      LocalTime time = LocalTime.of(hour, minute, second); // 20:38:33
      tCard.setBoardTime(time);

      // 하차시간 : 공백일때 처리 추가
      if(!str[4].equals("")) {
        int hour1 = Integer.parseInt(str[4].substring(8, 10));
        int minute1 = Integer.parseInt(str[4].substring(10, 12));
        int second1 = Integer.parseInt(str[4].substring(12));

        LocalTime time1 = LocalTime.of(hour1, minute1, second1); // 20:38:33
        tCard.setAlightTime(time1);
      } else {
        tCard.setAlightTime(LocalTime.of(0, 0));
      }

      // 노선 아이디가 공백인 경우 존재
      if(!str[6].equals("")) {
        tCard.setRouteId(Long.parseLong(str[6]));
      } else {
        tCard.setRouteId(Long.parseLong("0"));
      }

      tCard.setBoardSID(Long.parseLong(str[9]));

      if(!str[10].equals("")) {
        tCard.setAlightSID(Long.parseLong(str[10]));
      } else {
        tCard.setAlightSID(Long.parseLong("0"));
      }

      tCard.setPassengerCnt(Integer.parseInt(str[11]));

      list.add(tCard);
    }
//    System.out.println(list); // while 반복문으로 test 파일에서 필요한 값 추출 완료
    csvReader.close();
    reader.close();

    return list;
  }
}
