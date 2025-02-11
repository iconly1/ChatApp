import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private Scanner in;
    private Set<ClientHandler> clientHandlers;

    public ClientHandler(Socket socket, Set<ClientHandler> clientHandlers) {
        this.socket = socket;
        this.clientHandlers = clientHandlers;
    }

    @Override
    public void run() {
        try {
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream(), true);

            while (in.hasNextLine()) {
                String message = in.nextLine();
                broadcastMessage("Client " + socket.getPort() + ": " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    private void broadcastMessage(String message) {
        for (ClientHandler handler : clientHandlers) {
            handler.out.println(message);
        }
    }

    private void closeConnection() {
        try {
            clientHandlers.remove(this);
            socket.close();
            System.out.println("Client disconnected: " + socket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
