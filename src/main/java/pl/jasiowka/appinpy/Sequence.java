package pl.jasiowka.appinpy;

class Sequence {

    private static int sequenceHead = -1;

    public static String next() {
        return "id" + (++sequenceHead);
    }

}
