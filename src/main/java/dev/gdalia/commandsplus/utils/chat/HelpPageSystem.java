package dev.gdalia.commandsplus.utils.chat;

import dev.gdalia.commandsplus.structs.Message;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class HelpPageSystem {

	private String[] Messages;
	private final int MaxLinesPerPage;

	public HelpPageSystem(int MaxLinesPerPage, String... args) {
		this.MaxLinesPerPage = MaxLinesPerPage;
		this.Messages = args;
	}

	public void setMessageList(String... strings) {
		this.Messages = strings;
	}

	public void addMessageLine(String string) {
		Arrays.asList(this.Messages).add(string);
	}

	public void showPage(CommandSender sender, int Page) {
		int theLastPageFix = (Messages.length / MaxLinesPerPage);
		try {
			sender.sendMessage(Message.fixColor("&6&lCommandsPlus Help Menu " + Page + "/" + (MaxLinesPerPage * theLastPageFix - MaxLinesPerPage < theLastPageFix ? theLastPageFix : theLastPageFix + 1)));
			for (int i = 0; i < MaxLinesPerPage * Page; i++) {
				if (i < MaxLinesPerPage * Page - MaxLinesPerPage) continue;
				sender.sendMessage(Message.fixColor(Messages[i]));
				sender.sendMessage(" ");
			}
		} catch (ArrayIndexOutOfBoundsException e1) {
			sender.sendMessage(Message.fixColor("&cEnd Of Pages!"));
		}
	}
}
