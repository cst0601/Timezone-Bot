package com.alchemist;

import java.io.IOException;
import java.util.Properties;

import javax.security.auth.login.LoginException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

public class TimezoneBotMain {
	
	private JDA jda;
	private Logger logger;

	public static void main(String[] args) {
		try {
			new TimezoneBotMain().startUp();;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void startUp() {
		logger = LoggerFactory.getLogger(TimezoneBotMain.class);
		
		Properties properties = new Properties();
		try {
			properties.load(TimezoneBotMain.class.getResourceAsStream("/config.properties"));
		} catch (IOException e) {
			e.printStackTrace();
			logger.error("Failed to read from config.properties, please" +
					     "check if the file exists.");
			return;
		}
		
		String token = properties.getProperty("token", null);
		
		try {
			JDABuilder builder = JDABuilder.createDefault(token)
					.addEventListeners(new SystemCommandListener())
					.addEventListeners(new TimeListener())
					.setActivity(Activity.of(Activity.ActivityType.DEFAULT,"にゃっはろ〜"));
			
			jda = builder.build();
			jda.awaitReady();
			
		} catch (InterruptedException e) {
			// await is a blocking method, if interrupted
			e.printStackTrace();
		} catch (LoginException e) {
			// things go wrong in authentication
			e.printStackTrace();
		}
	}

}
