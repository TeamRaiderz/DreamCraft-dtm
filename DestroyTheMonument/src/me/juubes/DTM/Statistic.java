package me.juubes.DTM;

public class Statistic implements Stat {
	public Statistic(String name, Integer[] stats) {
		if (stats == null)
			stats = new Integer[] { 0, 0, 0, 0, 0 };
		this.name = name;
		this.stats = stats;
	}

	private Integer[] stats;
	private String name;

	public String getName() {
		return name;
	}

	public Integer[] getStats() {
		return stats;
	}

}
