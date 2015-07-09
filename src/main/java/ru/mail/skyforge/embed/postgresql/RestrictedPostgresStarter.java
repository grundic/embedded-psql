package ru.mail.skyforge.embed.postgresql;

import de.flapdoodle.embed.process.config.IRuntimeConfig;
import de.flapdoodle.embed.process.distribution.Distribution;
import de.flapdoodle.embed.process.extract.IExtractedFileSet;
import ru.yandex.qatools.embed.postgresql.PostgresExecutable;
import ru.yandex.qatools.embed.postgresql.PostgresProcess;
import ru.yandex.qatools.embed.postgresql.PostgresStarter;
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig;

/**
 * Author: g.chernyshev
 * Date: 07.07.2015
 */
public class RestrictedPostgresStarter extends PostgresStarter<PostgresExecutable, PostgresProcess> {

    public RestrictedPostgresStarter(IRuntimeConfig runtimeConfig) {
        super(PostgresExecutable.class, runtimeConfig);
    }

    @Override
    protected PostgresExecutable  newExecutable(PostgresConfig config, Distribution distribution, IRuntimeConfig runtime, IExtractedFileSet exe) {
        return new RestrictedPostgresExecutable(distribution, config, runtime, exe);
    }
}
