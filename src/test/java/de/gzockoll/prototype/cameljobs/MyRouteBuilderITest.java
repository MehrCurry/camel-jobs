package de.gzockoll.prototype.cameljobs;

import com.google.common.base.Stopwatch;
import com.google.common.eventbus.Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

import static de.gzockoll.prototype.cameljobs.CommandMode.FAST;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.IntStream.range;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(CamelJobsApplication.class)
@Slf4j
public class MyRouteBuilderITest {
    @EndpointInject(uri = "direct:command")
    private ProducerTemplate template;

    @Rule
    public TemporaryFolder folder1 = new TemporaryFolder();

    @Rule
    public TemporaryFolder folder2 = new TemporaryFolder();

    private List<Command> commandList;

    @Before
    public void setUp() {
        commandList = Collections.synchronizedList(new ArrayList<>());
    }

    @Subscribe
    public void commandCompleted(Command command) {
        // assertThat(commandList).contains(command);
        commandList.remove(command);
    }

    @Test
    public void testDelete() throws InterruptedException {
        submit(new DeleteFileCommand(FAST,"sdfsdfsdfsdfsdfsdf"));
        SECONDS.sleep(10);
    }

    private void submit(Command command) {
        commandList.add(command);
        template.sendBody(command);
    }

    @Test
    public void testDeleteAll() throws InterruptedException {
        Path temp = folder1.getRoot().toPath();
        List<Command> commands=new ArrayList<>();
        range(1, 1000).forEach(i -> {
            Path aPath = temp.resolve(String.format("temp%d.txt", i));
            try {
                Files.write(aPath, "Test".getBytes());
                commands.add(new DeleteFileCommand(FAST,aPath.toFile().getCanonicalPath()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        commands.forEach(this::submit);
        SECONDS.sleep(60);
    }

    @Test
    public void testCopy() throws InterruptedException {
        Path temp1 = folder1.getRoot().toPath();
        Path temp2 = folder2.getRoot().toPath();

        range(1, 100).forEach(i -> {
            Path aPath = temp1.resolve(String.format("copy%d.txt", i));
            Path dest = temp2.resolve(String.format("copy%d.txt", i));
            try {
                Files.write(aPath, "Test".getBytes());
                final CopyFileCommand command = new CopyFileCommand(FAST, aPath.toFile().getAbsolutePath(), dest.toFile().getAbsolutePath(), true);
                submit(command);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        SECONDS.sleep(30);
    }

    @Test
    public void testBig() throws IOException, InterruptedException {
        Path temp1 = folder1.getRoot().toPath();
        Path dir = Paths.get(System.getProperty("user.home")).resolve("wrk/testData");
        fillDirectory(dir);
        submit(new CopyDirectoryCommand(FAST,dir.toFile().getAbsolutePath(), temp1.toFile().getAbsolutePath()));
        SECONDS.sleep(60);
    }

    private static boolean isDirEmpty(final Path directory) throws IOException {
        try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(directory)) {
            return !dirStream.iterator().hasNext();
        }
    }

    private void fillDirectory(Path dir) throws IOException {
        dir.toFile().mkdirs();
        if (isDirEmpty(dir)) {
            Stopwatch sw = Stopwatch.createStarted();
            range(1, 10000).forEach(i -> {
                String id = UUID.randomUUID().toString().replace("-", "");
                String prefix = id.substring(0, 9);
                Path path = dir.resolve(
                        Arrays.asList(prefix.split("(?<=\\G.{3})")).stream()
                                .collect(Collectors.joining(File.separator)) + File.separator + id);
                path.toFile().getParentFile().mkdirs();
                try {
                    Files.write(path, "Test".getBytes());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            log.debug(sw.toString());
        }
    }

}
