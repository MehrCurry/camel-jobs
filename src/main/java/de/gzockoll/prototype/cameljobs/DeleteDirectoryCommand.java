package de.gzockoll.prototype.cameljobs;

import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.ToString;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static com.google.common.base.Preconditions.checkState;

@ToString
@EqualsAndHashCode
public class DeleteDirectoryCommand extends AbstractCommand {
    private static final long serialVersionUID = -1l;
    private final String toBeDeleted;

    public DeleteDirectoryCommand(CommandMode commandMode, String toBeDeleted) {
        super(commandMode);
        this.toBeDeleted = toBeDeleted;
    }

    @Override
    @SneakyThrows(IOException.class)
    public void run() {
        final File aFile = Paths.get(toBeDeleted).toFile();
        checkState(aFile.isDirectory());
        FileUtils.deleteDirectory(aFile);
    }
}
