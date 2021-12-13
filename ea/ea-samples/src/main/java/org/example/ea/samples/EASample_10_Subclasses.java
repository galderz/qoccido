package org.example.ea.samples;

import org.example.ea.Main;

public class EASample_10_Subclasses
{
    /*
     * This sample showcases the effects of calls on methods that are subclassed,
     * and the impact implementations have on whether values escape or not.
     */

    public static void main(String[] args)
    {
        /*
         * In the non-abstract sample,
         * a method is implemented in a super class and 2 classes override it.
         * One of those makes the parameter global escape.
         * As a result, calling that interface should pessimistically assume that the argument is always global escape.
         */
        NonAbstract.main();

        /*
         * In the abstract sample,
         * an abstract method is implemented by 2 classes.
         * One of those makes the parameter global escape.
         * As a result, calling that interface should pessimistically assume that the argument is always global escape.
         */
        Abstract.main();
    }

    static class NonAbstract
    {
        public static void main()
        {
            Main.print(weightInMars(60.0) == 22.69724770642202 ? '.' : 'F');
            Main.print(weightInJupiter(60.0) == 151.62079510703364 ? '.' : 'F');
        }

        private static double weightInMars(double weight)
        {
            final Person person = new Person(weight);
            final Mars mars = new Mars();
            return personWeight(person, mars);
        }

        private static double weightInJupiter(double weight)
        {
            final Person person = new Person(weight);
            final Jupiter jupiter = new Jupiter();
            return personWeight(person, jupiter);
        }

        private static double personWeight(Person person, Planet planet)
        {
            return planet.weight(person);
        }

        static class Planet
        {
            double weight(Person person)
            {
                throw new IllegalStateException("Can't calculate weight in unknown planet");
            }
        }

        static final class Mars extends Planet
        {
            @Override
            double weight(Person person)
            {
                return (person.weight / 9.81) * 3.711;
            }
        }

        static final class Jupiter extends Planet
        {
            static Person lastPerson;

            @Override
            double weight(Person person)
            {
                lastPerson = person;
                return (person.weight / 9.81) * 24.79;
            }
        }

        static final class Person
        {
            final double weight;

            Person(double weight)
            {
                this.weight = weight;
            }
        }
    }

    static class Abstract
    {
        public static void main()
        {
            Main.print(ageInMercury(20) == 82 ? '.' : 'F');
            Main.print(ageInSaturn(60) == 2 ? '.' : 'F');
        }

        private static int ageInMercury(int age)
        {
            final Person person = new Person(age);
            final Mercury mercury = new Mercury();
            return personAge(person, mercury);
        }

        private static int ageInSaturn(int age)
        {
            final Person person = new Person(age);
            final Saturn saturn = new Saturn();
            return personAge(person, saturn);
        }

        private static int personAge(Person person, Planet planet)
        {
            return planet.age(person);
        }

        static abstract class Planet
        {
            abstract int age(Person person);
        }

        static final class Mercury extends Planet
        {
            @Override
            int age(Person person)
            {
                return person.age * 365 / 88;
            }
        }

        static final class Saturn extends Planet
        {
            static Person lastPerson;

            @Override
            int age(Person person)
            {
                lastPerson = person;
                return (int) Math.round(person.age / 29.5);
            }
        }

        static final class Person
        {
            final int age;

            Person(int age)
            {
                this.age = age;
            }
        }
    }
}
