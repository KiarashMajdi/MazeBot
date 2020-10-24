import java.util.*;
import java.io.*;


public class Main{

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////                                                                //////////////////////
    //////////////////////                           Prologue                             //////////////////////
    //////////////////////                                                                //////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /* Project Description:

    A robot is asked to navigate a maze. 

    It is placed at a certain position (the starting position) in the maze 
    and is asked to try to reach another position (the goal position). 

    Positions in the maze will either be open (.) or blocked with an obstacle(#). 

    The robot can only move to positions without obstacles and must stay within the maze. 

    The robot should search for the shortest path from the starting position to the goal position (a solution path)
    until it finds one or until it exhausts all possibilities. 
    
    In addition, it should mark the path it finds (if any) in the maze. 

    */

    /*Project Objectives:

    •	Accept an input *.txt file that will be the maze (Maze1.txt, Maze2.txt, …, Maze6.txt)
        o	Symbols: 
            	'.' = open, 
            	'#' = blocked, 
            	'S' = start,
            	'G' = goal, 
            	'+' = path, 
    •	Parse the file and fill data into an 2D array
    •	Find 1 solution to the maze (including the no-path result)
    •	You must use RECURSION to solve this problem!
    •	Display the solved maze with the path marked by +

    */

    /*
    The project is written for ICS4U : Unit 2 - Recursion Project.

    School name; Bill Hogarth Secondary School
    Teacher name: Mr. Chu
    Student name: Kiarash Majdi

    This project consists of 1 file:
        Main.java

    */

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////                                                                //////////////////////
    //////////////////////                 Problem Solving and Algorithm                  //////////////////////
    //////////////////////                                                                //////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /* General solution steps in English:
        -1) Read the maze from input file and store it in integer representations.
        0) Initialize all necessary variables.
        1) Consider that you need to move n times in order to get to the Goal from the Starting point.
        2) Set starting point as Step number 0 and consider that the Goal is step number n.
        3) Mark every empty square beside the Starting square (Step #0: S0) as Step #1 (S1). 
            If one of those squares was goal, there is a solution.
            If there were no empty houses and none of them were goal there is no solution.
        4) Mark every empty square beside S1 as S2.
            If one of those squares was goal, there is a solution.
            If there were no empty houses and none of them were goal there is no solution.
        ...
        n+2) Mark every empty square beside Sn+1 as Sn+2
            If one of those squares was goal, there is a solution. GOAL IS HERE SO THERE IS A SOLUIION!
        n+3) Start from Goal (Sn+2), look for Sn+1 around it. Mark it as path.
        n+4) From Sn+1, look for Sn around it. Mark it as path.
        ...
        2n+2) From S1, look for S0 around it. Mark it as path.
        2n+3) Display the board with user friendly notations.
        End.
    */

    /*General solution steps by methods in the program:
        1) readMaze() -> maze[countXY()], start[x, y], goal[x, y]
        2) Type var = new Type()
        3) goStep(step = 0)
        4) goStep(step = 1)
        ...
        n+2) goStep(step = n-1) -> targetFound ::= true
        n+3) declarePath(step = n)
        n+4) declarePath(step = n-1)
        ...
        2n+2) declarePath(step = 1)
        2n+3) printMaze()
    */

    /*Recursives and loops are from the same family and they're always convertible, but sometimes one is really more efficient.
    Therefore, I made for loop equivalents using recursion in the following methods:
        countPlanned
        countAll
        dirArray
        in (Integer[])
        in (Integer)
        hasOnly
        printMaze
        doNextStep
    And I wrote the main idea of mine in a comment under the documentation of each function.


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////                                                                //////////////////////
    //////////////////////                            main()                              //////////////////////
    //////////////////////                                                                //////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /*

    A number of the single variables are declared as arrays of 1 element in order to be passed by reference:
        step
        lastChance
        targetFound
        allEmptyHousesCounter

    */

    /*Variable Description:
        maze: an integer 2d array (int[row][column]):
            For empty houses, holds 0
            For obstacles, holds -1
            For Starting point, holds -2
            For Goal, holds -3
            For Path, holds -4
            For step i (Si), holds i
        
        goneHouses: a 2d Array list of integer arrays (ArrayList<ArrayList<Integer[2]>>):
            For S = 0, goneHouses.get(0).get(0) holds the coordinates (Integer[0] = x, Integer[1] = y) starting point.
            For S = 1, goneHouses[1][i] holds the ith house which is right adjacent to a S = 0 house.
            For S = 2, goneHouses[2][i] holds the ith house which is right adjacent to a S = 1 house.
            ...
        
        
        start: a 2 element long integer array (int[2]):
            Keeps track of the starting point coordinates.

        goal: a 2 element long integer array (int[2]):
            Keeps track of the goal coordinates.

        lastChance: a 1 element long boolean arrat (boolean[1]):
            Begins True
            If we reach Sn-1, there might be impossible to take a new move. 
            At this point, we don't break the recursion if lastChance is true. We just make lastChance false to double check. 
                If Sn-1 is adjacent to the goal, it returns true on the lastChance = false round.
                Id Sn-1 is not adjacent to the goal, it means that we are surrounded bu obstacles, and it returns false.
                    Returning true means that there is a solution.
                    Returning false means that there is not a solution.

        targetFound: a 1 element long boolean array (boolean[1]):
            Begins False
            If there is a solution, it becomes true.
            If there is not a solution, it becomes false.

        allEmptyHousesCounter: a 1 element long integer array (int[1]):
            Holds the number of total empty houses in the beginning of the game.
        
        step: a 1 element long integer array (int[1]):
            Keeps track of the step being worked on. (0 < step < n) (step = 0 ::= start) (step = n ::= goal)
        

    */

    
    

    
    public static void main(String[] args) throws IOException{
        for (int h = 1; h < 7; h++){
            System.out.println();
            System.out.println();

        
            int[][] directions = new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            int[] mazeRowAndColumn = countMazeRowAndColumn(new File("mazes\\Maze" + h + ".txt"));

            int[][] maze = new int[mazeRowAndColumn[0]][mazeRowAndColumn[1]];
            ArrayList<ArrayList<Integer[]>> goneHouses = new ArrayList<ArrayList<Integer[]>>();
            
            Integer[] start = new Integer[2];
            Integer[] goal = new Integer[2];

            boolean[] lastChance = new boolean[]{true};
            boolean[] targetFound = new boolean[]{false};
            

            
            int[] allEmptyHousesCounter = new int[]{0};
            int[] step = new int[1];

            readMaze(new File("mazes\\Maze" + h + ".txt"), maze, start, goal);

            goneHouses.add(new ArrayList<Integer[]>());
            goneHouses.get(0).add(start);

            countAll(maze, allEmptyHousesCounter, 0, 0);
            targetFound[0] = goStep(maze, goneHouses, step, allEmptyHousesCounter, targetFound, directions, lastChance);


            printMaze(maze, 0, 0);

            
            System.out.println("\n*********************\n");

            
            if (targetFound[0]){
                step[0]--;
                Integer[] position = goal;
                declarePath(maze, position, goneHouses, step, targetFound, directions);
            }
            else{
                System.out.println("There is no solution to this maze.");
            }
            printMaze(maze, 0, 0);
            System.out.println();
            System.out.println();
            System.out.println("#####################################");
            
        }
        System.out.println("Press Enter to close!");
        new Scanner(System.in).nextLine();

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////                                                                //////////////////////
    //////////////////////                        Primary Methods                         //////////////////////
    //////////////////////                                                                //////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////





    /* void readMaze()

    Description:
        This procedure reads a .txt file which is already initialized as a File object 
            and saves the numeric equivalents in the maze 2d array of type int[][]. 
        When it reads 'S' from the .txt file, it declares the coordinates and assign it to start of type Integer[].
        When it reads 'G' from the .txt file, it declares the coordinates and assign it to goal of type Integer[].
        
    Local Variables:
        scanner:
            An object of type Scanner used for reading every line of the file.
        line:
            A String variable that takes hold of the line in the file being refered to.
        currentLineCharacters:
            An array of type char[] which breaks down the line variable into characters.
        rowTracker:
            An int counter keeping track of the number of row (line) in the file and maze.
        columnTracker:
            An int counter keeping track of the number of column (character in the line) in the file and maze.

    Parameters:
        file: 
            Type: File
            Description: The text file for input purpose which holds the maze initial positions.
        maze:
            Type: int[][]
            Description: The variable from main() method passed by reference.
        start:
            Type: int[]
            Description: The variable from main() method passed by reference.
        goal:
            Type: int[]
            Description: The variable from main() method passed by reference.

    Exceptions:
        IOException:
            Case: Lack of the possibility of passing the file parameter to the scanner object.



    */


    public static void readMaze(File file, int[][] maze, Integer[] start, Integer[] goal) throws IOException{
        
        Scanner scanner = new Scanner(file);
        char[] currentLineCharacters;
        int rowTracker = 0;
        while (scanner.hasNextLine()){
            String line = scanner.nextLine();
            currentLineCharacters = line.toCharArray();
            for (int columnTracker = 0; columnTracker < currentLineCharacters.length; columnTracker++){

                if (currentLineCharacters[columnTracker] == '.'){
                    maze[rowTracker][columnTracker] = 0;
                    
                }
                else if (currentLineCharacters[columnTracker] == 'S'){
                    maze[rowTracker][columnTracker] = -2;
                    start[0] = rowTracker;
                    start[1] = columnTracker;
                }
                else if (currentLineCharacters[columnTracker] == 'G'){
                    maze[rowTracker][columnTracker] = -3;
                    goal[0] = rowTracker;
                    goal[1] = columnTracker;
                }
                else if (currentLineCharacters[columnTracker] == '#'){
                    maze[rowTracker][columnTracker] = -1;
                }

            }

            rowTracker++;
        }

        
    }



    /*boolean goStep()
    Description:
        This procedure numbers the empty houses which are initially 0, to the earliest number of moves required to reach them.
            In case that the goal is found, it returns true.
            If there are no houses around the houses step = q to mark with step = q+1 (0 at step = q neighbours),
                if lastChance is true, it only changes it to false because there is a chance that we just find the goal house around. 
                if lastChance is false, it returns false.
            and saves the numeric equivalents in the maze 2d array of type int[][]. 
        As the maze variable is passed by reference, it just leaves it with the step numbers,
            so that the declarePath method looks for any house marked with step = n in the neighbourhood of goal
            and carry up with its own recursion which is dependant on the steps which are marked in the maze variable.
        
    Local Variables:
        allPossible: 
            Type: ArrayList<Integer>
            Description: it shows the initial value of all houses in neighbourhood with all houses with step = q.
                If there are no zeros in this variable, it means that we have not changed any of them,
                which means that we change the lastChance to false if it's true, and return false if it's false.
                If there is -3 inside, it means that we found the goal, and we return true.
        
    Parameters:
        
        int[][] maze:
            the maze board which is already declared to 0s, -1s, -2 and -3
            the reference is changed throughout the method.
        
        ArrayList<ArrayList<Integer[]>> goneHouses:
            starting eoth start point, keeps track of every house holding a step number 
            (for step=1, goneHouses.get(1) holds all [x, y] pairs that are step=1)

        int[] step    
            An integer to be passed by reference keeping track of the step.        

        int[] allEmptyHousesCounter:
            An integer to be passed by reference, meant to remain constant after first assignment, 
            showing number of all houses which are marked 0 at the beginning of the whole program. 

        boolean[] targetFound
            A boolean to be passed by reference, saying whether the goal is already found or not.

        int[][] directions
            An array to be remained constant after declaration, facilating recursion in directions.

        boolean[] lastChance
            A boolean to be passed by reference, explained in the method description.

    Exceptions:
        Not Applicable.



    */

    public static boolean goStep(int[][] maze, ArrayList<ArrayList<Integer[]>> goneHouses, int[] step, int[] allEmptyHousesCounter, boolean[] targetFound, int[][] directions, boolean[] lastChance){
        step[0] = goneHouses.size();

        goneHouses.add(new ArrayList<Integer[]>());

        ArrayList<Integer> allPossible = new ArrayList<Integer>();
        doNextStep(maze, goneHouses, step, targetFound, directions, 0, allPossible);
        if (in( allPossible, -3, 0)){
            return true;
        }

        if (countPlanned(maze, 0, 0) == allEmptyHousesCounter[0] || !in(allPossible, 0, 0)){
            if (!lastChance[0]){
                return false;
            }
            lastChance[0] = false;
        }


        
        return goStep(maze, goneHouses, step, allEmptyHousesCounter, targetFound, directions, lastChance);
    }

    /* boolean declarePath()

    Description:
        This procedure starts at the goal house, looks for a neighbour with step = n-1, marks it as -4
            and redoes the same thing for the house it just marked as -4 (looks for a house with step = n-2 while it's considered to be in a house n-1)
            and it keeps going until the step = 1 and when it does that step, the step becomes 0 where we return true to end the recursion. 
        
    Local Variables:
        pathPos: it refers to where we are changing to -4. It's a house, initially with step = q-1.
            An object of type Scanner used for reading every line of the file. 

    Parameters:
        int[][] maze:
            the maze board which is already marked by steps, -1s, -2 and -3.
            the reference is changed throughout the method.

        Integer[] position:
            refers to the position we are checking for its neighbours.

        ArrayList<ArrayList<Integer[]>> goneHouses:
            starting eoth start point, keeps track of every house holding a step number 
            (for step=1, goneHouses.get(1) holds all [x, y] pairs that are step=1)

        int[] step    
            An integer to be passed by reference keeping track of the step.

        boolean[] targetFound
            A boolean to be passed by reference, saying whether the goal is already found or not.
            
        int[][] directions
            An array to be remained constant after declaration, facilating recursion in directions.

    Exceptions:
        Not applicable.



    */
    public static boolean declarePath(int[][] maze, Integer[] position, ArrayList<ArrayList<Integer[]>> goneHouses, int[] step, boolean[] targetFound, int[][] directions){

        
        if (step[0] == 0){
            return true;
        }
        
        ArrayList<Integer> pathPos = dirArray(position, maze, goneHouses, step, targetFound, directions, new ArrayList<Integer>(), 0);

        position = new Integer[]{pathPos.get(0), pathPos.get(1)};

        maze[position[0]][position[1]] = -4;

        step[0] -= 1;

        return declarePath(maze, position, goneHouses, step, targetFound, directions);

    }

    /* void printMaze()

    Description:
        This procedure recursively iterates through the maze, prints out 
            "." for any number 0 or bigger,
            "+" for any -4, 
            "G" for -3, 
            "S" for -2 and 
            "#" for -1.
        It is also equivalent to a for loop which is also shown in comments.

    Local Variables:
        Not applicable.

    Parameters:
        int[][] maze:
            the maze board which is already marked by steps, -1s, -2 and -3 (also -4 in the final printing).

        int h:
            always passed 0 in the other methods, for the sake of recursion.

        int i:
            always passed 0 in the other methods, for the sake of recursion.

    Exceptions:
        Not applicable.

    for-loop equivalent:


        ########################
        for (int h = 0; h < maze.length; h++){
            for (int i = 0; i < maze[h].length; i++){
                if (maze[h][i] == -1){
                    System.out.print("X ");
                }
                else if (maze[h][i] == -2){
                    System.out.print("S ");
                }

                else if (maze[h][i] == -3){
                    System.out.print("G ");
                }
                else if (maze[h][i] == -4){
                    System.out.print("* ");
                }
                else{
                    System.out.print("- ");
                }
                
            }
            System.out.println("");
        }
        #################################
    */

    public static void printMaze(int[][] maze, int h, int i){

        


        if (h == maze.length){
            return;
        }

        if (i == maze[h].length){
            i = 0;
            System.out.println("");
            h++;
            printMaze(maze, h, i);
        }

        else{
            if (maze[h][i] == -1){
                System.out.print("# ");
            }
            else if (maze[h][i] == -2){
                System.out.print("S ");
            }

            else if (maze[h][i] == -3){
                System.out.print("G ");
            }
            else if (maze[h][i] == -4){
                System.out.print("+ ");
            }
            else{
                System.out.print(". ");
            }
            i++;
            printMaze(maze, h, i);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////                                                                //////////////////////
    //////////////////////                       Secondary Methods                        //////////////////////
    //////////////////////                                                                //////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*ArrayList<Integer> dirArray()
    Description:
        This function changes every 0 neighbour of step=q to step=q+1, 
        adds the neighbour coordinates to goneHouses.get(q+1),
        if targetFount is false, returns the value of every neighbour of step=q as 0, -1, -2 or -3 in an ArrayList<Integer>, 
        or if targetFound is true, positions of a house with step=q-1 should be returned.

        This function has a for-loop equivalent which has been written below.

    */

    /*
    for (int[] h: directions){

        if (validDir(h, pos, maze)){

            if (!targetFound[0]){
                values.add(maze[pos[0]+h[0]][pos[1]+h[1]]);
                if (maze[pos[0]+h[0]][pos[1]+h[1]] == 0){
                    maze[pos[0]+h[0]][pos[1]+h[1]] = step[0];
                    goneHouses.get(goneHouses.size() - 1).add(new Integer[]{pos[0] + h[0], pos[1] + h[1]});
                }
            }
            
            if(targetFound[0]){

                if (maze[pos[0]+h[0]][pos[1]+h[1]] == step[0]){

                    
                    values.add(h[0] + pos[0]);
                    values.add(h[1] + pos[1]);

                    return values;
                }

            }
        }
    }
    return values;

    */
    
    /*Parameters:

        int[] pos:
            the position where we start searching
        
        int[][] maze:
            the maze board
            the reference is changed throughout the method.
        
        ArrayList<ArrayList<Integer[]>> goneHouses:
            starting with start point, keeps track of every house holding a step number. New houses are added to a new index of this variable.
            (for step=1, goneHouses.get(1) holds all [x, y] pairs that are step=1)

        int[] step    
            An integer to be passed by reference keeping track of the step.        

        int[] allEmptyHousesCounter:
            An integer to be passed by reference, meant to remain constant after first assignment, 
            showing number of all houses which are marked 0 at the beginning of the whole program. 

        boolean[] targetFound
            A boolean to be passed by reference, saying whether the goal is already found or not.

        int[][] directions
            An array to be remained constant after declaration, facilating recursion in directions.

        ArrayList<Integer> values:
            A static reference to the returned ArrayList
        int g:
            for recursion purpose
    */

    

    public static ArrayList<Integer> dirArray(Integer[] pos, int[][] maze, ArrayList<ArrayList<Integer[]>> goneHouses, int[] step, boolean[] targetFound, int[][] directions, ArrayList<Integer> values, int g){
        
        

        if (g == directions.length){
            return values;
        }
        int[] h = directions[g];
        
        if (validDir(h, pos, maze)){

            if (!targetFound[0]){
                values.add(maze[pos[0]+h[0]][pos[1]+h[1]]);
                if (maze[pos[0]+h[0]][pos[1]+h[1]] == 0){
                    maze[pos[0]+h[0]][pos[1]+h[1]] = step[0];
                    goneHouses.get(goneHouses.size() - 1).add(new Integer[]{pos[0] + h[0], pos[1] + h[1]});
                }
            }
            
            if(targetFound[0]){

                if (maze[pos[0]+h[0]][pos[1]+h[1]] == step[0]){

                    
                    values.add(h[0] + pos[0]);
                    values.add(h[1] + pos[1]);

                    return values;
                }

            }
        }

        g++;
        return dirArray(pos, maze, goneHouses, step, targetFound, directions, values, g);
    }

    /*boolean validDir()
        returns true if we can do in dir[] way when we are in pos[] square
        param: dir, pos, maze
        returns: boolean
    */
    public static boolean validDir(int[] dir, Integer[] pos, int[][] maze){
        

        if (!(0 <= pos[0] + dir[0] && pos[0] + dir[0] < maze.length) || !(0 <= pos[1] + dir[1] && pos[1] + dir[1] < maze[0].length)){
            return false;
        }
        return true;
    }

    /*
        for each house with step=q, does dirArray().
        parameters: int[][]maze, ArrayList<ArrayList<Integer[]>> goneHouses, int[]step, boolean[]targetFound, int[][]directions, int h, ArrayList<Integer> allPossible
            h is for recursion purpose
        returns nothing.
    */
    public static void doNextStep(int[][]maze, ArrayList<ArrayList<Integer[]>> goneHouses, int[]step, boolean[]targetFound, int[][]directions, int h, ArrayList<Integer> allPossible){
        /*
        for (int h = 0; h < goneHouses.get(step[0] - 1).size(); h++){
            ArrayList<Integer> dir = dirArray(goneHouses.get(step[0] - 1).get(h), maze, goneHouses, step, targetFound, directions);

            allPossible.addAll(dir);
            
        }
        */
        if (h == goneHouses.get(step[0] - 1).size()){
            return;
        }
        ArrayList<Integer> dir = dirArray(goneHouses.get(step[0] - 1).get(h), maze, goneHouses, step, targetFound, directions, new ArrayList<Integer>(), 0);

        allPossible.addAll(dir);
        h++;
        doNextStep(maze, goneHouses, step, targetFound, directions, h, allPossible);
        
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////                                                                //////////////////////
    //////////////////////                        Utility Methods                         //////////////////////
    //////////////////////                                                                //////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /*int[] countMazeRowAndColumn()
        counts for x and y dimensions of the maze.
        parameters: file (maze file)
        returns: Integer[xDimension, yDimension]
    */
    public static int[] countMazeRowAndColumn(File file) throws IOException{
        Scanner scanner = new Scanner(file);
        char[] a;
        int x = 0;
        int y = 0;
        
        while (scanner.hasNextLine()){
            x++;
            String line = scanner.nextLine();
            a = line.toCharArray();
            y = a.length;
        }
        return new int[]{x, y};
    }

    
    /*void countAll()
        counts every house that equals zero (is empty) in maze and assigns it to counter.
        params: maze, counter, h, i
            h and i are for recursion purposes
        returns nothing.
    
    */
    public static void countAll(int[][] maze, int[] counter, int h, int i){
        /*
        for (int h = 0; h < maze.length; h++){
            for (int i = 0; i < maze[h].length; i++){
                if (maze[h][i] == 0){
                    counter[0]++;
                }
            }
        }
        */
        if (h == maze.length){
            return;
        }

        if (i == maze[h].length){
            i = 0;
            h++;
            countAll(maze, counter, h, i);
        }

        else{
            if (maze[h][i] == 0){
                counter[0]++;
            }
            i++;
            countAll(maze, counter, h, i);
        }
    }

    /*int countPlanned()
        counts every house that is bigger than zero (is already stepped) in maze and returns it.
        params: maze, h, i
            h and i are for recursion purposes
        returns cntr.
    
    */

    public static int countPlanned(int[][] maze, int h, int i){
        int cntr = 0;

        /*
        for (int h = 0; h < maze.length; h++){
            for (int i = 0; i < maze[h].length; i++){
                if (maze[h][i] > 0){
                    cntr++;
                }
            }
        }
        */
        
        if (h == maze.length){
            return cntr;
        }

        if (i == maze[h].length){
            i = 0;
            h++;
            return countPlanned(maze, h, i);
        }

        else{
            if (maze[h][i] > 0){
                cntr++;
            }
            i++;
            return countPlanned(maze, h, i);
        }
    }

    /*boolean in
        checks if an integer element is in an arrayList
        params: a, element, h
            h is for recursion purposes.
        returns: true or false
    */
    public static boolean in(ArrayList<Integer> a, Integer element, int h){
        /*
        for (int h = 0; h < a.size(); h++){
            if (a.get(h) == element){
                return true;
            }
        }
        return false;
        */
        if (h == a.size()){
            return false;
        }

        if (a.get(h) == element){
            return true;
        }


        h++;
        return in(a, element, h);
    }
}