import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private ServerSocket server;
    private int serverPort = 54321;
    private DHRSA dhrsa;
    private double totalLatency = 0;
    private int messageCount = 0;

    public Server() {
        try {
            server = new ServerSocket(serverPort);
            System.out.println("ServerSocket: " + server);
            dhrsa = new DHRSA(); // Initialize RSA
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        while (true) { // run until you terminate the program
            try {
                // Wait for connection. Block until a connection is made.
                Socket socket = server.accept();
                System.out.println("Socket: " + socket);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String encryptedMessage = reader.readLine();

                // Decrypt the received message using RSA private key
                String decryptedMessage = dhrsa.decryptRSA(encryptedMessage);

                // Print the decrypted message
                System.out.println("Decrypted Message: " + decryptedMessage);

                // Respond to the client
                OutputStream outputStream = socket.getOutputStream();
                PrintWriter writer = new PrintWriter(outputStream, true);
                String response = "Server received : " + decryptedMessage;

                // Encrypt the response before sending it back to the client
                String encryptedResponse = dhrsa.encryptRSA(response, dhrsa.getPublicKey());
                writer.println(encryptedResponse);
                writer.flush();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public double getTotalLatency() {
        return totalLatency;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public static void main(String[] args) {
        new Server().listen();
        // Start the server and listening
    }
}
