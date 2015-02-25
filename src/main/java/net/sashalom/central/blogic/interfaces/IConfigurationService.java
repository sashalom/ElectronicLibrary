package net.sashalom.central.blogic.interfaces;

public interface IConfigurationService {
	
	public String getString(String name);
	
	public String getString(String name, String defaultValue);
	
	public int getInt(String name);
	
	public int getInt(String name, int defaultValue);

	public void setString(String name, String value);

}
