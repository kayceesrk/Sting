//$ bin/sessionjc -cp ../../lib/servlet-api.jar';'tests/classes/';'tests/src/sjservlet/servlet/WEB-INF/classes/ tests/src/sjservlet/servlet/SJServlet.sj -d tests/src/sjservlet/servlet/WEB-INF/classes/

package sjservlet.servlet;

import java.io.*;
import java.net.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

import sessionj.runtime.*;
import sessionj.runtime.net.*;
import sessionj.runtime.util.*;

import sjservlet.server.Server;

import sessionj.runtime.net.SJRuntime;

public class SJServlet extends HttpServlet
{
	public static final String SUCCESS = "SUCCESS";
	public static final String FAIL = "FAIL";

	public void init(ServletConfig config) throws ServletException
	{
		super.init(config);
	}

	private void run(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, SJIOException
	{
		final noalias protocol p_server_dual { ^(Server.p_server) }

		Map params = request.getParameterMap();

		String[] ha = (String[]) params.get("HOST");
		String[] pa = (String[]) params.get("PORT");
		
		if (ha == null || pa == null)
		{
			throw new SJIOException("[SJServlet] Invalid parameters: " + ha + ", " + pa);
		}

		String host = ha[0];
		int port = Integer.parseInt(pa[0]);

		final noalias SJService c = SJService.create(p_server_dual, host, port);

		PrintWriter pw = new PrintWriter(response.getOutputStream()); // Also supposed to set the Content-Length header to permit persistent connections (although this is done automatically for small enough responses).

		pw.println("<html>");
		pw.println("<body>");

		noalias SJSocket s;

		try (s)
		{	
			s = c.request();					
			
			s.send("Hello from SJServlet.");

			pw.println(SUCCESS);
		}
		/*catch (SJIncompatibleSessionException ioe)
		{
			pw.println(FAIL);
		}
		catch (SJIOException ioe)
		{
			pw.println(FAIL);
		}*/
		//catch (Exception x)
		catch (Throwable x)
		{
			x.printStackTrace(pw);
			
			pw.println(FAIL);
		}		
		finally
		{
			pw.println("</body>");
			pw.println("</html>");
			pw.close();
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			run(request, response);
		}
		catch (SJIOException ioe)
		{
			throw new IOException(ioe);
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		try
		{
			run(request, response);
		}
		catch (SJIOException ioe)
		{
			throw new IOException(ioe);
		}		
	}
}
