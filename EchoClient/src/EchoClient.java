import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoClient {
    private final int port;
    private final String host;

    private EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static EchoClient connectTo(int port) {
        String localhost = "127.0.0.1";
        return new EchoClient(localhost, port);
    }
    public void run() {
        System.out.printf("Write 'bye' for exit%n%n%n");

        try (Socket socket = new Socket(host, port)) {
            Scanner scanner = new Scanner(System.in, "UTF-8");

            try (PrintWriter gos = new PrintWriter(socket.getOutputStream());
                 InputStream input = socket.getInputStream();) {
                InputStreamReader isr = new InputStreamReader(input, "UTF-8");

                try (Scanner sc = new Scanner(isr)) {
                    while (true) {
                        String message = scanner.nextLine();
                        gos.write(message);
                        gos.write(System.lineSeparator());

                        gos.flush();

                        String afs = sc.nextLine().strip();
                        System.out.println(afs);

                        if ("bye".equalsIgnoreCase(message)) {
                            return;
                        }
                    }
                }
            }
        } catch (NoSuchElementException ex){
            System.out.println("Connection dropped!");
        } catch (IOException e){
            System.out.printf("Can`t connect to %s:%s !%n", host, port);
            e.printStackTrace();
        }
    }
}
