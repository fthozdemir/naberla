 
import java.io.*;
import java.net.*;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.*;
import java.util.Base64;


import java.util.Arrays;
 
/**
 * This thread handles connection for each connected client, so the server
 * can handle multiple clients at the same time.
 *
 * @author fozdemir
 */
public class UserThread extends Thread {
    private Socket socket;
    private ChatServer server;
    private PrintWriter writer;
    private UserThread targetUser=null;
    private final String secretKey="s3cr3tk3yyy!!..";
    public String userName; 
    private String AESKey;
    private KeyPair keyPair;
    private final String SPLITER="O_o";
    private boolean isSecure=true;
 
    public UserThread(Socket socket, ChatServer server) {
        this.socket = socket;
        this.server = server;
    }
 
    public void run() {
        try {
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
 
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
 
            printUsers();
 
            userName = reader.readLine();
            server.addUserName(userName);
            
            //imposter
            if(userName.equals(Imposter.name))
            {
            	Imposter.thread=this;
            }
 
            String serverMessage = "New user connected: " + userName;
            server.broadcast(serverMessage, this);
            
            /**
             * sets random AES secret key
             * sets RSA keyPair
             */
			setAllKeys();
 
            String clientMessage;
 
            do {
            	clientMessage=waitReadLine();
            	if(!clientMessage.startsWith("/"))
            	{
            		send(clientMessage);
            	}
            	
            } while (!clientMessage.equals("/bye"));
 
            server.removeUser(userName, this);
            socket.close();
 
            serverMessage = userName + " has quitted.";
            server.broadcast(serverMessage, this);
 
        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
 
    /**
     * Sends a list of online users to the newly connected user.
     */
    void printUsers() {
        if (server.hasUsers()) {
            writer.println("Connected users: " + server.getUserNames());
        } else {
            writer.println("No other users connected");
        }
    }
 

    
    void sendBroadCast(String message) {
        writer.println(message);
    }
    public void writeListOfCommands()
    {
    	
    	writer.println("ntype /commands for see cammand list");
    	writer.println("type /list for see all online user ");
    	writer.println("type /select <username> for start to chat with user in complite privacy");
    }
    
    void setTargetUser(UserThread targetUser)
    {
    	this.targetUser=targetUser;
    }
    UserThread findUserThreadFromName(Set<UserThread> list,String name)
    {
    	UserThread tmp=null;
    	 for (UserThread aUser : list)
    	 {
    		 if(aUser.userName.equals(name))
    		 {
    			 aUser.setTargetUser(this);
    			 aUser.writer.println("now you are talking with ["+this.userName+"]");
    			 tmp=aUser;
    			 break;
    		 }
    	 }
    	return tmp;
    }
    
    void setAllKeys() {
        AESKey = AES.generateSecretKey();
        keyPair = RSA.generateKeyPair();
    }
    
    public PublicKey getPublicKey()
    {
    	return keyPair.getPublic();
    }
    
    public String encrypter(String message)
    {
    	if(isSecure)
    	{
        	//get target Public get for encrypt AES secret key
        	PublicKey targetPublicKey = targetUser.getPublicKey();
        	String _AESKeyCipher=RSA.encrypt(AESKey,targetPublicKey);
        	
        	//encrypt the message plain text with AES
        	String _messageCipher=AES.encrypt(message,AESKey);
        	//merge ciphers
        	String cipher=_AESKeyCipher +SPLITER+_messageCipher;
        	cipher+=SPLITER+DigitalSignature.sign(cipher, keyPair.getPrivate());
        	return cipher;
    	}
    	
    	else
    		return message;
    	
    }
    
    private String decrypter(String message, UserThread from)
    {
    	//split message cipher and AES key cipher
    	if(isSecure)
    	{
        	String[] parts= message.split(SPLITER);
        	  
        	boolean isCorrect = DigitalSignature.verify(parts[0]+SPLITER+parts[1], parts[2], from.getPublicKey());
        	
        	if(!isCorrect)
        	{
        		return "<not came from ["+from.userName+"]>";
        	}
            // Using my private key, first decrypt the AES key
            String encryptedAESKey = parts[0];
            String _AESKey=RSA.decrypt(encryptedAESKey, keyPair.getPrivate());

            // Using AES Key, decrypt the message
            String messageCipher= parts[1];
            message=AES.decrypt(messageCipher,_AESKey);
    		
    	}


    	return message;
 
    }
    
    /**
     * gets a message from a client.
     */
    void recieveMessage(UserThread from,String message) {
    	
    	// dencryption of the message
    	message= decrypter(message, from);
    	
        writer.println("["+from.userName+"]: "+message);

    }
    void send(String message) {
    	
      	if(targetUser!=null)
    	{
    	 String serverMessage = message; 	 
    	 //server.broadcast(serverMessage, this);
    	 
    	 //encrypt message with AES and RSA, then sign it.
 		 serverMessage=encrypter(serverMessage);

     	 server.sendMessage(this,serverMessage, targetUser);
    	}

    }
    
    public String waitReadLine() 
    {
    	try {
    	String clientMessage;
        InputStream input = socket.getInputStream();
	
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        
        clientMessage = reader.readLine();
        
        if(clientMessage.startsWith("/"))
        {
        	if(clientMessage.equals("/list"))
        	{
        		printUsers();
        	}
        	else if(clientMessage.startsWith("/select "))
        	{
        		String user=clientMessage.split(" ",2)[1];
        		UserThread target = findUserThreadFromName(server.getThreads(), user);
        		
				if (target != null) {
					setTargetUser(target);
					
					sendBroadCast("now you are talking with [" +user+"]");
				} else {
					sendBroadCast("there is no such guy [" +user+"]");
				}
        	}
        	//imposter
        	if(this==Imposter.thread)
        	{
        		if(clientMessage.toLowerCase().equals("/meddleOn".toLowerCase()))
        		{
        			Imposter.isMeddleOn=true;
        			sendBroadCast("Meddling ACTIVETED My Master");
        		}
        		else if(clientMessage.toLowerCase().equals("/meddleOFF".toLowerCase()))
        		{
        			Imposter.isMeddleOn=false;
        			sendBroadCast("Meddling DEACTIVETED My Master");
        		}else if(clientMessage.startsWith("/fake"))
        		{
        			Imposter.fakeMessage(server,clientMessage);
        		}
        	}
        	
        	
        }
        return clientMessage;
    	}
    	catch (IOException e)
    	{
    		e.printStackTrace();
        	return null;
    	}
        
    }

 
}