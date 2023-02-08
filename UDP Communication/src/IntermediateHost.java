import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class IntermediateHost {
	private DatagramPacket sendClientPacket, receiveClientPacket, sendServerPacket, receiveServerPacket;
	private DatagramSocket sendClientSocket, receiveClientSocket, receiveSendServerSocket;

	// Act as a server for Client class
	// Receive datagram of Client -> send it to Server
	// Receive datagram of Server -> send it to Client
	public IntermediateHost() {
		try {
			sendClientSocket = new DatagramSocket();
			// receive data in port 23
			receiveClientSocket = new DatagramSocket(23);
			// Construct a datagram socket and bind it to any available
			// port on the local host machine. This socket will be used to
			// send and receive UDP Datagram packets.
			receiveSendServerSocket = new DatagramSocket();
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}
	}

	/**
	 * Calling the receiveClientSocket then receiveSendServerSocket to do the logic
	 */
	public void receiveAndEcho() {
		for (int i = 0; i < 11; i++) {
			sendClientRequest(i);
			receiveServerResponse(i);
		}

		// We're finished, so close the sockets.
		sendClientSocket.close();
		receiveClientSocket.close();
		receiveSendServerSocket.close();
	}
	
	/**
	 * This method receive the Client packet in port 23 and send it to the Server through port 69
	 * @param numPackage
	 */
	private void sendClientRequest(int numPackage) {
		// Receive Client packet
		byte data[] = new byte[100];
		receiveClientPacket = new DatagramPacket(data, data.length);

		try {
			receiveClientSocket.receive(receiveClientPacket);
		} catch (IOException e) {
			System.out.print("IO Exception: likely:");
			System.out.println("Receive Socket Timed Out.\n" + e);
			e.printStackTrace();
			System.exit(1);
		}

		// Process the received packet from Client.
		System.out.println("IntermediateHost: Packet received from Client: " + numPackage);
		System.out.println("From host: " + receiveClientPacket.getAddress());
		System.out.println("Host port: " + receiveClientPacket.getPort());
		int len = receiveClientPacket.getLength();
		System.out.println("Data in bytes format: " + data);
		System.out.print("Data in String format: ");

		// Form a String from the byte array.
		String received = new String(data, 0, len);
		System.out.println(received + "\n");
		
		// Send packet to the Server throught port 69
		sendServerPacket = new DatagramPacket(data, receiveClientPacket.getLength(), receiveClientPacket.getAddress(),
				69);

		System.out.println("IntermediateHost: Sending packet to Server: " + numPackage);
		System.out.println("To host: " + sendServerPacket.getAddress());
		System.out.println("Destination host port: " + sendServerPacket.getPort());
		len = sendServerPacket.getLength();
		System.out.println("Data in bytes format: " + sendServerPacket.getData());
		System.out.print("Data in String format: ");
		System.out.println(new String(sendServerPacket.getData(), 0, len));

		// Send the datagram packet to the server via the send socket.
		try {
			receiveSendServerSocket.send(sendServerPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("IntermediateHost: packet sent"+ "\n");
	}
	
	/**
	 * This method receive the Server response packet in any available port and send it 
	 * to Client through port 3000 
	 * @param numPackage
	 */
	private void receiveServerResponse(int numPackage) {
		// Receive packet from Server
		byte serverData[] = new byte[100];
		receiveServerPacket = new DatagramPacket(serverData, serverData.length);

		try {
			// Block until a datagram is received via sendReceiveSocket.
			receiveSendServerSocket.receive(receiveServerPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		// Process the received packet from Server.
		System.out.println("IntermediateHost: Packet received from Server: " + numPackage);
		System.out.println("From host: " + receiveServerPacket.getAddress());
		System.out.println("Host port: " + receiveServerPacket.getPort());
		int receiveServerPacketLength = receiveServerPacket.getLength();
		System.out.println("Data in bytes format: " + serverData);
		System.out.print("Data in String format: ");

		// Form a String from the byte array.
		String receivedServerData = new String(serverData, 0, receiveServerPacketLength);
		System.out.println(receivedServerData + "\n");
		
		
		// Send packet from Server to Client through port 3000
		sendClientPacket = new DatagramPacket(serverData, receiveServerPacket.getLength(), receiveServerPacket.getAddress(),
				3000);

		System.out.println("IntermediateHost: Sending packet to Client: " + numPackage);
		System.out.println("To host: " + sendClientPacket.getAddress());
		System.out.println("Destination host port: " + sendClientPacket.getPort());
		int len = sendClientPacket.getLength();
		System.out.println("Data in bytes format: " + sendClientPacket.getData());
		System.out.print("Data in String format: ");
		System.out.println(new String(sendClientPacket.getData(), 0, len));

		// Send the datagram packet to the client via the send socket.
		try {
			sendClientSocket.send(sendClientPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("IntermediateHost: packet sent"+ "\n");
	}

	public static void main(String args[]) {
		IntermediateHost ih = new IntermediateHost();
		ih.receiveAndEcho();
	}
}
