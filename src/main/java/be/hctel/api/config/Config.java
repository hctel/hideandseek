package be.hctel.api.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.bukkit.plugin.Plugin;
import org.json.JSONException;
import org.json.JSONObject;

public class Config {
	private File file;
	private JSONObject config;
	
	public Config(Plugin plugin, String name) throws FileNotFoundException, JSONException, IOException {
		file = new File(plugin.getDataFolder(), name);
		if(!file.exists()) {
			file.getParentFile().mkdirs();
			plugin.saveResource(name, false);
		}
		InputStream is = new FileInputStream(file);
		String out = IOUtils.toString(is, "UTF-8");
		if(out == null || (!out.startsWith("{") && !out.endsWith("}"))) {
			config = new JSONObject();
		} else {
			config = new JSONObject(out);
		}
		
		
	}
	
	public JSONObject getConfig() {
		return config;
	}
	
	public void save(JSONObject toSave) throws IOException {
		config = toSave;
		FileWriter writer = new FileWriter(this.file);
		writer.write(config.toString());
		writer.flush();
		writer.close();
	}
}
