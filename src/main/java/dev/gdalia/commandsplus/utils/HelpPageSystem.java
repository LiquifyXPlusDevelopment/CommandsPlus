package dev.gdalia.commandsplus.utils;

import dev.gdalia.commandsplus.structs.Message;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HelpPageSystem {

	private final List<String> messages = new ArrayList<>();
	private final int maxLinesPerPage;

	public HelpPageSystem(int maxLinesPerPage, String... args) {
		this.maxLinesPerPage = maxLinesPerPage;
		this.messages.addAll(Arrays.asList(args));
	}

	public void setMessageList(String... strings) {
		this.messages.addAll(Arrays.asList(strings));
	}

	public void addMessageLine(String string) {
		this.messages.add(string);
	}

	public void showPage(CommandSender sender, int page) {
		int theLastPageFix = (messages.size() / maxLinesPerPage);
		try {
			sender.sendMessage(Message.fixColor("&6&lCommandsPlus Help Menu " + page + "/" + (maxLinesPerPage * theLastPageFix - maxLinesPerPage < theLastPageFix ? theLastPageFix : theLastPageFix + 1)));
			for (int i = 0; i < maxLinesPerPage * page; i++) {
				if (i < maxLinesPerPage * page - maxLinesPerPage) continue;
				sender.sendMessage(Message.fixColor(this.messages.get(i)));
				sender.sendMessage(" ");
			}
		} catch (ArrayIndexOutOfBoundsException e1) {
			sender.sendMessage(Message.fixColor("&cEnd Of Pages!"));
		}
	}
}
