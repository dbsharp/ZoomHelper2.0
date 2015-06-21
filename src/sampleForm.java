import apple.applescript.AppleScriptEngine;
import javax.script.ScriptEngine;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class sampleForm extends JFrame {

    private JButton getlogsButton;
    private JScrollPane logoutput;
    private JPanel panel1;
    private JTextArea textArea1;
    private JButton hammerButton;
    private JList chattersList;
    private JLabel userSelection;
    private JComboBox comboBox1;
    private JTextField textField1;
    private String userHandle;
    private DefaultListModel<String> theList;
    private DefaultListModel<String> theUsers;


    public sampleForm()
    {
        super("");

        setContentPane(panel1);
        pack();
        int size = 700;
        this.setSize(size, size);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        String userName = System.getProperty("user.home");
        String userDirectory = userName +"/Documents/Zoom/";
        ZoomFolders zoomFolders = new ZoomFolders(userDirectory);
        DefaultListModel<String> zoomRooms;
        zoomRooms = zoomFolders.GetZoomFolderList();
        int numberofRooms = zoomRooms.size();
        userHandle = "D B";

        for(int i = 0; i < numberofRooms; i++)
        {
            comboBox1.addItem(zoomRooms.elementAt(i));
        }


        getlogsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int textAreaOffset = textArea1.getLineCount();
                textArea1.setText("");
                userSelection.setText("");
                chattersList.removeAll();

                //GetChatsScript();
                ChatLogger myLogins = new ChatLogger();
                theList = myLogins.GetLogs();
                theUsers = myLogins.GetTheUsers();
                String[] theUsersArray;
                int numofLines = theList.size();
                for (int i = 0; i < numofLines; i++) {
                    textArea1.append(theList.elementAt(i).toString());
                    textArea1.append("\n");
                }

                numofLines = theUsers.size();
                theUsersArray = new String[numofLines];
                for (int i = 0; i < numofLines; i++) {
                    theUsersArray[i] = theUsers.elementAt(i);
                }
                Arrays.sort(theUsersArray);
                chattersList.setListData(theUsersArray);
            }
        });
        setVisible(true);

        chattersList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if(chattersList.getSelectedValue() != null) {
                    textArea1.setText("");
                    String chattersName = chattersList.getSelectedValue().toString();
                    userSelection.setText(chattersName);
                    int numofLines = theList.size();
                    for (int i = 0; i < numofLines; i++)
                    {
                        if(theList.elementAt(i).contains(chattersName)) {
                            textArea1.append(theList.elementAt(i).toString());
                            textArea1.append("\n");
                        }
                    }
                }
            }
        });

        hammerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ZoomHammer();
            }
        });

        comboBox1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(comboBox1.getSelectedItem() != null) {
                    textField1.setText((comboBox1.getSelectedItem().toString()));
                }
            }
        });
    }

    //Scripts
    public void GetChatsScript()
    {
        String script = "tell application \"System Events\"\n"
        + " tell application process \"zoom.us\"\n"
        + " if (window \"Zoom Group Chat\") exists then \n"
        + " click button \"Save Chat\" of splitter group 1 of splitter group 1 of window \"Zoom Group Chat\"\n"
        + " else \n"
        + " click button \"Chat\" of window 1 \n"
        + " delay 1 \n"
        + " click button \"Save Chat\" of splitter group 1 of splitter group 1 of window \"Zoom Group Chat\"\n"
        + " end if \n"
        + " end tell \n"
        + " end tell \n";

        String script2 = "display dialog \"Error Code \" buttons {\"Ok\"}";

        try {
            ScriptEngine engine = new AppleScriptEngine();
            engine.eval(script);
        }
        catch(Exception e){}
    }

    public void ZoomHammer()
    {
        String roomNumber = textArea1.toString();
       String hammer = "\n" +
                "on enabledGUIScripting(flag)\n" +
                "\tconsidering numeric strings\n" +
                "\t\tset MountainLionOrOlder to system version of (system info) < \"10.9\"\n" +
                "\tend considering\n" +
                "\tif MountainLionOrOlder then\n" +
                "\t\ttell application \"System Events\"\n" +
                "\t\t\tactivate -- brings System Events authentication dialog to front\n" +
                "\t\t\tset UI elements enabled to flag\n" +
                "\t\t\treturn UI elements enabled\n" +
                "\t\tend tell\n" +
                "\telse\n" +
                "\t\ttell application \"System Events\" to set GUIScriptingEnabled to UI elements enabled -- read-only in OS X 10.9 Mavericks and newer\n" +
                "\t\tif flag is true then\n" +
                "\t\t\tif not GUIScriptingEnabled then\n" +
                "\t\t\t\tactivate\n" +
                "\t\t\t\tset scriptRunner to name of current application\n" +
                "\t\t\t\tdisplay alert \"GUI Scripting is not enabled for \" & scriptRunner & \".\" message \"Open System Preferences, unlock the Security & Privacy preference, select \" & scriptRunner & \" in the Privacy Pane's Accessibility list, and then run this script again.\" buttons {\"Open System Preferences\", \"Cancel\"} default button \"Cancel\"\n" +
                "\t\t\t\tif button returned of result is \"Open System Preferences\" then\n" +
                "\t\t\t\t\ttell application \"System Preferences\"\n" +
                "\t\t\t\t\t\ttell pane id \"com.apple.preference.security\" to reveal anchor \"Privacy_Accessibility\"\n" +
                "\t\t\t\t\t\tactivate\n" +
                "\t\t\t\t\tend tell\n" +
                "\t\t\t\tend if\n" +
                "\t\t\tend if\n" +
                "\t\tend if\n" +
                "\t\treturn GUIScriptingEnabled\n" +
                "\tend if\n" +
                "end enabledGUIScripting\n" +
                "\n" +
                "set errorCode to \"\"\n" +
                //"set theAnswers to text returned of (display dialog \"enter zoom number,(password)\" default answer \"\")\n" +
               "set theAnswers to " + roomNumber +
               "\n" +
                "set astid to AppleScript's text item delimiters\n" +
                "set AppleScript's text item delimiters to \",\"\n" +
                "try\n" +
                "\tset {zoomNumber, zoomPassword} to {(theAnswers's text item 1), (theAnswers's text item 2)}\n" +
                "on error\n" +
                "\tset zoomNumber to first text item of theAnswers\n" +
                "end try\n" +
                "\n" +
                "\n" +
                "--set theAnswers to text returned of result\n" +
                "\n" +
                "set AppleScript's text item delimiters to astid\n" +
                "set passwordField to \"secure text field\"\n" +
                "\n" +
                "try\n" +
                "\tactivate application \"zoom.us\"\n" +
                "\tdelay 3\n" +
                "\trepeat while errorCode = \"\"\n" +
                "\t\ttell application \"System Events\"\n" +
                "\t\t\ttell application process \"zoom.us\"\n" +
                "\t\t\t\tclick menu item \"Join Meeting...\" of menu 1 of menu bar item \"zoom.us\" of menu bar 1\n" +
                "\t\t\t\tdelay 3\n" +
                "\t\t\t\tset value of text field 1 of window \"Join Meeting\" to zoomNumber\n" +
                "\t\t\t\tdelay 1\n" +
                "\t\t\t\tif button \"Join\" of window \"Join Meeting\" is enabled then\n" +
                "\t\t\t\t\tclick button \"Join\" of window \"Join Meeting\"\n" +
                "\t\t\t\telse\n" +
                "\t\t\t\t\tset errorCode to \"1\"\n" +
                "\t\t\t\t\texit repeat\n" +
                "\t\t\t\tend if\n" +
                "\t\t\t\tdelay 2\n" +
                "\t\t\t\t--set attributeList to value of every attribute of text field 1 of group 1 of group 1 of window \"Join Meeting\"\n" +
                "\t\t\t\t--if passwordField is in attributeList then\n" +
                "\t\t\t\t--set zoomPassword to second text item of theAnswers\n" +
                "\t\t\t\ttry\n" +
                "\t\t\t\t\tset value of text field 1 of group 1 of group 1 of window \"Join Meeting\" to zoomPassword\n" +
                "\t\t\t\t\tkey code 29 using control down\n" +
                "\t\t\t\t\t\n" +
                "\t\t\t\t\tdelay 1\n" +
                "\t\t\t\t\tclick button \"Join\" of group 1 of window \"Join Meeting\"\n" +
                "\t\t\t\t\t--end if\n" +
                "\t\t\t\tend try\n" +
                "\t\t\t\tdelay 2\n" +
                "\t\t\t\tif value of static text 2 of window 1 contains \"This meeting ID is not valid. Please check and try again.\" then\n" +
                "\t\t\t\t\tset errorCode to \"3\"\n" +
                "\t\t\t\t\texit repeat\n" +
                "\t\t\t\telse if button \"Leave\" of window 1 exists then\n" +
                "\t\t\t\t\tclick button \"Leave\" of window 1\n" +
                "\t\t\t\tend if\n" +
                "\t\t\t\tif (button \"Check\" of group 1 of window \"Join Meeting\") exists then\n" +
                "\t\t\t\t\tset errorCode to \"2\"\n" +
                "\t\t\t\t\texit repeat\n" +
                "\t\t\t\tend if\n" +
                "\t\t\t\tdelay 1\n" +
                "\t\t\tend tell\n" +
                "\t\tend tell\n" +
                "\tend repeat\n" +
                "end try\n" +
                "if errorCode ≠ \"\" then\n" +
                "\tdisplay dialog \"Error Code \" & errorCode buttons {\"Ok\"}\n" +
                "end if";
        try {
            ScriptEngine engine = new AppleScriptEngine();
            engine.eval(hammer);
        }
        catch (Exception e){}
    }
}
