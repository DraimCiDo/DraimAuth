package me.draimgoose.draimlogin.telegram.callbackmanager.textcommand;

import me.draimgoose.draimlogin.DraimLogin;
import me.draimgoose.draimlogin.telegram.TelegramBot;
import me.draimgoose.draimlogin.telegram.callbackmanager.AbstractUpdate;
import me.draimgoose.draimlogin.util.LangConstants;
import me.draimgoose.draimlogin.util.TelegramPlayerInfo;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class AccountListTextCommand extends AbstractUpdate {
    @Override
    public void onUpdateCall(TelegramBot bot, Update update, String[] args) {
        String chatId = String.valueOf(update.getMessage().getChatId());

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(LangConstants.TG_ACCOUNT_LIST.getString() + "\n");
        DraimLogin.getInstance().getSql().getTelegramPlayerInfoList(chatId).whenComplete((telegramPlayers, throwable) -> {
            if (throwable != null)
                throwable.printStackTrace();
        }).thenAccept(telegramPlayers -> {
            if (!telegramPlayers.isEmpty()) {
                for (TelegramPlayerInfo tp : telegramPlayers) {
                    String status = tp.isLocked() ? LangConstants.TG_ACCOUNT_LIST_LOCKED.getString() : LangConstants.TG_ACCOUNT_LIST_UNLOCKED.getString();
                    stringBuilder.append(LangConstants.TG_ACCOUNT_LIST_SINTAX.getString()
                            .replaceAll("%accountID%", String.valueOf(tp.getAccountId()))
                            .replaceAll("%player_name%", tp.getPlayerName())
                            .replaceAll("%status%", status)
                    );
                    stringBuilder.append("\n");
                }
            }
            sendMessage.setText(stringBuilder.toString());
            try {
                bot.execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        });
    }
}
