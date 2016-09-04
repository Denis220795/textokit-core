package ml.weka;

import org.w3c.dom.Attr;
import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

import java.lang.reflect.Field;

/**
 * Created by Денис on 30.08.2016.
 */
public class NEPredictor {

    Classifier classifier;

    public void setClassifier(Classifier classifier) {
        this.classifier = classifier;
    }

    public String predictBILOULabel(CharacteristicVector vector) {
        String characterizedString = vector.toString();


        return "";

        /*
        *  Attribute e = new Attribute("length");
            Attribute weight = new Attribute("weight");
            FastVector my_nominal_values = new FastVector(3);
            my_nominal_values.addElement("first");
            my_nominal_values.addElement("second");
            my_nominal_values.addElement("third");
            Attribute position = new Attribute("position", my_nominal_values);
            FastVector attributes = new FastVector(3);
            attributes.addElement(e);
            attributes.addElement(weight);
            attributes.addElement(position);
            Instances race = new Instances("race", attributes, 0);
            race.setClassIndex(position.index());
            Instance inst = new Instance(3);
            inst.setValue(e, 5.3D);
            inst.setValue(weight, 300.0D);
            inst.setValue(position, "first");
            inst.setDataset(race);
            System.out.println("The instance: " + inst);
            System.out.println("First attribute: " + inst.attribute(0));
            System.out.println("Class attribute: " + inst.classAttribute());
            System.out.println("Class index: " + inst.classIndex());
            System.out.println("Class is missing: " + inst.classIsMissing());
            System.out.println("Class value (internal format): " + inst.classValue());
            Instance copy = (Instance)inst.copy();
            System.out.println("Shallow copy: " + copy);
            copy.setDataset(inst.dataset());
            System.out.println("Shallow copy with dataset set: " + copy);
            copy.setDataset((Instances)null);
            copy.deleteAttributeAt(0);
            copy.insertAttributeAt(0);
            copy.setDataset(inst.dataset());
            System.out.println("Copy with first attribute deleted and inserted: " + copy);
            System.out.println("Enumerating attributes (leaving out class):");
            Enumeration enu = inst.enumerateAttributes();

            while(enu.hasMoreElements()) {
                Attribute meansAndModes = (Attribute)enu.nextElement();
                System.out.println(meansAndModes);
            }
        * */
    }
}
