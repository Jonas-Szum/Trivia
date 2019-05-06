package Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.*;

public class QnA {

    ////////////////////////////////        DATA VARIABLES         ////////////////////////////////////////

    //this is the data structure for the questions and answers
    private HashMap<String, ArrayList<String>> trivia_set = new HashMap<>();

    //this stores the picked question and its answers
    private String question;
    private ArrayList<String> answers;

    //this holds all of the questions
    private ArrayList<String> question_set = new ArrayList<String>();

    //this holds all of the answers
    private ArrayList<ArrayList<String>> answer_set = new ArrayList<ArrayList<String>>();

    ////////////////////////////////        ANSWER SETS         ////////////////////////////////////////

    //name answer set
    private ArrayList<String> answer_set_names = new ArrayList<String>(
            Arrays.asList("Jonathan","Jonas","Alex","Yunzhong")
    );

    //uic course answer set
    private ArrayList<String> answer_set_this_class = new ArrayList<String>(
            Arrays.asList("342","401","251","341")
    );

    //professors answer set
    private ArrayList<String> answer_set_professors = new ArrayList<String>(
            Arrays.asList("Hallenbeck","Hummel","Lillis","Me")
    );

    //privacy answer set
    private ArrayList<String> answer_set_privacy = new ArrayList<String>(
            Arrays.asList("Private","Final","Static","Void")
    );


    ////////////////////////////////        FUNCTIONS         ////////////////////////////////////////

    //default constructor
    public QnA() {
        //push questions into the quesiton set in the respective order to match answers
        create_question_set();

        //push answers into the answer set in the respective order
        create_answer_set();

        //push question and answers into the hashmap
        insert_into_map();
    }

    private void create_question_set() {
        question_set.add("Who is the oldest of the group?");
        question_set.add("What is the number of this CS course?");
        question_set.add("Who is the professor of this CS course?");
        question_set.add("What do you put in front of a method so it can't be accessed outside?");
    }


    private void create_answer_set() {
        answer_set.add(answer_set_names);
        answer_set.add(answer_set_this_class);
        answer_set.add(answer_set_professors);
        answer_set.add(answer_set_privacy);

    }

    //push the questions and answers into the hashmap iteratively
    private void insert_into_map() {
        for (int i=0; i < question_set.size(); i++) {
            trivia_set.put(question_set.get(i), answer_set.get(i));
        }
    }

    //pick the question randomly
    void roll_question() {
        //first, get the size of the whole array list
        int question_set_size = question_set.size();

        //second, randomly choose one of the index values
        Random random = new Random();

        //finally, set the question to the randomly generated index
        question = question_set.get(random.nextInt(question_set_size));
        answers = trivia_set.get(question);
    }

    String get_question() {
    	return question;
    }

    ArrayList<String> get_answers() {
        return answers;
    }

}
