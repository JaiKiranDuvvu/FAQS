package KBS;

import Util.Lessthan;
import Util.Equals;
import Util.GreaterthanEquals;
import Util.Greaterthan;
import Util.Operator;
import Util.LessthanEquals;
import Util.NotEquals;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.eobjects.metamodel.DataContext;
import org.eobjects.metamodel.DataContextFactory;
import org.eobjects.metamodel.data.DataSet;
import org.eobjects.metamodel.query.Query;
import org.eobjects.metamodel.schema.Column;
import org.eobjects.metamodel.schema.Schema;
import org.eobjects.metamodel.schema.Table;


import weka.classifiers.rules.PART;
import weka.classifiers.trees.J48;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

public class FQASystem {

    public void createduplicatedataset(String MainColumn, String[] keyword,
                                       String value, String opr,
                                       String inputquery) throws Exception {

        File datafile = new File(Path.path + "dataset.csv");

        DataContext dataContext =
            DataContextFactory.createCsvDataContext(datafile);
        Schema sch = dataContext.getDefaultSchema();
        Table[] tablearray = null;
        tablearray = sch.getTables();

        assert tablearray.length == 1;

        Table table = tablearray[0];

        ArrayList<Column> columnsArray = new ArrayList<Column>();
        StringBuilder columnCSV = new StringBuilder();
        int keyWordLength = keyword.length;
        for (int keyWordIter = 0; keyWordIter < keyWordLength; keyWordIter++) {
            columnsArray.add(table.getColumnByName(keyword[keyWordIter]));
        }

        // Building the csv file with column names and generating the subsets of the original dataset.
        for (int j = 0; j < columnsArray.size(); j++) {
            if (j == columnsArray.size() - 1) {
                columnCSV.append(columnsArray.get(j).getName());
            } else {
                columnCSV.append(columnsArray.get(j).getName());
                columnCSV.append(",");
            }
        }

        Query query =
            dataContext.query().from(table).select(keyword).toQuery();
        // Creating datasets using projections
        DataSet dsOutput = dataContext.executeQuery(query);

        StringBuffer stringBufferObj = new StringBuffer();
        final String sep = System.getProperty("line.separator");
        stringBufferObj.append(columnCSV);
        stringBufferObj.append(sep);

        // Iterate through the dataset obtained by executing the query
        while (dsOutput.next()) {
            double x1 =
                Double.parseDouble((String)dsOutput.getRow().getValue(table.getColumnByName(MainColumn)));
            double x2 = Double.parseDouble(value);
            Operator myopr = operator.get(opr);

            int columnsArraySize = columnsArray.size();
            for (int j = 0; j < columnsArraySize; j++) {
                if (j == (columnsArraySize - 1)) {
                    if (MainColumn.equalsIgnoreCase(columnsArray.get(j).getName().trim())) {
                        if (myopr.compute(x1, x2)) {
                            stringBufferObj.append("YES");
                        } else {
                            stringBufferObj.append("NO");
                        }
                    } else {
                        stringBufferObj.append(dsOutput.getRow().getValue(columnsArray.get(j)));
                    }

                } else {
                    if (MainColumn.equalsIgnoreCase(columnsArray.get(j).getName().trim())) {
                        if (myopr.compute(x1, x2)) {
                            stringBufferObj.append("YES");
                            stringBufferObj.append(",");
                        } else {
                            stringBufferObj.append("NO");
                            stringBufferObj.append(",");
                        }
                    } else {
                        stringBufferObj.append(dsOutput.getRow().getValue(columnsArray.get(j)));
                        stringBufferObj.append(",");
                    }

                }

            }
            stringBufferObj.append(sep); // appending to the string
        }
        dsOutput.close(); // closing the dataset

        try {
            BufferedWriter bw =
                new BufferedWriter(new FileWriter(Path.path +
                                                  MainColumn + ".csv"));
            bw.write(stringBufferObj.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Loading csv file and instantiating

        CSVLoader loadCSV = new CSVLoader();
        loadCSV.setSource(new File(Path.path + MainColumn + ".csv"));
        Instances ins = loadCSV.getDataSet();

        // saving the file in arff form
        ArffSaver ars = new ArffSaver();
        ars.setInstances(ins);
        ars.setFile(new File(Path.path + MainColumn + ".arff"));
        ars.writeBatch();

    }

    public void rulesFromWeka(String[] keyword, String[] value,
                              String[] operator,
                              String inputQuery) throws Exception {
        Vector top = null;
        Vector rules = new Vector();
        String temp = null;
        String[] tempArray;
        for (int i = 0; i < keyword.length; i++) {
            // reading the arff file
            BufferedReader br =
                new BufferedReader(new FileReader(Path.path +
                                                  keyword[i] + ".arff"));
            Instances data = new Instances(br);
            br.close();
            J48 algo = new J48();
            data.setClassIndex(i);
            algo.buildClassifier(data); // building decision tree with j48 object
            PART p = new PART();
            p.buildClassifier(data); // using part to access each rule

            Vector rulevector = p.m_root.getRules();
            top = new Vector();
            Enumeration en = rulevector.elements();
            while (en.hasMoreElements()) {
                temp = en.nextElement().toString();
                tempArray = temp.split(":");
                if (!((tempArray[0].contains("YES")) ||
                      (tempArray[0].contains("NO"))))
                    if (!tempArray[0].equalsIgnoreCase("")) {
                        String Statement =
                            tempArray[0] + " ^ " + keyword[i] + " " +
                            operator[i] + " " + value[i];
                        top.add(Statement);
                    }

            }
            for (int j = 0; j < top.size(); j++) {
                rules.add(top.get(j));
            }

        }

        NearestNeighbour nearObj = new NearestNeighbour();
        int distanceVal = nearObj.findDistance(rules, inputQuery);

        Refiner refineQueryObj = new Refiner();
        refineQueryObj.relaxQuery(inputQuery,
                                  rules.get(distanceVal).toString().replace("AND",
                                                                    " ^ "));
        FQAS.rulesoutput = rules.toString().substring(1,rules.toString().length()-1);
    }

    static Map<String, Operator> operator = new HashMap<String, Operator>();
    static {
        operator.put("=", new Equals());
        operator.put(">", new Greaterthan());
        operator.put(">=", new GreaterthanEquals());
        operator.put("<", new Lessthan());
        operator.put("<=", new LessthanEquals());
        operator.put("!=", new NotEquals());
    }
}
