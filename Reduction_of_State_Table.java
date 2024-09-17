import java.util.*;
import java.awt.Point;


interface Reduction_of_State_Table_Operations {
    /*** Prints the state table.*/
    void print_table(state[] states);

    /*** making assumptions of when two states are equal.*/
    Map<Point , ArrayList<Point>> assignment(state[]states);
    
    /*** checking the assumbtions if true then reducing.*/
    state[] Reduction (Map<Point , ArrayList<Point>> Pairing_assumbtions , state[] states);
        
}

public class Reduction_of_State_Table implements Reduction_of_State_Table_Operations {


    public void print_table(state[] states){
        int table_size = states.length;
        int next_states_no = states[0].next_states.length;
        int output_size = states[0].output.length;

        System.out.print("P.S |  ");

        System.out.print(" ".repeat(next_states_no*2-2) +"N.S"+" ".repeat(next_states_no*2-1)+ "|  ");

        System.out.println(" ".repeat(output_size*2-1) + "OP");

        for(int i = 0 ; i <table_size ; i++){
            System.out.print("S" + states[i].id +" ".repeat(2-states[i].id/10) +"|  ");

            for(int j = 0 ; j<next_states_no ; j++)
                System.out.print("S"+ states[i].next_states[j].id + " ".repeat(2-states[i].next_states[j].id/10));

            System.out.print("|  ");

            for(int k =0 ; k<output_size ; k++)
                    System.out.print(states[i].output[k] + "  ");

        System.out.println();
    }
}

    // ----------------------------------------------------------------------------------------------------------
    
    public Map<Point , ArrayList<Point>> assignment(state[]states){
        int no_of_states=states.length;
        int no_of_next_states=states[0].next_states.length;
        
        //Hashmap to take all the assumed equal states  and their conditions
        Map<Point , ArrayList<Point>> Pairing_assumbtions = new HashMap<>();

        for (int i = 0 ; i <no_of_states-1 ; i++){
            state currenState = states[i];

             //skipping as this state is a redundant (i.e equal to a previous state)
            if(currenState.id < i) 
                continue;

            for(int j = i+1 ; j<no_of_states ; j++){
                state comparing_State = states[j];

                //skipping this state is redundant or doesn't have the same output as the compared state
                if(comparing_State.id<j || !currenState.SameOutput(comparing_State)) 
                    continue;

                if(currenState.SameNextStates(comparing_State) && currenState.SameOutput(comparing_State)){
                    states[j] = states[i];
                    continue;
                }

                //to take the conditions of equality
                ArrayList<Point> state_pairs = new ArrayList<>();

                for(int k = 0 ; k< no_of_next_states ; k++){
                    state NextState_1 = currenState.next_states[k];
                    state NextState_2 = comparing_State.next_states[k];

                    if(NextState_1.id == NextState_2.id)
                        continue;
                    
                    boolean equal = NextState_1.SameOutput(NextState_2);

                    if(equal){
                        int first_point = Math.min(NextState_1.id, NextState_2.id);
                        int second_point = Math.max(NextState_1.id, NextState_2.id);
                        Point equal_pair = new Point(first_point, second_point);
                        state_pairs.add(equal_pair);
                    }else{
                        state_pairs.clear(); // clears all conditions for a pair of states
                        break;
                    }

                        
                }
                if(!state_pairs.isEmpty()){
                    System.out.println("{S"+currenState.id+" = S" + comparing_State.id+ "} if: ");
                    System.out.println("=".repeat(13));

                    for(Point point : state_pairs){
                        int first_point = point.x;
                        int second_point = point.y;
                        System.out.print( "(S"+first_point +"= S"+second_point+") " );
                    }
                    System.out.println();
                    System.out.println();

                    // putting the pair of states with their condtitions in hashmap
                    Point temp = new Point(currenState.id, comparing_State.id);
                    Pairing_assumbtions.put(temp , state_pairs); 
                }

            }

        }
        System.out.println();

        return Pairing_assumbtions;
    }



    // ----------------------------------------------------------------------------------------------------------

