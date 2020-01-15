package me.draww.superrup.api;

import me.draww.superrup.Rank;

public interface Action {

    default void onCompleted() {

    }

    String id();

    Integer queue();

    Rank rank();

}
