import javax.crypto.Cipher;

import static java.nio.charset.StandardCharsets.UTF_8;

import java.security.*;
import java.security.spec.*;
import java.util.Base64;
import java.security.interfaces.RSAPublicKey;


public class RSA {

	public static KeyPair generateKeyPair()  {
		try {
		    KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
		    generator.initialize(2048, new SecureRandom());
		    KeyPair pair = generator.generateKeyPair();

		    return pair;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		
		return null;
		
	
	}

	public static String decrypt(String cipherText, PrivateKey privateKey){
	    
		try {
		byte[] bytes = Base64.getDecoder().decode(cipherText);

	    Cipher decriptCipher = Cipher.getInstance("RSA");
	    decriptCipher.init(Cipher.DECRYPT_MODE, privateKey);

	    return new String(decriptCipher.doFinal(bytes), UTF_8);
	    
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		
		return null;
	}
	
	public static String encrypt(String plainText, PublicKey publicKey){
	    
		try {
		Cipher encryptCipher = Cipher.getInstance("RSA");
	    encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);

	    byte[] cipherText = encryptCipher.doFinal(plainText.getBytes(UTF_8));

	    return Base64.getEncoder().encodeToString(cipherText);
		}
		
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
		
		return null;
	}

}
