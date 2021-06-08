import java.io.*;
import java.util.*;
import java.math.*;
import java.security.KeyStore.Entry;

// import jdk.javadoc.internal.doclets.formats.html.resources.standard;

class Pagerank {

    static final double converge = 0.000000000001;

    public static void showTop3sIncomingAirport(Map<Double, String> treeMap, double[][] matrix, List<String> nameList, Map<String, Integer> nameMap, Map<String, Integer> rankingMap, List<Integer> countColumns) {
        int count = 0;

        for(Map.Entry<Double,String> entry : treeMap.entrySet()) {
            count++;

            double key = entry.getKey();
            String value = entry.getValue();

            System.out.println(count + ": " + key + " => " + value);

            Map<Integer, String> subTreeMap = new TreeMap<>();

            int index = nameMap.get(value);
            int countIncoming = countColumns.get(index);
            System.out.println("Total incoming airports: " + countIncoming);

            for(int i = 0; i < matrix.length; i++) {
                if(matrix[index][i] != 0.0) {
                    String name = nameList.get(i);
                    int ranking = rankingMap.get(name);
                    // System.out.println(name + "=>" + ranking);
                    subTreeMap.put(ranking, name);
                }
            }

            for(Map.Entry<Integer,String> subEntry : subTreeMap.entrySet()) {
                int ranking = subEntry.getKey();
                String name = subEntry.getValue();
                System.out.println(name + "=>" + ranking);
                
            }

            System.out.println();
            
            if(count >= 3) break;
        }
    }

    public static void adjustNameMap(List<String> nameList, Map<String, Integer> nameMap) {
        nameMap = new HashMap<>();

        for(int i = 0; i < nameList.size(); i++) {
            nameMap.put(nameList.get(i), i);
        }
    }

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

    public static Map<String, Integer> convertTreeMapToRankingMap(Map<Double, String> treeMap) {
        int count = 0;
        Map<String, Integer> rankingMap = new HashMap<>();

        for(Map.Entry<Double,String> entry : treeMap.entrySet()) {
            // double key = entry.getKey();
            String value = entry.getValue();
          
            count++;

            // System.out.println(count + ": " + key + " => " + value);
            
            // if(count >= 10) break;

            rankingMap.put(value, count);
        }

        return rankingMap;
    }

    public static Map<Double, String> rank(double[] vector, List<String> nameList) {
        Map<Double, String> treeMap = new TreeMap<>(Collections.reverseOrder());

        for(int i = 0; i < vector.length; i++) {
            treeMap.put(vector[i], nameList.get(i));
        }

        return treeMap;
    }

    public static double[] pagerank(double[][] matrix) {
        double[] vector = new double[matrix.length];
        Arrays.fill(vector, 1.0 / matrix.length);

        // System.out.println("vector.length: " + vector.length);
        // for(int i = 0; i < vector.length; i++) {
        //     System.out.print(vector[i] + " ");
        // }
        // System.out.println();

        double[] newVector;

        int times = 0;

        while(true) {
            times++;

            System.out.println("----------" + times + "-----------");

            newVector = new double[matrix.length];

            // for(int i = 0; i < newVector.length; i++) {
            //     System.out.print(newVector[i] + " ");
            // }
            // System.out.println();
            // System.out.println();

            int changeCount = 0;

            for(int i = 0; i < matrix.length; i++) {
                for(int j = 0; j < matrix.length; j++) {
                    newVector[i] += matrix[i][j] * vector[j];

                    // System.out.print(newVector[i] + " ");
                }

                if(Math.abs(newVector[i] - vector[i]) <= converge) changeCount++;

                // System.out.println();
                // System.out.println();
            }

            System.out.println(changeCount);

            // for(int i = 0; i < newVector.length; i++) {
            //     System.out.print(newVector[i] + " ");
            // }
            // System.out.println();

            if(changeCount >= vector.length) return newVector;

            // vector = newVector.clone();
            System.arraycopy(newVector, 0, vector, 0, newVector.length);

            // System.out.println();
            // System.out.println();

        }


    }

    public static double[] pagerank(List<List<Double>> columns) {
        double[] vector = new double[columns.size()];
        Arrays.fill(vector, 1.0 / columns.size());

        System.out.println("vector.length: " + vector.length);

        double[] newVector;

        int times = 0;

        while(true) {
            times++;

            // System.out.println("---" + times + "---");

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
            // System.out.println(changeCount);
            // System.out.println();

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
        Map<Double, String> treeMap = rank(vector, nameList);

        System.out.println("start convertTreeMapToRankingMap");
        Map<String, Integer> rankingMap = convertTreeMapToRankingMap(treeMap);

        // show(treeMap);

        System.out.println("start adjustNameMap");
        adjustNameMap(nameList, nameMap);

        System.out.println("start showTop3sIncomingAirport");
        showTop3sIncomingAirport(treeMap, matrix, nameList, nameMap, rankingMap, countColumns);

        // countIncomingOfTop3Airport()

    }
}