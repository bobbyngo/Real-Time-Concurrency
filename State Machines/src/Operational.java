/**
 * The Operational class wraps PedestrianEnabled and VehiclesEnabled
 *
 */
public abstract class Operational {
	private Context context;
	
	/**
	 * Constructor for Operational
	 * @param context
	 */
	public Operational(Context context) {
		this.context = context;
	}
	
	public abstract void handlePedestrianWaiting();
	public abstract void handleTimeout();
	public abstract String toString();
	
	/**
	 * Getter for context so all the children use the same object
	 * @return
	 */
	public Context getContext() {
		return this.context;
	}
}
