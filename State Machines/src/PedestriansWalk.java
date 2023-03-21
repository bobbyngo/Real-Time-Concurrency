/** 
 * PedestrianWalk State, after waiting the pedestrian walks
 * for 15s it will switch to the pedestrianFlash state
 */
public class PedestriansWalk extends PedestriansEnabled{
	/**
	 * Constructor for PedestrianWalk
	 * @param context
	 */
	public PedestriansWalk(Context context) {
		super(context);
		
		context.setTimer(PedestrianSignal.WALK.getTimeout());
		context.signalPedestrians(PedestrianSignal.WALK);
	}
	
	/**
	 * Handle waiting event
	 */
	@Override
	public void handlePedestrianWaiting() {
	}

	/**
	 * Handle timeout event
	 */
	@Override
	public void handleTimeout() {
		Context context = getContext();
		
		context.killTimer();
		context.setState(new PedestriansFlash(context));
	}

	/**
	 * Override toString method
	 */
	@Override
	public String toString() {
		return "pedestrians walk";
	}

}
