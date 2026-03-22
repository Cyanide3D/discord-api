package ru.cyanide3d.discord.jda.plugin.lavalink.player;

import dev.arbjerg.lavalink.client.player.Track;

public class PlayerResultMessageFormatterImpl implements PlayerResultMessageFormatter {

    public String format(PlayerActionResult result) {
        if (result == null) {
            return "Неизвестный результат.";
        }

        if (result.isFailure()) {
            return formatFailure(result);
        }

        if (result.isNoop()) {
            return formatNoop(result);
        }

        if (result instanceof PlayerPlayResult playResult) {
            return formatPlay(playResult);
        }

        return switch (result.getActionType()) {
            case PAUSED -> "⏸ Пауза.";
            case RESUMED -> "▶️ Продолжаю воспроизведение.";
            case STOPPED -> "⏹ Остановлено.";
            case SKIPPED -> "⏭ Трек пропущен.";
            case SEEKED -> "⏩ Позиция изменена.";
            case VOLUME_CHANGED -> "🔊 Громкость изменена.";
            case QUEUE_CLEARED -> "🧹 Очередь очищена.";
            case FORGET -> "🗑 Состояние плеера сброшено.";
            default -> "Операция выполнена.";
        };
    }

    protected String formatPlay(PlayerPlayResult result) {
        return switch (result.getActionType()) {
            case STARTED -> "▶️ Запускаю: **" + trackTitle(result.getTrack()) + "**";
            case ENQUEUED -> "➕ Добавлено в очередь: **" + trackTitle(result.getTrack())
                    + "** (позиция: " + result.getQueuePosition() + ")";
            case PLAYLIST_STARTED -> "▶️ Запускаю плейлист. Треков: " + result.getPlaylistSize()
                    + ". Первый: **" + trackTitle(result.getTrack()) + "**";
            case PLAYLIST_ENQUEUED -> "➕ Плейлист добавлен в очередь. Треков: " + result.getPlaylistSize()
                    + ". Первый: **" + trackTitle(result.getTrack()) + "**";
            default -> "Операция выполнена.";
        };
    }

    protected String formatFailure(PlayerActionResult result) {
        String errorCode = result.getErrorCode();
        if (errorCode == null) {
            return "Не удалось выполнить действие плеера.";
        }

        return switch (errorCode) {
            case "TRACK_NOT_FOUND" -> "Ничего не найдено.";
            case "LOAD_FAILED" -> "Не удалось загрузить трек из Lavalink.";
            case "INVALID_POSITION" -> "Позиция должна быть больше или равна 0.";
            default -> "Не удалось выполнить действие плеера.";
        };
    }

    protected String formatNoop(PlayerActionResult result) {
        String errorCode = result.getErrorCode();
        if (errorCode == null) {
            return "Ничего не изменилось.";
        }

        return switch (errorCode) {
            case "NO_ACTIVE_TRACK" -> "Сейчас ничего не воспроизводится.";
            case "ALREADY_PAUSED" -> "Плеер уже стоит на паузе.";
            case "ALREADY_RESUMED" -> "Плеер и так не на паузе.";
            case "ALREADY_STOPPED" -> "Плеер уже остановлен.";
            case "QUEUE_ALREADY_EMPTY" -> "Очередь уже пуста.";
            case "VOLUME_UNCHANGED" -> "Громкость уже установлена в это значение.";
            default -> "Ничего не изменилось.";
        };
    }

    protected String trackTitle(Track track) {
        if (track == null || track.getInfo() == null || track.getInfo().getTitle() == null) {
            return "unknown track";
        }
        return track.getInfo().getTitle();
    }
}