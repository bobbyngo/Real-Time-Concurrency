
/**
 * This class will set the timer based for 1s
 * then change the isPedestrianWaiting to false and signal
 * vehicle green when the timer it's up it will switch to the next
 * state based on isPedestrianWaiting  
 *
 */
public class VehiclesGreen extends VehiclesEnabled{
	private boolean isPedestrianWaiting;
	
	/**
	 * Constructor for VehiclesGreen
	 * @param context
	 */
	public VehiclesGreen(Context context) {
		super(context);
		context.setTimer(VehicleSignal.GREEN.getTimeout());
		this.isPedestrianWaiting = false;
		context.signalVehicles(VehicleSignal.GREEN);
	}

	/**
	 * Method for handle pedestrian waiting
	 */
	@Override
	public void handlePedestrianWaiting() {
		isPedestrianWaiting = true;
	}

	/**
	 * Method for handle timeout, switch to vehicleYellow
	 * when pedestrian is waiting else, vehicles green int
	 */
	@Override
	public void handleTimeout() {
		Context context = this.getContext();
		
		context.killTimer();
		if (this.isPedestrianWaiting) {
			context.setState(new VehiclesYellow(context));
		} else {
			context.setState(new VehiclesGreenInt(context));
		}
	}

	/**
	 * Override toString method
	 */
	@Override
	public String toString() {
		return "vehicle green";
	}
}
