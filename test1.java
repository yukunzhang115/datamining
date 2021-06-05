import java.io.*;
import java.util.*;
import java.math.*;

import jdk.javadoc.internal.doclets.formats.html.resources.standard;

class HelloWorld {

    static final double converge = 0.000000000001;

    public static void removeNode(List<List<String>> rows, boolean isRational) {
        int rowsNumber = rows.size();
        int colsNumber = rows.size();
        // int iterationTime = 0;

        while (true) {
            // iterationTime++;

            //after 5 iterations, we change all the data with "/" from rational to real number
            // if(isRational && iterationTime == 6) {
            //     for(int i = 0; i < rowsNumber; i++) {
            //         for(int j = 0; j < colsNumber; j++) {
            //             if(rows.get(i).get(j).contains("/")) {
            //                 String[] numbers = rows.get(i).get(j).split("/");
            //                 double num1 = Double.parseDouble(numbers[0]);
            //                 double num2 = Double.parseDouble(numbers[1]);
            //                 double num = num1 / num2;
            //                 String finalStr = String.valueOf(num);
            //                 rows.get(i).set(j, finalStr);
            //             }
            //         }
            //     }
            // }

            int node = -1;
            List<Integer> rowNumberList = new ArrayList<>();
            boolean isEnd = false;

            // find a node with zero or one non-zero number
            for (int j = 0; j < colsNumber; j++) {
                rowNumberList.clear();
                for (int i = 0; i < rowsNumber; i++) {
                    if (!rows.get(i).get(j).equals("0"))
                        rowNumberList.add(i);
                }
                if (rowNumberList.size() <= 1) {
                    node = j;
                    break;
                }
                if (j == colsNumber - 1)
                    isEnd = true;
            }

            // if there is no wrong node, then end while
            if (isEnd)
                break;

            // delete the wrong column
            for (int i = 0; i < rowsNumber; i++) {
                rows.get(i).remove(node);
            }
            colsNumber--;

            // System.out.println("---matrix after deleting wrong column---");
            // for (int i = 0; i < rowsNumber; i++) {
            //     for (int j = 0; j < colsNumber; j++) {

            //         System.out.print(rows.get(i).get(j) + "\t");
            //     }
            //     System.out.println();
            // }

            // change the data from this wrong row to other rows
            for (int j = 0; j < colsNumber; j++) {

                // detect one non-zero data in this row we need to change
                if (!rows.get(node).get(j).equals("0")) {

                    // System.out.println("j: " + j);

                    // calculate sum of column's non-zero number except the row we detect
                    double sum = 0;
                    for (int i = 0; i < rowsNumber; i++) {
                        if (i == node) {
                            rows.get(i).set(j, "0");
                            continue;
                        }
                        if (!rows.get(i).get(j).equals("0"))
                            sum++;
                    }

                    // System.out.println("sum: " + sum);

                    //if the data is real number, the new data should be real number
                    if(!isRational) {
                        double num = 1 / sum;
                        String newStr = String.valueOf(num);
                        for (int i = 0; i < rowsNumber; i++) {
                            if (!rows.get(i).get(j).equals("0"))
                                rows.get(i).set(j, newStr);
                        }

                        continue;
                    }

                    String str = sum == 1.0 ? "1" : "1/" + String.valueOf((int)sum);

                    // set new data to this column
                    for (int i = 0; i < rowsNumber; i++) {
                        if (!rows.get(i).get(j).equals("0"))
                            rows.get(i).set(j, str);
                    }
                }
            }

            // delete the wrong row
            rows.remove(node);
            rowsNumber--;

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

    // public static void rational() {
    // int sum = 2;
    // double a = 1 / sum;
    // double b = 1.0 / sum;
    // if(a % 1.0 == 0.0) System.out.println("zheng shu");
    // else System.out.println("xiao shu");
    // }

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

    public static void main(String args[]) {
        try {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                String line = in.readLine( );
                System.out.println(line);
        } catch ( IOException e) {
                System.out.println(e.getMessage( ));
                System.exit(1);
        }
        System.exit(0);


        // Scanner scanner = new Scanner(System.in);

        // String inputString = scanner.nextLine();
        // String[] inputStringList = inputString.split("\\s+");

        // String[] restRows = new String[inputStringList.length - 1];

        // List<List<String>> rows = new LinkedList<>();

        // int rowsNumber = inputStringList.length;

        // // setup first row
        // List<String> firstRow = new LinkedList<>();
        // for (int i = 0; i < rowsNumber; i++) {
        //     firstRow.add(inputStringList[i]);
        // }
        // // firstRow = Arrays.asList(inputStringList);
        // rows.add(firstRow);

        // // setup rest row
        // for (int i = 0; i < restRows.length; i++) {
        //     inputString = scanner.nextLine();
        //     inputStringList = inputString.split("\\s+");
        //     List<String> row = new LinkedList<>();
        //     for (int j = 0; j < rowsNumber; j++) {
        //         row.add(inputStringList[j]);
        //     }
        //     // row = Arrays.asList(inputStringList);
        //     rows.add(row);
        // }

        // scanner.close();

        // boolean isRational = false;
        // for(int i = 0; i < rowsNumber; i++) {
        //     for(int j = 0; j < rowsNumber; j++) {
        //         if(rows.get(i).get(j).contains("/")) {
        //             isRational = true;
        //             break;
        //         }
        //     }
        //     if(isRational) break;
        // }

        // removeNode(rows, isRational); 

        // iteration(rows);
    }
}

// 1 2 3
/*
 
1/3    0   0   0   1/3   0
0   1   1/2   0   0   0
0   0   0   0   1/3   0
0   0   1/2   1   0   0
1/3   0   0   0   0   1/2
1/3   0   0   0   1/3   1/2


1/3 1/2 0
1/3 0 1/2 1
1/3 1/2 x

 */