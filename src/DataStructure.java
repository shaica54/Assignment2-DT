import java.lang.reflect.Array;
import java.util.Arrays;

public class DataStructure implements DT {
    //////////////// DON'T DELETE THIS CONSTRUCTOR ////////////////
    //PRIVATE FIELDS
    private Container minX; //minX is also the *first* link of the first linkedlist
    private Container minY; //minY is also the *first* link of the second linkedlist
    private Container maxX; //*last* link of X-list
    private Container maxY; //*last* link of Y-list
    private int sizeOfList;
    ;

    //We implement two bi-directional linked lists
    //the Container plays as the 'Link'
    //the first linkedlist contains all points, sorted in increasing order of points' X value
    //the second linkedlist contains all points, sorted in increasing order of points' Y value
    public DataStructure() {
        this.minX = null;
        this.minY = null;
        this.maxX = null;
        this.maxY = null;
        this.sizeOfList = 0;
    }

    @Override
    public void addPoint(Point point) {

        if (point == null)
            throw new NullPointerException();

        Container newContainer = new Container(point);

        // if the new link is the first one in the list
        if (minX == null) {
            minX = newContainer;
            maxX = newContainer;
            minY = newContainer;
            maxY = newContainer;
        } else {
            // first we put the x values
            addToXList(newContainer);
            // then we put the y values
            addToYList(newContainer);
        }
        sizeOfList = sizeOfList + 1;
    }

    // add the x values
    // NOTE: if we decide to do O(n) we need to change the order of this!!!
    public void addToXList(Container newContainer) {

        //if the new container is the smallest one
        if (minX.getData().getX() >= newContainer.getData().getX()) {
            minX.setPrevX(newContainer);
            newContainer.setNextX(minX);
            minX = newContainer;
        }
        // if the new container is the larger one
        else if (maxX.getData().getX() < newContainer.getData().getX()) {
            maxX.setNextX(newContainer);
            newContainer.setPrevX(maxX);
            maxX = newContainer;
        } //5-8-12

        else {
            Container curr = minX;

            while (curr.getNextX().getData().getX() < newContainer.getData().getX()) {
                curr = curr.getNextX();
            }

            //for example: 8->12 and I want to add 9
            newContainer.setPrevX(curr); //   8<-9
            curr.getNextX().setPrevX(newContainer); //   9<-12
            newContainer.setNextX(curr.getNextX()); // 9->12
            curr.setNextX(newContainer); // 8->9


        }

    }

    // add the Y values
    // NOTE: if we decide to do O(n) we need to change the order of this!!!
    public void addToYList(Container newContainer) {

        if (minY.getData().getY() >= newContainer.getData().getY()) {
            minY.setPrevY(newContainer);
            newContainer.setNextY(minY);
            minY = newContainer;
        }
        // if the new container is the larger one
        else if (maxY.getData().getY() < newContainer.getData().getY()) {
            maxY.setNextY(newContainer);
            newContainer.setPrevY(maxY);
            maxY = newContainer;
        } else {
            Container curr = minY;

            while (curr.getNextY().getData().getY() < newContainer.getData().getY()) {
                curr = curr.getNextY();
            }

            newContainer.setPrevY(curr); // change new to point on curr
            curr.getNextY().setPrevY(newContainer); // change nextContainer to point on new
            newContainer.setNextY(curr.getNextY()); // change new to point on nextContainer
            curr.setNextY(newContainer); // change curr to point on new

        }
    }


    @Override
    public Point[] getPointsInRangeRegAxis(int min, int max, Boolean axis) {

        int size = 0;
        Point[] array = null;
        if (axis) {
            Container curr = minX;

            while (curr != null && curr.getData().getX() < min)
                curr = curr.getNextX();

            while (curr != null && curr.getData().getX() >= min && curr.getData().getX() <= max) {

                size = size + 1;

                if (curr.getNextX() == null || curr.getNextX().getData().getX() > max)
                    break;

                curr = curr.getNextX();
            }

            array = new Point[size];

            if (array.length > 0) {
                while (size > 0) {
                    size = size - 1;
                    array[size] = curr.getData();
                    curr = curr.getPrevX();
                }
            } else
                return null;
        } else {
            Container curr = minY;

            while (curr != null && curr.getData().getY() < min)
                curr = curr.getNextY();

            while (curr != null && curr.getData().getY() >= min && curr.getData().getY() <= max) {

                size = size + 1;

                if (curr.getNextY() == null || curr.getNextY().getData().getY() > max)
                    break;


                curr = curr.getNextY();
            }

            array = new Point[size];

            if (array.length > 0) {
                while (size > 0) {
                    size = size - 1;
                    array[size] = curr.getData();
                    curr = curr.getPrevY();
                }
            } else
                return null;
        }

        return array;
    }

