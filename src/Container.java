
//Don't change the class name
public class Container{
	private Point data;//Don't delete or change this field;
	
	//ADDITIONAL PRIVATE FIELDS
	private Container prevX;
	private Container nextX;
	private Container prevY;
	private Container nextY;
	private Boolean inRange;
	
	public Container(Point point, Container prevX, Container nextX, Container prevY, Container nextY) {
		this.data = point;
		this.prevX = prevX;
		this.nextX = nextX;
		this.prevY = prevY;
		this.nextY = nextY;
		this.inRange = false;
	}
	
	//Constructor of new empty link
	public Container(Point point) { 
		this(point, null, null, null, null);
	}
	//Don't delete or change this function
	public Point getData(){
		return data;
	}
	
	//Get Next\Prev X and Y
	public Container getPrevX() {
		return prevX;
	}
	public Container getNextX() {
		return nextX;
	}
	public Container getPrevY() {
		return prevY;
	}
	public Container getNextY() {
		return nextY;
	}
	
	//Set Next\Prev X and Y
	public void setPrevX(Container prevX) {
		this.prevX = prevX;
	}
	public void setNextX(Container nextX) {
		this.nextX = nextX;
	}
	public void setPrevY(Container prevY) {
		this.prevY = prevY;
	}
	public void setNextY(Container nextY) {
		this.nextY = nextY;
	}
	public Boolean inRange() {
		return inRange;
	}
	public void changeInRange(Boolean currStat) {
		this.inRange = currStat;
	}
	
	
}
