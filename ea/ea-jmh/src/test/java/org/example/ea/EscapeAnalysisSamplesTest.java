package org.example.ea;

import org.junit.jupiter.api.Test;

public class EscapeAnalysisSamplesTest
{
    @Test
    public void test000()
    {
        var sample = new org_example_ea_samples_EASample_01_Basic_sample1_I_I();
        consume(sample.eaFactory.connectionGraph(sample.get()));
    }

    void consume(Object o)
    {
        System.out.println(o);
    }
}
