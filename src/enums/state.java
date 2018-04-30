package enums;

public enum state {
 STOPPED(0), RUNNING(1), CLOSED(2);
 private int sensorState;

state(int sensorState){
	this.sensorState = sensorState;
}

public int getSensorState() {
	return this.sensorState;
}
 
}
