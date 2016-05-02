package com.textocat.lemma.predictor.utils.csv;

/**
 * Created by Денис on 30.04.2016.
 */

import com.textocat.lemma.predictor.utils.csv.raw.IRecord;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.QuoteMode;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CSVWriter {

    CSVPrinter printer;
    CSVFormat csvFormat;

    public void writeToCSV(String[] header, Collection<IRecord> records, File file, Double accuracy) throws IOException {
        csvFormat = CSVFormat.EXCEL.withHeader(header).withDelimiter(',').withEscape('"').withQuoteMode(QuoteMode.NONE);
        try {
            printer = new CSVPrinter(new FileWriter(file), csvFormat);
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> raws = new ArrayList<>();
        for (IRecord record : records) {
            String[] values = record.getValues();
            if (values.length != header.length) {
                System.err.print("Number of attributes in record is not equal to header attrbutes values!");
                System.exit(1);
                return;
            }
            Collections.addAll(raws, values);
            printer.printRecord(raws);
            printer.flush();
            raws.clear();
        }
        printer.println();
        if (accuracy != null) {
            accuracy = accuracy * 100;
            printer.print("Accuracy: " + accuracy.toString() + " %");
        }
        printer.close();
    }
}