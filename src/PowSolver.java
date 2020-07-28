import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PowSolver {

  public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException {

    if (args.length == 0) {
      System.out.println("You must provide a bypass to be solved.");
      System.exit(0);
    }

    String bypass = args[0];

    if (bypass.length() < 372) {
      System.out.println("Bypass is too short to be valid.");
      System.exit(0);
    }

    String session = bypass.substring(24, 24 + 344);
    String hash = bypass.substring(24 + 344);

    int secret = 0;

    int length = 256 * 8;
    int iter = 16384;

    SecretKeyFactory secretFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
    Base64.Encoder enc = Base64.getEncoder();

    System.out.println("Please wait for the secret.");

    while (true) {

      KeySpec spec = new PBEKeySpec(session.toCharArray(), Integer.toString(secret).getBytes(), iter, length);

      byte[] attempt = secretFactory.generateSecret(spec).getEncoded();

      if (enc.encodeToString(attempt).equals(hash)) {

        System.out.println(secret);
        System.exit(0);
      } else {
        secret++;
      }

    }

  }

}