    @Override
    public Point[] getPointsInRangeOppAxis(int min, int max, Boolean axis) {

        int numOfContainers = getPointsOppRangeHelp(min, max, axis);
        Point[] array = new Point[numOfContainers];
        int i = 0;

        // go over the !axis list and build an array only of the marked containers
        if (array.length > 0) {
            if (axis) {
                Container curr = minY;
                while (curr != null && i < numOfContainers) {
                    if (curr.inRange()) {
                        array[i] = curr.getData();
                        curr.changeInRange(false); // remove the mark for further use
                        i = i + 1;
                    }
                    curr = curr.getNextY();

                }
            } else {
                Container curr = minX;
                while (curr != null && i < numOfContainers) {
                    if (curr.inRange()) {
                        array[i] = curr.getData();
                        curr.changeInRange(false); // remove the mark for further use
                        i = i + 1;
                    }
                    curr = curr.getNextX();
                }
            }
        } else
            return null;

        return array;

    }

    // same code as 'getPointsInRangeRegAxis' only now, instead of insert the points to an array -- marks the containers in the range
    public int getPointsOppRangeHelp(int min, int max, Boolean axis) {

        int size = 0;

        if (axis) {
            Container curr = minX;

            while (curr != null && curr.getData().getX() < min)
                curr = curr.getNextX();

            while (curr != null && curr.getData().getX() >= min && curr.getData().getX() <= max) {

                curr.changeInRange(true);
                size = size + 1;

                if (curr.getNextX() == null || curr.getNextX().getData().getX() > max)
                    break;


                curr = curr.getNextX();
            }

        } else {
            Container curr = minY;

            while (curr != null && curr.getData().getY() < min)
                curr = curr.getNextY();

            while (curr != null && curr.getData().getY() >= min && curr.getData().getY() <= max) {

                curr.changeInRange(true);
                size = size + 1;

                if (curr.getNextY() == null || curr.getNextY().getData().getY() > max)
                    break;


                curr = curr.getNextY();
            }
        }
        return size;
    }

    @Override
    public double getDensity() {
        // TODO Auto-generated method stub
        return (double) sizeOfList / ((this.maxX.getData().getX() - this.minX.getData().getX()) * (this.maxY.getData().getY() - this.minY.getData().getY()));
    }

    @Override
    public void narrowRange(int min, int max, Boolean axis) {
        // TODO Auto-generated method stub
        if (axis)
            deleteFromX(min, max);
        else
            deleteFromY(min, max);
    }

    public void deleteFromX(int min, int max) {
        Container currMin = minX;
        Container currMax = maxX;

        // if the range is bigger or smaller than the values of the list
        // we will delete the list itself by returning an empty list
        if(currMin == null || currMax == null || currMin.getData().getX() > max || currMax.getData().getX() < min) {
            minX = null;
            maxX = null;
            minY = null;
            maxY = null;
            return;
        }
        // goes throw the list from the first container of X and deletes in Y-list
        while(currMin != null && currMin.getData().getX() < min) {

            Container nextMin = currMin.getNextY();
            Container prevMin = currMin.getPrevY();

            if(nextMin != null) {
                // first container in X-list is the first container in Y-list
                if(currMin == minY) {
                    minY = nextMin;
                    nextMin.setPrevY(null);
                }
                // in-between
               else {
                    prevMin.setNextY(nextMin);
                    nextMin.setPrevY(prevMin);
                }
            }
            else {
                maxY = prevMin;
                prevMin.setNextY(null);
            }
            // go to the other container by X value
            sizeOfList = sizeOfList - 1;
            currMin = currMin.getNextX();
        }

        while(currMax != null && currMax.getData().getX() > max){

            Container nextMax = currMax.getNextY();
            Container prevMax = currMax.getPrevY();

            if (prevMax != null) {
                if(currMax == maxY) {
                    maxY = prevMax;
                    prevMax.setNextY(null);
                }
                else {
                    prevMax.setNextY(nextMax);
                    nextMax.setPrevY(prevMax);

                }
            }
            else {
                minY = nextMax;
                nextMax.setPrevY(null);
            }
            sizeOfList = sizeOfList - 1;
            currMax = currMax.getPrevX();
        }

        if(sizeOfList != 1) {
            minX = currMin;
            currMin.setPrevX(null);
            maxX = currMax;
            currMax.setNextX(null);
        }




   /*     Container currMin = minX;
        Container currMax = maxX;

        while (currMin != null && currMin.getData().getX() < min) {
            currMin.changeInRange(true);
            currMin = currMin.getNextX();
        }

        while (currMax != null && currMax.getData().getX() > max) {
            currMax.changeInRange(true);
            currMax = currMax.getPrevX();
        }

        // start of change Y
        Container changeMinY = minY;

        while (changeMinY.inRange())
            changeMinY = changeMinY.getNextY();

        minY = changeMinY;

        Container changeMaxY = maxY;

        while (changeMaxY.inRange())
            changeMaxY = changeMinY.getNextY();

        maxY = changeMaxY;
        // end of change Y

        if (maxX.getData().getX() < min | minX.getData().getX() > max) {
            minX = null;
            minY = null;
            maxX = null;
            maxY = null;
            return;
        } else if (currMin != minX) {
            minX = currMin;
            currMin.setPrevX(null);
            currMin.setPrevY(null);
        } else if (currMax != maxX) {
            maxX = currMax;
            currMax.setNextX(null);
            currMax.setNextY(null);
        }
*/
    }

