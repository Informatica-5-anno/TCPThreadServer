import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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
    	 BufferedReader ibr= new BufferedReader(new InputStreamReader(socket.getInputStream()));
         DataOutputStream os = new DataOutputStream(socket.getOutputStream());
         
         String ur,userInput="";
         do {
        	 ur=ibr.readLine();
        	 userInput+="\n"+ur;
        	
         } while (!ur.isEmpty());
         System.out.println("Il Client ha scritto: " + userInput);
         String data=
      		"<HTML>\r\n" + 
         		"          <HEAD>\r\n" + 
         		"             <TITLE>Esempio HTML</TITLE>\r\n" + 
         		"          </HEAD>\r\n" +  
         		"          <BODY> \r\n" + 
         		"            PAGINA HTML DEMO\r\n" + 
         		"          </BODY>\r\n" + 
         		"</HTML>\r\n";
         
         os.writeBytes("HTTP/1.1 200 OK\r\n" + 
         		"Date: Tue, 05 Nov 2019 08:37:43 GMT\r\n" + 
         		"Server: Apache/2.4.6 (Scientific Linux) OpenSSL/1.0.2k-fips mod_fcgid/2.3.9 PHP/5.4.16\r\n" + 
         		"Last-Modified: Thu, 23 Oct 1997 20:34:37 GMT\r\n" + 
         		"ETag: \"378-31e3539813140\"\r\n" + 
         		"Accept-Ranges: bytes\r\n" + 
         		"Content-Length: "+data.length()+"\r\n" + 
         		"Keep-Alive: timeout=5, max=100\r\n" + 
         		"Connection: Keep-Alive\r\n" + 
         		"Content-Type: text/html\r\n" + 
         		"\r\n" + data);
       
         os.close();
         ibr.close();
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