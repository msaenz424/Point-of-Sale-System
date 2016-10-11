package globalValues;

public class KeyValue {
	private String key, value;
	
	public KeyValue(String key, String value){
		this.key = key;
		this.value = value;
	}
	
	public String getKey(){
		return this.key;
	}
	
	public String getValue(){
		return this.value;
	}
	
	@Override
	public String toString(){
		return value;
	}

}
