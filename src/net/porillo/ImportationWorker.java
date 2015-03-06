package net.porillo;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import static org.apache.commons.lang.WordUtils.capitalize;

public class ImportationWorker {

    private ColoredGroups cg;
    private Plugin vault;

    protected ImportationWorker(ColoredGroups cg) {
        this.cg = cg;
        this.vault = cg.getServer().getPluginManager().getPlugin("Vault");
    }

    protected void run(boolean override) {
        if (cg.getConfiguration().importer || override) {
            this.runImport(getPerms(), getChat(), getMainWorld());
        }
    }

    private Permission getPerms() {
        if (vault != null && vault.isEnabled()) {
            RegisteredServiceProvider<Permission> rsp = cg.getServer().getServicesManager()
                    .getRegistration(Permission.class);
            return rsp.getProvider();
        }
        return null;
    }

    private Chat getChat() {
        if (vault != null && vault.isEnabled()) {
            RegisteredServiceProvider<Chat> rsp = cg.getServer().getServicesManager()
                    .getRegistration(Chat.class);
            return rsp.getProvider();
        }
        return null;
    }

    private String getMainWorld() {
        return cg.getServer().getWorlds().get(0).getName();
    }

    private void cleanse() {
        YamlConfig conf = cg.getConfiguration();
        for (String section : conf.getYaml().getConfigurationSection("groups").getKeys(false)) {
            conf.deleteGroup(section);
        }
    }

    private void runImport(Permission perms, Chat chat, String world) {
        if (perms != null && chat != null) {
            this.cleanse();
            for (String group : perms.getGroups()) {
                cg.getConfiguration().createNewGroup(capitalize(group),
                        chat.getGroupPrefix(world, group), chat.getGroupSuffix(world, group), "&f",
                        "[%g]%p: %m", capitalize(group));
                cg.debug("Imported " + group);
            }
            cg.getConfiguration().set("options", "import", false);
        } else {
            cg.warn("Vault was not found, import could not be done");
            cg.getConfiguration().set("options", "import", false);
        }
    }
}
