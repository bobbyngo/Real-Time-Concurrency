/**
 * Enum for vehicle signal
 */
public enum VehicleSignal {
	GREEN(1000),
	YELLOW(3000),
	RED(0);

	private int timeout;
	/**
	 * Constructor for vehicle signal
	 * @param timeout
	 */
	private VehicleSignal(int timeout) {
		this.timeout = timeout;
	}
	
	/**
	 * Getter for timeout
	 * @return
	 */
	public int getTimeout() {
		return this.timeout;
	}
}
