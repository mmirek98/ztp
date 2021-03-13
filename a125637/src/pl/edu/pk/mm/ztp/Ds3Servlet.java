package pl.edu.pk.mm.ztp;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.fileupload.ByteArrayOutputStream;

/**
 * Servlet implementation class Ds3Servlet
 */
@WebServlet("/Ds3Servlet")
public class Ds3Servlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public Ds3Servlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO:
		//	- query params w GET ?image=
		//  - odes³aæ obrazek na GET req, sciezka do pliku w query params
		//  - odeslac jako obiekt binarny
		//	- decode url (œcie¿ka do resource)

		String pathToImage = request.getParameter("image");
		if (pathToImage != null) {
			String convertedPath = this.convertPath(pathToImage);
			String mime = this.extractMimeType(request.getServletContext(), convertedPath);
			File file = new File(convertedPath);

			this.setContentType(mime, response);
			this.setContentLength(response, (int) file.length());
			this.writeToOutputStream(file, response.getOutputStream());

		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}
	
	private String convertPath(String path) {
		Path convertedPath = Paths.get(path);
		return convertedPath.toString();
	}
	
	private void setContentType(String mime, HttpServletResponse response) {
		if (mime == null) {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		
		response.setContentType(mime);
	}
	
	private void setContentLength(HttpServletResponse response, int length) {
		response.setContentLength(length);
	}
	
	private String extractMimeType(ServletContext contex, String convertedPath ) {
		return contex.getMimeType(convertedPath);
	}
	
	private void writeToOutputStream(File file, OutputStream output) throws IOException {
		FileInputStream input = null;
		try {
			input = new FileInputStream(file);

			byte[] buffer = new byte[1024];
			int count = 0;
			while ((count = input.read(buffer)) >= 0) {
				output.write(buffer, 0, count);
			}
		
		} catch (Exception e) {
			output.close();
			input.close();
		}
	}
}
