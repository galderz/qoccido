package org.example.ea.samples;

import org.example.ea.Main;

public class EASample_07_Runnable
{
    /**
     * This sample showcases the effects of creating new Runnable instances.
     * These are often passed to other threads to run,
     * so they can be assumed to be global escape.
     */

    static char sample1()
    {
        SampleRunnable runnable = new SampleRunnable();
        runnable.run();
        return runnable.aChar;
    }

    public static void main(String[] args)
    {
        Main.print(sample1());
    }

    public static class SampleRunnable implements Runnable
    {
        char aChar = 'F';

        @Override
        public void run()
        {
            aChar = '.';
        }
    }
}
