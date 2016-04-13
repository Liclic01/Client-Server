
import connection.MyClient;
import java.io.IOException;
import java.net.Socket;


/**
 * Created by Алексей on 09.04.2016.
 */
public class Application {
    public static void main(String[] args) {

        try (Socket soc = new Socket("127.0.0.1", 29288)) {
            MyClient myClient = new MyClient(soc);
            myClient.createFrame();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}

