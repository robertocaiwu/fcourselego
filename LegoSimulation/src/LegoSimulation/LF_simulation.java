package LegoSimulation;

import ch.aplu.robotsim.LegoRobot;
import ch.aplu.robotsim.LightSensor;
import ch.aplu.robotsim.Motor;
import ch.aplu.robotsim.MotorPort;
import ch.aplu.robotsim.RobotContext;
import ch.aplu.robotsim.SensorPort;
import ch.aplu.robotsim.Tools;

public class LF_simulation {
	public LightSensor s1 = new LightSensor(SensorPort.S1);
	public LightSensor s2 = new LightSensor(SensorPort.S2);
	private Motor right_motor = new Motor(MotorPort.A);
	private Motor left_motor = new Motor(MotorPort.B);
	private LegoRobot robot = new LegoRobot();
	private int sensor;
	int P=0;
	public void setup_robot(){
		robot.addPart(right_motor);
		robot.addPart(left_motor);
		robot.addPart(s1);
		robot.addPart(s2);
		
	}
	public void move_forward(int left_m, int right_m){
		left_motor.setSpeed(left_m);
		left_motor.forward();
		right_motor.setSpeed(right_m);
		right_motor.forward();
		
	}
	public void set_sensor(){
		int ss1, ss2;
		
		if (s1.getValue()==0){
			ss1=0; //black
		}else{
			ss1=1; //white
		}
		if(s2.getValue()==0){
			ss2=0;
		}else{
			ss2=1;
		}
		System.out.println("S1 ="+ss1);
		System.out.println("S2 = "+ss2);
		sensor=0+0+ss1*4+ss2*8;
	//	System.out.println("Sensor= "+sensor);
//		System.out.println("test =" +0b1100);
	}
	public void tracking(){
		set_sensor();
		int pv = 0, sp = 0, error, kp = 50;
		int left_speed = 100, right_speed = 100;
		switch(sensor){
		case 0b0000 :
			pv = -1;
			move_forward(left_speed + P, right_speed - P*2);
		//	System.out.println();
			break;
		case 0b0100 :
			move_forward(left_speed, right_speed);
			pv = 0;
			break;
		case 0b1100 :
			move_forward(left_speed + P*2, right_speed - P);
			pv = +1;
			break;
		}
		error = sp-pv;
		P = kp * error;
		System.out.println(P);
		
	}
	public static void main(String[] args) {
		LF_simulation lego = new LF_simulation();
		lego.setup_robot();
		while(true){
			//lego.move_forward(100, 10);
			//Tools.delay(2000);
			lego.set_sensor();
			lego.tracking();
			
		}
		
	}
	
	static{
	    RobotContext.setStartPosition(70, 450);
	    RobotContext.setStartDirection(-90);
	    RobotContext.useBackground("sprites/track.png");
	    
	  
	} 

}
