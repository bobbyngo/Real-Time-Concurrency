/**
 * Super class for Pedestrian state 
 *
 */
public abstract class PedestriansEnabled extends Operational{

	/**
	 * Constructor for PedestrianEnabled
	 * @param context
	 */
	public PedestriansEnabled(Context context) {
		super(context);
		context.signalVehicles(VehicleSignal.RED);
	}
}
