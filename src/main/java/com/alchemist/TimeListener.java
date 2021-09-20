package com.alchemist;

import java.awt.Color;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.zone.ZoneRulesException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TimeListener extends ListenerAdapter {
	
	private Logger logger;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM月dd日 HH:mm");
	private static final MessageEmbed helpMessage = new EmbedBuilder()
			.setTitle(">help 使用說明")
			.setColor(Color.red)
			.addField(">now", "取得現在時間", false)
			.addField(">convert -from=<time_zone> -to=<time_zone> <time>",
					  "時間轉換\n"
					  + "    -from <time_zone>, 來源時區\n"
					  + "    -to <time_zone>, 目標時區\n"
					  + "<time> 時間格式必須為：yyyy-MM-dd_HH:mm", false)
			.addField(">help", "取得協助", false)
			.addField(">sudo otsumiko", "おつみこ〜", false)
			.addBlankField(false)
			.setFooter("Please support adandoning DST")
			.build();
	
	public TimeListener() {
		logger = LoggerFactory.getLogger(TimeListener.class);
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent event) {
		Message message = event.getMessage();
		MessageChannel channel = event.getChannel();
		
		//CommandParser parser = new CommandParser(message.getContentDisplay());
		ArgParser parser = new ArgParser(message.getContentDisplay());

		
		if (parser.getCommand().equals(">now")) {
			getTimeNow(channel);
		}
		else if (parser.getCommand().equals(">convert")) {
			try {
				parser.parse();
				if (parser.getCommandSize() < 4) { 
					channel.sendMessage("Command error, see >help for more information.").queue();
					return;
				}
				
				String originTimeZone = parser.getString("from");
				String targetTimeZone = parser.getString("to");
				String time = parser.getCommand(1);				
				
				convertTime(channel, originTimeZone, targetTimeZone, time);
			} catch (ArgumentParseException e) {
				channel.sendMessage("An error occured when parsing command.").queue();
			}
		}
		else if (parser.getCommand().equals(">help")) {
			getHelp(channel);
		}
		
	}
	
	private void getTimeNow(MessageChannel channel) {
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
	
	private void convertTime(MessageChannel channel,
						     String originTimeZone,
						     String targetTimeZone,
						     String time) {
		try {
			ZoneId originZoneId = ZoneId.of(originTimeZone);
			ZoneId targetZoneId = ZoneId.of(targetTimeZone);
			ZonedDateTime originTime = LocalDateTime.parse(
						time, DateTimeFormatter.ofPattern("yyyy-MM-dd_HH:mm")
					).atZone(originZoneId);
			ZonedDateTime targetTime = originTime.withZoneSameInstant(targetZoneId);
			EmbedBuilder builder = new EmbedBuilder()
					.setTitle("時間轉換にぇ！")
					.setColor(Color.red)
					.addField(originTimeZone + " 時間", formatter.format(originTime), false)
					.addField("為", "", false)
					.addField(targetTimeZone + " 時間", formatter.format(targetTime), false)
					.addBlankField(false)
					.setFooter("Please support abandoning DST");
			channel.sendMessage(builder.build()).queue();
		} catch (ZoneRulesException e) {
			channel.sendMessage("時間帯は間違ってるみたいだにぇ！").queue();
		} catch (DateTimeParseException e) {
			channel.sendMessage("時間のフォーマットは間違ってるみたいだにぇ！").queue();
		} catch (Exception e) {
			channel.sendMessage("何か問題があったみたい...").queue();
		}
	}
	
	private void getHelp(MessageChannel channel) {
		channel.sendMessage(helpMessage).queue();
	}

}