    public void deleteFromY(int min, int max) {

    }

    @Override
    public Boolean getLargestAxis() {
        // TODO Auto-generated method stub
        return (this.maxX.getData().getX() - this.minX.getData().getX()) > (this.maxY.getData().getY() - this.minY.getData().getY());
    }

    @Override
    public Container getMedian(Boolean axis) {
        // TODO Auto-generated method stub
        Container curr;
        if (axis) { //go to X-list
            curr = this.minX;
            int i = 0;
            while (i < sizeOfList / 2) {
                curr = curr.getNextX();
                i = i + 1;
            }

        } else {
            curr = this.minY;
            int i = 0;
            while (i < sizeOfList / 2) {
                curr = curr.getNextY();
                i = i + 1;
            }

        }
        return curr;
    }

    @Override
    public Point[] nearestPairInStrip(Container container, double width, Boolean axis) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Point[] nearestPair() {
        // TODO Auto-generated method stub
        return null;
    }


    //TODO: add members, methods, etc.


    //TODO: add members, methods, etc.
    public static void main(String[] args) {
        DataStructure dt = new DataStructure();
        Point p1 = new Point(6, 3, "bbb");
        Point p2 = new Point(9, 1, "ccc");
        Point p3 = new Point(7, 4, "aaa");
        Point p4 = new Point(6, 2, "ddd");
        Point p5 = new Point(5, 1, "eee");
        Point p6 = new Point(7, 1, "fff");
        Point p7 = new Point(1, 1, "hhh");
        Point p8 = new Point(1, 0, "hhh");
        Point p9 = new Point(10, 10, "hhh");
        dt.addPoint(p1);
        dt.addPoint(p2);
        dt.addPoint(p3);
        dt.addPoint(p4);
        dt.addPoint(p5);
        dt.addPoint(p6);
        dt.addPoint(p7);
        dt.addPoint(p8);
        dt.addPoint(p9);

        DataStructure dt2 = new DataStructure();
        dt2.addPoint(p1);

        printPointsX(dt);
        printPointsY(dt);
        System.out.println();
  //      System.out.println(dt.getMedian(false).getData());
        System.out.println();
//        printOrigRangeX(dt,2,3);
//        printOppRangeX(dt,5,7);
//        printOrigRangeY(dt, 12, 20);
//        printOrigRangeY(dt, -2, -1);
//        printOppRangeY(dt, 2,4);

        dt.deleteFromX(5,7);
        System.out.println("NEW LIST1 - range by X: from 5 to 7");
        System.out.print("By X:");
        printPointsX(dt);
        System.out.print("By Y:");
        printPointsY(dt);
        System.out.println();

        dt2.deleteFromX(5,7);
        System.out.println("NEW LIST2 - range by X: from 5 to 7");
        System.out.print("By X:");
        printPointsX(dt2);
        System.out.print("By Y:");
        printPointsY(dt2);
        System.out.println();

        DataStructure dt3 = new DataStructure();

        Point r1 = new Point(4, 20, "a");
        Point r2 = new Point(6, 10, "b");
        Point r3 = new Point(5, 4, "c");
        Point r4 = new Point(7, 5, "d");
        Point r5 = new Point(10, 5, "f");
        Point r6 = new Point(5, 3, "g");

        dt3.addPoint(r1);
        dt3.addPoint(r2);
        dt3.addPoint(r3);
        dt3.addPoint(r4);
        dt3.addPoint(r5);
        dt3.addPoint(r6);

        printPointsX(dt3);
        printPointsY(dt3);
        System.out.println("size of list: " +dt3.sizeOfList);


        dt3.deleteFromX(4,7);
        System.out.println("NEW LIST3 - range by X: from 4 to 7");
        System.out.print("By X:");
        printPointsX(dt3);
        System.out.print("By Y:");
        printPointsY(dt3);
        System.out.println("size of  after delete: " +dt3.sizeOfList);



    }

