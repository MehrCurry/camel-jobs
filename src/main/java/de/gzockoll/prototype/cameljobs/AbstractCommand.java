package de.gzockoll.prototype.cameljobs;

import lombok.Getter;

import static de.gzockoll.prototype.cameljobs.CommandMode.FAST;

public abstract class AbstractCommand implements Command {
    @Getter
    protected final CommandMode commandMode;

    public AbstractCommand(CommandMode commandMode) {
        this.commandMode = commandMode;
    }

    @Override
    public boolean isModeFast() {
        return FAST==commandMode;
    }
}
