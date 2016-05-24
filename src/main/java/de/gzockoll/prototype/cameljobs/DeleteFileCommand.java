package de.gzockoll.prototype.cameljobs;

import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.ToString;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static com.google.common.base.Preconditions.checkState;

@ToString
@EqualsAndHashCode
public class DeleteFileCommand extends AbstractCommand {
    private static final long serialVersionUID = -1l;
    private final String toBeDeleted;

    public DeleteFileCommand(CommandMode commandMode, String toBeDeleted) {
        super(commandMode);
        this.toBeDeleted = toBeDeleted;
    }

    @Override
    @SneakyThrows(IOException.class)
    public void run() {
        final Path path = Paths.get(toBeDeleted);
        if (Files.exists(path)) {
            checkState(path.toFile().isFile(),"This command supports only deleting files");
            Files.delete(path);
        }
    }
}