    public static void printPointsX(DataStructure dt) {
        Container curr = dt.minX;
        while (curr != null) {
            System.out.print(" " + curr.getData() + " ");
            curr = curr.getNextX();
        }
        System.out.println();
    }

    public static void printPointsY(DataStructure dt) {
        Container curr = dt.minY;
        while (curr != null) {
            System.out.print(" " + curr.getData() + " ");
            curr = curr.getNextY();
        }
        System.out.println();
    }

    public void printNextX(DataStructure dt) {
        Container curr = dt.minX;
        while (curr != null) {
            System.out.print(" " + curr.getData().getX() + " ");
            curr = curr.getNextX();
        }
        System.out.println();
    }

    public static void printPrevX(DataStructure dt) {
        Container lastCurr = dt.maxX;
        while (lastCurr != null) {
            System.out.print(" " + lastCurr.getData().getX() + " ");
            lastCurr = lastCurr.getPrevX();
        }
        System.out.println();
    }

    public static void printNextY(DataStructure dt) {
        Container currY = dt.minY;
        while (currY != null) {
            System.out.print(" " + currY.getData().getY() + " ");
            currY = currY.getNextY();
        }
        System.out.println();
    }

    public static void printPrevY(DataStructure dt) {
        Container lastCurrY = dt.maxY;
        while (lastCurrY != null) {
            System.out.print(" " + lastCurrY.getData().getY() + " ");
            lastCurrY = lastCurrY.getPrevY();
        }
        System.out.println();
    }

    public static void maxMinStatus(DataStructure dt) {
        System.out.println();
        System.out.println("max X: " + dt.maxX.getData().getX() + " \\ min X: " + dt.minX.getData().getX() + " \\ max Y: " + dt.maxY.getData().getY() + " \\ min Y: " + dt.minY.getData().getY());
    }

    public static void printOrigRangeX(DataStructure dt, int min, int max) {
        System.out.println("Print range x in order of x from " + min + " to " + max);
        Point[] pointRange = dt.getPointsInRangeRegAxis(min, max, true);
        if (pointRange == null)
            System.out.println("wtf");

        else {
            for (int i = 0; i < pointRange.length; i = i + 1) {
                System.out.print(" " + pointRange[i] + " ");
            }
        }
        System.out.println();
        System.out.println();
    }

    public static void printOrigRangeY(DataStructure dt, int min, int max) {
        System.out.println("Print range y in order of y from " + min + " to " + max);
        Point[] pointRangeY = dt.getPointsInRangeRegAxis(min, max, false);
        if (pointRangeY == null)
            System.out.println("wtf");

        else {
            for (int i = 0; i < pointRangeY.length; i = i + 1) {
                System.out.print(" " + pointRangeY[i] + " ");
            }
        }

        System.out.println();
        System.out.println();
    }

    public static void printOppRangeX(DataStructure dt, int min, int max) {
        System.out.println("Print range x in order of Y from " + min + " to " + max);
        Point[] pointRangeOppX = dt.getPointsInRangeOppAxis(min, max, true);
        if (pointRangeOppX == null)
            System.out.println("wtf");

        else {
            for (int i = 0; i < pointRangeOppX.length; i = i + 1) {
                System.out.print(" " + pointRangeOppX[i] + " ");
            }
        }
        System.out.println();
        System.out.println();
    }

    public static void printOppRangeY(DataStructure dt, int min, int max) {
        System.out.println("Print range y in order of X from " + min + " to " + max);
        Point[] pointRangeOppY = dt.getPointsInRangeOppAxis(min, max, false);
        if (pointRangeOppY == null)
            System.out.print("wtf");

        else {
            for (int i = 0; i < pointRangeOppY.length; i = i + 1) {
                System.out.println(" " + pointRangeOppY[i] + " ");
            }
        }
        System.out.println();
        System.out.println();
    }
}

