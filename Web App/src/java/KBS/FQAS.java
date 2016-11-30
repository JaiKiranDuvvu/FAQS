package KBS;

import java.io.IOException;

import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class FQAS extends HttpServlet {
    private static final long serialVersionUID = 1L;
    public static String result = "";
    public static String relaxed_query = "";
    public static String rulesoutput = "";
    public static String inputquery = "";

    /**
     * Default constructor.
     */
    public FQAS() {
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException,
                                                              IOException {
        // TODO Auto-generated method stub
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response) throws ServletException,
                                                               IOException {

        String temp = request.getParameter("query");
        String inputquery = temp.replace(">=", ">").replace("<=", "<");
        String auxStringArray[] = { "<", ">=", ">", "<=", "<>", "=" }, s1;

        String[] parts = inputquery.split("\\^");
        String keyword[] = new String[parts.length];
        String operator[] = new String[parts.length];
        String value[] = new String[parts.length];
		
        //splitting the input query into three parts 
		// 1) Attribute name
		// 2) The operatot and 
		// 3) The values specified for each attribute in the query
		int lengthOfPartsArray = parts.length;
		int lengthOfAuxStringArray = auxStringArray.length;
        for (int i = 0; i < lengthOfPartsArray; i++) {
            for (int j = 0; j < lengthOfAuxStringArray; j++) {
                if (parts[i].contains(auxStringArray[j])) {
                    String[] tempArray = parts[i].split(auxStringArray[j]);
                    keyword[i] = tempArray[0].trim();
                    operator[i] = auxStringArray[j].trim();
                    value[i] = tempArray[1].trim();
                }
            }
        }

		// Get the instance of the FQASystem
        FQASystem qas = new FQASystem();
        for (int iter = 0; iter < keyword.length; iter++) {
            try {
                qas.createduplicatedataset(keyword[iter], keyword, value[iter],
                                           operator[iter], inputquery);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        try {
            qas.rulesFromWeka(keyword, value, operator, inputquery);
        } catch (Exception e) {
            e.printStackTrace();
        }
        response.setHeader("result",result);
        response.setHeader("relaxed_query", relaxed_query);
        response.setHeader("rulesoutput", rulesoutput);
        PrintWriter out = response.getWriter();
        out.print("");

    }
}
