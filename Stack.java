public class Stack<A>{

    private class Elt{
        A elt;
        Elt son;
        Elt(A e, Elt s){
            elt = e;
            son = s;
        }
    }

    private Elt head;

    Stack(A e){
        head = new Elt(e, null);
    }

    void push(A e){
        head = new Elt(e, head);
    }

    A pop(){
        A result = head.elt;
        head = head.son;
        return result;
    }

}