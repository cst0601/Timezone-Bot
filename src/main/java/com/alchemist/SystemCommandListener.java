package com.alchemist;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class SystemCommandListener extends ListenerAdapter {
	
	private Logger logger;
	
	public SystemCommandListener() {
		logger = LoggerFactory.getLogger(SystemCommandListener.class);
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		Message message = event.getMessage();
		MessageChannel channel = event.getChannel();
		String messageText = message.getContentDisplay();
		
		if (message.isFromType(ChannelType.TEXT)) {
			
			Member member = event.getMember();
			if (messageText.equals(">sudo otsumiko")) {
				if (member.hasPermission(Permission.ADMINISTRATOR)) {
					logger.info("Exit command issued, exiting...");
					channel.sendMessage("おつみこ！").complete();
					
					System.exit(0);
					
				} else {
					logger.info("Attempt of issuing sudo command by " + message.getMember().getNickname());
					channel.sendMessage("Error: Sorry! You don't have the permission to do this!").queue();
				}
				
			}
		}
	}

}
