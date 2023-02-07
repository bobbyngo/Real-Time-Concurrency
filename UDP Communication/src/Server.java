import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Server {
	private DatagramPacket sendPacket, receivePacket;
	private DatagramSocket sendSocket, receiveSocket;
	
	// Starting with 01, {1} allows occurring once, + concat, \\ is escape \ to seperate, $ end
	// Start with 01 only: ^0(1{1}+), fileName and concat: ([^(a-zA-Z)_\\-]+)\\.([a-zA-Z_\\-]+)
	// 0 as separate then some text then 0 ended: 0([a-zA-Z_\\-\\.]+)0$
	private static final String READ_REGEX = "^01+([a-zA-Z_\\-]+)\\.([a-zA-Z_\\-]+)0([a-zA-Z_\\-\\.]+)0$";
	private static final String WRITE_REGEX = "^02+([a-zA-Z_\\-]+)\\.([a-zA-Z_\\-]+)0([a-zA-Z_\\-\\.]+)0$";

	
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
	 * Receiving request and send the response back to Intermediate Host
	 */
	public void receiveAndEcho() {
		for (int i = 0; i < 11; i++) {
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

			// Send the received packet back to the IntermediateHost

			byte[] response;
			try {
				response = parsePacket(received);
				sendPacket = new DatagramPacket(response, response.length, receivePacket.getAddress(),
						receivePacket.getPort());

				System.out.println("Server: Sending packet to IntermediateHost: " + i);
				System.out.println("To host: " + sendPacket.getAddress());
				System.out.println("Destination host port: " + sendPacket.getPort());
				len = sendPacket.getLength();
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
		// We're finished, so close the sockets.
		sendSocket.close();
		receiveSocket.close();
	}

	/**
	 * Parsing the request and return the response
	 * @param stringData
	 * @return
	 * @throws Exception
	 */
	private byte[] parsePacket(String stringData) throws Exception {
		String response;
		Pattern readPattern = Pattern.compile(READ_REGEX);
		Pattern writePattern = Pattern.compile(WRITE_REGEX);
		Matcher readMatcher = readPattern.matcher(stringData);
		Matcher writeMatcher = writePattern.matcher(stringData);
		
		// handle read request
		if (readMatcher.lookingAt()) {
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
