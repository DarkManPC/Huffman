public class ABR{

    enum State{
        DOUBLE,
        SIMPLELEFT,
        SIMPLERIGHT,
        LEAF,
        EMPTY;
    }

    private Elt root;

    private ABR left;
    private ABR right;

    private State state;

    public ABR(){

        this.root = null;
        this.left = null;
        this.right = null;
        this.state = State.EMPTY;

    }

    public ABR(char c){

        this.root = new Elt(c);
        this.left = new ABR();
        this.right = new ABR();
        this.state = State.LEAF;

    }

    public ABR(Elt e){

        this.root = e;
        this.left = new ABR();
        this.right = new ABR();
        this.state = State.LEAF;

    }

    public Elt getRoot(){
        return this.root;
    }

    public void setRoot(Elt e){
        this.root = e;
    }

    public ABR getLeft(){
        return this.left;
    }

    public void setLeft(ABR abr){
        this.left = abr;
    }

    public ABR getRight(){
        return this.right;
    }

    public void setRight(ABR abr){
        this.right = abr;
    }

    public State getState(){
        return this.state;
    }

    public void setState(State s){
        this.state = s;
    }

    public void incr(char c){

        Elt tmp = this.find(c);
        
        if(tmp != null){
            tmp.incr();
        }else{
            this.add(c);
        }
    }

    public Elt find(char c){

        Elt tmp = null;

        switch(this.state){

            case EMPTY:
                break;
            case LEAF:
                if(this.getRoot().getC() == c){
                    tmp = this.getRoot();
                }
                break;
            case SIMPLELEFT:
            case SIMPLERIGHT:
            case DOUBLE:
                if(this.getRoot().getC() == c){
                    tmp = this.getRoot();
                }else if(this.getRoot().getC() > c){
                    tmp = this.getLeft().find(c);
                }else{
                    tmp = this.getRight().find(c);
                }
                break;

        }

        return tmp;
    }

    public void add(char c){

        Elt tmp = new Elt(c);

        switch(this.state){

            case EMPTY:
                this.setRoot(tmp);
                this.setLeft(new ABR());
                this.setRight(new ABR());
                break;
            case LEAF:
            case SIMPLELEFT:
            case SIMPLERIGHT:
            case DOUBLE:
                if(this.getRoot().getC() < tmp.getC()){
                    this.getRight().add(c);
                }else{
                    this.getLeft().add(c);
                }
                break;

        }

        this.updateState();

    }

    public void suppr(Elt e){

        switch(this.state){

            case EMPTY:
                break;
            case LEAF:
                if(e.getC() == this.getRoot().getC()){
                    this.setRoot(null);
                    this.setState(State.EMPTY);
                }
                break;
            case SIMPLELEFT:
                if(e.getC() == this.getRoot().getC()){
                    this.setRoot(this.getLeft().getRoot());
                    this.setLeft(this.getLeft().getLeft());
                    this.setRight(this.getLeft().getRight());
                    this.setState(this.getLeft().getState());
                }else{
                    this.getLeft().suppr(e);
                }
                break;
            case SIMPLERIGHT:
                if(e.getC() == this.getRoot().getC()){
                    this.setRoot(this.getRight().getRoot());
                    this.setLeft(this.getRight().getLeft());
                    this.setRight(this.getRight().getRight());
                    this.setState(this.getRight().getState());
                }else{
                    this.getRight().suppr(e);
                }
                break;
            case DOUBLE:
                if(e.getC() == this.getRoot().getC()){
                    this.setRoot(this.getLeft().supprMax());
                }else if(e.getC() < this.getRoot().getC()){
                    this.getLeft().suppr(e);
                }else{
                    this.getRight().suppr(e);
                }
                break;

        }
    }

    public Elt supprMax(){
        ABR tmp = this;
        ABR parent = this;

        while(tmp.getRight() != null){
            parent = tmp;
            tmp = tmp.getRight();
        }

        Elt result = tmp.getRoot();

        switch(this.getState()){
            case EMPTY:
                break;
            case LEAF:
                parent.setRight(null);
                break;
            case SIMPLELEFT:
            case SIMPLERIGHT:
            case DOUBLE:
                tmp.setRoot(tmp.getLeft().getRoot());
                tmp.setLeft(tmp.getLeft().getLeft());
                tmp.setRight(tmp.getLeft().getRight());
                tmp.setState(tmp.getLeft().getState());
                break;
        }
        
        return result;
    } 

    public void balance(){
        // TODO re-balance ABR, differnce between max length branch and min length branch <= 1
    }

    private void updateState(){

        if(this.getRoot() == null){
            this.setState(State.EMPTY);
        }else if(this.getLeft() == null && this.getRight() == null){
            this.setState(State.LEAF);
        }else if(this.getLeft() == null && this.getRight() != null){
            this.setState(State.SIMPLERIGHT);
        }else if(this.getLeft() != null && this.getRight() == null){
            this.setState(State.SIMPLELEFT);
        }else{
            this.setState(State.DOUBLE);
        }
    }

    public int getNbChar(){

        int result = 0;

        switch(this.state){

            case EMPTY:
                break;
            case LEAF:
            case SIMPLELEFT:
            case SIMPLERIGHT:
            case DOUBLE:
                result = this.getLeft().getNbChar() + this.getRoot().getOcc() + this.getRight().getNbChar();
                break;

        }

        return result;

    }

    public int getNbElt(){

        int result = 0;

        switch(this.state){

            case EMPTY:
                break;
            case LEAF:
            case SIMPLELEFT:
            case SIMPLERIGHT:
            case DOUBLE:
                result = this.getLeft().getNbElt() + 1 + this.getRight().getNbElt();
                break;

        }

        return result;

    }

    public Elt minOcc(){

        Elt tmp = null;

        switch(this.getState()){
            case EMPTY:
                break;
            case LEAF:
                tmp = this.getRoot();
                break;
            case SIMPLELEFT:
                tmp = minEltOcc2(this.getRoot(), this.getLeft().minOcc());
                break;
            case SIMPLERIGHT:
                tmp = minEltOcc2(this.getRoot(), this.getRight().minOcc());
                break;
            case DOUBLE:
                tmp = minEltOcc3(this.getLeft().minOcc(), this.getRoot(), this.getRight().minOcc());
                break;
        }

        return tmp;
    }

    private Elt minEltOcc3(Elt e1, Elt e2, Elt e3){

        return minEltOcc2(e1, minEltOcc2(e2, e3));
    }

    private Elt minEltOcc2(Elt e1, Elt e2){

        Elt tmp;

        if (e1.getOcc() <= e2.getOcc()){
            tmp = e1;
        } else {
            tmp = e2;
        } 
        return tmp;
    }

    String toString_(){

        String result = "";

        switch(this.state){
            case EMPTY:
                result = "";
                break;
            case LEAF:
                result = this.getRoot().toString_();
                break;
            case SIMPLELEFT:
            case SIMPLERIGHT:
            case DOUBLE:
                result = this.getLeft().toString_() + this.getRoot().toString_() + this.getRight().toString_();
                break;
        }

        return result;
    }


}