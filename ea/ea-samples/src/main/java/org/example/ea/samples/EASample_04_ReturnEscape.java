package org.example.ea.samples;

import org.example.ea.Asserts;

import java.util.List;

public class EASample_04_ReturnEscape
{
    public static void main(String[] args)
    {
        returnSetField();
        returnInline();
        returnWrappedOne();
        returnWrappedMany();
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

    static void returnWrappedOne()
    {
        final List<Train> trains = wrappedOne();
        Asserts.equals(0, trains.get(0).speed);
    }

    static List<Train> wrappedOne()
    {
        return List.of(new Train());
    }

    static void returnWrappedMany()
    {
        final List<Train> trains = wrappedMany();
        Asserts.equals(0, trains.get(0).speed);
    }

    static List<Train> wrappedMany()
    {
        return List.of(new Train(), new Train());
    }

    static class Train
    {
        String name;
        int speed;
    }
}
