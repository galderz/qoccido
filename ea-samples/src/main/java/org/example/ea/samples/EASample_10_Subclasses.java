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
                return (person.earthWeight / 9.81) * 3.711;
            }
        }

        static final class Jupiter extends Planet
        {
            static Person lastPerson;

            @Override
            double weight(Person person)
            {
                lastPerson = person;
                return (person.earthWeight / 9.81) * 24.79;
            }
        }

        static final class Person
        {
            final double earthWeight;

            Person(double earthWeight)
            {
                this.earthWeight = earthWeight;
            }
        }
    }

}
