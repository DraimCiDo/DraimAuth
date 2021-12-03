package me.draimgoose.draimlogin.util;

import lombok.Getter;
import me.draimgoose.draimlogin.DraimLogin;
import org.bukkit.configuration.file.FileConfiguration;

public enum LangConstants {

    PREFIX("prefix"),
    TG_ADD_MESSAGE("info.telegram.add-message"),
    TG_LOGIN_MESSAGE("info.telegram.login-message"),
    TG_LOCKED_MESSAGE("info.telegram.locked-message"),
    TG_LOCKED_MESSAGE_BY_COMMAND("info.telegram.locked-message-by-command"),
    TG_CHATID_NOT_LINKED("errors.telegram.chat-id-not-linked"),
    TG_ACCOUNTID_NOT_LINKED("errors.telegram.account-id-not-linked"),
    TG_UNLOCKED_MESSAGE("info.telegram.unlocked-message"),
    TG_ABORT_BUTTON_TEXT("info.telegram.abort-button"),
    TG_CONFIRM_BUTTON_TEXT("info.telegram.confirm-button"),
    TG_LOGIN_EXECUTED("info.telegram.login-executed"),
    TG_LOCK_BUTTON_TEXT("info.telegram.lock-button"),
    TG_UNLOCK_BUTTON_TEXT("info.telegram.unlock-button"),
    TG_ABORTED_MESSAGE("info.telegram.aborted"),
    TG_CHATID_CONFIRMED("info.telegram.chat-id-confirmed"),
    TG_PLAYER_OFFLINE("errors.telegram.player-offline"),
    TG_HELP_MESSAGE("info.telegram.help-message"),
    TG_START_MESSAGE("info.telegram.start-message"),
    TG_CHATID_MESSAGE("info.telegram.chat-id-message"),
    TG_ACCOUNT_LIST("info.telegram.account-list.message"),
    TG_ACCOUNT_LIST_SINTAX("info.telegram.account-list.sintax"),
    TG_ACCOUNT_LIST_LOCKED("info.telegram.account-list.locked"),
    TG_ACCOUNT_LIST_UNLOCKED("info.telegram.account-list.unlocked"),
    TG_INVALID_VALUE("errors.telegram.invalid-value"),
    TG_UNLOCK_USAGE("errors.telegram.unlock-usage"),
    TG_LOCK_USAGE("errors.telegram.lock-usage"),
    INGAME_ACCOUNT_LIST("info.in-game.account-list.message"),
    INGAME_ACCOUNT_LIST_SINTAX("info.in-game.account-list.sintax"),
    INGAME_ACCOUNT_LIST_LOCKED("info.in-game.account-list.locked"),
    INGAME_ACCOUNT_LIST_UNLOCKED("info.in-game.account-list.unlocked"),
    INGAME_ACCOUNT_LINKED("info.in-game.account-linked"),
    INGAME_ADD_CHATID("info.in-game.add-chat-id"),
    INGAME_CHATID_CHANGED("info.in-game.chat-id-changed"),
    INGAME_TARGET_REMOVED("info.in-game.target-removed"),
    INGAME_CANNOT_REMOVE("errors.in-game.2FA.cannot-remove"),
    INGAME_TWOFA_DISABLED("errors.in-game.2FA.disabled"),
    INGAME_SENDER_ALREADY_2FA("errors.in-game.2FA.player-already2FA"),
    INGAME_SENDER_WITHOUT_DRAIMLOGIN("errors.in-game.2FA.player-without-draimLogin"),
    INGAME_WAIT_FOR_CONFIRM("info.in-game.wait-for-confirm"),
    INGAME_WAIT_FOR_LOGIN_CONFIRM("info.in-game.wait-for-login-confirm"),
    INGAME_INVALID_VALUE("errors.in-game.invalid-value"),
    INGAME_OPERATION_ABORTED("info.in-game.operation-aborted"),
    INGAME_ABORT_2FA("info.in-game.2FA.abort-2FA"),
    INGAME_ACCOUNT_DISCONNECTED("info.in-game.2FA.account-disconnected"),
    INGAME_KICK_LOG_AGAIN("info.in-game.kick-logagain"),
    INGAME_KICK_ACCOUNT_LOCKED("info.in-game.kick-accountLocked"),
    INGAME_LOGIN_EXECUTED("info.in-game.login-executed"),
    INGAME_HELP_PLAYER("info.in-game.help.player"),
    INGAME_HELP_ADMIN("info.in-game.help.admin"),
    INGAME_RELOAD_EXECUTED("info.in-game.reload-executed"),
    INGAME_ONLY_PLAYER("errors.in-game.only-player"),
    INGAME_CHANGE_CHATID_USAGE("errors.in-game.change-chat-id-usage"),
    INGAME_REMOVE_USAGE("errors.in-game.remove-usage"),
    INGAME_TARGET_OFFLINE("errors.in-game.target-offline"),
    INGAME_TARGET_WITHOUT_DRAIM_LOGIN("errors.in-game.target-without-draimLogin"),
    INGAME_LOGINSESSION_LOGGED("info.in-game.login-session.logged-automatically"),
    INGAME_NOPERMISSION("errors.in-game.no-permission");


    private final DraimLogin plugin = DraimLogin.getInstance();
    @Getter
    private final String path;

    LangConstants(String path) {
        this.path = path;
    }

    private String getPrefix() {
        return plugin.getLangConfig().getString(PREFIX.getPath());
    }

    public String getString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (plugin.getLangConfig().isList(path)) {
            for (String s : plugin.getLangConfig().getStringList(path)) {
                stringBuilder.append(s + "\n");
            }
        } else {
            return plugin.getLangConfig().getString(path).replaceAll("%prefix%", getPrefix());
        }
        return stringBuilder.toString().replaceAll("%prefix%", getPrefix());
    }

    public String getFormattedString() {
        return Util.color(getString());
    }

}
