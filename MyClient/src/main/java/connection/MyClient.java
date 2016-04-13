package connection;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import Frame.LinesComponent;


/**
 * Created by Алексей on 10.04.2016.
 */
public class MyClient {
    final String stringKey = "60:21:C0:2A:E0:33";
    Socket clientSoc;
    public BufferedReader in;
    public PrintWriter out;


    public MyClient(Socket soc) {
        try {
            clientSoc = soc;
            out = new PrintWriter(clientSoc.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSoc.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void createFrame() {
        JFrame testFrame = new JFrame("Онлайн доска");
        testFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        final LinesComponent comp = new LinesComponent();
        comp.setPreferredSize(new Dimension(1000, 1000));
        testFrame.getContentPane().add(comp, BorderLayout.CENTER);
        testFrame.pack();
        testFrame.setVisible(true);
        String line;
        Double x1=null;
        Double y1=null;

        out.println("START");
        try {
            while ((line = in.readLine()) != null) {
                String[] s = parseCommand(line);
                if (stringKey.compareTo(s[0]) != 0) {
                    continue;
                }


                if (s[1].compareTo("start") == 0) {
                    x1=Double.parseDouble(s[2])*testFrame.getWidth();
                    y1=Double.parseDouble(s[3])*testFrame.getHeight();
                }
                if (s[1].compareTo("move") == 0) {
                    comp.addLine(x1,y1,Double.parseDouble(s[2])*testFrame.getWidth(),Double.parseDouble(s[3])*testFrame.getHeight(),createColor(s));
                    x1=Double.parseDouble(s[2])*testFrame.getWidth();
                    y1=Double.parseDouble(s[3])*testFrame.getHeight();
                }


            }
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    String[] parseCommand(String command) {
        String[] s = command.split(";");
        return s;
    }

    Color createColor(String[] s) {
        return new Color(Integer.parseInt(s[4]));
    }

}


