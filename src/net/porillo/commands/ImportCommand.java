package net.porillo.commands;


import net.porillo.ColoredGroups;
import org.bukkit.command.CommandSender;

import java.util.List;

import static org.bukkit.ChatColor.GREEN;
import static org.bukkit.ChatColor.RED;

public class ImportCommand extends AbstractCommand {


    public ImportCommand(ColoredGroups cg) {
        super(cg);
        super.setName("import");
        super.setPermission("coloredgroups.import");
        super.addUsage("Import groups from your permissions plugin");
    }

    @Override
    public void runCommand(CommandSender sender, List<String> args) {
        if (!this.checkPermission(sender)) {
            this.noPermission(sender);
            return;
        }

        try {
            super.plugin.runImport(true);
        } catch (Exception ex) {
            sender.sendMessage(RED + "Error: " + ex.getMessage());
            sender.sendMessage(RED + "Vault couldn't find any chat providers to import from");
        }
        super.sendMessage(sender, GREEN + "Imported groups via vault!");
    }
}