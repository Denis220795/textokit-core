import com.textocat.morph.predictor.model.utils.comparators.NumberBasedComparator;
import com.textocat.morph.predictor.model.utils.Transformation;

import java.util.ArrayList;

/**
 * Created by Денис on 02.04.2016.
 */
public class ComparatorTest {
    public static void main(String[] args) {
        ArrayList<Transformation> transformations = new ArrayList<>();
        Transformation t1 = new Transformation("а",",");
        t1.setNum(50);
        Transformation t2 = new Transformation("а","у");
        t2.setNum(20);
        transformations.add(t1);
        transformations.add(t2);
        System.out.println(transformations);
        NumberBasedComparator comparator = new NumberBasedComparator();
        transformations.sort(comparator);
        System.out.println(transformations);
    }
}
