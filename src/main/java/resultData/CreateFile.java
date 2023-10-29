package resultData;

public class CreateFile {
  public static void main(String[] args) throws Exception{
    DistricODResult odResult = new DistricODResult();
    odResult.createDistricODResult();

    RouteStnResult routeStnResult = new RouteStnResult();
    routeStnResult.createRouteStnResult();

    StnTimeResult stnTimeResult = new StnTimeResult();
    stnTimeResult.createStnTimeResult();
  }
}
