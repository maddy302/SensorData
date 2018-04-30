package sensor;

public class OceanSensor extends sensorMeta{

	private String coordinates;
	private double t_25;
	
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
