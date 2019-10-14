import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
   //Creazione di una classe per il Multrithreading
   class ServerThread extends Thread {
     private Socket socket;
     public ServerThread (Socket socket) {
       this.socket = socket;
     }
     //esecuzione del Thread sul Socket
     public void run() {
       try {
         Scanner is = new Scanner(socket.getInputStream());
         DataOutputStream os = new DataOutputStream(socket.getOutputStream());
         while(is.hasNext()) {
           String userInput = is.nextLine();
           if (userInput == null || userInput.equals("QUIT"))
             break;
           os.writeBytes(userInput + '\n');
           System.out.println("Il Client ["+socket.getPort()+"] ha scritto: " + userInput);
         }
         os.close();
         is.close();
         System.out.println("Ho ricevuto una chiamata di chiusura da:\n" + socket +
"\n");
         socket.close();
       }
       catch (IOException e) {
         System.out.println("IOException: " + e);
       }
     }
   }
   //Classe Server per attivare la Socket
   public class TCPParallelServer {
     public void start() throws Exception {
       ServerSocket serverSocket = new ServerSocket(7777);
       //Ciclo infinito di ascolto dei Client
       while(true) {
         System.out.println("In attesa di chiamate dai Client... ");
         Socket socket = serverSocket.accept();
         System.out.println("Ho ricevuto una chiamata di apertura da:\n" + socket);
         ServerThread serverThread = new ServerThread(socket);
         serverThread.start();
       }
     }
     public static void main (String[] args) throws Exception {
       TCPParallelServer tcpServer = new TCPParallelServer();
       tcpServer.start();
     }
   }