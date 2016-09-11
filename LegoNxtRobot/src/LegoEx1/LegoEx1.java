package LegoEx1;

import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.comm.Bluetooth;
import lejos.util.Delay;

public class LegoEx1 {

	public void moveForward(int durationMS) {
		Motor.C.forward();
		Motor.B.forward();

		Delay.msDelay(durationMS);
		Motor.C.stop();
		Motor.B.stop();
		
	}
	
	public static void main(String[] args){
		LegoEx1 testRobot = new LegoEx1();
		testRobot.moveForward(2000);
		LightSensor s1 = new LightSensor(SensorPort.S1);
		System.out.print(s1);
	}
}
