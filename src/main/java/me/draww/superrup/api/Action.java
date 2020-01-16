package me.draww.superrup.api;

import me.draww.superrup.Rank;
import me.draww.superrup.api.exception.ActionException;

public interface Action {

    default void onSetup() throws ActionException {

    }

    String id();

    Integer queue();

    Rank rank();

}
