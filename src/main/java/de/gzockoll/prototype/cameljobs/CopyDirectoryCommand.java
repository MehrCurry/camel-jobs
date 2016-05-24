package de.gzockoll.prototype.cameljobs;

import com.google.common.base.Stopwatch;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Slf4j
@ToString
@EqualsAndHashCode
public class CopyDirectoryCommand extends AbstractCommand {
    private static final long serialVersionUID = -1l;
    private final String source;
    private final String destination;

    public CopyDirectoryCommand(CommandMode commandMode, String source, String destination) {
        super(commandMode);
        this.source = source;
        this.destination = destination;
    }

    @Override
    @SneakyThrows(IOException.class)
    public void run() {
        Stopwatch sw = Stopwatch.createStarted();
        FileUtils.copyDirectory(new File(source),new File(destination));
        sw.stop();
        log.debug("Copy directories took {}",sw.toString());

    }
}
