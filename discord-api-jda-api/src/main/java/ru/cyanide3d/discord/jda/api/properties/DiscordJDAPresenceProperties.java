package ru.cyanide3d.discord.jda.api.properties;

import lombok.Data;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

@Data
public class DiscordJDAPresenceProperties {

    private OnlineStatus status = OnlineStatus.ONLINE;

    private Activity.ActivityType activityType = Activity.ActivityType.LISTENING;

    private String activityName;

    private String activityUrl;

    public Activity toJdaActivity() {
        if (activityName == null || activityName.isBlank()) {
            return null;
        }
        return switch (activityType) {
            case PLAYING -> Activity.playing(activityName);
            case LISTENING -> Activity.listening(activityName);
            case WATCHING -> Activity.watching(activityName);
            case COMPETING -> Activity.competing(activityName);
            case CUSTOM_STATUS -> Activity.customStatus(activityName);
            case STREAMING -> Activity.streaming(activityName, activityUrl);
            default -> null;
        };
    }

}
