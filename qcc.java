///usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS cc.quarkus:qcc-driver:1.0.0-SNAPSHOT

import cc.quarkus.qcc.driver.Driver;
import cc.quarkus.qcc.driver.Main;

import static java.lang.System.*;

public class qcc
{
    public static void main(String... args)
    {
        out.println("QCC...");

        Main.main(new String[]{
            "--boot-module-path"
            , "hello.jar:/Users/g/1/qcc-class-library/openjdk/build/qccrt/jdk/modules/java.base"
            , "Hello"
        });
    }
}

