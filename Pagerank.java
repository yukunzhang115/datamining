import java.io.*;
import java.util.*;
import java.math.*;
// import java.security.KeyStore.Entry;
import java.util.Map.Entry;

// import jdk.javadoc.internal.doclets.formats.html.resources.standard;

class Pagerank {

    static final double converge = 0.000000000001;

    public static void showTop3sIncomingAirport(List<Entry<String, Double>> list, double[][] matrix, List<String> nameList, Map<String, Integer> nameMap, Map<String, Integer> rankingMap, List<Integer> countColumns) {
        for(int i = 0; i < list.size(); i++) {
            if(i < 3 || i >= list.size() - 3) {
                Entry<String, Double> entry = list.get(i);
                String key = entry.getKey();
                double value = entry.getValue();
    
                System.out.println((i + 1) + ": " + key + " => " + value);
    
                // Map<Integer, String> subTreeMap = new TreeMap<>();
    
                int index = nameMap.get(key);
                // int countIncoming = countColumns.get(index);
                // System.out.println("Total incoming airports: " + countIncoming);

                List<Entry<String, Integer>> subList = new ArrayList<>();
    
                for(int j = 0; j < matrix.length; j++) {
                    if(matrix[index][j] != 0.0) {
                        String name = nameList.get(j);
                        int ranking = rankingMap.get(name);
                        // System.out.println(name + "=>" + ranking);
                        Entry<String, Integer> subEntry = Map.entry(name, ranking);
                        subList.add(subEntry);
                    }
                }

                subList.sort(Comparator.comparing(Entry<String, Integer>::getValue));

                System.out.println("Total incoming airports: " + subList.size());
    
                for(int j = 0; j < subList.size(); j++) {
                    Entry<String, Integer> subEntry = subList.get(j);
                    System.out.println(subEntry.getKey() + "-" + (subEntry.getValue() + 1));
                }
    
                System.out.println();
            }
        }
    }

    public static Map<String, Integer> adjustNameMap(List<String> nameList) {
        Map<String, Integer> nameMap = new HashMap<>();

        for(int i = 0; i < nameList.size(); i++) {
            nameMap.put(nameList.get(i), i);
        }

        return nameMap;
    }

    public static void show(List<Entry<String, Double>> list) {
        for(int i = 0; i < list.size(); i++) {
            System.out.println((i + 1) + ": " + list.get(i).getKey() + "-" + list.get(i).getValue());
        }
    }

    public static Map<String, Integer> computeRankingMap(List<Entry<String, Double>> list) {
        Map<String, Integer> rankingMap = new HashMap<>();

        for(int i = 0; i < list.size(); i++) {
            rankingMap.put(list.get(i).getKey(), i);
        }

        return rankingMap;
    }

    public static List<Entry<String, Double>> rank(double[] vector, List<String> nameList) {
        List<Entry<String, Double>> list = new ArrayList<>();

        for(int i = 0; i < vector.length; i++) {
            Entry<String, Double> entry = Map.entry(nameList.get(i), vector[i]);
            list.add(entry);
        }

        list.sort(Comparator.comparing(Entry<String, Double>::getValue).reversed());

        return list;
    }

    public static double[] pagerank(double[][] matrix) {
        double[] vector = new double[matrix.length];
        Arrays.fill(vector, 1.0 / matrix.length);
        double[] newVector;

        int times = 0;

        while(true) {
            times++;
            
            newVector = new double[matrix.length];

            int changeCount = 0;

            for(int i = 0; i < matrix.length; i++) {
                for(int j = 0; j < matrix.length; j++) {
                    newVector[i] += matrix[i][j] * vector[j];
                }

                if(Math.abs(newVector[i] - vector[i]) <= converge) changeCount++;
            }

            System.out.println(times + ": converge count: " + changeCount);

            if(changeCount >= vector.length) return newVector;

            System.arraycopy(newVector, 0, vector, 0, newVector.length);
        }


    }

    public static double[][] convertMatrix(List<List<Double>> columns, List<Integer> countColumns) {
        double[][] matrix = new double[columns.size()][columns.size()];

        for(int i = 0; i < columns.size(); i++) {
            
            double num = 1.0 / countColumns.get(i);

            for(int j = 0; j < columns.size(); j++) {
                if(columns.get(i).get(j) == 1234567890.0) matrix[j][i] = num;
            }
        }

        return matrix;

    }

    public static void removeNode(List<List<Double>> columns, List<Integer> countColumns, List<String> nameList) {
        int count = 0;

        while (true) {
            count++;

            // find a column with count of 1 or 0
            int columnNumber = -1;
            for(int i = 0; i < countColumns.size(); i++) {
                if(countColumns.get(i) <= 1) {
                    columnNumber = i;
                    break;
                }
            }
            if(columnNumber == -1) break;

            // delete that column
            columns.remove(columnNumber);
            countColumns.remove(columnNumber);
            nameList.remove(columnNumber);

            // modify count of other columns AND delete that row
            for(int i = 0; i < columns.size(); i++) {
                if(columns.get(i).get(columnNumber) == 1234567890.0) countColumns.set(i, countColumns.get(i) - 1);

                columns.get(i).remove(columnNumber);
            }
        }
    }

    public static void readText(List<List<Double>> columns, List<Integer> countColumns, List<String> nameList, Map<String, Integer> nameMap) {
        BufferedReader reader;

        // Map<String, Integer> nameMap = new HashMap<>();

		try {
			reader = new BufferedReader(new FileReader("routes.txt"));
			String line = reader.readLine();

            int count = 0;
            

			while (line != null) {
                count++;

                String[] strArray = line.split(",");
                String departure = strArray[2];
                String destination = strArray[4];

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

                if(columns.get(indexOfDeparture).get(indexOfDestination) == 0.0) {
                    columns.get(indexOfDeparture).set(indexOfDestination, 1234567890.0);
                    countColumns.set(indexOfDeparture, countColumns.get(indexOfDeparture) + 1);
                }

				// read next line
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

    }

    public static void main(String args[]) {
        List<List<Double>> columns = new ArrayList<>();
        List<Integer> countColumns = new ArrayList<>();
        List<String> nameList = new ArrayList<>();
        Map<String, Integer> nameMap = new HashMap<>();
 
        System.out.println("start readText");
        readText(columns, countColumns, nameList, nameMap);

        System.out.println("start removeNode");
        removeNode(columns, countColumns, nameList); 

        System.out.println("start convertMatrix");
        double[][] matrix = convertMatrix(columns, countColumns);

        System.out.println("start pagerank");
        double[] vector = pagerank(matrix);

        System.out.println("start rank");
        List<Entry<String, Double>> list = rank(vector, nameList);
        
        //-------------------1.show ranking
        // show(list);

        //-------------------2. show top 3
        /*

        System.out.println("start computeRankingMap");
        Map<String, Integer> rankingMap = computeRankingMap(list);

        System.out.println("start adjustNameMap");
        Map<String, Integer> nameMap2 = adjustNameMap(nameList);

        // System.out.println("start showTop3sIncomingAirport");
        showTop3sIncomingAirport(list, matrix, nameList, nameMap2, rankingMap, countColumns);

        */

    }
}