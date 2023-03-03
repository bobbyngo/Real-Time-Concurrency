import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Send the request packet to port 24 and receive the data in port 69
 * If the data is not received, Server will keep sending the request packet. Else it will
 * parse the data and send the result to port 24
 */
public class Server {
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendSocket, receiveSocket;
	
	// Starting with 01, {1} allows occurring once, + concat, \\ is escape \ to seperate, $ end
	// Start with 01 only: ^0(1{1}+), fileName and concat: ([^(a-zA-Z)_\\-]+)\\.([a-zA-Z_\\-]+)
	// 0 as separate then some text then 0 ended: 0([a-zA-Z_\\-\\.]+)0$
	private static final String READ_REGEX = "^01+([a-zA-Z_\\-]+)\\.([a-zA-Z_\\-]+)0([a-zA-Z_\\-\\.]+)0$";
	private static final String WRITE_REGEX = "^02+([a-zA-Z_\\-]+)\\.([a-zA-Z_\\-]+)0([a-zA-Z_\\-\\.]+)0$";

	
	/**
	 * Constructor, init sendSocket and receiveSocket. Bind port 69 to receiveSocket of Server class
	 */
	public Server() {
		// Act as a server for Intermediate class
		try {
			sendSocket = new DatagramSocket();
			receiveSocket = new DatagramSocket(69);
		} catch (SocketException se) {
			se.printStackTrace();
			System.exit(1);
		}

	}

	/**
	 * Send the request data to Intermediate Host and receive the data
	 */
	public void receiveAndEcho() {
		// Request data from intermediate
		String received = "";
		for (int i = 0; i < 11; i++) {
			if (!received.equals("") && !received.equals("REQUEST_DATA")) {
				send(i, received);
			} else {
				send(i, "REQUEST_DATA");
			}
			
			received = receive(i);
			
			send(i, received);
			received = receive(i);
			
			while (received.equals("WAIT_FOR_REQUEST")) {	
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				send(i, "REQUEST_DATA");
				received = receive(i);
			}
		}
		// We're finished, so close the sockets.
		sendSocket.close();
		receiveSocket.close();
	}

	/**
	 * Send method for sending packet to port 24
	 * @param i index of the packet
	 * @param data of the packet 
	 */
	private void send(int i, String data) {
		try {
			byte[] response = parsePacket(data);
			// intermediate host needed
			sendPacket = new DatagramPacket(response, response.length, InetAddress.getLocalHost(), 24);

			System.out.println("Server: Sending packet to IntermediateHost: " + i);
			System.out.println("To host: " + sendPacket.getAddress());
			System.out.println("Destination host port: " + sendPacket.getPort());
			System.out.println("Data in bytes format: " + response);
			String responseInString = new String(response, 0, sendPacket.getLength());
			System.out.println("Data in String format: " + responseInString);
		} catch (Exception e1) {
			System.out.println(e1.toString());
			System.exit(1);
			
		}
		
		// Send the datagram packet to the intermediatehost via the send socket.
		try {
			sendSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

		System.out.println("Server: packet sent" + "\n");
	}
	
	/**
	 * Receiving method 
	 * @param i index of the packet
	 * @return data in String format
	 */
	private String receive(int i) {
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

		// Process the received packet from IntermediateHost.
		System.out.println("Server: Packet received from IntermediateHost: " + i);
		System.out.println("From host: " + receivePacket.getAddress());
		System.out.println("Host port: " + receivePacket.getPort());
		int len = receivePacket.getLength();
		System.out.println("Data in bytes format: " + data);
		System.out.print("Data in String format: ");

		// Form a String from the byte array.
		String received = new String(data, 0, len);
		System.out.println(received + "\n");
		
		return received;
	}
	
	
	
	/**
	 * Parsing the request and return the response
	 * @param stringData
	 * @return response of the packet
	 * @throws Exception
	 */
	private byte[] parsePacket(String stringData) throws Exception {
		String response;
		Pattern readPattern = Pattern.compile(READ_REGEX);
		Pattern writePattern = Pattern.compile(WRITE_REGEX);
		Matcher readMatcher = readPattern.matcher(stringData);
		Matcher writeMatcher = writePattern.matcher(stringData);
		
		if (stringData.equals("WAIT_FOR_REQUEST") || stringData.equals("REQUEST_DATA")) {
			response = "REQUEST_DATA";
		}
		// handle read request
		else if (readMatcher.lookingAt()) {
			response = "0301";
		} else if (writeMatcher.lookingAt()) {
		// handle write request
			response = "0400";
		}
		else {
		//handle error request
			throw new Exception("Invalid Request");
		}
		return response.getBytes();
	}

	public static void main(String args[]) {
		Server s = new Server();
		s.receiveAndEcho();
	}

}
