package LegoEx1;

import lejos.nxt.LightSensor;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.comm.Bluetooth;
import lejos.util.Delay;

public class LineFollower {
	public LightSensor s1 = new LightSensor(SensorPort.S1);//right
	public LightSensor s2 = new LightSensor(SensorPort.S2);//left
	//private MMXMotor right_motor = new Motor(MotorPort.A);
	//private Motor left_motor = new Motor(MotorPort.B);
	//private LegoRobot robot = new LegoRobot();
	private int sensor;
	int P=0, last_error;
	public void turn_left(){
		Motor.A.forward();
		Motor.B.backward();
	}
	public void turn_right(){
		Motor.A.backward();
		Motor.B.forward();
	}
	public void move_forward(){
		Motor.A.forward(); 
		Motor.B.forward();
	}
	public void move(int left_m, int right_m, int error, int last_error, int P){
		//-1, er=1, 150
		//1, er=-1, -150 //all black
		// Mot A = right // B=left
		int l_speed=0, r_speed=0;
		if(error == 0){
			l_speed = left_m;
			r_speed  = right_m;
			move_forward();
			
		}else if(error == -1){
			l_speed = left_m;
			r_speed = (right_m + P);
			turn_left();
		}else if(error == 1){
/*			if (last_error == 0){
				turn_left();
				l_speed = left_m;
				r_speed = right_m - P;
			}else {*/
				l_speed = (left_m - P);
				r_speed = right_m;
				turn_right();
			/*}*/
		}
		/*else if(error == 2){
			l_speed = left_m + P;
			r_speed = right_m;
			turn_left();
		}*/
		//System.out.println(l_speed+"++"+r_speed);
		//Delay.msDelay(200);
		Motor.A.setSpeed(l_speed);
		Motor.B.setSpeed(r_speed);
		//Motor.A.forward();
		//Motor.B.forward();
/*		if(pv == 0){
			Motor.A.setSpeed(left_m);
			Motor.A.forward();
			Motor.B.setSpeed(right_m);
			Motor.B.forward();
		}else if (pv == 1){
			Motor.A.setSpeed(left_m);
			Motor.A.forward();
			Motor.B.setSpeed(left_m);
			Motor.B.backward();
			Motor.A.setSpeed(left_m + P);
			Motor.A.forward();
			Motor.B.setSpeed(right_m);
			Motor.B.forward();
		}else if(pv == -1){
			Motor.A.setSpeed(left_m);
			Motor.A.backward();
			Motor.B.setSpeed(right_m);
			Motor.B.forward();
			
			Motor.A.setSpeed(left_m);
			Motor.A.forward();
			Motor.B.setSpeed(right_m - P);
			Motor.B.forward();
		}*/
		
		
		
	}
	public void set_sensor(){
		int ss1, ss2;
		if (s1.readValue()<=40){ //right
			ss1=0; //black
		}else{
			ss1=1; //white
		}
		if(s2.readValue()<=40){ //left
			ss2=0;
		}else{
			ss2=1;
		}
//		System.out.println("S1 ="+ss1);
	//	System.out.println("S2 = "+ss2);
	//	System.out.println("S1 ="+s1.readValue());
	//	System.out.println("S2 = "+s1.readValue());
		sensor=0+0+ss2*4+ss1*8;
	//	System.out.println("Sensor= "+sensor);
//		System.out.println("test =" +0b1100);
	}
	public void tracking(){
		set_sensor();
		//last_error=0;
		int pv = 0, sp = 0, error, kp = 100;
		int left_speed = 300, right_speed = 300;
		switch(sensor){
		case 0b0000 : //all black
			pv = 1;
			break;
		case 0b1000 : //
			pv = 1;
			//move_forward(left_speed, right_speed - P*2, pv);
			break;
		case 0b0100 : //left is white
			pv = 0;
			break;
		case 0b1100 : //turn_right
			pv = -1;
			break;
		}
		error = sp-pv;		//pv 1, err=-1, P=-150
		P = kp * error;		//pv -1, err=1, P=150
		last_error = error;
//		left_speed = left_speed + P;
//		right_speed = right_speed + P;
		move(left_speed, right_speed, error, last_error, P);
	//	System.out.println("PV = "+pv);
	//	System.out.println(left_speed+"++"+right_speed);
		//Delay.msDelay(500);
		
	}
	
	public static void main(String[] args) {
		LineFollower lego = new LineFollower();
		while(true){
			lego.set_sensor();
			lego.tracking();
			//Motor.A.setSpeed(300);
			//Delay.msDelay(200);
			//Motor.A.forward();
			
		}
		
	}
	
}
