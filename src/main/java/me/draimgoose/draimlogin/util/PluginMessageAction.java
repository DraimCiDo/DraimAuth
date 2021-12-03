package me.draimgoose.draimlogin.util;

import lombok.Getter;

public enum PluginMessageAction {
    ADD ("add"),
    REMOVE ("remove");

    @Getter
    private final String type;
    PluginMessageAction(String type){
        this.type = type;
    }
}
