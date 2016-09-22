package LegoEx1;

import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.util.Delay;

public class PLineFollower {
	// Right sensor
	public LightSensor s1 = new LightSensor(SensorPort.S2);

	// Left sensor
	public LightSensor s2 = new LightSensor(SensorPort.S3);

	// Starting speed. Does not change during execution. Used as starting
	// velocity for both motors.
	public static int constantspeed = 0;

	// Motors cannot go over this speed.
	public static int max_speed = 0;

	// Motor acceleration to increase speed when not turning
	public static int acceleration = 0;

	// Motor A speed. Changes during execution.
	public static float A_speed = 0;

	// Motor B speed. Changes during execution.
	public static float B_speed = 0;

	// Proportional constant
	public static float kp = 0;

	// Right sensor read value.
	public static int sensor1 = 0;

	// Left sensor read value.
	public static int sensor2 = 0;

	// Stores the difference between sensor1 and sensor2 readings.
	public static int sensor_dif = 0;

	// Stores status of sensor1. White = 1, Black = 0
	public static int sensor1stat = 0;

	// Stores status of sensor2. White = 1, Black = 0
	public static int sensor2stat = 0;

	// Comes from sensor_dif. Used to calculate motor speed.
	public static int error = 0;

	// Value used to determine sensor status.
	public static int sensor_calib = 0;

	// Stores conditional events. 11 == go forward, 10 = turn right, 01 = turn
	// left, 00 = crossed intersection = go forward
	public static String sensor_condition = "";

	// Stores previous conditional event.
	public static String sensor_last_condition = "";

	void set_values(int sen_cal, int er, int cspeed, float k, int mspeed) {
		sensor_calib = sen_cal;
		error = er;
		constantspeed = cspeed;
		kp = k;
		max_speed = mspeed;
	}

	void set_values() {
		sensor_calib = 46;
		error = 0;
		constantspeed = 200;
		kp = (float) 3;
		max_speed = 400;
	}

	void sensorvalue() {
		sensor1 = s1.readValue();
		sensor2 = s2.readValue();
		sensor_dif = sensor1 - sensor2;
	}

	void set_motor_acceleration(int accel_a, int accel_b) {
		Motor.A.setAcceleration(accel_a);
		Motor.B.setAcceleration(accel_b);
	}

	void print__val() {
		System.out.println("S1= " + sensor1 + " mA=" + (A_speed));
		System.out.println("-S2=" + sensor2 + " mB= " + (B_speed));
		System.out.println("C=" + sensor_condition);
		System.out.println("-LC=" + sensor_last_condition);
	}

	void move_foward() {
		Motor.A.forward();
		Motor.B.forward();
	}

	void move_left() {
		Motor.A.forward();
		if (B_speed > 0)
			Motor.B.forward();
		else
			Motor.B.backward();
	}

	void reduce_motor_b_speed() {
		for (int i = (int) B_speed; i > 0; i--) {
			Motor.B.setSpeed(i);
			Delay.msDelay(1);
		}
	}

	void move_right() {
		if (A_speed > 0)
			Motor.A.forward();
		else
			Motor.A.backward();
		Motor.B.forward();
	}

	void reduce_motor_a_speed() {
		for (int i = (int) A_speed; i > 0; i--) {
			Motor.A.setSpeed(i);
			Delay.msDelay(1);
		}
	}

	void set_motor_speed(float a_speed, float b_speed, int err, float kp) {
		if (A_speed <= max_speed && A_speed >= max_speed * -1) {
			A_speed = a_speed + (err * kp);
		} else {
			A_speed = constantspeed + (err * kp);
		}
		if (B_speed <= max_speed && B_speed >= max_speed * -1) {
			B_speed = b_speed - (err * kp);
		} else {
			B_speed = constantspeed - (err * kp);
		}
		Motor.A.setSpeed(A_speed);
		Motor.B.setSpeed(B_speed);
	}

	void sensor_tracker(int sensor_val) {
		sensorvalue();
		if (sensor1 <= sensor_val)// Black
			sensor1stat = 0;
		else// White
			sensor1stat = 1;
		if (sensor2 <= sensor_val)// Black
			sensor2stat = 0;
		else// White
			sensor2stat = 1;
		sensor_condition = String.valueOf(sensor2stat) + String.valueOf(sensor1stat);

	}

	void controller() {
		switch (sensor_condition) {
		case "11":// move forward
			error = 0;
			set_motor_speed(constantspeed, constantspeed, error, kp);
			if (sensor_last_condition != sensor_condition) {
				move_foward();
			}
			break;
		case "10":// move right
			error = sensor_dif;
			set_motor_speed(A_speed, B_speed, error, kp);
			if (sensor_last_condition != sensor_condition)
				move_right();
			break;
		case "01":// move left
			error = sensor_dif;
			set_motor_speed(A_speed, B_speed, error, kp);
			if (sensor_last_condition != sensor_condition)
				move_left();
			break;
		case "00":// crossed intersection
			error = 0;
			set_motor_speed(constantspeed, constantspeed, error, kp);
			if (sensor_last_condition != sensor_condition) {
				move_foward();
			}
			break;
		}
		sensor_last_condition = sensor_condition;
		// Delay.msDelay(100);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		PLineFollower rob = new PLineFollower();
		rob.set_values(48, // sensor calibration
				0, // error
				250, // constant speed
				6, // kp
				400); // max speed
		while (true) {
			rob.sensor_tracker(sensor_calib);
			rob.controller();
			// rob.print__val();
			// Delay.msDelay(5000);
		}
	}

}
