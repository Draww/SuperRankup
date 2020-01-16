package me.draww.superrup.api;

import me.draww.superrup.Rank;

public interface Action {

    default boolean onSetup() {
        return true;
    }

    String id();

    Integer queue();

    Rank rank();

}
