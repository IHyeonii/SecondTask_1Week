package inputData;

import com.opencsv.CSVReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.HashMap;

public class TCardData {
  private int cardId; //카드ID
  private int transactionId; //환승Id
  private int transCnt; //환승 횟수
  private LocalTime boardTime; //승차시간
  private LocalTime alightTime; //하차시간
  private int routeId; //노선ID
  private Long boardSID; //출발 정류장
  private Long alightSID; //도착 정류장
  private int passengerCnt; //승객 수

  public int getCardId() {
    return cardId;
  }

  public void setCardId(int cardId) {
    this.cardId = cardId;
  }

  public int getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(int transactionId) {
    this.transactionId = transactionId;
  }

  public int getTransCnt() {
    return transCnt;
  }

  public void setTransCnt(int transCnt) {
    this.transCnt = transCnt;
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

  public int getRouteId() {
    return routeId;
  }

  public void setRouteId(int routeId) {
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
    return "TransactionCard [cardId=" + cardId + ", transactionId=" + transactionId + ", transCnt=" + transCnt
        + ", boardTime=" + boardTime + ", alightTime=" + alightTime + ", routeId=" + routeId + ", boardSID="
        + boardSID + ", alightSID=" + alightSID + ", passengerCnt=" + passengerCnt + "]";
  }

  public static HashMap<Integer, HashMap<Integer, HashMap<Integer, TCardData>>> ReadTCDFile() throws Exception {
    String filePath = "C:\\Users\\ihyeon\\Desktop\\data\\TRIPS_inputData\\20201106";
//    String inputFile = "TCD_20201102.txt";
//    String inputFile = "TCD_20201103.txt";
//    String inputFile = "TCD_20201104.txt";
//    String inputFile = "TCD_20201105.txt";
    String inputFile = "TCD_20201106.txt";

    File file = new File(filePath, inputFile);
    BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
    CSVReader csvReader = new CSVReader(reader);

    String[] str = null; // 한줄씩 읽어서 String 변수에 담아

    // Key: 카드ID, 환승ID, 환승횟수
    HashMap<Integer, HashMap<Integer, HashMap<Integer, TCardData>>> cardData = new HashMap<>();

    String[] header = csvReader.readNext(); //처음 필드명 제외

    while ((str = csvReader.readNext()) != null) {
      TCardData tCardData = new TCardData();
      tCardData.setCardId(Integer.parseInt(str[0]));
      tCardData.setTransactionId(Integer.parseInt(str[1]));
      tCardData.setTransCnt(Integer.parseInt(str[2]));

      /**
       * 시간대 substring 파싱 -> DateTimeFormatter 변경
       * */
      // 승차시간, 20201102130301 -> hour: 13 / minute:3 / second:1
      String bTime = str[3];
      DateTimeFormatter bFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

      LocalTime boardTime = LocalTime.parse(bTime, bFormatter);
      tCardData.setBoardTime(boardTime);

      // 하차시간
      String aTime = str[4];
      DateTimeFormatter aFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

      LocalTime alightTime = null;

      try {
        alightTime = LocalTime.parse(aTime, aFormatter);
        tCardData.setAlightTime(alightTime);
      } catch (DateTimeParseException e) {
        // 공백일 때 0
        alightTime = LocalTime.of(0, 0);
        tCardData.setAlightTime(alightTime);
      }

      /**
       * 공백일 때 처리 삼항연산자로 수정
       * */
      tCardData.setRouteId(!str[6].isEmpty() ? Integer.parseInt(str[6]) : 0);
      tCardData.setBoardSID(Long.parseLong(str[9]));
      tCardData.setAlightSID(!str[10].isEmpty() ? Long.parseLong(str[10]) : 0);
      tCardData.setPassengerCnt(Integer.parseInt(str[11]));

      // Key 생성: 카드id + 트랜잭션id + 환승횟수
      int cardId = tCardData.getCardId();
      int transId = tCardData.getTransactionId();
      int transCnt = tCardData.getTransCnt();

      HashMap<Integer, HashMap<Integer, TCardData>> tranMap = cardData.getOrDefault(cardId, new HashMap<>());
      HashMap<Integer, TCardData> transCntMap = tranMap.getOrDefault(transId, new HashMap<>());

      transCntMap.put(transCnt, tCardData);
      tranMap.put(transId, transCntMap);
      cardData.put(cardId, tranMap);
    }
    csvReader.close();
    reader.close();

    return cardData;
  }
}
