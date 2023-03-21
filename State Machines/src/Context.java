import java.util.Timer;
import java.util.TimerTask;

/**
 * A context class that has reference to Operational class
 * and handle the timeout and pedestrianWaiting events
 *
 */
public class Context {
	private Operational operational = new VehiclesGreen(this);
	private PedestrianSignal pedestrianSignal;
	private VehicleSignal vehicleSignal;
	private Timer timer;
	
	/**
	 * Constructor for Context
	 */
	public Context() {
		 System.out.println(this);;
	}
	
	/**
	 * Timeout event handler method
	 */
	public void timeout() {
		System.out.println("Event: TIME_OUT");
		this.operational.handleTimeout();
		System.out.println(this);
	};
	
	/**
	 * pedestrian waiting handler method
	 */
	public void pedestrianWaiting() {
		System.out.println("Event: PEDESTRIAN_WAITING");
		this.operational.handlePedestrianWaiting();
		System.out.println(this);
	};
	
	/**
	 * Setter for operation
	 * @param operation
	 */
	public void setState(Operational operation) {
		this.operational = operation;
	}

	/**
	 * Setter for signal Pedestrians
	 * @param signal
	 */
	public void signalPedestrians(PedestrianSignal signal) {
		this.pedestrianSignal = signal;
	}
	
	/**
	 * Setter for signal vehicles
	 * @param signal
	 */
	public void signalVehicles(VehicleSignal signal) {
		this.vehicleSignal = signal;
	}
	
	/**
	 * Set timmer method, the method will execute the timeout
	 * after the provided time
	 * @param seconds
	 */
	public void setTimer(int seconds) {
		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				timeout();
			}
			
		};
		this.timer = new Timer();
		this.timer.schedule(timerTask, seconds);
	}
	
	/**
	 * kill the timer method
	 */
	public void killTimer() {
		if(this.timer!=null) {
			timer.cancel();
		}
	}
	
	/**
	 * Override method for toString
	 */
	@Override
	public String toString() {
		return String.format("\nOperation: %s \nPedestrian Signal: %s \nVehicleSignal: %s\n", this.operational, this.pedestrianSignal, this.vehicleSignal);
		
	}
	
	public static void main(String[] args) {
		Context context = new Context();
		context.pedestrianWaiting();
	}
}
