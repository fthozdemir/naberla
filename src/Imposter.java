import java.util.*;


public class Imposter {
	
	public static String name="muhittin";
	public static UserThread thread;
	public static boolean isOnline =false;
	public static boolean isMeddleOn =false;
	public static HashMap<UserThread,UserThread> userMap = new HashMap<UserThread,UserThread>();
	
	public static void sendActivity(UserThread s, UserThread r,String message)
	{
		userMap.put(s, r);
		thread.sendBroadCast("["+s.userName+"] -> ["+r.userName+"]: "+message );
	}
	public static void fakeMessage(ChatServer server,String input)
	{
		
		String from=getBetweenStrings(input, "from:", ";");
		String to=getBetweenStrings(input, "to:", ";");
		String message=input.split("message:")[1];
		
		UserThread _fromThread=null;
		UserThread _toThread=null;
		
		for (UserThread aUser : server.getThreads()) {
	            if (aUser.userName.equals(from)) {
	                _fromThread=aUser;
	            }
	            else if(aUser.userName.equals(to))
	            {
	            	_toThread=aUser;
	            }
	        }
		
		if(_fromThread!=null && _toThread!=null)
		{
			//þu an mesaj karþýnýn public key'i ile imzalanmýþ oluyor.
			thread.setTargetUser(_toThread);
			message=thread.encrypter(message);
			if(isMeddleOn)
			{
				isMeddleOn=!isMeddleOn;
				server.sendMessage(_fromThread, message, _toThread);
				isMeddleOn=!isMeddleOn;
			}
			else
				server.sendMessage(_fromThread, message, _toThread);

		}
		  
	}
    /**
     * Get text between two strings. Passed limiting strings are not 
     * included into result.
     *
     * @param text     Text to search in.
     * @param textFrom Text to start cutting from (exclusive).
     * @param textTo   Text to stop cuutting at (exclusive).
     */
    public static String getBetweenStrings(
      String text,
      String textFrom,
      String textTo) {

      String result = "";

      // Cut the beginning of the text to not occasionally meet a      
      // 'textTo' value in it:
      result =
        text.substring(
          text.indexOf(textFrom) + textFrom.length(),
          text.length());

      // Cut the excessive ending of the text:
      result =
        result.substring(
          0,
          result.indexOf(textTo));

      return result;
    }
	


}
