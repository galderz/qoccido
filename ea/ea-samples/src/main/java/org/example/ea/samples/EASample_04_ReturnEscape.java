package org.example.ea.samples;

import org.example.ea.Asserts;

import java.util.List;

public class EASample_04_ReturnEscape
{
    public static void main(String[] args)
    {
        returnSetField();
        returnInline();
        returnWrapped();
    }

    static void returnSetField()
    {
        Train train = setField();
        Asserts.equals("New Jersey Freighter", train.name);
    }

    static Train setField()
    {
        Train train = new Train();
        train.name = "New Jersey Freighter";
        train.speed = 65;
        return train;
    }

    static void returnInline()
    {
        Train train = inline();
        Asserts.equals(0, train.speed);
    }

    static Train inline()
    {
        return new Train();
    }

    static void returnWrapped()
    {
        final List<Train> trains = wrapped();
        Asserts.equals(0, trains.get(0).speed);
    }

    static List<Train> wrapped()
    {
        return List.of(new Train());
    }

    static class Train
    {
        String name;
        int speed;
    }
}
