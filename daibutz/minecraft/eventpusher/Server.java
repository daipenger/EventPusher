package daibutz.minecraft.eventpusher;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class Server {

	private int port;
	private int maxClients;
	
	private ServerSocket serverSocket;
	private Vector<BufferedWriter> clients;
	
	public Server(int port, int maxClients) {
		this.port = port;
		this.maxClients = maxClients;
		clients = new Vector<BufferedWriter>(maxClients);
	}
	
	/**
	 * Start listening for and manage incoming connections.
	 */
	public void startListening() {
		try {
			serverSocket = new ServerSocket(port);
			
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						try {
							// Listen for new incoming connections and add them to the list of clients.
							// This will block the thread. Calling serverSocket.close() will cause an
							// IOException.
							Socket newSocket = serverSocket.accept();
							
							if (clients.size() >= maxClients) {
								newSocket.close();
								continue;
							}
							
							// Reading from clients is not needed for now. It's sufficient to keep track
							// of the writers.
							BufferedWriter clientWriter = new BufferedWriter(
									new OutputStreamWriter(newSocket.getOutputStream()));
							
							clients.add(clientWriter);
							
						} catch (IOException e) {
							break;
						}
					}
				}
			}).start();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Stop listening and do some clean-up.
	 */
	public void stopListening() {
		if (serverSocket != null)
			try {
				serverSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		serverSocket = null;
		
		clients.clear();
	}
	
	/**
	 * Sends a string message to all currently connected clients.
	 * 
	 * @param message The message.
	 */
	
	public void sendToAllClients(String message) {
		for (BufferedWriter client : clients) {
			try {
				client.write(message);
				client.newLine();
				client.flush();
				
			} catch (IOException e) {
				// Assume the remote connection has closed.
				clients.remove(client);
				
				e.printStackTrace();
			}
		}
	}
}
