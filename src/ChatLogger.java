import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class ChatLogger {

    private DefaultListModel theLogs = new DefaultListModel();
    private ArrayList<String> theUsers = new ArrayList();
    private String userName;

    public ChatLogger()
    {
        //Variable declarations
        userName = SetUserName();
        String[] pUser;
        String outputMessage;
        String userDirectory = userName +"/Documents/Zoom/";

        String folderName;
        int i;
        int numberofLines;
        BufferedReader br;
        String chatFileName;
        String sCurrentLine;

        //Retrieve most recent logs
        RetrieveLogFiles();
        ZoomFolders zoomFolders = new ZoomFolders(userDirectory);
        //folderName = GetFolderName(userDirectory);
        folderName = zoomFolders.GetFolderName();

        //call file
        chatFileName = userDirectory + folderName + "/meeting_saved_chat.txt";

        try
        {
            br = new BufferedReader(new FileReader(chatFileName));
            numberofLines = (int)br.lines().count();

            String [] pChats = new String[numberofLines];
            boolean [] pFlag = new boolean[numberofLines];
            String [] pTime = new String[numberofLines];
            pUser = new String[numberofLines];
            int arraycount = 0;

            br = new BufferedReader(new FileReader(chatFileName));

            for (i = 0; (sCurrentLine = br.readLine()) != null; i++)
            {
                try
                {
                    String[] temp2 = sCurrentLine.split("\t", 0);
                    pTime[i] = temp2[0];
                    String[] temp = sCurrentLine.split(":", 4);
                    String[] temp3 = sCurrentLine.split("From");
                    String[] tempChat;

                    if (pTime[i].matches("\\d{2}:\\d{2}:\\d{2}") && sCurrentLine.contains("(privately)"))
                    {
                        tempChat = temp3[1].split("(privately)");
                        pUser[i] = tempChat[0].substring(0, tempChat[0].length() - 1);
                        pFlag[i] = true;

                        if (temp.length == 4)
                        {
                            pChats[i] = temp[3];
                        }
                    }
                    else
                    {
                        pFlag[i] = false;
                    }

                    if (pFlag[i])
                    {
                        outputMessage = pTime[i] + ", " + pUser[i] + ": " + pChats[i];
                        System.out.println(outputMessage);
                        theLogs.addElement(outputMessage);
                        theUsers.add(arraycount, pUser[i]);
                        arraycount++;
                    }
                }
                catch (ArrayIndexOutOfBoundsException e2){}
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //Private methods
    private String SetUserName()
    {
        return System.getProperty("user.home");
    }


    //Public methods
    public DefaultListModel GetLogs()
    {
        return theLogs;
    }

    public void RetrieveLogFiles()
    {
        try
        {
            String cmd = "open " + userName + "/Documents/SaveChat.app";
            Process q = Runtime.getRuntime().exec(cmd);
        }
        catch (IOException e)
        {
            JOptionPane.showConfirmDialog(null, "SaveChat.app not located in appropriate directory");
        }
    }

    public DefaultListModel<String> GetTheUsers()
    {
        DefaultListModel <String> listModel2 =  new DefaultListModel();
        String [] arraytheUsers = theUsers.toArray(new String[theUsers.size()]);
        int numberofUsers = arraytheUsers.length;
        String[] tempUser;

        for (int z = 0; z < numberofUsers; z++)
        {
            try
            {
                tempUser = arraytheUsers[z].split("to", 0);
                if (!listModel2.contains(tempUser[0]))
                {
                    listModel2.addElement(tempUser[0]);
                }
            }
            catch (ArrayIndexOutOfBoundsException e){}
        }
        return listModel2;
    }

    public String GetUserName()
    {
        return userName;
    }
}
