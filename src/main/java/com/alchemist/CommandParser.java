package com.alchemist;

public class CommandParser {
	private String[] commands;
	
	public CommandParser(String command) {
		command = command.trim();
		commands = command.split("\\s+");
	}
	
	public String getMain() {
		return commands[0];
	}
	
	public String get(int i) throws NullPointerException {
		i += 1;
		if (i < 0 || i >= commands.length) throw new NullPointerException();
		return commands[i];
	}
}
