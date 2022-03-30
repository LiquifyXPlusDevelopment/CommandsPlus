package dev.gdalia.commandsplus.utils;

import java.io.File;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import dev.gdalia.commandsplus.Main;

/**
 * MIT License
 * Copyright (c) 2022 Commands+
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * @author Gdalia, OfirTIM
 * @since 1.0-SNAPSHOT build number #2
 */

public class Config extends YamlConfiguration {

	private static final Plugin plugin = Main.getInstance();
	private static final File dataFolder = plugin.getDataFolder();
	
	@Getter
	private static final Map<String, Config> configs = new ConcurrentHashMap<>();
	
	@Setter
	@Getter
	private String name, folder;
	
	@Getter
	private File file;
	
	public Config(String name, @Nullable String folder, boolean loadFromDefault) {
		setName(name);
		setFolder(folder);
		configs.put(folder == null ? name : folder + File.separator + name, this);
		load(loadFromDefault);
	}
	
	public static Config getConfig(String name, @Nullable String folder, boolean loadFromDefault) {
		String path = folder == null ? name : folder + File.separator + name;
		return configs.containsKey(path) ? configs.get(name) : new Config(name, folder, loadFromDefault);
	}

	@SneakyThrows
	public void reloadConfig() {
		this.load(false);
	}

	@SneakyThrows
	protected void load(boolean defaultConfig) {
		if (defaultConfig) saveDefaultConfig();
		String path = (plugin.getDataFolder() + (folder != null ? (File.separator + folder) : ""));
		file = new File(path, name.toLowerCase() + ".yml");
		if (!plugin.getDataFolder().exists()) plugin.getDataFolder().mkdirs();
		if (!file.exists()) file.createNewFile();
		
		this.load(file);
	}

	
	@SneakyThrows
	public void saveDefaultConfig() {
		plugin.saveResource(name.toLowerCase() + ".yml", false);
	}
	
	@SneakyThrows
	public void saveConfig() {
		this.options().copyDefaults(true);
		this.options().copyHeader(true);
		this.save(file);
	}

	@SneakyThrows
	public static List<String> scanFolder(String folder) {
		String path = (folder != null ? (File.separator + folder) : "");
		File[] files = new File(dataFolder + path).listFiles();
		return Stream.of(files)
				.filter(File::isFile)
				.map(file -> file.getName().replaceAll(".yml", ""))
				.collect(Collectors.toList());
	}

	public Location convertToLocation(String section, boolean withYawPitch) {
		String[] splitter = this.getString(section).split(" ");
		World w = Bukkit.getWorld(splitter[0]);
		double x = Double.parseDouble(splitter[1]);
		double y = Double.parseDouble(splitter[2]);
		double z = Double.parseDouble(splitter[3]);

		if (!withYawPitch) return new Location(w, x, y, z);

		float yaw = Float.parseFloat(splitter[4]);
		float pitch = Float.parseFloat(splitter[5]);

		return new Location(w, x, y, z, yaw, pitch);
	}

	public static String convertFromLocation(Location loc) {
		DecimalFormat df = new DecimalFormat("#.##");

		return loc.getWorld().getName() + 
				" " + df.format(loc.getX()) +
				" " + df.format(loc.getY()) +
				" " + df.format(loc.getZ()) +
				" " + df.format(loc.getYaw()) +
				" " + df.format(loc.getPitch());
	}
}
