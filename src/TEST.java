import javax.crypto.Cipher;
import java.io.InputStream;
import java.security.*;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class TEST {
	
	static KeyPair pair = RSA.generateKeyPair();
	static String AESKey=AES.generateSecretKey();
	static String SPLITER="O_o";


    public static void main(String... argv) throws Exception {
  
    	String message ="selcuk";
    	String key="123456789abcdefg";
    	
    	String cipher=AES.encrypt(message, key);
    	System.out.println(cipher);
    }
    
   
}
