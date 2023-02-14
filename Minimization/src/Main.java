public class Main {

    public static void main(String[] args) {
	Automat a = new Automat();
    a.input("input.txt");
    a.minimizate();
    a.output("output.txt");
    }
}
