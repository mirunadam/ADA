import java.util.Arrays;
import java.util.Comparator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.Period;

interface Sorter2<T> {
    String getName();

    void sort(T[] a, Comparator<T> comparator);
}

class JavaArraysSorter2<T> implements Sorter2<T> {
    private final String name = "ArraysSort2";

    public String getName() {
        return name;
    }

    public void sort(T[] a, Comparator<T> comparator) {
        Arrays.sort(a, comparator);
    }
}
class InsertionSorter2<T> implements Sorter2<T> {
    private final String name = "InsertionSort2";

    public String getName() {
        return name;
    }

    private void exch(T[] a, int i, int j) {
        T temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    public void sort(T[] a, Comparator<T> comparator) {
        int n = a.length;
        for (int i = 0; i < n; i++) {
            for (int j = i; j > 0; j--) {
                if (comparator.compare(a[j], a[j-1]) < 0) exch(a, j, j - 1);
                else break;
            }
        }
    }
}
class MergeSorter2<T> implements Sorter2<T> {

    private T[] a;
    private T[] aux;
    private Comparator<T> comparator;

    @Override
    public String getName() {
        return "MergeSorter2";
    }

    public void sort(T[] a, Comparator<T> comparator) {
        this.a = a;
        this.aux = Arrays.copyOf(a, a.length);
        this.comparator = comparator;
        mergeSort(0, a.length - 1);
    }

    private void mergeSort(int lo, int hi) {
        if (hi <= lo) return;
        int mid = lo + (hi - lo) / 2;

        mergeSort(lo, mid);
        mergeSort(mid + 1, hi);

        if (comparator.compare(a[mid + 1],a[mid]) >= 0) return;  // don't waste time with a merge
        // if a[] is already in order

        merge(lo, mid, hi);
    }

    private void merge(int lo, int mid, int hi) {
        for (int k = lo; k <= hi; k++) {
            aux[k] = a[k];
        }

        int i = lo;
        int j = mid + 1;
        for (int k = lo; k <= hi; k++) {
            if (i > mid) a[k] = aux[j++];
            else if (j > hi) a[k] = aux[i++];
            else if (comparator.compare(aux[j], aux[i]) < 0) a[k] = aux[j++];
            else a[k] = aux[i++];
        }
    }
}

class Person{
    String ID;
    String firstname;
    String lastname;
    String birthdate;

    public Person(String ID, String firstname, String lastname, String birthdate) {
        this.ID = ID;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
    }

    public String toString() {
        return ID + " " + firstname + " " + lastname + " " + birthdate;
    }

    public int getAge() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate birthDate = LocalDate.parse(this.birthdate, formatter);
        LocalDate today = LocalDate.now();
        return Period.between(birthDate, today).getYears();
    }

}
class FirstnameComparator implements Comparator<Person> {
    public int compare(Person p1, Person p2) {
        return p1.firstname.compareTo(p2.firstname);
    }
}

class LastnameComparator implements Comparator<Person> {
    public int compare(Person p1, Person p2) {
        return p1.lastname.compareTo(p2.lastname);
    }
}

class AgeComparator implements Comparator<Person> {
    public int compare(Person p1, Person p2) {
            return Integer.compare(p1.getAge(), p2.getAge());
    }
}


public class GenericSorter {
    public static void main(String[] args) {
        Integer[] numbers = {5, 8, 2, 9, 1, 5, 6, 3, 7};
        Sorter2<Integer> sorterInt = new MergeSorter2<>();
        sorterInt.sort(numbers, Comparator.naturalOrder());
        System.out.println("sorted Integers: " + Arrays.toString(numbers));

        String[] words = {"Anna", "John", "Alex", "Mary", "Christian"};
        Sorter2<String> stringSorter = new JavaArraysSorter2<>();
        stringSorter.sort(words, Comparator.naturalOrder());
        System.out.println("sorted Strings: " + Arrays.toString(words));
        System.out.println();

        Person[] persons = {
                new Person("9E39Bfc4fdcc44e", "Diamond", "Dudley", "06/12/1945"),
                new Person("32C079F2Bad7e6F", "Ethan", "Hanson", "08/03/1985"),
                new Person("a1F7faeBf5f7A3a", "Grace", "Huerta", "21/01/1941"),
                new Person("c7Dd389CA514dce", "Jillian", "Bryant", "17/01/1933"),
                new Person("eb3Dae1D4cd0A35", "Warren", "Simpson", "02/05/2003")
        };

        Sorter2<Person> personSorter = new InsertionSorter2<>();
        personSorter.sort(persons, new FirstnameComparator());
        System.out.println("sorted by firstname:");
        for (Person p : persons) {
            System.out.println(p);
        }
        System.out.println();

        personSorter.sort(persons, new LastnameComparator());
        System.out.println("sorted by lastname:");
        for (Person p : persons) {
            System.out.println(p);
        }
        System.out.println();

        personSorter.sort(persons, new AgeComparator());
        System.out.println("sorted by age:");
        for (Person p : persons) {
            System.out.println(p + ", age: " + p.getAge());
        }
    }
}