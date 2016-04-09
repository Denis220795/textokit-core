import com.textocat.morph.predictor.model.model.LemmaPredictionModel;
import com.textocat.morph.predictor.model.utils.ioutils.IOModelUtil;

import java.io.IOException;

/**
 * Created by Денис on 08.04.2016.
 */
public class IOTest {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        LemmaPredictionModel lemmaPredictionModel = new LemmaPredictionModel();
        lemmaPredictionModel.id=255;
        System.out.println(lemmaPredictionModel.id);
        IOModelUtil.writeModel(lemmaPredictionModel);
        System.out.println(IOModelUtil.readModel().id);
    }
}