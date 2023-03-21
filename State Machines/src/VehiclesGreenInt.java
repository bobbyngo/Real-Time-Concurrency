/**
 * VehiclesGreentInt class will signal Pedestrian don't walk
 *
 */
public class VehiclesGreenInt  extends VehiclesEnabled{
	/** 
	 * Constructor for VehiclesGreenInt
	 */
	public VehiclesGreenInt(Context context) {
		super(context);
	}

	/**
	 * handlePedestrianWaiting event
	 */
	@Override
	public void handlePedestrianWaiting() {
		Context context = this.getContext();
		context.setState(new VehiclesYellow(context));
	}

	/**
	 * handle timeout event
	 */
	@Override
	public void handleTimeout() {
	}

	/**
	 * override toString method
	 */
	@Override
	public String toString() {
		return "vehicle green int";
	}


}
