package gameClient;

public class Student implements Comparable<Student>{
    private int ID, score, moves;

    public Student(int ID, int score, int moves) {
        this.ID=ID;
        this.score=score;
        this.moves=moves;
    }

    public int getID() {
        return ID;
    }


    public void setID(int iD) {
        ID = iD;
    }


    public int getScore() {
        return score;
    }


    public void setScore(int score) {
        this.score = score;
    }


    @Override
    public int compareTo(Student o) {
        if(this.getScore()<o.getScore())
            return 1;
        else if(this.getScore()>o.getScore())
            return -1;
        return 0;
    }

    public String toString() {
        return "ID: "+this.getID()+", score: "+this.getScore()+", moves: "+this.getMoves();
    }


    public int getMoves() {
        return this.moves;
    }

}
