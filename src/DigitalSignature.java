import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class DigitalSignature {
	public static String sign(String plainText, PrivateKey privateKey){

		try {

			Signature privateSignature = Signature.getInstance("SHA256withRSA");
			privateSignature.initSign(privateKey);
			privateSignature.update(plainText.getBytes(UTF_8));

			byte[] signature = privateSignature.sign();

			return Base64.getEncoder().encodeToString(signature);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}
	
	public static boolean verify(String plainText, String signature, PublicKey publicKey) {
		try {

			Signature publicSignature = Signature.getInstance("SHA256withRSA");
			publicSignature.initVerify(publicKey);
			publicSignature.update(plainText.getBytes(UTF_8));

			byte[] signatureBytes = Base64.getDecoder().decode(signature);

			return publicSignature.verify(signatureBytes);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	   
	}

}
