package LineFollower;

import lejos.nxt.Motor;
import lejos.util.Delay;

public class LineFollower {
	public void move_forward(int duration){
		Motor.C.forward();
		Motor.B.forward();

		Delay.msDelay(duration);
		Motor.C.stop();
		Motor.B.stop();
	}
	public void move_backward(int duration){
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LineFollower lego = new LineFollower();
		lego.move_forward(2000);
	}

}
