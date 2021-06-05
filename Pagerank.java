import java.io.*;
import java.util.*;
import java.math.*;

// import jdk.javadoc.internal.doclets.formats.html.resources.standard;

class Pagerank {

    static final double converge = 0.000000000001;

    public static void removeNode(List<List<Double>> columns, List<Integer> countColumns) {
        while (true) {
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

            // delete that column
            columns.remove(columnNumber);
            countColumns.remove(columnNumber);

            // colsNumber--;

            // System.out.println("---matrix after deleting wrong column---");
            // for (int i = 0; i < rowsNumber; i++) {
            //     for (int j = 0; j < colsNumber; j++) {

            //         System.out.print(rows.get(i).get(j) + "\t");
            //     }
            //     System.out.println();
            // }

            // modify count of other columns AND delete that row
            for(int i = 0; i < columns.size(); i++) {
                if(columns.get(i).get(columnNumber) == 1.0) countColumns.set(i, countColumns.get(i) - 1);

                columns.get(i).remove(columnNumber);
            }

            //show new matrix
            // System.out.println("---new matrix---");
            // for (int i = 0; i < rowsNumber; i++) {
            //     for (int j = 0; j < colsNumber; j++) {

            //         System.out.print(rows.get(i).get(j) + "\t");
            //     }
            //     System.out.println();
            // }


        }
    }

    public static String doubleToRationalString(double num1) {
        double num2 = 1;
        
        // let this number without decimal
        while (num1 % 1.0 != 0.0) {
            num1 *= 10;
            num2 *= 10;
        }

        // System.out.println("---after multiple--- num1: " + num1 + " num2: " + num2);

        // find greatest common dividor(gcd)
        int gcd = 1;
        for (int i = 1; i <= num1 && i <= num2; i++) {
            if (num1 % i == 0 && num2 % i == 0)
                gcd = i;
        }

        // let two numbers divide by gcd
        num1 /= gcd;
        num2 /= gcd;

        // System.out.println("---after divide by gcd--- num1: " + num1 + " num2: " + num2);

        // get final string of rational number
        int finalNum1 = (int) num1;
        int finalNum2 = (int) num2;
        String newString = finalNum2 == 1 ? String.valueOf(finalNum1)
                : String.valueOf(finalNum1) + "/" + String.valueOf(finalNum2);
        
        return newString;
    }

    public static void iteration(List<List<String>> rows) {
        if(rows.size() == 1) {
            return;
        }

        
        int times = 0;

        String str = "1/" + rows.size();
        String[] results = new String[rows.size()];
        Arrays.fill(results, str);

        double[] realResults = new double[rows.size()];
        double size = rows.size();
        double real = 1 / size;
        Arrays.fill(realResults, real);

        //show iteration
        // System.out.println("------iteration 0------");
        // for(int i = 0; i < results.length; i++) {
        //     System.out.println(results[i]);
        // }

        // String[] secondResults = new String[rows.size()];

        while(true) {
            if(times >= 5){
                realIteration(rows, realResults, times);
                return;
            }

            times++;

            String[] secondResults = new String[rows.size()];
            double[] secondRealResults = new double[rows.size()];

            // int[] changes = new int[results.length];

            for(int i = 0; i < results.length; i++) {

                // System.out.println("i = " + i);

                //initial sum = 0;
                BigInteger num1Up = new BigInteger("0");
                BigInteger num1Down = new BigInteger("0");

                // System.out.println("num1: " + num1Up + "/" + num1Down);

                for(int j = 0; j < rows.get(i).size(); j++) {

                    // System.out.println("j = " + j);

                    if(rows.get(i).get(j).equals("0")) continue;

                    //current node
                    String[] strArray = rows.get(i).get(j).split("/");
                    BigInteger num2Up = new BigInteger(strArray[0]);
                    BigInteger num2Down = new BigInteger(strArray[1]);

                    // System.out.println("node: " + num2Up + "/" + num2Down);

                    //column
                    String[] strArray2 = results[j].split("/");
                    BigInteger resultUp = new BigInteger(strArray2[0]);
                    BigInteger resultDown = new BigInteger(strArray2[1]);

                    // System.out.println("column: " + resultUp + "/" + resultDown);

                    //multiply node and column
                    resultUp = resultUp.multiply(num2Up);
                    resultDown = resultDown.multiply(num2Down);

                    // System.out.println("after mul: " + resultUp + "/" + resultDown);

                    //if previous sum = 0, then make this result as sum
                    if(num1Up.equals(BigInteger.ZERO)) {
                        // System.out.println("num1Up.equals(0)");
                        num1Up = resultUp;
                        num1Down = resultDown;

                    // if previous sum != 0, then add this result to sum
                    }else {

                        BigInteger tempUp = num1Up.multiply(resultDown).add(num1Down.multiply(resultUp));
                        BigInteger tempDown = num1Down.multiply(resultDown);

                        num1Up = tempUp;
                        num1Down = tempDown;
                    }

                    // System.out.println("after cal node: " + num1Up + "/" + num1Down);

                }

                //find gcd
                BigInteger gcd = num1Up.gcd(num1Down);
                // for (int k = 1; k <= num1Up && k <= num1Down; k++) {
                //     if (num1Up % k == 0 && num1Down % k == 0)
                //         gcd = k;
                // }

                //let two numbers divide by gcd
                num1Up = num1Up.divide(gcd);
                num1Down = num1Down.divide(gcd);

                // System.out.println("gcd = " + gcd);

                //change sum to string
                secondResults[i] = num1Up.toString() + "/" + num1Down.toString();

                // System.out.println("secondResults【" + i + "] = " + secondResults[i]);

                //change sum to double
                double num1UpReal = num1Up.doubleValue();
                double num1DownReal = num1Down.doubleValue();
                secondRealResults[i] = num1UpReal / num1DownReal;

            }

            int changeSum = 0;
            for(int i = 0; i < rows.size(); i++) {
                // System.out.println("previous real number: " + realResults[i]);
                // System.out.println("real number: " + secondRealResults[i]);
                // System.out.println("converge " + i + ": " + Math.abs(realResults[i] - secondRealResults[i]));

                if(Math.abs(realResults[i] - secondRealResults[i]) < converge) {
                    changeSum++;} 
            }
            if(changeSum == rows.size()) return;
            // if(times > 5) break;

            //clone the new results as the initial results
            results = secondResults.clone();
            realResults = secondRealResults.clone();

            //show iteration
            System.out.println("------iteration " + times + "------");
            for(int i = 0; i < results.length; i++) {
                System.out.println(results[i]);
            }



        }

    }

