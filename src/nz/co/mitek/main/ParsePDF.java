package nz.co.mitek.main;


 
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.PRTokeniser;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;
 
public class ParsePDF {

 
    /**
     * Parses the PDF using PRTokeniser
     * @param src  the path to the original PDF file
     * @param dest the path to the resulting text file
     * @throws IOException
     */
    public void convertPDFToText(String src) throws IOException {
        File txtFile = new File(src.replace(".pdf", ".txt"));
        File pdfFile = new File(src);
    	if(!txtFile.exists() && pdfFile.exists()){
    		txtFile.createNewFile();
    		
	    	PdfReader reader = new PdfReader(src);
	        StringBuffer buffer = new StringBuffer();
	        FileOutputStream fos = new FileOutputStream(txtFile);
	        Document document = new Document();
	        for (int i = 0; i < reader.getNumberOfPages(); i++) {
	            byte[] streamBytes = reader.getPageContent(i); 
	            if(streamBytes != null){
		            PRTokeniser tokenizer = new PRTokeniser(new RandomAccessFileOrArray(new RandomAccessSourceFactory().createSource(streamBytes)));
		            
		            while (tokenizer.nextToken()) {
		            	
		                if (tokenizer.getTokenType() == PRTokeniser.TokenType.STRING) {
		                	buffer.append(tokenizer.getStringValue());
		                	
		                }
		            }
		            
	            }
			}
	        
	        String test=buffer.toString();
	        StringReader stReader = new StringReader(test);
	        int t;
	        while((t=stReader.read())>0)
	        fos.write(t);
	        try {
				document.add(new Paragraph(".."));
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        document.close();
	
	
	        reader.close();
    	}
    }
    
    
    
    
 
  
}