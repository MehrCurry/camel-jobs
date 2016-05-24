package de.gzockoll.prototype.cameljobs;

import java.io.Serializable;

/**
 * Created by guido on 23.04.16.
 */
public interface Command extends Runnable,Serializable {
    CommandMode getCommandMode();
    boolean isModeFast();
}
