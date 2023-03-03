import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

/** Thread 1 is listening to port 23:
 * Receive datagram of Client in port 23 -> send message to Client 
 * through port 3000 to tell Host has received the message. Add the 
 * data to the share queue and clear the queue. After that, if the 
 * Client sends something it tells Client to wait until its turn
 *  
 * Thread 2 is listening to port 24:
 * Receive request from Server in port 24 -> send the data to Server 
 * through port 69 if it's available in the queue, else tell the Server to wait.
 * After receiving response of the data from server, add the response to the queue
 * and clear it. If the Server sends something, tell the Server to wait 
 */

public class IntermediateHost implements Runnable{
	private DatagramPacket sendClientPacket, sendServerPacket, receivePacket;
	private DatagramSocket receiveSocket, sendSocket;
	//Client
	private static final String WAIT_FOR_RESPONSE = "WAIT_FOR_RESPONSE";
	// Server
	private static final String WAIT_FOR_REQUEST = "WAIT_FOR_REQUEST";
	private int port;
	private ArrayList<String> requestList;
	
	public IntermediateHost(int port, ArrayList<String> requestList) {
		try {
			sendSocket = new DatagramSocket();
			this.port = port;
			// receive data in the assigned port
			receiveSocket = new DatagramSocket(port);
			this.requestList = requestList;
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}
	
	/**
	 * This method will listening to the port and send the response based on the
	 * condition
	 * @param port
	 */
	private void handleRequest(int port) {
		while (true) {
			String received = receive(port);
			if (!received.equals("REQUEST_DATA") && !received.equals("REQUEST_RESPONSE")) {
				requestList.add(received);
			}
			if (port == 23) {
				if (received.equals("REQUEST_RESPONSE") && requestList.size() != 0 && requestList.get(0).length() == 4) {
					sendClientPacket = new DatagramPacket(requestList.get(0).getBytes(), requestList.get(0).length(), receivePacket.getAddress(), 3000);
					// reset
					requestList.clear();
				} else {
					sendClientPacket = new DatagramPacket(WAIT_FOR_RESPONSE.getBytes(), WAIT_FOR_RESPONSE.length(), receivePacket.getAddress(), 3000);
				}
				send(port, sendClientPacket);
				
			} else if (port == 24) {
				if (received.equals("REQUEST_DATA") && requestList.size() != 0 && requestList.get(0).length() > 4) {
					sendServerPacket = new DatagramPacket(requestList.get(0).getBytes(), requestList.get(0).length(), receivePacket.getAddress(), 69);
					// reset
					requestList.clear();
				} else {
					sendServerPacket = new DatagramPacket(WAIT_FOR_REQUEST.getBytes(), WAIT_FOR_REQUEST.length(), receivePacket.getAddress(), 69);
				}
				send(port, sendServerPacket);
			}
		}
	}

	/**
	 * Receiving the data in the given port
	 * @param port the port that it's listening to
	 * @return data in String
	 */
	private String receive(int port) {
		// Receive packet
		byte data[] = new byte[100];
		receivePacket = new DatagramPacket(data, data.length);
		try {
			receiveSocket.receive(receivePacket);
		} catch (IOException e) {
			System.out.print("IO Exception: likely:");
			System.out.println("Receive Socket Timed Out.\n" + e);
			e.printStackTrace();
			System.exit(1);
		}

		// Process the received packet
		String direction = "";
		int len = receivePacket.getLength();
		if (port == 23) {
			direction = "Client";
		} else if (port == 24){
			direction = "Server";
		}
		System.out.println(String.format("IntermediateHost: Packet received from %s:", direction));
		System.out.println("From host: " + receivePacket.getAddress());
		System.out.println("Host port: " + receivePacket.getPort());
		System.out.println("Data in bytes format: " + data);
		System.out.print("Data in String format: ");
		String received = new String(data, 0, len);
		// Form a String from the byte array.
		System.out.println(received + "\n");
		
		return received;
	}
	
	/**
	 * Sending the data to the port by giving the data packet
	 * @param port destination port
	 * @param packet the packet that it will send
	 */
	private void send(int port, DatagramPacket packet) {
		String direction = "";
		// Client
		if (port == 23) {
			direction = "Client";
			
		} else if (port == 24) {
			//Server
			direction = "Server";
		}
		
		System.out.println(String.format("IntermediateHost: Sending packet to %s:", direction));
		System.out.println("To host: " + packet.getAddress());
		System.out.println("Destination host port: " + packet.getPort());
		int len = packet.getLength();
		System.out.println("Data in bytes format: " + packet.getData());
		System.out.print("Data in String format: ");
		System.out.println(new String(packet.getData(), 0, len));

		// Send the datagram packet to the server via the send socket.
		try {
			sendSocket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("IntermediateHost: packet sent"+ "\n");
	}
	
	@Override
	public void run() {
		handleRequest(this.port);
		// We're finished, so close the sockets.
		receiveSocket.close();
		sendSocket.close();
	}

	public static void main(String args[]) {
		ArrayList<String> requestList = new ArrayList<>();
		Thread ih1 = new Thread(new IntermediateHost(23, requestList));
		Thread ih2 = new Thread(new IntermediateHost(24, requestList));
		ih1.start();
		ih2.start();
	}
}
