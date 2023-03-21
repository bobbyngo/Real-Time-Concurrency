/**
 * Pedestrian flash will count down from 7, when the counter is even
 * the vehicleSignal will be BLANK, odd will be DONT_WALK, when the
 * counter is 0 the state will be VehiclesGreen
 */
public class PedestriansFlash extends PedestriansEnabled{

	private int pedestrianFlashCtr = 7;
	/**
	 * Constructor for PedestrianFlash
	 * @param context
	 */
	public PedestriansFlash(Context context) {
		super(context);
		context.setTimer(PedestrianSignal.DONT_WALK.getTimeout());
	}

	/**
	 * Constructor for PedestrianFlash
	 * @param context
	 * @param pedestrianFlashCtr
	 */
	public PedestriansFlash(Context context, int pedestrianFlashCtr) {
		this(context);
		this.pedestrianFlashCtr = pedestrianFlashCtr;
	}
	
	/**
	 * handle waiting event
	 */
	@Override
	public void handlePedestrianWaiting() {
		
	}

	/**
	 * Handle timeout event, decrement counter by 1 every timeout and set the new state
	 * to the current state and signal based on the counter, when the counter is 0, switching 
	 * to the new state
	 */
	@Override
	public void handleTimeout() {
		Context context = this.getContext();
		context.killTimer();
		
		this.pedestrianFlashCtr -= 1;
		if (this.pedestrianFlashCtr == 0) {
			context.setState(new VehiclesGreen(context));
		} else if ((this.pedestrianFlashCtr & 1) == 0) {
			context.signalPedestrians(PedestrianSignal.DONT_WALK);
			context.setState(new PedestriansFlash(context, this.pedestrianFlashCtr));
		} else {
			context.signalPedestrians(PedestrianSignal.BLANK);
			context.setState(new PedestriansFlash(context, this.pedestrianFlashCtr));
		}
	}

	/**
	 * Override toString
	 */
	@Override
	public String toString() {
		return "pedestrian flash";
	}

}
