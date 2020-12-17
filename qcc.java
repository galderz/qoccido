///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS cc.quarkus:qcc-main:1.0.0-SNAPSHOT
//DEPS cc.quarkus:qcc-plugin-native:1.0.0-SNAPSHOT
//DEPS cc.quarkus:qcc-plugin-linker:1.0.0-SNAPSHOT

import cc.quarkus.qcc.main.Main;

import static java.lang.System.*;

public class qcc
{
    public static void main(String... args)
    {
        out.println("Cociendo QCC...");

        Main.main(new String[]{
            "--boot-module-path"
            , "main.jar:/opt/qccrt/qccrt-java.base-11.0.1-SNAPSHOT.jar"
            , "--output-path"
            , "."
            , "main"
        });
    }
}

