import java.io.*;

public class Main {
    public static void main(String[] argv) {

        ABR abr = new ABR();

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader("test.txt"));
            fillABR(reader, abr);
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        int nbChar = abr.getNbChar();
        int nbElt = abr.getNbElt();

        int nbBitsWithoutCompress = (int) Math.ceil(Math.log((double)nbElt)/Math.log(2.0));

        System.out.println("File size without compression : " + (nbChar*nbBitsWithoutCompress) + " bits");
        //System.out.println("ABR : \n");
        //System.out.println(abr.getNbElt());
        //System.out.println(abr.getNbChar());
        //System.out.println(abr.toString_());
    }

    public static void fillABR(BufferedReader f, ABR abr) {

        int c;
        try {
            while ((c = f.read()) != -1) {
                abr.incr((char)c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static ABR huffman(ABR a){
        
        ABR result = new ABR();
        Stack<ABR> stack = new Stack<ABR>(result);

        Elt tmp = a.minOcc();
        result.setLeft(new ABR(tmp));
        a.suppr(tmp);
        
        tmp = a.minOcc();
        result.setRight(new ABR(tmp));
        a.suppr(tmp);

        return result;

    }
}