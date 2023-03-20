package contenedores;

/**
 * @author sanlosa
 * @author sergsan
 * @author juatorr
 *
 */
public class Container {
	private int weight;
	private int maxLoad;
	private int serialNum;
	
	public Container() {
		
	}
	
	public Container(int weight, int maxLoad, int serialNum) {
		setWeight(weight);
		setMaxLoad(maxLoad);
		setSerialNum(serialNum);
	}

	public int getWeight() {
		return weight;
	}

	private void setWeight(int weight) {
		this.weight = weight;
	}

	public int getMaxLoad() {
		return maxLoad;
	}

	private void setMaxLoad(int maxLoad) {
		this.maxLoad = maxLoad;
	}

	public int getSerialNum() {
		return serialNum;
	}

	private void setSerialNum(int serialNum) {
		this.serialNum = serialNum;
	}

}
