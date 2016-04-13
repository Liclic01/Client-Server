package connection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    final private String STRING_KEY = "60:21:C0:2A:E0:33";
    private Socket clientSoc;
    private BufferedReader in;
    private PrintWriter out;


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
        final JFrame testFrame = new JFrame("Онлайн доска");
        final LinesComponent comp = new LinesComponent();
        testFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        testFrame.setVisible(true);
        testFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        testFrame.getContentPane().add(comp);
        JButton start = new JButton("START");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                out.println("START");

            }
        });
        testFrame.getContentPane().add(start, BorderLayout.NORTH);
        testFrame.setSize(500, 500);
        draw(comp);

    }

    private void draw(LinesComponent comp) {

        Double x1 = null;
        Double y1 = null;

        String line;
        try {
            while ((line = in.readLine()) != null) {
                String[] s = parseCommand(line);

                if (s.length != 5) continue;


                if (!(STRING_KEY.equals(s[0]))) {
                    continue;
                }


                try {
                    switch (s[1]) {
                        case "start":
                            x1 = Double.parseDouble(s[2]);
                            y1 = Double.parseDouble(s[3]);
                            break;
                        case "move":
                            double xt = Double.parseDouble(s[2]);
                            double yt = Double.parseDouble(s[3]);
                            comp.addLine(x1, y1, xt, yt, createColor(s));
                            x1 = xt;
                            y1 = yt;
                            break;
                        default:
                            continue;
                    }
                } catch (NumberFormatException e) {
                    continue;
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


