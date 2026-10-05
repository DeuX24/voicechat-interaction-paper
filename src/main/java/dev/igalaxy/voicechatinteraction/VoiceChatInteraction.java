package dev.igalaxy.voicechatinteraction;

import de.maxhenkel.voicechat.api.BukkitVoicechatService;
import dev.igalaxy.voicechatinteraction.config.ServerConfig;
import org.bukkit.GameEvent;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class VoiceChatInteraction extends JavaPlugin {

    public static final String PLUGIN_ID = "voicechat_interaction";
    public static Logger LOGGER;
    public static ServerConfig SERVER_CONFIG;
    public static Server SERVER;
    public static GameEvent VOICE_GAME_EVENT;
    public static VoiceChatInteraction INSTANCE;

    /** Null until registered with Simple Voice Chat. */
    public static VoiceChatInteractionPlugin voicechatPlugin;

    @Override
    public void onEnable() {
        LOGGER = getLogger();
        SERVER = getServer();
        VOICE_GAME_EVENT = GameEvent.PRIME_FUSE;
        INSTANCE = this;

        FileConfiguration config = this.getConfig();
        config.addDefault("group_interaction", false);
        config.addDefault("whisper_interaction", false);
        config.addDefault("sneak_interaction", false);
        config.addDefault("minimum_activation_threshold", -50);
        config.addDefault("default_interaction_toggle", true);
        config.options().copyDefaults(true);
        saveConfig();

        SERVER_CONFIG = new ServerConfig(config);

        BukkitVoicechatService service = getServer().getServicesManager().load(BukkitVoicechatService.class);
        if (service != null) {
            voicechatPlugin = new VoiceChatInteractionPlugin();
            service.registerPlugin(voicechatPlugin);
            LOGGER.info("Successfully registered voicechat_interaction plugin");
        } else {
            LOGGER.warning("Failed to register voicechat_interaction plugin");
        }

        this.getCommand("voicechat_interaction").setExecutor(new VoiceChatInteractionCommand());
    }

    @Override
    public void onDisable() {
        if (voicechatPlugin != null) {
            getServer().getServicesManager().unregister(voicechatPlugin);
            LOGGER.info("Successfully unregistered voicechat_interaction plugin");
        }
    }
}
