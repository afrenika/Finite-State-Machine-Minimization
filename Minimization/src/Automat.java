import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

public class Automat {
    private int n;
    private HashSet<Integer> q;
    private HashMap<Integer, HashMap<String, Integer>> map;
    private String[] simb;
    private List<Integer> fin;

    public Automat() {}

    public void input(String inputFile){
        try(Scanner scanner = new Scanner(new File(inputFile))){
        map = new HashMap<>();
        n = Integer.parseInt(scanner.nextLine());
        q = new LinkedHashSet<>();
        fin = Arrays.stream(scanner.nextLine().split(" ")).map(Integer::parseInt).toList();
        simb = scanner.nextLine().trim().split(" ");
        for(int i = 0; i < n; i++){
            Integer [] s = Arrays.stream(scanner.nextLine().split(" ")).map(Integer::parseInt).toArray(Integer[]::new);
            HashMap<String, Integer> map1 = new HashMap<>();
            q.add(s[0]);
            for(int j = 0; j< simb.length; j++){
                map1.put(simb[j], s[j + 1]);}
            map.put(i, map1);}}
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void output(){
        if (n != 0){
            System.out.println(n);
            fin.forEach(i -> System.out.print(i + " "));
            System.out.print("\n  ");
            Arrays.stream(simb).toList().forEach(i -> System.out.print(i + " "));
            System.out.println();

            for(Map.Entry<Integer, HashMap<String, Integer>> entry1:map.entrySet()){
                System.out.print(entry1.getKey());
                entry1.getValue().values().forEach(i -> System.out.print(" " + i));
                System.out.println();
        }}}

    public void output(String output){
        try(Writer writer = new FileWriter(output)) {
            if (n != 0){
                writer.write(n + "\n");
                for(Integer i:fin){
                    writer.write(i + " ");}
                writer.write("\n  ");
                for(String s: simb){
                    writer.write(s + " ");}
                writer.write("\n");

                for(Map.Entry<Integer, HashMap<String, Integer>> entry1:map.entrySet()){
                    writer.write(entry1.getKey().toString());
                    for(Integer i:entry1.getValue().values()){
                        writer.write(" " + i.toString());}
                    writer.write("\n");
                }}
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void minimizate(){
        stepZero();
        HashMap<Integer, Integer> fact1 = nSteps();
        createAuto(fact1);
    }

    //removal of unreachable points
    public void stepZero(){
        Set<Integer> a = new TreeSet<>();
        Stack<Integer> stack = new Stack<>();
        stack.push(0);
        while (!stack.empty()){
            a.add(stack.peek());
            for(Integer i:map.get(stack.pop()).values()){
                if (!a.contains(i)){
                    stack.add(i);}}}
        int dop = n;
        for(int i = 1; i < n; i++){
            if(!a.contains(i)){
                map.remove(i);
                dop--;}}
        n = dop;
    }

    //
    private HashMap<Integer, Integer> nSteps(){
        boolean flag = true;
        HashMap<Integer, Integer> fact1 = new HashMap<>();
        for(Integer i:map.keySet()){fact1.put(i, fin.contains(i)?1:0);}
        HashMap<Integer, Integer> fact2 = new HashMap<>();
        int j, h;
        boolean y, y1;
        while (flag){
            j = - 1;
            for (Map.Entry<Integer, HashMap<String, Integer>> entry: map.entrySet()){
                if (!fact2.containsKey(entry.getKey())){
                    j++;
                    fact2.put(entry.getKey(), j);

                    for (Map.Entry<Integer, HashMap<String, Integer>> entry1: map.entrySet()) {
                        if (!fact2.containsKey(entry1.getKey())){
                            if (entry1.getKey() > entry.getKey()){
                                if ((fact1.get(entry.getKey()).equals(fact1.get(entry1.getKey())))){
                                    h = 0; y = true;
                                    while (y && (h != simb.length)){
                                        if (!fact1.get(entry.getValue().get(simb[h])).equals(fact1.get(entry1.getValue().get(simb[h])))){
                                            y = false;}
                                        else {h ++;}
                                    }
                                    if (y) {
                                        fact2.put(entry1.getKey(), j);
                                    }
                                }}
                        }}
                }}
            y1 = true;
            for(Integer key3:map.keySet()){
                if (!fact1.get(key3).equals(fact2.get(key3))){
                    y1 = false;
                    break;}}
            if (y1){flag = false;}
            else{fact1.clear();
                fact1 = fact2;
                fact2 = new HashMap<>();}
        }
        return fact1;
    }

    private void createAuto(HashMap<Integer, Integer> fact){
        q.clear();
        HashMap<Integer, HashMap<String, Integer>> nMap = new HashMap<>();
        int f = 1;
        for(Map.Entry<Integer, HashMap<String, Integer>> entry:map.entrySet()){
            if (!nMap.containsKey(fact.get(entry.getKey()))){
                HashMap<String, Integer>  dop = new HashMap<>();
                for(Map.Entry<String, Integer> entry1:entry.getValue().entrySet()){
                    dop.put(entry1.getKey(), fact.get(entry1.getValue()));}
                nMap.put(fact.get(entry.getKey()), dop);
                q.add(fact.get(entry.getKey()));}
        }
        map.clear();
        map = nMap;
        List<Integer> dop = new ArrayList<>();
        for(Integer i: fin){
            if (!dop.contains(fact.get(i))){
                dop.add(fact.get(i));}}
        fin = dop;
        n = nMap.size();
    }

}


