import java.util.Scanner;

public class state{
    static int state_index = 0;
    state[] next_states;
    int output[];
    int id;

    public state(int size , char type){
        this.next_states = new state[size];

        if(type == 'm')
            this.output = new int[size];
        else
            this.output = new int[1];

        this.id = state_index++;
}

//----------------------------------------------------------------------------------------------------------   
    public void set_next_states(state[] states , Scanner sc){

        int no_of_next_states = states[0].next_states.length;
        int no_of_states = states.length;
        String[] input_splited;
        boolean invalid=false;

        do{
        System.out.println("[States start from S0 to "+"S"+ (no_of_states-1) +"]");
        System.out.print("{Enter any integer between 0 & "+(no_of_states-1));
        System.out.println(" Or character between (a or A) to "+ "( " + Character.toString('A' + (no_of_states-1))+" or "+Character.toString('a' + (no_of_states-1)) +" ) }\n");
        System.out.println("enter your S" + this.id + " Next states"+"( "+ no_of_next_states +" States seperated by ',' ) in order :");
        String input = sc.nextLine();
        input = input.replaceAll("\\[\\]\\{\\}\\(\\)", "");
        input = input.replaceAll(" ", "");
        input_splited = input.split(",");
        invalid = false;

        if(input_splited.length!= no_of_next_states){
            System.out.print("\033[H\033[2J");
            System.out.println("invalid size of next states or data");
            invalid = true;
            continue;
        }

        for(int i =0 ; i<no_of_next_states ; i++){
            int temp;
            String state = input_splited[i];

            if(Character.toUpperCase(state.charAt(0))<='Z' && Character.toUpperCase(state.charAt(0))>='A'){
                temp = (Character.toUpperCase(state.charAt(0)) - 'A');

                if(temp>=no_of_states || state.length()!=1){
                    System.out.print("\033[H\033[2J");
                    System.out.println("Entered state isn't found or invalid."); 
                    invalid = true;
                    break;
                }

                input_splited[i] = Integer.toString(temp);
            }
            else if (state.charAt(0)>='0' && state.charAt(0)<='9'){

                try {
                    temp = Integer.parseInt(state);
                } catch (NumberFormatException e) {
                    System.out.print("\033[H\033[2J");
                    System.out.println("invalid state data");
                    invalid = true;
                    break;
                }
                
                if(temp>=no_of_states){
                    System.out.print("\033[H\033[2J");
                    System.out.println("Entered state isn't found"); 
                    invalid = true;
                    break;
                }

            }
            else{
                System.out.print("\033[H\033[2J");
                System.out.println("invalid state data");
                invalid = true;
                break;
            }
        }
        

        }while(invalid);
        System.out.print("\033[H\033[2J");
        
        
        for(int i = 0 ; i<no_of_next_states ; i++)
           this.next_states[i] = states[Integer.parseInt(input_splited[i])];

        
    }
    
    //----------------------------------------------------------------------------------------------------------

    public void set_output(Scanner sc){
        int output_size = this.output.length;
        String[] input_splited;
        boolean invalid=false;
        
        do{
            System.out.println("[you can only enter 0(s) and 1(s)]\n ");
            System.out.println("enter your S" + this.id + " output(s)"+" (" + output_size +" Output(s) {seperated by ','}) in order :");
            String input = sc.nextLine();
            input = input.replaceAll("\\[\\]\\{\\}\\(\\)", "");
            input = input.replaceAll(" ", "");
            input_splited = input.split(",");
            invalid = false;
    
            if(input_splited.length!= output_size){
                System.out.print("\033[H\033[2J");
                System.out.println("invalid size of output or data entered.");
                invalid = true;
                continue;
            }
            for(int i = 0 ; i < output_size ; i++){
                String output = input_splited[i];
                if(output.length()!=1 || output.charAt(0)!='1' && output.charAt(0)!='0'){
                    System.out.print("\033[H\033[2J");
                    System.out.println("invalid : you must enter each output as 1 or 0");
                    invalid = true;
                    break;
                }
            }

        }while(invalid);
        System.out.print("\033[H\033[2J");

        for(int i = 0 ; i<output_size ; i++)
            this.output[i] = Integer.parseInt(input_splited[i]);
    }

    //----------------------------------------------------------------------------------------------------------

    boolean SameNextStates(state comparing_state){
        int next_states_no = this.next_states.length;
        for(int i = 0 ; i<next_states_no ; i++){            
            if(this.next_states[i].id != comparing_state.next_states[i].id)
                return false;
        }
        return true;
    }

    //----------------------------------------------------------------------------------------------------------

    boolean SameOutput(state comparing_state){
        int output_size = this.output.length;
        for(int i = 0 ; i<output_size ; i++){            
            if(this.output[i]!= comparing_state.output[i])
                return false;
        }
        return true;
    }

}