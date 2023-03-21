/**
 * Super class for Vehicles State, signal don't walk
 * to pedestrian first
 */
public abstract class VehiclesEnabled extends Operational{

	/**
	 * Constructor for VehiclesEnabled
	 */
	public VehiclesEnabled(Context context) {
		super(context);
		context.signalPedestrians(PedestrianSignal.DONT_WALK);
		
	}

}
