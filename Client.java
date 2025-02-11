import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private static final String SERVER_ADDRESS = "localhost";
    private static final int SERVER_PORT = 12345;

    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT)) {
            System.out.println("Connected to the chat server");
            
            new Thread(new IncomingMessageHandler(socket)).start();
            
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            try (Scanner scanner = new Scanner(System.in)) {
                while (scanner.hasNextLine()) {
                    out.println(scanner.nextLine());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class IncomingMessageHandler implements Runnable {
    private Socket socket;

    public IncomingMessageHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (Scanner in = new Scanner(socket.getInputStream())) {
            while (in.hasNextLine()) {
                System.out.println(in.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
