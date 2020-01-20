package me.draww.superrup.api.exception;

import me.draww.superrup.api.Action;

public class ActionException extends Exception {

    private Action action;

    public ActionException(Action action, String message) {
        super(message);
        this.action = action;
    }

    public Action getAction() {
        return action;
    }
}
