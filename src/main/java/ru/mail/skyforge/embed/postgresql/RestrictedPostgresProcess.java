package ru.mail.skyforge.embed.postgresql;

import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.distribution.Distribution;
import de.flapdoodle.embed.process.extract.IExtractedFileSet;
import ru.yandex.qatools.embed.postgresql.PostgresExecutable;
import ru.yandex.qatools.embed.postgresql.PostgresProcess;
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: g.chernyshev
 * Date: 07.07.2015
 */
public class RestrictedPostgresProcess extends PostgresProcess {

    private IExtractedFileSet exe;
    private String storage;

    public RestrictedPostgresProcess(Distribution distribution, PostgresConfig config, IRuntimeConfig runtimeConfig, PostgresExecutable executable) throws IOException {
        super(distribution, config, runtimeConfig, executable);
    }

    @Override
    protected List<String> getCommandLine(Distribution distribution, PostgresConfig config, IExtractedFileSet exe) throws IOException {
        this.exe = exe;
        this.storage = config.storage().dbDir().getAbsolutePath();

        List<String> commandLine = new ArrayList<>();
        commandLine.addAll(
                Arrays.asList(
                        exe.executable().getAbsolutePath(),
                        "start",
                        "-D", config.storage().dbDir().getAbsolutePath()
                )
        );

        return commandLine;
    }

    @Override
    protected void stopInternal() {
        try {
            Runtime.getRuntime().exec(String.format("%s stop -D %s", this.exe.executable().getAbsolutePath(), this.storage));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
