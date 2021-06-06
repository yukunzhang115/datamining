import java.io.*;
import java.util.*;
import java.math.*;
import java.security.KeyStore.Entry;

// import jdk.javadoc.internal.doclets.formats.html.resources.standard;

class Pagerank {

    static final double converge = 0.000000000001;

    public static void show(Map<Double, String> treeMap) {
        int count = 0;

        for(Map.Entry<Double,String> entry : treeMap.entrySet()) {
            double key = entry.getKey();
            String value = entry.getValue();
          
            count++;

            System.out.println(count + ": " + key + " => " + value);
            
            // if(count >= 10) break;
          }
    }

    public static Map<Double, String> rank(double[] vector, List<String> nameList) {
        Map<Double, String> treeMap = new TreeMap<>(Collections.reverseOrder());

        for(int i = 0; i < vector.length; i++) {
            treeMap.put(vector[i], nameList.remove(0));
        }

        return treeMap;
    }

    public static double[] pagerank(List<List<Double>> columns) {
        double[] vector = new double[columns.size()];
        Arrays.fill(vector, 1.0 / columns.size());

        double[] newVector;

        int times = 0;

        while(true) {
            times++;

            System.out.println("---" + times + "---");

            newVector = new double[columns.size()];

            int changeCount = 0;

            for(int j = 0; j < columns.size(); j++) {
                for(int i = 0; i < columns.size(); i++) {
                    newVector[j] += columns.get(i).get(j) * vector[i];
                }

                if(Math.abs(newVector[j] - vector[j]) <= converge) changeCount++;

                // System.out.print(j + " ");
            }
            if(changeCount >= vector.length) return newVector;

            // vector = newVector.clone();
            System.arraycopy(newVector, 0, vector, 0, newVector.length);

            // System.out.println();
            System.out.println(changeCount);
            // System.out.println();

        }


    }

    public static void convertMatrix(List<List<Double>> columns, List<Integer> countColumns) {
        for(int i = 0; i < columns.size(); i++) {
            // System.out.println(i);
            double num = 1.0 / countColumns.get(i);
            for(int j = 0; j < columns.size(); j++) {
                if(columns.get(i).get(j) == 1.0) columns.get(i).set(j, num);
            }
        }
    }

    public static void removeNode(List<List<Double>> columns, List<Integer> countColumns, List<String> nameList) {
        int count = 0;

        while (true) {
            count++;

            boolean isEnd = false;

            // find a column with count of 1 or 0
            int columnNumber = -1;
            for(int i = 0; i < countColumns.size(); i++) {
                if(countColumns.get(i) <= 1) {
                    columnNumber = i;
                    break;
                }
                
                if(i == countColumns.size() - 1) isEnd = true;
            }
            if(isEnd) break;

            // System.out.println(count + "-columnNumber: " + columnNumber);

            // delete that column
            columns.remove(columnNumber);
            countColumns.remove(columnNumber);
            nameList.remove(columnNumber);

            // modify count of other columns AND delete that row
            for(int i = 0; i < columns.size(); i++) {
                if(columns.get(i).get(columnNumber) == 1.0) countColumns.set(i, countColumns.get(i) - 1);

                columns.get(i).remove(columnNumber);
            }
        }
    }

    public static void readText(List<List<Double>> columns, List<Integer> countColumns, List<String> nameList) {
        BufferedReader reader;

        Map<String, Integer> nameMap = new HashMap<>();

		try {
			reader = new BufferedReader(new FileReader("routes.txt"));
			String line = reader.readLine();

            int count = 0;
            

			while (line != null) {
                count++;
				// System.out.println(count + "-" + line);
                

                String[] strArray = line.split(",");
                String departure = strArray[2];
                String destination = strArray[4];

                // System.out.println(count + "-" + departure + "-" + destination);

                if(!nameMap.containsKey(departure)){
                    List<Double> newColumn = new ArrayList<>();

                    int i = 0;

                    for(; i < columns.size(); i++) {
                        newColumn.add(0.0);
                        columns.get(i).add(0.0);
                    }

                    newColumn.add(0.0);
                    columns.add(newColumn);

                    nameMap.put(departure, i);
                    nameList.add(departure);

                    countColumns.add(0);
                }

                if(!nameMap.containsKey(destination)){
                    List<Double> newColumn = new ArrayList<>();

                    int i = 0;

                    for(; i < columns.size(); i++) {
                        newColumn.add(0.0);
                        columns.get(i).add(0.0);
                    }

                    newColumn.add(0.0);
                    columns.add(newColumn);

                    nameMap.put(destination, i);
                    nameList.add(destination);

                    countColumns.add(0);
                }

                int indexOfDeparture = nameMap.get(departure);
                int indexOfDestination = nameMap.get(destination);
                columns.get(indexOfDeparture).set(indexOfDestination, 1.0);
                countColumns.set(indexOfDeparture, countColumns.get(indexOfDeparture) + 1);

                // for(int i = 0; i < columns.size(); i++) {
                //     for(int j = 0; j < columns.size(); j++) {
                //         System.out.print(columns.get(j).get(i) + " ");
                //     }
                //     System.out.println(countColumn);
                //     System.out.println();
                // }

				// read next line
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

        // for(int i = 0; i < columns.size(); i++) {
        //     System.out.println(i);
        //     double num = 1.0 / countColumns.get(i);
        //     for(int j = 0; j < columns.size(); j++) {
        //         if(columns.get(i).get(j) == 1.0) columns.get(i).set(j, num);
        //     }
        // }

    }

    public static void main(String args[]) {
        List<List<Double>> columns = new ArrayList<>();
        List<Integer> countColumns = new LinkedList<>();
        List<String> nameList = new LinkedList<>();
 
        System.out.println("start readText");

        readText(columns, countColumns, nameList);

        // System.out.println("columns size: " + columns.size());
        // System.out.println("countColumns: " + countColumns);

        System.out.println("start removeNode");

        removeNode(columns, countColumns, nameList); 

        // System.out.println("columns size: " + columns.size());
        // System.out.println("countColumns: " + countColumns);

        System.out.println("start convertMatrix");

        convertMatrix(columns, countColumns);

        // System.out.println("columns size: " + columns.size());
        // System.out.println("nameList size: " + nameList.size());

        System.out.println("start pagerank");

        double[] vector = pagerank(columns);

        System.out.println("start rank");

        Map<Double, String> treeMap = rank(vector, nameList);

        show(treeMap);

        // for(int i = 0; i < columns.size(); i++) {
        //     for(int j = 0; j < columns.size(); j++) {
        //         System.out.print(columns.get(j).get(i) + " ");
        //     }
        //     System.out.println();
        // }

    }
}