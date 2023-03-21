/**
 * VehicleYellow state, will set the timmer for 3s
 * After 3s it will set the new state which is PedestrianWalk
 */
public class VehiclesYellow  extends VehiclesEnabled{
	/**
	 *  Constructor for VehiclesYellow
	 * @param context
	 */
	public VehiclesYellow(Context context) {
		super(context);
		
		context.setTimer(VehicleSignal.YELLOW.getTimeout());
		context.signalVehicles(VehicleSignal.YELLOW);
	}

	/**
	 * Handle pedestrian waiting
	 */
	@Override
	public void handlePedestrianWaiting() {
		
	}
	
	/**
	 * handle timeout event and setting new state
	 */
	@Override
	public void handleTimeout() {
		Context context = getContext();
		context.killTimer();
		context.setState(new PedestriansWalk(context));
	}

	/**
	 * Override toString method
	 */
	@Override
	public String toString() {
		return "vehicle yellow";
	}
}
