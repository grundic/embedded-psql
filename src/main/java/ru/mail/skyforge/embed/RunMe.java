package ru.mail.skyforge.embed;

import de.flapdoodle.embed.process.config.IRuntimeConfig;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import ru.mail.skyforge.embed.postgresql.RestrictedPostgresStarter;
import ru.yandex.qatools.embed.postgresql.Command;
import ru.yandex.qatools.embed.postgresql.PostgresExecutable;
import ru.yandex.qatools.embed.postgresql.PostgresProcess;
import ru.yandex.qatools.embed.postgresql.PostgresStarter;
import ru.yandex.qatools.embed.postgresql.config.AbstractPostgresConfig;
import ru.yandex.qatools.embed.postgresql.config.PostgresConfig;
import ru.yandex.qatools.embed.postgresql.config.RuntimeConfigBuilder;

import java.io.IOException;

import static ru.yandex.qatools.embed.postgresql.distribution.Version.Main.V9_3;

/**
 * Author: g.chernyshev
 * Date: 02.07.15
 */
public class RunMe {

    public static void main(String[] args) throws Exception {
        new CommandLineParser().doMain(args);
    }

    private static class CommandLineParser {

        private PostgresExecutable exec;
        private PostgresProcess process;

        @Option(name = "--help", usage = "Show this message")
        private boolean help;

        @Option(name = "--hostname", usage = "Postgres listening host")
        private String hostname = "localhost";

        @Option(name = "--port", usage = "Postgres listening port")
        private Integer port = 5432;

        @Option(name = "--database-path", usage = "Postgres database path")
        private String databasePath = null;

        @Option(name = "--username", usage = "Postgres database username")
        private String username = "f1";

        @Option(name = "--password", usage = "Postgres database password")
        private String password = "f1";


        public void doMain(String[] args) throws IOException {
            CmdLineParser parser = new CmdLineParser(this);

            try {
                // parse the arguments.
                parser.parseArgument(args);

                if (help) {
                    parser.printUsage(System.out);
                    System.exit(0);
                }

                runPostgres(hostname, port, databasePath, username, password);
            } catch (CmdLineException e) {
                System.err.println(e.getMessage());
                parser.printUsage(System.err);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        private void runPostgres(String hostname, int port, String databasePath, String username, String password) {
            try {
                final IRuntimeConfig pgRuntimeConfig = new RuntimeConfigBuilder().defaults(Command.PgCtl).daemonProcess(false).build();
                PostgresStarter<PostgresExecutable, PostgresProcess> runtime = new RestrictedPostgresStarter(pgRuntimeConfig);

                final PostgresConfig config = new PostgresConfig(
                        V9_3,
                        new AbstractPostgresConfig.Net(hostname, port),
                        new AbstractPostgresConfig.Storage(null, databasePath),
                        new AbstractPostgresConfig.Locale("UTF-8", "C", "C", "C", "C", "C", "C", "C", true),
                        new AbstractPostgresConfig.Timeout(1000),
                        new AbstractPostgresConfig.Credentials(username, password)
                );

                exec = runtime.prepare(config);
                process = exec.start();

            } catch (Exception e) {
                e.printStackTrace();
            }


            Runtime.getRuntime().addShutdownHook(new Thread() {
                @Override
                public void run() {
                    process.stop();
                }
            });

            while (true) {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