    public state[] Reduction(Map<Point , ArrayList<Point>> Pairing_assumbtions , state[] states){
        int no_of_states=states.length;
        int no_of_next_states = states[0].next_states.length;
        
        for(int i = 0 ; i<no_of_states-1 ; i++){
            state currentState = states[i];

            //skips if current state is redundunt
            if(currentState.id <i)
                continue;

            for(int j = i+1 ; j<no_of_states ; j++){
                state comparing_State = states[j];
                Point equality_assumbtion = new Point(currentState.id , comparing_State.id);

                //skips if latter state is redundunt , or both of the states are initially equal or unequal
                if(comparing_State.id== currentState.id || Pairing_assumbtions.get(equality_assumbtion)==null || comparing_State.id <j)            
                    continue;
                
                // Arraylist of type Point contains all the conditions for a certain pair to be equal
                // e.g : a=b if c=d , b=d. the arraylist contains (c,d) & (b,d) and their key in the hashmap is (a,b)
                ArrayList <Point> conditions =  Pairing_assumbtions.get(equality_assumbtion);
                
                //checking to see if the pairs in the conditions of equality of a pair of states is present in hashmap
                for(Point condition : conditions){
                    if(!Pairing_assumbtions.containsKey(condition) && states[condition.x].id != states[condition.y].id){
                        Pairing_assumbtions.remove(equality_assumbtion);
                        break;
                    }

                }

                if(Pairing_assumbtions.containsKey(equality_assumbtion))     
                    states[j] = states[i];          
        }
    }

    //reducing the table after pairing each state with its equal.
    int index =0;

    Map<Integer, Integer> diff_states = new HashMap<>();

    //checking the unique states indexes
    for(state state : states){
        int state_no = state.id ;
        if(!diff_states.containsKey(state_no)){
            diff_states.put(state_no , index);
            index++;
        }
    }

    //changing the next states indexes to match the new reduced states
    for(int i = 0 ; i<no_of_states ;i++){
        for(int j = 0 ; j<no_of_next_states ; j++){
            state nextstate = states[i].next_states[j];
            states[i].next_states[j] = states[nextstate.id];
    }
}

    state[] reduced_states = new state[diff_states.size()];

    //going through the old table and putting its unique states in the new reduced table with ascending indexes
    for(int key : diff_states.keySet()){
        int new_index = diff_states.get(key);
        reduced_states[new_index] = states[key];
        reduced_states[new_index].id = new_index;
    }

        

    return reduced_states;
    }
    // ----------------------------------------------------------------------------------------------------------
    
    public static void main(String[] args) {
        Reduction_of_State_Table operation = new Reduction_of_State_Table();
        Scanner sc = new Scanner(System.in);
        // boolean to detect if the user misinputed
        boolean invalid_input = false;

        // no. of inputs in the machine
        int input_number = 0;

        // machine type according to the dependency of the output on the input
        // (Mealy : m) : Dependent  || (MOORE : M) : Independent
        char machine_type = 'x';

        // no. of states in the system
        int state_count = 0;

        //do-while loop that checks that the no. of inputs entered is valid
        do{
            try {
                System.out.print("Enter the number of inputs : ");
                input_number = sc.nextInt();
                sc.nextLine();
                System.out.println();
                invalid_input = false;
            } catch (InputMismatchException e) {
                System.out.print("\033[H\033[2J");
                System.out.println("Data entered is invalid");
                invalid_input = true;
                sc.nextLine();
            }
            if(input_number <=0){
                System.out.print("\033[H\033[2J");
                System.out.println("{the no of inputs must be an integer greater than 0}");
                invalid_input = true;
            }       
        }while(invalid_input);
        System.out.print("\033[H\033[2J");

        input_number = (int)Math.pow(2 , input_number);


        //do-while loop that checks that the no. of states entered is valid
        do{
        try {
            System.out.print("Enter the number of states : ");
            state_count = sc.nextInt();
            sc.nextLine();
            System.out.println();
            invalid_input = false;
        } catch (InputMismatchException e) {
            System.out.print("\033[H\033[2J");
            System.out.println("Data entered is invalid");
            invalid_input = true;
            sc.nextLine();
        }
        if(state_count <=0){
            System.out.print("\033[H\033[2J");
            System.out.println("{the no of states must be an integer greater than 0}");
            invalid_input = true;
        }

    }while(invalid_input);
    System.out.print("\033[H\033[2J");

    //do-while loop that checks that the type of the machine entered is valid
    do{
        try {
            System.out.print("Enter the type of the machine [M : (MOORE) || m : (Mealy)] : ");
            machine_type = sc.next().charAt(0);
            sc.nextLine();
            System.out.println();
            invalid_input = false;
        } catch (InputMismatchException e) {
            System.out.print("\033[H\033[2J");
            System.out.println("Data entered is invalid");
            invalid_input = true;
            continue;
        }
        if( machine_type!='M' && machine_type!='m' ){
            System.out.print("\033[H\033[2J");
            System.out.println("please select Either Mealy or a Moore type machine");
            invalid_input = true; 
        }         
    }while(invalid_input);
    System.out.print("\033[H\033[2J");
    
  

    //initializing the array containing the states
    state[] states = new state[state_count];

    //initializing & declaring each state's next state(s) & output(s)
    for(int i = 0 ; i<state_count ; i++)
        states[i] = new state(input_number, machine_type);
    
    for(int i = 0 ; i<state_count ; i++){
        states[i].set_next_states(states , sc);
        states[i].set_output(sc);
    }

    System.out.println("The Table :");
    System.out.println();
    operation.print_table(states);
    System.out.println();

    Map<Point , ArrayList<Point>> Pairing_assumbtions = operation.assignment(states);

    // reduced table array :
    state[] reduced_states = operation.Reduction(Pairing_assumbtions, states);

    System.out.println("Reduced Table : ");
    System.out.println();
    operation.print_table(reduced_states);
    System.out.println();

    sc.close();
    
    }
}