    public static void realIteration(List<List<String>> rows, double[] results, int times) {
        while(true) {
            times++;

            double[] secondResults = new double[rows.size()];

            for(int i = 0; i < rows.size(); i++) {

                // double sum = 0;

                for(int j = 0; j < rows.size(); j++) {
                    if(rows.get(i).get(j).equals("0")) continue;

                    double node = 0;
                    if(rows.get(i).get(j).contains("/")) {

                        //current node
                        String[] strArray = rows.get(i).get(j).split("/");
                        double nodeUp = Double.parseDouble(strArray[0]);
                        double nodeDown = Double.parseDouble(strArray[1]);
                        node = nodeUp / nodeDown;
                    }else node = Double.parseDouble(rows.get(i).get(j));

                    secondResults[i] += node * results[j];

                }


            }

            int changeSum = 0;
            for(int i = 0; i < rows.size(); i++) {
                // System.out.println("previous real number: " + realResults[i]);
                // System.out.println("real number: " + secondRealResults[i]);
                // System.out.println("converge " + i + ": " + Math.abs(realResults[i] - secondRealResults[i]));

                if(Math.abs(results[i] - secondResults[i]) < converge) changeSum++;
            }
            if(changeSum == rows.size()) return;

            //clone the new results as the initial results
            results = secondResults.clone();

            //show iteration
            System.out.println("------iteration " + times + "------");
            for(int i = 0; i < results.length; i++) {
                System.out.println(results[i]);
            }
        }
    }

    public static void readText(List<List<Double>> columns, List<Integer> countColumns, Map<String, Integer> nameMap) {
        BufferedReader reader;

        // List<Integer> countColumn = new LinkedList<>();

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

                System.out.println(count + "-" + departure + "-" + destination);

                if(!nameMap.containsKey(departure)){
                    List<Double> newColumn = new LinkedList<>();

                    int i = 0;

                    for(; i < columns.size(); i++) {
                        newColumn.add(0.0);
                        columns.get(i).add(0.0);
                    }

                    newColumn.add(0.0);
                    columns.add(newColumn);

                    nameMap.put(departure, i);

                    countColumns.add(0);
                }

                if(!nameMap.containsKey(destination)){
                    List<Double> newColumn = new LinkedList<>();

                    int i = 0;

                    for(; i < columns.size(); i++) {
                        newColumn.add(0.0);
                        columns.get(i).add(0.0);
                    }

                    newColumn.add(0.0);
                    columns.add(newColumn);

                    nameMap.put(destination, i);

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
        List<List<Double>> columns = new LinkedList<>();
        List<Integer> countColumns = new LinkedList<>();
        Map<String, Integer> nameMap = new HashMap<>();
 
        readText(columns, countColumns, nameMap);

        System.out.println("columns size: " + columns.size());
        System.out.println("countColumns: " + countColumns);

        // for(int i = 0; i < columns.size(); i++) {
        //     for(int j = 0; j < columns.size(); j++) {
        //         System.out.print(columns.get(j).get(i) + " ");
        //     }
        //     System.out.println();
        // }

        removeNode(columns, countColumns); 

        // iteration(rows);
    }
}