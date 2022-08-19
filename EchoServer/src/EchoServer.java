import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoServer {

    private final int port;

    private EchoServer(int port) {
        this.port = port;
    }

    public static EchoServer bindToPort(int port) {
        return new EchoServer(port);
    }

    public void run() {
        try (var server = new ServerSocket(port)) {
            System.out.printf("The server started on port %s%n", port);
            try(var clientSocket = server.accept()) {
                handle(clientSocket);
            }
        } catch (IOException e) {
            System.out.printf("Most likely the port %s is busy.%n", port);
            e.printStackTrace();
        }
    }

    private void handle(Socket clientside) throws IOException {

        InputStream input = clientside.getInputStream();
        InputStreamReader isr = new InputStreamReader(input, "UTF-8");

        try(Scanner sc = new Scanner(isr);
            PrintWriter gos = new PrintWriter(clientside.getOutputStream())){
            while (true) {
                String message = sc.nextLine().strip();
                System.out.printf("Got: %s%n", message);

                if ("bye".equalsIgnoreCase(message)){
                    gos.write("Hasta la vista, baby!");
                    gos.write(System.lineSeparator());

                    gos.flush();
                    return;
                }
                gos.write(reverseString(message));
                gos.write(System.lineSeparator());

                gos.flush();
            }
        } catch (NoSuchElementException e){
            System.out.println("Client dropped the connection!");
        }
    }

    private static String reverseString(String message) {
        return new StringBuffer(message).reverse().toString();
    }
}
