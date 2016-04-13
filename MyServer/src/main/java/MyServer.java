import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Created by Алексей on 09.04.2016.
 */
public class MyServer {
    public static void main(String[] args) throws IOException {
        System.out.println("Welcome to Server side");
        try (ServerSocket soc = new ServerSocket(29288)) {
            System.out.println("Server on Port 29288");

            while (true) {
                System.out.println("Waiting for Connection...");
                ConnectionClient connection = new ConnectionClient(soc.accept());
                connection.start();
            }
        } catch (IOException e) {
            System.out.println("Couldn't listen to port 29288");
            e.printStackTrace();
            System.exit(-1);
        }
    }


}

class ConnectionClient extends Thread {
    private Socket clientSoc;
    private PrintWriter out;
    private BufferedReader fileBufferedReader;
    private BufferedReader in;


   public ConnectionClient(Socket soc) {
        try {
            clientSoc = soc;
            in=new BufferedReader(new InputStreamReader(clientSoc.getInputStream()));
            out = new PrintWriter(clientSoc.getOutputStream(),true);
            fileBufferedReader= new BufferedReader(new FileReader("D:\\Java\\Java programs\\MyServer\\src\\main\\resources\\testFile"));
            System.out.println("Client Connected...");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while (true) {
            try {
                String command;
                try {
                    command=in.readLine();
                }catch (SocketException e) {
                    break;
                }
                if (command.compareTo("START")==0) {
                    System.out.println("START");
                    commandWriter(out);

                }

            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("IOException");
            }
        }
        }

    private void commandWriter(PrintWriter out) throws IOException{
        String line;
        while((line=fileBufferedReader.readLine())!=null){
            out.println(line);
        }
    }
}
