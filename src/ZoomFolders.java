import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ZoomFolders {

    private DefaultListModel<String> zoomRoomList = new DefaultListModel<String>();
    private String folderName;

    public ZoomFolders(String userDirectory)
    {
        int numberofFolders;
        String s;
        int l;
        int h =0;
        try
        {
            String testDir;
            testDir = "ls " + userDirectory;
            Process p = Runtime.getRuntime().exec(testDir);

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            numberofFolders = (int)stdInput.lines().count();
            String [] folderList = new String [numberofFolders];
            p = Runtime.getRuntime().exec("ls " + userDirectory);
            stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            // read the output from the command
            while ((s = stdInput.readLine()) != null)
            {
                folderList[h] = s;
                if(s.contains("Personal Meeting Room"))
                {
                    String[] temp = s.split("Personal Meeting Room");
                    s = temp[1];
                    if(!zoomRoomList.contains(s)) {
                        zoomRoomList.addElement(s);
                    }
                }
                h++;
            }

            l = numberofFolders - 1;
            folderName = folderList[l];
            //folderName= "2015-06-07 14.46.52 the one who should not be named's Personal Meeting Room 4441234567";
        }
        catch (IOException e)
        {
            System.out.println("exception happened - here's what I know: ");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public String GetFolderName()
    {
        return folderName;
    }

    public DefaultListModel<String> GetZoomFolderList()
    {
        return zoomRoomList;
    }
}
