/**
 * Enum that is holding the signal for pedestrian
 *
 */
public enum PedestrianSignal {
	WALK(1500),
	DONT_WALK(1000),
	BLANK(1000);

	private int timeout;

	/**
	 * Constructor for Pedestrian Signal
	 * @param timeout
	 */
	private PedestrianSignal(int timeout) {
		this.timeout = timeout;
	}
	
	/**
	 * Getter timeout
	 * @return timeout
	 */
	public int getTimeout() {
		return this.timeout;
	}
}
