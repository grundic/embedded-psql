package ru.mail.skyforge.embed.postgresql;

import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.distribution.Distribution;
import de.flapdoodle.embed.process.extract.IExtractedFileSet;
import ru.yandex.qatools.embed.postgresql.PostgresExecutable;
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig;

import java.io.IOException;

/**
 * Author: g.chernyshev
 * Date: 07.07.2015
 */
public class RestrictedPostgresExecutable extends PostgresExecutable {

    public RestrictedPostgresExecutable(Distribution distribution, PostgresConfig config, IRuntimeConfig runtimeConfig, IExtractedFileSet executable) {
        super(distribution, config, runtimeConfig, executable);
    }

    @Override
    protected RestrictedPostgresProcess start(Distribution distribution, PostgresConfig config, IRuntimeConfig runtime) throws IOException {
        return new RestrictedPostgresProcess(distribution, config, runtime, this);
    }
}
