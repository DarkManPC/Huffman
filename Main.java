import java.io.*;

public class Main {
    public static void main(String[] argv) {

        ABR abr = new ABR();

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("test.1.txt"));
            fillABR(reader, abr);
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int nbChar = abr.getNbChar();
        int nbElt = abr.getNbElt();

        int nbBitsWithoutCompress = (int) Math.ceil(Math.log((double) nbElt) / Math.log(2.0));

        //nbBitsWithoutCompress = 8;

        System.out.println("File size without compression : " + (nbChar * nbBitsWithoutCompress) + " bits");

        //System.out.println("nb char : " + nbChar);

        // System.out.println(huffman(abr).getRoot().toString_());

        int nbBitsAfterCompression = nbBitsCompress(huffman(abr));

        System.out.println("File size without compression : " + nbBitsAfterCompression + " bits");

    }

    public static void fillABR(BufferedReader f, ABR abr) {

        int c;
        try {
            while ((c = f.read()) != -1) {
                abr.incr((char) c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ABR huffman(ABR a) {

        int maxChar = a.getNbElt();

        ABR result = new ABR();
        Stack<ABR> stack = new Stack<ABR>();

        // System.out.println(a.toString_());

        Elt tmp = a.minOcc();
        result.setLeft(new ABR(tmp));
        a.suppr(tmp);

        tmp = a.minOcc();
        result.setRight(new ABR(tmp));
        a.suppr(tmp);

        // System.out.println(a.toString_());

        result.setRoot(new Elt(result.getRight().getRoot().getOcc() + result.getLeft().getRoot().getOcc()));
        result.updateState();

        // System.out.println(result.toString_());

        stack.push(result);

        while (!(a.isEmpty() && stack.hasOnlyOneElt())) {

            //System.out.println((int) ((float) ((float) (maxChar - a.getNbElt()) / maxChar) * 100) + "%");

            // System.out.println(a.getNbElt());
            // System.out.println(stack.size() + "\n");

            result = new ABR();

            if (a.isEmpty()) {

                result.setLeft(stack.pop());

                result.setRight(stack.pop());

                result.setRoot(new Elt(result.getRight().getRoot().getOcc() + result.getLeft().getRoot().getOcc()));
                result.updateState();

                stack.push(result);

                stack = sortStack(stack);

            } else if (stack.isEmpty()) {

                tmp = a.minOcc();
                a.suppr(tmp);

                if (a.isEmpty()) {
                    stack.push(new ABR(tmp));
                    stack = sortStack(stack);
                    continue;
                }

                result.setLeft(new ABR(tmp));

                tmp = a.minOcc();
                result.setRight(new ABR(tmp));
                a.suppr(tmp);

                result.setRoot(new Elt(result.getRight().getRoot().getOcc() + result.getLeft().getRoot().getOcc()));
                result.updateState();

                stack.push(result);

                stack = sortStack(stack);

            } else {

                ABR e1 = stack.pop();
                ABR e2 = null;

                // System.out.println(e1.toString_());

                if (a.minOcc().getOcc() > e1.getRoot().getOcc()) {

                    result.setLeft(e1);

                } else {

                    // System.out.println(a.toString_());

                    tmp = a.minOcc();
                    result.setLeft(new ABR(tmp));
                    a.suppr(tmp);

                    stack.push(e1);

                    stack = sortStack(stack);
                }

                if (!stack.isEmpty()) {
                    e2 = stack.pop();
                }

                if (e2 == null) {

                    tmp = a.minOcc();
                    result.setRight(new ABR(tmp));
                    a.suppr(tmp);

                    // System.out.println(tmp.toString_());

                } else if (a.isEmpty()) {

                    result.setRight(e2);

                } else if (a.minOcc().getOcc() > e2.getRoot().getOcc()) {

                    result.setRight(e2);

                } else {
                    tmp = a.minOcc();
                    result.setRight(new ABR(tmp));
                    a.suppr(tmp);

                    stack.push(e2);

                    stack = sortStack(stack);
                }

                // System.out.println(result.getRight().getState());

                result.setRoot(new Elt(result.getRight().getRoot().getOcc() + result.getLeft().getRoot().getOcc()));
                result.updateState();

                // System.out.println(result.toString_());

                stack.push(result);

                stack = sortStack(stack);
            }
        }

        //System.out.println(stack.headInfo().toString_());

        return stack.pop();

    }

    public static Stack<ABR> sortStack(Stack<ABR> s) {

        Stack<ABR> result = new Stack<ABR>();

        while (!s.isEmpty()) {

            ABR a = s.pop();

            Stack<ABR> tmp = new Stack<ABR>();

            if (result.isEmpty()) {

                // System.out.println("result empty");

                result.push(a);

            } else {

                // System.out.println("result not empty");

                while (!result.isEmpty() && result.headInfo().getRoot().getOcc() < a.getRoot().getOcc()) {

                    tmp.pushTail(result.pop());
                }

                result.push(a);

                result.concat(tmp, result);
            }

        }

        return result;
    }

    public static int nbBitsCompress(ABR a) {

        int result = 0;

        ABR param = a;

        while (!a.isEmpty()) {

            int nbBits = 0;
            ABR tmp = param;

            while (tmp != null && !tmp.isEmpty()) {

                //System.out.println(tmp.toString_());

                switch (tmp.getState()) {
                case EMPTY:
                    break;
                case LEAF:
                    if(tmp.getRoot().getC() != 0){
                        result += (nbBits * tmp.getRoot().getOcc());
                    }
                    param.supprNoBin(tmp.getRoot());
                    tmp = null;
                    break;
                case SIMPLELEFT:
                    tmp = tmp.getLeft();
                    nbBits++;
                    break;
                case SIMPLERIGHT:
                    tmp = tmp.getRight();
                    nbBits++;
                    break;
                case DOUBLE:
                    tmp = tmp.getLeft();
                    nbBits++;
                    break;
                default:
                    System.out.println("Erreur nbBitsCompress");

                }
            }

        }

        return result;
    }
}