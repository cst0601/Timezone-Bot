package com.alchemist;

import java.awt.Color;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TimeListener extends ListenerAdapter {
	
	private Logger logger;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM月dd日 HH:mm");
	
	public TimeListener() {
		logger = LoggerFactory.getLogger(TimeListener.class);
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		Message message = event.getMessage();
		MessageChannel channel = event.getChannel();
		
		if (message.getContentDisplay().equals(">now")) {
			ZonedDateTime cst = ZonedDateTime.now(ZoneId.of("UTC+8"));
			ZonedDateTime jst = ZonedDateTime.now(ZoneId.of("UTC+9"));
			ZonedDateTime pst = ZonedDateTime.now(ZoneId.of("America/Los_Angeles"));
			ZonedDateTime est = ZonedDateTime.now(ZoneId.of("America/Detroit"));
			
			EmbedBuilder builder = new EmbedBuilder()
					.setTitle("現在時間")
					.setColor(Color.red)
					.addField("臺北", formatter.format(cst), true)
					.addField("東京", formatter.format(jst), true)
					.addBlankField(false)
					.addField("河濱市", formatter.format(pst), true)
					.addField("安娜堡", formatter.format(est), true)
					.addBlankField(false)
					.setFooter("Please support adandoning DST");
			
			channel.sendMessage(builder.build()).queue();
		}
	}

}
