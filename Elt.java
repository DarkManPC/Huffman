public class Elt{

    private char c;
    private int occ;

    Elt(char c_){
        this.c = c_;
        this.occ = 1;
    }

    void incr(){
        this.occ = this.occ + 1;
    }

    char getC(){
        return this.c;
    }

    int getOcc(){
        return this.occ;
    }

    String toString_(){
        return this.getC() + " / " + this.getOcc() + "\n";
    }
}