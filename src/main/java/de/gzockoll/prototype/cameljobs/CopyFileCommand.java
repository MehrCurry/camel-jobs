package de.gzockoll.prototype.cameljobs;

import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Slf4j
@ToString
@EqualsAndHashCode
public class CopyFileCommand extends AbstractCommand {
    private static final long serialVersionUID = -1l;
    private final String source;
    private final String destination;
    private final boolean createTargetDirectories;

    public CopyFileCommand(CommandMode commandMode, String source, String destination, boolean createTargetDirectories) {
        super(commandMode);
        this.source = source;
        this.destination = destination;
        this.createTargetDirectories = createTargetDirectories;
    }

    @Override
    @SneakyThrows(IOException.class)
    public void run() {
        if (createTargetDirectories) {
            Paths.get(destination).toFile().getParentFile().mkdirs();
        }
        Files.copy(Paths.get(source),Paths.get(destination));
    }
}
