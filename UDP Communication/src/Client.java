import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * Create a request and send the packet to the IntermediateHost through port 23
 * Wait for the response in port 3000
 *
 */
public class Client {

	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendSocket, receiveSocket;

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
	 * Iterate from 0 to 10, if it's a number 10 then sends a error request. Even number then sends a read request, odd then sends a write request
	 * This method will send a datagram packet to IntermediateHost through port 23 then wait for the response in port 3000
	 */
	public void sendAndReceive() {
		byte[] msg;
		String request;

		for (int i = 0; i < 11; i++) {
			if (i == 10) {
				// Error request
				request = " error ";
				msg = "03test.txt0octet0".getBytes();
			} else if (i % 2 == 0) {
				// Read request
				request = " read ";
				msg = "01test.txt0octet0".getBytes();
	
			} else {
				// Write request
				request = " write ";
				msg = "02test.txt0octet0".getBytes();
			}

			try {
				// Construct a datagram packet that is to be sent to port 23 in localhost
				sendPacket = new DatagramPacket(msg, msg.length, InetAddress.getLocalHost(), 23);
			} catch (UnknownHostException e) {
				e.printStackTrace();
				System.exit(1);
			}

			System.out.println("Client: Sending" + request + "packet: " + i);
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
			System.out.println("Client: Received" + request + "packet: " + i);
			System.out.println("From host: " + receivePacket.getAddress());
			System.out.println("Host port: " + receivePacket.getPort());
			len = receivePacket.getLength();
			System.out.println("Information receive");
			System.out.println("Data in bytes format: " + data);
			System.out.print("Data in String format: ");

			// Form a String from the byte array.
			String received = new String(data, 0, len);
			System.out.println(received + "\n");

		}
		// We're finished, so close the socket.
		sendSocket.close();
		receiveSocket.close();
	}

	public static void main(String args[]) {
		byte[] byteArray0 = {'P', 'A', 'N', 'K', 'A', 'J'};
		byte[] byteArray1 = {80, 65, 78, 75, 65, 74};
		byte[] byteArray2 = {0, 1, 2, 3};
		byte[] byteArray3 = {'0', '1', '2', '3'};

		String str = new String(byteArray0, StandardCharsets.UTF_8);
		String str1 = new String(byteArray1, StandardCharsets.UTF_8);
		String str2 = Arrays.toString(byteArray0);
		String str3 = Arrays.toString(byteArray1);
		String str4 = new String(byteArray2, StandardCharsets.UTF_8);
		String str5 = Arrays.toString(byteArray2);
		String str6 = new String(byteArray3, StandardCharsets.UTF_8);
		String str7 = Arrays.toString(byteArray3);

		System.out.println(str);
		System.out.println(str1);
		System.out.println(str2);
		System.out.println(str3);
		System.out.println(str4);
		System.out.println(str5);
		System.out.println(str6);
		System.out.println(str7);
		//Client c = new Client();
		//c.sendAndReceive();
	}
}
