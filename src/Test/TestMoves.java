package Test;

import utils.MovesReader;

/**
 * Created with IntelliJ IDEA.
 * User: emilio
 * Date: 16/12/12
 * Time: 13:21
 * To change this template use File | Settings | File Templates.
 */
public class TestMoves {
    public static void main (String[] args )
    {
        MovesReader reader = new MovesReader();
        reader.load("/Users/emilio/Desktop/GraphStreamTest/files/export_lunes.txt");
        reader.getG().display(false);
    }
}
