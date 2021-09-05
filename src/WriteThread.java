 
import java.io.*;
import java.net.*;
import java.security.NoSuchAlgorithmException;
 
/**
 * This thread is responsible for reading user's input and send it
 * to the server.
 * It runs in an infinite loop until the user types 'bye' to quit.
 *
 * @author fozdemir
 */
public class WriteThread extends Thread {
    private PrintWriter writer;
    private Socket socket;
    private ChatClient client;
    private Console console;
 
    public WriteThread(Socket socket, ChatClient client) {
        this.socket = socket;
        this.client = client;
 
        try {
            OutputStream output = socket.getOutputStream();
            writer = new PrintWriter(output, true);
        } catch (IOException ex) {
            System.out.println("Error getting output stream: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
 
    public void run() {
 
        console = System.console();
        
        console.printf("\n \n888b    888          888                       d8b      888               \r\n"
        		+ "8888b   888          888                       88P      888               \r\n"
        		+ "88888b  888          888                       8P       888               \r\n"
        		+ "888Y88b 888  8888b.  88888b.   .d88b.  888d888 \"        888       8888b.  \r\n"
        		+ "888 Y88b888     \"88b 888 \"88b d8P  Y8b 888P\"            888          \"88b \r\n"
        		+ "888  Y88888 .d888888 888  888 88888888 888              888      .d888888 \r\n"
        		+ "888   Y8888 888  888 888 d88P Y8b.     888              888      888  888 \r\n"
        		+ "888    Y888 \"Y888888 88888P\"   \"Y8888  888              88888888 \"Y888888 \n \n");
        
        String userName = console.readLine("\nEnter your name: ");
        
        console.printf("\nWELCOME "+ userName+"\n");
        writeListOfCommands();
        
 
        client.setUserName(userName);
		
        
        writer.println(userName);
        
        String text;
 
        do {
			text = console.readLine("[" + userName + "]: ");
			if(text.equals("/commands"))
		        writeListOfCommands();
			
			else
			{
				writer.println(text);
			}          
			
        } while (!text.equals("/bye"));
 
        try {
            socket.close();
        } catch (IOException ex) {
 
            System.out.println("Error writing to server: " + ex.getMessage());
        }
    }
    
    public void writeListOfCommands()
    {
    	console.printf("\ntype /commands for see cammand list \n");
    	console.printf("type /list for see all online user \n");
    	console.printf("type /select <username> for start to chat with user in complite privacy \n");
    	console.printf("type /bye to leave the program \n\n");

    }
}