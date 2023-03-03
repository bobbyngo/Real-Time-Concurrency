import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * The Client will create and send the data to the IntermediateHost through port 23
 * Receive reply that IntermediateHost has received the data in port 3000
 * Create request_respond packet and send to port 23 to receive the response of its data
 * in port 3000. If the response is not received, it will keep asking
 */
public class Client {

	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendSocket, receiveSocket;
	
	/**
	 * Constructor for Client, binding the listening port to 3000
	 */
	public Client() {
		try {
			// Construct a datagram socket and bind it to any available
			// port on the local host machine. This socket will be used to
			// send UDP Datagram packets.
			sendSocket = new DatagramSocket();
			// port 3000 is for receiving packet from intermediatehost
			receiveSocket = new DatagramSocket(3000);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Send the data and receive response method in the order
	 */
	public void sendAndReceive() {
		byte[] msg;
		String request;

		for (int i = 0; i < 11; i++) {
			if (i == 10) {
				// Error request
				request = "error";
				msg = "03test.txt0octet0".getBytes();
			} else if (i % 2 == 0) {
				// Read request
				request = "read";
				msg = "01test.txt0octet0".getBytes();
	
			} else {
				// Write request
				request = "write";
				msg = "02test.txt0octet0".getBytes();
			}
			
			send(i, msg, request);
			String received = receive(i, request);
			
			request = "REQUEST_RESPONSE";
			//send(i, request.getBytes(), request);
			
			while (received.equals("WAIT_FOR_RESPONSE")) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				send(i, request.getBytes(), request);
				received = receive(i, request);			
			}
		}
		// We're finished, so close the socket.
		sendSocket.close();
		receiveSocket.close();
	}
	
	/**
	 * Send method for sending packet to port 23
	 * @param i index of the packet
	 * @param data of the packet that it will send
	 * @param request whether read, write, request or error request
	 */
	private void send(int i, byte[] data, String request) {
		try {
			// Construct a datagram packet that is to be sent to port 23 in localhost
			sendPacket = new DatagramPacket(data, data.length, InetAddress.getLocalHost(), 23);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Client: Sending " + request + " packet: " + i);
		System.out.println("To host: " + sendPacket.getAddress());
		System.out.println("Destination host port: " + sendPacket.getPort());
		int len = sendPacket.getLength();
		System.out.println("Information sending");
		System.out.print("Data in bytes format: ");
		System.out.println(sendPacket.getData());
		System.out.print("Data in String format: ");
		System.out.println(new String(sendPacket.getData(), 0, len));

		// Send the datagram packet to the server via the send/receive socket.
		try {
			sendSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Client: Packet sent.\n");
	}
	
	/**
	 * Receive method for receiving packet
	 * @param i index of packet
	 * @param request whether read, write, request or error request 
	 * @return response in String
	 */
	private String receive(int i, String request) {
		// Construct a DatagramPacket for receiving packets up
		// to 100 bytes long (the length of the byte array).
		byte data[] = new byte[100];
		receivePacket = new DatagramPacket(data, data.length);

		try {
			// Block until a datagram is received via sendReceiveSocket.
			receiveSocket.receive(receivePacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		// Process the received datagram.
		System.out.println("Client: Received " + request + " packet: " + i);
		System.out.println("From host: " + receivePacket.getAddress());
		System.out.println("Host port: " + receivePacket.getPort());
		int len = receivePacket.getLength();
		System.out.println("Information receive");
		System.out.println("Data in bytes format: " + data);
		System.out.print("Data in String format: ");

		// Form a String from the byte array.
		String received = new String(data, 0, len);
		System.out.println(received + "\n");
		return received;
	}

	public static void main(String args[]) {
		Client c = new Client();
		c.sendAndReceive();
	}
}
