package net.porillo;

import org.bukkit.ChatColor;

public class ChatStyle {

    private final String group;
    private final String format;
    private final String shownGroup;
    private final String tagColor;

    public ChatStyle(String... options) {
        this.group = options[0];
        this.format = options[1];
        this.shownGroup = options[2];
        this.tagColor = options[3];
    }

    public String format(boolean colorize, String... vars) {
        String chat = format.replace("%group", shownGroup);
        chat = chat.replace("%world", vars[0]);
        chat = chat.replace("%username", vars[1]);
        chat = chat.replace("%displayname", vars[2]);
        if (!colorize) {
            chat = ChatColor.translateAlternateColorCodes('&', chat);
            return chat.replace("%message", vars[3]);
        }
        chat = chat.replace("%message", vars[3]);
        return ChatColor.translateAlternateColorCodes('&', chat);
    }

    public String getExample() {
        return format(true, "world", "player", "player", "message");
    }

    public String getTagColor() {
        return tagColor;
    }

    public String getGroup() {
        return group;
    }
}
