public class Stack<A> {

    private class Elt {
        A elt;
        Elt son;

        Elt(A e, Elt s) {
            elt = e;
            son = s;
        }
    }

    private Elt head;

    Stack(A e) {
        head = new Elt(e, null);
    }

    Stack() {
        head = new Elt(null, null);
    }

    void push(A e) {
        if (head != null && head.elt != null) {

            head = new Elt(e, head);

        } else {

            head = new Elt(e, null);

        }
    }

    void pushTail(A e) {
        if (head == null) {

            head = new Elt(e, null);

        } else if (head.elt == null) {

            head = new Elt(e, null);

        } else {

            Elt tmp = head;

            while (tmp.son != null) {
                tmp = tmp.son;
            }

            tmp.son = new Elt(e, null);

        }
    }

    A pop() {
        if (!this.isEmpty()) {
            A result = head.elt;
            head = head.son;
            return result;
        } else {
            return null;
        }
    }

    boolean isEmpty() {
        if (this.head == null) {
            return true;
        } else if (this.head.elt == null) {
            return true;
        } else {
            return false;
        }
    }

    boolean hasOnlyOneElt() {
        if (this.head == null) {
            return false;
        } else if (this.head.elt != null && this.head.son == null) {
            return true;
        } else {
            return false;
        }
    }

    void concat(Stack<A> s1, Stack<A> s2) {

        Stack<A> result = new Stack<A>();

        while (!(s1.isEmpty())) {
            result.pushTail(s1.pop());
        }

        while (!(s2.isEmpty())) {
            result.pushTail(s2.pop());
        }

        this.head = result.head;
    }

    A headInfo() {
        if (this.isEmpty()) {
            System.out.println("empty stack");
            return null;
        }

        return this.head.elt;
    }

    int size() {
        int result = 0;

        if (!(this.isEmpty())) {
            Elt tmp = head;

            result++;

            while (tmp.son != null) {
                tmp = tmp.son;
                result++;
            }
        }

        return result;
    }

}