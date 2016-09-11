package LegoEx1;

import lejos.nxt.LightSensor;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.comm.Bluetooth;
import lejos.util.Delay;

public class LineFollower {
	public LightSensor s1 = new LightSensor(SensorPort.S1);
	public LightSensor s2 = new LightSensor(SensorPort.S2);
	//private MMXMotor right_motor = new Motor(MotorPort.A);
	//private Motor left_motor = new Motor(MotorPort.B);
	//private LegoRobot robot = new LegoRobot();
	private int sensor;
	int P=0;
	public void move_forward(int left_m, int right_m, int pv){

		if(pv == 0){
			Motor.A.setSpeed(left_m);
			Motor.A.forward();
			Motor.B.setSpeed(right_m);
			Motor.B.forward();
		}else if (pv == -1){
			Motor.A.setSpeed(left_m);
			Motor.A.forward();
			Motor.B.setSpeed(left_m);
			Motor.B.backward();
		}else if(pv == 1){
			Motor.A.setSpeed(left_m);
			Motor.A.backward();
			Motor.B.setSpeed(right_m);
			Motor.B.forward();
		}
		
		
		
	}
	public void set_sensor(){
		int ss1, ss2;
		if (s1.readValue()<=44){
			ss1=0; //black
		}else{
			ss1=1; //white
		}
		if(s2.readValue()<=44){
			ss2=0;
		}else{
			ss2=1;
		}
		//System.out.println("S1 ="+ss1);
		//System.out.println("S2 = "+ss2);
		sensor=0+0+ss1*4+ss2*8;
	//	System.out.println("Sensor= "+sensor);
//		System.out.println("test =" +0b1100);
	}
	public void tracking(){
		set_sensor();
		int pv = 0, sp = 0, error, kp = 100;
		int left_speed = 400, right_speed = 400;
		switch(sensor){
		case 0b0000 : //all black
			pv = 0;
			//move_forward(left_speed, right_speed, pv);
		//	move_forward(left_speed + P, right_speed - P*2);
		//	System.out.println();
			break;
		case 0b0100 : //right is white
			pv = -1;
			//move_forward(left_speed, right_speed - P*2, pv);
			break;
		case 0b1000 : //left is white
			pv = 1;
			//move_forward(left_speed + P*2, right_speed, pv);
			break;
		case 0b1100 :
			//move_forward(left_speed + P*2, right_speed - P);
			//move_forward(left_speed, right_speed);
			//Delay.msDelay(500);
			//pv = 1;
			break;
		}
		error = sp-pv;
		P = kp * error;
		move_forward(left_speed, right_speed, pv);
		//System.out.println("PV = "+pv);
		//Delay.msDelay(500);
		
	}
	
	public static void main(String[] args) {
		LineFollower lego = new LineFollower();
		while(true){
			lego.set_sensor();
			lego.tracking();
//			Motor.A.setSpeed(500);
//			Delay.msDelay(500);
//			Motor.A.forward();
			
		}
		
	}
	
}
