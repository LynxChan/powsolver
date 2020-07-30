import java.security.NoSuchAlgorithmException;

public class PowSolver {

  private static String version = "1.0.0";

  public static void main(String[] args) throws NoSuchAlgorithmException, InterruptedException {

    if (args.length == 0) {
      System.out.println("You must provide a bypass to be solved.");
      System.exit(0);
    }

    String bypass = args[0];

    if (bypass.length() < 712) {
      System.out.println("Bypass is too short to be valid.");
      System.exit(0);
    }

    String session = bypass.substring(24, 24 + 344);
    String hash = bypass.substring(24 + 344);

    System.out.println("LynxChan PoW solver v" + version + ". Please wait for the secret.");

    for (int i = 0; i < Runtime.getRuntime().availableProcessors(); i++) {
      new Thread(new SolverThread(session, hash, i)).start();
    }

    while (true) {
      Thread.sleep(1000);
    }

  }

}
