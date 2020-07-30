import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class SolverThread implements Runnable {

  private SecretKeyFactory secretFactory;
  private Base64.Encoder enc = Base64.getEncoder();
  public boolean running = false;
  private String session;
  private int secret;
  private String hash;

  public SolverThread(String session, String hash, int secret) throws NoSuchAlgorithmException {
    secretFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
    this.session = session;
    this.hash = hash;
    this.secret = secret;
  }

  @Override
  public void run() {

    int coreCount = Runtime.getRuntime().availableProcessors();
    int length = 256 * 8;
    int iter = 16384;

    while (true) {

      KeySpec spec = new PBEKeySpec(session.toCharArray(), Integer.toString(secret).getBytes(), iter, length);

      byte[] attempt;
      try {
        attempt = secretFactory.generateSecret(spec).getEncoded();
      } catch (InvalidKeySpecException e) {
        e.printStackTrace();
        return;
      }

      if (enc.encodeToString(attempt).equals(hash)) {

        System.out.println(secret);
        System.exit(0);
      } else {
        secret += coreCount;
      }

    }

  }

}
