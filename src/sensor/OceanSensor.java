package sensor;

import java.time.LocalDateTime;

public class OceanSensor extends sensorMeta{

	private String coordinates;
	private double t_25;
	private double pressure,salinity,conductivity;
	private LocalDateTime recorded_on; 
	
	

	@Override
    public String toString() {
		StringBuilder sb = new StringBuilder();
		sb = sb.append("Ocean Sensor [id="+this.sensorId+",");
		sb = sb.append("Name="+this.sensorName+",");
		sb = sb.append("UserID="+this.masterUser+",");
		sb = sb.append("IP="+this.ipAddress+",");
		sb = sb.append("Latituded="+this.latitude+",");
		sb = sb.append("Longitude="+this.longitude+",");
		sb = sb.append("Created On="+this.createdTime+",");
		sb = sb.append("Updtaed On="+this.timeUpdated+",");
		sb = sb.append("Updated By="+this.lastUpdateBy+",");
		sb = sb.append("Sate="+this.state);
		
		return sb.toString();

    }

	public LocalDateTime getRecorded_on() {
		return recorded_on;
	}

	public void setRecorded_on(LocalDateTime recorded_on) {
		this.recorded_on = recorded_on;
	}
	public double getPressure() {
		return pressure;
	}



	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

/*	public double getTemperature() {
		return temperature;
	}

	public void setTemperature(double temperature) {
		this.temperature = temperature;
	}
*/
	public double getSalinity() {
		return salinity;
	}

	public void setSalinity(double salinity) {
		this.salinity = salinity;
	}

	public double getConductivity() {
		return conductivity;
	}

	public void setConductivity(double conductivity) {
		this.conductivity = conductivity;
	}
	public double getT_25() {
		return t_25;
	}

	public void setT_25(double t_25) {
		this.t_25 = t_25;
	}

	public String getCoordinates() {
		return coordinates;
	}

	public void setCoordinates(String coordinates) {
		this.coordinates = coordinates;
	}
}
